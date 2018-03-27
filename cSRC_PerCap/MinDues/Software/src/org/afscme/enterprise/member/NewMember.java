
package org.afscme.enterprise.member;

import java.sql.Timestamp;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;

/**
 * Represents a New Member being added to the system.
 * Mod - 5/10/03 GRD
 * changed to use NewPerson for person attributes if a new person is being added as part of 
 * adding the member 
 * 
 */
public class NewMember 
{
    
    /*
    * if the affPk property is not null, then the affiliate finder was used and an affPk
    * from the user selection was recorded. If the affPk is null, then the user should have 
    * entered a valid and unique affiliate identifier. (If affPk is not null, then the affiliate
    * ID fields are still expected to be populated from the affiliate finder action 
    */
    protected Integer affPk = null;
    protected AffiliateIdentifier theAffiliateIdentifier = new AffiliateIdentifier();
    // protected Character affType = null;
   // protected String affLocalSubChapter = "0000";
   // protected String affStateNatType = null;
   // protected String affSubUnit = "0000";
   // protected String affCouncilRetireeChap = "0000";
   // protected Character affCode = null;
    protected Integer mbrType = null;
    protected Integer mbrStatus = null;
    protected java.sql.Timestamp  mbrJoinDt = null;
    /*
     * existingPersonComment should only be set if an existing person is being used to create the
     * membership. For a new person, the comment should be set and updated in the NewPerson object 
     */ 
    protected String existingPersonComment = null;
     
   /* 
    * If personPk is not null, then an existing person is being added to a new membership  
    */
    protected Integer personPk = null;
    /* 
    * If theNewPerson is not null, then a new person is being added first in order to add the 
    * member and their affiliation 
    */
    protected NewPerson theNewPerson = null;
    
    // protected Integer memberPk; no longer a memberPk based on data model change
    
        
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public java.lang.Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPK.
     * @param affPK New value of property affPK.
     *
     */
    public void setAffPk(java.lang.Integer affPk) {
        this.affPk = affPk;
    }
    
   
    /** Getter for property mbrType.
     * @return Value of property mbrType.
     *
     */
    public java.lang.Integer getMbrType() {
        return mbrType;
    }
    
    /** Setter for property mbrType.
     * @param mbrType New value of property mbrType.
     *
     */
    public void setMbrType(java.lang.Integer mbrType) {
        this.mbrType = mbrType;
    }
    
    /** Getter for property mbrStatus.
     * @return Value of property mbrStatus.
     *
     */
    public java.lang.Integer getMbrStatus() {
        return mbrStatus;
    }
    
    /** Setter for property mbrStatus.
     * @param mbrStatus New value of property mbrStatus.
     *
     */
    public void setMbrStatus(java.lang.Integer mbrStatus) {
        this.mbrStatus = mbrStatus;
    }
    
    /** Getter for property mbrJoinDt.
     * @return Value of property mbrJoinDt.
     *
     */
    public java.sql.Timestamp getMbrJoinDt() {
        return mbrJoinDt;
    }
    
    /** Setter for property mbrJoinDt.
     * @param mbrJoinDt New value of property mbrJoinDt.
     *
     */
    public void setMbrJoinDt(java.sql.Timestamp mbrJoinDt) {
        this.mbrJoinDt = mbrJoinDt;
    }
    
         
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public java.lang.Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(java.lang.Integer personPk) {
        this.personPk = personPk;
    }
    
    /** Getter for property theNewPerson.
     * @return Value of property theNewPerson.
     *
     */
    public org.afscme.enterprise.person.NewPerson getTheNewPerson() {
        return theNewPerson;
    }    
 
    /** Setter for property theNewPerson.
     * @param theNewPerson New value of property theNewPerson.
     *
     */
    public void setTheNewPerson(org.afscme.enterprise.person.NewPerson theNewPerson) {
        this.theNewPerson = theNewPerson;
    }    
    
    /** Getter for property existingPersonComment.
     * @return Value of property existingPersonComment.
     *
     */
    public java.lang.String getExistingPersonComment() {
        return existingPersonComment;
    }
    
    /** Setter for property existingPersonComment.
     * @param existingPersonComment New value of property existingPersonComment.
     *
     */
    public void setExistingPersonComment(java.lang.String existingPersonComment) {
        this.existingPersonComment = existingPersonComment;
    }
    
    /** Getter for property theAffiliateIdentifier.
     * @return Value of property theAffiliateIdentifier.
     *
     */
    public org.afscme.enterprise.affiliate.AffiliateIdentifier getTheAffiliateIdentifier() {
        return theAffiliateIdentifier;
    }
    
    /** Setter for property theAffiliateIdentifier.
     * @param theAffiliateIdentifier New value of property theAffiliateIdentifier.
     *
     */
    public void setTheAffiliateIdentifier(org.afscme.enterprise.affiliate.AffiliateIdentifier theAffiliateIdentifier) {
        this.theAffiliateIdentifier = theAffiliateIdentifier;
    }
    
}
