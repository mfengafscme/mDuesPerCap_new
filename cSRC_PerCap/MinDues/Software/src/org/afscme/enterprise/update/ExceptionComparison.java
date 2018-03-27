package org.afscme.enterprise.update;


/**
 * Represents the comparison between a field in the update file, and a field in 
 * the database.
 */
public class ExceptionComparison {
    
    /**
     * Name of the field (for display purpose)
     *
     * @TODO:   Evaluate if this field is needed. Should be handled by the PK 
     *          that is the key for this object in the ExceptionData's map.
     */
    protected String field = null;
    
    /**
     * The value of the filed in the file
     */
    protected String valueInFile = null;
    
    /**
     * The value of the field in the system
     */
    protected String valueInSystem = null;
    
    /**
     * True iff the field is in error
     */
    // Note, this is neccessary because when there is an exception(Error), all fields
    // including those without error need to be be recorded for that member(officer...).
    protected boolean error = false;
    //******************************************************************************
    //Maps to record id in the database
    protected int recordId  =   0;
    //*******************************************************************************
    public ExceptionComparison() {
        field = ""; // this is probably not needed any longer...
    }
    
    public ExceptionComparison(String fieldName) {
        field = fieldName;
    }
    
    public String toString() {
        return  "ExceptionComparison [" +
                "field=" + field +
                ", valueInFile=" + valueInFile +
                ", valueInSystem=" + valueInSystem +
                ", error=" + error +
                "]"
        ;
    }
    
    /** Getter for property error.
     * @return Value of property error.
     *
     */
    public boolean isError() {
        return error;
    }
    /*****************************************************************/
    /*Made a additional getter property for the error field                     */
    /*****************************************************************/
    /** Getter for property error.
     * @return Value of property error.
     *
     */
    public boolean getError() {
        return error;
    }
    /******************************************************************/
    /** Setter for property error.
     * @param error New value of property error.
     *
     */
    public void setError(boolean error) {
        this.error = error;
    }
    
    /** Getter for property field.
     * @return Value of property field.
     *
     */
    public String getField() {
        return field;
    }
    
    /** Setter for property field.
     * @param field New value of property field.
     *
     */
    public void setField(String field) {
        this.field = field;
    }
    
    /** Getter for property valueInFile.
     * @return Value of property valueInFile.
     *
     */
    public String getValueInFile() {
        return valueInFile;
    }
    
    /** Setter for property valueInFile.
     * @param valueInFile New value of property valueInFile.
     *
     */
    public void setValueInFile(String valueInFile) {
        this.valueInFile = valueInFile;
    }
    
    /** Getter for property valueInSystem.
     * @return Value of property valueInSystem.
     *
     */
    public String getValueInSystem() {
        return valueInSystem;
    }
    
    /** Setter for property valueInSystem.
     * @param valueInSystem New value of property valueInSystem.
     *
     */
    public void setValueInSystem(String valueInSystem) {
        this.valueInSystem = valueInSystem;
    }
    /*******************************************************************************/
    /*Added this getter and setter method so that it is easier to corelate to the 
     *summary record
     *******************************************************************************/
    /** Getter for property RecordId.
     * @return Value of property RecordId.
     *
     */
    public int getRecordId() {
        return recordId;
    }
    
    /** Setter for property RecordId.
     * @param valueInSystem New value of property RecordId.
     *
     */
    public void setRecordId(int id) {
        this.recordId = id;
    }
    /**************************************************************************************/
}
