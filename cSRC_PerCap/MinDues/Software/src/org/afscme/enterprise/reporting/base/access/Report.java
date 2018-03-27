/*
 * Report.java --- representing an entire report
 *
 */

package org.afscme.enterprise.reporting.base.access;


import java.io.Serializable;
import java.util.Map;

public class Report implements Serializable {
    
    /** Report's primary key */
    protected Integer reportPK = null;
    
    /** Report's ReportData object */
    protected ReportData reportData = null;
    
    /** A map of "fieldPK" to "ReportOutputFieldData"  */
    protected Map outputFields = null;
    
    /** A map of "feildPK" to "ReportSortFieldData" */
    protected Map sortFields = null;
    
    /** A map of "fieldPK" to a List of "ReportCriterionData".
     * The list is ordered by the "criterion_sequence_pk".
     */
    protected Map criteriaFields = null;

    
    /** Creates a new instance of Report */
    public Report() {
    }
    
    /** Getter for property reportPK.
     * @return Value of property reportPK.
     */
    public java.lang.Integer getReportPK() {
        return reportPK;
    }
    
    /** Setter for property reportPK.
     * @param reportPK New value of property reportPK.
     */
    public void setReportPK(java.lang.Integer reportPK) {
        this.reportPK = reportPK;
    }
    
    /** Getter for property reportData.
     * @return Value of property reportData.
     */
    public ReportData getReportData() {
        return reportData;
    }
    
    /** Setter for property reportData.
     * @param reportData New value of property reportData.
     */
    public void setReportData(ReportData reportData) {
        this.reportData = reportData;
    }
    
    /** Getter for property outputFields.
     * @return Value of property outputFields.
     */
    public java.util.Map getOutputFields() {
        return outputFields;
    }
    
    /** Setter for property outputFields.
     * @param outputFields New value of property outputFields.
     */
    public void setOutputFields(java.util.Map outputFields) {
        this.outputFields = outputFields;
    }
    
    /** Getter for property sortFields.
     * @return Value of property sortFields.
     */
    public java.util.Map getSortFields() {
        return sortFields;
    }
    
    /** Setter for property sortFields.
     * @param sortFields New value of property sortFields.
     */
    public void setSortFields(java.util.Map sortFields) {
        this.sortFields = sortFields;
    }
    
    /** Getter for property criteriaFields.
     * @return Value of property criteriaFields.
     */
    public java.util.Map getCriteriaFields() {
        return criteriaFields;
    }
    
    /** Setter for property criteriaFields.
     * @param criteriaFields New value of property criteriaFields.
     */
    public void setCriteriaFields(java.util.Map criteriaFields) {
        this.criteriaFields = criteriaFields;
    }
    
}
