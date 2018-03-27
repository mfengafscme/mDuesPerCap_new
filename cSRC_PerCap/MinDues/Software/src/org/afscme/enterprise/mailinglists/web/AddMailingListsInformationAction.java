package org.afscme.enterprise.mailinglists.web;

import java.util.List;
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
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.mailinglists.web.MailingListsInformationForm;

/**
 * @struts:action   path="/addMailingListsInformation"
 *                  scope="request"
 *                  validate="true"
 *        	    name="addMailingListsInformationForm"
 *                  input="/Membership/MailingListsInformationAdd.jsp"
 *
 * @struts:action-forward   name="View"  path="/Membership/MailingListsInformation.jsp"
 */
public class AddMailingListsInformationAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        // Request to submit
        if (!isCancelled(request)) {
            AddMailingListsInformationForm addForm = (AddMailingListsInformationForm)form;
            // Get Mailing Lists Information in session
            MailingListsInformationForm mlInfo = (MailingListsInformationForm) request.getSession().getAttribute("mailingListsInformationForm");
            if (mlInfo == null) {
                throw new RuntimeException("Mailing Lists Information is not in session.");
            }

            // For Mailing Lists By Organization, the Bulk Count is a required field if the Mail Code is a Bulk Type
            // For all others, the Bulk Count is default to 1 if it is not set
            if (addForm.getMailingListBulkCount() == null || 
                TextUtil.isEmpty(addForm.getMailingListBulkCount()) || 
                new Integer(addForm.getMailingListBulkCount()).intValue() <= 0) {
                if (!mlInfo.isMLBP() && addForm.getMailingListPk().intValue() == 99) {
                    ActionErrors errors = new ActionErrors();
                    return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.field.required.mailingListBulkCount");
                } else {
                    addForm.setMailingListBulkCount("1");
                }
            }
            
            // Determine the rules for getting default addess for Person-type entity or 
            // default location for Organization-type entity
            List mailingList = null;
            Integer addressPK = null;
            addForm.setPk(mlInfo.getPk());            
            if (mlInfo.isMLBP()) {
                // Add this person/member to Mailing List.
                // Use System Address as the default Mailing List Address association.
                PersonAddressRecord personAddress = s_systemAddress.getSystemAddress(addForm.getPk());
                if (personAddress != null) {
                    addressPK = personAddress.getRecordData().getPk();
                } 
                s_maintainPersonMailingLists.addPersonMailingList(addForm.getPk(), addForm.getMailingListPk(), addressPK, usd.getPersonPk());                    
                mailingList = s_maintainPersonMailingLists.getPersonMailingLists(addForm.getPk());
            } else {
                // Add this organization/affiliate to Mailing List
                // Use Primary Location as the default Mailing List location association.
                addressPK = s_maintainOrgMailingLists.getOrgPrimaryLocation(addForm.getPk());
                s_maintainOrgMailingLists.addMailingList(addForm.getPk(), addForm.getMailingListPk(), new Integer(addForm.getMailingListBulkCount()).intValue(), addressPK, usd.getPersonPk());
                mailingList = s_maintainOrgMailingLists.getMailingLists(addForm.getPk());
            }

            // reset mailing lists information
            mlInfo.setMailingLists(mailingList);           
            mlInfo.setPrivileges(mailingList); 
            
            // If Primary Location or Default Address is not found, then forward to Address Association page.
            if (addressPK == null) {
                return new ActionForward("/viewMailingListsAddressAssociation.action?mailingListPk="+addForm.getMailingListPk());
            }
        }
        
        // Reload the Mailing Lists Information page
        return mapping.findForward("View");
    }
}