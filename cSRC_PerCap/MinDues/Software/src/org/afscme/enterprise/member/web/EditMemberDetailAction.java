package org.afscme.enterprise.member.web;

/**
 * This action gets the Member Detail data and then Displays the Member Detail - Edit
 * page. Code has been added to support data level access control under the Affiliate
 * View Data Utility(vdu)
 *
 */

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

/**
 * @struts:action   path="/editMemberDetail"
 *                  name="memberDetailForm"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="EditMember"  path="/Membership/MemberDetailEdit.jsp"
 * @struts:action-forward   name="CancelEditMember"  path="/Membership/MemberDetail.jsp" 
 */
 public class EditMemberDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of EditPersonDetailAction */
     public EditMemberDetailAction() {
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
        MemberDetailForm mdf = (MemberDetailForm)form ;
       // MemberDetailForm mdf = new MemberDetailForm();

        log.debug("EditMemberDetailAction: Entering" + "personPk is : " + personPk);

        /* following code is added to support data level access control under the view data utility(vdu)
         * first check to see if the user is in the vdu , if so, getActingAsAffiliate will return 
         * non-null affiliate. If so, getAccessibleAffiliates (the affiliate hierarchy for this user)
         * and pass to getMemberDetail() which in turn will pass it to, getMemberAffiliateSummary() 
         * which will restrict the query. If not in the vdu, the null Collection is passed
         */
        Integer affPk = usd.getActingAsAffiliate(); 
        Collection vduAffiliates = null;
        if (affPk != null) {
         vduAffiliates = usd.getAccessibleAffiliates();
            log.debug("ViewBasicMemberCriteriaAction - affPk set from usd.getActingAsAffiliate not null : " + affPk + 
            " and usd.getAccessibleAffiliates is : " + usd.getAccessibleAffiliates());
        }

        // Set form fields from MemberData
        mdf.setMemberData(s_maintainMembers.getMemberDetail(personPk, usd.getDepartment(), vduAffiliates));
        mdf.setPersonAddressRecord(s_systemAddress.getSystemAddress(personPk));
             
        // Set the Comments to blank
        mdf.setComment("");
        
        // needed for jsp
        request.getSession().setAttribute("memberDetailForm", mdf);
        return mapping.findForward("EditMember");
    }
    
}
