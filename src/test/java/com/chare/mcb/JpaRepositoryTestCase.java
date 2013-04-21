package com.chare.mcb;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.chare.core.Db;
import com.chare.core.Utils;
import com.chare.entity.EntityWithId;
import com.chare.repository.JpaRepository;
import com.chare.repository.JpaRepositoryImpl;
import com.chare.repository.Restriction;

public abstract class JpaRepositoryTestCase<PK, Type extends EntityWithId<PK>, RepositoryType extends JpaRepository<PK, Type>> extends JpaTransactionalTestCase {

	protected RepositoryType repository;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		repository = getRepository();
	}

	protected abstract RepositoryType getRepository();
	protected abstract String getTableName();

	protected abstract Type setupEntity(Type entity);
	protected void assertPersistedEntity(Type findById, Type entity) {}

	protected Type create() throws Exception {
		ParameterizedType genericSuperclass = (ParameterizedType) repository.getClass().getGenericSuperclass();
		@SuppressWarnings("unchecked")
		Class<Type> clazz = (Class<Type>) genericSuperclass.getActualTypeArguments()[1];
		return clazz.newInstance();
	}

	@Test
	public void testTransactional() throws Exception {
		Class<?> clazz = repository.getClass();
		assertPublicMethodsAreAnnotatedTransactional(clazz);
	}

	public static void assertPublicMethodsAreAnnotatedTransactional(
			Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			int modifiers = method.getModifiers();
			if (method.getDeclaringClass().equals(Object.class))
				continue;
			if (Modifier.isPublic(modifiers) && !(isSynthetic(modifiers)) && !(Modifier.isStatic(modifiers))) {
				if (method.getAnnotation(JpaRepositoryImpl.NotTransactional.class) != null)
					continue;
				if (method.getAnnotation(Transactional.class) == null)
					fail("Method " + method.getName() + "(" + ArrayUtils.toString(method.getParameterTypes()) + "), " + " is not annotated @Transactional " + ".Annotations " + ArrayUtils.toString(method.getAnnotations()));
			}
		}
	}

	public static boolean isSynthetic(int modifiers) {//created by compiler
		return (modifiers & 0x00001000) != 0;
	}

	@Test
	public void testPersist() throws Exception {
		Type entity = persistEntity();
		entityManager.flush();
		entityManager.clear();
		Type loaded = repository.findById(entity.getId());
		assertNotNull(loaded.getId());
		repository.delete(entity);
		assertPersistedEntity(loaded, entity);
	}

	@Test
	public void testDelete() throws Exception {
		Type entity = persistEntity();
		repository.delete(entity);
		assertNull(repository.findById(entity.getId()));
	}

	protected Type persistEntity() throws Exception {
		Type entity = create();
		setupEntity(entity);
		repository.persist(entity);
		assertNotNull(entity.getId());
		return entity;
	}


	@Test
	public void testFindingById() throws Exception {
		PK id = getMaxId();
		assertNotNull(id);
		Type foundEntity = repository.findById(id);
		assertNotNull(foundEntity);
		assertEquals(id, foundEntity.getId());
	}

	protected PK getMaxId() {
		return findId(getTableName());
		//return (PK) Db.dlookup(connectionProvider, sql, null, null, null);
	}

	protected String getPrimaryKeyColumnName() {
		return "id";
	}

	protected String getPrimaryKeyColumnType() {
		return configuration.getParameter("DB_TEST" , "DEFAULT_PK_TYPE", "int");
	}


	@Test
	public void testCount() throws Exception {
		int correct = Utils.isNull(Db.dlookup(connectionProvider, "SELECT count(*) FROM " + getTableName(), null, null, -1), -1);
		assertEquals(correct, repository.getCount(null));
	}

	@Test
	public void testFindingByRestrictions() throws Exception {
		PK id = getMaxId();
		assertNotNull(id);
		List<Type> list = repository.find(-1, -1, Arrays.asList(new Restriction("id", id, Restriction.EQUALS)), null);
		assertEquals(1, list.size());
		assertEquals(id, list.get(0).getId());
	}

	protected PK findId(String tableName) {
		String sql = configuration.getParameter("DB_TEST" , "SELECT_MAX", "SELECT convert({primaryKeyColumnType}, MAX({primaryKeyColumnName})) FROM {tableName}");
		sql = sql.replace("{primaryKeyColumnType}", getPrimaryKeyColumnType());
		sql = sql.replace("{primaryKeyColumnName}", getPrimaryKeyColumnName());
		sql = sql.replace("{tableName}", tableName);
		return lookup(sql);
	}

	protected void persistAndFlushAndClear(Type entity) {
		repository.persist(entity);
		flushAndClearEntityManager();
	}

}
