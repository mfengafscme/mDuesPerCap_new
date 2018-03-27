package org.afscme.enterprise.update.officer;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.ejb.EJBException;
import org.xml.sax.Attributes;
import org.afscme.enterprise.update.FileProcessor;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.update.PreUpdateSummary;
import org.afscme.enterprise.update.AddressElement;
import org.afscme.enterprise.update.PersonUpdateElement;
import org.afscme.enterprise.update.officer.OfficerUpdateElement;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
//import org.apache.xerces.impl.xs.psvi.PSVIProvider;
import java.sql.Timestamp;
import org.afscme.enterprise.codes.ejb.*;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.Codes;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import org.afscme.enterprise.update.ejb.*;
import org.afscme.enterprise.update.FieldChange;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.update.Codes.UpdateFileType;
import org.afscme.enterprise.update.Codes.UpdateType;
import org.afscme.enterprise.update.Codes.MemberUpdateFields;
import org.afscme.enterprise.update.Codes.OfficerUpdateFields;
import org.afscme.enterprise.update.Codes.OfficerTitleCode;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.update.officer.OfficerChanges;
import org.afscme.enterprise.update.officer.OfficerReviewSummary;
import org.afscme.enterprise.update.officer.OfficerReviewData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.AffiliateErrorCodes;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateHierarchyEntry;
import org.afscme.enterprise.update.ExceptionComparison;
import org.afscme.enterprise.update.ExceptionData;
import org.afscme.enterprise.person.ejb.*;
import org.afscme.enterprise.person.*;
import org.afscme.enterprise.address.ejb.*;
import org.afscme.enterprise.address.*;
import org.afscme.enterprise.common.*;
/**************************************************************************************/
//imports needed for the parser
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.log4j.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
/***************************************************************************************/
/**
 * Handles validation and/or apply update for a officer update file.
 * The file processor processes one officer that it reads from the file one at a time
 * First the affiliate rules are applied as laid out in C&B document, then the exceptions are applied.
 * Afer eleminating the officers having exceptions get the staistics for field changes and populate
 * the summary data structure with the officer change statistics and field change statistics and exception 
 * list.
 * For the apply update processing after apply the rules it will create a review summary and store the 
 * review summary and apply the updates.
 */
public class OfficerFileProcessor extends FileProcessor {    
    /***********************************************************************************/
    //These should be defined in another class. But for now they defined here
    //POSIBLE XML FILE ELEMENTS STRINGS
    private static final String  E_AFFILIATE      =   "AffiliateIdentifier";
    private static final String  E_OFFICER        =   "Officer";
    private static final String  E_POSITIONA      =   "PositionAt-AffiliateIdentifier";
    private static final String  E_HOMEA          =   "Home-AffiliateIdentifier";
    private static final String  E_FULLNAME       =   "FullName";
    private static final String  E_PADDRESS       =   "PrimaryAddress";
    private static final String  E_PPHONE         =   "PrimaryPhone";
    /***********************************************************************************/
    //POSSIBLE XML FILE ATTRIBUTES STRINGS
    private static final String  A_TYPE           =   "Type";
    private static final String  A_STATEN         =   "StateNational";
    private static final String  A_COUNCIL        =   "CouncilChapter";
    private static final String  A_LCHAP          =   "LocalSubChapter";
    private static final String  A_SUNIT          =   "SubUnit";
    private static final String  A_MNUM           =   "AFSCMEMbrNum";
    private static final String  A_AMNUM          =   "AffiliateMbrNum";
    private static final String  A_TITLE          =   "OfficerTitle";
    private static final String  A_TEXPIRE        =   "TermExpiration";
    private static final String  A_LOCAL          =   "LocalSubChapter";
    private static final String  A_ADDRESS1       =   "Address1";
    private static final String  A_ADDRESS2       =   "Address2";
    private static final String  A_CITY           =   "City";
    private static final String  A_STATE          =   "State";
    private static final String  A_ZIP            =   "Zip";
    private static final String  A_ZIP4           =   "Zip4";
    private static final String  A_LNAME          =   "LastName";
    private static final String  A_FNAME          =   "FirstName";
    private static final String  A_MNAME          =   "MiddleName";
    private static final String  A_SUFFIX         =   "Suffix";
    private static final String  A_PREFIX         =   "Prefix";
    private static final String  A_SSN            =   "SSN";
    private static final String  A_STATUS         =   "Status";
    private static final String  A_TRANSTYPE      =   "TransactionType";
    private static final String  A_PHONECC        =   "CountryCode";
    private static final String  A_PHONEAC        =   "AreaCode";
    private static final String  A_PHONENUM       =   "PhoneNumber";
    
    private static final String  ADDTRANS        =   "Add";
    private static final String  REMTRANS        =   "Remove";
    private static final String  RENTRANS        =   "Renew";
    private static final String  VACTRANS        =   "Vacate";
    /***********************************************************************************/
     /* Field names used by UI */
    public static final String PREFIX               = "Prefix";   
    public static final String FIRST_NAME           = "First Name";
    public static final String MIDDLE_NAME          = "Middle Name";    
    public static final String LAST_NAME            = "Last Name";
    public static final String SUFFIX               = "Suffix";
    public static final String ADDR1                = "Primary Address 1";
    public static final String ADDR2                = "Primary Address 2";
    public static final String CITY                 = "City";
    public static final String STATE                = "State";
    public static final String COUNTRY              = "Country";
    public static final String ZIP                  = "Zip/Postal Code";
    public static final String ZIPPLUS              = "Zip+";
    public static final String PROVINCE             = "Province";
    public static final String PHONECOUNTRY         = "Country Code";
    public static final String PHONEAREA            = "Area Code";
    public static final String PHONENUMBER          = "Phone Number";
    public static final String TITLE                = "Officer Title";
    public static final String EXPIRE_TERM          = "Term Expiration";
    public static final String SSN                  = "Social Security Number";
    public static final String AFFILIATE_MEMBER_ID  = "Unique Affiliate Member ID";
    public static final String AFSCME_MEMBER_ID     = "AFSCME Member Number";    
    public static final String AFFILIATE_TYPE       = "AffiliateType";
    public static final String LOCAL_CHAPTER        = "Local/Sub Chapter";
    public static final String STATE_TYPE           = "State/National Type";
    public static final String SUB_UNIT             = "Sub Unit";
    public static final String COUNCIL_CHAPTER      = "Council/Retiree Chapter";

    // This is used by setErroFlag()
    public static final String AFF_AFFILIATE_TYPE   = "AFF_AffiliateType";
    public static final String AFF_LOCAL_CHAPTER    = "AFF_Local/Sub Chapter";
    public static final String AFF_STATE_TYPE       = "AFF_State/National Type";
    public static final String AFF_SUB_UNIT         = "AFF_Sub Unit";
    public static final String AFF_COUNCIL_CHAPTER  = "AFF_Council/Retiree Chapter"; 

    public static final String POS_AFFILIATE_TYPE   = "POS_AffiliateType";
    public static final String POS_LOCAL_CHAPTER    = "POS_Local/Sub Chapter";
    public static final String POS_STATE_TYPE       = "POS_State/National Type";
    public static final String POS_SUB_UNIT         = "POS_Sub Unit";
    public static final String POS_COUNCIL_CHAPTER  = "POS_Council/Retiree Chapter";     
    
    /*************************************************************************************************/
    private static Logger        logger           =   Logger.getLogger(OfficerFileProcessor.class);
    private OfficerChanges       oChanges         =   null;
    private OfficerReviewData    oReviewData      =   null;
    private boolean              aMemNumExists    =   false; 
    private boolean              affNumExists     =   false; 
    private Integer              repAffPk         =   null;
    private Integer              currAffPk        =   null;
    private Integer              submittingAffPk  =   null;     // or reporting affiliate or top affiliate in xml      
    private Integer              personPk         =   null;
    private Integer              homePk           =   null;
    private HashMap              fieldChanges     =   new HashMap();
    private HashMap              mapOffChanges    =   new HashMap();
    private HashMap              mapOffAllowed    =   new HashMap();
    private HashMap              mapPosChanges    =   new HashMap();
    private HashMap              affOfficers      =   new HashMap();
    private HashMap              offDetails       =   new HashMap();
    private ArrayList            oReviewList      =   new ArrayList();   //dataStructure to hold the rebateSummary
    private ArrayList            excepData        =   new ArrayList();
    //*************************************************************************************************
    //storing the data processed validated and now to updated and passed on to the ejb for updates
    private HashMap              offData          =   new HashMap();
    //************************************************************************************************
    private boolean              applyUpdate      =   false;
    private boolean              stFlag           =   false;
    private boolean              afscmeStaff      =   false;
    private boolean              affStaff         =   false;
    private boolean              affOfficer       =   false;
    private Integer              posCode          =   null;
    private Integer              queuePk          =   null;
    private Integer              userPk           =   null;
    private String               posDesc          =   "";
    private int                  mElected         =   0;
    private int                  yearExp          =   0;
    private int                  inFileCount      =   0;
    private int                  officerCounter   =   0 ;
    private int                  excepCount       =   0;
    private boolean              exception        =   false;
    private int                  SUCCESS          =   0;
    private int                  FAILURE          =   -1;
    private MaintainPersons                           personBean;
    private SystemAddress                             addressBean;
    private AddressElement                            address;  
    private PersonAddressRecord                       sAddress;
    private OfficerUpdateElement                      officer;
    private PersonData                                person;
    private AffiliateIdentifier                       topAffId;
    private AffiliateIdentifier                       posAffId;
    private AffiliateIdentifier                       homAffId;
    private Update                                    updateBean;             // reference to the update bean
    private FileQueue                                 fileQueue;              // reference to the file queue bean
    private MaintainAffiliates                        maintainAffiliates;     // reference to the MaintainAffiliatesBean
    private MaintainCodes                             codeBean;               // reference to the MaintainCodesBean
    private Collection                                allAffiliates;
    private Set                                       subAffiliates;
    private OfficerPreUpdateSummary                   oPreUpdSmry;
    private OfficerReviewSummary                      oReviewSmry;
    private OfficerReviewData                         oRData;
    private PhoneData                                 oPhone; 
    private PhoneData                                 sPhone;
    // map of affiliates in the update file
    // key -- affPk, value -- affId
    private Map                                     affiliates;
    
    // keeps track of what needs to be updated.
    // key -- affID, value -- UpdateTabulation
    private Map                                     updatableAffiliates;    
    private ExceptionData                           eDataGlobal;
    private boolean              posAffPkExist      =   false;     
    private HashMap officersSortByAffiliate         = new HashMap();
    private HashMap allAffOfficers                  = new HashMap();
    private List      officeList                    = new ArrayList();
    private HashMap mapInFileCount                  = new HashMap();
    private HashMap personPkDummyCounter            = new HashMap();

    /**************************************************************************************/
    //constructor to initialize critical variables before hand
    public OfficerFileProcessor(){
        //create the bean for later use
        try {
            updateBean      =   JNDIUtil.getUpdateHome().create();
            personBean      =   JNDIUtil.getMaintainPersonsHome().create();  
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        } catch (CreateException ce) {
            throw new RuntimeException(ce);
        }
    
    }
    public OfficerFileProcessor(FileQueue queueBean, MaintainAffiliates maintainAffBean, MaintainCodes maintainCodes) {
        fileQueue           =   queueBean;
        maintainAffiliates  =   maintainAffBean;
        logger.debug("maintainAffiliates. ============>" + maintainAffiliates);
        codeBean            =   maintainCodes;
        affiliates          =   new HashMap();
        updatableAffiliates =   new HashMap();
        
        oPreUpdSmry         =   new OfficerPreUpdateSummary();
        try {
            updateBean      =   JNDIUtil.getUpdateHome().create();
            personBean      =   JNDIUtil.getMaintainPersonsHome().create();
            addressBean     =   JNDIUtil.getSystemAddressHome().create();
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        } catch (CreateException ce) {
            throw new RuntimeException(ce);
        }
        
    }
    //******************************************************************************************************************
    /** Implemented by derived classes, generates the pre-update summary for a file.
     */
    public PreUpdateSummary generatePreUpdateSummary() {
        logger.debug("Generate Pre Update Summary method entered and existed.");        
        return (PreUpdateSummary) oPreUpdSmry;
        
    }    
    
    /** Implemented by derived classes, applys the updates for a file.
     * @param affPks A list of affiliate pks that the user has selected to process.
     *
     */
    public void applyUpdate(List affPks, Integer queuePk, Integer userPk) {
        logger.debug("----------------------------------------------------");
        logger.debug("applyUpdate called.");
        applyUpdate =   true;
        this.queuePk = queuePk;
        this.userPk  = userPk;
        OfficerReviewSummary oReviewSmry    =   new OfficerReviewSummary();
        logger.debug("affPk is = " + affPks);        
        processXMLFile(affPks, queuePk, userPk);
                
    }
    
    /**This method will process the affiliates that are passed and which are within the file
     @param affPks A list of affiliate pks that the user has selected to process.
     *
     */
    private int processXMLFile(List affPks, Integer queuePk, Integer userPk){
        logger.debug("----------------------------------------------------");
        logger.debug("processFixedLengthFileForUpdate called.");
        boolean includeMemberForUpdate = true;
        this.queuePk = queuePk;
        this.userPk  = userPk;
        //try {            
            File file = new File(fileEntry.getFilePath() + fileEntry.getFileName());                        
            AffiliateData topAffData = maintainAffiliates.getAffiliateData(fileEntry.getAffPk());
            if (topAffData == null || topAffData.getAffiliateId() == null) {
                throw new RuntimeException("Cannot find the Affiliate that sent this file. affPk = " + fileEntry.getAffPk());
            } // this should never happen...
            //**************************************************************************************************
            //The requirements that all the sub locals of the local hierarchy must be added to the file which 
            //didn't come in the file is implemented with the below function call  in part by getting all the 
            //affiliated sub locals of the reporting affiliate
            getAffiliatesSubLocals(fileEntry.getAffPk());
            //***********************************************************************************************
            //will return false if there are no affiliate rules errors otherwise true in case of errors
            boolean notProceed =   checkAffiliatesErrors(affPks);
            //*********************************************************************************************
            logger.debug("affiliate has errors so proceed=============>" + notProceed);
            if(notProceed){
                logger.debug("----------------------------------------------------");
                logger.debug("cannot proceed the affiliate is not part of hiararchy.");
                fileQueue.markFileBad(queuePk, userPk);
                return FAILURE;
            }else{
                logger.debug("----------------------------------------------------");
                logger.debug("processing xml file passed first check of afiliate rule check.");
                processXMLFile(file);
                //******************************************************************************************
                //last rule to be applied if complete reading the document
                //If either of the affiliate member number or the afscme member number is not present in the file
                //then it is an error and the file cannot b processed so mark the file  as having error
                //Note:-little repeatation needs to be made into a function call
                //********************************************************************************************
/*
                if(!aMemNumExists){
                    setAllOfficerChangesAsError();
                    logger.debug("Affiliate member number does not exists.");
                }
                if(!affNumExists){
                    setAllOfficerChangesAsError();
                    logger.debug("Affiliate id number does not exists.");
                }
 */
                //*********************************************************************************************                
                logger.debug("----------------------------------------------------");
                logger.debug("calling update officers.");
                updateBean.updateOfficers(offData, fileEntry.getQueuePk(), userPk, oReviewSmry);
                logger.debug("----------------------------------------------------");
                logger.debug("done calling update officers.");                
            }                        
            return SUCCESS;                    
    }
    /*
     *@param list of affpks
     *Will check if the affiliate coming in is part of the affiliate submitting the hierarchy and 
     *if the status is not deactivated or merged as they cannot submit officers
     *@returns true if no errors found or false if finds an error
     */
    public boolean checkAffiliatesErrors(List affPks){
        
        logger.debug("Check Affiliates Errors method entered.");
        boolean affErrors   =   false;        
        if(subAffiliates != null){                                               
            if(subAffiliates.containsAll(affPks)){
                Iterator        it          =   affPks.iterator();
                while(it.hasNext()){
                    Integer currAffPk           =   (Integer) it.next();
                    if(currAffPk != null){
                        AffiliateData curAffData    =    maintainAffiliates.getAffiliateData(currAffPk);
                        if(curAffData != null){                            
                            Integer affStatus    =   curAffData.getStatusCodePk();
                            if (affStatus       !=   null) {
                                /*******************************************************************************/
                                //rule 2: check the affiliate status if "merged" then it cannot have officers
                                //rule 3: check the affiliate status if "deactivated" then cannot be reporting officers
                               /*******************************************************************************/
                                if ((affStatus.equals(Codes.AffiliateStatus.M)) || (affStatus.equals(Codes.AffiliateStatus.D))) {
                                    logger.debug("Cannot be reporting officers as the status is ." + affStatus );
                                    //oChanges.setHasError(true);
                                    affErrors = true;
                                }//end of rule check                                                   
                            }else{
                                affErrors = true;
                            }//end of no status check                                           
                        }//end of curAffData check
                    }//end of currAffPk Check
                }//end of while
            }//end of aff hiararchy check
        }else{
            affErrors   =   true;
        }//end of outer if        
        
        logger.debug("Check Affiliates Errors method existed.");        
        return affErrors;
    }//end of checkAffiliatesWithinHierarchy method
    
    
    
    /*******************************************************************************************************
     * Validates the file set with setQueue()
     * @returns SUCCESS or FAILURE
     *******************************************************************************************************/
   
    public int validate() {        
        logger.debug("Validate method entered.");            
        File file = new File(fileEntry.getFilePath() + fileEntry.getFileName());
        //THE FILE SIZE SHOULD BE OF FILE SIZE SET IN THE CONFIGURATION FILES
        logger.debug("File info for file: "             + (fileEntry.getFilePath() + fileEntry.getFileName()));
        logger.debug("    actual file length:  "        + file.length());
        logger.debug("    min file length:     "        + ConfigUtil.getConfigurationData().getMinUploadFileSize());

       if (file.length() < ConfigUtil.getConfigurationData().getMinUploadFileSize()) {
            logger.debug("returning false.");
            return FAILURE;
        }
        
        applyUpdate =   false;                            
        
        processXMLFile(file);                               
        
//        if (this.checkForFileError() || !this.posAffPkExist)
        if (this.checkForFileError())
            return FAILURE; 
        
        logger.debug("Validate method existed.");            
       
        return SUCCESS;        
    }
    
    
    /**
     * Handles validation and/or apply update for an officer update file.
     * Please note that the current parser is not a validating one it must
     *be a validating one for the schema to make any sense
     */
    private void processXMLFile(File file) {
        logger.debug("Process XML File method entered.");            
        try{
            SAXParserFactory factory    =   SAXParserFactory.newInstance();            
            SAXParser sp                =   factory.newSAXParser();   
            //factory.setValidating(true);
            //factory.setNamespaceAware(false);            
            //factory.setFeature("http://apache.org/xml/features/validation/schema", true);                     
            logger.debug("    Created parse and parsing xml:  " );            
            //sp.parse("C:/vpandya_view/AFSCME/Software/doc/xml/SampleOfficerUpdate.xml", new OfficerFileProcessor());
            //XMLReader xmlReader     = sp.getXMLReader();
            //xmlReader.setErrorHandler(this);
            //xmlReader.setContentHandler(this);
            //InputSource is          = new InputSource(file); 
            //xmlReader.parse(is);
            logger.debug("validating====>" + sp.isValidating());
            sp.parse(file, this);           
            //org.xml.sax.Parser parser = sp.getParser();    
            //parser.setErrorHandler(this);
            //parser.setContentHandler(this);
            //parser.parse(new InputSource(file.toString()));
            //******************************************************************
            }catch(ParserConfigurationException pce){
                logger.debug(pce.getMessage());
            }catch(SAXException se){
                logger.debug(se.getMessage());
            }catch(IOException ie){
                logger.debug(ie.getMessage());
            }
        logger.debug("Process XML File method existed.");                    
    }
    
    // *****************************************************
    //  The following are SAXParser event handling methods
    // *****************************************************
    /*
     *The following call back method will be called for every officer read so process each officer.
     *apply all the rules and calculate all the statistics. It will be faster 
     */   
    public void startDocument() {
        logger.debug("Start Document method entered.");        
        address         =   new AddressElement();
        officer         =   new OfficerUpdateElement();
        topAffId        =   new AffiliateIdentifier();
        posAffId        =   new AffiliateIdentifier();
        homAffId        =   new AffiliateIdentifier();
        oPhone          =   new PhoneData();
        
        intializeFieldChange();
        this.posAffPkExist = false;
        //the officer changes are per affiliate that means there may be more than one officer 
        //per affiliate and they may be any where in the file. So just create one officer 
        //change object to make it available 
        //oChanges        =   new OfficerChanges();
        logger.debug("Start Document method existed.");                
    }    

    
    public void startElement(String nsUri, String localName, String qName, Attributes attrs) {
         logger.debug("Start Element method entered.");
         logger.debug("    parsing :  "        + qName);
        /***********************************************************************************
         *Get address details from the document and populate the data structure
         ***********************************************************************************/        
        if(qName.equals(E_PADDRESS)){
            //logger.debug("inside address    parsing :  "        + qName);
            address.setAddr1                (   attrs.getValue(A_ADDRESS1) != null ?  attrs.getValue(A_ADDRESS1)  : ""    );
            address.setAddr2                (   attrs.getValue(A_ADDRESS2) != null ?  attrs.getValue(A_ADDRESS2)  : ""    );
            address.setCity                 (   attrs.getValue(A_CITY)     != null ?  attrs.getValue(A_CITY)      : ""    );
            address.setState                (   attrs.getValue(A_STATE)    != null ?  attrs.getValue(A_STATE)     : ""    );
            address.setZipCode              (   attrs.getValue(A_ZIP)      != null ?  attrs.getValue(A_ZIP)       : ""    );
            address.setZipPlus              (   attrs.getValue(A_ZIP4)     != null ?  attrs.getValue(A_ZIP4)      : ""    ); 
        }
        /***********************************************************************************
         *Get phone details from the document and populate the data structure
         ***********************************************************************************/        
        if(qName.equals(E_PPHONE)){
            //logger.debug("inside phone    parsing :  "        + qName);
            oPhone.setCountryCode          (   attrs.getValue(A_PHONECC) != null ?  attrs.getValue(A_PHONECC)  : ""    );
            oPhone.setAreaCode             (   attrs.getValue(A_PHONEAC) != null ?  attrs.getValue(A_PHONEAC)  : ""    );
            oPhone.setPhoneNumber          (   attrs.getValue(A_PHONENUM)!= null ?  attrs.getValue(A_PHONENUM) : ""    );
            
        }
        /***********************************************************************************
         *Get officer details from the document and populate the data structure
         ***********************************************************************************/
        if(qName.equals(E_FULLNAME)){
            //logger.debug("inside fullname    parsing :  "        + qName);            
            officer.setLastName             (   attrs.getValue(A_LNAME)  != null ?  attrs.getValue(A_LNAME)  : ""    );
            officer.setFirstName            (   attrs.getValue(A_FNAME)  != null ?  attrs.getValue(A_FNAME)  : ""    );
            officer.setMiddleName           (   attrs.getValue(A_MNAME)  != null ?  attrs.getValue(A_MNAME)  : ""    );            
            officer.setSuffix               (   attrs.getValue(A_SUFFIX) != null ?  attrs.getValue(A_SUFFIX) : ""    );

            if (officer.getSuffix().length() == 1){                
                Integer suffixInt = new Integer(officer.getSuffix());                
                if (suffixInt.intValue() != 1 && suffixInt.intValue() != 2 && suffixInt.intValue() != 3 &&
                    suffixInt.intValue() != 4 && suffixInt.intValue() != 5 && suffixInt.intValue() != 6)
                {
                    officer.setSuffix("");                        
                }
            }else{
                officer.setSuffix("");// unknow code, store as null in database                   
            }
       
            officer.setPrefix               (   attrs.getValue(A_PREFIX) != null ?  attrs.getValue(A_PREFIX) : ""    );            

            if (officer.getPrefix().length() == 1){                
                Integer prefixInt = new Integer(officer.getPrefix());                
                if (prefixInt.intValue() != 1 && prefixInt.intValue() != 2 && prefixInt.intValue() != 3 &&
                    prefixInt.intValue() != 4 && prefixInt.intValue() != 5 && prefixInt.intValue() != 6 && prefixInt.intValue() != 7)
                {
                    officer.setPrefix("");                        
                }
            }else{
                officer.setPrefix("");// unknow code, store as null in database                   
            }            
                                   
            logger.debug("inside fullname parsing: Last Name of Officer = " + officer.getLastName());
        }        
        if(qName.equals(E_OFFICER)){
            //logger.debug("inside officer    parsing :  "        + qName);                        
            officer.setAffiliateMemberNumber(                   (    attrs.getValue  (A_AMNUM)          != null ?  attrs.getValue(A_AMNUM)     : ""   ));
            if (attrs.getValue(A_MNUM) != null && attrs.getValue(A_MNUM).length() > 0){
                officer.setAfscmeMemberNumber(new Integer(attrs.getValue(A_MNUM)));
            }else{
                officer.setAfscmeMemberNumber(null);
            }
            //officer.setAfscmeMemberNumber   (   new Integer     (    attrs.getValue  (A_MNUM)           != null && attrs.getValue(A_MNUM).length() > 0 ?  attrs.getValue(A_MNUM)      : "0"  ));       
            officer.setTitle                (   new Integer     (    attrs.getValue  (A_TITLE)          != null && attrs.getValue(A_TITLE).length() > 0 ?  attrs.getValue(A_TITLE)     : "0"  ));                       
            officer.setTermExpiration       (                        attrs.getValue  (A_TEXPIRE)        != null ?  attrs.getValue(A_TEXPIRE)   : ""   );
            officer.setSsn                  (                        attrs.getValue  (   A_SSN )        != null ?  attrs.getValue(A_SSN)       : ""   );
            officer.setStatus               (   new Integer     (    attrs.getValue  (A_STATUS )        != null && attrs.getValue(A_STATUS).length() > 0 ?  attrs.getValue(A_STATUS)    : "0"  ));
            officer.setTransactionType      (                        attrs.getValue  (A_TRANSTYPE)      != null ?  attrs.getValue(A_TRANSTYPE) : ""   );            
        }
        /***********************************************************************************
         *Get affiliate details from the document and populate the data structure
         ***********************************************************************************/
        if(qName.equals(E_AFFILIATE)){            
            //logger.debug("inside top affiliate    parsing :  "        + qName);           
            topAffId.setType                (   new Character(  attrs.getValue(A_TYPE          )        != null && attrs.getValue(A_TYPE).toString().length() > 0 ?  attrs.getValue(A_TYPE).charAt(0)    : '0' ));                                
            topAffId.setLocal               (                   attrs.getValue(A_LCHAP         )        != null ?  attrs.getValue(A_LCHAP)             : ""   );
            topAffId.setState               (                   attrs.getValue(A_STATEN        )        != null ?  attrs.getValue(A_STATEN)            : ""   );
            topAffId.setSubUnit             (                   attrs.getValue(A_SUNIT         )        != null ?  attrs.getValue(A_SUNIT)             : ""   );
            topAffId.setCouncil             (                   attrs.getValue(A_COUNCIL       )        != null ?  attrs.getValue(A_COUNCIL)           : ""   );                                                        
        }
        /***************************************************************************************/        
        /***********************************************************************************
         *Get position affiliate details from the document and populate the data structure
         ***********************************************************************************/
        if(qName.equals(E_POSITIONA)){            
            //logger.debug("inside position at affiliate    parsing :  "        + qName);                                
            posAffId.setType                (   new Character(  attrs.getValue(A_TYPE          )        != null && attrs.getValue(A_TYPE).toString().length() > 0 ?  attrs.getValue(A_TYPE).charAt(0)    : '0' ));
            posAffId.setLocal               (                   attrs.getValue(A_LCHAP         )        != null ?  attrs.getValue(A_LCHAP)             : ""   );
            posAffId.setState               (                   attrs.getValue(A_STATEN        )        != null ?  attrs.getValue(A_STATEN)            : ""   );
            posAffId.setSubUnit             (                   attrs.getValue(A_SUNIT         )        != null ?  attrs.getValue(A_SUNIT)             : ""   );
            posAffId.setCouncil             (                   attrs.getValue(A_COUNCIL       )        != null ?  attrs.getValue(A_COUNCIL)           : ""   );

        }
        /***************************************************************************************/        
        /***********************************************************************************
         *Get home affiliate details from the document and populate the data structure
         ***********************************************************************************/
        if(qName.equals(E_HOMEA)){                                            
            //logger.debug("inside home at affiliate    parsing :  "        + qName);                                
            homAffId.setType                (   new Character(  attrs.getValue(A_TYPE          )        != null && attrs.getValue(A_TYPE).toString().length() > 0 ?  attrs.getValue(A_TYPE).charAt(0)    : '0' ));
            homAffId.setLocal               (                   attrs.getValue(A_LCHAP         )        != null ?  attrs.getValue(A_LCHAP)             : ""   );
            homAffId.setState               (                   attrs.getValue(A_STATEN        )        != null ?  attrs.getValue(A_STATEN)            : ""   );
            homAffId.setSubUnit             (                   attrs.getValue(A_SUNIT         )        != null ?  attrs.getValue(A_SUNIT)             : ""   );
            homAffId.setCouncil             (                   attrs.getValue(A_COUNCIL       )        != null ?  attrs.getValue(A_COUNCIL)           : ""   );                                                        
        }         
        logger.debug("Start Element method existed.");         
    }
    
        
    /*
     *This method will be call back when the xml parser will find the end element
     */
    public void endElement(String nsUri, String localName, String qName) {        

        if(qName.equalsIgnoreCase(E_OFFICER) && (officer != null)){            
            logger.debug("End Element method entered.");
            
            //------------------------------------------------------------------            
            // When processing a new officer, set the exception flag to false
            exception = false;            
            
            //------------------------------------------------------------------            
            // First check for file error.            
            if (checkForFileError())
                return;

            logger.debug("There is no file error found.");                      
            
            //------------------------------------------------------------------        
            // getting the currAffPk: which is the aff_pk the officer works at.
            currAffPk = this.getAffPk(posAffId.getType(), posAffId.getLocal(), posAffId.getState(), posAffId.getSubUnit(), posAffId.getCouncil());

            // do not process the officer is the affiliate the officer associated with can not be found
            if (currAffPk == null || currAffPk.intValue() == 0){            
                logger.debug("currAffPk can not be found");
                AffiliateIdentifier affId = new AffiliateIdentifier();
                affId.setCouncil(posAffId.getCouncil());
                affId.setLocal(posAffId.getLocal());
                affId.setState(posAffId.getState());
                affId.setSubUnit(posAffId.getSubUnit());
                affId.setType(new Character(posAffId.getType().toString().charAt(0)));
                oChanges = new OfficerChanges();
                oChanges.setHasError(true);
                mapOffChanges.put(affId, oChanges);// replace the old statistics                                
                return;                                
                
            }else{
                officer.setAffPk(currAffPk);
            }
            
            //------------------------------------------------------------------        
            // obtain home aff_pk
            officer.setHomeAffPk(getAffPk(homAffId.getType(), homAffId.getLocal(), homAffId.getState(), homAffId.getSubUnit(), homAffId.getCouncil()));
            
            //------------------------------------------------------------------
            // If all pos aff pk do not exist then mark the file bad
            posAffPkExist = true;
            
            //------------------------------------------------------------------            
            // create a new OfficerChanges object 
            if (getOfficerChanges(currAffPk) != null){
                oChanges = getOfficerChanges(currAffPk);
            }
            else{
                oChanges = new OfficerChanges();
            }

            //------------------------------------------------------------------            
            // create a new exceptionData object.
            eDataGlobal = new ExceptionData();
            
            //------------------------------------------------------------------                                    
            boolean matchFound = false;
            boolean affiliateError = false;
            boolean requiredFieldError = false;
            boolean exceptionError = false;
            boolean addError = false;
            boolean renewRemoveVacateError = false;
            
            affiliateError = applyAffiliateRules();                        
            if (!affiliateError){            
                matchFound = matchOfficer();    
                if (matchFound){           
                    requiredFieldError = checkForRequiredFields();
                    if (!requiredFieldError){
                        exceptionError = checkForExceptions();
                        if (!exceptionError){
                            if (officer.getTransactionType().equalsIgnoreCase("Add"))
                                addError = checkForAddException();
                            else if(officer.getTransactionType().equalsIgnoreCase("Renew") || officer.getTransactionType().equalsIgnoreCase("Remove") || officer.getTransactionType().equalsIgnoreCase("Vacate"))
                                renewRemoveVacateError = this.checkForRenewRemoveVacateException();                           
                        }
                    }           
                }
            }
           
            logger.debug("the result for affiliateError = " + affiliateError);
            logger.debug("the result for requiredFieldError = " + requiredFieldError);
            logger.debug("the result for exceptionError = " + exceptionError);
            logger.debug("the result for addError = " + addError);
            logger.debug("the result for renewRemoveVacateError = " + renewRemoveVacateError);
            logger.debug("the result for matchFound = " + matchFound);
                       
            //------------------------------------------------------------------                    
            //if(affiliateError || requiredFieldError || exceptionError || addError || !matchFound){
            if (!affiliateError){
                if(requiredFieldError || exceptionError || addError || renewRemoveVacateError || !matchFound){

                    eDataGlobal.setAffPk(currAffPk);
                    eDataGlobal.setFirstName(officer.getFirstName());
                    eDataGlobal.setLastName(officer.getLastName());
                    eDataGlobal.setMiddleName(officer.getMiddleName());                        
                    eDataGlobal.setPersonPk(officer.getAfscmeMemberNumber());
                    eDataGlobal.setSuffix(officer.getSuffix());            

                    if(!applyUpdate)
                        oPreUpdSmry.addException(new Integer(excepCount), eDataGlobal);                                         
                    else
                        excepData.add(excepCount, (Object) eDataGlobal);   

                    excepCount += 1;
                    exception = !exception;
                }                                    
            }
            
            //------------------------------------------------------------------                                    
            // If all exception checks pass then add to the map of officer                            
            if (!affiliateError && !requiredFieldError && !exceptionError && !addError && !renewRemoveVacateError && matchFound){
                
                offData.put(new Integer(officerCounter++), officer);                                        
                
                List list = new ArrayList();
                if (officersSortByAffiliate.containsKey(currAffPk))
                {
                    list = (List)officersSortByAffiliate.get(currAffPk);                    
                }else{
                    
                    officersSortByAffiliate.put(currAffPk, list);
                }
                            
                list.add(officer);               
                
            }
            //------------------------------------------------------------------                        
            if (!applyUpdate && !affiliateError && !requiredFieldError && !exceptionError && !addError && !renewRemoveVacateError && matchFound){
                recordOfficerChanges();                                        
                processFieldChangeStatistics();                                            
            }    
            
            // populate this hashmap in all case
            oChanges.setAffPk(currAffPk);                                
            mapOffChanges.put(currAffPk, oChanges);// replace the old statistics                
            setAffiliateInFileCount(officer.getAfscmeMemberNumber());
                      
            //------------------------------------------------------------------
            //do this only for perform update:  oReviewList contains a list of affiliates
            if(applyUpdate && !affiliateError){                
                logger.debug("Apply Update flag is true");                
                boolean found = false;
                //The below logic attempts to add current affiliate read from the file to the review list
                //it will look if the affiliate is allready there the logic will not try to add dups
                if(oReviewList.isEmpty()){//empty arrayList                        
                    oRData = new OfficerReviewData();
                    oRData.setAffPk(currAffPk);
                    oReviewList.add(oRData);                        
                }else{//have allready populated the arraylist so look into it for the affiliate                        
                    Iterator it = oReviewList.iterator();
                    while(it.hasNext()){
                        OfficerReviewData reviewData =  (OfficerReviewData) it.next();
                        if(reviewData.getAffPk() != null && reviewData.getAffPk().intValue() == currAffPk.intValue()){                                
                            found = true;
                        }
                    }
                    if(!found){//affiliate not there
                        logger.debug("didn't find " + currAffPk  + " in oReviewList therefore adding to it ");
                        oRData                    = new OfficerReviewData();
                        oRData.setAffPk(currAffPk);
                        oReviewList.add(oRData);
                    }
                }                              
            }
            
            
            //------------------------------------------------------------------
            // Initialize again
            officer     =   new OfficerUpdateElement();
            //topAffId    =   new AffiliateIdentifier();
            posAffId    =   new AffiliateIdentifier();
            homAffId    =   new AffiliateIdentifier();
            address     =   new AddressElement();
            oPhone      =   new PhoneData();
            
            logger.debug("End Element method existed.");                        
        }//end of if(qName.equalsIgnoreCase(E_OFFICER)){              
    }

    
    /***************************************************************************
     **************************************************************************/
    public void endDocument()  {
        logger.debug("End of Document method entered.");
        
//        if (this.checkForFileError() || !this.posAffPkExist)        
          if (this.checkForFileError())        
            return;               
        
        if(!applyUpdate){                         
            if (this.posAffPkExist){
                setAffiliateSystemCount();
                getOfficerCardRun();
                //getPreOfficeDetails();                       
                //The following code designate affiliate as an error if this affiliate is valid but
                // has only one officer and the offficer is invalid
                Iterator itr = (Iterator)mapOffChanges.keySet().iterator();                
                while (itr.hasNext()){                  
                    Object ele = itr.next();
                    if (!(ele.getClass().getName().equals("org.afscme.enterprise.affiliate.AffiliateIdentifier"))){
                        Integer key = (Integer)ele;
                        if (!officersSortByAffiliate.containsKey(key)){
                            OfficerChanges offChanges = (OfficerChanges)mapOffChanges.get(key);
                            if (!offChanges.hasError) 
                                offChanges.setHasError(true);
                        }
                    }                                        
                }                      
            }            
            
            logger.debug("setting mapOffChanges in OfficerPreUpdateSummary object. Size = " + mapOffChanges.size());            
            oPreUpdSmry.setOfficerCounts    (   mapOffChanges   );            
            logger.debug("setting fieldChanges in OfficerPreUpdateSummary object. Size = " + fieldChanges.size());
            oPreUpdSmry.setFieldChanges     (   fieldChanges    );
            logger.debug("setting mapPosChanges in OfficerPreUpdateSummary object. Size = " + mapPosChanges.size());            
            oPreUpdSmry.setPositionChanges  (   mapPosChanges   );                       
            if (oPreUpdSmry.getExceptions() != null)
            logger.debug("OfficerPreUpdateSummary object. Exception Size = " + oPreUpdSmry.getExceptions().size());            

        }else{
            
            //This section of the processor deals with populating the review summary with reviewlist compiled
            //for each affiliate and also get the exception for each officer
            logger.debug("processing OfficerReviewSummary()");
            if(oReviewSmry == null){
                oReviewSmry = new OfficerReviewSummary();
            }
            
            logger.debug("the size of the list is " + oReviewList.size());
            
            if(!oReviewList.isEmpty()){
                Iterator itr = (Iterator)mapOffChanges.keySet().iterator();                
                while (itr.hasNext()){                  
                    Object ele = itr.next();
                    if (!(ele.getClass().getName().equals("org.afscme.enterprise.affiliate.AffiliateIdentifier"))){
                        Integer key = (Integer)ele;
                        if (!officersSortByAffiliate.containsKey(key)){                      
                            int size = oReviewList.size();
                            for (int c = 0; c < size; c++)
                            {
                                OfficerReviewData reviewData =  (OfficerReviewData) oReviewList.get(c);
                                if(reviewData.getAffPk() != null && reviewData.getAffPk().intValue() == key.intValue()){                                
                                    oReviewList.remove(c);
                                    logger.debug("the size of the list is " + oReviewList.size());                                    
                                    break;
                                }                            
                            }                   
                            
                            int size2 = excepData.size();
                            for (int c = 0; c < size2; c++)
                            {
                                ExceptionData excData =  (ExceptionData) excepData.get(c);
                                if(excData.getAffPk() != null && excData.getAffPk().intValue() == key.intValue()){                                
                                    excepData.remove(c);
                                    logger.debug("the size of the list is " + excepData.size());                                    
                                    break;
                                }                            
                            }                                               
                        }
                    }                                        
                }                                  
            }
            
            if(!oReviewList.isEmpty()){
                logger.debug("the size of the oReviewList = " + oReviewList.size());                 
                OfficerReviewData [] officerReview = (OfficerReviewData [] ) oReviewList.toArray(new OfficerReviewData[oReviewList.size()]);
                if(oReviewSmry != null){
                    oReviewSmry.setOfficerReviewData   ( officerReview );
                    logger.debug("done adding to the review summary review data ===========>" ); 
                }
            }
            logger.debug("adding to the review summary exception  data ===========>"  );
            if(!excepData.isEmpty()){
                oReviewSmry.setExceptionResult     ( (ExceptionData []     )       excepData.toArray(new ExceptionData[excepData.size()] ));
                logger.debug("done adding to the review summary exception  data ===========>"+ excepData );
            }
        }

        //rule 6: if the update type is full then check for missing affiliates 
        //and add them to the report
        //This affiliate error rule is implemented here as we want to make sure we check for 
        //missing affiliates only after processing the complete document 
        if (fileEntry.getUpdateType() == UpdateType.FULL ) {
            getAffiliatesSubLocals(fileEntry.getAffPk());
            addMissingAffiliates();
            if(!oReviewList.isEmpty()){
                logger.debug("the size of the oReviewList = " + oReviewList.size());                 
                OfficerReviewData [] officerReview = (OfficerReviewData [] ) oReviewList.toArray(new OfficerReviewData[oReviewList.size()]);
                if(oReviewSmry != null){
                    oReviewSmry.setOfficerReviewData   ( officerReview );
                    logger.debug("done adding to the review summary review data ===========>" ); 
                }
            }            
        }
        logger.debug("End of Document method existed");
        
    }//end of endDocument method 
    
    
    
    
    /***************************************************************************       
    // This method will check for file error
    /***************************************************************************/    
    private boolean checkForFileError(){
        logger.debug("Check For File Error method is entered.");
        //check the affiliate if it is in enterprise db otherwise it is an unknown affiliate
        //check if the reporting affiliate exists in enterprise database        
        // The entire file should not be missing the affiliate identifier
        // and affiliateID should be known.
        if (topAffId == null){
            logger.debug("Exception: topAffId is null.");            
            return true;
        }
        
        submittingAffPk = this.getAffPk(topAffId.getType(), topAffId.getLocal(), topAffId.getState(), topAffId.getSubUnit(), topAffId.getCouncil());
        if (submittingAffPk == null){
            logger.debug("Exception: submittingAffPk is null.");                        
            return true;        
        }
        
        // The affiliate identifier store in XML should match with affiliate identifier store in FileEntry        
        if (submittingAffPk.intValue() != fileEntry.getAffPk().intValue()){
            logger.debug("Exception: submittingAffPk entered from user input screen != pk in file.");                                    
            return true;
        }
        
        logger.debug("Check For File Error method is existed.");        
        return false;
        
    }
    
    /***************************************************************************       
    // This method will check for required fields specified in C&B document
    /***************************************************************************/

    private boolean checkForRequiredFields(){
        
        logger.debug("Check For Required Fields method entered.");        
        
        boolean failure = false;
       
        if (topAffId.getType() == null || topAffId.getType().toString().equals("0"))
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(AFF_AFFILIATE_TYPE, eDataGlobal);                                
            failure = true;
        }

        if (topAffId.getState() == null || topAffId.getState().length() == 0)
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(AFF_STATE_TYPE, eDataGlobal);                                
            failure = true;            
        }

        if ((topAffId.getLocal() == null || topAffId.getLocal().length() == 0) && (topAffId.getCouncil() == null || topAffId.getCouncil().length() == 0))
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(AFF_LOCAL_CHAPTER, eDataGlobal);                                                
            setErrorFlag(AFF_COUNCIL_CHAPTER, eDataGlobal);
            failure = true;            
        }

        if (posAffId.getType() == null || posAffId.getType().toString().equals("0"))
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(POS_AFFILIATE_TYPE, eDataGlobal);                                
            failure = true;            
        }

        if (posAffId.getState() == null || posAffId.getState().length() == 0)
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(POS_STATE_TYPE, eDataGlobal);                                
            failure = true;            
        }

        if ((posAffId.getLocal() == null || posAffId.getLocal().length() == 0) && (posAffId.getCouncil() == null || posAffId.getCouncil().length() == 0))
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(POS_LOCAL_CHAPTER, eDataGlobal);                                                
            setErrorFlag(POS_COUNCIL_CHAPTER, eDataGlobal);
            failure = true;            
        }

        if (officer.getAfscmeMemberNumber() == null || officer.getAfscmeMemberNumber().intValue() == 0)
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(A_MNUM, eDataGlobal);                
            failure = true;            
        }

        if (officer.getTitle() == null || officer.getTitle().intValue() == 0)
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(A_TITLE, eDataGlobal);                
            failure = true;            
        }

        if (officer.getTermExpiration() == null || officer.getTermExpiration().length() == 0)
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(A_TEXPIRE, eDataGlobal);                
            failure = true;            
        }

        if (officer.getLastName() == null || officer.getLastName().length() == 0)
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(A_LNAME, eDataGlobal);                
            failure = true;            
        }

        if (officer.getFirstName() == null || officer.getFirstName().length() == 0)
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(A_FNAME, eDataGlobal);
            failure = true;            
        }

        if (officer.getTransactionType() == null || officer.getTransactionType().length() == 0)
        {
            setAllOfficerFields(eDataGlobal);
            setErrorFlag(A_TRANSTYPE, eDataGlobal);                
            failure = true;            
        }            
        
        eDataGlobal.setUpdateErrorCodePk(new Integer(79007));
        logger.debug("Check For Required Fields method existed.");                
        return failure;
    }// end of checkForRequiredFields(){

    
    //****************************************************************************************
    //This method is called for applying business rules to the data read from the document
    // should mark the affiliate has error; display red on UI and not included in Update process                                
    //****************************************************************************************
    private boolean applyAffiliateRules(){               
        logger.debug("Apply Affiliate Rules method entered.");        
            
        // Each officer's affiliate id must be part of the hierarchy under 
        // the affiliate the file was imported for
        
        if (currAffPk != null){
            boolean valid = false;
            getAffiliatesSubLocals(submittingAffPk);            
            if(subAffiliates != null){            
                Iterator it =  subAffiliates.iterator();            
                while(it.hasNext()){
                    Integer affPk = (Integer) it.next();
                    if (affPk.intValue() == currAffPk.intValue())
                    {
                        valid = true;
                        break;
                    }
                }
            }
            if (valid == false){
                logger.debug("The affiliate is not part of hierarchy.");                        
                oChanges.setHasError(true);                    
                setAllOfficerFields(eDataGlobal);                
                setErrorFlag(POS_LOCAL_CHAPTER, eDataGlobal);                                                
                setErrorFlag(POS_STATE_TYPE, eDataGlobal);                                                
                setErrorFlag(POS_COUNCIL_CHAPTER, eDataGlobal);
                eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                
                return true;                
            }
        }

        
        // Check the reporting affiliate sub locals flag if "false" then it cannot report sublocals
        if (submittingAffPk.intValue() != currAffPk.intValue()){           
            if(!maintainAffiliates.getAffiliateAllowedSublocal(submittingAffPk)){     
                logger.debug("The submitting do not allow sublocals.");                                        
                oChanges.setHasError(true);  
                setAllOfficerFields(eDataGlobal);              
                setErrorFlag(POS_LOCAL_CHAPTER, eDataGlobal);                                                
                setErrorFlag(POS_STATE_TYPE, eDataGlobal);                                                
                setErrorFlag(POS_COUNCIL_CHAPTER, eDataGlobal);                
                eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                
                return true;                
            }           
        }
        
        // Check if affiliate that the officer is associated with has status "merged" then it cannot have officers
        // Check if affiliate that the officer is associated with has status "deactive" then it cannot have officers
        AffiliateData curAffData    =    maintainAffiliates.getAffiliateData(currAffPk);                            
        Integer affStatus    =   curAffData.getStatusCodePk();
        if (affStatus !=  null) {
           if ((affStatus.equals(Codes.AffiliateStatus.M)) || (affStatus.equals(Codes.AffiliateStatus.D))) {
                logger.debug("The affiliate has status of merged or deactive");        
                oChanges.setHasError(true);
                setAllOfficerFields(eDataGlobal);
                setErrorFlag(POS_LOCAL_CHAPTER, eDataGlobal);                                                
                setErrorFlag(POS_STATE_TYPE, eDataGlobal);                                                
                setErrorFlag(POS_COUNCIL_CHAPTER, eDataGlobal);                                
                eDataGlobal.setUpdateErrorCodePk(new Integer(79003));                
                return true;
            }                    
        }
        
        logger.debug("Apply Affiliate Rules method existed.");        
            
        return false;
    }
    
    
    
    /********************************************************************************************************************/
    //The following methods are for officer exception processing. Will apply the exception rules for pre and post processing
    /********************************************************************************************************************/
    private boolean checkForExceptions(){
        
        logger.debug("Checking For Exceptions entered." );
        
        boolean                 failure         =   false;       
        String                  ssn             =   "";
        int                     zeroCounter     =   0;
        Integer                 affPk           =   null;
        Integer                 memberPk        =   new Integer(0);        
        
        // An officer should either be a member of affiliate hierarchy or be an 
        // affiliate staff person or AFSCME staff person        
        if (officer.getAfscmeMemberNumber() != null && officer.getAfscmeMemberNumber().intValue() != 0){
                     
            getAffiliatesSubLocals(submittingAffPk);            
            logger.debug("Checking updateBean for type of staff/officer." );                
            memberPk = updateBean.getAffiliatePk(officer.getAfscmeMemberNumber());            
            boolean valid = false;

            if(subAffiliates != null && memberPk != null){            
                Iterator it =  subAffiliates.iterator();            
                while(it.hasNext()){
                    affPk = (Integer) it.next();
                    if (affPk.intValue() == memberPk.intValue())
                    {
                        valid = true;
                        break;
                    }
                }
            }            
                                   
            if(!valid){//not an affiliate member                                
                logger.debug("The officer is not an affiliate member." );                
                //check for affiliate staff
                memberPk = updateBean.getStaffPk(officer.getAfscmeMemberNumber());
                if(subAffiliates != null && memberPk != null){            
                    Iterator it =  subAffiliates.iterator();            
                    while(it.hasNext()){
                        affPk = (Integer) it.next();
                        if (affPk.intValue() == memberPk.intValue())
                        {
                            valid = true;
                            break;
                        }
                    }
                }                            
                if(!valid){//not a staff member
                    logger.debug("The officer is not a staff member." );                                
                    //Check for AFSCME Staff; Check with lana                
                    if(updateBean.getAFSCMEStaffDept(officer.getAfscmeMemberNumber())  == null){//not an AFSCME staff 
                        logger.debug("The officer is not an AFSCME staff." );                                    
                        //ask how to indicate on the ui
                        setAllOfficerFields(  eDataGlobal);
                        setErrorFlag(A_LNAME, eDataGlobal);          
                        eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                        
                        failure = true;                        
                    }else{
                        afscmeStaff =   true;
                    }
                 }else{
                    affStaff    =   true;
                }
            }
        }
/*
        //If the officer is not an ADMINISTRATOR, EXECUTIVE_DIRECTOR, FINANCIAL_REPORTING_OFFICER,
        //afscmeStaff or an affiliate staff then the home affiliate local number should be present        
        if(!afscmeStaff || !affStaff 
                        || (officer.getTitle().intValue() != OfficerTitleCode.ADMINISTRATOR.intValue()) 
                        || (officer.getTitle().intValue() != OfficerTitleCode.EXECUTIVE_DIRECTOR.intValue()) 
                        || (officer.getTitle().intValue() != OfficerTitleCode.FINANCIAL_REPORTING_OFFICER.intValue())) {
            if( (homAffId.getLocal().equals(""))){
                logger.debug("If the officer is not an ADMINISTRATOR, EXECUTIVE_DIRECTOR, FINANCIAL_REPORTING_OFFICER ");
                logger.debug("afscmeStaff or an affiliate staff then the home affiliate local number should be present. ");
                logger.debug("The local number is NOT present : " + homAffId.getLocal() );
                setAllOfficerFields ( eDataGlobal         );
                setErrorFlag        (A_LOCAL, eDataGlobal );                
                failure = true;                
            }
        }        
*/        
        //An officer title should be present in the enterprise system            
        if (officer.getTitle() != null && officer.getTitle().toString().length() > 0){
            if(updateBean.officerTitleExists(officer.getTitle()) == null){
                logger.debug("Exception: officer Title is not present in enterprise system. " + officer.getTitle());                
                setAllOfficerFields ( eDataGlobal           );
                setErrorFlag        ( A_TITLE, eDataGlobal  );
                eDataGlobal.setUpdateErrorCodePk(new Integer(79008));
                failure = true;                
            }
        }            
            
        // position reported in the file must have the terms that match the terms in the system
        if (officer.getTermExpiration() != null && officer.getTermExpiration().length() > 0 && 
            officer.getTitle() != null && officer.getTitle().intValue() != 0){                
            String titleDesc = null;
            StringTokenizer tok = new StringTokenizer(officer.getTermExpiration(), "-");
            int year         = new Integer(tok.nextToken()).intValue();
            int month        = new Integer(tok.nextToken()).intValue();
            titleDesc = updateBean.getTitleDesc(currAffPk, year, officer.getTitle().intValue(), month);                    
            if (titleDesc == null || titleDesc.length() == 0){
                logger.debug("Exception: the affiliate does not support the position(office) reported.");                                
                setAllOfficerFields ( eDataGlobal           );
                setErrorFlag        ( A_TITLE, eDataGlobal  );
                setErrorFlag        (A_TEXPIRE,eDataGlobal);
                eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                
                failure = true;                                
            }else{
/*                

*/                                
            }                      
        }

        // An officer should not have SSN as all 0's
        if((ssn = officer.getSsn()) != null){
            for(int i = 0 ; i < ssn.length() ; i++){
                if (ssn.charAt(i) == '0'){
                    zeroCounter++;
                }
            }
            if(ssn.length() == zeroCounter){
                logger.debug("Exception: officer SSN is NOT valid." + officer.getSsn());
                setAllOfficerFields (eDataGlobal          );
                setErrorFlag        (A_SSN, eDataGlobal   );
                eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                
                failure = true;                
            }
        }
                      
        logger.debug("Checking For Exceptions existed." );        
        return failure;                   
    }//end of checkForExceptions
    
    
    private boolean checkForAddException(){
        logger.debug("Check For Add Exception method is entered.");

        int                     inSystemOfficerCount = 0;        
        Integer                 offPk           =   null;
        Integer                 grpId           =   null;
        Integer                 maxOffAllowed   =   null;
        Integer                 persPk          =   null;
        boolean                 failure         =   false;
        List                    officeList      =   new ArrayList();
        String                    posEndDt        =   null;
        
        StringTokenizer tok = new StringTokenizer(officer.getTermExpiration(), "-");
        int year         = new Integer(tok.nextToken()).intValue();
        int month        = new Integer(tok.nextToken()).intValue();

        
        Integer pk = new Integer(0);
        if (officer.getAfscmeMemberNumber() != null){
            pk = officer.getAfscmeMemberNumber();
            
            boolean checkAgainstDB = true;
            
            if (officersSortByAffiliate != null && !officersSortByAffiliate.isEmpty()){
                if (officersSortByAffiliate.containsKey(currAffPk)){            
                    List list = (List)officersSortByAffiliate.get(currAffPk);
                    Iterator itr = list.iterator();
                    while (itr.hasNext()){
                        OfficerUpdateElement off = (OfficerUpdateElement)itr.next();
                        if (officer.getAfscmeMemberNumber().intValue() == off.getAfscmeMemberNumber().intValue()){
                            String transType = off.getTransactionType();
                            if (transType.equalsIgnoreCase("Remove") || transType.equalsIgnoreCase("Vacate")){
                                checkAgainstDB = false;
                                logger.debug("There exist a transaction that is remove");
                                break;
                            }
                        }                       
                    }                    
                }
            }
               
            if (checkAgainstDB){

                HashMap map = (HashMap) updateBean.getOffCurrentPositionDetails(currAffPk, officer.getAfscmeMemberNumber());        
                if (map != null && map.size() > 0){            
                    stFlag          =   ((Boolean) map.get(new Integer(1))).booleanValue()   ;         
                    //An officer cannot hold more than one position in a given affiliate except steward
                    if(!(stFlag) ){
                        logger.debug("Exception: An officer cannot hold two positions with the same affiliate." );
                        setAllOfficerFields ( eDataGlobal         );
                        setErrorFlag        (A_TITLE, eDataGlobal );
                        eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                    
                        return true;                    
                    }
                }                                                    
            }
        }
        
        // The code below is to verify offiers in file instead of DB.
        // The following query will return you the number of officers currently in the office.
        List list  =   (List) updateBean.getOfficersInOffice(currAffPk, month, year, officer.getTitle());
                            
        if( list != null && !list.isEmpty()){
            Iterator itr = list.iterator();
            while (itr.hasNext()){
            //if (itr.hasNext()){
                HashMap posDtl= (HashMap)itr.next();
                stFlag          =   ((Boolean) posDtl.get(new Integer(1))).booleanValue()   ; 
                posCode         =   ((Integer) posDtl.get(new Integer(2)))                  ; 
                posDesc         =   ((String)  posDtl.get(new Integer(3)))                  ;
                yearExp         =   ((Integer) posDtl.get(new Integer(4))).intValue()       ; 
                mElected        =   ((Integer) posDtl.get(new Integer(5))).intValue()       ; 
                maxOffAllowed   =   ((Integer) posDtl.get(new Integer(6)))                  ;
                offPk           =   ((Integer) posDtl.get(new Integer(7)))                  ;
                grpId           =   ((Integer) posDtl.get(new Integer(8)))                  ;
                persPk          =   ((Integer) posDtl.get(new Integer(9)))                  ;
                posEndDt          =   ((String) posDtl.get(new Integer(10)))                  ;                
/*                
                if (persPk.intValue() == 0 || (posEndDt != null && posEndDt.length() > 0))
                    inSystemOfficerCount = 0;
                else
                    inSystemOfficerCount = list.size();
*/                    
                if (persPk.intValue() != 0 && (posEndDt.length() == 0))
                    inSystemOfficerCount = inSystemOfficerCount + 1;                
            }

        }

        // For each officer/officer element
        PositionChanges pChanges = new PositionChanges();
        pChanges.setAffPk(currAffPk);        
        pChanges.setOfficePk(offPk);
        pChanges.setGroupId(grpId);        
        pChanges.setPersonPk(pk);        
        pChanges.setAllowed(maxOffAllowed.intValue());
        pChanges.setInFile(1);
        pChanges.setInSystem(inSystemOfficerCount);
        boolean found = false;

        if (mapPosChanges.get(currAffPk) != null)
        {
            logger.debug("mapPosChanges already has currAffPk ");                            
            officeList = (List)mapPosChanges.get(currAffPk);
            Iterator itr = (Iterator)officeList.iterator();                        
            while(itr.hasNext())
            {
                PositionChanges pChngs = (PositionChanges)itr.next();
                if (pChngs.getPersonPk().intValue() == pk.intValue()) // check against position recorded in XML
                {
                    logger.debug("Exception: An officer cannot hold two positions with the same affiliate." );
                    setAllOfficerFields (   eDataGlobal       );
                    setErrorFlag        (A_TITLE, eDataGlobal );
                    eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                    
                    return true;                    
                }
                
                Integer id = pChngs.getGroupId();
                logger.debug("group id in PositionChanges object " + id);
                logger.debug("group id in try to add " + grpId);                
                if (grpId.intValue() == id.intValue())
                {
                    found = true;                                
                    logger.debug("the office is already being accounted in currAffPk ");                                                
                    logger.debug("you can add multiple person to an office");
                    logger.debug("alllowed amount is " + pChngs.getAllowed());
                    logger.debug("in system officer  " + inSystemOfficerCount);
                    logger.debug("removevacate  " + pChngs.getRemoveVacate());
                    logger.debug("in file  " + pChngs.getInFile());                    
                    
                    int fileCount = pChngs.getInFile() + 1;
                    if (pChngs.getAllowed() >= (fileCount + inSystemOfficerCount - pChngs.getRemoveVacate())) // check againist the officers added to file and system
                    {
                        pChngs.setInFile(fileCount);
                        pChngs.setInSystem(inSystemOfficerCount);

                    }else{                                    
                        logger.debug("Exception: Can not add officers that exceed the allowed amount.");
                        setAllOfficerFields (   eDataGlobal       );
                        setErrorFlag        (A_TITLE, eDataGlobal );
                        eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                    
                        return true;                    
                    }
                }                            
            }// end of while(itr.hasNext())
            
            if (!found)
            {
                if (maxOffAllowed.intValue() >= inSystemOfficerCount + 1){                                
                    logger.debug("a new office for the same affiliate ");                                                    
                    officeList.add(pChanges);
                }else{
                    logger.debug("Exception: Can not add officers that exceed the allowed amount.");
                    setAllOfficerFields (   eDataGlobal       );
                    setErrorFlag        (A_TITLE, eDataGlobal );
                    eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                    
                    return true;                                                        
                }
            }                        
        }else{
            logger.debug("mapPosChanges does have aff pk yet; new affiliate ");                        
            if (maxOffAllowed.intValue() < inSystemOfficerCount + 1)
            {
                logger.debug("Exception: Can not add officers that exceed the allowed amount.");
                setAllOfficerFields (   eDataGlobal       );
                setErrorFlag        (A_TITLE, eDataGlobal );
                eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                    
                return true;                                                    
            }else{
                officeList = new ArrayList();
                officeList.add(pChanges);
                mapPosChanges.put(currAffPk, officeList);
            }
        }        
        
        logger.debug("Check For Add Exception method is existed.");        
        return false;        
    }
    
    
    private boolean checkForRenewRemoveVacateException(){
        logger.debug("Check For Renew Remove Vacate Exception method is entered.");                
        
        boolean found = false;
        
        StringTokenizer tok = new StringTokenizer(officer.getTermExpiration(), "-");
        int year         = new Integer(tok.nextToken()).intValue();
        int month        = new Integer(tok.nextToken()).intValue();
        
        HashMap map = (HashMap) updateBean.getOffCurrentPositionDetails(currAffPk, officer.getAfscmeMemberNumber());        
        if (map == null || map.isEmpty()){                        
            logger.debug("Exception: The officer must be an valid officer in affiliate stored in DB." );
            setAllOfficerFields ( eDataGlobal         );
            setErrorFlag        (A_TITLE, eDataGlobal );
            eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                    
            return true;                    
        }                

        if (!officer.getTransactionType().equalsIgnoreCase("Renew")){
            
            Integer grpId = ((Integer)map.get(new Integer(8)));
            Integer offPk = ((Integer)map.get(new Integer(7)));
            Integer maxOffAllowed = ((Integer)map.get(new Integer(6)));

            // For each officer/officer element
            PositionChanges pChanges = new PositionChanges();
            pChanges.setAffPk(currAffPk);        
            pChanges.setOfficePk(offPk);
            pChanges.setGroupId(grpId);        
            pChanges.setPersonPk(officer.getAfscmeMemberNumber());        
            pChanges.setAllowed(maxOffAllowed.intValue());
            pChanges.setInFile(0);
            //pChanges.setInSystem(inSystemOfficerCount);
            pChanges.setRemoveVacate(pChanges.getRemoveVacate() + 1);        

            if (mapPosChanges.get(currAffPk) != null)
            {
                officeList = (List)mapPosChanges.get(currAffPk);
                Iterator itr = (Iterator)officeList.iterator();                        
                while(itr.hasNext())
                {
                    PositionChanges pChngs = (PositionChanges)itr.next();        
                    if (grpId.intValue() == pChngs.getGroupId().intValue())
                    {
                        pChngs.setRemoveVacate(pChngs.getRemoveVacate() + 1);
                        found = true;
                        break;
                    }                              
                }

                if (!found)
                {
                    officeList.add(pChanges);
                }                                    
            }else{
                logger.debug("mapPosChanges does have aff pk yet; new affiliate ");                        
                officeList = new ArrayList();
                officeList.add(pChanges);
                mapPosChanges.put(currAffPk, officeList);
            }
        }
        logger.debug("Check For Renew Remove Vacate Exception method is existed.");                        
        return false;        
    }
    
    //**************************************************************************
    // This method will obtain aff_pk base on affiliate identifier given 
    // affiliate identifier could be submitting affiliate identifier,
    // position aff and home aff
    // from aff_org table.  
    //**************************************************************************
    private Integer getAffPk(Character type, String local, String state, String subUnit, String council){
        logger.debug("Get Aff Pk method entered.");
        Integer pk = null;
        
        if ((type == null || state == null) || (type.toString().length() == 0 || state.length() == 0))
            return null;        
        if ((local == null || local.length() == 0) && (council == null || council.length() == 0))
            return null;            
        
        pk = updateBean.getAffPkFromAffOrg(type, local, state, subUnit, council);
        logger.debug("Get Aff Pk method: Pk = " + pk);                
        logger.debug("Get Aff Pk method existed.");                
        return pk;
    }

    //THIS METHOD IS NOT BEING USED
    private void getOfficersForAffiliate(){
        logger.debug("Get Officers For Affiliate method is entered.");        
        
        if(allAffOfficers.isEmpty()){
            logger.debug("affOfficers is empty so getting the data for ============>" + currAffPk);
            affOfficers     =   (HashMap) updateBean.getAffOfficers(currAffPk);
            allAffOfficers.put(currAffPk, affOfficers);
        }else{
            logger.debug("affOfficers is not empty so getting the Iterator ============>" );
            if(!allAffOfficers.containsKey(currAffPk)){
                affOfficers     =  (HashMap) updateBean.getAffOfficers(currAffPk);
                allAffOfficers.put(currAffPk, affOfficers);                    
            }
        }
        logger.debug("Get Officers For Affiliate method is existed.");        
    }
   
    
    //**************************************************************************
    //This method will get the officer count for an affiliate and will set the count in officerChanges 
    //**************************************************************************    
    private void setAffiliateSystemCount(){
        logger.debug("Set Affiliate System Count method entered.");        
        Iterator    it      =   mapOffChanges.values().iterator();
            while(it.hasNext()){//loop through the map 
                OfficerChanges oChange             = (OfficerChanges) it.next();
                oChange.setInSystem((updateBean.getOfficerCount(oChange.getAffPk())));
            }
        logger.debug("Set Affiliate System Count method existed.");                
    }//end of setAffiliateSystemCount method
    
    private void setAffiliateInFileCount(Integer personPk){
        logger.debug("Set Affiliate In File Count method entered.");        
        List list = new ArrayList();
        boolean found = false;
        int count = 0;
        if (personPk == null || personPk.intValue() == 0){
            logger.debug("Person pk for this officer do not existed.");
            if (personPkDummyCounter.size() > 0 && personPkDummyCounter.get(currAffPk) != null)                
            {
                count = ((Integer)personPkDummyCounter.get(currAffPk)).intValue();
                personPk = new Integer(++count);
                
            }else{
                personPkDummyCounter.put(currAffPk, new Integer(count));               
                personPk = new Integer(count);
            }                        
        }
        
        list = (List)mapInFileCount.get(currAffPk);
        if (list == null || list.isEmpty()){
            logger.debug("The affiliate does not have a in file count yet.");                    
            list = new ArrayList();
            list.add(personPk);            
            mapInFileCount.put(currAffPk, list);
        }else{
            Iterator itr = list.iterator();
            logger.debug("Size of affiliate is = " + list.size());            
            while (itr.hasNext())
            {
                Integer pk = (Integer)itr.next();
                logger.debug("pk in file = " + pk);                            
                
                if (pk.intValue() == personPk.intValue()){
                    logger.debug("The affiliate has already count this officer.");                                        
                    found = true;
                    break;
                }
            }
            if (!found)
                list.add(personPk);
        }

        OfficerChanges oChange = (OfficerChanges)mapOffChanges.get(currAffPk);
        oChange.setInFile(((List)mapInFileCount.get(currAffPk)).size());
        
        logger.debug("Set Affiliate In File Count method existed.");                
    }//end of setAffiliateSystemCount method 
    
    //****************************************************************************************************
    //This method will obtain officer changes statistics each time the SAXParase process a officer   
    //****************************************************************************************************                
    private void recordOfficerChanges()
    {        
        logger.debug("Record Officer Changes method is entered.");

        oChanges.incrementInFile();                        
        incrementTransType();
        
        logger.debug("Record Officer Changes method:the size of mapOffChanges suppose be = 1, and the actual is = " + mapOffChanges.size());
        logger.debug("Record Officer Changes method is existed.");        
    }    

    // This method is not being called    
    private void recordOfficerVacated()
    {       
        logger.debug("Record Officer Vacated method is entered.");
        
        if (allAffOfficers.isEmpty()){
            logger.debug("The allAffOfficers hashmap is empty");
            return;
        }

        HashMap allAffOfficersInDB = (HashMap)allAffOfficers.get(currAffPk);            

        //HashMap allAffOfficersInDB     =   (HashMap) updateBean.getAffOfficers(currAffPk);                                    
        
        if (allAffOfficersInDB.size() == 0){
            logger.debug("affOfficers is empty.");            
            return;            
        }
         
        if (officersSortByAffiliate.size() == 0){
            logger.debug("offData is empty.");            
            return;
        }
            
        List list = (List)officersSortByAffiliate.get(currAffPk);
        
        Iterator it = list.iterator();

        while(it.hasNext()){
            OfficerUpdateElement currOfficer    =   (OfficerUpdateElement) it.next();
            matchOfficer(currOfficer, allAffOfficersInDB);  
        }
        
        oChanges.setVacant(allAffOfficersInDB.size());  
        logger.debug("Record Officer Vacated method is existed.");        
    }
    // This method is not being called
    private boolean matchOfficer(OfficerUpdateElement off, HashMap allAffOfficersInDB){
        logger.debug("Match Officer (OfficerUpdateElement) is entered.");        
        // affiliate officers in the database      
        
            Iterator it     =   allAffOfficersInDB.values().iterator();
            while(it.hasNext()){
                HashMap map =   (HashMap) it.next();
                Integer pPk         =  (Integer) map.get(new Integer(1));
                Integer affPk       =  (Integer) map.get(new Integer(2));
                String ssn          =  (String)  map.get(new Integer(3));
                String firstName    =  (String)  map.get(new Integer(4));
                String lastName     =  (String)  map.get(new Integer(5));
                logger.debug("pPk ============>"        + pPk);
                logger.debug("affPk ============>"      + affPk);
                logger.debug("ssn ============>"        + ssn);
                logger.debug("firstName ============>"  + firstName);
                logger.debug("lastName ============>"   + lastName);

                if((off.getAfscmeMemberNumber() != null) && (pPk.intValue() == off.getAfscmeMemberNumber().intValue())){
                    logger.debug("personPk matches for person ============>" + off.getFirstName());
                    it.remove();
                    return true;
                }else if((off.getAffiliateMemberNumber() != null) &&                      
                         (off.getFirstName() != null && off.getFirstName().length() > 0 ) && 
                         (off.getLastName() != null && off.getLastName().length() > 0) &&            
                         (affPk.intValue() == new Integer(off.getAffiliateMemberNumber()).intValue())  
                            && (off.getFirstName().equals(  firstName   )) 
                            && (off.getLastName().equals(   lastName    ))){
                    logger.debug("affpk  matches for posAffPk ============>" + off.getFirstName());
                    it.remove();                    
                    return true;
                }else if((off.getSsn() != null && off.getSsn().length() > 0) && 
                         (off.getFirstName() != null && off.getFirstName().length() > 0 ) && 
                         (off.getLastName() != null && off.getLastName().length() > 0) &&
                         (off.getSsn().equals(ssn)) && (off.getFirstName().equals(  firstName   )) 
                                                        && (off.getLastName().equals(   lastName    ))){
                    logger.debug("affpk  matches for posAffPk ============>" + off.getFirstName());
                    it.remove();                                        
                    return true;
                }
            }            

        logger.debug("Match Officer (OfficerUpdateElement) is existed.");                
        return false;
    }    
    
    
    //**************************************************************************
    //helper method returns the officer change associated with the affiliate
    //**************************************************************************    
    private OfficerChanges getOfficerChanges(Integer newAffPk){
        logger.debug("Get Officer Changes method entered.");
        if(newAffPk != null){
            Iterator    it      =   mapOffChanges.values().iterator();
            while(it.hasNext()){//search the map if exists in the map                
                OfficerChanges curOffChange             = (OfficerChanges) it.next();
                logger.debug("curOffChange = " + curOffChange);
                if(curOffChange.getAffPk() != null && curOffChange.getAffPk().intValue()   == newAffPk.intValue()){
                    return curOffChange;
                }
            }//end of while
        }
        logger.debug("Get Officer Changes method existed.");        
        return null;             
    }//end of getOfficerChangesAff
    
    
    
    //***************************************************************************
    //Helper method to set all officers of the affiliate as having error     
    //***************************************************************************    
    private void setAllOfficerChangesAsError(){
        logger.debug("Set All Officer Changes As Error method is entered.");
        if((mapOffChanges != null) && !(mapOffChanges.isEmpty())){
            Iterator    it      =   mapOffChanges.values().iterator();
            while(it.hasNext()){//search the map if exists in the map
                OfficerChanges curOffChange             = (OfficerChanges) it.next();
                curOffChange.setHasError(true);
                logger.debug("setting the officer " + curOffChange + " as having errors.");                
            }
        }                      
        logger.debug("Set All Officer Changes As Error method is existed.");
    }//end of getOfficerChangesAff
    
    
    
    //**************************************************************************
    //Helper method to set the transaction type of the current officer
    //**************************************************************************    
    private void incrementTransType(){
        logger.debug("Increment Trans Type method entered.");
        try{
            if((oChanges != null) && (officer != null)){
                logger.debug("Incrementing transaction type : " + officer.getTransactionType());
                if(officer.getTransactionType().equalsIgnoreCase(ADDTRANS)){
                    oChanges.incrementAdded();
                }else if(officer.getTransactionType().equalsIgnoreCase(REMTRANS)){
                    oChanges.incrementReplaced();
                }else if(officer.getTransactionType().equalsIgnoreCase(RENTRANS)){
                    oChanges.incrementChanged();
                }else if(officer.getTransactionType().equalsIgnoreCase(VACTRANS)){
                    oChanges.incrementVacant();
                }
            }

        }catch(Exception e){
            logger.debug("exception=>"+ e.getMessage());
        }
        logger.debug("Increment Trans Type method existed.");        
    }//end of incrementTransType method
    
    
   
    /******************************************************************************************************/
    /*
     *if there is an exception this method will delegate the setting of all the exceptioncomparision object
     *for all the fields to setAllECompFields() only the first time it is called.
     *utill the exception flag is set back again
     *@param boolean exceptionFlag
     *
     */
    private void setAllOfficerFields(ExceptionData eData){
        logger.debug("Set All Officer Fields method entered." );
        if(!exception){
            exception   =   true;
            setAllECompFields(eData);
        }
        logger.debug("Set All Officer Fields method existed." );        
    }//end of setAllOfficerFields
    
    
    
    //**********************************************************************************************
    //This method will mark the field name passed in as having an error. It will go through the list
    //and mark the field that matches as having error
    private void setErrorFlag(String fieldName, ExceptionData eData){
        logger.debug("Set Error Flag entered.");        
        //set this field to the error that being encountered after the transaction type is added
        //eData.setUpdateErrorCodePk(updateErrorCodePk);
        
        HashMap     eCompMap    =   (HashMap) eData.getFieldChangeDetails();               
        Iterator    it          =   eCompMap.values().iterator();        
        while(it.hasNext()){                       
            ArrayList    list                       =   (ArrayList) it.next();
            Iterator     listIt                     =   list.iterator();            
            while(listIt.hasNext()){
                ExceptionComparison    eComp        =   (ExceptionComparison) listIt.next();
                if(eComp.getField().equals(fieldName)){
                    logger.debug("Found exception field==========>" + fieldName + "  " + eComp.getField());
                    logger.debug("eComp.valueInFile=====>" + eComp.getValueInFile());
                    logger.debug("eComp.valueInFile=====>" + eComp.getValueInSystem());                    
                    eComp.setError(true);
                    break;
                }
            }
        }                
        logger.debug("Set Error Flag existed.");                
    }//end of setErrorFields
    
    
    
    //**********************************************************************************************************
    //This method will create exception 
    private void setAllECompFields(ExceptionData eData){
        logger.debug("Set All EComp Fields entered.");                
        
        String suffixFile = "";
        String prefixFile = "";
        
        if (officer.getSuffix().length() == 1){
            if (new Integer(officer.getSuffix()).intValue() == 1)
                suffixFile = "SR";
            else if (new Integer(officer.getSuffix()).intValue() == 2)
                suffixFile = "JR";
            else if (new Integer(officer.getSuffix()).intValue() == 3)
                suffixFile = "III";
            else if (new Integer(officer.getSuffix()).intValue() == 4)                
                suffixFile = "IV";
            else if (new Integer(officer.getSuffix()).intValue() == 5)
                suffixFile = "II";
            else if (new Integer(officer.getSuffix()).intValue() == 6)
                suffixFile = "ESQ";
            else
                suffixFile = "Unknown";                   
        }
                       
        if (officer.getPrefix().length() == 1){
            if (new Integer(officer.getPrefix()).intValue() == 1)
                prefixFile = "MR";
            else if (new Integer(officer.getPrefix()).intValue() == 2)
                prefixFile = "MS";
            else if (new Integer(officer.getPrefix()).intValue() == 3)
                prefixFile = "MRS";
            else if (new Integer(officer.getPrefix()).intValue() == 4)                
                prefixFile = "MISS";
            else if (new Integer(officer.getPrefix()).intValue() == 5)
                prefixFile = "REVEREND";
            else if (new Integer(officer.getPrefix()).intValue() == 6)
                prefixFile = "DOCTOR";
            else if (new Integer(officer.getPrefix()).intValue() == 7)
                prefixFile = "HONORABLE";                
            else
                prefixFile = "Unknown";                
        }                
        
        if(officer.getAfscmeMemberNumber() == null || officer.getAfscmeMemberNumber().intValue() == 0){
            logger.debug("Setting officer detail in eData object");        
            if(officer != null){
                eData.addDetails(OfficerUpdateFields.AFSCME_MEMBER_ID           ,    setFields(A_MNUM     ,   ""            ,     ""     ));
                eData.addDetails(OfficerUpdateFields.OFFICER_TITLE              ,    setFields(A_TITLE    ,   officer.getTitle().toString()                         ,     ""         ));
                eData.addDetails(OfficerUpdateFields.SSN                        ,    setFields(A_SSN      ,   officer.getSsn()                                      ,     ""            ));
                if (officer.getTermExpiration() != null && officer.getTermExpiration().length() > 0)
                eData.addDetails(OfficerUpdateFields.TERM_EXPIRATION            ,    setFields(A_TEXPIRE  ,   officer.getTermExpiration() + ""                      ,     ""    ));
                else
                eData.addDetails(OfficerUpdateFields.TERM_EXPIRATION            ,    setFields(A_TEXPIRE  ,   ""                                                    ,     ""    ));                
                eData.addDetails(OfficerUpdateFields.FIRST_NAME                 ,    setFields(A_FNAME    ,   officer.getFirstName()                                ,     ""        ));
                eData.addDetails(OfficerUpdateFields.LAST_NAME                  ,    setFields(A_LNAME    ,   officer.getLastName()                                 ,     ""         ));
                eData.addDetails(OfficerUpdateFields.MIDDLE_NAME                ,    setFields(A_MNAME    ,   officer.getMiddleName()                               ,     ""       ));
                eData.addDetails(OfficerUpdateFields.SUFFIX                     ,    setFields(A_SUFFIX   ,   suffixFile                                   ,     ""  ));
                eData.addDetails(OfficerUpdateFields.PREFIX                     ,    setFields(A_PREFIX   ,   prefixFile                                   ,     ""  ));
                eData.addDetails(OfficerUpdateFields.TRANSACTION_TYPE           ,    setFields(A_TRANSTYPE,   officer.getTransactionType()                          ,     ""));
            }else{
                logger.debug("Officer object is null.");                    
            }
            //***********************************************************************************************
            logger.debug("Setting position affiliate detail in eData object");

            if(posAffId != null){
                eData.addDetails(OfficerUpdateFields.POS_AFFILIATE_TYPE         ,    setFields(POS_AFFILIATE_TYPE     ,   posAffId.getType().toString()                      ,     ""));
                eData.addDetails(OfficerUpdateFields.POS_STATE_TYPE             ,    setFields(POS_STATE_TYPE         ,   posAffId.getState()                                ,     ""));
                eData.addDetails(OfficerUpdateFields.POS_COUNCIL_CHAPTER        ,    setFields(POS_COUNCIL_CHAPTER    ,   posAffId.getCouncil()                              ,     ""));
                eData.addDetails(OfficerUpdateFields.POS_LOCAL_CHAPTER          ,    setFields(POS_LOCAL_CHAPTER      ,   posAffId.getLocal()                                ,     ""));
                eData.addDetails(OfficerUpdateFields.POS_SUB_UNIT               ,    setFields(POS_SUB_UNIT           ,   posAffId.getSubUnit()                              ,     ""));
            }
            logger.debug("Setting home affiliate detail in eData object");        
            if(homAffId != null){
                eData.addDetails(OfficerUpdateFields.HOME_AFFILIATE_TYPE        ,    setFields(A_TYPE     ,   homAffId.getType().toString()                      ,     ""));
                eData.addDetails(OfficerUpdateFields.HOME_STATE_TYPE            ,    setFields(A_STATEN   ,   homAffId.getState()                                ,     ""));
                eData.addDetails(OfficerUpdateFields.HOME_COUNCIL_CHAPTER       ,    setFields(A_COUNCIL  ,   homAffId.getCouncil()                              ,     ""));
                eData.addDetails(OfficerUpdateFields.HOME_LOCAL_CHAPTER         ,    setFields(A_LCHAP    ,   homAffId.getLocal()                                ,     ""));
                eData.addDetails(OfficerUpdateFields.HOME_SUB_UNIT              ,    setFields(A_SUNIT    ,   homAffId.getSubUnit()                              ,     ""));
            }
/*
            logger.debug("Setting top affiliate detail in eData object");                
            if (topAffId != null){
                eData.addDetails(OfficerUpdateFields.AFF_AFFILIATE_TYPE        ,    setFields(AFF_AFFILIATE_TYPE     ,   topAffId.getType().toString()           ,     topAffId.getType()       !=null ? topAffId.getType().toString()      : ""));
                eData.addDetails(OfficerUpdateFields.AFF_STATE_TYPE            ,    setFields(AFF_STATE_TYPE         ,   topAffId.getState().toString()          ,     topAffId.getState()      !=null ? topAffId.getState().toString()     : ""));
                eData.addDetails(OfficerUpdateFields.AFF_COUNCIL_CHAPTER       ,    setFields(AFF_COUNCIL_CHAPTER    ,   topAffId.getCouncil().toString()        ,     topAffId.getCouncil()    !=null ? topAffId.getCouncil().toString()   : ""));
                eData.addDetails(OfficerUpdateFields.AFF_LOCAL_CHAPTER         ,    setFields(AFF_LOCAL_CHAPTER      ,   topAffId.getLocal().toString()          ,     topAffId.getLocal()      !=null ? topAffId.getLocal().toString()     : "")); 
                eData.addDetails(OfficerUpdateFields.AFF_SUB_UNIT              ,    setFields(AFF_SUB_UNIT           ,   topAffId.getSubUnit().toString()        ,     topAffId.getSubUnit()    !=null ? topAffId.getSubUnit().toString()   : ""));            
            }
*/
            logger.debug("Setting address detail in eData object");                
            if(address != null){
                eData.addDetails(OfficerUpdateFields.ADDR1                      ,    setFields(A_ADDRESS1   ,   address.getAddr1()              ,   ""            ));
                eData.addDetails(OfficerUpdateFields.ADDR2                      ,    setFields(A_ADDRESS2   ,   address.getAddr2()              ,   ""            ));
                eData.addDetails(OfficerUpdateFields.CITY                       ,    setFields(A_CITY       ,   address.getCity()               ,   ""            ));
                eData.addDetails(OfficerUpdateFields.STATE                      ,    setFields(A_STATE      ,   address.getState()              ,   ""            ));
                eData.addDetails(OfficerUpdateFields.ZIP                        ,    setFields(A_ZIP        ,   address.getZipCode()            ,   ""            ));

            }
            logger.debug("Setting phone detail in eData object");                
            if(oPhone != null){
                eData.addDetails(OfficerUpdateFields.PHONE_COUNTRY_CODE        ,    setFields(A_PHONECC     ,   oPhone.getCountryCode()         ,   ""         ));
                eData.addDetails(OfficerUpdateFields.PHONE_AREA_CODE           ,    setFields(A_PHONEAC     ,   oPhone.getAreaCode()            ,   ""         ));
                eData.addDetails(OfficerUpdateFields.PHONE_NUMBER              ,    setFields(A_PHONENUM    ,   oPhone.getPhoneNumber()         ,   ""         ));

            }
            logger.debug("Set All ECompFields existed.");                                            
            return;
        }// end of if(officer.getAfscmeMemberNumber().intValue() == 0)
        
        
        int                 counter         =     0;  
        boolean             titleExists     =     false;
        String titleDesc                    =     "";
        AffiliateData       affData         =     maintainAffiliates.getAffiliateData(currAffPk);         
        AffiliateIdentifier affId           =     affData.getAffiliateId();     
        AffiliateIdentifier homeAffId       =     null;
        person                              =     personBean.getPersonDetail(officer.getAfscmeMemberNumber(), null);        

        if(person !=null){
            Integer addPk                   =     person.getAddressPk();
            if(addPk != null){
                sAddress                    =     addressBean.getPersonAddress(addPk);                
            }else{
                sAddress                    =     new PersonAddressRecord();
            }            
            sPhone                          =     getPrimaryPhone(person);
            if(homePk != null){
                AffiliateData       homeAffData         =     maintainAffiliates.getAffiliateData(homePk);         
                                    homeAffId           =     affData.getAffiliateId();
            }else{
                                    homeAffId           =     new AffiliateIdentifier();
            }
        }
        
        // get title desc from db              
        if (officer.getTermExpiration() != null && officer.getTermExpiration().length() > 0 && officer.getTitle() != null && officer.getTitle().intValue() != 0){                
            StringTokenizer tok = new StringTokenizer(officer.getTermExpiration(), "-");
            int    year         = new Integer(tok.nextToken()).intValue();
            int    month        = new Integer(tok.nextToken()).intValue();
            titleDesc = updateBean.getTitleDesc(currAffPk, year, officer.getTitle().intValue(), month);                    
            if (titleDesc == null)
                titleDesc = officer.getTitle().toString();
        }
        
        HashMap posMap = updateBean.getPositionDetailsForAnOfficer(currAffPk, officer.getAfscmeMemberNumber());
        String titleSystem = "";
        String termExpSystem = "";
        if (posMap != null && !posMap.isEmpty()){
            titleSystem = posMap.get(new Integer(3)).toString();
            termExpSystem = posMap.get(new Integer(4)).toString() +  "-" + posMap.get(new Integer(5)).toString();

        }
        String suffixSystem = "";
        String prefixSystem = "";
        
        if (person.getSuffixNm() != null && person.getSuffixNm().intValue() != 0){            
            suffixSystem = codeBean.getCodeDescription(person.getSuffixNm());
        }
        
        if (person.getPrefixNm() != null && person.getPrefixNm().intValue() != 0){
            prefixSystem = codeBean.getCodeDescription(person.getPrefixNm());
        }

        
        //************************************************************************************************
        //set officer details

        logger.debug("Setting officer detail in eData object");        
        if(officer != null){
            eData.addDetails(OfficerUpdateFields.AFSCME_MEMBER_ID           ,    setFields(A_MNUM     ,   officer.getAfscmeMemberNumber().toString()            ,     person.getPersonPk().toString()     ));
            eData.addDetails(OfficerUpdateFields.OFFICER_TITLE              ,    setFields(A_TITLE    ,   officer.getTitle().toString()                         ,     titleSystem         ));
            eData.addDetails(OfficerUpdateFields.SSN                        ,    setFields(A_SSN      ,   officer.getSsn()                                      ,     person.getSsn()            ));
            if (officer.getTermExpiration() != null && officer.getTermExpiration().length() > 0)
            eData.addDetails(OfficerUpdateFields.TERM_EXPIRATION            ,    setFields(A_TEXPIRE  ,   officer.getTermExpiration() + ""                      ,     termExpSystem + ""    ));
            else
            eData.addDetails(OfficerUpdateFields.TERM_EXPIRATION            ,    setFields(A_TEXPIRE  ,   ""                                                    ,     termExpSystem + ""    ));                
            eData.addDetails(OfficerUpdateFields.FIRST_NAME                 ,    setFields(A_FNAME    ,   officer.getFirstName()                                ,     person.getFirstNm()        ));
            eData.addDetails(OfficerUpdateFields.LAST_NAME                  ,    setFields(A_LNAME    ,   officer.getLastName()                                 ,     person.getLastNm()         ));
            eData.addDetails(OfficerUpdateFields.MIDDLE_NAME                ,    setFields(A_MNAME    ,   officer.getMiddleName()                               ,     person.getMiddleNm()       ));
            eData.addDetails(OfficerUpdateFields.SUFFIX                     ,    setFields(A_SUFFIX   ,   suffixFile                                   ,     suffixSystem + ""  ));
            eData.addDetails(OfficerUpdateFields.PREFIX                     ,    setFields(A_PREFIX   ,   prefixFile                                   ,     prefixSystem + ""  ));
            eData.addDetails(OfficerUpdateFields.TRANSACTION_TYPE           ,    setFields(A_TRANSTYPE,   officer.getTransactionType()                          ,     ""));
        }else{
            logger.debug("Officer object is null.");                    
        }
        //***********************************************************************************************
        logger.debug("Setting position affiliate detail in eData object");
        
        if(posAffId != null){
            eData.addDetails(OfficerUpdateFields.POS_AFFILIATE_TYPE         ,    setFields(POS_AFFILIATE_TYPE     ,   posAffId.getType().toString()                      ,     affId.getType().toString()   ));
            eData.addDetails(OfficerUpdateFields.POS_STATE_TYPE             ,    setFields(POS_STATE_TYPE         ,   posAffId.getState()                                ,     affId.getState()             ));
            eData.addDetails(OfficerUpdateFields.POS_COUNCIL_CHAPTER        ,    setFields(POS_COUNCIL_CHAPTER    ,   posAffId.getCouncil()                              ,     affId.getCouncil()           ));
            eData.addDetails(OfficerUpdateFields.POS_LOCAL_CHAPTER          ,    setFields(POS_LOCAL_CHAPTER      ,   posAffId.getLocal()                                ,     affId.getLocal()             ));
            eData.addDetails(OfficerUpdateFields.POS_SUB_UNIT               ,    setFields(POS_SUB_UNIT           ,   posAffId.getSubUnit()                              ,     affId.getSubUnit()           ));
        }
        logger.debug("Setting home affiliate detail in eData object");        
        if(homAffId != null){
            eData.addDetails(OfficerUpdateFields.HOME_AFFILIATE_TYPE        ,    setFields(A_TYPE     ,   homAffId.getType().toString()                      ,     homeAffId.getType()      !=null ? homeAffId.getType().toString()     : ""));
            eData.addDetails(OfficerUpdateFields.HOME_STATE_TYPE            ,    setFields(A_STATEN   ,   homAffId.getState()                                ,     homeAffId.getState()     !=null ? homeAffId.getState()               : ""));
            eData.addDetails(OfficerUpdateFields.HOME_COUNCIL_CHAPTER       ,    setFields(A_COUNCIL  ,   homAffId.getCouncil()                              ,     homeAffId.getCouncil()   !=null ? homeAffId.getCouncil()             : ""));
            eData.addDetails(OfficerUpdateFields.HOME_LOCAL_CHAPTER         ,    setFields(A_LCHAP    ,   homAffId.getLocal()                                ,     homeAffId.getLocal()     !=null ? homeAffId.getLocal()               : ""));
            eData.addDetails(OfficerUpdateFields.HOME_SUB_UNIT              ,    setFields(A_SUNIT    ,   homAffId.getSubUnit()                              ,     homeAffId.getSubUnit()   !=null ? homeAffId.getSubUnit()             : ""));
        }

        logger.debug("Setting top affiliate detail in eData object");                
        if (topAffId != null){
            eData.addDetails(OfficerUpdateFields.AFF_AFFILIATE_TYPE        ,    setFields(AFF_AFFILIATE_TYPE     ,   topAffId.getType().toString()           ,     topAffId.getType()       !=null ? topAffId.getType().toString()      : ""));
            eData.addDetails(OfficerUpdateFields.AFF_STATE_TYPE            ,    setFields(AFF_STATE_TYPE         ,   topAffId.getState().toString()          ,     topAffId.getState()      !=null ? topAffId.getState().toString()     : ""));
            eData.addDetails(OfficerUpdateFields.AFF_COUNCIL_CHAPTER       ,    setFields(AFF_COUNCIL_CHAPTER    ,   topAffId.getCouncil().toString()        ,     topAffId.getCouncil()    !=null ? topAffId.getCouncil().toString()   : ""));
            eData.addDetails(OfficerUpdateFields.AFF_LOCAL_CHAPTER         ,    setFields(AFF_LOCAL_CHAPTER      ,   topAffId.getLocal().toString()          ,     topAffId.getLocal()      !=null ? topAffId.getLocal().toString()     : "")); 
            eData.addDetails(OfficerUpdateFields.AFF_SUB_UNIT              ,    setFields(AFF_SUB_UNIT           ,   topAffId.getSubUnit().toString()        ,     topAffId.getSubUnit()    !=null ? topAffId.getSubUnit().toString()   : ""));            
        }
        
        logger.debug("Setting address detail in eData object");                
        if(address != null){
            eData.addDetails(OfficerUpdateFields.ADDR1                      ,    setFields(A_ADDRESS1   ,   address.getAddr1()                               ,   sAddress.getAddr1()            ));
            eData.addDetails(OfficerUpdateFields.ADDR2                      ,    setFields(A_ADDRESS2   ,   address.getAddr1()                               ,   sAddress.getAddr2()            ));
            eData.addDetails(OfficerUpdateFields.CITY                       ,    setFields(A_CITY       ,   address.getCity()                                ,   sAddress.getCity()             ));
            eData.addDetails(OfficerUpdateFields.STATE                      ,    setFields(A_STATE      ,   address.getState()                               ,   sAddress.getState()            ));
            eData.addDetails(OfficerUpdateFields.ZIP                        ,    setFields(A_ZIP        ,   address.getZipCode()                             ,   sAddress.getZipCode()          ));
       
        }
        logger.debug("Setting phone detail in eData object");                
        if(oPhone != null){
            eData.addDetails(OfficerUpdateFields.PHONE_COUNTRY_CODE        ,    setFields(A_PHONECC     ,   oPhone.getCountryCode()                          ,   sPhone.getCountryCode()         ));
            eData.addDetails(OfficerUpdateFields.PHONE_AREA_CODE           ,    setFields(A_PHONEAC     ,   oPhone.getAreaCode()                             ,   sPhone.getAreaCode()            ));
            eData.addDetails(OfficerUpdateFields.PHONE_NUMBER              ,    setFields(A_PHONENUM    ,   oPhone.getPhoneNumber()                          ,   sPhone.getPhoneNumber()         ));

        }

        logger.debug("Set All ECompFields existed.");                                
    }//end of setAllECompFields()
    
    
    
    
    //*****************************************************************************************
    //Helper method will fetch from the db the primary phone of the officer
    private PhoneData getPrimaryPhone(PersonData person){
        logger.debug("Get Primary Phone entered.");
        PhoneData ph    =   new PhoneData();
         if(person.getThePhoneData() != null ){
                Collection phones   =   person.getThePhoneData();                
                if((phones != null) && (!phones.isEmpty())){                    
                    Iterator it     =   phones.iterator();                    
                    while(it.hasNext()){                        
                        ph = (PhoneData) it.next();                        
                        if(ph.getPhonePrmryFg().booleanValue()){
                            logger.debug("ph.getAreaCode()===========>" + ph.getAreaCode());
                            logger.debug("ph.getAreaCode()===========>" + ph.getCountryCode());
                            logger.debug("ph.getAreaCode()===========>" + ph.getPhoneNumber());
                            logger.debug("return primary phones==>");
                            return ph;
                        }
                    }
                }
            }
        logger.debug("Get Primary Phone existed.");        
        return ph;
    }
    
    
    //*****************************************************************************************************
    //This method will create an exception Comparision object for the field name passed to it. And initialize it 
    //with the  valueInFile and valueInSystem
    //@param fieldName for which to create the ExceptionComparison object
    //@param valueInFile the value comming in
    //@param valueInSystem the value in db
    //@return ExceptionComparison
    //****************************************************************************************************/
    private ExceptionComparison setFields( String fieldName, String valueInFile, String valueInSystem){
        logger.debug("Set Field Entered." );        
        ExceptionComparison     eComp           =   new ExceptionComparison();
        Map                     codes           =   null; 
        Integer                 codePk          =   null;                
        eComp.setField          (fieldName      );
        logger.debug("fieldName is " + fieldName);
        eComp.setValueInFile    (valueInFile    );
        eComp.setValueInSystem  (valueInSystem  );         
        logger.debug("Set Field existed." );                
        return eComp;
    }//end of setFields
    
    
    
    //Helper method to fetch the sub local for an affiliate
    private void getAffiliatesSubLocals(Integer affPk) {
        logger.debug("Get Affiliates Sub Locals method entered.");        
        // Retrieve all sub affiliates of the reporting affiliate.
        allAffiliates       = maintainAffiliates.getAffiliateSubHierarchy(fileEntry.getAffPk());        
        // The first aff in the list is the top affiliate, we should not count that.
        if(allAffiliates != null){            
            subAffiliates       = new HashSet(allAffiliates.size()-1);
            for (Iterator it    = allAffiliates.iterator();     it.hasNext(); ) {
                AffiliateHierarchyEntry affData = (AffiliateHierarchyEntry)it.next();
                subAffiliates.add(affData.getAffPk());
            } 
        }          
        logger.debug("Get Affiliates Sub Locals method existed.");                
    }//end of getAffiliatesSubLocals
    
    
    //*******************************************************************************************************
    private void addMissingAffiliates(){
        logger.debug("Add Missing Affiliates method entered.");
        if(subAffiliates != null){            
            Iterator it         =   subAffiliates.iterator();            
            while(it.hasNext()){
                Integer affPk   =   (Integer) it.next();
                if (!mapOffChanges.containsKey(affPk)) {
                    if(!applyUpdate){
                        OfficerChanges  offChg  =   new OfficerChanges();                        
                        offChg.setAffPk(affPk);
                        mapOffChanges.put(affPk  ,   offChg);     
                    }else{
                        oRData                    = new OfficerReviewData();
                        oRData.setAffPk(affPk);
                        oReviewList.add(oRData);
                    }
                }//end of if
            }//end of while            
        }//end of outer if
        logger.debug("Add Missing Affiliates method existed.");        
    }//end of add missing affiliates
    
        
    //************************************************************************************************************************
    //Helper method to initailize the fieldchange object
    private void intializeFieldChange(){
        logger.debug("Intialize Field Change method entered." );        
        
        fieldChanges.put(OfficerUpdateFields.PREFIX,               new FieldChange(PREFIX));
        fieldChanges.put(OfficerUpdateFields.FIRST_NAME,           new FieldChange(FIRST_NAME));
        fieldChanges.put(OfficerUpdateFields.MIDDLE_NAME,          new FieldChange(MIDDLE_NAME));
        fieldChanges.put(OfficerUpdateFields.LAST_NAME,            new FieldChange(LAST_NAME));
        fieldChanges.put(OfficerUpdateFields.SUFFIX,               new FieldChange(SUFFIX));
        fieldChanges.put(OfficerUpdateFields.ADDR1,                new FieldChange(ADDR1));
        fieldChanges.put(OfficerUpdateFields.ADDR2,                new FieldChange(ADDR2));
        fieldChanges.put(OfficerUpdateFields.CITY,                 new FieldChange(CITY));
        fieldChanges.put(OfficerUpdateFields.STATE,                new FieldChange(STATE));
        fieldChanges.put(OfficerUpdateFields.ZIP,                  new FieldChange(ZIP));
        fieldChanges.put(OfficerUpdateFields.SSN,                  new FieldChange(SSN));
        fieldChanges.put(OfficerUpdateFields.ZIP_PLUS,             new FieldChange(ZIPPLUS));
        fieldChanges.put(OfficerUpdateFields.PROVINCE,             new FieldChange(PROVINCE));
        fieldChanges.put(OfficerUpdateFields.PHONE_COUNTRY_CODE,   new FieldChange(PHONECOUNTRY));
        fieldChanges.put(OfficerUpdateFields.PHONE_AREA_CODE,      new FieldChange(PHONEAREA));
        fieldChanges.put(OfficerUpdateFields.PHONE_NUMBER,         new FieldChange(PHONENUMBER));
        fieldChanges.put(OfficerUpdateFields.TERM_EXPIRATION,      new FieldChange(EXPIRE_TERM));
        fieldChanges.put(OfficerUpdateFields.OFFICER_TITLE,        new FieldChange(TITLE));
        fieldChanges.put(OfficerUpdateFields.U_MEMBER_AFF_ID,      new FieldChange(AFFILIATE_MEMBER_ID));
        fieldChanges.put(OfficerUpdateFields.AFSCME_MEMBER_ID,     new FieldChange(AFSCME_MEMBER_ID));
        fieldChanges.put(OfficerUpdateFields.POS_AFFILIATE_TYPE,   new FieldChange(AFFILIATE_TYPE));
        fieldChanges.put(OfficerUpdateFields.POS_LOCAL_CHAPTER,    new FieldChange(LOCAL_CHAPTER));
        fieldChanges.put(OfficerUpdateFields.POS_STATE_TYPE,       new FieldChange(STATE_TYPE));
        fieldChanges.put(OfficerUpdateFields.POS_SUB_UNIT,         new FieldChange(SUB_UNIT));
        fieldChanges.put(OfficerUpdateFields.POS_COUNCIL_CHAPTER,  new FieldChange(COUNCIL_CHAPTER));
        fieldChanges.put(OfficerUpdateFields.HOME_AFFILIATE_TYPE,  new FieldChange(AFFILIATE_TYPE));
        fieldChanges.put(OfficerUpdateFields.HOME_LOCAL_CHAPTER,   new FieldChange(LOCAL_CHAPTER));
        fieldChanges.put(OfficerUpdateFields.HOME_STATE_TYPE,      new FieldChange(STATE_TYPE));
        fieldChanges.put(OfficerUpdateFields.HOME_SUB_UNIT,        new FieldChange(SUB_UNIT));
        fieldChanges.put(OfficerUpdateFields.HOME_COUNCIL_CHAPTER, new FieldChange(COUNCIL_CHAPTER));
        fieldChanges.put(OfficerUpdateFields.AFF_AFFILIATE_TYPE,    new FieldChange(AFF_AFFILIATE_TYPE));
        fieldChanges.put(OfficerUpdateFields.AFF_LOCAL_CHAPTER,     new FieldChange(AFF_LOCAL_CHAPTER));
        fieldChanges.put(OfficerUpdateFields.AFF_STATE_TYPE,        new FieldChange(AFF_STATE_TYPE));
        fieldChanges.put(OfficerUpdateFields.AFF_SUB_UNIT,          new FieldChange(AFF_SUB_UNIT));
        fieldChanges.put(OfficerUpdateFields.AFF_COUNCIL_CHAPTER,   new FieldChange(AFF_COUNCIL_CHAPTER));        
        
        logger.debug("Intialize Field Change method existed." );        
        
    }//end of populateFieldChange
    

    //**********************************************************************************************************************
    //The following methods are for calculating field changes for the officer and address
    //********************************************************************************************************************
    private void processFieldChangeStatistics(){
        logger.debug("Process Field Change Statistics method entered." );        
        if (officer.getAfscmeMemberNumber() == null || officer.getAfscmeMemberNumber().intValue() == 0)
            return;
        sAddress            =     addressBean.getSystemAddress(officer.getAfscmeMemberNumber());        
        person              =     personBean.getPersonDetail(officer.getAfscmeMemberNumber(), null);
        sPhone              =     getPrimaryPhone(person);
        getFieldChangeStatistics();
        logger.debug("Process Field Change Statistics method existed." );                
    }//end of getFieldChangeStatistics() method    
    
    
   //**************************************************************************************************************
    //Actual field change calculator. calculate how many times a field has been changed for the whole document
    private void getFieldChangeStatistics(){        
        logger.debug("Get Field Change Statistics Entered." );

        if (person.getSsn() == null)
            person.setSsn("");
        
        if((officer.getSsn() != null) && !(officer.getSsn().equals(person.getSsn()))){
            logger.debug("SSN change found.");
            logger.debug("Value in File:    " + officer.getSsn());
            logger.debug("Value in System:  " + person.getSsn());
          ((FieldChange)  fieldChanges.get(OfficerUpdateFields.SSN)).incrementCount();
        }
        
        if (person.getFirstNm() == null)
            person.setFirstNm("");
        
        if((officer.getFirstName() != null) && !(officer.getFirstName().equals(person.getFirstNm()))){
            logger.debug("First Name change found.");
            logger.debug("Value in File:    " + officer.getFirstName());
            logger.debug("Value in System:  " + person.getFirstNm());
          ((FieldChange)  fieldChanges.get(OfficerUpdateFields.FIRST_NAME)).incrementCount();
        }
        
        if (person.getLastNm() == null)
            person.setLastNm("");

        if((officer.getLastName() != null) && !(officer.getLastName().equals(person.getLastNm()))){
            logger.debug("Last Name change found.");
            logger.debug("Value in File:    " + officer.getLastName());
            logger.debug("Value in System:  " + person.getLastNm());
          ((FieldChange)  fieldChanges.get(OfficerUpdateFields.LAST_NAME)).incrementCount();
        }

        if (person.getMiddleNm() == null)
            person.setMiddleNm("");
        
        if((officer.getMiddleName()!=null) && !(officer.getMiddleName().equals(person.getMiddleNm()))){
            logger.debug("Middle Name change found.");
            logger.debug("Value in File:    " + officer.getMiddleName());
            logger.debug("Value in System:  " + person.getMiddleNm());
          ((FieldChange)  fieldChanges.get(OfficerUpdateFields.MIDDLE_NAME)).incrementCount();
        }

        if((officer.getPrefix() != null) && person.getPrefixNm().intValue() != 0 &&
        !(officer.getPrefix().equals(person.getPrefixNm()))){
            logger.debug("Prefix change found.");
            logger.debug("Value in File:    " + officer.getPrefix());
            logger.debug("Value in System:  " + person.getPrefixNm());
          ((FieldChange)  fieldChanges.get(OfficerUpdateFields.PREFIX)).incrementCount();
        }
        
        if((officer.getSuffix() != null) && person.getSuffixNm().intValue() != 0 &&
        !(officer.getSuffix().equals(person.getSuffixNm()))){
            logger.debug("Suffix change found.");
            logger.debug("Value in File:    " + officer.getSuffix());
            logger.debug("Value in System:  " + person.getSuffixNm());
          ((FieldChange)  fieldChanges.get(OfficerUpdateFields.SUFFIX)).incrementCount();
        }
        
        if(address != null){
            if (sAddress.getAddr1() == null)
                sAddress.setAddr1("");
            
            if((address.getAddr1() != null) && !(address.getAddr1().equals(sAddress.getAddr1()))){
                logger.debug("Address 1 change found.");
                logger.debug("Value in File:    " + address.getAddr1());
                logger.debug("Value in System:  " + sAddress.getAddr1());
              ((FieldChange)  fieldChanges.get(OfficerUpdateFields.ADDR1)).incrementCount();
            }

            
            if (sAddress.getAddr2() == null)
                sAddress.setAddr2("");
            
            if((address.getAddr2() != null) && !(address.getAddr2().equals(sAddress.getAddr2()))){
                logger.debug("Address 2 change found.");
                logger.debug("Value in File:    " + address.getAddr2());
                logger.debug("Value in System:  " + sAddress.getAddr2());
              ((FieldChange)  fieldChanges.get(OfficerUpdateFields.ADDR2)).incrementCount();
            }

            if (sAddress.getCity() == null)
                sAddress.setCity("");
            
            if((address.getCity() != null) && !(address.getCity().equals(sAddress.getCity()))){
                logger.debug("City change found.");
                logger.debug("Value in File:    " + address.getCity());
                logger.debug("Value in System:  " + sAddress.getCity());
              ((FieldChange)  fieldChanges.get(OfficerUpdateFields.CITY)).incrementCount();
            }

            if (sAddress.getState() == null)
                sAddress.setState("");
            
            if((address.getState() != null) && !(address.getState().equals(sAddress.getState()))){
                logger.debug("State change found.");
                logger.debug("Value in File:    " + address.getState());
                logger.debug("Value in System:  " + sAddress.getState());
              ((FieldChange)  fieldChanges.get(OfficerUpdateFields.STATE)).incrementCount();
            }

            if (sAddress.getZipCode() == null)
                sAddress.setZipCode("");
            
            if((address.getZipCode() != null) && !(address.getZipCode().equals(sAddress.getZipCode()))){
                logger.debug("Zip change found.");
                logger.debug("Value in File:    " + address.getZipCode() + "-" + address.getZipPlus());
                logger.debug("Value in System:  " + sAddress.getZipCode() + "-" + sAddress.getZipPlus());
              ((FieldChange)  fieldChanges.get(OfficerUpdateFields.ZIP)).incrementCount();
            }            
        }//end address check
        
        if(oPhone != null){
            
            if (sPhone.getCountryCode() == null)
                sPhone.setCountryCode("");
            
            if((oPhone.getCountryCode() != null) && !(oPhone.getCountryCode().equals(sPhone.getCountryCode()))){            
              ((FieldChange)  fieldChanges.get(OfficerUpdateFields.PHONE_COUNTRY_CODE)).incrementCount();
            }
            
            if (sPhone.getAreaCode() == null)
                sPhone.setAreaCode("");
            
            if((oPhone.getAreaCode() != null) && !(oPhone.getAreaCode().equals(sPhone.getAreaCode()))){            
              ((FieldChange)  fieldChanges.get(OfficerUpdateFields.PHONE_AREA_CODE)).incrementCount();
            }
            
            if (sPhone.getPhoneNumber() == null)
                sPhone.setPhoneNumber("");
            
            if((oPhone.getPhoneNumber() != null) && !(oPhone.getPhoneNumber().equals(sPhone.getPhoneNumber()))){            
              ((FieldChange)  fieldChanges.get(OfficerUpdateFields.PHONE_NUMBER)).incrementCount();
            }
        }//end of phone check
        
        logger.debug("Get Field Change Statistics existed." );        
    }//end of getFieldChangeStatistics method
    
    
    
    //*************************************************************************************************************************
    //gets the office details for the officers in the file
    //THIS METHOD IS NOT BEING CALLED
    private void getPreOfficeDetails(){
        logger.debug("Get Pre Officer Details method entered.");
        Map.Entry entry         =    null;
        Integer offAllowed      =    null;
        Integer offPk           =    null;
        Integer grpID           =    null;

        if(! mapOffChanges.isEmpty()){
            Iterator it         =   mapOffChanges.entrySet().iterator();            
            while(it.hasNext()){                
                entry                       =   (Map.Entry)it.next();
                Integer         affPk       =   (Integer) entry.getKey();
                logger.debug("affpk = " + affPk);
                OfficerChanges  oChng       =   (OfficerChanges) entry.getValue();
                
                if((mapOffAllowed != null) && (mapOffAllowed.containsKey(affPk))){
                    logger.debug("mapOffAllowed is not null; it = " + mapOffAllowed);                    
                    PositionChanges pChanges    =   new PositionChanges();                    
                    
                    HashMap offDtl      =   (HashMap) mapOffAllowed.get(affPk);
                    
                    logger.debug("setting  values =====>" + offDtl.get(new Integer(1)));
                    offAllowed  =   (Integer) offDtl.get(new Integer(1));
                    logger.debug("setting  values =====>" + offDtl.get(new Integer(2)));
                    offPk       =   (Integer) offDtl.get(new Integer(2));
                    logger.debug("setting  values =====>" + offDtl.get(new Integer(3)));
                    grpID       =   (Integer) offDtl.get(new Integer(3));
                    pChanges.setAllowed(offAllowed.intValue());
                    pChanges.setOfficePk(offPk);
                    pChanges.setGroupId(grpID);
                    pChanges.setAffPk(affPk);
                    pChanges.setInFile(oChng.getInFile());                                
                    
                    mapPosChanges.put(affPk, pChanges);
                    
                }
/*                
                else if((offPk != null) && (offPk.intValue() > 0) && (grpID != null) && (grpID.intValue() > 0)){
                    logger.debug("mapOffAllowed is null" );                    
                    logger.debug("grpID is " + grpID );                    
                    logger.debug("offPk " + offPk );                    
                    pChanges.setAllowed(0);
                    pChanges.setOfficePk(offPk);
                    pChanges.setGroupId(grpID);
                }

                logger.debug("pChanges.getAffPK=================>"      +   pChanges.getAffPk());
                logger.debug("pChanges.getAllowed()=================>"  +   pChanges.getAllowed());
                logger.debug("pChanges.getInFile()=================>"   +   pChanges.getInFile());
                logger.debug("Adding affpk=================>"   +   affPk);
                mapPosChanges.put(affPk, pChanges);
                //logger.debug("Done adding affpk=================>to map"   +   affPk);
 */
            }//end of outer while
        
        }//end of if(! mapOffChanges.isEmpty()){
        logger.debug("Get Pre Officer Details method existed.");        
    }//end of getPreOfficeDetails method
    
        
    //*****************************************************************************************************************
    private boolean matchOfficer(){        
        int pk = updateBean.matchOfficer(officer);                                                       
        if (pk > 0){
            officer.setAfscmeMemberNumber(new Integer(pk));            
            return true;
        }else{
            officer.setAfscmeMemberNumber(null);                
            // HLM: 1/28/2004. 
            // This is an exception case, not an Affiliate Error
            //oChanges.setHasError(true);                    
            setAllOfficerFields(eDataGlobal);                
            setErrorFlag(A_LNAME, eDataGlobal);                                                
            setErrorFlag(A_FNAME, eDataGlobal);                                                
            eDataGlobal.setUpdateErrorCodePk(new Integer(79008));                                
        }
        return false;        
    }
    
    //*****************************************************************************************************************
    //This method is no longer being used.
    private boolean matchOfficerOld(){
        logger.debug("Match Offier method is entered.");
                
        if(affOfficers.isEmpty()){
            logger.debug("affOfficers is empty so getting the data for ============>" + currAffPk);
            affOfficers     =   (HashMap) updateBean.getAffOfficers(currAffPk);            
        }else{
            logger.debug("affOfficers is not empty so getting the Iterator ============>" );
            Iterator it     =   affOfficers.values().iterator();
            while(it.hasNext()){                                
                if(!((HashMap) it.next()).containsValue(currAffPk)){
                    //logger.debug("didn't find in affOfficers currAffPk so getting the data for ============>" + currAffPk);
                    affOfficers     =  (HashMap) updateBean.getAffOfficers(currAffPk);
                }
            }
        }
                
        Iterator it     =   affOfficers.values().iterator();
        
        while(it.hasNext()){
            HashMap map =   (HashMap) it.next();
            Integer pPk         =  (Integer) map.get(new Integer(1));
            Integer affPk       =  (Integer) map.get(new Integer(2));
            String ssn          =  (String)  map.get(new Integer(3));
            String firstName    =  (String)  map.get(new Integer(4));
            String lastName     =  (String)  map.get(new Integer(5));
            Integer posAffpk    =  (Integer) map.get(new Integer(6));
            logger.debug("pPk ============>"        + pPk);
            logger.debug("affPk ============>"      + affPk);
            logger.debug("ssn ============>"        + ssn);
            logger.debug("firstName ============>"  + firstName);
            logger.debug("lastName ============>"   + lastName);
                       
            if((officer.getAfscmeMemberNumber() != null) && (pPk.intValue() == officer.getAfscmeMemberNumber().intValue())){
                logger.debug("personPk matches for person ============>" + officer.getFirstName());
                return true;
            }else if((officer.getAffiliateMemberNumber() != null && officer.getAffiliateMemberNumber().length() > 0) &&                      
                     (officer.getFirstName() != null && officer.getFirstName().length() > 0 ) && 
                     (officer.getLastName() != null && officer.getLastName().length() > 0) &&            
                     (affPk.intValue() == new Integer(officer.getAffiliateMemberNumber()).intValue())  
                        && (officer.getFirstName().equals(  firstName   )) 
                        && (officer.getLastName().equals(   lastName    ))){
                logger.debug("affpk  matches for posAffPk ============>" + officer.getFirstName());
                return true;
            }else if((officer.getSsn() != null && officer.getSsn().length() > 0) && 
                     (officer.getFirstName() != null && officer.getFirstName().length() > 0 ) && 
                     (officer.getLastName() != null && officer.getLastName().length() > 0) &&
                     (officer.getSsn().equals(ssn)) && (officer.getFirstName().equals(  firstName   )) 
                                                    && (officer.getLastName().equals(   lastName    ))){
                logger.debug("affpk  matches for posAffPk ============>" + officer.getFirstName());
                return true;
            }
        }
                
        setAllOfficerFields(eDataGlobal);
        logger.debug("Match Offier method is existed.");                
        return false;            
     }
    
    private void getOfficerCardRun(){
        logger.debug("Get Offier Card Run entered.");                        
        
        if (!officersSortByAffiliate.isEmpty())
        {
            Iterator itr = (Iterator)officersSortByAffiliate.keySet().iterator();             
            while (itr.hasNext()){  // for each affiliate
                int count = 0;
                Integer key = (Integer)itr.next();
                if (mapOffChanges.containsKey(key)){
                    List list = (List)officersSortByAffiliate.get(key);
                    if (!list.isEmpty()){
                        Iterator litr = (Iterator)list.iterator();
                        while (litr.hasNext()){
                            OfficerUpdateElement offEle = (OfficerUpdateElement)litr.next();
                            if (offEle.getTransactionType().equalsIgnoreCase("Add") ||offEle.getTransactionType().equalsIgnoreCase("Renew")){                                
                                StringTokenizer tok = new StringTokenizer(offEle.getTermExpiration(), "-");
                                int year         = new Integer(tok.nextToken()).intValue();
                                int month        = new Integer(tok.nextToken()).intValue();
                                Calendar cal = new GregorianCalendar();
  
                                int currentMonth = cal.get(Calendar.MONTH) + 1;           // 0=Jan, 1=Feb, ...                                
                                int currentYear = cal.get(Calendar.YEAR);           // 0=Jan, 1=Feb, ...                                                                
                                if (year - currentYear < 0)
                                    continue;
                                
                                if ((year - currentYear) == 0){
                                    if (month - currentMonth < 3)
                                        continue;
                                }
                                if ((year - currentYear) == 1){
                                    if (12 - currentMonth + month < 3)
                                        continue;
                                }
                                // Add to card run
                                count++;
                            }
                        }
                    }
                    
                    OfficerChanges offChanges = (OfficerChanges)mapOffChanges.get(key);
                    offChanges.setCards(count);
                }                    
            }            
        }
        logger.debug("Get Offier Card Run existed.");                              
    }
    
    //**********************************************************************************************************************
    //These callback event methods will be needed if the parser is to be validating against the schema
    public void warning(SAXParseException exception) throws SAXException {
        
        logger.debug("**Parsing Warning**" + "  Line:    " +  exception.getLineNumber() + "" +  "  URI:     " +  exception.getSystemId() + "" +  "  Message: " + exception.getMessage());        
        throw new SAXException("Warning encountered");
    }
    public void error(SAXParseException exception) throws SAXException {
        
        logger.debug("**Parsing Error**" +  "  Line:    " + exception.getLineNumber() + "" + "  URI:     " + exception.getSystemId() + "" + "  Message: " + exception.getMessage());        
        throw new SAXException("Error encountered");
    }
    public void fatalError(SAXParseException exception) throws SAXException {
        
        logger.debug("**Parsing Fatal Error**" +  "  Line:    " + exception.getLineNumber() + "" + "  URI:     " + exception.getSystemId() + "" +"  Message: " + exception.getMessage());        
        throw new SAXException("Fatal Error encountered");
    }

}
