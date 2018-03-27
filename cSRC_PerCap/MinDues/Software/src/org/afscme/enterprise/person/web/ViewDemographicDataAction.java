
package org.afscme.enterprise.person.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

// Java imports
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.DemographicData;

/**
 * This action is to view General Demographic Information.
 *
 * @struts:action   path="/viewDemographicData"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/Membership/GeneralDemographicInformation.jsp"
 */
public class ViewDemographicDataAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		DemographicData demographicData = s_maintainPersons.getGeneralDemographics(getCurrentPersonPk(request));

		// set the current flow
		// the request may have originated from an action class or a jsp.
		// handle the case in question appropriately
		String origin = request.getParameter("origin");
		if(origin == null) {
			origin = (String)request.getAttribute("origin");
		}
		setCurrentFlow(request, origin);

        request.setAttribute("origin", origin);
        request.setAttribute("demographicData", demographicData);
        return mapping.findForward("View");
    }
}
