package org.afscme.enterprise.rebate.web;

import java.util.Map;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * @struts:action   path="/save12MonthRebateAmount"
 *		    name="twelveMonthRebateAmountEditForm"
 *                  validate="true"
 *                  scope="request"
 *                  input="/Membership/TwelveMonthRebateAmountEdit.jsp"
 *
 * @struts:action-forward   name="View"  path="/view12MonthRebateAmount.action" redirect="true"
 */
public class Save12MonthRebateAmountAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        TwelveMonthRebateAmountEditForm editForm = (TwelveMonthRebateAmountEditForm)form;
        
        if (!isCancelled(request)) {
            // Check for errors: Percentage, Full-Time/Part-Time/Lower Part-Time/Retiree Rebate Amount are all required fields
            ActionErrors errors = new ActionErrors();
            if (TextUtil.isEmptyOrSpaces(editForm.getPrbPercentage())) {
                errors.add("prbPercentage", new ActionError("error.field.required.generic", "Rebate Percentage"));
            } else if (!TextUtil.isDouble(editForm.getPrbPercentage())) {
                errors.add("prbPercentage", new ActionError("error.field.mustBeInt.generic", "Rebate Percentage"));
            }
            
            if (TextUtil.isEmptyOrSpaces(editForm.getPrbFullTime())) {
                errors.add("prbFullTime", new ActionError("error.field.required.generic", "Full-Time Rebate Amount"));
            } else if (!TextUtil.isDouble(editForm.getPrbFullTime())) {
                errors.add("prbFullTime", new ActionError("error.field.mustBeInt.generic", "Full-Time Rebate Amount"));
            }
            
            if (TextUtil.isEmptyOrSpaces(editForm.getPrbPartTime())) {
                errors.add("prbPartTime", new ActionError("error.field.required.generic", "Part-Time Rebate Amount"));
            } else if (!TextUtil.isDouble(editForm.getPrbPartTime())) {
                errors.add("prbPartTime", new ActionError("error.field.mustBeInt.generic", "Part-Time Rebate Amount"));
            }
            
            if (TextUtil.isEmptyOrSpaces(editForm.getPrbLowerPartTime())) {
                errors.add("prbLowerPartTime", new ActionError("error.field.required.generic", "Lower Part-Time Rebate Amount"));
            } else if (!TextUtil.isDouble(editForm.getPrbLowerPartTime())) {
                errors.add("prbLowerPartTime", new ActionError("error.field.mustBeInt.generic", "Lower Part-Time Rebate Amount"));
            }
            
            if (TextUtil.isEmptyOrSpaces(editForm.getPrbRetiree())) {
                errors.add("prbRetiree", new ActionError("error.field.required.generic", "Retiree Rebate Amount"));
            } else if (!TextUtil.isDouble(editForm.getPrbRetiree())) {
                errors.add("prbRetiree", new ActionError("error.field.mustBeInt.generic", "Retiree Rebate Amount"));
            }
            
            // Present errors if any exists
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }
                        
            // Save rebate data
            if (editForm.isEdit()) {
                s_maintainPoliticalRebate.updatePRB12MonthAmount(editForm.getPRB12MonthRebateAmount(), usd.getPersonPk());
            } else {
                if (!s_maintainPoliticalRebate.addPRBYear(editForm.getPRB12MonthRebateAmount(), usd.getPersonPk())) {
                    errors.add("prbYear", new ActionError("error.field.rebateYear.duplicate", "prbYear"));
                    saveErrors(request, errors);
                    return new ActionForward(mapping.getInput());
                }
            }
        }
        return mapping.findForward("View");
    }
}
