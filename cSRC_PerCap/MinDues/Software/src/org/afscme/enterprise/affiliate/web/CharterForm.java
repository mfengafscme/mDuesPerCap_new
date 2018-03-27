package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.affiliate.CharterData;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;
import org.apache.log4j.Logger;

/**
 * @struts:form name="charterForm"
 */
public class CharterForm extends ActionForm {
    
    private static Logger logger =  Logger.getLogger(CharterForm.class);       
    
    private Integer affPk;
    private Character affIdType;
    private String name;
    private String jurisdiction;
    private Integer status;
    private Integer code;
    private Integer lastChangeEffectiveDateMonth;
    private String lastChangeEffectiveDateYear;
    private Integer dateMonth;
    private String dateYear;
    private String county1;
    private String county2;
    private String county3;
    private Collection councilAffiliations;
    private Integer reportingCouncilPk;
    private boolean canAssociateWithCouncil;
    
    /** Creates a new instance of CharterForm */
    public CharterForm() {
        this.init();
    }
    
// General Methods...
    
    protected void init() {
        this.affPk = null;
        this.affIdType = null;
        this.name = null;
        this.jurisdiction = null;
        this.status = null;
        this.code = null;
        this.lastChangeEffectiveDateMonth = null;
        this.lastChangeEffectiveDateYear = null;
        this.dateMonth = null;
        this.dateYear = null;
        this.county1 = null;
        this.county2 = null;
        this.county3 = null;
        this.councilAffiliations = null;
        this.reportingCouncilPk = null;
        this.canAssociateWithCouncil = false;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("CharterForm {");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", affIdType = ");
        sb.append(this.affIdType);        
        sb.append(", name = ");
        sb.append(this.name);
        sb.append(", jurisdiction = ");
        sb.append(this.jurisdiction);
        sb.append(", status = ");
        sb.append(this.status);
        sb.append(", code = ");
        sb.append(this.code);
        sb.append(", lastChangeEffectiveDateMonth = ");
        sb.append(this.lastChangeEffectiveDateMonth);
        sb.append(", lastChangeEffectiveDateYear = ");
        sb.append(this.lastChangeEffectiveDateYear);
        sb.append(", dateMonth = ");
        sb.append(this.dateMonth);
        sb.append(", dateYear = ");
        sb.append(this.dateYear);
        sb.append(", county1 = ");
        sb.append(this.county1);
        sb.append(", county2 = ");
        sb.append(this.county2);
        sb.append(", county3 = ");
        sb.append(this.county3);
        sb.append(", councilAffiliations = ");
        sb.append(CollectionUtil.toString(this.councilAffiliations));
        sb.append(", reportingCouncilPk = ");
        sb.append(this.reportingCouncilPk);
        sb.append(", canAssociateWithCouncil = ");
        sb.append(this.canAssociateWithCouncil);
        sb.append("}");
        return sb.toString().trim();
    }
    
// Struts Methods...
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.init();
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (!(status.equals(AffiliateStatus.N)) 
        && !(status.equals(AffiliateStatus.PC))
        &&  (this.getAffIdType().charValue() != 'U'))
        {
            if (TextUtil.isEmptyOrSpaces(this.getName())) {
                errors.add("name", new ActionError("error.affiliate.conditional.charter"));
            }           
            if (TextUtil.isEmptyOrSpaces(this.getJurisdiction())) {
                errors.add("jurisdiction", new ActionError("error.affiliate.conditional.jurisdiction"));
            }
            if (this.getCode() == null) {
                errors.add("code", new ActionError("error.affiliate.conditional.chartercode"));
            }
            if (TextUtil.isEmptyOrSpaces(this.getCounty1()) &&
                TextUtil.isEmptyOrSpaces(this.getCounty2()) && 
                TextUtil.isEmptyOrSpaces(this.getCounty3()))   {
                errors.add("county1", new ActionError("error.charter.required.one.county"));
            }
            if  (this.getDateMonth() == null || TextUtil.isEmptyOrSpaces(this.getDateYear())) {          
                errors.add("dateMonth", new ActionError("error.affiliate.conditional.charterdate"));
            }            
        }      
        if (!TextUtil.isEmptyOrSpaces(this.getDateYear()) &&(!TextUtil.isInt(this.getDateYear()))) {
            errors.add("dateYear", new ActionError("error.affiliate.charterdate.numeric", "Charter Year"));
        }
        boolean addToThree = false;
        if (!TextUtil.isEmptyOrSpaces(this.getCounty1())) {
            if (!TextUtil.isEmptyOrSpaces(this.getCounty2())) {
                if (this.getCounty1().equals(this.getCounty2())) {
                    errors.add("county2",new ActionError("error.duplicate.county"));
                }
                if (!TextUtil.isEmptyOrSpaces(this.getCounty3())) {
                    if (this.getCounty2().equals(this.getCounty3())) {
                        errors.add("county3",new ActionError("error.duplicate.county"));
                        addToThree = true;
                    }
                }
            }
            if (!addToThree) {
                if (!TextUtil.isEmptyOrSpaces(this.getCounty3())) {
                    if (this.getCounty1().equals(this.getCounty3())) {
                        errors.add("county3",new ActionError("error.duplicate.county"));
                    }
                }
            }
        } else {
            if (!TextUtil.isEmptyOrSpaces(this.getCounty2())) {
                if (!TextUtil.isEmptyOrSpaces(this.getCounty3())) {
                    if (this.getCounty2().equals(this.getCounty3())) {
                        errors.add("county3",new ActionError("error.duplicate.county"));
                        addToThree = true;
                    }
                }
            }
        }
        return errors;
    }
    
// Getter-Setter Methods...
    
    /** Getter for property code.
     * @return Value of property code.
     */
    public Integer getCode() {
        return code;
    }
    
    /** Setter for property code.
     * @param code New value of property code.
     */
    public void setCode(Integer code) {
        if (code == null || code.intValue() < 1) {
            this.code = null;
        } else {
            this.code = code;
        }
    }
    
    /** Getter for property councilAffiliations.
     * @return Value of property councilAffiliations.
     */
    public Collection getCouncilAffiliations() {
        return councilAffiliations;
    }
    
    /** Setter for property councilAffiliations.
     * @param councilAffiliations New value of property councilAffiliations.
     */
    public void setCouncilAffiliations(Collection councilAffiliations) {
        if (CollectionUtil.isEmpty(councilAffiliations)) {
            this.councilAffiliations = null;
        } else {
            this.councilAffiliations = councilAffiliations;
        }
    }
    
    /** Getter for property county1.
     * @return Value of property county1.
     */
    public String getCounty1() {
        return county1;
    }
    
    /** Setter for property county1.
     * @param county1 New value of property county1.
     */
    public void setCounty1(String county1) {
        if (TextUtil.isEmptyOrSpaces(county1)) {
            this.county1 = null;
        } else {
            this.county1 = county1;
        }
    }
    
    /** Getter for property county2.
     * @return Value of property county2.
     */
    public String getCounty2() {
        return county2;
    }
    
    /** Setter for property county2.
     * @param county2 New value of property county2.
     */
    public void setCounty2(String county2) {
        if (TextUtil.isEmptyOrSpaces(county2)) {
            this.county2 = null;
        } else {
            this.county2 = county2;
        }
    }
    
    /** Getter for property county3.
     * @return Value of property county3.
     */
    public String getCounty3() {
        return county3;
    }
    
    /** Setter for property county3.
     * @param county3 New value of property county3.
     */
    public void setCounty3(String county3) {
        if (TextUtil.isEmptyOrSpaces(county3)) {
            this.county3 = null;
        } else {
            this.county3 = county3;
        }
    }
    
    /** Getter for property dateMonth.
     * @return Value of property dateMonth.
     *
     */
    public Integer getDateMonth() {
        return dateMonth;
    }
    
    /** Setter for property dateMonth.
     * @param dateMonth New value of property dateMonth.
     */
    public void setDateMonth(Integer dateMonth) {
        if (dateMonth == null || dateMonth.intValue() < 1) {
            this.dateMonth = null;
        } else {



            this.dateMonth = dateMonth;
        }
    }
    
    /** Getter for property dateYear.
     * @return Value of property dateYear.
     */
    public String getDateYear() {
        return dateYear;
    }
    
    /** Setter for property dateYear.
     * @param dateYear New value of property dateYear.
     */
    public void setDateYear(String dateYear) {
        if (TextUtil.isEmptyOrSpaces(dateYear)) {
            this.dateYear = null;
        } else {
            this.dateYear = dateYear;
        }
    }
    
    /** Getter for property jurisdiction.
     * @return Value of property jurisdiction.
     */
    public String getJurisdiction() {
        return jurisdiction;
    }
    
    /** Setter for property jurisdiction.
     * @param jurisdiction New value of property jurisdiction.
     */
    public void setJurisdiction(String jurisdiction) {
        if (TextUtil.isEmptyOrSpaces(jurisdiction)) {
            this.jurisdiction = null;
        } else {
            this.jurisdiction = jurisdiction;
        }
    }
    
    /** Getter for property lastChangeEffectiveDateMonth.
     * @return Value of property lastChangeEffectiveDateMonth.
     */
    public Integer getLastChangeEffectiveDateMonth() {
        return lastChangeEffectiveDateMonth;
    }
    
    /** Setter for property lastChangeEffectiveDateMonth.
     * @param lastChangeEffectiveDateMonth New value of property lastChangeEffectiveDateMonth.
     */
    public void setLastChangeEffectiveDateMonth(Integer lastChangeEffectiveDateMonth) {
        if (lastChangeEffectiveDateMonth == null || lastChangeEffectiveDateMonth.intValue() < 1) {
            this.lastChangeEffectiveDateMonth = null;
        } else {



            this.lastChangeEffectiveDateMonth = lastChangeEffectiveDateMonth;
        }
    }
    
    /** Getter for property lastChangeEffectiveDateYear.
     * @return Value of property lastChangeEffectiveDateYear.
     */
    public String getLastChangeEffectiveDateYear() {
        return lastChangeEffectiveDateYear;
    }
    
    /** Setter for property lastChangeEffectiveDateYear.
     * @param lastChangeEffectiveDateYear New value of property lastChangeEffectiveDateYear.
     */
    public void setLastChangeEffectiveDateYear(String lastChangeEffectiveDateYear) {
        if (TextUtil.isEmptyOrSpaces(lastChangeEffectiveDateYear)) {
            this.lastChangeEffectiveDateYear = null;
        } else {
            this.lastChangeEffectiveDateYear = lastChangeEffectiveDateYear;
        }
    }
    
    /** Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        if (TextUtil.isEmptyOrSpaces(name)) {
            this.name = null;
        } else {
            this.name = name;
        }
    }
    
    /** Getter for property status.
     * @return Value of property status.
     */
    public Integer getStatus() {
        return status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(Integer status) {
        if (status == null || status.intValue() < 1) {            
            this.status = null;
        } else {        
            this.status = status;
        }
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     */
    public void setAffPk(Integer affPk) {
        if (affPk == null || affPk.intValue() < 1) {
            this.affPk = null;
        } else {
            this.affPk = affPk;
        }
    }
    
        /** Getter for property affIdType.
     * @return Value of property affIdType.
     *
     */
    public Character getAffIdType() {
        return affIdType;
    }
    
    /** Setter for property affIdType.
     * @param affIdType New value of property affIdType.
     *
     */
    public void setAffIdType(Character affIdType) {
        if (TextUtil.isEmptyOrSpaces(affIdType)) {
            this.affIdType = null;
        } else {
            this.affIdType = affIdType;
        }
    }
    
    /**
     * Builds a CharterData object based on the fields in the form.
     */
    public CharterData getCharterData() {
        CharterData data = new CharterData();
        data.setAffPk(this.getAffPk());
        data.setCharterCodeCodePk(this.getCode());
        if (this.getDateMonth() == null || 
            TextUtil.isEmptyOrSpaces(this.getDateYear())
        ) {
            data.setCharterDate(null);
        } else {
            data.setCharterDate(DateUtil.getTimestamp(this.getDateMonth().intValue(), 
                                Integer.parseInt(this.getDateYear()), false)
            
            );
        }
        data.setCouncilAffiliations(this.getCouncilAffiliations());
        data.addCounty(this.getCounty1());
        data.addCounty(this.getCounty2());
        data.addCounty(this.getCounty3());
        data.setJurisdiction(this.getJurisdiction());
        if (this.getLastChangeEffectiveDateMonth() == null || 
            TextUtil.isEmptyOrSpaces(this.getLastChangeEffectiveDateYear())
        ) {
            data.setLastChangeEffectiveDate(null);
        } else {
            data.setLastChangeEffectiveDate(
                    DateUtil.getTimestamp(
                        this.getLastChangeEffectiveDateMonth().intValue(), 
                        Integer.parseInt(this.getLastChangeEffectiveDateYear()), 
                        false
                    )
            );
        }
        data.setName(this.getName());
        data.setStatusCodePk(this.getStatus());
        data.setReportingCouncilPk(this.getReportingCouncilPk());
        return data;
    }
    
    public void setCharterData(CharterData data) {
        this.setAffPk(data.getAffPk());
        this.setCode(data.getCharterCodeCodePk());
        this.setCouncilAffiliations(data.getCouncilAffiliations());
        this.setJurisdiction(data.getJurisdiction());
        this.setName(data.getName());
        this.setStatus(data.getStatusCodePk());
        this.setReportingCouncilPk(data.getReportingCouncilPk());
        
        // set county values...
        if (!CollectionUtil.isEmpty(data.getCounties())) {
            Iterator it = data.getCounties().iterator();
            for (int i = 1; it.hasNext(); i++) {
                String county = (String)it.next();
                switch (i) {
                    case 1:
                        this.setCounty1(county);
                        break;
                    case 2:
                        this.setCounty2(county);
                        break;
                    case 3:
                        this.setCounty3(county);
                        break;
                    default:
                        // too many counties. ignore the rest.
                        break;
                }
            }
        }
        Calendar date = DateUtil.getCalendar(data.getCharterDate());
        // set charter date values...
        if (date == null) {
            this.setDateMonth(null);
            this.setDateYear(null);
        } else {
            this.setDateMonth(new Integer(date.get(Calendar.MONTH) + 1));
            this.setDateYear(Integer.toString(date.get(Calendar.YEAR)));
        }
        // set last change effective date values...
        date = DateUtil.getCalendar(data.getLastChangeEffectiveDate());
        if (date == null) {
            this.setLastChangeEffectiveDateMonth(null);
            this.setLastChangeEffectiveDateYear(null);
        } else {
            this.setLastChangeEffectiveDateMonth(new Integer(date.get(Calendar.MONTH) + 1));
            this.setLastChangeEffectiveDateYear(Integer.toString(date.get(Calendar.YEAR)));
        }
    }
    
    /** Getter for property reportingCouncilPk.
     * @return Value of property reportingCouncilPk.
     */
    public Integer getReportingCouncilPk() {
        return reportingCouncilPk;
    }
    
    /** Setter for property reportingCouncilPk.
     * @param reportingCouncilPk New value of property reportingCouncilPk.
     */
    public void setReportingCouncilPk(Integer reportingCouncilPk) {
        if (reportingCouncilPk == null || reportingCouncilPk.intValue() < 1) {
            this.reportingCouncilPk = null;
        } else {
            this.reportingCouncilPk = reportingCouncilPk;
        }
    }
    
    /** Getter for property canAssociateWithCouncil.
     * @return Value of property canAssociateWithCouncil.
     */
    public boolean isCanAssociateWithCouncil() {
        return canAssociateWithCouncil;
    }
    
    /** Setter for property canAssociateWithCouncil.
     * @param canAssociateWithCouncil New value of property canAssociateWithCouncil.
     */
    public void setCanAssociateWithCouncil(boolean canAssociateWithCouncil) {
        this.canAssociateWithCouncil = canAssociateWithCouncil;
    }
    
}

