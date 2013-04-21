package com.chare.mcb;

import static com.chare.test.DbTestCase.*;
import static com.chare.test.SpringTestCase.*;

import java.io.File;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import com.chare.core.Utils;
import com.chare.infrastructure.EnvironmentConfig;
import com.chare.infrastructure.JndiDataSourceConfig;

public class ConfigTest {


	protected static final String TEST_CONFIGURATION = "conf/configuration_test.ini";

	@Test
	public void testImported() throws Exception {
		Class<?>[] configs = Config.class.getAnnotation(Import.class).value();
		assertImportedConfig(configs, com.chare.mcb.ApplicationConfig.class);
		assertImportedConfig(configs, com.chare.mcb.ContextConfig.class);
		assertImportedConfig(configs, com.chare.infrastructure.EnvironmentConfig.class);
		assertImportedConfig(configs, com.chare.infrastructure.JndiDataSourceConfig.class);
		assertImportedConfig(configs, com.chare.mcb.repository.Config.class);
		assertImportedConfig(configs, com.chare.mcb.ApplicationConfig.class);
		assertImportedConfig(configs, com.chare.infrastructure.scheduler.Config.class);
		assertImportedConfig(configs, com.chare.service.audit.Config.class);

		assertImportedConfig(configs, com.chare.mcb.infrastructure.Config.class);
		assertImportedConfig(configs, com.chare.mcb.service.Config.class);
		assertImportedConfig(configs, com.chare.mcb.www.Config.class);

	}

	@Test
	public void testConfig() throws Exception {
		SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		builder.bind(JndiDataSourceConfig.createContextDatasourceName(Application.name), createDataSource(createConfiguration(getConfigurationFilename())));
		AbstractApplicationContext context = createContext(Config.class, OverrideConfig.class);
		context.close();
	}

	@Configuration
	static class OverrideConfig {
		@Bean(name = EnvironmentConfig.UPDATE_DIRECTORY)
		public File updateDirectory() {
			return new File(Utils.getTemporaryDirectory());
		}

	}
}
