package org.afscme.enterprise.codes.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;



/**
 * Handles the submits from the 'Add/Edit CodeType' page. 
 *
 * @struts:action   path="/deleteCode"
 *					name="codeForm"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Admin/EditCode.jsp"
 *
 * @struts:action-forward   name="Done"  path="/editCodeType.action" 
 */
public class DeleteCodeAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		s_maintainCodes.deleteCode(((CodeForm)form).getPk());
		
		return mapping.findForward("Done");
	}
}
