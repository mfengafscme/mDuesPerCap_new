/*
 * viewMmberAffiliateInformation.java
 *
 * Created on August 18, 2003, 3:16 PM
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

/* Executes the addToWeeklyCardRun method in the maintain members EJB to add the member 
 * for this affiliate to the weekly card run table
 */

/** 
 *
 * @struts:action   path="/generateMbrCard"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="MemberAff"  path="/viewMemberAffiliateInformation.action" 
 */
public class GenerateMbrCardAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    
    public GenerateMbrCardAction() 
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
                
        // get the MemberAffiliateData from the MaintainMembersBean
        s_maintainMembers.addToWeeklyCardRun(personPk, affPk);
                        
         return mapping.findForward("MemberAff");
    }
    
}
