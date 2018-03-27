
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
import org.afscme.enterprise.person.web.RelationDataForm;

/**
 * This action is to add a child.
 *
 * @struts:action   path="/addChild"
 *                  scope="request"
 * @struts:action-forward   name="View"  path="/Membership/AddChild.jsp"
 */
public class AddChildAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		setCurrentFlow(request, request.getParameter("origin"));
		RelationDataForm rdf = new RelationDataForm();
		request.setAttribute("relationDataForm", rdf);
		request.setAttribute("origin", request.getParameter("origin"));
		return mapping.findForward("View");
    }
}
