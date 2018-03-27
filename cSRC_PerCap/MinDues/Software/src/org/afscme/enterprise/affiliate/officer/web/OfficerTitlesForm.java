package org.afscme.enterprise.affiliate.officer.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.officer.OfficeData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.common.web.SearchForm;

/**
 * @struts:form name="officerTitlesForm"
 */
public class OfficerTitlesForm extends SearchForm {

    protected Boolean  approvedConstitution;
    protected Boolean  autoDelegateProvision;
    protected Integer  affiliateTitlePk;
    protected Integer  subAffiliateTitlePk;
      
    public SortData getSortData() {
        SortData result = new SortData();
        result.setPage(page);
        result.setDirection(order);
        result.setSortField(OfficeData.sortStringToCode(sortBy));
        
        return result;   
    }  
       
// Struts Methods...
               
    /** Getter for property approvedConstitution.
     * @return Value of property approvedConstitution.
     *
     */
    public java.lang.Boolean getApprovedConstitution() {
        return approvedConstitution;
    }
    
    /** Setter for property approvedConstitution.
     * @param approvedConstitution New value of property approvedConstitution.
     *
     */
    public void setApprovedConstitution(java.lang.Boolean approvedConstitution) {
        this.approvedConstitution = approvedConstitution;
    }
    
    /** Getter for property autoDelegateProvision.
     * @return Value of property autoDelegateProvision.
     *
     */
    public java.lang.Boolean getAutoDelegateProvision() {
        return autoDelegateProvision;
    }
    
    /** Setter for property autoDelegateProvision.
     * @param autoDelegateProvision New value of property autoDelegateProvision.
     *
     */
    public void setAutoDelegateProvision(java.lang.Boolean autoDelegateProvision) {
        this.autoDelegateProvision = autoDelegateProvision;
    }  
    
    /** Getter for property affiliateTitlePk.
     * @return Value of property affiliateTitlePk.
     *
     */
    public java.lang.Integer getAffiliateTitlePk() {
        return affiliateTitlePk;
    }
    
    /** Setter for property affiliateTitlePk.
     * @param affiliateTitlePk New value of property affiliateTitlePk.
     *
     */
    public void setAffiliateTitlePk(java.lang.Integer affiliateTitlePk) {
        this.affiliateTitlePk = affiliateTitlePk;
    }
    
    /** Getter for property subAffiliateTitlePk.
     * @return Value of property subAffiliateTitlePk.
     *
     */
    public java.lang.Integer getSubAffiliateTitlePk() {
        return subAffiliateTitlePk;
    }
    
    /** Setter for property subAffiliateTitlePk.
     * @param subAffiliateTitlePk New value of property subAffiliateTitlePk.
     *
     */
    public void setSubAffiliateTitlePk(java.lang.Integer subAffiliateTitlePk) {
        this.subAffiliateTitlePk = subAffiliateTitlePk;
    }
    
}
