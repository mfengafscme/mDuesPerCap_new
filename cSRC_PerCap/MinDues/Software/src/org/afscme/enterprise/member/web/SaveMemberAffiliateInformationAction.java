package org.afscme.enterprise.member.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.member.MemberData;
import org.afscme.enterprise.member.MemberAffiliateData;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;
import org.afscme.enterprise.codes.Codes.MemberStatus;
import org.afscme.enterprise.util.DBUtil;

/* This struts action performs the work to save the Member Affiliate Information - Edit page data and is
 * called from that page. Much simpler processing logic than Member Detail - Edit as the return from Dup
 * SSN does not happen (neither does the call and forward)
  */

/**
 * @struts:action   path="/saveMemberAffiliateInformation"
 *                  name="memberAffiliateInformationForm"
 *                  scope="session"
 *                  validate="false"
 *                  input="/Membership/MemberAffiliateInformationEdit.jsp"
 *
 * @struts:action-forward   name="MemberAffEdit"  path="/Membership/MemberAffiliateInformationEdit.jsp"
 * @struts:action-forward   name="MemberAffView"  path="/viewMemberAffiliateInformation.action"
 */
public class SaveMemberAffiliateInformationAction extends AFSCMEAction {

    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet request that is being processed
     * @param usd Security data for the user performing this action
     *
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response, UserSecurityData usd)
    throws Exception {
        log.debug("SaveAffiliateInformation: Entering");

  //      MemberAffiliateInformationForm maif = (MemberAffiliateInformationForm)form;
         HttpSession session = request.getSession();
         MemberAffiliateInformationForm maif = (MemberAffiliateInformationForm) session.getAttribute("memberAffiliateInformationForm");

       // Integer personPk = getCurrentPersonPk(request);

      //  String back = request.getParameter("back");
        // Set Return button action
       // request.setAttribute("back", back);
       // request.setAttribute("memberAffiliateInformation", maif);

        if (request.getParameter("cancel") != null)
        {
            // cleanup
            session.removeAttribute("memberAffiliateInformationForm");
            return mapping.findForward("MemberAffView"); // return to the MemberAffiliateInformation (view) page
       }


       if (request.getParameter("cancel") == null)
       {
            //validate manually
            ActionErrors errors = maif.validate(mapping, request);
            if (errors != null && !errors.isEmpty())
            {
                saveErrors(request, errors);

                //return to the Edit page if validation errors
                return mapping.findForward("MemberAffEdit");

            }
       }

        AffiliateData affiliateData = s_maintainAffiliates.getAffiliateData(maif.getAffPk());
        // if member status is not inactive and aff status is deactivated, cannot map member to affiliate
        if(!maif.getMbrStatus().equals(MemberStatus.I) &&
           affiliateData.getStatusCodePk().equals(AffiliateStatus.D)) {
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.updateMemberAffiliate.mustBeInactive"));
			saveErrors(request, errors);
            return mapping.getInputForward();
		}

        // set mail flags using hidden form element rather than checkbox element, since checkbox may be disabled
        MemberAffiliateData mad = maif.getMemberAffiliateData();
		mad.setNoMailFg(maif.getNoMailFlag());
		mad.setNoCardsFg(maif.getNoCardsFlag());
		mad.setNoPublicEmpFg(maif.getNoPublicEmpFlag());
		mad.setNoLegislativeMailFg(maif.getNoLegislativeMailFlag());

        // update member affiliate information for the member in this affiliate
        s_maintainMembers.updateMemberAffiliateData(mad, usd.getPersonPk());
        // cleanup viewAction will re-retrieve data from the database, so in this instance the
        session.removeAttribute("memberAfiliateInformationForm");
        // go to MemberDetaill view once everything is saved
        return mapping.findForward("MemberAffView");
    }
}
