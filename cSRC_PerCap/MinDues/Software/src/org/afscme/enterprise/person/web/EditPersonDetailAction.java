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
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.util.DBUtil;

/**
 * @struts:action   path="/editPersonDetail"
 *                  name="personDetailForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="EditPerson"  path="/Membership/PersonDetailEdit.jsp"
 * @struts:action-forward   name="CancelEditPerson"  path="/Membership/PersonDetail.jsp" 
 */
public class EditPersonDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of EditPersonDetailAction */
    public EditPersonDetailAction() {
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
        HttpSession session = request.getSession();
        PersonDetailForm personDetailForm;
        
        log.debug("EditPersonDetailAction: Entering");
        
        if (request.getParameter("cancel") != null) {       //cancel the save
            personDetailForm = (PersonDetailForm)form;
            personDetailForm.setPersonData(s_maintainPersons.getPersonDetail(personPk, usd.getDepartment()));            
            personDetailForm.setPersonAddressRecord(s_systemAddress.getSystemAddress(personPk));
            personDetailForm.setEmailData(s_maintainPersons.getPersonEmails(personPk));
            personDetailForm.setPhoneData(s_maintainPersons.getPersonPhones(personPk, usd.getDepartment()));            
            
            if(session.getAttribute("personDetailForm") != null) {
                session.setAttribute("personDetailForm", personDetailForm);
            }
        }else {
            session.removeAttribute("personDetailForm");
            personDetailForm = new PersonDetailForm();

            // Set form fields from PersonData
            personDetailForm.setPersonData(s_maintainPersons.getPersonDetail(personPk, usd.getDepartment()));
            personDetailForm.setPersonAddressRecord(s_systemAddress.getSystemAddress(personPk));

            // Set the Comments to blank
            personDetailForm.setComment("");
        }

        // needed for jsp
        personDetailForm.setPrevAction("edit");
        personDetailForm.setPreviousSsn(personDetailForm.getSsn());        
        request.setAttribute("personDetailForm", personDetailForm);
        return mapping.findForward("EditPerson");
    }
    
}
