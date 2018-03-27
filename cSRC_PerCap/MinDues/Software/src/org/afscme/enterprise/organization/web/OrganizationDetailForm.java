package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrganizationData;
import org.afscme.enterprise.util.TextUtil;


/**
 * Represents the form for the organization detail
 *
 * @struts:form name="organizationDetailForm"
 */
public class OrganizationDetailForm extends ActionForm {

    private Integer m_orgPK;
    private String m_orgName;
    private Integer m_orgType;
    private String m_orgWebSite;    
    private boolean m_markedForDeletion;

    // for PAC
    private String m_orgEmailDomain;

    /** The organization's Primary Location */
    protected LocationData m_orgPrimaryLocation;
    
    
    /** Creates a new instance of OrganizationDetailForm */
    public OrganizationDetailForm() {
        m_orgName=null;
    }

    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("orgPK: " + m_orgPK);
        buf.append(", orgName: " + m_orgName);
        buf.append(", orgType: " + m_orgType);
        buf.append(", orgWebSite: " + m_orgWebSite);
        buf.append(", markedForDeletion: " + m_markedForDeletion);        
        buf.append(", orgEmailDomain: " + m_orgEmailDomain);   
        return buf.toString()+"]";
    }

    /**
     * Returns true if this form is being used for an add operation (as opposed to edit)
     */
    public boolean isAdd() {
        return m_orgPK == null || m_orgPK.intValue() == 0;
    }
        
    /**
     * getOrganizationData method to copy all the form data fields 
     * to the organization object for processing 
     */
    public OrganizationData getOrganizationData() {
        
        OrganizationData data = new OrganizationData();
        RecordData rData = new RecordData();

        data.setOrgPK(m_orgPK);
        data.setOrgNm(m_orgName);
        data.setOrgType(m_orgType);
        if (!TextUtil.isEmpty(m_orgWebSite))
            data.setOrgWebURL(m_orgWebSite);
        data.setMarkedForDeletion(new Boolean(m_markedForDeletion));
        if (!TextUtil.isEmpty(m_orgEmailDomain))
            data.setOrgEmailDomain(m_orgEmailDomain);
        
//        data.setRecordData(rData);

        return data;
    }

    /**
     * setOrganizationData method to copy all the organization object data 
     * to the form data fields for screen display
     */
    public void setOrganizationData(OrganizationData data) {
        
        m_orgPK = data.getOrgPK();
        m_orgName = data.getOrgNm();
        m_orgType = data.getOrgType();
        m_orgWebSite = data.getOrgWebURL();
        m_markedForDeletion = data.getMarkedForDeletion().booleanValue();
        setOrgPrimaryLocation(data.getPrimaryLocationData());
    }
    

    /** Getter for property m_markedForDeletion.
     * @return Value of property m_markedForDeletion.
     *
     */
    public boolean getMarkedForDeletion() {
        return m_markedForDeletion;
    }
    
    /** Setter for property m_markedForDeletion.
     * @param markedForDeletion New value of property m_markedForDeletion.
     *
     */
    public void setMarkedForDeletion(boolean markedForDeletion) {
        this.m_markedForDeletion = markedForDeletion;
    }
    
    /** Getter for property m_orgEmailDomain.
     * @return Value of property m_orgEmailDomain.
     *
     */
    public java.lang.String getOrgEmailDomain() {
        return m_orgEmailDomain;
    }
    
    /** Setter for property m_orgEmailDomain.
     * @param orgEmailDomain New value of property m_orgEmailDomain.
     *
     */
    public void setOrgEmailDomain(java.lang.String orgEmailDomain) {
        this.m_orgEmailDomain = orgEmailDomain;
    }
    
    /** Getter for property m_orgName.
     * @return Value of property m_orgName.
     *
     */
    public java.lang.String getOrgName() {
        return m_orgName;
    }
    
    /** Setter for property m_orgName.
     * @param orgName New value of property m_orgName.
     *
     */
    public void setOrgName(java.lang.String orgName) {
        this.m_orgName = orgName;
    }
    
    /** Getter for property m_orgPK.
     * @return Value of property m_orgPK.
     *
     */
    public java.lang.Integer getOrgPK() {
        return m_orgPK;
    }
    
    /** Setter for property m_orgPK.
     * @param orgPK New value of property m_orgPK.
     *
     */
    public void setOrgPK(java.lang.Integer orgPK) {
        this.m_orgPK = orgPK;
    }
    
    /** Getter for property m_orgType.
     * @return Value of property m_orgType.
     *
     */
    public java.lang.Integer getOrgType() {
        return m_orgType;
    }
    
    /** Setter for property m_orgType.
     * @param orgType New value of property m_orgType.
     *
     */
    public void setOrgType(java.lang.Integer orgType) {
        this.m_orgType = orgType;
    }
    
    /** Getter for property m_orgWebSite.
     * @return Value of property m_orgWebSite.
     *
     */
    public java.lang.String getOrgWebSite() {
        return m_orgWebSite;
    }
    
    /** Setter for property m_orgWebSite.
     * @param orgWebSite New value of property m_orgWebSite.
     *
     */
    public void setOrgWebSite(java.lang.String orgWebSite) {
        this.m_orgWebSite = orgWebSite;
    }
    
    /** Getter for property m_orgPrimaryLocation.
     * @return Value of property m_orgPrimaryLocation.
     *
     */
    public LocationData getOrgPrimaryLocation() {
        return m_orgPrimaryLocation;
    }
    
    /** Setter for property m_orgPrimaryLocation.
     * @param orgPrimaryLocation New value of property m_orgPrimaryLocation.
     *
     */
    public void setOrgPrimaryLocation(LocationData orgPrimaryLocation) {
        this.m_orgPrimaryLocation = orgPrimaryLocation;
    }
        
    
    /**
     * validation method for this form
     */    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	if (m_orgName == null) {
            //new, ignore
            return null;
	}
        
        ActionErrors errors = new ActionErrors();
        if (m_orgName.length() == 0) {
            errors.add("orgName", new ActionError("error.field.required.generic", "Organization Name"));
        }
        if ((m_orgType == null) || (m_orgType.intValue() == 0)) {
            errors.add("orgType", new ActionError("error.field.required.generic", "Organization Type"));
        }
        
        return errors;
    }
   
}
