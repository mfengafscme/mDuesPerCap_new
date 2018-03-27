/*
 * CriterionMap.java
 *
 * This class is a helper class used by the GUI to support a combination of 
 * double-mapped, then nested Struts properties. That is:
 * runtimeCriteria:
 *      fieldPk --> CriterionMap
 *                      cKey --> ReportCriterionData
 */
package org.afscme.enterprise.reporting.base.access;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;


public class CriterionMap {
    
    private Integer fieldPk = null;
    
    private int index = 0;          // indicate the current key index
    
    private Map criteria = null;    // key -- String (c1..cn), value -- ReportCriterionData
    
    // following two data elements are for code field only
    
    // key -- cd_pk, value - CodeData
    private Map allCodes = null;    // keeps all code of a particular code type
    
    private Integer[] selectedCodes = new Integer[0];
    
    private Integer[] previousSelectedCodes = null;
    
    public CriterionMap() {
        criteria = new TreeMap();
    }
    
    public void reset() {
        selectedCodes = new Integer[0];
    }
    
    public void addCriterion(ReportCriterionData criterion) {
        // get the current key
        String key = "c" + index;
        index++;
        criteria.put(key, criterion);
    }
    
    public void removeCriterion(String cKey) {
        if (criteria.containsKey(cKey))
            criteria.remove(cKey);
    }
    
    public int getSize() {
        return criteria.size();
    }
    
    public Set getKeys() {
        if ((criteria == null) || (criteria.isEmpty()))
            return new TreeSet();
        
        return criteria.keySet();
    }
    
    // used by qForm's getReport method to form the DB version of criteria
    // here, we need to reset the sequence numbers of each criterion after all
    // the "add" and "remove" actions.
    public List getAllCriterion(boolean editable) {

        List result = new LinkedList();
        
        // first, add all selected code field into criteria map if any
        resetPreviousSelection();        
        if (hasSelectedCodesChanged()) {
            addCodeCriteria();            
            // reset the previous collection
            resetPreviousSelection();        
        }

        if ((criteria == null) || (criteria.isEmpty()))
            return result;
        
        int seq = 0;
        ReportCriterionData criterion;
        Iterator it = criteria.values().iterator();
        while (it.hasNext()) {
            criterion = (ReportCriterionData)it.next();
            criterion.setCriterionSequence(seq++);
            criterion.setEditable(editable);
            
            result.add(criterion);
        }
        
        return result;
    }
    
    // a mapped property getter method
    public ReportCriterionData getCriterion(String key) {
        return (ReportCriterionData)criteria.get(key);
    }
    
    // a mapped property setter method
    public void setCriterion(String key, ReportCriterionData criterion) {
        if (criteria.containsKey(key))
            criteria.put(key, criterion);
    }   
    
    /** Getter for property allCodes.
     * @return Value of property allCodes.
     */
    public java.util.Map getAllCodes() {
        return allCodes;
    }
    
    /** Setter for property allCodes.
     * @param allCodes New value of property allCodes.
     */
    public void setAllCodes(java.util.Map allCodes) {
        this.allCodes = allCodes;
    }
    
    /** Getter for property selectedCodes.
     * @return Value of property selectedCodes.
     */
    public java.lang.Integer[] getSelectedCodes() {
        return this.selectedCodes;
    }
    
    /** Setter for property selectedCodes.
     * @param selectedCodes New value of property selectedCodes.
     */
    public void setSelectedCodes(java.lang.Integer[] selectedCodes) {
        this.selectedCodes = selectedCodes;
    }
    
    /** Check if the output field selections has been changed by the user. We use this to determine
     *  if the second wizard page (output field order) needs to be cleared.
     */
    private boolean hasSelectedCodesChanged() {
        boolean changed = false;

        // let's check if user has changed the selection
        // if both are null, no change
        if ((selectedCodes == null) && (previousSelectedCodes == null))
            changed = false;
        else if (selectedCodes == null)
            changed = (previousSelectedCodes.length != 0);
        else if (previousSelectedCodes == null)
            changed = (selectedCodes.length != 0);
        else {
            // if both the old and new are empty, no change
            if ((selectedCodes.length == 0) && (previousSelectedCodes.length == 0))
                changed = false;
            else if (selectedCodes.length != previousSelectedCodes.length)  // if the old length is not the same as the new length, changed.
                changed = true;
            else {
                // now, we only left with equal length arrays. We need to check if the items are the same
                Arrays.sort(selectedCodes);
                Arrays.sort(previousSelectedCodes);                
                changed = Arrays.equals(selectedCodes, previousSelectedCodes);
            }
        }        
        
        return changed;
    } 
    
    private void resetPreviousSelection() {
        Integer[] currentSelection = getSelectedCodes();
        if ((currentSelection == null) || (currentSelection.length == 0)) {
            setPreviousSelectedCodes(null);
            return;
        }
        
        Integer[] previousSelection = new Integer[currentSelection.length];
        for (int i = 0; i < currentSelection.length; i++) {
            if (currentSelection[i] == null)
                return;
            previousSelection[i] = new Integer(currentSelection[i].intValue());
        }
        
        setPreviousSelectedCodes(previousSelection);        
    }     
    
    private void addCodeCriteria() {      
        if (selectedCodes == null) return;     
        
        // since this is a code field, we can destroy the entire criteria and rebuild
        // it from the new set of codes selected 
        criteria.clear();
        index = 0;
        
        ReportCriterionData criterion;
        for (int i = 0; i < selectedCodes.length; i++) {
            if(selectedCodes[i] == null)
                continue;
            criterion = new ReportCriterionData();
            criterion.setFieldPK(fieldPk);
            criterion.setCodePK(selectedCodes[i]);
            criterion.setOperator("IN");
           // criterion.setValue1(selectedCodes[i].toString());
            criterion.setCodeField(true);
            
            addCriterion(criterion);
        }
           
    }
    
    /** Getter for property fieldPk.
     * @return Value of property fieldPk.
     */
    public java.lang.Integer getFieldPk() {
        return fieldPk;
    }
    
    /** Setter for property fieldPk.
     * @param fieldPk New value of property fieldPk.
     */
    public void setFieldPk(java.lang.Integer fieldPk) {
        this.fieldPk = fieldPk;
    }
    
    /** Getter for property previousSelectedCodes.
     * @return Value of property previousSelectedCodes.
     */
    public java.lang.Integer[] getPreviousSelectedCodes() {
        return this.previousSelectedCodes;
    }
    
    /** Setter for property previousSelectedCodes.
     * @param previousSelectedCodes New value of property previousSelectedCodes.
     */
    public void setPreviousSelectedCodes(java.lang.Integer[] previousSelectedCodes) {
        this.previousSelectedCodes = previousSelectedCodes;
    }
    
}
