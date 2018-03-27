package org.afscme.enterprise.member.web;

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
import org.afscme.enterprise.member.MemberData;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.member.EmployerData;

/* This action displays the EmployerInformation - Edit page. It first retrieves
 * the current Employer Information for this member in this affiliate, 
 * populates the form, sets the form in the request and then forwards to the 
 * EMployerInformationEdit.JSP
 */


/**
 * @struts:action   path="/editEmployerInformation"
 *                  name="employerInformationForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="EditEmployer"  path="/Membership/EmployerInformationEdit.jsp"
 * @struts:action-forward   name="CancelEditEmployer"  path="/Membership/EmployerInformation.jsp" 
 */
 public class EditEmployerInformationAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of EditPersonDetailAction */
     public EditEmployerInformationAction() {
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
        Integer affPk = getCurrentAffiliatePk(request, "affPk");
       // MemberAffiliateInformationForm maif = new MemberAffiliateInformationForm();
        EmployerInformationForm eif = (EmployerInformationForm) form ;

        log.debug("EditEmployerInformation:  Entering" + "personPk is : " + personPk + "affPk is : " + affPk);

        // Set form fields from MemberData
        eif.setEmployerData(s_maintainMembers.getAffiliateEmployerData(personPk, affPk));
        // set affPk and personPk in case there was no record in the database
        eif.setAffPk(affPk);
        eif.setPersonPk(personPk);
        
 
        return mapping.findForward("EditEmployer");
    }
    
}
