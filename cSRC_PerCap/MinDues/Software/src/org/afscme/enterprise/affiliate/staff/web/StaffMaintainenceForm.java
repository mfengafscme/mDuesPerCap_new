package org.afscme.enterprise.affiliate.staff.web;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.affiliate.staff.StaffResult;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.common.web.SearchForm;



/**
 * Represents the form on the select user affiliates search page
 *
 * @struts:form name="staffMaintainenceForm"
 */
public class StaffMaintainenceForm extends SearchForm
{
    protected Integer pk;
    
    public Integer getPk() {
        return pk;
    }
    
    public void setPk(Integer pk) {
        this.pk = pk;
    }
    
     public SortData getSortData() {
        SortData result = new SortData();
        result.setPage(page);
        result.setDirection(order);
        result.setSortField(StaffResult.sortStringToCode(sortBy));
        
        return result;
     }
                
}



