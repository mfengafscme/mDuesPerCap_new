package org.afscme.enterprise.reporting.specialized.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import java.text.ParseException;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:form name="membershipBatchUpdateForm"
 */
public class MembershipBatchUpdateForm extends ActionForm {

    /** Holds value of property queuePk. */
    private Integer queuePk;

    /**
     * Getter for property queuePk.
     * @return Value of property queuePk.
	 *
	 */
	public Integer getQueuePk() {
		return this.queuePk;
	}

	/**
     * Setter for property queuePk.
	 * @param queuePk New value of property queuePk.
     *
	 */
	public void setQueuePk(Integer queuePk) {
		this.queuePk = queuePk;
    }
}
