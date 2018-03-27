
package org.afscme.enterprise.codes.web;

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
 * @struts:action   path="/deleteCodeType"
 *					name="codeTypeForm"
 *                  scope="request"
 *					validate="false"
 */
public class DeleteCodeTypeAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

		CodeTypeForm codeTypeForm = (CodeTypeForm)form;

        s_maintainCodes.deleteCodeType(codeTypeForm.getCodeTypeKey());
        return mapping.findForward("ListCodeTypes");
    }
}
