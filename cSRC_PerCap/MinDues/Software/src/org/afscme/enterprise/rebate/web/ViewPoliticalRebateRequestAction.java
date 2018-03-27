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
import org.afscme.enterprise.rebate.PRBConstants;
import org.afscme.enterprise.person.PRBRequestData;
import org.afscme.enterprise.person.PRBAffiliateData;

/**
 * @struts:action   path="/viewPoliticalRebateRequest"
 *                  name="politicalRebateRequestForm"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Membership/PoliticalRebateRequest.jsp"
 *
 * @struts:action-forward   name="View"  path="/Membership/PoliticalRebateRequest.jsp"
 * @struts:action-forward   name="Edit"  path="/editPoliticalRebateRequest.action"
 */
public class ViewPoliticalRebateRequestAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateRequestForm viewForm = (PoliticalRebateRequestForm)form;
        
        // Get the current person primary key from session
        viewForm.setPk(getCurrentPersonPk(request));

        // Check for the existence of PRB Request primary key
        if (viewForm.getRqstPk() == null || viewForm.getRqstPk().intValue() <= 0) {
            // Coming from another jsp
            String rqstPk = (String)request.getParameter("pk");
            if (rqstPk == null) {
                // Coming from another action
                rqstPk = (String)request.getAttribute("pk");                
                if (rqstPk == null) {
                    // Still cannot figure out rqstPk from anywhere so throw the exception
                    throw new JspException("The Political Rebate Request Primary Key is not specified in the request.");
                }
            }                        
            viewForm.setRqstPk(new Integer(rqstPk));
        }

        // Get Political Rebate Request and set it to this form
        PRBRequestData rqstData = s_maintainPoliticalRebate.getPRBRequest(viewForm.getPk(), viewForm.getRqstPk().intValue());
        viewForm.setPRBRequestData(rqstData);
        
        // Get Political Rebate Request Affiliates and set them to this form
        getPRBRequestAffiliates(viewForm);

        // Remove the Affiliate from the Political Rebate Request
        String affPk = (String)request.getParameter("affPk");
        if (affPk != null && request.getParameter("clear") != null) { 
            // Atleast one full Affiliate Identifier is required
            if (s_maintainPoliticalRebate.getPRBAffiliatesCount(viewForm.getPk(), viewForm.getRqstPk().intValue(), PRBConstants.PRB_REQUEST) == 1) { 
                return makeErrorForward(request, mapping, "affPk_1", "error.rebate.affiliate.clear");
            } else {
                // Remove the Affiliate from the Political Rebate Request
                s_maintainPoliticalRebate.removePRBAffiliate(viewForm.getRqstPk(), new Integer(affPk), PRBConstants.PRB_REQUEST);                
                // Get the new list of Political Rebate Request Affiliates and set them to this form
                getPRBRequestAffiliates(viewForm);
            }
        } 
        
        
        // Get the application mailed date.  If Application Mailed Date has been given a value,
        // then the PRB request is no longer be editable.
        if (viewForm.getAppPk() != null) {
            if (s_maintainPoliticalRebate.getPRBAppMailedDate(viewForm.getAppPk().intValue()) != null)
                viewForm.setPrbRequestEditable(false);                
        }
        
        // Forward to Political Rebate - Request Edit page
        if (request.getParameter("edit") != null) { 
            viewForm.setEdit(true);
            return mapping.findForward("Edit");
        }
            
        // Forward to Political Rebate - Request page
        return mapping.findForward("View");
    }
    
    
    public void getPRBRequestAffiliates(ActionForm form) {
        PoliticalRebateRequestForm viewForm = (PoliticalRebateRequestForm)form;
        List list = s_maintainPoliticalRebate.getPRBAffiliates(viewForm.getPk(), viewForm.getRqstPk().intValue(), PRBConstants.PRB_REQUEST);
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
