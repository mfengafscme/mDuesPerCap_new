
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
 * Retrieves primary information about a role and displays the edit (or add) role page.
 * Params:
 *      add - if present, displays the add page.
 *      RoleForm - contains the pk of the role to display.
 *
 * @struts:action   path="/editRole"
 *					name="roleForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="Edit"  path="/Admin/EditRole.jsp"
 * @struts:action-forward   name="Add"  path="/Admin/EditRole.jsp?add"
 */
public class EditRoleAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

		RoleForm roleForm = (RoleForm)form;
		
        if (roleForm.getPk() != null) {
			roleForm.setData(s_maintainPrivileges.getRole(roleForm.getPk()));
	        return mapping.findForward("Edit");
        } else {
	        return mapping.findForward("Add");
		}

	}
}
