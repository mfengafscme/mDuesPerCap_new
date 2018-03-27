package org.afscme.enterprise.organization;

import java.util.LinkedList;


/**
 * Data directly related to an individual organization.
 */
public class OrganizationData 
{
    public Integer orgPK;
    public String orgNm;
    public Integer orgType;
    public String orgWebURL;
    public Boolean markedForDeletion;    
    
    // for PAC
    public String orgEmailDomain;
    
    /**
     * Stores just the primary location for this organization. 
     * Used for the orgDetail screen only.
     */
    public LocationData primaryLocationData;

    /**
     * Stores all of the locations for this organization (list of LocationData objects). 
     * Used for the location maintenance
     */    
    public LinkedList locationData;
//    public LocationData LocationData;
    
    
    public OrgMailingListData theOrgMailingListData;

    
    /** Getter for property orgNm.
     * @return Value of property orgNm.
     *
     */
    public java.lang.String getOrgNm() {
        return orgNm;
    }
    
    /** Setter for property orgNm.
     * @param orgNm New value of property orgNm.
     *
     */
    public void setOrgNm(java.lang.String orgNm) {
        this.orgNm = orgNm;
    }
    
    /** Getter for property orgPK.
     * @return Value of property orgPK.
     *
     */
    public java.lang.Integer getOrgPK() {
        return orgPK;
    }
    
    /** Setter for property orgPK.
     * @param orgPK New value of property orgPK.
     *
     */
    public void setOrgPK(java.lang.Integer orgPK) {
        this.orgPK = orgPK;
    }
    
    /** Getter for property orgType.
     * @return Value of property orgType.
     *
     */
    public java.lang.Integer getOrgType() {
        return orgType;
    }
    
    /** Setter for property orgType.
     * @param orgType New value of property orgType.
     *
     */
    public void setOrgType(java.lang.Integer orgType) {
        this.orgType = orgType;
    }
    
    /** Getter for property orgWebURL.
     * @return Value of property orgWebURL.
     *
     */
    public java.lang.String getOrgWebURL() {
        return orgWebURL;
    }
    
    /** Setter for property orgWebURL.
     * @param orgWebURL New value of property orgWebURL.
     *
     */
    public void setOrgWebURL(java.lang.String orgWebURL) {
        this.orgWebURL = orgWebURL;
    }

    /** Getter for property markedForDeletion.
     * @return Value of property markedForDeletion.
     *
     */
    public java.lang.Boolean getMarkedForDeletion() {
        return markedForDeletion;
    }
    
    /** Setter for property markedForDeletion.
     * @param markedForDeletion New value of property markedForDeletion.
     *
     */
    public void setMarkedForDeletion(java.lang.Boolean markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }

    /** Getter for property orgEmailDomain.
     * @return Value of property orgEmailDomain.
     *
     */
    public java.lang.String getOrgEmailDomain() {
        return orgEmailDomain;
    }
    
    /** Setter for property orgEmailDomain.
     * @param orgEmailDomain New value of property orgEmailDomain.
     *
     */
    public void setOrgEmailDomain(java.lang.String orgEmailDomain) {
        this.orgEmailDomain = orgEmailDomain;
    }
   
    /** Getter for property primaryLocationData.
     * @return Value of property primaryLocationData.
     *
     */
    public org.afscme.enterprise.organization.LocationData getPrimaryLocationData() {
        return primaryLocationData;
    }
    
    /** Setter for property primaryLocationData.
     * @param primaryLocationData New value of property primaryLocationData.
     *
     */
    public void setPrimaryLocationData(org.afscme.enterprise.organization.LocationData primaryLocationData) {
        this.primaryLocationData = primaryLocationData;
    }

    /** Getter for property locationData.
     * @return Value of property locationData.
     *
     */
    public java.util.LinkedList getLocationData() {
        return locationData;
    }
    
    /** Setter for property locationData.
     * @param locationData New value of property locationData.
     *
     */
    public void setLocationData(java.util.LinkedList locationData) {
        this.locationData = locationData;
    }
        
    public String toString() {
        return "OrganizationData[" +
        "orgPK="+orgPK+", "+
        "orgNm="+orgNm+", "+
        "orgType="+orgType+", "+
        "orgWebURL="+orgWebURL+", "+
        "markedForDeletion="+markedForDeletion+", "+
        "orgEmailDomain="+orgEmailDomain+"]";
    }    
    

}
