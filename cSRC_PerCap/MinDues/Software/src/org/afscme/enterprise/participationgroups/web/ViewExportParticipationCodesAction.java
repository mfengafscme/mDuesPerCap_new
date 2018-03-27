package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;


/**
 * @struts:action   path="/viewExportParticipationCodes"
 *                  name="exportParticipationCodesForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="View"  path="/Membership/ExportParticipationCodes.jsp"
 */
public class ViewExportParticipationCodesAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response, UserSecurityData usd) throws Exception {
                
        String groupPk = request.getParameter("groupPk");
        String typePk = request.getParameter("typePk");
        String detailPk = request.getParameter("detailPk");
        if (groupPk == null || typePk == null) {
            throw new JspException("Participation Group/Type Primary Key is not specified in the request.");
        }
        
        ExportParticipationCodesForm epcForm = (ExportParticipationCodesForm)form;
        epcForm.setGroupPk(new Integer(groupPk));
        epcForm.setTypePk(new Integer(typePk));
        if (detailPk == null)
            epcForm.setDetailPk(null);
        else
            epcForm.setDetailPk(new Integer(detailPk));
        return mapping.findForward("View");
    }
}
