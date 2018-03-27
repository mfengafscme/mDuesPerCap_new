package org.afscme.enterprise.affiliate.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.AffiliateCriteria;
import org.afscme.enterprise.affiliate.AffiliateResult;
/** 
 * @struts:action   path="/saveCouncilAffiliation"
 *                  validate="true"
 *                  scope="session"
 *                  name="councilAffiliationForm"
 *                  input="/Membership/CouncilAffiliationMaintenance.jsp"
 */
public class SaveCouncilAffiliationAction extends AFSCMEAction {
    
    /** Creates a new instance of SaveCouncilAffiliationAction */
    public SaveCouncilAffiliationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        HttpSession session = request.getSession(true);
        CouncilAffiliationForm caf = (CouncilAffiliationForm)form;
        Integer affPk = getCurrentAffiliatePk(request);
        if (affPk == null) {
            throw new JspException("No current Affiliate is defined for which to perform this action Charter.");
        }
        if (caf.getAffPk() == null) {
            // check for request param from the Duplicate Affiliate Results screen.
            Integer councilPk = null;
            try {
                councilPk = new Integer(request.getParameter("affPk"));
            } catch (NumberFormatException nfe) {
                // ignore and leave value null...
            }
            if (councilPk == null) {
                // check for multiple affiliates 
                Collection councils = findAffiliatesWithID(
                                            caf.getAffIdCode(), caf.getAffIdCouncil(), 
                                            caf.getAffIdLocal(), caf.getAffIdState(), 
                                            caf.getAffIdSubUnit(), caf.getAffIdType()
                );
                

                if (councils == null) {
                    ActionErrors errors = new ActionErrors();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noResultsFound"));
                    saveErrors(request, errors);
                    return mapping.getInputForward();
                } else if (councils.size() == 1) {
                    AffiliateResult result = (AffiliateResult)councils.toArray()[0];
                    caf.setAffPk(result.getAffPk());
                } else { 
                    // have user choose parent...
                    /* Set the appropriate actions in the finder form, and 
                     * redirect user to the Duplication Affiliate Results screen 
                     * to choose an affiliate. Also set finder to false.
                     */
                    caf.setLinkAction("/saveCouncilAffiliation.action");
                    caf.setCancelAction("/viewCharterInformation.action");
                    caf.setFinder(false);
                    setCurrentAffiliateFinderForm(request, caf.getAffIdCode(), 
                            caf.getAffIdCouncil(), caf.getAffIdLocal(), 
                            caf.getAffIdState(), caf.getAffIdSubUnit(), 
                            caf.getAffIdType(), "/saveCouncilAffiliation.action", 
                            "/viewCharterInformation.action"
                    );
                    return mapping.findForward("SearchAffiliateFinderRedirect");
                }
            } else {
                caf.setAffPk(councilPk);
            }
        } // else do nothing. we already have the new council's pk... 
        
        int code = s_maintainAffiliates.addAffiliatedCouncil(affPk, caf.getAffPk(), usd.getPersonPk());
        log.debug("    addAffiliatedCouncil returned a value of " + code);
        if (code < 0) {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.codes.affiliate." + code));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        session.removeAttribute("councilAffiliationForm");
        setCurrentAffiliate(request, affPk);
        return mapping.findForward("ViewCharter");
    }
    
}
