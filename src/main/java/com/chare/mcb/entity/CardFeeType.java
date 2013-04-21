package com.chare.mcb.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = EntityWithIdInteger.CARD_FEE_TYPE_TABLE)
@Access(AccessType.FIELD)
public class CardFeeType extends com.chare.mcb.entity.EntityWithIdInteger /*FIXME version 2 implements Audited */{

	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "cardId")
	@NotNull
	public Card card;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "feeType")
	@NotNull
	private FeeType feeType;

	@Embedded
	@NotNull
	public FeeCalculation calculation;

	public CardFeeType() {
		this.calculation = new FeeCalculation();
	}
	public CardFeeType(Card card, FeeType feeType) {
		this();
		this.card = card;
		setFeeType(feeType);
	}

	@Override
	public Integer getId() {
		return id;
	}
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	public FeeType getFeeType() {
		return feeType;
	}
	public void setFeeType(FeeType feeType) {
		this.feeType = feeType;
		if (feeType != null)
			calculation.copyFrom(feeType.calculation);
	}

}
