package org.afscme.enterprise.affiliate.officer.web;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.afscme.enterprise.affiliate.officer.AffiliateOfficerMaintenance;
import org.afscme.enterprise.affiliate.officer.EBoardMaintenance;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.util.PropertyComparator;
import org.afscme.enterprise.affiliate.AffiliateData;

/**
 * @struts:action   path="/viewAffiliateOfficerMaintenance"
 *                  scope="session"
 *                  validate="false"
 *                  name="affiliateOfficerMaintenanceForm"
 *               
 * @struts:action-forward   name="View"  path="/Membership/AffiliateOfficerMaintenance.jsp"
 */
public class ViewAffiliateOfficerMaintenanceAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response,
    UserSecurityData usd) throws Exception {
        
        AffiliateOfficerMaintenanceForm aomf = (AffiliateOfficerMaintenanceForm)form;        
        
        if (request.getParameter("new") != null) {            
            aomf.setSortBy(null);            
        }
        
        Integer aff_pk = getCurrentAffiliatePk(request);        
        
        // get affiliate data so affiliate status can be determined.
        AffiliateData ad = s_maintainAffiliates.getAffiliateData(aff_pk);
        
        // fetch officers, passing in affiliate and affiliate status
        ArrayList eboard = s_maintainAffiliateOfficers.getAutoEBoardOfficers(aff_pk);               
        Map officers = s_maintainAffiliateOfficers.getOfficerMaintenanceList(aff_pk, ad.getStatusCodePk(), usd.isActingAsAffiliate());
        
        if(aomf.getSortBy() != null && aomf.getSortBy().startsWith("officer."))
        {
            String sortProperty = aomf.getSortBy().substring(8);
            boolean ascending = aomf.getOrder() > 0;
            List officerList = new ArrayList(officers.values());
            Comparator comp = new PropertyComparator(AffiliateOfficerMaintenance.class, sortProperty, ascending);
            Collections.sort(officerList, comp);
			officers = new TreeMap();
                
            for(int i = 0, max = officerList.size(); i < max; i++)
            {
                officers.put(new Integer(i), officerList.get(i));
            }
        }
        
        if(aomf.getSortBy() != null && aomf.getSortBy().startsWith("eboard."))
        {
            String sortProperty = aomf.getSortBy().substring(7);
            boolean ascending = aomf.getOrder() > 0;
            Comparator comp = new PropertyComparator(EBoardMaintenance.class, sortProperty, ascending);
            Collections.sort(eboard, comp);			         
        }        
        
        aomf.setExecutives(eboard);
        aomf.setOfficerList(officers);
        log.debug("OfficerList Size = " + aomf.getOfficerList().size());
                
        //Setup test data to form
        return mapping.findForward("View");
    }
}
