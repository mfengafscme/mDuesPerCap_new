package org.afscme.enterprise.rebate.web;

import java.util.Collection;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.log4j.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.rebate.PRBConstants;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.affiliate.AffiliateResult;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;


public abstract class DuesPaidInfoAction extends AFSCMEAction {
    
    static Logger log = Logger.getLogger(DuesPaidInfoAction.class);
    ActionErrors errors;
    
    protected void validateDuesPaidToAffIDs(ActionForm form, HttpServletRequest request, String formName) throws Exception {
        DuesPaidInfoForm editForm = (DuesPaidInfoForm)form;
        AffiliateIdentifier affID = null;
        AffiliateResult result = null;
        Collection affIDs = null;
        errors = new ActionErrors();  
        String formAction = null;
        
        if (formName.equalsIgnoreCase(PRBConstants.PRB_REQUEST_FORM)) {
            formAction = PRBConstants.PRB_REQUEST_ACTION;                
        } else if (formName.equalsIgnoreCase(PRBConstants.PRB_APPLICATION_FORM)) {
            formAction = PRBConstants.PRB_APPLICATION_ACTION;
        } else if (formName.equalsIgnoreCase(PRBConstants.PRB_ANNUAL_REBATE_INFORMATION_FORM)) {
            formAction = PRBConstants.PRB_ANNUAL_REBATE_INFORMATION_ACTION;
        } else {
            throw new JspException("No form name specified.");
        }

        // Check for the 1st Dues Paid To Affiliate Identifier
        if (editForm.getAffPk_1()==null) {
            affIDs = findAffiliatesWithID(editForm.getCode_1(), editForm.getCouncil_1(), editForm.getLocal_1(), editForm.getState_1(), editForm.getSubUnit_1(), editForm.getType_1());
            if (affIDs == null) {
                errors.add("affPk_1", new ActionError("error.field.affID.invalid", "#1"));
                saveErrors(request, errors);
            } else if (affIDs.size() == 1) {
                result = (AffiliateResult)affIDs.toArray()[0];
                affID = result.getAffiliateId();
                editForm.setAffPk_1(result.getAffPk());
                editForm.setCode_1(affID.getCode());
                editForm.setCouncil_1(affID.getCouncil());
                editForm.setLocal_1(affID.getLocal());
                editForm.setState_1(affID.getState());
                editForm.setSubUnit_1(affID.getSubUnit());
                editForm.setType_1(affID.getType());
            } else {
                // have user choose parent...
                    /* Set the appropriate actions in the finder form, and
                     * redirect user to the Duplication Affiliate Results screen
                     * to choose an affiliate. Also set finder to false.
                     */
                setCurrentAffiliateFinderForm(request, editForm.getCode_1(), editForm.getCouncil_1(), editForm.getLocal_1(), editForm.getState_1(), 
                                              editForm.getSubUnit_1(), editForm.getType_1(), formAction, formAction);
                editForm.setSearch(true);
                return;
            }
        }
        
        // Check for the 2nd Dues Paid To Affiliate Identifier
        if (editForm.getAffPk_2()==null && (editForm.getType_2()!=null || editForm.getLocal_2()!=null || editForm.getState_2()!=null || editForm.getCouncil_2()!=null)) {
            affIDs = findAffiliatesWithID(editForm.getCode_2(), editForm.getCouncil_2(), editForm.getLocal_2(), editForm.getState_2(), editForm.getSubUnit_2(), editForm.getType_2());
            if (affIDs == null) {
                errors.add("affPk_2", new ActionError("error.field.affID.invalid", "#2"));
                saveErrors(request, errors);
            } else if (affIDs.size() == 1) {
                result = (AffiliateResult)affIDs.toArray()[0];
                affID = result.getAffiliateId();
                editForm.setAffPk_2(result.getAffPk());
                editForm.setCode_2(affID.getCode());
                editForm.setCouncil_2(affID.getCouncil());
                editForm.setLocal_2(affID.getLocal());
                editForm.setState_2(affID.getState());
                editForm.setSubUnit_2(affID.getSubUnit());
                editForm.setType_2(affID.getType());
            } else {
                setCurrentAffiliateFinderForm(request, editForm.getCode_2(), editForm.getCouncil_2(), editForm.getLocal_2(),
                                              editForm.getState_2(), editForm.getSubUnit_2(), editForm.getType_2(), formAction, formAction);
                editForm.setSearch(true);
                return;
            }
        }
        
        // Check for the 3rd Dues Paid To Affiliate Identifier
        if (editForm.getAffPk_3()==null && (editForm.getType_3()!=null || editForm.getLocal_3()!=null || editForm.getState_3()!=null || editForm.getCouncil_3()!=null)) {
            affIDs = findAffiliatesWithID(editForm.getCode_3(), editForm.getCouncil_3(), editForm.getLocal_3(), editForm.getState_3(), editForm.getSubUnit_3(), editForm.getType_3());
            if (affIDs == null) {
                errors.add("affPk_3", new ActionError("error.field.affID.invalid", "#3"));
                saveErrors(request, errors);
            } else if (affIDs.size() == 1) {
                result = (AffiliateResult)affIDs.toArray()[0];
                affID = result.getAffiliateId();
                editForm.setAffPk_3(result.getAffPk());
                editForm.setCode_3(affID.getCode());
                editForm.setCouncil_3(affID.getCouncil());
                editForm.setLocal_3(affID.getLocal());
                editForm.setState_3(affID.getState());
                editForm.setSubUnit_3(affID.getSubUnit());
                editForm.setType_3(affID.getType());
            } else {
                // have user choose parent...
                setCurrentAffiliateFinderForm(request, editForm.getCode_3(), editForm.getCouncil_3(), editForm.getLocal_3(),
                                              editForm.getState_3(), editForm.getSubUnit_3(), editForm.getType_3(), formAction, formAction);
                editForm.setSearch(true);
                return;
            }
        }
    }
    
    protected void setDuesPaidToAffIDs(ActionForm form, Integer affPk) {
        DuesPaidInfoForm editForm = (DuesPaidInfoForm)form;        
        AffiliateIdentifier affId = s_maintainPoliticalRebate.getAffiliateIdentifier(affPk);
        
        if (affId != null) {
            if (editForm.getAffPk_1()==null) {
                editForm.setAffPk_1(affPk);
                editForm.setCode_1(affId.getCode());
                editForm.setType_1(affId.getType());
                editForm.setLocal_1(affId.getLocal());
                editForm.setState_1(affId.getState());
                editForm.setSubUnit_1(affId.getSubUnit());
                editForm.setCouncil_1(affId.getCouncil());
            } else if (editForm.getAffPk_2()==null) {
                editForm.setAffPk_2(affPk);
                editForm.setCode_2(affId.getCode());
                editForm.setType_2(affId.getType());
                editForm.setLocal_2(affId.getLocal());
                editForm.setState_2(affId.getState());
                editForm.setSubUnit_2(affId.getSubUnit());
                editForm.setCouncil_2(affId.getCouncil());
            } else if (editForm.getAffPk_3()==null) {
                editForm.setAffPk_3(affPk);
                editForm.setCode_3(affId.getCode());
                editForm.setType_3(affId.getType());
                editForm.setLocal_3(affId.getLocal());
                editForm.setState_3(affId.getState());
                editForm.setSubUnit_3(affId.getSubUnit());
                editForm.setCouncil_3(affId.getCouncil());
            }
        }
    }
    
    public void checkAffiliatesAssociationError(HttpServletRequest request, int result) {
        errors = new ActionErrors();
        if (result <= 0) {
            if (result == PRBConstants.ERROR_MAX_REQUEST_AFFILIATE_ALLOWED)
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.prbRequest.max.affiliates"));
            else
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.prbRequest.affiliate.association"));
            saveErrors(request, errors);
        }
    }    
}