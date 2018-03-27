package org.afscme.enterprise.reporting.base.web;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.reporting.base.web.QueryForm;
import org.apache.struts.action.ActionErrors;

/**
 * Handles all actions on the "SelectionCriteria.jsp" page.
 *
 * @struts:action   name="queryForm"
 *                  path="/selectionCriteria"
 *                  scope="session"
 *                  validate="true"
 *                  input="/Reporting/SelectionCriteria.jsp"
 */

public class SelectionCriteriaAction extends QueryToolAction {
    
    public ActionErrors perform(ActionMapping mapping, QueryForm qForm, HttpServletRequest request, UserSecurityData usd) throws Exception { 
        return null;
    }
    
}
