package org.afscme.enterprise.affiliate.officer.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports

import java.util.*;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.officer.OfficeData;
import org.afscme.enterprise.common.RecordData;

/**
 * @struts:form name="editOfficerTitlesForm"
 */
public class EditOfficerTitlesForm extends ActionForm {

    protected Boolean  approvedConstitution;
    protected Boolean  autoDelegateProvision;
    protected Integer  affiliateTitlePk;
    protected Integer  subAffiliateTitlePk;
    protected String   comment;
    protected Integer  termMonth;
    protected Integer  termYear;
    
    private ArrayList officerTitlesList = new ArrayList();
    
    OfficeData officeData = new OfficeData();
    
    public String toString() {
        return null;
    } 
     
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        autoDelegateProvision = new Boolean(false);
        
    }
           
    //validate data returning errors
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
          System.out.println("Inside EditOfficerTitleForm.validate().");
//        System.out.println("AfscmeTitle = " + afscmeTitle);
//        System.out.println("MonthOfElection = " + monthOfElection);
//        System.out.println("LengthOfTerm = " + lengthOfTerm);
//        System.out.println("NumWithTitle = " + numWithTitle);
//        System.out.println("TermEnd = " + termEnd);
//        System.out.println("DelegatePriority = " + delegatePriority);
//        System.out.println("RO = " + reportingOfficer);
//        System.out.println("EBoard = " + execBoard);
        ActionErrors errors = new ActionErrors();

        //Check Required fields
        for (int i=0; i<officerTitlesList.size(); i++) {            
         
            officeData = (OfficeData) officerTitlesList.get(i);
                      
            if ((officeData.getNumWithTitle() == null) || (officeData.getNumWithTitle().intValue() == 0)) {
               errors.add("numWithTitle", new ActionError("error.field.required.generic", "numWithTitle"));
            }
            
            System.out.println("Passed numWithTitle "+ errors.size() );
            
            if ((officeData.getMonthOfElection() == null) || (officeData.getMonthOfElection().intValue() == 0)) {
               errors.add("monthOfElection", new ActionError("error.field.required.generic", "monthOfElection"));
            }
        
            System.out.println("Passed monthOfElection "+ errors.size() );
            
            if ((officeData.getLengthOfTerm() == null) || (officeData.getLengthOfTerm().intValue() == 0)) {
               errors.add("lengthOfTerm", new ActionError("error.field.required.generic", "lengthOfTerm"));
            }
            
            System.out.println("Passed LengthOfTerm "+ errors.size() );
            
            // validate that indefinite terms only exist for the following offices
            // Financial Reporting Officer=6032, Administrator=6004, Steward=6046, Executive Director=6006 or Director=6007
//            if ((officeData.getLengthOfTerm().intValue() == 63005 ) && ((officeData.getAfscmeTitle().intValue() != 6004 ) && 
//                (officeData.getAfscmeTitle().intValue() != 6006 ) &&
//                (officeData.getAfscmeTitle().intValue() != 6007 ) &&
//                (officeData.getAfscmeTitle().intValue() != 6032 ) &&
//                (officeData.getAfscmeTitle().intValue() != 6046 ))) { 
//
//                 errors.add("lengthOfTerm", new ActionError("error.invalid.termLength"));
//                
//            } else if ((officeData.getLengthOfTerm() == null) || (officeData.getLengthOfTerm().intValue() == 0)) {
//               errors.add("lengthOfTerm", new ActionError("error.field.required.generic", "Length of Term"));
//            }        
//           
//            System.out.println("Passed indefinite check "+ errors.size() );
//            
//            //check that termEnd is entered unless lengthOfTerm == indefinite=63005 or temporary=63006
//            if ((officeData.getLengthOfTerm().intValue() != 63005 ) && (officeData.getLengthOfTerm().intValue() != 63006)) { 
//
//                if ((officeData.getTermEnd() == null) || (officeData.getTermEnd().intValue() == 0)) {
//                   errors.add("termEnd", new ActionError("error.field.required.generic", "Term End Date"));
//                }           
//            }      
//           
//            System.out.println("Passed indefinite or temp check "+ errors.size() );
//            
//            //validate intial length of term
//            if (((officeData.getLengthOfTerm().intValue() != 63005 ) && 
//                 (officeData.getLengthOfTerm().intValue() != 63006)) && 
//                ((officeData.getTermEnd() != null) || 
//                 (officeData.getTermEnd().intValue() != 0)) &&
//                 (officeData.getMonthOfElection() != null) ||
//                 (officeData.getMonthOfElection().intValue() != 0)){
//
//                GregorianCalendar calendar = new GregorianCalendar();
//                Date now  = calendar.getTime();
//                int month = calendar.get(calendar.MONTH) + 1;
//                int year  = calendar.get(calendar.YEAR);
//
//                if (officeData.getMonthOfElection().intValue() == 78001) {
//                   termMonth = new Integer(1);
//                } else if (officeData.getMonthOfElection().intValue() == 78002) {
//                   termMonth = new Integer(2);
//                } else if (officeData.getMonthOfElection().intValue() == 78003) {
//                   termMonth = new Integer(3);
//                } else if (officeData.getMonthOfElection().intValue() == 78004) {
//                   termMonth = new Integer(4);
//                } else if (officeData.getMonthOfElection().intValue() == 78005) {
//                   termMonth = new Integer(5);
//                } else if (officeData.getMonthOfElection().intValue() == 78006) {
//                   termMonth = new Integer(6);
//                } else if (officeData.getMonthOfElection().intValue() == 78007) {
//                   termMonth = new Integer(7);
//                } else if (officeData.getMonthOfElection().intValue() == 78008) {
//                   termMonth = new Integer(8);
//                } else if (officeData.getMonthOfElection().intValue() == 78009) {
//                   termMonth = new Integer(9);
//                } else if (officeData.getMonthOfElection().intValue() == 78010) {
//                   termMonth = new Integer(10);
//                } else if (officeData.getMonthOfElection().intValue() == 78011) {
//                   termMonth = new Integer(11);
//                } else if (officeData.getMonthOfElection().intValue() == 78012) {
//                   termMonth = new Integer(12);
//                } 
//
//                termYear = new Integer (officeData.getTermEnd().intValue());
//
//                //months (jan-dec)
//                if ((officeData.getMonthOfElection().intValue() != 78013) &&
//                   (officeData.getMonthOfElection().intValue() != 78014) &&
//                   (officeData.getMonthOfElection().intValue() != 78015) &&
//                   (officeData.getMonthOfElection().intValue() != 78016)) {
//
//                    if ((termYear.intValue() < year)) {
//
//                       errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
//
//                    } else if ((termMonth.intValue() < month) && (termYear.intValue() == year)) {
//
//                      errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
//
//                    }
//
//                //1st quarter (month = 1,2,3)  
//                } else if (officeData.getMonthOfElection().intValue() == 78013) {
//
//                    if ((termYear.intValue() == year) && (month > 3)) { 
//
//                        errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
//
//                    } else if (termYear.intValue() < year) {   
//
//                        errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
//
//                    } 
//
//                //2nd quarter (month = 4,5,6)     
//                } else if (officeData.getMonthOfElection().intValue() == 78014) {
//
//                     if ((termYear.intValue() == year) && (month > 6)) { 
//
//                        errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
//
//                     } else if (termYear.intValue() < year) {   
//
//                        errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
//
//                    } 
//
//                //3rd quarter (month = 7,8,9)
//                } else if (officeData.getMonthOfElection().intValue() == 78015) {
//
//                    if ((termYear.intValue() == year) && (month > 9)) { 
//
//                        errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
//
//                     } else if (termYear.intValue() < year) {   
//
//                        errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
//
//                    } 
//
//                //4th quarter  (month = 10,11,12)
//                } else if (officeData.getMonthOfElection().intValue() == 78016) {
//
//                    if (termYear.intValue() < year) {   
//
//                        errors.add("monthOfElection", new ActionError("error.invalid.intialTerm"));
//
//                    } 
//                }  
//
//            }
            
     }
        
        System.out.println("**************** Returning " + errors.size() + " error(s).");
        return errors;
    }
        
    /** Getter for property approvedConstitution.
     * @return Value of property approvedConstitution.
     *
     */
        
    public java.lang.Boolean getApprovedConstitution() {
        return approvedConstitution;
    }
    
    /** Setter for property approvedConstitution.
     * @param approvedConstitution New value of property approvedConstitution.
     *
     */
    public void setApprovedConstitution(java.lang.Boolean approvedConstitution) {
        this.approvedConstitution = approvedConstitution;
    }
    
    /** Getter for property autoDelegateProvision.
     * @return Value of property autoDelegateProvision.
     *
     */
    public java.lang.Boolean getAutoDelegateProvision() {
        return autoDelegateProvision;
    }
       
    /** Setter for property autoDelegateProvision.
     * @param autoDelegateProvision New value of property autoDelegateProvision.
     *
     */
    public void setAutoDelegateProvision(java.lang.Boolean autoDelegateProvision) {
        this.autoDelegateProvision = autoDelegateProvision;
    }
    
    /** Getter for property affiliateTitlePk.
     * @return Value of property affiliateTitlePk.
     *
     */
    public java.lang.Integer getAffiliateTitlePk() {
        return affiliateTitlePk;
    }
    
    /** Setter for property affiliateTitlePk.
     * @param affiliateTitlePk New value of property affiliateTitlePk.
     *
     */
    public void setAffiliateTitlePk(java.lang.Integer affiliateTitlePk) {
        this.affiliateTitlePk = affiliateTitlePk;
    }
    
    /** Getter for property subAffiliateTitlePk.
     * @return Value of property subAffiliateTitlePk.
     *
     */
    public java.lang.Integer getSubAffiliateTitlePk() {
        return subAffiliateTitlePk;
    }    
    
    /** Setter for property subAffiliateTitlePk.
     * @param subAffiliateTitlePk New value of property subAffiliateTitlePk.
     *
     */
    
    public void setSubAffiliateTitlePk(java.lang.Integer subAffiliateTitlePk) {
        this.subAffiliateTitlePk = subAffiliateTitlePk;
    }
       
    /** Getter for property comment.
     * @return Value of property comment.
     *
     */
    public java.lang.String getComment() {
        return comment;
    }
    
    /** Setter for property comment.
     * @param comment New value of property comment.
     *
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }
    
    // Getter and Setter Methods...     
    public ArrayList getOfficerTitlesList(String notUsed) {
        return this.officerTitlesList;
    }
    
    public void setOfficerTitlesList(ArrayList officerTitlesList) {
        this.officerTitlesList = officerTitlesList;
    }
    
    public OfficeData getOfficerTitlesList(int index){
        while (index >= this.officerTitlesList.size()) {
            this.officerTitlesList.add(new OfficeData() );
        }
        return (OfficeData)this.officerTitlesList.get(index);
    }
    
    public void setOfficerTitlesList(int index, OfficeData officeData) {
        officerTitlesList.set(index, officeData);
    }
    
    public Object[] getOfficerTitlesList() {
        if (officerTitlesList != null)
            return this.officerTitlesList.toArray();
        else
            return null;
    }   
        
}
    
