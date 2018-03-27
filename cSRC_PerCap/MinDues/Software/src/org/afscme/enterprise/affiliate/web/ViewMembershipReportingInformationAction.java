
package org.afscme.enterprise.affiliate.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.affiliate.MRData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



/**
 * @struts:action   path="/viewMembershipReportingInformation"
 *
 * @struts:action-forward   name="View"  path="/Membership/MembershipReportingInformation.jsp"
 *
 */
public class ViewMembershipReportingInformationAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer affPk = getCurrentAffiliatePk(request);
        
        MRData mrData = s_maintainAffiliates.getMembershipReportingData(affPk);
        if (mrData == null) {
            throw new JspException("No Membership Reporting info found with pk = " + affPk);
        }
        
        request.setAttribute("mrData", mrData);
        
        return mapping.findForward("View");
	}
}
