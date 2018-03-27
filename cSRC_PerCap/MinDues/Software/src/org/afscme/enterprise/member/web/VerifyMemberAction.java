package org.afscme.enterprise.member.web;

import java.util.List;
import java.util.LinkedList;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.member.web.VerifyMemberForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.person.PersonCriteria;
import org.afscme.enterprise.person.PersonResult;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.TextUtil;


/**
 * Part of the Verify person with member functionality flow.
 * Display the Veify Person page (VerifyMember.jsp) with add member functionality.
 * Set up to use VerifyMemberForm as the form and search functionality that is similar to ,
 * but augmented from the getDuplicatePerson functionality. . . .
 * This action does some clean up work, to account for the case when a user returns to the
 * Verify Member page from the Member Verify Results page
 *
 * @struts:action   path="/verifyMember"
 *                  input="/Membership/VerifyMember.jsp"
 *                  name="verifyMemberForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward name="VerifyForm" path="/Membership/VerifyMember.jsp"
 * @struts:action-forward name="VerifyResults" path="/Membership/MemberVerifyResults.jsp"
 *
 */
public class VerifyMemberAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        log.debug("VerifyMemberAction: back="+request.getParameter("back"));
        HttpSession session = request.getSession();
        VerifyMemberForm mbrForm = (VerifyMemberForm)form;   

        // Remove any current person - start over
        setCurrentPerson(request,  null);
        
        if (request.getParameter("new") != null) { //may exist but we want to reset
            clear(request);
            mbrForm.newSearch();
        } else {
            mbrForm.newVerifyMemberForm();
        }
        
        //reset the page parms, in case the form already existed, no longer valid from last search
        mbrForm.setPage(0);
        mbrForm.setTotal(0);
        mbrForm.setSortBy("");
        
        /*
         * Check to see if this action was called under the ViewDataUtility. If so, set a flag in the VerifyMemberForm
         * as "S" or set the vduAffiliates collection. This will be used to check for VDU access in the VerifyMemberResults
         * (Verify Person page) and turn off the view action in the results
         */
        Integer affPk = usd.getActingAsAffiliate();
        if (affPk != null) {
            //  mbrForm.setVduFlag(new String("S"));
            mbrForm.setVduAffiliates(usd.getAccessibleAffiliates());
            log.debug("VerifyMemberAction - affPk set from usd.getActingAsAffiliate not null : " + affPk +
            " and mbrForm.getVduAffiliates is : " + mbrForm.getVduAffiliates());
        }
        
        
        if (request.getParameter("back") != null) {
            //   session.setAttribute("back", request.getParameter("back"));
            session.removeAttribute("memberDetailAddForm");
            session.removeAttribute("verifyMemberForm");
        }
        if (request.getParameter("cancel") != null) {       //cancel brought me here
            session.removeAttribute("cancel");
            session.removeAttribute("memberDetailAddForm");
        }
        
        // added to ensure setVduAffiliates gets set
        session.setAttribute("verifyMemberForm", mbrForm);
        return mapping.findForward("VerifyForm");
        
    } // method perform
    
    
    public void clear(HttpServletRequest req) {
        if (req.getSession().getAttribute("verifyMemberForm") != null) req.getSession().removeAttribute("verifyMemberForm");
        VerifyMemberForm vmf = new VerifyMemberForm();
        
        req.getSession().setAttribute("verifyMemberForm", vmf);
    } // method clear                   
} // class
