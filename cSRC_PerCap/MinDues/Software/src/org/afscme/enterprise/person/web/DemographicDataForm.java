package org.afscme.enterprise.person.web;

import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import java.text.ParseException;
import org.apache.log4j.Logger;

import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.DateUtil;
/**
 * Class representing the editing of General Demographic Information.
 *
 * @struts:form name="demographicDataForm"
 */
public class DemographicDataForm extends org.apache.struts.action.ActionForm
{
    
    private static Logger logger =  Logger.getLogger(DemographicDataForm.class); 
    
    private String dob;
    private String deceasedDt;
    private Integer genderCodePK;
    private Integer ethnicOriginCodePK;
    private Integer citizenshipCodePK;
    private Boolean deceasedFg;

    /**
     * Array of Integer objects.
     */
    private Integer[] disabilityCodePKs;

    /**
     * Array of Integer objects.
     */
    private Integer[] disabilityAccommodationCodePKs;
    private Integer religionCodePK;
    private Integer maritalStatusCodePK;

    /**
     * Array of Integer objects.
     */
    private Integer[] otherLanguageCodePKs;
    private Integer primaryLanguageCodePK;

    private String[] childrenFirstNames;
    private String[] childrenMiddleNames;
    private String[] childrenLastNames;
    private Integer[] childrenSuffixNames;
    private String[] childrenBirthDates;

    private String partnerFirstName;
	private String partnerMiddleName;
	private String partnerLastName;
	private Integer partnerSuffixName;
    private String partnerBirthDate;
    private Integer[] childrenPks;
    private Integer partnerPk;

    /** Getter for property citizenshipCodePK.
     * @return Value of property citizenshipCodePK.
     *
     */
    public java.lang.Integer getCitizenshipCodePK() {
        return citizenshipCodePK;
    }

    /** Setter for property citizenshipCodePK.
     * @param citizenshipCodePK New value of property citizenshipCodePK.
     *
     */
    public void setCitizenshipCodePK(java.lang.Integer citizenshipCodePK) {
        this.citizenshipCodePK = citizenshipCodePK;
    }

    /** Getter for property deceasedDt.
     * @return Value of property deceasedDt.
     *
     */
    public String getDeceasedDt() {
        return deceasedDt;
    }

    /** Setter for property deceasedDt.
     * @param deceasedDt New value of property deceasedDt.
     *
     */
    public void setDeceasedDt(String deceasedDt) {
        this.deceasedDt = deceasedDt;
    }

    /** Getter for property disabilityAccommodationCodePKs.
     * @return Value of property disabilityAccommodationCodePKs.
     *
     */
    public Integer[] getDisabilityAccommodationCodePKs() {
        return disabilityAccommodationCodePKs;
    }

    /** Setter for property disabilityAccommodationCodePKs.
     * @param disabilityAccommodationCodePKs New value of property disabilityAccommodationCodePKs.
     *
     */
    public void setDisabilityAccommodationCodePKs(Integer[] disabilityAccommodationCodePKs) {
        this.disabilityAccommodationCodePKs = disabilityAccommodationCodePKs;
    }

    /** Getter for property disabilityCodePKs.
     * @return Value of property disabilityCodePKs.
     *
     */
    public Integer[] getDisabilityCodePKs() {
        return disabilityCodePKs;
    }

    /** Setter for property disabilityCodePKs.
     * @param disabilityCodePKs New value of property disabilityCodePKs.
     *
     */
    public void setDisabilityCodePKs(Integer[] disabilityCodePKs) {
        this.disabilityCodePKs = disabilityCodePKs;
    }

    /** Getter for property dob.
     * @return Value of property dob.
     *
     */
    public String getDob() {
        return dob;
    }

    /** Setter for property dob.
     * @param dob New value of property dob.
     *
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /** Getter for property ethnicOriginCodePK.
     * @return Value of property ethnicOriginCodePK.
     *
     */
    public java.lang.Integer getEthnicOriginCodePK() {
        return ethnicOriginCodePK;
    }

    /** Setter for property ethnicOriginCodePK.
     * @param ethnicOriginCodePK New value of property ethnicOriginCodePK.
     *
     */
    public void setEthnicOriginCodePK(java.lang.Integer ethnicOriginCodePK) {
        this.ethnicOriginCodePK = ethnicOriginCodePK;
    }

    /** Getter for property genderCodePK.
     * @return Value of property genderCodePK.
     *
     */
    public java.lang.Integer getGenderCodePK() {
        return genderCodePK;
    }

    /** Setter for property genderCodePK.
     * @param genderCodePK New value of property genderCodePK.
     *
     */
    public void setGenderCodePK(java.lang.Integer genderCodePK) {
        this.genderCodePK = genderCodePK;
    }

    /** Getter for property maritalStatusCodePK.
     * @return Value of property maritalStatusCodePK.
     *
     */
    public java.lang.Integer getMaritalStatusCodePK() {
        return maritalStatusCodePK;
    }

    /** Setter for property maritalStatusCodePK.
     * @param maritalStatusCodePK New value of property maritalStatusCodePK.
     *
     */
    public void setMaritalStatusCodePK(java.lang.Integer maritalStatusCodePK) {
        this.maritalStatusCodePK = maritalStatusCodePK;
    }

    /** Getter for property otherLanguageCodePKs.
     * @return Value of property otherLanguageCodePKs.
     *
     */
    public Integer[] getOtherLanguageCodePKs() {
        return otherLanguageCodePKs;
    }

    /** Setter for property otherLanguageCodePKs.
     * @param otherLanguageCodePKs New value of property otherLanguageCodePKs.
     *
     */
    public void setOtherLanguageCodePKs(Integer[] otherLanguageCodePKs) {
        this.otherLanguageCodePKs = otherLanguageCodePKs;
    }

    /** Getter for property primaryLanguageCodePK.
     * @return Value of property primaryLanguageCodePK.
     *
     */
    public java.lang.Integer getPrimaryLanguageCodePK() {
        return primaryLanguageCodePK;
    }

    /** Setter for property primaryLanguageCodePK.
     * @param primaryLanguageCodePK New value of property primaryLanguageCodePK.
     *
     */
    public void setPrimaryLanguageCodePK(java.lang.Integer primaryLanguageCodePK) {
        this.primaryLanguageCodePK = primaryLanguageCodePK;
    }

    /** Getter for property religionCodePK.
     * @return Value of property religionCodePK.
     *
     */
    public java.lang.Integer getReligionCodePK() {
        return religionCodePK;
    }

    /** Setter for property religionCodePK.
     * @param religionCodePK New value of property religionCodePK.
     *
     */
    public void setReligionCodePK(java.lang.Integer religionCodePK) {
        this.religionCodePK = religionCodePK;
    }

    /** Getter for property childrenFirstNames.
	 * @return Value of property childrenFirstNames.
	 *
     */
    public String[] getChildrenFirstNames() {
		return childrenFirstNames;
	}

    /** Setter for property childrenFirstNames.
	 * @param childrenFirstNames New value of property childrenFirstNames.
	 *
     */
    public void setChildrenFirstNames(String[] childrenFirstNames) {
		this.childrenFirstNames = childrenFirstNames;
	}

	/** Getter for property childrenMiddleNames.
	 * @return Value of property childrenMiddleNames.
	 *
     */
	public String[] getChildrenMiddleNames() {
		return childrenMiddleNames;
	}

	/** Setter for property childrenMiddleNames.
	 * @param childrenMiddleNames New value of property childrenMiddleNames.
	 *
     */
	public void setChildrenMiddleNames(String[] childrenMiddleNames) {
		this.childrenMiddleNames = childrenMiddleNames;
	}

	/** Getter for property childrenLastNames.
	 * @return Value of property childrenLastNames.
	 *
     */
	public String[] getChildrenLastNames() {
		return childrenLastNames;
	}

	/** Setter for property childrenLastNames.
     * @param childrenLastNames New value of property childrenLastNames.
	 *
     */
	public void setChildrenLastNames(String[] childrenLastNames) {
		this.childrenLastNames = childrenLastNames;
	}

	/** Getter for property childrenSuffixNames.
	 * @return Value of property childrenSuffixNames.
	 *
     */
	public Integer[] getChildrenSuffixNames() {
		return childrenSuffixNames;
	}

	/** Setter for property childrenSuffixNames.
	 * @param childrenSuffixNames New value of property childrenSuffixNames.
	 *
     */
	public void setChildrenSuffixNames(Integer[] childrenSuffixNames) {
		this.childrenSuffixNames = childrenSuffixNames;
	}

	/** Setter for property childrenBirthDates.
	 * @param childrenBirthDates New value of property childrenBirthDates.
	 *
     */
	public void setChildrenBirthDates(String[] childrenBirthDates) {
		this.childrenBirthDates = childrenBirthDates;
	}

	/** Getter for property childrenBirthDates.
	 * @return Value of property childrenBirthDates.
	 *
     */
	public String[] getChildrenBirthDates() {
		return childrenBirthDates;
	}

	/** Getter for property partnerFirstName.
	 * @return Value of property partnerFirstName.
	 *
     */
	public String getPartnerFirstName() {
		return partnerFirstName;
	}

	/** Setter for property partnerFirstName.
	 * @param partnerFirstName New value of property partnerFirstName.
	 *
     */
  	public void setPartnerFirstName(String partnerFirstName) {
		this.partnerFirstName = partnerFirstName;
	}

	/** Getter for property partnerMiddleName.
	 * @return Value of property partnerMiddleName.
	 *
     */
	public String getPartnerMiddleName() {
		return partnerMiddleName;
	}

	/** Setter for property partnerMiddleName.
	 * @param partnerMiddleName New value of property partnerMiddleName.
	 *
     */
	public void setPartnerMiddleName(String partnerMiddleName) {
		this.partnerMiddleName = partnerMiddleName;
	}

	/** Getter for property partnerLastName.
	 * @return Value of property partnerLastName.
	 *
     */
	public String getPartnerLastName() {
		return partnerLastName;
	}

	/** Setter for property partnerLastName.
	 * @param partnerLastName New value of property partnerLastName.
	 *
     */
	public void setPartnerLastName(String partnerLastName) {
		this.partnerLastName = partnerLastName;
	}

	/** Getter for property partnerSuffixName.
	 * @return Value of property partnerSuffixName.
	 *
     */
	public Integer getPartnerSuffixName() {
		return partnerSuffixName;
	}

	/** Setter for property partnerSuffixName.
	 * @param partnerSuffixName New value of property partnerSuffixName.
	 *
     */
	public void setPartnerSuffixName(Integer partnerSuffixName) {
		this.partnerSuffixName = partnerSuffixName;
	}

	/** Getter for property partnerPk.
	 * @return Value of property partnerPk.
	 *
     */
	public Integer getPartnerPk() {
		return partnerPk;
	}

	/** Setter for property partnerPk.
	 * @param partnerPk New value of property partnerPk.
	 *
     */
	public void setPartnerPk(Integer partnerPk) {
		this.partnerPk = partnerPk;
	}

	/** Getter for property childrenPks.
	 * @return Value of property childrenPks.
	 *
     */
	public Integer[] getChildrenPks() {
		return childrenPks;
	}

	/** Setter for property childrenPks.
	 * @param childrenPks New value of property childrenPks.
	 *
     */
	public void setChildrenPks(Integer[] childrenPks) {
		this.childrenPks = childrenPks;
	}

	/** Getter for property deceasedFg.
     * @return Value of property deceasedFg.
     *
     */
    public Boolean getDeceasedFg() {
        return deceasedFg;
    }

    /** Setter for property deceasedFg.
     * @param deceasedFg New value of property deceasedFg.
     *
     */
    public void setDeceasedFg(Boolean deceasedFg) {
        this.deceasedFg = deceasedFg;
    }

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// if the first name or last name is not entered for a partner and the user is not deleting,
		// show error
		if(getPartnerFirstName() != null && getPartnerFirstName().equals("") && !partnerFieldsEmpty()) {
			errors.add("", new ActionError("error.field.required.generic", "First Name and Last Name"));
			request.setAttribute("origin", request.getParameter("origin"));
			return errors;
		}
		// only call nameMatch if user isn't deleting the partner
		else if(!partnerFieldsEmpty()) {
                    request.setAttribute("origin", request.getParameter("origin"));
                    this.nameMatch(errors, this.getPartnerFirstName(), "");
                }

		if(getPartnerLastName() != null && getPartnerLastName().equals("") && !partnerFieldsEmpty()) {
			errors.add("", new ActionError("error.field.required.generic", "First Name and Last Name"));
			request.setAttribute("origin", request.getParameter("origin"));
			return errors;
		}

		// if the first name or last name is not entered for a child, show error
		if(childrenFirstNames != null) {
			for(int i = 0; i < childrenFirstNames.length; i++) {
				if(TextUtil.isEmptyOrSpaces(childrenFirstNames[i])) {
					errors.add("", new ActionError("error.field.required.generic", "First Name and Last Name"));
					request.setAttribute("origin", request.getParameter("origin"));
					return errors;
				}else{
                                    request.setAttribute("origin", request.getParameter("origin"));
                                    this.nameMatch(errors, childrenFirstNames[i], "");
                                }
				if(TextUtil.isEmpty(childrenLastNames[i])) {
					errors.add("", new ActionError("error.field.required.generic", "First Name and Last Name"));
					request.setAttribute("origin", request.getParameter("origin"));
					return errors;
				}
			}
		}


		// if any of the dates entered are invalid show error
		if(!isValidDate(dob, "Birth Date", errors)) {
			request.setAttribute("origin", request.getParameter("origin"));
		}

		if(!isValidDate(deceasedDt, "Deceased Date", errors)) {
			request.setAttribute("origin", request.getParameter("origin"));
		}

		if(childrenBirthDates != null) {
			for(int i = 0; i < childrenBirthDates.length; i++) {
				if(!isValidDate(childrenBirthDates[i], "Child Birth Date", errors)) {
					request.setAttribute("origin", request.getParameter("origin"));
					break;
				}
			}
		}

                // checking whether primary language and other language are the same
                if (this.otherLanguageCodePKs != null)
                {                    
                    for (int i = 0; i < this.otherLanguageCodePKs.length; i++)
                    {
                        if (this.otherLanguageCodePKs[i].intValue() == this.primaryLanguageCodePK.intValue())
                        {
                            errors.add("", new ActionError("error.person.language"));
                        }
                    }
                }                
		return errors;
	}

	private boolean isValidDate(String date, String fieldName, ActionErrors errors) {
		if(!date.equals("")) {
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(TextUtil.parseDate(date));
				if(!DateUtil.isYearInRange(calendar.get(Calendar.YEAR))) {
					errors.add("year", new ActionError("error.date.yearRange", String.valueOf(DateUtil.MIN_YEAR),
													   String.valueOf(DateUtil.MAX_YEAR), fieldName));
					return false;
				}
			} catch(ParseException e) {
				errors.add("", new ActionError("error.field.mustBeDate.generic", fieldName));
				return false;
			}
		}
		return true;
	}

	private boolean partnerFieldsEmpty() {
		// if the fields are empty and there is a partner record, the user is deleting
		if(partnerFirstName.equals("") && partnerMiddleName.equals("") && partnerLastName.equals("")
		   && partnerSuffixName.intValue() == 0) {
			return true;
		}
		return false;
	}

    /* JZhang
     * @parm errors: ActionErrors object
     * @parm name: value that need to be verified
     * @parm prop: name on jsp
     */
    private void nameMatch(ActionErrors errors, String name, String prop)
    {
        try
        {
            boolean match = Pattern.matches("([a-z A-Z]{1}[a-z A-Z 0-9]{0,24})", name);
            if (match == false ){
                 logger.debug("DemographicDataForm:nameMatch -- An error is added.");
                errors.add(prop, new ActionError("error.field.incorrect.name", "First Name"));
            }
        }catch (PatternSyntaxException pse)
        {
             logger.debug("DemographicDataForm:Pattern syntax exception");
             logger.debug(pse.getDescription());
        }
    }
}
