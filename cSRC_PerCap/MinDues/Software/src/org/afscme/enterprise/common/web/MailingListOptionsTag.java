package org.afscme.enterprise.common.web;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.OptionsTag;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.util.ResponseUtils;
import org.afscme.enterprise.person.ejb.*;
import org.afscme.enterprise.organization.ejb.*;



/**
 * Custom tag that displays the &ltoption&gt tags for the values in Mailing_Lists_By_Orgs/Mailing_Lists_By_Persons tables
 */
public class MailingListOptionsTag extends OptionsTag {
    
    /** Mailing Lists type - for Person or Organization */
    protected String type;
    
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
    
    /** the mailing list type: value should either be person or organization */
    protected static String s_mailingListType;

    /** static reference to the MaintainOrgMailingLists interface */
    protected static MaintainOrgMailingLists s_maintainOrgMailingLists;

    /** static reference to the MaintainPersons interface */
    protected static MaintainPersonMailingLists s_maintainPersonMailingLists;
    
    private static final String ORGANIZATION_MAILING_LIST = "organization";
    private static final String PERSON_MAILING_LIST = "person";
    
    /**
     * Process the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        
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
        s_mailingListType = getType();
        Map codes = null;
        if (s_mailingListType.equalsIgnoreCase(ORGANIZATION_MAILING_LIST)) {
            if (s_maintainOrgMailingLists == null) {
                init();
            }
            codes = s_maintainOrgMailingLists.getMailingListNames();
        } else if (s_mailingListType.equalsIgnoreCase(PERSON_MAILING_LIST)) {
            if (s_maintainPersonMailingLists == null) {
                init();
            }
            codes = s_maintainPersonMailingLists.getMailingListNames();
        }
        
        Iterator it = codes.keySet().iterator();
        String[] args = new String[2];
        String rsCode, rsValue;
        while (it.hasNext()) {
            Object key = it.next();
            rsCode = (String) key;   
            rsValue = (String) codes.get(key);                            
            args[0] = rsCode;
            args[1] = rsValue;
            addOption(sb, rsCode, messageFormat.format(args), selectTag.isMatched(rsCode));
        }
        
        // Render this element to our writer
        ResponseUtils.write(pageContext, sb.toString());
        
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
    protected void addOption(StringBuffer sb, String value, String label, boolean matched) {
        
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
    }
    
    /** Gets references to the MaintainCodes stateless session bean */
    private static synchronized void init() throws JspException {        
        try {            
            if (s_mailingListType.equalsIgnoreCase(ORGANIZATION_MAILING_LIST)) {
                s_maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
            } else if (s_mailingListType.equalsIgnoreCase(PERSON_MAILING_LIST)) {
                s_maintainPersonMailingLists = JNDIUtil.getMaintainPersonMailingListsHome().create();
            }
        } catch (NamingException e) {
            throw new JspException("Unable to find dependent EJB in MailingListOptionsTag.init()", e);
        } catch (CreateException e) {
            throw new JspException("Unable to find dependent EJB in MailingListOptionsTag.init()", e);
        }
    }
    
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
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
}
