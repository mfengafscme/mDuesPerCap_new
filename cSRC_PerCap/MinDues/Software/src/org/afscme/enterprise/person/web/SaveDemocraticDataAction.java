
package org.afscme.enterprise.person.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

// Java imports
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.DemographicData;
import org.afscme.enterprise.person.RelationData;
import org.afscme.enterprise.util.DateUtil;

/**
 * This action is to save General Demographic Information.
 *
 * @struts:action   path="/saveDemographicData"
 *                  scope="request"
 *					name="demographicDataForm"
 *					validate="true"
 *					input="/Membership/GeneralDemographicInformationEdit.jsp"
 *
 * @struts:action-forward   name="View"  path="/viewDemographicData.action"
 */
public class SaveDemocraticDataAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		// populate data object with values entered in form
		DemographicDataForm demographicDataForm = (DemographicDataForm)form;
		DemographicData demographicData = new DemographicData();
		demographicData.setDob(DateUtil.getTimestamp(demographicDataForm.getDob()));
		demographicData.setDeceasedDt(DateUtil.getTimestamp(demographicDataForm.getDeceasedDt()));

		// if the checkbox wasn't checked
		if(demographicDataForm.getDeceasedFg() == null) {
			demographicData.setDeceasedFg(new Boolean(false));
		}
		else {
			demographicData.setDeceasedFg(demographicDataForm.getDeceasedFg());
		}

		// if the user did not select an option with a blank value
		if(demographicDataForm.getGenderCodePK().intValue() != 0) {
			demographicData.setGenderCodePK(demographicDataForm.getGenderCodePK());
		}

		// if the user did not select an option with a blank value
		if(demographicDataForm.getEthnicOriginCodePK().intValue() != 0) {
			demographicData.setEthnicOriginCodePK(demographicDataForm.getEthnicOriginCodePK());
		}
		demographicData.setCitizenshipCodePK(demographicDataForm.getCitizenshipCodePK());

		// if the user did not select an option with a blank value
		if(demographicDataForm.getMaritalStatusCodePK().intValue() != 0) {
			demographicData.setMaritalStatusCodePK(demographicDataForm.getMaritalStatusCodePK());
		}
		demographicData.setPrimaryLanguageCodePK(demographicDataForm.getPrimaryLanguageCodePK());

		// if the user did not select an option with a blank value
		if(demographicDataForm.getReligionCodePK().intValue() != 0) {
			demographicData.setReligionCodePK(demographicDataForm.getReligionCodePK());
		}

		ArrayList disabilityList = new ArrayList();
		Integer[] disabilityCodePKs = demographicDataForm.getDisabilityCodePKs();
		// if the user selectected a disability
		if(disabilityCodePKs != null) {
			for(int i = 0; i < disabilityCodePKs.length; i++) {
				if(disabilityCodePKs[i].intValue() != 0) {
					disabilityList.add(disabilityCodePKs[i]);
				}
			}
		}
		demographicData.setDisabilityCodePKs(disabilityList);

		ArrayList disabilityAccommodationList = new ArrayList();
		Integer[] disabilityAccommodationCodePKs = demographicDataForm.getDisabilityAccommodationCodePKs();
		// if the user selectected an accomodation
		if(disabilityAccommodationCodePKs != null) {
			for(int i = 0; i < disabilityAccommodationCodePKs.length; i++) {
				if(disabilityAccommodationCodePKs[i].intValue() != 0) {
					disabilityAccommodationList.add(disabilityAccommodationCodePKs[i]);
				}
			}
		}
		demographicData.setDisabilityAccommodationCodePKs(disabilityAccommodationList);

		ArrayList otherLanguageList = new ArrayList();
		Integer[] otherLanguageCodePKs = demographicDataForm.getOtherLanguageCodePKs();
		// if the user selectected a secondary language
		if(otherLanguageCodePKs != null) {
			for(int i = 0; i < otherLanguageCodePKs.length; i++) {
				if(otherLanguageCodePKs[i].intValue() != 0) {
					otherLanguageList.add(otherLanguageCodePKs[i]);
				}
			}
		}
		demographicData.setOtherLanguageCodePKs(otherLanguageList);

		// set partner info
		RelationData partnerData = new RelationData();
		partnerData.setRelativeFirstNm(demographicDataForm.getPartnerFirstName());
		partnerData.setRelativeMiddleNm(demographicDataForm.getPartnerMiddleName());
		partnerData.setRelativeLastNm(demographicDataForm.getPartnerLastName());
		if(demographicDataForm.getPartnerSuffixName().intValue() != 0) {
			partnerData.setRelativeSuffixNm(demographicDataForm.getPartnerSuffixName());
		}
		partnerData.setRelativePk(demographicDataForm.getPartnerPk());
		demographicData.setThePartnerRelationData(partnerData);

		// set children info
		String[] childrenFirstNames = demographicDataForm.getChildrenFirstNames();
		String[] childrenMiddleNames = demographicDataForm.getChildrenMiddleNames();
		String[] childrenLastNames = demographicDataForm.getChildrenLastNames();
		Integer[] childrenSuffixNames = demographicDataForm.getChildrenSuffixNames();
		String[] childrenBirthDates = demographicDataForm.getChildrenBirthDates();
		Integer[] childrenPks = demographicDataForm.getChildrenPks();

		ArrayList childrenRelationData = new ArrayList();
		if(childrenFirstNames != null) {
			for(int i = 0; i < childrenFirstNames.length; i++) {
				RelationData rd = new RelationData();
				rd.setRelativeFirstNm(childrenFirstNames[i]);
				rd.setRelativeMiddleNm(childrenMiddleNames[i]);
				rd.setRelativeLastNm(childrenLastNames[i]);
				if(childrenSuffixNames[i].intValue() != 0) {
					rd.setRelativeSuffixNm(childrenSuffixNames[i]);
				}
				rd.setRelativeBirthDt(DateUtil.getTimestamp(childrenBirthDates[i]));
				rd.setRelativePk(childrenPks[i]);
				childrenRelationData.add(rd);
			}
		}
		demographicData.setTheChildrenRelationData((RelationData[])childrenRelationData.toArray(new RelationData[childrenRelationData.size()]));

		// update General Demographic Information
		s_maintainPersons.updateGeneralDemographics(getCurrentPersonPk(request), usd.getPersonPk(), demographicData);

		request.setAttribute("origin", request.getParameter("origin"));
		return mapping.findForward("View");
    }
}
