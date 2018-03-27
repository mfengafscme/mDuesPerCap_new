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
import org.afscme.enterprise.person.PRBCheckInfo;


/** Holds the data on the Political Rebate Annual Rebate Information screen
 * @struts:form name="politicalRebateAnnualRebateInformationForm"
 */
public class PoliticalRebateAnnualRebateInformationForm extends ActionForm {
    
    private Integer prbYear;
    private Integer affPk;
    private String prbRosterStatus;
    private List prbDuesPaidList;
    private PRBCheckInfo prbCheckInfo;
    
    
    public String toString() {
        return 
        "Rebate Year: " + prbYear +
        "Roster Status: " + prbRosterStatus +
        "Dues Paid List: " + prbDuesPaidList +
        "Check Information: " + prbCheckInfo;
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
            
    /** Getter for property prbRosterStatus.
     * @return Value of property prbRosterStatus.
     *
     */
    public java.lang.String getPrbRosterStatus() {
        return prbRosterStatus;
    }
    
    /** Setter for property prbRosterStatus.
     * @param prbRosterStatus New value of property prbRosterStatus.
     *
     */
    public void setPrbRosterStatus(java.lang.String prbRosterStatus) {
        this.prbRosterStatus = prbRosterStatus;
    }
    
    /** Getter for property prbCheckInfo.
     * @return Value of property prbCheckInfo.
     *
     */
    public org.afscme.enterprise.person.PRBCheckInfo getPrbCheckInfo() {
        return prbCheckInfo;
    }
    
    /** Setter for property prbCheckInfo.
     * @param prbCheckInfo New value of property prbCheckInfo.
     *
     */
    public void setPrbCheckInfo(org.afscme.enterprise.person.PRBCheckInfo prbCheckInfo) {
        this.prbCheckInfo = prbCheckInfo;
    }    
    
    /** Getter for property prbDuesPaidList.
     * @return Value of property prbDuesPaidList.
     *
     */
    public java.util.List getPrbDuesPaidList() {
        return prbDuesPaidList;
    }
    
    /** Setter for property prbDuesPaidList.
     * @param prbDuesPaidList New value of property prbDuesPaidList.
     *
     */
    public void setPrbDuesPaidList(java.util.List prbDuesPaidList) {
        this.prbDuesPaidList = prbDuesPaidList;
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
    
}


