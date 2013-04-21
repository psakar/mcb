package com.chare.mcb.www;

import org.springframework.context.ApplicationContext;

import com.chare.mcb.www.WebApplication;

/*to prevent bug in wicket tester during redirect to url resulting in exception in
 * wicketFilter.getRelativePath, where servletPath is webApplication.name and path is url from redirect ...
		String servletPath = request.getServletPath();
		path = path.substring(servletPath.length());
 */
class A extends WebApplication {
	private final ApplicationContext applicationContext;

	public A(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}