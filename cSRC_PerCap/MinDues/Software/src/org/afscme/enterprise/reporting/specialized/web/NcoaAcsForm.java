/** Class Name  : NcoaAcs.java
    Date Written: 20031030
    Author      : Kyung A. Callahan
    Description :
    Maintenance : 
*/
package org.afscme.enterprise.reporting.specialized.web;

import org.afscme.enterprise.common.web.AffiliateSearchForm;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:form name="ncoaAcsForm"
 */
public class NcoaAcsForm extends AffiliateSearchForm
{
    
    /** Holds value of property reportType. */
    private String reportType;
    
    /** Getter for property reportType.
     * @return Value of property reportType.
     *
     */
    public String getReportType() {
        return this.reportType;
    }
    
    /** Setter for property reportType.
     * @param firstName New value of property reportType.
     *
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    
}



