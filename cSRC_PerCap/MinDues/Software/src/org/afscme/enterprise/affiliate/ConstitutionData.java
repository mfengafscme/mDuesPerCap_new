package org.afscme.enterprise.affiliate;

import java.io.InputStream;
import java.sql.Timestamp;
import org.afscme.enterprise.common.RecordData;

/**
 * Affilaite constitution data
 */
public class ConstitutionData {
    
    private Integer affPk;
    private Boolean approved;
    private Boolean automaticDelegate;
    private Timestamp mostCurrentApprovalDate;
    private Timestamp affiliationAgreementDate;
    private Integer methodOfOfficerElectionCodePk;
    private Boolean constitutionalRegions;
    private Integer meetingFrequencyCodePk;
    private String documentName;
    private String documentType;
    private InputStream documentStream;
    private boolean documentUploaded;
    private RecordData recordData;
    
    public ConstitutionData() {
        this.affPk = null;
        this.approved = null;
        this.automaticDelegate = null;
        this.mostCurrentApprovalDate = null;
        this.affiliationAgreementDate = null;
        this.methodOfOfficerElectionCodePk = null;
        this.constitutionalRegions = null;
        this.meetingFrequencyCodePk = null;
        this.documentName = null;
        this.documentType = null;
        this.documentStream = null;
        this.documentUploaded = false;
        this.recordData = null;
    }
    
// General Methods...
    
    public String toString() {
        StringBuffer sb = new StringBuffer("ConstitutionData {");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", approved = ");
        sb.append(this.approved);
        sb.append(", automaticDelegate = ");
        sb.append(this.automaticDelegate);
        sb.append(", mostCurrentApprovalDate = ");
        sb.append(this.mostCurrentApprovalDate);
        sb.append(", affiliationAgreementDate = ");
        sb.append(this.affiliationAgreementDate);
        sb.append(", methodOfOfficerElectionCodePk = ");
        sb.append(this.methodOfOfficerElectionCodePk);
        sb.append(", constitutionalRegions = ");
        sb.append(this.constitutionalRegions);
        sb.append(", meetingFrequencyCodePk = ");
        sb.append(this.meetingFrequencyCodePk);
        sb.append(", documentName = ");
        sb.append(this.documentName);
        sb.append(", documentType = ");
        sb.append(this.documentType);
        sb.append(", documentStream = ");
        sb.append(this.documentStream);
        sb.append(", documentUploaded = ");
        sb.append(this.documentUploaded);
        sb.append(", recordData = ");
        sb.append(this.recordData);
        sb.append("}");
        return sb.toString().trim();
    }
    
// Getter and Setter Methods...
    
    /** Getter for property affiliationAgreementDate.
     * @return Value of property affiliationAgreementDate.
     *
     */
    public Timestamp getAffiliationAgreementDate() {
        return affiliationAgreementDate;
    }
    
    /** Setter for property affiliationAgreementDate.
     * @param affiliationAgreementDate New value of property affiliationAgreementDate.
     *
     */
    public void setAffiliationAgreementDate(Timestamp affiliationAgreementDate) {
        this.affiliationAgreementDate = affiliationAgreementDate;
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property approved.
     * @return Value of property approved.
     *
     */
    public Boolean getApproved() {
        return approved;
    }
    
    /** Setter for property approved.
     * @param approved New value of property approved.
     *
     */
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
    
    /** Getter for property automaticDelegate.
     * @return Value of property automaticDelegate.
     *
     */
    public Boolean getAutomaticDelegate() {
        return automaticDelegate;
    }
    
    /** Setter for property automaticDelegate.
     * @param automaticDelegate New value of property automaticDelegate.
     *
     */
    public void setAutomaticDelegate(Boolean automaticDelegate) {
        this.automaticDelegate = automaticDelegate;
    }
    
    /** Getter for property constitutionalRegions.
     * @return Value of property constitutionalRegions.
     *
     */
    public Boolean getConstitutionalRegions() {
        return constitutionalRegions;
    }
    
    /** Setter for property constitutionalRegions.
     * @param constitutionalRegions New value of property constitutionalRegions.
     *
     */
    public void setConstitutionalRegions(Boolean constitutionalRegions) {
        this.constitutionalRegions = constitutionalRegions;
    }
    
    /** Getter for property meetingFrequencyCodePk.
     * @return Value of property meetingFrequencyCodePk.
     *
     */
    public Integer getMeetingFrequencyCodePk() {
        return meetingFrequencyCodePk;
    }
    
    /** Setter for property meetingFrequencyCodePk.
     * @param meetingFrequencyCodePk New value of property meetingFrequencyCodePk.
     *
     */
    public void setMeetingFrequencyCodePk(Integer meetingFrequencyCodePk) {
        this.meetingFrequencyCodePk = meetingFrequencyCodePk;
    }
    
    /** Getter for property methodOfOfficerElectionCodePk.
     * @return Value of property methodOfOfficerElectionCodePk.
     *
     */
    public Integer getMethodOfOfficerElectionCodePk() {
        return methodOfOfficerElectionCodePk;
    }
    
    /** Setter for property methodOfOfficerElectionCodePk.
     * @param methodOfOfficerElectionCodePk New value of property methodOfOfficerElectionCodePk.
     *
     */
    public void setMethodOfOfficerElectionCodePk(Integer methodOfOfficerElectionCodePk) {
        this.methodOfOfficerElectionCodePk = methodOfOfficerElectionCodePk;
    }
    
    /** Getter for property mostCurrentApprovalDate.
     * @return Value of property mostCurrentApprovalDate.
     *
     */
    public Timestamp getMostCurrentApprovalDate() {
        return mostCurrentApprovalDate;
    }
    
    /** Setter for property mostCurrentApprovalDate.
     * @param mostCurrentApprovalDate New value of property mostCurrentApprovalDate.
     *
     */
    public void setMostCurrentApprovalDate(Timestamp mostCurrentApprovalDate) {
        this.mostCurrentApprovalDate = mostCurrentApprovalDate;
    }
    
    /** Getter for property recordData.
     * @return Value of property recordData.
     *
     */
    public RecordData getRecordData() {
        return recordData;
    }
    
    /** Setter for property recordData.
     * @param recordData New value of property recordData.
     *
     */
    public void setRecordData(RecordData recordData) {
        this.recordData = recordData;
    }
    
    /** Getter for property documentName.
     * @return Value of property documentName.
     *
     */
    public String getDocumentName() {
        return documentName;
    }
    
    /** Setter for property documentName.
     * @param documentName New value of property documentName.
     *
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
    
    /** Getter for property documentType.
     * @return Value of property documentType.
     *
     */
    public String getDocumentType() {
        return documentType;
    }
    
    /** Setter for property documentType.
     * @param documentType New value of property documentType.
     *
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    
    /** Getter for property documentStream.
     * @return Value of property documentStream.
     *
     */
    public InputStream getDocumentStream() {
        return documentStream;
    }
    
    /** Setter for property documentStream.
     * @param documentStream New value of property documentStream.
     *
     */
    public void setDocumentStream(InputStream documentStream) {
        this.documentStream = documentStream;
    }
    
    /** Getter for property documentUploaded.
     * @return Value of property documentUploaded.
     *
     */
    public boolean isDocumentUploaded() {
        return documentUploaded;
    }
    
    /** Setter for property documentUploaded.
     * @param documentUploaded New value of property documentUploaded.
     *
     */
    public void setDocumentUploaded(boolean documentUploaded) {
        this.documentUploaded = documentUploaded;
    }
    
}
