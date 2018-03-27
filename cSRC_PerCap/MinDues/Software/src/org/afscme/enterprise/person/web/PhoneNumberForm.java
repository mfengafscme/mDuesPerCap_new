package org.afscme.enterprise.person.web;

import java.sql.Timestamp;
// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.common.RecordData;
import org.apache.log4j.Logger;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.codes.Codes.Department;
import org.afscme.enterprise.codes.Codes.CountryCode;
import org.afscme.enterprise.codes.Codes.PhoneType;

/**
 * Represents the form when the user is viewing the person phone numbers
 *
 * @struts:form name="phoneNumberForm"
 */
public class PhoneNumberForm extends org.apache.struts.action.ActionForm {

    private static Logger logger =  Logger.getLogger(PhoneNumberForm.class); 
    
    private Integer m_personPk;

    /** Collection of PhoneData objects */
    private Collection m_phoneData;

    /** Department of the user viewing the data */
    protected Integer departmentPk;

    /** Map of Lists of PhoneData objects, by department pk */
    protected Map phoneListsByDepartmentPk;

    private Integer phonePk;
    private Integer dept;
    private Integer phoneType;
    private Boolean phonePrmryFg;
    private Boolean phonePrivateFg;
    private String countryCode;
    private String areaCode;
    private String phoneNumber;
    private String phoneExtension;
    private Boolean phoneBadFlag;
    private Timestamp phoneBadDate;
    private Boolean phoneDoNotCallFg;
    private RecordData theRecordData;

    //Flag to reset the Primary Phone Number
    private Boolean phoneResetPrimary;

    //Flag to identify if all DO NOT CALL are locked from update
    private Boolean lockedDoNotCallAll;

    //Flag to identify if DO NOT CALL is locked from update for Primary Home
    private Boolean lockedDoNotCallPrimary;

    /**
     * Action to use for the return button back to the last screen. Must include
     * parameters needed to return to previous screen.
     */
    private String returnAction;
    
    /** flag set to "V" if user is accessing through the view data utility
     */
     private String vduFlag;
    
    /** Creates a new instance of PhoneNumberForm */
    public PhoneNumberForm() {
        theRecordData = new RecordData();
        phoneResetPrimary = new Boolean(false);
        lockedDoNotCallAll = new Boolean(false);
        lockedDoNotCallPrimary = new Boolean(false);
        vduFlag = null;
    }

// Struts Methods...

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {   
        logger.debug("countryCode = " + countryCode);
        ActionErrors errors = new ActionErrors();

        //Check Required fields
        if (TextUtil.isEmptyOrSpaces(this.getPhoneNumber())) {
            errors.add("phoneNumber", new ActionError("error.field.required.generic", "Phone Number"));
        }

        if ((phoneType == null) || (phoneType.intValue() == 0)) {
            errors.add("phoneType", new ActionError("error.field.required.generic", "Phone Type"));
        }

        //Area Code is required if Country Code = United States
        if (countryCode!=null) {
            if ((countryCode.equals(PhoneData.COUNTRY_CODE_US) || countryCode.equals(String.valueOf(CountryCode.US))) && (areaCode == null)) {
                    errors.add("areaCode", new ActionError("error.field.required.generic", "Area Code"));
            }
        }

        //Only a phone number with PhoneType = HOME can be flagged as 'Primary'
        setPhonePrmryFg(phonePrmryFg);
        setPhoneResetPrimary(new Boolean(false));
        if (phonePrmryFg.booleanValue()) {
            if (!phoneType.equals(PhoneData.PHONE_TYPE_HOME)) {
                errors.add("phonePrmryFg", new ActionError("error.phone.primaryNotHome", "Primary Flag"));
            } else {
                setPhoneResetPrimary(new Boolean(true));
            }
        }

        logger.debug("**************** Returning " + errors.size() + " error(s).");
        return errors;
    }

// General Methods...

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");

        buf.append("phoneData: " + m_phoneData);
        buf.append("userDepartment: " + departmentPk);
        buf.append("phoneListsByDepartmentPk: " + phoneListsByDepartmentPk);
        buf.append("phonePk: " + phonePk);
        buf.append("dept: " + dept);
        buf.append("phonePrmryFg: " + phonePrmryFg);
        buf.append("phonePrivateFg: " + phonePrivateFg);
        buf.append("countryCode: " + countryCode);
        buf.append("areaCode: " + areaCode);
        buf.append("phoneNumber: " + phoneNumber);
        buf.append("phoneExtension: " + phoneExtension);
        buf.append("phoneBadFlag: " + phoneBadFlag);
        buf.append("phoneBadDate: " + phoneBadDate);
        buf.append("phoneType: " + phoneType);
        buf.append("phoneDoNotCallFg: " + phoneDoNotCallFg);
        buf.append("theRecordData: " + theRecordData);

        return buf.toString()+"]";
    }

// Getter and Setter Methods...

    /** Getter for property m_personPk.
     * @return Value of property m_personPk.
     *
     */
    public java.lang.Integer getPersonPk() {
        return m_personPk;
    }

    /** Setter for property m_personPk.
     * @param m_personPk New value of property m_personPk.
     *
     */
    public void setPersonPk(java.lang.Integer m_personPk) {
        this.m_personPk = m_personPk;
    }

    /** Getter for property m_phoneData.
     * @return Value of property m_phoneData.
     *
     */
    public java.util.Collection getPhoneData() {
        return m_phoneData;
    }

    /** Setter for property m_phoneData.
     * @param m_phoneData New value of property m_phoneData.
     *
     */
    public void setPhoneData(java.util.Collection m_phoneData) {
        this.m_phoneData = m_phoneData;
    }

    /** Getter for property departmentPk.
     * @return Value of property departmentPk.
     *
     */
    public java.lang.Integer getDepartment() {
        return departmentPk;
    }

    /** Setter for property departmentPk.
     * @param departmentPk New value of property departmentPk.
     *
     */
    public void setUserDepartment(Integer departmentPk) {
        if (departmentPk != null && departmentPk.intValue() == 0) {
            this.departmentPk = null;
        } else {
            this.departmentPk = departmentPk;
        }
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

    public boolean isEditable(PhoneData phone) {

        if (departmentPk != null) {
            return (phone.getDept() != null &&
                    phone.getDept().equals(departmentPk));
        } else {
            //user is using the data utility
            return (phone.getDept() != null &&
                    phone.getDept().equals(Department.MD) &&
                    phone.getPhoneType().equals(PhoneType.HOME));
        }
    }

    public Boolean isLockedDoNotCallAll() {
        return lockedDoNotCallAll;
    }

    public void setLockedDoNotCallAll(Boolean lockedDoNotCallAll) {
        this.lockedDoNotCallAll = lockedDoNotCallAll;
    }

    public Boolean isLockedDoNotCallPrimary() {
        return lockedDoNotCallPrimary;
    }

    public void setLockedDoNotCallPrimary(Boolean lockedDoNotCallPrimary) {
        this.lockedDoNotCallPrimary = lockedDoNotCallPrimary;
    }

    public boolean isLocked() {
        if (this.phonePrmryFg != null && this.phonePrmryFg.booleanValue()
            && (this.phoneType.equals(PhoneData.PHONE_TYPE_HOME))
            && (!isLockedDoNotCallAll().booleanValue())) {
            return isLockedDoNotCallPrimary().booleanValue();
        } else {
            return isLockedDoNotCallAll().booleanValue();
        }
    }

    public boolean isDeletable(PhoneData phone) {

        //can't delete a primary phone number
        if (phone.getPhonePrmryFg().booleanValue())
            return false;

        //can't delete a phone number if it's not editable (this is the department check)
        if (!isEditable(phone))
            return false;

        return true;
    }

    public List getDepartmentPhoneNumbers(int departmentPk) {
        return (List)phoneListsByDepartmentPk.get(new Integer(departmentPk));
    }

    public List getDepartments() {
        LinkedList depts = new LinkedList();
        depts.addAll(phoneListsByDepartmentPk.keySet());

        //put the user's department first
        if (depts.contains(departmentPk) && !depts.getFirst().equals(departmentPk)) {
            depts.remove(departmentPk);
            depts.addFirst(departmentPk);
        }

        return depts;
    }

    /** Setter for property addresses.
     * @param addresses New value of property addresses.
     *
     */
    public void setPhoneData(List phoneNumbers) {
        phoneListsByDepartmentPk = new HashMap();
        Iterator it = phoneNumbers.iterator();
        while (it.hasNext()) {
            PhoneData item = (PhoneData)it.next();
            Map map;
            Integer pk = item.getDept();
            if (pk != null) {
                List phoneList = (List)phoneListsByDepartmentPk.get(pk);
                if (phoneList == null) {
                    phoneList = new LinkedList();
                    phoneListsByDepartmentPk.put(pk, phoneList);
                }
                phoneList.add(item);
            } else {
                //phone number with no department can't be viewed on this page ;)
            }
        }
    }

    /** Getter for property phoneData.
     * @return Value of property phoneData.
     *
     */
    public PhoneData getPhoneNumberData() {
        PhoneData phoneData = new PhoneData();

        phoneData.setPhonePk(phonePk);
        phoneData.setDept(dept);
        phoneData.setPhoneType(phoneType);
        phoneData.setCountryCode(countryCode);
        phoneData.setAreaCode(areaCode);
        phoneData.setPhoneNumber(phoneNumber);
        phoneData.setPhoneExtension(phoneExtension);
        phoneData.setTheRecordData(theRecordData);
        phoneData.setPhonePrmryFg(phonePrmryFg);
        phoneData.setPhonePrivateFg(phonePrivateFg);
        phoneData.setPhoneBadFlag(phoneBadFlag);
        phoneData.setPhoneBadDate(phoneBadDate);
        phoneData.setPhoneDoNotCallFg (phoneDoNotCallFg );
        return phoneData;
    }

    /** Setter for property phoneData.
     * @param phoneData New value of property phoneData.
     *
     */
    public void setPhoneNumberData(PhoneData phoneData) {
        phonePk = phoneData.getPhonePk();
        dept = phoneData.getDept();
        phoneType = phoneData.getPhoneType();
        countryCode = phoneData.getCountryCode();
        areaCode = phoneData.getAreaCode();
        phoneNumber = phoneData.getPhoneNumber();
        phoneExtension = phoneData.getPhoneExtension();
        theRecordData = phoneData.getTheRecordData();
        phonePrmryFg = phoneData.getPhonePrmryFg();
        phonePrivateFg = phoneData.getPhonePrivateFg();
        phoneBadFlag = phoneData.getPhoneBadFlag();
        phoneBadDate = phoneData.getPhoneBadDate();
        phoneDoNotCallFg = phoneData.getPhoneDoNotCallFg();

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
        if (dept != null && dept.intValue() == 0) {
            this.dept = null;
        } else {
            this.dept = dept;
        }
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
        if (TextUtil.isEmptyOrSpaces(areaCode)) {
            this.areaCode = null;
        } else {
            this.areaCode = areaCode;
        }
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
        if (TextUtil.isEmptyOrSpaces(countryCode)) {
            this.countryCode = null;
        } else {
            this.countryCode = countryCode;
        }
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
        if (TextUtil.isEmptyOrSpaces(phoneExtension)) {
            this.phoneExtension = null;
        } else {
            this.phoneExtension = phoneExtension;
        }
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
        if (phonePrmryFg==null) {
            this.phonePrmryFg = new Boolean(false);
        } else {
            this.phonePrmryFg = phonePrmryFg;
        }
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

    /**
     * Returns true if this form is being used for an add operation (as opposed to edit)
     */
    public boolean isAdd() {
        return phonePk == null || phonePk.intValue() == 0;
    }

    /** Getter for property phoneResetPrimary.
     * @return Value of property phoneResetPrimary.
     *
     */
    public java.lang.Boolean getPhoneResetPrimary() {
        return phoneResetPrimary;
    }

    /** Setter for property phoneResetPrimary.
     * @param phoneResetPrimary New value of property phoneResetPrimary.
     *
     */
    public void setPhoneResetPrimary(java.lang.Boolean phoneResetPrimary) {
        this.phoneResetPrimary = phoneResetPrimary;
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
