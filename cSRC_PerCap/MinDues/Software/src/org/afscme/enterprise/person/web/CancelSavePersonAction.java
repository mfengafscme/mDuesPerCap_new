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
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.member.web.MemberDetailAddForm;
import org.afscme.enterprise.member.web.MemberDetailForm;

/**
 * @struts:action   path="/cancelSavePerson"
 *                  name="personDetailAddForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="PersonAdd"  path="/addPersonDetail.action?cancel" redirect="true"
 * @struts:action-forward   name="PersonEdit"  path="/editPersonDetail.action?cancel" redirect="true"
 * @struts:action-forward   name="MemberAdd"  path="/addMemberDetail.action?cancel" redirect="true"
 * @struts:action-forward   name="MemberAddAffiliation"  path="/viewMemberDetailAddAffiliation.action?cancel" redirect="true"
 * @struts:action-forward   name="MemberEdit"  path="/editMemberDetail.action?cancel" redirect="true"
 * @struts:action-forward   name="AssociateAdd"  path="/editOrganizationAssociateDetail.action?cancel" redirect="true" 
 * @struts:action-forward   name="ShowMain"  path="/common/MainMenu.jsp"
 */
public class CancelSavePersonAction extends AFSCMEAction {
    
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
        
        HttpSession session = request.getSession();
        VerifyPersonForm verifyPersonForm = (VerifyPersonForm)session.getAttribute("verifyPersonForm");

       // Set Return button action
//       request.setAttribute("back", verifyPersonForm.getBack());
//       request.setAttribute("personDetailAddForm", personDetailAddForm);

        // Set Return button action
        String forward = (String)session.getAttribute("back");

        log.debug("CancelSavePersonAction: back="+session.getAttribute("back"));

        if (forward.equals("PersonAdd")) { //keep this check here, don't want to open up a hole into the forward mechanism
            PersonDetailAddForm personDetailAddForm = (PersonDetailAddForm)form;
            request.setAttribute("personDetailAddForm", personDetailAddForm);
            return mapping.findForward("PersonAdd");
        }else if (forward.equals("PersonDetail")) { //keep this check here, don't want to open up a hole into the forward mechanism
            request.setAttribute("personDetailForm", (PersonDetailForm)session.getAttribute("personDetailForm"));
            return mapping.findForward("PersonEdit");
        }else if (forward.equals("MemberAdd")) { //keep this check here, don't want to open up a hole into the forward mechanism
            MemberDetailAddForm mdaf = (MemberDetailAddForm)session.getAttribute("memberDetailAddForm");
            request.setAttribute("memberDetailAddForm", mdaf);
            return mapping.findForward("MemberAdd");
            }else if (forward.equals("MemberAddAffiliation")) { //keep this check here, don't want to open up a hole into the forward mechanism
            MemberDetailAddForm mdaf = (MemberDetailAddForm)session.getAttribute("memberDetailAddForm");
            request.setAttribute("memberDetailAddForm", mdaf);
            return mapping.findForward("MemberAddAffiliation");
        }else if (forward.equals("MemberEdit")) { //keep this check here, don't want to open up a hole into the forward mechanism
            MemberDetailForm mdf = (MemberDetailForm)session.getAttribute("memberDetailForm");
            request.setAttribute("memberDetailForm", mdf);
            return mapping.findForward("MemberEdit");
        } else if (forward.equals("AssociateAdd")) {
            //if cancel save from dup ssn and org associate, then return to the correct org associate screen
            return mapping.findForward("AssociateAdd");
        }    
        // go back to the Person Add screen
        return mapping.findForward("ShowMain");
    }
}
