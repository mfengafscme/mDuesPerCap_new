package org.afscme.enterprise.organization.web;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import org.apache.struts.action.ActionForm;
import org.afscme.enterprise.codes.Codes.OrgAddressType;
import org.afscme.enterprise.codes.Codes.OrgPhoneType;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrgAddressRecord;
import org.afscme.enterprise.organization.OrgPhoneData;
import org.afscme.enterprise.util.TextUtil;


/**
 * Represents the form that holds the data for a Location
 *
 * @struts:form name="locationForm"
 */
public class LocationForm extends ActionForm {

    /** OrgLocationPK */
    private Integer pk; 
    
    /** General Location information */
    private String m_locationTitle;
    private boolean m_locationPrimary;
    private Integer m_orgPK;    

    /** Location Regular Address fields */
    private Integer m_regularLocationAddressPK;
    private String m_regularLocationAttention;
    private String m_regularLocationAddress1;
    private String m_regularLocationAddress2;
    private String m_regularLocationCity;
    private String m_regularLocationState;
    private String m_regularLocationZip;
    private String m_regularLocationZip4;
    private String m_regularLocationCounty;
    private String m_regularLocationProvince;
    private Integer m_regularLocationCountry;
    private boolean m_regularLocationAddressBad;
    private Timestamp m_regularLocationAddressBadDate;

    /** Location Shipping Address fields */
    private Integer m_shippingLocationAddressPK;    
    private String m_shippingLocationAttention;
    private String m_shippingLocationAddress1;
    private String m_shippingLocationAddress2;
    private String m_shippingLocationCity;
    private String m_shippingLocationState;
    private String m_shippingLocationZip;
    private String m_shippingLocationZip4;
    private String m_shippingLocationCounty;
    private String m_shippingLocationProvince;
    private Integer m_shippingLocationCountry;
    private boolean m_shippingLocationAddressBad;
    private Timestamp m_shippingLocationAddressBadDate;

    /** Location Office Phone fields */
    private Integer m_officeLocationPhonePK; 
    private String m_officeLocationPhoneCountryCode;
    private String m_officeLocationPhoneAreaCode;
    private String m_officeLocationPhoneNumber;
    private boolean m_officeLocationPhoneBad;
    private Timestamp m_officeLocationPhoneBadDate;
    
    /** Location Fax Phone fields */
    private Integer m_faxLocationPhonePK;     
    private String m_faxLocationPhoneCountryCode;
    private String m_faxLocationPhoneAreaCode;
    private String m_faxLocationPhoneNumber;
    private boolean m_faxLocationPhoneBad;
    private Timestamp m_faxLocationPhoneBadDate;
    
    private RecordData recordData;    

    /** TRUE if orgPk is an Affiliate */
    private boolean isAffiliatePk;
    
    private String back;
	    

    /** Creates a new instance of LocationForm */
    public LocationForm() {
            
        // default office phone country code to 1 (United States) if not filled in
        m_officeLocationPhoneCountryCode = "1";
    }

    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("orgLocationPK: " + pk); 
        buf.append(", locationTitle: " + m_locationTitle); 
        buf.append(", locationPrimary: " + m_locationPrimary);
        buf.append(", orgPK: " + m_orgPK);        
        buf.append(", regularLocationAddressPK: " + m_regularLocationAddressPK);
        buf.append(", regularLocationAttention: " + m_regularLocationAttention); 
        buf.append(", regularLocationAddress1: " + m_regularLocationAddress1); 
        buf.append(", regularLocationAddress2: " + m_regularLocationAddress2); 
        buf.append(", regularLocationCity: " + m_regularLocationCity); 
        buf.append(", regularLocationState: " + m_regularLocationState); 
        buf.append(", regularLocationZip: " + m_regularLocationZip); 
        buf.append(", regularLocationZip4: " + m_regularLocationZip4); 
        buf.append(", regularLocationCounty: " + m_regularLocationCounty); 
        buf.append(", regularLocationProvince: " + m_regularLocationProvince); 
        buf.append(", regularLocationCountry: " + m_regularLocationCountry); 
        buf.append(", regularLocationAddressBad: " + m_regularLocationAddressBad); 
        buf.append(", regularLocationAddressBadDate: " + m_regularLocationAddressBadDate);
        buf.append(", shippingLocationAddressPK: " + m_shippingLocationAddressPK);
        buf.append(", shippingLocationAttention: " + m_shippingLocationAttention); 
        buf.append(", shippingLocationAddress1: " + m_shippingLocationAddress1); 
        buf.append(", shippingLocationAddress2: " + m_shippingLocationAddress2); 
        buf.append(", shippingLocationCity: " + m_shippingLocationCity); 
        buf.append(", shippingLocationState: " + m_shippingLocationState); 
        buf.append(", shippingLocationZip: " + m_shippingLocationZip); 
        buf.append(", shippingLocationZip4: " + m_shippingLocationZip4); 
        buf.append(", shippingLocationCounty: " + m_shippingLocationCounty); 
        buf.append(", shippingLocationProvince: " + m_shippingLocationProvince); 
        buf.append(", shippingLocationCountry: " + m_shippingLocationCountry); 
        buf.append(", shippingLocationAddressBad: " + m_shippingLocationAddressBad); 
        buf.append(", shippingLocationAddressBadDate: " + m_shippingLocationAddressBadDate);
        buf.append(", officeLocationPhonePK: " + m_officeLocationPhonePK);
        buf.append(", officeLocationPhoneCountryCode: " + m_officeLocationPhoneCountryCode);
        buf.append(", officeLocationPhoneAreaCode: " + m_officeLocationPhoneAreaCode);
        buf.append(", officeLocationPhoneNumber: " + m_officeLocationPhoneNumber);
        buf.append(", officeLocationPhoneBad: " + m_officeLocationPhoneBad);
        buf.append(", officeLocationPhoneBadDate: " + m_officeLocationPhoneBadDate);
        buf.append(", faxLocationPhonePK: " + m_faxLocationPhonePK);
        buf.append(", faxLocationPhoneCountryCode: " + m_faxLocationPhoneCountryCode);
        buf.append(", faxLocationPhoneAreaCode: " + m_faxLocationPhoneAreaCode);
        buf.append(", faxLocationPhoneNumber: " + m_faxLocationPhoneNumber);
        buf.append(", faxLocationPhoneBad: " + m_faxLocationPhoneBad);
        buf.append(", faxLocationPhoneBadDate: " + m_faxLocationPhoneBadDate);
        buf.append(", recordData: " + recordData);
        buf.append(", back: " + back);        
        return buf.toString()+"]";
    }


    /**
     * Returns true if this form is being used for an add operation (as opposed to edit)
     */
    public boolean isAdd() {
        return pk == null || pk.intValue() == 0;
    }

    /** Getter for property pk.
     * @return Value of property pk.
     *
     */
    public Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     *
     */
    public void setPk(Integer pk) {
        this.pk = pk;
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
    
    /**
     * getLocationData method to copy all the form data fields 
     * to the location object for processing 
     */
    public LocationData getLocationData() {
        
        LocationData data = new LocationData();

        // general location data 
        data.setOrgLocationPK(pk);
        data.setOrgPk(m_orgPK);
        data.setLocationNm(m_locationTitle);
        data.setPrimaryLocationBoolean(new Boolean(m_locationPrimary));

        LinkedList addressList = null;
        
        // check if any data filled in for regular address before placing it in list or 
        // it is an existing one being blanked out
        if ((hasRegularAddress()) || 
            (m_regularLocationAddressPK != null) && (m_regularLocationAddressPK.intValue() != 0)) {
                
            OrgAddressRecord regAddress = new OrgAddressRecord();
            regAddress = getAddressFromRegularAddress();
            
            if (m_regularLocationAddressBad == true)
                regAddress.setBad(m_regularLocationAddressBad);

            regAddress.setType(OrgAddressType.REGULAR);
            RecordData recordData = new RecordData();
            recordData.setPk(m_regularLocationAddressPK);
            regAddress.setRecordData(recordData);

            addressList = new LinkedList();
            addressList.add(regAddress);
        }
        
        // check if any data filled in for shipping address before placing it in list or
        // it is an existing one being blanked out
        if ((hasShippingAddress()) || 
            (m_shippingLocationAddressPK != null) && (m_shippingLocationAddressPK.intValue() != 0)) {

            OrgAddressRecord shipAddress = new OrgAddressRecord();
            shipAddress = getAddressFromShippingAddress();
            
            if (m_shippingLocationAddressBad == true)
                shipAddress.setBad(m_shippingLocationAddressBad);
            
            shipAddress.setType(OrgAddressType.SHIPPING);
            RecordData recordData = new RecordData();
            recordData.setPk(m_shippingLocationAddressPK);
            shipAddress.setRecordData(recordData);

            if (addressList == null) 
                addressList = new LinkedList();
            addressList.add(shipAddress);
        }

        // add the address linked list if address if filled in
        if (addressList != null) 
            data.setOrgAddressData(addressList);

        
        LinkedList phoneList = null;
        
        // check if any data filled in for office phone before placing it in list or 
        // it is an existing one being blanked out
        if ((hasOfficePhone()) || 
            (m_officeLocationPhonePK != null) && (m_officeLocationPhonePK.intValue() != 0)) {

            OrgPhoneData officePhone = new OrgPhoneData();
            
            if (!TextUtil.isEmptyOrSpaces(m_officeLocationPhoneCountryCode))
                officePhone.setCountryCode(m_officeLocationPhoneCountryCode);
            if (!TextUtil.isEmptyOrSpaces(m_officeLocationPhoneAreaCode))
                officePhone.setAreaCode(m_officeLocationPhoneAreaCode);
            officePhone.setPhoneNumber(m_officeLocationPhoneNumber);
            
            if (m_officeLocationPhoneBad == true)            
                officePhone.setPhoneBadFlag(new Boolean(m_officeLocationPhoneBad));
            
            officePhone.setPhoneType(OrgPhoneType.LOC_PHONE_OFFICE);
            RecordData recordData = new RecordData();
            recordData.setPk(m_officeLocationPhonePK);
            officePhone.setTheRecordData(recordData);

            phoneList = new LinkedList();
            phoneList.add(officePhone);
        }
        
        // check if any data filled in for fax phone before placing it in list or 
        // it is an existing one being blanked out
        if ((hasFaxPhone()) || 
            (m_faxLocationPhonePK != null) && (m_faxLocationPhonePK.intValue() != 0)) {
        
            OrgPhoneData faxPhone = new OrgPhoneData();
            
            if (!TextUtil.isEmptyOrSpaces(m_faxLocationPhoneCountryCode))            
                faxPhone.setCountryCode(m_faxLocationPhoneCountryCode);
            if (!TextUtil.isEmptyOrSpaces(m_faxLocationPhoneAreaCode))
                faxPhone.setAreaCode(m_faxLocationPhoneAreaCode);
            faxPhone.setPhoneNumber(m_faxLocationPhoneNumber);
            
            if (m_faxLocationPhoneBad == true) 
                faxPhone.setPhoneBadFlag(new Boolean(m_faxLocationPhoneBad));
            
            faxPhone.setPhoneType(OrgPhoneType.LOC_PHONE_FAX);
            RecordData recordData = new RecordData();
            recordData.setPk(m_faxLocationPhonePK);
            faxPhone.setTheRecordData(recordData);            

            if (phoneList == null) {
                phoneList = new LinkedList();
            }    
            phoneList.add(faxPhone);
        }
            
        // add the phone linked list if phone if filled in
        if (phoneList != null) 
            data.setOrgPhoneData(phoneList);

        return data;
    }

    /**
     * setLocationData method to copy all the location object fields
     * to the form data fields for screen display
     */    
    public void setLocationData(LocationData data) {
        
        // general location data 
        pk = data.getOrgLocationPK();
        m_orgPK = data.getOrgPk();
        m_locationTitle = data.getLocationNm();
        m_locationPrimary = data.getPrimaryLocationBoolean().booleanValue();
        recordData = data.getRecordData();
        
        // check if any data in the database for addresses before placing it in form fields
        if (data.getOrgAddressData() != null) { 

            LinkedList addressList = data.getOrgAddressData();
            Iterator it = addressList.iterator();
            while (it.hasNext()) {
                
                OrgAddressRecord item = (OrgAddressRecord)it.next();
                if (item.getType().equals(OrgAddressType.REGULAR)) {
                    copyRegularAddress(item);
                } else if (item.getType().equals(OrgAddressType.SHIPPING)) {
                    copyShippingAddress(item);
                } 
            }            
        }
       
        // check if any data in the database for phones before placing it in form fields
        if (data.getOrgPhoneData() != null) { 

            LinkedList phoneList = data.getOrgPhoneData();
            Iterator it = phoneList.iterator();
            while (it.hasNext()) {
                
                OrgPhoneData item = (OrgPhoneData)it.next();
                if (item.getPhoneType().equals(OrgPhoneType.LOC_PHONE_OFFICE)) {
                    copyOfficePhone(item);
                } else if (item.getPhoneType().equals(OrgPhoneType.LOC_PHONE_FAX)) {
                    copyFaxPhone(item);
                } 
            }            
        }

        // default office phone country code to 1 (United States) if not filled in
        if (TextUtil.isEmptyOrSpaces(m_officeLocationPhoneCountryCode))
            setOfficeLocationPhoneCountryCode("1");
    }
    
    /** Getter for property m_orgPK.
     * @return Value of property m_orgPK.
     *
     */
    public java.lang.Integer getOrgPK() {
        return m_orgPK;
    }
    
    /** Setter for property m_orgPK.
     * @param orgPK New value of property m_orgPK.
     *
     */
    public void setOrgPK(java.lang.Integer orgPK) {
        this.m_orgPK = orgPK;
    }
    
    /** Getter for property m_locationTitle.
     * @return Value of property m_locationTitle.
     *
     */
    public java.lang.String getLocationTitle() {
        return m_locationTitle;
    }
    
    /** Setter for property m_locationTitle.
     * @param locationTitle New value of property m_locationTitle.
     *
     */
    public void setLocationTitle(java.lang.String locationTitle) {
        this.m_locationTitle = locationTitle;
    }
    
    /** Getter for property m_locationPrimary.
     * @return Value of property m_locationPrimary.
     *
     */
    public boolean isLocationPrimary() {
        return m_locationPrimary;
    }
    
    /** Setter for property m_locationPrimary.
     * @param locationPrimary New value of property m_locationPrimary.
     *
     */
    public void setLocationPrimary(boolean locationPrimary) {
        this.m_locationPrimary = locationPrimary;
    }
    
    /** Getter for property m_regularLocationAddressPK.
     * @return Value of property m_regularLocationAddressPK.
     *
     */
    public java.lang.Integer getRegularLocationAddressPK() {
        return m_regularLocationAddressPK;
    }
    
    /** Setter for property m_regularLocationAddressPK.
     * @param regularLocationAddressPK New value of property m_regularLocationAddressPK.
     *
     */
    public void setRegularLocationAddressPK(java.lang.Integer regularLocationAddressPK) {
        this.m_regularLocationAddressPK = regularLocationAddressPK;
    }
    
    /** Getter for property m_regularLocationAddress1.
     * @return Value of property m_regularLocationAddress1.
     *
     */
    public java.lang.String getRegularLocationAddress1() {
        return m_regularLocationAddress1;
    }
    
    /** Setter for property m_regularLocationAddress1.
     * @param regularLocationAddress1 New value of property m_regularLocationAddress1.
     *
     */
    public void setRegularLocationAddress1(java.lang.String regularLocationAddress1) {
        this.m_regularLocationAddress1 = regularLocationAddress1;
    }
    
    /** Getter for property m_regularLocationAddress2.
     * @return Value of property m_regularLocationAddress2.
     *
     */
    public java.lang.String getRegularLocationAddress2() {
        return m_regularLocationAddress2;
    }
    
    /** Setter for property m_regularLocationAddress2.
     * @param regularLocationAddress2 New value of property m_regularLocationAddress2.
     *
     */
    public void setRegularLocationAddress2(java.lang.String regularLocationAddress2) {
        this.m_regularLocationAddress2 = regularLocationAddress2;
    }
    
    /** Getter for property m_regularLocationAddressBad.
     * @return Value of property m_regularLocationAddressBad.
     *
     */
    public boolean isRegularLocationAddressBad() {
        return m_regularLocationAddressBad;
    }
    
    /** Setter for property m_regularLocationAddressBad.
     * @param regularLocationAddressBad New value of property m_regularLocationAddressBad.
     *
     */
    public void setRegularLocationAddressBad(boolean regularLocationAddressBad) {
        this.m_regularLocationAddressBad = regularLocationAddressBad;
    }
    
    /** Getter for property m_regularLocationAddressBadDate.
     * @return Value of property m_regularLocationAddressBadDate.
     *
     */
    public Timestamp getRegularLocationAddressBadDate() {
        return m_regularLocationAddressBadDate;
    }
    
    /** Setter for property m_regularLocationAddressBadDate.
     * @param regularLocationAddressBadDate New value of property m_regularLocationAddressBadDate.
     *
     */
    public void setRegularLocationAddressBadDate(Timestamp regularLocationAddressBadDate) {
        this.m_regularLocationAddressBadDate = regularLocationAddressBadDate;
    }
    
    /** Getter for property m_regularLocationAttention.
     * @return Value of property m_regularLocationAttention.
     *
     */
    public java.lang.String getRegularLocationAttention() {
        return m_regularLocationAttention;
    }
    
    /** Setter for property m_regularLocationAttention.
     * @param regularLocationAttention New value of property m_regularLocationAttention.
     *
     */
    public void setRegularLocationAttention(java.lang.String regularLocationAttention) {
        this.m_regularLocationAttention = regularLocationAttention;
    }
    
    /** Getter for property m_regularLocationCity.
     * @return Value of property m_regularLocationCity.
     *
     */
    public java.lang.String getRegularLocationCity() {
        return m_regularLocationCity;
    }
    
    /** Setter for property m_regularLocationCity.
     * @param regularLocationCity New value of property m_regularLocationCity.
     *
     */
    public void setRegularLocationCity(java.lang.String regularLocationCity) {
        this.m_regularLocationCity = regularLocationCity;
    }
    
    /** Getter for property m_regularLocationCountry.
     * @return Value of property m_regularLocationCountry.
     *
     */
    public java.lang.Integer getRegularLocationCountry() {
        return m_regularLocationCountry;
    }
    
    /** Setter for property m_regularLocationCountry.
     * @param regularLocationCountry New value of property m_regularLocationCountry.
     *
     */
    public void setRegularLocationCountry(java.lang.Integer regularLocationCountry) {
        this.m_regularLocationCountry = regularLocationCountry;
    }
    
    /** Getter for property m_regularLocationCounty.
     * @return Value of property m_regularLocationCounty.
     *
     */
    public java.lang.String getRegularLocationCounty() {
        return m_regularLocationCounty;
    }
    
    /** Setter for property m_regularLocationCounty.
     * @param regularLocationCounty New value of property m_regularLocationCounty.
     *
     */
    public void setRegularLocationCounty(java.lang.String regularLocationCounty) {
        this.m_regularLocationCounty = regularLocationCounty;
    }
    
    /** Getter for property m_regularLocationProvince.
     * @return Value of property m_regularLocationProvince.
     *
     */
    public java.lang.String getRegularLocationProvince() {
        return m_regularLocationProvince;
    }
    
    /** Setter for property m_regularLocationProvince.
     * @param regularLocationProvince New value of property m_regularLocationProvince.
     *
     */
    public void setRegularLocationProvince(java.lang.String regularLocationProvince) {
        this.m_regularLocationProvince = regularLocationProvince;
    }
    
    /** Getter for property m_regularLocationState.
     * @return Value of property m_regularLocationState.
     *
     */
    public String getRegularLocationState() {
        return m_regularLocationState;
    }
    
    /** Setter for property m_regularLocationState.
     * @param egularLocationState New value of property m_regularLocationState.
     *
     */
    public void setRegularLocationState(String regularLocationState) {
        this.m_regularLocationState = regularLocationState;
    }
    
    /** Getter for property m_regularLocationZip.
     * @return Value of property m_regularLocationZip.
     *
     */
    public java.lang.String getRegularLocationZip() {
        return m_regularLocationZip;
    }
    
    /** Setter for property m_regularLocationZip.
     * @param m_regularLocationZip New value of property m_regularLocationZip.
     *
     */
    public void setRegularLocationZip(java.lang.String regularLocationZip) {
        this.m_regularLocationZip = regularLocationZip;
    }
    
    /** Getter for property m_regularLocationZip4.
     * @return Value of property m_regularLocationZip4.
     *
     */
    public java.lang.String getRegularLocationZip4() {
        return m_regularLocationZip4;
    }
    
    /** Setter for property m_regularLocationZip4.
     * @param egularLocationZip4 New value of property m_regularLocationZip4.
     *
     */
    public void setRegularLocationZip4(java.lang.String regularLocationZip4) {
        this.m_regularLocationZip4 = regularLocationZip4;
    }

    /** Getter for property m_shippingLocationAddressPK.
     * @return Value of property m_shippingLocationAddressPK.
     *
     */
    public java.lang.Integer getShippingLocationAddressPK() {
        return m_shippingLocationAddressPK;
    }
    
    /** Setter for property m_shippingLocationAddressPK.
     * @param shippingLocationAddressPK New value of property m_shippingLocationAddressPK.
     *
     */
    public void setShippingLocationAddressPK(java.lang.Integer shippingLocationAddressPK) {
        this.m_shippingLocationAddressPK = shippingLocationAddressPK;
    }
    
    /** Getter for property m_shippingLocationAddress1.
     * @return Value of property m_shippingLocationAddress1.
     *
     */
    public java.lang.String getShippingLocationAddress1() {
        return m_shippingLocationAddress1;
    }
    
    /** Setter for property m_shippingLocationAddress1.
     * @param shippingLocationAddress1 New value of property m_shippingLocationAddress1.
     *
     */
    public void setShippingLocationAddress1(java.lang.String shippingLocationAddress1) {
        this.m_shippingLocationAddress1 = shippingLocationAddress1;
    }
    
    /** Getter for property m_shippingLocationAddress2.
     * @return Value of property m_shippingLocationAddress2.
     *
     */
    public java.lang.String getShippingLocationAddress2() {
        return m_shippingLocationAddress2;
    }
    
    /** Setter for property m_shippingLocationAddress2.
     * @param shippingLocationAddress2 New value of property m_shippingLocationAddress2.
     *
     */
    public void setShippingLocationAddress2(java.lang.String shippingLocationAddress2) {
        this.m_shippingLocationAddress2 = shippingLocationAddress2;
    }
    
    /** Getter for property m_shippingLocationAddressBad.
     * @return Value of property m_shippingLocationAddressBad.
     *
     */
    public boolean isShippingLocationAddressBad() {
        return m_shippingLocationAddressBad;
    }
    
    /** Setter for property m_shippingLocationAddressBad.
     * @param shippingLocationAddressBad New value of property m_shippingLocationAddressBad.
     *
     */
    public void setShippingLocationAddressBad(boolean shippingLocationAddressBad) {
        this.m_shippingLocationAddressBad = shippingLocationAddressBad;
    }
    
    /** Getter for property m_shippingLocationAddressBadDate.
     * @return Value of property m_shippingLocationAddressBadDate.
     *
     */
    public Timestamp getShippingLocationAddressBadDate() {
        return m_shippingLocationAddressBadDate;
    }
    
    /** Setter for property m_shippingLocationAddressBadDate.
     * @param shippingLocationAddressBadDate New value of property m_shippingLocationAddressBadDate.
     *
     */
    public void setShippingLocationAddressBadDate(Timestamp shippingLocationAddressBadDate) {
        this.m_shippingLocationAddressBadDate = shippingLocationAddressBadDate;
    }
    
    /** Getter for property m_shippingLocationAttention.
     * @return Value of property m_shippingLocationAttention.
     *
     */
    public java.lang.String getShippingLocationAttention() {
        return m_shippingLocationAttention;
    }
    
    /** Setter for property m_shippingLocationAttention.
     * @param shippingLocationAttention New value of property m_shippingLocationAttention.
     *
     */
    public void setShippingLocationAttention(java.lang.String shippingLocationAttention) {
        this.m_shippingLocationAttention = shippingLocationAttention;
    }
    
    /** Getter for property m_shippingLocationCity.
     * @return Value of property m_shippingLocationCity.
     *
     */
    public java.lang.String getShippingLocationCity() {
        return m_shippingLocationCity;
    }
    
    /** Setter for property m_shippingLocationCity.
     * @param m_shippingLocationCity New value of property m_shippingLocationCity.
     *
     */
    public void setShippingLocationCity(java.lang.String shippingLocationCity) {
        this.m_shippingLocationCity = shippingLocationCity;
    }
    
    /** Getter for property m_shippingLocationCountry.
     * @return Value of property m_shippingLocationCountry.
     *
     */
    public java.lang.Integer getShippingLocationCountry() {
        return m_shippingLocationCountry;
    }
    
    /** Setter for property m_shippingLocationCountry.
     * @param shippingLocationCountry New value of property m_shippingLocationCountry.
     *
     */
    public void setShippingLocationCountry(java.lang.Integer shippingLocationCountry) {
        this.m_shippingLocationCountry = shippingLocationCountry;
    }
    
    /** Getter for property m_shippingLocationCounty.
     * @return Value of property m_shippingLocationCounty.
     *
     */
    public java.lang.String getShippingLocationCounty() {
        return m_shippingLocationCounty;
    }
    
    /** Setter for property m_shippingLocationCounty.
     * @param shippingLocationCounty New value of property m_shippingLocationCounty.
     *
     */
    public void setShippingLocationCounty(java.lang.String shippingLocationCounty) {
        this.m_shippingLocationCounty = shippingLocationCounty;
    }
    
    /** Getter for property m_shippingLocationProvince.
     * @return Value of property m_shippingLocationProvince.
     *
     */
    public java.lang.String getShippingLocationProvince() {
        return m_shippingLocationProvince;
    }
    
    /** Setter for property m_shippingLocationProvince.
     * @param shippingLocationProvince New value of property m_shippingLocationProvince.
     *
     */
    public void setShippingLocationProvince(java.lang.String shippingLocationProvince) {
        this.m_shippingLocationProvince = shippingLocationProvince;
    }
    
    /** Getter for property m_shippingLocationState.
     * @return Value of property m_shippingLocationState.
     *
     */
    public String getShippingLocationState() {
        return m_shippingLocationState;
    }
    
    /** Setter for property m_shippingLocationState.
     * @param shippingLocationState New value of property m_shippingLocationState.
     *
     */
    public void setShippingLocationState(String shippingLocationState) {
        this.m_shippingLocationState = shippingLocationState;
    }
    
    /** Getter for property m_shippingLocationZip.
     * @return Value of property m_shippingLocationZip.
     *
     */
    public java.lang.String getShippingLocationZip() {
        return m_shippingLocationZip;
    }
    
    /** Setter for property m_shippingLocationZip.
     * @param shippingLocationZip New value of property m_shippingLocationZip.
     *
     */
    public void setShippingLocationZip(java.lang.String shippingLocationZip) {
        this.m_shippingLocationZip = shippingLocationZip;
    }
    
    /** Getter for property m_shippingLocationZip4.
     * @return Value of property m_shippingLocationZip4.
     *
     */
    public java.lang.String getShippingLocationZip4() {
        return m_shippingLocationZip4;
    }
    
    /** Setter for property m_shippingLocationZip4.
     * @param shippingLocationZip4 New value of property m_shippingLocationZip4.
     *
     */
    public void setShippingLocationZip4(java.lang.String shippingLocationZip4) {
        this.m_shippingLocationZip4 = shippingLocationZip4;
    }
    
    /** Getter for property m_officeLocationPhonePK.
     * @return Value of property m_officeLocationPhonePK.
     *
     */
    public java.lang.Integer getOfficeLocationPhonePK() {
        return m_officeLocationPhonePK;
    }
    
    /** Setter for property m_officeLocationPhonePK.
     * @param officeLocationPhonePK New value of property m_officeLocationPhonePK.
     *
     */
    public void setOfficeLocationPhonePK(java.lang.Integer officeLocationPhonePK) {
        this.m_officeLocationPhonePK = officeLocationPhonePK;
    }
    
    /** Getter for property m_officeLocationPhoneAreaCode.
     * @return Value of property m_officeLocationPhoneAreaCode.
     *
     */
    public java.lang.String getOfficeLocationPhoneAreaCode() {
        return m_officeLocationPhoneAreaCode;
    }
    
    /** Setter for property m_officeLocationPhoneAreaCode.
     * @param officeLocationPhoneAreaCode New value of property m_officeLocationPhoneAreaCode.
     *
     */
    public void setOfficeLocationPhoneAreaCode(java.lang.String officeLocationPhoneAreaCode) {
        this.m_officeLocationPhoneAreaCode = officeLocationPhoneAreaCode;
    }
    
    /** Getter for property m_officeLocationPhoneBad.
     * @return Value of property m_officeLocationPhoneBad.
     *
     */
    public boolean isOfficeLocationPhoneBad() {
        return m_officeLocationPhoneBad;
    }
    
    /** Setter for property m_officeLocationPhoneBad.
     * @param officeLocationPhoneBad New value of property m_officeLocationPhoneBad.
     *
     */
    public void setOfficeLocationPhoneBad(boolean officeLocationPhoneBad) {
        this.m_officeLocationPhoneBad = officeLocationPhoneBad;
    }
    
    /** Getter for property m_officeLocationPhoneBadDate.
     * @return Value of property m_officeLocationPhoneBadDate.
     *
     */
    public Timestamp getOfficeLocationPhoneBadDate() {
        return m_officeLocationPhoneBadDate;
    }
    
    /** Setter for property m_officeLocationPhoneBadDate.
     * @param officeLocationPhoneBadDate New value of property m_officeLocationPhoneBadDate.
     *
     */
    public void setOfficeLocationPhoneBadDate(Timestamp officeLocationPhoneBadDate) {
        this.m_officeLocationPhoneBadDate = officeLocationPhoneBadDate;
    }
    
    /** Getter for property m_officeLocationPhoneCountryCode.
     * @return Value of property m_officeLocationPhoneCountryCode.
     *
     */
    public java.lang.String getOfficeLocationPhoneCountryCode() {
        return m_officeLocationPhoneCountryCode;
    }
    
    /** Setter for property m_officeLocationPhoneCountryCode.
     * @param officeLocationPhoneCountryCode New value of property m_officeLocationPhoneCountryCode.
     *
     */
    public void setOfficeLocationPhoneCountryCode(java.lang.String officeLocationPhoneCountryCode) {
        this.m_officeLocationPhoneCountryCode = officeLocationPhoneCountryCode;
    }
    
    /** Getter for property m_officeLocationPhoneNumber.
     * @return Value of property m_officeLocationPhoneNumber.
     *
     */
    public java.lang.String getOfficeLocationPhoneNumber() {
        return m_officeLocationPhoneNumber;
    }
    
    /** Setter for property m_officeLocationPhoneNumber.
     * @param officeLocationPhoneNumber New value of property m_officeLocationPhoneNumber.
     *
     */
    public void setOfficeLocationPhoneNumber(java.lang.String officeLocationPhoneNumber) {
        this.m_officeLocationPhoneNumber = officeLocationPhoneNumber;
    }
    
    /** Getter for property m_faxLocationPhonePK.
     * @return Value of property m_faxLocationPhonePK.
     *
     */
    public java.lang.Integer getFaxLocationPhonePK() {
        return m_faxLocationPhonePK;
    }
    
    /** Setter for property m_faxLocationPhonePK.
     * @param faxLocationPhonePK New value of property m_faxLocationPhonePK.
     *
     */
    public void setFaxLocationPhonePK(java.lang.Integer faxLocationPhonePK) {
        this.m_faxLocationPhonePK = faxLocationPhonePK;
    }
    
    /** Getter for property m_faxLocationPhoneAreaCode.
     * @return Value of property m_faxLocationPhoneAreaCode.
     *
     */
    public java.lang.String getFaxLocationPhoneAreaCode() {
        return m_faxLocationPhoneAreaCode;
    }
    
    /** Setter for property m_faxLocationPhoneAreaCode.
     * @param faxLocationPhoneAreaCode New value of property m_faxLocationPhoneAreaCode.
     *
     */
    public void setFaxLocationPhoneAreaCode(java.lang.String faxLocationPhoneAreaCode) {
        this.m_faxLocationPhoneAreaCode = faxLocationPhoneAreaCode;
    }
    
    /** Getter for property m_faxLocationPhoneBad.
     * @return Value of property m_faxLocationPhoneBad.
     *
     */
    public boolean isFaxLocationPhoneBad() {
        return m_faxLocationPhoneBad;
    }
    
    /** Setter for property m_faxLocationPhoneBad.
     * @param faxLocationPhoneBad New value of property m_faxLocationPhoneBad.
     *
     */
    public void setFaxLocationPhoneBad(boolean faxLocationPhoneBad) {
        this.m_faxLocationPhoneBad = faxLocationPhoneBad;
    }
    
    /** Getter for property m_faxLocationPhoneBadDate.
     * @return Value of property m_faxLocationPhoneBadDate.
     *
     */
    public Timestamp getFaxLocationPhoneBadDate() {
        return m_faxLocationPhoneBadDate;
    }
    
    /** Setter for property m_faxLocationPhoneBadDate.
     * @param faxLocationPhoneBadDate New value of property m_faxLocationPhoneBadDate.
     *
     */
    public void setFaxLocationPhoneBadDate(Timestamp faxLocationPhoneBadDate) {
        this.m_faxLocationPhoneBadDate = faxLocationPhoneBadDate;
    }
    
    /** Getter for property m_faxLocationPhoneCountryCode.
     * @return Value of property m_faxLocationPhoneCountryCode.
     *
     */
    public java.lang.String getFaxLocationPhoneCountryCode() {
        return m_faxLocationPhoneCountryCode;
    }
    
    /** Setter for property m_faxLocationPhoneCountryCode.
     * @param faxLocationPhoneCountryCode New value of property m_faxLocationPhoneCountryCode.
     *
     */
    public void setFaxLocationPhoneCountryCode(java.lang.String faxLocationPhoneCountryCode) {
        this.m_faxLocationPhoneCountryCode = faxLocationPhoneCountryCode;
    }
    
    /** Getter for property m_faxLocationPhoneNumber.
     * @return Value of property m_faxLocationPhoneNumber.
     *
     */
    public java.lang.String getFaxLocationPhoneNumber() {
        return m_faxLocationPhoneNumber;
    }
    
    /** Setter for property m_faxLocationPhoneNumber.
     * @param faxLocationPhoneNumber New value of property m_faxLocationPhoneNumber.
     *
     */
    public void setFaxLocationPhoneNumber(java.lang.String faxLocationPhoneNumber) {
        this.m_faxLocationPhoneNumber = faxLocationPhoneNumber;
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
    
    /** 
     * Checks all the fields for a regular address and returns TRUE 
     * if any field for the address is filled in.
     */
    protected boolean hasRegularAddress() {
        
        /** Location Regular Address fields */
        if ((!TextUtil.isEmptyOrSpaces(this.m_regularLocationAttention)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_regularLocationAddress1)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_regularLocationAddress2)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_regularLocationCity)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_regularLocationState)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_regularLocationZip)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_regularLocationZip4)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_regularLocationCounty)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_regularLocationProvince)) ||
            ((this.m_regularLocationCountry != null) && (this.m_regularLocationCountry.intValue() != 0)) ||
            (this.m_regularLocationAddressBad == true))
        {
            return true;
        }
        
        return false;
    }
    
    /** 
     * Checks all the fields for a shipping address and returns TRUE 
     * if any field for the address is filled in.
     */
    protected boolean hasShippingAddress() {
        
        /** Location Shipping Address fields */
        if ((!TextUtil.isEmptyOrSpaces(this.m_shippingLocationAttention)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_shippingLocationAddress1)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_shippingLocationAddress2)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_shippingLocationCity)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_shippingLocationState)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_shippingLocationZip)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_shippingLocationZip4)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_shippingLocationCounty)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_shippingLocationProvince)) ||
            ((this.m_shippingLocationCountry != null) && (this.m_shippingLocationCountry.intValue() != 0)) ||
            (this.m_shippingLocationAddressBad == true))
        {
            return true;
        }
        
        return false;
    }

    /** 
     * Checks all the fields for an office phone and returns TRUE 
     * if any field for the phone is filled in.
     */
    protected boolean hasOfficePhone() {
        
        /** Location Office Phone fields */
        if ((!TextUtil.isEmptyOrSpaces(this.m_officeLocationPhoneCountryCode)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_officeLocationPhoneAreaCode)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_officeLocationPhoneNumber)) ||
            (this.m_officeLocationPhoneBad == true))
        {
            return true;
        }
        
        return false;
    }

    /** 
     * Checks all the fields for an office phone and returns TRUE 
     * if any field for the phone is filled in.
     */
    protected boolean hasFaxPhone() {
        
        /** Location Fax Phone fields */
        if ((!TextUtil.isEmptyOrSpaces(this.m_faxLocationPhoneCountryCode)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_faxLocationPhoneAreaCode)) ||
            (!TextUtil.isEmptyOrSpaces(this.m_faxLocationPhoneNumber)) ||
            (this.m_faxLocationPhoneBad == true)) 
        {
            return true;
        }
        
        return false;
    }

    
    /** 
     * Copies all the fields from an address record to the 
     * regular address form fields.
     */
    protected void copyRegularAddress(OrgAddressRecord address) {
        
        /** Location Regular Address fields */
        m_regularLocationAddressPK = address.recordData.getPk();
        m_regularLocationAttention = address.getAttentionLine();
        m_regularLocationAddress1 = address.getAddr1();
        m_regularLocationAddress2 = address.getAddr2();
        m_regularLocationCity = address.getCity();
        m_regularLocationState = address.getState();
        m_regularLocationZip = address.getZipCode();
        m_regularLocationZip4 = address.getZipPlus();
        m_regularLocationCounty = address.getCounty();
        m_regularLocationProvince = address.getProvince();
        m_regularLocationCountry = address.getCountryPk();
        m_regularLocationAddressBad = address.getBad();
        m_regularLocationAddressBadDate = address.getBadDate();
    }

    /** 
     * Copies all the fields from an address record to the 
     * shipping address form fields.
     */
    protected void copyShippingAddress(OrgAddressRecord address) {
        
        /** Location Shipping Address fields */
        m_shippingLocationAddressPK = address.recordData.getPk();
        m_shippingLocationAttention = address.getAttentionLine();
        m_shippingLocationAddress1 = address.getAddr1();
        m_shippingLocationAddress2 = address.getAddr2();
        m_shippingLocationCity = address.getCity();
        m_shippingLocationState = address.getState();
        m_shippingLocationZip = address.getZipCode();
        m_shippingLocationZip4 = address.getZipPlus();
        m_shippingLocationCounty = address.getCounty();
        m_shippingLocationProvince = address.getProvince();
        m_shippingLocationCountry = address.getCountryPk();
        m_shippingLocationAddressBad = address.getBad();
        m_shippingLocationAddressBadDate = address.getBadDate();
    }
    
    /** 
     * Copies all the fields from an phone record to the 
     * office phone form fields.
     */
    protected void copyOfficePhone(OrgPhoneData phone) {
        
        /** Location Office Phone fields */
        m_officeLocationPhonePK = phone.getTheRecordData().getPk();
        m_officeLocationPhoneCountryCode = phone.getCountryCode();
        m_officeLocationPhoneAreaCode = phone.getAreaCode();
        m_officeLocationPhoneNumber = phone.getPhoneNumber();
        m_officeLocationPhoneBad = phone.getPhoneBadFlag().booleanValue();
        m_officeLocationPhoneBadDate = phone.getPhoneBadDate();
    }

    /** 
     * Copies all the fields from an phone record to the 
     * fax phone form fields.
     */
    protected void copyFaxPhone(OrgPhoneData phone) {
        
        /** Location Fax Phone fields */
        m_faxLocationPhonePK = phone.getTheRecordData().getPk();        
        m_faxLocationPhoneCountryCode = phone.getCountryCode();
        m_faxLocationPhoneAreaCode = phone.getAreaCode();
        m_faxLocationPhoneNumber = phone.getPhoneNumber();
        m_faxLocationPhoneBad = phone.getPhoneBadFlag().booleanValue();
        m_faxLocationPhoneBadDate = phone.getPhoneBadDate();
    }

    /** 
     * Copies all the regular address fields from the form to the 
     * org address record.
     *
     * This will be used for filling in the object to process 
     * and also for validation by the save.
     */
    public OrgAddressRecord getAddressFromRegularAddress() {
            
        OrgAddressRecord address = new OrgAddressRecord();

        if (!TextUtil.isEmptyOrSpaces(m_regularLocationAttention))
            address.setAttentionLine(m_regularLocationAttention);
        address.setAddr1(m_regularLocationAddress1);
        if (!TextUtil.isEmptyOrSpaces(m_regularLocationAddress2))
            address.setAddr2(m_regularLocationAddress2);
        if (!TextUtil.isEmptyOrSpaces(m_regularLocationCity))
            address.setCity(m_regularLocationCity);
        if (!TextUtil.isEmptyOrSpaces(m_regularLocationState))
            address.setState(m_regularLocationState);
        if (!TextUtil.isEmptyOrSpaces(m_regularLocationZip))
            address.setZipCode(m_regularLocationZip); 
        if (!TextUtil.isEmptyOrSpaces(m_regularLocationZip4))
            address.setZipPlus(m_regularLocationZip4);
        if (!TextUtil.isEmptyOrSpaces(m_regularLocationCounty))
            address.setCounty(m_regularLocationCounty);
        if (!TextUtil.isEmptyOrSpaces(m_regularLocationProvince))
            address.setProvince(m_regularLocationProvince);
        address.setCountryPk(m_regularLocationCountry);
        
        return address;
    }            

    /** 
     * Copies all the shipping address fields from the form to the 
     * org address record.
     *
     * This will be used for filling in the object to process 
     * and also for validation by the save.
     */
    public OrgAddressRecord getAddressFromShippingAddress() {
  
        OrgAddressRecord address = new OrgAddressRecord();

        if (!TextUtil.isEmptyOrSpaces(m_shippingLocationAttention))
            address.setAttentionLine(m_shippingLocationAttention);
        address.setAddr1(m_shippingLocationAddress1);
        if (!TextUtil.isEmptyOrSpaces(m_shippingLocationAddress2))
            address.setAddr2(m_shippingLocationAddress2);
        if (!TextUtil.isEmptyOrSpaces(m_shippingLocationCity))
            address.setCity(m_shippingLocationCity);
        if (!TextUtil.isEmptyOrSpaces(m_shippingLocationState))
            address.setState(m_shippingLocationState);
        if (!TextUtil.isEmptyOrSpaces(m_shippingLocationZip))
            address.setZipCode(m_shippingLocationZip); 
        if (!TextUtil.isEmptyOrSpaces(m_shippingLocationZip4))
            address.setZipPlus(m_shippingLocationZip4);
        if (!TextUtil.isEmptyOrSpaces(m_shippingLocationCounty))
            address.setCounty(m_shippingLocationCounty);
        if (!TextUtil.isEmptyOrSpaces(m_shippingLocationProvince))
            address.setProvince(m_shippingLocationProvince);
        address.setCountryPk(m_shippingLocationCountry);
        
        return address;
    }            
        
    /**
     * validation method for this form
     */    
    public Set validate() {
        
        Set errors = new TreeSet();
        
        //Check Required fields        
        if (m_locationTitle.length() == 0) {
            errors.add(LocationData.ERROR_TITLE_EMPTY);
        }
        if ((m_regularLocationCountry == null) || (m_regularLocationCountry.intValue() == 0)) {
            errors.add(LocationData.ERROR_COUNTRY_EMPTY);
        }
        if (m_officeLocationPhoneNumber.length() == 0) {
            errors.add(LocationData.ERROR_OFFICE_PHONE_NUMBER_EMPTY);
        }

        //Check Conditionally Required fields
        //if Phone Country Code is 1 (United States), then Phone Area Code must have a value        
        if ((m_officeLocationPhoneCountryCode != null) && (m_officeLocationPhoneCountryCode.trim().equals("1"))) {
            if (m_officeLocationPhoneAreaCode.length() == 0)
                errors.add(LocationData.ERROR_OFFICE_PHONE_AREA_CODE_EMPTY);
        }
        if ((m_faxLocationPhoneCountryCode != null) && (m_faxLocationPhoneCountryCode.trim().equals("1"))) {
            if (m_faxLocationPhoneAreaCode.length() == 0)
                errors.add(LocationData.ERROR_FAX_PHONE_AREA_CODE_EMPTY);
        }
        
        //if any of the Fax Country Code, Area Code or Phone Number are filled, 
        //then any of the empty ones of these fields must be filled in (Area code caught above for US)
        if ((m_faxLocationPhoneCountryCode.length() != 0) || (m_faxLocationPhoneAreaCode.length() != 0) ||
            (m_faxLocationPhoneNumber.length() != 0)) {
            
            if (m_faxLocationPhoneCountryCode.length() == 0)
                errors.add(LocationData.ERROR_FAX_PHONE_COUNTRY_CODE_EMPTY);
            if (m_faxLocationPhoneNumber.length() == 0)
                errors.add(LocationData.ERROR_FAX_PHONE_PHONE_NUMBER_EMPTY);
        }        

        return errors;
    }    
}
