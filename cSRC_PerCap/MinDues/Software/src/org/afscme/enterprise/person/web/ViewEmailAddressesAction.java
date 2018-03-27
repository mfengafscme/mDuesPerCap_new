package org.afscme.enterprise.person.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.EmailData;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/** 
 * @struts:action   path="/viewEmailAddresses"
 *
 * @struts:action-forward   name="ViewEmailAddresses"  path="/Membership/EmailAddressMaintenance.jsp" 
 */
public class ViewEmailAddressesAction extends AFSCMEAction {
    
    /** Creates a new instance of ViewPersonDetailAction */
    public ViewEmailAddressesAction() {
    }
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     *
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) 
    throws Exception {
        
        Integer personPk = getCurrentPersonPk(request);
        EmailAddressForm emailAddressForm = new EmailAddressForm();
        
        // Set form fields from EmailData
        Collection emailData = s_maintainPersons.getPersonEmails(personPk);
        Iterator it = emailData.iterator();
        EmailData data = null;
        int cnt = 0;
        while (it.hasNext()){
              data = (EmailData) it.next();
              cnt++;
              if (cnt==1) {   
                emailAddressForm.setEmailData1(data);
            } else {
                emailAddressForm.setEmailData2(data);
            }
        }
//        emailAddressForm.setEmailData(s_maintainPersons.getPersonEmails(personPk));
        
        // debug code
        if ((emailAddressForm.getEmailData1()==null)||(emailAddressForm.getEmailData2()==null)) 
            log.debug("Email was NULL!!!!");
        else {
            log.debug("EmailData1 returned "+emailAddressForm.getEmailData1());
            log.debug("EmailData2 returned "+emailAddressForm.getEmailData2());
        }
        
        // Set Return button action
        String forward = request.getParameter("back");
        if (forward.equals("PersonDetail"))  
            emailAddressForm.setReturnAction("/viewPersonDetail.action?personPk=" + personPk);
        else if (forward.equals("MemberDetail"))  
            emailAddressForm.setReturnAction("/viewMemberDetail.action?personPk=" + personPk);
        else if (forward.equals("StaffDetail"))  
            emailAddressForm.setReturnAction("/viewAffiliateStaff.action");
        else if (forward.equals("AssociateDetail"))
            emailAddressForm.setReturnAction("/viewOrganizationAssociateDetail.action");
        else
            throw new JspException("invalid back parameter " + forward + " passed to viewEmailAddress.action");
        request.setAttribute("back", forward);

        // needed for jsp
        request.setAttribute("emailAddress", emailAddressForm);
        return mapping.findForward("ViewEmailAddresses");
    }
    
}
