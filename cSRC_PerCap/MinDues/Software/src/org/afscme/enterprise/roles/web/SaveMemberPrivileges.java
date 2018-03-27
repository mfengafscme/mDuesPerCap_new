
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
 * Handles the submit from the member privileges page.
 * 
 * @struts:action   path="/saveMemberPrivileges"
 *					name="memberPrivilegesForm"
 *                  scope="request"
 */
public class SaveMemberPrivileges extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

		MemberPrivilegesForm mpForm = (MemberPrivilegesForm)form;
		
		if (!isCancelled(request))
			s_maintainPrivileges.setMemberPrivileges(usd.getActingAsAffiliate(), mpForm.getData());

		return mapping.findForward("MainMenu");
	}
}
