package org.afscme.enterprise.affiliate.officer.web;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import org.afscme.enterprise.affiliate.officer.AffiliateOfficerMaintenance;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.TextUtil;


/** Holds the data on the Affiliate Officer Maintenance screen
 * @struts:form name="affiliateOfficerMaintenanceForm"
 */
public class AffiliateOfficerMaintenanceForm extends SearchForm {
    
    private static Logger logger =  Logger.getLogger(AffiliateOfficerMaintenanceForm.class);    
    
    // E-Board data retrievable by fields_PK
    // key -- field_pk, value --     
    private Collection executives;
    
    // Officer data retrievable by fields_PK
    // key -- field_pk, value -- AffiliateOfficerMaintenance
    private Map officerList;
    
    // Needed for the steward field on the edit screen.  
    // Stewards are only valid for types L and U.
    private Character type;

    public String toString() {
        return null;
    }
    
       
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();        
        Iterator mIt = officerList.entrySet().iterator();
        while (mIt.hasNext()) {
            
            Entry entry = (Entry)mIt.next();
            Integer fieldPk = (Integer)entry.getKey();
            AffiliateOfficerMaintenance aom = (AffiliateOfficerMaintenance)entry.getValue();
            
            if (aom.getOfficerAction() != null) {
                if (aom.getOfficerAction().equals("p")) {                             
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    if ((aom.getOfficerPersonPk() == null) || (aom.getOfficerPersonPk().intValue() < 1)) {
                        if (aom.getEndTerm().intValue() < year) {
                            errors.add("officerData(" + fieldPk.intValue() + ").endTerm" , new ActionError("error.invalid.maintenance.term"));   
                        }
                        
                    } else {
                        if (aom.getExpirationYear() != null && aom.getExpirationYear().intValue() < year) {
                            errors.add("officerData(" + fieldPk.intValue() + ").expirationYear" , new ActionError("error.invalid.maintenance.term"));   
                        }                                                
                    }
                    if (TextUtil.isEmptyOrSpaces(aom.getFirstName()) && TextUtil.isEmptyOrSpaces(aom.getLastName()))
                        errors.add("officerData(" + fieldPk.intValue() + ").firstName" , new ActionError("error.affiliate.maintenance.mustenter"));
                    if (!TextUtil.isEmptyOrSpaces(aom.getFirstName()))
                        this.nameMatch(errors, aom.getFirstName(), "officerData(" + fieldPk.intValue() + ").firstName", "First Name" );
                    if (!TextUtil.isEmptyOrSpaces(aom.getMiddleName()))
                        this.nameMatch(errors, aom.getMiddleName(), "officerData(" + fieldPk.intValue() + ").middleName", "Middle Name" );
                    if (!TextUtil.isEmptyOrSpaces(aom.getLastName()))
                        this.nameMatch(errors, aom.getLastName(), "officerData(" + fieldPk.intValue() + ").lastName", "Last Name");                                  
                }
            }
        }
        return errors;
    }
    
    // Getter and Setter Methods...     
    public Collection getExecutives() {
        return executives;
    }
    
    public void setExecutives(Collection executives) {
        this.executives = executives;
    }    
    
    
    /** Getter for property officerList.
     * @return Value of property officerList.
     */
    public Map getOfficerList() {
        return officerList;
    }    
    
    /** Setter for property officerList.
     * @param criteria New value of property officerList.
     */
    public void setOfficerList(Map officerList) {
        this.officerList = officerList;
    }
    
    // getter method for mapped property
    public AffiliateOfficerMaintenance getOfficerData(String fieldPk) {
        return (AffiliateOfficerMaintenance)officerList.get(Integer.valueOf(fieldPk));
    }
    
    // setter method for mapped property
    public void setOfficerData(String fieldPk, AffiliateOfficerMaintenance officerData) {
        if (officerList == null)
            officerList = new TreeMap();
        
        officerList.put(Integer.valueOf(fieldPk), officerData);
    }    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        
        if (officerList != null) {
            Iterator mIt = officerList.values().iterator();

            // First iteration through, just checking for replace data validation errors
            while (mIt.hasNext()) {
                AffiliateOfficerMaintenance aom = (AffiliateOfficerMaintenance)mIt.next();
                aom.setSteward(Boolean.FALSE);
                aom.setOfficerAction(null);
                aom.setElectedOfficerFg(Boolean.FALSE);
            }      
        }
    }
    
    /* JZhang
     * @parm errors: ActionErrors object
     * @parm name: value that need to be verified
     * @parm prop: name on jsp
     */
    private void nameMatch(ActionErrors errors, String name, String prop, String nameTitle) 
    {
        try
        {
            boolean match = Pattern.matches("([a-z A-Z]{1}[a-z A-Z 0-9]{0,24})", name);
            if (match == false)                
                errors.add(prop, new ActionError("error.field.incorrect.name", nameTitle));            
        }catch (PatternSyntaxException pse)
        {
            //
        }       
    }     
    

    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public Character getType() {
        return type;
    }

    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(Character type) {
        if (TextUtil.isEmptyOrSpaces(type)) {
            this.type = null;
        } else {
            this.type = type;
        }
    }    
    
}


