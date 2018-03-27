package org.afscme.enterprise.masschange.web;

import java.util.Collection;
// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.afscme.enterprise.affiliate.AffiliateCriteria;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.AffiliateResult;

// AFSCME imports
import org.afscme.enterprise.codes.Codes.AffiliateStatus;
import org.afscme.enterprise.codes.Codes.MassChange;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.masschange.MassChangeData;
import org.afscme.enterprise.masschange.MassChangeRequest;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;

// Apache Imports
import org.apache.log4j.Logger;

/** 
 * @struts:action   path="/saveEnableMassChange"
 *                  name="massChangeForm"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Membership/EnableMassChange.jsp"
 */
public class SaveEnableMassChangeAction extends AFSCMEAction {
    
    static Logger logger = Logger.getLogger(SaveEnableMassChangeAction.class);
    
    /** Creates a new instance of SaveEnableMassChangeAction */
    public SaveEnableMassChangeAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) throws Exception {
                        
        logger.debug("----------------------------------------------------------------");
        logger.debug("Inside SaveEnableMassChangeAction.perform");
        ActionErrors errors = new ActionErrors();
        Integer affPk = getCurrentAffiliatePk(request);
        if (affPk == null) {
            throw new JspException("No current Affiliate is defined for which to retrieve Mass Change Information.");
        }
        HttpSession session = request.getSession(true);
        MassChangeForm mcf = (MassChangeForm)form;
        logger.debug("Form:    " + mcf.toString());
        logger.debug("Input:   " + mapping.getInput());
        
        // check for cancel option from the Duplicate Affiliate screen
        if (request.getParameter("cancel") != null) {
            request.setAttribute("massChangeForm", session.getAttribute("massChangeForm"));
            session.removeAttribute("massChangeForm");
            return mapping.getInputForward();
        }        
        
        // if not coming from Affiliate Finder Duplicate Result Page, then validate form
        if (request.getParameter("affPk") == null) {
            logger.debug("calling validate.");
            errors = mcf.validate(mapping, request); 
            logger.debug("validate done.");
            if (errors != null && !errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.getInputForward();
            }
        }
        
        // handle two alternate flows...
        if (session.getAttribute("massChangeForm") == null) {  // input came from EnableMassChange screen
            errors = new ActionErrors();
            // if status change to merge or split, make sure the appropriate pk is there, or find affiliate
            if (mcf.getMassChangeSelect().intValue() == MassChangeForm.MASS_CHANGE_TYPE_STATUS) {
                int status = mcf.getStatusChangeSelect().intValue();
                
                
                if ((status == MassChangeForm.STATUS_CHANGE_MERGED && mcf.getMergedAffiliatePk() == null) ||
                    (status == MassChangeForm.STATUS_CHANGE_SPLIT && mcf.getSplitAffiliatePk() == null)
                ) {
                    AffiliateIdentifier id = null;
                    if (status == MassChangeForm.STATUS_CHANGE_MERGED) {
                        id = mcf.getMergedAffiliate();
                    } else {
                        id = mcf.getSplitAffiliate();
                    }
                    AffiliateCriteria cr = new AffiliateCriteria();
                    cr.setAffiliateIdCode(id.getCode());
                    cr.setAffiliateIdCouncil(id.getCouncil());
                    cr.setAffiliateIdLocal(id.getLocal());
                    cr.setAffiliateIdState(id.getState());
                    cr.setAffiliateIdSubUnit(id.getSubUnit());
                    cr.setAffiliateIdType(id.getType());
                    Collection results = s_maintainAffiliates.searchAffiliates(cr);
                    if (CollectionUtil.isEmpty(results)) {
                        if (status == MassChangeForm.STATUS_CHANGE_MERGED) {
                            errors.add("mergedAffiliate", new ActionError("error.affiliate.affId.notFound")); 
                        } else {
                            errors.add("splitAffiliate", new ActionError("error.affiliate.affId.notFound")); 
                        }
                    } else if (errors == null || errors.isEmpty()) { // only do this if no errors have been found
                        // cache form in session and forward to the Duplicate Affiliate Screen.
                        session.setAttribute("massChangeForm", mcf);
                        /* Use the parentCriteria to build an AffiliateFinderForm and 
                         * forward control to the finder action.
                         */
                        setCurrentAffiliateFinderForm(request, 
                            cr.getAffiliateIdCode(), cr.getAffiliateIdCouncil(), 
                            cr.getAffiliateIdLocal(), cr.getAffiliateIdState(), 
                            cr.getAffiliateIdSubUnit(), cr.getAffiliateIdType(), 
                            "/saveEnableMassChange.action", "/saveEnableMassChange.action?cancel"
                        );
                        // redirect to the searchAffiliateFinder functionality
                        return mapping.findForward("SearchAffiliateFinderRedirect");
                    }
                } 
            }
            if (errors != null && !errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.getInputForward();
            }
        } else {  // input came from DuplicateAffiliateIdentifierNotifier screen
            // get pk from request and use it to get the data
            Integer pk = new Integer(request.getParameter("affPk"));
            AffiliateData data = s_maintainAffiliates.getAffiliateData(pk);
            if (data == null) { // this should never happen...
                throw new JspException("Affiliate PK from Duplicate Screen did not match a valid Affiliate.");
            }
            
            // retrieve cached form from session, and set appropriate values with pk and id from data
            mcf = (MassChangeForm)session.getAttribute("massChangeForm");
            if (mcf.getStatusChangeSelect().intValue() == MassChangeForm.STATUS_CHANGE_MERGED) {
                mcf.setMergedAffiliatePk(pk);
                mcf.setMergedAffiliate(data.getAffiliateId());
            } else {
                mcf.setSplitAffiliatePk(pk);
                mcf.setSplitAffiliate(data.getAffiliateId());
            }
            
            // reset the form in the request (needed?) and cleanup the session
            request.setAttribute("massChangeForm", mcf);
            session.removeAttribute("massChangeForm");
        }
        
        // store Mass Change Request
        MassChangeRequest mcr = new MassChangeRequest();
        mcr.setAffPk(affPk);
        mcr.setUserPk(usd.getPersonPk());
        
        // build the request;
        MassChangeData mcd;
        switch (mcf.getMassChangeSelect().intValue()) {
            case MassChangeForm.MASS_CHANGE_TYPE_STATUS:
                mcd = new MassChangeData(); // can only have one status change
                switch (mcf.getStatusChangeSelect().intValue()) {
                    case MassChangeForm.STATUS_CHANGE_MERGED:
                        if (mcr.getAffPk().intValue() == mcf.getMergedAffiliatePk().intValue()) {
                            mcf.setMergedAffiliatePk(null);
                            errors.add("mergedAffiliate", new ActionError("error.affiliate.merged.sameAffId")); 
                            saveErrors(request, errors);
                            return mapping.getInputForward();
                        }
                        mcd.setMassChangeType(MassChange.STATUS_CHANGE_MERGED);
                        mcd.setNewAffPk(mcf.getMergedAffiliatePk());
                        mcd.setNewAffiliateID(mcf.getMergedAffiliate());
                        mcd.setStatusChangeType(AffiliateStatus.M);
                        break;
                    case MassChangeForm.STATUS_CHANGE_SPLIT:
                        if (mcr.getAffPk().intValue() == mcf.getSplitAffiliatePk().intValue()) {
                            mcf.setSplitAffiliatePk(null);
                            errors.add("splitAffiliate", new ActionError("error.affiliate.split.sameAffId")); 
                            saveErrors(request, errors);
                            return mapping.getInputForward();
                        }
                        mcd.setMassChangeType(MassChange.STATUS_CHANGE_SPLIT);
                        mcd.setNewAffPk(mcf.getSplitAffiliatePk());
                        mcd.setNewAffiliateID(mcf.getSplitAffiliate());
                        mcd.setStatusChangeType(AffiliateStatus.S);
                        break;
                    case MassChangeForm.STATUS_CHANGE_DEACTIVATED:
                        mcd.setMassChangeType(MassChange.STATUS_CHANGE_DEACTIVATED);
                        mcd.setStatusChangeType(AffiliateStatus.D);
                        break;
                    default:// should never happen...
                        break;
                }
                mcr.addToChangePriorityList(mcd);
                break;
            case MassChangeForm.MASS_CHANGE_TYPE_OTHER:
               if (mcf.isNewAffiliateFg()) {
                    // if its a disassociate/associate, and if its NOT a disassociate
                    if (mcf.getNewAffiliate().getType().charValue() == '#' && !TextUtil.isEmptyOrSpaces(mcf.getNewAffiliate().getCouncil())) {
                        AffiliateCriteria cr = new AffiliateCriteria();
                        cr.setAffiliateIdType(new Character('C'));
                        cr.setAffiliateIdCouncil(mcf.getNewAffiliate().getCouncil());
                        Collection results = s_maintainAffiliates.searchAffiliates(cr);
                        if (CollectionUtil.isEmpty(results)) {
                            errors.add("newAffiliate", new ActionError("error.affiliate.affId.notFound"));
                            saveErrors(request, errors);
                            return mapping.getInputForward();                                
                        }                        
                    }                    
                    mcd = new MassChangeData();
                    mcd.setMassChangeType(MassChange.NEW_AFFILIATE_ID);
                    AffiliateIdentifier ai = s_maintainAffiliates.getAffiliateData(affPk).getAffiliateId();
                    AffiliateIdentifier newAi = mcf.getNewAffiliate();
                    if (!TextUtil.isEmptyOrSpaces(newAi.getCouncil()))
                        ai.setCouncil(newAi.getCouncil());
                    if (!TextUtil.isEmptyOrSpaces(newAi.getLocal()))
                        ai.setLocal(newAi.getLocal());
                    if (!TextUtil.isEmptyOrSpaces(newAi.getState()))
                        ai.setState(newAi.getState());
                    if (!TextUtil.isEmptyOrSpaces(newAi.getSubUnit()))
                        ai.setSubUnit(newAi.getSubUnit());
                    
                    newAi.setCode(s_maintainAffiliates.getNextAffiliateSequenceCode(ai));
                    mcd.setNewAffiliateID(newAi);
                    mcr.addToChangePriorityList(mcd);
                }
                if (mcf.isInfoSourceFg()) {
                    mcd = new MassChangeData();
                    mcd.setMassChangeType(MassChange.MBR_INFO_RPRTG_SRC);
                    mcd.setNewSelect(mcf.getInfoSourceNew());
                    mcr.addToChangePriorityList(mcd);
                }
                if (mcf.isMbrCardBypassFg()) {
                    mcd = new MassChangeData();
                    mcd.setMassChangeType(MassChange.UNIT_NO_CARDS);
                    mcd.setNewFlag(new Boolean(mcf.isMbrCardBypassFgNew()));
                    mcr.addToChangePriorityList(mcd);
                }
                if (mcf.isPeMailBypassFg()) {
                    mcd = new MassChangeData();
                    mcd.setMassChangeType(MassChange.UNIT_NO_PE);
                    mcd.setNewFlag(new Boolean(mcf.isPeMailBypassFgNew()));
                    mcr.addToChangePriorityList(mcd);
                }
                if (mcf.isMbrRenewalFg()) {
                    mcd = new MassChangeData();
                    mcd.setMassChangeType(MassChange.MBR_RENEWAL);
                    mcd.setNewFlag(new Boolean(mcf.isMbrRenewalFgNew()));
                    mcr.addToChangePriorityList(mcd);
                }
                break;
            default:// should never happen...
                break;
        }
        s_massChange.scheduleMassChange(mcr);

        return mapping.findForward("ViewMembershipReporting");
    }
    
}
