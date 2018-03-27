package org.afscme.enterprise.mailinglists;


/**
 * Data about mailing list code
 */
public class MailingListData 
{
    protected Integer mailingListPk;
    protected String mailingListNm;
    protected Integer addressPk;
    protected Integer addressType;
    protected String locationNm;
    protected int mailingListBulkCount;
    
    
    /** Getter for property mailingListBulkCount.
     * @return Value of property mailingListBulkCount.
     *
     */
    public int getMailingListBulkCount() {
        return mailingListBulkCount;
    }    
    
    /** Setter for property mailingListBulkCount.
     * @param mailingListBulkCount New value of property mailingListBulkCount.
     *
     */
    public void setMailingListBulkCount(int mailingListBulkCount) {
        this.mailingListBulkCount = mailingListBulkCount;
    }
    
    /** Getter for property mailingListNm.
     * @return Value of property mailingListNm.
     *
     */
    public java.lang.String getMailingListNm() {
        return mailingListNm;
    }
    
    /** Setter for property mailingListNm.
     * @param mailingListNm New value of property mailingListNm.
     *
     */
    public void setMailingListNm(java.lang.String mailingListNm) {
        this.mailingListNm = mailingListNm;
    }
    
    /** Getter for property mailingListPk.
     * @return Value of property mailingListPk.
     *
     */
    public java.lang.Integer getMailingListPk() {
        return mailingListPk;
    }
    
    /** Setter for property mailingListPk.
     * @param mailingListPk New value of property mailingListPk.
     *
     */
    public void setMailingListPk(java.lang.Integer mailingListPk) {
        this.mailingListPk = mailingListPk;
    }   
            
    /** Getter for property locationNm.
     * @return Value of property locationNm.
     *
     */
    public java.lang.String getLocationNm() {
        return locationNm;
    }
    
    /** Setter for property locationNm.
     * @param locationNm New value of property locationNm.
     *
     */
    public void setLocationNm(java.lang.String locationNm) {
        this.locationNm = locationNm;
    }
    
    /** Getter for property addressPk.
     * @return Value of property addressPk.
     *
     */
    public java.lang.Integer getAddressPk() {
        return addressPk;
    }
    
    /** Setter for property addressPk.
     * @param addressPk New value of property addressPk.
     *
     */
    public void setAddressPk(java.lang.Integer addressPk) {
        this.addressPk = addressPk;
    }    
    
    /** Getter for property addressType.
     * @return Value of property addressType.
     *
     */
    public java.lang.Integer getAddressType() {
        return addressType;
    }
    
    /** Setter for property addressType.
     * @param addressType New value of property addressType.
     *
     */
    public void setAddressType(java.lang.Integer addressType) {
        this.addressType = addressType;
    }

    public String toString() {
        return
        "mailingListPk=" + mailingListPk + ", " +
        "mailingListName=" + mailingListNm + ", " +
        "mailingListBulkCount=" + mailingListBulkCount + "," +
        "addressPk=" + addressPk + "," +
        "addressType=" + addressType + "," +
        "locationName=" + locationNm;
    }                
}
