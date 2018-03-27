package org.afscme.enterprise.common.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.bean.WriteTag;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;


/**
 * Like struts bean:write, but puts a code description, instead of a code primary key, on the page.
 */
public class DateWriteTag extends WriteTag {
	
    /** Iff true, writes the date and time, otherwise, just writes the date */
    protected boolean writeTime;
    
    /** Iff true, also writes the value as a hidden property */
    protected boolean writeHidden;
    
    /** User can use this attribute to specify the format to use in formatting date using SimpleDateFormat. */
    private String format;
    
    /**
     * Retrieve the required property and expose it as a scripting variable.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        String display = null;

        Object value = lookup();
        if (value == null) {
            ResponseUtils.write(pageContext, "&nbsp;");
            return (SKIP_BODY);  
        }

        if (!(value instanceof Timestamp))
            throw new JspException("Property " + property + " is not a Timestamp");

        if (writeTime)
        {
            display = TextUtil.formatDateTime((Timestamp)value);
        }
        else
        {
            if (format != null && format.length() > 0){
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                display = sdf.format(new Date(((Timestamp)value).getTime()));
            }            
            else{
            
            display = TextUtil.format((Timestamp)value);
            }
        }
    
        ResponseUtils.write(pageContext, display);
        
        if (writeHidden)
            ResponseUtils.write(pageContext, "<input type=\"hidden\" name=\""+property+"\" value=\""+value+"\">");

        release();  //This should not be needed. Spec says release() is called after doEndTag(), Weblogic does this properly, Tomcat does not)
            
        return (SKIP_BODY);
    }
	
    protected Object lookup() throws JspException{
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
    
    public void release() {
        super.release();
        writeHidden = false;
        writeTime = false;
    }

    
    /** Getter for property writeTime.
     * @return Value of property writeTime.
     *
     */
    public boolean isWriteTime() {
        return writeTime;
    }
    
    /** Setter for property writeTime.
     * @param writeTime New value of property writeTime.
     *
     */
    public void setWriteTime(boolean writeTime) {
        this.writeTime = writeTime;
    }
    
    /** Getter for property writeHidden.
     * @return Value of property writeHidden.
     *
     */
    public boolean getWriteHidden() {
        return writeHidden;
    }
    
    /** Setter for property writeHidden.
     * @param writeHidden New value of property writeHidden.
     *
     */
    public void setWriteHidden(boolean writeHidden) {
        this.writeHidden = writeHidden;
    }

    /** Getter for property format.
     * @return Value of property format.
     *
     */    
    public String getFormat() {
        return format;
    }

    /** Setter for property format.
     * @param writeHidden New value of property format.
     *
     */    
    public void setFormat(String s){
        this.format = s;
    }    
    
}
