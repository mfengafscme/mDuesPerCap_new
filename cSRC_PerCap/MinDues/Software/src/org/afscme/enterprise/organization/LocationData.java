package org.afscme.enterprise.organization;

import java.util.Iterator;
import java.util.LinkedList;
import org.afscme.enterprise.codes.Codes.OrgAddressType;
import org.afscme.enterprise.codes.Codes.OrgPhoneType;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.util.TextUtil;

/**
 * Data about an individual organization location
 */
public class LocationData 
{
    /**
     * These are copies of the Address errors (for Shipping) by adding 10 to the Address value
     */
    public static final Integer ERROR_CITY_EMPTY=new Integer(-11);
    public static final Integer ERROR_STATE_EMPTY=new Integer(-12);
    public static final Integer ERROR_PROVINCE_EMPTY=new Integer(-13);
    public static final Integer ERROR_ZIPCODE_EMPTY=new Integer(-14);
    public static final Integer ERROR_ZIPCODE_INVALID=new Integer(-15);
    public static final Integer ERROR_ZIPPLUS_INVALID=new Integer(-16);
    public static final Integer ERROR_STATE_ZIP_MISMATCH=new Integer(-17);
    
    /**
     * Location Title is required, but not provided.
     */
    public static final Integer ERROR_TITLE_EMPTY=new Integer(-20);
    /**
     * Location Regular Address Country is required, but not provided.
     */
    public static final Integer ERROR_COUNTRY_EMPTY=new Integer(-21);    
    /**
     * Location Office Phone Number is required, but not provided.
     */
    public static final Integer ERROR_OFFICE_PHONE_NUMBER_EMPTY=new Integer(-22);
    /**
     * Location Office Area Code is required if Country Code is 1 (United States), but not provided.
     */
    public static final Integer ERROR_OFFICE_PHONE_AREA_CODE_EMPTY=new Integer(-23);
    /**
     * Location Fax Area Code is required if Country Code is 1 (United States), but not provided.
     */
    public static final Integer ERROR_FAX_PHONE_AREA_CODE_EMPTY=new Integer(-24);
    /**
     * Location Fax Country Code is required (if Area Code or Phone Number is entered), but not provided.
     */
    public static final Integer ERROR_FAX_PHONE_COUNTRY_CODE_EMPTY=new Integer(-25);
    /**
     * Location Fax Phone Number is required (if Country Code or Area Code is entered), but not provided.
     */
    public static final Integer ERROR_FAX_PHONE_PHONE_NUMBER_EMPTY=new Integer(-26);

    
    public Integer orgLocationPK;
    public String locationNm;
    public Boolean primaryLocationBoolean;
    
    public LinkedList orgAddressData;
    public LinkedList orgPhoneData;
    
    public Integer orgPk;
    
    public RecordData recordData;

    public LocationData() {
        orgAddressData = new LinkedList();
        orgPhoneData = new LinkedList();
        primaryLocationBoolean = new Boolean(false);
        recordData = new RecordData();
    }
    
    public String toString() {
        return "LocationData[" +
            "orgLocationPK="+orgLocationPK+", "+
            "locationNm="+locationNm+", "+
            "primaryLocationBoolean="+primaryLocationBoolean+", "+
            "orgAddressData="+orgAddressData+", "+
            "getHasBothPhones="+getHasBothPhones()+", "+
            "getHasOnlyFaxPhone="+getHasOnlyFaxPhone()+", "+
            "getHasOnlyOfficePhone="+getHasOnlyOfficePhone()+", "+
            "getHasBothAddresses="+getHasBothAddresses()+", "+
            "getHasOnlyShippingAddress="+getHasOnlyShippingAddress()+", "+
            "getHasOnlyRegularAddress="+getHasOnlyRegularAddress()+"]";
    }
    
    /**
     * Returns true if the content in this location is equal to the content of the 
     * given location.
     * @param location
     * @return boolean
     */
    public boolean equals(LocationData location) 
    {
        return 
            TextUtil.equals(locationNm, location.locationNm) &&
            TextUtil.equals(primaryLocationBoolean, location.primaryLocationBoolean) &&
            addressEquals(location.getOrgAddressData()) && 
            phoneEquals(location.getOrgPhoneData()); 
    }
    
    /**
     * Returns true if the addresses in this linked list is equal to the content of the 
     * given linked list.
     * @param linkedList
     * @return boolean
     */
    public boolean addressEquals(LinkedList linkedList) 
    {
        boolean match = true;
       
        if (orgAddressData != null && linkedList != null) {
            if (orgAddressData.size() == linkedList.size()) {
                
                // check for each item match only if not null and list size is same
                Iterator it1 = orgAddressData.iterator();
                Iterator it2 = linkedList.iterator();                
                while (it1.hasNext() && it2.hasNext()) {
                    
                    OrgAddressRecord addr1 = (OrgAddressRecord)it1.next();
                    OrgAddressRecord addr2 = (OrgAddressRecord)it2.next();
                    if (!(addr1.equals(addr2))) {
                        match = false;
                    }
                }
            }
            else {
                match = false;
            }
        }
        else {
            if (!(orgAddressData == null && linkedList == null)) { 
                match = false;
            }
        }    
        
        return match; 
    }

    /**
     * Returns true if the phones in this linked list is equal to the content of the 
     * given linked list.
     * @param linkedList
     * @return boolean
     */
    public boolean phoneEquals(LinkedList linkedList) 
    {
        boolean match = true;
       
        if (orgPhoneData != null && linkedList != null) {
            if (orgPhoneData.size() == linkedList.size()) {
                
                // check for each item match only if not null and list size is same
                Iterator it1 = orgPhoneData.iterator();
                Iterator it2 = linkedList.iterator();                
                while (it1.hasNext() && it2.hasNext()) {
                    
                    OrgPhoneData phone1 = (OrgPhoneData)it1.next();
                    OrgPhoneData phone2 = (OrgPhoneData)it2.next();
                    if (!(phone1.equals(phone2))) {
                        match = false;
                    }
                }     
            }
            else {
                match = false;
            }
        }
        else {
            if (!(orgPhoneData == null && linkedList == null)) { 
                match = false;
            }            
        }    
        
        return match; 
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
    
    /** Getter for property orgLocationPK.
     * @return Value of property orgLocationPK.
     *
     */
    public java.lang.Integer getOrgLocationPK() {
        return orgLocationPK;
    }
    
    /** Setter for property orgLocationPK.
     * @param orgLocationPK New value of property orgLocationPK.
     *
     */
    public void setOrgLocationPK(java.lang.Integer orgLocationPK) {
        this.orgLocationPK = orgLocationPK;
    }
    
    /** Getter for property primaryLocationBoolean.
     * @return Value of property primaryLocationBoolean.
     *
     */
    public java.lang.Boolean getPrimaryLocationBoolean() {
        return primaryLocationBoolean;
    }
   
    /** Setter for property primaryLocationBoolean.
     * @param primaryLocationBoolean New value of property primaryLocationBoolean.
     *
     */
    public void setPrimaryLocationBoolean(java.lang.Boolean primaryLocationBoolean) {
        this.primaryLocationBoolean = primaryLocationBoolean;
    }
    
    /** Getter for property orgAddressData.
     * @return Value of property orgAddressData.
     *
     */
    public LinkedList getOrgAddressData() {
    //    public org.afscme.enterprise.organization.OrgAddressRecord getOrgAddressData() {
        return this.orgAddressData;
    }
    
    /** Setter for property orgAddressData.
     * @param orgAddressData New value of property orgAddressData.
     *
     */
    public void setOrgAddressData(LinkedList orgAddressData) {
        this.orgAddressData = orgAddressData;
    }
    
    /** Getter for property orgPhoneData.
     * @return Value of property orgPhoneData.
     *
     */
    public LinkedList getOrgPhoneData() {
        return this.orgPhoneData;
    }
    
    /** Setter for property orgPhoneData.
     * @param orgPhoneData New value of property orgPhoneData.
     *
     */
    public void setOrgPhoneData(LinkedList orgPhoneData) {
        this.orgPhoneData = orgPhoneData;
    }
    
    /** Getter for property orgPk.
     * @return Value of property orgPk.
     *
     */
    public java.lang.Integer getOrgPk() {
        return orgPk;
    }
    
    /** Setter for property orgPk.
     * @param orgPk New value of property orgPk.
     *
     */
    public void setOrgPk(java.lang.Integer orgPk) {
        this.orgPk = orgPk;
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
     * Method to check if this location has only a Regular Address.
     * @return boolean.
     */
    public boolean getHasOnlyRegularAddress() {
        
        Iterator it = orgAddressData.iterator();
        while (it.hasNext()) {
            OrgAddressRecord item = (OrgAddressRecord)it.next();
            if (item.getType().intValue() != OrgAddressType.REGULAR.intValue()) {
                return false;
            }
        }
        
        // return true if only regular address found
        return true;
    }
    
    /** 
     * Method to check if this location has only a Shipping Address.
     * @return boolean.
     */
    public boolean getHasOnlyShippingAddress() {
        
        Iterator it = orgAddressData.iterator();
        while (it.hasNext()) {
            OrgAddressRecord item = (OrgAddressRecord)it.next();
            if (item.getType().intValue() != OrgAddressType.SHIPPING.intValue()) {
                return false;
            }
        }
        
        // return true if only shipping address found
        return true;
    }

    /** 
     * Method to check if this location has only both Regular and Shipping Address.
     * @return boolean.
     */    
    public boolean getHasBothAddresses() {
        
        boolean regular = false;
        boolean shipping = false;
        
        Iterator it = orgAddressData.iterator();
        while (it.hasNext()) {
            OrgAddressRecord item = (OrgAddressRecord)it.next();
            if (item.getType().intValue() == OrgAddressType.REGULAR.intValue()) {
                regular = true;
            }
            if (item.getType().intValue() == OrgAddressType.SHIPPING.intValue()) {
                shipping = true;
            }            
        }
        
        if (regular && shipping) 
            return true;
        else 
            return false;
    }
  
    /** 
     * Method to check if this location has only a Office Phone.
     * @return boolean.
     */
    public boolean getHasOnlyOfficePhone() {
        
        Iterator it = orgPhoneData.iterator();
        while (it.hasNext()) {
            OrgPhoneData item = (OrgPhoneData)it.next();
            if (item.getPhoneType().intValue() != OrgPhoneType.LOC_PHONE_OFFICE.intValue()) {
                return false;
            }
        }
        
        // return true if only office phone found
        return true;
    }

    /** 
     * Method to check if this location has only a Fax Phone.
     * @return boolean.
     */
    public boolean getHasOnlyFaxPhone() {
        
        Iterator it = orgPhoneData.iterator();
        while (it.hasNext()) {
            OrgPhoneData item = (OrgPhoneData)it.next();
            if (item.getPhoneType().intValue() != OrgPhoneType.LOC_PHONE_FAX.intValue()) {
                return false;
            }
        }
        
        // return true if only fax phone found
        return true;
    }

    /** 
     * Method to check if this location has both Office and Fax Phones.
     * @return boolean.
     */
    public boolean getHasBothPhones() {
        
        boolean office = false;
        boolean fax = false;
        
        Iterator it = orgPhoneData.iterator();
        while (it.hasNext()) {
            OrgPhoneData item = (OrgPhoneData)it.next();
            if (item.getPhoneType().intValue() == OrgPhoneType.LOC_PHONE_OFFICE.intValue()) {
                office = true;
            }
            if (item.getPhoneType().intValue() == OrgPhoneType.LOC_PHONE_FAX.intValue()) {
                fax = true;
            }            
        }
        
        if (office && fax) 
            return true;
        else 
            return false;        
    }

    /** 
     * Method to return an OrgAddressRecord by its PK.
     * @return OrgAddressRecord.
     */
    public OrgAddressRecord getOrgAddressByPK(Integer orgAddrPk) {
        
        Iterator it = orgAddressData.iterator();
        while (it.hasNext()) {
            OrgAddressRecord item = (OrgAddressRecord)it.next();
            if (item.recordData.getPk().intValue() == orgAddrPk.intValue()) {
                return item;
            }
        }
        
        // return null if can not find it
        return null;
    }

    /** 
     * Method to return an OrgPhoneData by its PK.
     * @return OrgPhoneData.
     */
    public OrgPhoneData getOrgPhoneByPK(Integer orgPhonePk) {
        
        Iterator it = orgPhoneData.iterator();
        while (it.hasNext()) {
            OrgPhoneData item = (OrgPhoneData)it.next();
            if (item.getTheRecordData().getPk().intValue() == orgPhonePk.intValue()) {
                return item;
            }
        }
        
        // return null if can not find it
        return null;
    }
}
