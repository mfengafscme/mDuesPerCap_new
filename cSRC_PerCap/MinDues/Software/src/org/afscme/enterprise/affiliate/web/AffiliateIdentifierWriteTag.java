package org.afscme.enterprise.affiliate.web;

// Java Imports
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

// Struts Imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.taglib.bean.WriteTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

// AFSCME Imports
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * <p>
 * Handles the display of an Affiliate Identifier along with it's html column 
 * structure. 
 *
 * <p>
 * Reuses the inherited name-property mechanism from the Struts Bean 
 * Write tag for retrieving an AffiliateIdentifier from the request or session 
 * (using the Write tag's scope attribute). If the associated bean is not an 
 * AffiliateIdentifier, the tag provides affIdXXX attributes that can be used in 
 * in conjunction with the name attribute to specify the name of the bean's 
 * Affiliate Identifier related fields.
 *
 * <p>
 * Examples:
 *
 * <p>
 * <blockquote>
 *      &lt;afscme:affiliateIdWrite name="result" property="affiliateId"  /&gt;
 *      <BR><BR>
 *
 *      OR
 *      <BR><BR>
 *      &lt;
 *          afscme:affiliateIdWrite 
 *                  name="affiliateDetail" 
 *                  affIdTypeProperty="affIdType" 
 *                  affIdLocalProperty="affIdLocal" 
 *                  affIdStateProperty="affIdState" 
 *                  affIdSubUnitProperty="affIdSubUnit" 
 *                  affIdCouncilProperty="affIdCouncil" 
 *                  affIdAdminCouncilProperty="affIdAdminLegisCouncil" 
 *                  tdClass="ContentTD"
 *      /&gt;
 *      <BR><BR>
 *
 *      OR
 *      <BR><BR>
 *      &lt;
 *          afscme:affiliateIdWrite 
 *                  name="affiliateId" 
 *                  tdClass="largeListFONT" 
 *                  width="4%,20%,22%" 
 *                  includeSubs="false"
 *      &gt;
 * </blockquote>
 */
public class AffiliateIdentifierWriteTag extends WriteTag {
    
    protected static final String DISTRICT_CN_FORMAT = "{0}";
    protected static final String ADMIN_DIST_CN_FORMAT = "{0}-{1}";
    protected static final String[] EMPTY_STRING_ARRAY = new String[] { "", "" };
    
    protected String affIdTypeProperty;
    protected String affIdLocalProperty;
    protected String affIdStateProperty;
    protected String affIdSubUnitProperty;
    protected String affIdCouncilProperty;
    protected String affIdAdminCouncilProperty;
    
    /** Indicates if columns should be in it's own table. Defaults to false. */
    protected boolean wrapTable;
    
    /** comma separated list of column width values */
    protected String width;
    
    /** Either 'left', 'center', or 'right'. Defaults to 'center'.*/
    protected String tdAlign;
    
    /** The Style Class to use for the columns. */
    protected String tdClass;
    
    /** Indicates if columns for the subs should be displayed or not. Uses the affIdType to determine which are subs. */
    protected boolean includeSubs;
    
    /** Creates a new instance of AffiliateIdentifierWriteTag */
    public AffiliateIdentifierWriteTag() {
        affIdTypeProperty = "type";
        affIdLocalProperty = "local";
        affIdStateProperty = "state";
        affIdSubUnitProperty = "subUnit";
        affIdCouncilProperty = "council";
        affIdAdminCouncilProperty = "administrativeLegislativeCouncil";
        width = null;
        tdAlign = "center";
        tdClass = "";
        includeSubs = true;
        wrapTable = false;
    }
    
    public int doStartTag() throws JspException {

        // Look up the requested bean (if necessary)
        Object bean = null;
        String beanName = (TextUtil.isEmptyOrSpaces(name)) ? Constants.BEAN_KEY : name;
        Object affIdType = null;
        Object affIdLocal = null;
        Object affIdState = null;
        Object affIdSubUnit = null;
        Object affIdCouncil = null;
        Object affIdAdminCn = null;
        if (ignore) {
            if (RequestUtils.lookup(pageContext, name, scope) == null &&
                RequestUtils.lookup(pageContext, name, property, scope) == null
            ) {
                return (SKIP_BODY);  // Nothing to output
            }
        }
        if (TextUtil.isEmptyOrSpaces(property)) {
            bean = RequestUtils.lookup(pageContext, name, scope);
            affIdType = RequestUtils.lookup(pageContext, beanName, affIdTypeProperty, scope);
            affIdLocal = RequestUtils.lookup(pageContext, beanName, affIdLocalProperty, scope);
            affIdState = RequestUtils.lookup(pageContext, beanName, affIdStateProperty, scope);
            affIdSubUnit = RequestUtils.lookup(pageContext, beanName, affIdSubUnitProperty, scope);
            affIdCouncil = RequestUtils.lookup(pageContext, beanName, affIdCouncilProperty, scope);
            affIdAdminCn = RequestUtils.lookup(pageContext, beanName, affIdAdminCouncilProperty, scope);
        } else {
            bean = RequestUtils.lookup(pageContext, name, property, scope);
            affIdType = ((AffiliateIdentifier)bean).getType();
            affIdLocal = ((AffiliateIdentifier)bean).getLocal();
            affIdState = ((AffiliateIdentifier)bean).getState();
            affIdSubUnit = ((AffiliateIdentifier)bean).getSubUnit();
            affIdCouncil = ((AffiliateIdentifier)bean).getCouncil();
            affIdAdminCn = ((AffiliateIdentifier)bean).getAdministrativeLegislativeCouncil();
        }
        
        if (bean == null) {
            return (SKIP_BODY);  // Nothing to output
        }
        
        String[] widths = new String[5];
        if (!TextUtil.isEmptyOrSpaces(width)) {
            int count = 0;
            StringTokenizer st = new StringTokenizer(width, ",");
            while (st.hasMoreTokens() && count < 5) {
                widths[count++] = st.nextToken();
            }
        }
        
        if (wrapTable) {
            ResponseUtils.write(pageContext, "<TABLE><TR>");
        }
        
        int colNum = 0;
        // print column for type
        printColumn(affIdType, widths[colNum++]);
        // print column for local if necessary
        if (includeSubs || "LSU".indexOf(affIdType.toString()) > -1 ) {
            printColumn(affIdLocal, widths[colNum++]);
        }
        // print column for state
        printColumn(affIdState, widths[colNum++]);
        // print column for subunit if necessary
        if (includeSubs || affIdType.toString().equals("U")) {
            printColumn(affIdSubUnit, widths[colNum++]);
        }
        // print column for council/admin council
        printCouncilColumn(affIdCouncil, affIdAdminCn, widths[colNum++]);
        
        if (wrapTable) {
            ResponseUtils.write(pageContext, "</TR></TABLE>");
        }
        
        return (SKIP_BODY);
    }
    
    private void printColumn(Object value, String width) throws JspException {
        String startTD = "<TD align='" + tdAlign + "' {0} {1}>";
        String endTD = "</TD>";
        MessageFormat tdFormat = new MessageFormat(startTD);
        String[] tdArgs = null;
        if (TextUtil.isEmpty(width) && TextUtil.isEmpty(tdClass)) {
            tdArgs = EMPTY_STRING_ARRAY;
        } else {
            tdArgs = new String[] { 
                            (TextUtil.isEmpty(tdClass)) ? "" : "class='" + tdClass + "'", 
                            (TextUtil.isEmpty(width)) ? "" : "width='"+width+"'"
            };
        }
        
        ResponseUtils.write(pageContext, tdFormat.format(tdArgs));
        if (TextUtil.isEmpty(value)) {
            ResponseUtils.write(pageContext, "&nbsp;");
        } else {
            ResponseUtils.write(pageContext, value.toString());
        }
        ResponseUtils.write(pageContext, endTD);
        
    }
    
    private void printCouncilColumn(Object distCn, Object adminCn, String width) throws JspException {
        MessageFormat format = null;
        String[] args = new String[] { distCn.toString().trim(), (adminCn == null) ? "" : adminCn.toString().trim() };
        if (adminCn == null) {
            format = new MessageFormat(DISTRICT_CN_FORMAT);
        } else {
            format = new MessageFormat(ADMIN_DIST_CN_FORMAT);
        }
        printColumn(format.format(args), width);
    }
    
    /** Getter for property tdClass.
     * @return Value of property tdClass.
     *
     */
    public String getTdClass() {
        return tdClass;
    }
    
    /** Setter for property tdClass.
     * @param tdClass New value of property tdClass.
     *
     */
    public void setTdClass(String tdClass) {
        if (tdClass != null) {
            this.tdClass = tdClass;
        }
    }
    
    /** Getter for property affIdLocalProperty.
     * @return Value of property affIdLocalProperty.
     *
     */
    public String getAffIdLocalProperty() {
        return affIdLocalProperty;
    }
    
    /** Setter for property affIdLocalProperty.
     * @param affIdLocalProperty New value of property affIdLocalProperty.
     *
     */
    public void setAffIdLocalProperty(String affIdLocalProperty) {
        this.affIdLocalProperty = affIdLocalProperty;
    }
    
    /** Getter for property affIdCouncilProperty.
     * @return Value of property affIdCouncilProperty.
     *
     */
    public String getAffIdCouncilProperty() {
        return affIdCouncilProperty;
    }
    
    /** Setter for property affIdCouncilProperty.
     * @param affIdCouncilProperty New value of property affIdCouncilProperty.
     *
     */
    public void setAffIdCouncilProperty(String affIdCouncilProperty) {
        this.affIdCouncilProperty = affIdCouncilProperty;
    }
    
    /** Getter for property affIdSubUnitProperty.
     * @return Value of property affIdSubUnitProperty.
     *
     */
    public String getAffIdSubUnitProperty() {
        return affIdSubUnitProperty;
    }
    
    /** Setter for property affIdSubUnitProperty.
     * @param affIdSubUnitProperty New value of property affIdSubUnitProperty.
     *
     */
    public void setAffIdSubUnitProperty(String affIdSubUnitProperty) {
        this.affIdSubUnitProperty = affIdSubUnitProperty;
    }
    
    /** Getter for property affIdTypeProperty.
     * @return Value of property affIdTypeProperty.
     *
     */
    public String getAffIdTypeProperty() {
        return affIdTypeProperty;
    }
    
    /** Setter for property affIdTypeProperty.
     * @param affIdTypeProperty New value of property affIdTypeProperty.
     *
     */
    public void setAffIdTypeProperty(String affIdTypeProperty) {
        this.affIdTypeProperty = affIdTypeProperty;
    }
    
    /** Getter for property affIdStateProperty.
     * @return Value of property affIdStateProperty.
     *
     */
    public String getAffIdStateProperty() {
        return affIdStateProperty;
    }
    
    /** Setter for property affIdStateProperty.
     * @param affIdStateProperty New value of property affIdStateProperty.
     *
     */
    public void setAffIdStateProperty(String affIdStateProperty) {
        this.affIdStateProperty = affIdStateProperty;
    }
    
    /** Getter for property includeSubs.
     * @return Value of property includeSubs.
     *
     */
    public boolean isIncludeSubs() {
        return includeSubs;
    }
    
    /** Setter for property includeSubs.
     * @param includeSubs New value of property includeSubs.
     *
     */
    public void setIncludeSubs(boolean includeSubs) {
        this.includeSubs = includeSubs;
    }
    
    /** Getter for property affIdAdminCouncilProperty.
     * @return Value of property affIdAdminCouncilProperty.
     *
     */
    public String getAffIdAdminCouncilProperty() {
        return affIdAdminCouncilProperty;
    }
    
    /** Setter for property affIdAdminCouncilProperty.
     * @param affIdAdminCouncilProperty New value of property affIdAdminCouncilProperty.
     *
     */
    public void setAffIdAdminCouncilProperty(String affIdAdminCouncilProperty) {
        this.affIdAdminCouncilProperty = affIdAdminCouncilProperty;
    }
    
    /** Getter for property tdAlign.
     * @return Value of property tdAlign.
     *
     */
    public String getTdAlign() {
        return tdAlign;
    }
    
    /** Setter for property tdAlign.
     * @param tdAlign New value of property tdAlign.
     *
     */
    public void setTdAlign(String tdAlign) {
        if (tdAlign != null) {
            this.tdAlign = tdAlign;
        }
    }
    
    /** Getter for property width.
     * @return Value of property width.
     *
     */
    public String getWidth() {
        return width;
    }
    
    /** Setter for property width.
     * @param width New value of property width.
     *
     */
    public void setWidth(String width) {
        this.width = width;
    }
    
    /** Getter for property wrapTable.
     * @return Value of property wrapTable.
     *
     */
    public boolean isWrapTable() {
        return wrapTable;
    }
    
    /** Setter for property wrapTable.
     * @param wrapTable New value of property wrapTable.
     *
     */
    public void setWrapTable(boolean wrapTable) {
        this.wrapTable = wrapTable;
    }
    
}
