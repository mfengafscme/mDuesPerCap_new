
package org.afscme.enterprise.affiliate.web;

import java.util.List;
import java.util.LinkedList;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.affiliate.MRData;
import org.afscme.enterprise.users.web.SelectUserAffiliatesSearchForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.controller.UserSecurityData;


/**
 * @struts:action   path="/editMembershipReportingInformation"
 *                  name="membershipReportingInformationForm"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Membership/MembershipReportingInformationEdit.jsp"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/MembershipReportingInformationEdit.jsp"
 *
 */
public class EditMembershipReportingInformationAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer affPk = getCurrentAffiliatePk(request);

        MRData mrData = s_maintainAffiliates.getMembershipReportingData(affPk);
        MembershipReportingInformationForm mrForm = (MembershipReportingInformationForm)form;
        mrForm.setMrData(mrData);
        mrForm.setNewStatus(mrData.getAffStatus());

        return mapping.findForward("Edit");
	}
}
