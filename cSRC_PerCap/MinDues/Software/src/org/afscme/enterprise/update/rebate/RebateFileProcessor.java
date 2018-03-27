package org.afscme.enterprise.update.rebate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateHierarchyEntry;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.ejb.*;
import org.afscme.enterprise.update.AddressElement;
import org.afscme.enterprise.update.FileProcessor;
import org.afscme.enterprise.update.PersonReviewData;
import org.afscme.enterprise.update.PreUpdateSummary;
import org.afscme.enterprise.update.UpdateErrorCodes;
import org.afscme.enterprise.update.ejb.*;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;



/**
 * Handles import of a rebate data file.
 */
public class RebateFileProcessor extends FileProcessor {    

    private static final String REBATE_MEMBER_TYPE = "RebateMbrType";
    private static final String REBATE_MEMBER_STATUS = "RebateMbrStatus";
    private static final String DUES_TYPE = "DuesType";
    private static final String REBATE_DURATION = "RebateDuration";
    private static final String REBATE_ACCEPTANCE_CODE = "RebateAcceptanceCode";

    private static Logger logger = Logger.getLogger(RebateFileProcessor.class);

    private MaintainAffiliates maintainAffiliates;  // reference to the MaintainAffiliatesBean
    private MaintainCodes codeBean;  // reference to the MaintainCodesBean
    private Update updateBean;      // reference to the update bean

    private RebateUpdateElement currRebate = null;  // the current rebate member being processed
    private RebatePreUpdateSummary preUpdateSummary = null;
    private RebateUpdateSummary updateSummary = null;    

    // map of affiliates in the update file
    // key -- affPk, value -- affId
    private Map affiliates;

    
    /** 
     * Constructor that also sets up enterprise beans it needs.  
     *
     * @param maintainAffBean   Reference to MaintainAffiliatesBean.
     * @param maintainCodes   Reference to MaintainCodesBean.
     */
    public RebateFileProcessor(MaintainAffiliates maintainAffBean, MaintainCodes maintainCodes) {

        maintainAffiliates = maintainAffBean;
        codeBean = maintainCodes;

        try {
            updateBean = JNDIUtil.getUpdateHome().create();
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        } catch (CreateException ce) {
            throw new RuntimeException(ce);
        }
        
        affiliates = new HashMap();
    }
        
    /** 
     * Applies the Update file by processing the file, determining the  
     * exceptions, and performing the update.
     *
     * @param affPks   The list of affPks to process (checked on the screen).
     * @param queuePk   The queue primary key to determine the file.
     * @param userPk   User Pk of the user changing the data
     */
    
    public void applyUpdate(List affPks, Integer queuePk, Integer userPk) {
        
        logger.debug("----------------------------------------------------");
        logger.debug("applyUpdate called.");
        
        updateSummary = new RebateUpdateSummary();
        
        // process the file to determine exceptions and rebates to process for selected affPks
        processFixedLengthFileForUpdate(affPks);
       
        // perform the update for all data changed
        updateBean.doRebateUpdate(userPk, fileEntry.getQueuePk(), updateSummary);
    }
    
    /** 
     * Creates the Pre-Update Summary by processing the file, determining the  
     * counts, and checking for Affiliate errors and warnings.
     *
     * @return  PreUpdateSummary The generated summary for all counts and exceptions
     */
    public PreUpdateSummary generatePreUpdateSummary() {
        
        logger.debug("----------------------------------------------------");
        logger.debug("generatePreUpdateSummary called.");

        // NOTE, the rebate update file is a positional text file.
        preUpdateSummary = new RebatePreUpdateSummary();
        
        // process the file to determine exceptions and rebates to process
        processFixedLengthFile();
        
        /* ------------------------------------------------------------------------
           now that each rebate record in the file is processed, we have a list of affiliates
           these rebates belong to.  Now determine rebate records in the system.
           ------------------------------------------------------------------------ */
        updateBean.calculateRebateSentCounts(affiliates.keySet(), preUpdateSummary);
        
        // check all affiliate level errors/warnings.
        postMatchCheckOnAffiliateErrorWarning(preUpdateSummary);
          
        return preUpdateSummary;        
    }    
    
    /** 
     * Validates that the file using the following criteria:
     *  1. the file can be opened
     *  2. the file is greater than a configurable size
     *  3. each line contains 283 characters
     *  4. at least one affiliate number exists
     *  5. at least one affiliate member number exists
     *  6. at least one zip code exists
     *
     * @return 0 if valid, else the error code if invalid
     */
    public int validate() {
        
        logger.debug("---------------------------------------------------------------");
        logger.debug("validate() called.");
        logger.debug("---------------------------------------------------------------");
        logger.debug("config data: " + ConfigUtil.getConfigurationData());
        logger.debug("---------------------------------------------------------------");
        
        int valid = 0;
        BufferedReader fin = null;
        
        try {
            File file = new File(fileEntry.getFilePath() + fileEntry.getFileName());
            logger.debug("File info for file: " + (fileEntry.getFilePath() + fileEntry.getFileName()));
            logger.debug("    actual file length:  " + file.length());
            logger.debug("    min file length:     " + ConfigUtil.getConfigurationData().getMinUploadFileSize());
            if (file.length() < ConfigUtil.getConfigurationData().getMinUploadFileSize()) {
                logger.debug("returning invalid.");
                return UpdateErrorCodes.ERROR_FILE_LENGTH_LESS_THAN_CONFIG_VALUE;
            }
            // check if the file can be opened
            fin = new BufferedReader(new FileReader(file));
            
            int memberCounter = 0;
            int missingAffNumCounter = 0;
            int missingMbrNumCounter = 0;
            int invalidAcceptCodeCounter = 0;
            String affLocalNumber = null;
            String affCouncilNumber = null;            
            String mbrNumber = null;
            String acceptCode = null;
            
            // read in one member per line at a time.
            String currLine = fin.readLine();
            int counter = 0;
            while (currLine != null && valid == 0) {
                logger.debug("Line: " + (++counter));
                if (TextUtil.isEmptyOrSpaces(currLine) || 
                    currLine.length() < 284 || 
                    currLine.length() > 285     // check that each line is the correct length
                ) {
                    logger.info("File Validation Error: File Length incorrect - length = " + currLine.length());
                    valid = UpdateErrorCodes.ERROR_FILE_LENGTH_INVALID_REBATE;
                    return valid;
                } else {
                    // count a member
                    memberCounter++;
                    
                    // count a missing affil number
                    affLocalNumber = currLine.substring(1, 5).trim();
                    affCouncilNumber = currLine.substring(11, 15).trim();                    
                    if ( (TextUtil.isEmptyOrSpaces(affLocalNumber) ||
                          TextUtil.isAllZeros(affLocalNumber) ||
                          !TextUtil.isInt(affLocalNumber)) &&
                         (TextUtil.isEmptyOrSpaces(affCouncilNumber) ||
                          TextUtil.isAllZeros(affCouncilNumber) ||
                          !TextUtil.isInt(affCouncilNumber)) )
                    {
                        missingAffNumCounter++;
                    }
                    
                    // count a missing afscme member number
                    mbrNumber = currLine.substring(15, 23).trim();
                    if ((TextUtil.isEmptyOrSpaces(mbrNumber)) || 
                        (TextUtil.isAllZeros(mbrNumber))) {
                        missingMbrNumCounter++;
                    }
                    
                    // count a missing or invalid acceptance code
                    acceptCode = currLine.substring(283, 284).trim();
                    if ( (TextUtil.isEmptyOrSpaces(acceptCode)) || 
                         ( !(acceptCode.equals("D")) && 
                           !(acceptCode.equals("C")) && 
                           !(acceptCode.equals("L")) ) ) 
                    {
                        invalidAcceptCodeCounter++;
                    }                    
                }
                currLine = fin.readLine();
            }
            logger.debug("    memberCounter:            " + memberCounter);
            logger.debug("    missingAffNumCounter:     " + missingAffNumCounter);
            logger.debug("    missingMbrNumCounter:     " + missingMbrNumCounter);
            logger.debug("    invalidAcceptCodeCounter: " + invalidAcceptCodeCounter);            
            
            // check if all affiliate numbers are missing.
            if (memberCounter == missingAffNumCounter) {
                logger.info("File Validation Error: All Affiliate Numbers are Missing!");
                valid = UpdateErrorCodes.ERROR_FILE_MISSING_AFFILIATE_NUMBER;
            }
            
            // check if all afscme member numbers are missing. 
            if (memberCounter == missingMbrNumCounter) {
                logger.info("File Validation Error: All AFSCME Member Numbers are Missing!");
                valid = UpdateErrorCodes.ERROR_FILE_MISSING_AFSCME_MEMBER_NUMBER;
            }
 
            // check if all acceptance codes are missing or invalid. 
            if (memberCounter == invalidAcceptCodeCounter) {
                logger.info("File Validation Error: All Acceptance Codes are Missing or Invalid!");
                valid = UpdateErrorCodes.ERROR_FILE_INVALID_ACCEPTANCE_CODE;
            }
            
        } catch (IOException e) {
            valid = UpdateErrorCodes.ERROR_FILE_IO;
        } finally {
            try { fin.close(); } catch (Exception e) {}
            logger.debug("---------------------------------------------------------------");
            logger.debug("validate() done. valid value is: " + valid);
            logger.debug("---------------------------------------------------------------");
        }
        return valid;        
    }
    
    /** 
     * Called in the generatePreUpdateSummary() method if the file is determined to be in the fixed 
     * length format.
     *
     * Checks the file using the following criteria:
     *  1. each record has a valid affiliate id (new affiliates cannot be added) - affiliate error
     *
     * Checks for affiliate errors.  Calls preMatchCheckOnAffiliateErrors() method.
     */    
    private void processFixedLengthFile() {
        
        logger.debug("----------------------------------------------------");
        logger.debug("processFixedLengthFile called.");
        
        try {
            String fileName = fileEntry.getFilePath() + fileEntry.getFileName();
            BufferedReader fin = new BufferedReader(new FileReader(fileName));
            
            AffiliateData topAffData = maintainAffiliates.getAffiliateData(fileEntry.getAffPk());
            if (topAffData == null || topAffData.getAffiliateId() == null) {
                throw new RuntimeException("Cannot find the Affiliate that sent this file. affPk = " + fileEntry.getAffPk());
            } // this should never happen...
            
            // start managing change statistics
            RebateChanges currRebateChanges = null;
            
            // read in one rebate per line at a time.         
            String currLine = fin.readLine();
            int counter = 0;
            
            while (currLine != null) {
                logger.debug("Processing rebate # " + (++counter)); 
                currRebate = new RebateUpdateElement();
                
                // get the affiliate pk
                AffiliateIdentifier currAffId = buildCurrentRebateAffiliateId(currLine, topAffData);
                Integer currAffPk = maintainAffiliates.getAffiliatePk(currAffId);
                
                // try to fill in aff code if pk exists
                if (currAffPk != null && currAffPk.intValue() > 0 && 
                    TextUtil.isEmptyOrSpaces(currAffId.getCode())) {
                        currAffId.setCode(maintainAffiliates.getAffiliateData(currAffPk).getAffiliateId().getCode());
                }
                
                // create an affiliate entry in the preUpdateSummary data structure
                if (!preUpdateSummary.getRebateCounts().containsKey(currAffId)) {
                    currRebateChanges = new RebateChanges();
                    if (currAffPk != null && currAffPk.intValue() > 0) {
                        currRebateChanges.setAffPk(currAffPk);
                    }
                    preUpdateSummary.getRebateCounts().put(currAffId, currRebateChanges);
                } else {
                    currRebateChanges = (RebateChanges)preUpdateSummary.getRebateCounts().get(currAffId);
                }
                
                // Rule 1. - check this currAffPk, if the affiliate does not exist in the system, we can not
                // do any matching activities, and it is an affiliate error.
                // And if the local number is missing, we will get multiple returns, thus the second
                // check condition will be true.
                if (currAffPk == null || currAffPk.intValue() < 1) { // error codes are negative
                    currRebateChanges.setHasError(true);
                    currRebateChanges.incrementReceived();
                    
                    // go to the next record in file.
                    currLine = fin.readLine();
                    continue;
                }
                
                // check some other affiliate level errors here
                preMatchCheckOnAffiliateErrors(currAffPk, currRebateChanges);
                
                currRebate.setAffPk(currAffPk);
                currRebate.setAffId(currAffId);
                
                // build the rebate update element object for the line
                buildCurrentRebateUpdateElement(currLine);

                // check each acceptance code for missing or invalid (affiliate error will be checked later)
                String acceptCode = currLine.substring(283, 284).trim();
                if ( (TextUtil.isEmptyOrSpaces(acceptCode)) || 
                     ( !(acceptCode.equals("D")) && 
                       !(acceptCode.equals("C")) && 
                       !(acceptCode.equals("L")) ) ) 
                {                
                    currRebateChanges.incrementInvalidAcceptCodeCountInFile();
                }
                
                // determine the file generated date if person pk exists and no date has been found
                if ((!((currRebate.getAfscmeMemberNumber() == null) || (currRebate.getAfscmeMemberNumber().intValue() == 0))) && 
                    (currRebateChanges.getFileGeneratedDt() == null)) {
                    currRebateChanges.setFileGeneratedDt(updateBean.getRebateFileGeneratedDate(currAffPk, 
                                                        currRebate.getAfscmeMemberNumber()));
                }

                // update the affiliate map
                if (!affiliates.containsKey(currAffPk))
                    affiliates.put(currAffPk, currAffId);
                
                // ************* Start processing the current rebate member record ***********
                preProcessRebate();
                                
                // go to the next rebate in file.
                currLine = fin.readLine();
            }
        } catch (IOException ie) {
            throw new EJBException(ie);
        }       
    }
    
    /**
     * Called in the applyUpdate() method for selected affPks if the file is 
     * determined to be in the fixed length format.
     */
    private void processFixedLengthFileForUpdate(List affPks) {
        
        logger.debug("----------------------------------------------------");
        logger.debug("processFixedLengthFileForUpdate called.");
        
        boolean includeRebateForUpdate = true;
        
        try {
            String fileName = fileEntry.getFilePath() + fileEntry.getFileName();
            BufferedReader fin = new BufferedReader(new FileReader(fileName));
            
            AffiliateData topAffData = maintainAffiliates.getAffiliateData(fileEntry.getAffPk());
            if (topAffData == null || topAffData.getAffiliateId() == null) {
                throw new RuntimeException("Cannot find the Affiliate that sent this file. affPk = " + fileEntry.getAffPk());
            } // this should never happen...
            
            // start managing statistics
            PersonReviewData affReviewData = null;
            
            // read in one rebate per line at a time.         
            String currLine = fin.readLine();
            int counter = 0;
            
            while (currLine != null) {
                logger.debug("Processing rebate # " + (++counter));                 
                currRebate = new RebateUpdateElement();
                
                // get the affiliate pk
                AffiliateIdentifier currAffId = buildCurrentRebateAffiliateId(currLine, topAffData);
                Integer currAffPk = maintainAffiliates.getAffiliatePk(currAffId);
                
                // check this currAffPk, if the affiliate does not exist in the system, we can not
                // do any matching activities, and it is an affiliate error.
                // And if the local number is missing, we will get multiple returns, thus the second
                // check condition will be true.
                if (currAffPk == null || currAffPk.intValue() < 1) { 
                    // go to the next rebate in file.
                    currLine = fin.readLine();
                    continue;
                } else if (!affPks.contains(currAffPk)) { // make sure this is the affiliate that the user wants to do update on
                    currLine = fin.readLine();
                    continue;
                }
                
                // insert the currAffPk into the updateSummary if it is not there yet.
                if (!updateSummary.affUpdateSummary.containsKey(currAffPk)) {
                    affReviewData = new PersonReviewData();
                    updateSummary.affUpdateSummary.put(currAffPk, affReviewData);
                } else {
                    affReviewData = (PersonReviewData)updateSummary.affUpdateSummary.get(currAffPk);
                }
                
                currRebate.setAffPk(currAffPk);
                currRebate.setAffId(currAffId);
                
                // build the rebate update element object for the line                
                buildCurrentRebateUpdateElement(currLine);

                // update the affiliate map
                if (!affiliates.containsKey(currAffPk)) {
                    affiliates.put(currAffPk, currAffId);
                }

                // determine the file generated date if person pk exists and no date has been found
                Timestamp fileGeneratedDt = (Timestamp)updateSummary.affFileGenerated.get(currAffPk);
                if ((!((currRebate.getAfscmeMemberNumber() == null) || (currRebate.getAfscmeMemberNumber().intValue() == 0))) && 
                    (fileGeneratedDt == null)) {
                    
                    updateSummary.affFileGenerated.put(currAffPk, updateBean.getRebateFileGeneratedDate(currAffPk, 
                                                        currRebate.getAfscmeMemberNumber()));
                }

                // ************* Start processing the current rebate member record ***********
                processRebate();
                
                // go to the next rebate in file.
                currLine = fin.readLine();
            }
        } catch (IOException ie) {
            throw new EJBException(ie);
        }       
    }
    
    /** 
     * Called in the processFixedLengthFile() method to build the affiliate id 
     * for the record.
     */
    private AffiliateIdentifier buildCurrentRebateAffiliateId(String line, AffiliateData mainAff) {
        
        Character affType = new Character(line.charAt(0));
        String affLocal = line.substring(1, 5).trim();
        String state = line.substring(5, 7);
        String affSubLocal = line.substring(7, 11).trim();
        Character affCode = null;
        String council = line.substring(11, 15).trim();

        return new AffiliateIdentifier(affType, affLocal, state, affSubLocal, 
                                        council, affCode, null
        );
    }

    /** 
     * Checks for Affiliate Errors.
     *
     * Checks the affiliate using the following criteria:
     *  1. check for id not in hierarchy error
     */    
    private void preMatchCheckOnAffiliateErrors(Integer affPk, RebateChanges rebateChanges) {

        if (rebateChanges.hasError)
            return;
        
        // Retrieve all sub affiliates of the reporting affiliate.
        Collection allAffiliates = maintainAffiliates.getAffiliateSubHierarchy(fileEntry.getAffPk());
        
        // The first aff in the list is the top affiliate, we should not count that.
        Set subAffiliates = new HashSet(allAffiliates.size()-1);
        for (Iterator it = allAffiliates.iterator(); it.hasNext(); ) {
            AffiliateHierarchyEntry affData = (AffiliateHierarchyEntry)it.next();
            subAffiliates.add(affData.getAffPk());
        } 
        
        // Rule 1. - check for id not in hierarchy error
        if (!subAffiliates.contains(affPk)) {
            rebateChanges.hasError = true;
            return;
        }
    }

    /** 
     * Performs the post matching Affiliate errors checks during the   
     * Pre-Update Summary process.
     *
     * Checks the file using the following criteria for affiliate errors:
     *  1. check for each affiliate in file, if AFSCME Member Number is missing in all its members.
     *  2. check for each affiliate in file, if acceptance code is invalid or missing, it is an error.
     *  3. check for each affiliate in file, if persons are removed from original file sent out, it is an error.
     *
     * @param preUpdateSummary  The pre-update summary object to be filled.
     */
    private void postMatchCheckOnAffiliateErrorWarning(RebatePreUpdateSummary updateSummary) {
        
        logger.debug("----------------------------------------------------");
        logger.debug("postMatchCheckOnAffiliateErrorWarning called.");
       
        // perform validation per affiliate 
        RebateChanges rChanges = null;
        for (Iterator it3 = updateSummary.getRebateCounts().values().iterator(); it3.hasNext(); ) {
            rChanges = (RebateChanges)it3.next();
            if (!rChanges.getHasError()) {          // no error so far, so we need to check error

                // Error Rule 1. - For each affiliate in file, if AFSCME Member Number is missing 
                //          in all its members, it is an Affiliate Error.
                // Error Rule 2. - For each affiliate in file, if all acceptance codes are missing 
                //          or invalid, it is an error.
                if ( rChanges.getAfscmeMemberNumCountInFile() == 0 ||
                     rChanges.getInvalidAcceptCodeCountInFile() == rChanges.getReceived() ) 
                {
                        rChanges.setHasError(true);
                        logger.debug("Error Rule 1 or Rule 2 found for affiliate=" + rChanges.getAffPk());
                }
            }

            if (!rChanges.getHasError()) {          // no error so far, so we need to check error
            
                // retrieve all the persons for that affiliate and file date
                List rebatePersons = updateBean.getRebatePersons(rChanges.getAffPk(), 
                                                                 rChanges.getFileGeneratedDt());
                Map removedPersons = new HashMap(); // key -- affPK, value -- personPk
        
                Iterator it = rebatePersons.iterator();
                while (it.hasNext()) {
                    Integer personPk = (Integer)it.next();
                    
                    // check if all persons exist in the matchedPersonPks set
                    if (!rChanges.matchedPersonPks.contains(personPk))
                        removedPersons.put(rChanges.getAffPk(), personPk);
                }
        
                // Error Rule 3. - if persons are removed from original file sent out 
                if (removedPersons.size() > 0) {
                    rChanges.setHasError(true);
                    logger.debug("Error Rule 3 found for affiliate=" + rChanges.getAffPk());
                }
            }
        }    
    }
    
    /**
     * Processes an individual rebate update element for the RebatePreUpdateSummary.
     *
     * currRebate - current Rebate Member record (rebateUpdateElement)
     * preUpdateSummary - preUpdateSummary object with counts
     */
    private void preProcessRebate() {
        updateBean.matchRebate(currRebate, preUpdateSummary);
    }

    /**
     * Processes an individual rebate update element.
     * 
     * currRebate - current Rebate Member record (rebateUpdateElement)
     * updateSummary - updateSummary object with counts
     */
    private void processRebate() {
        
        logger.debug("----------------------------------------------------");
        logger.debug("processRebate called.");
        
        updateBean.matchRebate(currRebate, updateSummary);
        
        logger.debug("Rebate:  " + currRebate);
    }
    
    /** 
     * Builds a RebateUpdateElement from the current line of data from the file   
     * during both the Pre-Update Summary and the Update process.
     *
     * @param currLine  The current line of data from the file.
     */
    private void buildCurrentRebateUpdateElement(String currLine) {
        
        Map codes;
        Integer codePk;

        // set AFSCME member number (or person_pk)
        String mbrNumber = currLine.substring(15, 23).trim();
        if (!TextUtil.isEmptyOrSpaces(mbrNumber)) {
            currRebate.setAfscmeMemberNumber(new Integer(mbrNumber));
        }

        // set SSN fields
        currRebate.setSsn(currLine.substring(23, 32).trim());
        
        char dupSSNFlag = currLine.charAt(32);
        if (dupSSNFlag != '1') {
            currRebate.setSsnDuplicate(false);
        } else {
            currRebate.setSsnDuplicate(true);
        }        

        // set Name fields
        currRebate.setFirstName(currLine.substring(33, 58).trim());
        currRebate.setMiddleName(currLine.substring(58, 83).trim());
        currRebate.setLastName(currLine.substring(83, 108).trim());

        // set Address fields
        AddressElement addr = new AddressElement();
        addr.setAddr1(currLine.substring(108, 158).trim());
        addr.setAddr2(currLine.substring(158, 208).trim());
        addr.setCity(currLine.substring(208, 233).trim());
        addr.setProvince(currLine.substring(233, 258).trim());
        addr.setState(currLine.substring(258, 260).trim());
        addr.setZipCode(currLine.substring(260, 272).trim());
        addr.setZipPlus(currLine.substring(272, 276).trim());
        addr.setCountry(currLine.substring(276, 278).trim());        

        currRebate.setPrimaryAddr(addr);

        // get the rebate member type pk
        String mbrType = currLine.substring(278, 279).trim();
        if (!TextUtil.isEmptyOrSpaces(mbrType)) {
            codes = codeBean.getCodesByCode(REBATE_MEMBER_TYPE);
            codePk = ((CodeData)codes.get(mbrType)).getPk();
            currRebate.setMemberTypePk(codePk);
        }

        // get the rebate member status pk
        String mbrStatus = currLine.substring(279, 280).trim();
        if (!TextUtil.isEmptyOrSpaces(mbrStatus))  {
            codes = codeBean.getCodesByCode(REBATE_MEMBER_STATUS);
            codePk = ((CodeData)codes.get(mbrStatus)).getPk();
            currRebate.setMemberStatusPk(codePk);
        }    

        // get the dues type pk
        String duesType = currLine.substring(280, 281).trim();
        if (!TextUtil.isEmptyOrSpaces(duesType)) {
            codes = codeBean.getCodesByCode(DUES_TYPE);
            codePk = ((CodeData)codes.get(duesType)).getPk();
            currRebate.setDuesTypePk(codePk);
        }    
        
        // get the rebate duration pk pk
        String numMonths = TextUtil.trimLeading(currLine.substring(281, 283).trim(), '0');
        if (!TextUtil.isEmptyOrSpaces(numMonths))  {
            codes = codeBean.getCodesByCode(REBATE_DURATION);
            codePk = ((CodeData)codes.get(numMonths)).getPk();
            currRebate.setNumMonthsPk(codePk);
        }    
        
        // get the acceptance code pk
        String accCode = currLine.substring(283, 284).trim();
        if (!TextUtil.isEmptyOrSpaces(accCode))  {
            codes = codeBean.getCodesByCode(REBATE_ACCEPTANCE_CODE);
            codePk = ((CodeData)codes.get(accCode)).getPk();
            currRebate.setAcceptanceCodePk(codePk);
        }    
    }
    
   
/*    private void processXMLFile() {
    }
    
    // *****************************************************
    //  The following are SAXParser event handling methods
    // *****************************************************
    
    public void startDocument() {
    }    
    
    public void startElement(String nsUri, String localName, String qName, Attributes attrs) {    
    }
    
    public void endElement(String nsUri, String localName, String qName) {
    }
    
    public void endDocument()  {
    }
*/    
}
