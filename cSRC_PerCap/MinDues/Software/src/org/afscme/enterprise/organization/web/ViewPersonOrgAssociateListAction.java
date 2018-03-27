package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;


/**
 * Display the list of Organization Associates for a person
 *
 * @struts:action   path="/viewPersonOrgAssociateList"
 *                  name="personOrgAssociateListForm"
 *
 * @struts:action-forward   name="View"  path="/Membership/PersonOrganizationAssociateList.jsp"
 */
public class ViewPersonOrgAssociateListAction extends AFSCMEAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, UserSecurityData usd) throws Exception {

        //retrieve the personPk
        Integer personPk = getCurrentPersonPk(request);
        
        PersonOrgAssociateListForm poalForm = (PersonOrgAssociateListForm)form;
        SortData sortData = poalForm.getSortData();

        //put the results in the form
        poalForm.setResults(s_maintainOrganizations.getPersonOrgAssociates(personPk, sortData));

        return mapping.findForward("View");
    }
}
