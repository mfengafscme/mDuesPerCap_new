package org.afscme.enterprise.update.web;

import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.CollectionUtil;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:form name="applyUpdateForm"
 */
public class ApplyUpdateForm extends ActionForm {
    
    private static Logger logger = Logger.getLogger(ApplyUpdateForm.class);
    
    private Integer queuePk     =   null;
    private Integer[] affPks    =   null;
    
    /** Creates a new instance of ApplyUpdateForm */
    public ApplyUpdateForm() {
        
    }
    
    public String toString() {
        return "ApplyUpdateForm[" + 
                    "queuePk=" + queuePk +
                    ", affPks=" + CollectionUtil.toString(affPks) + 
                "]"
        ;
    }
    
    /** Form's validation method */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        logger.debug("----------------------------------------------------");
        logger.debug("validate called.");
        
        ActionErrors errors = new ActionErrors();
        if (affPks == null)  {
            logger.debug("affpk is null");
            errors.add("affPks", new ActionError("error.update.missingAffiliates"));
        }
        return errors;
    }
    
    /** Getter for property affPks.
     * @return Value of property affPks.
     *
     */
    public Integer[] getAffPks() {
        return this.affPks;
    }
    
    /** Setter for property affPks.
     * @param affPks New value of property affPks.
     *
     */
    public void setAffPks(Integer[] affPks) {
        this.affPks = affPks;
    }
    
    /** Getter for property queuePk.
     * @return Value of property queuePk.
     *
     */
    public Integer getQueuePk() {
        return queuePk;
    }
    
    /** Setter for property queuePk.
     * @param queuePk New value of property queuePk.
     *
     */
    public void setQueuePk(Integer queuePk) {
        this.queuePk = queuePk;
    }
    
}
