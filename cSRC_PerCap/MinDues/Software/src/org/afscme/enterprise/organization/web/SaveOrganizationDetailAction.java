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
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;

/** 
 * Handles the submits from the 'Add/Edit Organization' page. 
 *
 * @struts:action   path="/saveOrganizationDetail"
 *                  name="organizationDetailForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="OrgDetailAdd"  path="/Membership/OrganizationDetailAdd.jsp"
 * @struts:action-forward   name="OrgDetailEdit"  path="/Membership/OrganizationDetailEdit.jsp"
 * @struts:action-forward   name="OrgDetail"  path="/viewOrganizationDetail.action"
 */
public class SaveOrganizationDetailAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        
        OrganizationDetailForm orgForm = (OrganizationDetailForm)form;

        //validate manually since form is shared with add and edit
        ActionErrors errors = orgForm.validate(mapping, request);
        if (errors != null && !errors.isEmpty()) {
            saveErrors(request, errors);

            if (orgForm.isAdd()) {
                //return to the Add page if validation errors and came from Add
                return mapping.findForward("OrgDetailAdd");
            }    
            else { 
              
                //set the primary location information so the re-displayed form will have them
		orgForm.setOrgPrimaryLocation(s_maintainOrgLocations.getOrgPrimaryLocation(orgForm.getOrgPK()));
                
                //return to the Edit page if validation errors and came from Edit
                return mapping.findForward("OrgDetailEdit");
            }
        }
        
        //save organization detail information
        Integer orgPK = null;
        
        if (orgForm.isAdd()) {
            //add a new organization
            orgPK = s_maintainOrganizations.addOrg( orgForm.getOrgName(), 
                                                    orgForm.getOrgType(), 
                                                    orgForm.getOrgWebSite(), 
                                                    usd.getPersonPk());
        } else {
            //edit organization detail information
            s_maintainOrganizations.updateOrgDetail( orgForm.getOrganizationData(), 
                                                     usd.getPersonPk());
        }
       
        setCurrentOrganization(request, orgPK);

        // go to org detail once everything is saved
        return mapping.findForward("OrgDetail");
    }
}
