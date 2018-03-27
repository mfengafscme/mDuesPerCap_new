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
 * Handles all actions on the "OutputFields.jsp" page.
 *
 * @struts:action   name="queryForm"
 *                  path="/outputFields"
 *                  scope="session"
 *                  validate="false"
 *                  input="/Reporting/OutputFields.jsp"
 */

public class OutputFieldsAction extends QueryToolAction {
    
    public ActionErrors perform(ActionMapping mapping, QueryForm qForm, HttpServletRequest request, UserSecurityData usd) throws Exception { 
        
        // if the selection has changed from the previous selection, we need to
        // clear data in page two (Output Fields Order).
        if (qForm.isCountQuery())
            qForm.clearOutputFieldsForCountQuery();
        else {
            if (qForm.hasOutputFieldSelectionChanged()) {
                qForm.resetOutputFieldsOrder();
                resetPreviousSelection(qForm);
            }
        }
        
        return null;
    }
    
    private void resetPreviousSelection(QueryForm form) {
        Integer[] currentSelection = form.getSelectedOutputFields();
        if ((currentSelection == null) || (currentSelection.length == 0)) {
            form.setPreviousSelectedOutputFields(null);
            return;
        }
        
        Integer[] previousSelection = new Integer[currentSelection.length];
        for (int i = 0; i < currentSelection.length; i++) 
            previousSelection[i] = new Integer(currentSelection[i].intValue());
        
        form.setPreviousSelectedOutputFields(previousSelection);        
    } 
    
    
}
