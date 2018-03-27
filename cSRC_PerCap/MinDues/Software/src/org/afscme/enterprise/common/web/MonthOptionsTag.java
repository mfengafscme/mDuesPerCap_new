package org.afscme.enterprise.common.web;

import javax.servlet.jsp.JspException;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.OptionsTag;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.util.ResponseUtils;

/**
 * Custom tag that displays the &ltoption&gt tags for the months of the year
 */
public class MonthOptionsTag extends org.apache.struts.taglib.html.OptionsTag {
    
    /** iff true, an option is displayed that sets the value to "" */
        protected boolean allowNull;
    
    /** if allowNull is set, this string is displayed for the null value */
	protected String nullDisplay = "";
    
    /** Creates a new instance of MonthOptionsTag */
    public MonthOptionsTag() {
    }
    
    /**
     * Process the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
		
        StringBuffer sb = new StringBuffer();
//        if (messageFormat == null)
//            messageFormat = new MessageFormat(format);

        // Acquire the select tag we are associated with
        SelectTag selectTag = (SelectTag) pageContext.getAttribute(Constants.SELECT_KEY);
        if (selectTag == null)
            throw new JspException(messages.getMessage("optionsTag.select"));

        //add the null option
        if (allowNull)
            addOption(sb, "", nullDisplay, selectTag.isMatched(""));

        //add the month options
        addOption(sb, "1", "January", selectTag.isMatched("1"));
        addOption(sb, "2", "February", selectTag.isMatched("2"));
        addOption(sb, "3", "March", selectTag.isMatched("3"));
        addOption(sb, "4", "April", selectTag.isMatched("4"));
        addOption(sb, "5", "May", selectTag.isMatched("5"));
        addOption(sb, "6", "June", selectTag.isMatched("6"));
        addOption(sb, "7", "July", selectTag.isMatched("7"));
        addOption(sb, "8", "August", selectTag.isMatched("8"));
        addOption(sb, "9", "September", selectTag.isMatched("9"));
        addOption(sb, "10", "October", selectTag.isMatched("10"));
        addOption(sb, "11", "November", selectTag.isMatched("11"));
        addOption(sb, "12", "December", selectTag.isMatched("12"));
        
        // Render this element to our writer
        ResponseUtils.write(pageContext, sb.toString().trim());

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
        this.setNullDisplay(null);
        this.setAllowNull(false);
    }
    
    /** Getter for property allowNull.
     * @return Value of property allowNull.
     *
     */
    public boolean isAllowNull() {
        return allowNull;
    }
    
    /** Setter for property allowNull.
     * @param allowNull New value of property allowNull.
     *
     */
    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }
    
    /** Getter for property nullDisplay.
     * @return Value of property nullDisplay.
     *
     */
    public String getNullDisplay() {
        return nullDisplay;
    }
    
    /** Setter for property nullDisplay.
     * @param nullDisplay New value of property nullDisplay.
     *
     */
    public void setNullDisplay(String nullDisplay) {
        this.nullDisplay = nullDisplay;
    }
    
}
