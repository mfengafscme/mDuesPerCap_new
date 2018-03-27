
package org.afscme.enterprise.affiliate.staff.web;

import java.util.List;
import java.util.LinkedList;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.users.web.SelectUserAffiliatesSearchForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.controller.UserSecurityData;


/**
 * @struts:action   path="/removeLocalServiced"
 *
 * @struts:action-forward   name="Done"  path="/viewAffiliateStaff.action" redirect="true"
 *
 */
public class RemoveLocalServicedAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer affPk = getCurrentAffiliatePk(request);
        Integer personPk = getCurrentPersonPk(request);
        Integer localPk = Integer.valueOf(request.getParameter("localPk"));
        s_maintainAffiliateStaff.removeLocalServiced(affPk, personPk, localPk);
        
		return mapping.findForward("Done");
	}
}
