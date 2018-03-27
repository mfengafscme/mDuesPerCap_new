package org.afscme.enterprise.cards.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.NewAffiliate;
import org.afscme.enterprise.affiliate.AffiliateCriteria;
import org.afscme.enterprise.affiliate.AffiliateResult;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.affiliate.officer.OfficeData;

/**
 * @struts:action   path="/saveBypassedAffiliateStateNationalAction"
 *                  name="saveBypassedaffiliateStateNational"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Membership/GetBypassedAffiliates.jsp"
 */
public class SaveBypassedAffiliateStateNationalAction extends AFSCMEAction {
    
    /** Creates a new instance of SaveBypassedAffiliateStateNationalAction*/
    public SaveBypassedAffiliateStateNationalAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response,
    UserSecurityData usd)
    throws Exception {
        
        HttpSession session = request.getSession();
        GetBypassedAffiliatesForm gbaf = null;
        if (request.getParameter("continue") != null || request.getParameter("cancel") != null) {
            gbaf = (GetBypassedAffiliatesForm)session.getAttribute("getBypassedAffiliateForm");;
            cleanupSession(session);
        } else {
            gbaf = (GetBypassedAffiliatesForm)form;
        }
        
        NewAffiliate newAffil = null;
        
        // check for cancel option from the Duplicate Affiliate screen
        if (request.getParameter("cancel") != null) {
            request.setAttribute("getBypassedAffiliatesForm", gbaf);
            cleanupSession(session);
            return mapping.getInputForward();
        }
        
         if (gbaf.getAffPk() != null) {
            // Came from Duplicate Affiliate screen. Parent was chosen.
            // Retrieve cached newAffiliate and set the parent fk to value in new form
            newAffil = (NewAffiliate)session.getAttribute("newAffiliate");
            newAffil.setParentAffPk(gbaf.getAffPk());
            // Retrieve cached form and set the parent fk to the value retrieve in the new form
            gbaf = (GetBypassedAffiliatesForm)session.getAttribute("newAffiliateForm");
            gbaf.setParentFk(newAffil.getParentAffPk());
            
            // cleanup the session
            cleanupSession(session);
        } else {
            // Need to find parent Affiliate based on hierarchy in Affiliate Identifier
           /* newAffil = gbaf.getNewAffiliate();*/
            AffiliateCriteria parentCriteria = new AffiliateCriteria();
            Collection parents = s_maintainAffiliates.findParentCriteria(newAffil.getAffiliateId(), parentCriteria);
            
            if (parents == null) {
                newAffil.setParentAffPk(null); // set this for add
            } else if (parents.size() == 1) {
                AffiliateResult result = (AffiliateResult)parents.toArray()[0];
                newAffil.setParentAffPk(result.getAffPk());
            } else { // have user choose parent...
                /* temporarily store GetBypassedAffiliatesForm in
                 * the session for the return
                 */
                session.setAttribute("newAffiliate", newAffil);
                session.setAttribute("newAffiliateForm", gbaf);
                /* Use the parentCriteria to build an AffiliateFinderForm and
                 * forward control to the finder action.
                 */
                setCurrentAffiliateFinderForm(request,
                parentCriteria.getAffiliateIdCode(), parentCriteria.getAffiliateIdCouncil(),
                parentCriteria.getAffiliateIdLocal(), parentCriteria.getAffiliateIdState(),
                parentCriteria.getAffiliateIdSubUnit(), parentCriteria.getAffiliateIdType(),
                "/saveAffiliateDetailAdd.action", "/saveAffiliateDetailAdd.action?cancel"
                );
                // redirect to the searchAffiliateFinder functionality
                return mapping.findForward("SearchAffiliateFinderRedirect");
            }
        }
        gbaf.setParentFk(newAffil.getParentAffPk()); // need for validation
        
        // validate the affiliate
        ActionErrors errors = gbaf.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        // before adding the affiliate, check and see if its a duplicate
        if (request.getParameter("continue") == null) {
            AffiliateCriteria ac = new AffiliateCriteria();
            ac.setAffiliateIdType(gbaf.getAffIdType());
            ac.setAffiliateIdLocal(gbaf.getAffIdLocal());
            ac.setAffiliateIdState(gbaf.getAffIdState());
            ac.setAffiliateIdSubUnit(gbaf.getAffIdSubUnit());
            ac.setAffiliateIdCouncil(gbaf.getAffIdCouncil());
            
            Collection results = s_maintainAffiliates.searchAffiliates(ac);
            if (!CollectionUtil.isEmpty(results)) {
                setCurrentAffiliateFinderForm(request,
                null, ac.getAffiliateIdCouncil(),
                ac.getAffiliateIdLocal(), ac.getAffiliateIdState(),
                ac.getAffiliateIdSubUnit(), ac.getAffiliateIdType(),
                "/saveAffiliateDetailAdd.action?continue", "/saveAffiliateDetailAdd.action?cancel"
                );
                session.setAttribute("getBypassedAffiliatesForm", gbaf);
                return mapping.findForward("SearchAffiliateFinderRedirect");
            }
        }
        
        // add the affiliate
        int pk = s_maintainAffiliates.addAffiliate(newAffil, usd.getPersonPk());
        if (pk < 1) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.codes.affiliate." + pk));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } else {
            gbaf.setAffPk(new Integer(pk));
        }
        
        s_maintainUsers.addAffToSuperUsers(new Integer(pk));
       /* 
        // Insert Default officers if it was selected
        if (gbaf.isGenerateDefaultOffices()) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            OfficeData od = new OfficeData();
            od.setAffPk(new Integer(pk));
            
            // Insert President
            od.setLengthOfTerm(new Integer("63001"));
            od.setNumWithTitle(new Integer("1"));
            od.setMonthOfElection(new Integer("78001"));
            od.setTermEnd(new Integer(year + 1));
            od.setOfficePk(new Integer("9"));
            s_maintainAffiliateOfficers.addOfficerTitle(od, usd.getPersonPk());
            
            // Insert VP
            od.setOfficePk(new Integer("13"));
            s_maintainAffiliateOfficers.addOfficerTitle(od, usd.getPersonPk());
            
            // Insert recording secretary
            od.setOfficePk(new Integer("24"));
            s_maintainAffiliateOfficers.addOfficerTitle(od, usd.getPersonPk());
            
            // Insert secretary treasurer
            od.setOfficePk(new Integer("28"));
            od.setReportingOfficer(new Boolean(true));
            s_maintainAffiliateOfficers.addOfficerTitle(od, usd.getPersonPk());
            
            // Insert Exec Board Member
            od.setReportingOfficer(new Boolean(false));
            od.setNumWithTitle(new Integer("3"));
            od.setOfficePk(new Integer("33"));
            s_maintainAffiliateOfficers.addOfficerTitle(od, usd.getPersonPk());
            
            // Insert trustee
            od.setLengthOfTerm(new Integer("63003"));
            od.setTermEnd(new Integer(year + 3));
            od.setOfficePk(new Integer("41"));
            s_maintainAffiliateOfficers.addOfficerTitle(od, usd.getPersonPk());
        }
       */ 
        setCurrentAffiliatePk(request, new Integer(pk));
        return mapping.findForward("ViewAffiliateDetail");
    }
    
    private void cleanupSession(HttpSession session) {
        session.removeAttribute("newAffiliate");
        session.removeAttribute("newAffiliateForm");
        session.removeAttribute("affiliateFinderForm");
        session.removeAttribute("affiliateDetailAddForm");
    }
    
}
