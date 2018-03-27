/*
 * ViewApplyUpdateOfficerEditDetailPerAffiliateAction.java
 *
 * Created on August 29, 2003, 12:24 PM
 */
/***********************************************************************************/
/* Called by:               ApplyUpdateOfficerEditSummary.jsp
 *
 * Forwards request to:     ApplyUpdateOfficerEditDetail.jsp
 *
 * Purpose:                 This action uses the methods of UpdateBean EJB to get the edit  
 *                          details per Affiliate pre-processing statistical information on 
 *                          the Officer update from the database and display it on the screen using 
 *                          ApplyUpdateOfficerEditDetail.jsp. 
 *
 * Required:                None
 *
 *
 *
 *************************************************************************************/
package org.afscme.enterprise.update.web;
/********************************************************************/
import java.util.Map;
import java.io.*;
import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.update.officer.OfficerChanges;
import org.afscme.enterprise.update.officer.OfficerPreUpdateSummary;


import org.afscme.enterprise.update.FieldChange;
import org.afscme.enterprise.update.Codes; 
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;
/******************************************************************************/
/**
 * @struts:action           path="/viewApplyUpdateOfficerEditDetailPerAffiliate"
 *                  
 *                          scope="request"
 *                  
 *
 * @struts:action-forward   name="success"  path="/Update/ApplyUpdateOfficerEditDetail.jsp"
 */

public class ViewApplyUpdateOfficerEditDetailPerAffiliateAction extends AFSCMEAction {
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        Logger log = Logger.getLogger(ViewApplyUpdateOfficerEditDetailPerAffiliateAction.class);
        log.debug("----------------------------------------------------");
        log.debug("perform called.");
        log.debug("------------------------------------------------------");
        log.debug("pk======>" + request.getParameter("queuePk"));
        log.debug("------------------------------------------------------");
        
        log.debug("------------------------------------------------------");
        log.debug("affpk======>" + request.getParameter("affPk"));
        log.debug("------------------------------------------------------");
        
        String sPK                                    = (String) (request.getParameter("queuePk"));
        int iPK                                       = Integer.parseInt(sPK);
        Integer pk                                    = new Integer(iPK);
        
        String sAffPK                                 = (String) (request.getParameter("affPk"));
        int iAffPK                                    = Integer.parseInt(sAffPK);
        Integer affPk                                 = new Integer(iAffPK);
        log.debug("------------------------------------------------------");
        log.debug("calling s_update.getOfficerPreOfficeDetails(pk, affPk)");
        log.debug("------------------------------------------------------");

        Map pCResults                                 = s_update.getOfficerPreOfficeDetails(pk, affPk);
        
        log.debug("------------------------------------------------------");
        log.debug("done calling s_update.getOfficerPreOfficeDetails(pk, affPk)");
        log.debug("------------------------------------------------------");
        request.setAttribute(       new String("PCResults")     ,   pCResults            );
        request.setAttribute(       new String("affPk")         ,   affPk                );
        request.setAttribute(       new String("queuePk")       ,   sPK                  );
        AffiliateData   data                           = s_maintainAffiliates.getAffiliateData(affPk);
        // needed for header and footer tags
        setCurrentAffiliate(request, data);
        /********************************************************************************/
        
        ActionErrors errors = new ActionErrors();
	if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput()); //go back to the edit page
        } else {
            log.debug("------------------------------------------------------");
            log.debug("Forwarding to view page");
            log.debug("------------------------------------------------------");
            return mapping.findForward("success");  //go back to the view page
	}

    }
    
    
}
