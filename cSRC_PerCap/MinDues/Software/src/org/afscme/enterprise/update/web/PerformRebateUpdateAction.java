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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.Map;
import java.io.*;
/***************************************************************************/
import org.afscme.enterprise.update.rebate.RebatePreUpdateSummary;
import org.afscme.enterprise.update.rebate.RebateChanges;
import org.afscme.enterprise.update.FieldChange;
import org.afscme.enterprise.update.Codes; 
/*************************************************************************/
/**
 * @struts:action   path="/performRebateUpdate"
 *                  name="applyUpdateForm"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Update/ApplyUpdateRebateEditSummary.jsp"
 *
 * @struts:action-forward   name="done" path="/Update/ApplyUpdateConfirmation.jsp"
 */
public class PerformRebateUpdateAction extends AFSCMEAction {
    
    private static Logger logger = Logger.getLogger(PerformRebateUpdateAction.class);
    
    /** Creates a new instance of PerformUpdateAction */
    public PerformRebateUpdateAction() {
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
        }
        /*******************************************************************************/
        //Added these request parameters for succes path
        /******************************************************************************/
        String sAffPK                                 =     (String) (request.getParameter("affPk"));
        int iAffPK                                    =     Integer.parseInt(sAffPK);
        Integer affPk                                 =     new Integer(iAffPK);
        String updateType                             =     (String) request.getParameter("updateType");
        
        
        RebatePreUpdateSummary rebatePreUpdateSummary = s_update.getRebatePreUpdateSummary(queuePk);
        
        Map rebateCount                               = rebatePreUpdateSummary.getRebateCounts();
        
        //Map fieldChgCount                             = rebatePreUpdateSummary.getFieldChanges();
        
        Map exceptions                                = rebatePreUpdateSummary.getExceptions();
        
        Map affID                                     = rebatePreUpdateSummary.getAffId();
        
        RebateChanges totalCount                      = rebatePreUpdateSummary.getTotalCounts();
        
        
        /****************************************************************************************/
        /*now that we have got the data structures and objects add them to the request so that  */
        /*we can iterate on the UI                                                              */
        /****************************************************************************************/
        request.setAttribute(       new String("memberCount")  ,   rebateCount          );
        //request.setAttribute(       new String("fieldChange")  ,   fieldChgCount        );
        request.setAttribute(       new String("tCount")       ,   totalCount           );
        request.setAttribute(       new String("exceptions")   ,   exceptions           );
        request.setAttribute(       new String("queuePk")      ,   queuePk              );
        request.setAttribute(       new String("view")         ,   new String("false")  );
        request.setAttribute(       new String("rejected")     ,   new String("false")  );
        request.setAttribute(       new String("updateType")   ,   updateType           );
        request.setAttribute(       new String("affPk")        ,   affPk                );
        
        
        ActionErrors errors = auf.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        
        
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
