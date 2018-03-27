package org.afscme.enterprise.member.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.member.ParticipationData;

/* This action will save an edited member participation record.
 * 
 */
 
/**
 * @struts:action   path="/saveParticipationDetail"
 *                  name="participationDetailForm"
 *                  input="/Membership/ParticipationDetailEdit.jsp"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="View"  path="/viewParticipationDetail.action"
 *
 */

public class SaveParticipationDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction{
    
    /** Creates a new instance of SaveParticipationDetailAction */
    public SaveParticipationDetailAction() {
    }
    
     public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        ParticipationDetailForm pdf = (ParticipationDetailForm)form;
        ParticipationData pd = null;
        
        // validate form
        ActionErrors errors = pdf.validate(mapping, request);
        
        // if no validation errors, check and see if record 
        // that is being added already exists, if it does, add error
        if (errors == null || errors.isEmpty()){
            if (!pdf.getOldDetailPk().equals(pdf.getDetailPk())) {
                pd = s_maintainMembers.getParticipationData(getCurrentPersonPk(request), pdf.getDetailPk());
                if (pd != null) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.participation.record.exists"));
                }
            }
        }

        // if errors present, re-prepare data for add page and return to input
        if (errors != null && !errors.isEmpty()) {
            saveErrors(request, errors);
            
            if (pdf.getTypePk() != null) {
                request.setAttribute("prepopTypes", s_maintainParticipationGroups.getParticipationTypes(pdf.getGroupPk()));
                request.setAttribute("prepopDetails", s_maintainParticipationGroups.getParticipationDetails(pdf.getGroupPk(),pdf.getTypePk()));                
            }
            if (pdf.getDetailPk() != null) {
                request.setAttribute("prepopOutcome", s_maintainParticipationGroups.getParticipationOutcomes(pdf.getGroupPk(),pdf.getTypePk(),pdf.getDetailPk())); 
            }

            Collection participationCollection = s_maintainParticipationGroups.getParticipationOutcomes();
            Collection distinctGroups = s_maintainParticipationGroups.getDistinctParticipationGroups();
            //set the participation data into the request
            request.setAttribute("participationCollection", participationCollection); 
            request.setAttribute("distinctGroups", distinctGroups);
            return mapping.getInputForward();
        }
        
        // add participation and return to view page
        pd = pdf.getParticipationData(getCurrentPersonPk(request), usd.getPersonPk());
        s_maintainMembers.editParticipationData(pd, pdf.getOldDetailPk());
        return mapping.findForward("View");
    }    
    
}
