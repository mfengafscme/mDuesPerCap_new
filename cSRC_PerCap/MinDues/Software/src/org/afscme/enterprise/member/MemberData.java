
package org.afscme.enterprise.member;

import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.common.RecordData;

import java.util.Collection;

/**
 * Represents member detail data, not specific to association with a specific 
 * affiliate. Unclear whether it should be derived from PersonData or contain 
 * attributes exclusive to a member.
 */
public class MemberData 
{
    protected java.sql.Timestamp mbrExpelledDt;
    protected Boolean mbrBarredFg;
    protected PersonData thePersonData;
    protected RecordData theRecordData;
    
    /** This object reference(a Collection) only needs to be set for retrieving data 
     * for the Member Detail page. On update (from Member Detail - Edit - this Collection
     * does not need to be set 
     */
    protected Collection theMemberAffiliateResults;
    
    /** Getter for property mbrExpelledDt.
     * @return Value of property mbrExpelledDt.
     *
     */
    public java.sql.Timestamp getMbrExpelledDt() {
        return mbrExpelledDt;
    }
    
    /** Setter for property mbrExpelledDt.
     * @param mbrExpelledDt New value of property mbrExpelledDt.
     *
     */
    public void setMbrExpelledDt(java.sql.Timestamp mbrExpelledDt) {
        this.mbrExpelledDt = mbrExpelledDt;
    }
    
    /** Getter for property mbrBarredFg.
     * @return Value of property mbrBarredFg.
     *
     */
    public java.lang.Boolean getMbrBarredFg() {
        return mbrBarredFg;
    }
    
    /** Setter for property mbrBarredFg.
     * @param mbrBarredFg New value of property mbrBarredFg.
     *
     */
    public void setMbrBarredFg(java.lang.Boolean mbrBarredFg) {
        this.mbrBarredFg = mbrBarredFg;
    }
    
    /** Getter for property thePersonData.
     * @return Value of property thePersonData.
     *
     */
    public org.afscme.enterprise.person.PersonData getThePersonData() {
        return thePersonData;
    }
    
    /** Setter for property thePersonData.
     * @param thePersonData New value of property thePersonData.
     *
     */
    public void setThePersonData(org.afscme.enterprise.person.PersonData thePersonData) {
        this.thePersonData = thePersonData;
    }
    
    /** Getter for property theMemberAffiliateResults.
     * @return Value of property theMemberAffiliateResults.
     *
     */
    public java.util.Collection getTheMemberAffiliateResults() {
        return theMemberAffiliateResults;
    }
    
    /** Setter for property theMemberAffiliateResults.
     * @param theMemberAffiliateResults New value of property theMemberAffiliateResults.
     *
     */
    public void setTheMemberAffiliateResults(java.util.Collection theMemberAffiliateResults) {
        this.theMemberAffiliateResults = theMemberAffiliateResults;
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
    
    /** Getter for property theMemberAffiliateResult.
     * @return Value of property theMemberAffiliateResult.
     *
     */
   
    
}
