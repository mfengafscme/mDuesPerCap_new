package org.afscme.enterprise.minimumdues.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:form name="AddDataEntryForm"
 */
public class AddDataEntryForm extends GenericDataEntryForm {

  // --------------- Instance Variables --------------------
  private String addYear;
  private String saveNewDataEntry;

  public AddDataEntryForm() {
	super();
  }

  public ActionErrors validate(ActionMapping mapping,
                 HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();

    return errors;
  }

  /** Getter for property addYear.
   * @return Value of property addYear.
   *
   */
  public java.lang.String getAddYear() {
      return addYear;
  }

  /** Setter for property addYear.
   * @param addYear New value of property addYear.
   *
   */
  public void setAddYear(java.lang.String addYear) {
      this.addYear = addYear;
  }

  /** Getter for property saveNewDataEntry.
   * @return Value of property saveNewDataEntry.
   *
   */
  public java.lang.String getSaveNewDataEntry() {
      return saveNewDataEntry;
  }

  /** Setter for property saveNewDataEntry.
   * @param saveNewDataEntry New value of property saveNewDataEntry.
   *
   */
  public void setSaveNewDataEntry(java.lang.String saveNewDataEntry) {
      this.saveNewDataEntry = saveNewDataEntry;
  }

}
