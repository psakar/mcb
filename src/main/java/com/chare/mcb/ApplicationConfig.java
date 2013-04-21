package com.chare.mcb;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Configuration;

import com.chare.infrastructure.Config;

@Configuration
public class ApplicationConfig extends Config {

	@Override
	public String getApplicationName() {
		return Application.name;
	}

	@Override
	public List<Locale> getApplicationLocales() {
		return Application.DEFAULT_LOCALES;
	}

	@Override
	public String getApplicationPackage() {
		return Application.class.getPackage().getName();
	}
}
