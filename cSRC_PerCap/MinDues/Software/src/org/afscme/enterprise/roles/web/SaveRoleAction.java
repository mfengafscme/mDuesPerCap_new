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
 * Handles the submits from the 'Add/Edit Role' page. 
 *
 * @struts:action   name="roleForm"
 *                  path="/saveRole"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Admin/EditRole.jsp"
 *
 * @struts:action-forward   name="SelectPrivileges"  path="/selectPrivileges.action" 
 * @struts:action-forward   name="SelectReportPrivileges"  path="/selectReportPrivileges.action"
 * @struts:action-forward   name="SelectFieldPrivileges"  path="/selectFieldPrivileges.action"
 */
public class SaveRoleAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
    {
        RoleForm roleForm = (RoleForm)form;
        Integer pk;

		//save the role info
        if (!isCancelled(request)) {
		   if (roleForm.getPk() == null || roleForm.getPk().intValue() == 0) {
				pk = s_maintainPrivileges.addRole(roleForm.getData()).getPk();
           } else {
                s_maintainPrivileges.updateRole(roleForm.getData());
                pk = roleForm.getPk();
            }
            request.setAttribute("pk", pk);
        }

		//go to the next screen
		if (roleForm.isSelectPrivilegesButton()) {
			return mapping.findForward("SelectPrivileges");
		} else if (roleForm.isSelectFieldPrivilegesButton()) {
			return mapping.findForward("SelectFieldPrivileges");
		} else if (roleForm.isSelectReportPrivilegesButton()) {
			return mapping.findForward("SelectReportPrivileges");
		} else {
			return mapping.findForward("ListRoles");
		}
	}
}
