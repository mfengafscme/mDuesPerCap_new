package org.afscme.enterprise.affiliate.web;

// Java Imports
import java.io.IOException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

// Struts Imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.taglib.html.LinkTag;

// AFSCME Imports
import org.afscme.enterprise.util.TextUtil;

/**
 * <p>Adds a Finder link to a screen allowing a popup search results to feed a screens
 * Affiliate Identifier fields.
 *
 * <p>The main attribute used in the finder search is the 'formName'. The remaining 
 * attributes are initialized to default values using the following naming structure 
 * for the fields in the form:
 * <p><blockquote>
 *      <li>affPk</li>
 *      <li>affIdType</li>
 *      <li>affIdCode</li>
 *      <li>affIdCouncil</li>
 *      <li>affIdLocal</li>
 *      <li>affIdState</li>
 *      <li>affIdSubUnit</li>
 *  </blockquote>
 *
 * <p>If the associated form's attribute names differ than those above, they can be 
 * passed into the tag as attributes (see the second example below.)
 *
 * <p>Here are some examples on how to use the tag:
 *
 * <p>
 * <blockquote>
 *      &lt;afscme:affiliateFinder formName="affiliateSearchForm"/&gt;
 *      <BR><BR>
 *
 *      OR
 *      <BR><BR>
 *
 *      &lt;afscme:affiliateFinder formName="affiliateFileUploadForm" affIdTypeParam="affType" affIdCouncilParam="affCouncil" affIdLocalParam="affLocal" affIdStateParam="affState" affIdSubUnitParam="affSubunit" affIdCodeParam="affCode"/&gt;
 * </blockquote>
 *
 * <p>
 * Both the jsp and the associated Struts Form object should have hidden fields 
 * for the Affiliate's PK and the Affiliate Identifier Code field. Both are not 
 * required but are useful. If both of them are not specified, then your action 
 * will not be able to exclusively determine which Affiliate was identified by 
 * the finder search. 
 *
 * <p>
 * <b>Note:</B> Most of the screens where the finder is included have the 
 * requirement for forwarding to the Duplicate Affiliate Screen if the finder 
 * was not used. There are some helper methods, findAffiliatesWithID and 
 * setCurrentAffiliateFinderForm, in the AFSCMEAction class that can be used 
 * for finding the duplicate Affiliates and setting an AffiliateFinderForm in 
 * the session to use for that screen. The action to forward to once it is saved 
 * is 'SearchAffiliateFinderRedirect'. The AffiliateFinderForm class contains a 
 * 'linkAction' that must be set to allow links back to your action with a request 
 * parameter called affPk.
 *
 * <p>
 * See the javadocs for these methods for more details. An example of this can 
 * also be found in the SaveCouncilAffiliationAction.
 */
public class AffiliateFinderTag extends LinkTag {
    
    /** 
     * Name of the form that contains the Affiliate Identifier fields for performing 
     * the finder search. 
     */
    private String formName;
    
    private String affPkParam;
    private String affIdTypeParam;
    private String affIdCodeParam;
    private String affIdCouncilParam;
    private String affIdLocalParam;
    private String affIdStateParam;
    private String affIdSubUnitParam;
    
    /** Creates a new instance of AffiliateFinderTag */
    public AffiliateFinderTag() {
        this.formName           = null;
        this.affPkParam         = "affPk";
        this.affIdCodeParam     = "affIdCode";
        this.affIdCouncilParam  = "affIdCouncil";
        this.affIdLocalParam    = "affIdLocal";
        this.affIdStateParam    = "affIdState";
        this.affIdSubUnitParam  = "affIdSubUnit";
        this.affIdTypeParam     = "affIdType";
        this.setStyleClass("TH");
        this.setTitle("Retrieve your Affilitate Identifier");
    }
	
    /**
     * 
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            /* the affIdTypeParam, affIdLocalParam, affIdStateParam, 
             * affIdSubUnitParam, affIdCouncilParam cannot be set to null by 
             * the tag user.
             */
            if (TextUtil.isEmptyOrSpaces(affIdTypeParam) ||
                TextUtil.isEmptyOrSpaces(affIdLocalParam) ||
                TextUtil.isEmptyOrSpaces(affIdStateParam) ||
                TextUtil.isEmptyOrSpaces(affIdSubUnitParam) ||
                TextUtil.isEmptyOrSpaces(affIdCouncilParam)
            ) {
                throw new JspException(
                    "The following fields cannot be set to null or empty String " + 
                    "in the tag: 'affIdTypeParam', 'affIdLocalParam', 'affIdStateParam', " + 
                    "'affIdSubUnitParam', 'affIdCouncilParam'. Either set them to valid " + 
                    "field names or ommit them from the tag allowing it to use default values."
                );
            }
            // else if all of the affiliateId fields are not null, then create the anchor link
            StringBuffer sb = new StringBuffer("<A href='javascript:showAffiliateIDResults(");
            sb.append(formName);
            sb.append(".elements[\"");
            sb.append(affIdTypeParam);
            sb.append("\"], ");
            sb.append(formName);
            sb.append(".elements[\"");
            sb.append(affIdLocalParam);
            sb.append("\"], ");
            sb.append(formName);
            sb.append(".elements[\"");
            sb.append(affIdStateParam);
            sb.append("\"], ");
            sb.append(formName);
            sb.append(".elements[\"");
            sb.append(affIdSubUnitParam);
            sb.append("\"], ");
            sb.append(formName);
            sb.append(".elements[\"");
            sb.append(affIdCouncilParam);
            sb.append("\"], ");
            if (TextUtil.isEmptyOrSpaces(affIdCodeParam)) {
                sb.append("null");
            } else {
                sb.append(formName);
                sb.append(".elements[\"");
                sb.append(affIdCodeParam);
                sb.append("\"]");
            }
            sb.append(", ");
            if (TextUtil.isEmptyOrSpaces(affPkParam)) {
                sb.append("null");
            } else {
                sb.append(formName);
                sb.append(".elements[\"");
                sb.append(affPkParam);
                sb.append("\"]");
            }
            sb.append(")' ");
            addAttribute(sb, "class", this.getStyleClass());
            addAttribute(sb, "title", this.getTitle());
            addAttribute(sb, "name", this.getLinkName());
            addAttribute(sb, "onblur", this.getOnblur());
            
            addAttribute(sb, "onclick", this.getOnclick());
            addAttribute(sb, "ondblclick", this.getOndblclick());
            addAttribute(sb, "onfocus", this.getOnfocus());
            addAttribute(sb, "onkeydown", this.getOnkeydown());
            addAttribute(sb, "onkeypress", this.getOnkeypress());
            addAttribute(sb, "onkeyup", this.getOnkeyup());
            addAttribute(sb, "onmousedown", this.getOnmousedown());
            addAttribute(sb, "onmousemove", this.getOnmousemove());
            addAttribute(sb, "onmouseout", this.getOnmouseout());
            addAttribute(sb, "onmouseover", this.getOnmouseover());
            addAttribute(sb, "onmouseup", this.getOnmouseup());
            addAttribute(sb, "style", this.getStyle());
            addAttribute(sb, "id", this.getStyleId());
            
            sb.append(">Finder</A>");

            out.println(sb.toString().trim());
            
            return (SKIP_BODY);
        } catch (IOException ioe) {
            throw new JspException(ioe);
        }
    }
    
    private void addAttribute(StringBuffer sb, String name, String value) {
        if (!TextUtil.isEmptyOrSpaces(value)) {
            sb.append(name);
            sb.append("='");
            sb.append(value);
            sb.append("' ");
        }
    }
    
    /** Getter for property formName.
     * @return Value of property formName.
     *
     */
    public String getFormName() {
        return formName;
    }
    
    /** Setter for property formName.
     * @param formName New value of property formName.
     *
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }
    
    /** Getter for property affIdCodeParam.
     * @return Value of property affIdCodeParam.
     *
     */
    public String getAffIdCodeParam() {
        return affIdCodeParam;
    }
    
    /** Setter for property affIdCodeParam.
     * @param affIdCodeParam New value of property affIdCodeParam.
     *
     */
    public void setAffIdCodeParam(String affIdCodeParam) {
        this.affIdCodeParam = affIdCodeParam;
    }
    
    /** Getter for property affIdCouncilParam.
     * @return Value of property affIdCouncilParam.
     *
     */
    public String getAffIdCouncilParam() {
        return affIdCouncilParam;
    }
    
    /** Setter for property affIdCouncilParam.
     * @param affIdCouncilParam New value of property affIdCouncilParam.
     *
     */
    public void setAffIdCouncilParam(String affIdCouncilParam) {
        this.affIdCouncilParam = affIdCouncilParam;
    }
    
    /** Getter for property affIdLocalParam.
     * @return Value of property affIdLocalParam.
     *
     */
    public String getAffIdLocalParam() {
        return affIdLocalParam;
    }
    
    /** Setter for property affIdLocalParam.
     * @param affIdLocalParam New value of property affIdLocalParam.
     *
     */
    public void setAffIdLocalParam(String affIdLocalParam) {
        this.affIdLocalParam = affIdLocalParam;
    }
    
    /** Getter for property affIdStateParam.
     * @return Value of property affIdStateParam.
     *
     */
    public String getAffIdStateParam() {
        return affIdStateParam;
    }
    
    /** Setter for property affIdStateParam.
     * @param affIdStateParam New value of property affIdStateParam.
     *
     */
    public void setAffIdStateParam(String affIdStateParam) {
        this.affIdStateParam = affIdStateParam;
    }
    
    /** Getter for property affIdSubUnitParam.
     * @return Value of property affIdSubUnitParam.
     *
     */
    public String getAffIdSubUnitParam() {
        return affIdSubUnitParam;
    }
    
    /** Setter for property affIdSubUnitParam.
     * @param affIdSubUnitParam New value of property affIdSubUnitParam.
     *
     */
    public void setAffIdSubUnitParam(String affIdSubUnitParam) {
        this.affIdSubUnitParam = affIdSubUnitParam;
    }
    
    /** Getter for property affIdTypeParam.
     * @return Value of property affIdTypeParam.
     *
     */
    public String getAffIdTypeParam() {
        return affIdTypeParam;
    }
    
    /** Setter for property affIdTypeParam.
     * @param affIdTypeParam New value of property affIdTypeParam.
     *
     */
    public void setAffIdTypeParam(String affIdTypeParam) {
        this.affIdTypeParam = affIdTypeParam;
    }
    
    /** Getter for property affPkParam.
     * @return Value of property affPkParam.
     *
     */
    public String getAffPkParam() {
        return affPkParam;
    }
    
    /** Setter for property affPkParam.
     * @param affPkParam New value of property affPkParam.
     *
     */
    public void setAffPkParam(String affPkParam) {
        this.affPkParam = affPkParam;
    }
    
}
