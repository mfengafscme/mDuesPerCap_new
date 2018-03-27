package org.afscme.enterprise.affiliate;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;

/**
 * Represents an Affiliate within a Hierarchy. 
 */
public class AffiliateHierarchyEntry {
    
    private Integer affPk;
    
    private AffiliateIdentifier affiliateId;
    
    private String name;
    
    private Integer status;
    
    /** 
     * Indicates an entry in the hierarchy that is also part of the primary sub 
     * hierarchy that was used as the starting point for building the overall 
     * hierarchy. */
    private boolean inPrimarySubHierarchy;
    
    /** Creates a new instance of AffiliateHierarchyEntry */
    public AffiliateHierarchyEntry() {
    }
    
// General Methods...
    
    public String toString() {
        StringBuffer sb = new StringBuffer("AffiliateHierarchyEntry {");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", affiliateId = ");
        sb.append(this.affiliateId);
        sb.append(", inPrimarySubHierarchy = ");
        sb.append(this.inPrimarySubHierarchy);
        sb.append("}");
        return sb.toString().trim();
    }
    
// Getter-Setter Methods...
    
    /** Getter for property affId.
     * @return Value of property affId.
     *
     */
    public AffiliateIdentifier getAffiliateId() {
        return affiliateId;
    }
    
    /** Setter for property affId.
     * @param affId New value of property affId.
     *
     */
    public void setAffiliateId(AffiliateIdentifier affiliateId) {
        this.affiliateId = affiliateId;
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
    
    /** Getter for property inPrimarySubHierarchy.
     * @return Value of property inPrimarySubHierarchy.
     *
     */
    public boolean isInPrimarySubHierarchy() {
        return inPrimarySubHierarchy;
    }
    
    /** Setter for property inPrimarySubHierarchy.
     * @param inPrimarySubHierarchy New value of property inPrimarySubHierarchy.
     *
     */
    public void setInPrimarySubHierarchy(boolean inPrimarySubHierarchy) {
        this.inPrimarySubHierarchy = inPrimarySubHierarchy;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public Integer getStatus() {
        return status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
}
