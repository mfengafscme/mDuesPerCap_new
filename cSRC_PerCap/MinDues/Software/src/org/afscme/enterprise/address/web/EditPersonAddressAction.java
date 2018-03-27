package org.afscme.enterprise.address.web;

import java.util.Map;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.CodeData;

/**
 * @struts:action   path="/editPersonAddress"
 *					name="personAddressForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/AddressMaintainenceEdit.jsp"
 */
public class EditPersonAddressAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        PersonAddressForm paForm = (PersonAddressForm)form;
        if (paForm.getAddrPk() != null) {
            PersonAddressRecord personAddress = s_systemAddress.getPersonAddress(paForm.getAddrPk());
            setCurrentPerson(request, personAddress.getPersonPk());
            paForm.setPersonAddress(personAddress);
        }
        return mapping.findForward("Edit");
	}
}
