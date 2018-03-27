package org.afscme.enterprise.mailinglists.web;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.mailinglists.MailingListData;

/**
 * @struts:action   path="/editMailingListsInformation"
 *                  scope="request"
 *                  validate="false"
 *        	    name="editMailingListsInformationForm"
 *                  input="/Membership/MailingListsInformationEdit.jsp"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/MailingListsInformationEdit.jsp" 
 * @struts:action-forward   name="View"  path="/Membership/MailingListsInformation.jsp"
 */
public class EditMailingListsInformationAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        List mailingList = null;
        
        // Request to cancel - forward to Mailing Lists Information page
        if (isCancelled(request)) {
            return mapping.findForward("View");
        }
        
        MailingListsInformationForm mlInfo = (MailingListsInformationForm) request.getSession().getAttribute("mailingListsInformationForm");
        if (mlInfo == null) {
            throw new RuntimeException("Mailing Lists Information is not in session.");
        }
        
        EditMailingListsInformationForm editForm = (EditMailingListsInformationForm)form;
        if (editForm == null) {
            new EditMailingListsInformationForm();
        }
        
        // Request to submit - update bulk count for bulk list type
        if (request.getParameter("submit") != null) {
            ActionErrors errors = editForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.getInputForward();
            }

            s_maintainOrgMailingLists.updateMailingListBulkCount(editForm.getPk(), editForm.getMailingListPk(), new Integer(editForm.getMailingListBulkCount()).intValue(), usd.getPersonPk());
            mailingList = s_maintainOrgMailingLists.getMailingLists(editForm.getPk());
            
            // Reset Mailing Lists Information
            mlInfo.setMailingLists(mailingList);
            mlInfo.setPrivileges(mailingList);
            return mapping.findForward("View");
        }
        
        // Load Mailing Lists Information to Mailing Lists Information Edit Form
        else {
            mailingList = mlInfo.getMailingLists();
            if (mailingList != null) {
                MailingListData data = null;
                Iterator itr = mailingList.iterator();
                
                while (itr.hasNext()) {
                    data = (MailingListData) itr.next();
                    if (data.getMailingListNm().indexOf("Bulk") > 0) {
                        editForm.setMailingListPk(data.getMailingListPk());
                        editForm.setMailingListBulkCount(new Integer(data.getMailingListBulkCount()).toString());
                    }
                }
            }
            editForm.setPk(mlInfo.getPk());
            request.setAttribute("editMailingListsInformationForm", editForm);
            return mapping.findForward("Edit");
        }
    }
}
