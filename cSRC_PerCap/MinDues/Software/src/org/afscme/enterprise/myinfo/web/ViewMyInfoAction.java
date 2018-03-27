
package org.afscme.enterprise.myinfo.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

// Java imports
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.myinfo.MyInfoData;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.common.PhoneData;

/**
 * This action is to view the Personal Information.
 *
 * @struts:action   path="/viewMyInfo"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/PersonalInformation/ViewPersonalInformation.jsp"
 */
public class ViewMyInfoAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		MyInfoData mid = s_maintainMyInfo.getMyInfoData(usd.getPersonPk());
		ArrayList checkedPhones = new ArrayList();
		PersonData personData = mid.getPersonData();
		Collection phoneData = personData.getThePhoneData();
		// set checked phones
		if(phoneData != null) {
			Iterator i = phoneData.iterator();
			while(i.hasNext()) {
				PhoneData pd = (PhoneData)i.next();
				if(pd.getPhoneBadFlag().booleanValue()) {
					checkedPhones.add(pd.getPhonePk());
				}
			}
		}


		MyInfoForm mif = new MyInfoForm();
		mif.setCheckedPhones((Integer[])checkedPhones.toArray(new Integer[checkedPhones.size()]));
		HttpSession session = request.getSession(false);
		session.setAttribute("mif", mif);
		session.setAttribute("mid", mid);

		// set incorrect address for the first time
		if(session.getAttribute("incorrectAddress") == null) {
			session.setAttribute("incorrectAddress", "");
		}
        return mapping.findForward("View");
    }
}
