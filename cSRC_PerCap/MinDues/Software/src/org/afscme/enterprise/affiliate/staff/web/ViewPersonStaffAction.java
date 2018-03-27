
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
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.organization.LocationData;

/**
 * @struts:action   path="/viewPersonStaff"
 *                  name="personStaffForm"
 *
 * @struts:action-forward   name="View"  path="/Membership/PersonStaffList.jsp"
 */
public class ViewPersonStaffAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer personPk = getCurrentPersonPk(request);
        PersonStaffForm psForm = (PersonStaffForm)form;
        SortData sortData = psForm.getSortData();
        
        psForm.setResults(s_maintainAffiliateStaff.getPersonStaff(personPk, sortData));
        
        return mapping.findForward("View");
    }
}
