package org.afscme.enterprise.affiliate.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.ConstitutionData;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.PreparedUpdateStatementBuilder;

/** 
 * @struts:action   path="/saveConstitutionInformation"
 *                  name="constitutionForm"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Membership/ConstitutionInformationEdit.jsp"
 */
public class SaveConstitutionInformationAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    /** Creates a new instance of SaveConstitutionInformationAction */
    public SaveConstitutionInformationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        ConstitutionForm cf = (ConstitutionForm)form;
        
        ActionErrors errors = cf.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        
        ConstitutionData data = cf.getConstitutionData();
        log.debug("Form: " + cf);
        log.debug("Data: " + data);
        //if (!s_maintainAffiliates.updateConstitutionData(data, usd.getPersonPk())) {
        if (!updateConstitutionData(cf, usd.getPersonPk())) {
            throw new JspException("Constitution could not be updated.");
        }
        return mapping.findForward("ViewConstitution");
    }
    
    private boolean updateConstitutionData(ConstitutionForm form, Integer userPk) throws JspException {
        if (form == null || form.getAffPk() == null || userPk == null) {
            throw new JspException("Parameters were not valid.");
        }
        ConstitutionData oldData = s_maintainAffiliates.getConstitutionData(form.getAffPk());
        Connection con = null;
        PreparedStatement ps = null;
        PreparedUpdateStatementBuilder builder = new PreparedUpdateStatementBuilder(8);
        String SQL_UPDATE_CONSTITUTION_DATA = 
            "UPDATE Aff_Constitution " + 
            "SET    most_current_approval_dt=?, approved_const_fg=?, " + 
            "       aff_agreement_dt=?, off_election_method=?, const_regions_fg=?, " + 
            "       meeting_frequency=?, lst_mod_dt=getDate(), lst_mod_user_pk=?";
        try {
            con = DBUtil.getConnection();
            if (form.getConstitutionDocument() != null) {
                builder.addUpdateField("aff_constitution_doc", form.getConstitutionDocument().getInputStream(), form.getConstitutionDocument().getFileSize());
                builder.addUpdateField("const_doc_file_content_type", form.getConstitutionDocument().getContentType());
                builder.addUpdateField("const_doc_file_nm", form.getConstitutionDocument().getFileName());
            }
            builder.addCriterion("aff_pk", form.getAffPk());
            ps = builder.getPreparedStatement(SQL_UPDATE_CONSTITUTION_DATA, con, false, true);
            DBUtil.setNullableTimestamp(ps, 1, DateUtil.getTimestamp(form.getMostCurrentApprovalDate()));
            if (DateUtil.getTimestamp(form.getMostCurrentApprovalDate()) != null)
                form.setApprovedConstitution(true);
            DBUtil.setBooleanAsShort(ps, 2, form.isApprovedConstitution());
            DBUtil.setNullableTimestamp(ps, 3, DateUtil.getTimestamp(form.getAffiliationAgreementDate()));
            DBUtil.setNullableInt(ps, 4, form.getMethodOfOfficerElection());
            DBUtil.setBooleanAsShort(ps, 5, form.isConstitutionalRegions());
            DBUtil.setNullableInt(ps, 6, form.getMeetingFrequency());
            ps.setInt(7, userPk.intValue());
            int update = ps.executeUpdate();
            if (update != 1) {
                /** @TODO: May want to change this to an error code. */
                return false;
            }
            ConstitutionData newData = form.getConstitutionData();
            s_maintainAffiliates.recordChangeToHistory(oldData, newData, userPk, con);
        } catch (Exception e) {
            throw new JspException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return true;
    }
    
}
