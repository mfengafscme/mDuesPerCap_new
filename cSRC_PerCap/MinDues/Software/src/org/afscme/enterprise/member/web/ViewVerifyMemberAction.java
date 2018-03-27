package org.afscme.enterprise.member.web;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.member.MemberCriteria;
import org.afscme.enterprise.member.web.VerifyMemberForm;
import org.afscme.enterprise.person.PersonCriteria;
import org.apache.struts.action.ActionErrors;

/**
 * View the verify person results, based on criteria in the searchMemberForm.
 * Calls the EJB to retrieve the results, if any match. If 0 results are 
 * returned, continue straight to Member Detail Add page (MemberDetailAdd.jsp)
 * via its action (addMemberDetail). If one or more results found, forward to
 * the Verify Member Results page. Validate entry on the form first, of course.
 * New search will return the user to the Verify Person (VerifyMember.jsp). Add New
 * from this page takes the user Member Detail - Add (blank or prefilled with name)
 * whereas Add from a result row takes the user to the Member Detail - Add page with 
 * data filled in for that person
 *
 * @struts:action   path="/viewVerifyMember"
 *                  name="verifyMemberForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward name="VerifyForm" path="/Membership/VerifyMember.jsp"
 * @struts:action-forward name="VerifyResults" path="/Membership/MemberVerifyResults.jsp"
 * @struts:action-forward name="AddNew" path="/addMemberDetail.action"
 */
public class ViewVerifyMemberAction extends AFSCMEAction {

    /** Creates a new instance of ViewVerifyMemberAction */
    public ViewVerifyMemberAction() {
    }

    /**
     * Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        log.debug("ViewVerifyMemberAction: Entering");
        log.debug("ViewVerifyMemberAction: back="+request.getParameter("back"));

        VerifyMemberForm mbrForm = (VerifyMemberForm)form;
                
       //if the request contained the 'new' parameter, don't perform the search, just show the search form.
        if (request.getParameter("new") != null) {
            mbrForm.newSearch();
            return mapping.findForward("VerifyForm");
        }
                       
        //get the search criteria from the form
        MemberCriteria data = mbrForm.getMemberCriteriaData();
        
        ArrayList result = new ArrayList();
        log.debug(" VerfyMemberForm search form contents are: " + mbrForm.toString());
        
        //validate manually 
       // mbrForm.personValidate(mapping, request);
        ActionErrors errors = mbrForm.personValidate(mapping, request);
        if (errors != null && !errors.isEmpty()) {
            saveErrors(request, errors);

            //return to the Verify page if validation errors 
            return mapping.findForward("VerifyForm");
        }

        //perform the search
        int count = s_maintainMembers.getDuplicatePersonMembers(data, result );
         
        log.debug("ViewVerifyMembersAction: after getDuplicatePersonMembers call count is: " + count);
         
        
        //show the correct page
        switch (count) {
            case 0:
                // go direct to adding a member
                return mapping.findForward("AddNew");
           
            default:  // i.e. more than one row returned in result
                //put the search result in the form
                request.setAttribute("firstNm", mbrForm.getFirstNm());
                request.setAttribute("lastNm", mbrForm.getLastNm());
                request.setAttribute("suffixNm", mbrForm.getSuffixNm());
                request.setAttribute("ssn", mbrForm.getSsn());
                log.debug("ViewVerifyMemberAction: ssn="+mbrForm.getSsn());
                mbrForm.setResults(result);
                mbrForm.setTotal(count);
                return mapping.findForward("VerifyResults");
        }     //case           

        
   } // perform method

} //class
