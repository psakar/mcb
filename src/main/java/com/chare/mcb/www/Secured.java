package com.chare.mcb.www;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To require only that the user is authenticed and to not to check his roles leave both lists empty. <br/>
 * eg. <br/> @Secured(required = {}, requiredAnyOf = {})<br/>
 * public class MenuPage
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Secured {
	/**
	 * Returns the list of roles (e.g. USER_ADMIN, APP_ADMIN) which all user must have
	 *
	 * @return String[] roles
	 */
	public String[] required();
	/**
	 * Returns the list of roles (e.g. USER_ADMIN, APP_ADMIN) which at least one of them user must have
	 *
	 * @return String[] roles
	 */
	public String[] requiredAnyOf();

}
