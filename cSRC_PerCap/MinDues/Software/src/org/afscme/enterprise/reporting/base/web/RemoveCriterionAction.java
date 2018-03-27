package org.afscme.enterprise.reporting.base.web;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
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
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.reporting.base.access.CriterionMap;
import org.afscme.enterprise.util.JNDIUtil;

/**
 * Remove a criterion for a field 
 *
 * @struts:action   name="queryForm"
 *                  path="/removeCriterion"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="SelectionCriteria"  path="/Reporting/SelectionCriteria.jsp"
 */

public class RemoveCriterionAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer fieldPk = Integer.valueOf(request.getParameter("pk"));
        String cKey = request.getParameter("cKey");
        
        QueryForm qForm = (QueryForm)form;
        
        qForm.removeCriterion(fieldPk, cKey);
        
        return mapping.findForward("SelectionCriteria");  
    }
    
}
