package com.chare.mcb.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = EntityWithIdInteger.CALENDAR_TABLE)
@Access(AccessType.FIELD)
public class Calendar extends EntityWithIdInteger {

	@Id
	@Temporal(TemporalType.DATE)
	public Date id_date;
	public Integer holiday;

	public Calendar() {}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public void setId(Integer id) {
	}

}