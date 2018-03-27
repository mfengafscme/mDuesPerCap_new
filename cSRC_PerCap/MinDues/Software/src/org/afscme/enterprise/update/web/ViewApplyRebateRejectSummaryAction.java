/*
 * ViewApplyMemberRejectSummaryAction.java
 *
 * Created on August 20, 2003, 3:27 PM
 */
/***********************************************************************************/
/* Called by:               ApplyUpdateRebateRejectSummary.jsp
 *
 * Forwards request to:     ApplyUpdate.jsp
 *
 * Purpose:                 This action marks the file rejected with the user comments
 *                          Uses ApplyUpdateRebateRejectForm.java as a buffer to hold the data
 *                          from the jsp
 *
 * Required:                Comments on ApplyUpdateRebateRejectSummary.jsp
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
import org.afscme.enterprise.log.SystemLog;
import org.afscme.enterprise.update.ejb.FileQueue;
/***************************************************************************/
import org.afscme.enterprise.update.rebate.RebatePreUpdateSummary;
import org.afscme.enterprise.update.rebate.RebateChanges;
import org.afscme.enterprise.update.member.MemberPreUpdateSummary;
import org.afscme.enterprise.update.member.MemberChanges;
import org.afscme.enterprise.update.FieldChange;
import org.afscme.enterprise.update.Codes; 
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;
/******************************************************************************/
/**
 * @struts:action   path="/viewApplyRebateRejectSummary"
 *                  name="ApplyUpdateRebateRejectForm"
 *                  validate="true"
 *                  scope="request"
 *                  input="/Update/ApplyUpdateRebateRejectSummary.jsp"
 *
 * @struts:action-forward   name="success"  path="/viewApplyUpdateQueue.action"
 */

public class ViewApplyRebateRejectSummaryAction extends AFSCMEAction{
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        Logger log = Logger.getLogger(ViewApplyUpdateRebateRejectSummaryAction.class);
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
        
        
        ApplyUpdateRebateRejectForm rejectForm        = (ApplyUpdateRebateRejectForm) form;
        
        String comments                               = rejectForm.getComments();
        
 
        request.setAttribute(       new String("queuePk")    ,  pk                      );
        request.setAttribute(       new String("view")       ,  "false"                 );              
        request.setAttribute(       new String("affPk")      ,  affPk                   );        
        request.setAttribute(       new String("rejected")   ,  new String("false")     );
        //*******************************************************************************
        //Added for system logging 
        //*******************************************************************************       
        RebatePreUpdateSummary rebatePreUpdateSummary = s_update.getRebatePreUpdateSummary(pk);                
        RebateChanges totalCount                      = rebatePreUpdateSummary.getTotalCounts();
        FileQueue fileQueue                           = JNDIUtil.getFileQueueHome().create();
        String fileName                               = fileQueue.getFileName(pk);
        SystemLog.logUpdateCancelled(totalCount.getCouncil(), totalCount.getLocal(), totalCount.getUnchanged(), fileName, usd.getUserId());
        //************************************************************************************************************************

	ActionErrors errors = new ActionErrors();
	if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput()); //go back to the edit page
        } else {
            log.debug("------------------------------------------------------");
            log.debug("Forwarding to view page");
            log.debug("------------------------------------------------------");
            if(comments.length() < 256){
                s_fileQueue.markFileRejected(pk, comments, usd.getPersonPk());
                return mapping.findForward("success");
            }else{
                return mapping.findForward("success");  //go back to the view page
            }
	}
        
        
        
    }//end of perform method
    
    
}// end of ViewApplyMemberRejectSummaryAction
