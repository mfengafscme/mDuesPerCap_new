package org.afscme.enterprise.update.participation;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import org.xml.sax.Attributes;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;
import org.afscme.enterprise.update.FileProcessor;
import org.afscme.enterprise.update.PreUpdateSummary;
import org.afscme.enterprise.update.ejb.*;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;
import org.afscme.enterprise.log.SystemLog;
import org.afscme.enterprise.update.filequeue.FileEntry;
import org.afscme.enterprise.update.*;
//imports needed for the parser
import javax.xml.parsers.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.*;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.*;


/**
 * Handles validation and/or apply update for a officer update file.
 */
public class ParticipationFileProcessor extends FileProcessor {    
    
    private static Logger logger = Logger.getLogger(ParticipationFileProcessor.class);
    private FileQueue fqBean = null;
    //XML FILE ELEMENTS STRINGS
    private static final String  E_PARTICIPATION  =   "Participation";
    private static final String  E_MBR_OUTCOME    =   "MemberOutcome";
    private static final String  E_AFFILIATE      =   "AffiliateIdentifier";
    private static final String  E_MEMBER         =   "Member";
    private static final String  E_OUTCOME        =   "Outcome";    
    
    /***********************************************************************************/
    //XML FILE ATTRIBUTES STRINGS
    private static final String  A_GROUP          =   "Group";
    private static final String  A_PART_TYPE      =   "Type";
    private static final String  A_DETAIL         =   "Detail";
    
    private static final String  A_TYPE           =   "Type";
    private static final String  A_STATEN         =   "StateNational";
    private static final String  A_COUNCIL        =   "CouncilChapter";
    private static final String  A_LCHAP          =   "LocalSubChapter";
    private static final String  A_SUNIT          =   "SubUnit";
    
    private static final String  A_MBRNUM         =   "MemberNumber";
    private static final String  A_SSN            =   "SSN";
    private static final String  A_LNAME          =   "LastName";
    private static final String  A_FNAME          =   "FirstName";
    
    private static final String  A_OUTNAME        =   "Name";
    private static final String  A_OUTDATE        =   "Date";
   
    private ParticipationOutcomeData       participation;
    private ParticipationOutcomeData       participationDetail;
    private ParticipationUpdateElement     update;
    private AffiliateIdentifier            affId;
    
    private Map  outcomeListPks;
    
    private ParticipationUpdateSummary updateSummary = null;       
    
    private boolean  fileErrorFg = false;
    private boolean  fileExceptionFg = false;
    
    private int    participationInFileCount = 0;
    private int    exceptionInFileCount = 0;
    
    private Update updateBean;      // reference to the update bean
    

    public ParticipationFileProcessor() {

        try {
            updateBean = JNDIUtil.getUpdateHome().create();
            fqBean = JNDIUtil.getFileQueueHome().create();
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        } catch (CreateException ce) {
            throw new RuntimeException(ce);
        }
    }
    
    /** Implemented by derived classes, generates the pre-update summary for a file.
     */
    public PreUpdateSummary generatePreUpdateSummary() {
        return null;
    }
    
    /** Implemented by derived classes, applys the updates for a file.
     * @param affPks A list of affiliate pks that the user has selected to process.
     *
     */
    public void applyUpdate(List affPks, Integer queuePk, Integer userPk) {
        
        logger.debug("----------------------------------------------------");
        logger.debug("applyUpdate called.");

        updateSummary = new ParticipationUpdateSummary();        
        
        // process the file to apply the participations
        processXMLFile();
       
        // check for file error before processing update
        if (fileErrorFg) {
            // need to write out file error
            logger.debug("File Error: More than one Participation Group/Type/Detail (no records processed)! ");
            FileEntry fileEntry = fqBean.getFileEntry(queuePk);
            SystemLog.logAffiliateFileError(fileEntry.getAffPk().toString(), "More than one Participation Group/Type/Detail (no records processed)!", fileEntry.getFileName(), fileEntry.getReceivedDate(), userPk.toString());
        }    
        else {
            // perform the update for all member participations sent in if no file error
            updateBean.doParticipationUpdate(userPk, updateSummary);
        }
        
        logger.debug(" end of applyUpdate!");
        logger.debug("fileErrorFg = " + fileErrorFg);
        logger.debug("fileExceptionFg = " + fileExceptionFg);
        
        logger.debug("participationInFileCount = " + participationInFileCount);
        logger.debug("exceptionInFileCount = " + exceptionInFileCount);        

        logger.debug("updateSummary = " + updateSummary);
    }
    
    /** Validates the file set with setQueue().  Returns 0 if valid, else the return code if error.
     *
     */
    public int validate() {
        return 0;
    }
    
    private void processXMLFile() {
        
        File file = new File(fileEntry.getFilePath() + fileEntry.getFileName());
        
        try {
            
            SAXParserFactory factory    =   SAXParserFactory.newInstance();
            SAXParser sp                =   factory.newSAXParser();
            
            logger.debug("    Created parse and parsing xml:  " );
            sp.parse(file, this);
        } 
        catch(ParserConfigurationException pce) {
            logger.debug(pce.getMessage());
        } 
        catch(SAXException se){
            logger.debug(se.getMessage());
        } 
        catch(IOException ie){
            logger.debug(ie.getMessage());
        }        
    }
    
    /**
     * Processes an individual member participation update element.
     * 
     * update - current Member Participation record (ParticipationUpdateElement)
     * boolean - if true if valid member so that participation record can be added
     */
    private boolean processMemberParticipation() {
        
        logger.debug("----------------------------------------------------");
        logger.debug("processMemberParticipation called.");
        
        return updateBean.matchParticipation(update);
    }
    
    
    // *****************************************************
    //  The following are SAXParser event handling methods
    // *****************************************************
    
    public void startDocument() {
        
        participation   =   new ParticipationOutcomeData();
        update          =   new ParticipationUpdateElement();        
        affId           =   new AffiliateIdentifier();
    }    
    
    public void startElement(String nsUri, String localName, String qName, Attributes attrs) {
         
        logger.debug("    parsing :  "        + qName);

        /***********************************************************************************
         *Get participation details from the document and populate the data structure
         ***********************************************************************************/
        if(qName.equals(E_PARTICIPATION)){
            participation.setGroupNm          (   attrs.getValue(A_GROUP)     != null ?  attrs.getValue(A_GROUP)     : "" );
            participation.setTypeNm           (   attrs.getValue(A_PART_TYPE) != null ?  attrs.getValue(A_PART_TYPE) : "" );
            participation.setDetailNm         (   attrs.getValue(A_DETAIL)    != null ?  attrs.getValue(A_DETAIL)    : "" );
        }
        
        /***********************************************************************************
         *Get member outcome details from the document and populate the data structure
         ***********************************************************************************/
        if(qName.equals(E_MBR_OUTCOME)){
            logger.debug("inside member outcome  parsing :  "        + qName);
        }

        /***********************************************************************************
         *Get affiliate details from the document and populate the data structure
         ***********************************************************************************/
        if(qName.equals(E_AFFILIATE)){
            affId.setType                (   new Character(  attrs.getValue(A_TYPE          )        != null ?  attrs.getValue(A_TYPE).charAt(0)    : '0' ));
            affId.setLocal               (                   attrs.getValue(A_LCHAP         )        != null ?  attrs.getValue(A_LCHAP)             : ""   );
            affId.setState               (                   attrs.getValue(A_STATEN        )        != null ?  attrs.getValue(A_STATEN)            : ""   );
            affId.setSubUnit             (                   attrs.getValue(A_SUNIT         )        != null ?  attrs.getValue(A_SUNIT)             : ""   );
            affId.setCouncil             (                   attrs.getValue(A_COUNCIL       )        != null ?  attrs.getValue(A_COUNCIL)           : ""   );
        }

        /***********************************************************************************
         *Get member details from the document and populate the data structure
         ***********************************************************************************/
        if(qName.equals(E_MEMBER)){
            update.setPersonPk    (   new Integer  (    attrs.getValue  (A_MBRNUM)  != null ?  attrs.getValue(A_MBRNUM)  : "0" ));
            update.setLastName    (   attrs.getValue(A_LNAME)  != null ?  attrs.getValue(A_LNAME)  : ""  );
            update.setFirstName   (   attrs.getValue(A_FNAME)  != null ?  attrs.getValue(A_FNAME)  : ""  );
            update.setSsn         (   attrs.getValue(A_SSN)    != null ?  attrs.getValue(A_SSN)    : ""  );
        }

        /***********************************************************************************
         *Get outcome details from the document and populate the data structure
         ***********************************************************************************/
        if(qName.equals(E_OUTCOME)){
            update.setOutcomeName   (   attrs.getValue(A_OUTNAME)  != null ?  attrs.getValue(A_OUTNAME)  : ""  );
            update.setOutcomeDate   (   attrs.getValue(A_OUTDATE)  != null ?  Timestamp.valueOf(attrs.getValue(A_OUTDATE) +" 00:00:00.000000000") : null );
        }        
    }
    
    public void endElement(String nsUri, String localName, String qName) {

        if(qName.equals(E_PARTICIPATION)){
            
            // count the number of participation elements in the file
            participationInFileCount++;
            
            // if more than one participation element, then file is rejected
            if (participationInFileCount > 1) {
                fileErrorFg = true;
            }
            else {
                
                // determine and fill in the participation pk
                participationDetail = updateBean.getParticipationDetailData(participation.getGroupNm(),
                                                                            participation.getTypeNm(),
                                                                            participation.getDetailNm());
                if (participationDetail == null) {
                    fileErrorFg = true;                    
                }
                else {

                    // get all the outcomes for the specific detail
                    outcomeListPks = updateBean.getParticipationOutcomes(participationDetail.getDetailPk());
                }
            }    
        }        
       
        if(qName.equalsIgnoreCase(E_MBR_OUTCOME)){
            
            boolean recordExceptionFg = false;
            
            // update the detail info and match the outcome (if exists)
            update.setDetailPk(participationDetail.getDetailPk());
            if (outcomeListPks.containsKey(update.getOutcomeName().trim().toUpperCase())) {
                update.setOutcomePk((Integer) outcomeListPks.get(update.getOutcomeName().trim().toUpperCase()));
            }
            else {
                
                // exception for invalid outcome for participation group, type & detail
                fileExceptionFg = true;
                recordExceptionFg = true;
                exceptionInFileCount++;
                
                // need to write out exception
                logger.debug("Exception: Invalid Outcome found for record (ssn/name/outcome) = " + 
                             update.getSsn() + "/" + update.getFirstName() +
                             " " + update.getLastName() + "/" + update.getOutcomeName() );
                SystemLog.logRecordUpdateError("ssn/name/outcome", new java.sql.Timestamp(new java.util.Date().getTime()), "Invalid Outcome found for record (ssn/name/outcome)", "");
                
                
            }    

            // ************* Start processing the current member participation record ***********
            if (!processMemberParticipation()) {   // returns false if member cannot be found
                
                // exception for not valid member (not match for afscme mbr number)
                fileExceptionFg = true;
                recordExceptionFg = true;
                exceptionInFileCount++;
                
                // need to write out exception
                logger.debug("Exception: Invalid Member not found for record (mbr_nbr/ssn/name) = " + 
                             update.getPersonPk() + "/" + update.getSsn() + "/" + 
                             update.getFirstName() + " " + update.getLastName() );
                SystemLog.logRecordUpdateError("mbr_nbr/ssn/name", new java.sql.Timestamp(new java.util.Date().getTime()), "Invalid Member not found for record (mbr_nbr/ssn/name)", "");

            }    
                
            // add to list to update if no exception found
            if (recordExceptionFg == false) {
                
                // if record has not been found to be updated yet, then add to list to update
                if (!updateSummary.getUpdates().containsKey(update.getPersonPk())) {
                    updateSummary.getUpdates().put(update.getPersonPk(), update);
                }
            }    
            
            // create new update elements for next one
            update = new ParticipationUpdateElement();
            affId  = new AffiliateIdentifier();
            recordExceptionFg = false;
        }               
    }
    
    public void endDocument()  {
    }     
    
}
