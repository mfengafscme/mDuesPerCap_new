package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.OrganizationAssociateData;


/**
 * Processing for the display Organization Associate Detail action
 *
 * @struts:action   path="/viewOrganizationAssociateDetail"
 *                  input="/Membership/OrganizationAssociateDetail.jsp"
 *                  name="organizationAssociateDetailForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/Membership/OrganizationAssociateDetail.jsp"
 */
public class ViewOrganizationAssociateDetailAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer personPk = getCurrentPersonPk(request, "personPk");
        Integer orgPk = getCurrentOrganizationPk(request, "orgPK");

        OrganizationAssociateDetailForm oadForm = (OrganizationAssociateDetailForm)form;

        //retrieve the organization associate detail information
        OrganizationAssociateData orgAssociateData = s_maintainOrganizations.getOrgAssociateDetail(orgPk, personPk);
        
        //set form fields from org associate info
        oadForm.setOrganizationAssociateData(orgAssociateData);
        oadForm.setSsn(orgAssociateData.getPersonData().getSsn());
        
        return mapping.findForward("View");
    }
}
