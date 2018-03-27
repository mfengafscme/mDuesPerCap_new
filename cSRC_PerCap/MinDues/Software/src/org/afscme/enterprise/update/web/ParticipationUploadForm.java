package org.afscme.enterprise.update.web;

import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.web.WebUtil;
import org.apache.log4j.Logger;


/**
 * @struts:form name="participationUploadForm"
 */
public class ParticipationUploadForm extends ActionForm {
    
    private static Logger logger = Logger.getLogger(ParticipationUploadForm.class);
    
    protected FormFile file = null;
    
    protected String validDateStr = null;
    protected Timestamp validDate = null;

    
    public ParticipationUploadForm() {
        validDate = DateUtil.getCurrentDateAsTimestamp();
        validDateStr = DateUtil.getSimpleDateString(validDate);
    }
    
   
    /** Form's validation method */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if ((file == null) || (TextUtil.isEmptyOrSpaces(file.getFileName()))) {
            errors.add("file", new ActionError("error.field.update.fileNotFound"));
        }
        
        validDate = WebUtil.checkRequiredDate("validDateStr", validDateStr, errors);
        
        return errors;
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
    public java.lang.String getValidDateStr() {
        return validDateStr;
    }    

    /** Setter for property validDateStr.
     * @param validDateStr New value of property validDateStr.
     */
    public void setValidDateStr(java.lang.String validDateStr) {
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
    
}



