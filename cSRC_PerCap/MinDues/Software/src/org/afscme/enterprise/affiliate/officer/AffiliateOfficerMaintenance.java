package org.afscme.enterprise.affiliate.officer;

import java.io.Serializable;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;


/**
 * Represents elements on the Affiliate Officer Maintenance screen
 */
public class AffiliateOfficerMaintenance implements Serializable {
    
    public AffiliateOfficerMaintenance () {
        super();
    }
    
    // this is radio button on edit page
    private String officerAction;
    
    private Integer officerPersonPk;
    private String officerTitle;
    private Integer monthOfElection;    
    private Integer endTerm;
    private Integer originalEndTerm;
    private boolean suspended;
    private boolean temporaryMember;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer suffix;
    private boolean reportingOfficer;
    private Boolean steward;
    private boolean executiveBoard;
    private AffiliateIdentifier ai;
    private Integer lengthOfTerm;
    private Integer officeGroupPk;    
    private Integer afscmeOfficePk;
    private Integer surrKey;
    private Integer expirationYear;
    private Integer originalExpirationYear;    
    private Integer expirationMonth;
    private Boolean electedOfficerFg;
    private Integer replaceAffPk;
    private Integer replacePersonPk;

    /** Getter for property officerAction.
     * @return Value of property officerAction.
     *
     */
    public java.lang.String getOfficerAction() {
        return officerAction;
    }
    
    /** Setter for property officerAction.
     * @param lastName New value of property officerAction.
     *
     */
    public void setOfficerAction(java.lang.String officerAction) {
        this.officerAction = officerAction;
    }    
    
    
    /** Getter for property officerPersonPk.
     * @return Value of property officerPersonPk.
     *
     */
    public Integer getOfficerPersonPk() {
        return officerPersonPk;
    }    
    
    /** Setter for property officerPersonPk.
     * @param officerPersonPk New value of property officerPersonPk.
     *
     */
    public void setOfficerPersonPk(Integer officerPersonPk) {
        this.officerPersonPk = officerPersonPk;
    }    
    
    /** Getter for property officerTitle.
     * @return Value of property officerTitle.
     *
     */
    public java.lang.String getOfficerTitle() {
        return officerTitle;
    }
    
    /** Setter for property officerTitle.
     * @param officerTitle New value of property officerTitle.
     *
     */
    public void setOfficerTitle(java.lang.String officerTitle) {
        this.officerTitle = officerTitle;
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
    
    
    /** Getter for property endTerm.
     * @return Value of property endTerm.
     *
     */
    public java.lang.Integer getEndTerm() {
        return endTerm;
    }    
    
    /** Setter for property endTerm.
     * @param endTerm New value of property endTerm.
     *
     */
    public void setEndTerm(java.lang.Integer endTerm) {
        this.endTerm = endTerm;
    }
    
    /** Getter for property originalEndTerm.
     * @return Value of property originalEndTerm.
     *
     */
    public java.lang.Integer getOriginalEndTerm() {
        return originalEndTerm;
    }    
    
    /** Setter for property originalEndTerm.
     * @param originalEndTerm New value of property originalEndTerm.
     *
     */
    public void setOriginalEndTerm(java.lang.Integer originalEndTerm) {
        this.originalEndTerm = originalEndTerm;
    }    
    
    /** Getter for property suspended.
     * @return Value of property suspended.
     *
     */
    public boolean isSuspended() {
        return suspended;
    }    
    
    /** Setter for property suspended.
     * @param endTerm New value of property suspended.
     *
     */
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }    
    
    /** Getter for property temporaryMember
     * @return Value of property temporaryMember.
     *
     */
    public boolean isTemporaryMember() {
        return temporaryMember;
    }
    
    /** Setter for property temporaryMember.
     * @param temporaryMember New value of property temporaryMember.
     *
     */
    public void setTemporaryMember(boolean temporaryMember) {
        this.temporaryMember = temporaryMember;
    }    
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     *
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     *
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     *
     */
    public java.lang.String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property middleName.
     * @param firstName New value of property middleName.
     *
     */
    public void setMiddleName(java.lang.String middleName) {
        this.middleName = middleName;
    }    
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     *
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     *
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
                     
    /** Getter for property suffix.
     * @return Value of property suffix.
     *
     */
    public java.lang.Integer getSuffix() {
        return suffix;
    }
    
    /** Setter for property suffix.
     * @param suffix New value of property suffix.
     *
     */
    public void setSuffix(java.lang.Integer suffix) {
        this.suffix = suffix;
    }
    
    /** Getter for property reportingOfficer
     * @return Value of property reportingOfficer.
     *
     */
    public boolean isReportingOfficer() {
        return reportingOfficer;
    }
    
    /** Setter for property reportingOfficer.
     * @param reportingOfficer New value of property reportingOfficer.
     *
     */
    public void setReportingOfficer(boolean reportingOfficer) {
        this.reportingOfficer = reportingOfficer;
    }    
    
    /** Getter for property steward
     * @return Value of property steward.
     *
     */
    public Boolean getSteward() {
        return steward;
    }
    
    /** Setter for property steward.
     * @param stewardFg New value of property steward.
     *
     */
    public void setSteward(Boolean steward) {
        this.steward = steward;
    }
    
    /** Getter for property executiveBoard
     * @return Value of property executiveBoard.
     *
     */
    public boolean isExecutiveBoard() {
        return executiveBoard;
    }
    
    /** Setter for property executiveBoard.
     * @param executiveBoard New value of property executiveBoard.
     *
     */
    public void setExecutiveBoard(boolean executiveBoard) {
        this.executiveBoard = executiveBoard;
    }    
    
    /** Getter for property ai
     * @return Value of property ai.
     *
     */
    public AffiliateIdentifier getAi() {
        return ai;
    }
    
    /** Setter for property ai.
     * @param ai New value of property ai.
     *
     */
    public void setAi(AffiliateIdentifier ai) {
        this.ai = ai;
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
    
    /** Getter for property officeGroupPk.
     * @return Value of property officeGroupPk.
     *
     */
    public java.lang.Integer getOfficeGroupPk() {
        return officeGroupPk;
    }    
    
    /** Setter for property officeGroupPk.
     * @param officeGroupPk New value of property officeGroupPk.
     *
     */
    public void setOfficeGroupPk(java.lang.Integer officeGroupPk) {
        this.officeGroupPk = officeGroupPk;
    }        
    
    /** Getter for property afscmeOfficePk.
     * @return Value of property afscmeOfficePk.
     *
     */
    public java.lang.Integer getAfscmeOfficePk() {
        return afscmeOfficePk;
    }    
    
    /** Setter for property afscmeOfficePk.
     * @param officeGroupPk New value of property afscmeOfficePk.
     *
     */
    public void setAfscmeOfficePk(java.lang.Integer afscmeOfficePk) {
        this.afscmeOfficePk = afscmeOfficePk;
    }        
    
    /** Getter for property surrKey.
     * @return Value of property surrKey.
     *
     */
    public java.lang.Integer getSurrKey() {
        return surrKey;
    }    
    
    /** Setter for property surrKey.
     * @param surrKey New value of property surrKey.
     *
     */
    public void setSurrKey(java.lang.Integer surrKey) {
        this.surrKey = surrKey;
    }               
    
    /** Getter for property expirationYear.
     * @return Value of property expirationYear.
     *
     */
    public java.lang.Integer getExpirationYear() {
        return expirationYear;
    }    
    
    /** Setter for property expirationYear.
     * @param expirationYear New value of property expirationYear.
     *
     */
    public void setExpirationYear(java.lang.Integer expirationYear) {
        this.expirationYear = expirationYear;
    }       
    
    /** Getter for property originalExpirationYear.
     * @return Value of property originalExpirationYear.
     *
     */
    public java.lang.Integer getOriginalExpirationYear() {
        return originalExpirationYear;
    }    
    
    /** Setter for property originalExpirationYear.
     * @param originalExpirationYear New value of property originalExpirationYear.
     *
     */
    public void setOriginalExpirationYear(java.lang.Integer originalExpirationYear) {
        this.originalExpirationYear = originalExpirationYear;
    }           
    
    /** Getter for property expirationMonth.
     * @return Value of property expirationMonth.
     *
     */
    public java.lang.Integer getExpirationMonth() {
        return expirationMonth;
    }    
    
    /** Setter for property expirationMonth.
     * @param expirationMonth New value of property expirationMonth.
     *
     */
    public void setExpirationMonth(java.lang.Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }     
    
    /** Getter for property electedOfficerFg.
     * @return Value of property electedOfficerFg.
     *
     */
    public Boolean getElectedOfficerFg() {
        return electedOfficerFg;
    }    
    
    /** Setter for property electedOfficerFg.
     * @param expirationMonth New value of property electedOfficerFg.
     *
     */
    public void setElectedOfficerFg(Boolean electedOfficerFg) {
        this.electedOfficerFg = electedOfficerFg;
    }  
    
    /**
     * Getter for the full name.
     * @return a string containing LastName, FirstName MiddleName
     */
    public String getFullName()
    {
        StringBuffer retVal = new StringBuffer();
        retVal.append(getLastName());
        retVal.append(", ").append(getFirstName());
        if(getMiddleName() != null && getMiddleName().length() > 0)
            retVal.append(" ").append(getMiddleName());
        return retVal.toString();
    }
      
    
    /** Getter for property replaceAffPk.
     * @return Value of property replaceAffPk.
     *
     */
    public Integer getReplaceAffPk() {
        return replaceAffPk;
    }    
    
    /** Setter for property replaceAffPk.
     * @param replaceAffPk New value of property replaceAffPk.
     *
     */
    public void setReplaceAffPk(Integer replaceAffPk) {
        this.replaceAffPk = replaceAffPk;
    }        
    
    /** Getter for property replacePersonPk.
     * @return Value of property replacePersonPk.
     *
     */
    public Integer getReplacePersonPk() {
        return replacePersonPk;
    }    
    
    /** Setter for property replacePersonPk.
     * @param replacePersonPk New value of property replacePersonPk.
     *
     */
    public void setReplacePersonPk(Integer replacePersonPk) {
        this.replacePersonPk = replacePersonPk;
    }            
    
}
