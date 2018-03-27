package org.afscme.enterprise.rebate.web;

import java.util.Calendar;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/editPoliticalRebateRequest"
 *		    name="politicalRebateRequestForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/PoliticalRebateRequestEdit.jsp"
 * @struts:action-forward   name="Save"  path="/savePoliticalRebateRequest.action"
 */
public class EditPoliticalRebateRequestAction extends DuesPaidInfoAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateRequestForm editForm = (PoliticalRebateRequestForm)form;
        
        // Get the affPk returned from Affiliate Finder Duplicate Result Page
        String affPk = (String)request.getParameter("affPk");
        if (affPk != null) {
            setDuesPaidToAffIDs(editForm, new Integer(affPk));
            return mapping.findForward("Save");            
        }
                           
        // Get the current person Primary Key from request
        editForm.setPk(getCurrentPersonPk(request));

        // Political Rebate - Request Edit
        String back = (String)request.getParameter("back");
        if (back != null) {
            editForm.setBack(back);
        }
        if (editForm.getBack() == null) {
            editForm.setEdit(true);        
        } 
        
        // Political Rebate - Request Add
        // If call from Political Rebate Summary By Year, the Rebate Year will be editable 
        // and will be set previous calendar year
        else if (editForm.getBack().equalsIgnoreCase("SummaryByYear")) {
            Calendar calendar = Calendar.getInstance();            
            editForm.setPrbYear(new Integer(calendar.get(calendar.YEAR)-1).toString());
            editForm.setPrbYearEditable(true);
        }   
        
        // Set default values for adding the PRB request
        if (!editForm.isEdit()) {
            editForm.setKeyedDate(DateUtil.getSimpleDateString(DateUtil.getCurrentDateAsTimestamp()));
            editForm.setDuration_1(new Integer(56012));
        }
        editForm.setSave(true);
        return mapping.findForward("Edit");
    }
}
