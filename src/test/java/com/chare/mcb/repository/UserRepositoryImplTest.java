package com.chare.mcb.repository;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.chare.core.CustomLocale;
import com.chare.mcb.JpaRepositoryTestCase;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.User;

public class UserRepositoryImplTest extends JpaRepositoryTestCase<Integer, User, UserRepository> {

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.USERS_TABLE;
	}

	@Override
	protected User setupEntity(User entity) {
		entity.email = "email@a.bc";
		entity.enabled = true;
		entity.languageId = 1;
		entity.name = "name";
		entity.setPassword("password");
		entity.phone = "phone";
		entity.surname = "surname";
		entity.setUnsuccessfulCount(1);
		entity.username = "username";
		entity.setTemplate(false);
		entity.setLoginTimestamp(new Date());
		return entity;
	}

	@Override
	protected void assertPersistedEntity(User loaded, User entity) {
		assertEquals(entity.id, loaded.id);
		assertArrayEquals(entity.getRoles().toArray(), loaded.getRoles().toArray());
	}

	@Override
	protected UserRepository getRepository() {
		return new UserRepositoryImpl(entityManager);
	}

	@Test
	public void testFindByUsername() throws Exception {
		Integer id = findId(getTableName());
		String username = lookup("SELECT username FROM " + getTableName() + " WHERE id = " + id);
		assertEquals(id, repository.findByUsername(username).getId());
	}

	@Test
	public void testFindByUsernameSetsLocale() throws Exception {
		Integer id = findId(getTableName());
		int lanaguageId = 3;
		executeUpdateSql("UPDATE " + getTableName() + " SET languageId = " + lanaguageId + " WHERE id = " + id);
		String username = lookup("SELECT username FROM " + getTableName() + " WHERE id = " + id);

		assertEquals(CustomLocale.LOCALE_CZ.getLanguage(), repository.findByUsername(username).getLocale().getLanguage());
	}

	@Test
	public void testUserRolesArePersited() throws Exception {
		User entity = create();
		setupEntity(entity);

		entity.addRole(Role.APP_ADMIN_ROLE);

		repository.persist(entity);
		assertNotNull(entity.getId());
		entityManager.flush();
		entityManager.clear();
		User loaded = repository.findById(entity.getId());
		assertNotNull(loaded.getId());
		repository.delete(entity);
		assertPersistedEntity(loaded, entity);
	}

}
