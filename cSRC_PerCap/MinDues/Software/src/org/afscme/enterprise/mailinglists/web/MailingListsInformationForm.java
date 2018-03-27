package org.afscme.enterprise.mailinglists.web;

import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.util.TextUtil;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.mailinglists.MailingListData;


/** Holds the data on the Mailing Lists Information screen
 * @struts:form name="mailingListsInformationForm"
 */
public class MailingListsInformationForm extends ActionForm {
    
    protected Integer pk;
    protected List mailingLists;
    protected String add;
    protected String originate;
    protected boolean editable=false;
    protected boolean addable=false;
    protected boolean MLBP=false;
    protected String header;
    // HLM: Fix defect #103
    protected boolean actingAsAffiliate;
    
    public MailingListsInformationForm() {
        this.pk = null;
        this.mailingLists = null;
        this.originate = null;
        this.add = null;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
    
    /** Setter for property MLBP.
     * @param MLBP New value of property MLBP.
     *
     */
    public void setMLBP(boolean MLBP) {
        this.MLBP = MLBP;
    }
    
    
    /** Mailing List By Person.
     * @return boolean.
     */
    public boolean isMLBP() {
        return MLBP;
    }
    
    /** Edit action is only available under the Affiliate or Organization flow
     *  iff one of the Mailing List Codes is a Bulk Type list.
     * @return boolean.
     */
    public boolean isEditable() {
        if (!isMLBP()) {
            return getEditable();
        }
        return false;
    }
    
    /** Entity can only be added to a Mailing List iff it has at least
     * one Address/Location to which it is associated.
     * @return boolean.
     */
    public boolean isAddable() {
        return getAddable();
    }
    
    /** Getter for property mailingLists.
     * @return Value of property mailingLists.
     *
     */
    public java.util.List getMailingLists() {
        return mailingLists;
    }
    
    /** Setter for property mailingLists.
     * @param mailingLists New value of property mailingLists.
     *
     */
    public void setMailingLists(java.util.List mailingLists) {
        this.mailingLists = mailingLists;
    }
    
    /** Getter for property addable.
     * @return boolean.
     *
     */
    public boolean getAddable() {
        return this.addable;
    }
    
    /** Setter for property addable.
     *  Allow to add New Mail Code to a member/entity if at least one Address/Location is already associated.
     * @param addable New value of property addable.
     *
     */
    public void setAddable(boolean addable) {
        this.addable = addable;
    }
    
    /** Getter for property editable.
     * @return boolean.
     *
     */
    public boolean getEditable() {
        return this.editable;
    }
    
    /** Setter for property editable.
     * @param editable New value of property editable.
     *
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
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
    
    /** Getter for property add.
     * @return Value of property add.
     *
     */
    public java.lang.String getAdd() {
        return add;
    }
    
    /** Setter for property add.
     * @param add New value of property add.
     *
     */
    public void setAdd(java.lang.String add) {
        this.add = add;
    }
    
    public boolean isAddLink() {
        return add != null;
    }
    
    /** Getter for property originate.
     * @return Value of property originate.
     *
     */
    public java.lang.String getOriginate() {
        return originate;
    }
    
    /** Setter for property originate.
     * @param tabFile New value of property originate.
     *
     */
    public void setOriginate(java.lang.String originate) {
        this.originate = originate;
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
    
    /** Determine if the mailing list name is a bulk type list
     * @param String Mailing List Name
     *
     */
    public boolean isBulkType(java.lang.String mailingListNm) {
        return (mailingListNm != null && mailingListNm.indexOf("Bulk") > 0) ? true : false;
    }
    
    /** Set up some business rules ..
     *  Allow to edit Bulk Count if it's an MLBO (Mailing List By Organization) and at least one Mail Code is a Bulk Type.
     *
     */
    public void setPrivileges(List mailingList) {
        setEditable(false);
        if (mailingList != null && mailingList.size() > 0) {
            MailingListData data = null;
            Iterator itr = mailingList.iterator();
            while (itr.hasNext()) {
                data = (MailingListData) itr.next();
                if (!isMLBP() && isBulkType(data.getMailingListNm())) {
                    setEditable(true);
                }
            }
        }
        return;
    }
    
    public String toString() {
        return
        "Pk=" + pk + ", " +
        "mailingLists=" + mailingLists + ", " +
        "editable=" + editable + ", " +
        "addable=" + addable + ", " +
        "originate=" + originate + ", " +
        "MLBP?=" + MLBP;
    }
    
    /** Getter for property actingAsAffiliate.
     * @return Value of property actingAsAffiliate.
     *
     */
    public boolean isActingAsAffiliate() {
        return actingAsAffiliate;
    }
    
    /** Getter for property actingAsAffiliate.
     * @return Value of property actingAsAffiliate.
     *
     */
    public boolean getActingAsAffiliate() {
        return actingAsAffiliate;
    }
    
    /** Setter for property actingAsAffiliate.
     * @param actingAsAffiliate New value of property actingAsAffiliate.
     *
     */
    public void setActingAsAffiliate(boolean actingAsAffiliate) {
        this.actingAsAffiliate = actingAsAffiliate;
    }
    
}


