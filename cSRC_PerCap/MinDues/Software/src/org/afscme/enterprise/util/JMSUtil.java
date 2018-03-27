package org.afscme.enterprise.util;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import org.afscme.enterprise.util.JNDIUtil;

/**
 * Provides static methods for common JMS functions
 */
public class JMSUtil {
    
    private static String JMS_FACTORY_NAME = ConfigUtil.getConfigurationData().getJMSFactoryName();
    
    public static final String REPORT_QUEUE = ConfigUtil.getConfigurationData().getJMSReportQueueName();
    public static final String SYSTEM_LOG_QUEUE = ConfigUtil.getConfigurationData().getJMSSystemLogQueueName();
    public static final String UPDATE_QUEUE = ConfigUtil.getConfigurationData().getJMSUpdateQueueName();
    
    /**
     * Retrieves a connection to the application's JMS Server.
     *
     * @return The QueueConnection object.
     */
    public static QueueConnection getConnection() {
        try {
            return ((QueueConnectionFactory)JNDIUtil.getInitialContext().lookup(JMS_FACTORY_NAME)).createQueueConnection();
        } catch (NamingException e) {
            throw new RuntimeException("Could not find JMS Connection Factory '" + JMS_FACTORY_NAME + "' because: " + e.toString());
        } catch (JMSException e) {
            throw new RuntimeException("Could not get a connection to the JMS Server because: " + e.toString());
        }
    }
    
    /**
     * Sends a ObjectMessage, using the Serializable msg, to one of the queues 
     * on the application's JMS Server.
     * 
     * @param queueConnection   connection to the JMS Server
     * @param queueName         name of the queue
     * @param msg               message to send
     */
    public static void sendObjectMessage(QueueConnection queueConnection, String queueName, Serializable msg) throws NamingException, JMSException {
        QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender queueSender = queueSession.createSender((Queue)JNDIUtil.getInitialContext().lookup(queueName));
        ObjectMessage om = queueSession.createObjectMessage(msg);
        //om.setJMSExpiration(9000000000000000000L);
        queueSender.send(om);
        queueSession.close();
    }
    
    /**
     * Sends a TextMessage, using the Serializable msg, to one of the queues 
     * on the application's JMS Server.
     * 
     * @param queueConnection   connection to the JMS Server
     * @param queueName         name of the queue
     * @param msgText           message to send
     */
    public static void sendTextMessage(QueueConnection queueConnection, String queueName, String msgText) throws NamingException, JMSException {
        QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender queueSender = queueSession.createSender((Queue)JNDIUtil.getInitialContext().lookup(queueName));
        queueSender.send(queueSession.createTextMessage(msgText));
        queueSession.close();
    }

}
