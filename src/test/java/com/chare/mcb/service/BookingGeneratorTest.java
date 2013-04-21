package com.chare.mcb.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardTransaction;
import com.chare.mcb.entity.CardType;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.SettlementType;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.entity.TransferType;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.repository.SettingRepository;

public class BookingGeneratorTest {

	private BookingGenerator generator;
	@Mock
	private SettingRepository settingRepository;
	@Mock
	private CardRepository cardRepository;

	private static final String transactAccount = "197931361000EUR";
	private static final String mirrorAccount = "197720002500EUR";
	private static final String costAccount = "19746106106000";

	private static final String cardTransactionCcy = "USD";
	private static final String cardNumber = "1234567812345678";
	private static final Date cardTransactionDate = Utils.getYesterday();
	private static final String cardDetails1 = "d1";
	private static final String cardDetails2 = "d2";

	private static final String cardSettlementAccount = "1971700292EUR";

	private PostingFile postingFile;
	private Statement statement;
	private StatementLine line;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		generator = new BookingGenerator(settingRepository, cardRepository);

		postingFile = new PostingFile();
		statement = new Statement();
		line = statement.addLine();
		line.valueDate = Utils.getYesterday();

	}

	private void setupCardAndRepository() {
		Card card = createCardWithSettlementAccount(cardSettlementAccount);
		when(cardRepository.findByNumber(cardNumber)).thenReturn(card);
	}

	private void setupAccountsFromSettings() {
		when(settingRepository.getValue(SettingRepository.MIRROR_ACC, null)).thenReturn(mirrorAccount);
		when(settingRepository.getValue(SettingRepository.TRANSACT_ACC, null)).thenReturn(transactAccount);
		when(settingRepository.getValue(SettingRepository.COST_ACC, null)).thenReturn(costAccount);
	}

	private Card createCardWithSettlementAccount(String settlementAccount) {
		Card card = new Card(1, cardNumber, new CardType("BUSINESS"));
		card.settlementAccount = settlementAccount;
		return card;
	}

	@Test
	public void testGenerateBookingsBankSettledCredit() throws Exception {
		setupAccountsFromSettings();
		line.transferType = createTransferTypeWithSettlementAccount(SettlementType.BANK_SETTLEMENT, "197123456700EUR");
		line.amount = new BigDecimal("123.45");

		List<Booking> bookings = generator.generateFor(line, postingFile);
		assertEquals(2, bookings.size());

		Booking booking = bookings.get(0);
		assertEquals(mirrorAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(transactAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(line.generateBookingDetailsForBankSettlement(), booking.details);

		booking = bookings.get(1);
		assertEquals(transactAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(line.transferType.settlementAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(line.generateBookingDetailsForBankSettlement(), booking.details);

	}


	@Test
	public void testBookingIsNotGeneratedForBankSettledWithZeroAmount() throws Exception {
		setupAccountsFromSettings();
		line.transferType = createTransferTypeWithSettlementAccount(SettlementType.BANK_SETTLEMENT, "197123456700EUR");
		line.amount = new BigDecimal("0.00");

		List<Booking> bookings = generator.generateFor(line, postingFile);
		assertEquals(0, bookings.size());
	}

	private TransferType createTransferTypeWithSettlementAccount(
			SettlementType settlementType, String settlementAccount) {
		TransferType type = createTransferType(settlementType);
		type.settlementAccount = settlementAccount;
		return type;
	}

	@Test
	public void testGenerateBookingsBankSettledDebit() throws Exception {
		setupAccountsFromSettings();
		line.transferType = createTransferTypeWithSettlementAccount(SettlementType.BANK_SETTLEMENT, "197123456700EUR");
		line.amount = new BigDecimal("-123.45");

		List<Booking> bookings = generator.generateFor(line, postingFile);
		assertEquals(2, bookings.size());

		Booking booking = bookings.get(0);
		assertEquals(line.transferType.settlementAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(transactAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(line.generateBookingDetailsForBankSettlement(), booking.details);

		booking = bookings.get(1);
		assertEquals(transactAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(mirrorAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(line.generateBookingDetailsForBankSettlement(), booking.details);
	}


	@Test
	public void testGenerateBookingsClientSettledCredit() throws Exception {
		setupAccountsFromSettings();
		setupCardAndRepository();
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT);
		line.amount = new BigDecimal("123.45");
		line.setCardTransaction(new BigDecimal("123.45"), cardTransactionCcy, cardNumber, cardTransactionDate, cardDetails1, cardDetails2);
		CardTransaction cardTransaction = line.getCardTransaction();

		List<Booking> bookings = generator.generateFor(line, postingFile);
		assertEquals(2, bookings.size());

		Booking booking = bookings.get(0);
		assertEquals(mirrorAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(transactAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);

		booking = bookings.get(1);
		assertEquals(transactAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(cardSettlementAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);

	}

	@Test
	public void testBookingIsNotGeneratedForClientSettledWithZeroAmount() throws Exception {
		setupAccountsFromSettings();
		setupCardAndRepository();
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT);
		line.amount = new BigDecimal(0);

		List<Booking> bookings = generator.generateFor(line, postingFile);
		assertEquals(0, bookings.size());
	}

	@Test
	public void testGenerateBookingsClientSettledDebit() throws Exception {
		setupAccountsFromSettings();
		setupCardAndRepository();
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT);
		line.amount = new BigDecimal("-123.45");
		line.setCardTransaction(new BigDecimal("-123.45"), cardTransactionCcy, cardNumber, cardTransactionDate, cardDetails1, cardDetails2);
		CardTransaction cardTransaction = line.getCardTransaction();

		List<Booking> bookings = generator.generateFor(line, postingFile);
		assertEquals(2, bookings.size());

		Booking booking = bookings.get(0);
		assertEquals(cardSettlementAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(transactAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);

		booking = bookings.get(1);
		assertEquals(transactAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(mirrorAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);
	}


	@Test
	public void testGenerateBookingsClientWithFeeSettledCredit() throws Exception {
		setupAccountsFromSettings();
		setupCardAndRepository();
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT_WITH_FEE);
		line.amount = new BigDecimal("123.45");
		line.setCardTransaction(new BigDecimal("123.45"), cardTransactionCcy, cardNumber, cardTransactionDate, cardDetails1, cardDetails2);
		CardTransaction cardTransaction = line.getCardTransaction();
		cardTransaction.feeAmount = new BigDecimal("12.45");
		cardTransaction.feeCurrency = Booking.CURRENCY;
		cardTransaction.feeType = createFeeTypeWithSettlementAccount("197555552200EUR");

		List<Booking> bookings = generator.generateFor(line, postingFile);

		assertEquals(4, bookings.size());

		Booking booking = bookings.get(0);
		assertEquals(mirrorAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(transactAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);

		booking = bookings.get(1);
		assertEquals(transactAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(costAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);

		booking = bookings.get(2);
		assertEquals(transactAccount, booking.debitAccount);
		assertEquals(line.amount.abs().add(cardTransaction.feeAmount.abs()), booking.amount);
		assertEquals(cardSettlementAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);

		booking = bookings.get(3);
		assertEquals(cardTransaction.feeType.settlementAccount, booking.debitAccount);
		assertEquals(line.amount.abs().add(cardTransaction.feeAmount.abs()), booking.amount);
		assertEquals(transactAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);
	}


	@Test
	public void testGenerateBookingsClientWithFeeWithoutBankFeeSettledCredit() throws Exception {
		setupAccountsFromSettings();
		setupCardAndRepository();
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT);
		line.amount = new BigDecimal("123.45");
		line.setCardTransaction(new BigDecimal("123.45"), cardTransactionCcy, cardNumber, cardTransactionDate, cardDetails1, cardDetails2);

		CardTransaction cardTransaction = line.getCardTransaction();

		List<Booking> bookings = generator.generateFor(line, postingFile);

		assertEquals(2, bookings.size());

		Booking booking = bookings.get(0);
		assertEquals(mirrorAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(transactAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);

		booking = bookings.get(1);
		assertEquals(transactAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(cardSettlementAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);
	}

	@Test
	public void testBookingIsNotGeneratedWhenClientWithZeroFee() throws Exception {
		setupAccountsFromSettings();
		setupCardAndRepository();
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT_WITH_FEE);
		line.amount = BigDecimal.ZERO;
		line.setCardTransaction(new BigDecimal("123.45"), cardTransactionCcy, cardNumber, cardTransactionDate, cardDetails1, cardDetails2);
		CardTransaction cardTransaction = line.getCardTransaction();
		cardTransaction.feeAmount = BigDecimal.ZERO;
		cardTransaction.feeCurrency = Booking.CURRENCY;
		cardTransaction.feeType = createFeeTypeWithSettlementAccount("197555552200EUR");

		List<Booking> bookings = generator.generateFor(line, postingFile);

		assertEquals(0, bookings.size());
	}

	@Test(expected = IllegalArgumentExceptionWithParameters.class)
	public void testBookingIsNotGeneratedWhenClientWithoutCardTransaction() throws Exception {
		setupAccountsFromSettings();
		setupCardAndRepository();
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT_WITH_FEE);
		line.amount = BigDecimal.ONE;

		generator.generateFor(line, postingFile);
	}


	@Test
	public void testGenerateBookingsClientWithFeeSettledDebit() throws Exception {
		setupAccountsFromSettings();
		setupCardAndRepository();
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT_WITH_FEE);
		line.amount = new BigDecimal("-123.45");
		line.setCardTransaction(line.amount, cardTransactionCcy, cardNumber, cardTransactionDate, cardDetails1, cardDetails2);
		CardTransaction cardTransaction = line.getCardTransaction();
		cardTransaction.feeAmount = new BigDecimal("-12.45");
		cardTransaction.feeCurrency = Booking.CURRENCY;
		cardTransaction.feeType = createFeeTypeWithSettlementAccount("197555552200EUR");

		List<Booking> bookings = generator.generateFor(line, postingFile);
		assertEquals(4, bookings.size());

		Booking booking = bookings.get(0);
		assertEquals(costAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(transactAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);

		booking = bookings.get(1);
		assertEquals(transactAccount, booking.debitAccount);
		assertEquals(line.amount.abs(), booking.amount);
		assertEquals(mirrorAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);

		booking = bookings.get(2);
		assertEquals(transactAccount, booking.debitAccount);
		assertEquals(line.amount.abs().add(cardTransaction.feeAmount.abs()), booking.amount);
		assertEquals(cardTransaction.feeType.settlementAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);

		booking = bookings.get(3);
		assertEquals(cardSettlementAccount, booking.debitAccount);
		assertEquals(line.amount.abs().add(cardTransaction.feeAmount.abs()), booking.amount);
		assertEquals(transactAccount, booking.creditAccount);
		assertEquals(line.valueDate, booking.debitValueDate);
		assertEquals(line.valueDate, booking.creditValueDate);
		assertEquals(cardTransaction.getDetails(), booking.details);
	}


	private FeeType createFeeTypeWithSettlementAccount(String settlementAccount) {
		FeeType type = new FeeType();
		type.settlementAccount = settlementAccount;
		return type;
	}

	private TransferType createTransferType(SettlementType settlementType) {
		TransferType type = new TransferType();
		type.settlementType = settlementType;
		return type;
	}

	public static Booking setupSettlement(Booking settlement) {
		settlement.amount = new BigDecimal("1.0");
		settlement.bankToBankInfo = "bankToBank";
		settlement.businessDate = Utils.getDate(2009, 1, 1);
		settlement.creditAccount = "crAccount";
		settlement.creditValueDate = Utils.getDate(2009, 1, 2);
		settlement.currency = "CZK";
		settlement.debitValueDate = Utils.getDate(2009, 1, 3);
		settlement.details = "details";
		settlement.debitAccount= "drAccount";
		settlement.orderingBankAddress= "orderingBankAddress";
		settlement.orderingBankName= "orderingBankName";
		settlement.sequenceNr = 2;
		settlement.source= "SO";
		settlement.trReference= "trReference";
		settlement.debitValueDate = Utils.getDate(2009, 1, 4);
		settlement.creditValueDate = Utils.getDate(2009, 1, 4);
		return settlement;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBookingCannotBeGeneratedWhenInactiveCardIsFound() throws Exception {
		setupAccountsFromSettings();
		line.amount = new BigDecimal("1.23");
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT);
		line.setCardTransaction(line.amount, cardTransactionCcy, cardNumber, cardTransactionDate, cardDetails1, cardDetails2);
		when(cardRepository.findByNumber(cardNumber)).thenReturn(createInvalidCard(cardNumber));

		generator.generateFor(line, postingFile);
	}

	private Card createInvalidCard(String cardNumber) {
		Card card = new Card();
		card.number = cardNumber;
		card.activeTo = Utils.getYesterday();
		return card;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBookingCannotBeGeneratedWhenCardIsNotFound() throws Exception {
		setupAccountsFromSettings();

		line.amount = new BigDecimal("1.23");
		line.transferType = createTransferType(SettlementType.CLIENT_SETTLEMENT);
		line.setCardTransaction(line.amount, cardTransactionCcy, cardNumber, cardTransactionDate, cardDetails1, cardDetails2);
		when(cardRepository.findByNumber(cardNumber)).thenReturn(null);

		generator.generateFor(line, postingFile);
	}

}
