package org.afscme.enterprise.update;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


/**
 * Used during an update to keep a running totals of the modifications.
 */
public class UpdateTabulation {
    
    /**
     * Map of MemberElement objects to be updated, by personPk.
     * key = personPk; value = MemberElement
     */
    protected Map updates; 
    
    /**
     * List of MemberElement objects to be added.
     */
    protected List additions; 
    
    /**
     * Map of affPk to personPk to be inactivated
     * key = affPk; value = List of personPk's
     */
    protected Map inactivated;
    
    /**
     * Map of affPk to personPk officers to be have T record
     * key = affPk; value = List of personPk's
     */
    protected Map tRecords;
    
    /**
     * List of addresses that will be updated.
     */
    protected List addresses;
    
    
    
    /**
     * List of MemberElement that will NOT be included in the transactions (member exception)
     */
    protected List notIncluded;
    
    /**
     * Number of active members/officers currently in the affiliate
     */
    protected int currentCount;
    
    
    public UpdateTabulation() {
        updates = new HashMap();
        additions = new ArrayList();
        inactivated = new HashMap();
        tRecords = new HashMap();
        notIncluded = new ArrayList();
        currentCount = 0;
    }
    
    /** Getter for property additions.
     * @return Value of property additions.
     *
     */
    public List getAdditions() {
        return additions;
    }
    
    /** Setter for property additions.
     * @param additions New value of property additions.
     *
     */
    public void setAdditions(List additions) {
        this.additions = additions;
    }
    
    /** Getter for property addresses.
     * @return Value of property addresses.
     *
     */
    public List getAddresses() {
        return addresses;
    }
    
    /** Setter for property addresses.
     * @param addresses New value of property addresses.
     *
     */
    public void setAddresses(List addresses) {
        this.addresses = addresses;
    }
    
    /** Getter for property currentCount.
     * @return Value of property currentCount.
     *
     */
    public int getCurrentCount() {
        return currentCount;
    }
    
    /** Setter for property currentCount.
     * @param currentCount New value of property currentCount.
     *
     */
    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
    
    public void incrementCurrentCount() {
        this.currentCount += 1;
    }
    
    public void decrementCurrentCount() {
        this.currentCount -= 1;
    }
    
    /** Getter for property inactivated.
     * @return Value of property inactivated.
     *
     */
    public Map getInactivated() {
        return inactivated;
    }
    
    /** Setter for property inactivated.
     * @param inactivated New value of property inactivated.
     *
     */
    public void setInactivated(Map inactivated) {
        this.inactivated = inactivated;
    }
    
    public void addInactivated(Integer affPk, Integer personPk) {
        if (inactivated == null) {
            inactivated = new HashMap();
        }
        if (inactivated.get(affPk) == null) {
            inactivated.put(affPk, new ArrayList());
        }
        ((List)inactivated.get(affPk)).add(personPk);
    }
    
    /** Getter for property notIncluded.
     * @return Value of property notIncluded.
     *
     */
    public List getNotIncluded() {
        return notIncluded;
    }
    
    /** Setter for property notIncluded.
     * @param notIncluded New value of property notIncluded.
     *
     */
    public void setNotIncluded(List notIncluded) {
        this.notIncluded = notIncluded;
    }
    
    /** Getter for property tRecords.
     * @return Value of property tRecords.
     *
     */
    public Map getTRecords() {
        return tRecords;
    }
    
    /** Setter for property tRecords.
     * @param tRecords New value of property tRecords.
     *
     */
    public void setTRecords(Map tRecords) {
        this.tRecords = tRecords;
    }
    
    public void addTRecords(Integer affPk, Integer personPk) {
        if (tRecords == null) {
            tRecords = new HashMap();
        }
        if (tRecords.get(affPk) == null) {
            tRecords.put(affPk, new ArrayList());
        }
        ((List)tRecords.get(affPk)).add(personPk);
    }
    
    /** Getter for property updates.
     * @return Value of property updates.
     *
     */
    public Map getUpdates() {
        return updates;
    }
    
    /** Setter for property updates.
     * @param updates New value of property updates.
     *
     */
    public void setUpdates(Map updates) {
        this.updates = updates;
    }
    
}
