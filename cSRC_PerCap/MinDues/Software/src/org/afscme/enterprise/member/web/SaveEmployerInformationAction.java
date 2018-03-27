package org.afscme.enterprise.member.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.member.MemberData;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.util.DBUtil;

/* This struts action performs the work to save the Employer Information - Edit page data and is 
 * called from that page. 
  */

/**
 * @struts:action   path="/saveEmployerInformation"
 *                  name="employerInformationForm"
 *                  scope="request"
 *                  validate="false"
 *                  input="/Membership/EmployerInformationEdit.jsp"
 *
 * @struts:action-forward   name="EmployerEdit"  path="/Membership/EmployerInformationEdit.jsp"
 * @struts:action-forward   name="EmployerView"  path="/viewEmployerInformation.action"
 */
public class SaveEmployerInformationAction extends AFSCMEAction {
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet request that is being processed
     * @param usd Security data for the user performing this action
     *
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) 
    throws Exception {
        
        EmployerInformationForm eif = (EmployerInformationForm)form;           
        HttpSession session = request.getSession();
        
       // Integer personPk = getCurrentPersonPk(request);              
        if (request.getParameter("cancel") != null) 
        {          
            return mapping.findForward("EmployerView"); // return to the MemberAffiliateInformation (view) page   
        }
              
        if (request.getParameter("cancel") == null)
        {
            //validate manually 
            ActionErrors errors = eif.validate(mapping, request);
            if (errors != null && !errors.isEmpty()) 
            {
                saveErrors(request, errors);

                //return to the Edit page if validation errors 
                return mapping.findForward("EmployerEdit");
        
            }
        }   
       
        log.debug("SaveEmployerInformation: form data is : " + eif.toString());
        // update member affiliate information for the member in this affiliate 
        s_maintainMembers.updateAffiliateEmployerData(eif.getEmployerData(), usd.getPersonPk());

        // go to MemberDetaill view once everything is saved
        return mapping.findForward("EmployerView");
    }
}
