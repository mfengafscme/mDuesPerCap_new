/*
 * SortedAddressFilter.java
 *
 * Created on November 19, 2003, 5:24 PM
 */

package org.afscme.enterprise.reporting.specialized;

import org.afscme.enterprise.address.Address;

/**  This is a utility class that helps to determine if an address is repeated from
 * the previous call to this utility.  For memory consumption reasons, It only
 * retains the address from the most recent call to the isAddressDuplicate method
 * and uses that address for comparision on the next subsequent call.
 * <P>
 * <B>
 * Therefore this utility assumes that the addresses being checked are sorted by
 * all of the address fields (such as with an order by clause in a query).  This
 * object will NOT operate correctly if the addresses are not processed in a sorted
 * order.</B>
 * </P>
 * @author ckovar
 */
public class DuplicateAddressFilter {
    
    /** Creates a new instance of SortedAddressFilter */
    public DuplicateAddressFilter() {
    }
    
    private Address current;
    private Address previous;
    
    /** Checks if the address given is identicle to the last addressed passed into
     * this method.
     * @return <CODE>true</CODE> if the addressed given is identicle to the previous address given
     * <CODE>false</CODE> otherwise
     */    
    public boolean isAddressDuplicate(String addr1, String addr2, String city, String state,
        				String province, String zipCode, String zipPlus, String country )
    {
        boolean retVal = false;
        
        if(current == null)
            current = new Address();
                
        current.setAddr1(addr1);
        current.setAddr2(addr2);
        current.setCity(city);
        current.setState(state);
        current.setProvince(province);
        current.setZipCode(zipCode);
        current.setZipPlus(zipPlus);
                
        if(previous != null) //reuse the addresses comparision logic
            retVal = current.equals(previous);
        
        //swap the addresses instances reusing the old "previous" instance to 
        //conserve on memory and to reduce the number of objectes GC'd
        Address temp = previous;
        previous = current;
        current = temp;
        
        return retVal;
    }
}
