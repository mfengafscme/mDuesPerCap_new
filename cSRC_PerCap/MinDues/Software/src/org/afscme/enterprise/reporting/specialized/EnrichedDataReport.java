
package org.afscme.enterprise.reporting.specialized;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import  java.text.SimpleDateFormat;
import java.util.Map;
import org.afscme.enterprise.reporting.specialized.web.SpecializedReportForm;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.codes.CodeData;

/**
 * This is an enriched data specialized report.
 */
public class EnrichedDataReport implements ReportHandler {
    
    /**
     * The date format of the member joined date to be displayed on the report
     */
    private static final String DATE_FORMAT_STRING = "MM/yyyy";
    
    private Map suffixCodes;
    private Map prefixCodes;
    private Map countryCodes;
    private Map memberStatusCodes;
    private Map memberTypeCodes;
    private Map genderCodes;
    private Map registeredVoterCodes;
    private Map politicalPartyCodes;
    private Map informationSourceCodes;
    private String outputFormat;
    
    /**
     * The SQL that implements the report
     */
    private static final String QUERY =
    "SELECT Aff_Organizations.aff_type, Aff_Organizations.aff_localSubchapter, Aff_Organizations.aff_stateNat_type, " +
    "Aff_Organizations.aff_subUnit, Aff_Organizations.aff_councilRetiree_chap, Person.last_nm, Person.suffix_nm, " +
    "Person.first_nm, Person.middle_nm, Person.prefix_nm, Person_Address.addr1, Person_Address.addr2, " +
    "Person_Address.city, Person_Address.state, Person_Address.province, Person_Address.zipcode, Person_Address.zip_plus, " +
    "Person_Address.province, Person_Address.country, Person_Address.lst_mod_dt, " +
    "Person_Phone.country_cd, Person_Phone.area_code, Person_Phone.phone_no, " +
    "Aff_Members.mbr_no_local, Person.person_pk, Person_Address.addr_bad_fg, " +
    "Aff_Members.no_mail_fg, Aff_Members.mbr_status, Aff_Members.mbr_type, " +
    "Person.ssn, Person_Demographics.gender, Aff_Members.mbr_join_dt, " +
    "Person_Political_Legislative.political_registered_voter, Person_Political_Legislative.political_party, " +
    "Aff_Members.primary_information_source, COM_Democracy_Ranged.congdist, " +
    "Person_Political_Legislative.ward_number, Person_Political_Legislative.precinct_number, " +
    "COM_Democracy_Ranged.upperdist, COM_Democracy_Ranged.lowerdist, Person_Phone.phone_do_not_call_fg, " +
    "COM_AFL_CIO_COPE.person_pk AS person_pk1, " +
    "COM_AFL_CIO_COPE.gender AS gender1, " +
    "COM_AFL_CIO_COPE.political_registered_voter AS political_registered_voter1, " +
    "COM_AFL_CIO_COPE.congdist AS congdist1, " +
    "COM_AFL_CIO_COPE.upperdist AS upperdist1, " +
    "COM_AFL_CIO_COPE.lowerdist AS lowerdist1, " +
    "COM_AFL_CIO_COPE.ward_number AS ward_number1, " +
    "COM_AFL_CIO_COPE.precinct_number AS precinct_number1, " +
    "COM_AFL_CIO_COPE.political_party AS political_party1 " +
    "FROM Aff_Organizations " +
    "LEFT JOIN Aff_Members ON Aff_Organizations.aff_pk = Aff_Members.aff_pk " +
    "LEFT JOIN Person ON Aff_Members.person_pk = Person.person_pk " +
    "LEFT JOIN Person_SMA  ON Aff_Members.person_pk = Person_SMA.person_pk " +
    "AND Person_SMA.current_fg = 1 " +
    "LEFT JOIN Person_Address ON Person_SMA.address_pk = Person_Address.address_pk " +
    "LEFT JOIN Person_Phone ON Person.person_pk = Person_Phone.person_pk " +
    "AND Person_Phone.phone_prmry_fg = 1 " +
    "LEFT JOIN Person_Political_Legislative ON Person.person_pk = Person_Political_Legislative.person_pk " +
    "LEFT JOIN Person_Demographics ON Person.person_pk = Person_Demographics.person_pk " +
    "LEFT JOIN COM_Democracy_Ranged ON Person_Address.zipcode = COM_Democracy_Ranged.zipcode " +
    "AND Person_Address.zip_plus >= COM_Democracy_Ranged.start_zip_plus " +
    "AND Person_Address.zip_plus <= COM_Democracy_Ranged.stop_zip_plus " +
    "LEFT JOIN COM_AFL_CIO_COPE ON Aff_Members.person_pk = COM_AFL_CIO_COPE.person_pk " +
    "WHERE Aff_Organizations.aff_pk = ?";
    
    /** The affiliate primary key to search on */
    private Integer affPk;
        
    /**
     *  From the ReportHander interface.  Returning null means use the report name as the file name
     */
    public String getFileName() {
        return null;
    }
    
    /**
     * Generates an enriched data report.
     * @param stream The enriched data report is generated into this stream.
     * @return A return code.
     */
    public int generate(OutputStream stream) throws Exception {
        BufferedWriter writer = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String delimiter;
        
        try {
            MaintainCodes maintainCodes = JNDIUtil.getMaintainCodesHome().create();
            MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
            
            // get codes
            suffixCodes = maintainCodes.getCodes("Suffix");
            prefixCodes = maintainCodes.getCodes("Prefix");
            countryCodes = maintainCodes.getCodes("Country");
            memberStatusCodes = maintainCodes.getCodes("MemberStatus");
            memberTypeCodes = maintainCodes.getCodes("MemberType");
            genderCodes = maintainCodes.getCodes("Gender");
            registeredVoterCodes = maintainCodes.getCodes("RegisteredVoter");
            politicalPartyCodes = maintainCodes.getCodes("PoliticalParty");
            informationSourceCodes = maintainCodes.getCodes("InformationSource");
            
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            
            con = DBUtil.getConnection();
            stmt = con.prepareStatement(QUERY);
            stmt.setInt(1, affPk.intValue());
            rs = stmt.executeQuery();
            
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
            while (rs.next()) {
                writeValueWithDelimiter(writer, rs.getString("aff_type"), 1);
                writeValueWithDelimiter(writer, rs.getString("aff_localSubchapter"), 4);
                writeValueWithDelimiter(writer, rs.getString("aff_stateNat_type"), 2);
                writeValueWithDelimiter(writer, rs.getString("aff_subUnit"), 4);
                writeValueWithDelimiter(writer, rs.getString("aff_councilRetiree_chap"), 4);
                writeValueWithDelimiter(writer, rs.getString("last_nm"), 25);
                
                CodeData suffix = (CodeData)suffixCodes.get(DBUtil.getIntegerOrNull(rs, "suffix_nm"));
                if(suffix == null) {
                    writeValueWithDelimiter(writer, null, 25);
                } else {
                    writeValueWithDelimiter(writer, suffix.getDescription(), 25);
                }
                
                writeValueWithDelimiter(writer, rs.getString("first_nm"), 25);
                writeValueWithDelimiter(writer, rs.getString("middle_nm"), 25);
                
                CodeData prefix = (CodeData)prefixCodes.get(DBUtil.getIntegerOrNull(rs, "prefix_nm"));
                if(prefix == null) {
                    writeValueWithDelimiter(writer, null, 25);
                } else {
                    writeValueWithDelimiter(writer, prefix.getDescription(), 25);
                }
                writeValueWithDelimiter(writer, rs.getString("addr1"), 50);
                writeValueWithDelimiter(writer, rs.getString("addr2"), 50);
                writeValueWithDelimiter(writer, rs.getString("city"), 25);
                writeValueWithDelimiter(writer, rs.getString("state"), 2);
                writeValueWithDelimiter(writer, rs.getString("province"), 25);
                writeValueWithDelimiter(writer, rs.getString("zipcode"), 12);
                writeValueWithDelimiter(writer, rs.getString("zip_plus"), 4);
                
                CodeData country = (CodeData)countryCodes.get(DBUtil.getIntegerOrNull(rs, "country"));
                if(country == null) {
                    writeValueWithDelimiter(writer, null, 25);
                } else {
                    writeValueWithDelimiter(writer, country.getDescription(), 25);
                }
                
                writeValueWithDelimiter(writer, DateUtil.getSimpleDateString(rs.getTimestamp("lst_mod_dt")), 10);                
                writeValueWithDelimiter(writer, rs.getString("country_cd"), 5);
                writeValueWithDelimiter(writer, rs.getString("area_code"), 3);
                writeValueWithDelimiter(writer, rs.getString("phone_no"), 15);
                writeValueWithDelimiter(writer, rs.getString("mbr_no_local"), 14);
                writeValueWithDelimiter(writer, rs.getString("person_pk"), 8);
                
                // mailable if addr_bad_fg is false
                String mailableFlag = rs.getString("addr_bad_fg");
                if(mailableFlag != null && mailableFlag.equals("1")) {
                    mailableFlag = "0";
                } else if(mailableFlag != null && mailableFlag.equals("0")) {
                    mailableFlag = "1";
                }
                writeValueWithDelimiter(writer, mailableFlag, 1);                
                writeValueWithDelimiter(writer, rs.getString("no_mail_fg"), 1);
                
                CodeData mbrStatus = (CodeData)memberStatusCodes.get(DBUtil.getIntegerOrNull(rs, "mbr_status"));
                if(mbrStatus == null) {
                    writeValueWithDelimiter(writer, null, 1);
                } else {
                    writeValueWithDelimiter(writer, mbrStatus.getCode(), 1);
                }
                
                CodeData mbrType = (CodeData)memberTypeCodes.get(DBUtil.getIntegerOrNull(rs, "mbr_type"));
                if(mbrType == null) {
                    writeValueWithDelimiter(writer, null, 1);
                } else {
                    writeValueWithDelimiter(writer, mbrType.getCode(), 1);
                }
                
                writeValueWithDelimiter(writer, rs.getString("ssn"), 9);
                
                CodeData gender = (CodeData)genderCodes.get(DBUtil.getIntegerOrNull(rs, "gender"));
                if(gender == null) {
                    // check to see if gender value exists in COPE table
                    gender = (CodeData)genderCodes.get(DBUtil.getIntegerOrNull(rs, "gender1"));
                    if(gender == null) {
                        writeValueWithDelimiter(writer, null, 1);
                    } else {
                        writeValueWithDelimiter(writer, gender.getCode(), 1);
                    }
                } else {
                    writeValueWithDelimiter(writer, gender.getCode(), 1);
                }
                
                String memberjoinDate = null;
                Timestamp mbrJoinDt = rs.getTimestamp("mbr_join_dt");
                if(mbrJoinDt != null) {
                    memberjoinDate = sdf.format(mbrJoinDt);
                }
                writeValueWithDelimiter(writer, memberjoinDate, 10);
                
                if(rs.getString("person_pk1") == null) {
                    writePoliticalData(writer, rs, "");
                } else {
                    // get data from COPE table
                    writePoliticalData(writer, rs, "1");
                }
                
                writeValueWithoutDelimiter(writer, rs.getString("phone_do_not_call_fg"), 1);
                writer.newLine();
            }
        } finally {
            DBUtil.cleanup(con, stmt, rs);
            writer.flush();
            writer.close();
        }
        return 1;
    }
        
    private void writeValueWithDelimiter(BufferedWriter writer, String value, int maximumLength) throws IOException {
        if(value == null) {
            value = "";
        }
        while(value.length() < maximumLength) {
            value += " ";
        }
        writer.write(value);
        writer.write(getOutputFormat());
    }
    
    private void writeValueWithoutDelimiter(BufferedWriter writer, String value, int maximumLength) throws IOException {
        if(value == null) {
            value = "";
        }
        while(value.length() < maximumLength) {
            value += " ";
        }
        writer.write(value);
    }

    private void writePoliticalData(BufferedWriter writer, ResultSet rs, String appendValue) throws IOException, SQLException {
        CodeData registeredVoter = (CodeData)registeredVoterCodes.get(DBUtil.getIntegerOrNull(rs, "political_registered_voter" + appendValue));
        if(registeredVoter == null) {
            writeValueWithDelimiter(writer, null, 1);
        } else {
            writeValueWithDelimiter(writer, registeredVoter.getCode(), 1);
        }
        
        CodeData politicalParty = (CodeData)politicalPartyCodes.get(DBUtil.getIntegerOrNull(rs, "political_party" + appendValue));
        if(politicalParty == null) {
            writeValueWithDelimiter(writer, null, 1);
        } else {
            writeValueWithDelimiter(writer, politicalParty.getCode(), 1);
        }
        
        
        CodeData informationSource = (CodeData)informationSourceCodes.get(DBUtil.getIntegerOrNull(rs, "primary_information_source" + appendValue));
        if(informationSource == null) {
            writeValueWithDelimiter(writer, null, 1);
        } else {
            writeValueWithDelimiter(writer, informationSource.getCode(), 1);
        }
        
        writeValueWithDelimiter(writer, rs.getString("congdist" + appendValue), 2);
        writeValueWithDelimiter(writer, rs.getString("ward_number"  + appendValue), 4);
        writeValueWithDelimiter(writer, rs.getString("precinct_number" + appendValue), 10);
        writeValueWithDelimiter(writer, rs.getString("upperdist" + appendValue), 3);
        writeValueWithDelimiter(writer, rs.getString("lowerdist" + appendValue), 3);
    }
    
    /**
     * Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return this.affPk;
    }
    
    /**
     * Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property outputFormat.
     * @return Value of property outputFormat.
     *
     */
    public java.lang.String getOutputFormat() {
        return outputFormat;
    }
    
    /** Setter for property outputFormat.
     * @param outputFormat New value of property outputFormat.
     *
     */
    public void setOutputFormat(java.lang.String outputFormat) {
        if (outputFormat.equalsIgnoreCase(SpecializedReportForm.TAB)) {
            this.outputFormat = "\t";
        } else if (outputFormat.equalsIgnoreCase(SpecializedReportForm.SEMICOLON)) {
            this.outputFormat = ";";
        } else if (outputFormat.equalsIgnoreCase(SpecializedReportForm.COMMA)) {
            this.outputFormat = ",";
        }
    }
    
}
