
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

/**
 * This action is to save a phone number.
 *
 * @struts:action   path="/deleteChild"
 *                  scope="request"
 *					name="relationDataForm"
 *					validate="false"
 * @struts:action-forward   name="View"  path="/viewDemographicData.action"
 */
public class DeleteChildAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		RelationDataForm rdf = (RelationDataForm)form;
		s_maintainPersons.deletePersonRelation(rdf.getRelativePk());
		request.setAttribute("origin", request.getParameter("origin"));
        return mapping.findForward("View");
    }
}
