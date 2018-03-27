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
;

/**
 * @struts:form name="processLogForm"
 */
public class ProcessLogForm extends SearchForm {
    
    private static Logger logger = Logger.getLogger(ProcessLogForm.class);
    
    private String log;
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
    
    /** Creates a new instance of ProcessLogForm */
    public ProcessLogForm() {
        
    }
    
    
    
    /** Getter for property queuePk.
     * @return Value of property queuePk.
     *
     */
    public String getSearchLogButton() {
        return log;
    }
    
    /** Setter for property queuePk.
     * @param queuePk New value of property queuePk.
     *
     */
    public void setSearchLogButton(String log) {
        this.log = log;
    }
    
     
}

