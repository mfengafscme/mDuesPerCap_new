package org.afscme.enterprise.affiliate.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.RequestUtils;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.CharterData;
    
/** 
 * @struts:action   path="/saveCharterInformation"
 *                  name="charterForm"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Membership/CharterInformationEdit.jsp"
 */
public class SaveCharterInformationAction extends AFSCMEAction {
    
    /** Creates a new instance of SaveCharterInformationAction */
    public SaveCharterInformationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        CharterForm cf = (CharterForm)form;
        
        ActionErrors errors = cf.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            cf.setCouncilAffiliations(s_maintainAffiliates.getAffiliatedCouncils(cf.getAffPk()));
            return mapping.getInputForward();
        }
        
        CharterData data = cf.getCharterData();
        log.debug("Form: " + cf);
        log.debug("Data: " + data);
        if (!s_maintainAffiliates.updateCharterData(data, usd.getPersonPk())) {
            throw new JspException("Charter could not be updated.");
        }
        return mapping.findForward("ViewCharter");
    }
    
}
