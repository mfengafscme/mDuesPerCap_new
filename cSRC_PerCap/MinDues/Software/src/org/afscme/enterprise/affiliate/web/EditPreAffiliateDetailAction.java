package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.PreAffiliateData;
import org.afscme.enterprise.organization.LocationData;

/**
 * @struts:action   path="/editPreAffiliateDetail"
 *                  name="preAffiliateDetailForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="edit"  path="/Minimumdues/MinimumDuesPreAffiliateDetailEdit.jsp"
 */
public class EditPreAffiliateDetailAction extends AFSCMEAction {

    /** Creates a new instance of EditPreAffiliateDetailAction */
    public EditPreAffiliateDetailAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {
        PreAffiliateDetailForm adf = (PreAffiliateDetailForm)form;
		//System.out.println("**********************####################################*************" + adf.getBatch_ID());
        /*
        if (adf.getBatch_ID() == null) {
            //if (getCurrentAffiliatePk(request) == null) {
			//if (getCurrentRowId(request) == null) {
                throw new JspException("No primary key was specified.");
            //}
            //adf.setBatch_ID(getCurrentRowId(request));
        }
		*/

		adf.setBatch_ID(request.getParameter("batch_ID").toString());

		PreAffiliateData data = s_maintainAffiliates.getPreAffiliateData(adf.getBatch_ID());

        // Set form fields from AffiliateData
        // adf.setPreAffiliateData(data);
        adf.setEmpAffPk(data.getEmpAffPk());
		adf.setAffPk(data.getAffPk());
		adf.setAffIdCouncil(data.getAffIdCouncil());
		adf.setAffIdLocal(data.getAffIdLocal());
		adf.setAffIdState(data.getAffIdState());
		adf.setAffIdSubUnit(data.getAffIdSubUnit());

		adf.setEmployerName(data.getEmployerName());
		adf.setAgmtEffDate(data.getAgmtEffDate());
		adf.setAgmtExpDate(data.getAgmtExpDate());
		adf.setNoMemFeePayer(data.getNoMemFeePayer());
		adf.setIfRecInc(data.getIfRecInc());
		adf.setIfInNego(data.getIfInNego());
		adf.setPercentWageInc(data.getPercentWageInc());
		adf.setWageIncEffDate(data.getWageIncEffDate());
		adf.setNoMemFeePayerAff1(data.getNoMemFeePayerAff1());
		adf.setCentPerHrDoLumpSumBonus(data.getCentPerHrDoLumpSumBonus());
		adf.setAvgWagePerHrYr(data.getAvgWagePerHrYr());
		adf.setEffDateInc(data.getEffDateInc());
		adf.setNoMemFeePayerAff2(data.getNoMemFeePayerAff2());
		adf.setSpeWageAgj(data.getSpeWageAgj());
		adf.setPercentInc(data.getPercentInc());
		adf.setDollarCent(data.getDollarCent());
		adf.setAvgPay(data.getAvgPay());
		adf.setNoMemFeePayerAff3(data.getNoMemFeePayerAff3());
		adf.setContactName(data.getContactName());
		adf.setContactPhoneEmail(data.getContactPhoneEmail());
		//adf.setNotes(data.getNotes());

		adf.setLoad_ID(data.getLoad_ID());
		adf.setBatch_ID(data.getBatch_ID());
		adf.setProcessed(data.getProcessed());
		adf.setIncrease_type(data.getIncrease_type());
		adf.setStatMbrCount(data.getStatMbrCount());
		adf.setMbrsAfps_Affected(data.getMbrsAfps_Affected());
		adf.setAdj_MbrsAfps_Affected(data.getAdj_MbrsAfps_Affected());
		adf.setUserPosting(data.getUserPosting());
		adf.setDoNotProcess(data.getDoNotProcess());
		adf.setComment(data.getComment());
		adf.setWifPk(data.getWifPk());
		adf.setWidPk(data.getWidPk());

        // Set New Affiliate ID Info Source fields.
        /*
        if (data.getNewAffiliateIDSourcePk() != null && data.getNewAffiliateIDSourcePk().intValue() > 0) {
            AffiliateData newAffId = s_maintainAffiliates.getAffiliateData(data.getNewAffiliateIDSourcePk());
            if (newAffId != null) {
                adf.setNewAffIdData(newAffId);
            }
        }
		*/

        return mapping.findForward("edit");
    }

}
