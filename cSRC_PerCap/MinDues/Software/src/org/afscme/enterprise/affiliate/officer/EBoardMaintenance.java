/*
 * EBoardMaintenance.java
 *
 * Created on November 25, 2003, 2:29 PM
 */

package org.afscme.enterprise.affiliate.officer;

import java.io.Serializable;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;


/**
 * Represents elements on the Affiliate Officer Maintenance screen
 */
public class EBoardMaintenance implements Serializable {
    
    public EBoardMaintenance () {
        super();
    }
        
    private Integer officerPersonPk;
    private String officerTitle;    
    private String subAffiliateTitle;    
    private Integer endTerm;
    private Integer monthOfElection;
    private boolean suspended;
    private boolean temporaryMember;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer suffix;
    private boolean reportingOfficer;
    private boolean steward;    
    private AffiliateIdentifier ai;
    
 
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
    
    
    /** Getter for property subAffiliateTitle.
     * @return Value of property subAffiliateTitle.
     *
     */
    public java.lang.String getSubAffiliateTitle() {
        return subAffiliateTitle;
    }
    
    /** Setter for property subAffiliateTitle.
     * @param officerTitle New value of property subAffiliateTitle.
     *
     */
    public void setSubAffiliateTitle(java.lang.String subAffiliateTitle) {
        this.subAffiliateTitle = subAffiliateTitle;
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
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     *
     */
    public java.lang.String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property middleName.
     * @param middleName New value of property middleName.
     *
     */
    public void setMiddleName(java.lang.String middleName) {
        this.middleName = middleName;
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
    public boolean isSteward() {
        return steward;
    }
    
    /** Setter for property steward.
     * @param stewardFg New value of property steward.
     *
     */
    public void setSteward(boolean steward) {
        this.steward = steward;
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
          
    
}
