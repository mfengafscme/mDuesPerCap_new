package org.afscme.enterprise.member;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import java.util.Collection;

/**
 * Contains data as a single result that supports the Member Detail UI page
 * specifically. Modified the object to get the affiliate Identifier so that the
 * UI does not need to loop through the results and retrieve this data for each
 * affPk
 */

public class MemberAffiliateResult
{
    protected Integer personPk;
    protected Integer affPk;
 	protected String abbreviatedName;

    /** needed to determine if the affiliate is under restricted administratorship */
    protected Integer affStatus;
    protected boolean AffRestrictedAdmin = false;

    protected Integer mbrStatus;
    protected Integer mbrType;

    protected AffiliateIdentifier theAffiliateIdentifier;

    /** modified as the rule has been changed so that a member can hold mulitple, current offices 
     in an affiliate. Changed to a Collection of OfficerTitleInfo objects
     reference */
    protected Collection theOfficerInfo;


    /** Getter for property personPK.
     * @return Value of property personPK.
     *
     */
    public java.lang.Integer getPersonPk() {
         return this.personPk;
    }

    /** Setter for property personPK.
     * @param personPK New value of property personPK.
     *
     */
    public void setPersonPk(java.lang.Integer personPk) {
        this.personPk = personPk;

    }

    /** Getter for property affPK.
     * @return Value of property affPK.
     *
     */
    public java.lang.Integer getAffPk() {
        return this.affPk;
    }

    /** Setter for property affPK.
     * @param affPK New value of property affPK.
     *
     */
    public void setAffPk(java.lang.Integer affPk) {
        this.affPk = affPk;
    }


    /** Getter for property mbrStatus.
     * @return Value of property mbrStatus.
     *
     */
    public java.lang.Integer getMbrStatus() {
        return this.mbrStatus;
    }

    /** Setter for property mbrStatus.
     * @param mbrStatus New value of property mbrStatus.
     *
     */
    public void setMbrStatus(java.lang.Integer mbrStatus) {
        this.mbrStatus = mbrStatus;
    }

    /** Getter for property mbrType.
     * @return Value of property mbrType.
     *
     */
    public java.lang.Integer getMbrType() {
        return this.mbrType;
    }

    /** Setter for property mbrType.
     * @param mbrType New value of property mbrType.
     *
     */
    public void setMbrType(java.lang.Integer mbrType) {
        this.mbrType = mbrType;
    }

    /** Getter for property theAffiliateIdentifier.
     * @return Value of property theAffiliateIdentifier.
     *
     */
    public org.afscme.enterprise.affiliate.AffiliateIdentifier getTheAffiliateIdentifier() {
        return this.theAffiliateIdentifier;
    }

    /** Setter for property theAffiliateIdentifier.
     * @param theAffiliateIdentifier New value of property theAffiliateIdentifier.
     *
     */
    public void setTheAffiliateIdentifier(org.afscme.enterprise.affiliate.AffiliateIdentifier theAffiliateIdentifier) {
        this.theAffiliateIdentifier = theAffiliateIdentifier;
    }

    /** Getter for property affStatus.
     * @return Value of property affStatus.
     *
     */
    public java.lang.Integer getAffStatus() {
        return this.affStatus;
    }

    /** Setter for property affStatus.
     * @param affStatus New value of property affStatus.
     *
     */
    public void setAffStatus(java.lang.Integer affStatus) {

        this.affStatus = affStatus;
    }

    /** Getter for property abbreviatedName.
	 * @return Value of property abbreviatedName.
	 *
	 */
	public String getAbbreviatedName() {
	    return this.abbreviatedName;
	}

	/** Setter for property abbreviatedName.
	 * @param abbreviatedName New value of property abbreviatedName.
	 *
	 */
	public void setAbbreviatedName(String abbreviatedName) {
	    this.abbreviatedName = abbreviatedName;
    }

     /** Getter for property isAffRestrictedAdmin.
     * @return Value of property isAffRestrictedAdmin.
     *
     */
    public boolean isAffRestrictedAdmin() {
        return this.AffRestrictedAdmin;
    }

    /** Setter for property isAffRestrictedAdmin.
     * @param isAffRestrictedAdmin New value of property isAffRestrictedAdmin.
     *
     */
    public void setIsAffRestrictedAdmin(boolean AffRestrictedAdmin) {
        this.AffRestrictedAdmin = AffRestrictedAdmin;
    }

    /** Setter for property theOfficerInfo.
     * @param theOfficerInfo New value of property theOfficerInfo.
     *
     */
    public void setTheOfficerInfo(java.util.Collection theOfficerInfo) {
        this.theOfficerInfo = theOfficerInfo;
    }
    
    /** Getter for property theOfficerInfo.
     * @return Value of property theOfficerInfo.
     *
     */
    public java.util.Collection getTheOfficerInfo() {
        return theOfficerInfo;
    }
    
}
