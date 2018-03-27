package org.afscme.enterprise.affiliate.officer;

import org.afscme.enterprise.common.RecordData;

/**
 * Represents a single office that may be associated with an affiliate.  e.g. Vice 
 * President, elected in January, re-elected every 2 years.
 */
public class OfficeData extends AFSCMETitleData { 
    
    public static final int SORT_FIELD_NONE                = 0;
    public static final int SORT_FIELD_CONSTITUTIONALTITLE = 1;
    public static final int SORT_FIELD_AFFILIATETITLE      = 2;
    public static final int SORT_FIELD_NUMWITHTITLE        = 3;
    public static final int SORT_FIELD_MONTHOFELECTION     = 4;
    public static final int SORT_FIELD_LENGTHOFTERM        = 5;
    public static final int SORT_FIELD_TERMEND             = 6;
    public static final int SORT_FIELD_DELEGATEPRIORITY    = 7;
    public static final int SORT_FIELD_RO                  = 8;
    public static final int SORT_FIELD_EBOARD              = 9; 
    public static final int SORT_FIELD_PRIORITY            = 10;
    
    protected Integer priority;
    protected Integer affPk;
    protected Integer officePk;
    protected Integer officeGroupID;
    protected Integer afscmeTitle;
    protected String  affiliateTitle;
    protected Integer numWithTitle;
    protected Integer monthOfElection;
    protected Integer lengthOfTerm;
    protected Integer termEnd;
    protected Integer delegatePriority;
    protected Boolean reportingOfficer;    
    protected Boolean execBoard;
    protected RecordData theRecordData;
    
    public static final int sortStringToCode(String sortBy) {
        if (sortBy == null)
            return OfficeData.SORT_FIELD_NONE;
        else if (sortBy.equals("constitutionalTitle"))
            return OfficeData.SORT_FIELD_CONSTITUTIONALTITLE;
        else if (sortBy.equals("affiliateTitle"))
            return OfficeData.SORT_FIELD_AFFILIATETITLE;
        else if (sortBy.equals("numWithTitle"))
            return OfficeData.SORT_FIELD_NUMWITHTITLE;
        else if (sortBy.equals("monthOfElection"))
            return OfficeData.SORT_FIELD_MONTHOFELECTION;
        else if (sortBy.equals("lenghtOfTerm"))
            return OfficeData.SORT_FIELD_LENGTHOFTERM;
        else if (sortBy.equals("termEnd"))
            return OfficeData.SORT_FIELD_TERMEND;
        else if (sortBy.equals("delegatePriority"))
            return OfficeData.SORT_FIELD_DELEGATEPRIORITY;
        else if (sortBy.equals("reportingOfficer"))
            return OfficeData.SORT_FIELD_RO;
        else if (sortBy.equals("eboard"))
            return OfficeData.SORT_FIELD_EBOARD;
        else if (sortBy.equals("priority"))
            return OfficeData.SORT_FIELD_PRIORITY;
        else 
            throw new RuntimeException("Invalid sort field '" + sortBy + "'");
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
    
    /** Getter for property officePk.
     * @return Value of property officePk.
     *
     */
    public java.lang.Integer getOfficePk() {
        return officePk;
    }
    
    /** Setter for property officePk.
     * @param officePk New value of property officePk.
     *
     */
    public void setOfficePk(java.lang.Integer officePk) {
        this.officePk = officePk;
    }
    
    /** Getter for property officeGroupID.
     * @return Value of property officeGroupID.
     *
     */
    public java.lang.Integer getOfficeGroupID() {
        return officeGroupID;
    }
    
    /** Setter for property officeGroupID.
     * @param officeGroupID New value of property officeGroupID.
     *
     */
    public void setOfficeGroupID(java.lang.Integer officeGroupID) {
        this.officeGroupID = officeGroupID;
    }
    
    /** Getter for property afscmeTitle.
     * @return Value of property afscmeTitle.
     *
     */
    public java.lang.Integer getAfscmeTitle() {
        return afscmeTitle;
    }
    
    /** Setter for property afscmeTitle.
     * @param afscmeTitle New value of property afscmeTitle.
     *
     */
    public void setAfscmeTitle(java.lang.Integer afscmeTitle) {
        this.afscmeTitle = afscmeTitle;
    }
    
    /** Getter for property affiliateTitle.
     * @return Value of property affiliateTitle.
     *
     */
    public java.lang.String getAffiliateTitle() {
        return affiliateTitle;
    }
    
    /** Setter for property affiliateTitle.
     * @param affiliateTitle New value of property affiliateTitle.
     *
     */
    public void setAffiliateTitle(java.lang.String affiliateTitle) {
        this.affiliateTitle = affiliateTitle;
    }
    
    /** Getter for property numWithTitle.
     * @return Value of property numWithTitle.
     *
     */
    public java.lang.Integer getNumWithTitle() {
        return numWithTitle;
    }
    
    /** Setter for property numWithTitle.
     * @param numWithTitle New value of property numWithTitle.
     *
     */
    public void setNumWithTitle(java.lang.Integer numWithTitle) {
        this.numWithTitle = numWithTitle;
    }
    
    /** Getter for property monthOfElection.
     * @return Value of property monthOfElection.
     *
     */
    public java.lang.Integer getMonthOfElection() {
        return monthOfElection;
    }
    
    /** Setter for property monthOfElection.
     * @param monthOfElection New value of property monthOfElection.
     *
     */
    public void setMonthOfElection(java.lang.Integer monthOfElection) {
        this.monthOfElection = monthOfElection;
    }
    
    /** Getter for property lengthOfTerm.
     * @return Value of property lengthOfTerm.
     *
     */
    public java.lang.Integer getLengthOfTerm() {
        return lengthOfTerm;
    }
    
    /** Setter for property lengthOfTerm.
     * @param lengthOfTerm New value of property lengthOfTerm.
     *
     */
    public void setLengthOfTerm(java.lang.Integer lengthOfTerm) {
        this.lengthOfTerm = lengthOfTerm;
    }
    
    /** Getter for property termEnd.
     * @return Value of property termEnd.
     *
     */
    public java.lang.Integer getTermEnd() {
        return termEnd;
    }
    
    /** Setter for property termEnd.
     * @param termEnd New value of property termEnd.
     *
     */
    public void setTermEnd(java.lang.Integer termEnd) {
        this.termEnd = termEnd;
    }
    
    /** Getter for property delegatePriority.
     * @return Value of property delegatePriority.
     *
     */
    public java.lang.Integer getDelegatePriority() {
        return delegatePriority;
    }
    
    /** Setter for property delegatePriority.
     * @param delegatePriority New value of property delegatePriority.
     *
     */
    public void setDelegatePriority(java.lang.Integer delegatePriority) {
        this.delegatePriority = delegatePriority;
    }
    
    /** Getter for property reportingOfficer.
     * @return Value of property reportingOfficer.
     *
     */
    public java.lang.Boolean getReportingOfficer() {
        return reportingOfficer;
    }
    
    /** Setter for property reportingOfficer.
     * @param reportingOfficer New value of property reportingOfficer.
     *
     */
    public void setReportingOfficer(java.lang.Boolean reportingOfficer) {
        this.reportingOfficer = reportingOfficer;
    }
    
    /** Getter for property execBoard.
     * @return Value of property execBoard.
     *
     */
    public java.lang.Boolean getExecBoard() {
        return execBoard;
    }
    
    /** Setter for property execBoard.
     * @param execBoard New value of property execBoard.
     *
     */
    public void setExecBoard(java.lang.Boolean execBoard) {
        this.execBoard = execBoard;
    }
    
    /** Getter for property theRecordData.
     * @return Value of property theRecordData.
     *
     */
    public org.afscme.enterprise.common.RecordData getTheRecordData() {
        return theRecordData;
    }
    
    /** Setter for property theRecordData.
     * @param theRecordData New value of property theRecordData.
     *
     */
    public void setTheRecordData(org.afscme.enterprise.common.RecordData theRecordData) {
        this.theRecordData = theRecordData;
    }
    
    /** Getter for property priority.
     * @return Value of property priority.
     *
     */
    public java.lang.Integer getPriority() {
        return priority;
    }
    
    /** Setter for property priority.
     * @param priority New value of property priority.
     *
     */
    public void setPriority(java.lang.Integer priority) {
        this.priority = priority;
    }
    
}
