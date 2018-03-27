package org.afscme.enterprise.address.web;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
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
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/viewAddressMaintainence"
 *
 * @struts:action-forward   name="View"  path="/Membership/AddressMaintainence.jsp"
 * @struts:action-forward   name="ListPersons"  path="/Membership/test_ListPersons.jsp"
 */
public class ViewAddressMaintainenceAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        List list;
        
        Integer personPk = getCurrentPersonPk(request, "pk");
        AddressMaintainence addressMaintainence = new AddressMaintainence();

        if (usd.isActingAsAffiliate()) {
            list = s_systemAddress.getPersonAddressesForAffiliate(personPk, usd.getActingAsAffiliate());
        }  else {
            list = s_systemAddress.getPersonAddressesForDepartment(personPk, usd.getDepartment());
            addressMaintainence.setUserDepartment(usd.getDepartment());
        }
        
        addressMaintainence.setAddresses(list);
        addressMaintainence.setSystemAddress(s_systemAddress.getSystemAddress(personPk));
         
        /*
         * Check to see if this action was called under the ViewDataUtility. If so, set a flag in the 
         * addressMaintainence as "V".  This will be used to check for VDU 
         * access in the AddressMaintainence.jsp   
         * if the value is not null , do not display the add action on the Address Maintenance page
         * 
        */
        Integer affAffPk = usd.getActingAsAffiliate(); 
        if (affAffPk != null) {
            addressMaintainence.setVduFlag(new String("V"));
        }  
                     
        request.setAttribute("addressMaintainence", addressMaintainence);
        
        // Get Return button action
        
        String back = request.getParameter("back");
        if (back == null)
            back = (String)request.getAttribute("back");
        if (back == null)
            throw new ServletException("Must bass 'back' parameter or attribute to ViewAddressMaintainenceAction");
        addressMaintainence.setBack(back);
        // Set Return button action
        
        if (back.equals("PersonDetail"))  
            addressMaintainence.setReturnAction("/viewPersonDetail.action?personPk=" + personPk);
        else if (back.equals("MemberDetail"))  
            addressMaintainence.setReturnAction("/viewMemberDetail.action?personPk=" + personPk);
        else if (back.equals("ListPersons"))  
            addressMaintainence.setReturnAction("/Membership/test_ListPersons.jsp");
        else
            throw new ServletException("Invalid 'back' parameter " + back + " passed to ViewAddressMaintainence");

        return mapping.findForward("View");
	}
}
