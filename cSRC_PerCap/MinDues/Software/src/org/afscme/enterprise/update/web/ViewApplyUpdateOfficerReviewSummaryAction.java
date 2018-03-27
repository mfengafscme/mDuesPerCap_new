/*
 * ViewApplyUpdateMemberReviewSummaryAction.java
 *
 * Created on August 4, 2003, 10:19 AM
 */
/***********************************************************************************/
/* Called by:               ApplyUpdate.jsp
 *
 * Forwards request to:     ApplyUpdateOfficerReviewSummary.jsp
 *
 * Purpose:                 This action uses the methods of UpdateBean EJB to get data (the results of   
 *                          applying the update to the database) from the database and display it 
 *                          on the screen using ApplyUpdateOfficerReviewSummary.jsp. Also displays the 
 *                          summary of exceptions encountered during the application of update.
 *
 * Required:                None
 *
 *
 *
 *************************************************************************************/

package org.afscme.enterprise.update.web;

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

/***************************************************************************/

import org.afscme.enterprise.update.officer.OfficerPreUpdateSummary;
import org.afscme.enterprise.update.officer.OfficerReviewSummary;
import org.afscme.enterprise.update.officer.OfficerReviewData;
import org.afscme.enterprise.update.ExceptionData;
import org.afscme.enterprise.update.FieldChange;
import org.afscme.enterprise.update.Codes; 
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;
import org.afscme.enterprise.update.PersonReviewData;
/******************************************************************************/
/**
 * @struts:action   path="/viewApplyUpdateOfficerReviewSummary"
 *                  
 *                  scope="request"
 *                  
 *
 * @struts:action-forward   name="success"  path="/Update/ApplyUpdateOfficerReviewSummary.jsp"
 */

public class ViewApplyUpdateOfficerReviewSummaryAction extends AFSCMEAction {
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        Logger log = Logger.getLogger(ViewApplyUpdateOfficerReviewSummaryAction.class);
        log.debug("----------------------------------------------------");
        log.debug("perform called.");
        log.debug("------------------------------------------------------");
        log.debug("QueuePk======>" + request.getParameter("queuePk"));
        log.debug("------------------------------------------------------");
         
        String                  sPK             =   (String) (request.getParameter("queuePk"));
        int                     iPK             =   Integer.parseInt(sPK);
        Integer                 pk              =   new Integer(iPK);
        
        String                  sAffPK          =   (String) (request.getParameter("affPk"));
        int                     iAffPK          =   Integer.parseInt(sAffPK);
        Integer                 affPk           =   new Integer(iAffPK);
        
        String                  updateType      =   (String) (request.getParameter("updateType"));
        
        OfficerReviewSummary    oSummary        =   s_update.getOfficerReviewSummary(pk);
        OfficerReviewData[]     oRSmry          =   oSummary.getOfficerReviewData();
        OfficerReviewData       tCount          =   oSummary.getTotals();
        
             
        ExceptionData[]         eResult         =   oSummary.getExceptionResult();
        //AffiliateIdentifier[]   affId           =   oSummary.getAffiliateIdentifier();
         /****************************************************************************************/
        /*now that we have got the data structures and objects add them to the request so that  */
        /*we can iterate on the UI                                                              */
        /****************************************************************************************/
        request.setAttribute(new String("updateSmry")   , oRSmry     );
        request.setAttribute(new String("exceptions")   , eResult    );
        //request.setAttribute(new String("affId")        , affId      );
        request.setAttribute(new String("tCount")       , tCount     );
        request.setAttribute(new String("updateType")   , updateType );
        
        // needed for header and footer tags
        setCurrentAffiliate(request                     , affPk         );
        //*********************************************************************************
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
