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

import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.member.web.VerifyMemberForm;
import org.afscme.enterprise.member.web.MemberDetailAddForm;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.TextUtil;

/* This action dsiplays one or more versions of the Member Detail - Add screen , initially when called 
 * from the main menu or Member search results pages - note there is at least one other version that is called 
 * from the add member affiliation flow 
 * first remove any current person set in the session - start over, EH?
 * Next, if the form came from Verify Member Screen along its path, set any first name, last name, ssn, parameters
 * into the memberDetailAddForm. Also need to set the back parameter on the form, so that on cancel, the user will be
 * returned to the correct page (main menu or ViewVerifyResults). This action does not handle the cancel, 
 * ViewVerifyMemberAction does. .  
 */
 
/**
 * @struts:action   path="/addMemberDetail"
 *                  name="memberDetailAddForm"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="AddMember"  path="/Membership/MemberDetailAdd.jsp"
 * @struts:action-forward   name="VerifyResults"  path="/Membership/MemberVerifyResults.jsp" 
 *
 */
public class AddMemberDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of AddPersonDetailAction */
    public AddMemberDetailAction() {
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
                                 HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception 
   {                
        Integer personPk;
        HttpSession session = request.getSession();
        MemberDetailAddForm mdaf = new MemberDetailAddForm();

        // Remove any current person - start over
        setCurrentPerson(request,  null);

        log.debug("AddMemberDetailAction: cancel ="+request.getParameter("cancel"));
        log.debug("AddMemberDetailAction: back from request parameter="+request.getParameter("back"));
        log.debug("AddMemberDetailAction: back from session="+session.getAttribute("back"));
        
        if (request.getParameter("cancel") != null) // handle the cancel from Duplicate SSN
        { 
            mdaf = (MemberDetailAddForm)session.getAttribute("memberDetailAddForm");
            return mapping.findForward("AddMember");
        }
        
         if (session.getAttribute("verifyMemberForm") != null) 
         {
             //set back atttribute, set carry forward values and then forward to AddMember jsp
             VerifyMemberForm vmf = (VerifyMemberForm) session.getAttribute("verifyMemberForm");
             
             log.debug("AddMemberDetailAction: inside verifyMemberForm != null");
                          
             mdaf.setFirstNm(vmf.getFirstNm());
             mdaf.setLastNm(vmf.getLastNm());
             mdaf.setSuffixNm((Integer)vmf.getSuffixNm());
             mdaf.setSsn(vmf.getSsn());
             AffiliateIdentifier affId = new AffiliateIdentifier();
             if (!TextUtil.isEmptyOrSpaces(vmf.getAffCode()))  affId.setCode(new Character(vmf.getAffCode().toCharArray()[0] ) );
             if (!TextUtil.isEmptyOrSpaces(vmf.getAffType()))  affId.setType(new Character(vmf.getAffType().toCharArray()[0] ) );
             affId.setLocal(vmf.getAffLocalSubChapter());
             affId.setCouncil(vmf.getAffCouncilRetireeChap());
             affId.setState(vmf.getAffStateNatType());
             affId.setSubUnit(vmf.getAffSubUnit());
             mdaf.setTheAffiliateIdentifier(affId);
             // set this mdaf object into session (replacing new MemberDetailAddForm, with data set 
             session.setAttribute("memberDetailAddForm", mdaf);
         }
         else if (session.getAttribute("searchMembersForm") != null)
         {
            SearchMembersForm smForm = (SearchMembersForm )session.getAttribute("searchMembersForm");
            log.debug("AddMemberDetailAction: inside searchMembersForm != null");
			           
            mdaf.setFirstNm(smForm.getFirstNm());
            mdaf.setMiddleNm(smForm.getMiddleNm());
            mdaf.setLastNm(smForm.getLastNm());
            mdaf.setSuffixNm((Integer)smForm.getSuffixNm());
            mdaf.setSsn(smForm.getSsn());
            mdaf.setState(smForm.getState());
            AffiliateIdentifier affId = new AffiliateIdentifier();
            if (!TextUtil.isEmptyOrSpaces(smForm.getAffCode()))  affId.setCode(new Character(smForm.getAffCode().toCharArray()[0] ) );
            if (!TextUtil.isEmptyOrSpaces(smForm.getAffType()))  affId.setType(new Character(smForm.getAffType().toCharArray()[0] ) );
            affId.setLocal(smForm.getAffLocalSubChapter());
            affId.setCouncil(smForm.getAffCouncilRetireeChap());
            affId.setState(smForm.getAffStateNatType());
            affId.setSubUnit(smForm.getAffSubUnit());
            mdaf.setTheAffiliateIdentifier(affId);
            // set this mdaf object into session (replacing new MemberDetailAddForm, with data set 
            session.setAttribute("memberDetailAddForm", mdaf);          
         }
         else { // interface verifyMemberForm attribute is null
            // return will be to main menu, so set back parameter and forward to AddMember jsp                         
            log.debug("AddMemberDetailAction: inside else (i.e. verifyMemberForm == null");                     
             
         }
         return mapping.findForward("AddMember");        
            
      
    } // perform method
} // class
