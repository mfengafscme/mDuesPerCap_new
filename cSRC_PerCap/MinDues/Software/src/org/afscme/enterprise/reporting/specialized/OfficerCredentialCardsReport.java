
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
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * This is a officer credential cards specialized report.
 */
public class OfficerCredentialCardsReport implements ReportHandler {

	/**
	 * The date format of the position enddate to be displayed on the report
     */
	private static final String DATE_FORMAT_STRING = "MMM yyyy";

    /**
     * The SQL that implements the report
     */
    private static final String QUERY =
    	"SELECT Aff_Organizations.aff_stateNat_type, Aff_Organizations.aff_councilRetiree_chap, " +
    	"Aff_Organizations.aff_localSubchapter, Common_Codes.com_cd_desc, Person.first_nm, " +
    	"Person.middle_nm, Person.last_nm, Officer_History.pos_end_dt, Aff_Organizations.aff_type " +
		"FROM Aff_Organizations, AFSCME_Offices, Person, Officer_History, Common_Codes " +
		"WHERE Aff_Organizations.aff_pk = Officer_History.aff_pk " +
		"AND Person.person_pk = Officer_History.person_pk  " +
		"AND AFSCME_Offices.afscme_office_pk = Officer_History.afscme_office_pk " +
		"AND AFSCME_Offices.afscme_title_nm = Common_Codes.com_cd_pk " +
		"AND Officer_History.lst_mod_dt >= ? and Officer_History.lst_mod_dt <= ? " +
		"ORDER BY Aff_Organizations.aff_stateNat_type ASC, Aff_Organizations.aff_councilRetiree_chap ASC, " +
		"Aff_Organizations.aff_localSubchapter ASC";

    /** The from date to search on */
    private String fromDate;

    /** The to date to search on */
    private String toDate;

	/**
	 *  From the ReportHander interface.  Returning null means use the report name as the file name
	 */
	public String getFileName() {
		return null;
	}

    /**
     * Generates a officer credential cards report.
     * @param stream The officer credential cards report is generated into this stream.
     * @return A return code.
     */
    public int generate(OutputStream stream)
    throws Exception
    {
		BufferedWriter writer = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;;
   		try {
        	writer = new BufferedWriter(new OutputStreamWriter(stream));
        	writeColumnSpace(writer, 38);
        	writer.write("OFFICER CREDENTIAL CARDS");
			writer.newLine();

			con = DBUtil.getConnection();

			stmt = con.prepareStatement(QUERY);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			rs = stmt.executeQuery();
			int stateCount = 0;
			int totalCount = 0;
			String previousState = null;
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
			while (rs.next()) {

				// print header if this is the first row
				if(stateCount == 0) {
					writeHeader(writer);
				}
				String currentState = rs.getString(1);

				// if the state is not different from the last row increase the counter, otherwise set it to one
				if(previousState == null || currentState.equals(previousState)) {
					stateCount++;
				}
				else {
					// print total for the state
					writeStateTotal(writer, stateCount);
					totalCount += (stateCount);
					stateCount = 1;
					// print header since this is a new state
					writeHeader(writer);
				}

				// write the row
				writeColumnSpace(writer, 21);
				writeValue(writer, currentState, 2);
				writeColumnSpace(writer, 4);
				writeValue(writer, rs.getString(2), 4);
				writeColumnSpace(writer, 3);
				writeValue(writer, rs.getString(3), 4);
				writeColumnSpace(writer, 5);

				String titleName = rs.getString(4);
				if(titleName == null) {
					writeValue(writer, "", 50);
				}
				else {
					writeValue(writer, titleName, 50);
				}
				writeColumnSpace(writer, 3);

				String firstName = rs.getString(5);
				String middleName = rs.getString(6);
				String lastName = rs.getString(7);
				String fullName = "";

				if(!TextUtil.isEmpty(firstName)) {
					fullName += (firstName + " ");
				}

				if(!TextUtil.isEmpty(middleName)) {
					fullName += (middleName+ " ");
				}

				if(!TextUtil.isEmpty(lastName)) {
					fullName += (lastName);
				}

				if(fullName == null) {
					writeValue(writer, "", 75);
				}
				else {
					writeValue(writer, fullName, 75);
				}
				writeColumnSpace(writer, 3);

				Timestamp positionEndDate = rs.getTimestamp(8);
				if(positionEndDate != null) {
					writeValue(writer, sdf.format(positionEndDate), 8);
				}
				else {
					writeValue(writer, "", 8);
				}
				writeColumnSpace(writer, 2);

				writeValue(writer, rs.getString(9), 1);
				writer.newLine();
				previousState = currentState;
			}
			totalCount += stateCount;
			if(totalCount > 0) {
				writeStateTotal(writer, stateCount);
			}
			writeHeader(writer);
			writeColumnSpace(writer, 4);
			writer.write("TOTAL CARDS - ");
			writeTotal(writer, totalCount);

		} finally {
        	DBUtil.cleanup(con, stmt, rs);
			writer.flush();
        	writer.close();
		}
        return 1;
    }

    /**
     * Getter for property fromDate.
	 * @return Value of property fromDate.
	 *
     */
    public String getFromDate() {
		return this.fromDate;
    }

    /**
	 * Setter for property fromDate.
     * @param mailingListId New value of property fromDate.
	 *
	 */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Getter for property toDate.
     * @return Value of property toDate.
	 *
	 */
	public String getToDate() {
		return this.toDate;
	}

	/**
     * Setter for property toDate.
	 * @param personPk New value of property toDate.
     *
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
    }

    private void writeHeader(BufferedWriter writer)
    throws IOException
    {
		writeColumnSpace(writer, 20);
		writer.write("STATE");
		writeColumnSpace(writer, 1);
		writer.write("COUNCIL");
		writeColumnSpace(writer, 1);
		writer.write("LOCAL");
		writeColumnSpace(writer, 4);
		writer.write("OFFICE");
		writeColumnSpace(writer, 54);
		writer.write("NAME");
		writeColumnSpace(writer, 69);
		writer.write("DATE");
		writeColumnSpace(writer, 3);
		writer.write("TYPE");
		writer.newLine();
	}

	private void writeColumnSpace(BufferedWriter writer, int totalSpace)
	throws IOException
	{
		for(int i  = 0; i < totalSpace; i++) {
			writer.write(" ");
		}
	}

	private void writeValue(BufferedWriter writer, String value, int maximumLength)
	throws IOException
	{
		while(value.length() < maximumLength) {
			value += " ";
		}
		writer.write(value);
	}

	private void writeStateTotal(BufferedWriter writer, int total)
	throws IOException
	{
		writeColumnSpace(writer, 20);
		writer.write("STATE TOTAL");
		writer.newLine();
		writeColumnSpace(writer, 10);
		writeTotal(writer, total);
		writer.newLine();
	}

	private void writeTotal(BufferedWriter writer, int total)
	throws IOException
	{
		String totalString = String.valueOf(total);
		while(totalString.length() < 4) {
			totalString = "0" + totalString;
		}
		writer.write(totalString);
	}
}
