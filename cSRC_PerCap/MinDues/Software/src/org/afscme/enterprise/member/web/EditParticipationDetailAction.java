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
import java.util.ArrayList;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.member.web.ParticipationDetailForm;
import org.afscme.enterprise.participationgroups.ParticipationDetailData;
import org.afscme.enterprise.member.ParticipationData;

/* This action allows for editing of a member's individual participation record.
 * 
 */
 
/**
 * @struts:action   path="/editParticipationDetail"
 *                  name="participationDetailForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/ParticipationDetailEdit.jsp" 
 *
 */

public class EditParticipationDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction{
    
    /** Creates a new instance of EditParticipationDetailAction */
    public EditParticipationDetailAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        ActionErrors errors = new ActionErrors();
        ParticipationDetailForm pdf = (ParticipationDetailForm)form;         
        
        String shortcut = request.getParameter("shortcut");
        
        // if shortcut was entered, fetch data matching shortcut
        if (shortcut != null) {
            
            // shortcut must be an integer
            if (TextUtil.isInt(shortcut)) {               
                pdf.setDetailShortcut(new Integer(shortcut));
                
                // get detail that matches shortcut
                ParticipationDetailData pdd = s_maintainParticipationGroups.getParticipationDetailData(new Integer(shortcut).intValue());
                if (pdd != null) {
                    if (request.getParameter("oldDetailPk") != null ) {
                        pdf.setOldDetailPk(new Integer(request.getParameter("oldDetailPk")));
                    } else {
                        pdf.setOldDetailPk(pdd.getDetailPk());
                    }                    
                    pdf.setGroupPk(pdd.getGroupPk());
                    pdf.setTypePk(pdd.getTypePk());
                    pdf.setDetailPk(pdd.getDetailPk());
                    request.setAttribute("prepopTypes", s_maintainParticipationGroups.getParticipationTypes(pdd.getGroupPk()));
                    request.setAttribute("prepopDetails", s_maintainParticipationGroups.getParticipationDetails(pdd.getGroupPk(),pdd.getTypePk()));            
                    request.setAttribute("prepopOutcome", s_maintainParticipationGroups.getParticipationOutcomes(pdd.getGroupPk(),pdd.getTypePk(),pdd.getDetailPk()));
                } else {
                    pdf.setGroupPk(null);                    
                    errors.add("detailShortcut", new ActionError("error.participation.shortcut.notfound"));
                    saveErrors(request, errors);
                    ParticipationData pd = s_maintainMembers.getParticipationData(getCurrentPersonPk(request), new Integer(request.getParameter("oldDetailPk")));              
                    pdf.setAll(pd);
                    pdf.setDetailShortcut(new Integer(shortcut));
                    request.setAttribute("prepopTypes", s_maintainParticipationGroups.getParticipationTypes(pdf.getGroupPk()));
                    request.setAttribute("prepopDetails", s_maintainParticipationGroups.getParticipationDetails(pdf.getGroupPk(),pdf.getTypePk()));            
                    request.setAttribute("prepopOutcome", s_maintainParticipationGroups.getParticipationOutcomes(pdf.getGroupPk(),pdf.getTypePk(),pdf.getDetailPk()));                        
                }                
            } else {
                // invalid shortcut, generate error
                errors.add("detailShortcut", new ActionError("error.field.mustBeInt"));
                saveErrors(request, errors);
            }
        } else {
            // get the detail to edit
            ParticipationData pd = s_maintainMembers.getParticipationData(getCurrentPersonPk(request), pdf.getDetailPk());              
            pdf.setAll(pd);
            request.setAttribute("prepopTypes", s_maintainParticipationGroups.getParticipationTypes(pdf.getGroupPk()));
            request.setAttribute("prepopDetails", s_maintainParticipationGroups.getParticipationDetails(pdf.getGroupPk(),pdf.getTypePk()));            
            request.setAttribute("prepopOutcome", s_maintainParticipationGroups.getParticipationOutcomes(pdf.getGroupPk(),pdf.getTypePk(),pdf.getDetailPk()));                          
        }
        
        // get all possible participation outcomes/details/types/groups and set them in request
        // for javascript to navigate
        Collection participationCollection = s_maintainParticipationGroups.getParticipationOutcomes();
        Collection distinctGroups = s_maintainParticipationGroups.getDistinctParticipationGroups();
        //set the participation data into the request
        request.setAttribute("participationCollection", participationCollection); 
        request.setAttribute("distinctGroups", distinctGroups);        
       
        return mapping.findForward("Edit");
    }    
    
}
