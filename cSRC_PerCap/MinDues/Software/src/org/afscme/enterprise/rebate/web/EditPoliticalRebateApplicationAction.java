package org.afscme.enterprise.rebate.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * @struts:action   path="/editPoliticalRebateApplication"
 *		    name="politicalRebateApplicationForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/PoliticalRebateApplicationEdit.jsp"
 * @struts:action-forward   name="Save"  path="/savePoliticalRebateApplication.action"
 */
public class EditPoliticalRebateApplicationAction extends DuesPaidInfoAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateApplicationForm editForm = (PoliticalRebateApplicationForm)form;
        
        // Get the affPk returned from Affiliate Finder Duplicate Result Page
        String affPk = (String)request.getParameter("affPk");
        if (affPk != null) {
            setDuesPaidToAffIDs(editForm, new Integer(affPk));
            return mapping.findForward("Save");            
        }
                    
        editForm.setPk(getCurrentPersonPk(request));
        editForm.setSave(true);
        return mapping.findForward("Edit");
    }
}
