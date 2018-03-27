package org.afscme.enterprise.common;

import java.sql.Timestamp;

/**
 * Contains 'record data' (auditing data) common to many objects.
 */
public class RecordData 
{
    
    /**
     * Primary key of the record
     */
    public Integer pk;
    
    /**
     * primary key of the user that created the record
     */
    public Integer createdBy;
    
    /**
     * primary key if the last user to modify the record
     */
    public Integer modifiedBy;
    
    /**
     * date the record was created
     */
    public Timestamp createdDate;
    
    /**
     * date the record was last modified
     */
    public Timestamp modifiedDate;
    
    /** Getter for property createdBy.
     * @return Value of property createdBy.
     *
     */
    public Integer getCreatedBy() {
        return createdBy;
    }
    
    /** Setter for property createdBy.
     * @param createdBy New value of property createdBy.
     *
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    
    /** Getter for property createdDate.
     * @return Value of property createdDate.
     *
     */
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    
    /** Setter for property createdDate.
     * @param createdDate New value of property createdDate.
     *
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    
    /** Getter for property modifiedBy.
     * @return Value of property modifiedBy.
     *
     */
    public Integer getModifiedBy() {
        return modifiedBy;
    }
    
    /** Setter for property modifiedBy.
     * @param modifiedBy New value of property modifiedBy.
     *
     */
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    /** Getter for property modifiedDate.
     * @return Value of property modifiedDate.
     *
     */
    public Timestamp getModifiedDate() {
        return modifiedDate;
    }
    
    /** Setter for property modifiedDate.
     * @param modifiedDate New value of property modifiedDate.
     *
     */
    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    /** Getter for property pk.
     * @return Value of property pk.
     *
     */
    public Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     *
     */
    public void setPk(Integer pk) {
        this.pk = pk;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        sb.append("pk = ");
        sb.append(this.pk);
        sb.append(", createdBy = ");
        sb.append(this.createdBy);
        sb.append(", createdDate = ");
        sb.append(this.createdDate);
        sb.append(", modifiedBy = ");
        sb.append(this.modifiedBy);
        sb.append(", modifiedDate = ");
        sb.append(this.modifiedDate);
        sb.append("]");
        return sb.toString().trim();
    }
    
}
