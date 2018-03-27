package org.afscme.enterprise.member.web;

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
import javax.servlet.http.HttpSession;
import org.afscme.enterprise.member.web.SearchMembersForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.member.MemberCriteria;
import org.afscme.enterprise.member.MemberResult;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.member.MemberData;
import org.afscme.enterprise.util.TextUtil;
import javax.ejb.EJBException;



/**
 * Searches for organizations, based on criteria in the SearchMembersForm.
 * Params:
 *      new - if present, the user is shown the search form.
 *
 * @struts:action   path="/searchMembers"
 *                  name="searchMembersForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward name="Basic" path="/Membership/BasicMemberSearch.jsp"
 * @struts:action-forward name="Power" path="/Membership/PowerMemberSearch.jsp"
 * @struts:action-forward name="SearchForm" path="/Membership/BasicMemberSearch.jsp"
 * @struts:action-forward name="SearchResults" path="/Membership/MemberSearchResults.jsp"
 * @struts:action-forward name="MbrDetail" path="/viewMemberDetail.action"
 * @struts:action-forward name="MbrDetailAdd" path="/addMemberDetail.action"
 */
public class SearchMembersAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        SearchMembersForm mbrForm = (SearchMembersForm)form;
        
        // do not need to validate if the user is sorting
        ActionErrors actionErrors = validate(mapping, request, mbrForm);
        if(actionErrors.size() > 0) {
            saveErrors(request, actionErrors);
            if(mbrForm.getInput().equals("power")) {
                return mapping.findForward("Power");
            } else {
                return mapping.findForward("Basic");
            }
        }
        
        Integer dept = usd.getDepartment();
        
        //get the search criteria from the form
        MemberCriteria data = mbrForm.getMemberCriteriaData();
        
        if (!TextUtil.isEmpty(mbrForm.getAddrUpdatedBy())) {
            try {
                // searhing by user's person pk
                data.setAddrUpdatedBy(s_maintainUsers.getPersonPK(mbrForm.getAddrUpdatedBy()).toString());
            } catch(EJBException e) {
                // user does not exist
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchMember.userId", mbrForm.getAddrUpdatedBy()));
                saveErrors(request, errors);
                if(mbrForm.getInput().equals("power")) {
                    return mapping.findForward("Power");
                } else {
                    return mapping.findForward("Basic");
                }
            }
        }
        
        if (!TextUtil.isEmpty(mbrForm.getLstModUserPk())) {
            try {
                // searhing by user's person pk
                data.setLstModUserPk(s_maintainUsers.getPersonPK(mbrForm.getLstModUserPk()));
            } catch(EJBException e) {
                // user does not exist
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchMember.userId", mbrForm.getLstModUserPk()));
                saveErrors(request, errors);
                if(mbrForm.getInput().equals("power")) {
                    return mapping.findForward("Power");
                } else {
                    return mapping.findForward("Basic");
                }
            }
        }
        ArrayList result = new ArrayList();
        log.debug(" SearchMembersForm search form contents are: " + mbrForm.toString());
        String[] selectList = mbrForm.getSelectList();
        
        /* for debug only -> for(int i = 0; i < selectList.length; i++)
        {
            log.debug("SearchMembersAction: before searchMembers selectList from form is - selectList member " + i + " is: " + selectList[i]);
         
        } */
        
        //perform the search
        int count = s_maintainMembers.searchMembers(data, dept, result );
        
        log.debug("SearcheMembersAction: after searchMembers call count is: " + count);
        
        //show the correct page
        switch (count) {
            case 0:
                // go direct to adding a member
                HttpSession session = request.getSession();
                session.removeAttribute("verifyMemberForm");
                
                //HLM: Fix defect #177
                session.setAttribute("returnTo", mbrForm.getInput());
                return mapping.findForward("MbrDetailAdd");
            case 1:
                /** In future go to member detail if only 1 row in search result */
                Iterator iter = result.iterator();
                MemberResult mr = (MemberResult) iter.next();
                Integer ppk = mr.getPersonPk();
                setCurrentPerson(request, ppk);                
                return mapping.findForward("MbrDetail");
            default:  // i.e. more than one row returned in result
                //put the search result in the form
                mbrForm.setResults(result);
                mbrForm.setTotal(count);
                return mapping.findForward("SearchResults");
        }
    }
    
    /**
     * validation method
     */
    // was originally in the form class, but since input is dynamic, it has been implemented here
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, SearchMembersForm mbrForm) {
        
        ActionErrors errors = new ActionErrors();
        // no criteria entered
        if (mbrForm.allNull()) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noSearchValueEntered"));
        }
        // no columns selected (fields to display)
        if (mbrForm.getSelectList() == null || mbrForm.getSelectList().length == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noDisplayFieldsSelected"));
        }
        
        if (mbrForm.getLastNm() != null && mbrForm.getLastNm().indexOf('%') > 0) { // lastNm has a percent wildcard
            int locCard = mbrForm.getLastNm().indexOf('%');
            StringBuffer sb = new StringBuffer(mbrForm.getLastNm());
            int lengthNm = sb.length();
            if (locCard != (lengthNm - 1)) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.search.wildCardPercentMustBeLast"));
            }
        }
        if (mbrForm.getFirstNm() != null && mbrForm.getFirstNm().indexOf('%') > 0) { // firstNm has a percent wildcard
            int locCard = mbrForm.getFirstNm().indexOf('%');
            StringBuffer sb = new StringBuffer(mbrForm.getFirstNm());
            int lengthNm = sb.length();
            if (locCard != (lengthNm - 1)) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.search.wildCardPercentMustBeLast"));
            }
        }
        return errors;
    }
}
