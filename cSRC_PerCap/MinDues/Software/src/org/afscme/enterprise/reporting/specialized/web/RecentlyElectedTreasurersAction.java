/** Class Name  : RecentlyElectedTreasurersAction.java
    Date Written: 20030905
    Author      : Kyung A. Callahan
    Description :
    Maintenance : 20031022 Change report number.
*/
package org.afscme.enterprise.reporting.specialized.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.specialized.RecentlyElectedTreasurersReport;
import org.afscme.enterprise.util.ConfigUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:action   path="/recentlyElectedTreasurersReport"
 *                  name="recentlyElectedTreasurersForm"
 *                  scope="request"
 *                  validate="true"
 */
public class RecentlyElectedTreasurersAction extends AFSCMEAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        RecentlyElectedTreasurersForm nuForm = (RecentlyElectedTreasurersForm)form;

        //submit the report for generation

        RecentlyElectedTreasurersReport report = new RecentlyElectedTreasurersReport();
        report.setReportType(nuForm.getReportType());
        s_baseReport.generateReport(usd.getUserId(), new Integer(16), new MediaType(MediaType.PRINT), report); 

        //show the configuration screen

        String msg = "Your report has been submitted for processing.  " +
        "When completed, it will be emailed to " + ConfigUtil.getConfigurationData().getReportQueueEmail();

        request.setAttribute("title", "Report Submitted");
        request.setAttribute("content", msg);
		return mapping.findForward("Message");
	}
}
