package com.chare.mcb.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = EntityWithIdInteger.STATEMENT_VIEW)
@Access(AccessType.FIELD)
public class StatementItem extends com.chare.mcb.entity.EntityWithIdInteger {

	@Id
	public Integer id;

	@Column(name = "version")
	@Temporal(TemporalType.TIMESTAMP)
	private Date version;

	@Formula("right('000' || number, 4)")
	public String number;

	@Column(name = "account")
	public String account;

	@Column(name = "statementDate")
	@Temporal(TemporalType.DATE)
	public Date statementDate;


	@Column(name = "sourceFilename")
	public String sourceFilename;

	@Column(name = "openingBalance")
	public BigDecimal openingBalance;

	@Column(name = "closingBalance")
	public BigDecimal closingBalance;

	@Column(name = "year")
	public int year;

	@Column(name = "postingFileId")
	public Integer postingFileId;

	@Column(name = "postingFilename")
	public String postingFilename;

	public StatementItem() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Date getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StatementItem))
			return false;
		StatementItem other = (StatementItem) obj;
		return toString().compareTo(other.toString()) == 0;
	}

}
