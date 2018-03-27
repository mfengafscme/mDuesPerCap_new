
package org.afscme.enterprise.affiliate.staff.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;


/**
 * @struts:action   path="/viewVerifyStaff"
 *		    name="verifyStaffForm"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="View"  path="/Membership/VerifyStaff.jsp"
 *
 */
public class ViewVerifyStaff extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        ((VerifyStaffForm)form).clear();
        return mapping.findForward("View");
    }
}
