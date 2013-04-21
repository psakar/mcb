package com.chare.mcb.www;

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.IRequestCycleProvider;
import org.apache.wicket.Page;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.cycle.RequestCycleContext;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.chare.core.Utils;
import com.chare.infrastructure.EnvironmentConfig;
import com.chare.mcb.entity.User;
import com.chare.mcb.service.UserPreferences;
import com.chare.service.AuditService;
import com.chare.spring.SpringUtils;
import com.chare.wicket.MountList.PageMount;

public class WebApplication extends org.apache.wicket.protocol.http.WebApplication {

	private final static Log log = LogFactory.getLog(WebApplication.class);
	//	private String contextPath;

	//	@Autowired
	//	TitleSources titleSources;

	@Autowired
	@Qualifier(EnvironmentConfig.BUILD_PROPERTIES)
	private Properties buildProperties;

	@Autowired
	private UserPreferences userPreferences;

	@Autowired
	private List<com.chare.wicket.MountList> mountLists;

	//@Autowired
	//private WicketValidationConverter wicketValidationConverter;

	//	public User getUser() {
	//		return userPreferences.getUser();
	//	}

	public WebApplication() {
		super();
		log.debug("Web application created");
	}

	@Override
	protected void init() {
		super.init();

		setupDependencyInjection();
		setupWebApplication();
		mountPages();

		log.info("Web application started version " + Utils.getBuildInfo(buildProperties));
	}

	private void setupWebApplication() {
		setRequestCycleProvider(new RequestCycleProvider(userPreferences));
		getDebugSettings().setAjaxDebugModeEnabled(false);

		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setDefaultMarkupEncoding("utf-8");
		getResourceSettings().setThrowExceptionOnMissingResource(false);
		//		getResourceSettings().getStringResourceLoaders().add(new StringResourceLoader());

		getSecuritySettings().setAuthorizationStrategy(new AuthorizationStrategy(userPreferences));

	}

	private void setupDependencyInjection() {
		ApplicationContext applicationContext = getApplicationContext();
		SpringUtils.injectDependencies(applicationContext, this);
		getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext, true));
	}

	protected ApplicationContext getApplicationContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}


	private void mountPages() {
		for (com.chare.wicket.MountList mountList : mountLists) {
			mountList.mount(new PageMount(this));
		}
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return MenuPage.class;
	}

	static class RequestCycleProvider implements IRequestCycleProvider {

		private final UserPreferences userPreferences;

		public RequestCycleProvider(UserPreferences userPreferences) {
			this.userPreferences = userPreferences;
		}

		@Override
		public RequestCycle get(RequestCycleContext context) {
			return new RequestCycle(context) {
				@Override
				protected void onBeginRequest() {
					//FIXME requestManager.requestStarted()
					User user = userPreferences.getUser();
					AuditService.setLoggerContext(user.getUsername());
					LocaleContextHolder.setLocale(user.getLocale());
				}

				@Override
				protected void onEndRequest() {
					//FIXME requestManager.requestEnded()
					AuditService.clearLoggerContext();
					LocaleContextHolder.setLocale(null);
				}
			};
		}
	}
}
