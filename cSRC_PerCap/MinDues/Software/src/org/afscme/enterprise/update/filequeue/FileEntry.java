package org.afscme.enterprise.update.filequeue;

import java.sql.Timestamp;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.common.web.SearchForm;
/**
 * Represents a file in the FileQueue, along with it's status and affiliate 
 * association.
 */
public class FileEntry{
    
    private Integer affPk;    
    private AffiliateIdentifier affId;    
    private Integer queuePk;    
    private String fileName;    
    private String filePath;    
    private int status;    
    private int fileType;    
    private int fileQueue;    
    private int updateType;    
    private String comments;    
    private Timestamp processedDate;    
    private Timestamp validDate;    
    private Timestamp receivedDate;    
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property affId.
     * @return Value of property affId.
     */
    public AffiliateIdentifier getAffId() {
        return affId;
    }
    
    /** Setter for property affId.
     * @param affId New value of property affId.
     */
    public void setAffId(AffiliateIdentifier affId) {
        this.affId = affId;
    }
    
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
    
    /** Getter for property fileName.
     * @return Value of property fileName.
     */
    public String getFileName() {
        return fileName;
    }
    
    /** Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /** Getter for property filePath.
     * @return Value of property filePath.
     */
    public String getFilePath() {
        return filePath;
    }
    
    /** Setter for property filePath.
     * @param filePath New value of property filePath.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    /** Getter for property processedDate.
     * @return Value of property processedDate.
     */
    public Timestamp getProcessedDate() {
        return processedDate;
    }
    
    /** Setter for property processedDate.
     * @param processedDate New value of property processedDate.
     */
    public void setProcessedDate(Timestamp processedDate) {
        this.processedDate = processedDate;
    }
    
    /** Getter for property validDate.
     * @return Value of property validDate.
     */
    public Timestamp getValidDate() {
        return validDate;
    }
    
    /** Setter for property validDate.
     * @param validDate New value of property validDate.
     */
    public void setValidDate(Timestamp validDate) {
        this.validDate = validDate;
    }
    
    /** Getter for property receivedDate.
     * @return Value of property receivedDate.
     */
    public Timestamp getReceivedDate() {
        return receivedDate;
    }
    
    /** Setter for property receivedDate.
     * @param receivedDate New value of property receivedDate.
     */
    public void setReceivedDate(Timestamp receivedDate) {
        this.receivedDate = receivedDate;
    }
    
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public int getStatus() {
        return status;
    }    
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(int status) {
        this.status = status;
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
    
    /** Getter for property fileQueue.
     * @return Value of property fileQueue.
     *
     */
    public int getFileQueue() {
        return fileQueue;
    }
    
    /** Setter for property fileQueue.
     * @param fileQueue New value of property fileQueue.
     *
     */
    public void setFileQueue(int fileQueue) {
        this.fileQueue = fileQueue;
    }
    
    /** Getter for property updateType.
     * @return Value of property updateType.
     *
     */
    public int getUpdateType() {
        return updateType;
    }
    
    /** Setter for property updateType.
     * @param updateType New value of property updateType.
     *
     */
    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }
    
}
