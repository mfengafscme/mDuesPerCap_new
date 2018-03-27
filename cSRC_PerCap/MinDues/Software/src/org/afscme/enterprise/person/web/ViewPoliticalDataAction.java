
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
import org.afscme.enterprise.person.PoliticalData;

/**
 * This action is to view Political Legislative Information.
 *
 * @struts:action   path="/viewPoliticalData"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/Membership/ViewPoliticalLegislativeInformation.jsp"
 */
public class ViewPoliticalDataAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		PoliticalData politicalData = s_maintainPersons.getPoliticalData(getCurrentPersonPk(request));

		// set the current flow
		// the request may have originated from an action class or a jsp.
		// handle the case in question appropriately
		String origin = request.getParameter("origin");
		if(origin == null) {
			origin = (String)request.getAttribute("origin");
		}
		setCurrentFlow(request, origin);

        request.setAttribute("origin", origin);
        request.setAttribute("politicalData", politicalData);
        return mapping.findForward("View");
    }
}
