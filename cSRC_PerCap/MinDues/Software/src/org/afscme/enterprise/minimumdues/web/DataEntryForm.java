package org.afscme.enterprise.minimumdues.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:form name="DataEntryForm"
 */
public class DataEntryForm extends GenericDataEntryForm {
  private String enterDataButton;
  private String addEmployerButton;
  private String activateEmployerBtn;

  public DataEntryForm() {
	super();
  }

  public ActionErrors validate(ActionMapping mapping,
                 HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    return errors;
  }

  /** Getter for property enterDataButton.
   * @return Value of property enterDataButton.
   *
   */
  public java.lang.String getEnterDataButton() {
      return enterDataButton;
  }

  /** Setter for property enterDataButton.
   * @param enterDataButton New value of property enterDataButton.
   *
   */
  public void setEnterDataButton(java.lang.String enterDataButton) {
      this.enterDataButton = enterDataButton;
  }

  /** Getter for property addEmployerButton.
   * @return Value of property addEmployerButton.
   *
   */
  public java.lang.String getAddEmployerButton() {
      return addEmployerButton;
  }

  /** Setter for property addEmployerButton.
   * @param addEmployerButton New value of property addEmployerButton.
   *
   */
  public void setAddEmployerButton(java.lang.String addEmployerButton) {
      this.addEmployerButton = addEmployerButton;
  }

  /** Getter for property activateEmployerBtn.
   * @return Value of property activateEmployerBtn.
   *
   */
  public java.lang.String getActivateEmployerBtn() {
      return activateEmployerBtn;
  }

  /** Setter for property activateEmployerBtn.
   * @param activateEmployerBtn New value of property activateEmployerBtn.
   *
   */
  public void setActivateEmployerBtn(java.lang.String activateEmployerBtn) {
      this.activateEmployerBtn = activateEmployerBtn;
  }

}
