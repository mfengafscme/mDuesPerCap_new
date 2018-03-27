package org.afscme.enterprise.person.web;

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
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/savePersonDetail"
 *                  name="personDetailForm"
 *                  scope="request"
 *                  validate="false"
 *                  input="/Membership/PersonDetailEdit.jsp"
 *
 * @struts:action-forward   name="PersonDuplicateSSN"  path="/viewDuplicateSSNNotifierPerson.action"
 * @struts:action-forward   name="PersonEdit"  path="/Membership/PersonDetailEdit.jsp"
 * @struts:action-forward   name="PersonView"  path="/viewPersonDetail.action"
 */
public class SavePersonDetailAction extends AFSCMEAction {
    
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
        log.debug("SavePersonAction: Entering");
        HttpSession session = request.getSession();
        session.setAttribute("back", "PersonDetail");
        
        PersonDetailForm personDetailForm = (PersonDetailForm)form; 
               
        if(session.getAttribute("personDetailForm") != null) {
            personDetailForm = (PersonDetailForm)session.getAttribute("personDetailForm");
        }
        
        Integer personPk = getCurrentPersonPk(request);
        String back = request.getParameter("back");             // used for the entry point Return
        // Set Return button action
        request.setAttribute("back", back);
        request.setAttribute("personDetailForm", personDetailForm);

        if (request.getParameter("continue") == null) {     // do not continue
            //validate manually 
            ActionErrors errors = personDetailForm.validate(mapping, request);
            if (errors != null && !errors.isEmpty()) {
                saveErrors(request, errors);

                personDetailForm.setPersonAddressRecord(s_systemAddress.getSystemAddress(personPk));
                personDetailForm.setEmailData(s_maintainPersons.getPersonEmails(personPk));
                personDetailForm.setPhoneData(s_maintainPersons.getPersonPhones(personPk, usd.getDepartment()));

                //return to the Edit page if validation errors 
                return mapping.findForward("PersonEdit");
            }            
            
            //check for Duplicate SSN
            if (personDetailForm.getPrevAction().equals("edit"))
            {
                PersonDetailForm personDetailFromScreen = (PersonDetailForm)form;                           
                session.setAttribute("newPersonDetailForm", personDetailFromScreen);

                if (personDetailForm.getPreviousSsn() != null )
		{
                    if (!personDetailForm.getPreviousSsn().equals(personDetailForm.getSsn()))
                    {
                    	if (s_maintainPersons.isDuplicateSSN(personDetailForm.getSsn())) 
			{
                        	session.setAttribute("personDetailForm", personDetailForm);
                                request.setAttribute("personDetailForm", personDetailForm);
                        	return mapping.findForward("PersonDuplicateSSN");
			}
                    }else
                    {
                            if (s_maintainPersons.isDuplicateSSNGreaterThan1(personDetailForm.getSsn())) 
                            {
                        	session.setAttribute("personDetailForm", personDetailForm);
                                request.setAttribute("personDetailForm", personDetailForm);                                
                                return mapping.findForward("PersonDuplicateSSN");
                            }					
                    }
		}else{
                	if (s_maintainPersons.isDuplicateSSN(personDetailForm.getSsn())) {
                    	session.setAttribute("personDetailForm", personDetailForm);
                        request.setAttribute("personDetailForm", personDetailForm);                        
                    	return mapping.findForward("PersonDuplicateSSN");
                	}                					
		}
            }else
            {
                if (s_maintainPersons.isDuplicateSSN(personDetailForm.getSsn())) {
                    session.setAttribute("personDetailForm", personDetailForm);
                    request.setAttribute("personDetailForm", personDetailForm);                    
                    return mapping.findForward("PersonDuplicateSSN");
                }                
            }                        
        }
       
        if (session.getAttribute("newPersonDetailForm") != null)
        {
            personDetailForm = (PersonDetailForm)session.getAttribute("newPersonDetailForm");
        }
        
        personDetailForm.setPersonPk(personPk);
        session.removeAttribute("personDetailForm");
        session.removeAttribute("newPersonDetailForm");
        //update phone number information        
        s_maintainPersons.updatePersonDetail(usd.getPersonPk(), personDetailForm.getPersonData());

        //check for a name change.  
        //If so, then add to weekly card run and reset name display on header/footer tags.
        String currentName = getCurrentPersonName(session);
        String newName = s_maintainUsers.getPersonName(personPk);  
        if (!TextUtil.equals(currentName, newName)) {

            // add to weekly card run so member gets new card for changed name
            s_maintainMembers.addToWeeklyCardRun(personPk);
            // Reset person name in session so headers/footers display correctly
            setCurrentPersonName(request, newName);           
        }

         // go to view phone numbers once everything is saved
        return mapping.findForward("PersonView");
    }
}
