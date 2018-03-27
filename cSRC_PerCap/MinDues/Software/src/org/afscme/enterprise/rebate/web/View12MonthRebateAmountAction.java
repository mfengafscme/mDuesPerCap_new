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
 * @struts:action   path="/view12MonthRebateAmount"
 *                  name="twelveMonthRebateAmountForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/Membership/TwelveMonthRebateAmount.jsp"
 */
public class View12MonthRebateAmountAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        TwelveMonthRebateAmountForm viewForm = (TwelveMonthRebateAmountForm)form;
        if (viewForm == null) {
            viewForm = new TwelveMonthRebateAmountForm();
        }        
        List list = s_maintainPoliticalRebate.getPRB12MonthAmount();        
        viewForm.setPrb12MonthRebateAmount(list);      
        request.setAttribute("twelveMonthRebateAmountForm", viewForm);
        return mapping.findForward("View");
    }
}
