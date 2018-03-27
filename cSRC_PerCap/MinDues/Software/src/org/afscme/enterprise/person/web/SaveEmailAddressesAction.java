package org.afscme.enterprise.person.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Iterator;
import java.util.Collection;
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
 * @struts:action   path="/saveEmailAddresses"
 *                  name="emailAddressForm"
 *                  scope="request"
 *                  validate="false"
 *                  input="/Membership/EmailAddressMaintenanceEdit.jsp"
 *
 * @struts:action-forward   name="EmailEdit"  path="/Membership/EditEmailAddressMaintenance.jsp"
 * @struts:action-forward   name="EmailView"  path="/viewEmailAddresses.action"
 */
public class SaveEmailAddressesAction extends AFSCMEAction {
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet request that is being processed
     * @param usd Security data for the user performing this action
     *
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        log.debug("SaveEmailAddressesAction: Entering");
        
        EmailAddressForm emailForm = (EmailAddressForm)form;
        Integer personPk = getCurrentPersonPk(request);
        String back = request.getParameter("back");             // used for the entry point Return
        if (form==null) {
            log.debug("SaveEmailAddressesAction: form is NULL");
        }
                
        //Set the original email data to check for changes
        Collection emailData = s_maintainPersons.getPersonEmails(personPk);
        Iterator it = emailData.iterator();
        EmailData data = null;
        int cnt = 0;
        while (it.hasNext()){
            data = (EmailData) it.next();
            cnt++;
            if (cnt==1) {
                emailForm.setPersonEmailAddr1_o(data.getPersonEmailAddr());
                emailForm.setEmailBadFg1_o(data.getEmailBadFg());
                emailForm.setIsPrimary1_o(data.getIsPrimary());
            } else {
                emailForm.setPersonEmailAddr2_o(data.getPersonEmailAddr());
                emailForm.setEmailBadFg2_o(data.getEmailBadFg());
                emailForm.setIsPrimary2_o(data.getIsPrimary());
            }
        }
        
        //Determine which Email Address should be set to Primary
        emailForm.setPrimary();
        
        //Updates the email addresses
        s_maintainPersons.updatePersonEmail(usd.getPersonPk(), personPk, emailForm.getEmailData());
        
        // Set Return button action
        request.setAttribute("back", back);
        log.debug("SaveEmailAddressesAction: back="+ back);
        request.setAttribute("emailAddress", emailForm);
        
        // go to view email addresses once everything is saved
        return mapping.findForward("EmailView");
    }
    
}
