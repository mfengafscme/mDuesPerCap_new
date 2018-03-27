package org.afscme.enterprise.affiliate.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.FinancialData;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.PreparedUpdateStatementBuilder;

/** 
 * @struts:action   path="/saveFinancialInformation"
 *                  name="financialForm"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Membership/FinancialInformationEdit.jsp"
 */
public class SaveFinancialInformationAction extends AFSCMEAction {
    
    /** Creates a new instance of SaveFinancialInformationAction */
    public SaveFinancialInformationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        FinancialForm ff = (FinancialForm)form;
        
        ActionErrors errors = ff.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        
        FinancialData data = ff.getFinancialData();
        log.debug("Form: " + ff);
        log.debug("Data: " + data);
        if (!s_maintainAffiliates.updateFinancialData(data, usd.getPersonPk())) {
            throw new JspException("Financial Info could not be updated.");
        }
        return mapping.findForward("ViewFinancial");
    }
    
}
