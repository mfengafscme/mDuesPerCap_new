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
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.person.PRBRequestData;
import org.afscme.enterprise.person.PRBAffiliateData;
import org.apache.log4j.Logger;


/**
 * @struts:action   path="/savePoliticalRebateRequest"
 *		    name="politicalRebateRequestForm"
 *                  validate="true"
 *                  scope="session"
 *                  input="/Membership/PoliticalRebateRequestEdit.jsp"
 *
 * @struts:action-forward   name="View"  path="/viewPoliticalRebateRequest.action"
 * @struts:action-forward   name="Summary"  path="/viewPoliticalRebateSummary.action"
 * @struts:action-forward   name="SummaryByYear"  path="/viewPoliticalRebateSummaryByYear.action" redirect="true"
 */
public class SavePoliticalRebateRequestAction extends DuesPaidInfoAction {
    
    static Logger logger = Logger.getLogger(SavePoliticalRebateRequestAction.class);
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateRequestForm editForm = (PoliticalRebateRequestForm)form;
        boolean saved = false;
        
        if (!isCancelled(request)) {
            // Validate PRB Request Affiliates for the Dues Paid To
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
            
            // Collect form attributes and put them into PRBRequestData
            PRBRequestData rqstData = editForm.getPRBRequestData();
            
            // Save political rebate request data
            if (editForm.isEdit()) {
                // Update PRB Request
                s_maintainPoliticalRebate.updatePRBRequest(rqstData, usd.getPersonPk());
            } else {
                // Add new PRB Request
                editForm.setRqstPk(s_maintainPoliticalRebate.addPRBRequest(rqstData, usd.getPersonPk()));
            }
            
            logger.debug(">>>>> Saving Political Rebate Request <<<<<");
            logger.debug("PRB Request Form: " +editForm);
            logger.debug("The request PK: " +editForm.getRqstPk());
            logger.debug(">>>>> Saving Political Rebate Request <<<<<");
            
            // PRB Request Primary key is not established or not available, can't go any further.
            if (editForm.getRqstPk() == null || editForm.getRqstPk().intValue()<=0) {
                errors = new ActionErrors();
                if (editForm.isEdit())
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.prbRequest.edit"));
                else
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.prbRequest.add"));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }
            
            // Save PRB Request Affiliates data
            int rs;
            PRBAffiliateData prbAffData = null;
            RecordData recData = new RecordData();
            recData.setPk(editForm.getPk());
            if (editForm.getAffPk_1()!=null) {
                prbAffData = editForm.getPRBDuesPaid(1);
                prbAffData.setTheRecordData(recData);
                rs = s_maintainPoliticalRebate.setPRBAffiliate(prbAffData, editForm.getRqstPk(), PRBConstants.PRB_REQUEST);
                
                // Check and present errors if any problem encountered while setting PRB Affiliates
                checkAffiliatesAssociationError(request, rs);
                if (!errors.isEmpty()) {
                    return mapping.getInputForward();
                }
            }
            if (editForm.getAffPk_2()!=null) {
                prbAffData = editForm.getPRBDuesPaid(2);
                prbAffData.setTheRecordData(recData);
                rs = s_maintainPoliticalRebate.setPRBAffiliate(prbAffData, editForm.getRqstPk(), PRBConstants.PRB_REQUEST);

                // Check and present errors if any problem encountered while setting PRB Affiliates
                checkAffiliatesAssociationError(request, rs);
                if (!errors.isEmpty()) {
                    return mapping.getInputForward();
                }
            }
            if (editForm.getAffPk_3()!=null) {
                prbAffData = editForm.getPRBDuesPaid(3);
                prbAffData.setTheRecordData(recData);
                rs = s_maintainPoliticalRebate.setPRBAffiliate(prbAffData, editForm.getRqstPk(), PRBConstants.PRB_REQUEST);

                // Check and present errors if any problem encountered while setting PRB Affiliates
                checkAffiliatesAssociationError(request, rs);
                if (!errors.isEmpty()) {
                    return mapping.getInputForward();
                }
            }
            saved = true;
        }
        
        // Remove this form from request session
        request.getSession().removeAttribute(PRBConstants.PRB_REQUEST_FORM);
        request.setAttribute("prbYear", editForm.getPrbYear());
        
        // Forward to the next appropriate page
        if (saved || editForm.isEdit()) {
            request.setAttribute("pk", editForm.getRqstPk().toString());
            return mapping.findForward("View");
        } else if (editForm.getBack() != null && editForm.getBack().equalsIgnoreCase("SummaryByYear")) {
            return mapping.findForward("SummaryByYear");
        }
        return mapping.findForward("Summary");
    }    
}

