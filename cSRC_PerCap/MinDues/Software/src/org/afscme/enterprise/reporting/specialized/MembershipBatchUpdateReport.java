
package org.afscme.enterprise.reporting.specialized;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.codes.Codes;

/**
 * This is a membership batch update specialized report.
 */
public class MembershipBatchUpdateReport implements ReportHandler {

    /**
     * The SQL that implements the report
     */
    private static final String QUERY =
    	"SELECT Aup_RV_Err_Smry.person_pk, Aup_RV_Err_Smry.aff_pk, Aup_RV_Err_Smry.record_id, Aup_Rv_Err_Dtl.upd_fld_nm, " +
    	"Aup_Rv_Err_Dtl.fld_value_in_file, " +
		"Aff_Organizations.aff_localSubChapter, Aff_Organizations.aff_subUnit, Aup_RV_Err_Smry.last_nm, Aup_RV_Err_Smry.first_nm, Aff_Organizations.aff_type, Aup_RV_Smry.trans_attempted_cnt " +
		"FROM Aup_RV_Err_Smry, Aup_Rv_Err_Dtl, Aff_Organizations, Aup_RV_Smry " +
		"WHERE Aup_RV_Err_Smry.aff_pk =  Aup_Rv_Err_Dtl.aff_pk " +
		"AND Aup_RV_Err_Smry.queue_pk =  Aup_Rv_Err_Dtl.queue_pk " +
		"AND Aup_RV_Err_Smry.record_id =  Aup_Rv_Err_Dtl.record_id " +
		"AND Aup_Rv_Err_Dtl.aff_pk = Aff_Organizations.aff_pk " +
		"AND Aup_RV_Smry.aff_pk = Aup_RV_Err_Smry.aff_pk " +
		"AND Aup_RV_Smry.queue_pk = Aup_RV_Err_Smry.queue_pk " +
		"AND Aup_RV_Smry.queue_pk = ? " +
		"ORDER BY Aff_Organizations.aff_localSubChapter ASC, " +
		"Aff_Organizations.aff_subUnit ASC, Aup_RV_Err_Smry.last_nm ASC, Aup_RV_Err_Smry.first_nm ASC";

	/** The queue primary key to search on */
    private Integer queuePk;

	/**
	 *  From the ReportHandler interface.  Returning null means use the report name as the file name
	 */
	public String getFileName() {
	 	return null;
	}

    /**
     * Generates a membership batch update report.
     * @param stream The report is generated into this stream.
     * @return A return code.
     */
    public int generate(OutputStream stream)
    throws Exception
    {
		BufferedWriter writer = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
   		try {

        	writer = new BufferedWriter(new OutputStreamWriter(stream));

			con = DBUtil.getConnection();
			stmt = con.prepareStatement(QUERY);
			stmt.setInt(1, getQueuePk().intValue());
			rs = stmt.executeQuery();

			// map consists of record id and aff pk
			HashMap affRecordMap = new HashMap();
			// stores list of keys, need to sort
			ArrayList list = new ArrayList();
			while (rs.next()) {
				String affRecord = rs.getString("aff_pk") + rs.getString("record_id");
				HashMap fieldMap = (HashMap)affRecordMap.get(affRecord);
				if(fieldMap == null) {
					// must be a unique record so add to the map
					fieldMap = new HashMap();
					fieldMap.put("transCmpltdCnt", new Integer(rs.getInt("trans_attempted_cnt")));
					fieldMap.put("personPk", rs.getString("person_pk"));
					fieldMap.put("unit", rs.getString("aff_type") + rs.getString("aff_localSubChapter"));
					fieldMap.put("subLocal", rs.getString("aff_subUnit"));
					fieldMap.put("affType", rs.getString("aff_type"));

					affRecordMap.put(affRecord, fieldMap);
					list.add(affRecord);
				}
				fieldMap.put(new Integer(rs.getInt("upd_fld_nm")), rs.getString("fld_value_in_file"));
			}


			// write tile of report
			writer.newLine();
			writer.newLine();
			writer.write(" 020327                         AMERICAN FEDERATION OF STATE, COUNTY AND MUNICIPAL EMPLOYEES, AFL-CIO                     PAGE      1");
			writer.newLine();
			writer.newLine();
            writer.write("                                                     BATCH UPDATE ERROR REPORT");

			// write header
			writer.newLine();
			writer.write("            MEMBER KEY             AUXILIARY ADDRESS               STATUS TYPE   TITLE         TELEPHONE     DATE JOINED    UPDATED");
			writer.newLine();
			writer.write("NAME                               PRIMARY ADDRESS                   UNIT          SEX                         REG. VOTER    TRANS");
			writer.newLine();
			writer.write("              MEMBER NUMBER        CITY            ST  ZIP             SUBLOCAL      MAILABLE                    PARTY        SOURCE");
			writer.newLine();
			writer.newLine();

			Iterator i = list.iterator();
			while(i.hasNext()) {
				String affRecord = (String)i.next();

				// write first row
				HashMap fieldMap = (HashMap)affRecordMap.get(affRecord);
				writeValue(writer, "", 12);
				writeValue(writer, (String)fieldMap.get("personPk"), 23);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.PRIMARY_ADDRESS_2), 32);

				String memberType = null;
				String memberStatus = (String)fieldMap.get(Codes.MemberUpdateFields.MEMBER_STATUS);
				String affType = (String)fieldMap.get("affType");

				// determine what the value of memberType should be
				if(memberStatus != null && (memberStatus.equals("A") || memberStatus.equals("I") || memberStatus.equals("T"))
				   && (affType.equals("R") || affType.equals("S"))) {
					memberType = "T";
				}
				else if(memberStatus != null && (memberStatus.equals("A") || memberStatus.equals("I") || memberStatus.equals("T"))) {
					memberType = "R";
				}

				writeValue(writer, memberStatus, 7);
				writeValue(writer, memberType, 7);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.PREFIX), 14);

				// the country code column is not available, so there is no value to get
				writeValue(writer, "", 4);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.MEMBER_PHONE_NUMBER), 10);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.DATE_JOINED), 15);

				// the updated column is not available, so there is no value to get
				writeValue(writer, "", 14);
				writer.newLine();

				// write name in format lastName, firstName middleName
				String firstName = (String)fieldMap.get(Codes.MemberUpdateFields.FIRST_NAME);
				String middleName = (String)fieldMap.get(Codes.MemberUpdateFields.MIDDLE_NAME);
				String lastName = (String)fieldMap.get(Codes.MemberUpdateFields.LAST_NAME);

				int lastNameSize = 0;
				int middleNameSize = 0;
				int firstNameSize = 0;

				if(lastName == null)
					lastNameSize = 0;
				else
					lastNameSize = lastName.length();
				if(middleName == null)
					middleNameSize = 0;
				else
					middleNameSize = middleName.length();
				if(firstName == null)
					firstNameSize = 0;
				else
					firstNameSize = firstName.length();

				// write second row
				writeValue(writer, lastName, lastNameSize);
				writeValue(writer, ", ", 2);
				writeValue(writer, firstName, firstNameSize);
				writeValue(writer, " ", 1);
				writeValue(writer, middleName, middleNameSize);
				writeValue(writer, "", 35 - (lastNameSize + middleNameSize + firstNameSize + 3));

				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.PRIMARY_ADDRESS_1), 34);
				writeValue(writer, (String)fieldMap.get("unit"), 14);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.GENDER), 14);
				writeValue(writer, "", 14);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.REGISTERED_VOTER), 14);
				writeValue(writer, ((Integer)fieldMap.get("transCmpltdCnt")).toString(), 13);
				writer.newLine();

				// write final row
				writeValue(writer, "", 14);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.UNIQUE_AFFILIATE_MEMBER_ID), 21);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.CITY), 16);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.STATE), 4);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.ZIP), 16);
				writeValue(writer, (String)fieldMap.get("subLocal"), 14);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.MAILABLE_ADDRESS_FLAG), 14);
				writeValue(writer, "", 14);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.POLITICAL_PARTY), 13);
				writeValue(writer, (String)fieldMap.get(Codes.MemberUpdateFields.PRIMARY_INFORMATION_SOURCE), 12);
				writer.newLine();
				writer.newLine();
			}
			writer.newLine();

			// write total members
			int total = affRecordMap.size();
			writer.write("        " + total + " MEMBER" + (total == 1 ? "" : "S"));
		} finally {
			DBUtil.cleanup(con, stmt, rs);
			writer.flush();
			writer.close();
		}
        return 1;
    }

	private void writeValue(BufferedWriter writer, String value, int maximumLength)
	throws IOException
	{
		if(value == null) {
			value = "";
		}
		while(value.length() < maximumLength) {
			value += " ";
		}
		writer.write(value);
	}

    /**
     * Getter for property queuePk.
     * @return Value of property queuePk.
	 *
	 */
	public Integer getQueuePk() {
		return this.queuePk;
	}

	/**
     * Setter for property queuePk.
	 * @param queuePk New value of property queuePk.
     *
	 */
	public void setQueuePk(Integer queuePk) {
		this.queuePk = queuePk;
    }
}

