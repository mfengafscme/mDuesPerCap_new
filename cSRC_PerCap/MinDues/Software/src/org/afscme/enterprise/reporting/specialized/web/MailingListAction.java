
package org.afscme.enterprise.reporting.specialized.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.util.ConfigUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.log.SystemLog;
import org.afscme.enterprise.reporting.specialized.MailingListReport;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
/**
 * @struts:action   path="/mailingListReport"
 *                  name="mailingListForm"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Reporting/Specialized/MailingListInput.jsp"
 */
public class MailingListAction extends AFSCMEAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        MailingListForm mlf = (MailingListForm)form;

        //submit the report for generation

        MailingListReport report = new MailingListReport();
        report.setMailingListId(mlf.getMailingListPk());
        report.setPersonPk(usd.getPersonPk());
        report.setFilterDuplicateAddresses(mlf.isFilterDuplicateAddressesEnabled());
        
        HttpSession session = request.getSession();
        log.debug("request.getAttribute('ReportName')==>" + session.getAttribute("ReportName"));
        
        s_baseReport.generateReport(usd.getUserId(), report.REPORT_PK, new MediaType(MediaType.PRINT), report);
        SystemLog.logMailingListGenerated(session.getAttribute("ReportName").toString(), "SPECIALIZED", usd.getUserId(), new java.sql.Timestamp(new java.util.Date().getTime()));
        
        //show the configuration screen

        String msg = "Your report has been submitted for processing.  " +
        "When completed, it will be emailed to " + ConfigUtil.getConfigurationData().getReportQueueEmail();

        request.setAttribute("title", "Report Submitted");
        request.setAttribute("content", msg);
		return mapping.findForward("Message");
	}
}
