package org.afscme.enterprise.mailinglists.web;

import java.util.Map;
import java.util.List;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;

/**
 * Form data from the Mailing Lists Address Association page.
 *
 * @struts:form name="mailingListsAddressAssociationForm"
 */
public class MailingListsAddressAssociationForm extends ActionForm {
    private Integer pk;
    private Integer currentAddressPk;
    private List addressList;
    protected Integer mailingListPk;
    protected String header;
    
    public MailingListsAddressAssociationForm() {
        currentAddressPk = null;
        addressList = null;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
    
    public String toString() {
        return
        "Pk=" + pk + ", " +
        "currentAddressPk=" + currentAddressPk + ", " +
        "mailingListPk=" + mailingListPk + ", " +
        "addressList=" + addressList;
    }        
    
    /** Getter for property currentAddressPk.
     * @return Value of property currentAddressPk.
     *
     */
    public java.lang.Integer getCurrentAddressPk() {
        return currentAddressPk;
    }
    
    /** Setter for property currentAddressPk.
     * @param currentAddressPk New value of property currentAddressPk.
     *
     */
    public void setCurrentAddressPk(java.lang.Integer currentAddressPk) {
        this.currentAddressPk = currentAddressPk;
    }
    
    /** Getter for property addressList.
     * @return Value of property addressList.
     *
     */
    public java.util.List getAddressList() {
        return addressList;
    }
    
    /** Setter for property addressList.
     * @param addressList New value of property addressList.
     *
     */
    public void setAddressList(java.util.List addressList) {
        this.addressList = addressList;
    }
    
    /** Getter for property pk.
     * @return Value of property pk.
     *
     */
    public java.lang.Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     *
     */
    public void setPk(java.lang.Integer pk) {
        this.pk = pk;
    }
       
    /** Getter for property mailingListPk.
     * @return Value of property mailingListPk.
     *
     */
    public java.lang.Integer getMailingListPk() {
        return mailingListPk;
    }
    
    /** Setter for property mailingListPk.
     * @param mailingListPk New value of property mailingListPk.
     *
     */
    public void setMailingListPk(java.lang.Integer mailingListPk) {
        this.mailingListPk = mailingListPk;
    }    
    
    /** Getter for property header.
     * @return Value of property header.
     *
     */
    public java.lang.String getHeader() {
        return header;
    }
    
    /** Setter for property header.
     * @param header New value of property header.
     *
     */
    public void setHeader(java.lang.String header) {
        this.header = header;
    }
    
}
