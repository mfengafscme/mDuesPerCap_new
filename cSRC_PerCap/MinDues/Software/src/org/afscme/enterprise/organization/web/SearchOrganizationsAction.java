package org.afscme.enterprise.organization.web;

import java.util.List;
import java.util.LinkedList;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.organization.web.SearchOrganizationsForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.OrganizationCriteria;
import org.afscme.enterprise.organization.OrganizationResult;
import org.afscme.enterprise.controller.UserSecurityData;


/**
 * Searches for organizations, based on criteria in the searchOrganizationForm.
 * Params:
 *      new - if present, the user is shown the empty search form.
 *
 * @struts:action   path="/searchOrganizations"
 *                  input="/Membership/OrganizationSearch.jsp"
 *                  name="searchOrganizationsForm"
 *                  validate="true"
 *                  scope="session"
 *
 * @struts:action-forward name="SearchForm" path="/Membership/OrganizationSearch.jsp"
 * @struts:action-forward name="SearchResults" path="/Membership/OrganizationSearchResults.jsp"
 * @struts:action-forward name="OrgDetail" path="/viewOrganizationDetail.action"
 */
public class SearchOrganizationsAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        SearchOrganizationsForm sosForm = (SearchOrganizationsForm)form;
        
        //if the request contained the 'new' parameter, don't perform the search, just show the search form.
        if (request.getParameter("new") != null) {
            sosForm.newSearch();
            return mapping.findForward("SearchForm");
        }
        
        //get the search criteria from the form
        OrganizationCriteria data = sosForm.getOrganizationCriteriaData();
        List result = new LinkedList();
        
        //perform the search
        int count = s_maintainOrganizations.searchOrgs(data, result);
        
        //show the correct page
        switch (count) {
            case 0:
                // go back to the search form
                return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.noResultsFound");
            case 1:
                //1 result - select that organization
                Integer orgPk = ((OrganizationResult)result.get(0)).getOrgPK();
                setCurrentOrganization(request, orgPk);
                return mapping.findForward("OrgDetail");
            default:
                //put the search result in the form
                sosForm.setResults(result);
                sosForm.setTotal(count);
                return mapping.findForward("SearchResults");
        }                
    }
}
