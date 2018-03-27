package org.afscme.enterprise.cards.ejb;

import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import org.afscme.enterprise.common.ejb.MessageBase;
import org.afscme.enterprise.log.SystemLog;
import org.afscme.enterprise.cards.CardRunMessage;
import org.afscme.enterprise.cards.CardFileGenerator;
import org.afscme.enterprise.cards.RunSummary;


/**
 * Allows CardsBean.performRun() to be called asynchronously.
 *
 * added abstract to ensure it is compiling, but need to remove once
 * onMessage() is implemented
 */
public abstract class CardsMessageBean implements MessageDrivenBean, MessageListener {
    
    /**
     * Called by the bean's container when a message has arrived for the bean to service.
     * It contains the business logic that handles the processing of the message. Only
     * message-driven beans can asynchronously receive messages. Session and entity beans
     * are not permitted to be JMS MessageListener.
     * Called by JMS.  Receives a CardRunMessage and in turn calls CardsBean.performRunSync()
     */
    public void onMessage    (Message message) {
       
    /**   try {
            
            ObjectMessage msg = (ObjectMessage)message;
            
            CardRunMessage cardRunMessage = (CardRunMessage)msg.getObject();
            RunSummary runSummary = (RunSummary)msg.getObject();
            CardFileGenerator cardFileGenerator = (CardFileGenerator)msg.getObject();
        
                    
            try {
                performRunSync(Integer (runType));
                } 
            catch (Exception msgException) {
                SystemLog.logApplicationError(msgException, null, null);
                throw new EJBException(msgException);
                
            }
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }
        
  */ }  
}