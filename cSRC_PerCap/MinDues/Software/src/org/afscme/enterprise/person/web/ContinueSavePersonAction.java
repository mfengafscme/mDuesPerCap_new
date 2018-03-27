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
import org.afscme.enterprise.organization.web.OrganizationAssociateDetailForm;
import org.afscme.enterprise.member.web.MemberDetailAddForm;
import org.afscme.enterprise.member.web.MemberDetailForm;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.util.DBUtil;

/**
 * @struts:action   path="/continueSavePerson"
 *                  name="phoneNumberForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="PersonView"  path="/viewPersonDetail.action"
 * @struts:action-forward   name="PersonAdd"  path="/savePersonDetailAdd.action?continue" redirect="true"
 * @struts:action-forward   name="PersonDetail"  path="/savePersonDetail.action?continue" redirect="true"
 * @struts:action-forward   name="AssociateAdd"  path="/saveOrganizationAssociate.action" redirect="true"
 * @struts:action-forward   name="MemberAdd"  path="/saveMemberDetailAdd.action?continue" redirect="true" 
 * @struts:action-forward   name="MemberAddAffiliation"  path="/saveMemberDetailAddAffiliation.action?continue" redirect="true" 
 * @struts:action-forward   name="MemberDetail"  path="/saveMemberDetail.action?continue" redirect="true"
*/
public class ContinueSavePersonAction extends AFSCMEAction {
    
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
        log.debug("ContinueSavePersonAction: back from request="+request.getAttribute("back"));
        log.debug("ContinueSavePersonAction: back from session="+request.getSession().getAttribute("back"));
        
//       PersonDetailAddForm personDetailAddForm = (PersonDetailAddForm)form;
        HttpSession session = request.getSession();
        PersonDetailAddForm personDetailAddForm = (PersonDetailAddForm)session.getAttribute("personDetailAddForm");
        VerifyPersonForm verifyPersonForm = (VerifyPersonForm)session.getAttribute("verifyPersonForm");
        Integer personPk;

       String back = (String)session.getAttribute("back");            // used for the entry point Return
       // Set Return button action
//       request.setAttribute("personDetailAddForm", personDetailAddForm);

        // Set Return button action
        if (back.equals("PersonAdd")) { //keep this check here, don't want to open up a hole into the forward mechanism
            return mapping.findForward("PersonAdd");
        }
        else if (back.equals("PersonDetail")) { //keep this check here, don't want to open up a hole into the forward mechanism
            return mapping.findForward("PersonDetail");
        }
        else if (back.equals("MemberEdit")) { //keep this check here, don't want to open up a hole into the forward mechanism
           // MemberDetailForm mdf = (MemberDetailForm)session.getAttribute("memberDetailForm");
           // request.setAttribute("memberDetailForm", mdf); not needed, already in session
            return mapping.findForward("MemberDetail");
        }
        else if (back.equals("MemberAdd")) { //keep this check here, don't want to open up a hole into the forward mechanism
            MemberDetailAddForm mdaf = (MemberDetailAddForm)session.getAttribute("memberDetailAddForm");
            request.setAttribute("memberDetailAddForm", mdaf);
            return mapping.findForward("MemberAdd");
        }
       else if (back.equals("MemberAddAffiliation")) { //keep this check here, don't want to open up a hole into the forward mechanism
            MemberDetailAddForm mdaf = (MemberDetailAddForm)session.getAttribute("memberDetailAddForm");
            request.setAttribute("memberDetailAddForm", mdaf);
            return mapping.findForward("MemberAddAffiliation");
        }
        else if (back.equals("AssociateAdd")) {
            //continue to save org associate if dup ssn continue for org associate record change
            OrganizationAssociateDetailForm oadForm = (OrganizationAssociateDetailForm)session.getAttribute("organizationAssociateDetailForm");
            oadForm.setIgnoreSsnDup(true);
            return mapping.findForward("AssociateAdd");
        }       
        // needed for jsp
        return mapping.findForward("ViewDetail");
    }
}
