
package org.afscme.enterprise.common.web;

import java.util.Map;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * Action that goes to the view personal information page.  This is a placeholder to
 * demonstrate the associated privileges, it will be implemented later, probably in
 * another package.
 *
 * @struts:action   path="/viewPersonalInformation"
 *
 * @struts:action-forward   name="View"  path="/Common/ViewPersonalInformation.jsp"
 */
public class ViewPersonalInformationAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        return mapping.findForward("View");
    }
}
