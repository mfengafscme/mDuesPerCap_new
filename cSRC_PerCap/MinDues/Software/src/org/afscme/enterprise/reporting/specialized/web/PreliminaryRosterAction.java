package org.afscme.enterprise.reporting.specialized.web;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.specialized.PreliminaryRosterReport;
import org.afscme.enterprise.reporting.specialized.RebateUpdateFileReport;
import org.afscme.enterprise.reporting.specialized.PreliminaryRosterAffiliate;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:action   path="/preliminaryRosterReport"
 *                  name="preliminaryRosterAffiliateForm"
 *                  input="/Membership/PreliminaryRosterAffiliateSelection.jsp"
 *                  validate="false"
 *
 * @struts:action-forward   name="ViewReports"  path="/listRegularReports.action"
 */
public class PreliminaryRosterAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PreliminaryRosterAffiliateForm pra = (PreliminaryRosterAffiliateForm)form;
                
        if (!isCancelled(request)) {            
            String selectedAffPks = "";
            PreliminaryRosterAffiliate affiliate = new PreliminaryRosterAffiliate();
            ArrayList affiliateList = pra.getAffiliateList(false);

            // Get selected affPks from form
            if (!pra.isSelectAll()) {
                for (int i=0; i<affiliateList.size(); i++) {
                    affiliate = (PreliminaryRosterAffiliate) affiliateList.get(i);
                    if (affiliate.isSelected()) {
                        // Construct the affPks list
                        selectedAffPks = (selectedAffPks == "") ?
                            "aff_pk IN ( " + new Integer(affiliate.getAffPk()).intValue() :
                            selectedAffPks.concat(", " + new Integer(affiliate.getAffPk()).intValue());
                    }
                }            
                if (selectedAffPks.length()>0) 
                    selectedAffPks = selectedAffPks.concat(" ) ");
            }
            
            // Generate reports
            if (pra.isSelectAll() || selectedAffPks.length()>0) {
                PreliminaryRosterReport preliminaryRosterReport = new PreliminaryRosterReport();
                RebateUpdateFileReport rebateUpdateFileReport = new RebateUpdateFileReport();
                
                // Set selected affPks to reports
                preliminaryRosterReport.setAffPks(selectedAffPks);
                rebateUpdateFileReport.setAffPks(selectedAffPks);
                
                // Submit the Preliminary report for generation
                s_baseReport.generateReport(usd.getUserId(), new Integer(4), new MediaType(MediaType.PRINT), preliminaryRosterReport);
                
                // Preliminary report requires to execute some processes.
                // We need to set atleast 2 secs wait time to allow the processes to finish ..
                Object pause = "pause";
                synchronized (pause) {
                    pause.wait(2000); 
                }                
                
                // Submit the Rebate Update File report for generation
                s_baseReport.generateReport(usd.getUserId(), new Integer(4), new MediaType(MediaType.PRINT), rebateUpdateFileReport);
                
                // Show the configuration screen
                String msg = "Your report has been submitted for processing.  " +
                "When completed, it will be emailed to " +
                ConfigUtil.getConfigurationData().getReportQueueEmail();
                
                request.setAttribute("title", "Report Submitted");
                request.setAttribute("content", msg);
                return mapping.findForward("Message");
            }
            
            // None of Affiliate IDs were selected - display error
            if (!pra.isSelectAll() && pra.getSize()>0 && selectedAffPks.length()==0) {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.PRBReports.noAffiliateSelected"));
                saveErrors(request, errors);
                request.setAttribute("preliminaryRosterAffiliateForm", pra);
                return mapping.getInputForward();
            }
        }
        return mapping.findForward("ViewReports");
    }
}
