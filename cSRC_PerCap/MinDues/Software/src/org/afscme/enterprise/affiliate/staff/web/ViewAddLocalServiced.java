package org.afscme.enterprise.affiliate.staff.web;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.LocationData;


/**
 * @struts:action   path="/viewAddLocalServiced"
 *                  name="addLocalServicedForm"
 *                  scope="session"
 *
 * @struts:action-forward   name="View"  path="/Membership/AddLocalServiced.jsp"
 */
public class ViewAddLocalServiced extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        Integer personPk = getCurrentPersonPk(request, "personPk");
        Integer affPk = getCurrentAffiliatePk(request, "affPk");
        
        AddLocalServicedForm alsForm = (AddLocalServicedForm)form;
        alsForm.setAffiliateId(s_maintainAffiliates.getAffiliateData(affPk).getAffiliateId());
        
        return mapping.findForward("View");
 }

}
