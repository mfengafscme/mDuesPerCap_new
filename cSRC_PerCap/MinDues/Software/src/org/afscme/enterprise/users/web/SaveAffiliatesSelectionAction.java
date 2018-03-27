
package org.afscme.enterprise.users.web;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.users.web.SelectUserAffiliatesSearchForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.controller.UserSecurityData;





/**
 * Associates the user with the affiliate selected on the select user affiliates page.
 * Deselects affiliates that are unchecked.
 *  
 * @struts:action   path="/saveUserAffiliatesSelection"
 *					input="/Admin/SelectUserAffiliatesSearchResults.jsp"
 *					name="selectUserAffiliatesSearchForm"
 *					validate="true"
 *                  scope="session"
 *
 * @struts:action-forward name="EditUser" path="/editUser.action"
 * @struts:action-forward name="SearchForm" path="/selectUserAffiliatesSearch.action?new"
 * @struts:action-forward name="Cancelled" path="/editUser.action"
 *
 */
public class SaveAffiliatesSelectionAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        if (isCancelled(request))
            return mapping.findForward("Cancelled");

        SelectUserAffiliatesSearchForm sasForm = (SelectUserAffiliatesSearchForm)form;
        Integer personPk = getCurrentPersonPk(request);

            if (isCancelled(request))
                    return mapping.findForward("SearchForm");
            else if (sasForm.isSelectAllResultsButton()) {
                    s_maintainUsers.setAffiliates(personPk, sasForm.getAffiliateData(), true);			
            }
            else if (sasForm.isDeSelectAllResultsButton())
                    s_maintainUsers.setAffiliates(personPk, sasForm.getAffiliateData(), false);
            else {
                Set selection = (Set)CollectionUtil.copy(new HashSet(), sasForm.getSelection());
                Set selectedPks = new HashSet();
                Set nonSelectedPks = new HashSet();
                Iterator it = sasForm.getResults().iterator();
                while (it.hasNext()) {
                        AffiliateData affiliate = (AffiliateData)it.next();
                        if (selection.contains(String.valueOf(affiliate.getPk())))
                                selectedPks.add(affiliate.getPk());
                        else
                                nonSelectedPks.add(affiliate.getPk());
                }
                if (!selectedPks.isEmpty())
                        s_maintainUsers.addAffiliates(personPk, selectedPks);
                if (!nonSelectedPks.isEmpty())
                        s_maintainUsers.removeAffiliates(personPk, nonSelectedPks);
            }
		
		return mapping.findForward("EditUser");
	}
}
