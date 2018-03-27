package org.afscme.enterprise.minimumdues;

/**
 * <p>This is a Model DAO object <p>
 *
 * In the application, this Model component updates a persistent data store
 */

// AFSCME Enterprise Imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DelimitedStringBuffer;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.PreparedStatementBuilder.Criterion;
import org.afscme.enterprise.address.ejb.SystemAddressBean;
import org.afscme.enterprise.affiliate.*;
import org.afscme.enterprise.log.SystemLog;


// Java Imports
import java.io.File;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;


// Other Imports
import org.apache.log4j.Logger;

public class DataEntryDAO {

  private static Logger log = Logger.getLogger(DataEntryDAO.class);
  private String affCode;

  private String email = null;
  private String telephone = null;
  private String personName = null;

  private String averageWage = null;
  private String amountType = null;
  private String section = null;

  private String initPercent_a = null;
  private String initEffective_a = null;
  private String initNoOfMember_a = null;
  private String initTypeOfPayment_adj_a = null;
  private String initPercentInc_adj_a = null;
  private String initNoOfMember_adj_a = null;
  private String initMbrTimesInc_a = null;

  private String initAmount_b = null;
  private String initEffective_b = null;
  private String initNoOfMember_b = null;
  private String initTypeOfPayment_adj_b = null;
  private String initAmountInc_adj_b = null;
  private String initNoOfMember_adj_b = null;
  private String initMbrTimesInc_b = null;

  private String statAverage = null;
  private String membershipCt = null;
  private String numberOfMember = null;
  private String employerName = null;
  private String affIdSubUnit = null;
  private String affIdLocal = null;
  private String affIdCouncil = null;
  private String affIdState = null;
  private String affIdStatus = null;
  private String affIdType = null;
  private String year = null;
  private int wageIncType = 1;

  private String duesyear = null;

  public static final int EMPLOYER_NOT_EXIST = 1;
  public static final int EMPLOYER_SAME_YR_EXIST = 2;
  public static final int EMPLOYER_EXIST_SAME_YR_NOT_EXIST = 3;

  private Connection con = null;
  private Statement stmt = null;
  private ResultSet rs = null;

  //private String tmpEmpId = null;
  private int empAffPk = 0;
  private int aff_fk = 0;
  private int empPk = 0;
  private int wifPk = 0;
  //private String empIdStr = null;

  // nested bean reference
  private ArrayList percentWageIncList;
  private ArrayList amountWageIncList;

  private PercentWageIncBean tmpPwib = null;
  private PercentWageIncBean initPwib = null;

  private AmountWageIncBean tmpAwib = null;
  private AmountWageIncBean initAwib = null;

  private ArrayList genericList = null;

  private boolean saveSecA = false;
  private String formCompleted = null;
  private boolean formCompleted_b = false;
  private String correspondence = null;
  private boolean correspondence_b = false;
  private String correspondenceDate = null;
  private String agreementReceived = null;
  private boolean agreementReceived_b = false;
  private String agreementDesc = null;

  private String comments = null;
  //private String moreLocals = null;
  private String durationTo = null;
  private String durationFrom = null;
  private String inNegotiations = null;
  private boolean inNegotiations_b = false;
  private String form_employer_name = null;

  private int agreementPk = 0;
  private String agreementName = null;

  public WageIncForm getWageIncForm(int empAffFk, String year)  {
	String SQL_WAGEINCREASE_FORM_SELECT = "SELECT WIFPK, AGREEMENTFK, TOT_MEM_NUM, AVERAGE_WAGES, "
			+ " form_completed, correspondence, correspondence_dt, comments, "
			+ " agreementFk, end_dt, start_dt, in_negotiations, agreement_received, agreement_desc, "
			+ " employer_name, contact_name, contact_email, contact_phone "
			+ " FROM MDU_WAGEINCREASE_FORM WHERE "
			+ " empAffFk = " + empAffFk + " AND DUESYEAR = " + year;

	WageIncForm wageIncForm = new WageIncForm();
	try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
	    rs = null;

	    log.debug("SQL_WAGEINCREASE_FORM_SELECT  = " + SQL_WAGEINCREASE_FORM_SELECT);

	    rs = stmt.executeQuery(SQL_WAGEINCREASE_FORM_SELECT);
	    while (rs.next()) {
		  wageIncForm.setWifPk(rs.getInt("wifpk"));
		  wageIncForm.setTot_num_mem(rs.getInt("tot_mem_num"));
		  wageIncForm.setAverage_wages(rs.getDouble("average_wages"));
		  wageIncForm.setFormCompleted(rs.getInt("form_completed"));
		  wageIncForm.setAgreementReceived(rs.getInt("agreement_received"));
		  wageIncForm.setAgreementDesc(rs.getString("agreement_desc"));
		  wageIncForm.setCorrespondence(rs.getInt("correspondence"));

		  wageIncForm.setEmployer_name(rs.getString("employer_name"));
		  wageIncForm.setContact_name(rs.getString("contact_name"));
		  wageIncForm.setContact_email(rs.getString("contact_email"));
		  wageIncForm.setContact_phone(rs.getString("contact_phone"));

		  String correspondence_dtStr = rs.getString("correspondence_dt");

		  if (correspondence_dtStr != null) {
				wageIncForm.setCorrespondenceDate(TextUtil.format(Timestamp.valueOf(correspondence_dtStr)));
		  }

		  wageIncForm.setComments(rs.getString("comments"));

		  int tmpAgreementPk = rs.getInt("agreementFk");
		  wageIncForm.setAgreementPk(tmpAgreementPk);

		  AgreementDAO agreementDao = new AgreementDAO();

		  String tmpAgreementName = agreementDao.getAgreementNameByPk(tmpAgreementPk);
		  wageIncForm.setAgreementName(tmpAgreementName);

		  String end_dtStr = rs.getString("end_dt");
		  if (end_dtStr != null) {
				wageIncForm.setDurationTo(TextUtil.format(Timestamp.valueOf(end_dtStr)));
		  }
		  String start_dtStr = rs.getString("start_dt");
		  if (start_dtStr != null) {
				wageIncForm.setDurationFrom(TextUtil.format(Timestamp.valueOf(start_dtStr)));
		  }
		  wageIncForm.setInNegotiations(rs.getInt("in_negotiations"));
	    }
 	}
 	catch (Exception exc) {
		exc.printStackTrace();
	}
	finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return wageIncForm;
  }

  public ArrayList getWageIncDataList(int wifFk) {
	String SQL_WAGEINCREASE_DATA_SELECT =
			"SELECT WAGE_INC_TYPE, WAGE_INC, EFFECTIVE_DATE, NUM_AFFECTED, PAYMENT_TYPE_ADJ, " +
			"WAGE_INC_ADJ, NUM_AFFECTED_ADJ, NUM_TIMES_INC FROM MDU_WAGEINCREASE_DATA " +
			"WHERE WIFFK = " + wifFk;

	WageIncData wageIncData = null;
	ArrayList wageIncDataList = new ArrayList();

    try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
	    rs = null;

	    log.debug("SQL_WAGEINCREASE_DATA_SELECT  = " + SQL_WAGEINCREASE_DATA_SELECT);

	    rs = stmt.executeQuery(SQL_WAGEINCREASE_DATA_SELECT);
	    while (rs.next()) {
                wageIncData = new WageIncData();
                wageIncData.setWageIncType(rs.getInt("WAGE_INC_TYPE"));
                wageIncData.setWageInc(rs.getDouble("WAGE_INC"));
                wageIncData.setEffectiveDate(TextUtil.format(Timestamp.valueOf(rs.getString("EFFECTIVE_DATE"))));
                wageIncData.setNumAffected(rs.getInt("NUM_AFFECTED"));
                wageIncData.setPaymentTypeAdj(rs.getString("PAYMENT_TYPE_ADJ"));
                wageIncData.setWageIncAdj(rs.getDouble("WAGE_INC_ADJ"));
                wageIncData.setNumAffectedAdj(rs.getInt("NUM_AFFECTED_ADJ"));
                wageIncData.setNumTimesInc(rs.getDouble("NUM_TIMES_INC"));
                wageIncDataList.add(wageIncData);
	    }
 	}
 	catch (Exception exc) {
		exc.printStackTrace();
	}
	finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return wageIncDataList;
  }


  public StatMembership getStatMembership (String year, int affFk, int agreementPk)  {
	StatMembership statMembership = new StatMembership();

	try {
		String SQL_SELECT_MDU_STAT_MEMBERSHIP = null;

		int totStat = 0;
		int totMemshipCt = 0;
		int affPkToUse = 0;

		String affType = getAffType(affFk);

		if (affType.equalsIgnoreCase("U"))
		  affPkToUse = getParentPk(affFk);
		else
		  affPkToUse = affFk;

		System.out.println("year = --------------" + year);
		System.out.println("affFk = --------------" + affFk);
		System.out.println("affPkToUse = --------------" + affPkToUse);
		System.out.println("agreementPk = --------------" + agreementPk);

		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;

		if (agreementPk == 0) {
			System.out.println("*******apk=0*****--------------");
			SQL_SELECT_MDU_STAT_MEMBERSHIP =
				"SELECT STATMBRCOUNT, MBRSHPCOUNT FROM MDU_STAT_MEMBERSHIP "
				+ " WHERE AFF_FK = " + affPkToUse + " AND DUESYR1 = " + year;
			log.debug("SQL_SELECT_MDU_STAT_MEMBERSHIP  = " + SQL_SELECT_MDU_STAT_MEMBERSHIP);

			rs = stmt.executeQuery(SQL_SELECT_MDU_STAT_MEMBERSHIP);
			if (rs.next()) {
				  statMembership.setStatMbrCt(rs.getInt("STATMBRCOUNT"));
				  statMembership.setMbrshpCt(rs.getInt("MBRSHPCOUNT"));
			}

		}
		else {
			System.out.println("***********apk not 0***********");
			// first use agreementPk to get all the empAffPk
			String SQL_SELECT_MDU_AGREEMENT_EMP =
					"SELECT EmpAffFk FROM MDU_AGREEMENT_EMP "
					+ " WHERE agreementFk = " + agreementPk;
			log.debug("SQL_SELECT_MDU_AGREEMENT_EMP  = " + SQL_SELECT_MDU_AGREEMENT_EMP);

			rs = stmt.executeQuery(SQL_SELECT_MDU_AGREEMENT_EMP);

			String tmpEmpAffFk = null;
			StringBuffer tmpEmpAffFkStr = new StringBuffer();

			while (rs.next()) {
				  tmpEmpAffFk = ""+rs.getInt("EmpAffFk");
				  tmpEmpAffFkStr.append(", ").append(tmpEmpAffFk);
			}

		    tmpEmpAffFkStr.deleteCharAt(0);

		    SQL_SELECT_MDU_STAT_MEMBERSHIP =
					" SELECT STATMBRCOUNT, MBRSHPCOUNT FROM V_MDU_Agreements_w_STAT " +
					" WHERE empAffPk IN ( " + tmpEmpAffFkStr.toString() +
					" ) AND agreementPk = " + agreementPk;
			log.debug("SQL_SELECT_MDU_STAT_MEMBERSHIP  = " + SQL_SELECT_MDU_STAT_MEMBERSHIP);

		    rs = stmt.executeQuery(SQL_SELECT_MDU_STAT_MEMBERSHIP);
		    while (rs.next()) {
				totStat = totStat + rs.getInt("STATMBRCOUNT");
				totMemshipCt = totMemshipCt + rs.getInt("MBRSHPCOUNT");
		    }

			statMembership.setStatMbrCt(totStat);
			statMembership.setMbrshpCt(totMemshipCt);
		}

	}
	catch (Exception exc) {
		exc.printStackTrace();
	}
	finally {
		DBUtil.cleanup(con, stmt, rs);
	}

System.out.println("***************totStat*******" + statMembership.getStatMbrCt());
System.out.println("***************totMemshipCt*******" + statMembership.getMbrshpCt());

	return statMembership;
  }

  public int checkAddOrModifyEmployerPossibility ()  {
	String tmpCouncil = this.getAffIdCouncil().trim();
	String tmpLocal = this.getAffIdLocal().trim();

	if ( (tmpCouncil == null) || (tmpCouncil.trim().length() == 0) || (tmpCouncil.equalsIgnoreCase("0")) ) {
		tmpCouncil = "";
	}

	if ( (tmpLocal == null) || (tmpLocal.trim().length() == 0) || (tmpLocal.equalsIgnoreCase("0")) ) {
		tmpLocal = "";
	}

System.out.println("******************************tmpCouncil = " + tmpCouncil);
System.out.println("******************************tmpLocal = " + tmpLocal);

	String SQL_SELECT_VIEW_MDU_AFF =
            " select aff_pk from v_MDU_Affiliates WHERE " +
            " STATE = '" + this.getAffIdState().trim() + "' AND " +
            " COUNCIL = '" + tmpCouncil + "' AND " +
            " LOCAL = " + tmpLocal;

System.out.println("******************************SQL_SELECT_VIEW_MDU_AFF = " + SQL_SELECT_VIEW_MDU_AFF);

    int aff_pk = 0;

    try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
	    rs = null;

	    log.debug("SQL_SELECT_VIEW_MDU_AFF  = " + SQL_SELECT_VIEW_MDU_AFF);

		rs = stmt.executeQuery(SQL_SELECT_VIEW_MDU_AFF);

	    if (rs.next()) {
              aff_pk = rs.getInt("aff_pk");
	    }
 	}
 	catch (Exception exc) {
		exc.printStackTrace();
	}
	finally {
		DBUtil.cleanup(con, stmt, rs);
    }

	return aff_pk;
  }

   public String getAffType(int affFk) {
	String aff_type = "";

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;

		String SQL_SELECT_AFF_TYPE = "select aff_type from aff_organizations where aff_pk = " + affFk;
		log.debug("SQL_SELECT_AFF_TYPE  = " + SQL_SELECT_AFF_TYPE);

	    rs = stmt.executeQuery(SQL_SELECT_AFF_TYPE);

	    while (rs.next()) {
              aff_type = rs.getString(1);
	    }
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return aff_type;
  }


   public int getParentPk(int affFk) {
	int parent_aff_fk = 0;

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;

		String SQL_SELECT_EMP_PARENT_AFF_FK = "select parent_aff_fk from aff_organizations where aff_pk = " + affFk;
		log.debug("SQL_SELECT_EMP_PARENT_AFF_FK  = " + SQL_SELECT_EMP_PARENT_AFF_FK);

	    rs = stmt.executeQuery(SQL_SELECT_EMP_PARENT_AFF_FK);

	    while (rs.next()) {
              parent_aff_fk = rs.getInt(1);
	    }
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return parent_aff_fk;
  }


  public void updateEmployer (int empAffPk, int aff_fk)  {
	String SQL_UPDATE_MDU_EMP_AFF =
            " UPDATE MDU_EMP_AFF SET " +
            " AFF_FK = " + aff_fk + ", " +
            " SUBUNIT = '" + this.getAffIdSubUnit().trim() + "', " +
            " CURR_EMPLOYER_NAME = '" + this.getEmployerName().trim().replaceAll("\'", "\'\'") + "', " +
            " ACTIVE = '" + this.getAffIdStatus().trim() + "' " +
            " WHERE empAffPk = " + empAffPk;

	String SQL_UPDATE_MDU_WAGEINCREASE_FORM_EMPLOYER_NAME =
            " UPDATE MDU_WAGEINCREASE_FORM SET " +
            " employer_name = '" + this.getEmployerName().trim() + "' " +
            " WHERE empAffFk = " + empAffPk + " AND DUESYEAR >= " + this.getDuesyear().trim();


    try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
	    rs = null;

	    log.debug("SQL_UPDATE_MDU_EMP_AFF  = " + SQL_UPDATE_MDU_EMP_AFF);
	    log.debug("SQL_UPDATE_MDU_WAGEINCREASE_FORM_EMPLOYER_NAME  => " + SQL_UPDATE_MDU_WAGEINCREASE_FORM_EMPLOYER_NAME);

	    stmt.executeUpdate(SQL_UPDATE_MDU_EMP_AFF);
	    stmt.executeUpdate(SQL_UPDATE_MDU_WAGEINCREASE_FORM_EMPLOYER_NAME);
 	}
 	catch (Exception exc) {
		exc.printStackTrace();
	}
	finally {
		DBUtil.cleanup(con, stmt, rs);
    }

  }

  public void changeEmployerActiveStatus (int empAffPk)  {
	  	String status = null;
	  	String active = "1";

		String SQL_SELECT_MDU_EMP_AFF_ACTIVE =
				" SELECT ACTIVE FROM MDU_EMP_AFF WHERE empAffPk = " + empAffPk;

		String SQL_UPDATE_MDU_EMP_AFF_ACTIVE =
				" UPDATE MDU_EMP_AFF SET " +
				" ACTIVE = " + active + " " +
				" WHERE empAffPk = " + empAffPk;

		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			rs = null;

			log.debug("SQL_SELECT_MDU_EMP_AFF_ACTIVE  = " + SQL_SELECT_MDU_EMP_AFF_ACTIVE);
			rs = stmt.executeQuery(SQL_SELECT_MDU_EMP_AFF_ACTIVE);

			if (rs.next()) {
				 status = rs.getString("active");
			}

			if ((status != null) && (status.trim().equalsIgnoreCase("1")))
				active = "0";
			else
				active = "1";

			log.debug("SQL_UPDATE_MDU_EMP_AFF_ACTIVE  = " + SQL_UPDATE_MDU_EMP_AFF_ACTIVE);
			stmt.executeUpdate(SQL_UPDATE_MDU_EMP_AFF_ACTIVE);
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			DBUtil.cleanup(con, stmt, rs);
		}

  }

  public void deleteEmployer(int empAffPk)  {
	String SQL_DELETE_MDU_EMP_AFF = "DELETE FROM MDU_EMP_AFF WHERE empAffPk = " + empAffPk;
	String SQL_DELETE_MDU_WAGEINCREASE_FORM = "DELETE FROM MDU_WAGEINCREASE_FORM " +
			" WHERE empAffFk = " + empAffPk;
	String SQL_DELETE_MDU_AGREEMENT_EMP = "DELETE FROM MDU_Agreement_Emp " +
			" WHERE EmpAffFk = " + empAffPk;

    try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
	    rs = null;
	    con.setAutoCommit(false);

		// delete form table value
		log.debug("SQL_DELETE_MDU_WAGEINCREASE_FORM  = " + SQL_DELETE_MDU_WAGEINCREASE_FORM);
		stmt.executeUpdate(SQL_DELETE_MDU_WAGEINCREASE_FORM);

		// delete MDU_Agreement_Emp table value
		log.debug("SQL_DELETE_MDU_AGREEMENT_EMP  = " + SQL_DELETE_MDU_AGREEMENT_EMP);
		stmt.executeUpdate(SQL_DELETE_MDU_AGREEMENT_EMP);

		log.debug("SQL_DELETE_MDU_EMP_AFF  = " + SQL_DELETE_MDU_EMP_AFF);
		stmt.executeUpdate(SQL_DELETE_MDU_EMP_AFF);
		con.commit();

 	}
 	catch (Exception exc) {
		try {
			con.rollback();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		exc.printStackTrace();
	}
	finally {
		DBUtil.cleanup(con, stmt, rs);
    }
  }

  public void updateIncreaseInfo()  {
	//log.debug("update Database from DAO");
	affCode = this.getAffCode();
	email = this.getEmail();
	telephone = this.getTelephone();
	personName = this.getPersonName();
	averageWage = this.getAverageWage();
	percentWageIncList = this.getPercentWageIncList();
	amountWageIncList = this.getAmountWageIncList();

	formCompleted = this.getFormCompleted();
	correspondence = this.getCorrespondence();
	correspondenceDate = this.getCorrespondenceDate();
	agreementReceived = this.getAgreementReceived();
	agreementDesc = this.getAgreementDesc();

	comments = this.getComments();
	//moreLocals = this.getMoreLocals();

	AgreementDAO agreementDao = new AgreementDAO();
	agreementPk = this.getAgreementPk();
	//agreementPk = agreementDao.getAgreementPkByName(agreementName);

	durationTo = this.getDurationTo();
	durationFrom = this.getDurationFrom();
	inNegotiations = this.getInNegotiations();

	statAverage = this.getStatAverage();
	membershipCt = this.getMembershipCt();
	numberOfMember = this.getNumberOfMember();
	employerName = this.getEmployerName();
	affIdSubUnit = this.getAffIdSubUnit();
	affIdLocal = this.getAffIdLocal();
	affIdCouncil = this.getAffIdCouncil();
	affIdState = this.getAffIdState();
	affIdType = this.getAffIdType();
	year = this.getYear();
	empAffPk = this.getEmpAffPk();

	agreementReceived = this.getAgreementReceived();
	agreementDesc = this.getAgreementDesc();

	comments = comments.replaceAll("\'", "\'\'");
	//moreLocals = moreLocals.replaceAll("\'", "\'\'");

    try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
		rs = null;
	    con.setAutoCommit(false);

		// delete all data with this employer
		/*
	    String SQL_MDU_WAGEINCREASE_FORM_PK_SELECT = "SELECT WIFPK FROM MDU_WAGEINCREASE_FORM WHERE empAffPk = "
					+ empAffPk + " AND DUESYEAR = " + year;
	    log.debug("SQL_MDU_WAGEINCREASE_FORM_PK_SELECT  = " + SQL_MDU_WAGEINCREASE_FORM_PK_SELECT);

	    rs = stmt.executeQuery(SQL_MDU_WAGEINCREASE_FORM_PK_SELECT);

		StringBuffer SQL_MDU_WAGEINCREASE_DATA_DELETE_BUFFER
			   = new StringBuffer("DELETE FROM MDU_WAGEINCREASE_DATA WHERE WIFFK = ");
		int tmpCt = 0;

	    while (rs.next()) {
                wifPk = rs.getInt(1);
                if (tmpCt == 0)
                    SQL_MDU_WAGEINCREASE_DATA_DELETE_BUFFER.append(""+wifPk);
                else
                    SQL_MDU_WAGEINCREASE_DATA_DELETE_BUFFER.append(" OR WIFFK = "+wifPk);

                tmpCt = tmpCt + 1;
	    }

		String SQL_MDU_WAGEINCREASE_DATA_DELETE = SQL_MDU_WAGEINCREASE_DATA_DELETE_BUFFER.toString();
		log.debug("SQL_MDU_WAGEINCREASE_DATA_DELETE  = " + SQL_MDU_WAGEINCREASE_DATA_DELETE);
		if (tmpCt > 0)
			stmt.executeUpdate(SQL_MDU_WAGEINCREASE_DATA_DELETE);
		*/

		String SQL_MDU_WAGEINCREASE_FORM_DELETE = "DELETE FROM MDU_WAGEINCREASE_FORM WHERE empAffFk = '"
					+ empAffPk + "' AND DUESYEAR = " + year;

		log.debug("SQL_MDU_WAGEINCREASE_FORM_DELETE  = " + SQL_MDU_WAGEINCREASE_FORM_DELETE);
		stmt.executeUpdate(SQL_MDU_WAGEINCREASE_FORM_DELETE);

		// insert data
		// test Sec A or Sec B
		if (amountWageIncList.isEmpty() && initAmount_b == null)
			saveSecA = true;
		else if (percentWageIncList.isEmpty() && initPercent_a == null)
			saveSecA = false;

		if (saveSecA)
			genericList = this.getPercentWageIncList();
		else
			genericList = this.getAmountWageIncList();

		insertIntoFormDataTable(con, stmt, rs);
		//insertIntoContactTable(con, stmt, rs);

	    con.commit();
    }
    catch (Exception exc) {
        try {
	      con.rollback();
        }
        catch (Exception e) {
           e.printStackTrace();
        }
        exc.printStackTrace();
    }
    finally {
        DBUtil.cleanup(con, stmt, rs);
    }
  }

  public int getEmployerAffFk(int empAffPk) {
	int aff_fk = 0;

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;


		String SQL_SELECT_EMP_AFF_FK = "Select aff_fk from MDU_EMP_AFF WHERE empAffPk = " + empAffPk;
		log.debug("SQL_SELECT_EMP_AFF_FK  = " + SQL_SELECT_EMP_AFF_FK);

	    rs = stmt.executeQuery(SQL_SELECT_EMP_AFF_FK);

	    while (rs.next()) {
              aff_fk = rs.getInt(1);
	    }
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return aff_fk;
  }

  public int checkEmployerExist() {  // check to see if the employer is already in the DB
	try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
		rs = null;

		StringBuffer SQL_GET_EMPLOYER_DATA = new StringBuffer("Select empAffPk from v_MDU_Emp_Aff WHERE ");

		if (!((affIdState == null || affIdState.trim().equalsIgnoreCase("") || affIdState.trim().length() == 0)))
				SQL_GET_EMPLOYER_DATA.append(" state = '" + affIdState.trim() + "'");

		//if (!((affIdType == null || affIdType.trim().equalsIgnoreCase("") || affIdType.trim().length() == 0)))
		//		SQL_GET_EMPLOYER_DATA.append(" AND type = '" + affIdType.trim() + "'");

		if (!((affIdCouncil == null || affIdCouncil.trim().equalsIgnoreCase("") || affIdCouncil.trim().length() == 0)))
				SQL_GET_EMPLOYER_DATA.append(" AND council = " + affIdCouncil);
		else
				SQL_GET_EMPLOYER_DATA.append(" AND (council = 0 or council = '') " );

		if (!((affIdLocal == null || affIdLocal.trim().equalsIgnoreCase("") || affIdLocal.trim().length() == 0)))
				SQL_GET_EMPLOYER_DATA.append(" AND local = " + affIdLocal);

		if (!((affIdSubUnit == null || affIdSubUnit.trim().equalsIgnoreCase("") || affIdSubUnit.trim().length() == 0)))
				SQL_GET_EMPLOYER_DATA.append(" AND (CHAPTER = '" + affIdSubUnit.trim() + "'" + " OR CHAPTER = '0"
				+ affIdSubUnit.trim() + "'" + " OR CHAPTER = '00"
				+ affIdSubUnit.trim() + "')");
		else
				SQL_GET_EMPLOYER_DATA.append(" AND CHAPTER = ''" );

		if (!((employerName == null || employerName.trim().equalsIgnoreCase("") || employerName.trim().length() == 0)))
				SQL_GET_EMPLOYER_DATA.append(" AND curr_employer_name = '" + employerName.trim().replaceAll("\'", "\'\'") + "'");

		log.debug("SQL_GET_EMPLOYER_DATA  = " + SQL_GET_EMPLOYER_DATA);

	    rs = stmt.executeQuery(SQL_GET_EMPLOYER_DATA.toString());
	    if (rs.next()) {
                  empAffPk = rs.getInt("empAffPk");
	    }
	}
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

	return empAffPk;
  }

  public int checkEmployerYrDataExist() {
	int retVal = EMPLOYER_NOT_EXIST;
    int empAffPk = checkEmployerExist();

	if (empAffPk != 0) {  // employer exists
		// check to see if the same yr data already entered
		String SQL_MDU_WAGEINCREASE_FORM_EXISTING_RECORD = "SELECT COUNT(*) FROM MDU_WAGEINCREASE_FORM WHERE DUESYEAR = "
			+ year + " AND empAffFk = " + empAffPk;
		log.debug("SQL_MDU_WAGEINCREASE_FORM_EXISTING_RECORD  = " + SQL_MDU_WAGEINCREASE_FORM_EXISTING_RECORD);

		try {
         	con = DBUtil.getConnection();
      		stmt = con.createStatement();
			rs = null;

			rs = stmt.executeQuery(SQL_MDU_WAGEINCREASE_FORM_EXISTING_RECORD);
			int ct = 0;

			while (rs.next()) {
		  		ct = rs.getInt(1);
			}

			if (ct != 0) {  			// the same year data for this employer already exists
				retVal = EMPLOYER_SAME_YR_EXIST;
			}
			else {
				retVal = EMPLOYER_EXIST_SAME_YR_NOT_EXIST;
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			DBUtil.cleanup(con, stmt, rs);
		}
	}

	return retVal;
  }

  public String checkEmployerActive(int empAffPk) {
	String retVal = "No";

	if (empAffPk != 0) {
		String SQL_MDU_EMP_AFF_EMPLOYER_ACTIVE = "SELECT ACTIVE FROM MDU_EMP_AFF WHERE EMPAFFPK = " + empAffPk;
		log.debug("SQL_MDU_EMP_AFF_EMPLOYER_ACTIVE  = " + SQL_MDU_EMP_AFF_EMPLOYER_ACTIVE);

		try {
         	con = DBUtil.getConnection();
      		stmt = con.createStatement();
			rs = null;

			rs = stmt.executeQuery(SQL_MDU_EMP_AFF_EMPLOYER_ACTIVE);

			String tmpResult = null;
			if (rs.next()) {
		  		tmpResult = rs.getString(1);
			}

			if (tmpResult != null && tmpResult.trim().equalsIgnoreCase("1")) {
				retVal = "Yes";
			}

		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			DBUtil.cleanup(con, stmt, rs);
		}
	}

	return retVal;
  }

  public int addEmployer(int aff_fk) {
	try {
	    	con = DBUtil.getConnection();
	    	stmt = con.createStatement();
			rs = null;
		    con.setAutoCommit(false);

			// escape ' from employer name
			employerName = employerName.replaceAll("\'", "\'\'");

			// insert into employer table using affpk
			String SQL_MDU_EMP_AFF_INSERT = "INSERT INTO MDU_EMP_AFF "
						+ "(aff_fk, subunit, curr_employer_name, active, "
						+ " created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) VALUES ("
						+ aff_fk + ", '" + affIdSubUnit.trim() + "', '" + employerName.trim() + "', "
                        + 1 + ", " + 10000002 + ", getDate()," + 10000002 + ", getDate() )";
			log.debug("SQL_MDU_EMP_AFF_INSERT  = " + SQL_MDU_EMP_AFF_INSERT);
			stmt.executeUpdate(SQL_MDU_EMP_AFF_INSERT);

            con.commit();

			// back select the PK
			String SQL_SELECT_MDU_EMP_AFF_PK = "SELECT MAX(empAffPk) from MDU_EMP_AFF WHERE "
						+ " aff_fk = " + aff_fk
						+ " AND subunit = '" + affIdSubUnit.trim() + "'"
						+ " AND curr_employer_name = '" + employerName.trim() + "'";

			log.debug("SQL_SELECT_MDU_EMP_AFF_PK  = " + SQL_SELECT_MDU_EMP_AFF_PK);
			rs = stmt.executeQuery(SQL_SELECT_MDU_EMP_AFF_PK);
	        if (rs.next()) {
                    empAffPk = rs.getInt(1);
	        }
    }
    catch (Exception exc) {
        try {
	      con.rollback();
        }
        catch (Exception e) {
           log.debug("Rollback Transaction.");
        }
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return empAffPk;
  }

  /**
   * This is a stub method that would be used for the Model to save
   * the information submitted to a persistent store.
   */
  public ArrayList saveWIFormDataToDB() {
	ArrayList errors = new ArrayList();

	affCode = this.getAffCode();
	email = this.getEmail().trim();
	telephone = this.getTelephone().trim();
	personName = this.getPersonName().trim();
	averageWage = this.getAverageWage().trim();
	amountWageIncList = this.getAmountWageIncList();

	formCompleted = this.getFormCompleted();
	correspondence = this.getCorrespondence();
	correspondenceDate = this.getCorrespondenceDate();
	agreementReceived = this.getAgreementReceived();
	agreementDesc = this.getAgreementDesc();
	comments = this.getComments();

	AgreementDAO agreementDao = new AgreementDAO();
	//agreementName = this.getAgreementName();
	//agreementPk = agreementDao.getAgreementPkByName(agreementName);
	agreementPk = this.getAgreementPk();

	durationTo = this.getDurationTo();
	durationFrom = this.getDurationFrom();
	inNegotiations = this.getInNegotiations();

	initPercent_a = this.getInitPercent_a();
	initEffective_a = this.getInitEffective_a();
	initNoOfMember_a = this.getInitNoOfMember_a();
	initTypeOfPayment_adj_a = this.getInitTypeOfPayment_adj_a();
	initPercentInc_adj_a = this.getInitPercentInc_adj_a();
	initNoOfMember_adj_a = this.getInitNoOfMember_adj_a();
	initMbrTimesInc_a = this.getInitMbrTimesInc_a();
	initAmount_b = this.getInitAmount_b();
	initEffective_b = this.getInitEffective_b();
	initNoOfMember_b = this.getInitNoOfMember_b();
	initTypeOfPayment_adj_b = this.getInitTypeOfPayment_adj_b();
	initAmountInc_adj_b = this.getInitAmountInc_adj_b();
	initNoOfMember_adj_b = this.getInitNoOfMember_adj_b();
	initMbrTimesInc_b = this.getInitMbrTimesInc_b();

	statAverage = this.getStatAverage();
	membershipCt = this.getMembershipCt();

	agreementReceived = this.getAgreementReceived();
	agreementDesc = this.getAgreementDesc();

	if (this.getNumberOfMember() != null)
	  numberOfMember = this.getNumberOfMember().trim();

    comments = comments.replaceAll("\'", "\'\'");
	//moreLocals = moreLocals.replaceAll("\'", "\'\'");

	year = this.getYear().trim();
	if (this.getEmpAffPk() != 0)
		empAffPk = this.getEmpAffPk();

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;
		con.setAutoCommit(false);

		// test Sec A or Sec B
		if (section.equalsIgnoreCase("section A"))
			saveSecA = true;
		else
			saveSecA = false;

		if (saveSecA)
			genericList = this.getPercentWageIncList();
		else
			genericList = this.getAmountWageIncList();

		String SQL_MDU_WAGEINCREASE_FORM_EXISTING_RECORD = "SELECT COUNT(*) FROM MDU_WAGEINCREASE_FORM WHERE DUESYEAR = "
			+ year + " AND empAffFk = " + empAffPk;
		log.debug("SQL_MDU_WAGEINCREASE_FORM_EXISTING_RECORD  = " + SQL_MDU_WAGEINCREASE_FORM_EXISTING_RECORD);
		rs = stmt.executeQuery(SQL_MDU_WAGEINCREASE_FORM_EXISTING_RECORD);

		int ct = 0;

		while (rs.next()) {
			ct = rs.getInt(1);
		}

		if (empAffPk != 0) {
			if (ct != 0) {
				// the same year data already exist
				errors.add("duplicateForSameYrSameEmp");
			}
			else { 		// employer exists but this year's data is not there
				insertIntoFormDataTable(con, stmt, rs);
				//insertIntoContactTable(con, stmt, rs);
			}
		}

		con.commit();
    }
    catch (Exception exc) {
		try {
		      con.rollback();
		}
		catch (Exception e) {
		   log.debug("Rollback Transaction.");
		}
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return errors;
  }

  private void insertIntoFormDataTable(Connection con, Statement stmt, ResultSet rs) throws java.sql.SQLException {

		/*
		String SQL_AFFFK_MDU_EMP_AFF_SELECT = "SELECT AFFPK FROM MDU_EMP_AFF WHERE EMPID = '" + empId + "'";
		log.debug("SQL_AFFFK_MDU_EMP_AFF_SELECT  = " + SQL_AFFFK_MDU_EMP_AFF_SELECT);

		rs = stmt.executeQuery(SQL_AFFFK_MDU_EMP_AFF_SELECT);

		while (rs.next()) {
		  affPk = rs.getInt(1);
		}
		*/

		// use above affFk to insert into form table;
		String SQL_WAGEINC_FORM_INSERT = "INSERT INTO MDU_WAGEINCREASE_FORM "
				+ "(empAffFk, duesyear, employer_name, tot_mem_num, average_wages, "
				+ " form_completed, correspondence, correspondence_dt, comments, "
				+ " agreementFk, end_dt, start_dt, in_negotiations, agreement_received, agreement_desc, "
				+ " contact_name, contact_email, contact_phone, "
				+ " created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) VALUES ("
				+ empAffPk + ", " + year.trim() + ", '" + this.getForm_employer_name().trim().replaceAll("\'", "\'\'") + "', "
				+ ((this.getNumberOfMember() == null || this.getNumberOfMember().trim().length() == 0) ? 0 : Integer.valueOf(this.getNumberOfMember().trim()).intValue()) + ", "
				+ ((averageWage == null || averageWage.trim().equalsIgnoreCase("") || averageWage.trim().length() == 0) ? "0.00" : averageWage.trim()) + ", "
				+ ((formCompleted == null || formCompleted.trim().length() == 0) ? 0 : 1) + ", "
				+ ((correspondence == null || correspondence.trim().length() == 0) ? 0 : 1) + ", "
				+ " '" + ((correspondenceDate == null || correspondenceDate.trim().equalsIgnoreCase("") || correspondenceDate.trim().length() == 0) ? "" : correspondenceDate.trim()) + "', "
				+ " '" + ((comments == null || comments.trim().equalsIgnoreCase("") || comments.trim().length() == 0) ? "" : comments.trim()) + "', "
				+ ((agreementPk == 0) ? null : (""+agreementPk))  + ", "
				+ " '" + ((durationTo == null || durationTo.trim().equalsIgnoreCase("") || durationTo.trim().length() == 0) ? "" : durationTo.trim()) + "', "
				+ " '" + ((durationFrom == null || durationFrom.trim().equalsIgnoreCase("") || durationFrom.trim().length() == 0) ? "" : durationFrom.trim()) + "', "
				+ ((inNegotiations != null && inNegotiations.trim().equalsIgnoreCase("no")) ? 0 : 1) + ", "
				+ ((agreementReceived == null || agreementReceived.trim().length() == 0) ? 0 : 1) + ", '"
				+ ((agreementDesc == null || agreementDesc.trim().equalsIgnoreCase("") || agreementDesc.trim().length() == 0) ? "" : agreementDesc.trim()) + "', '"
				+ ((personName == null || personName.trim().equalsIgnoreCase("") || personName.trim().length() == 0) ? "" : personName.trim()) + "', '"
				+ ((telephone == null || telephone.trim().equalsIgnoreCase("") || telephone.trim().length() == 0) ? "" : telephone.trim()) + "', '"
				+ ((email == null || email.trim().equalsIgnoreCase("") || email.trim().length() == 0) ? "" : email.trim()) + "', "
				+ 10000002 + ", getDate()," + 10000002 + ", getDate() )";

		log.debug("SQL_WAGEINC_FORM_INSERT  = " + SQL_WAGEINC_FORM_INSERT);

		stmt.executeUpdate(SQL_WAGEINC_FORM_INSERT);

		// get wifPk back from form table;
		String SQL_GET_WAGEINC_FORM_PK = "SELECT WIFPK FROM MDU_WAGEINCREASE_FORM WHERE empAffFk = "
			+ empAffPk + " AND DUESYEAR = " + this.getYear().trim();
		log.debug("SQL_GET_WAGEINC_FORM_PK  = " + SQL_GET_WAGEINC_FORM_PK);

		rs = stmt.executeQuery(SQL_GET_WAGEINC_FORM_PK);

		while (rs.next()) {
		  wifPk = rs.getInt(1);
		}

		// insert into data table using loop;
		// first add init row of data into WageIncrease_Data List
		if (saveSecA && initPercent_a != null && initPercent_a.trim().length() != 0) {
			initPwib = new PercentWageIncBean(
				initPercent_a.trim(),
				(initEffective_a == null) ? "" : initEffective_a.trim(),
				initNoOfMember_a.trim(),
				(initTypeOfPayment_adj_a == null) ? "" : initTypeOfPayment_adj_a.trim(),
				(((initPercentInc_adj_a == null || initPercentInc_adj_a.trim().equalsIgnoreCase("") || initPercentInc_adj_a.trim().length() == 0))) ? "0" : initPercentInc_adj_a.trim(),
				(((initNoOfMember_adj_a == null || initNoOfMember_adj_a.trim().equalsIgnoreCase("") || initNoOfMember_adj_a.trim().length() == 0))) ? "0" : initNoOfMember_adj_a.trim(),
				initMbrTimesInc_a.trim()
			);

			genericList.add(0, initPwib);

		}
		else if (initAmount_b != null && initAmount_b.trim().length() != 0) {
			initAwib = new AmountWageIncBean(
				initAmount_b.trim(),
				(initEffective_b == null) ? "" : initEffective_b.trim(),
				initNoOfMember_b.trim(),
				(initTypeOfPayment_adj_b == null) ? "" : initTypeOfPayment_adj_b.trim(),
				(((initAmountInc_adj_b == null || initAmountInc_adj_b.trim().equalsIgnoreCase("") || initAmountInc_adj_b.trim().length() == 0))) ? "0" : initAmountInc_adj_b.trim(),
				(((initNoOfMember_adj_b == null || initNoOfMember_adj_b.trim().equalsIgnoreCase("") || initNoOfMember_adj_b.trim().length() == 0))) ? "0" : initNoOfMember_adj_b.trim(),
				initMbrTimesInc_b.trim()
			);

			genericList.add(0, initAwib);
		}

		if (saveSecA)
			wageIncType = 1;
		else {
			if (amountType.trim().equalsIgnoreCase("cent/hr"))
				wageIncType = 2;
			else
				wageIncType = 3;
		}

		for (int i = 0; i < genericList.size(); i++) {
			String SQL_WAGEINC_DATA_INSERT = null;

			if (saveSecA) {
				tmpPwib = (PercentWageIncBean) genericList.get(i);

				SQL_WAGEINC_DATA_INSERT = "INSERT INTO MDU_WAGEINCREASE_DATA "
					+ "(wifFk, num_times_inc, wage_inc_type, wage_inc, effective_date, "
					+ " num_affected, payment_type_adj, wage_inc_adj, num_affected_adj, "
					+ " created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) VALUES ("
					+ wifPk + ", " + TextUtil.formatToNumber(tmpPwib.getMbrTimesInc_a().trim()) + ", " + wageIncType + ", "
					+ tmpPwib.getPercent_a().trim() + ", '"
					+ ((tmpPwib.getEffective_a() == null) ? null : tmpPwib.getEffective_a().trim()) + "', "
					+ TextUtil.formatToNumber(tmpPwib.getNoOfMember_a().trim()) + ", '"
					//+ ((tmpPwib.getTypeOfPayment_adj_a() == null) ? null : tmpPwib.getTypeOfPayment_adj_a().trim()) + "', "
					+ ((((tmpPwib.getTypeOfPayment_adj_a() == null || tmpPwib.getTypeOfPayment_adj_a().trim().equalsIgnoreCase("") || tmpPwib.getTypeOfPayment_adj_a().trim().length() == 0))) ? "" : tmpPwib.getTypeOfPayment_adj_a().trim()) + "', "
					+ ((((tmpPwib.getPercentInc_adj_a() == null || tmpPwib.getPercentInc_adj_a().trim().equalsIgnoreCase("") || tmpPwib.getPercentInc_adj_a().trim().length() == 0))) ? "0" : tmpPwib.getPercentInc_adj_a().trim()) + ", "
					+ TextUtil.formatToNumber(((((tmpPwib.getNoOfMember_adj_a() == null || tmpPwib.getNoOfMember_adj_a().trim().equalsIgnoreCase("") || tmpPwib.getNoOfMember_adj_a().trim().length() == 0))) ? "0" : tmpPwib.getNoOfMember_adj_a().trim())) + ", "
					+ 10000002 + ", getDate(), " + 10000002 + ", getDate() )";

				log.debug("SQL_WAGEINC_DATA_INSERT Sec A = " + SQL_WAGEINC_DATA_INSERT);
				stmt.executeUpdate(SQL_WAGEINC_DATA_INSERT);
			}
			else {
				tmpAwib = (AmountWageIncBean) genericList.get(i);

				SQL_WAGEINC_DATA_INSERT = "INSERT INTO MDU_WAGEINCREASE_DATA "
					+ "(wifFk, num_times_inc, wage_inc_type, wage_inc, effective_date, "
					+ " num_affected, payment_type_adj, wage_inc_adj, num_affected_adj, "
					+ " created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) VALUES ("
					+ wifPk + "," + TextUtil.formatToNumber(tmpAwib.getMbrTimesInc_b().trim()) + "," + wageIncType + ", "
					+ tmpAwib.getAmountInc_b().trim() + ", '"
					+ ((tmpAwib.getEffective_b() == null) ? null : tmpAwib.getEffective_b().trim()) + "', "
					+ TextUtil.formatToNumber(tmpAwib.getNoOfMember_b().trim()) + ", '"
					+ ((tmpAwib.getTypeOfPayment_adj_b() == null) ? "" : tmpAwib.getTypeOfPayment_adj_b().trim()) + "',"
					+ ((((tmpAwib.getAmountInc_adj_b() == null || tmpAwib.getAmountInc_adj_b().trim().equalsIgnoreCase("") || tmpAwib.getAmountInc_adj_b().trim().length() == 0))) ? "0.0" : tmpAwib.getAmountInc_adj_b().trim()) + ","
					+ TextUtil.formatToNumber(((((tmpAwib.getNoOfMember_adj_b() == null || tmpAwib.getNoOfMember_adj_b().trim().equalsIgnoreCase("") || tmpAwib.getNoOfMember_adj_b().trim().length() == 0))) ? "0.0" : tmpAwib.getNoOfMember_adj_b().trim())) + ","
					+ 10000002 + ", getDate()," + 10000002 + ", getDate() )";

				log.debug("SQL_WAGEINC_DATA_INSERT Sec B = " + SQL_WAGEINC_DATA_INSERT);
				stmt.executeUpdate(SQL_WAGEINC_DATA_INSERT);


				// calculate % and insert one more time for converting B data to A data
				double amountIncBtoA = ((Double.valueOf(tmpAwib.getAmountInc_b().trim()).doubleValue()) / ((Double.valueOf(averageWage.trim()).doubleValue())))*100;

				double amountIncBtoA_adj = 0.0;
				if (tmpAwib.getAmountInc_adj_b() == null || tmpAwib.getAmountInc_adj_b().trim() == "" || tmpAwib.getAmountInc_adj_b().trim().length() == 0)
					amountIncBtoA_adj = 0.0;
				else
					amountIncBtoA_adj = (((Double.valueOf(tmpAwib.getAmountInc_adj_b().trim()).doubleValue())) / ((Double.valueOf(averageWage.trim()).doubleValue())))*100;

				double mbrTimesIncBtoA = 0.0;
				if (amountIncBtoA_adj != 0.0)
					mbrTimesIncBtoA = amountIncBtoA * (Double.valueOf(tmpAwib.getNoOfMember_b().trim()).doubleValue())
						+ amountIncBtoA_adj * (Double.valueOf(tmpAwib.getNoOfMember_adj_b().trim()).doubleValue());
				else
					mbrTimesIncBtoA = amountIncBtoA * (Double.valueOf(tmpAwib.getNoOfMember_b().trim()).doubleValue());

				amountIncBtoA = Double.valueOf(TextUtil.formatDouble(amountIncBtoA)).doubleValue();
				amountIncBtoA_adj = Double.valueOf(TextUtil.formatDouble(amountIncBtoA_adj)).doubleValue();
				mbrTimesIncBtoA = Double.valueOf(TextUtil.formatDouble(mbrTimesIncBtoA)).doubleValue();

				SQL_WAGEINC_DATA_INSERT = "INSERT INTO MDU_WAGEINCREASE_DATA "
					+ "(wifFk, num_times_inc, wage_inc_type, wage_inc, effective_date, "
					+ " num_affected, payment_type_adj, wage_inc_adj, num_affected_adj, "
					+ " created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) VALUES ("
					+ wifPk + "," + TextUtil.formatToNumber(""+mbrTimesIncBtoA) + "," + 1 + ", "
					+ amountIncBtoA + ", '"
					+ ((tmpAwib.getEffective_b() == null) ? null : tmpAwib.getEffective_b().trim()) + "', "
					+ tmpAwib.getNoOfMember_b().trim() + ", '"
					+ ((tmpAwib.getTypeOfPayment_adj_b() == null) ? null : tmpAwib.getTypeOfPayment_adj_b().trim()) + "', "
					+ ((amountIncBtoA_adj<=0.0) ? 0.0 : amountIncBtoA_adj) + ","
					+ ((((tmpAwib.getNoOfMember_adj_b() == null || tmpAwib.getNoOfMember_adj_b().trim().equalsIgnoreCase("") || tmpAwib.getNoOfMember_adj_b().trim().length() == 0))) ? "0" : tmpAwib.getNoOfMember_adj_b().trim()) + ","
					+ 10000002 + ", getDate()," + 10000002 + ", getDate() )";

				log.debug("SQL_WAGEINC_DATA_INSERT Sec B To A = " + SQL_WAGEINC_DATA_INSERT);
				stmt.executeUpdate(SQL_WAGEINC_DATA_INSERT);
			}
		}

  }

  /*
  private void insertIntoContactTable(Connection con, Statement stmt, ResultSet rs) throws java.sql.SQLException {

	if ((personName != null && personName.trim().length() != 0) ||
		(telephone != null && telephone.trim().length() != 0) ||
		(email != null && email.trim().length() != 0) )
		{
			// insert into contact table
			String SQL_MDU_CONTACT_INSERT = "INSERT INTO MDU_CONTACT "
						+ "(WIFFK, NAME, PHONE, EMAIL) VALUES ("
						+ wifPk + ", '"
						+ ((personName == null) ? "" : personName.trim()) + "', '"
						+ ((telephone == null) ? "" : telephone.trim()) + "', '"
						+ ((email == null) ? "" : email.trim()) + "'" + ")";
			log.debug("SQL_MDU_CONTACT_INSERT  = " + SQL_MDU_CONTACT_INSERT);

			stmt.executeUpdate(SQL_MDU_CONTACT_INSERT);
		}
  }
  */

  public String getCurrentFormEmployerName(int empAffPk, String year) {
		String rtnVal = "";
		String SQL_SELECT_MDU_WAGEINCREASE_FORM_EMPLOYER_NAME =
            " select employer_name from mdu_wageincrease_form WHERE " +
            " empAffFk = " + empAffPk + " AND " +
            " duesyear = " + year;

		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			rs = null;

			log.debug("SQL_SELECT_MDU_WAGEINCREASE_FORM_EMPLOYER_NAME  = " + SQL_SELECT_MDU_WAGEINCREASE_FORM_EMPLOYER_NAME);

			rs = stmt.executeQuery(SQL_SELECT_MDU_WAGEINCREASE_FORM_EMPLOYER_NAME);

			if (rs.next()) {
				  rtnVal = rs.getString(1);
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			DBUtil.cleanup(con, stmt, rs);
		}


	  	return rtnVal;
  }

  public void deleteFormDataRecord(int empAffPk, String year) { //throws java.sql.SQLException {
	try {
	    //String SQL_MDU_WAGEINCREASE_FORM_PK_SELECT = "SELECT WIFPK FROM MDU_WAGEINCREASE_FORM WHERE EMPID = '"
		//			+ empId + "' AND DUESYEAR = " + year;
		//log.debug("SQL_MDU_WAGEINCREASE_FORM_PK_SELECT  = " + SQL_MDU_WAGEINCREASE_FORM_PK_SELECT);

		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;
		//con.setAutoCommit(false);

		//rs = stmt.executeQuery(SQL_MDU_WAGEINCREASE_FORM_PK_SELECT);

		//StringBuffer SQL_MDU_WAGEINCREASE_DATA_DELETE_BUFFER
		//	   = new StringBuffer("DELETE FROM MDU_WAGEINCREASE_DATA WHERE WIFFK = ");
		//int tmpCt = 0;

	    //while (rs.next()) {
        //        wifPk = rs.getInt(1);
        //        if (tmpCt == 0)
        //            SQL_MDU_WAGEINCREASE_DATA_DELETE_BUFFER.append(""+wifPk);
        //        else
        //            SQL_MDU_WAGEINCREASE_DATA_DELETE_BUFFER.append(" OR WIFFK = "+wifPk);

        //        tmpCt = tmpCt + 1;
		//}

		//String SQL_MDU_WAGEINCREASE_DATA_DELETE = SQL_MDU_WAGEINCREASE_DATA_DELETE_BUFFER.toString();
		//log.debug("SQL_MDU_WAGEINCREASE_DATA_DELETE  = " + SQL_MDU_WAGEINCREASE_DATA_DELETE);
		//if (tmpCt > 0)
		//		stmt.executeUpdate(SQL_MDU_WAGEINCREASE_DATA_DELETE);

		String SQL_MDU_WAGEINCREASE_FORM_DELETE = "DELETE FROM MDU_WAGEINCREASE_FORM WHERE empAffFk = "
								+ empAffPk + " AND DUESYEAR = " + year;
		log.debug("SQL_MDU_WAGEINCREASE_FORM_DELETE  = " + SQL_MDU_WAGEINCREASE_FORM_DELETE);

		stmt.executeUpdate(SQL_MDU_WAGEINCREASE_FORM_DELETE);
    }
	catch (Exception exc) {
		exc.printStackTrace();
    }
	finally {
		DBUtil.cleanup(con, stmt, rs);
    }

  }


  /** Getter for property amountWageIncList.
   * @return Value of property amountWageIncList.
   *
   */
  public java.util.ArrayList getAmountWageIncList() {
      return amountWageIncList;
  }

  /** Setter for property amountWageIncList.
   * @param amountWageIncList New value of property amountWageIncList.
   *
   */
  public void setAmountWageIncList(java.util.ArrayList amountWageIncList) {
      this.amountWageIncList = amountWageIncList;
  }

  /** Getter for property affCode.
   * @return Value of property affCode.
   *
   */
  public java.lang.String getAffCode() {
      return affCode;
  }

  /** Setter for property affCode.
   * @param affCode New value of property affCode.
   *
   */
  public void setAffCode(java.lang.String affCode) {
      this.affCode = affCode;
  }

  /** Getter for property telephone.
   * @return Value of property telephone.
   *
   */
  public java.lang.String getTelephone() {
      return telephone;
  }

  /** Setter for property telephone.
   * @param telephone New value of property telephone.
   *
   */
  public void setTelephone(java.lang.String telephone) {
      this.telephone = telephone;
  }

  /** Getter for property personName.
   * @return Value of property name.
   *
   */
  public java.lang.String getPersonName() {
      return this.personName;
  }

  /** Setter for property name.
   * @param name New value of property name.
   *
   */
  public void setPersonName(java.lang.String personName) {
      this.personName = personName;
  }

  /** Getter for property averageWage.
   * @return Value of property averageWage.
   *
   */
  public java.lang.String getAverageWage() {
      return averageWage;
  }

  /** Setter for property averageWage.
   * @param averageWage New value of property averageWage.
   *
   */
  public void setAverageWage(java.lang.String averageWage) {
  	  if (averageWage != null) {

        	this.averageWage = TextUtil.formatToNumber(averageWage);
  	  }
      else
      		this.averageWage = "";
  }

  /** Getter for property formCompleted.
   * @return Value of property formCompleted.
   *
   */
  public String getFormCompleted() {
      return formCompleted;
  }

  /** Setter for property formCompleted.
   * @param formCompleted New value of property formCompleted.
   *
   */
  public void setFormCompleted(String formCompleted) {
      this.formCompleted = formCompleted;
  }

  /** Getter for property correspondence.
   * @return Value of property correspondence.
   *
   */
  public String getCorrespondence() {
      return correspondence;
  }

  /** Setter for property correspondence.
   * @param correspondence New value of property correspondence.
   *
   */
  public void setCorrespondence(String correspondence) {
      this.correspondence = correspondence;
  }

  /** Getter for property correspondenceDate.
   * @return Value of property correspondenceDate.
   *
   */
  public java.lang.String getCorrespondenceDate() {
      return correspondenceDate;
  }

  /** Setter for property correspondenceDate.
   * @param correspondenceDate New value of property correspondenceDate.
   *
   */
  public void setCorrespondenceDate(java.lang.String correspondenceDate) {
      this.correspondenceDate = correspondenceDate;
  }

  /** Getter for property comments.
   * @return Value of property comments.
   *
   */
  public java.lang.String getComments() {
      return comments;
  }

  /** Setter for property comments.
   * @param comments New value of property comments.
   *
   */
  public void setComments(java.lang.String comments) {
	  if (comments != null)
      	this.comments = comments;
      else
      	this.comments = "";
  }


  /*
  public java.lang.String getMoreLocals() {
      return moreLocals;
  }

  public void setMoreLocals(java.lang.String moreLocals) {
	  if (moreLocals != null)
      	this.moreLocals = moreLocals;
      else
      	this.moreLocals = "";
  }
  */

  /** Getter for property initPercent_a.
   * @return Value of property initPercent_a.
   *
   */
  public java.lang.String getInitPercent_a() {
      return initPercent_a;
  }

  /** Setter for property initPercent_a.
   * @param initPercent_a New value of property initPercent_a.
   *
   */
  public void setInitPercent_a(java.lang.String initPercent_a) {
      this.initPercent_a = TextUtil.formatToNumber(initPercent_a);
  }

  /** Getter for property initEffective_a.
   * @return Value of property initEffective_a.
   *
   */
  public java.lang.String getInitEffective_a() {
      return initEffective_a;
  }

  /** Setter for property initEffective_a.
   * @param initEffective_a New value of property initEffective_a.
   *
   */
  public void setInitEffective_a(java.lang.String initEffective_a) {
      this.initEffective_a = initEffective_a;
  }

  /** Getter for property initNoOfMember_a.
   * @return Value of property initNoOfMember_a.
   *
   */
  public java.lang.String getInitNoOfMember_a() {
      return initNoOfMember_a;
  }

  /** Setter for property initNoOfMember_a.
   * @param initNoOfMember_a New value of property initNoOfMember_a.
   *
   */
  public void setInitNoOfMember_a(java.lang.String initNoOfMember_a) {
      this.initNoOfMember_a = TextUtil.formatToNumber(initNoOfMember_a);
  }

  /** Getter for property initTypeOfPayment_adj_a.
   * @return Value of property initTypeOfPayment_adj_a.
   *
   */
  public java.lang.String getInitTypeOfPayment_adj_a() {
      return initTypeOfPayment_adj_a;
  }

  /** Setter for property initTypeOfPayment_adj_a.
   * @param initTypeOfPayment_adj_a New value of property initTypeOfPayment_adj_a.
   *
   */
  public void setInitTypeOfPayment_adj_a(java.lang.String initTypeOfPayment_adj_a) {
      this.initTypeOfPayment_adj_a = initTypeOfPayment_adj_a;
  }

  /** Getter for property initPercentInc_adj_a.
   * @return Value of property initPercentInc_adj_a.
   *
   */
  public java.lang.String getInitPercentInc_adj_a() {
      return initPercentInc_adj_a;
  }

  /** Setter for property initPercentInc_adj_a.
   * @param initPercentInc_adj_a New value of property initPercentInc_adj_a.
   *
   */
  public void setInitPercentInc_adj_a(java.lang.String initPercentInc_adj_a) {
      this.initPercentInc_adj_a = TextUtil.formatToNumber(initPercentInc_adj_a);
  }

  /** Getter for property initNoOfMember_adj_a.
   * @return Value of property initNoOfMember_adj_a.
   *
   */
  public java.lang.String getInitNoOfMember_adj_a() {
      return initNoOfMember_adj_a;
  }

  /** Setter for property initNoOfMember_adj_a.
   * @param initNoOfMember_adj_a New value of property initNoOfMember_adj_a.
   *
   */
  public void setInitNoOfMember_adj_a(java.lang.String initNoOfMember_adj_a) {
      this.initNoOfMember_adj_a = TextUtil.formatToNumber(initNoOfMember_adj_a);
  }

  /** Getter for property initMbrTimesInc_a.
   * @return Value of property initMbrTimesInc_a.
   *
   */
  public java.lang.String getInitMbrTimesInc_a() {
      return initMbrTimesInc_a;
  }

  /** Setter for property initMbrTimesInc_a.
   * @param initMbrTimesInc_a New value of property initMbrTimesInc_a.
   *
   */
  public void setInitMbrTimesInc_a(java.lang.String initMbrTimesInc_a) {
      this.initMbrTimesInc_a = TextUtil.formatToNumber(initMbrTimesInc_a);
  }

  /** Getter for property initAmount_b.
   * @return Value of property initAmount_b.
   *
   */
  public java.lang.String getInitAmount_b() {
      return initAmount_b;
  }

  /** Setter for property initAmount_b.
   * @param initAmount_b New value of property initAmount_b.
   *
   */
  public void setInitAmount_b(java.lang.String initAmount_b) {
      this.initAmount_b = TextUtil.formatToNumber(initAmount_b);
  }

  /** Getter for property initEffective_b.
   * @return Value of property initEffective_b.
   *
   */
  public java.lang.String getInitEffective_b() {
      return initEffective_b;
  }

  /** Setter for property initEffective_b.
   * @param initEffective_b New value of property initEffective_b.
   *
   */
  public void setInitEffective_b(java.lang.String initEffective_b) {
      this.initEffective_b = initEffective_b;
  }

  /** Getter for property initNoOfMember_b.
   * @return Value of property initNoOfMember_b.
   *
   */
  public java.lang.String getInitNoOfMember_b() {
      return initNoOfMember_b;
  }

  /** Setter for property initNoOfMember_b.
   * @param initNoOfMember_b New value of property initNoOfMember_b.
   *
   */
  public void setInitNoOfMember_b(java.lang.String initNoOfMember_b) {
      this.initNoOfMember_b = TextUtil.formatToNumber(initNoOfMember_b);
  }

  /** Getter for property initTypeOfPayment_adj_b.
   * @return Value of property initTypeOfPayment_adj_b.
   *
   */
  public java.lang.String getInitTypeOfPayment_adj_b() {
      return initTypeOfPayment_adj_b;
  }

  /** Setter for property initTypeOfPayment_adj_b.
   * @param initTypeOfPayment_adj_b New value of property initTypeOfPayment_adj_b.
   *
   */
  public void setInitTypeOfPayment_adj_b(java.lang.String initTypeOfPayment_adj_b) {
      this.initTypeOfPayment_adj_b = initTypeOfPayment_adj_b;
  }

  /** Getter for property initAmountInc_adj_b.
   * @return Value of property initAmountInc_adj_b.
   *
   */
  public java.lang.String getInitAmountInc_adj_b() {
      return initAmountInc_adj_b;
  }

  /** Setter for property initAmountInc_adj_b.
   * @param initAmountInc_adj_b New value of property initAmountInc_adj_b.
   *
   */
  public void setInitAmountInc_adj_b(java.lang.String initAmountInc_adj_b) {
      this.initAmountInc_adj_b = TextUtil.formatToNumber(initAmountInc_adj_b);
  }

  /** Getter for property initNoOfMember_adj_b.
   * @return Value of property initNoOfMember_adj_b.
   *
   */
  public java.lang.String getInitNoOfMember_adj_b() {
      return initNoOfMember_adj_b;
  }

  /** Setter for property initNoOfMember_adj_b.
   * @param initNoOfMember_adj_b New value of property initNoOfMember_adj_b.
   *
   */
  public void setInitNoOfMember_adj_b(java.lang.String initNoOfMember_adj_b) {
      this.initNoOfMember_adj_b = TextUtil.formatToNumber(initNoOfMember_adj_b);
  }

  /** Getter for property initMbrTimesInc_b.
   * @return Value of property initMbrTimesInc_b.
   *
   */
  public java.lang.String getInitMbrTimesInc_b() {
      return initMbrTimesInc_b;
  }

  /** Setter for property initMbrTimesInc_b.
   * @param initMbrTimesInc_b New value of property initMbrTimesInc_b.
   *
   */
  public void setInitMbrTimesInc_b(java.lang.String initMbrTimesInc_b) {
      this.initMbrTimesInc_b = TextUtil.formatToNumber(initMbrTimesInc_b);
  }

  /** Getter for property durationTo.
   * @return Value of property durationTo.
   *
   */
  public java.lang.String getDurationTo() {
      return durationTo;
  }

  /** Setter for property durationTo.
   * @param durationTo New value of property durationTo.
   *
   */
  public void setDurationTo(java.lang.String durationTo) {
      this.durationTo = durationTo;
  }

  /** Getter for property inNegotiations.
   * @return Value of property inNegotiations.
   *
   */
  public String getInNegotiations() {
      return inNegotiations;
  }

  /** Setter for property inNegotiations.
   * @param inNegotiations New value of property inNegotiations.
   *
   */
  public void setInNegotiations(String inNegotiations) {
      this.inNegotiations = inNegotiations;
  }

  /** Getter for property statAverage.
   * @return Value of property statAverage.
   *
   */
  public java.lang.String getStatAverage() {
      return statAverage;
  }

  /** Setter for property statAverage.
   * @param statAverage New value of property statAverage.
   *
   */
  public void setStatAverage(java.lang.String statAverage) {
      this.statAverage = statAverage;
  }

  /** Getter for property membershipCt.
   * @return Value of property membershipCt.
   *
   */
  public java.lang.String getMembershipCt() {
      return membershipCt;
  }

  /** Setter for property membershipCt.
   * @param membershipCt New value of property membershipCt.
   *
   */
  public void setMembershipCt(java.lang.String membershipCt) {
      this.membershipCt = membershipCt;
  }

  /** Getter for property numberOfMember.
   * @return Value of property numberOfMember.
   *
   */
  public java.lang.String getNumberOfMember() {
      return numberOfMember;
  }

  /** Setter for property numberOfMember.
   * @param numberOfMember New value of property numberOfMember.
   *
   */
  public void setNumberOfMember(java.lang.String numberOfMember) {
	  if (numberOfMember != null)
      	this.numberOfMember = TextUtil.formatToNumber(numberOfMember.trim());
      else
      	this.numberOfMember = "";
  }

  /** Getter for property employerName.
   * @return Value of property employerName.
   *
   */
  public java.lang.String getEmployerName() {
      return employerName;
  }

  /** Setter for property employerName.
   * @param employerName New value of property employerName.
   *
   */
  public void setEmployerName(java.lang.String employerName) {
	  if (employerName != null)
      	this.employerName = employerName.trim();
      else
      	this.employerName = "";
  }

  /** Getter for property affIdSubUnit.
   * @return Value of property affIdSubUnit.
   *
   */
  public java.lang.String getAffIdSubUnit() {
      return affIdSubUnit;
  }

  /** Setter for property affIdSubUnit.
   * @param affIdSubUnit New value of property affIdSubUnit.
   *
   */
  public void setAffIdSubUnit(java.lang.String affIdSubUnit) {
	  if (affIdSubUnit != null)
      	this.affIdSubUnit = affIdSubUnit.trim();
      else
      	this.affIdSubUnit = "";
  }

  /** Getter for property affIdLocal.
   * @return Value of property affIdLocal.
   *
   */
  public java.lang.String getAffIdLocal() {
      return affIdLocal;
  }

  /** Setter for property affIdLocal.
   * @param affIdLocal New value of property affIdLocal.
   *
   */
  public void setAffIdLocal(java.lang.String affIdLocal) {
	  if (affIdLocal != null)
      	this.affIdLocal = affIdLocal.trim();
      else
      	this.affIdLocal = "";
  }

  /** Getter for property affIdCouncil.
   * @return Value of property affIdCouncil.
   *
   */
  public java.lang.String getAffIdCouncil() {
      return affIdCouncil;
  }

  /** Setter for property affIdCouncil.
   * @param affIdCouncil New value of property affIdCouncil.
   *
   */
  public void setAffIdCouncil(java.lang.String affIdCouncil) {
	  if (affIdCouncil != null)
      	this.affIdCouncil = affIdCouncil.trim();
      else
      	this.affIdCouncil = "";
  }

  /** Getter for property affIdState.
   * @return Value of property affIdState.
   *
   */
  public java.lang.String getAffIdState() {
      return affIdState;
  }

  /** Setter for property affIdState.
   * @param affIdState New value of property affIdState.
   *
   */
  public void setAffIdState(java.lang.String affIdState) {
	  if (affIdState != null)
      	this.affIdState = affIdState.trim();
      else
      	this.affIdState = "";
  }

  /** Getter for property year.
   * @return Value of property year.
   *
   */
  public java.lang.String getYear() {
      return year;
  }

  /** Setter for property year.
   * @param year New value of property year.
   *
   */
  public void setYear(java.lang.String year) {
      this.year = year;
  }

  /** Getter for property amountType.
   * @return Value of property amountType.
   *
   */
  public java.lang.String getAmountType() {
      return amountType;
  }

  /** Setter for property amountType.
   * @param amountType New value of property amountType.
   *
   */
  public void setAmountType(java.lang.String amountType) {
      this.amountType = amountType;
  }

  /** Getter for property email.
   * @return Value of property email.
   *
   */
  public java.lang.String getEmail() {
      return email;
  }

  /** Setter for property email.
   * @param email New value of property email.
   *
   */
  public void setEmail(java.lang.String email) {
      this.email = email;
  }

  /** Getter for property affIdType.
   * @return Value of property affIdType.
   *
   */
  public java.lang.String getAffIdType() {
      return affIdType;
  }

  /** Setter for property affIdType.
   * @param affIdType New value of property affIdType.
   *
   */
  public void setAffIdType(java.lang.String affIdType) {
	  if (affIdType != null)
      	this.affIdType = affIdType;
      else
      	this.affIdType = "";
  }

  /** Getter for property section.
   * @return Value of property section.
   *
   */
  public java.lang.String getSection() {
      return section;
  }

  /** Setter for property section.
   * @param section New value of property section.
   *
   */
  public void setSection(java.lang.String section) {
      this.section = section;
  }

  /** Getter for property agreementReceived.
   * @return Value of property agreementReceived.
   *
   */
  public java.lang.String getAgreementReceived() {
      return agreementReceived;
  }

  /** Setter for property agreementReceived.
   * @param agreementReceived New value of property agreementReceived.
   *
   */
  public void setAgreementReceived(java.lang.String agreementReceived) {
      this.agreementReceived = agreementReceived;
  }

  /** Getter for property agreementDesc.
   * @return Value of property agreementDesc.
   *
   */
  public java.lang.String getAgreementDesc() {
      return agreementDesc;
  }

  /** Setter for property agreementDesc.
   * @param agreementDesc New value of property agreementDesc.
   *
   */
  public void setAgreementDesc(java.lang.String agreementDesc) {
      this.agreementDesc = agreementDesc;
  }

  private String getDurationFrom() {
    return this.durationFrom;
  }

  public void setDurationFrom(String durationFrom) {
    this.durationFrom = durationFrom;
  }

  public ArrayList getPercentWageIncList() {
      return this.percentWageIncList;
  }

  public void setPercentWageIncList(ArrayList percentWageIncList) {
      this.percentWageIncList = percentWageIncList;
  }

  /** Getter for property empAffPk.
   * @return Value of property empAffPk.
   *
   */
  public int getEmpAffPk() {
      return empAffPk;
  }

  /** Setter for property empAffPk.
   * @param empAffPk New value of property empAffPk.
   *
   */
  public void setEmpAffPk(int empAffPk) {
      this.empAffPk = empAffPk;
  }

  /** Getter for property affIdStatus.
   * @return Value of property affIdStatus.
   *
   */
  public java.lang.String getAffIdStatus() {
      return affIdStatus;
  }

  /** Setter for property affIdStatus.
   * @param affIdStatus New value of property affIdStatus.
   *
   */
  public void setAffIdStatus(java.lang.String affIdStatus) {
      this.affIdStatus = affIdStatus;
  }

  /** Getter for property duesyear.
   * @return Value of property duesyear.
   *
   */
  public java.lang.String getDuesyear() {
      return duesyear;
  }

  /** Setter for property duesyear.
   * @param duesyear New value of property duesyear.
   *
   */
  public void setDuesyear(java.lang.String duesyear) {
      this.duesyear = duesyear;
  }

  /** Getter for property form_employer_name.
   * @return Value of property form_employer_name.
   *
   */
  public java.lang.String getForm_employer_name() {
      return form_employer_name;
  }

  /** Setter for property form_employer_name.
   * @param form_employer_name New value of property form_employer_name.
   *
   */
  public void setForm_employer_name(java.lang.String form_employer_name) {
	  if (form_employer_name != null)
      	this.form_employer_name = form_employer_name.trim();
      else
      	this.form_employer_name = "";
  }

  /** Getter for property agreementPk.
   * @return Value of property agreementPk.
   *
   */
  public int getAgreementPk() {
      return agreementPk;
  }

  /** Setter for property agreementPk.
   * @param agreementPk New value of property agreementPk.
   *
   */
  public void setAgreementPk(int agreementPk) {
      this.agreementPk = agreementPk;
  }

  /** Getter for property agreementName.
   * @return Value of property agreementName.
   *
   */
  public java.lang.String getAgreementName() {
      return agreementName;
  }

  /** Setter for property agreementName.
   * @param agreementName New value of property agreementName.
   *
   */
  public void setAgreementName(java.lang.String agreementName) {
      this.agreementName = agreementName;
  }

}