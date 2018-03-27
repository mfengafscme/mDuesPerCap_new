
package org.afscme.enterprise.controller.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.AttemptedSecurityViolation;
import org.afscme.enterprise.controller.UserSecurityData;




/**
 * Selects the affiliate the user will start using the Data Utility as.
 *
 * @struts:action   path="/selectAffiliate"
 */
public class SelectAffiliateAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

		Integer affiliatePk = Integer.valueOf(request.getParameter("pk"));

        //check if the user tried to hack their way in with URL re-writing.
        if (!s_maintainUsers.getAffiliates(usd.getPersonPk()).contains(affiliatePk))
			throw new AttemptedSecurityViolation(usd, "User attempted act as affilaite with primary key: " + affiliatePk);

        //make use act as the selected affiliate
		usd.setActingAsAffiliate(affiliatePk);
        usd.setAffiliateName(s_accessControl.getAffiliateName(affiliatePk));
        usd.setAccessibleAffiliates(s_accessControl.getAccessibleAffiliates(affiliatePk));
		return mapping.findForward("MainMenu");
	}
}
