
package org.afscme.enterprise.reporting.specialized;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.afscme.enterprise.person.ejb.MaintainPersonMailingLists;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * This is a mailing list specialized report.
 */
public class MailingListReport implements ReportHandler {

    /**
     * Primary key of this report in the Reports table
     */
    public static final Integer REPORT_PK = new Integer(2);

    /**
     * The SQL that implements the report
     * NOTE:  do not append an order by clause to this query string because
     *one will be added in the getQuery() method if duplicate addresses need
     *to be filtered
     */
    private static final String QUERY =
        "SELECT  Person.person_pk, Person.alternate_mailing_nm, Person.first_nm, Person.middle_nm, " +
        "Person.last_nm, cc1.com_cd_desc, Person_Address.addr1, Person_Address.addr2, Person_Address.City, Person_Address.state, " +
        "Person_Address.province, Person_Address.zipcode, Person_Address.zip_plus, cc.com_cd_desc " +
		"from MLBP_Persons " +
		"inner join Person on  MLBP_Persons.person_pk = Person.person_pk " +
		"inner join Person_Address on MLBP_Persons.address_pk = Person_Address.address_pk " +
    	"left outer join Person_Demographics on Person_Demographics.person_pk = Person.person_pk " +
		"left join Common_Codes cc on Person_Address.country = cc.com_cd_pk " +
 		"left join Common_Codes cc1 on Person.suffix_nm = cc1.com_cd_pk " +
		"WHERE isnull(Person.marked_for_deletion_fg, 0) = 0 " +
		"and isnull(Person_Demographics.deceased_fg, 0) = 0 " +
		"and MLBP_Persons.mlbp_mailing_list_pk = ?";

    /**
     * Gets the mailing list name file name
     */
    private static final String NAME_QUERY =
        "SELECT MLBP_Mailing_list_nm FROM mailing_lists_by_person WHERE mlbp_mailing_list_pk = ?";

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
            
			while (rs.next()) {
				String alternateMailingName = rs.getString(2);
				String addr1 = rs.getString(7);
				String addr2 = rs.getString(8);
				String city = rs.getString(9);
				String state = rs.getString(10);
				String province = rs.getString(11);
				String zipCode = rs.getString(12);
				String zipPlus = rs.getString(13);
				String country = rs.getString(14);
                
                if(dupAddressFilter != null)
                {
                    if(dupAddressFilter.isAddressDuplicate(addr1, addr2, city, state, province, zipCode, zipPlus, country))
                        continue;                    
                }
                
				writer.write(rs.getInt(1) + ",");
				if(alternateMailingName == null || alternateMailingName.equals("")) {
					String firstName = rs.getString(3);
					String middleName = rs.getString(4);
					String lastName = rs.getString(5);
					String suffixName = rs.getString(6);

					boolean addSpace = false;
					if(!TextUtil.isEmpty(firstName)) {
						addSpace = true;
						writer.write(firstName);
					}
					if(!TextUtil.isEmpty(middleName)) {
						if(addSpace) {
							writer.write(" ");
						}
						else {
							addSpace = true;
						}
						writer.write(middleName);
					}
					if(!TextUtil.isEmpty(lastName)) {
						if(addSpace) {
							writer.write(" ");
						}
						else {
							addSpace = true;
						}
						writer.write(lastName);
					}
					if(!TextUtil.isEmpty(suffixName)) {
						if(addSpace) {
							writer.write(" ");
						}
						writer.write(suffixName);
					}
				}
				else if(alternateMailingName != null && !alternateMailingName.equals("")) {
					writer.write(alternateMailingName);
				}

				writer.write(",");

				if(addr1 != null && !addr1.equals("")) {
					writer.write(addr1);
				}
				writer.write(",");

				if(addr2 != null && !addr2.equals("")) {
					writer.write(addr2);
				}
				writer.write(",");

				if(city != null && !city.equals("")) {
					writer.write(city);
				}
				writer.write(",");

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

				if(zipCode != null && !zipCode.equals("")) {
					writer.write(zipCode);
				}
				if(zipCode != null && !zipCode.equals("") && zipPlus != null && !zipPlus.equals("")) {
					writer.write("-" + zipPlus);
				}
				else if(zipPlus != null && !zipPlus.equals("")) {
					writer.write(zipPlus);
				}
				writer.write(",");

				if(country != null && !country.equals("")) {
					writer.write(country);
				}
				writer.write("," + getPersonPk());
				writer.newLine();
			}
			updateCorrespondenceHistory();
		} finally {
	       	DBUtil.cleanup(con, stmt, rs);
			writer.flush();
	       	writer.close();
		}
		return 0;
   }

    /**
     * Adds correspondence history for all the persons on this mailing list
     */
    private void updateCorrespondenceHistory() throws Exception {
        MaintainPersonMailingLists bean = JNDIUtil.getMaintainPersonMailingListsHome().create();
        bean.addCorrespondences(getMailingListId());
        bean.remove();
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
     * Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     */
    public String getFileName() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;;
        String name;
   		try {
			con = DBUtil.getConnection();

			stmt = con.prepareStatement(NAME_QUERY);
			stmt.setInt(1, mailingListId);
			rs = stmt.executeQuery();
            rs.next();
            name = rs.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);  //have to supress this, interface doesn't expect checked exceptions
		} finally {
        	DBUtil.cleanup(con, stmt, rs);
		}
        return name;
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
