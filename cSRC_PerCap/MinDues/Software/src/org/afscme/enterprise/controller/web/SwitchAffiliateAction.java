package org.afscme.enterprise.controller.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * Allows a data utility user to switch the affiliate they are acting as.
 * 
 * This performs the same function as directing the user to /selectAffiliateSearch.action?new,
 * however, this is created as a separate action so it's permissions can be managed separately.
 * i.e., the user can only see this action if they have access to multiple affiliates.
 *
 * @struts:action   path="/switchAffiliate"
*/
public class SwitchAffiliateAction extends AFSCMEAction 
{
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
    {
        //go to the affiliate data utilty
        String returnString = "/selectAffiliateSearch.action?new=&cancel=selectAffiliate&pk=" + usd.getActingAsAffiliate().toString();
        usd.setActingAsAffiliate(null);
        usd.setAffiliateName(null);
        usd.setAccessibleAffiliates(null);
        return new ActionForward(returnString);
    }
}
