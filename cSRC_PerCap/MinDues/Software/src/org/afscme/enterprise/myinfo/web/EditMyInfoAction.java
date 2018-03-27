
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
 * This action is to edit the Personal Information.
 *
 * @struts:action   path="/editMyInfo"
 *                  scope="request"
 * @struts:action-forward   name="Edit"  path="/PersonalInformation/EditPersonalInformation.jsp"
 */
public class EditMyInfoAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
        MyInfoData mid = s_maintainMyInfo.getMyInfoData(usd.getPersonPk());
		MyInfoForm mif = new MyInfoForm();
		ArrayList checkedPhones = new ArrayList();
		PersonData personData = mid.getPersonData();
		Collection phoneData = personData.getThePhoneData();
		if(phoneData != null) {
			Iterator i = phoneData.iterator();
			while(i.hasNext()) {
				PhoneData pd = (PhoneData)i.next();
				if(pd.getPhoneBadFlag().booleanValue()) {
					checkedPhones.add(pd.getPhonePk());
				}
			}
		}
		request.getSession(false).setAttribute("allPhones", mid.getPersonData().getThePhoneData());
		mif.setCheckedPhones((Integer[])checkedPhones.toArray(new Integer[checkedPhones.size()]));
		if(mid.getPersonAddressRecord()!= null) {
			mif.setState(mid.getPersonAddressRecord().getState());
			mif.setCountryPk(mid.getPersonAddressRecord().getCountryPk());
		}
		request.setAttribute("myInfoForm", mif);
		HttpSession session = request.getSession(false);
		session.setAttribute("mid", mid);
		request.getSession(false).setAttribute("correctAddress", mid.getPersonAddressRecord());
		return mapping.findForward("Edit");
    }
}
