package org.afscme.enterprise.address;

import java.sql.Timestamp;
import org.afscme.enterprise.common.RecordData;

/**
 * Aggregate of person address data and record data.  Using this aggregate to 
 * combine address data and record data, rather than combinding them directly, 
 * allows address data to be manipulated separately from data about an addresses 
 * storage.
 */
public class PersonAddressRecord extends PersonAddress 
{
    protected RecordData recordData;
    protected Integer personPk;
    
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
    
    public String toString() {
        return "PersonAddressRecord[personPk="+personPk+", "+super.toString()+recordData.toString()+"]";
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
    
}
