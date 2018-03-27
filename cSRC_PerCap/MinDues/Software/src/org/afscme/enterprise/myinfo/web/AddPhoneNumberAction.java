
package org.afscme.enterprise.myinfo.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

// Java imports
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.web.PhoneNumberForm;

/**
 * This action is to view the Personal Information.
 *
 * @struts:action   path="/addPhoneNumber"
 *                  scope="request"
 * @struts:action-forward   name="View"  path="/PersonalInformation/AddPhoneNumber.jsp"
 */
public class AddPhoneNumberAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		setCurrentPerson(request, usd.getPersonPk());
		PhoneNumberForm pnf = new PhoneNumberForm();
		pnf.setCountryCode("1");
		request.setAttribute("phoneNumberForm", pnf);
		return mapping.findForward("View");
    }
}
