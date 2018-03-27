/*
 * ReportCriterionData.java
 *
 * An instance of this class represents a row in [Report_Selection_Criteria] table.
 *
 */

package org.afscme.enterprise.reporting.base.access;

import java.io.Serializable;
import java.util.Set;
import org.afscme.enterprise.reporting.base.BRUtil;
import org.afscme.enterprise.util.CollectionUtil;
import org.apache.log4j.Logger;

public class ReportCriterionData implements Serializable {

    private static Logger logger =  Logger.getLogger(ReportCriterionData.class);    
    
    protected Integer fieldPK;
    protected int criterionSequence;

    protected String operator = "EQ";
    protected String value1;
    protected String value2;     // when operator is "BT", this value should not be "NULL".
    protected boolean editable;
    protected Integer codePK;

    protected boolean codeField;            // indicate if this field is a coded field.

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

    /** Getter for property criterionSequence.
     * @return Value of property criterionSequence.
     */
    public int getCriterionSequence() {
        return criterionSequence;
    }

    /** Setter for property criterionSequence.
     * @param criterionSequence New value of property criterionSequence.
     */
    public void setCriterionSequence(int criterionSequence) {
        this.criterionSequence = criterionSequence;
    }

    /** Getter for property value1.
     * @return Value of property value1.
     */
    public java.lang.String getValue1() {
        return value1;
    }

    /** Setter for property value1.
     * @param value1 New value of property value1.
     */
    public void setValue1(java.lang.String value1) {
        this.value1 = value1;
    }

    /** Getter for property value2.
     * @return Value of property value2.
     */
    public java.lang.String getValue2() {
        return value2;
    }

    /** Setter for property value2.
     * @param value2 New value of property value2.
     */
    public void setValue2(java.lang.String value2) {
        this.value2 = value2;
    }

    /** Getter for property editable.
     * @return Value of property editable.
     */
    public boolean isEditable() {
        return editable;
    }

    /** Setter for property editable.
     * @param editable New value of property editable.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /** Getter for property codePK.
     * @return Value of property codePK.
     */
    public java.lang.Integer getCodePK() {
        return codePK;
    }

    /** Setter for property codePK.
     * @param codePK New value of property codePK.
     */
    public void setCodePK(java.lang.Integer codePK) {
        this.codePK = codePK;
    }

    /** Getter for property codeField.
     * @return Value of property codeField.
     */
    public boolean isCodeField() {
        return codeField;
    }

    /** Setter for property codeField.
     * @param codeField New value of property codeField.
     */
    public void setCodeField(boolean codeField) {
        this.codeField = codeField;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public void debug() {
        logger.debug("Criterion information ****************");
        logger.debug("pk = " + fieldPK);
        logger.debug("criterionSequence = " + criterionSequence);
        logger.debug("operator = " + operator);
        logger.debug("value 1 = " + value1);
        logger.debug("value 2 = " + value2);
        logger.debug("editable = " + editable);
    }

}
