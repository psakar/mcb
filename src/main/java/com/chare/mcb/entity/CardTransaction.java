package com.chare.mcb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Embeddable
@Access(AccessType.FIELD)
public class CardTransaction implements Serializable {

	@Column(name = "cardTransactionDate")
	@Temporal(TemporalType.DATE)
	public Date date;

	@Column(name = "cardNumber")
	@Size(max = 16)
	public String cardNumber;

	@Column(name = "cardTransactionDetails1")
	@Size(max = 35)
	public String details1;

	@Column(name = "cardTransactionDetails2")
	@Size(max = 35)
	public String details2;

	@Column(name = "cardTransactionAmount")
	public BigDecimal amount;

	@Column(name = "cardTransactionCurrency")
	@Size(max = 3)
	public String currency;

	@Column(name = "feeAmount")
	public BigDecimal feeAmount;

	@Column(name = "feeCurrency")
	@Size(max = 3)
	public String feeCurrency;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "feeType")
	public FeeType feeType;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getDetails() {
		//		String cardNumberHint = StringUtils.right(StringUtils.defaultString(cardNumber), 6);
		return (StringUtils.defaultString(details1) + " " + StringUtils.defaultString(details2)).trim();
		//		return cardNumberHint + (details.length() > 0 ? (cardNumberHint != null && cardNumberHint.length() > 0 ? " " : "") + details : "");
	}
}
