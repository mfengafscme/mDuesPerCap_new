/*
 * @(#)ReportEmailData.java
 *
 * Copyright (c) 2002 AFSCME org.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of AFSCME
 * Orgnization. ("Confidential Information"). 
 */
 
package org.afscme.enterprise.reporting.base.email;
 
import java.io.Serializable;
import java.io.InputStream;
import java.util.Date;
 
/**
 * This is a value object used by the EmailGenerator
 */
public class ReportEmailData implements Serializable {
    
	public static final String TEXT_MIME = "text/plain";
	public static final String PDF_MIME = "application/pdf";
    
    protected InputStream generatedReport = null;
 	protected String from = "";
 	protected String fromName = "";
 	protected String subject = "no subject";
 	protected String reportName = "";
 	protected String outputFormat = "";
 	protected String requestorUserID = "";
 	protected Date requestedTime = null;
 	protected boolean mailingList = false;
 	protected int numOfMail = 0;
 	protected String reportMimeType = TEXT_MIME;
    
    /** Getter for property generatedReport.
     * @return Value of property generatedReport.
     */
    public java.io.InputStream getGeneratedReport() {
        return generatedReport;
    }
    
    /** Setter for property generatedReport.
     * @param generatedReport New value of property generatedReport.
     */
    public void setGeneratedReport(java.io.InputStream generatedReport) {
        this.generatedReport = generatedReport;
    }
    
    /** Getter for property from.
     * @return Value of property from.
     */
    public java.lang.String getFrom() {
        return from;
    }
    
    /** Setter for property from.
     * @param from New value of property from.
     */
    public void setFrom(java.lang.String from) {
        this.from = from;
    }
    
    /** Getter for property fromName.
     * @return Value of property fromName.
     */
    public java.lang.String getFromName() {
        return fromName;
    }
    
    /** Setter for property fromName.
     * @param fromName New value of property fromName.
     */
    public void setFromName(java.lang.String fromName) {
        this.fromName = fromName;
    }
    
    /** Getter for property subject.
     * @return Value of property subject.
     */
    public java.lang.String getSubject() {
        return subject;
    }
    
    /** Setter for property subject.
     * @param subject New value of property subject.
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }
    
    /** Getter for property reportName.
     * @return Value of property reportName.
     */
    public java.lang.String getReportName() {
        return reportName;
    }
    
    /** Setter for property reportName.
     * @param reportName New value of property reportName.
     */
    public void setReportName(java.lang.String reportName) {
        this.reportName = reportName;
    }
    
    /** Getter for property outputFormat.
     * @return Value of property outputFormat.
     */
    public java.lang.String getOutputFormat() {
        return outputFormat;
    }
    
    /** Setter for property outputFormat.
     * @param outputFormat New value of property outputFormat.
     */
    public void setOutputFormat(java.lang.String outputFormat) {
        this.outputFormat = outputFormat;
    }
    
    /** Getter for property requestorUserID.
     * @return Value of property requestorUserID.
     */
    public java.lang.String getRequestorUserID() {
        return requestorUserID;
    }
    
    /** Setter for property requestorUserID.
     * @param requestorUserID New value of property requestorUserID.
     */
    public void setRequestorUserID(java.lang.String requestorUserID) {
        this.requestorUserID = requestorUserID;
    }
    
    /** Getter for property requestedTime.
     * @return Value of property requestedTime.
     */
    public java.util.Date getRequestedTime() {
        return requestedTime;
    }
    
    /** Setter for property requestedTime.
     * @param requestedTime New value of property requestedTime.
     */
    public void setRequestedTime(java.util.Date requestedTime) {
        this.requestedTime = requestedTime;
    }
    
    /** Getter for property mailingList.
     * @return Value of property mailingList.
     */
    public boolean isMailingList() {
        return mailingList;
    }
    
    /** Setter for property mailingList.
     * @param mailingList New value of property mailingList.
     */
    public void setMailingList(boolean mailingList) {
        this.mailingList = mailingList;
    }
    
    /** Getter for property numOfMail.
     * @return Value of property numOfMail.
     */
    public int getNumOfMail() {
        return numOfMail;
    }
    
    /** Setter for property numOfMail.
     * @param numOfMail New value of property numOfMail.
     */
    public void setNumOfMail(int numOfMail) {
        this.numOfMail = numOfMail;
    }
    
    /** Getter for property reportMimeType.
     * @return Value of property reportMimeType.
     */
    public java.lang.String getReportMimeType() {
        return reportMimeType;
    }
    
    /** Setter for property reportMimeType.
     * @param reportMimeType New value of property reportMimeType.
     */
    public void setReportMimeType(java.lang.String reportMimeType) {
        this.reportMimeType = reportMimeType;
    }
    
}