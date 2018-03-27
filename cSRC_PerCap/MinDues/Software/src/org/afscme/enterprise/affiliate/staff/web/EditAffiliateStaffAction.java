
package org.afscme.enterprise.affiliate.staff.web;

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
import org.afscme.enterprise.affiliate.staff.web.VerifyStaffForm;
import org.afscme.enterprise.users.web.SelectUserAffiliatesSearchForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PersonData;
import org.apache.log4j.Logger;

/**
 * @struts:action   path="/editAffiliateStaff"
 *					input="/Membership/AffiliateStaffDetailEdit.jsp"
 *					name="staffForm"
 *                  scope="session"
 *					validate="true"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/AffiliateStaffDetailEdit.jsp"
 * @struts:action-forward   name="SearchResults"  path="/Membership/AffiliateStaffMaintainence.jsp"
 *
 */
public class EditAffiliateStaffAction extends AFSCMEAction {
        static Logger logger = Logger.getLogger(EditAffiliateStaffAction.class);	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        StaffForm staffForm = (StaffForm)form;
        Integer personPk = null;
        
        Integer affPk = getCurrentAffiliatePk(request);
        
        if (staffForm.isNewPerson())
            setCurrentPerson(request, null);
        else
            personPk = getCurrentPersonPk(request, "personPk");
        
        if (staffForm.isUpdate()) {
            StaffData staffData = s_maintainAffiliateStaff.getAffiliateStaff(affPk, personPk);
            staffForm.setStaffData(staffData);
            request.setAttribute("emails", s_maintainPersons.getPersonEmails(personPk));
            request.setAttribute("locals", s_maintainAffiliateStaff.getLocalsServiced(affPk, personPk));
            request.setAttribute("location",  s_maintainOrgLocations.getOrgLocation(staffData.getLocationPk()));
        }
        
        if (personPk != null)
            staffForm.setPersonData(s_maintainPersons.getPersonDetail(personPk, usd.getDepartment())); // need to fix this, user could be affiliate
        else {
            VerifyStaffForm verifyStaffForm = (VerifyStaffForm)request.getSession().getAttribute("verifyStaffForm");
            PersonData personData = staffForm.getPersonData();
            personData.setFirstNm(verifyStaffForm.getFirstNm());
            personData.setLastNm(verifyStaffForm.getLastNm());
            personData.setSsn(verifyStaffForm.getSsn1()+verifyStaffForm.getSsn2()+verifyStaffForm.getSsn3());
            personData.setSuffixNm(verifyStaffForm.getSuffixNm());
        }
        
		return mapping.findForward("Edit");
	}
}
