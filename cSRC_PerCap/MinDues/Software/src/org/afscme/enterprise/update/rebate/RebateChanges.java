package org.afscme.enterprise.update.rebate;

import java.sql.Timestamp;
import java.util.Set;
import java.util.HashSet;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.CollectionUtil;


/**
 * Contains the counts of rebate changes made to a specific affiliate in an update.
 */
public class RebateChanges {

    protected Integer   affPk           = null;
    protected boolean   hasWarning      = false;
    
    // Note, if the affiliate has both errors and warnings, the error will
    // over-power the warnings, and thus the UI will show Red instead of Yellow.    
    protected boolean   hasError        = false;

    // the number of AFSCME Member Numbers in File within this affiliate
    protected int afscmeMemberNumCountInFile = 0;
    
    // the number of invalid Acceptance codes in File within this affiliate.
    protected int invalidAcceptCodeCountInFile = 0;
    
    
    //******************************************************************
    //  UI counters
    //******************************************************************
    
    /**
     * number of members sent
     */
    protected int sent;
    
    /**
     * number of members received
     */
    protected int received;
    
    /**
     * number of members unknown
     */
    protected int unknown;
    
    /**
     * number of acceptance code changes by the council
     */
    protected int council;
    
    /**
     * number of acceptance code changes by the local
     */
    protected int local;
    
    /**
     * number of acceptance codes unchanged
     */
    protected int unchanged;
    

    /**
     * a Set to keep track of matched (in system) personPKs for each affiliate
     */
    protected Set matchedPersonPks;

    protected AffiliateIdentifier affiliateIdentifier;
    
    protected Timestamp fileGeneratedDt = null;    

    
     /****************************************************************************************/
    
    public RebateChanges() {
        matchedPersonPks = new HashSet();
    }
        

    public String toString() {
        return "RebateChanges[" +
            "affPk=" + affPk + 
            ", hasError=" + hasError + 
            ", hasWarning=" + hasWarning + 
            ", sent=" + sent + 
            ", received=" + received + 
            ", unknown=" + unknown + 
            ", council=" + council + 
            ", local=" + local + 
            ", unchanged=" + unchanged + 
            ", afscmeMemberNumCountInFile=" + afscmeMemberNumCountInFile + 
            ", invalidAcceptCodeCountInFile=" + invalidAcceptCodeCountInFile +
            ", fileGeneratedDt=" + fileGeneratedDt +
            ", matchedPersonPks=" + CollectionUtil.toString(matchedPersonPks) + 
            "]"
        ;
    }
    
 
    /** Getter for property affiliateIdentifier.
     * @return Value of property affiliateIdentifier.
     *
     */
    public AffiliateIdentifier getAffiliateIdentifier() {
        return affiliateIdentifier;
    }
    
    /** Setter for property affiliateIdentifier.
     * @param affiliateIdentifier New value of property affiliateIdentifier.
     *
     */
    public void setAffiliateIdentifier(AffiliateIdentifier affiliateIdentifier) {
        this.affiliateIdentifier = affiliateIdentifier;
    }
    
    /** Getter for property fileGeneratedDt.
     * @return Value of property fileGeneratedDt.
     *
     */
    public Timestamp getFileGeneratedDt() {
        return fileGeneratedDt;
    }
    
    /** Setter for property fileGeneratedDt.
     * @param fileGeneratedDt New value of property fileGeneratedDt.
     *
     */
    public void setFileGeneratedDt(Timestamp fileGeneratedDt) {
        this.fileGeneratedDt = fileGeneratedDt;
    }
    
    /** Getter for property matchedPersonPks.
     * @return Value of property matchedPersonPks.
     *
     */
    public Set getMatchedPersonPks() {
        return matchedPersonPks;
    }
    
    /** Setter for property matchedPersonPks.
     * @param matchedPersonPks New value of property matchedPersonPks.
     *
     */
    public void setMatchedPersonPks(Set matchedPersonPks) {
        this.matchedPersonPks = matchedPersonPks;
    }

    
    /** Getter for property afscmeMemberNumCountInFile.
     * @return Value of property afscmeMemberNumCountInFile.
     *
     */
    public int getAfscmeMemberNumCountInFile() {
        return afscmeMemberNumCountInFile;
    }
    
    /** Setter for property afscmeMemberNumCountInFile.
     * @param afscmeMemberNumCountInFile New value of property afscmeMemberNumCountInFile.
     *
     */
    public void setAfscmeMemberNumCountInFile(int afscmeMemberNumCountInFile) {
        this.afscmeMemberNumCountInFile = afscmeMemberNumCountInFile;
    }
    
    public void incrementAfscmeMemberNumCountInFile() {
        this.afscmeMemberNumCountInFile += 1;
    }
    
    public void decrementAfscmeMemberNumCountInFile() {
        this.afscmeMemberNumCountInFile -= 1;
    }

    /** Getter for property invalidAcceptCodeCountInFile.
     * @return Value of property invalidAcceptCodeCountInFile.
     *
     */
    public int getInvalidAcceptCodeCountInFile() {
        return invalidAcceptCodeCountInFile;
    }
    
    /** Setter for property invalidAcceptCodeCountInFile.
     * @param invalidAcceptCodeCountInFile New value of property invalidAcceptCodeCountInFile.
     *
     */
    public void setInvalidAcceptCodeCountInFile(int invalidAcceptCodeCountInFile) {
        this.invalidAcceptCodeCountInFile = invalidAcceptCodeCountInFile;
    }
    
    public void incrementInvalidAcceptCodeCountInFile() {
        this.invalidAcceptCodeCountInFile += 1;
    }
    
    public void decrementInvalidAcceptCodeCountInFile() {
        this.invalidAcceptCodeCountInFile -= 1;
    }

    
    /** Getter for property council.
     * @return Value of property council.
     *
     */
    public int getCouncil() {
        return council;
    }
    
    /** Setter for property council.
     * @param council New value of property council.
     *
     */
    public void setCouncil(int council) {
        this.council = council;
    }
    
    public void incrementCouncil() {
        this.council += 1;
    }
    
    public void decrementCouncil() {
        this.council -= 1;
    }
    
    /** Getter for property local.
     * @return Value of property local.
     *
     */
    public int getLocal() {
        return local;
    }
    
    /** Setter for property local.
     * @param local New value of property local.
     *
     */
    public void setLocal(int local) {
        this.local = local;
    }
    
    public void incrementLocal() {
        this.local += 1;
    }
    
    public void decrementLocal() {
        this.local -= 1;
    }
    
    /** Getter for property received.
     * @return Value of property received.
     *
     */
    public int getReceived() {
        return received;
    }
    
    /** Setter for property received.
     * @param received New value of property received.
     *
     */
    public void setReceived(int received) {
        this.received = received;
    }
    
    public void incrementReceived() {
        this.received += 1;
    }
    
    public void decrementReceived() {
        this.received -= 1;
    }
    
    /** Getter for property sent.
     * @return Value of property sent.
     *
     */
    public int getSent() {
        return sent;
    }
    
    /** Setter for property sent.
     * @param sent New value of property sent.
     *
     */
    public void setSent(int sent) {
        this.sent = sent;
    }
    
    public void incrementSent() {
        this.sent += 1;
    }
    
    public void decrementSent() {
        this.sent -= 1;
    }
    
    /** Getter for property unchanged.
     * @return Value of property unchanged.
     *
     */
    public int getUnchanged() {
        return unchanged;
    }
    
    /** Setter for property unchanged.
     * @param unchanged New value of property unchanged.
     *
     */
    public void setUnchanged(int unchanged) {
        this.unchanged = unchanged;
    }
    
    public void incrementUnchanged() {
        this.unchanged += 1;
    }
    
    public void decrementUnchanged() {
        this.unchanged -= 1;
    }
    
    /** Getter for property unknown.
     * @return Value of property unknown.
     *
     */
    public int getUnknown() {
        return unknown;
    }
    
    /** Setter for property unknown.
     * @param unknown New value of property unknown.
     *
     */
    public void setUnknown(int unknown) {
        this.unknown = unknown;
    }
    
    public void incrementUnknown() {
        this.unknown += 1;
    }
    
    public void decrementUnknown() {
        this.unknown -= 1;
    }
    
    /********************************************************************************/
    
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
    
    public void addToValues(RebateChanges changes) {
        if (changes != null) {
            this.council    +=  changes.council;
            this.local      +=  changes.local;
            this.received   +=  changes.received;
            this.sent       +=  changes.sent;
            this.unchanged  +=  changes.unchanged;
            this.unknown    +=  changes.unknown;
            this.afscmeMemberNumCountInFile     +=  changes.afscmeMemberNumCountInFile;
            this.invalidAcceptCodeCountInFile   +=  changes.invalidAcceptCodeCountInFile;
        }
    }

}
