
package org.afscme.enterprise.affiliate.staff.web;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.afscme.enterprise.affiliate.staff.StaffResult;
import org.afscme.enterprise.users.web.SelectUserAffiliatesSearchForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.organization.LocationData;

/**
 * @struts:action   path="/viewAffiliateStaff"
 *
 * @struts:action-forward   name="View"  path="/Membership/AffiliateStaffDetail.jsp"
 *
 */
public class ViewAffiliateStaffAction extends AFSCMEAction {
	
    public static final String SESSION_STAFF_DETAIL_RETURN = "SESSION_STAFF_DETAIL_RETURN";
    
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer personPk = getCurrentPersonPk(request, "personPk");
        Integer affPk = getCurrentAffiliatePk(request, "affPk");

        //set return action
        String back = request.getParameter("back");
        if (back == null)
            back = (String)request.getSession().getAttribute(SESSION_STAFF_DETAIL_RETURN);
        if (back == null)
            back = "/viewStaffMaintainence.action";
        request.getSession().setAttribute(SESSION_STAFF_DETAIL_RETURN, back);
        request.setAttribute("back", back);
        
        PersonData personData = s_maintainPersons.getPersonDetail(personPk, usd.getDepartment()); // need to fix this, user could be affiliate
        StaffData staffData = s_maintainAffiliateStaff.getAffiliateStaff(affPk, personPk);
        CommentData commentData = s_maintainAffiliateStaff.getComment(affPk, personPk);
        Collection emails = s_maintainPersons.getPersonEmails(personPk);
        List locals = s_maintainAffiliateStaff.getLocalsServiced(affPk, personPk);
        LocationData locationData = s_maintainOrgLocations.getOrgLocation(staffData.getLocationPk());

        request.setAttribute("location", locationData);
        request.setAttribute("personData", personData);
        request.setAttribute("staffData", staffData);
        request.setAttribute("emails", emails);
        request.setAttribute("locals", locals);
        request.setAttribute("comment", (commentData == null) ? null : commentData.getComment());
        request.setAttribute("affiliateId",getCurrentAffiliateId(request));

        return mapping.findForward("View");
	}
}
