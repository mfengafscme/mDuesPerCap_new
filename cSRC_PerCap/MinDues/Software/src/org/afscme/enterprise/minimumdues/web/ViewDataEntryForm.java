package org.afscme.enterprise.minimumdues.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:form name="ViewDataEntryForm"
 */
public class ViewDataEntryForm extends GenericDataEntryForm {
  // --------------- Instance Variables --------------------
  private String editButton;
  private String deleteButton;
  private String h_amountType;
  private String h_correspondence;
  private String h_formCompleted;
  private String h_inNegotiations;
  private String empEditable;
  private String h_agreementReceived;

  public ViewDataEntryForm() {
	super();
  }

  public ActionErrors validate(ActionMapping mapping,
                 HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    return errors;
  }

  /** Getter for property editButton.
   * @return Value of property editButton.
   *
   */
  public java.lang.String getEditButton() {
      return editButton;
  }

  /** Setter for property editButton.
   * @param editButton New value of property editButton.
   *
   */
  public void setEditButton(java.lang.String editButton) {
      this.editButton = editButton;
  }

  /** Getter for property deleteButton.
   * @return Value of property deleteButton.
   *
   */
  public java.lang.String getDeleteButton() {
      return deleteButton;
  }

  /** Setter for property deleteButton.
   * @param deleteButton New value of property deleteButton.
   *
   */
  public void setDeleteButton(java.lang.String deleteButton) {
      this.deleteButton = deleteButton;
  }

  /** Getter for property h_amountType.
   * @return Value of property h_amountType.
   *
   */
  public java.lang.String getH_amountType() {
      return h_amountType;
  }

  /** Setter for property h_amountType.
   * @param h_amountType New value of property h_amountType.
   *
   */
  public void setH_amountType(java.lang.String h_amountType) {
      this.h_amountType = h_amountType;
  }

  /** Getter for property empEditable.
   * @return Value of property empEditable.
   *
   */
  public java.lang.String getEmpEditable() {
      return empEditable;
  }

  /** Setter for property empEditable.
   * @param empEditable New value of property empEditable.
   *
   */
  public void setEmpEditable(java.lang.String empEditable) {
      this.empEditable = empEditable;
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
