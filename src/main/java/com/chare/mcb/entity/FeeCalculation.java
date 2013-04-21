package com.chare.mcb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.chare.core.Utils;

@Embeddable
@Access(AccessType.FIELD)
public class FeeCalculation implements Serializable {

	public FeeCalculation() {
		this(BigDecimal.ZERO, BigDecimal.ZERO);
	}

	public FeeCalculation(BigDecimal amount, BigDecimal percentage) {
		this.amount = amount;
		this.percentage = percentage;
	}

	@Column(name = "amount")
	@NotNull
	public BigDecimal amount;

	@Column(name = "percentage")
	@NotNull
	public BigDecimal percentage;

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof FeeCalculation))
			return false;
		FeeCalculation other = (FeeCalculation) obj;
		return Utils.isEqual(this.amount, other.amount) && Utils.isEqual(this.percentage, other.percentage);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public void copyFrom(FeeCalculation calculation) {
		amount = calculation.amount;
		percentage = calculation.percentage;
	}

	public BigDecimal calculate(BigDecimal amount) {
		return this.amount.negate().add(amount.multiply(percentage).divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_EVEN) ;
	}
}
