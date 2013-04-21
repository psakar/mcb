package com.chare.mcb.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.chare.entity.EntityWithIdImpl;
import com.chare.service.audit.Audited;


@Entity
@Table(name = EntityWithIdInteger.CARD_TYPE_TABLE)
@Access(AccessType.FIELD)
public class CardType extends EntityWithIdImpl<String> implements Audited {

	@Id
	@NotNull
	@Size(max = 10)
	public String code;
	@Embedded
	public Description description;


	@Version
	@Column(name = "version")
	@Temporal(TemporalType.TIMESTAMP)
	private Date version;

	public CardType(String code, Description description) {
		this.code = code;
		this.description = description;
	}

	public CardType() {
		this(null);
	}

	public CardType(String code) {
		this.code = code;
		this.description = new Description();
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

	@PrePersist
	public void prePersist() {
		version = new Date();
	}

	@Override
	public String getAuditInfo() {
		return AuditUtils.join(AuditUtils.CARD_TYPE,
				this.code,
				this.description.description1,
				this.description.description2,
				this.description.description3,
				AuditUtils.dateToString(this.version)
				);
	}
}
