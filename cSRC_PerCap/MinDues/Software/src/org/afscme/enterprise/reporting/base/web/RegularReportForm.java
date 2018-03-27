package org.afscme.enterprise.reporting.base.web;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Set;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import java.text.DateFormat;
import java.text.ParseException;
import javax.ejb.CreateException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.reporting.base.generator.ReportGenerator;
import org.afscme.enterprise.reporting.base.generator.OutputFormat;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.base.generator.OutputFormat;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.reporting.base.access.CriterionMap;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;


/**
 * @struts:form name="regularReportForm"
 */
public class RegularReportForm extends ActionForm {
    /*-----------------------------------------------------------------*/
    /*-------------   Static Data ------------------------------------*/
    /*-----------------------------------------------------------------*/
    public static final String TAB = "Tab";
    public static final String COMMA = "Comma";
    public static final String SEMICOLON = "Semicolon";
    public static final String PDF = "PDF";
    
    public static final String SPECIALIZED = "Specialized";
    public static final String CUSTOM = "Custom";

    private Integer reportPk = null;
    
    private String reportType = null;
    
    private String media = MediaType.SCREEN;
    
    private String outputFormat = null;
    
    private String button = null;
    
    private String pageName = null;
    
    private Map runtimeCriteria = null;
    

    // Detailed query field data retrievable by fields_PK
    // key -- field_pk, value -- an instance of "ReportField" class
    private Map queryFields = null;
    
    // all fields that are children of some parent field
    private Map children = null;
    
    private MediaType mediaType = null;    
    
    //true if the user is using the data utility
    private boolean dataUtility;
   
    public void clearForm() {
        reportPk = null;
        media = MediaType.SCREEN;
        outputFormat = null;
        button = null;
        if (runtimeCriteria != null) {
            runtimeCriteria.clear();
            runtimeCriteria = null;
        }
        
        mediaType = null;
    }
    
    /** Getter for property media.
     * @return Value of property media.
     */
    public java.lang.String getMedia() {
        return media;
    }    
    
    /** Setter for property media.
     * @param media New value of property media.
     */
    public void setMedia(java.lang.String media) {
        this.media = media;
    }    
    
    /** Getter for property outputFormat.
     * @return Value of property outputFormat.
     */
    public java.lang.String getOutputFormat() {
        return outputFormat;
    }
    
    /** Setter for property outputFormat.
     * @param outputFormat New value of property outputFormat.
     */
    public void setOutputFormat(java.lang.String outputFormat) {
        this.outputFormat = outputFormat;
    }
    
    /** Get the MeidaType object */
    public MediaType getMediaTypeObject() {
        if (mediaType == null) 
            mediaType = new MediaType(media);
            
        return new MediaType(media);
    }
    
    /** Get the outputFormat instance */
    public OutputFormat getOutputFormatObject() {
        if (outputFormat == null)
            return  new OutputFormat(OutputFormat.PDF);
        if (outputFormat.equalsIgnoreCase(TAB))
            return new OutputFormat(OutputFormat.TAB);
        else if (outputFormat.equalsIgnoreCase(COMMA))
            return  new OutputFormat(OutputFormat.COMMA);
        else if (outputFormat.equalsIgnoreCase(SEMICOLON))
            return  new OutputFormat(OutputFormat.SEMICOLON);
        else 
            return  new OutputFormat(OutputFormat.PDF);
    }
    
    /** Getter for property button.
     * @return Value of property button.
     */
    public java.lang.String getButton() {
        return button;
    }
    
    /** Setter for property button.
     * @param button New value of property button.
     */
    public void setButton(java.lang.String button) {
        this.button = button;
    }
    
    /** Getter for property runtimeCriteria.
     * @return Value of property runtimeCriteria.
     */
    public java.util.Map getRuntimeCriteria() {
        return runtimeCriteria;
    }
    
    /** Setter for property runtimeCriteria.
     * @param runtimeCriteria New value of property runtimeCriteria.
     */
    public void setRuntimeCriteria(java.util.Map runtimeCriteria) {
        this.runtimeCriteria = runtimeCriteria;
    }
    
    /** Getter for property reportType.
     * @return Value of property reportType.
     */
    public java.lang.String getReportType() {
        return reportType;
    }
    
    /** Setter for property reportType.
     * @param reportType New value of property reportType.
     */
    public void setReportType(java.lang.String reportType) {
        this.reportType = reportType;
    }
    
    /** Getter for property reportPk.
     * @return Value of property reportPk.
     */
    public java.lang.Integer getReportPk() {
        return reportPk;
    }
    
    /** Setter for property reportPk.
     * @param reportPk New value of property reportPk.
     */
    public void setReportPk(java.lang.Integer reportPk) {
        this.reportPk = reportPk;
    }
    
    // getter method for the mapped property
    public CriterionMap getCriterionMap(String fieldPk) {
        return (CriterionMap)runtimeCriteria.get(fieldPk);
    }
    
    // setter method for the mapped property
    public void setCriterionMap(String fieldPk, CriterionMap criterionMap) {
        runtimeCriteria.put(fieldPk, criterionMap);
    }
    
    public void addCriterion(String fieldPkStr) {
        if (runtimeCriteria.containsKey(fieldPkStr)) {
            CriterionMap cMap = (CriterionMap)runtimeCriteria.get(fieldPkStr);
            
            ReportCriterionData criterion = new ReportCriterionData();
            criterion.setFieldPK(new Integer(fieldPkStr));
            
            cMap.addCriterion(criterion);
        }
    } 
    
    public void removeCriterion(String fieldPkStr, String cKey) {
        if (runtimeCriteria.containsKey(fieldPkStr)) {
            CriterionMap cMap = (CriterionMap)runtimeCriteria.get(fieldPkStr);
            
            cMap.removeCriterion(cKey);
        }
    }    
    
    // construct the report with the runtime criteria combined
    public Map getMergedCriteria(Report dbReport) {        
        Map dbCriteria = dbReport.getCriteriaFields();
        
        if (runtimeCriteria != null) {
            Iterator it = runtimeCriteria.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry)it.next();
                Integer fieldPk = new Integer((String)entry.getKey());
                CriterionMap cMap = (CriterionMap)entry.getValue();
            
                dbCriteria.put(fieldPk, cMap.getAllCriterion(true));
            }
        }        
        
        return dbCriteria;
    }
    
        
    /** Getter for property queryFields.
     * @return Value of property queryFields.
     */
    public java.util.Map getQueryFields() {
        return queryFields;
    }    
        
    /** Setter for property queryFields.
     * @param queryFields New value of property queryFields.
     */
    public void setQueryFields(java.util.Map queryFields) {
        this.queryFields = queryFields;
        
        // let's make a map for all children
        // key -- field_pk, value -- ReportField
        children = new HashMap();
        ReportField topField;
        ReportField childField;
        Iterator pIt = this.queryFields.values().iterator();
        Iterator cIt;
        while (pIt.hasNext()) {
            topField = (ReportField)pIt.next();
            if (topField.hasChildren()) {
                cIt = topField.getChildren().iterator();
                while (cIt.hasNext()) {
                    childField = (ReportField)cIt.next();
                    children.put(childField.getPk(), childField);
                }
            }
        }               
    }    
    
    /** Getter for property children.
     * @return Value of property children.
     */
    public java.util.Map getChildren() {
        return children;
    }
    
    /** Setter for property children.
     * @param children New value of property children.
     */
    public void setChildren(java.util.Map children) {
        this.children = children;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        ActionError error;
        
        if (pageName == null) return errors;
        
        if (button.equals("Cancel")) return null;
        
        if (pageName.equals("Runtime Criteria")) {
            // selection criteria
            Map.Entry entry;
            Integer fieldPk;
            String fieldPkStr;
            CriterionMap cMap;
            ReportCriterionData criterion;
            ReportField field;
            String value1, value2;
            String testStr;
            int result;
            Iterator it = runtimeCriteria.entrySet().iterator();
            while (it.hasNext()) {
                entry = (Map.Entry)it.next();
                fieldPkStr = (String)entry.getKey();
                fieldPk = new Integer(fieldPkStr);
                cMap = (CriterionMap)entry.getValue();
            
                field = (ReportField)queryFields.get(fieldPk);
                if (field == null)
                    field = (ReportField)children.get(fieldPk);
            
                if (field.getDisplayType() == 'S') {
                    // single text field, make sure it does not have spaces
                    criterion = cMap.getCriterion("c0");
                    value1 = criterion.getValue1();
                    testStr = value1.trim();
                    if (testStr.indexOf(' ') != -1) {
                        error = new ActionError("error.field.criterion.hasSpace");
                        errors.add(ActionErrors.GLOBAL_ERROR, error);
                        return errors;
                    }
                    else
                        criterion.setValue1(testStr);
                }
                else if (field.getDisplayType() == 'I') {
                    // one or more regular number field, make sure they are numbers
                    Set keys = cMap.getKeys();
                    String cKey;
                    Iterator kIt = keys.iterator();
                    while (kIt.hasNext()) {
                        cKey = (String)kIt.next();
                        criterion = cMap.getCriterion(cKey);
                        // check the value1 first
                        testStr = criterion.getValue1().trim();
                        try {
                            result = Integer.parseInt(testStr);
                        }
                        catch (NumberFormatException exp) {
                            error = new ActionError("error.field.criterion.invalidNumber");
                            errors.add(ActionErrors.GLOBAL_ERROR, error);
                            return errors;
                        }
                        criterion.setValue1(testStr);
                    
                        // check the value2 if operator is "BT"
                        if (criterion.getOperator().equals("BT")) {
                            testStr = criterion.getValue2().trim();
                            try {
                                result = Integer.parseInt(testStr);
                            }
                            catch (NumberFormatException exp) {
                                error = new ActionError("error.field.criterion.invalidNumber");
                                errors.add(ActionErrors.GLOBAL_ERROR, error);
                                return errors;
                            } 
                            criterion.setValue2(testStr);
                        }
                    }
                }
                else if (field.getDisplayType() == 'D') {
                    // a date field, make sure it is date format
                    Set keys = cMap.getKeys();
                    String cKey;
                    Iterator kIt = keys.iterator();
                    while (kIt.hasNext()) {
                        cKey = (String)kIt.next();
                        criterion = cMap.getCriterion(cKey);
                        // check the value1 first
                        testStr = criterion.getValue1().trim();
                        try {
                            DateFormat.getDateInstance().parse(testStr);
                        }
                        catch (ParseException exp) {
                            error = new ActionError("error.field.criterion.invalidDate");
                            errors.add(ActionErrors.GLOBAL_ERROR, error);
                            return errors;
                        }
                        criterion.setValue1(testStr);
                    
                        // check the value2 if operator is "BT"
                        if (criterion.getOperator().equals("BT")) {
                            testStr = criterion.getValue2().trim();
                            try {
                                DateFormat.getDateInstance().parse(testStr);
                            }
                            catch (ParseException exp) {
                                error = new ActionError("error.field.criterion.invalidDate");
                                errors.add(ActionErrors.GLOBAL_ERROR, error);
                                return errors;
                            } 
                            criterion.setValue2(testStr);
                        }
                    }
                }
                // note, we don't need to check for code field
            }
        }           
        
        return errors;
        
    }    
    
    /** Getter for property pageName.
     * @return Value of property pageName.
     */
    public java.lang.String getPageName() {
        return pageName;
    }
    
    /** Setter for property pageName.
     * @param pageName New value of property pageName.
     */
    public void setPageName(java.lang.String pageName) {
        this.pageName = pageName;
    }
    
    /** Getter for property dataUtility.
     * @return Value of property dataUtility.
     *
     */
    public boolean isDataUtility() {
        return dataUtility;
    }
    
    /** Setter for property dataUtility.
     * @param dataUtility New value of property dataUtility.
     *
     */
    public void setDataUtility(boolean dataUtility) {
        this.dataUtility = dataUtility;
    }
    
}
