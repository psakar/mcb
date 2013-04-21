package com.chare.mcb.www;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

import com.chare.mcb.www.Config;
import com.chare.test.SpringTestCase;
import com.chare.wicket.MountList;

public class ConfigTest {

	@Test
	public void testConfig() {
		AbstractApplicationContext context = SpringTestCase.createContext(Config.class, DependenciesConfig.class);
		assertNotNull(context.getBean(Config.MODULE_NAME, MountList.class));
		context.close();
	}

	@Configuration
	static class DependenciesConfig {
	}
}
