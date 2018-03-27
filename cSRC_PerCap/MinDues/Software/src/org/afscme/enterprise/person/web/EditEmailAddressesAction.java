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

/**
 * @struts:action   path="/editEmailAddresses"
 *                  name="emailAddressForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="EditEmail"  path="/Membership/EmailAddressMaintenanceEdit.jsp"
 * @struts:action-forward   name="CancelEditEmail"  path="/Membership/EmailAddressMaintenance.jsp" 
 */
public class EditEmailAddressesAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of EditEmailAddressesAction */
    public EditEmailAddressesAction() {
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
        
        Integer personPk = getCurrentPersonPk(request, "personPk");
//        EmailAddressForm emailAddressForm = (EmailAddressForm)form;
        EmailAddressForm emailAddressForm = new EmailAddressForm();
        String back = request.getParameter("back");         // used for the entry point Return
        
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
        emailAddressForm.setBack(back);
        
        
        // debug code
        if ((emailAddressForm.getEmailData1()==null)||(emailAddressForm.getEmailData2()==null)) 
            log.debug("Email was NULL!!!!");
        else {
            log.debug("EmailData1 returned "+emailAddressForm.getEmailData1());
            log.debug("EmailData2 returned "+emailAddressForm.getEmailData2());
        }
        
        // Set Return button action
        request.setAttribute("back", back);
        log.debug("EditEmailAddressesAction: "+ back);

        // needed for jsp
        request.setAttribute("emailAddressForm", emailAddressForm);
        return mapping.findForward("EditEmail");
    }
    
}
