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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name = EntityWithIdInteger.STATEMENT_LINE_TABLE)
@Access(AccessType.FIELD)
public class CardStatementLine extends com.chare.mcb.entity.EntityWithIdInteger {

	@Id
	@GeneratedValue
	public Integer id;

	@Column(name = "amount")
	@NotNull
	public BigDecimal amount;

	@Column(name = "swiftType")
	@Size(min = 4, max = 4)
	public String swiftType;

	@Column(name = "reference2")
	@Size(min = 1, max = 16)
	public String reference2;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "transferType")
	public TransferType transferType;

	@Embedded
	public CardTransaction cardTransaction;

	@Column(name = "bookDate")
	@Temporal(TemporalType.DATE)
	public Date bookDate;

	public CardStatementLine() {
		amount = BigDecimal.ZERO;
		cardTransaction = new CardTransaction();
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
		return stringBuilder.toString();
	}

	public String getCurrency() {
		return Booking.CURRENCY;
	}

	public CardTransactionType getType() {
		return transferType == null ? null : transferType.cardTransactionType;
	}

	@Transient
	private CardStatementLine relatedLineWithFee;

	public void setRelatedLineWithFee(CardStatementLine line) {
		relatedLineWithFee = line;
	}

	public BigDecimal getTransactionFeeAmount() {
		return relatedLineWithFee == null ? BigDecimal.ZERO : relatedLineWithFee.calculateTransactionFeeAmount();
	}

	BigDecimal calculateTransactionFeeAmount() {
		return amount.add(cardTransaction.feeAmount);
	}

	public CardStatementLine getRelatedLineWithFee() {
		return relatedLineWithFee;
	}

	public boolean showFeeLine() {
		boolean show = (relatedLineWithFee!= null) && (relatedLineWithFee.getFee().compareTo(BigDecimal.ZERO)!=0);
		//		System.err.println("Show fee line " + show + " related Line " + relatedLineWithFee + " fee amount " + getFeeAmount());
		return show;
	}

	@Transient
	public CardStatementLine getThis() {
		return this;
	}

	public boolean isRelatedFeeLineOfLine(CardStatementLine lineWithFee) {
		String feeReference = getFeeReference();
		return feeReference != null && feeReference.equals(lineWithFee.getFeeReference());
	}

	public boolean isRelatedFeeLine() {
		return transferType != null && transferType.cardTransactionType != null && transferType.cardTransactionType == CardTransactionType.TRANSACTION_FEE;
	}

	public boolean isFeeLine() {
		return transferType != null && transferType.cardTransactionType != null && transferType.cardTransactionType == CardTransactionType.FEE;
	}

	public BigDecimal getFee() {
		return isFeeLine() || isRelatedFeeLine() ? amount.add(getBankFee()) : BigDecimal.ZERO;
	}

	public BigDecimal getBankFee() {
		return cardTransaction != null && cardTransaction.feeAmount != null ? cardTransaction.feeAmount : BigDecimal.ZERO;
	}

	String getFeeReference() {
		if (reference2 != null && reference2.startsWith("//")) {
			int index = reference2.substring(2).indexOf("-");
			if (index > 0)
				return reference2.substring(2 + 1 + index);
		}
		return null;
	}

	public BigDecimal getAmountWithFees() {
		BigDecimal total = amount.add(getBankFee());
		if (relatedLineWithFee != null)
			total = total.add(relatedLineWithFee.getFee());
		return total;
	}
}
