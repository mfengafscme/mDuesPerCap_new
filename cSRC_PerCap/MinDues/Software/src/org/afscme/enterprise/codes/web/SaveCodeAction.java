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
 * @struts:action   path="/saveCode"
 *					name="codeForm"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Admin/EditCode.jsp"
 *
 * @struts:action-forward   name="AddCode"  path="/editCode.action?new" 
 * @struts:action-forward   name="Done"  path="/editCodeType.action" 
 */
public class SaveCodeAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
    {
        CodeForm codeForm = (CodeForm)form;

		//save the codeType info
        if (!isCancelled(request)) {
			boolean result;
			if (codeForm.isAdd())
				result = s_maintainCodes.addCode(codeForm.getCodeTypeKey(), codeForm.getData());
			else
				result = s_maintainCodes.updateCode(codeForm.getData());

		   if (!result) {
				ActionErrors errors = new ActionErrors();
				errors.add("code", new ActionError("error.field.code.notUnique"));
				saveErrors(request, errors);
				return new ActionForward(mapping.getInput());
			}
		}
		
        request.setAttribute("codeTypeKey", codeForm.getCodeTypeKey());
		return mapping.findForward("Done");
	}
}
