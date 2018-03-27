/*
 * viewMmberAffiliateInformation.java
 * Displays the Member Affiliate Information page
 *
 * Created on August 18, 2003, 3:16 PM
 *
 * Modified October 27, 2003 to support view data utility(VDU) data level access control
 *
 */

package org.afscme.enterprise.member.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.member.MemberData;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.member.MemberAffiliateData;

import org.afscme.enterprise.util.DBUtil;

/** 
 * @struts:action   path="/viewMemberAffiliateInformation"
 *                  input="/Membership/MemberAffiliateInformation.jsp"
 *                  name="memberAffiliateInformationForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="ViewMemberAffiliate"  path="/Membership/MemberAffiliateInformation.jsp" 
 */
public class ViewMemberAffiliateInformationAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of When updating member – affiliate information, update the Aff_Mbr_Activity table iewMemberDetailAction */
    public ViewMemberAffiliateInformationAction() 
    {
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
    throws Exception 
    {
                        
        Integer personPk = getCurrentPersonPk(request, "personPk"); // should be set if coming from tab . . or from the memberdetail page
        Integer affPk = getCurrentAffiliatePk(request, "affPk"); // These two replace the commented code below
        MemberAffiliateInformationForm maif = (MemberAffiliateInformationForm)form;
        
        Integer dept = usd.getDepartment();
        
        // get the MemberAffiliateData from the MaintainMembersBean
        MemberAffiliateData data = s_maintainMembers.getMemberAffiliateData(personPk, affPk);
        
        // set the data into the form.
        maif.setMemberAffiliateData(data);

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
                        
        }  
        
        return mapping.findForward("ViewMemberAffiliate");
    }
    
}
