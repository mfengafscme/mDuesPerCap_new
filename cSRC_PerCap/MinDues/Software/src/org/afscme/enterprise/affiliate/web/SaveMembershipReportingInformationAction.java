
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
import org.afscme.enterprise.codes.Codes.AffiliateStatus;


/**
 * @struts:action   path="/saveMembershipReportingInformation"
 *                  name="membershipReportingInformationForm"
 *                  scope="request"
 *                  validate="false"
 *                  input="/Membership/MembershipReportingInformationEdit.jsp"
 *
 * @struts:action-forward   name="Done"  path="viewMembershipReportingInformation.action" redirect="true"
 *
 */
public class SaveMembershipReportingInformationAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        if (!isCancelled(request)) {
            Integer affPk = getCurrentAffiliatePk(request);
            MembershipReportingInformationForm mrForm = (MembershipReportingInformationForm)form;
            // validate the affiliate 
            ActionErrors errors = mrForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                MRData mrData = s_maintainAffiliates.getMembershipReportingData(affPk);
                mrForm.setMrData(mrData);
                request.setAttribute("membershipReportingInformationForm", mrForm);
                saveErrors(request, errors);
                return mapping.getInputForward();
            }            
            MRData mrData = mrForm.getMrData();            
            if (mrForm.getNewStatus() != null)
                mrData.setAffStatus(mrForm.getNewStatus());
            mrData.setAffPk(affPk);
            
            if (mrForm.getNewStatus().equals(AffiliateStatus.DU)) {
                // inactivate members
                s_maintainAffiliates.inactivateAffiliatesMembers(affPk, usd.getPersonPk());
                // inactivate officers
                s_maintainAffiliates.inactivateAffiliatesOfficers(affPk, usd.getPersonPk());                
            }
            
            if (mrForm.getNewStatus().equals(AffiliateStatus.RA)) {
                // suspend officers
                s_maintainAffiliates.suspendAffiliatesOfficers(affPk, usd.getPersonPk());               
            }            
            
            s_maintainAffiliates.updateMembershipReportingData(affPk, mrData, usd.getPersonPk());
        }

        return mapping.findForward("Done");
	}
}
