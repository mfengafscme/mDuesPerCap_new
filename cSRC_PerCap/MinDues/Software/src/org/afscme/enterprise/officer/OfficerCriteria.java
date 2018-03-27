package org.afscme.enterprise.officer;

import java.sql.Timestamp;
import org.afscme.enterprise.person.PersonCriteria;

/**
 * Contains data used to query for officers.  Includes information about which 
 * result fields are desired, sort information and pagining information.
 */
public class OfficerCriteria extends PersonCriteria 
{
    private String affType;
    private String affLocalSubChapter;
    private String affStateNatType;
    private String affSubUnit;
    private String affCouncilRetireeChaper;
    private String affCode;
    private String mbrNumber;
    private String afscmeTitleNm;
    private Boolean suspendedFg;
    private Boolean stewards;
    private Integer primaryInformationSource;
    private Timestamp termBegin;
    private Timestamp termEnd;
    private Timestamp lstModifiedDate;
    
}
