package org.afscme.enterprise.rebate.web;

import java.util.List;
import java.util.Calendar;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.util.TextUtil;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;

/** Holds the data on the Twelve Month Rebate Amount screen
 * @struts:form name="twelveMonthRebateAmountForm"
 */
public class TwelveMonthRebateAmountForm extends ActionForm {
    
    private List prb12MonthRebateAmount;
    
    public String toString() {
        return "12 Month Rebate Amount List" + prb12MonthRebateAmount;
    }
    
    /** Getter for property prb12MonthRebateAmount.
     * @return Value of property prb12MonthRebateAmount.
     *
     */
    public java.util.List getPrb12MonthRebateAmount() {
        return prb12MonthRebateAmount;
    }
    
    /** Setter for property prb12MonthRebateAmount.
     * @param prb12MonthRebateAmount New value of property prb12MonthRebateAmount.
     *
     */
    public void setPrb12MonthRebateAmount(java.util.List prb12MonthRebateAmount) {
        this.prb12MonthRebateAmount = prb12MonthRebateAmount;
    }
    
    public boolean isPreviousRebateYear(Integer prbYear) {
        Calendar calendar = Calendar.getInstance();
        return (prbYear.intValue() == calendar.get(calendar.YEAR)-1) ? true : false;
    }
}


