package org.afscme.enterprise.returnedmail.ejb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import javax.naming.NamingException;
import javax.naming.Context; 
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.address.ejb.SystemAddress;
import org.afscme.enterprise.returnedmail.*;
import java.rmi.RemoteException;
import javax.ejb.*;
import org.apache.log4j.Logger;


/**
 * Encapsulates access to process returned mail data.
 *
 * @ejb:bean name="ProcessReturnedMail" display-name="ProcessReturnedMail"
 * jndi-name="ProcessReturnedMail"
 * type="Stateless" view-type="local"
 */

public class ProcessReturnedMailBean extends SessionBase {
    
    protected SystemAddress systemAddressBean = null;

    //Address prefix
    private static final String PERSON_ADDRESS_PREFIX   = "1";
    private static final String ORG_ADDRESS_PREFIX      = "2";
    //Exceptions
    private static final String RECENT          = "Recent";
    private static final String INACTIVE        = "Inactive";
    private static final String EXPELLED        = "Expelled";
    private static final String DECEASED        = "Deceased";
    private static final String MEMBER_INACTIVE = "Inactive";
        
    /** Selects person address */
    private static final String SQL_SELECT_PERSON_ADDRESS =
    "SELECT a.prefix_nm, a.first_nm, a.middle_nm, a.last_nm, a.suffix_nm, a.member_fg, a.mbr_expelled_dt, b.* " +
    "FROM Person a, Person_Address b " +
    "WHERE a.person_pk=b.person_pk and b.address_pk=?";
    
    /** Selects SMA (System Mailing Address) */
    private static final String SQL_SELECT_SMA =
    "SELECT count(*) FROM Person_SMA WHERE address_pk=?";
    
    /** Selects member status */
    private static final String SQL_SELECT_MEMBER_STATUS =
    "SELECT com_cd_desc " +
    "FROM Aff_Members a " +
    "JOIN Common_Codes c on c.com_cd_pk = a.mbr_status " +
    "WHERE person_pk=?";
    
    /** Selects person deceased flag */
    private static final String SQL_SELECT_PERSON_DECEASED_FLAG =
    "SELECT deceased_fg FROM Person_Demographics WHERE person_pk=?";
    
    /** Selects organization address */
    private static final String SQL_SELECT_ORGANIZATION_ADDRESS =
    "SELECT a.org_pk, a.location_nm, b.* " +
    "FROM  Org_Locations a, Org_Address b " +
    "WHERE a.org_locations_pk=b.org_addr_pk and b.org_addr_pk=?";
    
    /** Selects affiliate abbreviated name */
    private static final String SQL_SELECT_AFFILIATE_ABBREVIATED_NAME =
    "SELECT aff_abbreviated_nm FROM Aff_Organizations WHERE aff_pk=?";
    
    /** Updates a person address */
    private static final String SQL_UPDATE_PERSON_ADDRESS =
    "UPDATE Person_Address SET addr_bad_fg=?, addr_marked_bad_dt=?, lst_mod_dt=?, lst_mod_user_pk=? WHERE address_pk=?";
    
    /** Updates an organization address */
    private static final String SQL_UPDATE_ORGANIZATION_ADDRESS =
    "UPDATE Org_Address SET addr_bad_fg=?, addr_bad_dt=?, lst_mod_dt=?, lst_mod_user_pk=? WHERE org_addr_pk=?";

    private static Logger log = Logger.getLogger(ProcessReturnedMailBean.class);
    
    /* --------  Bean Creation method ---------------------------------- */
    /** Loads all non-changeable data into local member variables */
    public void ejbCreate() throws CreateException {
        try {
            systemAddressBean = JNDIUtil.getSystemAddressHome().create();
        } catch (NamingException exp) {
            throw new EJBException(exp);
        }
    }
    
    public void ejbRemove() {
        try {
            systemAddressBean.remove();
        } catch (RemoveException exp) {
            throw new EJBException(exp);
        }
    }
    
    /**
     * @J2EE_METHOD  --  processReturnedAddresses
     * Processes a list of Address IDs and flags them as bad addresses, and returns a Summary
     *  report.
     *
     * @param Collection of Address IDs to mark as bad.
     * @param Integer Person ID of the current log in user.
     * @return ReturnedMailSummary
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public ReturnedMailSummary processReturnedAddresses(java.util.Collection addressIDs, Integer modPersonPk) {
        Iterator itr = addressIDs.iterator();
        ReturnedMailSummary summary = new ReturnedMailSummary();
        ReturnedOrganizationAddress orgAddr = null;
        ReturnedPersonAddress personAddr = null;
        RecordData recData = null;
        boolean exception=false;
        Integer i_addressId;
        String s_addressId=null;
        String prefix=null;
        
        while (itr.hasNext()) {
            try {
                s_addressId = (String) itr.next();
                prefix = s_addressId.substring(0, 1);
                s_addressId = s_addressId.substring(1);
                i_addressId = new Integer(Integer.parseInt(s_addressId));                
                
                //It's a Person Address
                if (prefix.equalsIgnoreCase(PERSON_ADDRESS_PREFIX)) {    
                    personAddr = getPersonAddress(i_addressId);
                    
                    if (personAddr != null) {
                        //This person is a member and status is inactive
                        if (personAddr.isMbrFlag()) {
                            if (exception = isMemberStatusInactive(personAddr.getPersonPK())) {
                                personAddr.setExceptionReason(INACTIVE);
                            }
                        }
                        //This person is expelled
                        if (personAddr.getExpelledDate()!=null) {
                            exception = true;
                            personAddr.setExceptionReason(EXPELLED);
                        }
                        //This person is deceased
                        if (exception = isPersonDeceased(personAddr.getPersonPK())) {
                            personAddr.setExceptionReason(DECEASED);
                        }
                        //Address has been marked bad within the last month
                        if (exception = isModifyRecently(personAddr.getBadDate())) {
                            personAddr.setExceptionReason(RECENT);
                        }
                        //Exception occurred
                        if (exception) {
                            summary.addToPersonExceptionList(personAddr);
                            summary.incrementExceptionCount();
                        }
                        //Everything is good, mark the address bad
                        else {
                            systemAddressBean.markBad(i_addressId);
                            summary.addToPersonSuccessfulList(personAddr);
                            summary.incrementSuccessfulCount();
                        }
                    }
                }
                
                //It's an Organization Address
                if (prefix.equalsIgnoreCase(ORG_ADDRESS_PREFIX)) {    
                    orgAddr = getOrganizationAddress(i_addressId);
                    
                    if (orgAddr != null) {
                        //If exception occurred: Recently modified
                        if (isModifyRecently(orgAddr.getBadDate())) {
                            orgAddr.setExceptionReason(RECENT);
                            summary.addToOrganizationExceptionList(orgAddr);
                            summary.incrementExceptionCount();
                        }
                        //Everything is good, mark address bad
                        else {
                            recData = orgAddr.getTheRecordData();
                            recData.setModifiedDate(new java.sql.Timestamp(new java.util.Date().getTime()));
                            recData.setModifiedBy(modPersonPk);
                            orgAddr.setBad(true);
                            orgAddr.setBadDate(new java.sql.Timestamp(new java.util.Date().getTime()));
                            saveOrganizationAddress(orgAddr);
                            summary.addToOrganizationSuccessfulList(orgAddr);
                            summary.incrementSuccessfulCount();
                        }
                    }
                }
                
                //Invalid addressId - addressId is not found in either person_address or org_address
                if (!prefix.equalsIgnoreCase(PERSON_ADDRESS_PREFIX) &&
                    !prefix.equalsIgnoreCase(ORG_ADDRESS_PREFIX) ||
                    (personAddr==null && orgAddr==null)) {
                    summary.addToInvalidAddressList(i_addressId);
                    summary.incrementInvalidAddressCount();
                    summary.incrementExceptionCount();
                }
                //Increment attempted count of update
                summary.incrementAttemptedCount();
                
                //Reset address record to process the next one
                personAddr = null;
                orgAddr = null;
                prefix = null;
                
            } catch (NumberFormatException nfe) {
                summary.addToInvalidAddressList(s_addressId);
                summary.incrementInvalidAddressCount();
                summary.incrementExceptionCount();
                summary.incrementAttemptedCount();
            }
        } // end while
        
        //Return the summary status
        return summary;
    }
    
    /**
     * @J2EE_METHOD  --  isModifyRecently
     *
     * @return boolean true if the address was marked within the last month
     */
    public boolean isModifyRecently(Timestamp badDate) {
        //Difference in days
        if (badDate != null) {
            long dayDiff = (System.currentTimeMillis() - badDate.getTime()) / 86400000;
            return (dayDiff >= 0 && dayDiff <= 30) ? true : false;
        }
        return false;
    }
    
    /**
     * @J2EE_METHOD  --  isMemberStatusInactive
     * Retrieves the status of a member.
     *
     * @return boolean true is inactive, false is active
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isMemberStatusInactive(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String status = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBER_STATUS);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                status = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return (status!=null && status.equalsIgnoreCase(MEMBER_INACTIVE)) ? true : false;
    }
    
    /**
     * @J2EE_METHOD  --  isPersonDeceased
     * Retrieves if the person is deceased.
     *
     * @return boolean true is deceased, false is not
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isPersonDeceased(Integer personPk) {
        boolean deceased = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON_DECEASED_FLAG);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                deceased = (rs.getInt(1)==1) ? true : false;
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return deceased;
    }
    
    /**
     * @J2EE_METHOD  --  isSMA
     * Retrieves if address is SMA (System Mailing Address).
     *
     * @return boolean true if SMA record exist, false if not exist
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isSMA(Integer addressPk) {
        boolean sma = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_SMA);
            ps.setInt(1, addressPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                sma = (rs.getInt(1) > 0) ? true : false;
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return sma;
    }
        
    /**
     * @J2EE_METHOD  --  getPersonAddress
     * Retrieves a Person Address and Information from the database.
     *
     * @return ReturnedPersonAddress Address and information of the person
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public ReturnedPersonAddress getPersonAddress(Integer addressPK) {
        ReturnedPersonAddress result = null;
        RecordData recData = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON_ADDRESS);
            ps.setInt(1, addressPK.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = new ReturnedPersonAddress();
                recData = new RecordData();
                result.setPrefixNm(rs.getString(1));
                result.setFirstNm(rs.getString(2));
                result.setMiddleNm(rs.getString(3));
                result.setLastNm(rs.getString(4));
                result.setSuffixNm(rs.getString(5));
                result.setMbrFlag(rs.getBoolean(6));
                result.setExpelledDate(rs.getTimestamp(7));
                result.setPersonPK(new Integer(rs.getInt("person_pk")));
                result.setAddressPK(addressPK);
                result.setAddr1(rs.getString("addr1"));
                result.setAddr2(rs.getString("addr2"));
                result.setCarrierRoute(rs.getString("carrier_route_info"));
                result.setCity(rs.getString("city"));
                result.setCountryPk(new Integer(rs.getInt("country")));
                result.setCounty(rs.getString("county"));
                result.setProvince(rs.getString("province"));
                result.setState(rs.getString("state"));
                result.setBad(rs.getBoolean("addr_bad_fg"));
                result.setBadDate(rs.getTimestamp("addr_marked_bad_dt"));
                result.setDepartment(new Integer(rs.getInt("dept")));
                result.setPrimary(rs.getBoolean("addr_prmry_fg"));
                result.setPrivate(rs.getBoolean("addr_private_fg"));
                result.setSource(rs.getString("addr_source").charAt(0));
                result.setType(new Integer(rs.getInt("addr_type")));
                recData.setPk(addressPK);                
                recData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
                recData.setCreatedDate(rs.getTimestamp("created_dt"));
                recData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                recData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                result.setTheRecordData(recData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        if (result != null) {
            result.setSmaFlag(isSMA(addressPK));
        }
        return result;
    }
    
    /**
     * @J2EE_METHOD  --  getOrganizationAddress
     * Retrieves an Organization Address from the database.
     *
     * @return ReturnedOrganizationAddress Address of the Organization
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public ReturnedOrganizationAddress getOrganizationAddress(Integer addressPK) {
        ReturnedOrganizationAddress result = null;
        RecordData recData = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORGANIZATION_ADDRESS);
            ps.setInt(1, addressPK.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = new ReturnedOrganizationAddress();
                recData = new RecordData();
                result.setAddressPK(addressPK);
                result.setOrgPK(new Integer(rs.getInt(1)));
                result.setAddrTypeDescr(rs.getString(2));
                result.setAddr1(rs.getString("addr1"));
                result.setAddr2(rs.getString("addr2"));
                result.setCity(rs.getString("city"));
                result.setCountryPk(new Integer(rs.getInt("country")));
                result.setCounty(rs.getString("county"));
                result.setProvince(rs.getString("province"));
                result.setState(rs.getString("state"));
                result.setBadDate(rs.getTimestamp("addr_bad_dt"));
                result.setBad(rs.getBoolean("addr_bad_fg"));
                result.setAttentionLine(rs.getString("attention_line"));
                recData.setPk(addressPK);                
                recData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
                recData.setCreatedDate(rs.getTimestamp("created_dt"));
                recData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                recData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                result.setTheRecordData(recData);
            }
            
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        //Get affiliate abbreviated name
        if (result != null && result.getOrgPK() != null) {
            result.setAffAbbreviatedName(getAffAbbreviatedName(result.getOrgPK()));
        }
        return result;
    }
    
    /**
     * @J2EE_METHOD  --  getAffAbbreviatedName
     * Retrieves the Affiliate Abbreviated Name from the database.
     *
     * @return String Abbreviated Affiliate Name
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public String getAffAbbreviatedName(Integer affPK) {
        String abbreviatedNm = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_AFFILIATE_ABBREVIATED_NAME);
            ps.setInt(1, affPK.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                abbreviatedNm = rs.getString(1);
            }
            
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return abbreviatedNm;
    }
    
    /**
     * @J2EE_METHOD  --  savePersonAddress
     * Stores the updated Person Address into the database.
     *
     * @param address ReturnedPersonAddress
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void savePersonAddress(ReturnedPersonAddress address) {
        RecordData recData = null;
        Connection con = null;
        PreparedStatement ps = null;
        
        //update person address object into the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PERSON_ADDRESS);
            recData = address.getTheRecordData();
            ps.setBoolean(1, address.isBad());
            ps.setTimestamp(2, address.getBadDate());
            ps.setTimestamp(3, recData.getModifiedDate());
            ps.setInt(4, recData.getModifiedBy().intValue());
            ps.setInt(5, address.getAddressPK().intValue());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * @J2EE_METHOD  --  saveOrganizationAddress
     * Stores the updated Organization Address into the database.
     *
     * @param address ReturnedOrganizationAddress
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void saveOrganizationAddress(ReturnedOrganizationAddress address) {
        RecordData recData = null;
        Connection con = null;
        PreparedStatement ps = null;
        
        //Update organization address object into the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_ORGANIZATION_ADDRESS);
            recData = address.getTheRecordData();
            ps.setBoolean(1, address.isBad());
            ps.setTimestamp(2, address.getBadDate());
            ps.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
            ps.setInt(4, recData.getModifiedBy().intValue());
            ps.setInt(5, address.getAddressPK().intValue());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * @J2EE_METHOD  --  ProcessReturnedMailBean
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public ProcessReturnedMailBean() {
        
    }
}