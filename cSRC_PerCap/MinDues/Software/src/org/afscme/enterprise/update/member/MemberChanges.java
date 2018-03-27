package org.afscme.enterprise.update.member;

import java.util.Set;
import java.util.HashSet;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.CollectionUtil;

/**
 * Contains the counts of member changes made to a specific affiliate in an update.
 */
public class MemberChanges {
    
    protected Integer affPk = null;

    protected boolean hasWarning = false;
    
    // Note, if the affiliate has both errors and warnings, the error will
    // over-power the warnings, and thus the UI will show Red instead of Yellow.    
    protected boolean hasError = false;
    
    // the number of Affiliate Member Numbers in File within this affiliate
    protected int affMemberNumCountInFile = 0;
    
    // the number of all-zero zip codes in File within this affiliate.
    protected int zeroZipCount = 0;
    
    // the number of matched Affiliate Member Numbers
    protected int matchedAffMemberNumCount = 0;
    
    // the number of officers within this affiliate in File
    protected int officerCount = 0;
    
    
    //******************************************************************
    //  UI counters
    //******************************************************************
    
    /**
     * count of members in the system
     */
    protected int inSystem = 0;
    
    /**
     * count of members in the file
     */
    protected int inFile = 0;
    
    /**
     * count of members that will be added
     */
    protected int added = 0;
    
    /**
     * count of members that will be inactivated
     * When a member is not in the update file, and it is found in the system, and 
     * it is not an officer, it is set to be inactivated.
     */
    protected int inactivated = 0;
    
    /**
     * count of members that will be changed
     */
    protected int changed = 0;
    
    /**
     * count of members that will have status set to 'T'
     * When the a member is not in the update file, and it is found in the system, if
     * this member is an officer, it is set to T.
     */
    protected int newTRecords = 0;
    
    /**
     * count of members that matched existing members
     */
    protected int match = 0;
    
    /**
     * count of members that did not match existing data
     */
    protected int nonMatch = 0;
    
    /**
     * Pointer to common code type UpdateFileStatus
     */
    protected char status;
    
    /**
     * a Set to keep track of matched (in system) members's personPK for each affiliate
     */
    protected Set matchedMemberPks;
    
    public MemberChanges() {
        matchedMemberPks = new HashSet();
    }
    
    public String toString() {
        return "MemberChanges[" +
            "affPk=" + affPk + 
            ", hasError=" + hasError + 
            ", hasWarning=" + hasWarning + 
            ", added=" + added + 
            ", affMemberNumCountInFile=" + affMemberNumCountInFile + 
            ", changed=" + changed + 
            ", inactivated=" + inactivated + 
            ", inFile=" + inFile + 
            ", inSystem=" + inSystem + 
            ", match=" + match + 
            ", matchedAffMemberNumCount=" + matchedAffMemberNumCount + 
            ", matchedMemberPks=" + CollectionUtil.toString(matchedMemberPks) + 
            ", newTRecords=" + newTRecords + 
            ", nonMatch=" + nonMatch +
            ", officerCount=" + officerCount +
            ", status=" + status + 
            ", zeroZipCount=" + zeroZipCount +
            "]"
        ;
    }
    
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
    
    /** Getter for property affMemberNumCountInFile.
     * @return Value of property affMemberNumCountInFile.
     *
     */
    public int getAffMemberNumCountInFile() {
        return affMemberNumCountInFile;
    }
    
    /** Setter for property affMemberNumCountInFile.
     * @param affMemberNumCountInFile New value of property affMemberNumCountInFile.
     *
     */
    public void setAffMemberNumCountInFile(int affMemberNumCountInFile) {
        this.affMemberNumCountInFile = affMemberNumCountInFile;
    }
    
    public void incrementAffMemberNumCountInFile() {
        this.affMemberNumCountInFile += 1;
    }
    
    public void decrementAffMemberNumCountInFile() {
        this.affMemberNumCountInFile -= 1;
    }
    
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
    
    /** Getter for property hasError.
     * @return Value of property hasError.
     *
     */
    public boolean isHasError() {
        return hasError;
    }
    /****************************************************************************/
    /*VBP 08/05/03 made a new getter method so that this could be used on the UI
     ****************************************************************************/
    public boolean getHasError() {
        return hasError;
    }
    /********************************************End Addition*******************/
    /** Setter for property hasError.
     * @param hasError New value of property hasError.
     *
     */
    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
    
    /** Getter for property hasWarning.
     * @return Value of property hasWarning.
     *
     */
    public boolean isHasWarning() {
        return hasWarning;
    }
    /****************************************************************************/
    /*08/05/03 made a new getter method so that this could be used on the UI
     ****************************************************************************/
    public boolean getHasWarning() {
        return hasWarning;
    }
    /********************************************End Addition*******************/
    /** Setter for property hasWarning.
     * @param hasWarning New value of property hasWarning.
     *
     */
    public void setHasWarning(boolean hasWarning) {
        this.hasWarning = hasWarning;
    }
    
    /** Getter for property inactivated.
     * @return Value of property inactivated.
     *
     */
    public int getInactivated() {
        return inactivated;
    }
    
    /** Setter for property inactivated.
     * @param inactivated New value of property inactivated.
     *
     */
    public void setInactivated(int inactivated) {
        this.inactivated = inactivated;
    }
    
    public void incrementInactivated() {
        this.inactivated += 1;
    }
    
    public void decrementInactivated() {
        this.inactivated -= 1;
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
    
    /** Getter for property match.
     * @return Value of property match.
     *
     */
    public int getMatch() {
        return match;
    }
    
    /** Setter for property match.
     * @param match New value of property match.
     *
     */
    public void setMatch(int match) {
        this.match = match;
    }
    
    public void incrementMatch() {
        this.match += 1;
    }
    
    public void decrementMatch() {
        this.match -= 1;
    }
    
    /** Getter for property matchedAffMemberNumCount.
     * @return Value of property matchedAffMemberNumCount.
     *
     */
    public int getMatchedAffMemberNumCount() {
        return matchedAffMemberNumCount;
    }
    
    /** Setter for property matchedAffMemberNumCount.
     * @param matchedAffMemberNumCount New value of property matchedAffMemberNumCount.
     *
     */
    public void setMatchedAffMemberNumCount(int matchedAffMemberNumCount) {
        this.matchedAffMemberNumCount = matchedAffMemberNumCount;
    }
    
    public void incrementMatchedAffMemberNumCount() {
        this.matchedAffMemberNumCount += 1;
    }
    
    public void decrementMatchedAffMemberNumCount() {
        this.matchedAffMemberNumCount -= 1;
    }
    
    /** Getter for property matchedMemberPks.
     * @return Value of property matchedMemberPks.
     *
     */
    public Set getMatchedMemberPks() {
        return matchedMemberPks;
    }
    
    /** Setter for property matchedMemberPks.
     * @param matchedMemberPks New value of property matchedMemberPks.
     *
     */
    public void setMatchedMemberPks(Set matchedMemberPks) {
        this.matchedMemberPks = matchedMemberPks;
    }
    
    /** Getter for property newTRecords.
     * @return Value of property newTRecords.
     *
     */
    public int getNewTRecords() {
        return newTRecords;
    }
    
    /** Setter for property newTRecords.
     * @param newTRecords New value of property newTRecords.
     *
     */
    public void setNewTRecords(int newTRecords) {
        this.newTRecords = newTRecords;
    }
    
    public void incrementNewTRecords() {
        this.newTRecords += 1;
    }
    
    public void decrementNewTRecords() {
        this.newTRecords -= 1;
    }
    
    /** Getter for property nonMatch.
     * @return Value of property nonMatch.
     *
     */
    public int getNonMatch() {
        return nonMatch;
    }
    
    /** Setter for property nonMatch.
     * @param nonMatch New value of property nonMatch.
     *
     */
    public void setNonMatch(int nonMatch) {
        this.nonMatch = nonMatch;
    }
    
    public void incrementNonMatch() {
        this.nonMatch += 1;
    }
    
    public void decrementNonMatch() {
        this.nonMatch -= 1;
    }
    
    /** Getter for property officerCount.
     * @return Value of property officerCount.
     *
     */
    public int getOfficerCount() {
        return officerCount;
    }
    
    /** Setter for property officerCount.
     * @param officerCount New value of property officerCount.
     *
     */
    public void setOfficerCount(int officerCount) {
        this.officerCount = officerCount;
    }
    
    public void incrementOfficerCount() {
        this.officerCount += 1;
    }
    
    public void decrementOfficerCount() {
        this.officerCount -= 1;
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
    
    /** Getter for property zeroZipCount.
     * @return Value of property zeroZipCount.
     *
     */
    public int getZeroZipCount() {
        return zeroZipCount;
    }
    
    /** Setter for property zeroZipCount.
     * @param zeroZipCount New value of property zeroZipCount.
     *
     */
    public void setZeroZipCount(int zeroZipCount) {
        this.zeroZipCount = zeroZipCount;
    }
    
    public void incrementZeroZipCount() {
        this.zeroZipCount += 1;
    }
    
    public void decrementZeroZipCount() {
        this.zeroZipCount -= 1;
    }
    
    public void addToValues(MemberChanges changes) {
        if (changes != null) {
            this.added += changes.added;
            this.affMemberNumCountInFile += changes.affMemberNumCountInFile;
            this.changed += changes.changed;
            this.inactivated += changes.inactivated;
            this.inFile += changes.inFile;
            this.inSystem += changes.inSystem;
            this.match += changes.match;
            this.matchedAffMemberNumCount += changes.matchedAffMemberNumCount;
            this.newTRecords += changes.newTRecords;
            this.nonMatch += changes.nonMatch;
            this.officerCount += changes.officerCount;
            this.zeroZipCount += changes.zeroZipCount;
        }
    }
    
}
