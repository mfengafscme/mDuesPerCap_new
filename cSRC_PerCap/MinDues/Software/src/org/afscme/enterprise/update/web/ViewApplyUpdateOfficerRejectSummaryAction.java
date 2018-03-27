/*
 * ViewApplyUpdateMemberRejectSummaryAction.java
 *
 * Created on August 20, 2003, 12:09 PM
 */
/***********************************************************************************/
/* Called by:               ApplyUpdateOfficerEditSummary.jsp
 *
 * Forwards request to:     ApplyUpdateOfficerRejectSummary.jsp
 *
 * Purpose:                 This action transfers the request to ApplyUpdateOfficerRejectSummary.jsp
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
import org.afscme.enterprise.update.member.MemberPreUpdateSummary;
import org.afscme.enterprise.update.member.MemberChanges;
import org.afscme.enterprise.update.FieldChange;
import org.afscme.enterprise.update.Codes; 
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;
/******************************************************************************/
/**
 * @struts:action           path="/viewApplyUpdateOfficerRejectSummary"
 *
 *                          scope="request"
 *                          input="/Update/ApplyUpdateOfficerEditSummary.jsp"
 *
 * @struts:action-forward   name="success"  path="/Update/ApplyUpdateOfficerRejectSummary.jsp"
 */

public class ViewApplyUpdateOfficerRejectSummaryAction extends AFSCMEAction{
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        Logger log = Logger.getLogger(ViewApplyUpdateOfficerRejectSummaryAction.class);
        log.debug("----------------------------------------------------");
        log.debug("perform called.");
        log.debug("------------------------------------------------------");
        log.debug("pk======>" + request.getParameter("queuePk"));
        log.debug("------------------------------------------------------");
        
        String sPK                                    = (String) (request.getParameter("queuePk"));
        int iPK                                       = Integer.parseInt(sPK);
        Integer pk                                    = new Integer(iPK);
        
        String sAffPK                                 = (String) (request.getParameter("affPk"));
        int iAffPK                                    = Integer.parseInt(sAffPK);
        Integer affPk                                 = new Integer(iAffPK);
        
        // needed for header and footer tags
        setCurrentAffiliate(        request                  ,  affPk                       );

        request.setAttribute(       new String("queuePk")    ,   sPK                        );
        
        request.setAttribute(       new String("affPk")      ,   affPk                      );
        
        request.setAttribute(       new String("rejected")   ,   new String("false")        );

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
        
        
        
    }//end of perform method
    
}//end of ViewApplyUpdateMemberRejectSummaryAction


    
    

