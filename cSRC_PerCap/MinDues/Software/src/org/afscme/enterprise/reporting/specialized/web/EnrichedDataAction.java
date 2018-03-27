
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
import org.apache.struts.action.ActionErrors;

import org.afscme.enterprise.reporting.specialized.EnrichedDataReport;

/**
 * @struts:action   path="/enrichedDataReport"
 *                  name="enrichedDataForm"
 *                  scope="request"
 *					input="/Reporting/Specialized/ViewEnrichedDataCriteria.jsp"
 */
public class EnrichedDataAction extends AFSCMEAction {

	private static final Integer REPORT_PK = new Integer(11);

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
								 HttpServletResponse response, UserSecurityData usd) throws Exception {
		EnrichedDataForm edf = (EnrichedDataForm)form;

        // get pk from form in case finder was used...
		Integer affPk = edf.getAffPk();
		if (affPk == null || affPk.intValue() < 1) {
	    // otherwise find it...
		    affPk = s_maintainAffiliates.getAffiliatePk(edf.getAffId());
		}

		if (affPk == null) {
			log.debug("affPk was null");
		    return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.field.update.invalidAffiliateId");
		 } else if (affPk.intValue() < 1 ) {
		     log.debug("affPk was " + affPk);
		     return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.codes.affiliate." + affPk.intValue());
		}

        //submit the report for generation

        EnrichedDataReport report = new EnrichedDataReport();
        report.setAffPk(affPk);
        s_baseReport.generateReport(usd.getUserId(), REPORT_PK, new MediaType(MediaType.PRINT), report);

        //show the configuration screen

        String msg = "Your report has been submitted for processing.  " +
        "When completed, it will be emailed to " + ConfigUtil.getConfigurationData().getReportQueueEmail();

        request.setAttribute("title", "Report Submitted");
        request.setAttribute("content", msg);
		return mapping.findForward("Message");
	}
}
