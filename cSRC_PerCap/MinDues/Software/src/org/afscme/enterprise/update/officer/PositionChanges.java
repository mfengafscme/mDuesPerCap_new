package org.afscme.enterprise.update.officer;

import java.sql.Timestamp;

/**
 * Encapsulates the officer position changes made during an officer update
 */
public class PositionChanges {
    
    /**
     * number allowed
     */
    protected int allowed;
    
    /**
     * count of changes in the file
     */
    protected int inFile;
    
    /**
     * count of changes in the file
     */
    protected int inSystem;
    
    
    protected int removeVacate;
    
    /**
     * expiration date of the position change
     */
    protected Timestamp expirationDate;
    /**********************************************************************************/
    //included in here for safe use in future
    /**
     * expiration date of the position change
     */
    protected String expDate;
    /***********************************************************************************/
    /**
     * primary key of the affiliate
     */
    protected Integer affPk;
    
    /***********************************************************************************/
    /**
     * primary key of the officeGroups
     */
    protected Integer officePk;
    /***********************************************************************************/
    /**
     * primary key of the officeGroups
     */
    protected Integer groupId;
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    
    protected Integer personPk;

    public PositionChanges(){
        this.inSystem = 0;
        this.inFile = 0;
        this.removeVacate = 0;
    }
    
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
    
    /** Getter for property allowed.
     * @return Value of property allowed.
     *
     */
    public int getAllowed() {
        return allowed;
    }
    
    /** Setter for property allowed.
     * @param allowed New value of property allowed.
     *
     */
    public void setAllowed(int allowed) {
        this.allowed = allowed;
    }
    
    public void incrementAllowed() {
        this.allowed += 1;
    }
    
    public void decrementAllowed() {
        this.allowed -= 1;
    }
    
    /** Getter for property expirationDate.
     * @return Value of property expirationDate.
     *
     */
    public Timestamp getExpirationDate() {
        return expirationDate;
    }
    
    /** Setter for property expirationDate.
     * @param expirationDate New value of property expirationDate.
     *
     */
    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }
    /****************************************************************************************/
    //Included in here as most of the methods of timestamp class are deprecated and to make
    //it safe for the future use
    //***************************************************************************************
    /** Getter for property Date.
     * @return Value of property Date.
     *
     */
    public String getDate() {
        return expDate;
    }
    
    /** Setter for property Date.
     * @param eDate New value of property Date.
     *
     */
    public void setDate(String eDate) {
        this.expDate = eDate;
    }
    //*****************************************************************************************
    /** Getter for property inFile.
     * @return Value of property inFile.
     *
     */
    public int getInFile() {
        return inFile;
    }
    
    /** Setter for property inFile.
     * @param inFile New value of property inFile.
     *
     */
    public void setInFile(int inFile) {
        this.inFile = inFile;
    }
    
    public void incrementInFile() {
        this.inFile += 1;
    }
    
    public void decrementInFile() {
        this.inFile -= 1;
    }
    /**************************************************************************************************/
    /** Getter for property OfficePk.
     * @return Value of property OfficePk.
     *
     */
    public Integer getOfficePk() {
        return officePk;
    }
    
    /** Setter for property OfficePk.
     * @param OfficePk New value of property OfficePk.
     *
     */
    public void setOfficePk(Integer officePk) {
        this.officePk = officePk;
    }
    
    /** Getter for property GroupId.
     * @return Value of property GroupId.
     *
     */
    public Integer getGroupId() {
        return groupId;
    }
    
    /** Setter for property GroupId.
     * @param GroupId New value of property GroupId.
     *
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    
    
    public Integer getPersonPk(){
        return personPk;
    }
    
    public void setPersonPk(Integer pk){
        personPk = pk;
    }
    
    public int getInSystem(){
        return inSystem;
    }
    
    public void setInSystem(int is){
        inSystem = is;        
    }
    
    public int getRemoveVacate(){
        return removeVacate;
    }
    
    public void setRemoveVacate(int rv){
        removeVacate = rv;        
    }    
}
