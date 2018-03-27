package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.FinancialData;
import org.apache.log4j.Logger;

/**
 * @struts:form name="financialForm"
 */
public class FinancialForm extends ActionForm {
    
    private static Logger logger =  Logger.getLogger(FinancialForm.class);       
    
    private Integer affPk;
    private String employerIDNumber;
    private Integer perCapitaStatAvg;
    private String perCapitaTaxPaymentMethod;
    private String perCapitaTaxLastPaidDate;
    private Integer perCapitaTaxLastMemberCount;
    private String perCapitaTaxInfoLastUpdateDate;
    private String comment;
    
    /** Creates a new instance of FinancialForm */
    public FinancialForm() {
    }
    
// General Methods...
    
    public void init() {
        this.affPk = null;
        this.comment = null;
        this.employerIDNumber = null;
        this.perCapitaStatAvg = null;
        this.perCapitaTaxInfoLastUpdateDate = null;
        this.perCapitaTaxLastMemberCount = null;
        this.perCapitaTaxLastPaidDate = null;
        this.perCapitaTaxPaymentMethod = null;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("FinancialForm {");
        sb.append("affPk=");
        sb.append(this.affPk);
        sb.append(", employerIDNumber=");
        sb.append(this.employerIDNumber);
        sb.append(", perCapitaStatAvg=");
        sb.append(this.perCapitaStatAvg);
        sb.append(", perCapitaTaxInfoLastUpdateDate=");
        sb.append(this.perCapitaTaxInfoLastUpdateDate);
        sb.append(", perCapitaTaxLastMemberCount=");
        sb.append(this.perCapitaTaxLastMemberCount);
        sb.append(", perCapitaTaxLastPaidDate=");
        sb.append(this.perCapitaTaxLastPaidDate);
        sb.append(", perCapitaTaxPaymentMethod=");
        sb.append(this.perCapitaTaxPaymentMethod);
        sb.append(", comment=");
        sb.append(this.comment);
        sb.append("}");
        return sb.toString().trim();
    }
    
// Struts Methods...
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.init();
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        logger.debug("**************** Returning " + errors.size() + " error(s).");
        return errors;
    }
    
// Getter and Setter Methods...
    
    /** Gets a FinancialData object using the fields in the form.
     * @return the FinancialData representing this form.
     */
    public FinancialData getFinancialData() {
        FinancialData data = new FinancialData();
        data.setAffPk(this.getAffPk());
        data.setComment(this.getComment());
        data.setEmployerIDNumber(this.getEmployerIDNumber());
        data.setPerCapitaStatAvg(this.getPerCapitaStatAvg());
        data.setPerCapitaTaxInfoLastUpdateDate(DateUtil.getTimestamp(this.getPerCapitaTaxInfoLastUpdateDate()));
        data.setPerCapitaTaxLastMemberCount(this.getPerCapitaTaxLastMemberCount());
        data.setPerCapitaTaxLastPaidDate(DateUtil.getTimestamp(this.getPerCapitaTaxLastPaidDate()));
        data.setPerCapitaTaxPaymentMethod(this.getPerCapitaTaxPaymentMethod());
        return data;
    }
    
    /** Sets all of the fields in this form with the values in the param.
     * @param data The FinancialData object.
     */
    public void setFinancialData(FinancialData data) {
        this.setAffPk(data.getAffPk());
        this.setComment(data.getComment());
        this.setEmployerIDNumber(data.getEmployerIDNumber());
        this.setPerCapitaStatAvg(data.getPerCapitaStatAvg());
        this.setPerCapitaTaxInfoLastUpdateDate(DateUtil.getSimpleDateString(data.getPerCapitaTaxInfoLastUpdateDate()));
        this.setPerCapitaTaxLastMemberCount(data.getPerCapitaTaxLastMemberCount());
        this.setPerCapitaTaxLastPaidDate(DateUtil.getSimpleDateString(data.getPerCapitaTaxLastPaidDate()));
        this.setPerCapitaTaxPaymentMethod(data.getPerCapitaTaxPaymentMethod());
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        if (affPk == null || affPk.intValue() < 1) {
            this.affPk = null;
        } else {
            this.affPk = affPk;
        }
    }
    
    /** Getter for property comment.
     * @return Value of property comment.
     *
     */
    public String getComment() {
        return comment;
    }
    
    /** Setter for property comment.
     * @param comment New value of property comment.
     *
     */
    public void setComment(String comment) {
        if (TextUtil.isEmptyOrSpaces(comment)) {
            this.comment = null;
        } else {
            this.comment = comment;
        }
    }
    
    /** Getter for property employerIDNumber.
     * @return Value of property employerIDNumber.
     *
     */
    public String getEmployerIDNumber() {
        return employerIDNumber;
    }
    
    /** Setter for property employerIDNumber.
     * @param employerIDNumber New value of property employerIDNumber.
     *
     */
    public void setEmployerIDNumber(String employerIDNumber) {
        if (TextUtil.isEmptyOrSpaces(employerIDNumber)) {
            this.employerIDNumber = null;
        } else {
            this.employerIDNumber = employerIDNumber;
        }
    }
    
    /** Getter for property perCapitaStatAvg.
     * @return Value of property perCapitaStatAvg.
     *
     */
    public Integer getPerCapitaStatAvg() {
        return perCapitaStatAvg;
    }
    
    /** Setter for property perCapitaStatAvg.
     * @param perCapitaStatAvg New value of property perCapitaStatAvg.
     *
     */
    public void setPerCapitaStatAvg(Integer perCapitaStatAvg) {
        if (perCapitaStatAvg == null || perCapitaStatAvg.intValue() < 1) {
            this.perCapitaStatAvg = null;
        } else {
            this.perCapitaStatAvg = perCapitaStatAvg;
        }
    }
    
    /** Getter for property perCapitaTaxInfoLastUpdateDate.
     * @return Value of property perCapitaTaxInfoLastUpdateDate.
     *
     */
    public String getPerCapitaTaxInfoLastUpdateDate() {
        return perCapitaTaxInfoLastUpdateDate;
    }
    
    /** Setter for property perCapitaTaxInfoLastUpdateDate.
     * @param perCapitaTaxInfoLastUpdateDate New value of property perCapitaTaxInfoLastUpdateDate.
     *
     */
    public void setPerCapitaTaxInfoLastUpdateDate(String perCapitaTaxInfoLastUpdateDate) {
        if (TextUtil.isEmptyOrSpaces(perCapitaTaxInfoLastUpdateDate)) {
            this.perCapitaTaxInfoLastUpdateDate = null;
        } else {
            this.perCapitaTaxInfoLastUpdateDate = perCapitaTaxInfoLastUpdateDate;
        }
    }
    
    /** Getter for property perCapitaTaxLastMemberCount.
     * @return Value of property perCapitaTaxLastMemberCount.
     *
     */
    public Integer getPerCapitaTaxLastMemberCount() {
        return perCapitaTaxLastMemberCount;
    }
    
    /** Setter for property perCapitaTaxLastMemberCount.
     * @param perCapitaTaxLastMemberCount New value of property perCapitaTaxLastMemberCount.
     *
     */
    public void setPerCapitaTaxLastMemberCount(Integer perCapitaTaxLastMemberCount) {
        if (perCapitaTaxLastMemberCount == null || perCapitaTaxLastMemberCount.intValue() < 1) {
            this.perCapitaTaxLastMemberCount = null;
        } else {
            this.perCapitaTaxLastMemberCount = perCapitaTaxLastMemberCount;
        }
    }
    
    /** Getter for property perCapitaTaxLastPaidDate.
     * @return Value of property perCapitaTaxLastPaidDate.
     *
     */
    public String getPerCapitaTaxLastPaidDate() {
        return perCapitaTaxLastPaidDate;
    }
    
    /** Setter for property perCapitaTaxLastPaidDate.
     * @param perCapitaTaxLastPaidDate New value of property perCapitaTaxLastPaidDate.
     *
     */
    public void setPerCapitaTaxLastPaidDate(String perCapitaTaxLastPaidDate) {
        if (TextUtil.isEmptyOrSpaces(perCapitaTaxLastPaidDate)) {
            this.perCapitaTaxLastPaidDate = null;
        } else {
            this.perCapitaTaxLastPaidDate = perCapitaTaxLastPaidDate;
        }
    }
    
    /** Getter for property perCapitaTaxPaymentMethod.
     * @return Value of property perCapitaTaxPaymentMethod.
     *
     */
    public String getPerCapitaTaxPaymentMethod() {
        return perCapitaTaxPaymentMethod;
    }
    
    /** Setter for property perCapitaTaxPaymentMethod.
     * @param perCapitaTaxPaymentMethod New value of property perCapitaTaxPaymentMethod.
     *
     */
    public void setPerCapitaTaxPaymentMethod(String perCapitaTaxPaymentMethod) {
        if (TextUtil.isEmptyOrSpaces(perCapitaTaxPaymentMethod)) {
            this.perCapitaTaxPaymentMethod = null;
        } else {
            this.perCapitaTaxPaymentMethod = perCapitaTaxPaymentMethod;
        }
    }
    
}
