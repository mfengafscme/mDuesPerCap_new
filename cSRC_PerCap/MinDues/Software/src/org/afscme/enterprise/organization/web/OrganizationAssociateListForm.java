package org.afscme.enterprise.organization.web;

import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.organization.OrgAssociateResult;


/**
 * Represents the form for all the Organization Associates for an 
 * External Organization
 *
 * @struts:form name="organizationAssociateListForm"
 */
public class OrganizationAssociateListForm extends SearchForm {

    private Integer pk;
    
     /**
     * constructor to set up values
     */   
    public OrganizationAssociateListForm() {
        newList();
    }

    /** resets the sort values to the default */
    public void newList() {
        sortBy = "name";
        order = SortData.DIRECTION_ASCENDING;
        page=0;
    }
    
    public Integer getPk() {
        return pk;
    }
    
    public void setPk(Integer pk) {
        this.pk = pk;
    }

    /**
     * toString method
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("pk: " + pk); 
        buf.append(", order: " + order);
        buf.append(", sortBy: " + sortBy);
        buf.append(", page: " + page);
        buf.append(", total: " + total);
        return buf.toString()+"]";
    }
    
    public SortData getSortData() {
        SortData result = new SortData();
        result.setPage(page);
        result.setDirection(order);
        if (sortBy == null)
            result.setSortField(OrgAssociateResult.SORT_FIELD_NONE);
        else if (sortBy.equals("name"))
            result.setSortField(OrgAssociateResult.SORT_FIELD_NAME);
        else if (sortBy.equals("title"))
            result.setSortField(OrgAssociateResult.SORT_FIELD_TITLE);
        else if (sortBy.equals("location"))
            result.setSortField(OrgAssociateResult.SORT_FIELD_LOCATION);
        else if (sortBy.equals("phone"))
            result.setSortField(OrgAssociateResult.SORT_FIELD_PHONE);
        else if (sortBy.equals("email"))
            result.setSortField(OrgAssociateResult.SORT_FIELD_EMAIL);
        
        return result;
    }
}



