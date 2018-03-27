package org.afscme.enterprise.controller.web;

import javax.servlet.*;
import javax.servlet.http.*;
import org.afscme.enterprise.util.ConfigUtil;

/**
 * Loaded at application startup.  Performs initialized needed by the web application
 */
public class InitializationServlet extends HttpServlet {

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ConfigUtil.init();
	}
	
}
