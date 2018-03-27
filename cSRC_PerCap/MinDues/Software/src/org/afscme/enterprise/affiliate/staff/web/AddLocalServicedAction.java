
package org.afscme.enterprise.affiliate.staff.web;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.afscme.enterprise.affiliate.*;
import org.afscme.enterprise.users.web.SelectUserAffiliatesSearchForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.util.CollectionUtil;


/**
 * @struts:action   path="/addLocalServiced"
 *					input="/Membership/AddLocalServiced.jsp"
 *					name="addLocalServicedForm"
 *                  scope="session"
 *					validate="false"
 *
 * @struts:action-forward   name="View"  path="/viewAffiliateStaff.action" redirect="true"
 * @struts:action-forward   name="Results"  path="/Membership/AddLocalServicedSearchResults.jsp"
 *
 */
public class AddLocalServicedAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        if (isCancelled(request))
    		return mapping.findForward("View");
        
        Integer personPk = getCurrentPersonPk(request);
        Integer affPk = getCurrentAffiliatePk(request);

        if (request.getParameter("affPk") != null) {
            s_maintainAffiliateStaff.addLocalServiced(affPk, personPk, Integer.valueOf(request.getParameter("affPk"))); 
    		return mapping.findForward("View");
        }
        
        AddLocalServicedForm alsForm = (AddLocalServicedForm)form;
        AffiliateCriteria ac = alsForm.getAffiliateCriteria();
        Collection results = s_maintainAffiliates.searchAffiliates(ac);
        
        if (CollectionUtil.isEmpty(results)) {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noResultsFound"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } else if (results.size() == 1)  {
            AffiliateResult ar = (AffiliateResult)results.toArray()[0];
            s_maintainAffiliateStaff.addLocalServiced(affPk, personPk, ar.getAffPk());
    		return mapping.findForward("View");
        } else {
            alsForm.setResults(results);
            if (alsForm.getTotal() < 1)
                alsForm.setTotal(s_maintainAffiliates.getSearchAffiliatesCount(ac));
    		return mapping.findForward("Results");
        }
    }
}
