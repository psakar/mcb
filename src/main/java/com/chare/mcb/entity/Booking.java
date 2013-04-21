package com.chare.mcb.entity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.chare.core.Utils;

@Entity
@Table(name = EntityWithIdInteger.BOOKING_TABLE)
@Access(AccessType.FIELD)
public class Booking extends EntityWithIdInteger {

	private static final DecimalFormat decimalFormat = new DecimalFormat("00000");
	public static final String BRANCH = "197";
	public static final String CURRENCY = "EUR";
	public static final String SOURCE = "MC";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public BigDecimal amount;

	@Column(name = "bankToBankInfo")
	public String bankToBankInfo;

	@Temporal(TemporalType.DATE)
	@Column(name = "businessDate")
	public Date businessDate;

	@Column(name = "creditAccount")
	public String creditAccount;

	@Column(name = "creditValueDate")
	public Date creditValueDate;

	@Column(name = "debitValueDate")
	public Date debitValueDate;

	public String details;

	@Column(name = "debitAccount")
	public String debitAccount;

	@Column(name = "orderingBankAddress")
	public String orderingBankAddress;

	@Column(name = "orderingBankName")
	public String orderingBankName;

	@Column(name = "sequenceNr")
	public int sequenceNr;

	public String source;

	@Column(name = "trReference")
	public String trReference;

	@Column(name = "currency")
	public String currency;

	//	@ManyToOne
	//	@JoinColumn(name = "fk__ecmc_transaction_id")
	//	private EcmcTransaction ecmcTransaction;

	@ManyToOne
	@JoinColumn(name = "postingFileId")
	PostingFile postingFile;

	public Booking() {
		this.amount = BigDecimal.ZERO;
		this.currency = CURRENCY;
		this.source = SOURCE;
		this.businessDate = Utils.getToday();
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public DateFormat getReferenceDateFormat() {
		return new SimpleDateFormat("yyMMdd");
	}

	public String createTrReference(Date businessDate, int referenceNr) {
		return createTrReferencePrefix(businessDate) + decimalFormat.format(referenceNr);
	}

	public String createTrReferencePrefix(Date businessDate) {
		return BRANCH + getReferenceDateFormat().format(businessDate) + "MC";
	}

}