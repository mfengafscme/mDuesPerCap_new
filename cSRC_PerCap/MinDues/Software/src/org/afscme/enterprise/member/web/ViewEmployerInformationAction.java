/*
 * viewEmployerInformationAction.java
 *
 * Created on August 27, 2003, 6:16 PM - on a plane sitting on the Tarmac
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
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.member.EmployerData;

import org.afscme.enterprise.util.DBUtil;

/** 
 * @struts:action   path="/viewEmployerInformation"
 *                  input="/Membership/EmployerInformation.jsp"
 *                  name="employerInformationForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="ViewEmployerInfo"  path="/Membership/EmployerInformation.jsp" 
 */
public class ViewEmployerInformationAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of When updating member – affiliate information, update the Aff_Mbr_Activity table iewMemberDetailAction */
    public ViewEmployerInformationAction() 
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
        EmployerInformationForm eif = (EmployerInformationForm)form;
        
        Integer dept = usd.getDepartment();
        
        // get the MemberAffiliateData from the MaintainMembersBean
        EmployerData data = s_maintainMembers.getAffiliateEmployerData(personPk, affPk);
        
        // set the data into the form.
        eif.setEmployerData(data);
                
        return mapping.findForward("ViewEmployerInfo");
    }
    
}
