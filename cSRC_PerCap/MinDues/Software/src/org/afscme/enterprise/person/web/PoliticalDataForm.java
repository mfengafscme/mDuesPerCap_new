
package org.afscme.enterprise.person.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents the form when the user is viewing or editing political data
 *
 * @struts:form name="politicalDataForm"
 */
public class PoliticalDataForm extends ActionForm
{
    private Boolean politicalObjectorFg = new Boolean(false);
    private Boolean politicalDoNotCallFg = new Boolean(false);
    private Boolean politicalObjectorEnabled = new Boolean(true);
    private Boolean politicalDoNotCallEnabled = new Boolean(true);

    /** Getter for property politicalDoNotCallFg.
     * @return Value of property politicalDoNotCallFg.
     *
     */
    public java.lang.Boolean getPoliticalDoNotCallFg() {
        return politicalDoNotCallFg;
    }

    /** Setter for property politicalDoNotCallFg.
     * @param politicalDoNotCallFg New value of property politicalDoNotCallFg.
     *
     */
    public void setPoliticalDoNotCallFg(java.lang.Boolean politicalDoNotCallFg) {
        this.politicalDoNotCallFg = politicalDoNotCallFg;
    }

    /** Getter for property politicalObjectorFg.
     * @return Value of property politicalObjectorFg.
     *
     */
    public java.lang.Boolean getPoliticalObjectorFg() {
        return politicalObjectorFg;
    }

    /** Setter for property politicalObjectorFg.
     * @param politicalObjectorFg New value of property politicalObjectorFg.
     *
     */
    public void setPoliticalObjectorFg(java.lang.Boolean politicalObjectorFg) {
        this.politicalObjectorFg = politicalObjectorFg;
    }

    /** Getter for property politicalObjectorEnabled.
	 * @return Value of property politicalObjectorEnabled.
	 *
	 */
	public java.lang.Boolean getPoliticalObjectorEnabled() {
		return politicalObjectorEnabled;
	}

	/** Setter for property politicalObjectorEnabled.
	 * @param politicalObjectorEnabled New value of property politicalObjectorEnabled.
	 *
	 */
	public void setPoliticalObjectorEnabled(java.lang.Boolean politicalObjectorEnabled) {
		this.politicalObjectorEnabled = politicalObjectorEnabled;
    }

    /** Getter for property politicalDoNotCallEnabled.
     * @return Value of property politicalDoNotCallEnabled.
	 *
	 */
	public java.lang.Boolean getPoliticalDoNotCallEnabled() {
		return politicalDoNotCallEnabled;
	}

	/** Setter for property politicalDoNotCallEnabled.
	 * @param politicalDoNotCallEnabled New value of property politicalDoNotCallEnabled.
	 *
	 */
	public void setPoliticalDoNotCallEnabled(java.lang.Boolean politicalDoNotCallEnabled) {
		this.politicalDoNotCallEnabled = politicalDoNotCallEnabled;
    }
}

