/**
 * A runtime convenient class for getting the Common Codes
 * The codes are obtain from com_cd_pk field of the Common_Codes table.
 */

package org.afscme.enterprise.update;


public interface Codes {

    
    /** Common Code Type 'MemberUpdateFields' */
    public interface MemberUpdateFields {
        public static final Integer PREFIX              = new Integer(74001);   
        public static final Integer FIRST_NAME          = new Integer(74002);                
        public static final Integer MIDDLE_NAME         = new Integer(74003);
        public static final Integer LAST_NAME           = new Integer(74004);
        public static final Integer SUFFIX              = new Integer(74005);
        public static final Integer ADDR1               = new Integer(74006);
        public static final Integer ADDR2               = new Integer(74007);
        public static final Integer CITY                = new Integer(74008);
        public static final Integer STATE               = new Integer(74009);
        public static final Integer ZIP                 = new Integer(74010);
        public static final Integer MAILABLE_ADDRESS    = new Integer(74011);
        public static final Integer NO_MAIL             = new Integer(74012);
        public static final Integer PHONE               = new Integer(74013);
        public static final Integer STATUS              = new Integer(74014);           
        public static final Integer GENDER              = new Integer(74015);
        public static final Integer DATE_JOINED         = new Integer(74016);
        public static final Integer REGISTERED_VOTER    = new Integer(74017);
        public static final Integer POLITICAL_PARTY     = new Integer(74018);
        public static final Integer SSN                 = new Integer(74019);
        public static final Integer INFORMATION_SOURCE  = new Integer(74020);
        public static final Integer AFFILIATE_MEMBER_ID = new Integer(74021); 
    } 
    
    /** Common Code Type "UpdateFileType" */
    public interface UpdateFileType {
        public static final int MEMBER          = 16001;
        public static final int OFFICER         = 16002;
        public static final int REBATE          = 16003;
        public static final int PARTICIPATION   = 16004;
    }
    
    /** Common Code Type "UpdateFileQueue" */
    public interface UpdateFileQueue {
        public static final int INITIAL         = 14001;
        public static final int GOOD            = 14002;
        public static final int BAD             = 14003;
    }
    
    /** Common Code Type "UpdateType" */
    public interface UpdateType {
        public static final int FULL            = 13001;
        public static final int PARTIAL         = 13002;
    }
    
    /** Common Code Type "UpdateFileStatus" */
    public interface UpdateFileStatus {
        public static final int REVIEW          = 15001;
        public static final int PENDING         = 15002;
        public static final int PROCESSED       = 15003;
        public static final int REJECTED        = 15004;
        public static final int UPLOADED        = 15005;
        public static final int INVALID         = 15006;
    }
    
    /** Common Code Type 'UpdateFieldErrorType' */
    public interface UpdateFieldError {
        public static final Integer ADD             = new Integer(79001);
        public static final Integer CHANGE          = new Integer(79002);
        public static final Integer INACTIVATE      = new Integer(79003);
        public static final Integer DUPLICATE       = new Integer(79004);
        public static final Integer RECENT          = new Integer(79005);
        public static final Integer UNKNOWN         = new Integer(79006);
        public static final Integer REQUIRED        = new Integer(79007);
        public static final Integer INVALID         = new Integer(79008);
    }
    
    /** Sort Elements */
    public interface Sort {
        public static final int RDATE             =   1;
        public static final int VALID_DATE        =   2;
        public static final int COMMENTS          =   3;
        public static final int STATUS            =   4;
        public static final int QPK               =   5;
        public static final int FTYPE             =   6;
        public static final int PROCESSED         =   7;
        public static final int UPDATE_TYPE       =   8;
        public static final int TYPE              =   9;
        public static final int LOC               =   10;
        public static final int STATE             =   11;
        public static final int SUB_UNIT          =   12;
        public static final int CN_CHAP           =   13;
    }
    
    /** Common Code Type 'RebateUpdateFields' */
    public interface RebateUpdateFields {
        public static final Integer AFSCME_MEMBER_ID    = new Integer(81001);
        public static final Integer SSN                 = new Integer(81002);
        public static final Integer DUP_SSN             = new Integer(81003);
        public static final Integer FIRST_NAME          = new Integer(81004);
        public static final Integer MIDDLE_NAME         = new Integer(81005);
        public static final Integer LAST_NAME           = new Integer(81006);
        public static final Integer ADDR1               = new Integer(81007);
        public static final Integer ADDR2               = new Integer(81008);
        public static final Integer CITY                = new Integer(81009);
        public static final Integer PROVINCE            = new Integer(81010);
        public static final Integer STATE               = new Integer(81011);
        public static final Integer ZIP                 = new Integer(81012);
        public static final Integer ZIP_PLUS            = new Integer(81013);        
        public static final Integer COUNTRY             = new Integer(81014);
        public static final Integer MEMBER_TYPE         = new Integer(81015);
        public static final Integer MEMBER_STATUS       = new Integer(81016);
        public static final Integer DUES_TYPE           = new Integer(81017);
        public static final Integer NUM_MONTHS          = new Integer(81018);
        public static final Integer ACCEPTANCE_CODE     = new Integer(81019);
    } 
    
    /** Common Code Type "RebateAcceptanceCode" */
    public interface RebateAcceptanceCode {
        public static final Integer COUNCIL_ACCEPTED = new Integer(54001);
        public static final Integer LOCAL_ACCEPTED   = new Integer(54002);
        public static final Integer DENIED           = new Integer(54003);
    }
    
    /** Common Code Type "RebateMbrType" */
    public interface RebateMbrType {
        public static final Integer REGULAR              = new Integer(59001);
        public static final Integer RETIREE              = new Integer(59002);
        public static final Integer RETIREE_SPOUSE       = new Integer(59003);
        public static final Integer UNION_SHOP           = new Integer(59004);
        public static final Integer UNION_SHOP_OBJECTOR  = new Integer(59005);
        public static final Integer AGENCY_FEE_PAYER     = new Integer(59006);
        public static final Integer NON_MEMBER           = new Integer(59007);        
        
    }
    
    /** Common Code Type "OfficerTitleCode" */
    public interface OfficerTitleCode {
        public static final Integer EXECUTIVE_DIRECTOR          = new Integer(6006);
        public static final Integer ADMINISTRATOR               = new Integer(6004);
        public static final Integer FINANCIAL_REPORTING_OFFICER = new Integer(6032);
    }
    
    /** Common Code Type 'RebateUpdateFields' */
    public interface OfficerUpdateFields {
        public static final Integer AFSCME_MEMBER_ID    = new Integer(82001);
        public static final Integer SSN                 = new Integer(82002);
        public static final Integer TERM_EXPIRATION     = new Integer(82003);
        public static final Integer OFFICER_TITLE       = new Integer(82004);
        public static final Integer PREFIX              = new Integer(82005);        
        public static final Integer FIRST_NAME          = new Integer(82006);
        public static final Integer MIDDLE_NAME         = new Integer(82007);
        public static final Integer LAST_NAME           = new Integer(82008);
        public static final Integer SUFFIX              = new Integer(82009);
        public static final Integer ADDR1               = new Integer(82010);
        public static final Integer ADDR2               = new Integer(82011);
        public static final Integer CITY                = new Integer(82012);
        public static final Integer PROVINCE            = new Integer(82013);
        public static final Integer STATE               = new Integer(82014);
        public static final Integer ZIP                 = new Integer(82015);
        public static final Integer ZIP_PLUS            = new Integer(82016);        
        public static final Integer COUNTRY             = new Integer(82017);
        public static final Integer PHONE_COUNTRY_CODE  = new Integer(82018);
        public static final Integer PHONE_AREA_CODE     = new Integer(82019);
        public static final Integer PHONE_NUMBER        = new Integer(82020);
        public static final Integer U_MEMBER_AFF_ID     = new Integer(82021);              
        public static final Integer POS_AFFILIATE_TYPE  = new Integer(82022);
        public static final Integer POS_LOCAL_CHAPTER   = new Integer(82023);
        public static final Integer POS_STATE_TYPE      = new Integer(82024);
        public static final Integer POS_SUB_UNIT        = new Integer(82025);
        public static final Integer POS_COUNCIL_CHAPTER = new Integer(82026);
        public static final Integer HOME_AFFILIATE_TYPE = new Integer(82027);
        public static final Integer HOME_LOCAL_CHAPTER  = new Integer(82028);
        public static final Integer HOME_STATE_TYPE     = new Integer(82029);
        public static final Integer HOME_SUB_UNIT       = new Integer(82030);
        public static final Integer HOME_COUNCIL_CHAPTER= new Integer(82031);
        public static final Integer AFF_AFFILIATE_TYPE  = new Integer(82032);
        public static final Integer AFF_LOCAL_CHAPTER   = new Integer(82033);
        public static final Integer AFF_STATE_TYPE      = new Integer(82034);
        public static final Integer AFF_SUB_UNIT        = new Integer(82035);
        public static final Integer AFF_COUNCIL_CHAPTER = new Integer(82036);        
        public static final Integer TRANSACTION_TYPE    = new Integer(82037);
    } 
}
