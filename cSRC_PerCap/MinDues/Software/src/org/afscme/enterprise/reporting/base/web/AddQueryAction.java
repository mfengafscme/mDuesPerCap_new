package org.afscme.enterprise.reporting.base.web;

import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.codes.CodeTypeData;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.util.JNDIUtil;

/**
 * Handles the start of creating a new custom query. 
 *
 * @struts:action   name="queryForm"
 *                  path="/addQuery"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="OutputFields"  path="/Reporting/OutputFields.jsp"
 */

public class AddQueryAction extends AFSCMEAction {
    
    /** start the first page of query wizard in "new" mode
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        QueryForm qForm = (QueryForm)form;
        qForm.clearForm();
        
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        MaintainCodes maintainCodes = JNDIUtil.getMaintainCodesHome().create();        
        
        /*---- Prepare the base data for the UI ---*/
        // get all fields if not gotton yet
        if (qForm.getQueryFields() == null) {
            Map allFields = reportAccess.getReportFields();
            qForm.setQueryFields(allFields);
        }
        
        if (qForm.getCodes() == null) {
            Map allCodes = new TreeMap();
            Map allTypes = maintainCodes.getCodeTypes();
            
            CodeTypeData codeType;
            CodeData code;
            String codeTypeKey;
            Map codes;
            Iterator ctIt = allTypes.values().iterator();
            while (ctIt.hasNext()) {
                codeType = (CodeTypeData)ctIt.next();
                codeTypeKey = codeType.getKey();
                // get all the code values for this type key
                codes = maintainCodes.getCodes(codeTypeKey);
                allCodes.put(codeType, codes);
            }
            
            qForm.setCodes(allCodes);
        }
                
        
        // set up those fields the current user can access
        qForm.setUpUserFields(usd.getFields());
        
        // set the owner information (user_id and pk)
        qForm.setPersonPk(usd.getPersonPk());
        qForm.setUserId(usd.getUserId());
        
        return mapping.findForward("OutputFields");  
    }
    
}
