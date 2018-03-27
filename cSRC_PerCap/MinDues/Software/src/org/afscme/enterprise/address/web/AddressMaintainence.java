package org.afscme.enterprise.address.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.codes.Codes.Department;
import org.afscme.enterprise.codes.Codes.PersonAddressType;
import org.afscme.enterprise.util.TextUtil;

/** Holds the data on the AddressMaintainence screen
 */
public class AddressMaintainence
{
    /** Map of Lists of PersonAddressRecord objects, by department pk */
    protected Map addressListsByDepartmentPk;
    
    /** The person's SMA */
    protected PersonAddressRecord systemAddress;
    
    /** Department of the user viewing the data (this should be null if affilaitePk is set)*/
    protected Integer departmentPk;
    
    /** Affiliates the user viewing the data has access to (this should be null if departmentPk is set*/
    protected Set affiliates;
    
    /** set flag to indicate user is accessing through VDU. Not sure the Set affiliates above is needed */ 
    private String vduFlag;
    
    /**
     * Action to use for the return button back to the last screen. Must include
     * parameters needed to return to previous screen.
     */
    protected String returnAction;
    
    /**
     * Name of the return action
     */
    protected String back;
    
    public List getDepartmentAddresses(int departmentPk) {
        return (List)addressListsByDepartmentPk.get(new Integer(departmentPk));
    }
    
    public void setUserDepartment(Integer departmentPk) {
        this.departmentPk = departmentPk;
    }
    
    public List getDepartments() {
        LinkedList depts = new LinkedList();
        depts.addAll(addressListsByDepartmentPk.keySet());
        
        //put the user's department first
        if (depts.contains(departmentPk) && !depts.getFirst().equals(departmentPk)) {
            depts.remove(departmentPk);
            depts.addFirst(departmentPk);
        }
        
        return depts;
    }
    
    public boolean isEditable(PersonAddressRecord address) {
        
        if (departmentPk != null) {
            return (address.getDepartment() != null &&
                    address.getDepartment().equals(departmentPk));
        } else {
            //user is using the data utility, they can edit the Membership Home address ;)
            // the address department must be non null, must equal the Membership department, and the address type must be not null
                    return (address.getDepartment() != null &&
                    address.getDepartment().equals(Department.MD) &&
                    address.getType().equals(PersonAddressType.HOME));
        }
    }
        
    public boolean isDeletable(PersonAddressRecord address) {
        
        //though address should never be null
        if (address == null)
            return false;
        
        //can't delete the SMA
        if (systemAddress != null &&
            address.getRecordData().getPk().equals(systemAddress.getRecordData().getPk()))
            return false;
        
        //can't delete a primary address
        if (address.isPrimary())
            return false;
        
        //can't delete an address if it's not editable (this is the department check)
        if (!isEditable(address))
            return false;
        
        return true;
    }
    
    /** Setter for property addresses.
     * @param addresses New value of property addresses.
     *
     */
    public void setAddresses(List addresses) {
        addressListsByDepartmentPk = new HashMap();
        Iterator it = addresses.iterator();
        while (it.hasNext()) {
            PersonAddressRecord item = (PersonAddressRecord)it.next();
            Map map;
            Integer pk = item.getDepartment();
            if (pk != null) {
                List addressList = (List)addressListsByDepartmentPk.get(pk);
                if (addressList == null) {
                    addressList = new LinkedList();
                    addressListsByDepartmentPk.put(pk, addressList);
                }
                addressList.add(item);
            } else {
                //address with no department can't be viewed on this page ;)
            }
        }
    }
    
    public PersonAddressRecord getSystemAddress() {
        return systemAddress;
    }
    
    public void setSystemAddress(PersonAddressRecord systemAddress) {
        this.systemAddress = systemAddress;
    }
    
    /** Getter for property returnAction.
     * @return Value of property returnAction.
     *
     */
    public String getReturnAction() {
        return returnAction;
    }
    
    /** Setter for property returnAction.
     * @param returnAction New value of property returnAction.
     *
     */
    public void setReturnAction(String returnAction) {
        if (TextUtil.isEmptyOrSpaces(returnAction)) {
            this.returnAction = null;
        } else {
            this.returnAction = returnAction;
        }
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
    
    /** Getter for property vduFlag.
     * @return Value of property vduFlag.
     *
     */
    public java.lang.String getVduFlag() {
        return vduFlag;
    }
    
    /** Setter for property vduFlag.
     * @param vduFlag New value of property vduFlag.
     *
     */
    public void setVduFlag(java.lang.String vduFlag) {
        this.vduFlag = vduFlag;
    }
    
}



