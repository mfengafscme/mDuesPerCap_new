package org.afscme.enterprise.codes;

/**
 * Holds the values of some predefined codes that are needed
 * to implement business rules in application code.
 */
public interface Codes {

    //* Common Code Type 'PersonAddressType' */
    public interface PersonAddressType {
        /** Home address */
        public static final Integer HOME = new Integer(12001);
    }

    //* Common Code Type 'Country' */
    public interface Country {
        /** United States */
        public static final Integer US = new Integer(9001);
        /** Canada */
        public static final Integer CA = new Integer(9002);
    }

    //* Common Code Type 'CountryCode' */
	public interface CountryCode {
		/** United States */
		public static final Integer US = new Integer(8001);
    }

    //* Common Code Type 'Affiliate Type' */
    public interface AffiliateType {
        /** Retiree Chapter */
        public static final Integer R = new Integer(27004);
        /** Retiree Sub Chapter' */
        public static final Integer S = new Integer(27005);
    }

    //* Common Code Type 'MemberType' */
    public interface MemberType {
        /** Regular */
        public static final Integer R = new Integer(29001);
        /** Retiree */
        public static final Integer T = new Integer(29002);
        /** Agency Fee Payer */
        public static final Integer A = new Integer(29003);
        /** Union Shop */
        public static final Integer U = new Integer(29004);
        /** Union Shop Objector */
        public static final Integer O = new Integer(29005);
        /** Retiree Spouse */
        public static final Integer S = new Integer(29006);
    }

    //* Common Code Type 'MemberStatus' */
    public interface MemberStatus {
        /** Active */
        public static final Integer A = new Integer(31001);
        /** Inactive */
        public static final Integer I = new Integer(31002);
        /** Temporary */
        public static final Integer T = new Integer(31003);
    }


    //* Common Code Type 'Department' */
    public interface Department {
        /** Membership */
        public static final Integer MD = new Integer(4001);
        /** PEOPLE */
        public static final Integer PD = new Integer(4005);
    }

    /** Common Code Type 'OrganizationSubType; */
    public interface OrganizationSubType {
        /** Affiliate Sub Type */
        public static final Integer A = new Integer(24001);

        /** Organization Sub Type */
        public static final Integer O = new Integer(24002);
    }

    //* Common Code Type 'OrgAddressType' */
    public interface OrgAddressType {
        /** Regular address */
        public static final Integer REGULAR = new Integer(72001);
        /** Shipping address */
        public static final Integer SHIPPING = new Integer(72002);
    }

     //* Common Code Type 'OrgPhoneType' */
     public interface OrgPhoneType {
         /** Organization Location Office phone */
         public static final Integer LOC_PHONE_OFFICE = new Integer(73001);
         /** Organization Location Fax phone */
         public static final Integer LOC_PHONE_FAX = new Integer(73002);
     }

    /** Common Code Type 'ActivityType; */
    public interface ActivityType {
        /** Add activity */
        public static final Integer A = new Integer(30001);

        /** Update activity */
        public static final Integer U = new Integer(30002);

       /** Deactivate activity */
        public static final Integer D = new Integer(30003);
    }

    /** Common Code Type 'EmailType' */
    public interface EmailType {
        /* Primary */
        public static final Integer PRIMARY = new Integer(71001);
	 /* Alternate */
        public static final Integer ALTERNATE = new Integer(71002);

}

    /** Common Code Type 'PhoneType' */
    public interface PhoneType {

	public static final Integer HOME        = new Integer(3001);
        public static final Integer WORK        = new Integer(3002);
        public static final Integer CELL        = new Integer(3003);
        public static final Integer FAX         = new Integer(3004);
        public static final Integer OTHER       = new Integer(3005);
 }


    /** Common Code Type 'AffiliateStatus' */
    public interface AffiliateStatus {
        /** Administrative/Legislative Council */
        public static final Integer AC = new Integer(17001);

        /** Chartered PK*/
        public static final Integer C = new Integer(17002);

        /** Certified Code*/
        public static final Integer CU = new Integer(17003);

        /** Deactivated Code */
        public static final Integer D = new Integer(17004);

        /** Decertified Code */
        public static final Integer DU = new Integer(17005);

        /** Merged Code */
        public static final Integer M = new Integer(17006);

        /** Not Chartered Code */
        public static final Integer N = new Integer(17007);

        /** Pending Charter Code */
        public static final Integer PC = new Integer(17008);

        /** Pending Deactivation Code */
        public static final Integer PD = new Integer(17009);

        /** Restricted Administratorship Code */
        public static final Integer RA = new Integer(17010);

        /** Split Code */
        public static final Integer S = new Integer(17011);

        /** Unrestricted Administratorship Code */
        public static final Integer UA = new Integer(17012);
    }

    /** Common Code Type 'AffiliateSections' */
    public interface AffiliateSections {
        public static final Integer DETAIL                  = new Integer(39001);
        public static final Integer LOCATION                = new Integer(39002);
        public static final Integer CHARTER                 = new Integer(39003);
        public static final Integer CONSTITUTION            = new Integer(39004);
        public static final Integer OFFICER_TITLES          = new Integer(39005);
        public static final Integer FINANCIAL               = new Integer(39006);
        public static final Integer MEMBERSHIP_REPORTING    = new Integer(39007);
        public static final Integer AFFILIATE_ID            = new Integer(39008);
    }

    /** Common Code Type 'ChangeHistoryFields' */
    public interface ChangeHistoryFields {
        public static final Integer ABBREVIATED_NAME                    = new Integer(64001);
        public static final Integer AFSCME_LEG_DISTRICT                 = new Integer(64002);
        public static final Integer AFSCME_REGION                       = new Integer(64003);
        public static final Integer MULT_EMPLOYERS                      = new Integer(64004);
        public static final Integer EMPLOYER_SECTORS                    = new Integer(64005);
        public static final Integer SUB_LOCALS_ALLOWED                  = new Integer(64006);
        public static final Integer MULT_OFFICES                        = new Integer(64007);
        public static final Integer ANNUAL_CARD_RUN_TYPE                = new Integer(64008);
        public static final Integer MEMBER_RENEWAL                      = new Integer(64009);
        public static final Integer WEBSITE                             = new Integer(64010);
        public static final Integer CHARTER_NAME                        = new Integer(64011);
        public static final Integer CHARTER_JURISDICTION                = new Integer(64012);
        public static final Integer CHARTER_CODE                        = new Integer(64013);
        public static final Integer LAST_CHANGE_EFFECTIVE_DATE          = new Integer(64014);
        public static final Integer CHARTER_DATE                        = new Integer(64015);
        public static final Integer COUNTIES                            = new Integer(64016);
        public static final Integer COUNCIL_AFFILIATIONS                = new Integer(64017);
        public static final Integer MOST_CURRENT_APPROVAL_DATE          = new Integer(64018);
        public static final Integer AFFILIATION_AGREEMENT_DATE          = new Integer(64019);
        public static final Integer METHOD_OF_OFFICER_ELECTION          = new Integer(64020);
        public static final Integer CONSTITUTIONAL_REGIONS              = new Integer(64021);
        public static final Integer MEETING_FREQUNECY                   = new Integer(64022);
        public static final Integer EMPLOYER_ID_NUMBER                  = new Integer(64023);
        public static final Integer TITLE_OF_LOCATION                   = new Integer(64024);
        public static final Integer PRIMARY_LOCATION_FLAG               = new Integer(64025);
        public static final Integer MAIN_ATTENTION                      = new Integer(64026);
        public static final Integer MAIN_ADDRESS_1                      = new Integer(64027);
        public static final Integer MAIN_ADDRESS_2                      = new Integer(64028);
        public static final Integer MAIN_CITY                           = new Integer(64029);
        public static final Integer MAIN_STATE                          = new Integer(64030);
        public static final Integer MAIN_ZIP_CODE                       = new Integer(64031);
        public static final Integer MAIN_ZIP_4                          = new Integer(64032);
        public static final Integer MAIN_COUNTY                         = new Integer(64033);
        public static final Integer MAIN_PROVINCE                       = new Integer(64034);
        public static final Integer MAIN_COUNTRY                        = new Integer(64035);
        public static final Integer MAIN_BAD_ADDRESS_FLAG               = new Integer(64036);
        public static final Integer MAIN_DATE_MARKED_BAD                = new Integer(64037);
        public static final Integer SHIP_ATTENTION                      = new Integer(64038);
        public static final Integer SHIP_ADDRESS_1                      = new Integer(64039);
        public static final Integer SHIP_ADDRESS_2                      = new Integer(64040);
        public static final Integer SHIP_CITY                           = new Integer(64041);
        public static final Integer SHIP_STATE                          = new Integer(64042);
        public static final Integer SHIP_ZIP_CODE                       = new Integer(64043);
        public static final Integer SHIP_ZIP_4                          = new Integer(64044);
        public static final Integer SHIP_COUNTY                         = new Integer(64045);
        public static final Integer SHIP_PROVINCE                       = new Integer(64046);
        public static final Integer SHIP_COUNTRY                        = new Integer(64047);
        public static final Integer SHIP_BAD_ADDRESS_FLAG               = new Integer(64048);
        public static final Integer SHIP_DATE_MARKED_BAD                = new Integer(64049);
        public static final Integer OFFICE_COUNTRY_CODE                 = new Integer(64050);
        public static final Integer OFFICE_AREA_CODE                    = new Integer(64051);
        public static final Integer OFFICE_PHONE_NUMBER                 = new Integer(64052);
        public static final Integer OFFICE_BAD_PHONE_FLAG               = new Integer(64053);
        public static final Integer OFFICE_DATE_MARKED_BAD              = new Integer(64054);
        public static final Integer FAX_COUNTRY_CODE                    = new Integer(64055);
        public static final Integer FAX_AREA_CODE                       = new Integer(64056);
        public static final Integer FAX_PHONE_NUMBER                    = new Integer(64057);
        public static final Integer FAX_BAD_PHONE_FLAG                  = new Integer(64058);
        public static final Integer FAX_DATE_MARKED_BAD                 = new Integer(64059);
        public static final Integer AFF_ID_TYPE                         = new Integer(64060);
        public static final Integer AFF_ID_LOCAL                        = new Integer(64061);
        public static final Integer AFF_ID_STATE                        = new Integer(64062);
        public static final Integer AFF_ID_SUB_UNIT                     = new Integer(64063);
        public static final Integer AFF_ID_COUNCIL                      = new Integer(64064);
        public static final Integer STATUS                              = new Integer(64065);
        public static final Integer UNIT_WIDE_NO_MEMBER_CARDS           = new Integer(64066);
        public static final Integer UNIT_WIDE_NO_PE_MAIL                = new Integer(64067);
        public static final Integer NEW_AFF_ID_SOURCE                   = new Integer(64068);
        public static final Integer MBRSHP_INFO_RPRTG_SOURCE            = new Integer(64069);
        public static final Integer AFFILIATE_TITLE                     = new Integer(64070);
        public static final Integer NUM_WITH_TITLE                      = new Integer(64071);
        public static final Integer MONTH_OF_ELECTION                   = new Integer(64072);
        public static final Integer LENGTH_OF_TERM                      = new Integer(64073);
        public static final Integer TERM_END                            = new Integer(64074);
        public static final Integer DELEGATE_PRIORITY                   = new Integer(64075);
        public static final Integer REPORTING_OFFICER                   = new Integer(64076);
        public static final Integer E_BOARD                             = new Integer(64077);
        public static final Integer AUTO_E_BOARD_AFFILIATE_TITLE        = new Integer(64078);
        public static final Integer AUTO_E_BOARD_SUB_AFFILIATE_TITLE    = new Integer(64079);
    }

    /** Common Code Type 'MassChange' */
    public interface MassChange {
        public static final Integer MBR_INFO_RPRTG_SRC              = new Integer(22001);
        public static final Integer MBR_RENEWAL                     = new Integer(22002);
        public static final Integer NEW_AFFILIATE_ID                = new Integer(22003);
        public static final Integer UNIT_NO_CARDS                   = new Integer(22004);
        public static final Integer UNIT_NO_PE                      = new Integer(22005);
        public static final Integer STATUS_CHANGE_DEACTIVATED       = new Integer(22006);
        public static final Integer STATUS_CHANGE_MERGED            = new Integer(22007);
        public static final Integer STATUS_CHANGE_SPLIT             = new Integer(22008);
    }

    /** Common Code Type 'TermLength' */
    public interface TermLength {
        public static final Integer INDEFINITE  = new Integer(63005);
        public static final Integer TEMPORARY   = new Integer(63006);
    }

    /** Common Code Type 'RebateAcceptanceCode' */
    public interface RebateAcceptanceCode {
        public static final Integer C  = new Integer(54001);
        public static final Integer L  = new Integer(54002);
        public static final Integer D  = new Integer(54003);
    }

    /** Common Code Type 'RebateAppEvalCode' */
    public interface RebateAppEvalCode {
        public static final Integer NR  = new Integer(55001);
        public static final Integer NT  = new Integer(55002);
        public static final Integer Q1  = new Integer(55003);
        public static final Integer Q7  = new Integer(55004);
        public static final Integer QB  = new Integer(55005);
    }

    /** Common Code Type 'RebateDuration' */
    public interface RebateDuration {
        public static final Integer ONE     = new Integer(56001);
        public static final Integer TWO     = new Integer(56002);
        public static final Integer THREE   = new Integer(56003);
        public static final Integer FOUR    = new Integer(56004);
        public static final Integer FIVE    = new Integer(56005);
        public static final Integer SIX     = new Integer(56006);
        public static final Integer SEVEN   = new Integer(56007);
        public static final Integer EIGHT   = new Integer(56008);
        public static final Integer NINE    = new Integer(56009);
        public static final Integer TEN     = new Integer(56010);
        public static final Integer ELEVEN  = new Integer(56011);
        public static final Integer TWELVE  = new Integer(56012);
    }

    /** Common Code Type 'RebateMbrType' */
    public interface RebateMbrType {
        public static final Integer R  = new Integer(59001);
        public static final Integer T  = new Integer(59002);
        public static final Integer S  = new Integer(59003);
        public static final Integer U  = new Integer(59004);
        public static final Integer O  = new Integer(59005);
        public static final Integer A  = new Integer(59006);
        public static final Integer N  = new Integer(59007);
        public static final Integer K  = new Integer(59008);
    }

    /** Common Code Type 'RebateStatus' */
    public interface RebateStatus {
        public static final Integer A  = new Integer(75001);
        public static final Integer D  = new Integer(75002);
        public static final Integer IP = new Integer(75003);
    }

    /** Common Code Type 'PRBRosterStatus' */
    public interface PRBRosterStatus {
        public static final Integer P  = new Integer(77001);
        public static final Integer F  = new Integer(77002);
    }

    /** Common Code Type 'Gender' */
	public interface Gender {
	  	public static final Integer MALE  = new Integer(33001);
	   	public static final Integer FEMALE  = new Integer(33002);
    }

    /** Common Code Type 'Relation' */
	public interface Relation {
	  	public static final Integer CHILD  = new Integer(80001);
	   	public static final Integer SPOUSE_DOMESTIC_PARTNER  = new Integer(80002);
    }

    /** Common Code Type 'MemberUpdateFields' */
	public interface MemberUpdateFields {
		public static final Integer PREFIX = new Integer(74001);
		public static final Integer FIRST_NAME = new Integer(74002);
		public static final Integer MIDDLE_NAME = new Integer(74003);
		public static final Integer LAST_NAME = new Integer(74004);
		public static final Integer SUFFIX = new Integer(74005);
		public static final Integer PRIMARY_ADDRESS_1 = new Integer(74006);
		public static final Integer PRIMARY_ADDRESS_2 = new Integer(74007);
		public static final Integer CITY = new Integer(74008);
		public static final Integer STATE = new Integer(74009);
		public static final Integer ZIP = new Integer(74010);
		public static final Integer MAILABLE_ADDRESS_FLAG = new Integer(74011);
		public static final Integer NO_MAIL_FLAG = new Integer(74012);
		public static final Integer MEMBER_PHONE_NUMBER = new Integer(74013);
		public static final Integer MEMBER_STATUS = new Integer(74014);
		public static final Integer GENDER = new Integer(74015);
		public static final Integer DATE_JOINED = new Integer(74016);
		public static final Integer REGISTERED_VOTER = new Integer(74017);
		public static final Integer POLITICAL_PARTY = new Integer(74018);
		public static final Integer SOCIAL_SECURITY_NUMBER = new Integer(74019);
		public static final Integer PRIMARY_INFORMATION_SOURCE = new Integer(74020);
		public static final Integer UNIQUE_AFFILIATE_MEMBER_ID = new Integer(74021);
    }
        
    /** Common Code Type 'AfscmeTitle' */
    /** This is NOT a completed list.. **/
    public interface AfscmeTitle {
        public static final Integer INTERNATIONAL_PRESIDENT = new Integer(6001);
        public static final Integer INTERNATIONAL_SECRETARY_TREASURER = new Integer(6002);
        public static final Integer INTERNATIONAL_VICE_PRESIDENT = new Integer(6003);
        public static final Integer ADMINISTRATOR = new Integer(6004);
        public static final Integer DEPUTY_ADMINISTRATOR = new Integer(6005);
        public static final Integer EXECUTIVE_DIRECTOR = new Integer(6006);
        public static final Integer DIRECTOR = new Integer(6007);
        public static final Integer REGIONAL_DIRECTOR = new Integer(6008);
        public static final Integer PRESIDENT = new Integer(6009);
        public static final Integer CHAIRMAN = new Integer(6010);
    }       
}
