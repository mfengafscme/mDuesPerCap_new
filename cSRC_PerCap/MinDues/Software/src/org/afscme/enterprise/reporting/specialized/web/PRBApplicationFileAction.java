package org.afscme.enterprise.reporting.specialized.web;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.specialized.PRBApplicationFileReport;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * @struts:action   path="/prbApplicationFileReport"
 *                  name="prbApplicationFileForm"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Reporting/Specialized/PRBApplicationFileInput.jsp"
 */
public class PRBApplicationFileAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        PRBApplicationFileForm rptForm = (PRBApplicationFileForm)form;
        PRBApplicationFileReport report = new PRBApplicationFileReport();
        
        // Validation
        ActionErrors errors = new ActionErrors();
        if (TextUtil.isEmptyOrSpaces(rptForm.getAppMailedDate())) {
            errors.add("appMailedDate", new ActionError("error.field.required.generic", "Application Mailed Date"));
        } else {
            try {
                report.setAppMailedDate(TextUtil.parseDate(rptForm.getAppMailedDate()));
            } catch (ParseException pe) {
                errors.add("appMailedDate", new ActionError("error.field.mustBeDate.generic", "Application Mailed Date"));
            }
        }
        
        // Present errors if any exists
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
        // Submit the report for generation
        report.setUserPk(usd.getPersonPk());
        s_baseReport.generateReport(usd.getUserId(), new Integer(3), new MediaType(MediaType.PRINT), report);
        
        // Show the configuration screen        
        String msg = "Your report has been submitted for processing.  " +
        "When completed, it will be emailed to " + ConfigUtil.getConfigurationData().getReportQueueEmail();
        
        request.setAttribute("title", "Report Submitted");
        request.setAttribute("content", msg);
        return mapping.findForward("Message");
    }
}
