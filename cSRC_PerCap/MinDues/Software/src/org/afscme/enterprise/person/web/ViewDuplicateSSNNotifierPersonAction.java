package org.afscme.enterprise.person.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.member.web.MemberDetailAddForm;
import org.afscme.enterprise.member.web.MemberDetailForm; 
import org.afscme.enterprise.member.web.VerifyMemberForm;
import org.afscme.enterprise.organization.web.OrganizationAssociateDetailForm;
import org.afscme.enterprise.person.PersonCriteria;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.member.web.VerifyMemberForm; 

/** 
 * @struts:action   path="/viewDuplicateSSNNotifierPerson"
 *                  name="verifyPersonForm"
 *                  validate="false"
 *                  scope="request"
 *
 *
 * @struts:action-forward   name="ViewDetail"  path="/Membership/PersonDetail.jsp" 
 * @struts:action-forward   name="ViewDuplicates"  path="/Membership/DuplicateSSNNotifier.jsp" 
 */
public class ViewDuplicateSSNNotifierPersonAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of ViewDuplicateSSNNotifierPersonAction */
    public ViewDuplicateSSNNotifierPersonAction() {
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

        VerifyPersonForm verifyPersonForm = (VerifyPersonForm)form;
        request.setAttribute("verifyPersonForm", verifyPersonForm);
        request.setAttribute("back", request.getAttribute("back"));
        
        HttpSession session = request.getSession();
        log.debug("ViewDuplicateSSNNotifierPerson: back="+session.getAttribute("back"));
        List result = new LinkedList();
        PersonCriteria data = verifyPersonForm.getPersonCriteriaData();

        if (session.getAttribute("back") != null) {
            if (session.getAttribute("back").equals("PersonAdd")) {
                //get the search criteria from the form
                PersonDetailAddForm personDetailAddForm = (PersonDetailAddForm)request.getAttribute("personDetailAddForm");
                data.setSsn(personDetailAddForm.getSsn()); 
                log.debug("ViewDuplicateSSNNotiferPersonAction:PersonAdd ssn="+data.getSsn());
                request.setAttribute("verifyPersonForm", verifyPersonForm);
            }else if (session.getAttribute("back").equals("PersonDetail")) {
                //get the search criteria from the form
                PersonDetailForm personDetailForm = (PersonDetailForm) request.getAttribute("personDetailForm");
                data.setSsn(personDetailForm.getSsn());

                log.debug("ViewDuplicateSSNNotiferPersonAction:PersonDetail ssn="+data.getSsn());

                //reset the page parms
                verifyPersonForm.setPage(0);
                verifyPersonForm.setTotal(0);
                verifyPersonForm.setSortBy("personNm");
            } else if (session.getAttribute("back").equals("MemberAdd")) {
                MemberDetailAddForm mdaf = (MemberDetailAddForm)session.getAttribute("memberDetailAddForm");
                data.setSsn(mdaf.getSsn());
                log.debug("ViewDuplicateSSNNotiferPersonAction:MemberAdd ssn="+data.getSsn());
                
                //reset the VerifyPersonForm page parms, as Member paths use the MemberVerifyForm instead. . . 
                verifyPersonForm.setPage(0);
                verifyPersonForm.setTotal(0);
                verifyPersonForm.setSortBy("personNm");

                request.setAttribute("verifyPersonForm", verifyPersonForm);  
             } else if (session.getAttribute("back").equals("MemberEdit")) {
                MemberDetailForm mdf = (MemberDetailForm)session.getAttribute("memberDetailForm");
                data.setSsn(mdf.getSsn());
                log.debug("ViewDuplicateSSNNotiferPersonAction:MemberDetail Edit ssn="+data.getSsn());

                //reset the VerifyPersonForm page parms, as Member paths use the MemberVerifyForm instead. . . 
                verifyPersonForm.setPage(0);
                verifyPersonForm.setTotal(0);
                verifyPersonForm.setSortBy("personNm");
                
                request.setAttribute("verifyPersonForm", verifyPersonForm);  
            } else if (session.getAttribute("back").equals("AssociateAdd")) {
                //get ssn for org associate view of dup ssn 
                OrganizationAssociateDetailForm oadForm = (OrganizationAssociateDetailForm)session.getAttribute("organizationAssociateDetailForm");
                data.setSsn(oadForm.getPersonData().getSsn());
                request.setAttribute("verifyPersonForm", verifyPersonForm);
            }            
        }
        int count = s_maintainPersons.getDuplicateSSN(data, result);

        //put the search result in the form
        if (result!=null) verifyPersonForm.setPersonResult(result);
        verifyPersonForm.setResults(result);
        verifyPersonForm.setTotal(count);

        // debug code
        if (result==null)
            log.debug("ViewDuplicateSSNNotifierPerson: Form is null");
        else {
            log.debug("ViewDuplicateSSNNotifierPerson returned "+result.size() + " items:");
        }

        return mapping.findForward("ViewDuplicates");
    }
/*
        // Set Return button action
        String back = request.getParameter("back");
        if (back.equals("PersonDetail")) { //keep this check here, don't want to open up a hole into the forward mechanism
            PersonDetailForm personDetailForm = new PersonDetailForm();
            request.setAttribute("personDetailForm", personDetailForm);
//            personDetailForm.setReturnAction("/viewPersonDetail.action?personPk=" + personPk);
        }
        else if (back.equals("MemberDetail"))  //keep this check here, don't want to open up a hole into the forward mechanism
//            phoneNumberForm.setReturnAction("/viewMemberDetail.action?personPk=" + personPk);
        request.setAttribute("back", back);

        // needed for jsp
        return mapping.findForward("ViewDetail");
    }
*/    
}
