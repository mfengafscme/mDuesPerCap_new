package org.afscme.enterprise.member.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.member.web.SearchVendorMembersForm;

/**
 * Display the Vendor Member Search page, based on criteria in the searchVendorMembersForm,
 * if any exists. However, reset page and total to zero in preparation of doing a new search
 *
 * @struts:action   path="/viewVendorMemberCriteria"
 *                  name="searchVendorMembersForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="VendorMemberSearch"  path="/Common/VendorMemberSearch.jsp"
 */
public class ViewVendorMemberCriteriaAction extends AFSCMEAction {

    /** Creates a new instance of ViewVendorMemberCriteriaAction */
    public ViewVendorMemberCriteriaAction() {
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

       SearchVendorMembersForm vendorMbrForm = (SearchVendorMembersForm)form;

        //reset the page parms
        vendorMbrForm.setPage(0);
        vendorMbrForm.setTotal(0);
        vendorMbrForm.setHasCriteria(false);
        return mapping.findForward("VendorMemberSearch");
    }
}
