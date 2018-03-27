package org.afscme.enterprise.reporting.base.web;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.reporting.base.ejb.BaseReport;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.reporting.base.generator.OutputFormat;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.base.generator.ReportGenerator;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;


/**
 * Handles all actions on the "SelectionCriteria.jsp" page.
 *
 * @struts:action   name="regularReportForm"
 *                  input="/Reporting/RegularRuntimeCriteria.jsp"
 *                  path="/regularGenerate"
 *                  scope="session"
 *                  validate="true"
 *
 * @struts:action-forward   name="Generate"         path="/listRegularReports.action"
 * @struts:action-forward   name="GenerateScreen"     path="/Reporting/ShowReportOnScreen.jsp"
 * @struts:action-forward   name="GenerateSave"   path="/regularReportSaveAs" redirect="true"
 * @struts:action-forward   name="Cancel"       path="/listRegularReports.action"
 */

public class RegularGenerateAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception { 
        // check which button pushed
        RegularReportForm rForm = (RegularReportForm)form;
        String button = rForm.getButton();
        if (button.equals("Cancel")) {
            rForm.clearForm();
            return mapping.findForward("Cancel");
        }
        
        // get the reportPK
        Integer reportPk = rForm.getReportPk();
        
        // retrieve the form information on output formats
        String media = rForm.getMedia();
        String format = rForm.getOutputFormat();
        
        // retrieve the report and other information
        if (rForm.getQueryFields() == null) {
            Map allFields = s_reportAccess.getReportFields();
            rForm.setQueryFields(allFields);
        }
        Report report = s_reportAccess.getReport(reportPk);         
        
        // merge the runtime criteria
        Map mergedCriteria = rForm.getMergedCriteria(report);
        
        if (media.equals(MediaType.SCREEN)) {     // go to on-screen generation
            doScreenGeneration(rForm, request, report, usd);
            return mapping.findForward("GenerateScreen");
        } else if (media.equals(MediaType.SAVE)) {
            // HLM Fix defect #755
            return mapping.findForward("GenerateSave");        
        } else {
            BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
            baseReport.generateReport(usd.getUserId(), usd.getAccessibleAffiliates(), reportPk, rForm.getMediaTypeObject(), rForm.getOutputFormatObject(), mergedCriteria);
	}
                           
        return mapping.findForward("Generate");          
 
    }
    
    protected void doScreenGeneration(RegularReportForm form, HttpServletRequest request, Report report, UserSecurityData usd) throws Exception {

        List reportResult = new LinkedList();
        ReportGenerator reportGenerator = new ReportGenerator(s_reportAccess.getReportFields(), report, usd.getAccessibleAffiliates());
        reportGenerator.generateReport(reportResult);
        List fieldNames = reportGenerator.getOutputFieldNames();
        
        request.setAttribute("reportName", report.getReportData().getName());
        request.setAttribute("fieldNames", fieldNames);
        request.setAttribute("reportResult", reportResult);
    } 
}
