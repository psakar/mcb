package com.chare.mcb.entity;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.chare.core.LanguageIndex;
import com.chare.entity.EntityWithIdImpl;


@Entity
@Table(name = EntityWithIdInteger.ROLE_TABLE)
@Access(AccessType.FIELD)
public class Role extends EntityWithIdImpl<String> {

	public static final String UPLOAD_STATEMENTS = "UPLOAD_STATEMENTS";
	public static final String EXPORT_POSTINGS = "EXPORT_POSTINGS";
	public static final String APP_ADMIN = "APP_ADMIN";
	public static final String USER_ADMIN = "USER_ADMIN";
	public static final Role USER_ADMIN_ROLE = new Role(USER_ADMIN,"User administration","User administration","Správa uživatelů");
	public static final Role APP_ADMIN_ROLE = new Role(APP_ADMIN,"Application administration","Application administration","Správa aplikace");
	public static final Role EXPORT_POSTINGS_ROLE = new Role(EXPORT_POSTINGS,"Export postings","Export postings","Export zaúčtování");
	public static final Role UPLOAD_STATEMENTS_ROLE = new Role(UPLOAD_STATEMENTS,"Upload statements","Upload statements","Nahrání výpisů");



	public static final List<Role> ROLES = Arrays.asList(USER_ADMIN_ROLE, APP_ADMIN_ROLE, EXPORT_POSTINGS_ROLE, UPLOAD_STATEMENTS_ROLE);

	@Id
	@NotNull
	@Size(max = 20)
	public String code;
	@Size(max = 50)
	public String description1;
	@Size(max = 50)
	public String description2;
	@Size(max = 50)
	public String description3;

	public Role(String code, String description1, String description2, String description3) {
		this.code = code;
		this.description1 = description1;
		this.description2 = description2;
		this.description3 = description3;
	}

	public Role() {}

	public Role(String code) {
		this.code = code;
	}

	public String getDescription(LanguageIndex languageIndex) {
		if (languageIndex == null)
			return description1;
		switch (languageIndex.getLanguageIndex()) {
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
	public String getId() {
		return code;
	}
	@Override
	public void setId(String id) {
		this.code = id;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
		/*
		if (! (obj instanceof Role))
			return false;
		Role other = (Role) obj;
		return Utils.isEqual(getId(), other.getId());
		 */
	}
}
