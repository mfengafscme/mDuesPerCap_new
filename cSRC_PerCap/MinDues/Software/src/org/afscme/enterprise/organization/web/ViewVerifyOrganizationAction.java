package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;

/**
 * View the verify organization name, based on criteria in the verifyOrganizationForm.
 *
 * @struts:action   path="/viewVerifyOrganization"
 *                  name="verifyOrganizationForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="viewVerify"  path="/Membership/VerifyOrganization.jsp"
 */
public class ViewVerifyOrganizationAction extends AFSCMEAction {

    /** Creates a new instance of ViewVerifyOrganizationAction */
    public ViewVerifyOrganizationAction() {
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

        VerifyOrganizationForm voForm = (VerifyOrganizationForm)form;

        //reset the page parms
        voForm.setPage(0);
        voForm.setTotal(0);
        voForm.setSortBy("orgName");
        voForm.setOrder(1);
        return mapping.findForward("viewVerify");
    }

}
