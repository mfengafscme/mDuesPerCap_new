package org.afscme.enterprise.minimumdues;

import java.util.*;

import org.afscme.enterprise.util.TextUtil;

/*
 *  This is a class for Sec .B --- AmountWageInc in the Nested Extension
 */
public class AmountWageIncBean {      //implements java.io.Serializable {

  // private member variables
  private String amountInc_b = "";
  private String effective_b = "";
  private String noOfMember_b = "";
  private String typeOfPayment_adj_b  = "";
  private String amountInc_adj_b = "";
  private String noOfMember_adj_b = "";
  private String mbrTimesInc_b = "";

  public AmountWageIncBean() {
  }

  public AmountWageIncBean(String amount, String effective, String noOfMem, String typeOfPaymentAdj,
  		String amountAdj, String noOfMemAdj, String mbrTimesInc) {
    this.setAmountInc_b(amount);
    this.setEffective_b(effective);
    this.setNoOfMember_b(noOfMem);

    this.setTypeOfPayment_adj_b(typeOfPaymentAdj);
    this.setAmountInc_adj_b(amountAdj);
    this.setNoOfMember_adj_b(noOfMemAdj);
    this.setMbrTimesInc_b(mbrTimesInc);
  }

  public void init() {
    this.setAmountInc_b("");
    this.setEffective_b("");
    this.setNoOfMember_b("");

    this.setTypeOfPayment_adj_b("");
    this.setAmountInc_adj_b("");
    this.setNoOfMember_adj_b("");
    this.setMbrTimesInc_b("");
  }

  /** Getter for property effective_b.
   * @return Value of property effective_b.
   *
   */
  public java.lang.String getEffective_b() {
      return this.effective_b;
  }

  /** Setter for property effective_b.
   * @param effective_b New value of property effective_b.
   *
   */
  public void setEffective_b(java.lang.String effective_b) {
      this.effective_b = effective_b;
  }

  /** Getter for property noOfMember_b.
   * @return Value of property noOfMember_b.
   *
   */
  public java.lang.String getNoOfMember_b() {
      return this.noOfMember_b;
  }

  /** Setter for property noOfMember_b.
   * @param noOfMember_b New value of property noOfMember_b.
   *
   */
  public void setNoOfMember_b(java.lang.String noOfMember_b) {
      this.noOfMember_b = TextUtil.formatToNumber(noOfMember_b);
  }

  /** Getter for property typeOfPayment_adj_b.
   * @return Value of property typeOfPayment_adj_b.
   *
   */
  public java.lang.String getTypeOfPayment_adj_b() {
      return typeOfPayment_adj_b;
  }

  /** Setter for property typeOfPayment_adj_b.
   * @param typeOfPayment_adj_b New value of property typeOfPayment_adj_b.
   *
   */
  public void setTypeOfPayment_adj_b(java.lang.String typeOfPayment_adj_b) {
      this.typeOfPayment_adj_b = typeOfPayment_adj_b;
  }

  /** Getter for property noOfMember_adj_b.
   * @return Value of property noOfMember_adj_b.
   *
   */
  public java.lang.String getNoOfMember_adj_b() {
      return noOfMember_adj_b;
  }

  /** Setter for property noOfMember_adj_b.
   * @param noOfMember_adj_b New value of property noOfMember_adj_b.
   *
   */
  public void setNoOfMember_adj_b(java.lang.String noOfMember_adj_b) {
      this.noOfMember_adj_b = TextUtil.formatToNumber(noOfMember_adj_b);
  }

  /** Getter for property mbrTimesInc_b.
   * @return Value of property mbrTimesInc_b.
   *
   */
  public java.lang.String getMbrTimesInc_b() {
      return mbrTimesInc_b;
  }

  /** Setter for property mbrTimesInc_b.
   * @param mbrTimesInc_b New value of property mbrTimesInc_b.
   *
   */
  public void setMbrTimesInc_b(java.lang.String mbrTimesInc_b) {
      this.mbrTimesInc_b = TextUtil.formatToNumber(mbrTimesInc_b);
  }

  /** Getter for property amountInc_b.
   * @return Value of property amountInc_b.
   *
   */
  public java.lang.String getAmountInc_b() {
      return amountInc_b;
  }

  /** Setter for property amountInc_b.
   * @param amountInc_b New value of property amountInc_b.
   *
   */
  public void setAmountInc_b(java.lang.String amountInc_b) {
      this.amountInc_b = TextUtil.formatToNumber(amountInc_b);
  }

  /** Getter for property amountInc_adj_b.
   * @return Value of property amountInc_adj_b.
   *
   */
  public java.lang.String getAmountInc_adj_b() {
      return amountInc_adj_b;
  }

  /** Setter for property amountInc_adj_b.
   * @param amountInc_adj_b New value of property amountInc_adj_b.
   *
   */
  public void setAmountInc_adj_b(java.lang.String amountInc_adj_b) {
      this.amountInc_adj_b = TextUtil.formatToNumber(amountInc_adj_b);
  }

}
