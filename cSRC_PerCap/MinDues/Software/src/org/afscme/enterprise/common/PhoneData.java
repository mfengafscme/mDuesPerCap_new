package org.afscme.enterprise.common;

import java.sql.Timestamp;

public class PhoneData
{
    protected String countryCode;
    protected String areaCode;
    protected String phoneNumber;
    protected Boolean phoneBadFlag;
    protected Timestamp phoneBadDate;
    protected String phoneExtension;
    private RecordData theRecordData;
    private Boolean phonePrmryFg;
    private Boolean phonePrivateFg;
    private Integer phonePk;
    private Integer dept;
    private Integer phoneType;
    private Boolean phoneDoNotCallFg;

        /** Country Code for United States */
    public static final String COUNTRY_CODE_US = "1";

        /** Common Code PK for Phone Type = "HOME" */
    public static final Integer PHONE_TYPE_HOME = new Integer(3001);

    public PhoneData () {
        this.phonePk = null;
        this.countryCode = null;
        this.areaCode = null;
        this.phoneNumber = null;
        this.phoneExtension = null;
        this.dept = null;
        this.phoneType = null;
        this.phonePrmryFg = new Boolean(false);
        this.phonePrivateFg = new Boolean(false);
        this.phoneBadFlag = new Boolean(false);
        this.phoneBadDate = null;
        this.phoneDoNotCallFg = new Boolean(false);
    }

    /** Getter for property areaCode.
     * @return Value of property areaCode.
     *
     */
    public java.lang.String getAreaCode() {
        return areaCode;
    }

    /** Setter for property areaCode.
     * @param areaCode New value of property areaCode.
     *
     */
    public void setAreaCode(java.lang.String areaCode) {
        this.areaCode = areaCode;
    }

    /** Getter for property dept.
     * @return Value of property dept.
     *
     */
    public java.lang.Integer getDept() {
        return dept;
    }

    /** Setter for property dept.
     * @param dept New value of property dept.
     *
     */
    public void setDept(java.lang.Integer dept) {
        this.dept = dept;
    }

    /** Getter for property phoneBadDate.
     * @return Value of property phoneBadDate.
     *
     */
    public java.sql.Timestamp getPhoneBadDate() {
        return phoneBadDate;
    }

    /** Setter for property phoneBadDate.
     * @param phoneBadDate New value of property phoneBadDate.
     *
     */
    public void setPhoneBadDate(java.sql.Timestamp phoneBadDate) {
        this.phoneBadDate = phoneBadDate;
    }

    /** Getter for property phoneBadFlag.
     * @return Value of property phoneBadFlag.
     *
     */
    public java.lang.Boolean getPhoneBadFlag() {
        return phoneBadFlag;
    }

    /** Setter for property phoneBadFlag.
     * @param phoneBadFlag New value of property phoneBadFlag.
     *
     */
    public void setPhoneBadFlag(java.lang.Boolean phoneBadFlag) {
        this.phoneBadFlag = phoneBadFlag;
    }

    /** Getter for property phoneDoNotCallFg.
     * @return Value of property phoneDoNotCallFg.
     *
     */
    public java.lang.Boolean getPhoneDoNotCallFg() {
        return phoneDoNotCallFg;
    }

    /** Setter for property phoneDoNotCallFg.
     * @param phoneDoNotCallFg New value of property phoneDoNotCallFg.
     *
     */
    public void setPhoneDoNotCallFg(java.lang.Boolean phoneDoNotCallFg) {
        this.phoneDoNotCallFg = phoneDoNotCallFg;
    }

    /** Getter for property phoneExtension.
     * @return Value of property phoneExtension.
     *
     */
    public java.lang.String getPhoneExtension() {
        return phoneExtension;
    }

    /** Setter for property phoneExtension.
     * @param phoneExtension New value of property phoneExtension.
     *
     */
    public void setPhoneExtension(java.lang.String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    /** Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     *
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }

    /** Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     *
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /** Getter for property phonePk.
     * @return Value of property phonePk.
     *
     */
    public java.lang.Integer getPhonePk() {
        return phonePk;
    }

    /** Setter for property phonePk.
     * @param phonePk New value of property phonePk.
     *
     */
    public void setPhonePk(java.lang.Integer phonePk) {
        this.phonePk = phonePk;
    }

    /** Getter for property phonePrivateFg.
     * @return Value of property phonePrivateFg.
     *
     */
    public java.lang.Boolean getPhonePrivateFg() {
        return phonePrivateFg;
    }

    /** Setter for property phonePrivateFg.
     * @param phonePrivateFg New value of property phonePrivateFg.
     *
     */
    public void setPhonePrivateFg(java.lang.Boolean phonePrivateFg) {
        this.phonePrivateFg = phonePrivateFg;
    }

    /** Getter for property phonePrmryFg.
     * @return Value of property phonePrmryFg.
     *
     */
    public java.lang.Boolean getPhonePrmryFg() {
        return phonePrmryFg;
    }

    /** Setter for property phonePrmryFg.
     * @param phonePrmryFg New value of property phonePrmryFg.
     *
     */
    public void setPhonePrmryFg(java.lang.Boolean phonePrmryFg) {
        this.phonePrmryFg = phonePrmryFg;
    }

    /** Getter for property phoneType.
     * @return Value of property phoneType.
     *
     */
    public java.lang.Integer getPhoneType() {
        return phoneType;
    }

    /** Setter for property phoneType.
     * @param phoneType New value of property phoneType.
     *
     */
    public void setPhoneType(java.lang.Integer phoneType) {
        this.phoneType = phoneType;
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

    /** Getter for property countryCode.
     * @return Value of property countryCode.
     *
     */
    public java.lang.String getCountryCode() {
        return countryCode;
    }

    /** Setter for property countryCode.
     * @param countryCode New value of property countryCode.
     *
     */
    public void setCountryCode(java.lang.String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */
    public String toString() {
        return "PhoneData[" +
        "countryCode="+countryCode+", "+
        "areaCode="+areaCode+", "+
        "phoneNumber="+phoneNumber+", "+
        "phoneBadFlag="+phoneBadFlag+", "+
        "phoneBadDate="+phoneBadDate+", "+
        "phoneExtension="+phoneExtension+", "+
        "phonePrmryFg="+phonePrmryFg+", "+
        "phonePrivateFg="+phonePrivateFg+", "+
        "phonePk="+phonePk+", "+
        "dept="+dept+", "+
        "phoneType="+phoneType+", "+
        "phoneDoNotCallFg="+phoneDoNotCallFg+", "+
        "theRecordData="+theRecordData+"]";
    }

	/**
	 * Clones an existing PhoneData object.
	 * @return The new cloned object.
     */
    public Object clone() {
		PhoneData pd = new PhoneData();
		pd.phonePk = this.phonePk;
		pd.countryCode = this.countryCode;
		pd.areaCode = this.areaCode;
		pd.phoneNumber = this.phoneNumber;
		pd.phoneExtension = this.phoneExtension;
		pd.dept = this.dept;
		pd.phoneType = this.phoneType;
		pd.phonePrmryFg = this.phonePrmryFg;
		pd.phonePrivateFg = this.phonePrivateFg;
		pd.phoneBadFlag = this.phoneBadFlag;
		pd.phoneBadDate = this.phoneBadDate;
        pd.phoneDoNotCallFg = this.phoneDoNotCallFg;
        return pd;
	}
}
