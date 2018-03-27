package org.afscme.enterprise.reporting.specialized.web;

import java.text.ParseException;
import java.sql.Timestamp;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.web.WebUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;

/**
 * @struts:form name="enrichedDataForm"
 */
public class EnrichedDataForm extends ActionForm {

    protected Integer affPk = null;
    protected Character affType = null;
    protected String affLocal = null;
    protected String affState = null;
    protected String affSubunit = null;
    protected String affCouncil = null;
    protected Character affCode = null;
    
    public EnrichedDataForm() {
        affType = new Character('L');
    }

    public String toString() {
        return  "EnrichedDataForm [" +
                "affPk=" + affPk +
                ", affType=" + affType +
                ", affLocal=" + affLocal +
                ", affState=" + affState +
                ", affSubunit=" + affSubunit +
                ", affCouncil=" + affCouncil +
                ", affCode=" + affCode +
                "]"
        ;
    }

    /** Form's validation method */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {;
        ActionErrors errors = new ActionErrors();
        WebUtil.checkValidAffiliateIdentifier("affType", affType, "affLocal", affLocal,
                                              "affState", affState, "affSubunit", affSubunit,
                                              "affCouncil", affCouncil, errors);
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
}