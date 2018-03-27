package org.afscme.enterprise.person.web;

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
//import org.afscme.enterprise.person.web.SearchPersonForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.person.PersonCriteria;
import org.afscme.enterprise.person.PersonResult;
import org.afscme.enterprise.controller.UserSecurityData;


/**
 * Verify person, based on criteria in the verifyPersonForm.
 *
 * @struts:action   path="/verifyPerson"
 *                  input="/Membership/VerifyPerson.jsp"
 *                  name="verifyPersonForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward name="VerifyForm" path="/Membership/VerifyPerson.jsp"
 * @struts:action-forward name="VerifyResults" path="/Membership/PersonVerifyResults.jsp"
 * @struts:action-forward name="AddNew" path="/Membership/PersonDetailAdd.jsp"
 */
public class VerifyPersonAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        log.debug("VerifyPersonAction: back="+request.getParameter("back"));
        
        HttpSession session = request.getSession();
        
        VerifyPersonForm verifyPersonForm = (VerifyPersonForm)form;
        setCurrentPerson(request, null);

            //reset the page parms
        verifyPersonForm.setPage(0);
        verifyPersonForm.setTotal(0);
        verifyPersonForm.setSortBy("personNm");
        verifyPersonForm.setOrder(1);
//        verifyPersonForm.setBack(request.getParameter("back"));
        
//        request.setAttribute("verifyPersonForm", verifyPersonForm);
        if (request.getParameter("back") != null) {
            session.setAttribute("back", request.getParameter("back"));
            session.removeAttribute("personDetailAddForm");
            session.removeAttribute("verifyPersonForm");
        }
        if (request.getParameter("cancel") != null) {       //cancel brought me here
            session.removeAttribute("personDetailAddForm");
        }

        return mapping.findForward("VerifyForm");

    }
}
