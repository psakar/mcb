package com.chare.mcb.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.chare.core.Utils;

@Entity
@Table(name = EntityWithIdInteger.POSTING_FILE_TABLE)
@Access(AccessType.FIELD)
public class PostingFile extends EntityWithIdInteger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Version
	@Column(name = "version")
	@Temporal(TemporalType.TIMESTAMP)
	private Date version;

	@Temporal(TemporalType.TIMESTAMP)
	private Date approved;

	@ManyToOne
	@JoinColumn(name = "approvedUsersId")
	private User approvedUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	private Date created;

	@ManyToOne
	@JoinColumn(name = "createdUsersId")
	private User createdUser;

	//	@Column(name = "fileType")
	//	private int fileType;
	@Column(name = "filename")
	public String filename;

	@OneToMany(mappedBy = "postingFile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	private List<Booking> bookings;

	public PostingFile() {
		bookings = new ArrayList<Booking>();
	}

	public PostingFile(User createdBy, String filename) {
		this();
		this.filename = filename;
		setCreatedUser(createdBy);
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Date getApproved() {
		return this.approved;
	}

	public User getApprovedUser() {
		return this.approvedUser;
	}

	public void setApprovedUser(User user) {
		this.approved = new Date();
		this.approvedUser = user;
	}

	public Date getCreated() {
		return created;
	}

	public User getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(User user) {
		this.createdUser = user;
		this.created = new Date();
	}

	public Booking addBooking() {
		Booking booking = new Booking();
		booking.postingFile = this;
		bookings.add(booking);
		return booking;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public boolean canBeApprovedBy(User user) {
		if (createdUser == null || user == null || approvedUser != null)
			return false;
		return !Utils.isEqual(user.getId(), createdUser.getId());
	}

	public boolean isExported() {
		return approvedUser != null;
	}
	public Date getVersion() {
		return version;
	}

}