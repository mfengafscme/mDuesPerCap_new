package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.organization.OrganizationCriteria;

/**
 * Represents the parameters form when the user is entering verify organization criteria
 *
 * @struts:form name="verifyOrganizationForm"
 */
public class VerifyOrganizationForm extends SearchForm {

    private String m_orgName;

    public VerifyOrganizationForm() {
        newSearch();
    }

    /** resets the search values to the default */
    public void newSearch() {
        m_orgName=null;
        sortBy="orgName";
        order=1;
        page=0;
        total=0;
    }

    /**
     * getOrganizationCriteriaData method to copy all the organization name
     * data field to the criteria object to process.
     */
    public OrganizationCriteria getOrganizationCriteriaData() {

        OrganizationCriteria data = new OrganizationCriteria();

        if (!TextUtil.isEmpty(m_orgName))
            data.setOrgName(m_orgName);

            data.setPage(page);
            data.setPageSize(getPageSize());

            if (sortBy.equals("orgName"))
                data.setSortField(OrganizationCriteria.FIELD_NAME);
            else if (sortBy.equals("orgType"))
                data.setSortField(OrganizationCriteria.FIELD_TYPE);

            data.setSortOrder(order);

        return data;
    }

    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("orgName: " + m_orgName);
        buf.append(", order: " + order);
        buf.append(", sortBy: " + sortBy);
        buf.append(", page: " + page);
        buf.append(", total: " + total);
        return buf.toString()+"]";
    }

    /** Getter for property orgName.
     * @return Value of property orgName.
     *
     */
    public java.lang.String getOrgName() {
        return m_orgName;
    }

    /** Setter for property orgName.
     * @param orgName New value of property orgName.
     *
     */
    public void setOrgName(java.lang.String orgName) {
        m_orgName = orgName;
    }

    /**
     * validation method for this form
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        if (m_orgName == null) {
            //new, ignore
            return null;
        }

        ActionErrors errors = new ActionErrors();
        if (m_orgName.length() == 0) {
            errors.add("orgName", new ActionError("error.field.required.generic", "Organization Name"));
            m_orgName = null;  //reset to null
        }

        return errors;
    }
}



