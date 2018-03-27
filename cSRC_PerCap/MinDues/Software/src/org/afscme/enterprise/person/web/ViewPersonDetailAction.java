/*
 * viewPersonDetailAction.java
 *
 * Created on May 30, 2003, 3:16 PM
 */

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

/** 
 * @struts:action   path="/viewPersonDetail"
 *
 * @struts:action-forward   name="ViewPersonDetail"  path="/Membership/PersonDetail.jsp" 
 */
public class ViewPersonDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of ViewPersonDetailAction */
    public ViewPersonDetailAction() {
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
        log.debug("ViewPersonDetailAction: Entering");
        Integer personPk = null;
        try {
            personPk = new Integer(request.getParameter("personPk"));
            setCurrentPerson(request, personPk);
        } catch (NumberFormatException nfe) {
            personPk = getCurrentPersonPk(request);
        }

        if (personPk == null) {
            try {
                personPk = new Integer(request.getParameter("personPk"));
            } catch (Throwable t) {
                log.debug("Inside catch *****************************************");
                throw new JspException("No primary key was specified in the request.", t);
            }
        }
        
        PersonDetailForm pdf = new PersonDetailForm();
        PersonData data = s_maintainPersons.getPersonDetail(personPk, usd.getDepartment());
        log.debug("ViewPersonDetailAction: data=|"+data+"|");
        
        // Set form fields from PersonData
        pdf.setPersonData(data);
        
        log.debug("ViewPersonDetailAction: SystemAddress=|"+s_systemAddress.getSystemAddress(personPk)+"|");
        log.debug("ViewPersonDetailAction: Persona=|"+s_maintainPersons.getPersona(personPk)+"|");
        pdf.setPersonAddressRecord(s_systemAddress.getSystemAddress(personPk));
        pdf.setPersona(s_maintainPersons.getPersona(personPk));
        
        // debug code
        if (pdf.getPersona()==null)
            log.debug("Persona was NULL!!!!");
        else {
            log.debug("Persona returned "+pdf.getPersona().size() + " items:");
        }

        // needed for jsp
        request.setAttribute("personDetail", pdf);
        return mapping.findForward("ViewPersonDetail");
    }
    
}
