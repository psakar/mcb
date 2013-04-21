package com.chare.mcb.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.chare.entity.EntityWithIdImpl;
import com.chare.service.audit.Audited;


@Entity
@Table(name = EntityWithIdInteger.TRANSFER_TYPE_TABLE)
@Access(AccessType.FIELD)
public class TransferType extends EntityWithIdImpl<String> implements Audited {

	@Id
	@NotNull
	@Size(max = 10)
	public String code;
	@Embedded
	public Description description;

	@Column(name = "settlementType")
	@Enumerated(EnumType.ORDINAL)
	public SettlementType settlementType;


	@Column(name = "settlementAccount")
	@Size(min = 14, max = 15)
	public String settlementAccount;

	@Column(name = "cardTransactionType")
	@Enumerated(EnumType.ORDINAL)
	public CardTransactionType cardTransactionType;

	@Version
	@Column(name = "version")
	@Temporal(TemporalType.TIMESTAMP)
	private Date version;


	public TransferType() {
		this(null);
	}

	public TransferType(String code) {
		this(code, new Description());
	}

	public TransferType(String code, Description description) {
		this.code = code;
		this.description = description;
		this.settlementType = SettlementType.CLIENT_SETTLEMENT;
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
	public String getAuditInfo() {
		return AuditUtils.join(AuditUtils.TRANSFER_TYPE,
				this.code,
				this.description.description1,
				this.description.description2,
				this.description.description3,
				this.settlementType,
				this.settlementAccount,
				this.cardTransactionType,
				AuditUtils.dateToString(this.version)
				);
	}
}
