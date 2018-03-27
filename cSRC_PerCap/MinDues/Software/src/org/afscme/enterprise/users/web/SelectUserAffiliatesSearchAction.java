
package org.afscme.enterprise.users.web;

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
 * @struts:action   path="/selectUserAffiliatesSearch"
 *					input="/Admin/SelectUserAffiliatesSearch.jsp"
 *					name="selectUserAffiliatesSearchForm"
 *					validate="true"
 *                  scope="session"
 *
 * @struts:action-forward name="SearchForm" path="/Admin/SelectUserAffiliatesSearch.jsp"
 * @struts:action-forward name="SearchResults" path="/Admin/SelectUserAffiliatesSearchResults.jsp"
 * @struts:action-forward name="Cancelled" path="/editUser.action"
 *
 */
public class SelectUserAffiliatesSearchAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        if (isCancelled(request))
            return mapping.findForward("Cancelled");
        
		SelectUserAffiliatesSearchForm sasForm = (SelectUserAffiliatesSearchForm)form;
        Integer personPk = getCurrentPersonPk(request);

        //if the request contained the 'new' parameter, don't perform the searhc, just show the search form.
		if (request.getParameter("new") != null) {
			sasForm.newSearch();
			return mapping.findForward("SearchForm");
		}

        //get the search criteria from the form
		AffiliateData data = sasForm.getAffiliateData();
		AffiliateSortData sortData = sasForm.getAffiliateSortData();
		List result = new LinkedList();

        //perform the search
		int count = s_maintainUsers.getAffiliates(personPk, data, sortData, result);
		if (count == 0)
            return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.noResults");
		
        //put the search result in the form
		sasForm.setResults(result);
		sasForm.setTotal(count);
        
        //show the results page
		return mapping.findForward("SearchResults");
	}
}
