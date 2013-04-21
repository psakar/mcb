package com.chare.mcb.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = EntityWithIdInteger.USERS_TABLE)
@Access(AccessType.FIELD)
public class UserItem extends com.chare.mcb.entity.EntityWithIdInteger {


	public static final String DEFAULT_PASSWORD = "start123";

	@Id
	@GeneratedValue
	public Integer id;
	@Column(name = "template")
	public boolean template;

	@Size(min = 6, max = 20)
	@NotEmpty
	@Column(name = "username")
	public String username;
	@NotEmpty
	@Column(name = "password")
	@Access(AccessType.FIELD)
	public byte[] password;
	@Column(name = "surname")
	@NotEmpty
	@Size(min = 3, max = 40)
	public String surname;
	@Column(name = "name")
	@Size(min = 3, max = 40)
	public String name;
	@Column(name = "languageId")
	public int languageId;
	@NotEmpty
	@Email
	@Size(min = 6, max = 50)
	@Column(name = "email")
	public String email;
	@Column(name = "phone")
	@Size(max = 35)
	public String phone;
	@Column(name = "enabled")
	public boolean enabled;
	@Column(name = "unsuccessfulCount")
	private int unsuccessfulCount;


	@Column(name = "lastAccess")
	@Temporal(TemporalType.TIMESTAMP)
	private Date loginTimestamp;

	public UserItem() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}


	public String getFullname() {
		return StringUtils.trim((surname == null ? "" : surname) + (name == null ? "" : " " + name));
	}

	public Date getLoginTimestamp() {
		return loginTimestamp;
	}

	public int getUnsuccessfulCount() {
		return unsuccessfulCount;
	}

}
