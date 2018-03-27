
package org.afscme.enterprise.person.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.DemographicData;
import org.afscme.enterprise.person.RelationData;
import org.afscme.enterprise.util.DateUtil;

/**
 * This action is to edit General Demographic Information.
 *
 * @struts:action   path="/editDemographicData"
 * @struts:action-forward   name="Edit"  path="/Membership/GeneralDemographicInformationEdit.jsp"
 */
public class EditDemographicDataAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		Integer personPk = getCurrentPersonPk(request);

		// get info to be displayed on the edit screen
		DemographicData demographicData = s_maintainPersons.getGeneralDemographics(personPk);

		// populate form with the data retrieved from the database
		DemographicDataForm demographicDataForm = new DemographicDataForm();
		// convert dates from Timestamp to String
		if(demographicData.getDob() != null) {
			demographicDataForm.setDob(DateUtil.getSimpleDateString(demographicData.getDob()));
		}
		if(demographicData.getDeceasedDt() != null) {
			demographicDataForm.setDeceasedDt(DateUtil.getSimpleDateString(demographicData.getDeceasedDt()));
		}
		demographicDataForm.setGenderCodePK(demographicData.getGenderCodePK());
		demographicDataForm.setEthnicOriginCodePK(demographicData.getEthnicOriginCodePK());
		demographicDataForm.setCitizenshipCodePK(demographicData.getCitizenshipCodePK());
		demographicDataForm.setDisabilityCodePKs((Integer[])demographicData.getDisabilityCodePKs().toArray(new Integer[0]));
		demographicDataForm.setDisabilityAccommodationCodePKs((Integer[])demographicData.getDisabilityAccommodationCodePKs().toArray(new Integer[0]));
		demographicDataForm.setReligionCodePK(demographicData.getReligionCodePK());
		demographicDataForm.setMaritalStatusCodePK(demographicData.getMaritalStatusCodePK());
		demographicDataForm.setOtherLanguageCodePKs((Integer[])demographicData.getOtherLanguageCodePKs().toArray(new Integer[0]));
		demographicDataForm.setDeceasedFg(demographicData.getDeceasedFg());

		RelationData[] relationData = demographicData.getTheChildrenRelationData();
		RelationData rd = demographicData.getThePartnerRelationData();

		// the person might still be single
		if(rd != null) {
			// if not populate the form
			demographicDataForm.setPartnerFirstName(rd.getRelativeFirstNm());
			demographicDataForm.setPartnerMiddleName(rd.getRelativeMiddleNm());
			demographicDataForm.setPartnerLastName(rd.getRelativeLastNm());
			demographicDataForm.setPartnerSuffixName(rd.getRelativeSuffixNm());
			demographicDataForm.setPartnerPk(rd.getRelativePk());
		}

		// populate the form with children data
		ArrayList childrenFirstNames = new ArrayList();
		ArrayList childrenMiddleNames = new ArrayList();
		ArrayList childrenLastNames = new ArrayList();
		ArrayList childrenSuffixNames = new ArrayList();
		ArrayList childrenBirthDates = new ArrayList();
		ArrayList childrenPks = new ArrayList();
		for(int i = 0;i < relationData.length; i++) {
			childrenFirstNames.add(relationData[i].getRelativeFirstNm());
			childrenMiddleNames.add(relationData[i].getRelativeMiddleNm());
			childrenLastNames.add(relationData[i].getRelativeLastNm());
			childrenSuffixNames.add(relationData[i].getRelativeSuffixNm());
			childrenBirthDates.add(DateUtil.getSimpleDateString(relationData[i].getRelativeBirthDt()));
			childrenPks.add(relationData[i].getRelativePk());
		}
		demographicDataForm.setChildrenFirstNames((String[])childrenFirstNames.toArray(new String[0]));
		demographicDataForm.setChildrenMiddleNames((String[])childrenMiddleNames.toArray(new String[0]));
		demographicDataForm.setChildrenLastNames((String[])childrenLastNames.toArray(new String[0]));
		demographicDataForm.setChildrenSuffixNames((Integer[])childrenSuffixNames.toArray(new Integer[0]));
		demographicDataForm.setChildrenBirthDates((String[])childrenBirthDates.toArray(new String[0]));
		demographicDataForm.setChildrenPks((Integer[])childrenPks.toArray(new Integer[0]));

        request.setAttribute("origin", request.getParameter("origin"));
        request.setAttribute("demographicData", demographicData);
        request.setAttribute("demographicDataForm", demographicDataForm);
        return mapping.findForward("Edit");
    }
}
