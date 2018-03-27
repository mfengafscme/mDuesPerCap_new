
package org.afscme.enterprise.controller.web;

import javax.servlet.ServletException;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Removes the users session and sends them to the welcome page.
 *
 * @struts:action  path="/logout"
*/
public class LogoutAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession(false);
		if (session != null)
			session.invalidate();
		
		return mapping.findForward("Welcome");
	}
}
