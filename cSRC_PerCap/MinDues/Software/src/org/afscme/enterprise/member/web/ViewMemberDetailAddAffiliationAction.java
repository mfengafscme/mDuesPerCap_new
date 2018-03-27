/*
 * viewMemberDetailAddAffiliationAction.java
 * Displays the Member Detail - Add page version 2 (or Member Detail Add Affiliation page
 * when called from the Person (Member) Verify results page. Uses getMemberDetail method 
 * get data to populate the form. Sets the "add path"
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
 * @struts:action   path="/viewMemberDetailAddAffiliation"
 *                  input="/Membership/MemberDetailAddAffiliation.jsp"
 *                  name="memberDetailAddForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="ViewMemberAddAffiliation"  path="/Membership/MemberDetailAddAffiliation.jsp" 
 */
public class ViewMemberDetailAddAffiliationAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    
    public ViewMemberDetailAddAffiliationAction() 
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
        // in this case we want the request.parameter                
        Integer personPk = getCurrentPersonPk(request, "personPk"); // should be set if coming from tab . . or from the memberdetail page
                
        MemberDetailAddForm mdaf = (MemberDetailAddForm)form;
        
        Integer dept = usd.getDepartment();
        
        // get the MemberAffiliateData from the MaintainMembersBean
        PersonData data = s_maintainPersons.getPersonDetail(personPk, dept);
        
        log.debug("ViewMemberDetailAddAffiliationAction personPk from PersonData is : " + data.getPersonPk());
        
        // set the data into the form. note that uses a different set data method, because the page will display with all existing person data filled in
        mdaf.setPersonDetailData(data);
        mdaf.setPersonAddressRecord(s_systemAddress.getSystemAddress(personPk));
        
        mdaf.setBack("VerifyResults");
        log.debug("ViewMemberDetailAddAffiliationAction personPk from memberDetailAddForm is : " + mdaf.getPersonPk());
        
        // put this in the sessopn, so all de attributes will be der
        request.getSession().setAttribute("memberDetailAddForm", mdaf);    
        
        return mapping.findForward("ViewMemberAddAffiliation");
    }
    
}

