
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
 * This action is to edit Political Legislative Information.
 *
 * @struts:action   path="/savePoliticalData"
 *                  scope="request"
 *					name="politicalDataForm"
 *					validate="false"
 *
 * @struts:action-forward   name="View"  path="/viewPoliticalData.action"
 */
public class SavePoliticalDataAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		PoliticalDataForm politicalDataForm = (PoliticalDataForm)form;
		PoliticalData politicalData = new PoliticalData();
		politicalData.setPoliticalObjectorFg(politicalDataForm.getPoliticalObjectorFg());
		politicalData.setPoliticalDoNotCallFg(politicalDataForm.getPoliticalDoNotCallFg());
		s_maintainPersons.updatePoliticalData(getCurrentPersonPk(request), usd.getPersonPk(), usd.getDepartment(),
											  politicalData);

		request.setAttribute("origin", request.getParameter("origin"));
		return mapping.findForward("View");
    }
}
