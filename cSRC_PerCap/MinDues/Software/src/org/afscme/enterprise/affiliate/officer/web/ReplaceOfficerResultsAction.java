/*
 * ReplaceOfficerResultsaction.java
 *
 * Created on December 16, 2003, 11:09 AM
 */

package org.afscme.enterprise.affiliate.officer.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.*;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.officer.AffiliateOfficerMaintenance;
import org.afscme.enterprise.affiliate.officer.web.ReplaceOfficerResultsForm;
import org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria;

/**
 *
 * @struts:action   path="/replaceOfficerResults"
 *                  name="replaceOfficerResultsForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="View"  path="/Membership/ReplaceOfficerResults.jsp" 
 *
 */
public class ReplaceOfficerResultsAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) throws Exception {
                
        ReplaceOfficerResultsForm rorf = (ReplaceOfficerResultsForm)form;        
        ReplaceOfficerCriteria oc = new ReplaceOfficerCriteria();        
        
        // set defaults when entering the first time
        if (request.getParameter("new") != null) {
            rorf.setPage(0);
            rorf.setResults(null);
            rorf.setSortBy(null);
            rorf.setTotal(0);
        }        
        
        oc.setFirstName(rorf.getFirstName());
        oc.setMiddleName(rorf.getMiddleName());
        oc.setLastName(rorf.getLastName());
        oc.setSuffix(rorf.getSuffix());
        oc.setAffPk(getCurrentAffiliatePk(request));
        oc.setElected(rorf.isElected());
        oc.setPage(rorf.getPage());        
        oc.setPageSize(rorf.getPageSize());
        oc.setOrderBy(rorf.getSortBy());
        oc.setOrdering(rorf.getOrder());
        
        Collection results = new ArrayList();
        
        rorf.setTotal(s_maintainAffiliateOfficers.maintainOfficerSearch(oc, results, true));
        rorf.setResults(results);
        
        if (rorf.getTotal() == 0) {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, 
                          new ActionError("error.noResults"));
            saveErrors(request, errors);                        
        }               
        
        return mapping.findForward("View");                        
    }    
}
