
package org.afscme.enterprise.affiliate.staff.web;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.afscme.enterprise.users.web.SelectUserAffiliatesSearchForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PersonResult;


/**
 * @struts:action   path="/verifyStaff"
 *                  input="/Membership/VerifyStaff.jsp"
 *  		    name="verifyStaffForm"
 *                  scope="session"
 *		    validate="false"
 *
 * @struts:action-forward   name="Add"  path="/editAffiliateStaff.action"
 * @struts:action-forward   name="View"  path="/viewAffiliateStaff.action"
 * @struts:action-forward   name="AddNew"  path="/editAffiliateStaff.action?newPerson=true"
 * @struts:action-forward   name="ViewMatches"  path="/Membership/StaffVerifyResults.jsp"
 */
public class VerifyStaffAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        VerifyStaffForm verifyStaffForm = (VerifyStaffForm)form;
        Integer affPk = getCurrentAffiliatePk(request);
        
        
        ActionErrors errors = verifyStaffForm.validate(mapping, request);                        
        // Present errors if any exists
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
        org.afscme.enterprise.affiliate.staff.StaffCriteria criteria = verifyStaffForm.getStaffCriteria();
        
        List results = new LinkedList();
        int total = s_maintainAffiliateStaff.getExistingStaff(criteria, results);
        if (results.size() == 0) { 
            return mapping.findForward("AddNew");
        } else if (results.size() == 1) {
            PersonResult pr = (PersonResult)results.iterator().next();
            Integer personsAffil = pr.getAffPk();            
            Integer personPk = pr.getPersonPk();
            setCurrentPerson(request, personPk);
            if (affPk.equals(personsAffil))
                return mapping.findForward("View");
            return mapping.findForward("Add");
        } else {
            verifyStaffForm.setTotal(total);
            verifyStaffForm.setResults(results);
            return mapping.findForward("ViewMatches");
        }
	}
}
