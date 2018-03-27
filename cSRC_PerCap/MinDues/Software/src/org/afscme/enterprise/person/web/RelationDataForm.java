package org.afscme.enterprise.person.web;


import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.Calendar;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.DateUtil;
import org.apache.log4j.Logger;

/**
 * Represents a form to input relation data.
 * @struts:form name="relationDataForm"
 */
public class RelationDataForm extends ActionForm
{
    
    private static Logger logger =  Logger.getLogger(RelationDataForm.class);     
    
    private String relativeFirstNm;
    private String relativeMiddleNm;
    private String relativeLastNm;
    private Integer relativeSuffixNm;
    private String relativeBirthDt;
    private Integer relativePk;

    /** Getter for property relativeBirthDt.
     * @return Value of property relativeBirthDt.
     *
     */
    public String getRelativeBirthDt() {
        return relativeBirthDt;
    }

    /** Setter for property relativeBirthDt.
     * @param relativeBirthDt New value of property relativeBirthDt.
     *
     */
    public void setRelativeBirthDt(String relativeBirthDt) {
        this.relativeBirthDt = relativeBirthDt;
    }

    /** Getter for property relativeFirstNm.
     * @return Value of property relativeFirstNm.
     *
     */
    public java.lang.String getRelativeFirstNm() {
        return relativeFirstNm;
    }

    /** Setter for property relativeFirstNm.
     * @param relativeFirstNm New value of property relativeFirstNm.
     *
     */
    public void setRelativeFirstNm(java.lang.String relativeFirstNm) {
        this.relativeFirstNm = relativeFirstNm;
    }

    /** Getter for property relativeLastNm.
     * @return Value of property relativeLastNm.
     *
     */
    public java.lang.String getRelativeLastNm() {
        return relativeLastNm;
    }

    /** Setter for property relativeLastNm.
     * @param relativeLastNm New value of property relativeLastNm.
     *
     */
    public void setRelativeLastNm(java.lang.String relativeLastNm) {
        this.relativeLastNm = relativeLastNm;
    }

    /** Getter for property relativeMiddleNm.
     * @return Value of property relativeMiddleNm.
     *
     */
    public java.lang.String getRelativeMiddleNm() {
        return relativeMiddleNm;
    }

    /** Setter for property relativeMiddleNm.
     * @param relativeMiddleNm New value of property relativeMiddleNm.
     *
     */
    public void setRelativeMiddleNm(java.lang.String relativeMiddleNm) {
        this.relativeMiddleNm = relativeMiddleNm;
    }

    /** Getter for property relativeSuffixNm.
     * @return Value of property relativeSuffixNm.
     *
     */
    public Integer getRelativeSuffixNm() {
        return relativeSuffixNm;
    }

    /** Setter for property relativeSuffixNm.
     * @param relativeSuffixNm New value of property relativeSuffixNm.
     *
     */
    public void setRelativeSuffixNm(Integer relativeSuffixNm) {
        this.relativeSuffixNm = relativeSuffixNm;
    }

    /** Getter for property relativePk.
	 * @return Value of property relativePk.
	 *
	 */
	public java.lang.Integer getRelativePk() {
		return relativePk;
	}

    /** Setter for property relativePk.
	 * @param relativePk New value of property relativePk.
	 *
	 */
	public void setRelativePk(java.lang.Integer relativePk) {
		this.relativePk = relativePk;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// if the first name or last name is not entered show error
                if (TextUtil.isEmptyOrSpaces(this.getRelativeFirstNm())) {

			errors.add("firstName", new ActionError("error.field.required.generic", "First Name"));
		}else{
                    this.nameMatch(errors, this.getRelativeFirstNm(), "firstName");
                }

                if (TextUtil.isEmptyOrSpaces(this.getRelativeLastNm())) {

			errors.add("lastName", new ActionError("error.field.required.generic", "Last Name"));
		}

		// if invalid date is entered show error
		if(!getRelativeBirthDt().equals("")) {
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(TextUtil.parseDate(getRelativeBirthDt()));
                                DateUtil.getSimpleDateString(DateUtil.getTimestamp(getRelativeBirthDt()));
				if(!DateUtil.isYearInRange(calendar.get(Calendar.YEAR))) {
					errors.add("year", new ActionError("error.date.yearRange", String.valueOf(DateUtil.MIN_YEAR),
												   String.valueOf(DateUtil.MAX_YEAR), ""));
				}else
                                {
                                    this.setRelativeBirthDt(DateUtil.getSimpleDateString(DateUtil.getTimestamp(getRelativeBirthDt())));
                                }
			} catch(ParseException e) {
				errors.add("birthDate", new ActionError("error.field.mustBeDate.generic", "Birth Date"));
			}
		}

		// set origin if validation did not succeed
		if(errors.size() > 0) {
			request.setAttribute("origin", request.getParameter("origin"));
		}
		return errors;
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
                logger.debug("RelationDataForm:nameMatch -- An error is added.");
                errors.add(prop, new ActionError("error.field.incorrect.name", "First Name"));
            }
        } catch (PatternSyntaxException pse) {
            logger.debug("RelationDataForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }
    }
}

