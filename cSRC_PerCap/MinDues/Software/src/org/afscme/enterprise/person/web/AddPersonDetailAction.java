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
 * @struts:action   path="/addPersonDetail"
 *                  name="personDetailAddForm"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="AddPerson"  path="/Membership/PersonDetailAdd.jsp"
 * @struts:action-forward   name="CancelEditPerson"  path="/Membership/PersonDetail.jsp" 
 */
public class AddPersonDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of AddPersonDetailAction */
    public AddPersonDetailAction() {
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
        
        log.debug("AddPersonDetailAction: back="+request.getParameter("back"));
        Integer personPk;
        HttpSession session = request.getSession();
        PersonDetailAddForm personDetailAddForm;
        log.debug("AddPersonDetailAction: cancel ="+request.getParameter("cancel"));
        if (request.getParameter("cancel") != null) {       //cancel the save
            personDetailAddForm = (PersonDetailAddForm)form;
        }else {
            session.removeAttribute("personDetailAddForm");
            personDetailAddForm = new PersonDetailAddForm();
            VerifyPersonForm verifyPersonForm = (VerifyPersonForm)session.getAttribute("verifyPersonForm");
            log.debug("AddPersonDetailAction: firstNm="+verifyPersonForm.getFirstNm());
            log.debug("AddPersonDetailAction: lastNm="+verifyPersonForm.getLastNm());
            log.debug("AddPersonDetailAction: ssn="+verifyPersonForm.getSsn());

            personDetailAddForm.setFirstNm(verifyPersonForm.getFirstNm());
            personDetailAddForm.setLastNm(verifyPersonForm.getLastNm());
            personDetailAddForm.setSuffixNm((Integer)verifyPersonForm.getSuffixNm());
            personDetailAddForm.setSsn(verifyPersonForm.getSsn());
//            personDetailAddForm.setCountryCode("1");
            log.debug("AddPersonDetailAction: ssn1="+personDetailAddForm.getSsn1());
            log.debug("AddPersonDetailAction: ssn2="+personDetailAddForm.getSsn2());
            log.debug("AddPersonDetailAction: ssn3="+personDetailAddForm.getSsn3());
            session.setAttribute("personDetailAddForm", personDetailAddForm);
        }
        
        // needed for jsp
//        sesson.setAttribute("back", verifyPersonForm.getBack());
//        request.setAttribute("personDetailAddForm", personDetailAddForm);
        return mapping.findForward("AddPerson");
    }
    
}
