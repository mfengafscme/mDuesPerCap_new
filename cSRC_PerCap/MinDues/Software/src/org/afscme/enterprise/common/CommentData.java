package org.afscme.enterprise.common;

import java.sql.Timestamp;

/**
 * Comment fields that are common to components that maintain a comment history.
 */
public class CommentData 
{
    
    /**
     * The actual comment text.
     */
    private String comment;
    private Timestamp commentDt;
    
    private RecordData recordData;
    
    /** Getter for property comment.
     * @return Value of property comment.
     *
     */
    public java.lang.String getComment() {
        return comment;
    }
    
    /** Setter for property comment.
     * @param comment New value of property comment.
     *
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }
    
     /** Getter for property recordData.
     * @return Value of property recordData.
     *
     */
    public org.afscme.enterprise.common.RecordData getRecordData() {
        return recordData;
    }
    
    /** Setter for property recordData.
     * @param recordData New value of property recordData.
     *
     */
    public void setRecordData(org.afscme.enterprise.common.RecordData recordData) {
        this.recordData = recordData;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        sb.append("comment = ");
        sb.append(this.comment);
        sb.append(", commentDt = ");
        sb.append(this.commentDt);        
        sb.append(", recordData = ");
        sb.append(this.recordData);
        sb.append("]");
        return sb.toString().trim();
    }
    
    /** Getter for property commentDt.
     * @return Value of property commentDt.
     *
     */
    public java.sql.Timestamp getCommentDt() {
        return commentDt;
    }
    
    /** Setter for property commentDt.
     * @param commentDt New value of property commentDt.
     *
     */
    public void setCommentDt(java.sql.Timestamp commentDt) {
        this.commentDt = commentDt;
    }
    
}
