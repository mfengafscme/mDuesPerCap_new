package org.afscme.enterprise.person.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.List;
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

/** 
 * @struts:action   path="/viewPhoneNumberInformation"
 *
 * @struts:action-forward   name="ViewPhoneNumberInformation"  path="/Membership/PhoneNumberMaintenance.jsp" 
 */
public class ViewPhoneNumberInformationAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of ViewPhoneNumberInformationAction */
    public ViewPhoneNumberInformationAction() {
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
        
        List list;
        
        Integer personPk = getCurrentPersonPk(request);
        PhoneNumberForm phoneNumberForm = new PhoneNumberForm();
        
        // Set form fields from PhoneData
        // if in data utility, do not set department
       if (usd.isActingAsAffiliate()) {
            list = (List) s_maintainPersons.getPersonPhones(personPk, usd.getDepartment());
            if (list!=null) phoneNumberForm.setPhoneData(list);
       }
       else { 
            list = (List) s_maintainPersons.getPersonPhones(personPk, usd.getDepartment());
            phoneNumberForm.setUserDepartment(usd.getDepartment());
            if (list!=null) phoneNumberForm.setPhoneData(list);
       }
        
        // debug code
        if (list==null) 
            log.debug("ViewPhoneNumberInformationAction: Phone Data=NULL");
        else {
            log.debug("Phone returned "+list.size() + " items:");
        }

         /**
         * Check to see if this action was called under the ViewDataUtility. If so, set a flag in the 
         * phoneNumberForm as "V".  This will be used to check for VDU 
         * access in the PhoneNumberMaintenance.jsp   
         * if the value is not null , do not display the add action on the Phone Number Maintenance pag
          */    
        Integer affAffPk = usd.getActingAsAffiliate(); 
        if (affAffPk != null) {
            phoneNumberForm.setVduFlag(new String("V"));
        }  
        
        // Set Return button action
        String forward = request.getParameter("back");
        if (forward.equals("PersonDetail"))  //keep this check here, don't want to open up a hole into the forward mechanism
            phoneNumberForm.setReturnAction("/viewPersonDetail.action?personPk=" + personPk);
        else if (forward.equals("MemberDetail"))  //keep this check here, don't want to open up a hole into the forward mechanism
            phoneNumberForm.setReturnAction("/viewMemberDetail.action?personPk=" + personPk);
        request.setAttribute("back", forward);

        // needed for jsp
        request.setAttribute("phoneNumber", phoneNumberForm);
        return mapping.findForward("ViewPhoneNumberInformation");
    }
    
}
