package org.afscme.enterprise.controller.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.ActionPrivileges;

/**
 * Shows the main menu.
 *
 * If the user is using the data utility, they are shown that main menu.
 * Otherwise, if the user is an AFSCME International staff, they are shown the AFSCME Main Menu.
 * Otherwise, the user is show the view personal informaiton page.  (If they don't have access to that page, they will see an error page)
 * Modified, so that if the user is an affiliate staff only, the noReturn paramter is also passed, so that
 * return to AFSCME Main Menu action is not displayed on the Data Utility Main Menu. Unable to turn off
 * showMain action in the data utility user privilege, as it is used to display the main menu itself and this
 * privilege is automatically assigned to all data utility users.
 *
 * @struts:action   path="/showMain"
 * @struts:action-forward   name="AFSCMEMain"  path="/Common/MinimumDuesMainMenu.jsp?noMain=true"
 * @struts:action-forward   name="DataUtilityMain"  path="/Common/DataUtilityMainMenu.jsp?noMain=true"
 * @struts:action-forward   name="DataUtilityMainNoReturn"  path="/Common/DataUtilityMainMenu.jsp?noMain=noReturn"
*/
public class ShowMainAction extends AFSCMEAction
{
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
	{
		if (usd.getActingAsAffiliate() != null & usd.getDepartment() == null){
                        return mapping.findForward("DataUtilityMainNoReturn");
                }else if (usd.getActingAsAffiliate() != null & usd.getDepartment() != null){
                        return mapping.findForward("DataUtilityMain");
                }else if (usd.getDepartment() != null){
					return mapping.findForward("AFSCMEMain");
                } //else if (usd.getPrivileges().contains(ActionPrivileges.PRIVILEGE_VIEW_PERSONAL_INFORMATION)){
                //    return mapping.findForward("ViewPersonalInformation");
                //}
                else{
                    return mapping.findForward("ErrorNoPrivileges");
                }
	}
}
