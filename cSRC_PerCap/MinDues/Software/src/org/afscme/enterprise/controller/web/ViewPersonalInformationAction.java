package org.afscme.enterprise.controller.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.AttemptedSecurityViolation;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * @struts:action   path="/viewPersonalInformation"
 * 				
 * @struts:action-forward name="View" path="/PersonalInformation/ViewPersonalInformation.jsp"
 */
public class ViewPersonalInformationAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
		return mapping.findForward("View");
	}
}
