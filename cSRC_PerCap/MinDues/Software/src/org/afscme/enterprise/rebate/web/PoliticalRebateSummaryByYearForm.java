package org.afscme.enterprise.rebate.web;

import java.util.List;
import java.util.Calendar;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.util.TextUtil;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;

/** Holds the data on the Political Rebate Summary By Year screen
 * @struts:form name="politicalRebateSummaryByYearForm"
 */
public class PoliticalRebateSummaryByYearForm extends ActionForm {
    
    private List prbSummaryByYear;
    private String originateFrom;
    
    public String toString() {
        return 
        "Political Rebate Summary By Year List: " + prbSummaryByYear +
        "Originate From: " + originateFrom;
    }
    
    /** Getter for property prbSummaryByYear.
     * @return Value of property prbSummaryByYear.
     *
     */
    public java.util.List getPrbSummaryByYear() {
        return prbSummaryByYear;
    }    

    /** Setter for property prbSummaryByYear.
     * @param prbSummaryByYear New value of property prbSummaryByYear.
     *
     */
    public void setPrbSummaryByYear(java.util.List prbSummaryByYear) {
        this.prbSummaryByYear = prbSummaryByYear;
    }
    
    
    public boolean originateFromPerson() {
        return getOriginateFrom().equalsIgnoreCase("person") ? true : false;
    }
    
    /** Getter for property originateFrom.
     * @return Value of property originateFrom.
     *
     */
    public java.lang.String getOriginateFrom() {
        return originateFrom;
    }
    
    /** Setter for property originateFrom.
     * @param originateFrom New value of property originateFrom.
     *
     */
    public void setOriginateFrom(java.lang.String originateFrom) {
        this.originateFrom = originateFrom;
    }
    
}


