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
import javax.servlet.http.HttpSession;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;


/** 
 * @struts:action   path="/addOrganizationDetail"
 *                  input="/Membership/OrganizationDetailAdd.jsp"
 *                  name="organizationDetailForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="AddNew"  path="/Membership/OrganizationDetailAdd.jsp"
 * @struts:action-forward   name="Cancelled"  path="/showMain.action" 
 */
public class AddOrganizationDetailAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        
        OrganizationDetailForm orgForm = (OrganizationDetailForm)form;

        //retrieve the orgName from verify and then get rid of verify in session
        HttpSession session = request.getSession();
        VerifyOrganizationForm voForm = (VerifyOrganizationForm) session.getAttribute("verifyOrganizationForm");
        session.removeAttribute("verifyOrganizationForm");            
        
        if (isCancelled(request))
            return mapping.findForward("Cancelled");

        //set the org name if provided in verify
        orgForm.setOrgName(voForm.getOrgName());
            
        return mapping.findForward("AddNew");
    }
}
