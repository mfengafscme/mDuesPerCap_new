package org.afscme.enterprise.person.web;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.person.web.SearchPersonForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.person.PersonCriteria;
import org.afscme.enterprise.person.PersonResult;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PersonData;


/**
 * Searches for organizations, based on criteria in the SearchPowerPersonForm.
 * Params:
 *      new - if present, the user is shown the search form.
 *
 * @struts:action   path="/searchPowerPerson"
 *                  input="/Membership/PowerPersonSearch.jsp"
 *                  name="searchPersonForm"
 *                  validate="true"
 *                  scope="session"
 *
 * @struts:action-forward name="SearchForm" path="/Membership/PowerPersonSearch.jsp"
 * @struts:action-forward name="SearchResults" path="/Membership/PersonSearchResults.jsp"
 * @struts:action-forward name="PersonDetail" path="/viewPersonDetail.action"
 * @struts:action-forward name="PersonDetailAdd" path="/verifyPerson.action?back=PersonAdd"
 */
public class SearchPowerPersonAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, 
           UserSecurityData usd) throws Exception {
        
        SearchPersonForm personForm = (SearchPersonForm)form;
        Integer dept = usd.getDepartment();
        
               
        //get the search criteria from the form
        PersonCriteria data = personForm.getPersonCriteriaData();
        ArrayList result = new ArrayList();
        log.debug(" SearchPowerPersonForm search form contents are: " + personForm.toString());
        
        //perform the search
        int count = s_maintainPersons.searchPersons(data, dept, result );
         
        log.debug("SearchPowerPersonAction: after searchPowerPersons call count is: " + count);
         
        
        //show the correct page
        switch (count) {
            case 0:
                // go direct to adding a person
                return mapping.findForward("PersonDetailAdd");
            case 1:
             /** In future go to person detail if only 1 row in search result */
                Iterator iter = result.iterator();
                PersonResult personResult = (PersonResult) iter.next();
                Integer ppk = personResult.getPersonPk();
                setCurrentPerson(request, ppk);

                return mapping.findForward("PersonDetail");
            default:  // i.e. more than one row returned in result
                //put the search result in the form
                personForm.setResults(result);
                personForm.setTotal(count);
                return mapping.findForward("SearchResults");
        }                
    }
}
