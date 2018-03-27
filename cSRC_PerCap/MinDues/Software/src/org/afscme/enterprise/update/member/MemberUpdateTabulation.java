package org.afscme.enterprise.update.member;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import org.afscme.enterprise.update.UpdateTabulation;
import org.afscme.enterprise.util.CollectionUtil;

/**
 * Used during a member update to keep a running totals of the modifications.
 */
public class MemberUpdateTabulation extends UpdateTabulation {
    
    /**
     * Number of members that have been changed to 'T' records by the affiliate.  The 
     * total number of 'T' records will be this number, plus the number of 'T' records 
     * created by inactivation of officers.
     */
    protected int forcedTCount;
    
    /**
     * Map of affPk to person primary keys for the current officers.
     * key = affPk; value = List of personPk's.
     */
    protected Map currentOfficers;

    /**
     * a Set to keep track of matched (in system) members's personPK for each affiliate
     */
    protected Set matchedMemberPks;
    
    
    public MemberUpdateTabulation() {
        super();
        
        currentOfficers = new HashMap();
        matchedMemberPks = new HashSet();        
    }
    
    public String toString() {
        return "MemberUpdateTabulation[" + 
                    "additions=" + CollectionUtil.toString(additions) +
                    ", addresses=" + CollectionUtil.toString(addresses) +
                    ", currentCount=" + currentCount +
                    ", currentOfficers=" + CollectionUtil.toString(currentOfficers) +
                    ", forcedTCount=" + forcedTCount +
                    ", inactivated=" + CollectionUtil.toString(inactivated) +
                    ", notIncluded=" + CollectionUtil.toString(notIncluded) +
                    ", tRecords=" + CollectionUtil.toString(tRecords) +
                    ", updates=" + CollectionUtil.toString(updates) +
                    ", matchedMemberPks=" + CollectionUtil.toString(matchedMemberPks) +
                "]"
        ;
    }
    
    /** Getter for property forcedTCount.
     * @return Value of property forcedTCount.
     *
     */
    public int getForcedTCount() {
        return forcedTCount;
    }
    
    /** Setter for property forcedTCount.
     * @param forcedTCount New value of property forcedTCount.
     *
     */
    public void setForcedTCount(int forcedTCount) {
        this.forcedTCount = forcedTCount;
    }
    
    public void incrementForcedTCount() {
        this.forcedTCount += 1;
    }
    
    /** Getter for property currentOfficers.
     * @return Value of property currentOfficers.
     *
     */
    public Map getCurrentOfficers() {
        return currentOfficers;
    }
    
    /** Setter for property currentOfficers.
     * @param currentOfficers New value of property currentOfficers.
     *
     */
    public void setCurrentOfficers(Map currentOfficers) {
        this.currentOfficers = currentOfficers;
    }
    
    public void addCurrentOfficer(Integer affPk, Integer personPk) {
        if (currentOfficers == null) {
            currentOfficers = new HashMap();
        }
        if (currentOfficers.get(affPk) == null) {
            currentOfficers.put(affPk, new ArrayList());
        }
        ((List)currentOfficers.get(affPk)).add(personPk);
    }

    /** Getter for property matchedMemberPks.
     * @return Value of property matchedMemberPks.
     *
     */
    public Set getMatchedMemberPks() {
        return matchedMemberPks;
    }
    
    /** Setter for property matchedMemberPks.
     * @param matchedMemberPks New value of property matchedMemberPks.
     *
     */
    public void setMatchedMemberPks(Set matchedMemberPks) {
        this.matchedMemberPks = matchedMemberPks;
    }
    
}
