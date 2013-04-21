package com.chare.mcb.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = EntityWithIdInteger.POSTING_FILE_TABLE)
@Access(AccessType.FIELD)
public class PostingFileItem extends EntityWithIdInteger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Version
	@Column(name = "version")
	@Temporal(TemporalType.TIMESTAMP)
	public Date version;

	@Temporal(TemporalType.TIMESTAMP)
	public Date approved;

	@ManyToOne
	@JoinColumn(name = "approvedUsersId")
	public User approvedUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	public Date created;

	@ManyToOne
	@JoinColumn(name = "createdUsersId")
	public User createdUser;

	//	@Column(name = "fileType")
	//	private int fileType;
	@Column(name = "filename")
	public String filename;


	public PostingFileItem() {
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}