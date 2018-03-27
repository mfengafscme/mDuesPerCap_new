/*
 * ViewApplyUpdateMemberEditSummaryAction.java
 *
 * Created on July 29, 2003, 9:37 AM
 */
/***********************************************************************************/
/* Called by:               ApplyUpdate.jsp
 *
 * Forwards request to:     ApplyUpdateMemberEditSummary.jsp
 *
 * Purpose:                 This action uses the methods of UpdateBean EJB to get the edit  
 *                          summary data from the database and display it on the screen using 
 *                          ApplyUpdateMemberEditSummary.jsp. Also sends the smry of exceptions to be displayed. 
 *                          This action is also used to display  the rejected file data. 
 *                          The rejected flag turns on or off the peform Update
 *                          and reject buttons. Once the file is rejected the update checkboxes used to 
 *                          perform update for a particular affiliate is also grayed out.
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
 * @struts:action   path="/viewApplyUpdateMemberEditSummary"
 *                  
 *                  scope="request"
 *                  
 *
 * @struts:action-forward   name="success"  path="/Update/ApplyUpdateMemberEditSummary.jsp"
 */

public class ViewApplyUpdateMemberEditSummaryAction extends AFSCMEAction {
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        Logger log = Logger.getLogger(ViewApplyUpdateMemberEditSummaryAction.class);
        //MemberPreUpdateSummary memberPreUpdateSummary = JNDIUtil.getMemberPreUpdateSummaryHome();
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
        
        String updateType                             = (String) (request.getParameter("updateType"));
        String rejected                               = (String) (request.getParameter("rejected"));
        
        if(rejected == null){
            rejected    =   "false";
        }
        
        log.debug("updateType=================>" + updateType);
        
        MemberPreUpdateSummary memberPreUpdateSummary = s_update.getMemberPreUpdateSummary(pk);
        
        Map memberCount                               = memberPreUpdateSummary.getMemberCounts();
        
        Map fieldChgCount                             = memberPreUpdateSummary.getFieldChanges();
        
        Map exceptions                                = memberPreUpdateSummary.getExceptions();
        
        Map affID                                     = memberPreUpdateSummary.getAffId();
        
        MemberChanges totalCount                      = memberPreUpdateSummary.getTotalCounts();
        
        
        /****************************************************************************************/
        /*now that we have got the data structures and objects add them to the request so that  */
        /*we can iterate on the UI                                                              */
        /****************************************************************************************/
        request.setAttribute(       new String("memberCount")  ,   memberCount          );
        
        request.setAttribute(       new String("fieldChange")  ,   fieldChgCount        );
        
        request.setAttribute(       new String("tCount")       ,   totalCount           );
        
        request.setAttribute(       new String("exceptions")   ,   exceptions           );
        
        request.setAttribute(       new String("affId")        ,   affID                );
        
        request.setAttribute(       new String("queuePk")      ,   sPK                  );
        
        request.setAttribute(       new String("updateType")   ,   updateType           );
        
        request.setAttribute(       new String("affPk")        ,   affPk                );
        
        if(rejected.equalsIgnoreCase("true")){
            request.setAttribute(   new String("rejected")      ,   new Boolean("true") );  
        }else{
            request.setAttribute(   new String("rejected")      ,   new Boolean("false"));
        }
        
        // needed for header and footer tags
        setCurrentAffiliate(request, affPk);
        /******************************************************************************************/
        
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
    
}//end of ViewApplyUpdateMemberEditSummaryAction


    

