package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.chare.infrastructure.EnvironmentConfig;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.User;
import com.chare.mcb.service.AuthenticationService;
import com.chare.mcb.service.UserPreferences;
import com.chare.test.SpringTestCase;
import com.chare.wicket.ExternalLink;
import com.chare.wicket.MountList;
import com.chare.wicket.TextField;

public abstract class WicketTestCase implements Serializable {

	protected static final String COMPONENT_ID = "component";

	protected WicketTester tester;
	protected ConfigurableApplicationContext applicationContext;
	protected WebApplication application;

	protected UserPreferences userPreferences;
	protected User user;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		applicationContext = createApplicationContext();
		SpringTestCase.addSessionScope(applicationContext);

		user = createUser();
		userPreferences = setupSecurityContext(applicationContext.getBean(UserPreferences.class), user);

		application = createApplication(applicationContext);
		tester = createTester(application);
	}

	public static UserPreferences setupSecurityContext(UserPreferences userPreferences, User user) {
		userPreferences.setUser(user);
		return userPreferences;
	}

	protected User createUser() {
		User user = new User(null, "username");
		List<Role> roles = createUserRoles();
		for (Role role : roles) {
			user.addRole(role);
		}
		return user;
	}

	protected List<Role> createUserRoles() {
		return new ArrayList<Role>();
	}

	protected void authenticateUser() {
		user.setId(1);
	}

	@After
	public void tearDown() {
		tester.destroy();
		applicationContext.close();
	}

	private WicketTester createTester(WebApplication application) {
		return new WicketTester(application);
	}

	//	protected void authorizeUser(Role role) {
	//		AuthorizationService authorizationService = applicationContext.getBean(AuthorizationService.class);
	//		when(authorizationService.isAuthorized(any(User.class), eq(role))).thenReturn(true);
	//	}

	protected Class<?> getCustomConfig() {
		return PageConfig.class;
	}

	protected Class<?>[] getConfigs() {
		return new Class<?>[] { WebApplicationConfig.class, getCustomConfig() };
	}

	private ConfigurableApplicationContext createApplicationContext() {
		return new AnnotationConfigApplicationContext(getConfigs());
	}

	protected WebApplication createApplication(ApplicationContext applicationContext) {
		return new A(applicationContext);
	}

	protected String getPath(String... componentIds) {
		return StringUtils.join(componentIds, Component.PATH_SEPARATOR);
	}

	protected Component getComponent(String id) {
		return tester.getComponentFromLastRenderedPage(id);
	}

	protected void assertComponentIs(Class<? extends Component> clazz, String id) {
		Component component = getComponent(id);
		assertTrue("Expected " + clazz.getName() + " found " + (component == null ? "null" : component.getClass()), clazz.isAssignableFrom(component.getClass()));
	}

	protected void assertTextField(String id) {
		assertComponentIs(TextField.class, id);
	}

	protected void assertExternalLink(String id) {
		assertComponentIs(ExternalLink.class, id);
	}

	protected void assertCheckBox(String id) {
		assertComponentIs(CheckBox.class, id);
	}

	//	protected void loginUser(User user) {
	//		getUserPreferences().setUser(user);
	//	}

	//	protected UserPreferences getUserPreferences() {
	//		return applicationContext.getBean(com.chare.service.Config.USER_PREFERENCES_SESSION_ID, UserPreferences.class);
	//	}

	protected <C extends Page> void assertPageIsRendered(Class<C> pageClass) {
		tester.setFollowRedirects(false);
		tester.startPage(pageClass);

		tester.assertNoErrorMessage();
		tester.assertRenderedPage(pageClass);

	}

	protected <C extends Component> void assertContainsComponent(Component parent, Class<C> componentClass, String ... pathIds) {
		String path = convertComponentIdsToPath(pathIds);
		assertNotNull(parent.get(path).getClass().asSubclass(componentClass));
	}

	public static String convertComponentIdsToPath(String... pathIds) {
		String path = "";
		for (String id : pathIds) {
			path += Component.PATH_SEPARATOR + id;
		}
		return path.substring(1);
	}

	public static void assertPageIsSecured(Class<? extends Page> pageClass) {
		assertNotNull(AuthorizationStrategy.isSecured(pageClass));
	}

	@Configuration
	protected static class PageConfig {
	}

	protected void assertContainsWords(String... words) {
		for (String word : words)
			tester.assertContains(word);
	}

	protected void assertPageRenderedWithNoErrorOrInfoMessages(Class<? extends Page> pageClass) {
		tester.assertNoErrorMessage();
		tester.assertNoInfoMessage();
		tester.assertRenderedPage(pageClass);
	}

	public static class WebApplicationConfig {

		static final String CONTEXT_DIR = "src/main/webapp/";

		@Bean
		public String contextUrl() {
			return "/";
		}

		@Bean
		public String servletPath() {
			return contextUrl() + "servlet/";
		}

		@Bean
		public String configurationFilename() {
			return "conf/configuration_test.ini";
		}

		@Bean
		public String loggerConfigurationFilename() {
			return configurationFilename();
		}

		@Bean
		@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public UserPreferences userPreferences() {
			return new UserPreferences();
		}
		//		@Bean
		//		public TitleSources titleSources() {
		//			TitleSources titleSources = new TitleSources();
		//			titleSources.put("en", new TitleProperties("en", new Properties()));
		//			titleSources.put("de", new TitleProperties("de", new Properties()));
		//			titleSources.put("cz", new TitleProperties("cz", new Properties()));
		//			return titleSources;
		//		}

		@Bean(name = EnvironmentConfig.BUILD_PROPERTIES)
		public Properties buildProperties() {
			return new Properties();
		}

		@Bean(name = com.chare.mcb.www.Config.MODULE_NAME)
		public MountList mountList() {
			return new com.chare.mcb.www.MountList();
		}

		@Bean
		public AuthenticationService authenticationService() {
			return mock(AuthenticationService.class);
		}

	}


}
