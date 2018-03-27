
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

import org.afscme.enterprise.reporting.specialized.OrgMailingListReport;

/**
 * @struts:action   path="/orgMailingListReport"
 *                  name="mailingListForm"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Reporting/Specialized/OrgMailingListInput.jsp"
 */
public class OrgMailingListAction extends AFSCMEAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        MailingListForm mlf = (MailingListForm)form;

        //submit the report for generation

        OrgMailingListReport report = new OrgMailingListReport();
        report.setMailingListId(mlf.getMailingListPk());
        report.setPersonPk(usd.getPersonPk());
        report.setFilterDuplicateAddresses(mlf.isFilterDuplicateAddressesEnabled());
        
        s_baseReport.generateReport(usd.getUserId(), new Integer(8), new MediaType(MediaType.PRINT), report);

        //show the configuration screen

        String msg = "Your report has been submitted for processing.  " +
        "When completed, it will be emailed to " + ConfigUtil.getConfigurationData().getReportQueueEmail();

        request.setAttribute("title", "Report Submitted");
        request.setAttribute("content", msg);
		return mapping.findForward("Message");
	}
}
