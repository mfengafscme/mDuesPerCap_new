package org.afscme.enterprise.reporting.specialized.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.specialized.RebateCheckFileReport;
import org.afscme.enterprise.reporting.specialized.RebateCheckRegisterReport;
import org.afscme.enterprise.reporting.specialized.FinalRosterReport;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * @struts:action   path="/rebateCheckFileReport"
 *                  name="rebateCheckFileForm"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Reporting/Specialized/RebateCheckFileInput.jsp"
 */
public class RebateCheckFileAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        RebateCheckFileForm rptForm = (RebateCheckFileForm)form;
        RebateCheckFileReport checkFileReport = new RebateCheckFileReport();
        RebateCheckRegisterReport checkRegisterReport = new RebateCheckRegisterReport();
        FinalRosterReport finalRosterReport = new FinalRosterReport();
        
        // Validation
        ActionErrors errors = new ActionErrors();
        if (TextUtil.isEmptyOrSpaces(rptForm.getCheckNumber())) {
            errors.add("checkNumber", new ActionError("error.field.required.generic", "Starting Check Number"));
        } else if (!TextUtil.isInt(rptForm.getCheckNumber())) {
            errors.add("checkNumber", new ActionError("error.field.mustBeInt.generic", "Starting Check Number"));
        }
        
        // Present errors if any exists
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
        checkFileReport.setStartingCheckNumber(new Integer(rptForm.getCheckNumber()).intValue());
        checkFileReport.setUserPk(usd.getPersonPk());

        // Submit the Rebate Check File report for generation
        s_baseReport.generateReport(usd.getUserId(), new Integer(5), new MediaType(MediaType.PRINT), checkFileReport);
        
        // HLM Fix defect #749
        // Rebate Check File report requires to execute some processes.
        // We need to set atleast 5 secs wait time to allow the processes to finish ..
        Object pause = "pause";
        synchronized (pause) {
            pause.wait(5000); 
        }
        
        // Submit the Rebate Check Register report for generation
        s_baseReport.generateReport(usd.getUserId(), new Integer(5), new MediaType(MediaType.PRINT), checkRegisterReport);
        
        // Submit the Final Roster report for generation        
        s_baseReport.generateReport(usd.getUserId(), new Integer(5), new MediaType(MediaType.PRINT), finalRosterReport);
        
        // Show the configuration screen        
        String msg = "Your report has been submitted for processing.  " +
        "When completed, it will be emailed to " + ConfigUtil.getConfigurationData().getReportQueueEmail();
        
        request.setAttribute("title", "Report Submitted");
        request.setAttribute("content", msg);
        return mapping.findForward("Message");
    }
}
