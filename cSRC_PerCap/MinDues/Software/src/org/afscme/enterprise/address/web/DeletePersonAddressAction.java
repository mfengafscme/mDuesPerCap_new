package org.afscme.enterprise.address.web;

import java.util.Map;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.CodeData;

/**
 * @struts:action   path="/deletePersonAddress"
 *
 * @struts:action-forward   name="Done"  path="/viewAddressMaintainence.action"
 */
public class DeletePersonAddressAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer addressPk = Integer.valueOf(request.getParameter("addrPk"));
        if (usd.isActingAsAffiliate()) {
            //TODO: check if they have access to this address
        }
        
        s_systemAddress.deleteAddress(usd.getPersonPk(), usd.getDepartment(), addressPk);
        
        return mapping.findForward("Done");
	}
}
