package org.afscme.enterprise.reporting.base.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.generator.ReportGenerator;


/**
 * Handles the report save as option. HLM Fix defect #755
 *
 * @struts:action   name="regularReportForm"
 *                  path="/saveAsGeneration"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="Return"  path="/listRegularReports.action"
 */

public class SaveAsGenerationAction extends AFSCMEAction {
        
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        RegularReportForm rForm = (RegularReportForm)form;
        
        // get the reportPK
        Report report = s_reportAccess.getReport(rForm.getReportPk());
        
        response.setContentType("text/data");
        response.setHeader("Content-Disposition", "attachment; filename=" + report.getReportData().getName() + ".txt");
        ReportGenerator reportGenerator = new ReportGenerator(s_reportAccess.getReportFields(), report, rForm.getOutputFormatObject(), usd.getAccessibleAffiliates());
        reportGenerator.generate(response.getOutputStream());
        
        return mapping.findForward("Return");  
    }
}
