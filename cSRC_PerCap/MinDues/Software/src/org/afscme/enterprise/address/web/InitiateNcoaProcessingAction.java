package org.afscme.enterprise.address.web;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.log4j.Logger;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.address.ejb.SystemAddressBean;

/**
 * Initiate Ncoa Processing
 *
 * @struts:action   path="/initiateNcoaProcessing"                 
 *
 */


public class InitiateNcoaProcessingAction extends AFSCMEAction 
{
    
    static Logger logger = Logger.getLogger(InitiateNcoaProcessingAction.class);	    
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) 
    throws Exception 
    {
        Integer personPk = null;
        personPk = usd.getPersonPk();
        
        if (personPk != null){
            s_systemAddress.insertNcoaActivity(personPk.intValue());
            request.getSession().setAttribute("ncoaProcess", "Success");
        }
        else
        {
            if (logger.isDebugEnabled()) {
                logger.debug("InitiateNcoaProcessingAction: personPk is null");
            }
            request.getSession().setAttribute("ncoaProcess", "Failure");            
        }

        return mapping.findForward("MainMenu");
    }        
}
