package org.afscme.enterprise.organization;

import java.sql.Timestamp;
import org.afscme.enterprise.address.Address;
import org.afscme.enterprise.util.TextUtil;


/**
 * Organization address data.
 */
public class OrgAddressData extends Address 
{
    protected Integer addressType;
    protected boolean addressBadFlag;
    protected Timestamp addressBadDate;
    
    protected String attentionLine;
    protected Integer type;
    protected boolean bad;
    protected Timestamp badDate;
    protected String locationNm;
    protected boolean primary;
    protected boolean privateFg;
    
    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */    
    public String toString() {
        return "OrgAddressData["+
        "addressType="+addressType+","+
        "addressBadFlag="+addressBadFlag+","+
        "addressBadDate="+addressBadDate+","+        
        "attentionLine="+attentionLine+","+
        "type="+addressType+","+
        "bad="+addressType+","+
        "badDate="+badDate+","+
        "locationNm="+locationNm+","+
        "primary="+primary+","+
        "privateFg="+privateFg+","+
        "super="+super.toString()+"]";
    }
    
    /**
     * Returns true if the content in this orgAddress is equal to the content of the 
     * given orgAddress.
     * @param orgAddress
     * @return boolean
     */
    public boolean equals(OrgAddressData orgAddress) 
    {
        return 
            TextUtil.equals(addr1, orgAddress.addr1) &&
            TextUtil.equals(addr2, orgAddress.addr2) &&
            TextUtil.equals(city, orgAddress.city) &&
            TextUtil.equals(county, orgAddress.county) &&
            TextUtil.equals(province, orgAddress.province) &&
            TextUtil.equals(countryPk, orgAddress.countryPk) &&
            TextUtil.equals(state, orgAddress.state) &&
            TextUtil.equals(zipCode, orgAddress.zipCode) &&
            TextUtil.equals(zipPlus, orgAddress.zipPlus) &&
            TextUtil.equals(type, orgAddress.type) &&
            TextUtil.equals(attentionLine, orgAddress.attentionLine) &&
            TextUtil.equals(new Boolean(bad), new Boolean(orgAddress.bad));
    }
    
    
    /** Getter for property addressBadDate.
     * @return Value of property addressBadDate.
     * @deprecated Use getBadDate instead.
     *
     */
    public Timestamp getAddressBadDate() {
        return addressBadDate;
    }
    
    /** Setter for property addressBadDate.
     * @param addressBadDate New value of property addressBadDate.
     * @deprecated Use setBadDate instead.
     *
     */
    public void setAddressBadDate(Timestamp addressBadDate) {
        this.addressBadDate = addressBadDate;
    }
    
    /** Getter for property addressBadFlag.
     * @return Value of property addressBadFlag.
     * @deprecated Use isBad() instead.
     *
     */
    public boolean isAddressBadFlag() {
        return addressBadFlag;
    }
    
    /** Setter for property addressBadFlag.
     * @param addressBadFlag New value of property addressBadFlag.
     * @deprecated Use getBad instead.
     *
     */
    public void setAddressBadFlag(boolean addressBadFlag) {
        this.addressBadFlag = addressBadFlag;
    }
    
    /** Getter for property addressType.
     * @return Value of property addressType.
     * @deprecated Use getType instead.
     *
     */
    public java.lang.Integer getAddressType() {
        return addressType;
    }
    
    /** Setter for property addressType.
     * @param addressType New value of property addressType.
     * @deprecated Use setType instead.
     *
     */
    public void setAddressType(java.lang.Integer addressType) {
        this.addressType = addressType;
    }
    
    /** Getter for property attentionLine.
     * @return Value of property attentionLine.
     *
     */
    public java.lang.String getAttentionLine() {
        return attentionLine;
    }
    
    /** Setter for property attentionLine.
     * @param attentionLine New value of property attentionLine.
     *
     */
    public void setAttentionLine(java.lang.String attentionLine) {
        this.attentionLine = attentionLine;
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
    
    /** Getter for property primary.
     * @return Value of property primary.
     *
     */
    public boolean isPrimary() {
        return primary;
    }

    /** Getter for property primary.
     * @return Value of property primary.
     *
     */
    public boolean getPrimary() {
        return this.primary;
    }
    
    /** Setter for property primary.
     * @param primary New value of property primary.
     *
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
    
    /** Getter for property privateFg.
     * @return Value of property privateFg.
     *
     */
    public boolean isPrivate() {
        return privateFg;
    }

    /** Getter for property privateFg.
     * @return Value of property privateFg.
     *
     */
    public boolean getPrivate() {
        return this.privateFg;
    }
    
    /** Setter for property privateFg.
     * @param privateFg New value of property privateFg.
     *
     */
    public void setPrivate(boolean privateFg) {
        this.privateFg = privateFg;
    }
    
    /** Getter for property bad.
     * @return Value of property bad.
     *
     */
    public boolean isBad() {
        return bad;
    }
    
    /** Getter for property bad.
     * @return Value of property bad.
     *
     */
    public boolean getBad() {
        return this.bad;
    }

    /** Setter for property bad.
     * @param bad New value of property bad.
     *
     */
    public void setBad(boolean bad) {
        this.bad = bad;
    }
    
    /** Getter for property badDate.
     * @return Value of property badDate.
     *
     */
    public Timestamp getBadDate() {
        return badDate;
    }
    
    /** Setter for property badDate.
     * @param badDate New value of property badDate.
     *
     */
    public void setBadDate(Timestamp badDate) {
        this.badDate = badDate;
    }
    
    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public java.lang.Integer getType() {
        return type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(java.lang.Integer type) {
        this.type = type;
    }
}
