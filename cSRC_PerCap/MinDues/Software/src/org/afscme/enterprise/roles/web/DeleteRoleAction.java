
package org.afscme.enterprise.roles.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.roles.ejb.MaintainPrivileges;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * Delets a role.
 *
 * Params:
 *      pk - Primary key of the role to delete
 *
 * @struts:action   path="/deleteRole"
 */
public class DeleteRoleAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

		String pk = request.getParameter("pk");

		s_maintainPrivileges.deleteRole(Integer.valueOf(pk));
		
		return mapping.findForward("ListRoles");
	}
}
