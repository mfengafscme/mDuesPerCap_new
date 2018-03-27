package org.afscme.enterprise.reporting.base.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.BRUtil;
import org.afscme.enterprise.reporting.base.access.CriterionMap;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.access.ReportOutputFieldData;
import org.afscme.enterprise.reporting.base.access.ReportSortFieldData;
import org.afscme.enterprise.reporting.base.generator.ReportGenerator;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.web.WebUtil;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

/**
 * @struts:form name="queryForm"
 */
public class QueryForm extends ActionForm {
    
    private static Logger logger =  Logger.getLogger(QueryForm.class);    
    
    public static final String BUTTON_SAVE = "Save";
    public static final String BUTTON_SAVE_AS = "Save As";
    public static final String BUTTON_CANCEL = "Cancel";

    public static final String PAGE_OUTPUT_FIELDS = "Output Fields";
    public static final String PAGE_OUTPUT_FIELDS_ORDER = "Output Fields Order";
    public static final String PAGE_SELECTION_CRITERIA_FIELDS = "Selection Criteria Fields";
    public static final String PAGE_SELECTION_CRITERIA = "Selection Criteria";
    public static final String PAGE_SORT_CRITERIA_FIELDS = "Sort Criteria Fields";
    public static final String PAGE_SORT_CRITERIA = "Sort Criteria";
    public static final String PAGE_PREVIEW_QUERY = "Preview Query";
    public static final String PAGE_SAVE_CONFIRMATION = "Save Confirmation";
    
    public static final String[] WIZARD_PAGE_NAMES = new String[]
    {
        PAGE_OUTPUT_FIELDS,
        PAGE_OUTPUT_FIELDS_ORDER,
        PAGE_SELECTION_CRITERIA_FIELDS,
        PAGE_SELECTION_CRITERIA,
        PAGE_SORT_CRITERIA_FIELDS,
        PAGE_SORT_CRITERIA,
        PAGE_PREVIEW_QUERY
    };
    
    public static final String[] FORWARDS = new String[]
    {
        "OutputFields",
        "OutputFieldsOrder",
        "SelectionCriteriaFields",
        "SelectionCriteria",
        "SortCriteriaFields",
        "SortCriteria",
        "PreviewQuery"
    };
    
    // Detailed query field data retrievable by fields_PK
    // key -- field_pk, value -- an instance of "ReportField" class
    private Map queryFields ;
    
    // all fields that are children of some parent field
    private Map children ;
    
    // codes by primary key
    // key -- code_type, value -- map of pk to CodeData
    private Map codes ;
    
    /*-----------------------------------------------------------------*/
    /*-------------   Common Data ------------------------------------*/
    /*-----------------------------------------------------------------*/
    private String pageName ;             // page indicator
    private String linkClicked ;          // wizard link indicator
    private String saveButton ;           // for Save button
    private String saveAsButton ;         // for save as button
    
    // User accessible fields
    // key -- entity (Char: M, O, A, P), value -- Map (key: category, value: list of ReportField)
    private Map userFields ;
    
    // the current user id
    private String userId ;
    
    // current owner pk
    private Integer personPk ;
    

    /*-----------------------------------------------------------------*/    
    /*-------------   Output Fields (page 1)        -------------------*/
    /*-----------------------------------------------------------------*/    
    private boolean countQuery;         // indicates it is a count query or not
    
    // keeps track of user selected output field_pk.
    // Note the index order of these fields should match those in the "outputFieldsOrder".
    private Integer[] selectedOutputFields = new Integer[0];   
    
    // keeps previous selected output fields in order to compare when leaving the
    // first wizard page
    private Integer[] previousSelectedOutputFields ;
    
    
    
    /*-----------------------------------------------------------------*/    
    /*-------------   Output Fields Order (page 2)        -------------*/
    /*-----------------------------------------------------------------*/    
    // mapped property for the output fields order
    // key -- fieldPk (String), value -- an instance of Short
    private Map outputFieldsOrder = new TreeMap();
    
    
    /*-----------------------------------------------------------------*/    
    /*-------------   Selection Criteria Fields (page 3)        -------*/
    /*-----------------------------------------------------------------*/
    // keeps track of user selected criteria field_pk.
    private Integer[] selectedCriteriaFields = new Integer[0];
    
    // keeps the previous selected criteria
    private Integer[] previousSelectedCriteriaFields ;
    
    
    
    /*-----------------------------------------------------------------*/    
    /*-------------   Selection Criteria (page 4)        --------------*/
    /*-----------------------------------------------------------------*/
    // keeps track of all currently defined criteria
    // key -- field_pk, value -- criterionMap
    private Map criteria ;
   
    // keeps the editable information about each field
    private Integer[] editableFields = new Integer[0];
    
    
    
    /*-----------------------------------------------------------------*/    
    /*-------------   Sort Criteria Fields (page 5)        ------------*/
    /*-----------------------------------------------------------------*/
    private Integer[] selectedSortFields = new Integer[0];
    
    private Integer[] previousSelectedSortFields ;
    
    /*-----------------------------------------------------------------*/    
    /*-------------   Sort Criteria            (page 6) ---------------*/
    /*-----------------------------------------------------------------*/
    // mapped property
    // key -- fieldPk (String), value -- an instance of Short
    private Map sortFieldsOrder = new TreeMap();
    
    // mapped property
    // key -- fieldPk (String), value -- "A" or "D" (String)
    private Map orderDirections = new TreeMap();
    
    /*-----------------------------------------------------------------*/    
    /*-------------   Preview Query (page 7) ---------------*/
    /*-----------------------------------------------------------------*/    
    private Integer pk;          // query's primary key, null when "Add Query"
    private String name;
    private String oriName;      // the original name in "Edit" mode, it is null for "Add" mode
    private String description;
    private boolean mailingList;
    private boolean updateCorrespondenceHistory;
    
    private List previewResult;  // the query result in row-column table.
    private List fieldNames;    // list if field display names in the user defined order.

    private int totalRows;
    
    /*-----------------------------------------------------------------*/
    /*-----------------------------------------------------------------*/
    
    /** Struts' reset method */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // we need to get the hidden "pageName" from the request object instead
        // of this form because the form has not been set yet.
        String fromPageName = request.getParameter("pageName");
        if (fromPageName == null)
            return;
                
        // reset for page one (output fields)
        if (fromPageName.equals(PAGE_OUTPUT_FIELDS)) {
            countQuery = false;
            selectedOutputFields = new Integer[0];
        }
        else if (fromPageName.equals(PAGE_SELECTION_CRITERIA_FIELDS)) {
            selectedCriteriaFields = new Integer[0];
        }
        else if (fromPageName.equals(PAGE_SELECTION_CRITERIA)) {
            editableFields = new Integer[0];
            
            // reset all code fields if any
            if (criteria != null) {
                Iterator it = criteria.values().iterator();
                CriterionMap cMap;
                while (it.hasNext()) {
                    cMap = (CriterionMap)it.next();
                    cMap.reset();
                }
            }
        }
        else if (fromPageName.equals(PAGE_SORT_CRITERIA_FIELDS)) {
            selectedSortFields = new Integer[0];
        }
        else if (fromPageName.equals(PAGE_PREVIEW_QUERY)) {
            name = null;
            description = null;
            mailingList = false;
            updateCorrespondenceHistory = false;
        }
    }
    
    /** Struts' validation method */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        ActionError error;
        
        if (pageName == null) return errors;
        
        if (pageName.equals(PAGE_SELECTION_CRITERIA) && (criteria != null) && (!criteria.isEmpty())) {
            // selection criteria
            Iterator it = criteria.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry)it.next();
                Integer fieldPk = (Integer)entry.getKey();
                CriterionMap cMap = (CriterionMap)entry.getValue();
            
                ReportField field = (ReportField)queryFields.get(fieldPk);
                if (field == null)
                    field = (ReportField)children.get(fieldPk);

                if (field.getDisplayType() == 'S') {
                    // single text field, make sure it does not have spaces
                    ReportCriterionData criterion = cMap.getCriterion("c0");
                    String value1 = criterion.getValue1();
                    if (value1 != null) {
                        String testStr = value1.trim();
                        if (testStr.indexOf(' ') != -1) {
                            error = new ActionError("error.field.criterion.hasSpace");
                            errors.add(ActionErrors.GLOBAL_ERROR, error);
                            return errors;
                        }
                        else
                            criterion.setValue1(testStr);
                    }
                }
                else if (field.getDisplayType() == 'I') {
                    // one or more regular number field, make sure they are numbers
                    Set keys = cMap.getKeys();
                    String cKey;
                    Iterator kIt = keys.iterator();
                    while (kIt.hasNext()) {
                        cKey = (String)kIt.next();
                        ReportCriterionData criterion = cMap.getCriterion(cKey);
                        if (!(criterion.getOperator().equals("NL") || criterion.getOperator().equals("NN"))) {
                            WebUtil.checkOptionalInt(criterion.getValue1(), errors);
                            if (criterion.getOperator().equals("BT"))
                                WebUtil.checkOptionalInt(criterion.getValue2(), errors);
                        }
                    }
                }
                else if (field.getDisplayType() == 'D') {
                    Iterator kIt = cMap.getKeys().iterator();
                    while (kIt.hasNext()) {
                        String cKey = (String)kIt.next();
                        ReportCriterionData criterion = cMap.getCriterion(cKey);
                        if (!(criterion.getOperator().equals("NL") || criterion.getOperator().equals("NN"))) {
                            WebUtil.checkOptionalDate(criterion.getValue1(), errors);
                            if (criterion.getOperator().equals("BT"))
                                WebUtil.checkOptionalDate(criterion.getValue2(), errors);
                        }
                    }
                }
                // note, we don't need to check for code field
            }
        }
        
        
               
        // preview report
        if ((pageName.equals(PAGE_PREVIEW_QUERY)) && (((saveButton != null) && saveButton.equals(BUTTON_SAVE)) || ((saveAsButton != null) && (saveAsButton.equals(BUTTON_SAVE_AS)))))
        {
            WebUtil.checkFieldLength("name", name, 1, -1, errors);
            this.nameMatch(errors, name);
        }
        return errors;
        
    }
    
    /* Clear this session form */
    public void clearForm() {
        
        pageName = null;
        linkClicked = null;
        
        countQuery = false;
        selectedOutputFields = new Integer[0];
        previousSelectedOutputFields = null;
        
        if (outputFieldsOrder != null) {
            outputFieldsOrder.clear();
            outputFieldsOrder = null;
        }

        selectedCriteriaFields = new Integer[0];
        previousSelectedCriteriaFields = null;
        
        if (criteria != null) {
            criteria.clear();
            criteria = null;
        }
        editableFields = new Integer[0];
        
        selectedSortFields = new Integer[0];
        previousSelectedSortFields = null;
        
        if (sortFieldsOrder != null) {
            sortFieldsOrder.clear();
            sortFieldsOrder = null;
        }
        if (orderDirections != null) {
            orderDirections.clear();
            orderDirections = null;
        }
        
        pk = null;
        name = null;
        oriName = null;
        description = null;
        mailingList = false;
        updateCorrespondenceHistory = false;
        userFields = null;
    }
    
    /*----------------------  Output Fields p1 ------------------------------*/

    /** Getter for property countQuery.
     * @return Value of property countQuery.
     */
    public boolean isCountQuery() {
        return countQuery;
    }    
    
    /** Setter for property countQuery.
     * @param countQuery New value of property countQuery.
     */
    public void setCountQuery(boolean countQuery) {
        this.countQuery = countQuery;
    }
    
    /** Getter for property selectedOutputFields.
     * @return Value of property selectedOutputFields.
     */
    public java.lang.Integer[] getSelectedOutputFields() {
        return this.selectedOutputFields;
    }
    
    /** Setter for property selectedOutputFields.
     * @param selectedOutputFields New value of property selectedOutputFields.
     */
    public void setSelectedOutputFields(java.lang.Integer[] selectedOutputFields) {
        Arrays.sort(selectedOutputFields);
        this.selectedOutputFields = selectedOutputFields;
    }

    /** Getter for property previousSelectedOutputFields.
     * @return Value of property previousSelectedOutputFields.
     */
    public java.lang.Integer[] getPreviousSelectedOutputFields() {
        return this.previousSelectedOutputFields;
    }
    
    /** Setter for property previousSelectedOutputFields.
     * @param previousSelectedOutputFields New value of property previousSelectedOutputFields.
     */
    public void setPreviousSelectedOutputFields(java.lang.Integer[] previousSelectedOutputFields) {
        this.previousSelectedOutputFields = previousSelectedOutputFields;
    }
    
    /** Check if the output field selections has been changed by the user. We use this to determine
     *  if the second wizard page (output field order) needs to be cleared.
     */
    public boolean hasOutputFieldSelectionChanged() {
        boolean changed = false;

        // let's check if user has changed the selection
        // if both are null, no change
        if ((selectedOutputFields == null) && (previousSelectedOutputFields == null)) {
            changed = false;
        }
        else if (selectedOutputFields == null) {
            if (previousSelectedOutputFields.length != 0) {
                changed = true;
            }
            else {
                changed = false;
            }
        }
        else if (previousSelectedOutputFields == null) {
            if (selectedOutputFields.length != 0) {
                changed = true;
            }
            else {
                changed = false;
            }
        }
        else {
            // if both the old and new are empty, no change
            if ((selectedOutputFields.length == 0) && (previousSelectedOutputFields.length == 0)) {
                changed = false;
            }
            else if (selectedOutputFields.length != previousSelectedOutputFields.length) {  // if the old length is not the same as the new length, changed.
                changed = true;
            }
            else {
                // now, we only left with equal length arrays. We need to check if the items are the same
                Arrays.sort(selectedOutputFields);
                Arrays.sort(previousSelectedOutputFields);                
                boolean arrayEqual = Arrays.equals(selectedOutputFields, previousSelectedOutputFields);
                if (arrayEqual) { 
                    changed = false;
                }
                else {
                    changed = true;
                }
            }
        }        
        
        return changed;
    } 
    
    /** clear up the previous selection if the user has now selected as "COUNT" query */
    public void clearOutputFieldsForCountQuery() {
        selectedOutputFields = new Integer[0];
        previousSelectedOutputFields = null;
        if (outputFieldsOrder != null)
            outputFieldsOrder.clear();
        else
            outputFieldsOrder = new TreeMap();
    }
    
    /** Reset the underlining data structure for mapped property "field order"
     *  when the user has changed the selection on page 1 (Output Fields).
     */
    public void resetOutputFieldsOrder() {
        if (outputFieldsOrder != null)
            outputFieldsOrder.clear();
        else
            outputFieldsOrder = new TreeMap();
        
        for (int i = 0; i < selectedOutputFields.length; i++)
            outputFieldsOrder.put(selectedOutputFields[i].toString(), new Short((short)(i+1)));
    }     
    
    /*----------------------  Output Fields Order p2 ------------------------*/
    
    /** Getter method for the mapped property "field order" */
    public Short getFieldOrder(String fieldPk) {
        return (Short)outputFieldsOrder.get(fieldPk);
    }
    
    /** Setter method for the mapped property "field order" */
    public void setFieldOrder(String fieldPk, Short order) {
        outputFieldsOrder.put(fieldPk, order);
    }
    
    /** Getter for property outputFieldsOrder.
     * @return Value of property outputFieldsOrder.
     */
    public Map getOutputFieldsOrder() {
        return outputFieldsOrder;
    }    
    
    /** Setter for property outputFieldsOrder.
     * @param outputFieldsOrder New value of property outputFieldsOrder.
     */
    public void setOutputFieldsOrder(Map outputFieldsOrder) {
        this.outputFieldsOrder = outputFieldsOrder;
    }
    
    /** Get a list of ReportFields that the user has selected for ordering
     *  on the second page.
     */
    public List getOutputFieldsForOrder() {
        if (isCountQuery())
            return new LinkedList();
        
        List outputFields = new LinkedList();
        
        Integer fieldPk;
        ReportField field;
        for (int i = 0; i < selectedOutputFields.length; i++) {
            fieldPk = selectedOutputFields[i];
            if (queryFields.containsKey(fieldPk))
                field = (ReportField)queryFields.get(fieldPk);
            else
                field = (ReportField)children.get(fieldPk);
            
            outputFields.add(field);                    
        }
        
        return outputFields;            
    }    
    
    /*----------------------- Selection Criteria Fields p3 ------------------- */
    
    /** Getter for property selectedCriteriaFields.
     * @return Value of property selectedCriteriaFields.
     */
    public java.lang.Integer[] getSelectedCriteriaFields() {
        return this.selectedCriteriaFields;
    }
    
    /** Setter for property selectedCriteriaFields.
     * @param selectedCriteriaFields New value of property selectedCriteriaFields.
     */
    public void setSelectedCriteriaFields(java.lang.Integer[] selectedCriteriaFields) {
        this.selectedCriteriaFields = selectedCriteriaFields;
    }
    
    /** Getter for property previousSelectedCriteriaFields.
     * @return Value of property previousSelectedCriteriaFields.
     */
    public java.lang.Integer[] getPreviousSelectedCriteriaFields() {
        return this.previousSelectedCriteriaFields;
    }    

    /** Setter for property previousSelectedCriteriaFields.
     * @param previousSelectedCriteriaFields New value of property previousSelectedCriteriaFields.
     */
    public void setPreviousSelectedCriteriaFields(java.lang.Integer[] previousSelectedCriteriaFields) {
        this.previousSelectedCriteriaFields = previousSelectedCriteriaFields;
    }    
    
    /** Check if the selection criteria fields have been changed since last time */
    public boolean hasSelectionCriteriaFieldsChanged() {
        boolean changed = false;

        // let's check if user has changed the selection
        // if both are null, no change
        if ((selectedCriteriaFields == null) && (previousSelectedCriteriaFields == null)) {
            changed = false;
        }
        else if (selectedCriteriaFields == null) {
            if (previousSelectedCriteriaFields.length != 0)
                changed = true;
            else
                changed = false;
        }
        else if (previousSelectedCriteriaFields == null) {
            if (selectedCriteriaFields.length != 0)
                changed = true;
            else
                changed = false;
        }
        else {
            // if both the old and new are empty, no change           
            if ((selectedCriteriaFields.length == 0) && (previousSelectedCriteriaFields.length == 0))
                changed = false;
            else if (selectedCriteriaFields.length != previousSelectedCriteriaFields.length)   // if the old length is not the same as the new length, changed.
                changed = true;
            else {
                // now, we only left with equal length arrays. We need to check if the items are the same
                Arrays.sort(selectedCriteriaFields);
                Arrays.sort(previousSelectedCriteriaFields); 
                boolean arrayEqual = Arrays.equals(selectedCriteriaFields, previousSelectedCriteriaFields);
                if (arrayEqual) 
                    changed = false;
                else
                    changed = true;
            }
        }        
        
        return changed;
    } 
    
    /*---------------------- Selection Criteria p4 ---------------------------*/  

    /** Getter for property criteria.
     * @return Value of property criteria.
     */
    public Map getCriteria() {
        return criteria;
    }    
    
    /** Setter for property criteria.
     * @param criteria New value of property criteria.
     */
    public void setCriteria(Map criteria) {
        this.criteria = criteria;
    }
    
    // set up the criteria from the criteria in database
    // and set up the editable array
    public void setUpCriteria(Map dbCriteria) throws Exception {
        if ((dbCriteria == null) || (dbCriteria.isEmpty()))
            return;
        
        MaintainCodes codeBean = JNDIUtil.getMaintainCodesHome().create();
        Map allCodes = null;
        
        criteria = new TreeMap();
        
        Set editables = new HashSet();
        
        Iterator mIt = dbCriteria.entrySet().iterator();
        while (mIt.hasNext()) {
            CriterionMap cMap = new CriterionMap();
            
            Entry entry = (Entry)mIt.next();
            Integer fieldPk = (Integer)entry.getKey();
            List criterionList = (List)entry.getValue();
            
            ReportField field;
            // get the field info
            if (queryFields.containsKey(fieldPk))
                field = (ReportField)queryFields.get(fieldPk);
            else
                field = (ReportField)children.get(fieldPk);
            
            cMap.setFieldPk(fieldPk);
            Iterator lIt = criterionList.iterator();
            if (field.getDisplayType() == 'C') {
                cMap.setAllCodes(codeBean.getCodes(field.getCommonCodeTypeKey()));
                Integer[] selectedCodes = new Integer[criterionList.size()];
                
                int index = 0;
                while (lIt.hasNext()) {
                    ReportCriterionData criterion = (ReportCriterionData)lIt.next();
                    selectedCodes[index++] = criterion.getCodePK();
                    
                    if (criterion.isEditable())
                        editables.add(fieldPk);
                }
                cMap.setSelectedCodes(selectedCodes);
            }
            else {
                while (lIt.hasNext()) {
                    ReportCriterionData criterion = (ReportCriterionData)lIt.next();
           
                    cMap.addCriterion(criterion);
                
                    if (criterion.isEditable())
                        editables.add(fieldPk);
                }
            }
            
            criteria.put(fieldPk, cMap);

        }
        
        codeBean.remove();
        
        // set up the editable array
        editableFields = new Integer[editables.size()];
        Iterator sIt = editables.iterator();
        int index = 0;
        while (sIt.hasNext())
            editableFields[index++] = (Integer)sIt.next();
    }
    
    // reset the criteria when the criteria fields have been changed
    public void resetCriteria() throws Exception {
        // if nothing selected, reset to null
        if (selectedCriteriaFields.length == 0) {
            if (criteria != null) {
                criteria.clear();
                criteria = null;
            }
            return;
        }
        
        Map fields = getNewSelectedCriteriaFields();
        
        if (criteria == null)
            criteria = new TreeMap();
        
        // first, remove the ones user no longer select
        Iterator it = criteria.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry)it.next();
            Integer fieldPk = (Integer)entry.getKey();
            if (!fields.containsKey(fieldPk))     // need to remove this field
                it.remove();
        }
        
        // then, add those the user newly added
        
        // get the maintainCode bean in case we have code field
        MaintainCodes codeBean = JNDIUtil.getMaintainCodesHome().create();
        Map allCodes = null;
                    
        it = fields.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry)it.next();
            Integer fieldPk = (Integer)entry.getKey();
            ReportField field = (ReportField)entry.getValue();
            
            if (!criteria.containsKey(fieldPk)) { // new one
                CriterionMap cMap = new CriterionMap();
                cMap.setFieldPk(fieldPk);
                
                if (field.getDisplayType() != 'C') {
                    ReportCriterionData criterion = new ReportCriterionData();
                    if (field.getDisplayType() == 'D')
                        criterion.setOperator("BT");
                    criterion.setFieldPK(fieldPk);
                    
                    cMap.addCriterion(criterion);
                }
                else {  // this is a code field, we need get all possible codes
                    allCodes = codeBean.getCodes(field.getCommonCodeTypeKey());
                    cMap.setAllCodes(allCodes);
                }
                criteria.put(fieldPk, cMap);
            }
        }
        
        codeBean.remove();
    }        
        
    
    /** Getter for property editableFields.
     * @return Value of property editableFields.
     */
    public java.lang.Integer[] getEditableFields() {
        return this.editableFields;
    }
    
    /** Setter for property editableFields.
     * @param editableFields New value of property editableFields.
     */
    public void setEditableFields(java.lang.Integer[] editableFields) {
        this.editableFields = editableFields;
    }
    
    /** Convert the editable array to List so that we can do search for action
     *  on page 4 (Selection Criteria)
     */
    public List getAllEditableFields() {
        if ((editableFields == null) || (editableFields.length == 0))
            return new LinkedList();
        
        List fields = new LinkedList();
        for (int i = 0; i < editableFields.length; i++)
            fields.add(editableFields[i]);
        
        return fields;
    }    
    
    /** Get the current report fields the user has selected for defining Selection Criteria
     *  on page 4 (Selection Criteria)
     */
    public Map getNewSelectedCriteriaFields() {
        Map fields = new TreeMap();
        
        Integer fieldPk;
        ReportField field;
        for (int i = 0; i < selectedCriteriaFields.length; i++) {
            fieldPk = selectedCriteriaFields[i];
            if (queryFields.containsKey(fieldPk))
                field = (ReportField)queryFields.get(fieldPk);
            else
                field = (ReportField)children.get(fieldPk);
            
            fields.put(fieldPk, field);
        }
        
        return fields;
    } 
    
    // getter method for mapped property
    public CriterionMap getCriterionMap(String fieldPk) {
        return (CriterionMap)criteria.get(Integer.valueOf(fieldPk));
    }
    
    // setter method for mapped property
    public void setCriterionMap(String fieldPk, CriterionMap criterionMap) {
        if (criteria == null)
            criteria = new TreeMap();
        
        criteria.put(Integer.valueOf(fieldPk), criterionMap);
    }
    
    public void addCriterion(Integer fieldPk) {
        if (criteria.containsKey(fieldPk)) {
            CriterionMap cMap = (CriterionMap)criteria.get(fieldPk);
            
            ReportCriterionData criterion = new ReportCriterionData();
            criterion.setFieldPK(fieldPk);
            
            cMap.addCriterion(criterion);
        }
    } 
    
    public void removeCriterion(Integer fieldPk, String cKey) {
        if (criteria.containsKey(fieldPk)) {
            CriterionMap cMap = (CriterionMap)criteria.get(fieldPk);
            
            cMap.removeCriterion(cKey);
        }
    }
     
    
    /*---------------------- Sort Criteria Fields p5 -------------------------*/
    
    /** Getter for property selectedSortFields.
     * @return Value of property selectedSortFields.
     */
    public java.lang.Integer[] getSelectedSortFields() {
        return this.selectedSortFields;
    }
    
    /** Setter for property selectedSortFields.
     * @param selectedSortFields New value of property selectedSortFields.
     */
    public void setSelectedSortFields(java.lang.Integer[] selectedSortFields) {
        this.selectedSortFields = selectedSortFields;
    } 
    
    /** Getter for property previousSelectedSortFields.
     * @return Value of property previousSelectedSortFields.
     */
    public java.lang.Integer[] getPreviousSelectedSortFields() {
        return this.previousSelectedSortFields;
    }
    
    /** Setter for property previousSelectedSortFields.
     * @param previousSelectedSortFields New value of property previousSelectedSortFields.
     */
    public void setPreviousSelectedSortFields(java.lang.Integer[] previousSelectedSortFields) {
        this.previousSelectedSortFields = previousSelectedSortFields;
    }    
    
    public boolean hasSortFieldsChanged() {
        boolean changed = false;

        // let's check if user has changed the selection
        // if both are null, no change
        if ((selectedSortFields == null) && (previousSelectedSortFields == null)) {
            changed = false;
        }
        else if (selectedSortFields == null) {
            if (previousSelectedSortFields.length != 0)
                changed = true;
            else
                changed = false;
        }
        else if (previousSelectedSortFields == null) {
            if (selectedSortFields.length != 0)
                changed = true;
            else
                changed = false;
        }
        else {
            // if both the old and new are empty, no change           
            if ((selectedSortFields.length == 0) && (previousSelectedSortFields.length == 0))
                changed = false;
            else if (selectedSortFields.length != previousSelectedSortFields.length)   // if the old length is not the same as the new length, changed.
                changed = true;
            else {
                // now, we only left with equal length arrays. We need to check if the items are the same
                Arrays.sort(selectedSortFields);
                Arrays.sort(previousSelectedSortFields); 
                boolean arrayEqual = Arrays.equals(selectedSortFields, previousSelectedSortFields);
                if (arrayEqual) 
                    changed = false;
                else
                    changed = true;
            }
        }        
        
        return changed;
    } 
    
    /** Reset the underlining data structure for mapped property "orderDirection"
     *  when the user has changed the selection on page 5 (Sort Criteria Fields).
     */
    public void resetOrderDirections() {
        if (orderDirections != null)
            orderDirections.clear();
        else
            orderDirections = new TreeMap();
        
        for (int i = 0; i < selectedSortFields.length; i++)
            orderDirections.put(selectedSortFields[i].toString(), "A");
    } 
    
    /** Reset the underlining data structure for the mapped property "sort order"
     *  when the user has changed the selection on Page 5 (Sort Criteria Fields).
     */
    public void resetSortFieldsOrder() {
        // first, clear out the old stuff
        if (sortFieldsOrder != null) 
            sortFieldsOrder.clear();
        else
            sortFieldsOrder = new TreeMap();
        
        // then, put in the new ones
        for (int i = 0; i < selectedSortFields.length; i++)
            sortFieldsOrder.put(selectedSortFields[i].toString(), new Short((short)(i+1)));                  
    }    
    
    /*---------------------- Sort Criteria p6    ----------------------------*/
    
    /** Get a list of currently selected report fields */
    public List getSortFields() {
        List fields = new LinkedList();
        
        Integer fieldPk;
        ReportField field;
        for (int i = 0; i < selectedSortFields.length; i++) {
            fieldPk = selectedSortFields[i];
            if (queryFields.containsKey(fieldPk))
                field = (ReportField)queryFields.get(fieldPk);
            else
                field = (ReportField)children.get(fieldPk);
            
            fields.add(field);
        }
        
        return fields;
    } 
    
    /** the getter method for the mapped property "sort order" */
    public Short getSortOrder(String fieldPk) {
        return (Short)sortFieldsOrder.get(fieldPk);
    }
    
    /** the setter method for the mapped property "sort order" */
    public void setSortOrder(String fieldPk, Short order) {
        sortFieldsOrder.put(fieldPk, order);
    }     
    
    /** Getter method for the mapped property "orderDirection" */
    public Object getOrderDirection(String fieldPk) {
        return orderDirections.get(fieldPk);
    }
    
    /** Setter method for the mapped property "orderDirection" */
    public void setOrderDirection(String fieldPk, Object dir) {
        orderDirections.put(fieldPk, dir);
    }  
    
    /** Getter for property sortFieldsOrder.
     * @return Value of property sortFieldsOrder.
     */
    public Map getSortFieldsOrder() {
        return sortFieldsOrder;
    }
    
    /** Setter for property sortFieldsOrder.
     * @param sortFieldsOrder New value of property sortFieldsOrder.
     */
    public void setSortFieldsOrder(Map sortFieldsOrder) {
        this.sortFieldsOrder = sortFieldsOrder;
    }    
    
    /** Getter for property orderDirections.
     * @return Value of property orderDirections.
     */
    public Map getOrderDirections() {
        return orderDirections;
    }
    
    /** Setter for property orderDirections.
     * @param orderDirections New value of property orderDirections.
     */
    public void setOrderDirections(Map orderDirections) {
        this.orderDirections = orderDirections;
    } 
    
    
    /*-----------------------------  Preview Report p7 ------------------------*/
    
    /** Getter for property pk.
     * @return Value of property pk.
     */
    public java.lang.Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     */
    public void setPk(java.lang.Integer pk) {
        this.pk = pk;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /** Getter for property oriName.
     * @return Value of property oriName.
     */
    public java.lang.String getOriName() {
        return oriName;
    }    

    /** Setter for property oriName.
     * @param oriName New value of property oriName.
     */
    public void setOriName(java.lang.String oriName) {
        this.oriName = oriName;
    }     
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property mailingList.
     * @return Value of property mailingList.
     */
    public boolean isMailingList() {
        return mailingList;
    }
    
    /** Setter for property mailingList.
     * @param mailingList New value of property mailingList.
     */
    public void setMailingList(boolean mailingList) {
        this.mailingList = mailingList;
    }
    
    /** Getter for property updateCorrespondenceHistory.
     * @return Value of property updateCorrespondenceHistory.
     */
    public boolean isUpdateCorrespondenceHistory() {
        return updateCorrespondenceHistory;
    }
    
    /** Setter for property updateCorrespondenceHistory.
     * @param updateCorrespondenceHistory New value of property updateCorrespondenceHistory.
     */
    public void setUpdateCorrespondenceHistory(boolean updateCorrespondenceHistory) {
        this.updateCorrespondenceHistory = updateCorrespondenceHistory;
    }
    
    /** Getter for property previewResult.
     * @return Value of property previewResult.
     */
    public List getPreviewResult() {
        return previewResult;
    }    

    /** Getter for property fieldNames.
     * @return Value of property fieldNames.
     */
    public List getFieldNames() {
        return fieldNames;
    }
    
    /** Setter for property fieldNames.
     * @param fieldNames New value of property fieldNames.
     */
    public void setFieldNames(List fieldNames) {
        this.fieldNames = fieldNames;
    }    
    
    /*----------------------  Common         --------------------------------*/
    
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
    
    /** Getter for property queryFields.
     * @return Value of property queryFields.
     */
    public Map getQueryFields() {
        return queryFields;
    }    
    
    /** Setter for property queryFields. 
     * @param queryFields New value of property queryFields.
     */
    public void setQueryFields(Map queryFields) {
        this.queryFields = queryFields;
        
        // let's make a map for all children
        // key -- field_pk, value -- ReportField
        children = new TreeMap();
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
    
    /** Getter for property userFields.
     * @return Value of property userFields.
     */
    public Map getUserFields() {
        return userFields;
    }    
     
    /** Setter for property userFields.
     * @param userFields New value of property userFields.
     */
    public void setUserFields(Map userFields) {
        this.userFields = userFields;
    } 
    
    /** Setup a new map of report fields that are accessible by the current user */
    public void setUpUserFields(Set accessibleFieldPks) { 
        // create the entity-category map
        userFields = new TreeMap(BRUtil.entityOrder());
        Iterator pIt = queryFields.values().iterator();
        while (pIt.hasNext()) {
            ReportField topField = (ReportField)pIt.next();
            Character entity = new Character(topField.getEntityType());
            String category = topField.getCategoryName();
            
            Map categories = (Map)userFields.get(entity);
            if (categories == null) {
                categories = new TreeMap(BRUtil.categoryOrder());
                userFields.put(entity, categories);
            }
            Set fields = (Set)categories.get(category);
            if (fields == null) {
                fields = new TreeSet(BRUtil.fieldOrder());
                categories.put(category, fields);
            }
            // mark the field if accessible
            if (accessibleFieldPks.contains(topField.getPk())) {
                topField.setAccessible(true);
            } else {
                topField.setAccessible(false);
            }
            
            fields.add(topField);            
            
            if (topField.hasChildren()) {
                Iterator cIt = topField.getChildren().iterator();
                while (cIt.hasNext()) {
                    ReportField child = (ReportField)cIt.next();
                    child.setAccessible(accessibleFieldPks.contains(child.getPk()));
                }
            }                    
        }
    }
        
    /** Getter for property linkClicked.
     * @return Value of property linkClicked.
     */
    public java.lang.String getLinkClicked() {
        return linkClicked;
    }
    
    /** Setter for property linkClicked.
     * @param linkClicked New value of property linkClicked.
     */
    public void setLinkClicked(java.lang.String linkClicked) {
        this.linkClicked = linkClicked;
    }    
    
    /** Getter for property children.
     * @return Value of property children.
     */
    public Map getChildren() {
        return children;
    }
    
    /** Setter for property children.
     * @param children New value of property children.
     */
    public void setChildren(Map children) {
        this.children = children;
    }
    
    /** Getter for property codes.
     * @return Value of property codes.
     */
    public Map getCodes() {
        return codes;
    }
    
    /** Setter for property codes.
     * @param codes New value of property codes.
     */
    public void setCodes(Map codes) {
        this.codes = codes;
    }
    
    public Map getCodesByType(String codeType) {
        return (Map)codes.get(codeType);
    }
    
    public Map getAllCodesForField(Integer fieldPk) {       
        ReportField field;
        if (queryFields.containsKey(fieldPk))
            field = (ReportField)queryFields.get(fieldPk);
        else
            field = (ReportField)children.get(fieldPk);
        
        if (field.getDisplayType() != 'C')
            return null;
        
        String codeTypeKey = field.getCommonCodeTypeKey();
        
        Map allCodes = (Map)codes.get(codeTypeKey);
        
        return allCodes;
        
    }
    
    /** Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }    
   
    /** Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }    
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     */
    public java.lang.Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     */
    public void setPersonPk(java.lang.Integer personPk) {
        this.personPk = personPk;
    }    
    
    /** Generate the report preview result and store it in a row-column map */
    public void generatePreviewQuery(UserSecurityData usd) throws Exception {

        ReportGenerator reportGenerator = new ReportGenerator(queryFields, getReport(), ConfigUtil.getConfigurationData().getSearchResultPageSize(), usd.getAccessibleAffiliates());
        previewResult = new LinkedList();
        totalRows = reportGenerator.generateReport(previewResult);
        fieldNames = reportGenerator.getOutputFieldNames();
    }
 
    /** Getter for property saveButton.
     * @return Value of property saveButton.
     */
    public java.lang.String getSaveButton() {
        return saveButton;
    }    
    
    /** Setter for property saveButton.
     * @param saveButton New value of property saveButton.
     */
    public void setSaveButton(java.lang.String saveButton) {
        this.saveButton = saveButton;
    }
    
    /** Getter for property saveAsButton.
     * @return Value of property saveAsButton.
     */
    public java.lang.String getSaveAsButton() {
        return saveAsButton;
    }
    
    /** Setter for property saveAsButton.
     * @param saveAsButton New value of property saveAsButton.
     */
    public void setSaveAsButton(java.lang.String saveAsButton) {
        this.saveAsButton = saveAsButton;
    }
    
    /** Getter for property totalRows.
     * @return Value of property totalRows.
     *
     */
    public int getTotalRows() {
        return totalRows;
    }
    
    /** Setter for property totalRows.
     * @param totalRows New value of property totalRows.
     *
     */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

        // get the current report
    public Report getReport() {
        Report report = new Report();
        
        ReportData reportData = new ReportData();
        reportData.setPk(pk);
        reportData.setName(name);
        reportData.setDescription(description);
        reportData.setCanAddEntities(false);
        reportData.setMailingList(mailingList);
        reportData.setCustom(true);
        reportData.setNeedUpdateCorrespondence(updateCorrespondenceHistory);
        reportData.setCustomHandlerClassName(null);
        reportData.setCountReport(countQuery);
        reportData.setLastUpdateUID(userId);
        Timestamp time = new Timestamp(new Date().getTime());
        reportData.setLastUpateDate(time);
        reportData.setOwnerPK(personPk);
        
        // output fields
        Map outputFields = new TreeMap();
        ReportOutputFieldData outputFieldData;
        
        Integer fieldPk;
        Iterator it;
        if (!isCountQuery()) {      // only collect output fields if it isn't a count query.
            for (int i = 0; i < selectedOutputFields.length; i++) {
                fieldPk = selectedOutputFields[i];
                outputFieldData = new ReportOutputFieldData();
                outputFieldData.setFieldPK(fieldPk);
                outputFieldData.setOutputOrder(((Short)outputFieldsOrder.get(fieldPk.toString())).shortValue());
            
                outputFields.put(fieldPk, outputFieldData);
            }
        }
        
        // criteria fields (needs to setup the "editable" too for each field)
        Map dbCriteria = new TreeMap();
        List criterionList;
        
        List editableFields = getAllEditableFields();
        boolean editable = false;
        
        if (criteria != null) {
            Entry entry;
            CriterionMap cMap;
            it = criteria.entrySet().iterator();
            while (it.hasNext()) {
                entry = (Entry)it.next();
                fieldPk = (Integer)entry.getKey();
                cMap = (CriterionMap)entry.getValue();
            
                if (editableFields.contains(fieldPk))
                    dbCriteria.put(fieldPk, cMap.getAllCriterion(true));
                else
                    dbCriteria.put(fieldPk, cMap.getAllCriterion(false));
            }
        }
        
        // sort fields
        Map sortFields = new TreeMap();
        ReportSortFieldData sortFieldData;
        
        if ((sortFieldsOrder != null) && (!sortFieldsOrder.isEmpty())) {
            it = sortFieldsOrder.entrySet().iterator();
            Entry element;
            short order;
            while (it.hasNext()) {
                element = (Entry)it.next();
                fieldPk = new Integer((String)element.getKey());
                order = ((Short)element.getValue()).shortValue();
            
                sortFieldData = new ReportSortFieldData();
                sortFieldData.setFieldPK(fieldPk);
                sortFieldData.setFieldSortOrder(order);
                sortFieldData.setFieldSortDirection((String)orderDirections.get(fieldPk.toString()));
            
                sortFields.put(fieldPk, sortFieldData);
            }
        }
        
        report.setReportPK(pk);
        report.setReportData(reportData);
        report.setOutputFields(outputFields);
        report.setCriteriaFields(dbCriteria);
        report.setSortFields(sortFields);
        
        return report;
    }

    /* JZhang
     * @parm errors: ActionErrors object
     * @parm name: value that need to be verified
     * @parm prop: the name used on jsp 
     */
    private void nameMatch(ActionErrors errors, String name) 
    {
        logger.debug("----------------------------------------------------------------------");
        logger.debug("QueryForm:nameMatch Begin");
        logger.debug("----------------------------------------------------------------------");
        try
        {
            boolean match = Pattern.matches("([a-z_A-Z]{1}[a-z_A-Z_0-9]{0,24})", name);
            if (match == false ){
                logger.debug("PersonDetailForm:nameMatch -- An error is added.");
                errors.add(errors.GLOBAL_ERROR, new ActionError("error.field.incorrect.name", "Query Name"));
            }
        }catch (PatternSyntaxException pse)
        {
            logger.debug("PersonDetailForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }       
    }    	
}
