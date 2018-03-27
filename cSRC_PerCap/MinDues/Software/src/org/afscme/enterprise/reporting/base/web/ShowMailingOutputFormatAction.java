package org.afscme.enterprise.reporting.base.web;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Display the output format page for mailing report generation.
 *
 * @struts:action   name="mailingReportForm"
 *                  path="/showMailingOutputFormat"
 *                  scope="session"
 *                  validate="false"
 *                  input="/Reporting/ListMailingListReports.jsp"
 *
 * @struts:action-forward   name="Custom"       path="/Reporting/MailingOutputFormat.jsp"
 * @struts:action-forward   name="Specialized"  path="/Reporting/SpecializedOutputFormat.jsp"
 * @struts:action-forward   name="Denied"       path="/listMailingReports.action"
 */

public class ShowMailingOutputFormatAction extends AFSCMEAction
{

    public ActionForward perform(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response,
        UserSecurityData usd)
        throws Exception
    {
        // get the report PK from the request parameter.
        Integer reportPk = new Integer(request.getParameter("rpk"));

        // Check user access to this report
        Set allowedReportPks = usd.getReports();
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        List myReports =
            reportAccess.getReportsForUser(true, usd.getPersonPk());
        Report report = reportAccess.getReport(reportPk);
        if (report.getReportData().isCustom())
        {
            Iterator it = myReports.iterator();
            ReportData reportData;
            boolean allowed = false;
            while (it.hasNext())
            {
                reportData = (ReportData) it.next();
                if (reportData.getPk().equals(reportPk))
                {
                    allowed = true;
                    break;
                }
            }
            if (!allowed)
                return mapping.findForward("Denied");
        }
        else
        {
            if (!allowedReportPks.contains(reportPk))
                return mapping.findForward("Denied");
        }

        // clear the form
        MailingReportForm mForm = (MailingReportForm) form;
        mForm.clearForm();

        // set the reportPk
        mForm.setReportPk(reportPk);
        mForm.setFilterDuplicateAddresses(
            request.getParameter("filterDuplicateAddresses"));

        // check if it is a speicalized report
        if (report.getReportData().isCustom())
        {
            mForm.setReportType(MailingReportForm.CUSTOM);
            return mapping.findForward("Custom");
        }
        else
        {
            mForm.setReportType(MailingReportForm.SPECIALIZED);
            return mapping.findForward("Specialized");
        }
    }

}
