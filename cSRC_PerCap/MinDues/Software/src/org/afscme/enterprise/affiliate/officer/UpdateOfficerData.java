package org.afscme.enterprise.affiliate.officer;

import org.afscme.enterprise.officer.OfficerData;

/**
 * Represents the data needed to Update a single Officer, which supports a single 
 * update on the Affiliate Officer Maintenance Edit screen.
 */
public class UpdateOfficerData extends OfficerData 
{
    // Static Variables. Make these private later if they are not needed outside this class.
    public static final int UPDATE_TYPE_RENEW = 1;
    public static final int UPDATE_TYPE_REPLACE = 2;
    public static final int UPDATE_TYPE_VACATE = 3;
    public static final int UPDATE_TYPE_CHANGE = 4;
    public static final int UPDATE_TYPE_NONE = 5;
    
    private int updateType;
    private int errorCode;
    
// Getter and Setter Methods...
    
    /** Getter for property errorCode.
     * @return Value of property errorCode.
     *
     */
    public int getErrorCode() {
        return errorCode;
    }
    
    /** Setter for property errorCode.
     * @param errorCode New value of property errorCode.
     *
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    
    /** Getter for property updateType.
     * @return Value of property updateType.
     *
     */
    public int getUpdateType() {
        return updateType;
    }
    
    /** Setter for property updateType.
     * @param updateType New value of property updateType.
     *
     */
    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }
    
}
