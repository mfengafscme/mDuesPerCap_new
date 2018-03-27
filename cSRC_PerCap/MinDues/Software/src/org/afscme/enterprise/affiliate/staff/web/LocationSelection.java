package org.afscme.enterprise.affiliate.staff.web;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.afscme.enterprise.organization.LocationData;


/**
 * Holds the data for the Location Maintenance screen
 */
public class LocationSelection  {

    /** List of LocationData objects */
    private List locationList;
    
    /** pk of currently selected location */
    private Integer currentLocation;

    /**
     * Action to use for the cancel button back to the last screen. Must include
     * parameters needed to return to previous screen.
     */
    private String returnAction;
    
    /**
     * Name of the return action
     */
    private String back;
    
    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        return buf.toString()+"]";
    }


    public List getLocations() {
        return locationList;
    }

    /** Setter for property locations.
     * @param locations New value of property locations.
     *
     */
    public void setLocations(List locations) {
        this.locationList = locations;
    }
    
    /** Getter for property currentLocation.
     * @return Value of property currentLocation.
     *
     */
    public java.lang.Integer getCurrentLocation() {
        return currentLocation;
    }
    
    /** Setter for property currentLocation.
     * @param currentLocation New value of property currentLocation.
     *
     */
    public void setCurrentLocation(java.lang.Integer currentLocation) {
        this.currentLocation = currentLocation;
    }
    
    /** Getter for property back.
     * @return Value of property back.
     *
     */
    public java.lang.String getBack() {
        return back;
    }
    
    /** Setter for property back.
     * @param back New value of property back.
     *
     */
    public void setBack(java.lang.String back) {
        this.back = back;
    }
    
    /** Getter for property returnAction.
     * @return Value of property returnAction.
     *
     */
    public java.lang.String getReturnAction() {
        return returnAction;
    }
    
    /** Setter for property returnAction.
     * @param returnAction New value of property returnAction.
     *
     */
    public void setReturnAction(java.lang.String returnAction) {
        this.returnAction = returnAction;
    }
    
}



