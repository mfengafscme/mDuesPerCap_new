
package org.afscme.enterprise.reporting.specialized;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.afscme.enterprise.codes.Codes;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * This is a orgmailing list specialized report.
 */
public class OrgMailingListReport implements ReportHandler {

    /**
     * The SQL that implements the report
     * NOTE:  do not append an order by clause to this query string because
     *one will be added in the getQuery() method if duplicate addresses need
     *to be filtered
     */
    private static final String QUERY =
        "SELECT Aff_Organizations.aff_pk, External_Organizations.org_pk, Aff_Organizations.aff_abbreviated_nm, " +
        "External_Organizations.org_nm, Org_Address.addr1, Org_Address.addr2, Org_Address.city, " +
        "Org_Address.province, Org_Address.state, Org_Address.zipcode, Org_Address.zip_plus, Common_Codes.com_cd_desc, " +
        "Org_Address.attention_line, Mailing_List_Orgs.mailing_list_bulk_cnt " +
        "FROM Mailing_List_Orgs " +
		"LEFT JOIN Aff_Organizations ON Mailing_List_Orgs.org_pk = Aff_Organizations.aff_pk " +
		"LEFT JOIN External_Organizations ON Mailing_List_Orgs.org_pk = External_Organizations.org_pk " +
		"LEFT JOIN Org_Address ON Mailing_List_Orgs.org_locations_pk = Org_Address.org_locations_pk " +
		"LEFT JOIN Common_Codes ON Org_Address.country = Common_Codes.com_cd_pk " +
		"WHERE Aff_Organizations.aff_status not in (17004,17009)" + //not in peding deactivation or deactivated
		" AND Org_Address.org_addr_type = " + Codes.OrgAddressType.REGULAR +
		" AND Mailing_List_Orgs.MLBO_mailing_list_pk = ?";

    /** The id to search on */
    private int mailingListId;
    /** The source to be added at the end of each line of the report */
    private Integer personPk;

    /**Holds the value of the property for filtering duplicate addresses*/
    private boolean filterDuplicateAddresses;
    
    /** Setter method for the filterDuplicateAddresses property*/
    public void setFilterDuplicateAddresses(boolean value)
    {
        filterDuplicateAddresses = value;
    }
    
    /** getter for the filterDuplicateAddresses property*/    
    public boolean isFilterDuplicateAddresses()
    {
        return filterDuplicateAddresses;
    }

	/**
	 *  From the ReportHander interface.  Returning null means use the report name as the file name
	 */
	public String getFileName() {
		return null;
	}

    /**
     * Generates a mailing list report and adds correspondence data to each person's correspondence history
     * @param stream The mailing list report is generated into this stream.
     * @return A return code.
     */
    public int generate(OutputStream stream)
    throws Exception
    {
        Logger log = Logger.getLogger(this.getClass());
        log.debug("isFilterDuplicateAddresses() ====> " + this.isFilterDuplicateAddresses());
		BufferedWriter writer = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;;
   		try {
        	writer = new BufferedWriter(new OutputStreamWriter(stream));

			con = DBUtil.getConnection();

			// Construct the specialized SQL query or a series of queries for this report
			stmt = con.prepareStatement(getQuery());
			stmt.setInt(1, mailingListId);
			rs = stmt.executeQuery();
            
            //check if duplicate addresses should be filtered out.
            DuplicateAddressFilter dupAddressFilter = null;
			if(isFilterDuplicateAddresses())
            { //only instantiate if they are going to be used
                dupAddressFilter= new DuplicateAddressFilter();
            }
            
            while(rs.next()) {
				int orgPk = rs.getInt(1);
				int affPk = rs.getInt(2);
				String affAbbreviatedName = rs.getString(3);
				String orgName = rs.getString(4);
				String addr1 = rs.getString(5);
				String addr2 = rs.getString(6);
				String city = rs.getString(7);
				String province = rs.getString(8);
				String state = rs.getString(9);
				String zipCode = rs.getString(10);
				String zipPlus = rs.getString(11);
				String country = rs.getString(12);
				String attentionLine = rs.getString(13);
                
                if(dupAddressFilter != null)
                {
                    if(dupAddressFilter.isAddressDuplicate(addr1, addr2, city, state, province, zipCode, zipPlus, country))
                        continue;                    
                }
                
                
				int bulkCount = rs.getInt(14);

				// one of the primary keys will not be zero and one will be zero
				if(orgPk != 0) {
					writer.write(orgPk + ",");
				}
				else {
					writer.write(affPk + ",");
				}

				if(!TextUtil.isEmpty(affAbbreviatedName)) {
					writer.write(affAbbreviatedName + ",");
				}
				else if(!TextUtil.isEmpty(orgName)) {
					writer.write(orgName + ",");
				}
				else {
					writer.write(",");
				}

				if(!TextUtil.isEmpty(attentionLine)) {
					writer.write(attentionLine);
				}
				else {
					writer.write("");
				}
				writer.write("," );

				writer.write(addr1 + ",");

				if(!TextUtil.isEmpty(addr2)) {
					writer.write(addr2 + ",");
				}
				else  {
					writer.write(",");
				}

				if(!TextUtil.isEmpty(city)) {
					writer.write(city + ",");
				}
				else  {
					writer.write(",");
				}

				// assuming that all "other" countries will have provinces
				if(!TextUtil.isEmpty(country) && country.equals("United States")) {
					if(!TextUtil.isEmpty(state)) {
						writer.write(state + "," );
					}
					else {
						writer.write("," );
					}
				}
				else if(!TextUtil.isEmpty(country) && !country.equals("United States")) {
					if(!TextUtil.isEmpty(province)) {
						writer.write(province + "," );
				}
					else {
						writer.write("," );
					}
				}
				else if(!TextUtil.isEmpty(province)) {
					writer.write(province + "," );
				}
				else if(!TextUtil.isEmpty(state)) {
					writer.write(state + "," );
				}
				else {
					writer.write("," );
				}

				if(!TextUtil.isEmpty(zipCode)) {
					writer.write(zipCode);
				}
				if(!TextUtil.isEmpty(zipCode) && !TextUtil.isEmpty(zipPlus)) {
					writer.write("-" + zipPlus);
				}
				else if(!TextUtil.isEmpty(zipPlus)) {
					writer.write(zipPlus);
				}
				writer.write("," );

				if(!TextUtil.isEmpty(country)) {
					writer.write(country);
				}
				else {
					writer.write("");
				}
				writer.write("," );

				if(bulkCount != 0) {
					writer.write(String.valueOf(bulkCount));
				}
				else {
					writer.write("");
				}

				writer.write("," + getPersonPk());
				writer.newLine();
			}
		} finally {
        	DBUtil.cleanup(con, stmt, rs);
			writer.flush();
        	writer.close();
		}
        return 1;
    }

    /** Getter for property mailingListId.
	 * @return Value of property mailingListId.
	 *
     */
    public int getMailingListId() {
		return this.mailingListId;
    }

    /**
	 * Setter for property mailingListId.
     * @param mailingListId New value of property mailingListId.
	 *
	 */
    public void setMailingListId(int mailingListId) {
        this.mailingListId = mailingListId;
    }

    /** Getter for property personPk.
     * @return Value of property personPk.
	 *
	 */
	public Integer getPersonPk() {
		return this.personPk;
	}

	/**
     * Setter for property personPk.
	 * @param personPk New value of property personPk.
     *
	 */
	public void setPersonPk(Integer personPk) {
		this.personPk = personPk;
    }
    
    /**
     *Gets the query to retrive the mailing list.  It appends the order by
     *needed for filtering out duplicate addresses if isFilterDuplicateAddresses()
     *= true
     */
    private String getQuery()
    {
        if(this.isFilterDuplicateAddresses())
            return QUERY + " order by addr1, addr2, city, state, zipcode, zip_plus, province, country";
        return QUERY;
    }
}
