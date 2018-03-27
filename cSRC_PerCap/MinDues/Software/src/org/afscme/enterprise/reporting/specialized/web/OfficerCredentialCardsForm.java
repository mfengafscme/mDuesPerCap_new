package org.afscme.enterprise.reporting.specialized.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import java.text.ParseException;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:form name="officerCredentialCardsForm"
 */
public class OfficerCredentialCardsForm extends ActionForm {

    /** Holds value of property fromDate. */
    private String fromDate;

    /** Holds value of property fromDate. */
    private String toDate;

    /** Getter for property fromDate.
     * @return Value of property fromDate.
     *
     */
    public java.lang.String getFromDate() {
        return fromDate;
    }

    /** Setter for property fromDate.
     * @param fromDate New value of property fromDate.
     *
     */
    public void setFromDate(java.lang.String fromDate) {
        this.fromDate = fromDate;
    }

    /** Getter for property toDate.
	 * @return Value of property toDate.
	 *
	 */
	public java.lang.String getToDate() {
		return toDate;
	}

	/** Setter for property toDate.
	 * @param toDate New value of property toDate.
	 *
	 */
	public void setToDate(java.lang.String toDate) {
		this.toDate = toDate;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    	ActionErrors errors = new ActionErrors();
    	String fromParsedDate = null;
    	String toParsedDate = null;

    	if (TextUtil.isEmptyOrSpaces(getFromDate())) {
	    	errors.add("fromDate", new ActionError("error.field.required.generic", "From Date"));
	    }
	    else {
	    	try {
	        	fromParsedDate = TextUtil.parseDate(getFromDate()).toString();
	       	} catch (ParseException pe) {
	   			errors.add("fromDate", new ActionError("error.field.mustBeDate.generic", "From Date"));
	       	}
        }
        if (TextUtil.isEmptyOrSpaces(getToDate())) {
			errors.add("toDate", new ActionError("error.field.required.generic", "To Date"));
		}
		else {
			try {
				toParsedDate = TextUtil.parseDate(getToDate()).toString();
			} catch (ParseException pe) {
				errors.add("toDate", new ActionError("error.field.mustBeDate.generic", "To Date"));
			}
		}
		// if validation succeeds, format date in a format the database will understand
		if(errors.size() == 0) {
			setFromDate(fromParsedDate);
			setToDate(toParsedDate);
		}
		return errors;
	}
}
