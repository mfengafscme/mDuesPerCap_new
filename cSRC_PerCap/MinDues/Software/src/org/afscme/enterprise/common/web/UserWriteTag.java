package org.afscme.enterprise.common.web;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.struts.taglib.bean.WriteTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;


/**
 * Like struts bean:write, but puts takes a personPk, and write's their user id
 */
public class UserWriteTag extends WriteTag {
	
    /** Iff true, also writes the property to hidden property with this name */
    protected boolean writeHidden;
    
    /** static reference to the MaintainUsers interface */
	protected static MaintainUsers s_maintainUsers;
    
    /** flag that determines if the MaintainUsers reference has been received */
	protected static boolean s_initialized;

    /**
	 * Retrieve the required property and expose it as a scripting variable.
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {

		if (!s_initialized) {
			init();
		}
        
        Object value = lookup();
        if (value == null)
            return (SKIP_BODY);  // Nothing to output

        if (!(value instanceof Integer))
            throw new JspException("Property " + property + " is not an Integer");

        String display = s_maintainUsers.getUserId((Integer)value);
        
        ResponseUtils.write(pageContext, display);
        if (writeHidden)
            ResponseUtils.write(pageContext, "<input type=\"hidden\" name=\""+property+"\" value=\""+value+"\">");
        
        release();  //This should not be needed. Spec says release() is called after doEndTag(), Weblogic does this properly, Tomcat does not)

        return (SKIP_BODY);
	}
    
    public void release() {
        super.release();
        writeHidden = false;
    }
	
    protected Object lookup() throws JspException {
		// Look up the requested bean (if necessary)
		Object bean = null;
		if (ignore) {
			if (RequestUtils.lookup(pageContext, name, scope) == null)
                return null;
		}
		
        String beanName = name == null ? Constants.BEAN_KEY : name;
        Object value = RequestUtils.lookup(pageContext, beanName, property, scope);

        return value;
    }
    
    /** Gets a static reference to the MaintianCodes ejb */
    private static synchronized void init() throws JspException {
		if (s_initialized)
			return;
		
		s_initialized = true;

		try {
			s_maintainUsers = JNDIUtil.getMaintainUsersHome().create();
		} catch (NamingException e) {
			throw new JspException("Unable to find dependent EJB in UserWriteTag.init()", e);
		} catch (CreateException e) {
			throw new JspException("Unable to find dependent EJB in UserWriteTag.init()", e);
		}
	}
    
    /** Getter for property writeHidden.
     * @return Value of property writeHidden.
     *
     */
    public boolean isWriteHidden() {
        return writeHidden;
    }
    
    /** Setter for property writeHidden.
     * @param writeHidden New value of property writeHidden.
     *
     */
    public void setWriteHidden(boolean writeHidden) {
        this.writeHidden = writeHidden;
    }
    
}
