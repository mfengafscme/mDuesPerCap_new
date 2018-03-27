package org.afscme.enterprise.rebate.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.rebate.PRBConstants;
import org.afscme.enterprise.codes.Codes.RebateStatus;
import org.afscme.enterprise.codes.Codes.RebateMbrType;
import org.afscme.enterprise.codes.Codes.RebateAcceptanceCode;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.person.Persona;
import org.afscme.enterprise.person.PRBApplicationData;
import org.afscme.enterprise.person.PRBAffiliateData;

/**
 * @struts:action   path="/savePoliticalRebateApplication"
 *		    name="politicalRebateApplicationForm"
 *                  validate="true"
 *                  scope="session"
 *                  input="/Membership/PoliticalRebateApplicationEdit.jsp"
 *
 * @struts:action-forward   name="View"  path="/viewPoliticalRebateApplication.action"
 * @struts:action-forward   name="Summary"  path="/viewPoliticalRebateSummary.action"
 */
public class SavePoliticalRebateApplicationAction extends DuesPaidInfoAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateApplicationForm editForm = (PoliticalRebateApplicationForm)form;
        
        if (!isCancelled(request)) {
            // Validate PRB Application Affiliates for the Dues Paid To
            validateDuesPaidToAffIDs(form, request, mapping.getName());
            
            // Forward to Affiliate Search Finder to allow user to select the Affiliate Identifier
            // if duplicate Affiliate Identifiers found
            if (editForm.isSearch()) {
                editForm.setSearch(false);
                return mapping.findForward("SearchAffiliateFinderRedirect");
            }
            
            // Present errors if any invalid Affiliate Identifier found
            if (!errors.isEmpty()) {
                return mapping.getInputForward();
            }
            
            // Collect form attributes and put them into PRBApplicationData
            PRBApplicationData appData = editForm.getPRBApplicationData();
            
            // Create or Update the Political Rebate Annual Rebate record IFF Non-Negative Application record
            // Non-Negative Application record = Application Evaluation Code is blank AND Application Comment Analysis Code is not null
            boolean updateAnnualRebate = false;
            boolean isMember = false;
            
            // Set Rebate Application Status to Approved
            if ((editForm.getAppEvalCode()==null || editForm.getAppEvalCode().intValue()==0) && 
                (editForm.getAppCommentAnalCode()!=null && editForm.getAppCommentAnalCode().intValue()>0)) {
                updateAnnualRebate = true;
                isMember = s_maintainPersons.isPersona(editForm.getPk(), Persona.MEMBER);                    
            }
            
            // Fix defect AFSM200000508
            // Set Rebate Application Status to Denied
            else if (editForm.getAppEvalCode()!=null && editForm.getAppEvalCode().intValue()>0) {
                s_maintainPoliticalRebate.updatePRBApplicationStatus(editForm.getAppPk(), usd.getPersonPk(), RebateStatus.D.intValue());
            }

            // Save political rebate application data
            s_maintainPoliticalRebate.updatePRBApplication(appData, usd.getPersonPk());
            
            // Save PRB Application Affiliates data
            int rs;
            PRBAffiliateData prbAffData = null;
            RecordData recData = new RecordData();
            recData.setPk(editForm.getPk());
            recData.setCreatedBy(usd.getPersonPk());
            recData.setModifiedBy(usd.getPersonPk());
            if (editForm.getAffPk_1()!=null) {
                prbAffData = editForm.getPRBDuesPaid(1);
                prbAffData.setTheRecordData(recData);
                rs = s_maintainPoliticalRebate.setPRBAffiliate(prbAffData, editForm.getAppPk(), PRBConstants.PRB_APPLICATION);
                
                // Check and present errors if any problem encountered while setting PRB Affiliates
                checkAffiliatesAssociationError(request, rs);
                if (!errors.isEmpty()) {
                    return mapping.getInputForward();
                }
                
                // Create/Update Annual Rebate Record
                if (updateAnnualRebate) {
                    if (!isMember) 
                        prbAffData.setRbtMbrTypePk(null);                    
                    prbAffData.setAcceptanceCodePk(null);                    
                    s_maintainPoliticalRebate.updatePRBRosterPerson(prbAffData, editForm.getPrbYear());
                }
            }
            if (editForm.getAffPk_2()!=null) {
                prbAffData = editForm.getPRBDuesPaid(2);
                prbAffData.setTheRecordData(recData);
                rs = s_maintainPoliticalRebate.setPRBAffiliate(prbAffData, editForm.getAppPk(), PRBConstants.PRB_APPLICATION);

                // Check and present errors if any problem encountered while setting PRB Affiliates
                checkAffiliatesAssociationError(request, rs);
                if (!errors.isEmpty()) {
                    return mapping.getInputForward();
                }

                // Create/Update Annual Rebate Record
                if (updateAnnualRebate) {
                    if (!isMember) 
                        prbAffData.setRbtMbrTypePk(null);
                    prbAffData.setAcceptanceCodePk(null);
                    s_maintainPoliticalRebate.updatePRBRosterPerson(prbAffData, editForm.getPrbYear());
                }                
            }
            if (editForm.getAffPk_3()!=null) {
                prbAffData = editForm.getPRBDuesPaid(3);
                prbAffData.setTheRecordData(recData);
                rs = s_maintainPoliticalRebate.setPRBAffiliate(prbAffData, editForm.getAppPk(), PRBConstants.PRB_APPLICATION);

                // Check and present errors if any problem encountered while setting PRB Affiliates
                checkAffiliatesAssociationError(request, rs);
                if (!errors.isEmpty()) {
                    return mapping.getInputForward();
                }

                // Create/Update Annual Rebate Record
                if (updateAnnualRebate) {
                    if (!isMember)
                        prbAffData.setRbtMbrTypePk(null);
                    prbAffData.setAcceptanceCodePk(null);
                    s_maintainPoliticalRebate.updatePRBRosterPerson(prbAffData, editForm.getPrbYear());
                }                
            }
            
            // The Annual Rebate record has been generate, now set the
            // PRB_Apps.aff_roster_generate_fg to true
            if (updateAnnualRebate) {
                s_maintainPoliticalRebate.updatePRBApplicationStatus(editForm.getAppPk(), usd.getPersonPk());
            }
        }
        
        // Remove this form from request session
        request.getSession().removeAttribute(PRBConstants.PRB_APPLICATION_FORM);
        request.setAttribute("pk", editForm.getAppPk().toString());
        return mapping.findForward("View");
    }    
}

