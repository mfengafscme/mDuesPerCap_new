package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;

/**
 * View the Search organizations, based on criteria in the searchOrganizationsForm.
 *
 * @struts:action   path="/viewOrganizationCriteria"
 *                  name="searchOrganizationsForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="viewSearch"  path="/Membership/OrganizationSearch.jsp"
 */
public class ViewOrganizationCriteriaAction extends AFSCMEAction {

    /** Creates a new instance of ViewOrganizationCriteriaAction */
    public ViewOrganizationCriteriaAction() {
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

        SearchOrganizationsForm sosForm = (SearchOrganizationsForm)form;

        //reset the page parms
        sosForm.setPage(0);
        sosForm.setTotal(0);
        sosForm.setSortBy("orgName");
        sosForm.setOrder(1);
        return mapping.findForward("viewSearch");
    }

}
