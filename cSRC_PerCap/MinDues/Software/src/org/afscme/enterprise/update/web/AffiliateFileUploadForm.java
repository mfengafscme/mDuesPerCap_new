package org.afscme.enterprise.update.web;

import java.text.ParseException;
import java.sql.Timestamp;
import org.apache.struts.action.*;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.util.DateUtil;
import org.apache.struts.upload.*;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.web.WebUtil;
import org.apache.log4j.Logger;

/**
 * @struts:form name="affiliateFileUploadForm"
 */
public class AffiliateFileUploadForm extends ActionForm {
    
    private static Logger logger = Logger.getLogger(AffiliateFileUploadForm.class);
    
    protected Integer affPk = null;
    protected Character affType = null;
    protected String affLocal = null;
    protected String affState = null;
    protected String affSubunit = null;
    protected String affCouncil = null;
    protected Character affCode = null;
    
    protected FormFile file = null;
    
    protected String validDateStr = null;
    protected Timestamp validDate = null;

    protected int updateType;
    protected int updateTypeCode;
    
    public AffiliateFileUploadForm() {
        validDate = DateUtil.getCurrentDateAsTimestamp();
        validDateStr = DateUtil.getSimpleDateString(validDate);
        affType = new Character('L');
    }
    
    public String toString() {
        return  "AffiliateFileUploadForm [" +
                "affPk=" + affPk +
                ", affType=" + affType +
                ", affLocal=" + affLocal + 
                ", affState=" + affState + 
                ", affSubunit=" + affSubunit + 
                ", affCouncil=" + affCouncil + 
                ", affCode=" + affCode + 
                ", file=" + (file == null ? "null" : file.getFileName()) + 
                ", validDate=" + validDate + 
                ", updateType=" + updateType +
                "]"
        ;
    }
    
    /** Form's validation method */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        logger.debug("----------------------------------------------------");
        logger.debug("validate called.");
        ActionErrors errors = new ActionErrors();
        WebUtil.checkValidAffiliateIdentifier("affType", affType, "affLocal", affLocal, 
                                              "affState", affState, "affSubunit", affSubunit, 
                                              "affCouncil", affCouncil, errors
        );
        logger.debug("file=" + file);
        if (file == null || (TextUtil.isEmptyOrSpaces(file.getFileName()))) {
            errors.add("file", new ActionError("error.field.update.fileNotFound"));
        }
        
        validDate = WebUtil.checkRequiredDate("validDateStr", validDateStr, errors);
        logger.debug("validate complete. Returning " + errors.size() + " error(s).");
        return errors;
    }
    
    public AffiliateIdentifier getAffId() {
        return new AffiliateIdentifier(
            affType, 
            TextUtil.isEmpty(affLocal) ? "" : affLocal,
            TextUtil.isEmpty(affState) ? "" : affState,
            TextUtil.isEmpty(affSubunit) ? "" : affSubunit,
            TextUtil.isEmpty(affCouncil) ? "" : "0".equals(affCouncil) ? "" : affCouncil, 
            affCode, null
        );
    }
    
    
    /** Getter for property affType.
     * @return Value of property affType.
     */
    public Character getAffType() {
        return affType;
    }    

    /** Setter for property affType.
     * @param affType New value of property affType.
     */
    public void setAffType(Character affType) {
        this.affType = affType;
    }    
    
    /** Getter for property affLocal.
     * @return Value of property affLocal.
     */
    public String getAffLocal() {
        return affLocal;
    }
    
    /** Setter for property affLocal.
     * @param affLocal New value of property affLocal.
     */
    public void setAffLocal(String affLocal) {
        this.affLocal = affLocal;
    }
    
    /** Getter for property affState.
     * @return Value of property affState.
     */
    public String getAffState() {
        return affState;
    }
    
    /** Setter for property affState.
     * @param affState New value of property affState.
     */
    public void setAffState(String affState) {
        this.affState = affState;
    }
    
    /** Getter for property affSubunit.
     * @return Value of property affSubunit.
     */
    public String getAffSubunit() {
        return affSubunit;
    }
    
    /** Setter for property affSubunit.
     * @param affSubunit New value of property affSubunit.
     */
    public void setAffSubunit(String affSubunit) {
        this.affSubunit = affSubunit;
    }
    
    /** Getter for property affCouncil.
     * @return Value of property affCouncil.
     */
    public String getAffCouncil() {
        return affCouncil;
    }
    
    /** Setter for property affCouncil.
     * @param affCouncil New value of property affCouncil.
     */
    public void setAffCouncil(String affCouncil) {
        this.affCouncil = affCouncil;
    }
    
    /** Getter for property file.
     * @return Value of property file.
     */
    public FormFile getFile() {
        return file;
    }
    
    /** Setter for property file.
     * @param file New value of property file.
     */
    public void setFile(FormFile file) {
        this.file = file;
    }
 
    /** Getter for property validDateStr.
     * @return Value of property validDateStr.
     */
    public String getValidDateStr() {
        return validDateStr;
    }    

    /** Setter for property validDateStr.
     * @param validDateStr New value of property validDateStr.
     */
    public void setValidDateStr(String validDateStr) {
        this.validDateStr = validDateStr;
    }    
    
    /** Getter for property validDate.
     * @return Value of property validDate.
     */
    public Timestamp getValidDate() {
        return validDate;
    }
    
    /** Setter for property validDate.
     * @param validDate New value of property validDate.
     */
    public void setValidDate(Timestamp validDate) {
        this.validDate = validDate;
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
        this.affPk = affPk;
    }
    
    /** Getter for property affCode.
     * @return Value of property affCode.
     *
     */
    public Character getAffCode() {
        return affCode;
    }
    
    /** Setter for property affCode.
     * @param affCode New value of property affCode.
     *
     */
    public void setAffCode(Character affCode) {
        this.affCode = affCode;
    }
    
    /** Getter for property updateType.
     * @return Value of property updateType.
     *
     */
    public int getUpdateType() {
        return updateType;
    }
    
    /** Setter for property updateType.
     * @param updateType New value of property updateType.
     *
     */
    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }
    
    /************************************************************************************************/
    /** Getter for property updateTypeCode.
     * @return Value of property updateTypeCode.
     *
     */
    public int getUpdateTypeCode() {
        return updateTypeCode;
    }
    
    /** Setter for property updateTypeCode.
     * @param updateType New value of property updateTypeCode.
     *
     */
    public void setUpdateTypeCode(int updateTypeCode) {
        this.updateTypeCode = updateTypeCode;
    }
    /****************************************************************************************************/
}