package org.afscme.enterprise.reporting.specialized.web;

import org.apache.struts.action.ActionForm;
import org.afscme.enterprise.util.CollectionUtil;


/**
 * @struts:form name="rebateCheckFileForm"
 */
public class RebateCheckFileForm extends ActionForm {
    
    /** Holds value of property start check number */
    private String checkNumber;        
    
    /** Getter for property checkNumber.
     * @return Value of property checkNumber.
     *
     */
    public java.lang.String getCheckNumber() {
        return checkNumber;
    }
    
    /** Setter for property checkNumber.
     * @param checkNumber New value of property checkNumber.
     *
     */
    public void setCheckNumber(java.lang.String checkNumber) {
        this.checkNumber = checkNumber;
    }
    
}



