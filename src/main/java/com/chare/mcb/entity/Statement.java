package com.chare.mcb.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import com.chare.core.Utils;
import com.chare.service.audit.Audited;

/**
 * statement unique number - number and year
 * statement amounts are in EUR
 * statement split into pages (eg. number 74/1, 74/2 ... 74/X) in swift file is merged into one (eg. number 74)
 * store uploaded filename on statement (only info)
 */

@Entity
@Table(name = EntityWithIdInteger.STATEMENT_TABLE)
@Access(AccessType.FIELD)
public class Statement extends com.chare.mcb.entity.EntityWithIdInteger implements Audited {

	@Id
	@GeneratedValue
	public Integer id;

	@Version
	@Column(name = "version")
	@Temporal(TemporalType.TIMESTAMP)
	private Date version;

	@Size(min = 1, max = 4)
	@Column(name = "number")
	@NotEmpty
	public String number;

	@Size(min = 1, max = 15)
	@Column(name = "account")
	@NotEmpty
	public String account;

	@Column(name = "statementDate")
	@Temporal(TemporalType.DATE)
	@NotNull
	public Date statementDate;


	@Column(name = "sourceFilename")
	@NotNull
	public String sourceFilename;

	@Column(name = "openingBalance")
	@NotNull
	public BigDecimal openingBalance;

	@Column(name = "closingBalance")
	@NotNull
	public BigDecimal closingBalance;

	@Column(name = "year")
	private int year;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "statement", fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("number")
	private List<StatementLine> lines;

	@Column(name = "postingFileId")
	public Integer postingFileId;

	public Statement() {
		openingBalance = BigDecimal.ZERO;
		closingBalance = BigDecimal.ZERO;
		lines = new ArrayList<StatementLine>();
	}

	public Statement(Integer id) {
		this();
		setId(id);
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
		if (statementDate != null)
			year = Utils.getDatePart(Calendar.YEAR, statementDate);
	}

	public Date getVersion() {
		return version;
	}

	public int getYear() {
		return year;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public List<StatementLine> getLines() {
		return lines;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Statement))
			return false;
		Statement other = (Statement) obj;
		return toString().compareTo(other.toString()) == 0;
	}

	public StatementLine addLine() {
		StatementLine line = new StatementLine();
		line.setStatement(this);
		line.number = lines.size();
		lines.add(line);
		return line ;
	}

	public boolean isReadyToGenerateBookings() {
		return ! (hasGeneratedPostingFile() || requiresResolution() || isEmpty());
	}

	public boolean isEmpty() {
		return lines.size() == 0;
	}

	public boolean hasGeneratedPostingFile() {
		return postingFileId != null;
	}

	public boolean requiresResolution() {
		if (isEmpty())
			return false;
		for (StatementLine line : lines) {
			if (line.requiresResolution())
				return true;
		}
		return false;
	}

	@Override
	public String getAuditInfo() {
		return AuditUtils.join(AuditUtils.STATEMENT,
				this.number,
				this.year,
				AuditUtils.dateToString(this.version)
				);
	}

}
