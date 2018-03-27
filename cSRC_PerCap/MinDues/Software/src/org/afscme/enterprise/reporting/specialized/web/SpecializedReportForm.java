package org.afscme.enterprise.reporting.specialized.web;

import javax.naming.NamingException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.reporting.base.generator.OutputFormat;

/**
 * @struts:form name="specializedReportForm"
 */
public class SpecializedReportForm extends ActionForm {
    /*-------------   Static Data ------------------------------------*/
    public static final String TAB = "Tab";
    public static final String COMMA = "Comma";
    public static final String SEMICOLON = "Semicolon";
    /*-----------------------------------------------------------------*/

    private Integer affPk = null;    
    protected String reportName = null;
    private String outputFormat = TAB;        
    
    /** Getter for property outputFormat.
     * @return Value of property outputFormat.
     */
    public java.lang.String getOutputFormat() {
        return outputFormat;
    }
    
    /** Setter for property outputFormat.
     * @param outputFormat New value of property outputFormat.
     */
    public void setOutputFormat(java.lang.String outputFormat) {
        this.outputFormat = outputFormat;
    }
                            
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public java.lang.Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(java.lang.Integer affPk) {
        this.affPk = affPk;
    }    
    
    /** Getter for property reportName.
     * @return Value of property reportName.
     *
     */
    public java.lang.String getReportName() {
        return reportName;
    }    

    /** Setter for property reportName.
     * @param reportName New value of property reportName.
     *
     */
    public void setReportName(java.lang.String reportName) {
        this.reportName = reportName;
    }
}
