/*
 * ReportGenerationPayload.java
 * 
 * Used to set the "javax.jms.ObjectMessage" for report generation.
 */

package org.afscme.enterprise.reporting.base.generator;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.afscme.enterprise.reporting.ReportHandler;

public class ReportMessageData implements Serializable {

    protected Set accessibleAffiliates;

    protected boolean customReport;

    /** Holds the value of the property for filtering duplicate addresses */
    protected boolean filterDuplicateAddresses;

    protected MediaType mediaType;

    protected OutputFormat outputFormat;

    protected ReportHandler reportHandler;

    protected Integer reportPK;

    protected Date requestedTime;

    protected String requestorUID;

    protected Map runtimeCriteria;

    /**
	 * Getter for property accessibleAffiliates.
	 * 
	 * @return Value of property accessibleAffiliates.
	 *  
	 */
    public java.util.Set getAccessibleAffiliates() {
        return accessibleAffiliates;
    }

    /**
	 * Getter for property mediaType.
	 * 
	 * @return Value of property mediaType.
	 */
    public org
        .afscme
        .enterprise
        .reporting
        .base
        .generator
        .MediaType getMediaType() {
        return mediaType;
    }

    /**
	 * Getter for property outputFormat.
	 * 
	 * @return Value of property outputFormat.
	 */
    public org
        .afscme
        .enterprise
        .reporting
        .base
        .generator
        .OutputFormat getOutputFormat() {
        return outputFormat;
    }

    /**
	 * Getter for property reportHandler.
	 * 
	 * @return Value of property reportHandler.
	 */
    public ReportHandler getReportHandler() {
        return reportHandler;
    }

    /**
	 * Getter for property reportPK.
	 * 
	 * @return Value of property reportPK.
	 */
    public java.lang.Integer getReportPK() {
        return reportPK;
    }

    /**
	 * Getter for property requestedTime.
	 * 
	 * @return Value of property requestedTime.
	 */
    public Date getRequestedTime() {
        return requestedTime;
    }

    /**
	 * Getter for property requestorUID.
	 * 
	 * @return Value of property requestorUID.
	 */
    public java.lang.String getRequestorUID() {
        return requestorUID;
    }

    /**
	 * Getter for property runtimeCriteria.
	 * 
	 * @return Value of property runtimeCriteria.
	 */
    public Map getRuntimeCriteria() {
        return runtimeCriteria;
    }

    /**
	 * Getter for property customReport.
	 * 
	 * @return Value of property customReport.
	 */
    public boolean isCustomReport() {
        return customReport;
    }

    /** getter for the filterDuplicateAddresses property */
    public boolean isFilterDuplicateAddresses() {
        return filterDuplicateAddresses;
    }

    /**
	 * Setter for property accessibleAffiliates.
	 * 
	 * @param accessibleAffiliates
	 *            New value of property accessibleAffiliates.
	 *  
	 */
    public void setAccessibleAffiliates(java.util.Set accessibleAffiliates) {
        this.accessibleAffiliates = accessibleAffiliates;
    }

    /**
	 * Setter for property customReport.
	 * 
	 * @param customReport
	 *            New value of property customReport.
	 */
    public void setCustomReport(boolean customReport) {
        this.customReport = customReport;
    }

    /** Setter method for the filterDuplicateAddresses property */
    public void setFilterDuplicateAddresses(boolean value) {
        filterDuplicateAddresses = value;
    }

    /**
	 * Setter for property mediaType.
	 * 
	 * @param mediaType
	 *            New value of property mediaType.
	 */
    public void setMediaType(
        org.afscme.enterprise.reporting.base.generator.MediaType mediaType) {
        this.mediaType = mediaType;
    }

    /**
	 * Setter for property outputFormat.
	 * 
	 * @param outputFormat
	 *            New value of property outputFormat.
	 */
    public void setOutputFormat(
        org
            .afscme
            .enterprise
            .reporting
            .base
            .generator
            .OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
	 * Setter for property reportHandler.
	 * 
	 * @param reportHandler
	 *            New value of property reportHandler.
	 */
    public void setReportHandler(ReportHandler reportHandler) {
        this.reportHandler = reportHandler;
    }

    /**
	 * Setter for property reportPK.
	 * 
	 * @param reportPK
	 *            New value of property reportPK.
	 */
    public void setReportPK(java.lang.Integer reportPK) {
        this.reportPK = reportPK;
    }

    /**
	 * Setter for property requestedTime.
	 * 
	 * @param requestedTime
	 *            New value of property requestedTime.
	 */
    public void setRequestedTime(Date requestedTime) {
        this.requestedTime = requestedTime;
    }

    /**
	 * Setter for property requestorUID.
	 * 
	 * @param requestorUID
	 *            New value of property requestorUID.
	 */
    public void setRequestorUID(java.lang.String requestorUID) {
        this.requestorUID = requestorUID;
    }

    /**
	 * Setter for property runtimeCriteria.
	 * 
	 * @param runtimeCriteria
	 *            New value of property runtimeCriteria.
	 */
    public void setRuntimeCriteria(Map runtimeCriteria) {
        this.runtimeCriteria = runtimeCriteria;
    }

}
