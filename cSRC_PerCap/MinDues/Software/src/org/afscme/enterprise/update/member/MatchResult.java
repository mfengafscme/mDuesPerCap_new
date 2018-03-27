package org.afscme.enterprise.update.member;


/**
 * Encapsulates the result of a match operation, performed by 
 * UpdateBean.matchXXX().
 */
public class MatchResult {
    
    /**
     * The person pk of the member that was found, or null of none.
     */
    protected Integer personPk;
    
    /**
     * Set to true if a mach was found and member data is different than the given 
     * data.
     */
    protected boolean changePerson;
    
    /**
     * Set to true if a mach was found and address data is different than the given 
     * data.
     */
    protected boolean changedAdress;
    
    /**
     * Set to true if a match was found, the existing record is 'A', and the new data 
     * is 'I' or 'T'
     */
    protected boolean inactivated;
    
    /**
     * True iff the record's rebate status changed.
     */
    protected boolean rebateStatusChanged;
    
    /** Getter for property changedAdress.
     * @return Value of property changedAdress.
     *
     */
    public boolean isChangedAdress() {
        return changedAdress;
    }
    
    /** Setter for property changedAdress.
     * @param changedAdress New value of property changedAdress.
     *
     */
    public void setChangedAdress(boolean changedAdress) {
        this.changedAdress = changedAdress;
    }
    
    /** Getter for property changePerson.
     * @return Value of property changePerson.
     *
     */
    public boolean isChangePerson() {
        return changePerson;
    }
    
    /** Setter for property changePerson.
     * @param changePerson New value of property changePerson.
     *
     */
    public void setChangePerson(boolean changePerson) {
        this.changePerson = changePerson;
    }
    
    /** Getter for property inactivated.
     * @return Value of property inactivated.
     *
     */
    public boolean isInactivated() {
        return inactivated;
    }
    
    /** Setter for property inactivated.
     * @param inactivated New value of property inactivated.
     *
     */
    public void setInactivated(boolean inactivated) {
        this.inactivated = inactivated;
    }
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(Integer personPk) {
        this.personPk = personPk;
    }
    
    /** Getter for property rebateStatusChanged.
     * @return Value of property rebateStatusChanged.
     *
     */
    public boolean isRebateStatusChanged() {
        return rebateStatusChanged;
    }
    
    /** Setter for property rebateStatusChanged.
     * @param rebateStatusChanged New value of property rebateStatusChanged.
     *
     */
    public void setRebateStatusChanged(boolean rebateStatusChanged) {
        this.rebateStatusChanged = rebateStatusChanged;
    }
    
}
