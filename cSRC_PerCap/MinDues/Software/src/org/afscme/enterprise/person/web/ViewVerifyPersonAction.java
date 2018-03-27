package org.afscme.enterprise.person.web;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.person.PersonCriteria;
import org.apache.struts.action.ActionErrors;

/**
 * View the verify person, based on criteria in the verifyPersonForm.
 *
 * @struts:action   path="/viewVerifyPerson"
 *                  name="verifyPersonForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward name="VerifyForm" path="/Membership/VerifyPerson.jsp"
 * @struts:action-forward name="VerifyResults" path="/Membership/PersonVerifyResults.jsp"
 * @struts:action-forward name="AddNewPerson" path="/addPersonDetail.action"
 * @struts:action-forward name="AddNewAssociate" path="/editOrganizationAssociateDetail.action?newPerson=true"
 */
public class ViewVerifyPersonAction extends AFSCMEAction {

    /** Creates a new instance of ViewVerifyPersonAction */
    public ViewVerifyPersonAction() {
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
        log.debug("ViewVerifyPersonAction: back="+request.getParameter("back"));

        VerifyPersonForm verifyPersonForm = (VerifyPersonForm)form;
        request.setAttribute("verifyPersonForm", verifyPersonForm);

        //if the request contained the 'new' parameter, don't perform the search, just show the search form.
        if (request.getParameter("new") != null) {
            verifyPersonForm.newSearch();
            return mapping.findForward("VerifyForm");
        }

        //validate manually 
        ActionErrors errors = verifyPersonForm.validate(mapping, request);
        if (errors != null && !errors.isEmpty()) {
            saveErrors(request, errors);

            //return to the Verify page if validation errors 
            return mapping.findForward("VerifyForm");
        }

        //get the search criteria from the form
        PersonCriteria data = verifyPersonForm.getPersonCriteriaData();
        List result = new LinkedList();
        
        request.setAttribute("firstNm", verifyPersonForm.getFirstNm());
        request.setAttribute("lastNm", verifyPersonForm.getLastNm());
        request.setAttribute("suffixNm", verifyPersonForm.getSuffixNm());
        request.setAttribute("ssn", verifyPersonForm.getSsn());
        log.debug("ViewVerifyPersonAction: ssn="+verifyPersonForm.getSsn());
        //if no duplicate matches were found, go straight to the Add New
        if (!s_maintainPersons.isExistingPerson(data)) {
            if (request.getParameter("back").equals("AssociateAdd")) 
                //go to Add Org Associate page
                return mapping.findForward("AddNewAssociate");
            else
                //go to the Add page  
                return mapping.findForward("AddNewPerson");
        }

        int count = s_maintainPersons.getDuplicatePersons(data, result);

        //put the search result in the form
        if (result!=null) verifyPersonForm.setPersonResult(result);
        verifyPersonForm.setResults(result);
        verifyPersonForm.setTotal(count);
        verifyPersonForm.setBack(request.getParameter("back"));
        

        // debug code
        if (result==null) 
            log.debug("verifyPersonForm: Form is null");
        else {
            log.debug("verifyPersonForm returned "+result.size() + " items:");
        }

//        request.setAttribute("back", verifyPersonForm.getBack());
//        request.setAttribute("verifyPersonForm", verifyPersonForm);
        return mapping.findForward("VerifyResults");
    }

}
