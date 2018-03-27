package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;

// Java imports
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.affiliate.ConstitutionData;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;

/**
 * @struts:form name="constitutionForm"
 */
public class ConstitutionForm extends ActionForm {

    private static Logger logger =  Logger.getLogger(ConstitutionForm.class);       
    
    private Integer affPk;
    private boolean approvedConstitution;
    private boolean automaticDelegateProvision;
    private String mostCurrentApprovalDate;
    private String affiliationAgreementDate;
    private Integer methodOfOfficerElection;
    private boolean constitutionalRegions;
    private Integer meetingFrequency;
    private FormFile constitutionDocument;
    private boolean documentUploaded;
    
    /** Creates a new instance of ConstitutionForm */
    public ConstitutionForm() {
        this.init();
    }
    
// General Methods...
    
    protected void init() {
        this.approvedConstitution = false;
        this.automaticDelegateProvision = false;
        this.mostCurrentApprovalDate = null;
        this.affiliationAgreementDate = null;
        this.methodOfOfficerElection = null;
        this.constitutionalRegions = false;
        this.meetingFrequency = null;
        this.constitutionDocument = null;
        this.documentUploaded = false;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("ConstitutionForm {");
        sb.append("approvedConstitution = ");
        sb.append(this.approvedConstitution);
        sb.append(", automaticDelegateProvision = ");
        sb.append(this.automaticDelegateProvision);
        sb.append(", mostCurrentApprovalDate = ");
        sb.append(this.mostCurrentApprovalDate);
        sb.append(", affiliationAgreementDate = ");
        sb.append(this.affiliationAgreementDate);
        sb.append(", methodOfOfficerElection = ");
        sb.append(this.methodOfOfficerElection);
        sb.append(", constitutionalRegions = ");
        sb.append(this.constitutionalRegions);
        sb.append(", meetingFrequency = ");
        sb.append(this.meetingFrequency);
        sb.append(", constitutionDocument = ");
        sb.append(this.constitutionDocument);
        sb.append(", documentUploaded = ");
        sb.append(this.documentUploaded);
        sb.append("}");
        return sb.toString().trim();
    }
    
    public boolean equals(ConstitutionForm form) {
        if (form == null) {
            return false;
        }
        return TextUtil.equals(this.affiliationAgreementDate, form.affiliationAgreementDate) && 
            TextUtil.equals(this.constitutionDocument, form.constitutionDocument) && 
            TextUtil.equals(this.meetingFrequency, form.meetingFrequency) &&
            TextUtil.equals(this.methodOfOfficerElection, form.methodOfOfficerElection) &&
            TextUtil.equals(this.mostCurrentApprovalDate, form.mostCurrentApprovalDate) &&
            this.approvedConstitution == form.approvedConstitution &&
            this.automaticDelegateProvision == form.automaticDelegateProvision &&
            this.constitutionalRegions == form.constitutionalRegions && 
            this.documentUploaded == form.documentUploaded
        ;
    }
    
    public boolean isEmptyDocument() {
        return isEmpty(this.constitutionDocument);
    }
    
    private boolean isEmpty(FormFile ff) {
        try {
            return  (ff == null) || 
                    TextUtil.isEmptyOrSpaces(ff.getFileName()) ||
                    TextUtil.isEmptyOrSpaces(ff.getContentType()) ||
                    (ff.getInputStream() == null) || 
                    (ff.getFileSize() < 1) 
            ;
        } catch (IOException e) {
            return true;
        } 
    }

// Struts Methods...
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.init();
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (this.isApprovedConstitution() && TextUtil.isEmptyOrSpaces(this.getMostCurrentApprovalDate())) {
            errors.add("mostCurrentApprovalDate", new ActionError("error.constitution.mostCurrentApprovalDate.required"));
        }
        if (!TextUtil.isEmptyOrSpaces(this.getMostCurrentApprovalDate()) && DateUtil.getTimestamp(this.getMostCurrentApprovalDate()) == null) {
            errors.add("mostCurrentApprovalDate", new ActionError("error.field.criterion.invalidDate"));
        }
        if (!TextUtil.isEmptyOrSpaces(this.getAffiliationAgreementDate()) && DateUtil.getTimestamp(this.getAffiliationAgreementDate()) == null) {
            errors.add("affiliationAgreementDate", new ActionError("error.field.criterion.invalidDate"));
        }        
        if (this.isDocumentUploaded()) {
            if (!this.getConstitutionDocument().getContentType().equals("application/pdf")) {
                errors.add("constitutionDocument",new ActionError("error.constitution.fileformat"));   
            }
            logger.debug("    ***** Document name: " + this.getConstitutionDocument().getFileName());
            logger.debug("    ***** Document type: " + this.getConstitutionDocument().getContentType());
            logger.debug("    ***** Document size: " + this.getConstitutionDocument().getFileSize());
        } else {
            logger.debug("    ***** Document was NULL.");
        }
        return errors;
    }
    
// Getter and Setter Methods...
    
    /** Gets a ConstitutionData object using the fields in the form.
     * @return the ConstitutionData representing this form.
     */
    public ConstitutionData getConstitutionData() {
        ConstitutionData data = new ConstitutionData();
        data.setAffPk(this.getAffPk());
        data.setAffiliationAgreementDate(DateUtil.getTimestamp(this.getAffiliationAgreementDate()));
        data.setApproved(new Boolean(this.isApprovedConstitution()));
        data.setAutomaticDelegate(new Boolean(this.isAutomaticDelegateProvision()));
        data.setConstitutionalRegions(new Boolean(this.isConstitutionalRegions()));
        data.setMeetingFrequencyCodePk(this.getMeetingFrequency());
        data.setMethodOfOfficerElectionCodePk(this.getMethodOfOfficerElection());
        data.setMostCurrentApprovalDate(DateUtil.getTimestamp(this.getMostCurrentApprovalDate()));
        if (this.getConstitutionDocument() != null && this.getConstitutionDocument().getFileName() != null) {
            try {
                data.setDocumentStream(this.getConstitutionDocument().getInputStream());
                data.setDocumentName(this.getConstitutionDocument().getFileName());
                data.setDocumentType(this.getConstitutionDocument().getContentType());
            } catch (IOException ioe) {
                /** @TODO: Determine how to handle. Probably just want to ignore. */
            }
        }
        return data;
    }
    
    /** Sets all of the fields in this form with the values in the param.
     * @param data The ConstitutionData object.
     */
    public void setConstitutionData(ConstitutionData data) {
        this.setAffPk(data.getAffPk());
        this.setAffiliationAgreementDate(DateUtil.getSimpleDateString(data.getAffiliationAgreementDate()));
        if (data.getApproved() == null) {
            this.setApprovedConstitution(false);
        } else {
            this.setApprovedConstitution(data.getApproved().booleanValue());
        }
        if (data.getAutomaticDelegate() == null) {
            this.setAutomaticDelegateProvision(false);
        } else {
            this.setAutomaticDelegateProvision(data.getAutomaticDelegate().booleanValue());
        }
        if (data.getConstitutionalRegions() == null) {
            this.setConstitutionalRegions(false);
        } else {
            this.setConstitutionalRegions(data.getConstitutionalRegions().booleanValue());
        }
        this.setDocumentUploaded(data.isDocumentUploaded());
        this.setMeetingFrequency(data.getMeetingFrequencyCodePk());
        this.setMethodOfOfficerElection(data.getMethodOfOfficerElectionCodePk());
        this.setMostCurrentApprovalDate(DateUtil.getSimpleDateString(data.getMostCurrentApprovalDate()));
    }
    
    /** Getter for property affiliationAgreementDate.
     * @return Value of property affiliationAgreementDate.
     *
     */
    public String getAffiliationAgreementDate() {
        return affiliationAgreementDate;
    }
    
    /** Setter for property affiliationAgreementDate.
     * @param affiliationAgreementDate New value of property affiliationAgreementDate.
     *
     */
    public void setAffiliationAgreementDate(String affiliationAgreementDate) {
        if (TextUtil.isEmptyOrSpaces(affiliationAgreementDate)) {
            this.affiliationAgreementDate = null;
        } else {
            this.affiliationAgreementDate = affiliationAgreementDate;
        }
    }
    
    /** Getter for property approvedConstitution.
     * @return Value of property approvedConstitution.
     *
     */
    public boolean isApprovedConstitution() {
        return approvedConstitution;
    }
    
    /** Setter for property approvedConstitution.
     * @param approvedConstitution New value of property approvedConstitution.
     *
     */
    public void setApprovedConstitution(boolean approvedConstitution) {
        this.approvedConstitution = approvedConstitution;
    }
    
    /** Getter for property automaticDelegateProvision.
     * @return Value of property automaticDelegateProvision.
     *
     */
    public boolean isAutomaticDelegateProvision() {
        return automaticDelegateProvision;
    }
    
    /** Setter for property automaticDelegateProvision.
     * @param automaticDelegateProvision New value of property automaticDelegateProvision.
     *
     */
    public void setAutomaticDelegateProvision(boolean automaticDelegateProvision) {
        this.automaticDelegateProvision = automaticDelegateProvision;
    }
    
    /** Getter for property constitutionalRegions.
     * @return Value of property constitutionalRegions.
     *
     */
    public boolean isConstitutionalRegions() {
        return constitutionalRegions;
    }
    
    /** Setter for property constitutionalRegions.
     * @param constitutionalRegions New value of property constitutionalRegions.
     *
     */
    public void setConstitutionalRegions(boolean constitutionalRegions) {
        this.constitutionalRegions = constitutionalRegions;
    }
    
    /** Getter for property constitutionDocument.
     * @return Value of property constitutionDocument.
     *
     */
    public FormFile getConstitutionDocument() {
        return constitutionDocument;
    }
    
    /** Setter for property constitutionDocument.
     *
     * <p><b>Note:</b> This method should only be called by Struts.
     *
     * @param constitutionDocument New value of property constitutionDocument.
     *
     */
    public void setConstitutionDocument(FormFile constitutionDocument) {
        if (isEmpty(constitutionDocument)) {
            this.constitutionDocument = null;
            /* Do not set documentUploaded to false. it may have been set to true by 
             * the sql, without initializing the constitutionDocument attribute.
             */
        } else {
            this.constitutionDocument = constitutionDocument;
            this.setDocumentUploaded(true); // If we have a document, set this to true.
        }
    }
    
    /** Getter for property meetingFrequency.
     * @return Value of property meetingFrequency.
     *
     */
    public Integer getMeetingFrequency() {
        return meetingFrequency;
    }
    
    /** Setter for property meetingFrequency.
     * @param meetingFrequency New value of property meetingFrequency.
     *
     */
    public void setMeetingFrequency(Integer meetingFrequency) {
        if (meetingFrequency == null || meetingFrequency.intValue() < 1) {
            this.meetingFrequency = null;
        } else {
            this.meetingFrequency = meetingFrequency;
        }
    }
    
    /** Getter for property methodOfOfficerElection.
     * @return Value of property methodOfOfficerElection.
     *
     */
    public Integer getMethodOfOfficerElection() {
        return methodOfOfficerElection;
    }
    
    /** Setter for property methodOfOfficerElection.
     * @param methodOfOfficerElection New value of property methodOfOfficerElection.
     *
     */
    public void setMethodOfOfficerElection(Integer methodOfOfficerElection) {
        if (methodOfOfficerElection == null || methodOfOfficerElection.intValue() < 1) {
            this.methodOfOfficerElection = null;
        } else {
            this.methodOfOfficerElection = methodOfOfficerElection;
        }
    }
    
    /** Getter for property mostCurrentApprovalDate.
     * @return Value of property mostCurrentApprovalDate.
     *
     */
    public String getMostCurrentApprovalDate() {
        return mostCurrentApprovalDate;
    }
    
    /** Setter for property mostCurrentApprovalDate.
     * @param mostCurrentApprovalDate New value of property mostCurrentApprovalDate.
     *
     */
    public void setMostCurrentApprovalDate(String mostCurrentApprovalDate) {
        if (TextUtil.isEmptyOrSpaces(mostCurrentApprovalDate)) {
            this.mostCurrentApprovalDate = null;
        } else {
            this.mostCurrentApprovalDate = mostCurrentApprovalDate;
        }
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
        if (affPk == null || affPk.intValue() < 1) {
            this.affPk = null;
        } else {
            this.affPk = affPk;
        }
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
