package org.afscme.enterprise.log.ejb;

import javax.ejb.EJBException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import org.afscme.enterprise.common.ejb.MessageBase;
import org.afscme.enterprise.util.ConfigUtil;
import org.apache.log4j.Logger;
import javax.jms.ObjectMessage;
import org.afscme.enterprise.log.ApplicationLog;
import org.afscme.enterprise.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Receives log events from a JMS queue and logs them to the system log file.
 * @ejb:bean name="SystemEventReceiver" display-name="SystemEventReceiver"
 *           destination-type="javax.jms.Queue"
 *
 * @ejb:transaction type="Required"
 *
 * @jboss:destination-jndi-name name="queue/SystemEventReceiver"
 *                          
 */
public class SystemEventReceiverBean extends MessageBase {

    private static String LOGGER_NAME = "afscme.event";
    
    private static final String SQL_INSERT_APP_LOG = 
    "insert into COM_App_Log                " +
    "   (event_nm                           " +
    ",  event_logged_dt                     " +
    ",  event_user_id                       " +
    ",  event_data_string)                  " +
    "values(?, GETDATE(), ?, ?)              " ;
    
    public void ejbCreate() {
        ConfigUtil.init();
    }  

    /**
     * Called by the bean's container when a message has arrived for the bean to service.
     * It contains the business logic that handles the processing of the message. Only
     * message-driven beans can asynchronously receive messages. Session and entity beans
     * are not permitted to be JMS MessageListener.
     * Called by the container when a log message arrives.
     */
    public void onMessage(Message message) {        
        Connection              con   =   DBUtil.getConnection();;
        PreparedStatement       ps    =   null;
        
        
        try {
            if(message instanceof TextMessage){
                Logger logger   = Logger.getLogger(LOGGER_NAME);
                TextMessage msg = (TextMessage)message;
                logger.debug("Got Text message");
                logger.info(msg.getText());
            }else if(message instanceof ObjectMessage){
               Logger logger       =   Logger.getLogger(SystemEventReceiverBean.class);                
                logger.debug("Got Object message");
                Object msg          =   ((ObjectMessage)message).getObject();
                if(msg instanceof ApplicationLog){
                    
                    ApplicationLog log  = (ApplicationLog) msg;                
                    ps                  = con.prepareStatement(SQL_INSERT_APP_LOG); 
                    
                    logger.debug("got application logging message=>" + log.getEventName());
                    
                    ps.setString(   1, log.getEventName()   );
                    ps.setString(   2, log.getEventUserId() );
                    ps.setString(   3, log.getEventData()   );         
                    
                    int rows            = ps.executeUpdate();                
                    logger.debug("rows inside execInsert======>" + rows);
                } else {
                    throw new RuntimeException("Unknown message type " + msg + " sent to UpdateMessageBean");
                }
                message.acknowledge();
                logger.debug("Message has been acknowledged.");    
                }
            
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }catch(SQLException e){
            throw new EJBException(e);
        }finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
}
