package org.afscme.enterprise.codes.web;

import java.util.Map;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.CodeData;

/**
 * @struts:action   path="/editCode"
 *					name="codeForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="Edit"  path="/Admin/EditCode.jsp"
 * @struts:action-forward   name="Add"  path="/Admin/EditCode.jsp?add"
 */
public class EditCodeAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        CodeForm codeForm;
        
        if (request.getParameter("new") != null) {

            //Add
            
    		codeForm = (CodeForm)form;
            if (codeForm == null) {
                codeForm = new CodeForm();
                request.setAttribute("codeForm", new CodeForm());
            }
            codeForm.setCodeTypeKey((String)request.getAttribute("codeTypeKey"));
	        return mapping.findForward("Add");
        } else {
            
            //Edit
            
    		codeForm = (CodeForm)form;
			Map codes = s_maintainCodes.getCodes(codeForm.getCodeTypeKey());
			CodeData codeData = (CodeData)codes.get(codeForm.getPk());
			codeForm.setData(codeData);
            codeForm.setCodeTypeKey(request.getParameter("codeTypeKey"));
	        return mapping.findForward("Edit");
		}
	}
}
