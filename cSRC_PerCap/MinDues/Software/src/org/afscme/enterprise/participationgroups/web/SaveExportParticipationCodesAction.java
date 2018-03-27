package org.afscme.enterprise.participationgroups.web;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.participationgroups.ParticipationGroupData;
import org.afscme.enterprise.participationgroups.web.ParticipationMaintenance;

/**
 * @struts:action   path="/saveExportParticipationCodes"
 *                  input="/Membership/ExportParticipationCodes.jsp"
 *                  name="exportParticipationCodesForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="View"  path="/viewParticipationGroup.action"
 * @struts:action-forward   name="SaveAs"  path="/saveAs" redirect="true"
 */
public class SaveExportParticipationCodesAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response,
                                 UserSecurityData usd) throws Exception {
        
        ExportParticipationCodesForm epcForm = (ExportParticipationCodesForm)form;
        if (isCancelled(request)) {
            return mapping.findForward("View");
        }
        
        // Validations
        ActionErrors errors = epcForm.validate(mapping, request);
        
        // Return to input page if errors
        if (errors != null && !errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        
        // Get participation groups data   
        ParticipationMaintenance participationMaintenance = new ParticipationMaintenance();
        participationMaintenance.setGroup(epcForm.getGroupPk());
        ParticipationGroupData groupData = s_maintainParticipationGroups.getParticipationGroupData(epcForm.getGroupPk());
        if (groupData != null) {
            participationMaintenance.setGroupNm(groupData.getName());            
        }
        
        // Retrieve all of the participation type, detail and outcome objects for a group
        participationMaintenance.setTypes(s_maintainParticipationGroups.getParticipationTypes(epcForm.getGroupPk(), epcForm.getTypePk()));        
        if (epcForm.getDetailPk()!=null && epcForm.getDetailPk().intValue()>0) {
            participationMaintenance.setDetails(s_maintainParticipationGroups.getParticipationDetails(epcForm.getGroupPk(), epcForm.getTypePk(), epcForm.getDetailPk()));
            participationMaintenance.setOutcomes(s_maintainParticipationGroups.getParticipationOutcomes(epcForm.getGroupPk(), epcForm.getTypePk(), epcForm.getDetailPk()));
        } else {
            participationMaintenance.setDetails(s_maintainParticipationGroups.getParticipationDetails(epcForm.getGroupPk(), epcForm.getTypePk()));
            participationMaintenance.setOutcomes(s_maintainParticipationGroups.getParticipationOutcomes(epcForm.getGroupPk(), epcForm.getTypePk()));
        }

        // HLM Fix defect #564: Change to redirect
        // Set the hierarchy data into the request session
        request.getSession().setAttribute("participationMaintenance", participationMaintenance);      
        request.getSession().setAttribute("delimiter", epcForm.getOutputFormat().toString());
        return mapping.findForward("SaveAs");        
    }
}

