/** Class Name  : OfficerStatisticalSunmmaryDetailAction.java
    Date Written: 20031024
    Author      : Kyung A. Callahan
    Description :
    Maintenance : 
*/
package org.afscme.enterprise.reporting.specialized.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.specialized.OfficerStatisticalSunmmaryDetailReport;
import org.afscme.enterprise.util.ConfigUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:action   path="/officerStatisticalSunmmaryDetailReport"
 *                  name="officerStatisticalSunmmaryDetailForm"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Reporting/Specialized/OfficerStatisticalSunmmaryDetail.jsp"
 */
public class OfficerStatisticalSunmmaryDetailAction extends AFSCMEAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

         OfficerStatisticalSunmmaryDetailForm nuForm = ( OfficerStatisticalSunmmaryDetailForm)form;

        //submit the report for generation

        OfficerStatisticalSunmmaryDetailReport report = new OfficerStatisticalSunmmaryDetailReport();
        report.setReportType(nuForm.getReportType());
        s_baseReport.generateReport(usd.getUserId(), new Integer(14), new MediaType(MediaType.PRINT), report); 

        //show the configuration screen

        String msg = "Your report has been submitted for processing.  " +
        "When completed, it will be emailed to " + ConfigUtil.getConfigurationData().getReportQueueEmail();

        request.setAttribute("title", "Report Submitted");
        request.setAttribute("content", msg);
		return mapping.findForward("Message");
	}
}
