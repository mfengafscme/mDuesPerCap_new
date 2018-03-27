/*
 * ViewApplyUpdateMemberExceptionDetail.java
 *
 * Created on August 8, 2003, 11:08 AM
 */
/***********************************************************************************/
/* Called by:               ApplyUpdateMemberEditSummary.jsp
 *
 * Forwards request to:     ApplyUpdateMemberExceptionDetail.jsp
 *
 * Purpose:                 This action uses the methods of UpdateBean EJB to get the data 
 *                          from the database about the exceptions. The exceptions occur as a result of 
 *                          applying business rules to the data coming in. The exceptions are displayed  
 *                          on the screen using ApplyUpdateMemberExceptionDetail.jsp. 
 *
 * Required:                None
 *
 *
 *
 *************************************************************************************/
package org.afscme.enterprise.update.web;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ArrayList;
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
import org.afscme.enterprise.update.ExceptionData;
/******************************************************************************/
/**
 * @struts:action   path="/viewApplyUpdateMemberExceptionDetail"
 *                  
 *                  scope="request"
 *                  
 *
 * @struts:action-forward   name="success"  path="/Update/ApplyUpdateMemberExceptionDetail.jsp"
 */

public class ViewApplyUpdateMemberExceptionDetailAction extends AFSCMEAction {
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        Logger log = Logger.getLogger(ViewApplyUpdateMemberExceptionDetailAction.class);
        log.debug("----------------------------------------------------");
        log.debug("Exception perform called.");
        log.debug("------------------------------------------------------");
        
        String sId          = request.getParameter("recId");
        int iId             = Integer.parseInt(sId);
        Integer id          = new Integer(iId);
        
        String sPK          = (String) (request.getParameter("affPk"));
        int iPK             = Integer.parseInt(sPK);
        Integer pk          = new Integer(iPK);
        
        
        HttpSession session = request.getSession();
        
        Iterator it         =  ((Map) session.getAttribute("e")).entrySet().iterator();
        //****************************************************************************
        Iterator        lt           = null;
        Map.Entry       parentEntry  = null;
        Map.Entry       childEntry   = null;
        HashMap         map          = null;
        ArrayList       list         = null;
        ExceptionData   eData        = null;
        //***************************************************************************
        while (it.hasNext()) {
             parentEntry  = (Map.Entry)it.next();
             
             
             list   = (ArrayList) parentEntry.getValue();
             
             lt     = list.iterator();
             while(lt.hasNext()){
                 
                 eData = (ExceptionData) lt.next();
                 if((pk.intValue() == eData.getAffPk().intValue()) && (id.intValue() == eData.getRecordId().intValue())){
                     log.debug("RecordId=========>" + eData.getRecordId());
                     log.debug("AffPk=========>"    + eData.getAffPk());
                     request.setAttribute(new String("exception"), eData.getFieldChangeDetails());
                     request.setAttribute(new String("recId")    , eData.getRecordId()  );
                     request.setAttribute(new String("affPk")    , eData.getAffPk()  );
                 }
             }
         }
        //*********************************************************************************
        setCurrentAffiliate(request, pk);
        
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
    
    
}//end of action class
