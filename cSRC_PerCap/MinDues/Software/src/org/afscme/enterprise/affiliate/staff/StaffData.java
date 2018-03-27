package org.afscme.enterprise.affiliate.staff;

import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.common.CommentData;
import java.util.Collection;
import org.afscme.enterprise.common.RecordData;

/**
 * Represents an Affiliate Staff (or POC) data.  i.e., Data about a person's 
 * position as a point of contact for an affiliate.
 */
public class StaffData
{
    private Integer staffTitlePk;
    private Integer pocForPk;
    private Integer locationPk;
    private RecordData recordData;
    
    
    /** Getter for property pocForPk.
     * @return Value of property pocForPk.
     *
     */
    public Integer getPocForPk() {
        return pocForPk;
    }
    
    /** Setter for property pocForPk.
     * @param pocForPk New value of property pocForPk.
     *
     */
    public void setPocForPk(Integer pocForPk) {
        this.pocForPk = pocForPk;
    }
    
    /** Getter for property staffTitlePk.
     * @return Value of property staffTitlePk.
     *
     */
    public java.lang.Integer getStaffTitlePk() {
        return staffTitlePk;
    }
    
    /** Setter for property staffTitlePk.
     * @param staffTitlePk New value of property staffTitlePk.
     *
     */
    public void setStaffTitlePk(java.lang.Integer staffTitlePk) {
        this.staffTitlePk = staffTitlePk;
    }
    
    /** Getter for property locationPk.
     * @return Value of property locationPk.
     *
     */
    public java.lang.Integer getLocationPk() {
        return locationPk;
    }
    
    /** Setter for property locationPk.
     * @param locationPk New value of property locationPk.
     *
     */
    public void setLocationPk(java.lang.Integer locationPk) {
        this.locationPk = locationPk;
    }
    
    /** Getter for property recordData.
     * @return Value of property recordData.
     *
     */
    public RecordData getRecordData() {
        return recordData;
    }
    
    /** Setter for property recordData.
     * @param recordData New value of property recordData.
     *
     */
    public void setRecordData(RecordData recordData) {
        this.recordData = recordData;
    }
    
}
