package org.afscme.enterprise.rebate.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Calendar;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * @struts:action   path="/edit12MonthRebateAmount"
 *		    name="twelveMonthRebateAmountEditForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/TwelveMonthRebateAmountEdit.jsp"
 */
public class Edit12MonthRebateAmountAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        TwelveMonthRebateAmountEditForm editForm = (TwelveMonthRebateAmountEditForm)form;
        
        if (editForm.getPrbYear() != null) {
            editForm.setPRB12MonthRebateAmount(s_maintainPoliticalRebate.getPRB12MonthAmount(new Integer(editForm.getPrbYear())));
            editForm.setEdit(true);
        } else {
            // If Add - set the political rebate year to the current year
            Calendar calendar = Calendar.getInstance();            
            editForm.setPrbYear(new Integer(calendar.get(calendar.YEAR)).toString());
        }
        
        return mapping.findForward("Edit");
    }
}
