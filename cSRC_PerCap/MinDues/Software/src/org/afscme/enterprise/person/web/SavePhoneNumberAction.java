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
import org.afscme.enterprise.codes.Codes.Department;

/**
 * @struts:action   path="/savePhoneNumber"
 *                  name="phoneNumberForm"
 *                  scope="request"
 *                  validate="false"
 *                  input="/Membership/PhoneNumberMaintenanceEdit.jsp"
 *
 * @struts:action-forward   name="PhoneAdd"  path="/Membership/PhoneNumberMaintenanceEdit.jsp"
 * @struts:action-forward   name="PhoneEdit"  path="/Membership/PhoneNumberMaintenanceEdit.jsp"
 * @struts:action-forward   name="PhoneView"  path="/viewPhoneNumberInformation.action"
 */
public class SavePhoneNumberAction extends AFSCMEAction {

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
       log.debug("SavePhoneNumberAction: Entering");

       PhoneNumberForm phoneForm = (PhoneNumberForm)form;

       Integer personPk = getCurrentPersonPk(request);
       String back = request.getParameter("back");             // used for the entry point Return
       // Set Return button action
       request.setAttribute("back", back);
       request.setAttribute("phoneNumberForm", phoneForm);

        //validate manually since form is shared with add and edit
        ActionErrors errors = phoneForm.validate(mapping, request);
        if (errors != null && !errors.isEmpty()) {
            saveErrors(request, errors);

            if (phoneForm.isAdd()) {
                //return to the Add page if validation errors and came from Add
                return mapping.findForward("PhoneAdd");
            }
            else {
                //return to the Edit page if validation errors and came from Edit
                return mapping.findForward("PhoneEdit");
            }
        }

        //check for the Reset Primary
        if (phoneForm.getPhoneResetPrimary().booleanValue()) {
            if (usd.getActingAsAffiliate() != null && usd.getDepartment() == null)
                s_maintainPersons.resetPersonPhonePrimary(usd.getPersonPk(), Department.MD, personPk, phoneForm.getPhonePk());
            else
                s_maintainPersons.resetPersonPhonePrimary(usd.getPersonPk(), usd.getDepartment(), personPk, phoneForm.getPhonePk());                
        }

        if(phoneForm.getPhoneBadFlag() == null) {
			phoneForm.setPhoneBadFlag(new Boolean(false));
		}

        //save phone number information
        if (phoneForm.isAdd()) {
            //Business rule: If Political Objector, then set the Do Not Call flag to true
            //Business rule: If general Do Not Call is true, then set the Do Not Call flag to true
            phoneForm.setLockedDoNotCallAll(new Boolean(s_maintainPersons.isPoliticalObjector(personPk)));
            phoneForm.setLockedDoNotCallPrimary(new Boolean(s_maintainPersons.isDoNotCall(personPk)));
            phoneForm.setPhoneDoNotCallFg(phoneForm.getPhoneDoNotCallFg());

            //add a new phone is here
            s_maintainPersons.addPersonPhone(usd.getPersonPk(), usd.getDepartment(), personPk, phoneForm.getPhoneNumberData());
        } else {
            //update phone number information
            s_maintainPersons.updatePersonPhone(usd.getPersonPk(), personPk, phoneForm.getPhoneNumberData());
        }

        // go to view phone numbers once everything is saved
        return mapping.findForward("PhoneView");
    }
}
