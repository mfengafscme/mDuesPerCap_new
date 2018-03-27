
package org.afscme.enterprise.affiliate.staff.web;

import java.util.Collection;
import java.util.Iterator;
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
import org.afscme.enterprise.affiliate.staff.StaffCriteria;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.afscme.enterprise.affiliate.staff.StaffResult;
import org.afscme.enterprise.users.web.SelectUserAffiliatesSearchForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.PersonResult;
import org.afscme.enterprise.util.DelimitedStringBuffer;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/saveAffiliateStaff"
 *					input="/Membership/AffiliateStaffDetailEdit.jsp"
 *					name="staffForm"
 *                  scope="session"
 *					validate="true"
 *
 * @struts:action-forward   name="ViewStaff"  path="/viewAffiliateStaff.action" redirect="true"
 * @struts:action-forward   name="ViewLocationSelection"  path="/viewLocationSelection.action?back=StaffDetail"
 * @struts:action-forward   name="ViewAffiliate"  path="/viewStaffMaintainence.action" redirect="true"
 * @struts:action-forward   name="ViewDups"  path="/Membership/ViewDuplicateStaffSSN.jsp"
 *
 */
public class SaveAffiliateStaffAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        HttpSession session = request.getSession();
        
        Integer personPk = getCurrentPersonPk(request);
        Integer affPk = getCurrentAffiliatePk(request);
		StaffForm staffForm = (StaffForm)form;
        
        
        if (isCancelled(request)) {
            request.getSession().removeAttribute("staffForm");
            if(staffForm.isUpdate())
                return mapping.findForward("ViewStaff");
            else
        		return mapping.findForward("ViewAffiliate");
       
        }
        
        StaffData staffData = staffForm.getStaffData();
        String comment = staffForm.getComment();
        boolean needLocation = false;
        
        if (staffForm.isUpdate()) {

            //updating affiliate staff detail and person detail
            PersonData personData = staffForm.getPersonData();
            personData.setPersonPk(personPk);
            s_maintainAffiliateStaff.updateAffiliateStaff(affPk, personData, staffData, comment, usd.getPersonPk());

            //check for a name change.
            //If so, then add to weekly card run and reset name display on header/footer tags.
            String currentName = getCurrentPersonName(session);
            String newName = s_maintainUsers.getPersonName(personPk);
            if (!TextUtil.equals(currentName, newName)) {

                // add to weekly card run so member gets new card for changed name
                s_maintainMembers.addToWeeklyCardRun(personPk);
                // Reset person name in session so headers/footers display correctly
                setCurrentPersonName(request, newName);
            }
            
        }  else {

            //add

            if (staffForm.isNewPerson()) {
            
                //add new person
                
                NewPerson newPerson = staffForm.getNewPerson();
                String ssn = staffForm.getNewPerson().getSsn();
                if (!staffForm.isIgnoreSsnDup() && !TextUtil.isEmpty(ssn)) {
                    
                    //check for duplicate ssn
                    StaffCriteria staffCriteria = new StaffCriteria();
                    staffCriteria.getSortData().setDirection(staffForm.getOrder());
                    staffCriteria.getSortData().setSortField(StaffResult.sortStringToCode(staffForm.getSortBy()));
                    staffCriteria.getSortData().setPageSize(1000);
                    staffCriteria.setSsn(ssn);
                    List dups = new LinkedList();
                    if (s_maintainAffiliateStaff.getExistingStaff(staffCriteria, dups) > 0) {
                        request.setAttribute("dups", dups);
                        request.setAttribute("ssn", ssn);
                        return mapping.findForward("ViewDups");
                    }
                }
                personPk = s_maintainAffiliateStaff.addAffiliateStaff(affPk, newPerson, staffData, comment, usd.getPersonPk());
                setCurrentPerson(request, personPk);

            } else {

                //adding affiliate staff to exsting person
                s_maintainAffiliateStaff.addAffiliateStaff(affPk, personPk, staffData, comment, usd.getPersonPk());


            }
        }
        
        request.getSession().removeAttribute("staffForm");

        //
        // As per Defect 138: 
        // We have to check if the staff was not assigned a location, even though 
        // the affiliate does have locations.  In this case, go directly to the 
        // select locations screen, instead of the staff detail screen.
        //
        Integer locationPk = s_maintainOrgLocations.getOrgPrimaryLocationPK(affPk);
        Collection allLocations = s_maintainOrgLocations.getOrgLocations(affPk);
        if (locationPk == null && allLocations.size() != 0)
            return mapping.findForward("ViewLocationSelection");
        else
    		return mapping.findForward("ViewStaff");
	}
}
