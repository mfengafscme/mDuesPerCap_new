package org.afscme.enterprise.common.web;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.StringTokenizer;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.OptionsTag;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.util.ResponseUtils;



/**
 * Custom tag that displays the &ltoption&gt tags for the values in a code table
 */
public class CodeOptionsTag extends OptionsTag {

    /** The key of the common code type */
        protected String codeType;
    
    /** iff true, an option is displayed that sets the value to "" */
        protected boolean allowNull;
    
    /** if allowNull is set, this string is displayed for the null value */
	protected String nullDisplay = "";
    
    /** defines how the code and description will be displayed.  (See {@link java.text.MessageFormat})
     *  The {0} argument is the code and the {1} argument is the description.
     * Defaults to "{0} - {1}" */
        protected String format = CodeWriteTag.DEFAULT_FORMAT;

    /** the parsed version of the 'format' property */
        protected MessageFormat messageFormat;
    
    /** flag that determines if the MaintainCodes reference has been received */
	protected static boolean s_initialized;

    /** static reference to the MaintainCodes interface */
	protected static MaintainCodes s_maintainCodes;

    /** Iff true, uses the code as the value, instead of the primary key */
        protected boolean useCode;
    
    /** Comma separated list of codes to exclude. */
        protected String excludeCodes;
		
    /**
     * Process the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        if (!s_initialized) {
                init();
        }
		
        // use the excludeCodes String to build an array
        Set exclude = new TreeSet();
        if (excludeCodes != null) {
            StringTokenizer st = new StringTokenizer(excludeCodes, ",");
            while (st.hasMoreTokens()) {
                exclude.add(st.nextToken().trim());
            }
        }
        
        StringBuffer sb = new StringBuffer();
        if (messageFormat == null)
            messageFormat = new MessageFormat(format);

        // Acquire the select tag we are associated with
        SelectTag selectTag = (SelectTag) pageContext.getAttribute(Constants.SELECT_KEY);
        if (selectTag == null)
            throw new JspException(messages.getMessage("optionsTag.select"));

        //add the null option
        if (allowNull)
            addOption(sb, "", nullDisplay, selectTag.isMatched("")); 

        //add the code options
        Map codes = s_maintainCodes.getCodes(codeType);
        Iterator it = codes.keySet().iterator();
        String[] args = new String[2];
        while (it.hasNext()) {
            CodeData codeData = (CodeData)codes.get(it.next());
            if (!codeData.isActive())
                continue;
            String value = useCode ? codeData.getCode() : codeData.getPk().toString();
            args[0] = codeData.getCode();
            args[1] = codeData.getDescription();
            if (!exclude.contains(codeData.getCode()))
                addOption(sb, value, messageFormat.format(args), selectTag.isMatched(value));
            
        }

        // Render this element to our writer
        ResponseUtils.write(pageContext, sb.toString());

        release();  //This should not be needed. Spec says release() is called after doEndTag(), Weblogic does this properly, Tomcat does not)
            
        // Evaluate the remainder of this page
        return EVAL_PAGE;
    }
	
    /**
     * Add an option element to the specified StringBuffer based on the
     * specified parameters.
     *
     * @param sb StringBuffer accumulating our results
     * @param value Value to be returned to the server for this option
     * @param label Value to be shown to the user for this option
     * @param matched Should this value be marked as selected?
     */
    protected void addOption(StringBuffer sb, String value, String label,
                             boolean matched) {

        sb.append("<option value=\"");
        sb.append(value);
        sb.append("\"");
        if (getStyle() != null) {
            sb.append(" style=\"");
            sb.append(getStyle());
            sb.append("\"");
        }
        if (getStyleClass() != null) {
            sb.append(" class=\"");
            sb.append(getStyleClass());
            sb.append("\"");
        }
        if (matched)
            sb.append(" SELECTED");
        sb.append(">");
        sb.append(ResponseUtils.filter(label));
        sb.append("</option>\r\n");

    }

    public void release() {
        setFormat(CodeWriteTag.DEFAULT_FORMAT);
		excludeCodes = null;
    }
    
    /** Gets references to the MaintainCodes stateless session bean */
    private static synchronized void init() throws JspException {
		if (s_initialized)
			return;
        
		s_initialized = true;

		try {
			s_maintainCodes = JNDIUtil.getMaintainCodesHome().create();
		} catch (NamingException e) {
			throw new JspException("Unable to find dependent EJB in CodeOptionsTag.init()", e);
		} catch (CreateException e) {
			throw new JspException("Unable to find dependent EJB in CodeOptionsTag.init()", e);
		}
	}
    

    public String getCodeType() {
		return codeType;
	}
	
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	
	public boolean isAllowNull() {
		return allowNull;
	}
	
	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
	}
	
	public String getNullDisplay() {
		return nullDisplay;
	}
	
	public void setNullDisplay(String nullDisplay) {
		this.nullDisplay = nullDisplay;
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
    
    /** Getter for property excludeCodes.
     * @return Value of property excludeCodes.
     *
     */
    public String getExcludeCodes() {
        return excludeCodes;
    }
    
    /** Setter for property excludeCodes.
     * @param excludeCodes New value of property excludeCodes.
     *
     */
    public void setExcludeCodes(String excludeCodes) {
        this.excludeCodes = excludeCodes;
    }
	
}
