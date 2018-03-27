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
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.Exception;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.member.MemberErrorCodes;
import org.afscme.enterprise.member.MemberAffiliateResult;
import org.afscme.enterprise.address.ejb.SystemAddress;
import org.afscme.enterprise.address.Address;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.member.NewMember;
import org.afscme.enterprise.affiliate.web.AffiliateFinderForm;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;

/*
 *
 * Implements the SaveMemberAddAffiliation action. Supports, Save, Save and Add another
 * and cancel from the Member Detail - Add (version) or Member Detail Add Affiliation
 * page - for now, this path from the Verify Person (Member) Results page. Perhaps it
 * can be use for the path from the Member Detail (add action)
 *
 * Checks to see if action is save or save and add another, if save and
 * add another, sets "another" request parameter into session. Next checks to
 * see if the user was presented with the duplicate SSN , and that they selected
 * the continue "action" - if so, validate manually, including calling the SystemAddress
 * ejb validate method and passing it the address entered by the user. If there are
 * errors, stick them in the session , and call mapping.getInputForward to return
 * the user to the add page in order that they may correct their errors.
 *
 * For this page, since the person/member being added is already a person, no check
 * for duplicate persons is performed. The ssn should not have been changed, however,
 * to be consistent, we'll do the dup ssn check so that the user can see all of the
 * dup ssn's that they have.
 *
 * continue on
 * check to see if one or more duplicate SSN exist for this data. If so, send the
 * user to the verifyDuplicateSSN screen (if the continue parameter has NOT been
 * set. Finally call the MaintainMember ejb to execute the add (addMember). Process
 * any errors returned and return the user to different pages (potentially) based on
 * the error.
 *
 */


/**
 * @struts:action   path="/saveMemberDetailAddAffiliation"
 *                  name="memberDetailAddForm"
 *                  scope="session"
 *                  validate="false"
 *                  input="/Membership/MemberDetailAddAffiliation.jsp"
 *
 * @struts:action-forward   name="PersonDuplicateSSN"  path="/viewDuplicateSSNNotifierPerson.action"
 * @struts:action-forward   name="MemberAdd"  path="/saveMemberDetailAddAffiliation.action"
 *  @struts:action-forward   name="VerifyMember"  path="/verifyMember.action"
 * @struts:action-forward   name="MemberView"  path="/viewMemberDetail.action"
 * @struts:action-forward   name="VerifyResults"  path="/Membership/MemberVerifyResults.jsp"
 * @struts:action-forward   name="MemberAddPage"  path="/Membership/MemberDetailAddAffiliation.jsp"
 */
public class SaveMemberDetailAddAffiliationAction extends AFSCMEAction {
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet request that is being processed
     * @param usd Security data for the user performing this action
     *
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response, UserSecurityData usd)
                                throws Exception {
        
        HttpSession session = request.getSession();
        log.debug("SaveMemberDetailAction: cancel ="+request.getParameter("cancel"));
        log.debug("SaveMemberDetailAction: another ="+request.getParameter("another"));
        log.debug("SaveMemberDetailAction: back from request ="+request.getParameter("back"));
        log.debug("SaveMemberDetailAction: back from session ="+session.getAttribute("back"));
        
        //  MemberDetailAddForm memberDetailAddForm = (MemberDetailAddForm)form;
        //  Get the Form out of the session
        MemberDetailAddForm memberDetailAddForm = (MemberDetailAddForm) session.getAttribute("memberDetailAddForm");
        
        Integer personPk;
       /*
        * the affPk should only be in the request parameters if the user entered a partial affiliate identifier to
        * which multiple matches are made and has selected an affiliate from the dup affiliate results screen.
        */
        String saffPk = (String)request.getParameter("affPk");
        if (!TextUtil.isEmptyOrSpaces(saffPk)) {
            memberDetailAddForm.setAffPk(new Integer(saffPk));
            session.setAttribute("memberDetailAddForm", memberDetailAddForm);
        }        
        
        int resultsCount=0;
        
        //first check to see if the user canceled from the Member Detail - Add Affiliation page
        if (request.getParameter("cancel") != null) {
            if (request.getSession().getAttribute("verifyMemberForm") != null)
                resultsCount = (((VerifyMemberForm)request.getSession().getAttribute("verifyMemberForm")).getTotal());

            log.debug("SaveMemberDetailAddAffiliationAction: inside if (cancel != null) resultscount= " + resultsCount);
            // clean up add form
            session.removeAttribute("memberDetailAddForm");
            
            //path is add member affiliation for existing person
            if (resultsCount > 0) {        
                //first remove the MemberDetailAddFormn from the session if it exists there
                session.removeAttribute("memberDetailAddForm");
                return mapping.findForward("VerifyResults");            
            }
            //path is add existing person with no duplicate verify results or came from the MemberDetail page
            else {       
                //first remove the MemberDetailAddForm from the session if it exists there
                session.removeAttribute("memberDetailAddForm");
                return mapping.findForward("MemberView");
            }
        }
                
        
        /*
         If the process has not gone through Duplicate SSN checking, do the page validation first
         */
        
        if (request.getParameter("continue") == null) { 
            
            // HLM: Fix defect #159. Need to go to this block for form validation
            // && (request.getParameter("another") == null || request.getParameter("another").equals(""))) {  
            
            //validate manually - using validation tailored to add affiliation
            ActionErrors errors = memberDetailAddForm.validateForAddAffiliation(mapping, request); // regular form validation
            // address validation not needed, since address can not be entered
            
            if (errors != null && !errors.isEmpty()) {
                saveErrors(request, errors);
                
                //return to the Add page if validation errors
                return mapping.findForward("MemberAddPage");
            }
            
            
            // no check for Duplicate SSN since SSN is not editable on that page. . . . .
            //            if (s_maintainPersons.isDuplicateSSN(memberDetailAddForm.getSsn()))
            //            {
            //                // set as sesison Attribute to support the shared duplicate ssn processing code
            //                session.setAttribute("back", "MemberAddAffiliation");
            //                session.setAttribute("memberDetailAddForm", memberDetailAddForm); // set current values in session if needed for cancel
            //                return mapping.findForward("PersonDuplicateSSN");
            //            }
            
        } // if continue == null
        
        /*
         * handle the case where a partial affiliate identifier is typed in and either no affiliate
         * matches, an error, or multiple match, in which case the Affiliate Identifier Finder Results
         * in the case that the user has used the affiliate finder and an affPk has already been set,
         * but the user may have modified the affiliate identifier, performing the findAffiliatesWithID
         * "check" should a single affiliate
         */
        
        Collection affiliates = findAffiliatesWithID(memberDetailAddForm.getTheAffiliateIdentifier());
        
        if ( memberDetailAddForm.isSearched() == false | TextUtil.isEmpty(memberDetailAddForm.getAffPk()) ) {
            if (affiliates == null) {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.affiliate.affId.notFound"));
                saveErrors(request, errors);
                return mapping.findForward("MemberAddPage");
                
            } else if (affiliates.size() == 1 && memberDetailAddForm.getAffPk() == null) {
                /* one affiliate exists for the affiliate identifier, but the affPk was not set so
                get the affPk for this affiliate identifier. . .
                 */
                memberDetailAddForm.setAffPk(s_maintainAffiliates.getAffiliatePk(memberDetailAddForm.getTheAffiliateIdentifier()));
                
            } else if (affiliates.size() > 1) {
                /* In this case we assume that even if the affiliate finder was used to set an affiliate Pk, the user
                 * has modified the affiliate identifier, such that it returns multiple affiliates. The user will
                 * be presented with a list of affiliates
                 */
                setCurrentAffiliateFinderForm(request, memberDetailAddForm.getTheAffiliateIdentifier().getCode(),
                memberDetailAddForm.getTheAffiliateIdentifier().getCouncil(),
                memberDetailAddForm.getTheAffiliateIdentifier().getLocal(),
                memberDetailAddForm.getTheAffiliateIdentifier().getState(),
                memberDetailAddForm.getTheAffiliateIdentifier().getSubUnit(),
                memberDetailAddForm.getTheAffiliateIdentifier().getType(),
                "/saveMemberDetailAddAffiliation.action",
                ""
                );
                
                // set searched boolean to true, so that we know if the affPk is not null and searched is true, then the user has selected an affiliate from the dup screen
                memberDetailAddForm.setSearched(true);
                
                
                //set in the global forwards, so not defined in this action . . .
                return mapping.findForward("SearchAffiliateFinderRedirect");
            } // if affiliates >= 1
            
        } // if isSearched == false
        /*
         *   by now we've processed a cancel and performed "first time save" (e.g. validation) logic.
         *   the action (left) is either save or save and add another. the following logic performs the
         *  save, processes any save errors and handles the which page to forward to if
         * the operation is save and add another
         *
         */
        
        /*
         * The following logic checks to see if the user is accessing the system through the view data utility
         * (vdu) If so, the Set of accessible AffPks for the affiliate they are logged in as, is determined, and
         * the affPk associated with the member affiliation is determined from the affilaite identifier and that
         * affPk is then checked against the accessible affPks to determine if the user can make the add or not
         */
        
        Integer vduAffPk = usd.getActingAsAffiliate(); //get the affiliate the user is logged into the vdu as
        
        if (vduAffPk != null) {
            Set accessibleAffs = usd.getAccessibleAffiliates();
            log.debug("SaveMemberDetailAddAffiliationAction - affPk set from usd.getActingAsAffiliate not null : " + vduAffPk +
                        " and usd.getAccessibleAffiliates is : " + usd.getAccessibleAffiliates());
            
            Integer affAffPk; // set the affiliation AffPk - should already be set, but just in case
            if (memberDetailAddForm.getAffPk() == null)
                affAffPk = s_maintainAffiliates.getAffiliatePk(memberDetailAddForm.getTheAffiliateIdentifier());
            else affAffPk = memberDetailAddForm.getAffPk();
            
            if (accessibleAffs != null) { // should always contain one entry if vduAffPk != null
                Iterator iter = accessibleAffs.iterator();
                boolean found = false;
                while(iter.hasNext()) {
                    Integer accAffPk = (Integer) iter.next();
                    log.debug("SaveMemberDetailAddAffiliation Inside loop on accessible affPks, affiliation affPk is : "
                    + affAffPk + " accessible affPk is : " + accAffPk);
                    if (accAffPk.equals(affAffPk)) found = true;
                    
                }
                if (found == false) { // if after the iteration, the affAffPk is not found, this is a security violation
                    ActionErrors errors = new ActionErrors();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.vdu.invalidAffiliate"));
                    saveErrors(request, errors);
                    return mapping.findForward("MemberAddPage");
                    
                }                
            }            
        }
        
        AffiliateData affiliateData = s_maintainAffiliates.getAffiliateData(memberDetailAddForm.getAffPk());
        if (affiliateData.getStatusCodePk().equals(AffiliateStatus.AC) ||
            affiliateData.getStatusCodePk().equals(AffiliateStatus.PD)
            || affiliateData.getStatusCodePk().equals(AffiliateStatus.D)) {
            // do not add affiliate
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.addmember.cannotBeAdded"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        
        Collection memberAffiliations = s_maintainMembers.getMemberAffiliation(memberDetailAddForm.getPersonPk());
        Iterator i = memberAffiliations.iterator();
        while(i.hasNext()) {
            MemberAffiliateResult mar = (MemberAffiliateResult)i.next();
            // duplicate affiliate is being inserted
            if(mar.getAffPk().equals(memberDetailAddForm.getAffPk())) {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.addMemberAffiliate.dupe"));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }
        }
        
        
        // create a new member object to pass by reference
        NewMember nMember = memberDetailAddForm.getDataForAffiliation();
        //call the business method to add member (& person) detail information
        int rc = s_maintainMembers.addMemberAffiliateData( nMember, usd.getPersonPk() );
        
        
        if (rc == MemberErrorCodes.BARRED_MEMBER) {
            // tried to add a person as a member of an affiliate who is barred
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.addmember.memberBarred"));
            return mapping.getInputForward();
        }
        
        log.debug("SaveMemberDetailAction: another ="+request.getParameter("another"));
        
        // in case the user decided to save and add another go back to add page
        if (request.getParameter("another") != null && request.getParameter("another").equals("yes")) {     // go back to verify after submit
            log.debug("SaveMemberDetailAddAction: save and add another processing");
            log.debug("SaveMemberDetailAction: another ="+request.getParameter("another"));
            log.debug("SaveMemberDetailAction: back from request ="+request.getParameter("back"));
            log.debug("SaveMemberDetailAction: back from session ="+session.getAttribute("back"));
            
            // Clean Up
            session.removeAttribute("memberDetailAddForm");
            
            // leave the existing VerifyMemberForm in the session
            // blank out some fields, first name, last name, ssn, dob but leave affiliate identifier
            VerifyMemberForm vmf = (VerifyMemberForm)session.getAttribute("verifyMemberForm");
            if(vmf != null) {
                vmf.setSsn1("");
                vmf.setSsn2("");
                vmf.setSsn3("");
                vmf.setFirstNm("");
                vmf.setLastNm("");
                vmf.setSuffixNm(null);
                vmf.setDob("");
            }
            
            return mapping.findForward("VerifyMember");
        }
        else {  // i.e. another == null, so "save" action
            log.debug("SaveMemberDetailAddAction: save processing");
            log.debug("SaveMemberDetailAction: another ="+request.getParameter("another"));
            
            // not needed for header and footer tags as the person is not changing . . .
            // setCurrentPerson(request, personPk);
            // setCurrentAffiliate(request, affPk);
            /* maybe needed because Current Affiliate not cleaned up when hit main menu. .
            and needed from Member Affiliate Tab */
            
            // remove the VerifyMemberForm - all done
            session.removeAttribute("verifyMemberForm" );
            // clean up add member form
            session.removeAttribute("memberDetailAddForm");
            
            // go to view member detail once everything is saved
            return mapping.findForward("MemberView");
        }
    }
}
