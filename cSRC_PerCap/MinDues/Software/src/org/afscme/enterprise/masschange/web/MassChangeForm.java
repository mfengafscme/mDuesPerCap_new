package org.afscme.enterprise.masschange.web;

// Java imports
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.MRData;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;

// Apache imports
import org.apache.log4j.Logger;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * @struts:form name="massChangeForm"
 */
public class MassChangeForm extends org.apache.struts.action.ActionForm {
    
    public static final int MASS_CHANGE_TYPE_STATUS     = 1;
    public static final int MASS_CHANGE_TYPE_OTHER      = 2;
    
    public static final int STATUS_CHANGE_MERGED         = 1;
    public static final int STATUS_CHANGE_SPLIT          = 2;
    public static final int STATUS_CHANGE_DEACTIVATED    = 3;
    
    private Integer affPk;
    private AffiliateIdentifier affiliateIdCurrent;
    private Integer statusChangeSelect;
    private Integer massChangeSelect;
    private Integer mergedAffiliatePk;
    private AffiliateIdentifier mergedAffiliate;
    private Integer splitAffiliatePk;
    private AffiliateIdentifier splitAffiliate;
    private Integer statusCurrent;
    private boolean infoSourceFg;
    private Integer infoSourceCurrent;
    private Integer infoSourceNew;
    private boolean mbrCardBypassFg;
    private boolean mbrCardBypassFgCurrent;
    private boolean mbrCardBypassFgNew;
    private boolean peMailBypassFg;
    private boolean peMailBypassFgCurrent;
    private boolean peMailBypassFgNew;
    private boolean mbrRenewalFg;
    private boolean mbrRenewalFgNew;
    private boolean newAffiliateFg;
    private Integer newAffiliatePk;
    private AffiliateIdentifier newAffiliate;
    
    static Logger logger = Logger.getLogger(MassChangeForm.class);
    
    /** Creates a new instance of MassChangeForm */
    public MassChangeForm() {
        this.init();
    }
    
// Struts Methods...
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        logger.debug("---------------------------------------------------------------");
        logger.debug("Inside MassChangeForm.validate().");
        logger.debug("---------------------------------------------------------------");
        logger.debug("Form: " + this.toString());
        logger.debug("---------------------------------------------------------------");
        logger.debug("Current Affiliate Type: " + this.getAffiliateIdCurrent().getType());
        
        ActionErrors errors = new ActionErrors();
        List affIdErrors = null;
        if (this.getMassChangeSelect() == null) {
            errors.add(errors.GLOBAL_ERROR, new ActionError("errors.massChange.noMassChangeSelected"));
            logger.debug("      added errors.massChange.noMassChangeSelected");
        } else if (this.getMassChangeSelect().equals(new Integer(MASS_CHANGE_TYPE_STATUS))) {
            if (this.getStatusChangeSelect() == null) {
                errors.add("statusChangeSelect", new ActionError("errors.massChange.noStatusChangeSelected"));
                logger.debug("      added errors.massChange.noStatusChangeSelected");
            } else { // validate the appropriate field based on value of statusChangeSelect
                switch (this.getStatusChangeSelect().intValue()) {
                    case STATUS_CHANGE_MERGED:
                        logger.debug("      inside case STATUS_CHANGE_MERGED");
                        if (this.getAffiliateIdCurrent().getType().charValue() != 'L' &&
                            this.getAffiliateIdCurrent().getType().charValue() != 'S'
                        ) {
                            errors.add("statusChangeSelect", new ActionError("errors.massChange.statusInvalid", "Locals and Sub Chapters"));
                            logger.debug("      added errors.massChange.statusInvalid for Locals and Sub Chapters");
                        } else {
                            affIdErrors = new ArrayList(); // needed for call in first else if
                            if (TextUtil.isEmptyOrSpaces(this.getMergedAffiliate().getType()) || 
                                !this.getMergedAffiliate().getType().equals(this.getAffiliateIdCurrent().getType())
                            ) {
                                errors.add("mergedAffiliate", new ActionError("errors.massChange.affTypeInvalid", this.getAffiliateIdCurrent().getType().toString()));
                                logger.debug("      added errors.massChange.affTypeInvalid for " + this.getAffiliateIdCurrent().getType());
                            } else if (this.hasAffiliateIdentifierErrors(this.getMergedAffiliate(), affIdErrors)) {
                                for (Iterator it = affIdErrors.iterator(); it.hasNext(); ) {
                                    errors.add("mergedAffiliate", (ActionError)it.next());
                                }
                            } else {
                                // Affiliate Id is valid. set fields that may have been disabled to their default values...
                                switch (this.mergedAffiliate.getType().charValue()) {
                                    case 'C':
                                    case 'R':
                                        this.mergedAffiliate.setLocal(AffiliateIdentifier.DEFAULT_ID_NUMBER);
                                        logger.debug("reset local");
                                        // drop to next case and initialize the subUnit field...
                                    case 'L':
                                    case 'S':
                                        this.mergedAffiliate.setSubUnit(AffiliateIdentifier.DEFAULT_ID_NUMBER);
                                        logger.debug("reset subUnit");
                                    default:// do nothing...
                                        break;
                                }
                            }
                        }
                        break;
                    case STATUS_CHANGE_SPLIT:
                        if (this.getAffiliateIdCurrent().getType().charValue() != 'U') {
                            errors.add("statusChangeSelect", new ActionError("errors.massChange.statusInvalid", "Sub Locals"));
                            logger.debug("      added errors.massChange.statusInvalid for Sub Locals");
                        } else {
                            affIdErrors = new ArrayList(); // needed for call in first else if
                            if (TextUtil.isEmptyOrSpaces(this.getSplitAffiliate().getType()) || 
                                this.getSplitAffiliate().getType().charValue() != 'L'
                            ) {
                                errors.add("splitAffiliate", new ActionError("errors.massChange.affTypeInvalid", "L"));
                                logger.debug("      added errors.massChange.affTypeInvalid for " + this.getAffiliateIdCurrent().getType());
                            } else if (this.hasAffiliateIdentifierErrors(this.getSplitAffiliate(), affIdErrors)) {
                                for (Iterator it = affIdErrors.iterator(); it.hasNext(); ) {
                                    errors.add("splitAffiliate", (ActionError)it.next());
                                }
                            } else {
                                // Affiliate Id is valid. set fields that may have been disabled to their default values...
                                switch (this.splitAffiliate.getType().charValue()) {
                                    case 'C':
                                    case 'R':
                                        this.splitAffiliate.setLocal(AffiliateIdentifier.DEFAULT_ID_NUMBER);
                                        logger.debug("reset local");
                                        // drop to next case and initialize the subUnit field...
                                    case 'L':
                                    case 'S':
                                        this.splitAffiliate.setSubUnit(AffiliateIdentifier.DEFAULT_ID_NUMBER);
                                        logger.debug("reset subUnit");
                                    default:// do nothing...
                                        break;
                                }
                            }
                        }
                        break;
                    default: // Value was STATUS_CHANGE_DEACTIVATED. Do nothing...
                        break;
                }
            }
        } else { // other Changes selected...
            if (!this.isInfoSourceFg() && !this.isMbrCardBypassFg() && 
                !this.isMbrRenewalFg() && !this.isNewAffiliateFg() && 
                !this.isPeMailBypassFg()
            ) {
                errors.add("otherChangeSelect", new ActionError("errors.massChange.noOtherChangeSelected"));
                logger.debug("      added errors.massChange.noOtherChangeSelected");
            } else { // validate the appropriate field based on value of each individual flag
                if (this.isNewAffiliateFg()) {
                    affIdErrors = new ArrayList();
                    if (this.hasAffiliateIdentifierErrors(this.getNewAffiliate(), affIdErrors)) {
                        for (Iterator it = affIdErrors.iterator(); it.hasNext(); ) {
                            errors.add("newAffiliate", (ActionError)it.next());
                        }
                    } else {
                        // Affiliate Id is valid. set fields that may have been disabled to their default values...
                        switch (this.newAffiliate.getType().charValue()) {
                            case 'C':
                            case 'R':
                                this.newAffiliate.setLocal(AffiliateIdentifier.DEFAULT_ID_NUMBER);
                                logger.debug("reset local");
                                // drop to next case and initialize the subUnit field...
                            case 'L':
                            case 'S':
                                this.newAffiliate.setSubUnit(AffiliateIdentifier.DEFAULT_ID_NUMBER);
                                logger.debug("reset subUnit");
                                // drop to next case and initialize the council field if it is empty...
                            case 'U':
                                if (TextUtil.isEmptyOrSpaces(this.newAffiliate.getCouncil())) {
                                    this.newAffiliate.setCouncil(AffiliateIdentifier.DEFAULT_ID_NUMBER);
                                }
                            default:// do nothing...
                                break;
                        }
                    }
                }
                if (this.isInfoSourceFg()) {
                    if (this.getInfoSourceNew() == null) {
                        errors.add("infoSourceNew", new ActionError("errors.massChange.required.generic", "Membership Information Reporting Source"));
                        logger.debug("      added errors.massChange.required.generic for Membership Information Reporting Source");
                    }
                }
                if (this.isMbrRenewalFg()) {
                    if (this.affiliateIdCurrent.getType().charValue() != 'R' &&
                        this.affiliateIdCurrent.getType().charValue() != 'S'
                    ) {
                        errors.add("mbrRenewalFg", new ActionError("errors.massChange.optionInvalid", "Retiree Chapters and Retiree Sub Chapters"));
                        logger.debug("      added errors.massChange.optionInvalid for Retiree Chapters and Retiree Sub Chapters");
                    }
                }
                // don't validate anything for mbrCardBypassFg or peMailBypassFg
            }
        }
        logger.debug("   returning " + errors.size() + " error(s).");
        return errors;
    }
    
// General Methods...
    
    private boolean hasAffiliateIdentifierErrors(AffiliateIdentifier affId, List actionErrors) {
        if (TextUtil.isEmptyOrSpaces(affId.getType())) {
            actionErrors.add(new ActionError("error.field.required.generic", "Type"));
            logger.debug("      added error.field.required.generic for Type");
        } else {
            switch (affId.getType().charValue()) {
                // Check for valid status based on Affiliate Type.
                case 'U':
                    if (TextUtil.isEmptyOrSpaces(affId.getSubUnit())) {
                        actionErrors.add(new ActionError("error.affiliate.conditional.subUnit"));
                        logger.debug("      added error.affiliate.conditional.subUnit");
                    } else if (!TextUtil.isAlphaNumeric(affId.getSubUnit())) {
                        actionErrors.add(new ActionError("error.field.mustBeAlphaNumeric.generic", "Sub Unit"));
                        logger.debug("      added error.field.mustBeAlphaNumeric.generic for Sub Unit");
                    }
                    break;
                case 'S':
                case 'L':
                    if (TextUtil.isEmptyOrSpaces(affId.getLocal())) {
                        actionErrors.add(new ActionError("error.affiliate.conditional.local"));
                        logger.debug("      added error.affiliate.conditional.local");
                    } else if (!TextUtil.isInt(affId.getLocal())) {
                        actionErrors.add(new ActionError("error.field.mustBeInt.generic", "Loc/Sub Chap"));
                        logger.debug("      added error.field.mustBeInt.generic for Loc/Sub Chap");
                    }                                      
                    break;
                case 'R':
                case 'C':
                    if (TextUtil.isEmptyOrSpaces(affId.getCouncil())) {
                        actionErrors.add(new ActionError("error.affiliate.conditional.council"));
                        logger.debug("      added error.affiliate.conditional.council");
                    } else if (!TextUtil.isInt(affId.getCouncil())) {
                        actionErrors.add(new ActionError("error.field.mustBeInt.generic", "CN/Ret Chap"));
                        logger.debug("      added error.field.mustBeInt.generic for CN/Ret Chap");
                    } 
                    break;
                case '#':
                    if (TextUtil.isEmptyOrSpaces(affiliateIdCurrent.getCouncil()) && TextUtil.isEmptyOrSpaces(affId.getCouncil())) {
                        actionErrors.add(new ActionError("errors.massChange.council.required", "CN/Ret Chap"));
                    }
                default:                    
                    break;
            }
        }
        if (TextUtil.isEmptyOrSpaces(affId.getState())) {
            if (!(!TextUtil.isEmptyOrSpaces(affId.getType()) && affId.getType().charValue() == '#')) {                
                actionErrors.add(new ActionError("error.field.required.generic", "State/Nat'l"));
                logger.debug("      added error.field.required.generic for State/Nat'l");
            }
        }
        logger.debug(" ******** actionErrors: " + actionErrors);
        return !CollectionUtil.isEmpty(actionErrors);
    }
    
    protected void init() {
        this.affPk = null;
        this.affiliateIdCurrent = new AffiliateIdentifier(null, null, null, null, null, null, null);
        this.massChangeSelect = null;
        this.statusChangeSelect = null;
        this.statusCurrent = null;
        this.mergedAffiliatePk = null;
        this.mergedAffiliate = new AffiliateIdentifier(null, null, null, null, null, null, null);
        this.splitAffiliatePk = null;
        this.splitAffiliate = new AffiliateIdentifier(null, null, null, null, null, null, null);
        this.infoSourceFg = false;
        this.infoSourceCurrent = null;
        this.infoSourceNew = null;
        this.mbrCardBypassFg = false;
        this.mbrCardBypassFgCurrent = false;
        this.mbrCardBypassFgNew = false;
        this.peMailBypassFg = false;
        this.peMailBypassFgCurrent = false;
        this.peMailBypassFgNew = false;
        this.mbrRenewalFg = false;
        this.mbrRenewalFgNew = false;
        this.newAffiliateFg = false;
        this.newAffiliatePk = null;
        this.newAffiliate = new AffiliateIdentifier(null, null, null, null, null, null, null);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("affPk=");
        sb.append(this.affPk);
        sb.append(", affiliateIdCurrent=");
        sb.append(this.affiliateIdCurrent);
        sb.append(", massChangeSelect=");
        sb.append(this.massChangeSelect);
        sb.append(", statusChangeSelect=");
        sb.append(this.statusChangeSelect);
        sb.append(", statusCurrent=");
        sb.append(this.statusCurrent);
        sb.append(", mergedAffiliatePk=");
        sb.append(this.mergedAffiliatePk);
        sb.append(", mergedAffiliate=");
        sb.append(this.mergedAffiliate);
        sb.append(", splitAffiliatePk=");
        sb.append(this.splitAffiliatePk);
        sb.append(", splitAffiliate=");
        sb.append(this.splitAffiliate);
        sb.append(", infoSourceFg=");
        sb.append(this.infoSourceFg);
        sb.append(", infoSourceCurrent=");
        sb.append(this.infoSourceCurrent);
        sb.append(", infoSourceNew=");
        sb.append(this.infoSourceNew);
        sb.append(", mbrCardBypassFg=");
        sb.append(this.mbrCardBypassFg);
        sb.append(", mbrCardBypassFgCurrent=");
        sb.append(this.mbrCardBypassFgCurrent);
        sb.append(", mbrCardBypassFgNew=");
        sb.append(this.mbrCardBypassFgNew);
        sb.append(", peMailBypassFg=");
        sb.append(this.peMailBypassFg);
        sb.append(", peMailBypassFgCurrent=");
        sb.append(this.peMailBypassFgCurrent);
        sb.append(", peMailBypassFgNew=");
        sb.append(this.peMailBypassFgNew);
        sb.append(", mbrRenewalFg=");
        sb.append(this.mbrRenewalFg);
        sb.append(", mbrRenewalFgNew=");
        sb.append(this.mbrRenewalFgNew);
        sb.append(", newAffiliateFg=");
        sb.append(this.newAffiliateFg);
        sb.append(", newAffiliatePk=");
        sb.append(this.newAffiliatePk);
        sb.append(", newAffiliate=");
        sb.append(this.newAffiliate);
        sb.append("}");
        return sb.toString().trim();
    }
    
// Getter and Setter Methods...
    
    /** Getter for property infoSourceFg.
     * @return Value of property infoSourceFg.
     *
     */
    public boolean isInfoSourceFg() {
        return infoSourceFg;
    }
    
    /** Setter for property infoSourceFg.
     * @param infoSourceFg New value of property infoSourceFg.
     *
     */
    public void setInfoSourceFg(boolean infoSourceFg) {
        this.infoSourceFg = infoSourceFg;
    }
    
    /** Getter for property infoSourceNew.
     * @return Value of property infoSourceNew.
     *
     */
    public Integer getInfoSourceNew() {
        return infoSourceNew;
    }
    
    /** Setter for property infoSourceNew.
     * @param infoSourceNew New value of property infoSourceNew.
     *
     */
    public void setInfoSourceNew(Integer infoSourceNew) {
        if (infoSourceNew == null || infoSourceNew.intValue() < 1) {
            this.infoSourceNew = null;
        } else {
            this.infoSourceNew = infoSourceNew;
        }
    }
    
    /** Getter for property massChangeSelect.
     * @return Value of property massChangeSelect.
     *
     */
    public Integer getMassChangeSelect() {
        return massChangeSelect;
    }
    
    /** Setter for property massChangeSelect.
     * @param massChangeSelect New value of property massChangeSelect.
     *
     */
    public void setMassChangeSelect(Integer massChangeSelect) {
        if (massChangeSelect == null || massChangeSelect.intValue() < 1) {
            this.massChangeSelect = null;
        } else {
            this.massChangeSelect = massChangeSelect;
        }
    }
    
    /** Getter for property mbrCardBypassFg.
     * @return Value of property mbrCardBypassFg.
     *
     */
    public boolean isMbrCardBypassFg() {
        return mbrCardBypassFg;
    }
    
    /** Setter for property mbrCardBypassFg.
     * @param mbrCardBypassFg New value of property mbrCardBypassFg.
     *
     */
    public void setMbrCardBypassFg(boolean mbrCardBypassFg) {
        this.mbrCardBypassFg = mbrCardBypassFg;
    }
    
    /** Getter for property mbrCardBypassFgCurrent.
     * @return Value of property mbrCardBypassFgCurrent.
     *
     */
    public boolean isMbrCardBypassFgCurrent() {
        return mbrCardBypassFgCurrent;
    }
    
    /** Setter for property mbrCardBypassFgCurrent.
     * @param mbrCardBypassFgCurrent New value of property mbrCardBypassFgCurrent.
     *
     */
    public void setMbrCardBypassFgCurrent(boolean mbrCardBypassFgCurrent) {
        this.mbrCardBypassFgCurrent = mbrCardBypassFgCurrent;
    }
    
    /** Getter for property mbrCardBypassFgNew.
     * @return Value of property mbrCardBypassFgNew.
     *
     */
    public boolean isMbrCardBypassFgNew() {
        return mbrCardBypassFgNew;
    }
    
    /** Setter for property mbrCardBypassFgNew.
     * @param mbrCardBypassFgNew New value of property mbrCardBypassFgNew.
     *
     */
    public void setMbrCardBypassFgNew(boolean mbrCardBypassFgNew) {
        this.mbrCardBypassFgNew = mbrCardBypassFgNew;
    }
    
    /** Getter for property mbrRenewalFg.
     * @return Value of property mbrRenewalFg.
     *
     */
    public boolean isMbrRenewalFg() {
        return mbrRenewalFg;
    }
    
    /** Setter for property mbrRenewalFg.
     * @param mbrRenewalFg New value of property mbrRenewalFg.
     *
     */
    public void setMbrRenewalFg(boolean mbrRenewalFg) {
        this.mbrRenewalFg = mbrRenewalFg;
    }
    
    /** Getter for property mbrRenewalFgNew.
     * @return Value of property mbrRenewalFgNew.
     *
     */
    public boolean isMbrRenewalFgNew() {
        return mbrRenewalFgNew;
    }
    
    /** Setter for property mbrRenewalFgNew.
     * @param mbrRenewalFgNew New value of property mbrRenewalFgNew.
     *
     */
    public void setMbrRenewalFgNew(boolean mbrRenewalFgNew) {
        this.mbrRenewalFgNew = mbrRenewalFgNew;
    }
    
    /** Getter for property mergedAffiliate.
     * @return Value of property mergedAffiliate.
     *
     */
    public AffiliateIdentifier getMergedAffiliate() {
        return mergedAffiliate;
    }
    
    /** Setter for property mergedAffiliate.
     * @param mergedAffiliate New value of property mergedAffiliate.
     *
     */
    public void setMergedAffiliate(AffiliateIdentifier mergedAffiliate) {
        if (mergedAffiliate == null) {
            this.mergedAffiliate = new AffiliateIdentifier(null, null, null, null, null, null, null);
        } else {
            this.mergedAffiliate = mergedAffiliate;
        }
    }
    
    /** Getter for property newAffiliate.
     * @return Value of property newAffiliate.
     *
     */
    public AffiliateIdentifier getNewAffiliate() {
        return newAffiliate;
    }
    
    /** Setter for property newAffiliate.
     * @param newAffiliate New value of property newAffiliate.
     *
     */
    public void setNewAffiliate(AffiliateIdentifier newAffiliate) {
        if (newAffiliate == null) {
            this.newAffiliate = new AffiliateIdentifier(null, null, null, null, null, null, null);
        } else {
            this.newAffiliate = newAffiliate;
        }
    }
    
    /** Getter for property newAffiliateFg.
     * @return Value of property newAffiliateFg.
     *
     */
    public boolean isNewAffiliateFg() {
        return newAffiliateFg;
    }
    
    /** Setter for property newAffiliateFg.
     * @param newAffiliateFg New value of property newAffiliateFg.
     *
     */
    public void setNewAffiliateFg(boolean newAffiliateFg) {
        this.newAffiliateFg = newAffiliateFg;
    }
    
    /** Getter for property peMailBypassFg.
     * @return Value of property peMailBypassFg.
     *
     */
    public boolean isPeMailBypassFg() {
        return peMailBypassFg;
    }
    
    /** Setter for property peMailBypassFg.
     * @param peMailBypassFg New value of property peMailBypassFg.
     *
     */
    public void setPeMailBypassFg(boolean peMailBypassFg) {
        this.peMailBypassFg = peMailBypassFg;
    }
    
    /** Getter for property peMailBypassFgCurrent.
     * @return Value of property peMailBypassFgCurrent.
     *
     */
    public boolean isPeMailBypassFgCurrent() {
        return peMailBypassFgCurrent;
    }
    
    /** Setter for property peMailBypassFgCurrent.
     * @param peMailBypassFgCurrent New value of property peMailBypassFgCurrent.
     *
     */
    public void setPeMailBypassFgCurrent(boolean peMailBypassFgCurrent) {
        this.peMailBypassFgCurrent = peMailBypassFgCurrent;
    }
    
    /** Getter for property peMailBypassFgNew.
     * @return Value of property peMailBypassFgNew.
     *
     */
    public boolean isPeMailBypassFgNew() {
        return peMailBypassFgNew;
    }
    
    /** Setter for property peMailBypassFgNew.
     * @param peMailBypassFgNew New value of property peMailBypassFgNew.
     *
     */
    public void setPeMailBypassFgNew(boolean peMailBypassFgNew) {
        this.peMailBypassFgNew = peMailBypassFgNew;
    }
    
    /** Getter for property splitAffiliate.
     * @return Value of property splitAffiliate.
     *
     */
    public AffiliateIdentifier getSplitAffiliate() {
        return splitAffiliate;
    }
    
    /** Setter for property splitAffiliate.
     * @param splitAffiliate New value of property splitAffiliate.
     *
     */
    public void setSplitAffiliate(AffiliateIdentifier splitAffiliate) {
        if (splitAffiliate == null) {
            this.splitAffiliate = new AffiliateIdentifier(null, null, null, null, null, null, null);
        } else {
            this.splitAffiliate = splitAffiliate;
        }
    }
    
    /** Getter for property statusChangeSelect.
     * @return Value of property statusChangeSelect.
     *
     */
    public Integer getStatusChangeSelect() {
        return statusChangeSelect;
    }
    
    /** Setter for property statusChangeSelect.
     * @param statusChangeSelect New value of property statusChangeSelect.
     *
     */
    public void setStatusChangeSelect(Integer statusChangeSelect) {
        if (statusChangeSelect == null || statusChangeSelect.intValue() < 1) {
            this.statusChangeSelect = null;
        } else {
            this.statusChangeSelect = statusChangeSelect;
        }
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
    
    /** Getter for property infoSourceCurrent.
     * @return Value of property infoSourceCurrent.
     *
     */
    public Integer getInfoSourceCurrent() {
        return infoSourceCurrent;
    }
    
    /** Setter for property infoSourceCurrent.
     * @param infoSourceCurrent New value of property infoSourceCurrent.
     *
     */
    public void setInfoSourceCurrent(Integer infoSourceCurrent) {
        if (infoSourceCurrent == null || infoSourceCurrent.intValue() < 1) {
            this.infoSourceCurrent = null;
        } else {
            this.infoSourceCurrent = infoSourceCurrent;
        }
    }
    
    /** Getter for property statusCurrent.
     * @return Value of property statusCurrent.
     *
     */
    public Integer getStatusCurrent() {
        return statusCurrent;
    }
    
    /** Setter for property statusCurrent.
     * @param statusCurrent New value of property statusCurrent.
     *
     */
    public void setStatusCurrent(Integer statusCurrent) {
        if (statusCurrent == null || statusCurrent.intValue() < 1) {
            this.statusCurrent = null;
        } else {
            this.statusCurrent = statusCurrent;
        }
    }
    
    /** Getter for property affiliateIdCurrent.
     * @return Value of property affiliateIdCurrent.
     *
     */
    public AffiliateIdentifier getAffiliateIdCurrent() {
        return affiliateIdCurrent;
    }
    
    /** Setter for property affiliateIdCurrent.
     * @param affiliateIdCurrent New value of property affiliateIdCurrent.
     *
     */
    public void setAffiliateIdCurrent(AffiliateIdentifier affiliateIdCurrent) {
        if (affiliateIdCurrent == null) {
            this.affiliateIdCurrent = new AffiliateIdentifier(null, null, null, null, null, null, null);
        } else {
            this.affiliateIdCurrent = affiliateIdCurrent;
        }
    }
    
    /** Getter for property mergedAffiliatePk.
     * @return Value of property mergedAffiliatePk.
     *
     */
    public Integer getMergedAffiliatePk() {
        return mergedAffiliatePk;
    }
    
    /** Setter for property mergedAffiliatePk.
     * @param mergedAffiliatePk New value of property mergedAffiliatePk.
     *
     */
    public void setMergedAffiliatePk(Integer mergedAffiliatePk) {
        if (mergedAffiliatePk == null || mergedAffiliatePk.intValue() < 1) {
          this.mergedAffiliatePk = null;   
        } else {
            this.mergedAffiliatePk = mergedAffiliatePk;
        }
    }
    
    /** Getter for property splitAffiliatePk.
     * @return Value of property splitAffiliatePk.
     *
     */
    public Integer getSplitAffiliatePk() {
        return splitAffiliatePk;
    }
    
    /** Setter for property splitAffiliatePk.
     * @param splitAffiliatePk New value of property splitAffiliatePk.
     *
     */
    public void setSplitAffiliatePk(Integer splitAffiliatePk) {
        if (splitAffiliatePk == null || splitAffiliatePk.intValue() < 1) {
          this.splitAffiliatePk = null;   
        } else {
            this.splitAffiliatePk = splitAffiliatePk;
        }
    }
    
    /** Getter for property newAffiliatePk.
     * @return Value of property newAffiliatePk.
     *
     */
    public Integer getNewAffiliatePk() {
        return newAffiliatePk;
    }
    
    /** Setter for property newAffiliatePk.
     * @param newAffiliatePk New value of property newAffiliatePk.
     *
     */
    public void setNewAffiliatePk(Integer newAffiliatePk) {
        if (newAffiliatePk == null || newAffiliatePk.intValue() < 1) {
          this.newAffiliatePk = null;   
        } else {
            this.newAffiliatePk = newAffiliatePk;
        }
    }
    
}
