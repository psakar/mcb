package com.chare.mcb.repository;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.AbstractApplicationContext;

import com.chare.mcb.entity.User;
import com.chare.mcb.infrastructure.ValidatorFactoryCreator;
import com.chare.test.SpringTestCase;

public class ValidatorTest {

	ValidatorFactory validatorFactory;
	private AbstractApplicationContext context;

	@Before
	public void setup() throws Exception {
		context = SpringTestCase.createContext(DependencyConfig.class);
		validatorFactory = new ValidatorFactoryCreator().create(context);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
	}

	@After
	public void after() throws Exception {
		if (context != null)
			context.close();
	}


	@Test
	public void testUserValidation() throws Exception {
		User entity = new User();
		assertNumberOfViolations(entity, 3);
		entity.username = "username";
		assertNumberOfViolations(entity, 2);
		entity.name = "tooLongName1tooLongName1tooLongName1tooLongName1";
		entity.surname = "tooLongName2tooLongName2tooLongName2tooLongName2";
		assertNumberOfViolations(entity, 3);
	}


	private <T> Set<ConstraintViolation<T>> assertNumberOfViolations(T entity,
			int number) {
		Set<ConstraintViolation<T>> constraintViolations = validatorFactory.getValidator().validate(entity);
		assertEquals(number, constraintViolations.size());
		return constraintViolations;
	}

	@Configuration
	static class DependencyConfig {

	}
}
