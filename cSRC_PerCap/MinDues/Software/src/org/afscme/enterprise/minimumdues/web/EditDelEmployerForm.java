package org.afscme.enterprise.minimumdues.web;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.minimumdues.PercentWageIncBean;
import org.afscme.enterprise.minimumdues.AmountWageIncBean;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:form name="EditDelEmployerForm"
 */
public class EditDelEmployerForm extends ActionForm {
// public class DataEntryForm extends org.apache.struts.validator.ValidatorForm {
  // --------------- Instance Variables --------------------
  private int empAffPk = 0;
  private String affIdType = null;
  private String employerName = null;
  private String affIdSubUnit = null;
  private String affIdLocal = null;
  private String affIdCouncil = null;
  private String affIdState = null;
  private String affIdStatus = null;
  private String duesyear = null;

  private String editEmpButton;
  private String deleteEmpButton;
  private String cancelEditEmpButton;

  public EditDelEmployerForm() {
  }


  public void reset(ActionMapping mapping, HttpServletRequest request) {

  }

  public ActionErrors validate(ActionMapping mapping,
                 HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    return errors;
  }

    /** Getter for property affIdType.
     * @return Value of property affIdType.
     *
     */
    public java.lang.String getAffIdType() {
        return affIdType;
    }

    /** Setter for property affIdType.
     * @param affIdType New value of property affIdType.
     *
     */
    public void setAffIdType(java.lang.String affIdType) {
        this.affIdType = affIdType;
    }

  /** Getter for property empAffPk.
   * @return Value of property empAffPk.
   *
   */
  public int getEmpAffPk() {
      return empAffPk;
  }

  /** Setter for property empAffPk.
   * @param empAffPk New value of property empAffPk.
   *
   */
  public void setEmpAffPk(int empAffPk) {
      this.empAffPk = empAffPk;
  }

  /** Getter for property employerName.
   * @return Value of property employerName.
   *
   */
  public java.lang.String getEmployerName() {
      return employerName;
  }

  /** Setter for property employerName.
   * @param employerName New value of property employerName.
   *
   */
  public void setEmployerName(java.lang.String employerName) {
      this.employerName = employerName;
  }

  /** Getter for property affIdSubUnit.
   * @return Value of property affIdSubUnit.
   *
   */
  public java.lang.String getAffIdSubUnit() {
      return affIdSubUnit;
  }

  /** Setter for property affIdSubUnit.
   * @param affIdSubUnit New value of property affIdSubUnit.
   *
   */
  public void setAffIdSubUnit(java.lang.String affIdSubUnit) {
      this.affIdSubUnit = affIdSubUnit;
  }

  /** Getter for property affIdLocal.
   * @return Value of property affIdLocal.
   *
   */
  public java.lang.String getAffIdLocal() {
      return affIdLocal;
  }

  /** Setter for property affIdLocal.
   * @param affIdLocal New value of property affIdLocal.
   *
   */
  public void setAffIdLocal(java.lang.String affIdLocal) {
      this.affIdLocal = affIdLocal;
  }

  /** Getter for property affIdCouncil.
   * @return Value of property affIdCouncil.
   *
   */
  public java.lang.String getAffIdCouncil() {
      return affIdCouncil;
  }

  /** Setter for property affIdCouncil.
   * @param affIdCouncil New value of property affIdCouncil.
   *
   */
  public void setAffIdCouncil(java.lang.String affIdCouncil) {
      this.affIdCouncil = affIdCouncil;
  }

  /** Getter for property affIdState.
   * @return Value of property affIdState.
   *
   */
  public java.lang.String getAffIdState() {
      return affIdState;
  }

  /** Setter for property affIdState.
   * @param affIdState New value of property affIdState.
   *
   */
  public void setAffIdState(java.lang.String affIdState) {
      this.affIdState = affIdState;
  }

  /** Getter for property editEmpButton.
   * @return Value of property editEmpButton.
   *
   */
  public java.lang.String getEditEmpButton() {
      return editEmpButton;
  }

  /** Setter for property editEmpButton.
   * @param editEmpButton New value of property editEmpButton.
   *
   */
  public void setEditEmpButton(java.lang.String editEmpButton) {
      this.editEmpButton = editEmpButton;
  }

  /** Getter for property deleteEmpButton.
   * @return Value of property deleteEmpButton.
   *
   */
  public java.lang.String getDeleteEmpButton() {
      return deleteEmpButton;
  }

  /** Setter for property deleteEmpButton.
   * @param deleteEmpButton New value of property deleteEmpButton.
   *
   */
  public void setDeleteEmpButton(java.lang.String deleteEmpButton) {
      this.deleteEmpButton = deleteEmpButton;
  }

  /** Getter for property cancelEditEmpButton.
   * @return Value of property cancelButton.
   *
   */
  public java.lang.String getCancelEditEmpButton() {
      return cancelEditEmpButton;
  }

  /** Setter for property cancelEditEmpButton.
   * @param cancelEditEmpButton New value of property cancelEditEmpButton.
   *
   */
  public void setCancelEditEmpButton(java.lang.String cancelEditEmpButton) {
      this.cancelEditEmpButton = cancelEditEmpButton;
  }

  /** Getter for property affIdStatus.
   * @return Value of property affIdStatus.
   *
   */
  public java.lang.String getAffIdStatus() {
      return affIdStatus;
  }

  /** Setter for property affIdStatus.
   * @param affIdStatus New value of property affIdStatus.
   *
   */
  public void setAffIdStatus(java.lang.String affIdStatus) {
      this.affIdStatus = affIdStatus;
  }

  /** Getter for property duesyear.
   * @return Value of property duesyear.
   *
   */
  public java.lang.String getDuesyear() {
      return duesyear;
  }
  
  /** Setter for property duesyear.
   * @param duesyear New value of property duesyear.
   *
   */
  public void setDuesyear(java.lang.String duesyear) {
      this.duesyear = duesyear;
  }
  
}
