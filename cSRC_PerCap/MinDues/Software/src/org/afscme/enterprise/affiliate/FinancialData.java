package org.afscme.enterprise.affiliate;

import java.sql.Timestamp;

/**
 * Affiliate financial data
 */
public class FinancialData {
    private Integer affPk;
    private String employerIDNumber;
    private Integer perCapitaStatAvg;
    private String perCapitaTaxPaymentMethod;
    private Timestamp perCapitaTaxLastPaidDate;
    private Integer perCapitaTaxLastMemberCount;
    private Timestamp perCapitaTaxInfoLastUpdateDate;
    private String comment;
    
// Getter and Setter Methods...
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property comment.
     * @return Value of property comment.
     *
     */
    public String getComment() {
        return comment;
    }
    
    /** Setter for property comment.
     * @param comment New value of property comment.
     *
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /** Getter for property employerIDNumber.
     * @return Value of property employerIDNumber.
     *
     */
    public String getEmployerIDNumber() {
        return employerIDNumber;
    }
    
    /** Setter for property employerIDNumber.
     * @param employerIDNumber New value of property employerIDNumber.
     *
     */
    public void setEmployerIDNumber(String employerIDNumber) {
        this.employerIDNumber = employerIDNumber;
    }
    
    /** Getter for property perCapitaStatAvg.
     * @return Value of property perCapitaStatAvg.
     *
     */
    public Integer getPerCapitaStatAvg() {
        return perCapitaStatAvg;
    }
    
    /** Setter for property perCapitaStatAvg.
     * @param perCapitaStatAvg New value of property perCapitaStatAvg.
     *
     */
    public void setPerCapitaStatAvg(Integer perCapitaStatAvg) {
        this.perCapitaStatAvg = perCapitaStatAvg;
    }
    
    /** Getter for property perCapitaTaxInfoLastUpdateDate.
     * @return Value of property perCapitaTaxInfoLastUpdateDate.
     *
     */
    public Timestamp getPerCapitaTaxInfoLastUpdateDate() {
        return perCapitaTaxInfoLastUpdateDate;
    }
    
    /** Setter for property perCapitaTaxInfoLastUpdateDate.
     * @param perCapitaTaxInfoLastUpdateDate New value of property perCapitaTaxInfoLastUpdateDate.
     *
     */
    public void setPerCapitaTaxInfoLastUpdateDate(Timestamp perCapitaTaxInfoLastUpdateDate) {
        this.perCapitaTaxInfoLastUpdateDate = perCapitaTaxInfoLastUpdateDate;
    }
    
    /** Getter for property perCapitaTaxLastMemberCount.
     * @return Value of property perCapitaTaxLastMemberCount.
     *
     */
    public Integer getPerCapitaTaxLastMemberCount() {
        return perCapitaTaxLastMemberCount;
    }
    
    /** Setter for property perCapitaTaxLastMemberCount.
     * @param perCapitaTaxLastMemberCount New value of property perCapitaTaxLastMemberCount.
     *
     */
    public void setPerCapitaTaxLastMemberCount(Integer perCapitaTaxLastMemberCount) {
        this.perCapitaTaxLastMemberCount = perCapitaTaxLastMemberCount;
    }
    
    /** Getter for property perCapitaTaxLastPaidDate.
     * @return Value of property perCapitaTaxLastPaidDate.
     *
     */
    public Timestamp getPerCapitaTaxLastPaidDate() {
        return perCapitaTaxLastPaidDate;
    }
    
    /** Setter for property perCapitaTaxLastPaidDate.
     * @param perCapitaTaxLastPaidDate New value of property perCapitaTaxLastPaidDate.
     *
     */
    public void setPerCapitaTaxLastPaidDate(Timestamp perCapitaTaxLastPaidDate) {
        this.perCapitaTaxLastPaidDate = perCapitaTaxLastPaidDate;
    }
    
    /** Getter for property perCapitaTaxPaymentMethod.
     * @return Value of property perCapitaTaxPaymentMethod.
     *
     */
    public String getPerCapitaTaxPaymentMethod() {
        return perCapitaTaxPaymentMethod;
    }
    
    /** Setter for property perCapitaTaxPaymentMethod.
     * @param perCapitaTaxPaymentMethod New value of property perCapitaTaxPaymentMethod.
     *
     */
    public void setPerCapitaTaxPaymentMethod(String perCapitaTaxPaymentMethod) {
        this.perCapitaTaxPaymentMethod = perCapitaTaxPaymentMethod;
    }
    
}
