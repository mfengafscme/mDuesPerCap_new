package org.afscme.enterprise.rebate.web;

import java.util.Map;
import java.util.Collection;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.rebate.PRBConstants;
import org.afscme.enterprise.person.PRBCheckInfo;
import org.afscme.enterprise.person.PRBAffiliateData;
import org.afscme.enterprise.common.RecordData;

/**
 * @struts:action   path="/savePoliticalRebateAnnualRebateInformation"
 *		    name="politicalRebateAnnualRebateInformationEditForm"
 *                  validate="true"
 *                  scope="session"
 *                  input="/Membership/PoliticalRebateAnnualRebateInformationEdit.jsp"
 *
 * @struts:action-forward   name="View"  path="/viewPoliticalRebateAnnualRebateInformation.action"
 */
public class SavePoliticalRebateAnnualRebateInformationAction extends DuesPaidInfoAction {
    
    static Logger log = Logger.getLogger(SavePoliticalRebateAnnualRebateInformationAction.class);
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateAnnualRebateInformationEditForm editForm = (PoliticalRebateAnnualRebateInformationEditForm)form;
        
        if (!isCancelled(request)) {                    
            // Validate Affiliate Identifiers for the Dues Paid To
            validateDuesPaidToAffIDs(form, request, mapping.getName());

            // Forward to Duplicate Affiliate Identifier Notifier page 
            // to allow user to select the appropriate Affiliate Identifier
            if (editForm.isSearch()) {
                editForm.setSearch(false);
                return mapping.findForward("SearchAffiliateFinderRedirect");
            }
            
            // Present errors if any exists
            if (!errors.isEmpty()) {
                return new ActionForward(mapping.getInput());
            }
                        
            // Get the current person/member pk
            Integer pk = getCurrentPersonPk(request);
                                
            // Save the Political Rebate Annual Rebate Information..            
            // Save Dues Paid Information
            PRBAffiliateData prbAffData = null;
            RecordData recData = new RecordData();
            recData.setPk(pk);
            recData.setCreatedBy(usd.getPersonPk());
            recData.setModifiedBy(usd.getPersonPk());
            if (editForm.getAffPk_1()!=null) {
                prbAffData = editForm.getPRBDuesPaid(1); 
                prbAffData.setTheRecordData(recData);
                s_maintainPoliticalRebate.updatePRBRosterPerson(prbAffData, editForm.getPrbYear());
            }
            if (editForm.getAffPk_2()!=null) {
                prbAffData = editForm.getPRBDuesPaid(2);
                prbAffData.setTheRecordData(recData);
                s_maintainPoliticalRebate.updatePRBRosterPerson(prbAffData, editForm.getPrbYear());
            }
            if (editForm.getAffPk_3()!=null) {
                prbAffData = editForm.getPRBDuesPaid(3);
                prbAffData.setTheRecordData(recData);
                s_maintainPoliticalRebate.updatePRBRosterPerson(prbAffData, editForm.getPrbYear());
            }            
            
            // Save Check Information
            if (!editForm.isActingAsAffiliate()) {
                PRBCheckInfo prbCheckInfo = editForm.getPRBCheckInfo();
                prbCheckInfo.setPersonPK(pk);
                s_maintainPoliticalRebate.updatePRBCheckInfo(prbCheckInfo, usd.getPersonPk());
            }            
        }
        
        // Remove form from session
        request.getSession().removeAttribute(PRBConstants.PRB_ANNUAL_REBATE_INFORMATION_FORM);

        // Set prbYear to return to view Political Rebate Annual Rebate Information page
        request.setAttribute("prbYear", editForm.getPrbYear());        
        return mapping.findForward("View");
    }
}
