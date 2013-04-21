package com.chare.mcb.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardTransaction;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.SettlementType;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.repository.SettingRepository;

public class BookingGenerator {
	private final SettingRepository settingRepository;
	private final CardRepository cardRepository;

	public BookingGenerator(SettingRepository settingRepository, CardRepository cardRepository) {
		this.settingRepository = settingRepository;
		this.cardRepository = cardRepository;
	}


	List<Booking> generateFor(StatementLine statementLine, PostingFile postingFile) {
		List<Booking> generated = new ArrayList<Booking>();
		String mirrorAccount = findMirrorAccount();
		String transactAccount = findTransactAccount();
		if (statementLine.transferType.settlementType == SettlementType.BANK_SETTLEMENT && (isNotZero(statementLine.amount))) {
			boolean isDebet = statementLine.amount.signum() < 0;

			Booking booking = postingFile.addBooking();
			booking.debitAccount = isDebet ? statementLine.transferType.settlementAccount : mirrorAccount;
			booking.creditAccount = transactAccount;
			booking.amount = statementLine.amount.abs();
			booking.debitValueDate = statementLine.valueDate;
			booking.creditValueDate = statementLine.valueDate;
			booking.details = statementLine.generateBookingDetailsForBankSettlement();

			generated.add(booking);

			booking = postingFile.addBooking();
			booking.debitAccount = transactAccount;
			booking.creditAccount = isDebet ? mirrorAccount :  statementLine.transferType.settlementAccount;
			booking.amount = statementLine.amount.abs();
			booking.debitValueDate = statementLine.valueDate;
			booking.creditValueDate = statementLine.valueDate;
			booking.details = statementLine.generateBookingDetailsForBankSettlement();

			generated.add(booking);
		}
		if (statementLine.transferType.settlementType == SettlementType.CLIENT_SETTLEMENT && isNotZero(statementLine.amount)) {

			String cardSettlementAccount = findCardSettlementAccount(statementLine);
			String cardTransactionDetails = statementLine.getCardTransaction().getDetails();


			boolean isDebet = statementLine.amount.signum() < 0;

			Booking booking = postingFile.addBooking();
			booking.debitAccount = isDebet ? cardSettlementAccount : mirrorAccount;
			booking.creditAccount = transactAccount;
			booking.amount = statementLine.amount.abs();
			booking.debitValueDate = statementLine.valueDate;
			booking.creditValueDate = statementLine.valueDate;
			booking.details = cardTransactionDetails;

			generated.add(booking);

			booking = postingFile.addBooking();
			booking.debitAccount = transactAccount;
			booking.creditAccount = isDebet ? mirrorAccount :  cardSettlementAccount;
			booking.amount = statementLine.amount.abs();
			booking.debitValueDate = statementLine.valueDate;
			booking.creditValueDate = statementLine.valueDate;
			booking.details = cardTransactionDetails;

			generated.add(booking);
		}

		if (statementLine.transferType.settlementType == SettlementType.CLIENT_SETTLEMENT_WITH_FEE) {

			CardTransaction cardTransaction = statementLine.getCardTransaction();
			if (cardTransaction == null)
				throw new IllegalArgumentExceptionWithParameters("Booking of type client settlement with fee can not be generated, card transaction is missing", new Object[] {statementLine.number});

			String cardSettlementAccount = findCardSettlementAccount(statementLine);
			String cardTransactionDetails = cardTransaction.getDetails();

			String costAccount = findCostAccount();

			String profitAccount = findProfitAccount(cardTransaction);

			boolean isDebet = statementLine.amount.signum() < 0;

			if (isNotZero(statementLine.amount)) {
				Booking booking = postingFile.addBooking();
				booking.debitAccount = isDebet ? costAccount : mirrorAccount;
				booking.creditAccount = transactAccount;
				booking.amount = statementLine.amount.abs();
				booking.debitValueDate = statementLine.valueDate;
				booking.creditValueDate = statementLine.valueDate;
				booking.details = cardTransactionDetails;

				generated.add(booking);

				booking = postingFile.addBooking();
				booking.debitAccount = transactAccount;
				booking.creditAccount = isDebet ? mirrorAccount :  costAccount;
				booking.amount = statementLine.amount.abs();
				booking.debitValueDate = statementLine.valueDate;
				booking.creditValueDate = statementLine.valueDate;
				booking.details = cardTransactionDetails;

				generated.add(booking);
			}

			BigDecimal feeAmount = statementLine.amount.abs().add(cardTransaction.feeAmount.abs());

			if (isNotZero(feeAmount)) {
				Booking booking = postingFile.addBooking();
				booking.debitAccount = transactAccount;
				booking.creditAccount = isDebet ? profitAccount :  cardSettlementAccount;
				booking.amount = feeAmount;
				booking.debitValueDate = statementLine.valueDate;
				booking.creditValueDate = statementLine.valueDate;
				booking.details = cardTransactionDetails;

				generated.add(booking);

				booking = postingFile.addBooking();
				booking.debitAccount = isDebet ? cardSettlementAccount : profitAccount;
				booking.creditAccount = transactAccount;
				booking.amount = feeAmount;
				booking.debitValueDate = statementLine.valueDate;
				booking.creditValueDate = statementLine.valueDate;
				booking.details = cardTransactionDetails;

				generated.add(booking);
			}
		}

		return generated;
	}


	private boolean isNotZero(BigDecimal amount) {
		return amount.abs().compareTo(BigDecimal.ZERO) > 0;
	}


	private String findMirrorAccount() {
		return assertAccountFromSettings(SettingRepository.MIRROR_ACC, "Mirror account 14 or 15 chars long not defined in application settings ");
	}
	private String findTransactAccount() {
		return assertAccountFromSettings(SettingRepository.TRANSACT_ACC, "Transact account 14 or 15 chars long not defined in application settings ");
	}
	private String findCostAccount() {
		return assertAccountFromSettings(SettingRepository.COST_ACC, "Cost account 14 or 15 chars long not defined in application settings ");
	}


	private String assertAccountFromSettings(String settingName,
			String exceptionMessage) {
		String account = settingRepository.getValue(settingName, null);
		if (StringUtils.isBlank(account) || (account.length() < 14 && account.length() > 15)) {
			throw new IllegalArgumentException(exceptionMessage);
		}
		return account;
	}


	private String findCardSettlementAccount(StatementLine statementLine) {
		String cardNumber = statementLine.getCardTransaction().cardNumber;
		Card card = cardRepository.findByNumber(cardNumber);
		//		if (card == null) {
		//			throw new IllegalArgumentExceptionWithParameters("Card with number not found", new Object[] {cardNumber});
		//		}
		if (card == null) {
			throw new IllegalArgumentException("Card with number " + cardNumber + " was not found");//FIXME localize
		}
		if (card.isInactive())
			throw new IllegalArgumentException("Card with number " + cardNumber + " is not active");//FIXME localize
		return card.settlementAccount;
	}

	private String findProfitAccount(CardTransaction cardTransaction) {
		FeeType feeType = cardTransaction.feeType;
		if (feeType == null) {
			throw new IllegalArgumentExceptionWithParameters("Card transaction missing fee type", new Object[] {cardTransaction.cardNumber});
		}
		return feeType.settlementAccount;
	}


	//FIXME vraceni penez klientovi pro transakci s poplatkem ? co kdyz se mezitim poplatek zmeni
}
