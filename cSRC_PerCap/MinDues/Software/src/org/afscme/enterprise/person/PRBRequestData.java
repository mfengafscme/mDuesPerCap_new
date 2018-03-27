package org.afscme.enterprise.person;

import java.sql.Timestamp;

/**
 * This class represents the entire data about a person's political rebate request 
 * and serves as both a result row as well as detail (i.e. there is not separate 
 * result object)
 */
public class PRBRequestData 
{
    private int rqstPk;
    private int personPk;
    private Timestamp rqstDt;
    private String rqstRebateYear;
    private String rqstCertMailNum;
    private Boolean rqstDeniedFg;
    private Integer rqstDeniedReason;
    private Timestamp rqstDeniedDt;
    private Timestamp rqstKeyedDt;
    private Boolean rqstResubmitFg;
    private Timestamp rqstResubmitDeniedDt;
    private Integer rqstResubmitDeniedReason;
    private Integer rqstStatus;
    private String commentsTxt;
    private Integer prbAppPK;
    
    /** Getter for property commentsTxt.
     * @return Value of property commentsTxt.
     *
     */
    public java.lang.String getCommentsTxt() {
        return commentsTxt;
    }
    
    /** Setter for property commentsTxt.
     * @param commentsTxt New value of property commentsTxt.
     *
     */
    public void setCommentsTxt(java.lang.String commentsTxt) {
        this.commentsTxt = commentsTxt;
    }
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public int getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(int personPk) {
        this.personPk = personPk;
    }
    
    /** Getter for property prbAppPK.
     * @return Value of property prbAppPK.
     *
     */
    public java.lang.Integer getPrbAppPK() {
        return prbAppPK;
    }
    
    /** Setter for property prbAppPK.
     * @param prbAppPK New value of property prbAppPK.
     *
     */
    public void setPrbAppPK(java.lang.Integer prbAppPK) {
        this.prbAppPK = prbAppPK;
    }
    
    /** Getter for property rqstCertMailNum.
     * @return Value of property rqstCertMailNum.
     *
     */
    public java.lang.String getRqstCertMailNum() {
        return rqstCertMailNum;
    }
    
    /** Setter for property rqstCertMailNum.
     * @param rqstCertMailNum New value of property rqstCertMailNum.
     *
     */
    public void setRqstCertMailNum(java.lang.String rqstCertMailNum) {
        this.rqstCertMailNum = rqstCertMailNum;
    }
    
    /** Getter for property rqstDeniedDt.
     * @return Value of property rqstDeniedDt.
     *
     */
    public java.sql.Timestamp getRqstDeniedDt() {
        return rqstDeniedDt;
    }
    
    /** Setter for property rqstDeniedDt.
     * @param rqstDeniedDt New value of property rqstDeniedDt.
     *
     */
    public void setRqstDeniedDt(java.sql.Timestamp rqstDeniedDt) {
        this.rqstDeniedDt = rqstDeniedDt;
    }
    
    /** Getter for property rqstDeniedFg.
     * @return Value of property rqstDeniedFg.
     *
     */
    public java.lang.Boolean getRqstDeniedFg() {
        return rqstDeniedFg;
    }
    
    /** Setter for property rqstDeniedFg.
     * @param rqstDeniedFg New value of property rqstDeniedFg.
     *
     */
    public void setRqstDeniedFg(java.lang.Boolean rqstDeniedFg) {
        this.rqstDeniedFg = rqstDeniedFg;
    }
    
    /** Getter for property rqstDeniedReason.
     * @return Value of property rqstDeniedReason.
     *
     */
    public Integer getRqstDeniedReason() {
        return rqstDeniedReason;
    }
    
    /** Setter for property rqstDeniedReason.
     * @param rqstDeniedReason New value of property rqstDeniedReason.
     *
     */
    public void setRqstDeniedReason(Integer rqstDeniedReason) {
        this.rqstDeniedReason = rqstDeniedReason;
    }
    
    /** Getter for property rqstDt.
     * @return Value of property rqstDt.
     *
     */
    public java.sql.Timestamp getRqstDt() {
        return rqstDt;
    }
    
    /** Setter for property rqstDt.
     * @param rqstDt New value of property rqstDt.
     *
     */
    public void setRqstDt(java.sql.Timestamp rqstDt) {
        this.rqstDt = rqstDt;
    }
    
    /** Getter for property rqstKeyedDt.
     * @return Value of property rqstKeyedDt.
     *
     */
    public java.sql.Timestamp getRqstKeyedDt() {
        return rqstKeyedDt;
    }
    
    /** Setter for property rqstKeyedDt.
     * @param rqstKeyedDt New value of property rqstKeyedDt.
     *
     */
    public void setRqstKeyedDt(java.sql.Timestamp rqstKeyedDt) {
        this.rqstKeyedDt = rqstKeyedDt;
    }
    
    /** Getter for property rqstRebateYear.
     * @return Value of property rqstRebateYear.
     *
     */
    public java.lang.String getRqstRebateYear() {
        return rqstRebateYear;
    }
    
    /** Setter for property rqstRebateYear.
     * @param rqstRebateYear New value of property rqstRebateYear.
     *
     */
    public void setRqstRebateYear(java.lang.String rqstRebateYear) {
        this.rqstRebateYear = rqstRebateYear;
    }
    
    /** Getter for property rqstResubmitDeniedDt.
     * @return Value of property rqstResubmitDeniedDt.
     *
     */
    public java.sql.Timestamp getRqstResubmitDeniedDt() {
        return rqstResubmitDeniedDt;
    }
    
    /** Setter for property rqstResubmitDeniedDt.
     * @param rqstResubmitDeniedDt New value of property rqstResubmitDeniedDt.
     *
     */
    public void setRqstResubmitDeniedDt(java.sql.Timestamp rqstResubmitDeniedDt) {
        this.rqstResubmitDeniedDt = rqstResubmitDeniedDt;
    }
    
    /** Getter for property rqstResubmitDeniedReason.
     * @return Value of property rqstResubmitDeniedReason.
     *
     */
    public java.lang.Integer getRqstResubmitDeniedReason() {
        return rqstResubmitDeniedReason;
    }
    
    /** Setter for property rqstResubmitDeniedReason.
     * @param rqstResubmitDeniedReason New value of property rqstResubmitDeniedReason.
     *
     */
    public void setRqstResubmitDeniedReason(java.lang.Integer rqstResubmitDeniedReason) {
        this.rqstResubmitDeniedReason = rqstResubmitDeniedReason;
    }
    
    /** Getter for property rqstResubmitFg.
     * @return Value of property rqstResubmitFg.
     *
     */
    public java.lang.Boolean getRqstResubmitFg() {
        return rqstResubmitFg;
    }
    
    /** Setter for property rqstResubmitFg.
     * @param rqstResubmitFg New value of property rqstResubmitFg.
     *
     */
    public void setRqstResubmitFg(java.lang.Boolean rqstResubmitFg) {
        this.rqstResubmitFg = rqstResubmitFg;
    }
    
    /** Getter for property rqstStatus.
     * @return Value of property rqstStatus.
     *
     */
    public java.lang.Integer getRqstStatus() {
        return rqstStatus;
    }
    
    /** Setter for property rqstStatus.
     * @param rqstStatus New value of property rqstStatus.
     *
     */
    public void setRqstStatus(java.lang.Integer rqstStatus) {
        this.rqstStatus = rqstStatus;
    }
    
    /** Getter for property rqstPk.
     * @return Value of property rqstPk.
     *
     */
    public int getRqstPk() {
        return rqstPk;
    }
    
    /** Setter for property rqstPk.
     * @param rqstPk New value of property rqstPk.
     *
     */
    public void setRqstPk(int rqstPk) {
        this.rqstPk = rqstPk;
    }
    
}
