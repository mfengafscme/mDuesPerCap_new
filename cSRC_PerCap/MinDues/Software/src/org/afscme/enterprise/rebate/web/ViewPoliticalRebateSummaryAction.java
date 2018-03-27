package org.afscme.enterprise.rebate.web;

import java.util.List;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.rebate.PRBConstants;
import org.afscme.enterprise.person.PRBCheckInfo;

/**
 * @struts:action   path="/viewPoliticalRebateSummary"
 *                  name="politicalRebateSummaryForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/Membership/PoliticalRebateSummary.jsp"
 */
public class ViewPoliticalRebateSummaryAction extends AFSCMEAction {
        
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateSummaryForm viewForm = (PoliticalRebateSummaryForm)form;

        // Remove PRB session attributes
        cleanupSession(request);
        
        // Get current person primary key from session
        Integer pk = getCurrentPersonPk(request);
        
        // If prbYear is not a parameter in the request, get it from the request attribute                    
        if (viewForm.getPrbYear() == null) {
            String prbYear = (String)request.getAttribute("prbYear");
            if (prbYear == null) {
                // Still cannot figure out prbYear from anywhere so throw the exception
                throw new JspException("No Political Rebate Year was specified in the request.");
            }
            viewForm.setPrbYear(new Integer(prbYear));
        }
        
        // Get the list of political rebate request
        viewForm.setPrbRequestList(s_maintainPoliticalRebate.getPRBRequests(pk, viewForm.getPrbYear().toString()));
        
        // Get the list of political rebate application
        viewForm.setPrbApplicationList(s_maintainPoliticalRebate.getPRBApplications(pk, viewForm.getPrbYear().toString()));
        
        // Check if Annual Rebate Information or Check Info exists
        List annualRebateList = s_maintainPoliticalRebate.getPRBAnnualRebateAffiliates(pk, viewForm.getPrbYear());
        PRBCheckInfo checkInfo = s_maintainPoliticalRebate.getPRBCheckInfo(pk, viewForm.getPrbYear());
        
        // Get the political rebate annual rebate status
        if (annualRebateList.size() > 0 || checkInfo != null) {
            viewForm.setPrbAnnualInfoStatus(s_maintainPoliticalRebate.getAnnualRebateStatus(pk, viewForm.getPrbYear()));
        }
        
        return mapping.findForward("View");
    }
    
    public void cleanupSession(HttpServletRequest request) {
        request.getSession().removeAttribute(PRBConstants.PRB_REQUEST_FORM);
        request.getSession().removeAttribute(PRBConstants.PRB_APPLICATION_FORM);
    }
}
