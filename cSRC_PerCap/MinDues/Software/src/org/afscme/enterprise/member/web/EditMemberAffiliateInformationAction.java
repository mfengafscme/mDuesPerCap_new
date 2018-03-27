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

/* This action displays the MemberAffiliateInformation - Edit page. It first retrieves
 * the current Member Affiliate Information for this member in this affiliate, 
 * populates the form, sets the form in the session  and then forwards to the 
 * MemberAffiliateInformationEdit.JSP - modified to support data access control rules
 *  under the view data utility(vdu) - if vdu set flag in maif form so that the
 * jsp can check and not display no public employee and no legislative mail flags
 * under vdu
 */


/**
 * @struts:action   path="/editMemberAffiliateInformation"
 *                  name="memberAffiliateInformationForm"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="EditMemberAff"  path="/Membership/MemberAffiliateInformationEdit.jsp"
 * @struts:action-forward   name="CancelEditMemberAff"  path="/Membership/MemberAffiliateInformation.jsp" 
 */
 public class EditMemberAffiliateInformationAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of EditPersonDetailAction */
     public EditMemberAffiliateInformationAction() {
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
        MemberAffiliateInformationForm maif = (MemberAffiliateInformationForm)form ;

        log.debug("EditMemberAffiliateInformation:  Entering" + "personPk is : " + personPk + "affPk is : " + affPk);

        // Set form fields from MemberData
        maif.setMemberAffiliateData(s_maintainMembers.getMemberAffiliateData(personPk, affPk));
        AffiliateIdentifier affId = getCurrentAffiliateId(request);
        maif.setAffType(affId.getType().toString() ); // needed for business rules
   
        /*
         * Check to see if this action was called under the ViewDataUtility. If so, set a flag in the 
         * MemberAffiliateInformationForm maif as "V".  This will be used to check for VDU 
         * access in the MemberAffiliateInformation.jsp and MemberAffiliateInformationEdit.jsp  
         * if the value is not null , do not display the fields no public employee and no legislative mail in 
         * either page
        */
        Integer affAffPk = usd.getActingAsAffiliate(); 
        if (affAffPk != null) {
            maif.setVduFlag(new String("V"));
            log.debug("VerifyMemberAction - affAffPk set from usd.getActingAsAffiliate not null : " + affAffPk + 
               " maif.getVduFlag() is : " + maif.getVduFlag() );
             
        }  
        
        log.debug("EditMemberAffiliateInformation:  memberAfifliateForm affType is : " + maif.getAffType() );      
   
        // needed for edit, because of collection introduced for officer info
        request.getSession().setAttribute("memberAffiliateInformationForm", maif);
        return mapping.findForward("EditMemberAff");
    }
    
}
