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
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.address.ejb.SystemAddress;
import org.afscme.enterprise.address.Address;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;

/**
 * @struts:action   path="/savePersonDetailAdd"
 *                  name="personDetailAddForm"
 *                  scope="session"
 *                  validate="false"
 *                  input="/Membership/PersonDetailAdd.jsp"
 *
 * @struts:action-forward   name="PersonDuplicateSSN"  path="/viewDuplicateSSNNotifierPerson.action"
 * @struts:action-forward   name="PersonAdd"  path="/verifyPerson.action"
 * @struts:action-forward   name="PersonView"  path="/viewPersonDetail.action"
 */
public class SavePersonDetailAddAction extends AFSCMEAction {
    
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
        log.debug("SavePersonActionAdd: Entering");
        HttpSession session = request.getSession();
       
        PersonDetailAddForm personDetailAddForm = (PersonDetailAddForm)form;
        Integer personPk;

        // Set Return button action
        request.setAttribute("personDetailAddForm", personDetailAddForm);
        if (request.getParameter("another") != null) {     // go back to verify after submit
            session.setAttribute("another", request.getParameter("another"));
        }
        if (request.getParameter("continue") == null) {     // do not skip the following
            //validate manually 
            ActionErrors errors = personDetailAddForm.validate(mapping, request);
            if (personDetailAddForm.hasAddress()) {
                Set addrErrors = s_systemAddress.validate(personDetailAddForm.getPersonAddress());
                if (!CollectionUtil.isEmpty(addrErrors)) {
                    List errorFields = Address.getErrorFields(addrErrors);
                    List errorMessages = Address.getErrorMessages(addrErrors);
                    Iterator fit = errorFields.iterator();
                    Iterator eit = errorMessages.iterator();
                    while (fit.hasNext())
                        errors.add((String)fit.next(), new ActionError((String)eit.next()));
                }
            }
            if (errors != null && !errors.isEmpty()) {
                saveErrors(request, errors);

                //return to the Add page if validation errors 
                return mapping.getInputForward();
            }

            //check for Duplicate SSN
            if (s_maintainPersons.isDuplicateSSN(personDetailAddForm.getSsn())) {
                return mapping.findForward("PersonDuplicateSSN");
            }
        }
        
        //add person detail information
        personPk = s_maintainPersons.addPerson(usd.getPersonPk(), personDetailAddForm.getNewPerson(), usd.getDepartment(), null);
        personDetailAddForm.setPersonPk(personPk);
        log.debug("SavePersonActionAdd: personPk="+personPk);
        
        //check for a name change.  
        //If so, then add to weekly card run and reset name display on header/footer tags.
        
        session.removeAttribute("personDetailAddForm");
        session.removeAttribute("verifyPersonForm");
        if (session.getAttribute("another") != null) {     // go back to verify after submit
            session.removeAttribute("another");
            return mapping.findForward("PersonAdd");
        }

        // needed for header and footer tags
        setCurrentPerson(request, personPk);  

        // go to view person detail once everything is saved
        return mapping.findForward("PersonView");
    }
}
