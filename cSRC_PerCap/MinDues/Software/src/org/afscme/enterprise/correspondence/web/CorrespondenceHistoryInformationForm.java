package org.afscme.enterprise.correspondence.web;

import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.person.CorrespondenceData;


/** Holds the data on the Correspondence History Information screen
 * @struts:form name="correspondenceHistoryInformationForm"
 */
public class CorrespondenceHistoryInformationForm extends SearchForm {

    private List correspondenceHistoryList;
    private String origin;

    public CorrespondenceHistoryInformationForm() {
        sortBy = "date";
        order = SortData.DIRECTION_DESCENDING;
    }
    
    public SortData getSortData() {
        SortData sortData = new SortData();
        
		// Get and sort correspondence list based on the parameters passed
        if (sortBy.equals("date"))
            sortData.setSortField(CorrespondenceData.SORT_BY_DATE);
        else if(sortBy.equals("name"))
            sortData.setSortField(CorrespondenceData.SORT_BY_NAME);

        sortData.setDirection(order);
        
        return sortData;
    }

    /** Getter for property correspondenceHistoryList.
     * @return Value of property correspondenceHistoryList.
     *
     */
    public java.util.List getCorrespondenceHistoryList() {
        return correspondenceHistoryList;
    }
    
    /** Setter for property correspondenceHistoryList.
     * @param correspondenceHistoryList New value of property correspondenceHistoryList.
     *
     */
    public void setCorrespondenceHistoryList(java.util.List correspondenceHistoryList) {
        this.correspondenceHistoryList = correspondenceHistoryList;
    }
    
    
    /** Getter for property origin.
     * @return Value of property origin.
     *
     */
    public java.lang.String getOrigin() {
        return origin;
    }
    
    /** Setter for property origin.
     * @param origin New value of property origin.
     *
     */
    public void setOrigin(java.lang.String origin) {
        this.origin = origin;
    }
    
}
