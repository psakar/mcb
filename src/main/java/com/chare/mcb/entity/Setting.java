package com.chare.mcb.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = EntityWithIdInteger.SETTINGS_TABLE)
@Access(AccessType.FIELD)
public class Setting extends EntityWithIdInteger {

	@Id
	@GeneratedValue
	public Integer id;
	@NotEmpty
	@Size(max = 15)
	public String code;
	@Size(max = 50)
	public String description1;
	@Size(max = 50)
	public String description2;
	@Size(max = 50)
	public String description3;
	public int type;
	@Size(max = 255)
	public String value;

	public Setting() {}

	public Setting(Integer id, String code, String description1, String description2, String description3, int type, String value) {
		this.id = id;
		this.code = code;
		this.description1 = description1;
		this.description2 = description2;
		this.description3 = description3;
		this.type = type;
		this.value = value;
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