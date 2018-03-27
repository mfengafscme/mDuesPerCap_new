package org.afscme.enterprise.returnedmail;

import java.sql.Timestamp;
import org.afscme.enterprise.organization.OrgAddressData;
import org.afscme.enterprise.common.RecordData;

public class ReturnedOrganizationAddress extends OrgAddressData {    
    private Integer addressPK;
    private Integer orgPK;
    private String affAbbreviatedName;
    private String exceptionReason; 
    private String addrTypeDescr;
    private RecordData theRecordData;    
    
    /** Getter for property addressPK.
     * @return Value of property addressPK.
     *
     */
    public java.lang.Integer getAddressPK() {
        return addressPK;
    }
    
    /** Setter for property addressPK.
     * @param addressPK New value of property addressPK.
     *
     */
    public void setAddressPK(java.lang.Integer addressPK) {
        this.addressPK = addressPK;
    }
    
    /** Getter for property exceptionReason.
     * @return Value of property exceptionReason.
     *
     */
    public java.lang.String getExceptionReason() {
        return exceptionReason;
    }
    
    /** Setter for property exceptionReason.
     * @param exceptionReason New value of property exceptionReason.
     *
     */
    public void setExceptionReason(java.lang.String exceptionReason) {
        this.exceptionReason = exceptionReason;
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
    
    /** Getter for property affAbbreviatedName.
     * @return Value of property affAbbreviatedName.
     *
     */
    public java.lang.String getAffAbbreviatedName() {
        return affAbbreviatedName;
    }    

    /** Setter for property affAbbreviatedName.
     * @param affAbbreviatedName New value of property affAbbreviatedName.
     *
     */
    public void setAffAbbreviatedName(java.lang.String affAbbreviatedName) {
        this.affAbbreviatedName = affAbbreviatedName;
    }
         
    /** Getter for property addrTypeDescr.
     * @return Value of property addrTypeDescr.
     *
     */
    public java.lang.String getAddrTypeDescr() {
        return addrTypeDescr;
    }
    
    /** Setter for property addrTypeDescr.
     * @param addrTypeDescr New value of property addrTypeDescr.
     *
     */
    public void setAddrTypeDescr(java.lang.String addrTypeDescr) {
        this.addrTypeDescr = addrTypeDescr;
    }

    /** Getter for property theRecordData.
     * @return Value of property theRecordData.
     *
     */
    public org.afscme.enterprise.common.RecordData getTheRecordData() {
        return theRecordData;
    }
    
    /** Setter for property theRecordData.
     * @param theRecordData New value of property theRecordData.
     *
     */
    public void setTheRecordData(org.afscme.enterprise.common.RecordData theRecordData) {
        this.theRecordData = theRecordData;
    }    
}
