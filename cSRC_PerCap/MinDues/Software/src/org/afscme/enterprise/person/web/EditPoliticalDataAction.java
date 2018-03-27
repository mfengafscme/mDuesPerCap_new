
package org.afscme.enterprise.person.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

// Java imports
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.PoliticalData;
import org.afscme.enterprise.member.MemberAffiliateResult;
import org.afscme.enterprise.codes.Codes;

/**
 * This action is to edit Political Legislative Information.
 *
 * @struts:action   path="/editPoliticalData"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/EditPoliticalLegislativeInformation.jsp"
 */
public class EditPoliticalDataAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		Integer personPk = getCurrentPersonPk(request);
		PoliticalData politicalData = s_maintainPersons.getPoliticalData(personPk);

		PoliticalDataForm politicalDataForm = new PoliticalDataForm();
		politicalDataForm.setPoliticalObjectorFg(politicalData.getPoliticalObjectorFg());
		politicalDataForm.setPoliticalDoNotCallFg(politicalData.getPoliticalDoNotCallFg());
                
                /*
                 * Modifications made to support view data utility data level access control methods
                 * Pass null vduAffiliates into getMemberAffiliateSummary() to support message signature
                 * 
                 */
                
                Collection vduAffiliates = null; // will always be null for this action
		Collection memberAffiliateResults = s_maintainMembers.getMemberAffiliatesSummary(personPk, vduAffiliates);
		Iterator i = memberAffiliateResults.iterator();
		while(i.hasNext()) {
			MemberAffiliateResult mar = (MemberAffiliateResult)i.next();
			if(mar.getMbrType().equals(Codes.MemberType.O)) {
				politicalDataForm.setPoliticalObjectorEnabled(new Boolean(false));
				politicalDataForm.setPoliticalDoNotCallEnabled(new Boolean(false));
				break;
			}
			else if(mar.getMbrType().equals(Codes.MemberType.A)) {
				politicalDataForm.setPoliticalDoNotCallEnabled(new Boolean(false));
			}
		}

        request.setAttribute("origin", request.getParameter("origin"));
        request.setAttribute("politicalData", politicalData);
        request.setAttribute("politicalDataForm", politicalDataForm);
        return mapping.findForward("Edit");
    }
}
