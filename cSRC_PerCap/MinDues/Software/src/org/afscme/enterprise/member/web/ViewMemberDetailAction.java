/*
 * viewMemberDetailAction.java
 * This action retrieves the member detail data for the person selected,
 * sets it in the MemberDetailForm and then forwards to the jsp to
 * display the Member Detail page.
 *
 * Modified to check and see if the user is operating under the Affiliate
 * view data utility(vdu), if so, calls getMemberDetail() with a reference
 * to the Collection of affiliates in the members current vdu affiliate
 * hierarchy. If not under the vdu, a null Collection is passed.
 *
 *
 * Created on June 4, 2003, 3:16 PM
 */

package org.afscme.enterprise.member.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.log4j.Logger;

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

import org.afscme.enterprise.util.DBUtil;

/**
 * @struts:action   path="/viewMemberDetail"
 *
 * @struts:action-forward   name="ViewMemberDetail"  path="/Membership/MemberDetail.jsp"
 */
public class ViewMemberDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    private static Logger logger = Logger.getLogger(ViewMemberDetailAction.class);
    
    /** Creates a new instance of When updating member – affiliate information, update the Aff_Mbr_Activity table iewMemberDetailAction */
    public ViewMemberDetailAction() {
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
        
        Integer dept = usd.getDepartment();
        /* Affiliate View Data Utility related functionality */
        
        Integer affPk = usd.getActingAsAffiliate();
        Collection vduAffiliates = null;
        if (affPk != null) {
            vduAffiliates = usd.getAccessibleAffiliates();
            logger.debug("ViewBasicMemberCriteriaAction - affPk set from usd.getActingAsAffiliate not null : " + affPk +
            " and usd.getAccessibleAffiliates is : " + usd.getAccessibleAffiliates());
        }
        
        MemberDetailForm mdf = new MemberDetailForm();
        MemberData data = s_maintainMembers.getMemberDetail(personPk, dept, vduAffiliates);
        
        // Set form fields from PersonData
        mdf.setMemberData(data);
        mdf.setPersonAddressRecord(s_systemAddress.getSystemAddress(personPk));
        
        
        // needed for header and footer tags
        setCurrentPerson(request, personPk);  //remove this later
        
        // needed for jsp
        request.setAttribute("memberDetail", mdf);
        return mapping.findForward("ViewMemberDetail");
    }
    
}
