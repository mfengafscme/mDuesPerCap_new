package org.afscme.enterprise.update;

import java.io.Serializable;
import java.util.List;

/**
 * Sent by UpdateManager to UpdateMessageBean when an update is to be performed.
 */
public class UpdateMessage implements Serializable {
    
    private Integer queuePk;
    private Integer userPk;
    private List affPks;
    
    /** Getter for property queuePk.
     * @return Value of property queuePk.
     */
    public Integer getQueuePk() {
        return queuePk;
    }
    
    /** Setter for property queuePk.
     * @param queuePk New value of property queuePk.
     */
    public void setQueuePk(Integer queuePk) {
        this.queuePk = queuePk;
    }
    
    /** Getter for property userPk.
     * @return Value of property userPk.
     */
    public Integer getUserPk() {
        return userPk;
    }
    
    /** Setter for property userPk.
     * @param userPk New value of property userPk.
     */
    public void setUserPk(Integer userPk) {
        this.userPk = userPk;
    }
    
    /** Getter for property affPks.
     * @return Value of property affPks.
     *
     */
    public List getAffPks() {
        return affPks;
    }
    
    /** Setter for property affPks.
     * @param affPks New value of property affPks.
     *
     */
    public void setAffPks(List affPks) {
        this.affPks = affPks;
    }
    
}
