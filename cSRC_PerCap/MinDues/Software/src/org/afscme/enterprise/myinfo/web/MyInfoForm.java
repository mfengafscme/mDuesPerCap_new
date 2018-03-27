package org.afscme.enterprise.myinfo.web;

import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;

import org.afscme.enterprise.person.web.PhoneNumberForm;
import org.afscme.enterprise.person.web.EmailAddressForm;
import org.afscme.enterprise.address.web.PersonAddressForm;
import org.afscme.enterprise.person.web.PersonDetailForm;
import org.afscme.enterprise.util.TextUtil;

/** Holds the data on the Correspondence History Information screen
 * @struts:form name="myInfoForm"
 */
public class MyInfoForm extends ActionForm {

    private Integer addressPk ;
    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String zipCode;
    private String zipPlus;
    private String county;
    private String province;
    private Integer countryPk;
    private String[] phoneNumbers;
    private String[] areaCodes;
    private String[] countryCodes;
    private String[] phoneExtensions;
    private Integer[] phoneTypes;
    private Integer[] departments;
    private Boolean[] phonePrmryFlags;
    private Boolean[] phonePrivateFlags;
    private Boolean[] phoneDoNotCallFlags;;
    private Integer[] checkedPhones;
    private Integer[] emailPks;
	private String[] personEmailAddresses;
    private Boolean[] emailBadFlags;
    private Integer[] emailTypes;

    public MyInfoForm() {
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    /** Getter for property addressPk.
     * @return Value of property addressPk.
	 *
    */
  	public Integer getAddressPk() {
       return addressPk;
    }

   /** Setter for property addressPk.
    * @param addressPk New value of property addressPk.
    *
    */
    public void setAddressPk(Integer addressPk) {
		this.addressPk = addressPk;
    }

    /** Getter for property phoneTypes.
     * @return Value of property phoneTypes.
     *
     */
     public Integer[] getPhoneTypes() {
		return phoneTypes;
	}

    /** Setter for property phoneTypes.
     * @param phoneTypes New value of property phoneTypes.
     *
     */
    public void setPhoneTypes(Integer[] phoneTypes) {
        this.phoneTypes = phoneTypes;
    }


    /** Getter for property emailTypes.
	 * @return Value of property emailTypes.
     *
	 */
	public Integer[] getEmailTypes() {
	    return emailTypes;
	}

	/** Setter for property emailTypes.
     * @param emailTypes New value of property emailTypes.
	 *
	 */
	public void setEmailTypes(Integer[] emailTypes) {
	    this.emailTypes = emailTypes;
    }

    /** Getter for property phoneExtensions.
	 * @return Value of property phoneExtensions.
	 *
	 */
	public String[] getPhoneExtensions() {
	    return phoneExtensions;
	}

   /** Setter for property phoneExtensions.
    * @param phoneExtensions New value of property phoneExtensions.
    *
    */
    public void setPhoneExtensions(String[] phoneExtensions) {
		this.phoneExtensions = phoneExtensions;
    }

    /** Getter for property areaCodes.
     * @return Value of property areaCodes.
     *
     */
     public String[] getAreaCodes() {
		return areaCodes;
	}

	/** Setter for property areaCodes.
	 * @param areaCodes New value of property areaCodes.
	 *
	 */
	public void setAreaCodes(String[] areaCodes) {
	    this.areaCodes = areaCodes;
    }

    /** Getter for property countryCodes.
	 * @return Value of property countryCodes.
	 *
	 */
	public String[] getCountryCodes() {
	    return countryCodes;
	}

	/** Setter for property countryCodes.
	 * @param countryCodes New value of property countryCodes.
	 *
	 */
	public void setCountryCodes(String[] countryCodes) {
	    this.countryCodes = countryCodes;
    }

    /** Getter for property phoneNumbers.
	 * @return Value of property phoneNumbers.
     *
	 */
	public String[] getPhoneNumbers() {
		return phoneNumbers;
	}

	/** Setter for property phoneNumbers.
	 * @param phoneNumbers New value of property phoneNumbers.
	 *
	 */
	public void setPhoneNumbers(String[] phoneNumbers) {
	    this.phoneNumbers = phoneNumbers;
    }

    /** Getter for property departments.
	 * @return Value of property departments.
	 *
     */
    public Integer[] getDepartments() {
        return departments;
    }

	/** Setter for property departments.
	 * @param correspondenceHistoryList New value of property departments.
     *
     */
     public void setDepartments(Integer[] departments) {
		this.departments = departments;
    }

    /** Getter for property phonePrmryFlags.
	 * @return Value of property phonePrmryFlags.
	 *
	 */
	public Boolean[] getPhonePrmryFlags() {
		return phonePrmryFlags;
	}

	/** Setter for property phonePrmryFlags.
	 * @param phonePrmryFlags New value of property phonePrmryFlags.
	 *
     */
	public void setPhonePrmryFlags(Boolean[] phonePrmryFlags) {
		this.phonePrmryFlags = phonePrmryFlags;
    }

    /** Getter for property phonePrivateFlags.
	 * @return Value of property phonePrivateFlags.
	 *
	 */
	public Boolean[] getPhonePrivateFlags() {
	 	return phonePrivateFlags;
	}

	/** Setter for property phonePrivateFlags.
	 * @param phonePrivateFlags New value of property phonePrivateFlags.
	 *
	 */
	 public void setPhonePrivateFlags(Boolean[] phonePrivateFlags) {
	 	this.phonePrivateFlags = phonePrivateFlags;
    }

    /** Getter for property phoneDoNotCallFlags.
	 * @return Value of property phoneDoNotCallFlags.
	 *
	 */
	public Boolean[] getPhoneDoNotCallFlags() {
		return phoneDoNotCallFlags;
	}

	/** Setter for property phoneDoNotCallFlags.
	 * @param phoneDoNotCallFlags New value of property phoneDoNotCallFlags.
     *
	 */
	public void setPhoneDoNotCallFlags(Boolean[] phoneDoNotCallFlags) {
		this.phoneDoNotCallFlags = phoneDoNotCallFlags;
    }

    /** Getter for property emailBadFlags.
	 * @return Value of property emailBadFlags.
	 *
	 */
	public Boolean[] getEmailBadFlags() {
		return emailBadFlags;
	}

	/** Setter for property emailBadFlags.
	 * @param emailBadFlags New value of property emailBadFlags.
	 *
	 */
	public void setEmailBadFlags(Boolean[] emailBadFlags) {
		this.emailBadFlags = emailBadFlags;
    }

    /** Getter for property checkedPhones.
     * @return Value of property checkedPhones.
     *
     */
    public Integer[] getCheckedPhones() {
        return checkedPhones;
    }

    /** Setter for property checkedPhones.
     * @param checkedPhones New value of property checkedPhones.
     *
     */
    public void setCheckedPhones(Integer[] checkedPhones) {
        this.checkedPhones = checkedPhones;
    }

    /** Getter for property emailPks.
	 * @return Value of property emailPks.
	 *
	 */
	 public Integer[] getEmailPks() {
		return emailPks;
	 }

	 /** Setter for property emailPks.
	  * @param emailPks New value of property emailPks.
	  *
	  */
	public void setEmailPks(Integer[] emailPks) {
		this.emailPks = emailPks;
    }

    /** Getter for property personEmailAddresses.
	  * @return Value of property personEmailAddresses.
	  *
	  */
	public String[] getPersonEmailAddresses() {
		return personEmailAddresses;
	}

	/** Setter for property personEmailAddresses.
	 * @param personEmailAddresses New value of property personEmailAddresses.
	 *
	 */
	 public void setPersonEmailAddresses(String[] personEmailAddresses) {
	 	this.personEmailAddresses = personEmailAddresses;
    }

    /** Getter for property addr1.
	  * @return Value of property addr1.
	  *
	  */
	public String getAddr1() {
		return addr1;
	}

	    /** Setter for property addr1.
	     * @param addr1 New value of property addr1.
	     *
	     */
	    public void setAddr1(String addr1) {
	        this.addr1 = addr1;
    }

    /** Getter for property addr1.
	 * @return Value of property addr1.
	 *
	 */
	public String getAddr2() {
		return addr2;
	}

	/** Setter for property addr2.
	  * @param addr2 New value of property addr2.
	  *
	  */
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
    }

    /** Getter for property city.
	  * @return Value of property city.
	  *
	  */
	public String getCity() {
		return city;
	}

	/** Setter for property city.
	 * @param city New value of property city.
	 *
     */
	public void setCity(String city) {
		this.city = city;
    }

    /** Getter for property state.
	 * @return Value of property state.
	 *
	 */
	public String getState() {
		return state;
	}

	/** Setter for property state.
	 * @param state New value of property state.
	 *
	 */
	public void setState(String state) {
		this.state = state;
	}

	/** Getter for property zipCode.
     * @return Value of property zipCode.
	 *
	 */
	public String getZipCode() {
		return zipCode;
	}

	/** Setter for property zipCode.
	 * @param zipCode New value of property zipCode.
	 *
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/** Getter for property zipPlus.
     * @return Value of property zipPlus.
     *
	 */
	public String getZipPlus() {
		return zipPlus;
	}

	/** Setter for property zipPlus.
	 * @param zipPlus New value of property zipPlus.
	 *
	 */
	public void setZipPlus(String zipPlus) {
		this.zipPlus = zipPlus;
	}

	/** Getter for property county.
     * @return Value of property county.
	 *
	 */
	public String getCounty() {
		return county;
	}

	/** Setter for property county.
	 * @param county New value of property county.
	 *
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/** Getter for property province.
	 * @return Value of property province.
	 *
	 */
	public String getProvince() {
		return province;
	}

	/** Setter for property province.
	 * @param province New value of property province.
	 *
	 */
	public void setProvince(String province) {
		this.province = province;
	}


	/** Getter for property countryPk.
	 * @return Value of property countryPk.
	 *
	 */
	public Integer getCountryPk() {
		return countryPk;
	}

	/** Setter for property countryPk.
	 * @param countryPk New value of property countryPk.
	 *
     */
	public void setCountryPk(Integer countryPk) {
		this.countryPk = countryPk;
	}
}
