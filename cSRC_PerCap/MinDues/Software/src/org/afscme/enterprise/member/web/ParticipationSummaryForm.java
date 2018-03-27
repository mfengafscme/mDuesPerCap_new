package org.afscme.enterprise.member.web;

import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.member.ParticipationData;


/** Holds the data on the Member Participation Search screen
 * @struts:form name="participationSummaryForm"
 */

public class ParticipationSummaryForm extends SearchForm {
    
    private String origin;
    private Collection memberParticipations;    
    
    /** Creates a new instance of ParticipationSummaryForm */
    public ParticipationSummaryForm() {
        sortBy = "date";
        order = SortData.DIRECTION_DESCENDING;        
    }
       
    public SortData getSortData() {
        SortData sortData = new SortData();
        
	// Get and sort participation list based on the parameters passed
        if (sortBy.equals("group"))
            sortData.setSortField(ParticipationData.SORT_BY_GROUP);
        else if(sortBy.equals("type"))
            sortData.setSortField(ParticipationData.SORT_BY_TYPE);
        else if(sortBy.equals("detail"))
            sortData.setSortField(ParticipationData.SORT_BY_DETAIL);
        else if(sortBy.equals("outcome"))
            sortData.setSortField(ParticipationData.SORT_BY_OUTCOME);
        else if(sortBy.equals("date"))
            sortData.setSortField(ParticipationData.SORT_BY_DATE);        

        sortData.setDirection(order);
        
        return sortData;
    }    
    
    /** Getter for property memberParticipations.
     * @return Value of property memberParticipations.
     *
     */    
    public Collection getMemberParticipations() {
        return memberParticipations;
    }
    
   /** Setter for property memberParticipations.
     * @param origin New value of property memberParticipations.
     *
     */    
    public void setMemberParticipations(Collection memberParticipations) {
        this.memberParticipations = memberParticipations;
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
