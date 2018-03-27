
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
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.myinfo.MyInfoData;
import org.afscme.enterprise.person.web.PhoneNumberForm;

/**
 * This action is to save a phone number.
 *
 * @struts:action   path="/savePhoneNumberMyInfo"
 *                  scope="request"
 *					name="phoneNumberForm"
 *					input="/PersonalInformation/AddPhoneNumber.jsp"
 * @struts:action-forward   name="View"  path="/viewMyInfo.action"
 */
public class SavePhoneNumberAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		PhoneNumberForm pnf = (PhoneNumberForm)form;
		PhoneData pd = new PhoneData();
		pd.setPhonePrmryFg(pnf.getPhonePrmryFg());
		pd.setPhoneType(pnf.getPhoneType());
		pd.setCountryCode(pnf.getCountryCode());
		pd.setAreaCode(pnf.getAreaCode());
		pd.setPhoneNumber(pnf.getPhoneNumber());
		s_maintainPersons.addPersonPhone(usd.getPersonPk(), s_maintainUsers.getUser(usd.getPersonPk()).getDepartment(),
										 usd.getPersonPk(), pd);
        return mapping.findForward("View");
    }
}
