package com.chare.mcb.entity;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

@Embeddable
@Access(AccessType.FIELD)
public class Address implements Serializable {
	@Column(name = "title")
	@Size(max = 40)
	public String title;
	@Column(name = "name")
	@Size(max = 40)
	@NotEmpty
	public String name;
	@Column(name = "name2")
	@Size(max = 40)
	public String name2;
	@Column(name = "street")
	@Size(max = 40)
	@NotEmpty
	public String street;
	@Column(name = "zip")
	@Size(max = 10)
	@NotEmpty
	public String zip;
	@Column(name = "town")
	@Size(max = 40)
	@NotEmpty
	public String town;
	@Column(name = "country")
	@Size(max = 40)
	@NotEmpty
	public String country;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getFullName() {
		return join(name, name2);
	}
	private String join(String s1, String s2) {
		return (StringUtils.defaultString(s1) + " " + StringUtils.defaultString(s2)).trim();
	}

	public String getZipAndTown() {
		return join(zip, town);
	}
}
