package org.afscme.enterprise.update.ejb;

import org.afscme.enterprise.update.FileProcessor;
import org.afscme.enterprise.update.UpdateMessage;
import org.afscme.enterprise.update.PreUpdateMessage;
import org.afscme.enterprise.common.ejb.MessageBase;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import org.apache.log4j.Logger;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.update.PreUpdateMessage;
import org.afscme.enterprise.update.UpdateMessage;
import org.afscme.enterprise.log.SystemLog;
import org.afscme.enterprise.util.JNDIUtil;

/**
 * Receives messages to kick off the pre-update generation and apply update processes.
 */
public class UpdateMessageBean extends MessageBase {
    
    protected Update updateBean = null;

    protected static Logger logger = Logger.getLogger(UpdateMessageBean.class);

    public void ejbCreate() {
        
        ConfigUtil.init();
        
        try {
            updateBean = JNDIUtil.getUpdateHome().create();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (CreateException exp) {
            throw new EJBException(exp);
        }
    }
    
    public void ejbRemove() {
        try {
            updateBean.remove();
        }
        catch (RemoveException exp) {
            throw new EJBException(exp);
        }
    }    
    
    /**
     * Called by the bean's container when a message has arrived for the bean to service.
     * It contains the business logic that handles the processing of the message. Only
     * message-driven beans can asynchronously receive messages. Session and entity beans
     * are not permitted to be JMS MessageListener.
     * Called by JMS when an UpdateMessage or PreUpdateMessage is sent.  Calls applyUpdate()
     *  or generatePreUpdateSummary() respectively.
     *
     * @ejb:transaction type="Required"
     */
    public void onMessage(Message message) { 
        logger.debug("----------------------------------------------------");
        logger.debug("onMessage called. Message is: " + message);
        try {
            Object msg = ((ObjectMessage)message).getObject();
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
                throw new RuntimeException("Unknown message type " + msg + " sent to UpdateMessageBean");
            }
            message.acknowledge();
            logger.debug("Message has been acknowledged.");
        }
        catch (JMSException e) {
            logger.error("error occurred.");
            throw new EJBException(e);
        } 
        logger.debug("The following message has completed: " + message);
    }
}
