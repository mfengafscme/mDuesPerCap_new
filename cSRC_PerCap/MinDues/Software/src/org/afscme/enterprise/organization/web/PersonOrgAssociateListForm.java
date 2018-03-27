package org.afscme.enterprise.organization.web;

import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.organization.OrgAssociateResult;


/**
 * Represents the form for all the Organization Associates records for a Person 
 *
 * @struts:form name="personOrgAssociateListForm"
 */
public class PersonOrgAssociateListForm extends SearchForm
{
    
    /** toString method
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("order: " + order);
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
 
        return result;
     }

}



