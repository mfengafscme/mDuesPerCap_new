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
 * @struts:form name="searchLogForm"
 */
public class SearchLogForm extends SearchForm {
    
    private static Logger logger = Logger.getLogger(SearchLogForm.class);
    
    private   String    log;
    protected Integer   affPk       = null;
    protected Character affType     = null;
    protected String    affLocal    = null;
    protected String    affState    = null;
    protected String    affSubunit  = null;
    protected String    affCouncil  = null;
    protected Character affCode     = null;
    
    protected String    fromDate    = null;
    protected String    toDate      = null;
    
    
    protected int updateType;
    protected int sort;
    
    //********************************************************************************
    private AffiliateIdentifier affId;    
    private Integer queuePk;    
    private String fileName;    
    private String filePath;    
    private int status;    
    private int fileType;    
    private int fileQueue;    
    private int updateTypes;    
    private String comments;    
    private Timestamp processedDate;    
    private Timestamp validDate;    
    private Timestamp receivedDate;    
    /** Creates a new instance of SearchLogForm */
    
   
    public SearchLogForm() {
        //affType = new Character('L');
        
    }
    
    
    
   
     /** Getter for property affType.
     * @return Value of property affType.
     */
    public Character getAffType() {
        return affType;
    }    

    /** Setter for property affType.
     * @param affType New value of property affType.
     */
    public void setAffType(Character affType) {
        this.affType = affType;
    }    
    
    /** Getter for property affLocal.
     * @return Value of property affLocal.
     */
    public String getAffLocal() {
        return affLocal;
    }
    
    /** Setter for property affLocal.
     * @param affLocal New value of property affLocal.
     */
    public void setAffLocal(String affLocal) {
        this.affLocal = affLocal;
    }
    
    /** Getter for property affState.
     * @return Value of property affState.
     */
    public String getAffState() {
        return affState;
    }
    
    /** Setter for property affState.
     * @param affState New value of property affState.
     */
    public void setAffState(String affState) {
        this.affState = affState;
    }
    
    /** Getter for property affSubunit.
     * @return Value of property affSubunit.
     */
    public String getAffSubunit() {
        return affSubunit;
    }
    
    /** Setter for property affSubunit.
     * @param affSubunit New value of property affSubunit.
     */
    public void setAffSubunit(String affSubunit) {
        this.affSubunit = affSubunit;
    }
    
    /** Getter for property affCouncil.
     * @return Value of property affCouncil.
     */
    public String getAffCouncil() {
        return affCouncil;
    }
    
    /** Setter for property affCouncil.
     * @param affCouncil New value of property affCouncil.
     */
    public void setAffCouncil(String affCouncil) {
        this.affCouncil = affCouncil;
    }
    
  
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property affCode.
     * @return Value of property affCode.
     *
     */
    public Character getAffCode() {
        return affCode;
    }
    
    /** Setter for property affCode.
     * @param affCode New value of property affCode.
     *
     */
    public void setAffCode(Character affCode) {
        this.affCode = affCode;
    }
    
    /** Getter for property updateType.
     * @return Value of property updateType.
     *
     */
    public int getUpdateType() {
        return updateType;
    }
    
    /** Setter for property updateType.
     * @param updateType New value of property updateType.
     *
     */
    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }
    /** Getter for property sort.
     * @return Value of property sort.
     *
     */
    public int getSort() {
        return sort;
    }
    
    /** Setter for property sort.
     * @param sort New value of property sort.
     *
     */
    public void setSort(int sort) {
        this.sort = sort;
    }
    /** Getter for property fromDate.
     * @return Value of property fromDate.
     */
    public String getFromDate() {
        return fromDate;
    }
    
    /** Setter for property fromDate.
     * @param fromDate New value of property fromDate.
     */
    public void setFromDate(String fromDate) { 
        this.fromDate = fromDate;
    }
    /** Getter for property toDate.
     * @return Value of property ToDate.
     */
    public String getToDate() {
        return toDate;
    }
    
    /** Setter for property toDate.
     * @param toDate New value of property ToDate.
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    /***********************************************************************************************************/
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        logger.debug("----------------------------------------------------");
        logger.debug("validate called.");
        
        ActionErrors errors = new ActionErrors();
        
        //from date is not empty
        if((fromDate != null) && (!fromDate.equals(""))){
            logger.debug("fromDate===============>." + fromDate);
            if (!parseDate(fromDate) ) {//need to talk with Guy or Pamela about using DateUtil
            //logger.debug("DateUtil.getTimestamp(fromDate)===============>." + DateUtil.getTimestamp(fromDate));
            //if(DateUtil.getTimestamp(fromDate) == null){
                logger.debug("failed to parse fromDate===============>." + fromDate);
                errors.add("invalid from date format", new ActionError("error.update.fromdateformat"));
                return errors;
            }
        }    
        //fromDate and toDate are not empty
        if(((fromDate != null) && (!fromDate.equals(""))) && ((toDate !=null) && (!toDate.equals("")))){
            logger.debug("toDate===============>." + toDate);
            logger.debug("parsing toDate===============>." + toDate);
            if (!parseDate(toDate) ) {
                logger.debug("parsed toDate===============>." + toDate);
                
                logger.debug("compared toDate===============>." + toDate);
                //if(DateUtil.getTimestamp(toDate) == null){
                logger.debug("failed to parse toDate===============>." + toDate);
                errors.add("invalid to date format", new ActionError("error.update.todateformat"));
                return errors;

            }
            //fromDate is empty but toDate is filled. fromDate is req.
        }else if(((fromDate == null) || (fromDate.equals(""))) && ((toDate !=null) && (!toDate.equals("")))){
            logger.debug("failed to parse both dates===============>." );
            errors.add("invalid to date format", new ActionError("error.update.missingfromdate"));
            return errors;
        }
        if(((fromDate != null) && (!fromDate.equals(""))) && ((toDate !=null) && (!toDate.equals("")))){
            if(!compare(fromDate, toDate)){
                logger.debug("parsed toDate===============>." + toDate);

                    logger.debug("compared toDate===============>." + toDate);
                    //if(DateUtil.getTimestamp(toDate) == null){
                    logger.debug("failed to parse toDate===============>." + toDate);
                    errors.add("from date less than to date", new ActionError("error.update.fromdatelessthantoDate"));
                    return errors;

            }
        }
        return errors;
    }
    /**************************************************************************************************************/
    public boolean parseDate(String date){
        
        logger.debug(" parseDate, date.indexOf('/')====================>" + date);
        
        boolean slash       = date.indexOf('/') > 0 ? true: false;
        if(!slash){
            boolean dash    = date.indexOf('-') > 0 ? true: false;  
            if(!dash){
                return false;
            }else{
                return validateDate('-', date);
            }
        }else{
            return validateDate('/', date);
        }
        
    }//end of parsemethod
    
    public boolean validateDate(char separator, String date){
        boolean isValidDate   =   true; //flag for date validity
        logger.debug("separator====================>" + separator);
        logger.debug("date====================>" + date);
        int month   = new Integer(date.substring(0, date.indexOf(separator))).intValue() ;
        logger.debug("month====================>" + month);
        
        date        = date.substring(date.indexOf(separator) + 1); 
        logger.debug("date====================>" + date);
        int day     = new Integer(date.substring(0, date.indexOf(separator))).intValue();
        
        logger.debug("day====================>" + day);
        String sYear = date.substring(date.indexOf(separator) + 1);
        
        
        if(sYear.length() !=4){
            logger.debug("year====================>" + sYear);
            isValidDate     =  false;
        }
        
        int iYear = new Integer(sYear).intValue();
        if(((iYear%4 == 0) || (iYear%400==0))){//leap year
            if((month == 2) && !((day >0) && (day < 30))){//february last day cannot exceed 29
                isValidDate =  false;
            }else if(!((day >0) && (day < 32))){//other month days 
                isValidDate =  false;
            }   
                
        }else{
            if((month == 2) && !((day >0) && (day < 29))){//february last day cannot exceed 28
                isValidDate =  false;
            }else if(!((day >0) && (day < 32))){//other month days 
                isValidDate =  false;
            }
        }
        if(!((month > 0) && (month < 13))){
            isValidDate     = false;
        }
        return isValidDate;
    }//end of validateDate method
    
    public boolean compare(String fromDate, String toDate){
        String date       = "";
        char separator    = '/';
        logger.debug("compare====================>fromDate" + fromDate + "toDate===>" + toDate);
        //
        int fromMonth   = new Integer(fromDate.substring(0, fromDate.indexOf(separator))).intValue() ;
        logger.debug("month====================>" + fromMonth);
        
        fromDate        = fromDate.substring(fromDate.indexOf(separator) + 1); 
        logger.debug("date====================>" + fromDate);
        int fromDay     = new Integer(fromDate.substring(0, fromDate.indexOf(separator))).intValue();
        
        logger.debug("day====================>" + fromDay);
        int fromYear = new Integer(fromDate.substring(fromDate.indexOf(separator) + 1)).intValue();
        
        int toMonth   = new Integer(toDate.substring(0, toDate.indexOf(separator))).intValue() ;
        logger.debug("month====================>" + toMonth);
        
        toDate        = toDate.substring(toDate.indexOf(separator) + 1); 
        logger.debug("date====================>" + toDate);
        int toDay     = new Integer(toDate.substring(0, toDate.indexOf(separator))).intValue();
        
        logger.debug("day====================>" + toDay);
        int toYear = new Integer(toDate.substring(toDate.indexOf(separator) + 1)).intValue();
        
        if ((fromYear > toYear) ){//from year cannot be greater than toYear
            return false;
        }
        
        if ((fromYear == toYear) && (fromMonth > toMonth)){//from year being equal fromMonth should be less than toMonth
            return false;
        }
        
        if ((fromYear == toYear) && (fromMonth == toMonth) && (fromDay > toDay)){//from year & fromMonth being equal to toYear and toMonth fromDay should be less than toDay
            return false;
        }
        
        return true;
    }
    public SearchLogForm getSearchLogCriteriaData() {
        
        SearchLogForm data = new SearchLogForm();
        if (!TextUtil.isEmptyOrSpaces(affType))
            data.setAffType(affType);
        if (!TextUtil.isEmptyOrSpaces(affLocal))
            data.setAffLocal(affLocal);
        if (!TextUtil.isEmptyOrSpaces(affState))
            data.setAffState(affState);
        if (!TextUtil.isEmptyOrSpaces(affSubunit))
            data.setAffSubunit(affSubunit);
        
        if (!TextUtil.isEmptyOrSpaces(affCouncil))
            data.setAffCouncil(affCouncil);
        if (!TextUtil.isEmptyOrSpaces(fromDate))
            data.setFromDate(fromDate);
        if (!TextUtil.isEmptyOrSpaces(toDate))
            data.setToDate(toDate);
        if (!TextUtil.isEmptyOrSpaces(new Integer(updateType).toString()))
            data.setUpdateType(updateType);
        // set page and sort values
        //data.setPage(page);
        //data.setPageSize(getPageSize());        
        //data.setOrderBy(sortBy);        
        //data.setOrdering(order);        
        return data;
    }
    //************************************************************************************************************
    /** Getter for property affId.
     * @return Value of property affId.
     */
    public AffiliateIdentifier getAffId() {
        return affId;
    }
    
    /** Setter for property affId.
     * @param affId New value of property affId.
     */
    public void setAffId(AffiliateIdentifier affId) {
        this.affId = affId;
    }
    
    /** Getter for property queuePk.
     * @return Value of property queuePk.
     */
    public Integer getQueuePk() {
        return queuePk;
    }
    
    /** Setter for property queuePk.
     * @param queuePk New value of property queuePk.
     */
    public void setQueuePk(Integer queuePk) {
        this.queuePk = queuePk;
    }
    
    /** Getter for property fileName.
     * @return Value of property fileName.
     */
    public String getFileName() {
        return fileName;
    }
    
    /** Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /** Getter for property filePath.
     * @return Value of property filePath.
     */
    public String getFilePath() {
        return filePath;
    }
    
    /** Setter for property filePath.
     * @param filePath New value of property filePath.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    /** Getter for property processedDate.
     * @return Value of property processedDate.
     */
    public Timestamp getProcessedDate() {
        return processedDate;
    }
    
    /** Setter for property processedDate.
     * @param processedDate New value of property processedDate.
     */
    public void setProcessedDate(Timestamp processedDate) {
        this.processedDate = processedDate;
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
    
    /** Getter for property receivedDate.
     * @return Value of property receivedDate.
     */
    public Timestamp getReceivedDate() {
        return receivedDate;
    }
    
    /** Setter for property receivedDate.
     * @param receivedDate New value of property receivedDate.
     */
    public void setReceivedDate(Timestamp receivedDate) {
        this.receivedDate = receivedDate;
    }
    
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public int getStatus() {
        return status;
    }    
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    /** Getter for property fileType.
     * @return Value of property fileType.
     *
     */
    public int getFileType() {
        return fileType;
    }
    
    /** Setter for property fileType.
     * @param fileType New value of property fileType.
     *
     */
    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
    
    /** Getter for property fileQueue.
     * @return Value of property fileQueue.
     *
     */
    public int getFileQueue() {
        return fileQueue;
    }
    
    /** Setter for property fileQueue.
     * @param fileQueue New value of property fileQueue.
     *
     */
    public void setFileQueue(int fileQueue) {
        this.fileQueue = fileQueue;
    }
    
    /** Getter for property updateType.
     * @return Value of property updateType.
     *
     */
    public int getUpdateTypes() {
        return updateType;
    }
    
    /** Setter for property updateType.
     * @param updateType New value of property updateType.
     *
     */
    public void setUpdateTypes(int updateType) {
        this.updateType = updateType;
    }
    
    
}

