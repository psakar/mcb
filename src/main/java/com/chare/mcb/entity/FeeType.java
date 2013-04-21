package com.chare.mcb.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.chare.entity.EntityWithIdImpl;
import com.chare.service.audit.Audited;


@Entity
@Table(name = EntityWithIdInteger.FEE_TYPE_TABLE)
@Access(AccessType.FIELD)
public class FeeType extends EntityWithIdImpl<String> implements Audited {

	@Id
	@NotNull
	@Size(max = 10)
	public String code;
	@Embedded
	public Description description;

	@ManyToOne(cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "cardType")
	@NotNull
	public CardType cardType;


	@Embedded
	public FeeCalculation calculation;

	@Column(name = "settlementAccount")
	@Size(min = 14, max = 15)
	public String settlementAccount;

	@ManyToOne(cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "transferType")
	@NotNull
	public TransferType transferType;

	@Version
	@Column(name = "version")
	@Temporal(TemporalType.TIMESTAMP)
	private Date version;


	public FeeType() {
		this(null);
	}

	public FeeType(String code) {
		this(code, new Description());
	}

	public FeeType(String code, Description description) {
		this.code = code;
		this.description = description;
		this.calculation = new FeeCalculation();
	}

	@Override
	public String getId() {
		return code;
	}
	@Override
	public void setId(String id) {
		this.code = id;
	}

	public Date getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public String getAuditInfo() {
		return AuditUtils.join(AuditUtils.FEE_TYPE,
				this.code,
				this.description.description1,
				this.description.description2,
				this.description.description3,
				this.cardType.code,
				this.transferType.code,
				this.settlementAccount,
				AuditUtils.numberToString(calculation.amount),
				AuditUtils.numberToString(calculation.percentage),
				AuditUtils.dateToString(this.version)
				);
	}
}
