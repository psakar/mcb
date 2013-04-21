package com.chare.mcb.infrastructure;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.Configuration;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocaleContextMessageInterpolator;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import com.chare.core.CustomLocale;
import com.chare.core.XmlResourceBundlesReader;

public class ValidatorFactoryCreator {

	public ValidatorFactory create(ApplicationContext applicationContext) throws Exception {
		Configuration<?> configuration = Validation.byDefaultProvider().configure();
		final Map<Locale, ResourceBundle> resourceBundles = readUserResourceBundles();
		ResourceBundleLocator resourceBundleLocator = new ResourceBundleLocator() {
			@Override
			public ResourceBundle getResourceBundle(Locale locale) {
				if (!resourceBundles.containsKey(locale))
					return resourceBundles.values().iterator().next();
				return resourceBundles.get(locale);
			}
		};
		MessageInterpolator messageInterpolator = new LocaleContextMessageInterpolator(new ResourceBundleMessageInterpolator(resourceBundleLocator));

		return configuration
				.messageInterpolator(messageInterpolator)
				.constraintValidatorFactory(new SpringConstraintValidatorFactory(applicationContext.getAutowireCapableBeanFactory()))
				.buildValidatorFactory();
	}

	static Map<Locale, ResourceBundle> readUserResourceBundles() throws IOException {
		String baseName = "/" + ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES;
		Map<Locale, ResourceBundle> resourceBundles = new XmlResourceBundlesReader().readResourceBundles(baseName, Locale.ENGLISH, Locale.GERMAN, CustomLocale.LOCALE_CZ);
		return resourceBundles;
	}

	// For tests
	public ValidatorFactory create() throws Exception {
		Configuration<?> configuration = Validation.byDefaultProvider().configure();
		final Map<Locale, ResourceBundle> resourceBundles = readUserResourceBundles();
		ResourceBundleLocator resourceBundleLocator = new ResourceBundleLocator() {
			@Override
			public ResourceBundle getResourceBundle(Locale locale) {
				if (!resourceBundles.containsKey(locale))
					return resourceBundles.values().iterator().next();
				return resourceBundles.get(locale);
			}
		};
		MessageInterpolator messageInterpolator = new ResourceBundleMessageInterpolator(resourceBundleLocator );

		return configuration
				.messageInterpolator(new LocaleContextMessageInterpolator(messageInterpolator))
				.buildValidatorFactory();
	}

}
