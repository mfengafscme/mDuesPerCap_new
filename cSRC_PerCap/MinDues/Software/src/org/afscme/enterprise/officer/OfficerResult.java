package org.afscme.enterprise.officer;

import java.sql.Timestamp;

/**
 * Represents a single result returned by an Officer search.
 */
public class OfficerResult {
    
    private String firstNm;
    private String middleNm;
    private String lastNm;
    private String suffixNm;
    private String affType;
    private String affLocalSubChapter;
    private String affStateNatType;
    private String affCouncilRetireeChapter;
    private String affCode;
    private String afscmeTitleNm;
    private Timestamp termEnd;
    private Boolean reportingOfficerFg;
}
