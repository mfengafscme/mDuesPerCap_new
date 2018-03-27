
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
 * @struts:action   path="/editCodeType"
 *					name="codeTypeForm"
 *                  scope="request"
 *					validate="false"
 *
 * @struts:action-forward   name="Edit"  path="/Admin/EditCodeType.jsp"
 * @struts:action-forward   name="Add"  path="/Admin/EditCodeType.jsp?add"
 */
public class EditCodeTypeAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

		CodeTypeForm codeTypeForm = (CodeTypeForm)form;
		
		Map categories = s_maintainCodes.getCategories();
		codeTypeForm.setCategories(categories);
			
        if (codeTypeForm.getCodeTypeKey() != null) {
			codeTypeForm.setData(s_maintainCodes.getCodeType(codeTypeForm.getCodeTypeKey()));
			codeTypeForm.setResults(s_maintainCodes.getCodes(codeTypeForm.getCodeTypeKey()).values());
	        return mapping.findForward("Edit");
        } else {
	        return mapping.findForward("Add");
		}

	}
}
