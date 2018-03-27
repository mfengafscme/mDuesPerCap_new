package org.afscme.enterprise.update.web;

import java.util.ArrayList;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.update.UpdateMessage;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.JMSUtil;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.Map;
import java.io.*;
/***************************************************************************/
import org.afscme.enterprise.update.member.MemberPreUpdateSummary;
import org.afscme.enterprise.update.member.MemberChanges;
import org.afscme.enterprise.update.FieldChange;
import org.afscme.enterprise.update.Codes; 
/*************************************************************************/
/**
 * @struts:action   path="/performMemberUpdate"
 *                  name="applyUpdateForm"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Update/ApplyUpdateMemberEditSummary.jsp"
 *
 * @struts:action-forward   name="done" path="/Update/ApplyUpdateConfirmation.jsp"
 */
public class PerformMemberUpdateAction extends AFSCMEAction {
    
    private static Logger logger = Logger.getLogger(PerformMemberUpdateAction.class);
    
    /** Creates a new instance of PerformUpdateAction */
    public PerformMemberUpdateAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        logger.debug("----------------------------------------------------");
        logger.debug("perform called.");
        ApplyUpdateForm auf = (ApplyUpdateForm)form;
        
        
        Integer queuePk = auf.getQueuePk();
        if (queuePk == null || queuePk.intValue() < 1
        ) {
            throw new JspException("No queuePk was found to perform Update.");
            //return mapping.getInputForward();
        }
        /*******************************************************************************/
        //Added these request parameters for succes path
        /******************************************************************************/
        String sAffPK                                 =     (String) (request.getParameter("affPk"));
        int iAffPK                                    =     Integer.parseInt(sAffPK);
        Integer affPk                                 =     new Integer(iAffPK);
        String updateType                             =     (String) request.getParameter("updateType");
        
        MemberPreUpdateSummary memberPreUpdateSummary = s_update.getMemberPreUpdateSummary(queuePk);
        
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
        request.setAttribute(       new String("queuePk")      ,   queuePk              );
        request.setAttribute(       new String("view")         ,   new String("false")  );
        request.setAttribute(       new String("rejected")     ,   new String("false")  );
        request.setAttribute(       new String("updateType")   ,   updateType           );
        request.setAttribute(       new String("affPk")        ,   affPk                );
        
        logger.debug("updateType====>." + updateType);
        logger.debug("request.getAttribute('updateType')====>." + request.getAttribute("updateType"));
        if((auf.getAffPks() == null) ){
            ActionErrors errors = new ActionErrors();        
            logger.debug("affpk is null");
            errors.add("affPks", new ActionError("error.update.missingAffiliates")); 
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        //ActionErrors errors = auf.validate(mapping, request);
        /*ActionErrors errors = new ActionErrors();
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            logger.debug("Sending to input page");
            return mapping.getInputForward();
        }*/
        
        
        List affPks = (List)CollectionUtil.copy(new ArrayList(auf.getAffPks().length), auf.getAffPks());
        
        QueueConnection queueConnection = JMSUtil.getConnection();
        try {
            
            UpdateMessage msg = new UpdateMessage();
            msg.setQueuePk(queuePk);
            msg.setAffPks(affPks);
            msg.setUserPk(usd.getPersonPk());
            
            JMSUtil.sendObjectMessage(queueConnection, JMSUtil.UPDATE_QUEUE, msg);
            
            // set file to pending now for change of AUP to batch initiate
            s_fileQueue.markFilePending(queuePk, usd.getPersonPk());
            
        } catch (NamingException ne) {
            throw new JspException(ne);
        } catch (JMSException je) {
            throw new JspException(je);
        }
        logger.debug("message sent to the jms queue");
        
        
        return mapping.findForward("done");
    }
    
}
