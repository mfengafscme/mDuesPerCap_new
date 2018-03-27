package org.afscme.enterprise.reporting.base.web;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.reporting.base.web.QueryForm;
import org.apache.struts.action.ActionErrors;

/**
 * Handles all actions on the "OutputFieldsOrder.jsp" page
 *
 * @struts:action   name="queryForm"
 *                  path="/outputFieldsOrder"
 *                  scope="session"
 *                  validate="false"
 */

public class OutputFieldsOrderAction extends QueryToolAction {
    
    public ActionErrors perform(ActionMapping mapping, QueryForm qForm, HttpServletRequest request, UserSecurityData usd) throws Exception { 
        return null;
    }
    
}
