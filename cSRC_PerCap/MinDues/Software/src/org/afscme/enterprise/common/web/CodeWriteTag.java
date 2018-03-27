package org.afscme.enterprise.common.web;

import org.afscme.enterprise.codes.ejb.MaintainCodes;
import java.util.Map;
import org.apache.struts.taglib.bean.WriteTag;
import javax.servlet.ServletException;
import org.apache.struts.taglib.html.Constants;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import javax.servlet.jsp.JspException;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import org.afscme.enterprise.codes.CodeData;
import java.text.MessageFormat;


/**
 * Like struts bean:write, but puts a code description, instead of a code primary key, on the page.
 */
public class CodeWriteTag extends WriteTag {
	
    /** The key of the common code type */
    protected String codeType;
	
    /** flag that determines if the MaintainCodes reference has been received */
	protected static boolean s_initialized;

    /** static reference tot he MaintainCodes interface */
	protected static MaintainCodes s_maintainCodes;

    /** The default format string for the code value display */
    protected static final String DEFAULT_FORMAT = "{0} - {1}";

    /** defines how the code and description will be displayed.  (See {@link java.text.MessageFormat})
     *  The {0} argument is the code and the {1} argument is the description.
     * Defaults to "{0} - {1}" */
    protected String format = DEFAULT_FORMAT;

    /** the parsed version of the 'format' property */
    protected MessageFormat messageFormat;

    /** Iff true, uses the code to look up the value, instead of the primary key */
    protected boolean useCode;

    /** Allows the user to directly specify the pk of the code to write, instead of using a bean */
    protected Integer pk;
    
    /** Iff true, also writes the value as a hidden property */
    protected boolean writeHidden;
    
    /**
     * Retrieve the required property and expose it as a scripting variable.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        if (!s_initialized) {
            init();
        }

        // Look up the requested bean (if necessary)
        Object bean = null;
        if (ignore) {
            if (RequestUtils.lookup(pageContext, name, scope) == null)
                return (SKIP_BODY);  // Nothing to output
        }
		
        Object key;
        if (pk != null && !useCode) {
            key = pk;
        } else {
            // Look up the requested property value
            String beanName = name == null ? Constants.BEAN_KEY : name;
            key = RequestUtils.lookup(pageContext, beanName, property, scope);
        }
        
        if (key == null ||
            (key instanceof Integer && ((Integer)key).intValue() == 0) || 
            (key instanceof String && ((String)key).trim().length() == 0))
        {
            ResponseUtils.write(pageContext, "&nbsp;");
            return (SKIP_BODY);
        }
        
        CodeData codeData = getCodeData(key);

        if (messageFormat == null)
            messageFormat = new MessageFormat(format);
        String[] args = new String[] { codeData.getCode(), codeData.getDescription() };

        ResponseUtils.write(pageContext, messageFormat.format(args));

        if (writeHidden) {
            if (useCode) 
                ResponseUtils.write(pageContext, "<input type=\"hidden\" name=\""+property+"\" value=\""+codeData.getCode()+"\">");
            else 
                ResponseUtils.write(pageContext, "<input type=\"hidden\" name=\""+property+"\" value=\""+key+"\">");
        }
                
        release();  //This should not be needed. Spec says release() is called after doEndTag(), Weblogic does this properly, Tomcat does not)
        
        // Continue processing this page
        return (SKIP_BODY);
    }
	
	public String getCodeType() {
		return codeType;
	}
	
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        if (!this.format.equals(format)) {
            this.format = format;
            messageFormat = null;
        }
    }

    public void release() {
        setFormat(CodeWriteTag.DEFAULT_FORMAT);
        writeHidden = false;
    }
    
    /** Gets the CodeData object that will be written */
    protected CodeData getCodeData(Object key) throws JspException {
        CodeData codeData = null;
        Map codes = null;
        
        if (useCode) 
            codes = (Map)s_maintainCodes.getCodesByCode(codeType);
        else
            codes = (Map)s_maintainCodes.getCodes(codeType);

		if (codes == null)
			throw new JspException("CodeType '" + codeType + "' does not exist");

        //if we're using primary key, coerce 'key' to an Integer
        if (!useCode) {
            if (key instanceof String) {
                try {
                    key = Integer.valueOf((String)key);
                } catch (NumberFormatException e) {
                    throw new JspException("The String '" + key + "' was passed to codeWrite, intended to be used as the primary key for code type ' " + codeType + "', but this value could not be converted to an Integer");
                }
            }
        }
        
        codeData = (CodeData)codes.get(key);
        
		if (codeData == null)
			throw new JspException((useCode ? "Code '" : "Primary Key '") + key + "' does not exist in code type '" + codeType + "'. values that do exist are: " + codes);
            
        return codeData;
    }

    /** Gets a static reference to the MaintianCodes ejb */
    private static synchronized void init() throws JspException {
		if (s_initialized)
			return;
		
		s_initialized = true;

		try {
			s_maintainCodes = JNDIUtil.getMaintainCodesHome().create();
		} catch (NamingException e) {
			throw new JspException("Unable to find dependent EJB in CodeWriteTag.init()", e);
		} catch (CreateException e) {
			throw new JspException("Unable to find dependent EJB in CodeWriteTag.init()", e);
		}
	}
    
    /** Getter for property useCode.
     * @return Value of property useCode.
     *
     */
    public boolean isUseCode() {
        return useCode;
    }
    
    /** Setter for property useCode.
     * @param useCode New value of property useCode.
     *
     */
    public void setUseCode(boolean useCode) {
        this.useCode = useCode;
    }
    
    /** Getter for property pk.
     * @return Value of property pk.
     *
     */
    public Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     *
     */
    public void setPk(Integer pk) {
        this.pk = pk;
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
