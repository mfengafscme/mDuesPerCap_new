
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
import org.afscme.enterprise.roles.*;

/**
 * Retrieves member privileges for the affiliate and displays the edit member privileges page.
 * 
 * @struts:action   path="/editMemberPrivileges"
 *					name="memberPrivilegesForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="Edit"  path="/Admin/EditMemberPrivileges.jsp"
 */
public class EditMemberPrivilegesAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

		MemberPrivilegesForm mpForm = (MemberPrivilegesForm)form;
		
		mpForm.setData(s_maintainPrivileges.getMemberPrivileges(usd.getActingAsAffiliate()));
		return mapping.findForward("Edit");
	}
}
