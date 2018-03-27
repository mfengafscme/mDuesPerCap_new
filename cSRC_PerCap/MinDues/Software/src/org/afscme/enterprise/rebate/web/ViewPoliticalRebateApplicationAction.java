package org.afscme.enterprise.rebate.web;

import java.util.List;
import java.util.Iterator;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PRBAffiliateData;
import org.afscme.enterprise.person.PRBApplicationData;
import org.afscme.enterprise.rebate.PRBConstants;

/**
 * @struts:action   path="/viewPoliticalRebateApplication"
 *                  name="politicalRebateApplicationForm"
 *                  validate="false"
 *                  scope="session"
 *                  input="/Membership/PoliticalRebateApplication.jsp"
 *
 * @struts:action-forward   name="View"  path="/Membership/PoliticalRebateApplication.jsp"
 * @struts:action-forward   name="Edit"  path="/editPoliticalRebateApplication.action"
 */
public class ViewPoliticalRebateApplicationAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateApplicationForm viewForm = (PoliticalRebateApplicationForm)form;
        
        // Get the current person primary key from request
        viewForm.setPk(getCurrentPersonPk(request));
        
        // Check for the existence of PRB Application primary key
        if (viewForm.getAppPk() == null || viewForm.getAppPk().intValue() <= 0) {
            // Coming from another jsp
            String appPk = (String)request.getParameter("pk");
            if (appPk == null) {
                // Coming from another action
                appPk = (String)request.getAttribute("pk");                
                if (appPk == null) {
                    // Still cannot figure out appPk from anywhere so throw the exception
                    throw new JspException("The Political Rebate Application Primary Key is not specified in the request.");
                }
            }                        
            viewForm.setAppPk(new Integer(appPk));
        }

        // Get Political Rebate Application and set it to this form
        PRBApplicationData appData = s_maintainPoliticalRebate.getPRBApplication(viewForm.getPk(), viewForm.getAppPk());
        viewForm.setPRBApplicationData(appData);
          
        // Get Political Rebate Application Affiliates and set them to this form
        getPRBApplicationAffiliates(viewForm);

        // Remove the Affiliate from the Political Rebate Application
        String affPk = (String)request.getParameter("affPk");
        if (affPk != null && request.getParameter("clear") != null) { 
            // Atleast one full Affiliate Identifier is required
            if (s_maintainPoliticalRebate.getPRBAffiliatesCount(viewForm.getPk(), viewForm.getAppPk().intValue(), PRBConstants.PRB_APPLICATION) == 1) { 
                return makeErrorForward(request, mapping, "affPk_1", "error.rebate.affiliate.clear");
            } else {
                // Remove the Affiliate from the Political Rebate Request
                s_maintainPoliticalRebate.removePRBAffiliate(viewForm.getAppPk(), new Integer(affPk), PRBConstants.PRB_APPLICATION);                
                // Get the new list of Political Rebate Request Affiliates and set them to this form
                getPRBApplicationAffiliates(viewForm);
            }
        }         
        
        // Forward to Political Rebate - Application Edit page
        if (request.getParameter("edit") != null) { 
            viewForm.setEdit(true);
            return mapping.findForward("Edit");
        }
            
        // Forward to Political Rebate - Application page
        return mapping.findForward("View");
    }
    
    
    public void getPRBApplicationAffiliates(ActionForm form) {
        PoliticalRebateApplicationForm viewForm = (PoliticalRebateApplicationForm)form;
        List list = s_maintainPoliticalRebate.getPRBAffiliates(viewForm.getPk(), viewForm.getAppPk().intValue(), PRBConstants.PRB_APPLICATION);
        if (list!=null && list.size()>0) {
            PRBAffiliateData data = null;
            Iterator itr = list.iterator();
            int i=1;
            viewForm.initDuesPaidInfo();
            while (itr.hasNext()) {
                data = (PRBAffiliateData) itr.next();                
                viewForm.setPRBDuesPaid(data, i);
                ++i;
            }
        }
    }
}
