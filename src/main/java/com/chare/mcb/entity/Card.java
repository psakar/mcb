package com.chare.mcb.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.chare.core.Utils;
import com.chare.service.audit.Audited;

@Entity
@Table(name = EntityWithIdInteger.CARD_TABLE)
@Access(AccessType.FIELD)
public class Card extends com.chare.mcb.entity.EntityWithIdInteger implements Audited {

	static final int DEFAULT_LANGUAGE = 3;

	@Id
	@GeneratedValue
	public Integer id;

	@Size(min = 16, max = 16)
	@Column(name = "number")
	@NotEmpty
	public String number;

	@ManyToOne(cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "cardType")
	@NotNull
	public CardType cardType;

	@Column(name = "cardHolderName")
	@Size(min = 3, max = 40)
	@NotEmpty
	public String holderName;

	@Email
	@Size(max = 50)
	@Column(name = "email")
	public String email;

	@Column(name = "phone")
	@Size(max = 35)
	public String phone;

	@Embedded
	@NotNull
	@Valid
	private Address address;

	@Column(name = "activeFrom")
	@Temporal(TemporalType.DATE)
	@NotNull
	public Date activeFrom;

	@Column(name = "activeTo")
	@Temporal(TemporalType.DATE)
	public Date activeTo;

	@Column(name = "settlementAccount")
	@Size(min = 14, max = 15)
	@NotEmpty
	public String settlementAccount;

	@Column(name = "statementPeriod")
	@Enumerated(EnumType.ORDINAL)
	public StatementPeriod statementPeriod;


	@Version
	@Column(name = "version")
	@Temporal(TemporalType.TIMESTAMP)
	private Date version;

	/**
	 *  list of client special conditions overrides default fee type
	 *  (client do not pay fee of type XY)
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "card", fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("id")
	private List<CardFeeType> feeTypes;

	@Column(name = "languageId")
	public int languageId;

	@Column(name = "\"limit\"")
	public BigDecimal limit;

	@Column(name = "lastStatementNr")
	public Integer lastStatementNr;

	@Column(name = "lastStatementDate")
	@Temporal(TemporalType.DATE)
	public Date lastStatementDate;

	@Column(name = "nextStatementDate")
	@Temporal(TemporalType.DATE)
	public Date nextStatementDate;

	public Card() {
		address = new Address();
		activeFrom = Utils.getToday();
		statementPeriod = StatementPeriod.DAILY;
		feeTypes = new ArrayList<CardFeeType>();
		languageId = DEFAULT_LANGUAGE;
	}

	public Card(Integer id, String number, CardType cardType) {
		this();
		this.id = id;
		this.number = number;
		this.cardType = cardType;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@PrePersist
	public void beforePersist() {
		version = new Date();
	}

	public Date getVersion() {
		return version;
	}

	public Address getMailAddress() {
		return address;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public List<CardFeeType> getFeeTypes() {
		return feeTypes;
	}

	public void addFeeType(FeeType feeType) {
		feeTypes.add(new CardFeeType(this, feeType));
	}

	public void prepareForNextStatement(Date lastStatementDate, Date nextStatementDate, int lastStatementNr) {
		this.lastStatementDate = nextStatementDate;
		this.nextStatementDate = nextStatementDate;
		this.lastStatementNr = lastStatementNr;
	}


	public Date getStatementEndDate() {
		return nextStatementDate;
	}

	public boolean isInactive() {
		return activeTo != null && activeTo.before(Utils.getToday());
	}

	@Override
	public String getAuditInfo() {
		return AuditUtils.join(AuditUtils.CARD,
				this.id,
				this.number,
				this.cardType.code,
				this.settlementAccount,
				this.holderName,
				this.email,
				this.phone,
				this.address.title,
				this.address.name,
				this.address.name2,
				this.address.street,
				this.address.town,
				this.address.zip,
				this.address.country,
				//AuditUtils.booleanToString(this.enabled),
				this.statementPeriod,
				AuditUtils.dateToString(this.activeFrom),
				AuditUtils.dateToString(this.activeTo),
				languageId,
				this.lastStatementNr,
				AuditUtils.dateToString(this.lastStatementDate),
				AuditUtils.dateToString(this.nextStatementDate),
				AuditUtils.dateToString(this.version)
				//FIXME version 2 feeType
				);
	}
}
