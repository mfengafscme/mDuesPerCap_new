package org.afscme.enterprise.returnedmail.web;

import java.util.LinkedList;
import java.util.Map;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.returnedmail.ReturnedMailSummary;

/**
 * Form data from the Process Returned Mail page.
 *
 * @struts:form name="processReturnedMailForm"
 */
public class ProcessReturnedMailForm extends ActionForm {
    private String m_addressIds;
    private String m_search;
    private String m_submit;
    private ReturnedMailSummary summary;
    private LinkedList personExceptionList;
    private LinkedList personSuccessfulList;
    private LinkedList orgExceptionList;
    private LinkedList orgSuccessfulList;
    private java.util.Map invalidAddressList;
    private boolean exceptionFlag=false;
    private boolean successfulFlag=false;
    private ActionErrors errors=null;
    
    public ProcessReturnedMailForm() {
        summary = new ReturnedMailSummary();
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        //if form data hasn't been set yet, don't validate
        if (isSubmitButton()) {
            errors = new ActionErrors();        
            if (TextUtil.isEmpty(getAddressIds())) {
                errors.add("addressIds", new ActionError("error.field.required.addressIds"));
                clearActions();
            }
            return errors;
        }
        return null;
    }
    
    
    /** Getter for property m_addressIds.
     * @return Value of property m_addressIds.
     *
     */
    public java.lang.String getAddressIds() {
        return m_addressIds;
    }
    
    /** Setter for property m_addressIds.
     * @param m_addressIds New value of property m_addressIds.
     *
     */
    public void setAddressIds(java.lang.String addressIds) {
        this.m_addressIds = addressIds;
    }
    
    public boolean isSearchButton() {
        return m_search != null;
    }
    
    /** Getter for property m_search.
     * @return Value of property m_search.
     *
     */
    public java.lang.String getSearch() {
        return m_search;
    }
    
    /** Setter for property m_search.
     * @param m_search New value of property m_search.
     *
     */
    public void setSearch(java.lang.String m_search) {
        this.m_search = m_search;
    }
    
    public boolean isSubmitButton() {
        return m_submit != null;
    }
    
    /** Getter for property m_submit.
     * @return Value of property m_submit.
     *
     */
    public java.lang.String getSubmit() {
        return m_submit;
    }
    
    /** Setter for property m_submit.
     * @param m_submit New value of property m_submit.
     *
     */
    public void setSubmit(java.lang.String m_submit) {
        this.m_submit = m_submit;
    }
    
    /** Getter for property summary.
     * @return Value of property summary.
     *
     */
    public org.afscme.enterprise.returnedmail.ReturnedMailSummary getSummary() {
        return summary;
    }
    
    /** Setter for property summary.
     * @param summary New value of property summary.
     *
     */
    public void setSummary(org.afscme.enterprise.returnedmail.ReturnedMailSummary summary) {
        this.summary = summary;
    }
    
    /** Getter for property personExceptionList.
     * @return Value of property personExceptionList.
     *
     */
    public java.util.LinkedList getPersonExceptionList() {
        return getSummary().getPersonExceptionList();
    }

    /** Getter for property personSuccessfulList.
     * @return Value of property personSuccessfulList.
     *
     */
    public java.util.LinkedList getPersonSuccessfulList() {
        return getSummary().getPersonSuccessfulList();
    }

    /** Getter for property orgExceptionList.
     * @return Value of property orgExceptionList.
     *
     */
    public java.util.LinkedList getOrgExceptionList() {
        return getSummary().getOrganizationExceptionList();
    }

    /** Getter for property orgSuccessfulList.
     * @return Value of property orgSuccessfulList.
     *
     */
    public java.util.LinkedList getOrgSuccessfulList() {
        return getSummary().getOrganizationSuccessfulList();
    }

    /** Getter for property invalidAddressList.
     * @return Value of property invalidAddressList.
     *
     */
    public java.util.Map getInvalidAddressList() {
        return getSummary().getInvalidAddressList();
    }
    
    /** Getter for property exceptionFlag.
     * @return Value of property exceptionFlag.
     *
     */
    public boolean getExceptionFlag() {
        return (getPersonExceptionList().size()>0 || 
                getOrgExceptionList().size()>0 ||
                getInvalidAddressList().size()>0) ? true : false;
    }
        
    /** Getter for property successfulFlag.
     * @return Value of property successfulFlag.
     *
     */
    public boolean getSuccessfulFlag() {
        return (getPersonSuccessfulList().size()>0 || 
                getOrgSuccessfulList().size()>0) ? true : false;        
    }        

    /** Clear form fields and actions from session
     *
     */
    public void clearAll() {
        m_addressIds = (m_addressIds == null) ? m_addressIds : null;
        m_search = (m_search == null) ? m_search : null;
        m_submit = (m_submit == null) ? m_submit : null;
        errors = (errors == null) ? errors : null;
    }

    /** Clear form actions from session
     *
     */
    public void clearActions() {
        m_search = (m_search == null) ? m_search : null;
        m_submit = (m_submit == null) ? m_submit : null;
    }
        
    public String toString() {
        return "Address Ids: " + m_addressIds + ", Request Basic Search: " + m_search + ",Summary: " + summary;
    }    
}
