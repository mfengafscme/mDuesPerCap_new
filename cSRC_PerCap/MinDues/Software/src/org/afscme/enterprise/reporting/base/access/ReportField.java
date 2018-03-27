/*
 * ReportField.java
 *
 */

package org.afscme.enterprise.reporting.base.access;

import java.io.Serializable;
import java.util.Set;

public class ReportField implements Serializable, Comparable {

    public static final char DISPLAY_TYPE_STRING = 'S';
    public static final char DISPLAY_TYPE_DATE = 'D';
    public static final char DISPLAY_TYPE_INTEGER = 'I';
    public static final char DISPLAY_TYPE_CODE = 'C';    
    public static final char DISPLAY_TYPE_BOOLEAN = 'B';    

    protected Integer pk;
    protected float printWidth;       // the width in "inch" for PDF generator
    protected char entityType;      
    protected String categoryName;  // for grouping the display such as "Employer Data", "Legislative Data", or "Participation Data"
    protected String tableName;
    protected String columnName;
    protected String displayName;
    protected String fullName;
    protected char displayType;
    protected String commonCodeTypeKey;
    
    // note, in the current system, these two are muturely exclusive.
    // That is, if one is not "null", the other must be "null". We don't have the situation
    // where a child field has further children of its own.
    protected Integer parentPK;  // the primary key of its parent field (if any)
    protected Set children;      // include all children of this field.        
    
    // accessible mark
    protected boolean accessible = false;
    
    /** Getter for property categoryName.
     * @return Value of property categoryName.
     */
    public java.lang.String getCategoryName() {
        return categoryName;
    }
    
    /** Setter for property categoryName.
     * @param categoryName New value of property categoryName.
     */
    public void setCategoryName(java.lang.String categoryName) {
        this.categoryName = categoryName;
    }
    
    /** Getter for property displayName.
     * @return Value of property displayName.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /** Setter for property displayName.
     * @param displayName New value of property displayName.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    /** Getter for property columnName.
     * @return Value of property columnName.
     */
    public String getColumnName() {
        return columnName;
    }
    
    /** Setter for property columnName.
     * @param columnName New value of property columnName.
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    /** Getter for property commonCodeTypeKey.
     * @return Value of property commonCodeTypeKey.
     */
    public String getCommonCodeTypeKey() {
        return commonCodeTypeKey;
    }
    
    /** Setter for property commonCodeTypeKey.
     * @param commonCodeTypeKey New value of property commonCodeTypeKey.
     */
    public void setCommonCodeTypeKey(String commonCodeTypeKey) {
        this.commonCodeTypeKey = commonCodeTypeKey;
    }
    
    /** Getter for property displayType.
     * @return Value of property displayType.
     */
    public char getDisplayType() {
        return displayType;
    }
    
    /** Setter for property displayType.
     * @param displayType New value of property displayType.
     */
    public void setDisplayType(char displayType) {
        this.displayType = displayType;
    }
    
    /** Getter for property entityType.
     * @return Value of property entityType.
     */
    public char getEntityType() {
        return entityType;
    }
    
    /** Setter for property entityType.
     * @param entityType New value of property entityType.
     */
    public void setEntityType(char entityType) {
        this.entityType = entityType;
    }
    
    /** Getter for property printWidth.
     * @return Value of property printWidth.
     */
    public float getPrintWidth() {
        return printWidth;
    }
    
    /** Setter for property printWidth.
     * @param printWidth New value of property printWidth.
     */
    public void setPrintWidth(float printWidth) {
        this.printWidth = printWidth;
    }
    
    /** Getter for property tableName.
     * @return Value of property tableName.
     */
    public java.lang.String getTableName() {
        return tableName;
    }
    
    /** Setter for property tableName.
     * @param tableName New value of property tableName.
     */
    public void setTableName(java.lang.String tableName) {
        this.tableName = tableName;
    }
    
    /** Getter for property pk.
     * @return Value of property pk.
     */
    public Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     */
    public void setPk(Integer pk) {
        this.pk = pk;
    }
    
    /** Getter for property parentPK.
     * @return Value of property parentPK.
     */
    public java.lang.Integer getParentPK() {
        return parentPK;
    }
    
    /** Setter for property parentPK.
     * @param parentPK New value of property parentPK.
     */
    public void setParentPK(java.lang.Integer parentPK) {
        this.parentPK = parentPK;
    }
    
    /** Check if this field has parent
     */
    public boolean hasParent() {
        boolean hasParent = false;
        if (parentPK != null)
            hasParent = true;
        return hasParent;
    }
    
    /** Getter for property children.
     * @return Value of property children.
     */
    public java.util.Set getChildren() {
        return children;
    }
    
    /** Setter for property children.
     * @param children New value of property children.
     */
    public void setChildren(java.util.Set children) {
        this.children = children;
    }
    
    /** Check if this field has children
     */
    public boolean hasChildren() {
        return (children != null);
    }
    
    /** Getter for property accessible.
     * @return Value of property accessible.
     */
    public boolean isAccessible() {
        return accessible;
    }    

    /** Setter for property accessible.
     * @param accessible New value of property accessible.
     */
    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }    
    
    /** Getter for property fullName.
     * @return Value of property fullName.
     *
     */
    public java.lang.String getFullName() {
        return fullName;
    }
    
    /** Setter for property fullName.
     * @param fullName New value of property fullName.
     *
     */
    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
    }
    
    /** Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.<p>
     *
     * @param   o the Object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     * 		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this Object.
     *
     */
    public int compareTo(Object o) {
        return ((ReportField)o).pk.compareTo(pk);
    }
}
