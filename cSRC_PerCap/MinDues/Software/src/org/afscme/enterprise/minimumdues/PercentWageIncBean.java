package org.afscme.enterprise.minimumdues;

import java.util.*;

import org.afscme.enterprise.util.TextUtil;

/*
 *  This is a class for Sec .A --- percentWageInc in the Nested Extension
 */
public class PercentWageIncBean { //implements java.io.Serializable {

  // private member variables
  private String percent_a = "";
  private String effective_a = "";
  private String noOfMember_a = "";
  private String typeOfPayment_adj_a  = "";
  private String percentInc_adj_a = "";
  private String noOfMember_adj_a = "";
  private String mbrTimesInc_a = "";

  public PercentWageIncBean() {
  }

  public PercentWageIncBean(String percent, String effective, String noOfMem, String typeOfPaymentAdj,
  		String percentAdj, String noOfMemAdj, String mbrTimesInc) {
    this.setPercent_a(percent);
    this.setEffective_a(effective);
    this.setNoOfMember_a(noOfMem);

    this.setTypeOfPayment_adj_a(typeOfPaymentAdj);
    this.setPercentInc_adj_a(percentAdj);
    this.setNoOfMember_adj_a(noOfMemAdj);
    this.setMbrTimesInc_a(mbrTimesInc);
  }

  public void init() {
    this.setPercent_a("");
    this.setEffective_a("");
    this.setNoOfMember_a("");

    this.setTypeOfPayment_adj_a("");
    this.setPercentInc_adj_a("");
    this.setNoOfMember_adj_a("");
    this.setMbrTimesInc_a("");
  }

  /** Getter for property percent_a.
   * @return Value of property percent_a.
   *
   */
  public java.lang.String getPercent_a() {
      return this.percent_a;
  }

  /** Setter for property percent_a.
   * @param percent_a New value of property percent_a.
   *
   */
  public void setPercent_a(java.lang.String percent_a) {
      this.percent_a = TextUtil.formatToNumber(percent_a);
  }

  /** Getter for property effective_a.
   * @return Value of property effective_a.
   *
   */
  public java.lang.String getEffective_a() {
      return this.effective_a;
  }

  /** Setter for property effective_a.
   * @param effective_a New value of property effective_a.
   *
   */
  public void setEffective_a(java.lang.String effective_a) {
      this.effective_a = effective_a;
  }

  /** Getter for property noOfMember_a.
   * @return Value of property noOfMember_a.
   *
   */
  public java.lang.String getNoOfMember_a() {
      return this.noOfMember_a;
  }

  /** Setter for property noOfMember_a.
   * @param noOfMember_a New value of property noOfMember_a.
   *
   */
  public void setNoOfMember_a(java.lang.String noOfMember_a) {
      this.noOfMember_a = TextUtil.formatToNumber(noOfMember_a);
  }

  /** Getter for property typeOfPayment_adj_a.
   * @return Value of property typeOfPayment_adj_a.
   *
   */
  public java.lang.String getTypeOfPayment_adj_a() {
      return typeOfPayment_adj_a;
  }

  /** Setter for property typeOfPayment_adj_a.
   * @param typeOfPayment_adj_a New value of property typeOfPayment_adj_a.
   *
   */
  public void setTypeOfPayment_adj_a(java.lang.String typeOfPayment_adj_a) {
      this.typeOfPayment_adj_a = typeOfPayment_adj_a;
  }

  /** Getter for property percentInc_adj_a.
   * @return Value of property percentInc_adj_a.
   *
   */
  public java.lang.String getPercentInc_adj_a() {
      return percentInc_adj_a;
  }

  /** Setter for property percentInc_adj_a.
   * @param percentInc_adj_a New value of property percentInc_adj_a.
   *
   */
  public void setPercentInc_adj_a(java.lang.String percentInc_adj_a) {
      this.percentInc_adj_a = TextUtil.formatToNumber(percentInc_adj_a);
  }

  /** Getter for property noOfMember_adj_a.
   * @return Value of property noOfMember_adj_a.
   *
   */
  public java.lang.String getNoOfMember_adj_a() {
      return noOfMember_adj_a;
  }

  /** Setter for property noOfMember_adj_a.
   * @param noOfMember_adj_a New value of property noOfMember_adj_a.
   *
   */
  public void setNoOfMember_adj_a(java.lang.String noOfMember_adj_a) {
      this.noOfMember_adj_a = TextUtil.formatToNumber(noOfMember_adj_a);
  }

  /** Getter for property mbrTimesInc_a.
   * @return Value of property mbrTimesInc_a.
   *
   */
  public java.lang.String getMbrTimesInc_a() {
      return mbrTimesInc_a;
  }

  /** Setter for property mbrTimesInc_a.
   * @param mbrTimesInc_a New value of property mbrTimesInc_a.
   *
   */
  public void setMbrTimesInc_a(java.lang.String mbrTimesInc_a) {
      //this.mbrTimesInc_a = TextUtil.formatToNumber(mbrTimesInc_a);
      this.mbrTimesInc_a = mbrTimesInc_a;
  }

}
