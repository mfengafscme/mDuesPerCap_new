package org.afscme.enterprise.controller.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.AttemptedSecurityViolation;
import org.afscme.enterprise.controller.UserSecurityData;

import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Takes the use out of 'data utility' mode, and puts them in AFSCME International User mode.
 *
 * @struts:action   path="/leaveDataUtility"
 */
public class LeaveDataUtilityAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
                
                HttpSession session = request.getSession();
        
		if (usd.getDepartment() == null)
			throw new AttemptedSecurityViolation(usd, "User attempted to act as AFSCME Staff ");
		
		usd.setActingAsAffiliate(null);
                usd.setAffiliateName(null);
                usd.setAccessibleAffiliates(null);
		
                // clean up forms
                session.removeAttribute("memberAffiliateInformationForm");
                session.removeAttribute("memberDetailForm");
                session.removeAttribute("memberDetailAddForm");    
                session.removeAttribute("searchMembersForm");
                
		return mapping.findForward("MainMenu");
	}
}
