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
import org.afscme.enterprise.person.EmailData;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.util.DBUtil;

/**
 * @struts:action   path="/deletePhoneNumber"
 *                  name="phoneNumberForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="PhoneView"  path="/viewPhoneNumberInformation.action" 
 */
public class DeletePhoneNumberAction extends AFSCMEAction {
    
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
        Integer phonePk = Integer.valueOf(request.getParameter("phonePk"));
        String back = request.getParameter("back");             // used for the entry point Return
        
        //edit phone number information
        s_maintainPersons.deletePhoneNumber(usd.getPersonPk(), personPk, phonePk); 

        // Set Return button action
        request.setAttribute("back", back);

        // go to view phone numbers once everything is saved
        return mapping.findForward("PhoneView");
    }
}
