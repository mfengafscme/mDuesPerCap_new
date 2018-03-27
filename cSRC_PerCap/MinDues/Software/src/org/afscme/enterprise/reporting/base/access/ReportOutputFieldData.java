/*
 * ReportOutputFieldData.java
 *
 * An instance of this class represents a row in the [Report_Output_Fields] table.
 */

package org.afscme.enterprise.reporting.base.access;

import java.io.Serializable;

public class ReportOutputFieldData implements Serializable, Comparable {
    
    protected Integer fieldPK = null;
    protected short outputOrder = 1;
    
    /** Creates a new instance of ReportOutputFieldData */
    public ReportOutputFieldData() {
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
    
    /** Getter for property outputOrder.
     * @return Value of property outputOrder.
     */
    public short getOutputOrder() {
        return outputOrder;
    }
    
    /** Setter for property outputOrder.
     * @param outputOrder New value of property outputOrder.
     */
    public void setOutputOrder(short outputOrder) {
        this.outputOrder = outputOrder;
    }
    
    /** Compare to the same type using its "outputOrder" field */
    public int compareTo(Object obj) {
        ReportOutputFieldData target = (ReportOutputFieldData)obj;
        
        short targetOutputOrder = target.getOutputOrder();
        if (this.outputOrder < targetOutputOrder)
            return -1;
        else if (this.outputOrder == targetOutputOrder)
            return 0;
        else
            return 1;
    }
    
}
