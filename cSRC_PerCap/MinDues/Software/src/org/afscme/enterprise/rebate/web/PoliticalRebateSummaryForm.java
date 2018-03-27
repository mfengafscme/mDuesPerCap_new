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

/** Holds the data on the Political Rebate Summary screen
 * @struts:form name="politicalRebateSummaryForm"
 */
public class PoliticalRebateSummaryForm extends ActionForm {
    
    private Integer prbYear;
    private List prbRequestList;
    private List prbApplicationList;
    private String prbAnnualInfoStatus;
    
    public String toString() {
        return 
        "Rebate Year: " + prbYear +
        "Rebate Request List: " + prbRequestList +
        "Rebate Application List: " + prbApplicationList +
        "Annual Rebate Information Status: " + prbAnnualInfoStatus;
    }
    
    /** Getter for property prbAnnualInfoStatus.
     * @return Value of property prbAnnualInfoStatus.
     *
     */
    public java.lang.String getPrbAnnualInfoStatus() {
        return prbAnnualInfoStatus;
    }
    
    /** Setter for property prbAnnualInfoStatus.
     * @param prbAnnualInfoStatus New value of property prbAnnualInfoStatus.
     *
     */
    public void setPrbAnnualInfoStatus(java.lang.String prbAnnualInfoStatus) {
        this.prbAnnualInfoStatus = prbAnnualInfoStatus;
    }
    
    /** Getter for property prbApplicationList.
     * @return Value of property prbApplicationList.
     *
     */
    public java.util.List getPrbApplicationList() {
        return prbApplicationList;
    }
    
    /** Setter for property prbApplicationList.
     * @param prbApplicationList New value of property prbApplicationList.
     *
     */
    public void setPrbApplicationList(java.util.List prbApplicationList) {
        this.prbApplicationList = prbApplicationList;
    }
    
    /** Getter for property prbRequestList.
     * @return Value of property prbRequestList.
     *
     */
    public java.util.List getPrbRequestList() {
        return prbRequestList;
    }
    
    /** Setter for property prbRequestList.
     * @param prbRequestList New value of property prbRequestList.
     *
     */
    public void setPrbRequestList(java.util.List prbRequestList) {
        this.prbRequestList = prbRequestList;
    }
    
    /** Getter for property prbYear.
     * @return Value of property prbYear.
     *
     */
    public java.lang.Integer getPrbYear() {
        return prbYear;
    }
    
    /** Setter for property prbYear.
     * @param prbYear New value of property prbYear.
     *
     */
    public void setPrbYear(java.lang.Integer prbYear) {
        this.prbYear = prbYear;
    }
    
}


