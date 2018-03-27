package org.afscme.enterprise.reporting.base.web;

import java.util.Set;
import java.util.List;
import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.util.JNDIUtil;

/**
 * Display the output format page for regular report generation.
 *
 * @struts:action   name="regularReportForm"
 *                  path="/showRegularOutputFormat"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="Custom"       path="/Reporting/RegularOutputFormat.jsp"
 * @struts:action-forward   name="Specialized"  path="/Reporting/SpecializedOutputFormat.jsp"
 */

public class ShowRegularOutputFormatAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        // get the report PK from the request parameter
        Integer reportPk = new Integer(request.getParameter("rpk"));

        // Check user access to this report
        Set allowedReportPks = usd.getReports();
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        List myReports = reportAccess.getReportsForUser(false, usd.getPersonPk());
        Report report = reportAccess.getReport(reportPk); 
        if (report.getReportData().isCustom()) {
            Iterator it = myReports.iterator();
            ReportData reportData;
            boolean allowed = false;
            while (it.hasNext()) {
                reportData = (ReportData)it.next();
                if (reportData.getPk().equals(reportPk)) {
                    allowed = true;
                    break;
                }
            }
            if (!allowed)
                return mapping.findForward("Denied");
        }
        else {
            if (!allowedReportPks.contains(reportPk))
                return mapping.findForward("Denied");
        }        
        
        // clear the form
        RegularReportForm rForm = (RegularReportForm)form;
        rForm.clearForm();
        
        // set the reportPk
        rForm.setReportPk(reportPk);
        
        // check if it is a speicalized report 
        if (report.getReportData().isCustom()) {
            rForm.setReportType(RegularReportForm.CUSTOM);
            rForm.setDataUtility(usd.isActingAsAffiliate());
            return mapping.findForward("Custom");
        }
        else {
            rForm.setReportType(RegularReportForm.SPECIALIZED);
            return mapping.findForward("Specialized"); 
        }
    }
    
}
