package org.afscme.enterprise.participationgroups.web;

import org.afscme.enterprise.common.web.SearchForm;


/**
 * Represents the form for all the Participation Groups
 *
 * @struts:form name="participationCodeListForm"
 */
public class ParticipationCodeListForm extends SearchForm {

     /**
     * constructor to set up values
     */
    public ParticipationCodeListForm() {
    }

    /**
     * toString method
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append(", order: " + order);
        buf.append(", sortBy: " + sortBy);
        buf.append(", page: " + page);
        buf.append(", total: " + total);
        return buf.toString()+"]";
    }
}



