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
 * Handles the submits from the 'Add/Edit CodeType' page.
 *
 * @struts:action   path="/saveCodeType"
 *                  name="codeTypeForm"
 *                  scope="request"
 *                  validate="false"
 *                  input="/Admin/EditCodeType.jsp"
 *
 * @struts:action-forward   name="ListCodeTypes"  path="/listCodeTypes.action" redirect="true"
 * @struts:action-forward   name="EditCodeType"  path="/editCodeType.action?add=false"
 * @struts:action-forward   name="AddCode"  path="/editCode.action?new=true"
 */
public class SaveCodeTypeAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        CodeTypeForm codeTypeForm = (CodeTypeForm)form;
        
        //validate manually
        ActionErrors errors = codeTypeForm.validate(mapping, request);
        if (errors != null && !errors.isEmpty()) {
            saveErrors(request, errors);
            
            //set the categories and codes so the re-displayed form will have them
            codeTypeForm.setCategories(s_maintainCodes.getCategories());
            if (!codeTypeForm.isAdd())
                codeTypeForm.setResults(s_maintainCodes.getCodes(codeTypeForm.getCodeTypeKey()).values());
            
            return new ActionForward(mapping.getInput());
        }
        
        //save the codeType info
        if (!isCancelled(request)) {
            if (codeTypeForm.isAdd()) {
                if (!s_maintainCodes.addCodeType(codeTypeForm.getData())) {
                    codeTypeForm.setCategories(s_maintainCodes.getCategories());//<- set the categories so the re-displayed form will know them
                    return makeErrorForward(request, mapping, "codeTypeKey", "error.field.codeTypeKey.notUnique");
                }
            } else {
                s_maintainCodes.updateCodeType(codeTypeForm.getData());
            }
        }
        
        if (codeTypeForm.isAddCodeButton()) {
            request.setAttribute("codeTypeKey", codeTypeForm.getCodeTypeKey());
            return mapping.findForward("AddCode");
        }
        else
            return mapping.findForward("ListCodeTypes");
    }
}
