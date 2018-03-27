package org.afscme.enterprise.affiliate.web;

import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.affiliate.MRData;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * @struts:form name="membershipReportingInformationForm"
 */
public class MembershipReportingInformationForm extends ActionForm {
    
    private MRData mrData;
    private Integer newStatus;
    private Character type;
    
    
    private final static Integer[] VALID_COUNCIL_STATUS = {
                AffiliateStatus.AC, AffiliateStatus.C, AffiliateStatus.D, 
                AffiliateStatus.N, AffiliateStatus.PC, AffiliateStatus.PD, 
                AffiliateStatus.RA, AffiliateStatus.UA
    };
    
    private final static Integer[] VALID_RETIREECHAPTER_STATUS = {
                AffiliateStatus.C, AffiliateStatus.D, AffiliateStatus.N, 
                AffiliateStatus.PC, AffiliateStatus.PD, AffiliateStatus.RA, 
                AffiliateStatus.UA
    };
    
    private final static Integer[] VALID_SUBCHAPTER_STATUS = {
                AffiliateStatus.C, AffiliateStatus.D, AffiliateStatus.M, 
                AffiliateStatus.N, AffiliateStatus.PC, AffiliateStatus.PD, 
                AffiliateStatus.RA, AffiliateStatus.UA
    };
    
    private final static Integer[] VALID_LOCAL_STATUS = {
                AffiliateStatus.C, AffiliateStatus.D, AffiliateStatus.M, 
                AffiliateStatus.N, AffiliateStatus.PC, AffiliateStatus.PD, 
                AffiliateStatus.RA, AffiliateStatus.UA
    };
    
    private final static Integer[] VALID_SUBLOCAL_STATUS = {
                AffiliateStatus.CU, AffiliateStatus.DU
    };
    
    
    public MembershipReportingInformationForm() {
        mrData = new MRData();
    }
    
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();        
        if (mrData.getInformationSource() != null && mrData.getInformationSource().intValue() == 0)
            mrData.setInformationSource(null);
       
        if (getNewStatus() != null) {          
            if (!TextUtil.isEmptyOrSpaces(getType())) {
                switch (getType().charValue()) {
                    case 'C':
                        if (!CollectionUtil.contains(VALID_COUNCIL_STATUS, getNewStatus())) {
                            errors.add("newStatus", new ActionError("error.affiliate.invalidStatus", "Council"));
                        }
                        break;
                    case 'R':
                        if (!CollectionUtil.contains(VALID_RETIREECHAPTER_STATUS, getNewStatus())) {
                            errors.add("newStatus", new ActionError("error.affiliate.invalidStatus", "Retiree Chapter"));
                        }
                        break;
                    case 'L':
                        if (!CollectionUtil.contains(VALID_LOCAL_STATUS, getNewStatus())) {
                            errors.add("newStatus", new ActionError("error.affiliate.invalidStatus", "Local"));
                        }
                        break;
                    case 'S':
                        if (!CollectionUtil.contains(VALID_SUBCHAPTER_STATUS, getNewStatus())) {
                            errors.add("newStatus", new ActionError("error.affiliate.invalidStatus", "Sub Chapter"));
                        }
                        break;
                    case 'U':
                        if (!CollectionUtil.contains(VALID_SUBLOCAL_STATUS, getNewStatus())) {
                            errors.add("newStatus", new ActionError("error.affiliate.invalidStatus", "Sub Local"));
                        }
                        break;
                    default:
                        // should never happen...
                }
            }        
        }
        return errors;
    }
               
    
    /** Getter for property mrData.
     * @return Value of property mrData.
     *
     */
    public MRData getMrData() {
        return mrData;
    }
    
    /** Setter for property mrData.
     * @param mrData New value of property mrData.
     *
     */
    public void setMrData(MRData mrData) {
        this.mrData = mrData;
    }
    
    /** Getter for property newStatus.
     * @return Value of property newStatus.
     *
     */
    public java.lang.Integer getNewStatus() {
        return newStatus;
    }
    
    /** Setter for property newStatus.
     * @param newStatus New value of property newStatus.
     *
     */
    public void setNewStatus(java.lang.Integer newStatus) {
        this.newStatus = newStatus;
    }
    
    
    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public java.lang.Character getType() {
        return type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(java.lang.Character type) {
        this.type = type;
    }    
}
