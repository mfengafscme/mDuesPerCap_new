
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
import org.afscme.enterprise.person.RelationData;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.codes.Codes;

/**
 * This action is to save a child.
 *
 * @struts:action   path="/saveChild"
 *                  scope="request"
 *					name="relationDataForm"
 *					input="/Membership/AddChild.jsp"
 * @struts:action-forward   name="View"  path="/viewDemographicData.action"
 */
public class SaveChildAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		RelationDataForm rdf = (RelationDataForm)form;

		// populate realtion data
		RelationData rd = new RelationData();
		rd.setRelativeFirstNm(rdf.getRelativeFirstNm());
		rd.setRelativeMiddleNm(rdf.getRelativeMiddleNm());
		rd.setRelativeLastNm(rdf.getRelativeLastNm());

		if(rdf.getRelativeSuffixNm().intValue() != 0) {
			// user must have selected the option with a blank value
			rd.setRelativeSuffixNm(rdf.getRelativeSuffixNm());
		}

		if(rdf.getRelativeBirthDt() != null) {
			// convert String to Timestamp
			rd.setRelativeBirthDt(DateUtil.getTimestamp(rdf.getRelativeBirthDt()));
		}
		rd.setPersonRelativeType(Codes.Relation.CHILD);

		// add child
		s_maintainPersons.addPersonRelation(getCurrentPersonPk(request), usd.getPersonPk(), rd);
		request.setAttribute("origin", request.getParameter("origin"));
        return mapping.findForward("View");
    }
}
