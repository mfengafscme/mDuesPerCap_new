/*
 * ReportSortFieldData.java
 *
 * An instance of this class represents a row in the "Report_SortFields] table.
 *
 */

package org.afscme.enterprise.reporting.base.access;

import java.io.Serializable;

public class ReportSortFieldData implements Serializable, Comparable {
    
    public static final String ASCEND = "A";
    public static final String DESCEND = "D";
        
    protected Integer fieldPK = null;
    protected short fieldSortOrder = 0;
    protected String fieldSortDirection = ASCEND;
    
    /** Creates a new instance of ReportSortFieldData */
    public ReportSortFieldData() {
    }
    
    /** Getter for property fieldPK.
     * @return Value of property fieldPK.
     */
    public java.lang.Integer getFieldPK() {
        return fieldPK;
    }    
    
    /** Setter for property fieldPK.
     * @param fieldPK New value of property fieldPK.
     */
    public void setFieldPK(java.lang.Integer fieldPK) {
        this.fieldPK = fieldPK;
    }    

    /** Getter for property fieldSortOrder.
     * @return Value of property fieldSortOrder.
     */
    public short getFieldSortOrder() {
        return fieldSortOrder;
    }
    
    /** Setter for property fieldSortOrder.
     * @param fieldSortOrder New value of property fieldSortOrder.
     */
    public void setFieldSortOrder(short fieldSortOrder) {
        this.fieldSortOrder = fieldSortOrder;
    }
    
    /** Getter for property fieldSortDirection.
     * @return Value of property fieldSortDirection.
     */
    public java.lang.String getFieldSortDirection() {
        return fieldSortDirection;
    }    
    
    /** Setter for property fieldSortDirection.
     * @param fieldSortDirection New value of property fieldSortDirection.
     */
    public void setFieldSortDirection(java.lang.String fieldSortDirection) {
        this.fieldSortDirection = fieldSortDirection;
    }
    
    /** Compare to the same type using its "fieldSortOrder" field */
    public int compareTo(Object obj) {
        ReportSortFieldData target = (ReportSortFieldData)obj;
        
        short targetSortOrder = target.getFieldSortOrder();
        if (this.fieldSortOrder < targetSortOrder)
            return -1;
        else if (this.fieldSortOrder == targetSortOrder)
            return 0;
        else
            return 1;
    }
    
}
