package org.afscme.enterprise.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.update.member.MemberUpdateElement;
import org.afscme.enterprise.update.rebate.RebateUpdateElement;
import org.afscme.enterprise.util.CollectionUtil;
import org.apache.log4j.Logger;

/**
 * An individual exception in a review summary
 */
public class ExceptionData  {
    
    public static final int MEMBER = 0;
    public static final int OFFICER = 1;
    public static final int PARTICIPATION = 2;
    public static final int REBATE = 3;
    static int i = 0;
    /**
     * The record id for this entry in the database.
     */
    protected Integer recordId = null;
    
    /**
     * The person PK in this Exception
     */
    protected Integer personPk = null;
    
    /**
     * The last name of the person
     */
    protected String lastName = null;
    
    /**
     * The middle name of the person
     */
    protected String middleName = null;
    
    /**
     * The first name of the person
     */
    protected String firstName = null;
    
    /**
     * The suffix of the person
     */
    protected String suffix = null;
    
    /**
     * Update Error Type Common Code Pk
     */
    protected Integer updateErrorCodePk = null;
    

    /**
     * A map of ExceptionComparison objects.
     */
    // key -- fieldNameCodePk, value -- ExceptionComparison
    protected Map fieldChangeDetails = null;
    
    
    
    /****************************************************************************************/
    //Added this affiliateId to relate it to other structures and pages
    /****************************************************************************************/
    protected AffiliateIdentifier affiliateId   =   null;
    protected Integer affPk                     =   null;
    /****************************************************************************************/
    
    public ExceptionData(){
    }
    public ExceptionData(int type) {
        fieldChangeDetails = new HashMap();
        
        if (type == MEMBER) {
            fieldChangeDetails.put(Codes.MemberUpdateFields.PREFIX, new ExceptionComparison(MemberUpdateElement.PREFIX));
            fieldChangeDetails.put(Codes.MemberUpdateFields.FIRST_NAME, new ExceptionComparison(MemberUpdateElement.FIRST_NAME));
            fieldChangeDetails.put(Codes.MemberUpdateFields.MIDDLE_NAME, new ExceptionComparison(MemberUpdateElement.MIDDLE_NAME));
            fieldChangeDetails.put(Codes.MemberUpdateFields.LAST_NAME, new ExceptionComparison(MemberUpdateElement.LAST_NAME));
            fieldChangeDetails.put(Codes.MemberUpdateFields.SUFFIX, new ExceptionComparison(MemberUpdateElement.SUFFIX));
            fieldChangeDetails.put(Codes.MemberUpdateFields.ADDR1, new ExceptionComparison(MemberUpdateElement.ADDR1));
            fieldChangeDetails.put(Codes.MemberUpdateFields.ADDR2, new ExceptionComparison(MemberUpdateElement.ADDR2));
            fieldChangeDetails.put(Codes.MemberUpdateFields.CITY, new ExceptionComparison(MemberUpdateElement.CITY));
            fieldChangeDetails.put(Codes.MemberUpdateFields.STATE, new ExceptionComparison(MemberUpdateElement.STATE));
            fieldChangeDetails.put(Codes.MemberUpdateFields.ZIP, new ExceptionComparison(MemberUpdateElement.ZIP));
            fieldChangeDetails.put(Codes.MemberUpdateFields.MAILABLE_ADDRESS, new ExceptionComparison(MemberUpdateElement.MAILABLE_ADDRESS));
            fieldChangeDetails.put(Codes.MemberUpdateFields.NO_MAIL, new ExceptionComparison(MemberUpdateElement.NO_MAIL));
            fieldChangeDetails.put(Codes.MemberUpdateFields.PHONE, new ExceptionComparison(MemberUpdateElement.PHONE));
            fieldChangeDetails.put(Codes.MemberUpdateFields.STATUS, new ExceptionComparison(MemberUpdateElement.STATUS));
            fieldChangeDetails.put(Codes.MemberUpdateFields.GENDER, new ExceptionComparison(MemberUpdateElement.GENDER));
            fieldChangeDetails.put(Codes.MemberUpdateFields.DATE_JOINED, new ExceptionComparison(MemberUpdateElement.DATE_JOINED));
            fieldChangeDetails.put(Codes.MemberUpdateFields.REGISTERED_VOTER, new ExceptionComparison(MemberUpdateElement.REGISTERED_VOTER));
            fieldChangeDetails.put(Codes.MemberUpdateFields.POLITICAL_PARTY, new ExceptionComparison(MemberUpdateElement.POLITICAL_PARTY));
            fieldChangeDetails.put(Codes.MemberUpdateFields.SSN, new ExceptionComparison(MemberUpdateElement.SSN));
            fieldChangeDetails.put(Codes.MemberUpdateFields.INFORMATION_SOURCE, new ExceptionComparison(MemberUpdateElement.INFORMATION_SOURCE));
            fieldChangeDetails.put(Codes.MemberUpdateFields.AFFILIATE_MEMBER_ID, new ExceptionComparison(MemberUpdateElement.AFFILIATE_MEMBER_ID));        
        }
        else if (type == REBATE) {
            fieldChangeDetails.put(Codes.RebateUpdateFields.AFSCME_MEMBER_ID, new ExceptionComparison(RebateUpdateElement.AFSCME_MEMBER_ID));
            fieldChangeDetails.put(Codes.RebateUpdateFields.SSN, new ExceptionComparison(RebateUpdateElement.SSN));
            fieldChangeDetails.put(Codes.RebateUpdateFields.DUP_SSN, new ExceptionComparison(RebateUpdateElement.DUP_SSN));
            fieldChangeDetails.put(Codes.RebateUpdateFields.FIRST_NAME, new ExceptionComparison(RebateUpdateElement.FIRST_NAME));
            fieldChangeDetails.put(Codes.RebateUpdateFields.MIDDLE_NAME, new ExceptionComparison(RebateUpdateElement.MIDDLE_NAME));
            fieldChangeDetails.put(Codes.RebateUpdateFields.LAST_NAME, new ExceptionComparison(RebateUpdateElement.LAST_NAME));
            fieldChangeDetails.put(Codes.RebateUpdateFields.ADDR1, new ExceptionComparison(RebateUpdateElement.ADDR1));
            fieldChangeDetails.put(Codes.RebateUpdateFields.ADDR2, new ExceptionComparison(RebateUpdateElement.ADDR2));
            fieldChangeDetails.put(Codes.RebateUpdateFields.CITY, new ExceptionComparison(RebateUpdateElement.CITY));
            fieldChangeDetails.put(Codes.RebateUpdateFields.PROVINCE, new ExceptionComparison(RebateUpdateElement.PROVINCE));
            fieldChangeDetails.put(Codes.RebateUpdateFields.STATE, new ExceptionComparison(RebateUpdateElement.STATE));
            fieldChangeDetails.put(Codes.RebateUpdateFields.ZIP, new ExceptionComparison(RebateUpdateElement.ZIP));
            fieldChangeDetails.put(Codes.RebateUpdateFields.ZIP_PLUS, new ExceptionComparison(RebateUpdateElement.ZIP_PLUS));
            fieldChangeDetails.put(Codes.RebateUpdateFields.COUNTRY, new ExceptionComparison(RebateUpdateElement.COUNTRY));
            fieldChangeDetails.put(Codes.RebateUpdateFields.MEMBER_TYPE, new ExceptionComparison(RebateUpdateElement.MEMBER_TYPE));
            fieldChangeDetails.put(Codes.RebateUpdateFields.MEMBER_STATUS, new ExceptionComparison(RebateUpdateElement.MEMBER_STATUS));
            fieldChangeDetails.put(Codes.RebateUpdateFields.DUES_TYPE, new ExceptionComparison(RebateUpdateElement.DUES_TYPE));
            fieldChangeDetails.put(Codes.RebateUpdateFields.NUM_MONTHS, new ExceptionComparison(RebateUpdateElement.NUM_MONTHS));
            fieldChangeDetails.put(Codes.RebateUpdateFields.ACCEPTANCE_CODE, new ExceptionComparison(RebateUpdateElement.ACCEPTANCE_CODE));
        }
        else if (type == OFFICER) {
            // TBD
        }
        else if (type == PARTICIPATION) {
            // TBD
        }
        else {
        }
    }
    
    public String toString() {
        return  "ExceptionData [" +
                "recordId=" + recordId +
                ", personPk=" + personPk +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", middleName=" + middleName +
                ", suffix=" + suffix +
                ", updateErrorCodePk=" + updateErrorCodePk +
                ", fieldChangeDetails=" + CollectionUtil.toString(fieldChangeDetails) +
                "]"
        ;
    }
    
    /** Getter for property fieldChangeDetails.
     * @return Value of property fieldChangeDetails.
     *
     */
    public Map getFieldChangeDetails() {
        return fieldChangeDetails;
    }
    
    /** Setter for property fieldChangeDetails.
     * @param fieldChangeDetails New value of property fieldChangeDetails.
     *
     */
    public void setFieldChangeDetails(Map fieldChangeDetails) {
        this.fieldChangeDetails = fieldChangeDetails;
    }
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     *
     */
    public String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     *
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     *
     */
    public String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     *
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     *
     */
    public String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property middleName.
     * @param middleName New value of property middleName.
     *
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(Integer personPk) {
        this.personPk = personPk;
    }
    
    /** Getter for property suffix.
     * @return Value of property suffix.
     *
     */
    public String getSuffix() {
        return suffix;
    }
    
    /** Setter for property suffix.
     * @param suffix New value of property suffix.
     *
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    /** Getter for property updateErrorCodePk.
     * @return Value of property updateErrorCodePk.
     *
     */
    public Integer getUpdateErrorCodePk() {
        return updateErrorCodePk;
    }
    
    /** Setter for property updateErrorCodePk.
     * @param updateErrorCodePk New value of property updateErrorCodePk.
     *
     */
    public void setUpdateErrorCodePk(Integer updateErrorCodePk) {
        this.updateErrorCodePk = updateErrorCodePk;
    }
    
    /** Getter for property recordId.
     * @return Value of property recordId.
     *
     */
    public Integer getRecordId() {
        return recordId;
    }
    
    /** Setter for property recordId.
     * @param recordId New value of property recordId.
     *
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
    /****************************************************************************************/
     /** Getter for property affPk.
     * @return Value of property AffPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property AffPk.
     * @param AffPk New value of property AffPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    /********************************************************************/
    /*This method adds exception detail per field name                  */
    /********************************************************************/
    public void addDetails(Integer key, ExceptionComparison value) {
        Logger log = Logger.getLogger(ExceptionData.class);
        
        if (this.fieldChangeDetails == null) {
            //log.debug("----------------------------------------------------");
            //log.debug("Add Details method: Creating a new fieldChangeDetails object which is a HashMap");
            
            this.fieldChangeDetails= new HashMap();
        }
        List list = (List)this.fieldChangeDetails.get(key);
        if (list == null) {
            list = new ArrayList();            
            this.fieldChangeDetails.put(key, list);
        }
        i++;
/*        
        log.debug("----------------------------------------------------");
        log.debug("Add Details method: In Exception Data " + i);
        log.debug("----------------------------------------------------");
        log.debug("Add Details method: field name = " + value.getField());
        log.debug("----------------------------------------------------");
*/        
        try{
            list.add(value);
        }catch(Exception e){
            log.debug(e.getMessage());
            e.printStackTrace();
        }
        
    }
   
    /********************************************************************************************/
    /** Getter for property AffId.
     * @return Value of property AffId.
     *
     */
    public AffiliateIdentifier getAffId() {
        return affiliateId;
    }
    
    /** Setter for property AffId.
     * @param affId New value of property AffId.
     *
     */
    public void setAffId(AffiliateIdentifier affId) {
        this.affiliateId = affId;
    }
    /************************************************************************************************/

}
