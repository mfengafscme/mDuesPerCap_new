package org.afscme.enterprise.person;

import java.sql.Timestamp;

/**
 * Contains a person's application data for Political Rebate.
 */
public class PRBApplicationData 
{
    private Integer prbAppPK;
    private Integer personPK;
    private Integer rbtYear;
    private Timestamp appMailedDt;
    private Timestamp appReturnedDt;
    private Integer prbEvaluationCd;
    private Integer prbCommentAnalCd;
    private String commentTxt;
    private Boolean affRosterGeneratedFg;
    
    
    /** Getter for property appMailedDt.
     * @return Value of property appMailedDt.
     *
     */
    public java.sql.Timestamp getAppMailedDt() {
        return appMailedDt;
    }
    
    /** Setter for property appMailedDt.
     * @param appMailedDt New value of property appMailedDt.
     *
     */
    public void setAppMailedDt(java.sql.Timestamp appMailedDt) {
        this.appMailedDt = appMailedDt;
    }
    
    /** Getter for property appReturnedDt.
     * @return Value of property appReturnedDt.
     *
     */
    public java.sql.Timestamp getAppReturnedDt() {
        return appReturnedDt;
    }
    
    /** Setter for property appReturnedDt.
     * @param appReturnedDt New value of property appReturnedDt.
     *
     */
    public void setAppReturnedDt(java.sql.Timestamp appReturnedDt) {
        this.appReturnedDt = appReturnedDt;
    }
    
    /** Getter for property commentTxt.
     * @return Value of property commentTxt.
     *
     */
    public java.lang.String getCommentTxt() {
        return commentTxt;
    }
    
    /** Setter for property commentTxt.
     * @param commentTxt New value of property commentTxt.
     *
     */
    public void setCommentTxt(java.lang.String commentTxt) {
        this.commentTxt = commentTxt;
    }
    
    /** Getter for property personPK.
     * @return Value of property personPK.
     *
     */
    public java.lang.Integer getPersonPK() {
        return personPK;
    }
    
    /** Setter for property personPK.
     * @param personPK New value of property personPK.
     *
     */
    public void setPersonPK(java.lang.Integer personPK) {
        this.personPK = personPK;
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
    
    /** Getter for property prbCommentAnalCd.
     * @return Value of property prbCommentAnalCd.
     *
     */
    public java.lang.Integer getPrbCommentAnalCd() {
        return prbCommentAnalCd;
    }
    
    /** Setter for property prbCommentAnalCd.
     * @param prbCommentAnalCd New value of property prbCommentAnalCd.
     *
     */
    public void setPrbCommentAnalCd(java.lang.Integer prbCommentAnalCd) {
        this.prbCommentAnalCd = prbCommentAnalCd;
    }
    
    /** Getter for property prbEvaluationCd.
     * @return Value of property prbEvaluationCd.
     *
     */
    public java.lang.Integer getPrbEvaluationCd() {
        return prbEvaluationCd;
    }
    
    /** Setter for property prbEvaluationCd.
     * @param prbEvaluationCd New value of property prbEvaluationCd.
     *
     */
    public void setPrbEvaluationCd(java.lang.Integer prbEvaluationCd) {
        this.prbEvaluationCd = prbEvaluationCd;
    }
    
    /** Getter for property rbtYear.
     * @return Value of property rbtYear.
     *
     */
    public java.lang.Integer getRbtYear() {
        return rbtYear;
    }
    
    /** Setter for property rbtYear.
     * @param rbtYear New value of property rbtYear.
     *
     */
    public void setRbtYear(java.lang.Integer rbtYear) {
        this.rbtYear = rbtYear;
    }    
    
    /** Getter for property affRosterGeneratedFg.
     * @return Value of property affRosterGeneratedFg.
     *
     */
    public java.lang.Boolean getAffRosterGeneratedFg() {
        return affRosterGeneratedFg;
    }
    
    /** Setter for property affRosterGeneratedFg.
     * @param affRosterGeneratedFg New value of property affRosterGeneratedFg.
     *
     */
    public void setAffRosterGeneratedFg(java.lang.Boolean affRosterGeneratedFg) {
        this.affRosterGeneratedFg = affRosterGeneratedFg;
    }
    
}
