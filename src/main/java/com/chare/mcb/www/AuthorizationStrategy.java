package com.chare.mcb.www;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.component.IRequestableComponent;

import com.chare.mcb.entity.User;
import com.chare.mcb.service.UserPreferences;

class AuthorizationStrategy implements IAuthorizationStrategy {
	private final UserPreferences userPreferences;

	public AuthorizationStrategy(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	@Override
	public boolean isActionAuthorized(Component component, Action action) {
		return true;
	}

	@Override
	public <T extends IRequestableComponent>boolean isInstantiationAuthorized(Class<T> componentClass) {
		if (isSecured(componentClass)) {
			User user = userPreferences.getUser();
			if (!user.isAuthenticated())
				throw new RestartResponseAtInterceptPageException(LoginPage.class);

			String [] requiredRoles = componentClass.getAnnotation(Secured.class).required();
			String [] requiredAnyOfRoles = componentClass.getAnnotation(Secured.class).requiredAnyOf();

			if (!(user.hasRoles(requiredRoles) && user.hasAnyOfRoles(requiredAnyOfRoles)))
				throw new RestartResponseAtInterceptPageException(MenuPage.class);//FIXME message you are not authorized or unauthorized page
		}
		return true;
	}

	static <T extends IRequestableComponent> boolean isSecured(Class<T> componentClass) {
		return componentClass.getAnnotation(Secured.class) != null;
	}

}