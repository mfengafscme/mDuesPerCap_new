package org.afscme.enterprise.minimumdues.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:form name="EditDataEntryForm"
 */
public class EditDataEntryForm extends GenericDataEntryForm {
  // --------------- Instance Variables --------------------
  private String saveEditDataButton;
  private String cancelEditDataButton;
  //private String h_statAverage;
  private String h_formCompleted;
  private String h_correspondence;
  private String h_inNegotiations;
  private String h_agreementReceived;

  public EditDataEntryForm() {
	super();
  }

  public ActionErrors validate(ActionMapping mapping,
                 HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    return errors;
  }

  /** Getter for property saveEditDataButton.
   * @return Value of property saveEditDataButton.
   *
   */
  public java.lang.String getSaveEditDataButton() {
      return saveEditDataButton;
  }

  /** Setter for property saveEditDataButton.
   * @param saveEditDataButton New value of property saveEditDataButton.
   *
   */
  public void setSaveEditDataButton(java.lang.String saveEditDataButton) {
      this.saveEditDataButton = saveEditDataButton;
  }

  /** Getter for property cancelEditDataButton.
   * @return Value of property cancelEditDataButton.
   *
   */
  public java.lang.String getCancelEditDataButton() {
      return cancelEditDataButton;
  }

  /** Setter for property cancelEditDataButton.
   * @param cancelEditDataButton New value of property cancelEditDataButton.
   *
   */
  public void setCancelEditDataButton(java.lang.String cancelEditDataButton) {
      this.cancelEditDataButton = cancelEditDataButton;
  }

  /** Getter for property h_formCompleted.
   * @return Value of property h_formCompleted.
   *
   */
  public java.lang.String getH_formCompleted() {
      return h_formCompleted;
  }

  /** Setter for property h_formCompleted.
   * @param h_formCompleted New value of property h_formCompleted.
   *
   */
  public void setH_formCompleted(java.lang.String h_formCompleted) {
      this.h_formCompleted = h_formCompleted;
  }

  /** Getter for property h_correspondence.
   * @return Value of property h_correspondence.
   *
   */
  public java.lang.String getH_correspondence() {
      return h_correspondence;
  }

  /** Setter for property h_correspondence.
   * @param h_correspondence New value of property h_correspondence.
   *
   */
  public void setH_correspondence(java.lang.String h_correspondence) {
      this.h_correspondence = h_correspondence;
  }

  /** Getter for property h_inNegotiations.
   * @return Value of property h_inNegotiations.
   *
   */
  public java.lang.String getH_inNegotiations() {
      return h_inNegotiations;
  }

  /** Setter for property h_inNegotiations.
   * @param h_inNegotiations New value of property h_inNegotiations.
   *
   */
  public void setH_inNegotiations(java.lang.String h_inNegotiations) {
      this.h_inNegotiations = h_inNegotiations;
  }

  /** Getter for property h_agreementReceived.
   * @return Value of property h_agreementReceived.
   *
   */
  public java.lang.String getH_agreementReceived() {
      return h_agreementReceived;
  }

  /** Setter for property h_agreementReceived.
   * @param h_agreementReceived New value of property h_agreementReceived.
   *
   */
  public void setH_agreementReceived(java.lang.String h_agreementReceived) {
      this.h_agreementReceived = h_agreementReceived;
  }

}
