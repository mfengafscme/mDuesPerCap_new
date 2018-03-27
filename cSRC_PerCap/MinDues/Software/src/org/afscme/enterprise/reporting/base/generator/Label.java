/*
 * Label.java
 *
 * This class holds all data for a label. It is used to temporarily hold a label
 * to generate 4 labels per row.
 */

package org.afscme.enterprise.reporting.base.generator;

import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.reporting.base.LabelConfigurationData;

/* Label templates 
 
    if "Officer Title" is selected in the output:
 
                "Carrier Route"
    "Address PK"
    “Officer Title” “Affiliate Type” “Affiliate Code” “Affiliate State Code”
	“First Name” “Middle Name” “Last Name”
	“Address 1” “Address 2”
	“City”  “State” “Zip Code”     
 
    Example:
 
    PRESIDENT  LOCAL 3889       55                                                                                                      
    JANITZA BERNIER                                                                                                                     
    P O BOX 276                                                                                                                         
    GUAYAMA         PR  00785
                                                                                                            
    otherwise:
                "Carrier Route"
    "Address PK"
    “Member Number”                     ---- member number is whatever the primary key in member table.
    “First Name” “Middle Name” “Last Name”
    “Address 1” “Address 2”
    “City”  "State” “Zip Code”
 *
    --------------------------------------------------------------------
    1		                        **CARRIER ROUTE "Carrier Route #"
    2
    3 Address PK
    4 "Oficer Title" "Affiliate Type” “Affiliate Code” “Affiliate State Code" | "Member PK"
    5 {["Prefix" | "Alternate Mailing Name"] “First Name” “Middle Name” “Last Name”} | "Full Name"
    6 “Address 1” “Address 2”
    7 “City”  "State” “Zip Code”-"zipplus"
 
 */

public class Label
{
    protected static final char PAD = ' ';

    protected int labelWidth; // read in from the 'label_config.xml'
    protected int cityWidth;

    // First Line -- Carrier Route information
    protected String carrierRoute = "";

    // Second line -- blank line

    // Third line -- address id (address pk)
    protected String addressID = "";

    // Fourth line -- these fields should be valid when "officerTitle" is part of output
    protected String officerTitle = "";
    protected String affiliateType = "";
    protected String affiliateCode = "";
    protected String affiliateStateCode = "";

    // Alternative Fourth line -- this field should be valid when "officerTitle" is not in output
    protected String memberNo = "0";
    // this should be the primary key of "member" table

    // Fifth line -- if either "alternate mailing name" or "prefix" exists.
    protected String altMailingName = ""; // these two mutually exclusive
    protected String prefix = "";

    protected String firstName = "";
    protected String middleName = "";
    protected String lastName = "";

    // Alternate Fifth line  -- if "full name" exists
    protected String fullName = "";

    // Sixth line
    protected String address1 = "";
    protected String address2 = "";

    // Seventh line
    protected String city = ""; // city takes 16 character space
    protected String state = "";
    // there are 2 char space between state and zip.
    protected String zip = "";

    protected String province = "";
    protected String country = "";

    /** Creates a new instance of Label */
    public Label()
    {
        LabelConfigurationData config = ConfigUtil.getLabelConfigurationData();
        labelWidth = config.getLabelWidth();
        cityWidth = config.getCityWidth();
    }

    /** Get the entire line include its padding.
     *  The index can only be 1 to 4.
     */
    public String getLabelLine(int index)
    {
        String line = "";

        switch (index)
        {
            case 1 :
                String cStr =
                    TextUtil.isEmpty(carrierRoute) ? "" : "**" + carrierRoute;
                cStr = TextUtil.padLeading(cStr, cityWidth + 9, PAD);
                line = TextUtil.padTrailing(cStr, labelWidth, PAD);
                break;
            case 2 :
                line = TextUtil.padTrailing(addressID, labelWidth, PAD);
                break;
            case 3 :
                if (TextUtil.isEmpty(officerTitle))
                    line = TextUtil.padTrailing(memberNo, labelWidth, PAD);
                else
                    line =
                        TextUtil.padTrailing(
                            officerTitle
                                + PAD
                                + affiliateType
                                + PAD
                                + affiliateCode
                                + PAD
                                + affiliateStateCode,
                            labelWidth,
                            PAD);
                break;
            case 4 :

                String nameStr = firstName + PAD + middleName + PAD + lastName;
                if (TextUtil.isEmpty(altMailingName))
                    line =
                        TextUtil.padTrailing(
                            prefix + PAD + nameStr,
                            labelWidth,
                            PAD);
                else
                    line =
                        TextUtil.padTrailing(altMailingName, labelWidth, PAD);
                break;
            case 5 :
                line =
                    TextUtil.padTrailing(
                        address1 + PAD + address2,
                        labelWidth,
                        PAD);
                break;
            case 6 :
                line =
                    TextUtil.padTrailing(
                        TextUtil.padTrailing(city, cityWidth, PAD)
                            + (country.startsWith("UNITED STATES")
                                ? state
                                : province)
                            + PAD
                            + PAD
                            + zip,
                        labelWidth,
                        PAD);
                break;
            case 7 :
                if (country != null
                    && country.length() > 0
                    && !country.startsWith("UNITED STATES"))
                    line =
                        TextUtil.padTrailing(
                            country.toUpperCase(),
                            labelWidth,
                            PAD);
                else
                    line = TextUtil.padTrailing("", labelWidth, PAD);
                break;
        }

        return line;
    }

    /** Getter for property officerTitle.
     * @return Value of property officerTitle.
     */
    public java.lang.String getOfficerTitle()
    {
        return officerTitle;
    }

    /** Setter for property officerTitle.
     * @param officerTitle New value of property officerTitle.
     */
    public void setOfficerTitle(java.lang.String officerTitle)
    {
        this.officerTitle = officerTitle;
    }

    /** Getter for property affiliateType.
     * @return Value of property affiliateType.
     */
    public java.lang.String getAffiliateType()
    {
        return affiliateType;
    }

    /** Setter for property affiliateType.
     * @param affiliateType New value of property affiliateType.
     */
    public void setAffiliateType(java.lang.String affiliateType)
    {
        this.affiliateType = affiliateType;
    }

    /** Getter for property affiliateCode.
     * @return Value of property affiliateCode.
     */
    public java.lang.String getAffiliateCode()
    {
        return affiliateCode;
    }

    /** Setter for property affiliateCode.
     * @param affiliateCode New value of property affiliateCode.
     */
    public void setAffiliateCode(java.lang.String affiliateCode)
    {
        this.affiliateCode = affiliateCode;
    }

    /** Getter for property affiliateStateCode.
     * @return Value of property affiliateStateCode.
     */
    public java.lang.String getAffiliateStateCode()
    {
        return affiliateStateCode;
    }

    /** Setter for property affiliateStateCode.
     * @param affiliateStateCode New value of property affiliateStateCode.
     */
    public void setAffiliateStateCode(java.lang.String affiliateStateCode)
    {
        this.affiliateStateCode = affiliateStateCode;
    }

    /** Getter for property memberNo.
     * @return Value of property memberNo.
     */
    public String getMemberNo()
    {
        return memberNo;
    }

    /** Setter for property memberNo.
     * @param memberNo New value of property memberNo.
     */
    public void setMemberNo(String memberNo)
    {
        this.memberNo = memberNo;
    }

    /** Getter for property firstName.
     * @return Value of property firstName.
     */
    public java.lang.String getFirstName()
    {
        return firstName;
    }

    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(java.lang.String firstName)
    {
        this.firstName = firstName;
    }

    /** Getter for property middleName.
     * @return Value of property middleName.
     */
    public java.lang.String getMiddleName()
    {
        return middleName;
    }

    /** Setter for property middleName.
     * @param middleName New value of property middleName.
     */
    public void setMiddleName(java.lang.String middleName)
    {
        this.middleName = middleName;
    }

    /** Getter for property lastName.
     * @return Value of property lastName.
     */
    public java.lang.String getLastName()
    {
        return lastName;
    }

    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(java.lang.String lastName)
    {
        this.lastName = lastName;
    }

    /** Getter for property address1.
     * @return Value of property address1.
     */
    public java.lang.String getAddress1()
    {
        return address1;
    }

    /** Setter for property address1.
     * @param address1 New value of property address1.
     */
    public void setAddress1(java.lang.String address1)
    {
        this.address1 = address1;
    }

    /** Getter for property address2.
     * @return Value of property address2.
     */
    public java.lang.String getAddress2()
    {
        return address2;
    }

    /** Setter for property address2.
     * @param address2 New value of property address2.
     */
    public void setAddress2(java.lang.String address2)
    {
        this.address2 = address2;
    }

    /** Getter for property city.
     * @return Value of property city.
     */
    public java.lang.String getCity()
    {
        return city;
    }

    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setCity(java.lang.String city)
    {
        this.city = city;
    }

    /** Getter for property state.
     * @return Value of property state.
     */
    public java.lang.String getState()
    {
        return state;
    }

    /** Setter for property state.
     * @param state New value of property state.
     */
    public void setState(java.lang.String state)
    {
        this.state = state;
    }

    /** Getter for property zip.
     * @return Value of property zip.
     */
    public java.lang.String getZip()
    {
        return zip;
    }

    /** Setter for property zip.
     * @param zip New value of property zip.
     */
    public void setZip(java.lang.String zip)
    {
        this.zip = zip;
    }

    /** Getter for property carrierRoute.
     * @return Value of property carrierRoute.
     */
    public java.lang.String getCarrierRoute()
    {
        return carrierRoute;
    }

    /** Setter for property carrierRoute.
     * @param carrierRoute New value of property carrierRoute.
     */
    public void setCarrierRoute(java.lang.String carrierRoute)
    {
        this.carrierRoute = carrierRoute;
    }

    /** Getter for property addressID.
     * @return Value of property addressID.
     */
    public java.lang.String getAddressID()
    {
        return addressID;
    }

    /** Setter for property addressID.
     * @param addressID New value of property addressID.
     */
    public void setAddressID(java.lang.String addressID)
    {
        this.addressID = addressID;
    }

    /** Getter for property altMailingName.
     * @return Value of property altMailingName.
     */
    public java.lang.String getAltMailingName()
    {
        return altMailingName;
    }

    /** Setter for property altMailingName.
     * @param altMailingName New value of property altMailingName.
     */
    public void setAltMailingName(java.lang.String altMailingName)
    {
        this.altMailingName = altMailingName;
    }

    /** Getter for property prefix.
     * @return Value of property prefix.
     */
    public java.lang.String getPrefix()
    {
        return prefix;
    }

    /** Setter for property prefix.
     * @param prefix New value of property prefix.
     */
    public void setPrefix(java.lang.String prefix)
    {
        this.prefix = prefix;
    }

    /** Getter for property fullName.
     * @return Value of property fullName.
     */
    public java.lang.String getFullName()
    {
        return fullName;
    }

    /** Setter for property fullName.
     * @param fullName New value of property fullName.
     */
    public void setFullName(java.lang.String fullName)
    {
        this.fullName = fullName;
    }

    /**
     * @return Returns the country.
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * @param country The country to set.
     */
    public void setCountry(String country)
    {
        this.country = country == null ? null : country.toUpperCase();
    }

    /**
     * @return Returns the province.
     */
    public String getProvince()
    {
        return province;
    }

    /**
     * @param province The province to set.
     */
    public void setProvince(String province)
    {
        this.province = province;
    }

}
