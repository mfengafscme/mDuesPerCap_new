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

public class AgreementDAO {

  private static Logger log = Logger.getLogger(AgreementDAO.class);
  private String agreementName;

  private String startDate = null;
  private String endDate = null;
  private String comments = null;

  private Connection con = null;
  private Statement stmt = null;
  private ResultSet rs = null;

  private int agreementPk = 0;

  public int addAgreement() {
	try {
	    	con = DBUtil.getConnection();
	    	stmt = con.createStatement();
			rs = null;
			con.setAutoCommit(false);

			log.debug("New Agreement");

			// escape ' from Agreement name and comments
			agreementName = this.getAgreementName().replaceAll("\'", "\'\'");
			comments = this.getComments().replaceAll("\'", "\'\'");

			// insert into Agreement table
			String SQL_MDU_AGREEMENTS_INSERT = "INSERT INTO MDU_AGREEMENTS "
						+ "(agreement_name, start_dt, end_dt, comments, "
						+ " created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt ) VALUES ("
						+ "'" + this.getAgreementName().trim() + "', "
						+ "'" + this.getStartDate().trim() + "', "
						+ "'" + this.getEndDate().trim() + "', "
                        + "'" + this.getComments().trim() + "', "
                        + 10000002 + ", getDate()," + 10000002 + ", getDate() )";
			log.debug("SQL_MDU_AGREEMENTS_INSERT  = " + SQL_MDU_AGREEMENTS_INSERT);
			stmt.executeUpdate(SQL_MDU_AGREEMENTS_INSERT);
			con.commit();


			// back select the PK
			String SQL_SELECT_MDU_AGREEMENTS_PK = "SELECT agreementPk FROM MDU_AGREEMENTS " +
				" WHERE agreement_Name = '" + this.getAgreementName().trim() + "'";

			log.debug("SQL_SELECT_MDU_AGREEMENTS_PK  = " + SQL_SELECT_MDU_AGREEMENTS_PK);
			rs = stmt.executeQuery(SQL_SELECT_MDU_AGREEMENTS_PK);

			if (rs.next()) {
				  agreementPk = rs.getInt("agreementPk");
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

    return agreementPk;
  }


  public int getAgreementPkByName(String agreementName) {
	int agreementPk = 0;

	try {
	    	con = DBUtil.getConnection();
	    	stmt = con.createStatement();
			rs = null;

			// escape ' from Agreement name and comments
			agreementName = agreementName.replaceAll("\'", "\'\'");

			String SQL_SELECT_MDU_AGREEMENTS_PK = "SELECT top 1 * FROM MDU_AGREEMENTS " +
				" WHERE agreement_Name = '" + agreementName.trim() + "' order by created_dt DESC";

			log.debug("SQL_SELECT_MDU_AGREEMENTS_PK  = " + SQL_SELECT_MDU_AGREEMENTS_PK);
			rs = stmt.executeQuery(SQL_SELECT_MDU_AGREEMENTS_PK);

			if (rs.next()) {
				  agreementPk = rs.getInt("agreementPk");
			}
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return agreementPk;
  }

  public String getAgreementNameByPk(int agreementPk) {
	String agreementName = null;

	try {
	    	con = DBUtil.getConnection();
	    	stmt = con.createStatement();
			rs = null;

			// back select the PK
			String SQL_SELECT_MDU_AGREEMENTS_NAME = "SELECT agreement_name FROM MDU_AGREEMENTS " +
				" WHERE agreementPk = " + agreementPk;

			log.debug("SQL_SELECT_MDU_AGREEMENTS_NAME  = " + SQL_SELECT_MDU_AGREEMENTS_NAME);
			rs = stmt.executeQuery(SQL_SELECT_MDU_AGREEMENTS_NAME);

			if (rs.next()) {
				  agreementName = rs.getString("agreement_name");
			}
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return agreementName;
  }

  public void deleteAgreement(int agreementPk)  {

    try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
	    rs = null;
	    con.setAutoCommit(false);

		// delete table value
		String SQL_DELETE_MDU_AGREEMENT_EMP = "DELETE FROM MDU_AGREEMENT_EMP WHERE agreementFk = " + agreementPk;
		//String SQL_DELETE_MDU_AGREEMENTS = "DELETE FROM MDU_AGREEMENTS WHERE agreementPk = " + agreementPk;
		String SQL_DELETE_MDU_AGREEMENTS =  " UPDATE MDU_AGREEMENTS SET agmt_status=0  WHERE agreementPk = " + agreementPk;

		log.debug("SQL_DELETE_MDU_AGREEMENT_EMP  = " + SQL_DELETE_MDU_AGREEMENT_EMP);
		stmt.executeUpdate(SQL_DELETE_MDU_AGREEMENT_EMP);

		log.debug("SQL_DELETE_MDU_AGREEMENTS  = " + SQL_DELETE_MDU_AGREEMENTS);
		stmt.executeUpdate(SQL_DELETE_MDU_AGREEMENTS);

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


  public void updateAgreement (int agreementPk)  {

	String SQL_UPDATE_MDU_AGREEMENTS =
            " UPDATE MDU_AGREEMENTS SET " +
            " start_dt = '" + this.getStartDate().trim() + "', " +
            " end_dt = '" + this.getEndDate().trim() + "', " +
            " comments = '" + this.getComments().trim().replaceAll("\'", "\'\'") + "' " +
            " WHERE agreementPk = " + agreementPk;

    try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
	    rs = null;

	    log.debug("SQL_UPDATE_MDU_AGREEMENTS  = " + SQL_UPDATE_MDU_AGREEMENTS);

	    stmt.executeUpdate(SQL_UPDATE_MDU_AGREEMENTS);
 	}
 	catch (Exception exc) {
		exc.printStackTrace();
	}
	finally {
		DBUtil.cleanup(con, stmt, rs);
    }

  }


  public void removeFromAgreement(int empAffPk, int agreementPk)  {
	String SQL_DELETE_MDU_AGREEMENT_EMP = "DELETE FROM MDU_AGREEMENT_EMP "
			+ " WHERE agreementFk = " + agreementPk
			+ " AND empAffFk = " + empAffPk;

    try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
	    rs = null;
	    con.setAutoCommit(false);

		log.debug("SQL_DELETE_MDU_AGREEMENT_EMP  = " + SQL_DELETE_MDU_AGREEMENT_EMP);
		stmt.executeUpdate(SQL_DELETE_MDU_AGREEMENT_EMP);

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

  public void addToAgreement(int empAffPk, int agreementPk)  {
	String SQL_ADD_MDU_AGREEMENT_EMP = "INSERT INTO MDU_AGREEMENT_EMP (agreementFk, empAffFk) "
			+ " values (" + agreementPk + "," + empAffPk + ")";

    try {
	    con = DBUtil.getConnection();
	    stmt = con.createStatement();
	    rs = null;
	    con.setAutoCommit(false);

		log.debug("SQL_ADD_MDU_AGREEMENT_EMP  = " + SQL_ADD_MDU_AGREEMENT_EMP);
		stmt.executeUpdate(SQL_ADD_MDU_AGREEMENT_EMP);

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

  public Collection getAgreementCoveredEmployers(int agreementPk) {
	Collection results = new ArrayList();
	EmployerData data = null;

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;

		String SQL_SELECT_MDU_AGREEMENT_EMP = "SELECT MDU_AGREEMENT_EMP.empAffFk "
			+ " FROM MDU_AGREEMENT_EMP inner join v_mdu_emp_aff "
			+ " on v_mdu_emp_aff.empAffPk = MDU_AGREEMENT_EMP.empAffFk "
			+ " WHERE agreementFk = " + agreementPk
			+ " ORDER BY v_mdu_emp_aff.state, v_mdu_emp_aff.council, cast(v_mdu_emp_aff.local as int), v_mdu_emp_aff.chapter";


		log.debug("SQL_SELECT_MDU_AGREEMENT_EMP  = " + SQL_SELECT_MDU_AGREEMENT_EMP);
		rs = stmt.executeQuery(SQL_SELECT_MDU_AGREEMENT_EMP);

		while (rs.next()) {
			int empAffPk = rs.getInt("empAffFk");

			data = getEmployer(empAffPk);
			results.add(data);
		}
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return results;
  }

  public Collection getAgreementAddEligibleEmployers(int agreementPk) {
	Collection results = new ArrayList();
	EmployerData data = null;

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;

		String SQL_SELECT_MDU_AGREEMENT_EMP_NOT_COVERED = "SELECT MDU_EMP_AFF.empAffPk "
			+ " FROM MDU_EMP_AFF inner join Aff_Organizations "
			+ " on Aff_Organizations.aff_pk = MDU_EMP_AFF.aff_fk "
			+ " WHERE MDU_EMP_AFF.empAffPk not in "
			+ " (SELECT empAffFk from MDU_AGREEMENT_EMP WHERE agreementFk = " + agreementPk
			+ " ) order by Aff_Organizations.AFF_STATEnAT_TYPE, "
			+ " Aff_Organizations.AFF_councilRetiree_chap, "
			+ " cast(Aff_Organizations.AFF_localSubChapter as int), mdu_emp_aff.subunit";


		log.debug("SQL_SELECT_MDU_AGREEMENT_EMP_NOT_COVERED  = " + SQL_SELECT_MDU_AGREEMENT_EMP_NOT_COVERED);
		rs = stmt.executeQuery(SQL_SELECT_MDU_AGREEMENT_EMP_NOT_COVERED);

		while (rs.next()) {
			int empAffPk = rs.getInt("empAffPk");

			data = getEmployer(empAffPk);
			results.add(data);
		}
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return results;
  }


  public Collection getAgreementCoveredEmployersWithStat(int agreementPk) {
	Collection results = new ArrayList();
	EmployerData data = null;

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;

		int empAffPk = 0;
		int stat = 0;
		int memCt = 0;

		String SQL_SELECT_V_MDU_AGREEMENT_EMP = "SELECT EmpAffPk, type, state, council,"
				+ " local, subunit, CurrentEmployerName, statMbrCount, mbrshpCount "
				+ " FROM V_MDU_Agreements_w_STAT "
				+ " WHERE agreementPk = " + agreementPk
				+ " ORDER BY STATE";

System.out.println("**********************");
		log.debug("SQL_SELECT_V_MDU_AGREEMENT_EMP  = " + SQL_SELECT_V_MDU_AGREEMENT_EMP);
		rs = stmt.executeQuery(SQL_SELECT_V_MDU_AGREEMENT_EMP);

		while (rs.next()) {
			empAffPk = rs.getInt("empAffPk");
			stat = rs.getInt("statMbrCount");
			memCt = rs.getInt("mbrshpCount");

			data = getEmployer(empAffPk);
			data.setStat(stat);
			data.setMemCt(memCt);

			results.add(data);
		}
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return results;
  }

  public Collection getAllAgreementsPkNamePair() {
	Collection results = new ArrayList();
	AgreementBean data = null;

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;

		String SQL_SELECT_AGREEMENT_NAME_PK =
        	"SELECT agreementPk, agreement_name FROM mdu_agreements where agmt_status=1 order by agreement_name";


		log.debug("SQL_SELECT_AGREEMENT_NAME_PK  = " + SQL_SELECT_AGREEMENT_NAME_PK);
		rs = stmt.executeQuery(SQL_SELECT_AGREEMENT_NAME_PK);

		results.add(new AgreementBean(""));

		while (rs.next()) {
			String agreementName = rs.getString("agreement_name");
			int agreementPk = rs.getInt("agreementPk");

			data = new AgreementBean(agreementPk, agreementName);
			results.add(data);
		}
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return results;
  }

  public AgreementBean getAgreementBean(int agreementPk) {
	AgreementBean data = null;

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;

		String SQL_SELECT_AGREEMENT_BEAN =
        	"SELECT agreement_name, start_dt, end_dt, comments FROM mdu_agreements where agreementPk = " + agreementPk;


		log.debug("SQL_SELECT_AGREEMENT_BEAN  = " + SQL_SELECT_AGREEMENT_BEAN);
		rs = stmt.executeQuery(SQL_SELECT_AGREEMENT_BEAN);

		if (rs.next()) {
			String tmpAgreementName = rs.getString("agreement_name");
			String tmpStartDate = rs.getString("start_dt");
			String tmpEndDate = rs.getString("end_dt");
			String tmpComments = rs.getString("comments");


			data = new AgreementBean(	agreementPk,
										tmpAgreementName,
										tmpStartDate == null ? "" : TextUtil.format(Timestamp.valueOf(tmpStartDate)),
										tmpEndDate == null ? "" : TextUtil.format(Timestamp.valueOf(tmpEndDate)),
										tmpComments);
		}
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return data;
  }


  public Collection getAllEmployers() {
	Collection results = new ArrayList();
	EmployerData data = null;

	try {
		con = DBUtil.getConnection();
		stmt = con.createStatement();
		rs = null;

		//String SQL_SELECT_MDU_AGREEMENT_EMPD = "SELECT MDU_EMP_AFF.empAffPk FROM MDU_EMP_AFF";
		String SQL_SELECT_MDU_AGREEMENT_EMPD = "SELECT EMPAFFPK, Aff_Organizations.AFF_STATEnAT_TYPE "
				+ " FROM MDU_EMP_AFF inner join Aff_Organizations "
				+ " on Aff_Organizations.aff_pk = MDU_EMP_AFF.aff_fk "
				+ " ORDER BY Aff_Organizations.AFF_STATEnAT_TYPE";

		log.debug("SQL_SELECT_MDU_AGREEMENT_EMPD  = " + SQL_SELECT_MDU_AGREEMENT_EMPD);
		rs = stmt.executeQuery(SQL_SELECT_MDU_AGREEMENT_EMPD);

		while (rs.next()) {
			int empAffPk = rs.getInt("empAffPk");

			data = getEmployer(empAffPk);
			results.add(data);
		}
    }
    catch (Exception exc) {
		exc.printStackTrace();
    }
    finally {
		DBUtil.cleanup(con, stmt, rs);
    }

    return results;
  }

  public EmployerData getEmployer(int empAffPk) {

        if (empAffPk == 0) {
            return null;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        EmployerData data = null;

    	String SQL_SELECT_EMPLOYER_DETAIL = "SELECT a.Type, "
    		+ " a.curr_employer_name, a.State, a.Council, a.Local, a.Chapter, a.active "
			+ " FROM v_MDU_Emp_Aff a WHERE a.empAffPk=?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_EMPLOYER_DETAIL);
            //System.out.println("SQL_SELECT_EMPLOYER_DETAIL = " + SQL_SELECT_EMPLOYER_DETAIL);
            ps.setInt(1, empAffPk);

            rs = ps.executeQuery();

            if (rs.next()) {
                data = new EmployerData();

				data.setType(rs.getString("type"));
                data.setEmployer(rs.getString("curr_employer_name"));
                data.setState(rs.getString("state"));
                data.setCouncil(rs.getInt("council"));
                data.setLocal(rs.getInt("local"));
                data.setChapter(rs.getString("chapter"));
                //data.setStatus(rs.getString("active"));
                data.setEmpAffPk(empAffPk);
            }
            else { // will return null
                log.debug("---------- Affiliate Data NOT Found for empAffPk = " + empAffPk + ". ---------- ");
            }

        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return data;
  }

  /** Getter for property agreementName.
   * @return Value of property agreementName.
   *
   */
  public java.lang.String getAgreementName() {
	  if (agreementName == null)
	  	agreementName = "";

	  return agreementName;
  }

  /** Setter for property agreementName.
   * @param agreementName New value of property agreementName.
   *
   */
  public void setAgreementName(java.lang.String agreementName) {
	  if (agreementName != null)
      	this.agreementName = agreementName.trim();
      else
      	this.agreementName = "";
  }

  /** Getter for property startDate.
   * @return Value of property startDate.
   *
   */
  public java.lang.String getStartDate() {
	  if (startDate == null)
	  	startDate = "";

	  return startDate;
  }

  /** Setter for property startDate.
   * @param startDate New value of property startDate.
   *
   */
  public void setStartDate(java.lang.String startDate) {
      this.startDate = startDate;
  }

  /** Getter for property endDate.
   * @return Value of property endDate.
   *
   */
  public java.lang.String getEndDate() {
	  if (endDate == null)
	  	endDate = "";

      return endDate;
  }

  /** Setter for property endDate.
   * @param endDate New value of property endDate.
   *
   */
  public void setEndDate(java.lang.String endDate) {
      this.endDate = endDate;
  }

  /** Getter for property comments.
   * @return Value of property comments.
   *
   */
  public java.lang.String getComments() {
	  if (comments == null)
	  	comments = "";

      return comments;
  }

  /** Setter for property comments.
   * @param comments New value of property comments.
   *
   */
  public void setComments(java.lang.String comments) {
 	  if (comments != null)
       	this.comments = comments.trim();
       else
      	this.comments = "";
  }

}

