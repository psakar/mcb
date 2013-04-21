package com.chare.mcb.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = EntityWithIdInteger.USER_ROLES_TABLE)
@Access(AccessType.FIELD)
public class UserRole extends com.chare.mcb.entity.EntityWithIdInteger {

	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "role")
	public Role role;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "usersId")
	public User user;

	public UserRole() {}
	public UserRole(User user, Role role) {
		this.user = user;
		this.role = role;
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
