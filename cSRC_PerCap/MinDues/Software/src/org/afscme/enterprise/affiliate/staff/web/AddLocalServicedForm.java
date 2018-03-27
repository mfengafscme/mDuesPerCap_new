package org.afscme.enterprise.affiliate.staff.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;

import org.afscme.enterprise.affiliate.AffiliateCriteria;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.common.web.SearchForm;

/**
 *
 * @struts:form name="addLocalServicedForm"
 */
public class AddLocalServicedForm extends SearchForm 
{
    protected Character affIdType;
    protected String affIdLocal;
    protected String affIdState;
    protected String affIdSubUnit;
    protected String affIdCouncil;
    protected String affTypeExcludes;
    protected boolean localLocked;
    protected boolean stateLocked;
    
    
    protected static Logger log = Logger.getLogger(AddLocalServicedForm.class);

    public AddLocalServicedForm() {
        log.debug("In ALS constructor");
    }
    
    public String toString() {
        return "[" + 
            "affIdType="+affIdType+", "+
            "affIdLocal="+affIdLocal+", "+
            "affIdState="+affIdState+", "+
            "affIdSubUnit="+affIdSubUnit+", "+
            "affIdCouncil="+affIdCouncil+", "+
            "affTypeExcludes="+affTypeExcludes+", "+
            "localLocked="+localLocked+", "+
            "super="+super.toString()+"]";
    }
    
    public void setAffiliateId(AffiliateIdentifier affId) throws JspException {
    
        log.debug("Setting affId to " + affId);
        
        affIdCouncil = affId.getCouncil();
        affIdState = affId.getState();
        
        switch (affId.getType().charValue()) {
            case 'C':
                affTypeExcludes="R,S";
                affIdType = new Character('L');
                localLocked = false;
				stateLocked = false;
                break;
            case 'L':
                affTypeExcludes="R,S,C";
                affIdLocal = affId.getLocal();
                localLocked = true;
                stateLocked = true;
                affIdType = new Character('L');
                break;
            case 'R':
                affTypeExcludes="C,L,U";
                stateLocked = true;
                localLocked = false;
                affIdType = new Character('S');
                break;
            default:
                throw new JspException("Cannot have an affiliate staff who's affiliate is of type 'S' or 'U'");
        }
    }
    
    public AffiliateCriteria getAffiliateCriteria() {
        AffiliateCriteria ac = new AffiliateCriteria();
        ac.setAffiliateIdCouncil(affIdCouncil);
        ac.setAffiliateIdLocal(affIdLocal);
        ac.setAffiliateIdState(affIdState);
        ac.setAffiliateIdSubUnit(affIdSubUnit);
        ac.setAffiliateIdType(affIdType);
        ac.setPage(page);
        ac.setPageSize(pageSize);
        ac.setOrderBy(getSortBy());
        ac.setOrdering(order);
        return ac;
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
    
    /** Getter for property affIdType.
     * @return Value of property affIdType.
     *
     */
    public java.lang.Character getAffIdType() {
        return affIdType;
    }
    
    /** Setter for property affIdType.
     * @param affIdType New value of property affIdType.
     *
     */
    public void setAffIdType(java.lang.Character affIdType) {
        this.affIdType = affIdType;
    }
    
    /** Getter for property affTypeExcludes.
     * @return Value of property affTypeExcludes.
     *
     */
    public java.lang.String getAffTypeExcludes() {
        return affTypeExcludes;
    }
    
    /** Getter for property localLocked.
     * @return Value of property localLocked.
     *
     */
    public boolean isLocalLocked() {
        return localLocked;
    }    
    
    /** Setter for property localLocked.
     * @param localLocked New value of property localLocked.
     *
     */
    public void setLocalLocked(boolean localLocked) {
        this.localLocked = localLocked;
    }
    
    /** getter for property stateLocked
     * @return Returns the stateLocked.
     */
    public boolean isStateLocked()
    {
        return stateLocked;
    }

    /**setter for property stateLocked
     * @param stateLocked The stateLocked to set.
     */
    public void setStateLocked(boolean stateLocked)
    {
        this.stateLocked = stateLocked;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        // TODO Auto-generated method stub
        super.reset(arg0, arg1);
		affIdType = null;
		affIdLocal = null;
		affIdState = null;
		affIdSubUnit = null;
		affIdCouncil = null;
		affTypeExcludes = null;
		localLocked = false;
		stateLocked = false;
    }

}