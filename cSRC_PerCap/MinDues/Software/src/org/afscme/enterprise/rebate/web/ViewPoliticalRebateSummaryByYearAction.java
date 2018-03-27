package org.afscme.enterprise.rebate.web;

import java.util.List;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/viewPoliticalRebateSummaryByYear"
 *                  name="politicalRebateSummaryByYearForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/Membership/PoliticalRebateSummaryByYear.jsp"
 */
public class ViewPoliticalRebateSummaryByYearAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateSummaryByYearForm viewForm = (PoliticalRebateSummaryByYearForm)form;
        if (viewForm == null) {
            viewForm = new PoliticalRebateSummaryByYearForm();
        }        
        
        // Get/Set the path flow of how the user gets to this point
        String origin = (String)request.getParameter("origin");
        if (origin == null) {
            origin = getCurrentFlow(request);
        } else {
            setCurrentFlow(request, origin);
        }
        
        // Get the person primary key from session
        Integer pk = getCurrentPersonPk(request);
        viewForm.setOriginateFrom(origin);
        
        // Get the list of political rebates of the all the years in which this individual has applied for
        viewForm.setPrbSummaryByYear(s_maintainPoliticalRebate.getPersonPRBSummaryByYear(pk));
        request.setAttribute("politicalRebateSummaryByYearForm", viewForm);
        return mapping.findForward("View");
    }
}
