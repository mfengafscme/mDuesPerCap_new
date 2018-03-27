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
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/* This struts action performs the work to save the Meber Detail - Edit page data and is
 called from that page. It handles the return through the back parameter .
 it uses the MaintainPersons method isDuplicateSSN and person Duplicate SSN page to check for
 duplicate SSN on save. */

/**
 * @struts:action   path="/saveMemberDetail"
 *                  name="memberDetailForm"
 *                  scope="session"
 *                  validate="false"
 *                  input="/Membership/MemberDetailEdit.jsp"
 *
 * @struts:action-forward   name="PersonDuplicateSSN"  path="/viewDuplicateSSNNotifierPerson.action"
 * @struts:action-forward   name="MemberEdit"  path="/Membership/MemberDetailEdit.jsp"
 * @struts:action-forward   name="MemberView"  path="/viewMemberDetail.action"
 */
public class SaveMemberDetailAction extends AFSCMEAction {

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
        log.debug("SaveMemberAction: Entering");

        HttpSession session = request.getSession();
        MemberDetailForm mdf = (MemberDetailForm) session.getAttribute("memberDetailForm");

       Integer personPk = getCurrentPersonPk(request);
       String back = request.getParameter("back");             // used for the entry point Return
       // Set Return button action
       request.setAttribute("back", back);
       request.setAttribute("memberDetailForm", mdf);

       if (request.getParameter("cancel") != null) // handle the cancel from dup ssn
       {
           // mdf = (MemberDetailForm)session.getAttribute("memberDetailForm");
            return mapping.findForward("MemberEdit");
       }
       if (request.getParameter("continue") != null) // conmtinue from dup ssn
       {
           mdf = (MemberDetailForm)session.getAttribute("memberDetailForm"); // set the data and then roll down to process the edit
       }


       // validate if we haven't been this way before, cancel or continue means we came from dup ssn
       // this block will be skipped if continue != null, should not get here if cancel != null
       if (request.getParameter("continue") == null)
       {
            // the box wasn't checked
            if(request.getParameter("ssnValid") == null) {
                mdf.setSsnValid(new Boolean(false));
            }

            // the box wasn't checked
            if(request.getParameter("mbrBarredFg") == null) {
                mdf.setMbrBarredFg(new Boolean(false));
            }
            //validate manually
            ActionErrors errors = mdf.validate(mapping, request);
            if (s_maintainMembers.isPersonACurrentOfficer(personPk) & mdf.getMbrBarredFg().booleanValue()) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.memberDetailEdit.memberStillAnOfficer"));
                mdf.setMbrBarredFg(new Boolean(false));
            }
            
            if (errors != null && !errors.isEmpty())
            {
                saveErrors(request, errors);

                mdf.setPersonAddressRecord(s_systemAddress.getSystemAddress(personPk));
                mdf.setEmailData(s_maintainPersons.getPersonEmails(personPk));
                mdf.setPhoneData(s_maintainPersons.getPersonPhones(personPk, usd.getDepartment()));

                //return to the Edit page if validation errors
                return mapping.findForward("MemberEdit");

            }
/*			
            //check for Duplicate SSN
            if (s_maintainPersons.isDuplicateSSN(mdf.getSsn()))
            {
                session.setAttribute("back", "MemberEdit");
                session.setAttribute("memberDetailForm", mdf); // save values in case return is needed
                return mapping.findForward("PersonDuplicateSSN");
            }
*/

           if (mdf.getPreviousSsn() != null)
           {				   
                if (!mdf.getPreviousSsn().equals(mdf.getSsn()))
                {
                    if (s_maintainPersons.isDuplicateSSN(mdf.getSsn())) 
                    {
                        session.setAttribute("back", "MemberEdit");
                        session.setAttribute("memberDetailForm", mdf); // save values in case return is needed
                        return mapping.findForward("PersonDuplicateSSN");
                    }
                } else if (mdf.getPreviousSsn().equals(mdf.getSsn()))
                    {
                        if (s_maintainPersons.isDuplicateSSNGreaterThan1(mdf.getSsn())) 
                        {
                            session.setAttribute("back", "MemberEdit");
                            session.setAttribute("memberDetailForm", mdf); // save values in case return is needed
                            return mapping.findForward("PersonDuplicateSSN");
                        }				
                     }
           } else {
            	if (s_maintainPersons.isDuplicateSSN(mdf.getSsn()))
            	{
                    session.setAttribute("back", "MemberEdit");
                    session.setAttribute("memberDetailForm", mdf); // save values in case return is needed
                    return mapping.findForward("PersonDuplicateSSN");
            	}				
           }

        }

        mdf.setPersonPk(personPk);

        //update member detail information
        MemberData md = mdf.getMemberData();
        /*if(request.getParameter("continue") == null) {
        	if(request.getParameter("ssnValid") == null) {
				//mdf.setMbrBarredFg(new Boolean(false));
				md.getThePersonData().setSsnValid(new Boolean(false));
			}
			if(request.getParameter("mbrBarredFg") == null) {
				md.setMbrBarredFg(new Boolean(false));
			}
		}*/
        s_maintainMembers.updateMemberDetail(md, usd.getPersonPk());

        //check for a name change.
        //If so, then add to weekly card run and reset name display on header/footer tags.
        String currentName = getCurrentPersonName(session);
        String newName = s_maintainUsers.getPersonName(personPk);
        if (!TextUtil.equals(currentName, newName)) {

            // add to weekly card run so member gets new card for changed name
            s_maintainMembers.addToWeeklyCardRun(personPk);
            // Reset person name in session so headers/footers display correctly
            setCurrentPersonName(request, newName);
        }

        // go to MemberDetaill view once everything is saved
        // cleanup - remove detail form from session
        session.removeAttribute("memberDetailForm");
        return mapping.findForward("MemberView");
    }
}
