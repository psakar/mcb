package com.chare.mcb.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = EntityWithIdInteger.CARD_TABLE)
@Access(AccessType.FIELD)
public class CardItem extends com.chare.mcb.entity.EntityWithIdInteger {

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

	@Column(name = "settlementAccount")
	@Size(min = 15, max = 15)
	@NotEmpty
	public String settlementAccount;

	@Column(name = "statementPeriod")
	@Enumerated(EnumType.ORDINAL)
	public StatementPeriod statementPeriod;

	@Email
	@Size(min = 0, max = 50)
	@Column(name = "email")
	public String email;

	@Column(name = "phone")
	@Size(max = 35)
	public String phone;

	@Embedded
	@NotNull
	public Address address;

	@Column(name = "activeFrom")
	@Temporal(TemporalType.DATE)
	@NotNull
	public Date activeFrom;

	@Column(name = "activeTo")
	@Temporal(TemporalType.DATE)
	@NotNull
	public Date activeTo;

	@Column(name = "languageId")
	public int languageId;

	@Column(name = "\"limit\"")
	public BigDecimal limit;

	@Column(name = "lastStatementNr")
	public Integer lastStatementNr;

	@Column(name = "version")
	@Temporal(TemporalType.TIMESTAMP)
	public Date version;

	public CardItem() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}
