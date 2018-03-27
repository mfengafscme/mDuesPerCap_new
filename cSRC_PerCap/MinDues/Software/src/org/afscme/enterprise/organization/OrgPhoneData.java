package org.afscme.enterprise.organization;

import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.util.TextUtil;


/**
 * Data about an individual organization phone number
 */
public class OrgPhoneData extends PhoneData 
{
    
    /**
     * Returns true if the content in this orgPhone is equal to the content of the 
     * given orgPhone.
     * @param orgPhone
     * @return boolean
     */
    public boolean equals(OrgPhoneData orgPhone) 
    {
        return 
            TextUtil.equals(countryCode, orgPhone.countryCode) &&
            TextUtil.equals(areaCode, orgPhone.areaCode) &&
            TextUtil.equals(phoneNumber, orgPhone.phoneNumber) &&
            TextUtil.equals(phoneExtension, orgPhone.phoneExtension) &&
            TextUtil.equals(phoneBadFlag, orgPhone.phoneBadFlag);
    }    
}
