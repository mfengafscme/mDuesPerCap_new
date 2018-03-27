package org.afscme.enterprise.common.ejb;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.MessageListener;

/**
 * @ejb:bean generate="false" transaction-type="Container"
 *
 * 
 */
public abstract class MessageBase implements MessageDrivenBean, MessageListener {

    protected MessageDrivenContext context;
    
    public void ejbCreate() {
    }
    
    public void ejbRemove() {
    }
    
    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) {
        context = messageDrivenContext;
    }

}
