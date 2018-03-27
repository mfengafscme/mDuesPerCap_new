package org.afscme.enterprise.update;

import java.io.Serializable;

/**
 * Sent by Struts actions to UpdateMessageBean when a pre update summary is to be 
 * generated.
 */
public class PreUpdateMessage implements Serializable {

    
    /**
     * primary key of the file queue to be run the pre-update on
     */
    private Integer queuePk;
    
    /**
     * primary key of the user performing the pre-update
     */
    private Integer userPk;
    
    /**
     * file type code 
     */
    private int fileType;
    
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
    
    /** Getter for property fileType.
     * @return Value of property fileType.
     *
     */
    public int getFileType() {
        return fileType;
    }
    
    /** Setter for property fileType.
     * @param fileType New value of property fileType.
     *
     */
    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
    
}
