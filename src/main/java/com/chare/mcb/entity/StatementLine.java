package com.chare.mcb.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name = EntityWithIdInteger.STATEMENT_LINE_TABLE)
@Access(AccessType.FIELD)
public class StatementLine extends com.chare.mcb.entity.EntityWithIdInteger {

	@Id
	@GeneratedValue
	public Integer id;

	@Column(name = "number")
	public int number;

	@Column(name = "valueDate")
	@Temporal(TemporalType.DATE)
	@NotNull
	public Date valueDate;

	@Column(name = "amount")
	@NotNull
	public BigDecimal amount;

	@Column(name = "swiftType")
	@Size(min = 4, max = 4)
	public String swiftType;

	@Column(name = "reference1")
	@Size(min = 1, max = 14)
	public String reference1;

	@Column(name = "reference2")
	@Size(min = 1, max = 16)
	public String reference2;

	@Column(name = "details1")
	@Size(min = 0, max = 35)
	public String details1;
	@Column(name = "details2")
	@Size(min = 0, max = 35)
	public String details2;
	@Column(name = "details3")
	@Size(min = 0, max = 35)
	public String details3;
	@Column(name = "details4")
	@Size(min = 0, max = 35)
	public String details4;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "statementId")
	private Statement statement;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "transferType")
	public TransferType transferType;

	@Embedded
	private CardTransaction cardTransaction;

	@Column(name = "bookDate")
	@Temporal(TemporalType.DATE)
	public Date bookDate;

	public StatementLine() {
		amount = BigDecimal.ZERO;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		ReflectionToStringBuilder stringBuilder = new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE, null, null, false, false);
		stringBuilder.setExcludeFieldNames(new String[] {"statement"});
		return stringBuilder.toString();
	}

	public Statement getStatement() {
		return statement;
	}

	void setStatement(Statement statement) {
		this.statement = statement;
	}

	public CardTransaction getCardTransaction() {
		return cardTransaction;
	}

	public String getTransferTypeCode() {
		String reference2TransferTypeCodePart = findReference2TransferTypeCodePart();
		if (swiftType == null || reference2TransferTypeCodePart == null)
			return null;
		return swiftType + reference2TransferTypeCodePart;
	}

	private String findReference2TransferTypeCodePart() {
		if (reference2 != null && reference2.startsWith("//") && reference2.substring(2).indexOf("-") > 0)
			return reference2.substring(2, reference2.substring(2).indexOf("-") + 2); // expected //xxxx-yyyyyyy, xxxx is what we look for
		return null;
	}

	public boolean parseCardTransactionDetails() {
		return transferType != null && transferType.cardTransactionType != null;
	}

	public void clearCardTransaction() {
		cardTransaction = null;
	}

	public void setCardTransaction(BigDecimal amount, String currency,
			String cardNumber, Date date, String details1,
			String details2) {
		if (cardTransaction == null)
			cardTransaction = new CardTransaction();
		cardTransaction.amount = amount;
		cardTransaction.currency = currency;
		cardTransaction.cardNumber = cardNumber;
		cardTransaction.date = date;
		cardTransaction.details1 = details1;
		cardTransaction.details2 = details2;
	}

	public String getCurrency() {
		return Booking.CURRENCY;
	}

	public boolean requiresResolution() {
		if (transferType == null)
			return true;
		CardTransactionType cardTransactionType = transferType.cardTransactionType;
		if (cardTransactionType != null) {
			CardTransaction cardTransaction = getCardTransaction();
			if (cardTransaction == null)
				return true;
			if (cardTransactionType == CardTransactionType.TRANSACTION_FEE || cardTransactionType == CardTransactionType.TRANSACTION_FEE)
				if (cardTransaction.feeType == null)
					return true;
		}
		return false;
	}

	public String generateBookingDetailsForBankSettlement() {
		return (transferType.code + " " + StringUtils.defaultString(details1) + " " + StringUtils.defaultString(details2)).trim();
	}


}
