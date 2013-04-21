package com.chare.mcb.www;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.RequestHandlerStack.ReplaceHandlerException;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;
import org.springframework.context.i18n.LocaleContextHolder;

import com.chare.mcb.Application;
import com.chare.mcb.entity.User;
import com.chare.mcb.service.AuthenticationService;

public final class LoginPage extends PageWithMenu {

	public static final String LOGIN_FORM_ID = "loginForm";

	@SpringBean
	private AuthenticationService authenticationService;

	public LoginPage(PageParameters pageParameters) {
		super(pageParameters);
		if (pageParameters != null && !pageParameters.get("username").isEmpty()) {
			// try to login user
			String username = pageParameters.get("username").toString();
			String passwd = pageParameters.get("passwd").toString();
			login(username, passwd);
		}
		add(new Label("lbl_masterCard", new ResourceModel("MCB", "Master Card Bratislava")));
		add(new Label("lbl_login", new ResourceModel("login.title")));
		add(new Label("lbl_welcome", new ResourceModel("login.welcome")));
		add(new Label("lbl_support", new ResourceModel("login.support")));
		add(new LoginForm(LOGIN_FORM_ID));
		add(new FeedbackPanel("feedback"));
	}


	protected final class LoginForm extends Form<Void> {
		static final String PASSWORD_ID = "password";
		static final String USERNAME_ID = "username";
		// El-cheapo model for form
		private final ValueMap properties = new ValueMap();

		public LoginForm(final String id) {
			super(id);

			ResourceModel usernameLabelModel = new ResourceModel("login.username");
			Label usernameLabel = new Label("lbl_userName", usernameLabelModel);
			add(usernameLabel);
			FormComponent<String> username = new RequiredTextField<String>(USERNAME_ID, new PropertyModel<String>(properties, USERNAME_ID))
					.setType(String.class)
					.setLabel(usernameLabelModel);
			add(username);
			ResourceModel passwordLabelModel = new ResourceModel("login.password");
			Label passwordLabel = new Label("lbl_password", passwordLabelModel);
			add(passwordLabel);
			FormComponent<String> password = new PasswordTextField(PASSWORD_ID, new PropertyModel<String>(properties, PASSWORD_ID))
			.setType(String.class)
			.setLabel(passwordLabelModel);
			add(password);
		}

		@Override
		public final void onSubmit() {
			// Get session info

			String username = properties.getString(USERNAME_ID);
			if (StringUtils.isBlank(username)) {
				String errmsg = getLocalizer().getString("login.error_username", this, "Username not entered");
				error(errmsg);
				return;
			}
			String password = properties.getString(PASSWORD_ID);
			login(username, password);
		}
	}

	private void login(String username, String password) {
		try {
			User user = authenticationService.authenticate(username, password,  getUserIpAddress());
			userPreferences.setUser(user);
			LocaleContextHolder.setLocale(user.getLocale());
			getSession().setLocale(user.getLocale());
			redirectUser(user);
		} catch (RedirectToUrlException e) {
			throw e;
		} catch (ReplaceHandlerException e) {
			throw e;
		} catch (IllegalStateException e) {
			displayLoginError();
		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Login error " + e.getMessage(), e);
			displayLoginError();
		}
	}

	private void displayLoginError() {
		error(new ResourceModel("login.failed", "Can not log you in").getObject());
	}

	private String getUserIpAddress() {
		return ((HttpServletRequest) getRequest().getContainerRequest()).getRemoteAddr();
	}

	private void redirectUser(User user) {
		if (continueToOriginalDestination())
			return;
		String url = (getPageParameters() != null && !getPageParameters().get("url").isEmpty()
				? getPageParameters().get("url").toString() : null);

		if (url == null)
			url = "/" + Application.MENU_PAGE;
		throw new RedirectToUrlException(url);
	}

	@Override
	protected String getTitleCode() {
		return "login.title";
	}

	@Override
	public void renderHead(IHeaderResponse headerResponse) {
		headerResponse.renderOnLoadJavaScript("if (window != top) top.location.href = location.href;");// + browser.location);");
	}
}
