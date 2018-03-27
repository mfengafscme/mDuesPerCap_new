package org.afscme.enterprise.myinfo;

import java.util.Collection;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.address.Address;

/**
 * Data about an individual person.
 */
public class MyInfoData
{
    private Collection memberAffiliateResults;
    private PersonData personData;
    private PersonAddressRecord personAddressRecord;

    /** Getter for property personData.
     * @return Value of property personData.
     *
     */
    public PersonData getPersonData() {
        return personData;
    }

    /** Setter for property personData.
     * @param firstNm New value of property personData.
     *
     */
    public void setPersonData(PersonData personData) {
        this.personData = personData;
    }

    /** Getter for property personAddressRecord.
     * @return Value of property personAddressRecord.
     *
     */
    public PersonAddressRecord getPersonAddressRecord() {
        return personAddressRecord;
    }

    /** Setter for property personAddressRecord.
     * @param personAddressRecord New value of property personAddressRecord.
     *
     */
    public void setPersonAddressRecord(PersonAddressRecord personAddressRecord) {
        this.personAddressRecord = personAddressRecord;
    }

    /** Getter for property memberAffiliateResults.
     * @return Value of property memberAffiliateResults.
     *
     */
    public Collection getMemberAffiliateResults() {
        return memberAffiliateResults;
    }

    /** Setter for property memberAffiliateResults.
     * @param memberAffiliateResults New value of property memberAffiliateResults.
     *
     */
    public void setMemberAffiliateResults(Collection memberAffiliateResults) {
        this.memberAffiliateResults = memberAffiliateResults;
    }
}
