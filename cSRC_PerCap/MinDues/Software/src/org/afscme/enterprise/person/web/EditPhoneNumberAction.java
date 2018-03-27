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
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.codes.Codes;
import org.afscme.enterprise.member.MemberAffiliateResult;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.util.DBUtil;

/**
 * @struts:action   path="/editPhoneNumber"
 *                  name="phoneNumberForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="EditPhone"  path="/Membership/PhoneNumberMaintenanceEdit.jsp"
 * @struts:action-forward   name="CancelEditPhone"  path="/Membership/PhoneNumberMaintenance.jsp"
 */
public class EditPhoneNumberAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    /** Creates a new instance of EditPhoneNumberAction */
    public EditPhoneNumberAction() {
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
        PhoneNumberForm phoneNumberForm = new PhoneNumberForm();
        String back = request.getParameter("back");         // used for the entry point Return
        Integer phonePk = null;

        if (request.getParameter("phonePk") != null) {      // this is an edit
            phonePk = Integer.valueOf(request.getParameter("phonePk"));
            phoneNumberForm.setPhoneNumberData(s_maintainPersons.getPersonPhone(phonePk));

            // business rule for the DO NOT CALL flag - used to set locks in JSP
            phoneNumberForm.setLockedDoNotCallAll(new Boolean(s_maintainPersons.isPoliticalObjector(personPk)));
            phoneNumberForm.setLockedDoNotCallPrimary(new Boolean(s_maintainPersons.isDoNotCall(personPk)));

            if(!phoneNumberForm.isLockedDoNotCallAll().booleanValue()) {
				Collection memberAffiliateResults = s_maintainMembers.getMemberAffiliation(personPk);
				Iterator i = memberAffiliateResults.iterator();
				while(i.hasNext()) {
					MemberAffiliateResult mar = (MemberAffiliateResult)i.next();
					if(mar.getMbrType().equals(Codes.MemberType.O)) {
						phoneNumberForm.setLockedDoNotCallAll(new Boolean(true));
						break;
					}
				}
			}
        }else {                                             // this is an add
            phoneNumberForm.setCountryCode("1");
			if(!phoneNumberForm.isLockedDoNotCallAll().booleanValue()) {
				Collection memberAffiliateResults = s_maintainMembers.getMemberAffiliation(personPk);
				Iterator i = memberAffiliateResults.iterator();
				while(i.hasNext()) {
					MemberAffiliateResult mar = (MemberAffiliateResult)i.next();
					if(mar.getMbrType().equals(Codes.MemberType.O)) {
						phoneNumberForm.setLockedDoNotCallAll(new Boolean(true));
						break;
					}
				}
			}
        }

        // debug code
        if (phoneNumberForm.getPhoneNumberData()==null) 
            log.debug("Phone number was NULL!!!!");
        else {
            log.debug("PhoneData returned "+phoneNumberForm.getPhoneNumberData());
        }

        // Set Return button action
        request.setAttribute("back", back);
        log.debug("EditPhoneNumberAction: back="+ back);

        // needed for jsp
        request.setAttribute("phoneNumberForm", phoneNumberForm);
        return mapping.findForward("EditPhone");
    }

}
