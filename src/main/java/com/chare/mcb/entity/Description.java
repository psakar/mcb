package com.chare.mcb.entity;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import com.chare.core.Utils;

@Embeddable
@Access(AccessType.FIELD)
public class Description implements Serializable {
	@Column(name = "description1")
	@Size(max = 50)
	@NotEmpty
	public String description1;
	@Column(name = "description2")
	@Size(max = 50)
	@NotEmpty
	public String description2;
	@Column(name = "description3")
	@Size(max = 50)
	@NotEmpty
	public String description3;

	public Description() {
	}

	public Description(String description1, String description2, String description3) {
		this.description1 = description1;
		this.description2 = description2;
		this.description3 = description3;
	}

	public Description(String description) {
		this(description, description, description);
	}

	public String getDescription(int index) {
		switch (index) {
			case 1:
				return description1;
			case 2:
				return description2;
			case 3:
				return description3;
			default:
				return description1;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Description))
			return false;
		Description other = (Description) obj;
		return Utils.isEqual(description1, other.description1) &&
				Utils.isEqual(description2, other.description2) &&
				Utils.isEqual(description3, other.description3) ;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
