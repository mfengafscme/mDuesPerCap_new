package org.afscme.enterprise.reporting.specialized.web;

import org.apache.struts.action.ActionForm;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;


/**
 * @struts:form name="prbApplicationFileForm"
 */
public class PRBApplicationFileForm extends ActionForm {
    
    /** Holds value of property appMailedDate. */
    private String appMailedDate;        
    
    /** Getter for property appMailedDate.
     * @return Value of property appMailedDate.
     *
     */
    public java.lang.String getAppMailedDate() {
        return appMailedDate;
    }
    
    /** Setter for property appMailedDate.
     * @param appMailedDate New value of property appMailedDate.
     *
     */
    public void setAppMailedDate(java.lang.String appMailedDate) {
        this.appMailedDate = appMailedDate;
    }    
}



