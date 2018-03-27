package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.OrganizationData;

/**
 * @struts:action   path="/editOrganizationDetail"
 *                  name="organizationDetailForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/OrganizationDetailEdit.jsp"
 * @struts:action-forward   name="Cancelled"  path="/Membership/OrganizationDetail.jsp" 
 */
public class EditOrganizationDetailAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        Integer orgPk = getCurrentOrganizationPk(request, "orgPK");
        OrganizationDetailForm orgForm = (OrganizationDetailForm)form;

        //retrieve the org detail for the pk
        OrganizationData data = s_maintainOrganizations.getOrgDetail(orgPk);

        // Set form fields from OrganizationData
        orgForm.setOrganizationData(data);
        orgForm.setOrgPrimaryLocation(data.getPrimaryLocationData());        

        return mapping.findForward("Edit");
    }
}
