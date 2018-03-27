package org.afscme.enterprise.returnedmail;

import java.util.LinkedList;
import java.util.HashMap;

/**
 * Holds the results of a processed returned mail action
 */
public class ReturnedMailSummary {
    private java.util.Map InvalidAddressList;
    private LinkedList PersonExceptionList;
    private LinkedList PersonSuccessfulList;
    private LinkedList OrganizationExceptionList;
    private LinkedList OrganizationSuccessfulList;
    private Integer attemptedCount;
    private Integer successfulCount;
    private Integer exceptionCount;
    private Integer invalidAddrCount;
    
    public ReturnedMailSummary() {
        attemptedCount = new Integer(0);
        successfulCount = new Integer(0);
        exceptionCount = new Integer(0);
        invalidAddrCount = new Integer(0);
        InvalidAddressList = new HashMap();
        PersonExceptionList = new LinkedList();
        PersonSuccessfulList = new LinkedList();
        OrganizationExceptionList = new LinkedList();
        OrganizationSuccessfulList = new LinkedList();
    }
    
    /**
     * Adds the Invalid Address to a list
     *
     * @param address Integer
     */
    public void addToInvalidAddressList(Integer address) {
        String key="Address_"+address;
        InvalidAddressList.put(key, address);
    }
    
    /**
     * Adds the Invalid Address to a list
     *
     * @param address String
     */
    public void addToInvalidAddressList(String address) {
        String key="Address_"+address;
        InvalidAddressList.put(key, address);
    }

    
    /**
     * Adds the Person Address to a list of Person Addresses that were successfully
     * marked as bad.
     *
     * @param address ReturnedPersonAddress
     */
    public void addToPersonSuccessfulList(ReturnedPersonAddress address) {
        PersonSuccessfulList.add(address);
    }
    
    /**
     * Adds the Organization Address to a list of Organization Addresses that were
     * successfully marked as bad.
     *
     * @param address ReturnedOrganizationAddress
     */
    public void addToOrganizationSuccessfulList(ReturnedOrganizationAddress address) {
        OrganizationSuccessfulList.add(address);
    }
    
    /**
     * Adds the Person Address to a list of Person Addresses that were unsuccessfully
     * marked as bad.
     *
     * @param address ReturnedPersonAddress
     */
    public void addToPersonExceptionList(ReturnedPersonAddress address) {
        PersonExceptionList.add(address);
    }
    
    /**
     * Adds the Organization Address to a list of Organization Addresses that were
     * unsuccessfully marked as bad.
     *
     * @param address ReturnedOrganizationAddress
     */
    public void addToOrganizationExceptionList(ReturnedOrganizationAddress address) {
        OrganizationExceptionList.add(address);
    }
    
    /**
     * Used to update the total number of Invalid Addresses
     */
    public void incrementInvalidAddressCount() {
        invalidAddrCount = new Integer(invalidAddrCount.intValue() + 1);
    }
    
    /**
     * Used to update the total number of Person and Organization addresses that were
     * successfully marked as bad.
     */
    public void incrementSuccessfulCount() {
        successfulCount = new Integer(successfulCount.intValue() + 1);
    }
    
    /**
     * Used to update the total number of Person and Organization addresses that were
     * attempted to be processed regardless if it was successful or not.
     */
    public void incrementAttemptedCount() {
        attemptedCount = new Integer(attemptedCount.intValue() + 1);
    }
    
    /**
     * Used to update the total number of Person and Organization addresses that were
     * unsuccessfully marked as bad.
     */
    public void incrementExceptionCount() {
        exceptionCount = new Integer(exceptionCount.intValue() + 1);
    }
    
    /** Getter for property attemptedCount.
     * @return Value of property attemptedCount.
     *
     */
    public java.lang.Integer getAttemptedCount() {
        return attemptedCount;
    }
    
    /** Setter for property attemptedCount.
     * @param attemptedCount New value of property attemptedCount.
     *
     */
    public void setAttemptedCount(java.lang.Integer attemptedCount) {
        this.attemptedCount = attemptedCount;
    }
    
    /** Getter for property exceptionCount.
     * @return Value of property exceptionCount.
     *
     */
    public java.lang.Integer getExceptionCount() {
        return exceptionCount;
    }
    
    /** Setter for property exceptionCount.
     * @param exceptionCount New value of property exceptionCount.
     *
     */
    public void setExceptionCount(java.lang.Integer exceptionCount) {
        this.exceptionCount = exceptionCount;
    }
    
    /** Getter for property OrganizationExceptionList.
     * @return Value of property OrganizationExceptionList.
     *
     */
    public LinkedList getOrganizationExceptionList() {
        return OrganizationExceptionList;
    }
    
    /** Setter for property OrganizationExceptionList.
     * @param OrganizationExceptionList New value of property OrganizationExceptionList.
     *
     */
    public void setOrganizationExceptionList(LinkedList OrganizationExceptionList) {
        this.OrganizationExceptionList = OrganizationExceptionList;
    }
    
    /** Getter for property OrganizationSuccessfulList.
     * @return Value of property OrganizationSuccessfulList.
     *
     */
    public LinkedList getOrganizationSuccessfulList() {
        return OrganizationSuccessfulList;
    }
    
    /** Setter for property OrganizationSuccessfulList.
     * @param OrganizationSuccessfulList New value of property OrganizationSuccessfulList.
     *
     */
    public void setOrganizationSuccessfulList(LinkedList OrganizationSuccessfulList) {
        this.OrganizationSuccessfulList = OrganizationSuccessfulList;
    }
    
    /** Getter for property PersonExceptionList.
     * @return Value of property PersonExceptionList.
     *
     */
    public LinkedList getPersonExceptionList() {
        return PersonExceptionList;
    }
    
    /** Setter for property PersonExceptionList.
     * @param PersonExceptionList New value of property PersonExceptionList.
     *
     */
    public void setPersonExceptionList(LinkedList PersonExceptionList) {
        this.PersonExceptionList = PersonExceptionList;
    }
    
    /** Getter for property PersonSuccessfulList.
     * @return Value of property PersonSuccessfulList.
     *
     */
    public LinkedList getPersonSuccessfulList() {
        return PersonSuccessfulList;
    }
    
    /** Setter for property PersonSuccessfulList.
     * @param PersonSuccessfulList New value of property PersonSuccessfulList.
     *
     */
    public void setPersonSuccessfulList(LinkedList PersonSuccessfulList) {
        this.PersonSuccessfulList = PersonSuccessfulList;
    }
    
    /** Getter for property successfulCount.
     * @return Value of property successfulCount.
     *
     */
    public java.lang.Integer getSuccessfulCount() {
        return successfulCount;
    }
    
    /** Setter for property successfulCount.
     * @param successfulCount New value of property successfulCount.
     *
     */
    public void setSuccessfulCount(java.lang.Integer successfulCount) {
        this.successfulCount = successfulCount;
    }
    
    /** Getter for property invalidAddrCount.
     * @return Value of property invalidAddrCount.
     *
     */
    public java.lang.Integer getInvalidAddrCount() {
        return invalidAddrCount;
    }
    
    /** Setter for property invalidAddrCount.
     * @param invalidAddrCount New value of property invalidAddrCount.
     *
     */
    public void setInvalidAddrCount(java.lang.Integer invalidAddrCount) {
        this.invalidAddrCount = invalidAddrCount;
    }
    
    /** Getter for property InvalidAddressList.
     * @return Value of property InvalidAddressList.
     *
     */
    public java.util.Map getInvalidAddressList() {
        return InvalidAddressList;
    }
    
    /** Setter for property InvalidAddressList.
     * @param InvalidAddressList New value of property InvalidAddressList.
     *
     */
    public void setInvalidAddressList(java.util.Map InvalidAddressList) {
        this.InvalidAddressList = InvalidAddressList;
    }
    
}
