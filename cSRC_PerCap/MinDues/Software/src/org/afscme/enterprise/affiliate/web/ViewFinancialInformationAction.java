package org.afscme.enterprise.affiliate.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.FinancialData;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.web.FinancialForm;

/** 
 * @struts:action   path="/viewFinancialInformation"
 *
 * @struts:action-forward   name="viewFinancial"  path="/Membership/FinancialInformation.jsp"
 */
public class ViewFinancialInformationAction extends AFSCMEAction {
    
    /** Creates a new instance of ViewFinancialInformationAction */
    public ViewFinancialInformationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        Integer affPk = getCurrentAffiliatePk(request);
        if (affPk == null) {
            throw new JspException("No current Affiliate is defined for which to retrieve Financial Info.");
        }
        FinancialData data = s_maintainAffiliates.getFinancialData(affPk);
        if (data == null) {
            throw new JspException("No Financial Info found with pk = " + affPk);
        }
        
        FinancialForm ff = new FinancialForm();
        ff.setFinancialData(data);
        
        request.setAttribute("financialForm", ff);
        return mapping.findForward("viewFinancial");
    }
    
}
