package org.afscme.enterprise.reporting.specialized.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.AttemptedSecurityViolation;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.*;
import org.afscme.enterprise.reporting.specialized.*;
import org.afscme.enterprise.reporting.*;
import org.afscme.enterprise.reporting.base.*;
import org.afscme.enterprise.reporting.base.access.*;
import org.afscme.enterprise.reporting.base.generator.*;

/**
 * Forwards the user to the appropriate URL for a specialized report.
 * Input:
 *      rpk - primary key of the report
 * 
 * @struts:action   path="/runSpecializedReport"
 */
public class RunSpecializedReportAction extends AFSCMEAction {
	protected static Logger log = Logger.getLogger(RunSpecializedReportAction.class);
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
            //todo use log4j or remove
            log.debug("request.getAttribute(name)=====>" + request.getParameter("name"));
            log.debug("request.getAttribute(filterDuplicateAddresses)=====>" + request.getParameter("filterDuplicateAddresses"));
            
            Integer rpk = Integer.valueOf(request.getParameter("rpk"));
            request.setAttribute("ReportName", request.getParameter("name"));
            //check if the user has access to this report
            if (!usd.getReports().contains(rpk))
                return new ActionForward("Error/NoAccess.jsp");

            //go to the report's url
            Report report = s_reportAccess.getReport(rpk);
            String url =  report.getReportData().getCustomHandlerClassName();
            log.debug("url getting forwarded to =====>" + url);

            return new ActionForward(url);
	}
}
