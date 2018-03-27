package org.afscme.enterprise.reporting.base.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.ejb.BaseReport;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Handles all actions on the "SelectionCriteria.jsp" page.
 *
 * @struts:action   name="mailingReportForm"
 *                  path="/mailingGenerate"
 *                  scope="session"
 *                  validate="true"
 *
 * @struts:action-forward   name="Generate"     path="/listMailingReports.action"
 * @struts:action-forward   name="Cancel"       path="/listMailingReports.action"
 */

public class MailingGenerateAction extends AFSCMEAction
{

    public ActionForward perform(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response,
        UserSecurityData usd)
        throws Exception
    {
        // check which button pushed
        MailingReportForm mForm = (MailingReportForm) form;
        String button = mForm.getButton();
        if (button.equals("Cancel"))
        {
            mForm.clearForm();
            return mapping.findForward("Cancel");
        }

        // get the reportPK
        Integer reportPk = mForm.getReportPk();

        // retrieve the form information on output formats
        String media = mForm.getMedia();
        String format = mForm.getOutputFormat();

        // retrieve the report and other information
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        if (mForm.getQueryFields() == null)
        {
            Map allFields = reportAccess.getReportFields();
            mForm.setQueryFields(allFields);
        }
        Report report = reportAccess.getReport(reportPk);

        // merge the runtime criteria
        Map mergedCriteria = mForm.getMergedCriteria(report);

        // generate report
        BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
        baseReport.generateReport(
            usd.getUserId(),
            usd.getAccessibleAffiliates(),
            reportPk,
            mForm.getMediaTypeObject(),
            mForm.getOutputFormatObject(),
            mergedCriteria,
            mForm.isFilterDuplicateAddressesEnabled());

        return mapping.findForward("Generate");

    }

}
