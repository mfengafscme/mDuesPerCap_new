package org.afscme.enterprise.minimumdues.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.organization.LocationData;
import org.apache.log4j.Logger;


/**
 * @struts:form name="AffiliateChooseAddForm"
 */
public class AffiliateChooseAddForm extends ActionForm {

  // --------------- Instance Variables --------------------
  private String type;
  private String state;
  private String council;
  private String local;
  private String chapter;
  private String employer;
  private String status;
  private int empAffPk;
  private ArrayList existing_year;
  private String addNewYearButton;


  public AffiliateChooseAddForm() {
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    this.type = null;
    this.state = null;
    this.council = null;
    this.local = null;
    this.chapter = null;
    this.employer = null;
    this.status = null;
    this.empAffPk = 0;
  }

  public ActionErrors validate(ActionMapping mapping,
                 HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    return errors;
  }

  /** Getter for property type.
   * @return Value of property type.
   *
   */
  public java.lang.String getType() {
      return type;
  }

  /** Setter for property type.
   * @param type New value of property type.
   *
   */
  public void setType(java.lang.String type) {
      this.type = type;
  }

  /** Getter for property state.
   * @return Value of property state.
   *
   */
  public java.lang.String getState() {
      return state;
  }

  /** Setter for property state.
   * @param state New value of property state.
   *
   */
  public void setState(java.lang.String state) {
      this.state = state;
  }

  /** Getter for property council.
   * @return Value of property council.
   *
   */
  public java.lang.String getCouncil() {
      return council;
  }

  /** Setter for property council.
   * @param council New value of property council.
   *
   */
  public void setCouncil(java.lang.String council) {
      this.council = council;
  }

  /** Getter for property local.
   * @return Value of property local.
   *
   */
  public java.lang.String getLocal() {
      return local;
  }

  /** Setter for property local.
   * @param local New value of property local.
   *
   */
  public void setLocal(java.lang.String local) {
      this.local = local;
  }

  /** Getter for property chapter.
   * @return Value of property chapter.
   *
   */
  public java.lang.String getChapter() {
      return chapter;
  }

  /** Setter for property chapter.
   * @param chapter New value of property chapter.
   *
   */
  public void setChapter(java.lang.String chapter) {
      this.chapter = chapter;
  }

  /** Getter for property employer.
   * @return Value of property employer.
   *
   */
  public java.lang.String getEmployer() {
      return employer;
  }

  /** Setter for property employer.
   * @param employer New value of property employer.
   *
   */
  public void setEmployer(java.lang.String employer) {
      this.employer = employer;
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

  /** Getter for property existing_year.
   * @return Value of property existing_year.
   *
   */
  public ArrayList getExisting_year() {
      return existing_year;
  }

  /** Setter for property existing_year.
   * @param existing_year New value of property existing_year.
   *
   */
  public void setExisting_year(ArrayList existing_year) {
      this.existing_year = existing_year;
  }

  /** Getter for property addNewYearButton.
   * @return Value of property addNewYearButton.
   *
   */
  public java.lang.String getAddNewYearButton() {
      return addNewYearButton;
  }

  /** Setter for property addNewYearButton.
   * @param addNewYearButton New value of property addNewYearButton.
   *
   */
  public void setAddNewYearButton(java.lang.String addNewYearButton) {
      this.addNewYearButton = addNewYearButton;
  }

  /** Getter for property status.
   * @return Value of property status.
   *
   */
  public java.lang.String getStatus() {
      return status;
  }

  /** Setter for property status.
   * @param status New value of property status.
   *
   */
  public void setStatus(java.lang.String status) {
      this.status = status;
  }

}
