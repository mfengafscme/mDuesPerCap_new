package org.afscme.enterprise.update;

import javax.ejb.*;
import javax.jms.*;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.RemoteException;
import org.apache.log4j.Logger;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.update.PreUpdateMessage;
import org.afscme.enterprise.update.UpdateMessage;
import org.afscme.enterprise.log.SystemLog;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.JMSUtil;
import org.afscme.enterprise.update.ejb.*;
import org.afscme.enterprise.update.FileProcessor;
import org.afscme.enterprise.update.UpdateMessage;
import org.afscme.enterprise.update.PreUpdateMessage;

/**
 * Creation date: (11/10/2002)
 * @author: Holly Maiwald
 */
public class ProcessUpdateMessage {
    
    private static Logger logger = Logger.getLogger(ProcessUpdateMessage.class);
    protected Update updateBean = null;
    
    public ProcessUpdateMessage() {
        super();
    }
    
    public void processMessage() throws Exception {
        ObjectMessage message = null;
        Object msg = null;
        QueueConnection qConnection = null;
        Queue updateQueue = null;
        
        // Lookup update bean and update queue in JNDI
        try {
            updateBean = JNDIUtil.getUpdateHome().create();
            //updateQueue = (Queue)JNDIUtil.getClusterWideInitialContext().lookup(JMSUtil.UPDATE_QUEUE);            
            updateQueue = (Queue) JNDIUtil.getInitialContext().lookup(JMSUtil.UPDATE_QUEUE);
        } catch (NamingException exp) {
            logger.debug("Naming Exception in ProcessUpdateMessage: " +exp.toString());
            throw new Exception(exp);
        } catch (CreateException exp) {
            logger.debug("Create Exception in ProcessUpdateMessage: " +exp.toString());
            throw new Exception(exp);
        }        
                
        try {
            qConnection = JMSUtil.getConnection();
            QueueSession qSession = qConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueReceiver qReceiver = qSession.createReceiver(updateQueue);
            qConnection.start();            
            
            
            // get update messages until the queue is empty
            while (true) {
                message = (ObjectMessage) qReceiver.receiveNoWait();
                if (message == null) break;

                logger.debug("----------------------------------------------------");
                logger.debug("Process Update Message");                
                msg = ((ObjectMessage)message).getObject();
                if (msg instanceof PreUpdateMessage) {
                    PreUpdateMessage preMsg = (PreUpdateMessage)msg;
                    // call the generatePreUpdateSummary in UpdateBean
                    logger.debug("PreUpdateMessage found. Calling UpdateBean.generatePreUpdateSummary().");
                    updateBean.generatePreUpdateSummary(preMsg.getQueuePk(), preMsg.getUserPk(), preMsg.getFileType());
                }
                else if (msg instanceof UpdateMessage) {
                    UpdateMessage upMsg = (UpdateMessage)msg;
                    // call the applyUpdate in UpdateBean
                    logger.debug("UpdateMessage found. Calling UpdateBean.applyUpdate().");
                    updateBean.applyUpdate(upMsg.getAffPks(), upMsg.getQueuePk(), upMsg.getUserPk());
                } else {
                    logger.debug("Unknown message type " + msg + " sent to Update Queue.");
                }
                logger.debug("The following message has completed: " + message);
            }
            
            qSession.close();
            qConnection.close();
            
        } catch (JMSException e) {
            logger.debug("JMSException in ProcessUpdateMessage: " +e.toString());
            throw new Exception(e);
        } finally {
            if (qConnection != null)
                qConnection.close();
        }
        
        // Remove update bean
        try {
            updateBean.remove();
        } catch (RemoveException e) {
            throw new Exception(e);
        }
    }
}