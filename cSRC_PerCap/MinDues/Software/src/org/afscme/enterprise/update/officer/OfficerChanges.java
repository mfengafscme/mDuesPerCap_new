package org.afscme.enterprise.update.officer;


/**
 * Contains the counts of officer changes made to a specific affiliate in an 
 * update.
 */
public class OfficerChanges {
    
    /**
     * Number of officers currenly in the system
     */
    protected int inSystem;
    
    /**
     * count of officers in the file
     */
    protected int inFile;
    
    /**
     * Count of officers that will be replaced
     */
    protected int replaced;
    
    /**
     * Count of officers that will be changed.
     */
    protected int changed;
    
    /**
     * count of offices that will be vacant
     */
    protected int vacant;
    
    /**
     * count of officers that need new cards
     */
    protected int cards;
    
    /**
     * 'G' - good
     * 'W' - warning
     * 'E' - error
     */
    protected char status;
    
    /**
     * The number of officers that were added to the affiliate
     */
    protected int added;
    /****************************************************************************************/
    protected Integer   affPk           = null;
    protected boolean   hasWarning      = false;
    
    // Note, if the affiliate has both errors and warnings, the error will
    // over-power the warnings, and thus the UI will show Red instead of Yellow.    
    protected boolean   hasError        = false;
    /****************************************************************************************/
    /** Getter for property added.
     * @return Value of property added.
     *
     */
    public int getAdded() {
        return added;
    }
    
    /** Setter for property added.
     * @param added New value of property added.
     *
     */
    public void setAdded(int added) {
        this.added = added;
    }
    
    public void incrementAdded() {
        this.added += 1;
    }
    
    public void decrementAdded() {
        this.added -= 1;
    }
    
    /** Getter for property cards.
     * @return Value of property cards.
     *
     */
    public int getCards() {
        return cards;
    }
    
    /** Setter for property cards.
     * @param cards New value of property cards.
     *
     */
    public void setCards(int cards) {
        this.cards = cards;
    }
    
    public void incrementCards() {
        this.cards += 1;
    }
    
    public void decrementCards() {
        this.cards -= 1;
    }
    
    /** Getter for property changed.
     * @return Value of property changed.
     *
     */
    public int getChanged() {
        return changed;
    }
    
    /** Setter for property changed.
     * @param changed New value of property changed.
     *
     */
    public void setChanged(int changed) {
        this.changed = changed;
    }
    
    public void incrementChanged() {
        this.changed += 1;
    }
    
    public void decrementChanged() {
        this.changed -= 1;
    }
    
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
    
    /** Getter for property inSystem.
     * @return Value of property inSystem.
     *
     */
    public int getInSystem() {
        return inSystem;
    }
    
    /** Setter for property inSystem.
     * @param inSystem New value of property inSystem.
     *
     */
    public void setInSystem(int inSystem) {
        this.inSystem = inSystem;
    }
    
    public void incrementInSystem() {
        this.inSystem += 1;
    }
    
    public void decrementInSystem() {
        this.inSystem -= 1;
    }
    
    /** Getter for property replaced.
     * @return Value of property replaced.
     *
     */
    public int getReplaced() {
        return replaced;
    }
    
    /** Setter for property replaced.
     * @param replaced New value of property replaced.
     *
     */
    public void setReplaced(int replaced) {
        this.replaced = replaced;
    }
    
    public void incrementReplaced() {
        this.replaced += 1;
    }
    
    public void decrementReplaced() {
        this.replaced -= 1;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public char getStatus() {
        return status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(char status) {
        this.status = status;
    }
    
    
    /** Getter for property vacant.
     * @return Value of property vacant.
     *
     */
    public int getVacant() {
        return vacant;
    }
    
    /** Setter for property vacant.
     * @param vacant New value of property vacant.
     *
     */
    public void setVacant(int vacant) {
        this.vacant = vacant;
    }
    
    public void incrementVacant() {
        this.vacant += 1;
    }
    
    public void decrementVacant() {
        this.vacant -= 1;
    }
    /*********************************************************************************/
    
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
    
    public void addToValues(OfficerChanges changes) {
        if (changes != null) {
            this.added      +=  changes.added;
            this.cards      +=  changes.cards;
            this.changed    +=  changes.changed;
            this.inFile     +=  changes.inFile;
            this.inSystem   +=  changes.inSystem;
            this.replaced   +=  changes.replaced;
            this.vacant     +=  changes.vacant;
            
        }
    }
    
    /** Getter for property hasError.
     * @return Value hasError of property hasError.
     *
     */
    public boolean getHasError() {
        return hasError;
    }
    
    /** Setter for property hasError.
     * @param hasError New value of property hasError.
     *
     */
    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
    /** Getter for property hasWarning.
     * @return Value hasWarning of property hasWarning.
     *
     */
   
    public boolean getHasWarning() {
        return hasWarning;
    }
   
    /** Setter for property hasWarning.
     * @param hasWarning New value of property hasWarning.
     *
     */
    public void setHasWarning(boolean hasWarning) {
        this.hasWarning = hasWarning;
    }
    /********************************************************************************/
    
}
