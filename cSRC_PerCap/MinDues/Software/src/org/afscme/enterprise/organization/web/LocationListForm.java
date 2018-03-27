package org.afscme.enterprise.organization.web;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.util.TextUtil;


/**
 * Represents the form that holds the data for the Location Maintenance screen
 *
 * @struts:form name="locationListForm"
 */
public class LocationListForm extends ActionForm {

    /** List of LocationData objects */
    private List locationList;

    /** The org's Primary Location */
    private LocationData primaryLocation;

    /** The organization Pk */
    private Integer orgPK;    
    
    /** TRUE if orgPk is an Affiliate */
    private boolean isAffiliatePk;


     /**
     * constructor and to set up values
     */   
    public LocationListForm() {
        isAffiliatePk = false;
    }

    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("locationList: " + TextUtil.toString(locationList));
        buf.append(", primaryLocation: " + primaryLocation);
        buf.append(", isAffiliatePk: " + isAffiliatePk);        
        return buf.toString()+"]";
    }


    /** 
     * Getter for property locationList.
     * @return Value of property locationList.
     */
    public List getLocations() {
        return locationList;
    }

    /** 
     * Setter for property locations.
     * @param locations New value of property locations.
     */
    public void setLocations(List locations) {
        this.locationList = locations;
    }

    /** 
     * Getter for property primaryLocation.
     * @return Value of property primaryLocation.
     */
    public LocationData getPrimaryLocation() {
        return primaryLocation;
    }

    /** 
     * Setter for property primaryLocation.
     * @param locations New value of property primaryLocation.
     */
    public void setPrimaryLocation(LocationData primaryLocation) {
        this.primaryLocation = primaryLocation;
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
    
    /** 
     * Getter for property isAffiliatePk.
     * @return Value of property isAffiliatePk.
     */
    public boolean isAffiliatePk() {
        return isAffiliatePk;
    }

    /** 
     * Setter for property isAffiliatePk.
     * @param isAffiliatePk New value of property isAffiliatePk.
     */
    public void setIsAffiliatePk(boolean isAffiliatePk) {
        this.isAffiliatePk = isAffiliatePk;
    }
   
}
