package org.afscme.enterprise.common.ejb;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.SessionBean;

/**
 * @ejb:bean generate="false"
 * @ejb:home generate="false"
 * @ejb:interface generate="false"
 * @ejb:pk generate ="false"
 * @ejb:data-object generate ="false"
 *
 */
public abstract class SessionBase implements SessionBean {

    protected SessionContext m_context;

    public void ejbActivate() {
    }

    public void ejbRemove() {
    }

    public void ejbPassivate() {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext context){
        m_context = context;
    }

    public void unsetSessionContext(){
        m_context = null;
    }

}

