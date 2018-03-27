//Source file: c:\\rosetemp\\org\\afscme\\enterprise\\officer\\OfficerData.java

package org.afscme.enterprise.officer;

import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.officer.OfficeData;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Represents an Officer holding an Office at an Affiliate.
 */
public class OfficerData extends PersonData {
    
    public static final int SORT_FIELD_NONE                = 0;
    public static final int SORT_FIELD_OFFICERTITLE        = 1;
    public static final int SORT_FIELD_OFFICERFULLNM       = 2;
    public static final int SORT_FIELD_TERMSTART           = 3;
    public static final int SORT_FIELD_TERMEND             = 4;
    public static final int SORT_FIELD_PERSONPK            = 5;
    
    private Integer posAddrFromPersonPk;
    private Integer posAddrFromOrgPk;
    private Timestamp posStartDt;
    private Timestamp posEndDt;
    private Boolean suspendedFg;
    private Timestamp suspendedDt;
    private Boolean posStewardFg;
    /** Common Code Key for the Member Status if this Officer is a Member. Will be null  if the Officer is not a Member.*/
    private Integer mbrStatusCodePk;
    private Integer positionAffiliation;
    private Collection theOtherOfficeData;
    private AffiliateIdentifier theAffiliateIdentifier;
    private OfficeData theOfficeData;
    private String officerTitle;
    private String officerFullNM;
    private Integer officerPersonPK;
    
     public static final int sortStringToCode(String sortBy) {
        if (sortBy == null)
            return OfficerData.SORT_FIELD_NONE;
        else if (sortBy.equals("officerTitle"))
            return OfficerData.SORT_FIELD_OFFICERTITLE;
        else if (sortBy.equals("officerFullNM"))
            return OfficerData.SORT_FIELD_OFFICERFULLNM;
        else if (sortBy.equals("termStart"))
            return OfficerData.SORT_FIELD_TERMSTART;
        else if (sortBy.equals("termEnd"))
            return OfficerData.SORT_FIELD_TERMEND;
        else if (sortBy.equals("personPK"))
            return OfficerData.SORT_FIELD_PERSONPK;
        else 
            throw new RuntimeException("Invalid sort field '" + sortBy + "'");
    }    
    
    /** Getter for property mbrStatusCodePk.
     * @return Value of property mbrStatusCodePk.
     *
     */
    public java.lang.Integer getMbrStatusCodePk() {
        return mbrStatusCodePk;
    }
    
    /** Setter for property mbrStatusCodePk.
     * @param mbrStatusCodePk New value of property mbrStatusCodePk.
     *
     */
    public void setMbrStatusCodePk(java.lang.Integer mbrStatusCodePk) {
        this.mbrStatusCodePk = mbrStatusCodePk;
    }
    
    /** Getter for property posAddrFromOrgPk.
     * @return Value of property posAddrFromOrgPk.
     *
     */
    public java.lang.Integer getPosAddrFromOrgPk() {
        return posAddrFromOrgPk;
    }
    
    /** Setter for property posAddrFromOrgPk.
     * @param posAddrFromOrgPk New value of property posAddrFromOrgPk.
     *
     */
    public void setPosAddrFromOrgPk(java.lang.Integer posAddrFromOrgPk) {
        this.posAddrFromOrgPk = posAddrFromOrgPk;
    }
    
    /** Getter for property posAddrFromPersonPk.
     * @return Value of property posAddrFromPersonPk.
     *
     */
    public java.lang.Integer getPosAddrFromPersonPk() {
        return posAddrFromPersonPk;
    }
    
    /** Setter for property posAddrFromPersonPk.
     * @param posAddrFromPersonPk New value of property posAddrFromPersonPk.
     *
     */
    public void setPosAddrFromPersonPk(java.lang.Integer posAddrFromPersonPk) {
        this.posAddrFromPersonPk = posAddrFromPersonPk;
    }
    
    /** Getter for property posEndDt.
     * @return Value of property posEndDt.
     *
     */
    public java.sql.Timestamp getPosEndDt() {
        return posEndDt;
    }
    
    /** Setter for property posEndDt.
     * @param posEndDt New value of property posEndDt.
     *
     */
    public void setPosEndDt(java.sql.Timestamp posEndDt) {
        this.posEndDt = posEndDt;
    }
    
    /** Getter for property positionAffiliation.
     * @return Value of property positionAffiliation.
     *
     */
    public java.lang.Integer getPositionAffiliation() {
        return positionAffiliation;
    }
    
    /** Setter for property positionAffiliation.
     * @param positionAffiliation New value of property positionAffiliation.
     *
     */
    public void setPositionAffiliation(java.lang.Integer positionAffiliation) {
        this.positionAffiliation = positionAffiliation;
    }
    
    /** Getter for property posStartDt.
     * @return Value of property posStartDt.
     *
     */
    public java.sql.Timestamp getPosStartDt() {
        return posStartDt;
    }
    
    /** Setter for property posStartDt.
     * @param posStartDt New value of property posStartDt.
     *
     */
    public void setPosStartDt(java.sql.Timestamp posStartDt) {
        this.posStartDt = posStartDt;
    }
    
    /** Getter for property posStewardFg.
     * @return Value of property posStewardFg.
     *
     */
    public java.lang.Boolean getPosStewardFg() {
        return posStewardFg;
    }
    
    /** Setter for property posStewardFg.
     * @param posStewardFg New value of property posStewardFg.
     *
     */
    public void setPosStewardFg(java.lang.Boolean posStewardFg) {
        this.posStewardFg = posStewardFg;
    }
    
    /** Getter for property suspendedDt.
     * @return Value of property suspendedDt.
     *
     */
    public java.sql.Timestamp getSuspendedDt() {
        return suspendedDt;
    }
    
    /** Setter for property suspendedDt.
     * @param suspendedDt New value of property suspendedDt.
     *
     */
    public void setSuspendedDt(java.sql.Timestamp suspendedDt) {
        this.suspendedDt = suspendedDt;
    }
    
    /** Getter for property suspendedFg.
     * @return Value of property suspendedFg.
     *
     */
    public java.lang.Boolean getSuspendedFg() {
        return suspendedFg;
    }
    
    /** Setter for property suspendedFg.
     * @param suspendedFg New value of property suspendedFg.
     *
     */
    public void setSuspendedFg(java.lang.Boolean suspendedFg) {
        this.suspendedFg = suspendedFg;
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
    
    /** Getter for property theOfficeData.
     * @return Value of property theOfficeData.
     *
     */
    public org.afscme.enterprise.affiliate.officer.OfficeData getTheOfficeData() {
        return theOfficeData;
    }
    
    /** Setter for property theOfficeData.
     * @param theOfficeData New value of property theOfficeData.
     *
     */
    public void setTheOfficeData(org.afscme.enterprise.affiliate.officer.OfficeData theOfficeData) {
        this.theOfficeData = theOfficeData;
    }
    
    /** Getter for property theOtherOfficeData.
     * @return Value of property theOtherOfficeData.
     *
     */
    public java.util.Collection getTheOtherOfficeData() {
        return theOtherOfficeData;
    }
    
    /** Setter for property theOtherOfficeData.
     * @param theOtherOfficeData New value of property theOtherOfficeData.
     *
     */
    public void setTheOtherOfficeData(java.util.Collection theOtherOfficeData) {
        this.theOtherOfficeData = theOtherOfficeData;
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
    
    /** Getter for property officerFullNM.
     * @return Value of property officerFullNM.
     *
     */
    public java.lang.String getOfficerFullNM() {
        return officerFullNM;
    }
    
    /** Setter for property officerFullNM.
     * @param officerFullNM New value of property officerFullNM.
     *
     */
    public void setOfficerFullNM(java.lang.String officerFullNM) {
        this.officerFullNM = officerFullNM;
    }   
       
    /** Getter for property officerPersonPK.
     * @return Value of property officerPersonPK.
     *
     */
    public java.lang.Integer getOfficerPersonPK() {
        return officerPersonPK;
    }
    
    /** Setter for property officerPersonPK.
     * @param officerPersonPK New value of property officerPersonPK.
     *
     */
    public void setOfficerPersonPK(java.lang.Integer officerPersonPK) {
        this.officerPersonPK = officerPersonPK;
    }
    
}
