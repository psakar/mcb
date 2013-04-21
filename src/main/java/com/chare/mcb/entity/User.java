package com.chare.mcb.entity;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.chare.core.CustomLocale;
import com.chare.core.ILocale;
import com.chare.core.LanguageIndex;
import com.chare.core.Utils;
import com.chare.service.audit.Audited;

@Entity
@Table(name = EntityWithIdInteger.USERS_TABLE)
@Access(AccessType.FIELD)
public class User extends com.chare.mcb.entity.EntityWithIdInteger implements ILocale, LanguageIndex, com.chare.entity.UserType, Audited  {


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

	@Transient
	private Date lastAccess;
	@Transient
	private Integer lastUnsuccessfulCount;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("role")
	private List<UserRole> userRoles;

	public static final Locale localeSk = new Locale("sk", "SK");

	public User() {
		this.languageId = 1;
		this.password = Utils.encodePassword(User.DEFAULT_PASSWORD.getBytes());
		this.enabled = true;
		this.userRoles = new ArrayList<UserRole>();
	}

	public User(Integer id, String username) {
		this();
		this.id = id;
		this.username = username;
	}

	public User(Integer id) {
		this(id, null);
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean userTemplate) {
		this.template = userTemplate;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = (password == null ? null
				: Utils.encodePassword(password.getBytes()));
	}

	public Date getLastAccess() {
		return lastAccess;
	}

	public int getLastUnsuccessfulCount() {
		return lastUnsuccessfulCount;
	}

	@Override
	public Locale getLocale() {
		if (languageId == 4)
			return User.localeSk;
		if (languageId == 3)
			return CustomLocale.LOCALE_CZ;
		else if (languageId == 2)
			return Locale.GERMAN;
		return Locale.ENGLISH;
	}

	public int getLocaleId() {
		return languageId;
	}

	public void setLocale(final Locale locale) {
		if (locale == null)
			return;
		languageId = 1;
		if (locale.getLanguage().compareTo(Locale.GERMAN.getLanguage()) == 0) {
			languageId = 2;
		}
		if (locale.getLanguage().compareTo(CustomLocale.LOCALE_CZ.getLanguage()) == 0)
			languageId = 3;
		if (locale.getLanguage().compareTo(User.localeSk.getLanguage()) == 0)
			languageId = 4;
	}

	public String getLocaleCode() {
		return getLocale().getLanguage();
	}

	public void setLocaleId(int value) {
		languageId = value;
	}

	@Override
	public NumberFormat getCurrencyNumberFormat() {
		return CustomLocale.createCurrencyNumberFormat(getLocale());
	}

	@Override
	public NumberFormat getCurrencyFormat() {
		return CustomLocale.createCurrencyNumberFormat(getLocale());
	}

	@Override
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
	}

	@Override
	public DateFormat getDateTimeFormat() {
		return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, getLocale());
	}

	/*public DateTimeFormatter getJodaDateTimeFormat() {
    return DateTimeFormat.mediumDateTime().withLocale(getLocale());
  }

  public DateTimeFormatter getJodaDateFormat() {
    return DateTimeFormat.mediumDate().withLocale(getLocale());
  }*/

	@Override
	public NumberFormat getIntegerFormat() {
		return NumberFormat.getIntegerInstance(getLocale());
	}

	@Override
	public NumberFormat getNumberFormat() {
		NumberFormat numberInstance = NumberFormat.getNumberInstance(getLocale());
		numberInstance.setMaximumFractionDigits(10);
		numberInstance.setMinimumFractionDigits(0);
		return numberInstance;
	}

	@Override
	public NumberFormat getPercentFormat() {
		return NumberFormat.getPercentInstance(getLocale());
	}

	@Override
	public DateFormat getTimeFormat() {
		return DateFormat.getTimeInstance(DateFormat.LONG, getLocale());
	}

	public String getFullname() {
		return StringUtils.trim((surname == null ? "" : surname) + (name == null ? "" : " " + name));
	}

	public boolean isAuthenticated() {
		return isPersistent();
	}

	@Override
	public int getLanguageIndex() {
		if (languageId == 3)
			return 2;
		if (languageId == 4)
			return 1;
		return 1;
	}

	public void resetPassword() {
		setPassword(DEFAULT_PASSWORD);
		this.unsuccessfulCount = 0;
	}

	public void resetUnsuccessfulCount() {
		if (this.enabled)
			this.unsuccessfulCount = 0;
	}

	public Date getLoginTimestamp() {
		return loginTimestamp;
	}

	public void setLoginTimestamp(Date loginTimestamp) {
		this.lastAccess = this.loginTimestamp;
		this.loginTimestamp = loginTimestamp;
		if (this.lastUnsuccessfulCount == null)
			this.lastUnsuccessfulCount = this.unsuccessfulCount;
		this.unsuccessfulCount = 0;
	}

	public int getUnsuccessfulCount() {
		return unsuccessfulCount;
	}

	public void setUnsuccessfulCount(int unsuccessfulCount) {
		this.unsuccessfulCount = unsuccessfulCount;
	}

	public List<Role> getRoles() {
		List<Role> roles = new ArrayList<Role>();
		for (UserRole userRole : userRoles) {
			roles.add(userRole.role);
		}
		return roles;
	}

	public List<String> getRoleCodes() {
		List<String> roleCodes = new ArrayList<String>();
		for (UserRole userRole : userRoles) {
			roleCodes.add(userRole.role.code);
		}
		return roleCodes;
	}

	public void addRole(Role role) {
		if (getRoles().contains(role.code))
			return;
		userRoles.add(new UserRole(this, role));
	}

	public boolean hasRoles(String ... roles) {
		if (roles.length == 0)
			return true;
		List<String> roleCodes = getRoleCodes();
		for (String role : roles) {
			if (!roleCodes.contains(role))
				return false;
		}
		return true;
	}

	public boolean hasAnyOfRoles(String ... roles) {
		if (roles.length == 0)
			return true;
		List<String> roleCodes = getRoleCodes();
		for (String role : roles) {
			if (roleCodes.contains(role))
				return true;
		}
		return false;
	}

	public void addRoles(Role ... roles) {
		for (Role role : roles) {
			addRole(role);
		}
	}

	public void removeRole(Role role) {
		UserRole toRemove = null;
		for (UserRole userRole : userRoles) {
			if (userRole.role.equals(role)) {
				toRemove = userRole;
				break;
			}
		}
		if (toRemove != null)
			userRoles.remove(toRemove);
	}

	public void recordUnsuccessfulLogin(Date loginTimestamp) {
		this.lastAccess = this.loginTimestamp;
		this.loginTimestamp = loginTimestamp;
		this.unsuccessfulCount++;
	}

	@Override
	public String getAuditInfo() {
		return AuditUtils.join(AuditUtils.USER,
				this.id,
				this.name,
				this.surname,
				this.username,
				this.email,
				this.phone,
				AuditUtils.booleanToString(this.enabled),
				languageId,
				AuditUtils.dateToString(this.lastAccess),
				this.unsuccessfulCount,
				AuditUtils.booleanToString(this.template),
				ArrayUtils.toString(getRoleCodes())
				);
	}
}
