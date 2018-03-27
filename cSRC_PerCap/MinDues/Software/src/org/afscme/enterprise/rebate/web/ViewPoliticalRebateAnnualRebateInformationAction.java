package org.afscme.enterprise.rebate.web;

import java.util.List;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/viewPoliticalRebateAnnualRebateInformation"
 *                  name="politicalRebateAnnualRebateInformationForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/Membership/PoliticalRebateAnnualRebateInformation.jsp"
 */
public class ViewPoliticalRebateAnnualRebateInformationAction extends AFSCMEAction {
        
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateAnnualRebateInformationForm viewForm = (PoliticalRebateAnnualRebateInformationForm)form;

        // Get the current person primary key from request
        Integer pk = getCurrentPersonPk(request);

        // If prbYear is not a parameter in the request, get it from the request attribute                    
        if (viewForm.getPrbYear() == null) {
            Integer prbYear = (Integer) request.getAttribute("prbYear");
            if (prbYear == null) {
                // Still cannot figure out prbYear from anywhere so throw the exception
                throw new JspException("No prbYear was specified in the request.");
            }
            viewForm.setPrbYear(prbYear);
        }
          
        // Remove the Affiliate from the Annual Rebate
        if (request.getParameter("clear") != null && viewForm.getAffPk() != null) {
            s_maintainPoliticalRebate.removeAffiliateFromAnnualRebate(pk, viewForm.getPrbYear(), viewForm.getAffPk());
        } 
        
        // Get the Annual Rebate Information
        viewForm.setPrbRosterStatus(s_maintainPoliticalRebate.getRosterStatus(pk, viewForm.getPrbYear()));
        viewForm.setPrbDuesPaidList(s_maintainPoliticalRebate.getPRBAnnualRebateAffiliates(pk, viewForm.getPrbYear()));
        viewForm.setPrbCheckInfo(s_maintainPoliticalRebate.getPRBCheckInfo(pk, viewForm.getPrbYear()));               
        return mapping.findForward("View");
    }
}
