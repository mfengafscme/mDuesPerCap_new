package org.afscme.enterprise.minimumdues;

import java.util.*;


public class AgreementBean {      //implements java.io.Serializable {

  // private member variables
  private int agreementPk = 0;
  private String agreementName = "";
  private String startDate = "";
  private String endDate = "";
  private String comments = "";

  public AgreementBean(String agreementName) {
	this.agreementPk = 0;
	this.agreementName = agreementName;
	this.startDate = "";
	this.endDate = "";
	this.comments = "";
  }

  public AgreementBean(int agreementPk) {
	this.agreementPk = agreementPk;
	this.agreementName = "";
	this.startDate = "";
	this.endDate = "";
	this.comments = "";
  }

  public AgreementBean(int agreementPk, String agreementName) {
	this.agreementPk = agreementPk;
	this.agreementName = agreementName;
	this.startDate = "";
	this.endDate = "";
	this.comments = "";
  }

  public AgreementBean(int agreementPk, String agreementName, String startDate, String endDate, String comments) {
	this.agreementPk = agreementPk;
	this.agreementName = agreementName;

	if (startDate == null || (startDate.trim().equalsIgnoreCase("01/01/1900")))
		this.startDate = "";
	else
		this.startDate = startDate;

	if (endDate == null || (endDate.trim().equalsIgnoreCase("01/01/1900")))
		this.endDate = "";
	else
		this.endDate = endDate;

	this.comments = comments;
  }

  public void init() {
	this.agreementPk = 0;
	this.agreementName = "";
	this.startDate = "";
	this.endDate = "";
	this.comments = "";
  }

  /** Getter for property agreementName.
   * @return Value of property agreementName.
   *
   */
  public java.lang.String getAgreementName() {
      return agreementName;
  }

  /** Setter for property agreementName.
   * @param agreementName New value of property agreementName.
   *
   */
  public void setAgreementName(java.lang.String agreementName) {
      this.agreementName = agreementName;
  }

  /** Getter for property startDate.
   * @return Value of property startDate.
   *
   */
  public java.lang.String getStartDate() {
      return startDate;
  }

  /** Setter for property startDate.
   * @param startDate New value of property startDate.
   *
   */
  public void setStartDate(java.lang.String startDate) {
      this.startDate = startDate;
  }

  /** Getter for property endDate.
   * @return Value of property endDate.
   *
   */
  public java.lang.String getEndDate() {
      return endDate;
  }

  /** Setter for property endDate.
   * @param endDate New value of property endDate.
   *
   */
  public void setEndDate(java.lang.String endDate) {
      this.endDate = endDate;
  }

  /** Getter for property comments.
   * @return Value of property comments.
   *
   */
  public java.lang.String getComments() {
      return comments;
  }

  /** Setter for property comments.
   * @param comments New value of property comments.
   *
   */
  public void setComments(java.lang.String comments) {
      this.comments = comments;
  }

  /** Getter for property agreementPk.
   * @return Value of property agreementPk.
   *
   */
  public int getAgreementPk() {
      return agreementPk;
  }

  /** Setter for property agreementPk.
   * @param agreementPk New value of property agreementPk.
   *
   */
  public void setAgreementPk(int agreementPk) {
      this.agreementPk = agreementPk;
  }

}
