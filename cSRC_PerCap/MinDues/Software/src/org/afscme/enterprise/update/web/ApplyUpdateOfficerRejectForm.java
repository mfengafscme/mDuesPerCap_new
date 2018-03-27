/*
 * ApplyUpdateMemberRejectForm.java
 *
 * Created on August 20, 2003, 1:38 PM
 */

package org.afscme.enterprise.update.web;

import java.text.ParseException;
import java.sql.Timestamp;
import org.apache.struts.action.*;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.codes.CodeData;
import org.apache.struts.upload.*;
import org.afscme.enterprise.util.TextUtil;
import java.util.Vector;
import org.apache.log4j.Logger;
/**
 * @struts:form name="ApplyUpdateOfficerRejectForm"
 */
public class ApplyUpdateOfficerRejectForm extends ActionForm{
    
    //**********************************************************************
    protected String    comments    =   "";
    protected Integer   queuePk     =   null;
    protected Integer   affPk       =   null;
    //**********************************************************************
    
    private static Logger logger = Logger.getLogger(ApplyUpdateOfficerRejectForm.class);
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     *
     */
    public void setComments(String comments){
        this.comments = comments;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     *
     */
      
    public String getComments(){
        return comments;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk){
        this.affPk = affPk;
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
      
    public Integer getAffPk(){
        return affPk;
    }
    
    /** Setter for property QueuePk.
     * @param pk New value of property QueuePk.
     *
     */
    public void setQueuePk(Integer pk){
        this.queuePk = pk;
    }
    
    /** Getter for property QueuePk.
     * @return Value of property QueuePk.
     *
     */
      
    public Integer getQueuePk(){
        return queuePk;
    }
    /************************************************************************************/
     /** Form's validation method */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        logger.debug("----------------------------------------------------");
        logger.debug("validate called.");
        
        ActionErrors errors = new ActionErrors();
        if (comments == null || comments.length() == 0) {
            request.setAttribute(new String("queuePk"), queuePk);
            request.setAttribute(new String("affPk"), affPk);
            errors.add("comments", new ActionError("error.update.comments"));
        }
        return errors;
    }
    /************************************************************************************/
}

    

