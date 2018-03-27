package org.afscme.enterprise.rebate.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import java.util.Calendar;
import java.util.List;
import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PRBCheckInfo;
import org.afscme.enterprise.person.PRBAffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;

/**
 * @struts:action   path="/editPoliticalRebateAnnualRebateInformation"
 *		    name="politicalRebateAnnualRebateInformationEditForm"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/PoliticalRebateAnnualRebateInformationEdit.jsp"
 * @struts:action-forward   name="Save"  path="/savePoliticalRebateAnnualRebateInformation.action"
 */
public class EditPoliticalRebateAnnualRebateInformationAction extends DuesPaidInfoAction {

    static Logger log = Logger.getLogger(EditPoliticalRebateAnnualRebateInformationAction.class);
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PoliticalRebateAnnualRebateInformationEditForm editForm = (PoliticalRebateAnnualRebateInformationEditForm)form;
        
        // Get the affPk returned from Affiliate Finder Duplicate Result Page
        String affPk = (String)request.getParameter("affPk");
        if (affPk != null) {
            setDuesPaidToAffIDs(editForm, new Integer(affPk));
            return mapping.findForward("Save");            
        }
         
        // Forward to Political Rebate Annual Rebate Information - Edit page
        if (editForm.isEdit()) {
            return mapping.findForward("Edit");
        }        
        
        // Get the current person primary key from request
        Integer pk = getCurrentPersonPk(request);
        
        // Check if the login user is acting as an Affiliate via using Data Utility flow
        if (usd.getActingAsAffiliate() != null) {
            editForm.setActingAsAffiliate(true);
        }
        
        // Get the Annual Rebate Information
        editForm.setPrbRosterStatus(s_maintainPoliticalRebate.getRosterStatus(pk, editForm.getPrbYear()));
        editForm.setPRBCheckInfo(s_maintainPoliticalRebate.getPRBCheckInfo(pk, editForm.getPrbYear()));
        List list = s_maintainPoliticalRebate.getPRBAnnualRebateAffiliates(pk, editForm.getPrbYear());
        if (list!=null && list.size()>0) {
            PRBAffiliateData data = null;
            Iterator itr = list.iterator();
            int i=1;
            while (itr.hasNext()) {
                data = (PRBAffiliateData) itr.next();                
                editForm.setPRBDuesPaid(data, i);
                ++i;
            }
        }
        editForm.setEdit(true);
        return mapping.findForward("Edit");
    }
}
