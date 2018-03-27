
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

import org.afscme.enterprise.reporting.specialized.MembershipBatchUpdateReport;

/**
 * @struts:action   path="/membershipBatchUpdateReport"
 *					name="membershipBatchUpdateForm"
 *                  scope="request"
 *                  validate="false"
 */
public class MembershipBatchUpdateAction extends AFSCMEAction {

	private final static Integer REPORT_PK = new Integer(12);

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,
								 UserSecurityData usd) throws Exception {

        //submit the report for generation
		MembershipBatchUpdateForm mbuf = (MembershipBatchUpdateForm)form;

        MembershipBatchUpdateReport report = new MembershipBatchUpdateReport();
        report.setQueuePk(mbuf.getQueuePk());
        s_baseReport.generateReport(usd.getUserId(), REPORT_PK, new MediaType(MediaType.PRINT), report);

        //show the configuration screen

        String msg = "Your report has been submitted for processing.  " +
        "When completed, it will be emailed to " + ConfigUtil.getConfigurationData().getReportQueueEmail();

        request.setAttribute("title", "Report Submitted");
        request.setAttribute("content", msg);
		return mapping.findForward("Message");
	}
}
