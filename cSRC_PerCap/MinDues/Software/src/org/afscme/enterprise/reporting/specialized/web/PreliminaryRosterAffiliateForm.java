package org.afscme.enterprise.reporting.specialized.web;

import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.reporting.specialized.PreliminaryRosterAffiliate;


/** Holds the data on the Preliminary Roster Affiliates screen
 * @struts:form name="preliminaryRosterAffiliateForm"
 */
public class PreliminaryRosterAffiliateForm extends SearchForm {
    
    // Property to hold Affiliates
    private ArrayList affiliateList = new ArrayList();
    private boolean selectAll = false;
    private int size = 0;
    
    public String toString() {
        return affiliateList.toString();
    }         
    
// Getter and Setter Methods...     
    public ArrayList getAffiliateList(boolean b) {
        return this.affiliateList;
    }
    
    public void setAffiliateList(ArrayList affiliateList) {
        this.affiliateList = affiliateList;
    }
    
    public PreliminaryRosterAffiliate getAffiliateList(int index){
        while (this.affiliateList.size() <= index) {
            this.affiliateList.add(new PreliminaryRosterAffiliate());
        }
        return (PreliminaryRosterAffiliate)this.affiliateList.get(index);
    }
    
    public void setAffiliateList(int index, PreliminaryRosterAffiliate affiliateData) {
        affiliateList.set(index, affiliateData);
    }
    
    public Object[] getAffiliateList() {
        if (affiliateList != null)
            return this.affiliateList.toArray();
        else
            return null;
    }        
    
    /** Getter for property size.
     * @return Value of property size.
     *
     */
    public int getSize() {
        return size;
    }
    
    /** Setter for property size.
     * @param size New value of property size.
     *
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /** Getter for property selectAll.
     * @return Value of property selectAll.
     *
     */
    public boolean isSelectAll() {
        return selectAll;
    }

    /** Getter for property selectAll.
     * @return Value of property selectAll.
     *
     */
    public boolean getSelectAll() {
        return selectAll;
    }
    
    /** Setter for property selectAll.
     * @param selectAll New value of property selectAll.
     *
     */
    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }        
}


