
package org.afscme.enterprise.reporting.specialized.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * This class forwards to the page to select the criteria to generate the Enriched Data Report.
 *
 * @struts:action   path="/viewEnrichedDataCriteria"
 * @struts:action-forward   name="View" path="/Reporting/Specialized/ViewEnrichedDataCriteria.jsp"
 */
public class ViewEnrichedDataCriteriaAction extends AFSCMEAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
								 HttpServletResponse response, UserSecurityData usd) throws Exception {
		return mapping.findForward("View");
	}
}
