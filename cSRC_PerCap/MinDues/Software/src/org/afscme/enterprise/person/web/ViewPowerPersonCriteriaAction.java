package org.afscme.enterprise.person.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;

import org.afscme.enterprise.person.web.SearchPersonForm;

/**
 * Display the Power Person Search page, based on criteria in the searchPersonForm, if any exists.
 * However, reset page and total to zero in preparation of doing a new search
 *
 * @struts:action   path="/viewPowerPersonCriteria"
 *                  name="searchPersonForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="viewSearch"  path="/Membership/PowerPersonSearch.jsp"
 */
public class ViewPowerPersonCriteriaAction extends AFSCMEAction {

    /** Creates a new instance of ViewPowerMemberCriteriaAction */
    public ViewPowerPersonCriteriaAction() {
    }

    /**
     * Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

       SearchPersonForm personForm = (SearchPersonForm)form;

        //reset the page parms
        personForm.setPage(0);
        personForm.setTotal(0);
        personForm.setSearchPage(1); 
        
        // handled in ejb
        return mapping.findForward("viewSearch");

    }

}
