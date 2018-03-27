package org.afscme.enterprise.update.member;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import javax.ejb.EJBException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.AffiliateErrorCodes;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateHierarchyEntry;
import org.afscme.enterprise.codes.ejb.*;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.Codes;
import org.afscme.enterprise.update.ejb.*;
import org.afscme.enterprise.update.FileProcessor;
import org.afscme.enterprise.update.filequeue.FileEntry;
import org.afscme.enterprise.update.PreUpdateSummary;
import org.afscme.enterprise.update.AddressElement;
import org.afscme.enterprise.update.UpdateErrorCodes;
import org.afscme.enterprise.update.UpdateTabulation;
import org.afscme.enterprise.update.PersonReviewData;
import org.afscme.enterprise.update.member.MemberUpdateTabulation;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.update.Codes.UpdateFileType;
import org.afscme.enterprise.update.Codes.UpdateType;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;

/**
 * Handles validation and/or apply update for a member update file.
 */
public class MemberFileProcessor extends FileProcessor {
    
    private static final String PREFIX_TYPE = "Prefix";
    private static final String SUFFIX_TYPE = "Suffix";
    private static final String STATE_CODE_TYPE = "State";
    private static final String MEMBER_STATUS_TYPE = "MemberStatus";
    private static final String GENDER_TYPE = "Gender";
    private static final String POLITICAL_PARTY_TYPE = "PoliticalParty";
    private static final String REGISTERED_VOTER_TYPE = "RegisteredVoter";
    private static final String INFORMATION_SOURCE_TYPE = "InformationSource";    

    private static Logger logger = Logger.getLogger(MemberFileProcessor.class);
    
    private FileQueue fileQueue;    // reference to the file queue bean
    private MaintainAffiliates maintainAffiliates;  // reference to the MaintainAffiliatesBean
    private MaintainCodes codeBean;  // reference to the MaintainCodesBean
    private Update updateBean;      // reference to the update bean
    
    /**
     * Map of MemberUpdateTabulation objects, by primary key.
     */
    //protected Map tabs;
    
    //private Integer affPk;  // the (top) affiliate that submitted the file for udpate
    
    private MemberUpdateElement currMember = null;  // the current member being processed
    private MemberPreUpdateSummary preUpdateSummary = null; 
    private MemberUpdateSummary updateSummary = null;

    // map of affiliates in the update file
    // key -- affPk, value -- affId
    private Map affiliates;
    
    // keeps track of what needs to be updated.
    // key -- affID, value -- UpdateTabulation
    private Map updatableAffiliates;
    

    public MemberFileProcessor(FileQueue queueBean, MaintainAffiliates maintainAffBean, MaintainCodes maintainCodes) {
        fileQueue = queueBean;
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
        updatableAffiliates = new HashMap();
    }
    
    /** 
     * Applies the Update file by processing the file, determining the  
     * inactivated members, and performing the update.
     *
     * @param affPks   The list of affPks to process (checked on the screen).
     * @param queuePk   The queue primary key to determine the file.
     * @param userPk   User Pk of the user changing the data
     */
    public void applyUpdate(List affPks, Integer queuePk, Integer userPk) {
        
        logger.debug("----------------------------------------------------");
        logger.debug("applyUpdate called.");
        
        updateSummary = new MemberUpdateSummary();
        MemberUpdateTabulation tab = new MemberUpdateTabulation();
        
        // process the file to determine exceptions and members to process
        processFixedLengthFileForUpdate(tab, affPks);
        
        // inactive the members that were not in the file for the affiliate
        updateBean.processInactiveMembers(affPks, updateSummary, tab);
        
        // perform the update for all data changed or added 
        updateBean.doMemberUpdate(userPk, fileEntry.getQueuePk(), updateSummary, tab);
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

        // NOTE, the member update file is a positional text file.
        preUpdateSummary = new MemberPreUpdateSummary();
        preUpdateSummary.prepopulateFieldChanges();
        
        // process the file to determine exceptions and members to process
        processFixedLengthFile();
        
        /* ------------------------------------------------------------------------
           now that each member in the file is processed, we have a list of affiliates
           these members belong to. We now start doing statistics of those members who
           are not in the update file, but are in the system.
           ------------------------------------------------------------------------ */
        updateBean.calculateMemberInSystemCounts(affiliates.keySet(), preUpdateSummary);
        
        // check all affiliate level errors/warnings.
        postMatchCheckOnAffiliateErrorWarning(preUpdateSummary);
          
        return preUpdateSummary;
    }
    
    /** 
     * Validates that the file using the following criteria:
     *  1. the file can be opened
     *  2. the file is greater than a configurable size
     *  3. each line contains 198-200 characters
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
            int zeroZipCounter = 0;
            int missingAffMbrNumCounter = 0;
            String affLocalNumber = null;
            String zip = null;
            String affMbrNum = null;
            
            // read in one member per line at a time.
            String currLine = fin.readLine();
            int counter = 0;
            while (currLine != null && valid == 0) {
                logger.debug("Line: " + (++counter));
                if (TextUtil.isEmptyOrSpaces(currLine) || 
                    currLine.length() < 198 || 
                    currLine.length() > 200     // check that each line is the correct length
                ) {
                    logger.info("File Validation Error: File Length incorrect - length = " + currLine.length());
                    valid = UpdateErrorCodes.ERROR_FILE_LENGTH_INVALID_MEMBER;
                    return valid;
                } else {
                    // count a member
                    memberCounter++;
                    
                    // count a missing affil number
                    affLocalNumber = currLine.substring(0, 4).trim();
                    if (TextUtil.isEmptyOrSpaces(affLocalNumber) || 
                        !TextUtil.isInt(affLocalNumber)
                    ) {
                        missingAffNumCounter++;
                    }
                    
                    // count a missing affiliate member identifier
                    affMbrNum = currLine.substring(184, 198).trim();
                    if (TextUtil.isEmptyOrSpaces(affMbrNum)) {
                        missingAffMbrNumCounter++;
                    }
                    
                    // count a zero value zip code (or spaces)
                    zip = currLine.substring(132, 137).trim();
                    if (TextUtil.isEmptyOrSpaces(zip) ||
                        TextUtil.isAllZeros(zip)
                    ) {
                        zeroZipCounter++;
                    }
                }
                currLine = fin.readLine();
            }
            logger.debug("    memberCounter:            " + memberCounter);
            logger.debug("    missingAffNumCounter:     " + missingAffNumCounter);
            logger.debug("    zeroZipCounter:           " + zeroZipCounter);
            logger.debug("    missingAffMbrNumCounter:  " + missingAffMbrNumCounter);
            
            // check if all affiliate numbers are missing.
            if (memberCounter == missingAffNumCounter) {
                logger.info("File Validation Error: All Affiliate Numbers are Missing!");
                valid = UpdateErrorCodes.ERROR_FILE_MISSING_AFFILIATE_NUMBER;
            }
            
            // check if all affiliate member numbers are missing. 
            if (memberCounter == missingAffMbrNumCounter) {
                logger.info("File Validation Error: All Affiliate Member Numbers are Missing!");
                valid = UpdateErrorCodes.ERROR_FILE_MISSING_AFFILIATE_MEMBER_NUMBER;
            }
            
            // check if all zip codes are zero
            if (memberCounter == zeroZipCounter) {
                logger.info("File Validation Error: All Zip Codes are Zero or Blank!");
                valid = UpdateErrorCodes.ERROR_FILE_ZIP_ZERO_OR_BLANK;
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
     *  2. check if Affiliate reports Sub-Locals when they are not authorized to have them - affiliate error
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
            MemberChanges currMemberChanges = null;
            
            // read in one member per line at a time.         
            Map codes;
            Integer codePk;
            String currLine = fin.readLine();
            int counter = 0;
            
            while (currLine != null) {
                logger.debug("Processing member # " + (++counter)); 
                currMember = new MemberUpdateElement();
                
                // get the affiliate pk
                AffiliateIdentifier currAffId = buildCurrentMemberAffiliateId(currLine, topAffData);
                Integer currAffPk = maintainAffiliates.getAffiliatePk(currAffId);
                
                // check if need to determine states for matches
                if ((currAffPk != null && currAffPk.intValue() < 1 &&
                     currAffPk.intValue() == AffiliateErrorCodes.ERROR_GET_PK_MORE_THAN_ONE_RESULT &&
                     TextUtil.isEmptyOrSpaces(currAffId.getState())) ||
                    (currAffPk != null && currAffPk.intValue() > 0 && 
                     TextUtil.isEmptyOrSpaces(currAffId.getState()))) {
                        
                        // if multiple, then select the first state and assign to it
                        String state = updateBean.determineStateForSimilarLocals(currAffId);
                        if (!(TextUtil.isEmptyOrSpaces(state))) {
                            currAffId.setState(state);
                            currAffPk = maintainAffiliates.getAffiliatePk(currAffId);
                        }
                }

                // try to fill in aff code if pk exists
                if (currAffPk != null && currAffPk.intValue() > 0 && 
                    TextUtil.isEmptyOrSpaces(currAffId.getCode())) {
                        currAffId.setCode(maintainAffiliates.getAffiliateData(currAffPk).getAffiliateId().getCode());
                }
                
                // create an affiliate entry in the preUpdateSummary data structure
                if (!preUpdateSummary.getMemberCounts().containsKey(currAffId)) {
                    currMemberChanges = new MemberChanges();
                    if (currAffPk != null && currAffPk.intValue() > 0) {
                        currMemberChanges.setAffPk(currAffPk);
                    }
                    preUpdateSummary.getMemberCounts().put(currAffId, currMemberChanges);
                } else {
                    currMemberChanges = (MemberChanges)preUpdateSummary.getMemberCounts().get(currAffId);
                }
                
                // Rule 1. - check this currAffPk, if the affiliate does not exist in the system, we can not
                // do any matching activities, and it is an affiliate error.
                // And if the local number is missing, we will get multiple returns, thus the second
                // check condition will be true.
                if (currAffPk == null || currAffPk.intValue() < 1) { // error codes are negative
                    currMemberChanges.setHasError(true);
                    currMemberChanges.incrementInFile();
                    
                    // go to the next member in file.
                    currLine = fin.readLine();
                    continue;
                }
                
                // Rule 2. - check for Affiliate reports Sub-Locals when they are not authorized to have them
                if (!TextUtil.isEmptyOrSpaces(currAffId.getType()) && 
                    (currAffId.getType().charValue() == 'U')) {

                     if (!(updateBean.checkSubLocalsAllowed((maintainAffiliates.getParentAffiliate(currAffPk)).getAffPk()))) {
                        currMemberChanges.setHasError(true);
                        currMemberChanges.incrementInFile();

                        // go to the next member in file.
                        currLine = fin.readLine();
                        continue;
                    }    
                }

                // check some other affiliate level errors here
                preMatchCheckOnAffiliateErrors(currAffPk, currMemberChanges);
                
                currMember.setAffPk(currAffPk);
                currMember.setAffId(currAffId);
                
                // build the member update element object for the line
                buildCurrentMemberUpdateElement(currLine);

                // check each zip for zero or invalid (affiliate error will be checked later)
                String zip = currMember.getPrimaryAddr().getZipCode();
                if (zip.length() != 5 || TextUtil.isAllZeros(zip) || TextUtil.isEmptyOrSpaces(zip)) {
                    currMemberChanges.incrementZeroZipCount();
                }

                // update the affiliate map
                if (!affiliates.containsKey(currAffPk))
                    affiliates.put(currAffPk, currAffId);
                
                // ************* Start processing the current member ***********
                preProcessMember();
                
                // go to the next member in file.
                currLine = fin.readLine();
            }
        } catch (IOException ie) {
            throw new EJBException(ie);
        }       
    }
    
    /**
     * Called in the applyUpdate() method if the file is determined to be in the fixed 
     * length format.
     *
     */
    private void processFixedLengthFileForUpdate(MemberUpdateTabulation tab, List affPks) {
        logger.debug("----------------------------------------------------");
        logger.debug("processFixedLengthFileForUpdate called.");
        boolean includeMemberForUpdate = true;
        
        try {
            String fileName = fileEntry.getFilePath() + fileEntry.getFileName();
            BufferedReader fin = new BufferedReader(new FileReader(fileName));
            
            AffiliateData topAffData = maintainAffiliates.getAffiliateData(fileEntry.getAffPk());
            if (topAffData == null || topAffData.getAffiliateId() == null) {
                throw new RuntimeException("Cannot find the Affiliate that sent this file. affPk = " + fileEntry.getAffPk());
            } // this should never happen...
            
            // start managing statistics
            PersonReviewData affReviewData = null;
            
            // read in one member per line at a time.         
            Map codes;
            Integer codePk;
            String currLine = fin.readLine();
            int counter = 0;
            
            while (currLine != null) {
                logger.debug("Processing member # " + (++counter));                 
                currMember = new MemberUpdateElement();
                
                // get the affiliate pk
                AffiliateIdentifier currAffId = buildCurrentMemberAffiliateId(currLine, topAffData);
                Integer currAffPk = maintainAffiliates.getAffiliatePk(currAffId);
                
                // check this currAffPk, if the affiliate does not exist in the system, we can not
                // do any matching activities, and it is an affiliate error.
                // And if the local number is missing, we will get multiple returns, thus the second
                // check condition will be true.
                if (currAffPk == null || currAffPk.intValue() < 1) { 
                    // go to the next member in file.
                    currLine = fin.readLine();
                    continue;
                } else if (!affPks.contains(currAffPk)) { // make sure this is the affiliate that the user do want to do update on
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
                
                currMember.setAffPk(currAffPk);
                currMember.setAffId(currAffId);
                
                // build the member update element object for the line                
                buildCurrentMemberUpdateElement(currLine);

                // update the affiliate map
                if (!affiliates.containsKey(currAffPk)) {
                    affiliates.put(currAffPk, currAffId);
                }
                
                // ************* Start processing the current member ***********
                processMember(tab);
                
                // go to the next member in file.
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
    private AffiliateIdentifier buildCurrentMemberAffiliateId(String line, AffiliateData mainAff) {
        String affLocal = line.substring(0, 4).trim();
        Character affType = new Character(line.charAt(17));
        String affSubLocal = line.substring(23, 26).trim();
        
        if (TextUtil.isEmptyOrSpaces(affSubLocal) || TextUtil.isAllZeros(affSubLocal)) {
            // if null, set to default value, so matches what is in the DB...
            affSubLocal = AffiliateIdentifier.DEFAULT_ID_NUMBER;
        } else {
            /* this is a Sub Local, so make sure affType is U in this case. it 
             * may have been L if affiliates reporting the old way...*/
            affType = new Character('U');
        }
        Character affCode = null;
        if (TextUtil.equals(affType, mainAff.getAffiliateId().getType())) { 
            // initialize the code for Affiliate's reporting for themselves.
            affCode = mainAff.getAffiliateId().getCode();
        }
        String council = mainAff.getAffiliateId().getCouncil();
        String state = null;
        if (TextUtil.isEmptyOrSpaces(council)) {
            // this is an unaffiliated local, we'll need the state to find the parent...
            state = mainAff.getAffiliateId().getState();
        } 
        /* else if local or sub local is affiliated with a council, state 
         * should remain null for search purposes, so that locals that are 
         * affiliated with councils across a region can be found. Assumption is 
         * that Council numbers are unique.
         */
        return new AffiliateIdentifier(affType, affLocal, state, affSubLocal, 
                                        council, affCode, null
        );
    }
    
    /**
     * Processes an individual member update element for the MemberPreUpdateSummary.
     *
     * currMember - current Member record (memberUpdateElement)
     * preUpdateSummary - preUpdateSummary object with counts
     */
    private void preProcessMember() {
        updateBean.matchMember(currMember, preUpdateSummary);
    }
    
    /**
     * processes an individual member update element
     * 
     * @param member The member to update - Set of affiliate primary key Integers
     */
    private void processMember(MemberUpdateTabulation tab) {    
        logger.debug("----------------------------------------------------");
        logger.debug("processMember called.");
        
        updateBean.matchMember(currMember, updateSummary, tab);
        
        logger.debug("Member:      " + currMember);
//        logger.debug("Summary:     " + updateSummary);
        logger.debug("Tabulation:  " + tab);
    }
    
    /** 
     * Checks for Affiliate Errors.
     *
     * Checks the affiliate using the following criteria:
     *  1. check for id not in hierarchy error
     *  2. check for affiliate merge status
     *  3. check for deactivated affiliate or a decertified sub local
     *  4. check for split sub local
     */    
    private void preMatchCheckOnAffiliateErrors(Integer affPk, MemberChanges memberChanges) {

        if (memberChanges.hasError)
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
            memberChanges.hasError = true;
            return;
        }
        
        Integer affStatus = updateBean.getAffiliateStatusCode(affPk);
        if (affStatus != null) {
            if (affStatus.equals(Codes.AffiliateStatus.M) ||    // Rule 2. - check for affiliate merge status
                affStatus.equals(Codes.AffiliateStatus.D) ||    // Rule 3. - check for deactivated affiliate or...
                affStatus.equals(Codes.AffiliateStatus.DU) ||   // ...a decertified sub local 
                affStatus.equals(Codes.AffiliateStatus.S)       // Rule 4. - check for split sub local
            ) {
                memberChanges.hasError = true;
                return;
            }
        } // else will never happen...
    }
    
    /** 
     * Performs the matching Affiliate errors and warnings checks during the   
     * Pre-Update Summary process.
     *
     * Checks the file using the following criteria for affiliate errors:
     *  1. if the update is "FULL", check for missing affiliates 
     *  2. check for each affiliate in file, if Affiliate Member Number is missing in all its members.
     *  3. check for each affiliate in file, if all zip code is zero, it is an error.
     *
     * Checks the file using the following criteria for affiliate warnings:
     *  1. check for each affiliate in file, if unit with new Members (all members are new ones 
     *      added into the system for a new affiliate that is an affiliate with no members).
     *  2. check for each affiliate in file, if all members are new ones added into the system 
     *     (that is, if new member ids are assigned each time), it is a warning.
     *  3. check for each affiliate in file, if 50% of a units officers have been changed to "T" records.
     *
     * @param preUpdateSummary  The pre-update summary object to be filled.
     */
    private void postMatchCheckOnAffiliateErrorWarning(MemberPreUpdateSummary updateSummary) {
        
        logger.debug("----------------------------------------------------");
        logger.debug("postMatchCheckOnAffiliateErrorWarning called.");
        
        // First, get all sub-affiliates of this reporting affiliate
        Integer topAffPk = fileEntry.getAffPk();
        Collection allAffiliates = maintainAffiliates.getAffiliateSubHierarchy(topAffPk);
        
        // The first aff in the list is the top affiliate, we should not count that.
        Set subAffiliates = new HashSet(allAffiliates.size()-1);
        
        Map missingAffiliates = new HashMap(); // key -- affPK, value -- affID
        
        Iterator it = allAffiliates.iterator();
        Integer affPk = null;
        AffiliateIdentifier affId = null;
        if (it.hasNext()) {  // skip the first one
            it.next();
        }
        
        while (it.hasNext()) {
            AffiliateHierarchyEntry affData = (AffiliateHierarchyEntry)it.next();
            affId = affData.getAffiliateId();
            affPk = affData.getAffPk();
            subAffiliates.add(affPk);
            if (!affiliates.containsKey(affPk))
                missingAffiliates.put(affPk, affId);
        }
        
        // Error Rule 1. - if the update is "FULL", check for missing affiliates
        if (fileEntry.getUpdateType() == UpdateType.FULL && missingAffiliates.size() > 0) {
            // reports these affiliates as errors
            MemberChanges memberChanges = null;
            Map.Entry entry;
            for (Iterator it2 = missingAffiliates.entrySet().iterator(); it2.hasNext(); ) {
                entry = (Map.Entry)it2.next();
                
                memberChanges = new MemberChanges();
                memberChanges.setAffPk((Integer)entry.getKey());
                memberChanges.setHasError(true);
                memberChanges.setInSystem(updateBean.getMemberCount(memberChanges.getAffPk()));
                
                updateSummary.getMemberCounts().put((AffiliateIdentifier)entry.getValue(), memberChanges);
            }
        }

        // perform validation per affiliate 
        MemberChanges mChanges = null;
        for (Iterator it3 = updateSummary.getMemberCounts().values().iterator(); it3.hasNext(); ) {
            mChanges = (MemberChanges)it3.next();
            if (!mChanges.isHasError()) {          // no error so far, so we need to check error

                // Error Rule 2. - For each affiliate in file, if Affiliate Member Number is missing 
                //          in all its members, it is an Affiliate Error.
                // Error Rule 3. - For each affiliate in file, if all zip code is zero, it is an error.
                if ( mChanges.getAffMemberNumCountInFile() == 0 ||
                     mChanges.getZeroZipCount() == mChanges.getInFile() ) 
                {
                        mChanges.setHasError(true);
                } 
                else       // no error so far, so we need to check for warnings
                {
                    // Warning Rule 1. - For each affiliate in file, if unit with new Members (all members are new ones 
                    //                   added into the system for a new affiliate that is an affiliate with no members).
                    // Warning Rule 2. - For each affiliate in file, if all members are new ones added 
                    //                   into the system (new member ids), it is a warning.
                    // Warning Rule 3. - For each affiliate in file, if 50% of a units officers have 
                    //                   been changed to "T" records.
                    if ( ((mChanges.getAdded() == mChanges.getInFile()) &&
                          (mChanges.getInSystem() == 0)) ||
                         ((mChanges.getAdded() == mChanges.getInFile()) &&
                          (mChanges.getAffMemberNumCountInFile() > 0) && 
                          (mChanges.getMatchedAffMemberNumCount() == 0)) ||                      
                         (mChanges.getOfficerCount()/2) <= mChanges.getNewTRecords() ) 
                    {
                            mChanges.setHasWarning(true);
                    }
                }                    
            }
        }
    }
    
    /** 
     * Builds a MemberUpdateElement from the current line of data from the file   
     * during both the Pre-Update Summary and the Update process.
     *
     * @param currLine  The current line of data from the file.
     */
    private void buildCurrentMemberUpdateElement(String currLine) {
        Map codes;
        Integer codePk;

        currMember.setLastName(currLine.substring(26, 41).trim());
        currMember.setFirstName(currLine.substring(41, 56).trim());
        currMember.setMiddleName(currLine.substring(56, 57).trim());

        // get code pk for prefix and suffix
        String prefix = currLine.substring(57, 58).trim();
        if (!prefix.equals("U")) {
            codes = codeBean.getCodesByCode(PREFIX_TYPE);
            codePk = ((CodeData)codes.get(prefix)).getPk();
            currMember.setPrefixCodePk(codePk);
        }

        String suffix = currLine.substring(58, 59).trim();
        if (!suffix.equals("U")) {
            codes = codeBean.getCodesByCode(SUFFIX_TYPE);
            codePk = ((CodeData)codes.get(suffix)).getPk();
            currMember.setSuffixCodePk(codePk);
        }

        // get the state code pk
        String zip = currLine.substring(132, 137).trim();
        
        AddressElement addr = new AddressElement();
        addr.setAddr1(currLine.substring(59, 87).trim());
        addr.setAddr2(currLine.substring(87, 115).trim());
        addr.setCity(currLine.substring(115, 130).trim());
        addr.setState(currLine.substring(130, 132).trim());
        addr.setZipCode(zip);
        addr.setZipPlus(currLine.substring(137, 141).trim());
        
        currMember.setPrimaryAddr(addr);

        char mailableFlag = currLine.charAt(141);
        if ((zip == null) || (zip.length() == 0) || mailableFlag != 'Y') {
            currMember.setMailableAddr(false);
        } else {
            currMember.setMailableAddr(true);
        }

        currMember.setNoMailFlag(Integer.parseInt(currLine.substring(142, 143).trim()));

        currMember.setAreaCode(currLine.substring(143, 146).trim());
        currMember.setPhoneNumber(currLine.substring(146, 153).trim());

        // get the status
        currMember.setStatus(currLine.substring(153, 154).trim());

        // get the gender code pk
        String gender = currLine.substring(154, 155).trim();
        if (!gender.equals("U")) {
            codes = codeBean.getCodesByCode(GENDER_TYPE);
            codePk = ((CodeData)codes.get(gender)).getPk();
            currMember.setGenderCodePk(codePk);
        }

        int mm = Integer.parseInt(currLine.substring(155, 157).trim());
        int yy = Integer.parseInt(currLine.substring(157, 159).trim());
        currMember.setDateJoined(DateUtil.getTimestamp(mm, yy, false));

        String regVoter = currLine.substring(164, 165).trim();
        if (!regVoter.equals("U")) {
            codes = codeBean.getCodesByCode(REGISTERED_VOTER_TYPE);
            codePk = ((CodeData)codes.get(regVoter)).getPk();
            currMember.setRegisteredVoterCodePk(codePk);
        }

        String polParty = currLine.substring(165, 166).trim();
        if (!polParty.equals("U")) {
            codes = codeBean.getCodesByCode(POLITICAL_PARTY_TYPE);
            codePk = ((CodeData)codes.get(polParty)).getPk();
            currMember.setPoliticalPartyCodePk(codePk);
        }

        currMember.setSsn(currLine.substring(167, 176).trim());

        // get the information source code pk
        String infosource = currLine.substring(183, 184).trim();
        if ((infosource != null) && (TextUtil.isInt(infosource)) && (!(infosource.equals("0")))) {        
            codes = codeBean.getCodesByCode(INFORMATION_SOURCE_TYPE);
            codePk = ((CodeData)codes.get(infosource)).getPk();
            currMember.setInfoSourceCodePk(codePk);
        }    
        
        currMember.setAffMbrNumber(currLine.substring(184, 198).trim());
    }
    
    // *****************************************************
    //  The following are SAXParser event handling methods
    // *****************************************************
    
    /*
    public void startDocument() { 
    }    
    
    public void startElement(String nsUri, String localName, String qName, Attributes attrs) { 
        // check if the element is the top affiliate or one of its members
        if (qName.equals(QNAME_AFF_ID)) {
            if (topAffIdFound == false) {
                topAffIdFound = true;    // this is the top Affiliate ID
                AffiliateIdentifier topAffId = new AffiliateIdentifier(new Character(attrs.getValue("Type").charAt(0)),
                                                                       attrs.getValue("LocalSubChapter"),
                                                                       attrs.getValue("StateNational"),
                                                                       attrs.getValue("SubUnit"),
                                                                       attrs.getValue("CouncilChapter"),
                                                                       null);
                affPk = maintainAffiliates.getAffiliatePk(topAffId);
            }
            else {                      // TBD -- this is the affiliate ID inside the current member
                AffiliateIdentifier memAffId = new AffiliateIdentifier(new Character(attrs.getValue("Type").charAt(0)),
                                                                       attrs.getValue("LocalSubChapter"),
                                                                       attrs.getValue("StateNational"),
                                                                       attrs.getValue("CouncilChapter"),
                                                                       null);
                currMember.affId = memAffId;
                currMember.affPk = maintainAffiliates.getAffiliatePk(memAffId);                
            }
        }
        else if (qName.equals(QNAME_MEMBER)) {      // found a member element
            Map codes;
            
            // we can only set the attributes here. The sub-elements will come as later events.
            currMember = new MemberUpdateElement();
            
            currMember.affiliateMemberNumber = attrs.getValue("AffiliateMbrNum");
            currMember.AFSCMEMemberNumber = new Integer(attrs.getValue("AFSCMEMbrNum"));
            currMember.mailableAddr = Boolean.getBoolean(attrs.getValue("MailableAddressFlag"));
            currMember.noMailFlag = Integer.parseInt(attrs.getValue("NoMailFlag"));
            
            // retrieve the code key for the current code value of "status".
            String memStatusCode = attrs.getValue("Status");
            codes = codeBean.getCodesByCode("MemberStatus"); 
            currMember.status = ((CodeData)codes.get(memStatusCode)).getPk();
            
            // retrieve the code key for the current code value of "type".
            String memTypeCode = attrs.getValue("Type");
            codes = codeBean.getCodesByCode("MemberType");
            currMember.type = ((CodeData)codes.get(memTypeCode)).getPk();
            
            currMember.ssn = attrs.getValue("SSN");
            
            // retrieve the code key for the current code value of "gender".
            // we need to check if the value is null because the attribute is optional
            String memGenderCode = attrs.getValue("Gender");
            if (memGenderCode != null) {
                codes = codeBean.getCodesByCode("Gender"); 
                currMember.gender = ((CodeData)codes.get(memGenderCode)).getPk();
            }
            
            String memDateJoined = attrs.getValue("DateJoined");
            if (memDateJoined != null)
                currMember.dateJoined = new Timestamp(Long.parseLong(memDateJoined));
            
            // retrieve the code key for the current code value of "RegisteredVoter"
            String memRegisteredVoter = attrs.getValue("RegisteredVoter");
            if (memRegisteredVoter != null) {
                codes = codeBean.getCodesByCode("RegisteredVotor");
                currMember.registeredVoter = ((CodeData)codes.get(memRegisteredVoter)).getPk();
            }
            
            // retrieve the code key for the current code value of "PoliticalParty"
            String memPoliticalParty = attrs.getValue("PoliticalParty");
            if (memPoliticalParty != null) {
                codes = codeBean.getCodesByCode("PoliticalParty"); 
                currMember.politicalParty = ((CodeData)codes.get(memPoliticalParty)).getPk();
            }
        }            
    }
    
    public void endElement(String nsUri, String localName, String qName) {
        if (qName.equals(QNAME_MEMBER)) {
            // now we have collected everyting about a member (including its sub-elements
            // we can start the real data matching business process here
            processMember();
        }
            
    }    
    
    public void endDocument() {
    }
     */
    
}
