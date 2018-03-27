
package org.afscme.enterprise.myinfo.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

// Java imports
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.myinfo.MyInfoData;
import org.afscme.enterprise.myinfo.ejb.MaintainMyInfo;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.EmailData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.person.web.PhoneNumberForm;
import org.afscme.enterprise.person.web.EmailAddressForm;
import org.afscme.enterprise.address.web.PersonAddressForm;
import org.afscme.enterprise.address.Address;
import org.afscme.enterprise.util.TextUtil;

/**
 * This action is to save the Personal Information.
 *
 * @struts:action   path="/saveMyInfo"
 *                  scope="request"
 *                  name="myInfoForm"
 *					input="/PersonalInformation/EditPersonalInformation.jsp"
 * @struts:action-forward   name="View"  path="/viewMyInfo.action"
 */
public class SaveMyInfoAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		HttpSession session = request.getSession(false);
		MyInfoForm mif = (MyInfoForm)form;
		MyInfoData mid = (MyInfoData)session.getAttribute("mid");
		PersonData pd = mid.getPersonData();
		pd.setPersonPk(usd.getPersonPk());

		// get the correct address and set it.
		PersonAddressRecord correctAddress = (PersonAddressRecord)session.getAttribute("correctAddress");
		PersonAddressRecord pad = new PersonAddressRecord();
		pad = new PersonAddressRecord();
		pad.setAddr1(mif.getAddr1());
		pad.setAddr2(mif.getAddr2());
		pad.setCity(mif.getCity());
		pad.setState(mif.getState());
		pad.setZipCode(mif.getZipCode());
		pad.setZipPlus(mif.getZipPlus());
		pad.setCounty(mif.getCounty());
		pad.setProvince(mif.getProvince());
		pad.setCountryPk(mif.getCountryPk());
		RecordData rd = new RecordData();
		rd.setPk(mif.getAddressPk());
		pad.setRecordData(rd);
		pad.setPersonPk(usd.getPersonPk());
		mid.setPersonAddressRecord(pad);

		//set the email data
		ArrayList emailList = new ArrayList();
		String[] emailAddresses = mif.getPersonEmailAddresses();
		Integer[] emailPks = mif.getEmailPks();
		Integer[] emailTypes = mif.getEmailTypes();
		Boolean[] emailBadFlags = mif.getEmailBadFlags();
		for(int i = 0; i < emailAddresses.length; i++) {
			EmailData ed = new EmailData();
			ed.setEmailPk(emailPks[i]);
			ed.setPersonEmailAddr(emailAddresses[i]);
			ed.setEmailType(emailTypes[i]);
			ed.setEmailBadFg(emailBadFlags[i]);
			emailList.add(ed);
		}
		pd.setTheEmailData(emailList);

		mid.setPersonData(pd);
		// validation should be done in form class
		ActionErrors validationErrors = new ActionErrors();
		if (!TextUtil.isEmpty(mif.getZipCode()) && !TextUtil.isInt(mif.getZipCode()))
			validationErrors.add("zipCode", new ActionError("error.address.zipCode.invalid"));
		if (!TextUtil.isEmpty(mif.getZipPlus()) && !TextUtil.isInt(mif.getZipPlus()))
			validationErrors.add("zipPlus", new ActionError("error.address.zipPlus.invalid"));
		for(int i = 0; i < emailAddresses.length; i++) {
			if (!TextUtil.isEmpty(emailAddresses[i]) && !TextUtil.isEmail(emailAddresses[i])) {
				validationErrors.add("emailAddress", new ActionError("error.email.invalid"));
				break;
			}
		}
        if(validationErrors.size() != 0) {
			saveErrors(request, validationErrors);
        	return new ActionForward(mapping.getInput());
		}

		// set the checked and unchecked phones
		Integer[] checkedPhones = mif.getCheckedPhones();
		ArrayList allThePhones = (ArrayList)(request.getSession(false).getAttribute("allPhones"));
		ArrayList clonedPhones = new ArrayList();
		ArrayList changedPhones = new ArrayList();
		if(allThePhones != null) {
			for(int i = 0; i < allThePhones.size(); i++) {
				PhoneData phoneData = (PhoneData)allThePhones.get(i);
				clonedPhones.add(phoneData.clone());
				Integer phonePk = phoneData.getPhonePk();
				boolean hasBadFlag = false;
				if(checkedPhones != null) {
					for(int j = 0; j < checkedPhones.length; j++) {
						if(checkedPhones[j].equals(phonePk)) {
							if(phoneData.getPhoneBadFlag().equals(new Boolean(false))) {
								phoneData.setPhoneBadFlag(new Boolean(true));
								changedPhones.add(phoneData);
							}
							hasBadFlag = true;
							break;
						}
					}
				}
				if(!hasBadFlag) {
					if(phoneData.getPhoneBadFlag().equals(new Boolean(true))) {
						phoneData.setPhoneBadFlag(new Boolean(false));
						changedPhones.add(phoneData);
					}
				}
			}
		}
		pd.setThePhoneData(changedPhones);

		// update only if the address is different
		if(!s_systemAddress.equalsAffiliateAddress(mid.getPersonAddressRecord())) {
			// update MyInfo, if there are errors regarding the address forward to the view
			Set errors = s_maintainMyInfo.updateMyInfoData(mid);
			if (errors != null) {
				// revert back to the original data
				pd.setThePhoneData(clonedPhones);
				request.getSession(false).setAttribute("allPhones", clonedPhones);
				List errorFields = Address.getErrorFields(errors);
				List errorMessages = Address.getErrorMessages(errors);
				return makeErrorForward(request, mapping, errorFields, errorMessages);
			}
		}
		// else throw an error
		else {
			ActionErrors errors = new ActionErrors();
			errors.add("sameAddress", new ActionError("errors.myInfo.sameAddress"));
			saveErrors(request, errors);
			return new ActionForward(mapping.getInput());
		}

        // set an incorrect address only if it differs from the existing correct address
        if(correctAddress != null && !correctAddress.equals(pad)) {
			session.setAttribute("incorrectAddress", correctAddress);
		}
		return mapping.findForward("View");
	}
}

