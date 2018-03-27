package org.afscme.enterprise.organization;

import org.afscme.enterprise.common.RecordData;

/**
 * Aggregate of organization address data and record data.  Using this aggregate to 
 * combine address data and record data, rather than combinding them directly, 
 * allows address data to be manipulated separately from data about an addresses 
 * storage.
 */
public class OrgAddressRecord extends OrgAddressData 
{
    public RecordData recordData;
    
    /** Getter for property recordData.
     * @return Value of property recordData.
     *
     */
    public org.afscme.enterprise.common.RecordData getRecordData() {
        return recordData;
    }    
    
    /** Setter for property recordData.
     * @param recordData New value of property recordData.
     *
     */
    public void setRecordData(org.afscme.enterprise.common.RecordData recordData) {
        this.recordData = recordData;
    }

    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */    
    public String toString() {
        return "OrgAddressRecord[" +
        super.toString() +", "+
        "recordData="+recordData+"]";
    }
}
