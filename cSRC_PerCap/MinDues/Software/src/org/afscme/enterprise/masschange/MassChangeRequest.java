package org.afscme.enterprise.masschange;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains data about a requested set of mass changes for an affiliate.
 */
public class MassChangeRequest {
    
    /** User ID of the user that requested a group of changes. */
    private Integer userPk;
    /** Primary Key of the Affiliate. */
    private Integer affPk;
    /**
     * Ordered List of MassChangeData objects requested by a user. Use this ordering 
     * to determine the priority for the processing of the Mass Changes.
     */
    private List changePriorityList;
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property changePriorityList.
     * @return Value of property changePriorityList.
     *
     */
    public List getChangePriorityList() {
        return changePriorityList;
    }
    
    /** Setter for property changePriorityList.
     * @param changePriorityList New value of property changePriorityList.
     *
     */
    public void addToChangePriorityList(MassChangeData data) {
        if (this.changePriorityList == null) {
            this.changePriorityList = new ArrayList();
        }
        this.changePriorityList.add(data);
    }
    
    /** Getter for property userPk.
     * @return Value of property userPk.
     *
     */
    public Integer getUserPk() {
        return userPk;
    }
    
    /** Setter for property userPk.
     * @param userPk New value of property userPk.
     *
     */
    public void setUserPk(Integer userPk) {
        this.userPk = userPk;
    }
    
}
