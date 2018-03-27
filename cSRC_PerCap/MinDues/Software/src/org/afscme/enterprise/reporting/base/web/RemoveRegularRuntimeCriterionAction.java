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
 * @struts:action   name="regularReportForm"
 *                  path="/removeRegularRuntimeCriterion"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="SelectionCriteria"  path="/Reporting/RegularRuntimeCriteria.jsp"
 */

public class RemoveRegularRuntimeCriterionAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        // get the fieldPk and cKey
        String fieldPkStr = request.getParameter("pk");
        String cKey = request.getParameter("cKey");
        
        RegularReportForm rForm = (RegularReportForm)form;
        
        rForm.removeCriterion(fieldPkStr, cKey);
        
        return mapping.findForward("SelectionCriteria");  
    }
    
}
