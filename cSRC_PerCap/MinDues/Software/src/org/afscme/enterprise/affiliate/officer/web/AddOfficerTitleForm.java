package org.afscme.enterprise.affiliate.officer.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports

import java.util.*;
import java.text.*;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.ConstitutionData;
import org.afscme.enterprise.affiliate.officer.OfficeData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.common.web.SearchForm;
import org.apache.log4j.Logger;

/**
 * @struts:form name="addOfficerTitleForm"
 */
public class AddOfficerTitleForm extends SearchForm {

    private static Logger logger =  Logger.getLogger(AddOfficerTitleForm.class);       
    
    protected Integer  aff_Pk;
    protected Integer  afscmeTitle;
    protected String   affiliateTitle;
    protected Integer  monthOfElection;
    protected Integer  lengthOfTerm;
    protected Integer  numWithTitle;
    protected Integer  termEnd;
    protected Integer  delegatePriority;
    protected Boolean  reportingOfficer;
    protected Boolean  execBoard;
    protected Boolean  presidentExists;
    protected Integer  termMonth;
    protected Integer  termYear;
    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        reportingOfficer = new Boolean(false); 
        execBoard = new Boolean(false); 
    }
    
// Struts Methods...

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        logger.debug("Inside AddOfficerTitleForm.validate().");
        logger.debug("AfscmeTitle = " + afscmeTitle);
        logger.debug("MonthOfElection = " + monthOfElection);
        logger.debug("LengthOfTerm = " + lengthOfTerm);
        logger.debug("NumWithTitle = " + numWithTitle);
        logger.debug("TermEnd = " + termEnd);
        logger.debug("DelegatePriority = " + delegatePriority);
        logger.debug("ReportingOfficer = " + reportingOfficer);
        logger.debug("EBoard = " + execBoard);
        ActionErrors errors = new ActionErrors();

        //Check for one president
        if ((afscmeTitle.intValue() == 9) && (presidentExists.booleanValue() == true)) {
            errors.add("afscmeTitle", new ActionError("error.president.exists"));
        }        
        
        if ((afscmeTitle.intValue() == 9) && (numWithTitle.intValue() != 1)) {
            errors.add("numWithTitle", new ActionError("error.invalid.numwithtitle"));
        }      
        
        //Check Required fields
        if ((afscmeTitle == null)|| (afscmeTitle.intValue() == 0)) {
            errors.add("afscmeTitle", new ActionError("error.field.required.generic", "Constitutional Title"));
        }
        
        if ((numWithTitle == null) || (numWithTitle.intValue() == 0)) {
            errors.add("numWithTitle", new ActionError("error.field.required.generic", "Number with Title"));
        }
        
        if ((monthOfElection == null) || (monthOfElection.intValue() == 0)) {
            errors.add("monthOfElection", new ActionError("error.field.required.generic", "Month of Election"));
        }
            
        // validate that indefinite terms only exist for the following offices
        // Financial Reporting Officer=6032, Administrator=6004, Steward=6046, Executive Director=6006 or Director=6007
        if ((lengthOfTerm.intValue() == 63005 ) && ((afscmeTitle.intValue() != 4 ) && 
            (afscmeTitle.intValue() != 6 ) &&
            (afscmeTitle.intValue() != 7 ) &&
            (afscmeTitle.intValue() != 32 ) &&
            (afscmeTitle.intValue() != 46 ))) { 
                         
            errors.add("lengthOfTerm", new ActionError("error.invalid.termLength"));
            
        } else if ((lengthOfTerm == null) || (lengthOfTerm.intValue() == 0) &&
                   (lengthOfTerm.intValue() != 63005) && (lengthOfTerm.intValue() != 63006)) {
               errors.add("lengthOfTerm", new ActionError("error.field.required.generic", "Length of Term"));
        } 
        
        //check that termEnd is entered unless lengthOfTerm == indefinite=63005 or temporary=63006
        if ((lengthOfTerm.intValue() != 63005 ) && (lengthOfTerm.intValue() != 63006) && (afscmeTitle.intValue() != 46)) { 
                   
            if ((termEnd == null) || (termEnd.intValue() == 0)) {
               errors.add("termEnd", new ActionError("error.field.required.generic", "Term End Date"));
            }           
        }      
        
        //validate intial length of term
        if (((lengthOfTerm.intValue() != 63005 ) &&
             (lengthOfTerm.intValue() != 63006) && 
             (lengthOfTerm.intValue() != 0) && 
             (lengthOfTerm != null)) &&
            ((monthOfElection != null) ||
             (monthOfElection.intValue() != 0)) && 
            ((termEnd != null) ||
             (termEnd.intValue() != 0)) && 
             (afscmeTitle.intValue() != 46)) {
             
            GregorianCalendar calendar = new GregorianCalendar();
            Date now  = calendar.getTime();
            int month = calendar.get(calendar.MONTH) + 1;
            int year  = calendar.get(calendar.YEAR);
            
            if (monthOfElection.intValue() == 78001) {
               termMonth = new Integer(1);
            } else if (monthOfElection.intValue() == 78002) {
               termMonth = new Integer(2);
            } else if (monthOfElection.intValue() == 78003) {
               termMonth = new Integer(3);
            } else if (monthOfElection.intValue() == 78004) {
               termMonth = new Integer(4);
            } else if (monthOfElection.intValue() == 78005) {
               termMonth = new Integer(5);
            } else if (monthOfElection.intValue() == 78006) {
               termMonth = new Integer(6);
            } else if (monthOfElection.intValue() == 78007) {
               termMonth = new Integer(7);
            } else if (monthOfElection.intValue() == 78008) {
               termMonth = new Integer(8);
            } else if (monthOfElection.intValue() == 78009) {
               termMonth = new Integer(9);
            } else if (monthOfElection.intValue() == 78010) {
               termMonth = new Integer(10);
            } else if (monthOfElection.intValue() == 78011) {
               termMonth = new Integer(11);
            } else if (monthOfElection.intValue() == 78012) {
               termMonth = new Integer(12);
            } 
                      
            termYear = new Integer (termEnd.intValue());
           
            logger.debug("termMonth = " + termMonth);
            logger.debug("termYear = " + termYear);
            logger.debug("month = " + month);
            logger.debug("year = " + year);
            
            //months (jan-dec)
            if ((monthOfElection.intValue() != 78013) &&
               (monthOfElection.intValue() != 78014) &&
               (monthOfElection.intValue() != 78015) &&
               (monthOfElection.intValue() != 78016)) {
                
                if (termYear.intValue() < year) {

                   errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));

                } else if ((termMonth.intValue() < month) && (termYear.intValue() == year)) {

                  errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));

                }
                
            //1st quarter (month = 1,2,3)  
            } else if (monthOfElection.intValue() == 78013) {
            
                if ((termYear.intValue() == year) && (month > 3)) { 
                    
                    errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
                
                } else if (termYear.intValue() < year) {   
                    
                    errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
                
                } 
                
            //2nd quarter (month = 4,5,6)     
            } else if (monthOfElection.intValue() == 78014) {
            
                 if ((termYear.intValue() == year) && (month > 6)) { 
                    
                    errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
                
                 } else if (termYear.intValue() < year) {   
                    
                    errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
                
                } 
                 
            //3rd quarter (month = 7,8,9)
            } else if (monthOfElection.intValue() == 78015) {
            
                if ((termYear.intValue() == year) && (month > 9)) { 
                    
                    errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
                
                 } else if (termYear.intValue() < year) {   
                    
                    errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
                
                } 
                
            //4th quarter  (month = 10,11,12)
            } else if (monthOfElection.intValue() == 78016) {
            
                if (termYear.intValue() < year) {   
                    
                    errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
                
                } 
            }  
            
        }
        
        logger.debug("**************** Returning " + errors.size() + " error(s).");
        return errors;
    }             

    /** Getter for property aff_Pk.
     * @return Value of property aff_Pk.
     *
     */
    public java.lang.Integer getAff_Pk() {
        return aff_Pk;
    }    
 
    /** Setter for property aff_Pk.
     * @param aff_Pk New value of property aff_Pk.
     *
     */
    public void setAff_Pk(java.lang.Integer aff_Pk) {
        this.aff_Pk = aff_Pk;
    }    
    
    /** Getter for property afscmeTitle.
     * @return Value of property afscmeTitle.
     *
     */
    public java.lang.Integer getAfscmeTitle() {
        return afscmeTitle;
    }
    
    /** Setter for property afscmeTitle.
     * @param afscmeTitle New value of property afscmeTitle.
     *
     */
    public void setAfscmeTitle(java.lang.Integer afscmeTitle) {
        this.afscmeTitle = afscmeTitle;
    }
    
    /** Getter for property affiliateTitle.
     * @return Value of property affiliateTitle.
     *
     */
    public java.lang.String getAffiliateTitle() {
        return affiliateTitle;
    }
    
    /** Setter for property affiliateTitle.
     * @param affiliateTitle New value of property affiliateTitle.
     *
     */
    public void setAffiliateTitle(java.lang.String affiliateTitle) {
        this.affiliateTitle = affiliateTitle;
    }
    
    /** Getter for property monthOfElection.
     * @return Value of property monthOfElection.
     *
     */
    public java.lang.Integer getMonthOfElection() {
        return monthOfElection;
    }
    
    /** Setter for property monthOfElection.
     * @param monthOfElection New value of property monthOfElection.
     *
     */
    public void setMonthOfElection(java.lang.Integer monthOfElection) {
        this.monthOfElection = monthOfElection;
    }
    
    /** Getter for property lengthOfTerm.
     * @return Value of property lengthOfTerm.
     *
     */
    public java.lang.Integer getLengthOfTerm() {
        return lengthOfTerm;
    }
    
    /** Setter for property lengthOfTerm.
     * @param lengthOfTerm New value of property lengthOfTerm.
     *
     */
    public void setLengthOfTerm(java.lang.Integer lengthOfTerm) {
        this.lengthOfTerm = lengthOfTerm;
    }
    
    /** Getter for property numWithTitle.
     * @return Value of property numWithTitle.
     *
     */
    public java.lang.Integer getNumWithTitle() {
        return numWithTitle;
    }
    
    /** Setter for property numWithTitle.
     * @param numWithTitle New value of property numWithTitle.
     *
     */
    public void setNumWithTitle(java.lang.Integer numWithTitle) {
        this.numWithTitle = numWithTitle;
    }
    
    /** Getter for property termEnd.
     * @return Value of property termEnd.
     *
     */
    public java.lang.Integer getTermEnd() {
        return termEnd;
    }
    
    /** Setter for property termEnd.
     * @param termEnd New value of property termEnd.
     *
     */
    public void setTermEnd(java.lang.Integer termEnd) {
        this.termEnd = termEnd;
    }
    
    /** Getter for property delegatePriority.
     * @return Value of property delegatePriority.
     *
     */
    public java.lang.Integer getDelegatePriority() {
        return delegatePriority;
    }
    
    /** Setter for property delegatePriority.
     * @param delegatePriority New value of property delegatePriority.
     *
     */
    public void setDelegatePriority(java.lang.Integer delegatePriority) {
        this.delegatePriority = delegatePriority;
    }
    
    /** Getter for property rO.
     * @return Value of property rO.
     *
     */
    public java.lang.Boolean getReportingOfficer() {
        return reportingOfficer;
    }
    
    /** Setter for property rO.
     * @param rO New value of property rO.
     *
     */
    public void setReportingOfficer(java.lang.Boolean reportingOfficer) {
        this.reportingOfficer = reportingOfficer;
    }
    
    /** Getter for property eBoard.
     * @return Value of property eBoard.
     *
     */
    public java.lang.Boolean getExecBoard() {
        return execBoard;
    }
    
    /** Setter for property eBoard.
     * @param eBoard New value of property eBoard.
     *
     */
    public void setExecBoard(java.lang.Boolean execBoard) {
        this.execBoard = execBoard;
    }
    
    /** Getter for property presidentExists.
     * @return Value of property presidentExists.
     *
     */
    public java.lang.Boolean getPresidentExists() {
        return presidentExists;
    }
    
    /** Setter for property presidentExists.
     * @param presidentExists New value of property presidentExists.
     *
     */
    public void setPresidentExists(java.lang.Boolean presidentExists) {
        this.presidentExists = presidentExists;
    }
    
}
