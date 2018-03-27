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
 * Verify organizations, based on criteria in the verifyOrganizationForm.
 *
 * @struts:action   path="/verifyOrganization"
 *                  input="/Membership/VerifyOrganization.jsp"
 *                  name="verifyOrganizationForm"
 *                  validate="true"
 *                  scope="session"
 *
 * @struts:action-forward name="VerifyForm" path="/Membership/VerifyOrganization.jsp"
 * @struts:action-forward name="VerifyResults" path="/Membership/OrganizationVerifyResults.jsp"
 * @struts:action-forward name="AddNew" path="/addOrganizationDetail.action"
 */
public class VerifyOrganizationAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        VerifyOrganizationForm vosForm = (VerifyOrganizationForm)form;
        
        //if the request contained the 'new' parameter, don't perform the search, just show the search form.
        if (request.getParameter("new") != null) {
            vosForm.newSearch();
            return mapping.findForward("VerifyForm");
        }
        
        //get the search criteria from the form
        OrganizationCriteria data = vosForm.getOrganizationCriteriaData();
        List result = new LinkedList();
        
        if (s_maintainOrganizations.isDuplicateOrgs(data.getOrgName())) {
            
            int count = s_maintainOrganizations.getDuplicateOrgs(data.getOrgName(), data, result);
        
            //put the search result in the form
            vosForm.setResults(result);
            vosForm.setTotal(count);
        
            //show the results page
            return mapping.findForward("VerifyResults");      
        }

        //show the organization add new page if no duplicate name found
        return mapping.findForward("AddNew");            
        

    }
}
