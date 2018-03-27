
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
 * Searches for affiliates, based on criteria in the selectUserAffiliatesSearchForm.
 * Params:
 *      new - if present, the user is shown the search form.
 * 
 * @struts:action   path="/viewStaffMaintainence"
 *					input="/Membership/AffiliateStaffMaintainence.jsp"
 *					name="staffMaintainenceForm"
 *					validate="true"
 *
 * @struts:action-forward   name="SearchResults"  path="/Membership/AffiliateStaffMaintainence.jsp"
 *
 */
public class ViewStaffMaintainanceAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer affPk = getCurrentAffiliatePk(request);
        
        StaffMaintainenceForm smForm = (StaffMaintainenceForm)form;
        List results = new LinkedList();
        int total = s_maintainAffiliateStaff.getAffiliateStaff(affPk, smForm.getSortData(), results);
        smForm.setResults(results);
        smForm.setTotal(total);
        
        //show the results page
		return mapping.findForward("SearchResults");
	}
}
