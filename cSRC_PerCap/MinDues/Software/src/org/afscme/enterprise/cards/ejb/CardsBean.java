package org.afscme.enterprise.cards.ejb;

import org.afscme.enterprise.codes.Codes.MemberType;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJBException;
import org.afscme.enterprise.cards.RunSummary;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.util.DBUtil;
import org.apache.log4j.Logger;


/**
 * Implements the functionality of the 'Perform Annual Membership Card Run' specification.
 *
 * @ejb:bean name="Cards" display-name="Cards"
 *              jndi-name="Cards"
 *              type="Stateless" view-type="local"
 */
public class CardsBean extends SessionBase{
    
    static Logger logger = Logger.getLogger(CardsBean.class);
    
    private static final String SQL_SELECT_GET_BYPASSED =
        "SELECT t1.aff_pk, t1.aff_type, t1.aff_localSubChapter, " + 
        "       t1.aff_code,  t1.aff_stateNat_type, t1.aff_subUnit, " + 
        "       t1.aff_councilRetiree_chap , t1.aff_ann_mbr_card_run_group, " + 
        "       Number_Total_Members, Number_Cards_Generate, " + 
        "       (Number_Total_Members - Number_Cards_Generate) as Number_Non_Mailable " + 
        "FROM " + 
        "(SELECT DISTINCT ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "                 ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "                 ao.aff_ann_mbr_card_run_group, " + 
        "                 COUNT(DISTINCT am.person_pk) Number_Total_Members " + 
        "FROM         Aff_Organizations ao " + 
        "INNER JOIN   Aff_Mbr_Rpt_Info amri ON ao.aff_pk = amri.aff_pk " + 
        "INNER JOIN   Aff_Members am        ON ao.aff_pk = am.aff_pk " + 
        "WHERE        amri.unit_wide_no_mbr_cards_fg = 1 " + 
        "AND          mbr_yearly_card_run_done_fg = 0 " + 
        "AND          am.mbr_type NOT IN(" + MemberType.A + "," + MemberType.O + ")" + 
        "GROUP BY  ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "          ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "          ao.aff_ann_mbr_card_run_group) as t1 " + 
        "LEFT OUTER JOIN " + 
        "(SELECT DISTINCT ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "                 ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "                 ao.aff_ann_mbr_card_run_group, " + 
        "                 COUNT(DISTINCT am.person_pk) Number_Cards_Generate " + 
        "FROM         Aff_Organizations ao " + 
        "INNER JOIN   Aff_Mbr_Rpt_Info amri ON ao.aff_pk = amri.aff_pk " + 
        "INNER JOIN   Aff_Members am        ON ao.aff_pk = am.aff_pk " + 
        "INNER JOIN   Person_Address pa     ON am.person_pk = pa.person_pk " + 
        "WHERE        amri.unit_wide_no_mbr_cards_fg = 1 " + 
        "AND          mbr_yearly_card_run_done_fg = 0 " + 
        "AND          am.mbr_type NOT IN(" + MemberType.A + "," + MemberType.O + ")" + 
        "AND          addr_bad_fg <> 1 " + 
        "GROUP BY  ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "          ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "          ao.aff_ann_mbr_card_run_group) as t2 " + 
        "ON  t1.aff_pk = t2.aff_pk " + 
        "AND t1.aff_type = t2.aff_type " + 
        "AND t1.aff_localSubChapter = t2.aff_localSubChapter " + 
        "AND t1.aff_code = t2.aff_code " + 
        "AND t1.aff_stateNat_type = t2.aff_stateNat_type " + 
        "AND t1.aff_subUnit = t2.aff_subUnit " + 
        "AND t1.aff_councilRetiree_chap = t2.aff_councilRetiree_chap " + 
        "AND t1.aff_ann_mbr_card_run_group = t2.aff_ann_mbr_card_run_group "; 
    
  
    
private static final String SQL_SELECT_GET_SCHEDULED =
        "SELECT t1.aff_pk, t1.aff_type, t1.aff_localSubChapter, " + 
        "       t1.aff_code,  t1.aff_stateNat_type, t1.aff_subUnit, " + 
        "       t1.aff_councilRetiree_chap , t1.aff_ann_mbr_card_run_group, " + 
        "       Number_Total_Members, Number_Cards_Generate, " + 
        "       (Number_Total_Members - Number_Cards_Generate) as Number_Non_Mailable " + 
        "FROM " + 
        "(SELECT DISTINCT ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "                 ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "                 ao.aff_ann_mbr_card_run_group, " + 
        "                 COUNT(DISTINCT am.person_pk) Number_Total_Members " + 
        "FROM         Aff_Organizations ao " + 
        "INNER JOIN   Aff_Mbr_Rpt_Info amri ON ao.aff_pk = amri.aff_pk " + 
        "INNER JOIN   Aff_Members am        ON ao.aff_pk = am.aff_pk " + 
        "WHERE        amri.unit_wide_no_mbr_cards_fg = 1 " + 
        "AND          mbr_yearly_card_run_done_fg = 0 " + 
        "AND          am.mbr_type NOT IN(" + MemberType.A + "," + MemberType.O + ")" + 
        "GROUP BY  ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "          ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "          ao.aff_ann_mbr_card_run_group) as t1 " + 
        "LEFT OUTER JOIN " + 
        "(SELECT DISTINCT ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "                 ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "                 ao.aff_ann_mbr_card_run_group, " + 
        "                 COUNT(DISTINCT am.person_pk) Number_Cards_Generate " + 
        "FROM         Aff_Organizations ao " + 
        "INNER JOIN   Aff_Mbr_Rpt_Info amri ON ao.aff_pk = amri.aff_pk " + 
        "INNER JOIN   Aff_Members am        ON ao.aff_pk = am.aff_pk " + 
        "INNER JOIN   Person_Address pa     ON am.person_pk = pa.person_pk " + 
        "WHERE        amri.unit_wide_no_mbr_cards_fg = 1 " + 
        "AND          mbr_yearly_card_run_done_fg = 0 " + 
        "AND          am.mbr_type NOT IN(" + MemberType.A + "," + MemberType.O + ")" + 
        "AND          addr_bad_fg <> 1 " + 
        "GROUP BY  ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "          ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "          ao.aff_ann_mbr_card_run_group) as t2 " + 
        "ON  t1.aff_pk = t2.aff_pk " + 
        "AND t1.aff_type = t2.aff_type " + 
        "AND t1.aff_localSubChapter = t2.aff_localSubChapter " + 
        "AND t1.aff_code = t2.aff_code " + 
        "AND t1.aff_stateNat_type = t2.aff_stateNat_type " + 
        "AND t1.aff_subUnit = t2.aff_subUnit " + 
        "AND t1.aff_councilRetiree_chap = t2.aff_councilRetiree_chap " + 
        "AND t1.aff_ann_mbr_card_run_group = t2.aff_ann_mbr_card_run_group ";

   private static final String SQL_SELECT_GET_COMPLETED =
     "SELECT t1.aff_pk, t1.aff_type, t1.aff_localSubChapter, " +
     "  t1.aff_code,  t1.aff_stateNat_type, t1.aff_subUnit, " + 
     "  t1.aff_councilRetiree_chap , t1.aff_ann_mbr_card_run_group, " +
     "  t1.amc_card_run_dt, " +
     "FROM AMC_Group_Completed_Info agci, " +
     "      Aff_Organizations ao " +
     "WHERE ao.aff_pk = agci.aff_pk " ;
  
  private static final String SQL_SELECT_ADD_TO_BYPASS =
    "SELECT t1.aff_pk, t1.aff_type, t1.aff_localSubChapter, " +
     "  t1.aff_code,  t1.aff_stateNat_type, t1.aff_subUnit, " + 
     "  t1.aff_councilRetiree_chap ,  " +
     "FROM  Aff_Organizations ao " +
     "INNER JOIN   Aff_Mbr_Rpt_Info amri ON ao.aff_pk = amri.aff_pk ";
  
  private static final String SQL_SELECT_ADD_TO_BYPASS_RESULTS =
    "SELECT t1.aff_pk, t1.unit_wide_no_mbr_cards_fg, t1.aff_abbreviated_name " +
    "  t1.aff_code,  t1.aff_stateNat_type, t1.aff_subUnit, " + 
    " t1.aff_councilRetiree_chap ,  " +
    "FROM  Aff_Organizations ao " +
    "INNER JOIN   Aff_Mbr_Rpt_Info amri ON ao.aff_pk = amri.aff_pk ";
  
  private static final String SQL_SELECT_REMOVE_FROM_BYPASS = 
     "UPDATE Aff_Mbr_Rpt_Info "  +
     "SET unit_wide_no_mbr_cards_fg = 0 " +
     "WHERE aff_pk = ? " ;
    
  
  private static final String SQL_SELECT_CALCULATE_RUN = 
      
        "SELECT t1.aff_pk, t1.aff_type, t1.aff_localSubChapter, " + 
        "       t1.aff_code,  t1.aff_stateNat_type, t1.aff_subUnit, " + 
        "       t1.aff_councilRetiree_chap , t1.aff_ann_mbr_card_run_group, " + 
        "       Number_Total_Members, Number_Cards_Generate, " + 
        "       (Number_Total_Members - Number_Cards_Generate) as Number_Non_Mailable " + 
        "FROM " + 
        "(SELECT DISTINCT ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "                 ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "                 ao.aff_ann_mbr_card_run_group, " + 
        "                 COUNT(DISTINCT am.person_pk) Number_Total_Members " + 
        "FROM         Aff_Organizations ao " + 
        "INNER JOIN   Aff_Mbr_Rpt_Info amri ON ao.aff_pk = amri.aff_pk " + 
        "INNER JOIN   Aff_Members am        ON ao.aff_pk = am.aff_pk " + 
        "WHERE        amri.unit_wide_no_mbr_cards_fg = 1 " + 
        "AND          ao.aff_ann_mbr_card_run_group = ?  "  +
        "AND          mbr_yearly_card_run_done_fg = 0 " + 
        "AND          am.mbr_type NOT IN(" + MemberType.A + "," + MemberType.O + ")" + 
        "GROUP BY  ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "          ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "          ao.aff_ann_mbr_card_run_group) as t1 " + 
        "LEFT OUTER JOIN " + 
        "(SELECT DISTINCT ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "                 ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "                 ao.aff_ann_mbr_card_run_group, " + 
        "                 COUNT(DISTINCT am.person_pk) Number_Cards_Generate " + 
        "FROM         Aff_Organizations ao " + 
        "INNER JOIN   Aff_Mbr_Rpt_Info amri ON ao.aff_pk = amri.aff_pk " + 
        "INNER JOIN   Aff_Members am        ON ao.aff_pk = am.aff_pk " + 
        "INNER JOIN   Person_Address pa     ON am.person_pk = pa.person_pk " + 
        "WHERE        amri.unit_wide_no_mbr_cards_fg = 1 " + 
        "AND          ao.aff_ann_mbr_card_run_group = ?  "  +
        "AND          mbr_yearly_card_run_done_fg = 0 " + 
        "AND          am.mbr_type NOT IN(" + MemberType.A + "," + MemberType.O + ")" + 
        "AND          addr_bad_fg <> 1 " + 
        "GROUP BY  ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "          ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "          ao.aff_ann_mbr_card_run_group) as t2 " + 
        "ON  t1.aff_pk = t2.aff_pk " + 
        "AND t1.aff_type = t2.aff_type " + 
        "AND t1.aff_localSubChapter = t2.aff_localSubChapter " + 
        "AND t1.aff_code = t2.aff_code " + 
        "AND t1.aff_stateNat_type = t2.aff_stateNat_type " + 
        "AND t1.aff_subUnit = t2.aff_subUnit " + 
        "AND t1.aff_councilRetiree_chap = t2.aff_councilRetiree_chap " + 
        "AND t1.aff_ann_mbr_card_run_group = t2.aff_ann_mbr_card_run_group ";
      
    private static final String SQL_SELECT_PERFORM_RUN =
           "SELECT first_nm, middle_nm, last_nm, aff_localSubChapter, " +
                "aff_councilRetiree_chap, addr1, addr2, city, " +
               " state, zipcode, zip_plus, p.person_pk " +
	   "FROM Aff_Organizations ao " +	
           "JOIN Aff_Members am ON ao.aff_pk = am.aff_pk " +
           "JOIN Person p ON p.person_pk  = am.person_pk " + 
           "JOIN Person_Address addr ON addr.person_pk = p.person_pk and " +
           " address_pk IN " +
	   "            (SELECT address_pk  " +
                                "FROM Person_Address addr " +
                                "WHERE person_pk = addr.person_pk and addr_prmry_fg = 1 " +
				"AND (addr_bad_fg = 0 or addr_bad_fg IS NULL)) " +   
           
        
        "INNER JOIN   Aff_Mbr_Rpt_Info amri ON ao.aff_pk = amri.aff_pk " +  
         
        "WHERE       amri.unit_wide_no_mbr_cards_fg = 0  " +
        "AND         mbr_yearly_card_run_done_fg = 0 " + 
	"AND	     (no_cards_fg = 0 OR no_cards_fg IS NULL)" +
	"AND	     (no_mail_fg = 0 OR no_mail_fg IS NULL) " +
        "AND         mbr_renewal_fg = 0 "  +
                   
        
        "AND          am.mbr_type NOT IN( (" + MemberType.A + "," + MemberType.O + ") " +
        
	"ORDER BY p.person_pk,aff_localSubChapter, " +
        "  aff_councilRetiree_chap, last_nm, first_nm, state, city " ;
    
    private static final String SQL_SELECT_PERFORM_RUN_SYNC =
        
        "INNER JOIN   Person_Address pa     ON am.person_pk = pa.person_pk " + 
        "WHERE        amri.unit_wide_no_mbr_cards_fg = 1 " + 
        "AND          ao.aff_ann_mbr_card_run_group = ?  "  +
        "AND          mbr_yearly_card_run_done_fg = 0 " + 
        "AND          am.mbr_type NOT IN(" + MemberType.A + "," + MemberType.O + ")" + 
        "AND          addr_bad_fg <> 1 " + 
        "GROUP BY  ao.aff_pk, ao.aff_type, ao.aff_localSubChapter, ao.aff_code, " + 
        "          ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_councilRetiree_chap, " + 
        "          ao.aff_ann_mbr_card_run_group) as t2 " + 
        "ON  t1.aff_pk = t2.aff_pk ";

  
  private static final String SQL_SELECT_INITIALIZE_NEW_RUN =

        "UPDATE       Aff_Organizations ao SET mbr_yearly_card_run_done_fg = 0 " +
        "UPDATE       Aff_Members am SET mbr_ret_dues_renewal_fg = 0 " ;
       
  
    /**
     * Calculates the run summaries for all the non-bypassed affiliates, for the given run
     *  type.
     *
     * @param runType common code pk for the type of card run to perform.
     *
     * @return A Set of RunSummary objects
     *
     */
    
  private LinkedList calculateRun(Integer runType) {
        
        Connection con = null;
        PreparedStatement ps = null;        
        ResultSet rs = null;
        LinkedList calculateRunList = new LinkedList();
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_CALCULATE_RUN);
            ps.setInt(1, runType.intValue());
            ps.setInt(2, runType.intValue());
             
            
           //Retrieve Scheduled Cards to Calculate Run
            rs = ps.executeQuery();
            while (rs.next()){
                
                RunSummary runSummary = new RunSummary();
                runSummary.setAffCode(rs.getString("aff_code"));
                runSummary.setAfflocalSubChapter(rs.getString("aff_localSubChapter"));
                runSummary.setAffPk(new Integer(rs.getInt("aff_pk")));
                runSummary.setAffStateNatType(rs.getString("aff_StateNat_type"));
                runSummary.setAffsubUnit(rs.getString("aff_subUnit"));
                runSummary.setCouncilRetireeChap(rs.getString("aff_councilRetiree_chap"));
                runSummary.setRunType(new Integer(rs.getInt("aff_ann_mbr_card_run_group")));
                runSummary.setTotalMembers(rs.getInt("Number_Total_Members"));
                runSummary.setCardsToGenerate(rs.getInt("Number_Cards_Generate"));
                runSummary.setNonMailable(rs.getInt("Number_Non_Mailable"));
                
              
                calculateRunList.add(runSummary);

            }            
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }        
        
        
        return calculateRunList;
       
  }                      
    /**
     * Gets a list of RunSummary objects for all the affiliate scheduled.
     *
     * @return List of RunSummary objects
     */
    
    public LinkedList getScheduled() {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LinkedList cardScheduledResult = new LinkedList();
        
        try {
            
            con = DBUtil.getConnection();
            logger.debug(SQL_SELECT_GET_SCHEDULED);
            ps = con.prepareStatement(SQL_SELECT_GET_SCHEDULED);
            
            //Retrieve Scheduled Cards
            rs = ps.executeQuery();
            while (rs.next()){
                
                RunSummary runSummary = new RunSummary();
                runSummary.setAffCode(rs.getString("aff_code"));
                runSummary.setAfflocalSubChapter(rs.getString("aff_localSubChapter"));
                runSummary.setAffPk(new Integer(rs.getInt("aff_pk")));
                runSummary.setAffStateNatType(rs.getString("aff_StateNat_type"));
                runSummary.setAffsubUnit(rs.getString("aff_subUnit"));
                runSummary.setCouncilRetireeChap(rs.getString("aff_councilRetiree_chap"));
                runSummary.setRunType(new Integer(rs.getInt("aff_ann_mbr_card_run_group")));
                runSummary.setTotalMembers(rs.getInt("Number_Total_Members"));
                runSummary.setCardsToGenerate(rs.getInt("Number_Cards_Generate"));
                runSummary.setNonMailable(rs.getInt("Number_Non_Mailable"));
                
                 
                cardScheduledResult.add(runSummary);
            }
            
        } catch (SQLException e) {
            throw new EJBException("Error getting Scheduled Cards Data CardsBean.getScheduled()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
       
        return cardScheduledResult;
    }

    /**
     * Gets run summary objects for the affiliates that are to be bypassed.
     *
     * @return List of RunSummary objects for the affiliates that are to be bypassed
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    
    public LinkedList getByPassed() {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LinkedList cardbypassedResult = new LinkedList();
      
        
        
        try {
            
            con = DBUtil.getConnection();
            logger.debug(SQL_SELECT_GET_BYPASSED);
            ps = con.prepareStatement(SQL_SELECT_GET_BYPASSED);
            
            //Retrieve Bypassed Cards
            rs = ps.executeQuery();
            while (rs.next()){
                
                RunSummary runSummary = new RunSummary();
                runSummary.setAffCode(rs.getString("aff_code"));
                runSummary.setAfflocalSubChapter(rs.getString("aff_localSubChapter"));
                runSummary.setAffPk(new Integer(rs.getInt("aff_pk")));
                runSummary.setAffStateNatType(rs.getString("aff_StateNat_type"));
                runSummary.setAffsubUnit(rs.getString("aff_subUnit"));
                runSummary.setCouncilRetireeChap(rs.getString("aff_councilRetiree_chap"));
                runSummary.setRunType(new Integer(rs.getInt("aff_ann_mbr_card_run_group")));
                runSummary.setTotalMembers(rs.getInt("Number_Total_Members"));
                runSummary.setCardsToGenerate(rs.getInt("Number_Cards_Generate"));
                runSummary.setNonMailable(rs.getInt("Number_Non_Mailable"));
               
                
                cardbypassedResult.add(runSummary);
            }
            
        } catch (SQLException e) {
            throw new EJBException("Error getting By Passed Data CardsBean.getByPassed()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
           
        }
        
        return cardbypassedResult;
        
    }
 
    /**
     * Gets the list of completed card runs
     *
     * @return List of run summary objects.
     */
    public LinkedList getCompleted() {
         
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LinkedList cardcompletedResult = new LinkedList();
        
        try {
            
            con = DBUtil.getConnection();
            logger.debug(SQL_SELECT_GET_COMPLETED);
            ps = con.prepareStatement(SQL_SELECT_GET_COMPLETED);
            
            //Retrieve Completed Cards
            rs = ps.executeQuery();
            while (rs.next()){
                
                RunSummary runSummary = new RunSummary();
                runSummary.setAmcCardRunDt(rs.getTimestamp("amc_card_run_dt"));
                runSummary.setAffCode(rs.getString("aff_code"));
                runSummary.setAfflocalSubChapter(rs.getString("aff_localSubChapter"));
                runSummary.setAffPk(new Integer(rs.getInt("aff_pk")));
                runSummary.setAffStateNatType(rs.getString("aff_StateNat_type"));
                runSummary.setAffsubUnit(rs.getString("aff_subUnit"));
                runSummary.setCouncilRetireeChap(rs.getString("aff_councilRetiree_chap"));
                runSummary.setRunType(new Integer(rs.getInt("aff_ann_mbr_card_run_group")));
                
                
                cardcompletedResult.add(runSummary);
            }
            
        } catch (SQLException e) {
            throw new EJBException("Error getting Completed Data CardsBean.getByPassed()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return cardcompletedResult;
       
    
        
    }
    
    /**
     * Removes an affiliate from bypass.
     *
     * @param affiliates Set of Integers.  The primary keys of the affiliates to schedule.
     */
    
   public void removeFromBypass(Set affiliate) {
        
                Connection con = null;
                PreparedStatement ps = null;
                logger.debug(SQL_SELECT_REMOVE_FROM_BYPASS);
                  
        
                try {
                  con = DBUtil.getConnection();  
                  Iterator it = affiliate.iterator();
                  while (it.hasNext()) { 
                  Integer aff_pk = (Integer)it.next();
                  
               
                  ps = con.prepareStatement(SQL_SELECT_REMOVE_FROM_BYPASS);
                  ps.setInt(1, aff_pk.intValue());
                 int rt = ps.executeUpdate();
                    }
                }
        
           catch (SQLException e) {
              throw new EJBException("SQL Error Occurred in Remove From Bypass method" + e);
              }
        
               
            finally {
            DBUtil.cleanup(con, ps, null);
            
}

   }
       
    /**
     * Adds an affiliate to the bypass list
     *
     * @param affiliates Set of Integers.  The primary keys of the affiliates to bypass.
     */
    
    public void addToBypass( Integer personPk, Integer affPk) {
        
        Connection con = null;
        PreparedStatement ps = null;
        logger.debug(SQL_SELECT_ADD_TO_BYPASS);
        
        
        try {
            
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ADD_TO_BYPASS);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue());
            int rt = ps.executeUpdate();
  
 
        }
            
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in Add To Bypass method" + e);
            }
           
        finally {
            DBUtil.cleanup(con, ps, null);
            
                   
       }
      
    }
     /**
     * Initiates the run for a given run type.  This method returns immediately, before the
     *  run is performed.
     *
     * @param runType common code primary key.  The type of run to perform
     */
    public void performRun (OutputStream stream) throws Exception   {


        BufferedWriter writer = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        
        
        try {
            
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            con = DBUtil.getConnection();
            
            // Construct the specialized SQL query or a series of queries for this report
            ps = con.prepareStatement(SQL_SELECT_PERFORM_RUN);
            rs = ps.executeQuery();
            writer.write("First Name + Middle Name + Last Name,Local Number,Council Number," +
                         "Address 1 + Address 2 + City, State + ZipCode + ZipPlus" );
                         
            writer.newLine();
            
            while (rs.next()) {
                String fullName = (TextUtil.isEmptyOrSpaces(rs.getString(1)) ? "" : rs.getString(1)) + //first name
                                  (TextUtil.isEmptyOrSpaces(rs.getString(2)) ? "" : " " + rs.getString(2)) + //middle name
                                  (TextUtil.isEmptyOrSpaces(rs.getString(3)) ? "" : " " + rs.getString(3)); //last name
                                  
                String address = (TextUtil.isEmptyOrSpaces(rs.getString(7)) ? "" : rs.getString(7)) + //addr1
                                 (TextUtil.isEmptyOrSpaces(rs.getString(8)) ? "" : " " + rs.getString(8)) + //addr2 
                                 (TextUtil.isEmptyOrSpaces(rs.getString(9)) ? "" : " " + rs.getString(9)) + "," + //city
                                 (TextUtil.isEmptyOrSpaces(rs.getString(10)) ? "" : " " + rs.getString(10)) + //state
                                 (TextUtil.isEmptyOrSpaces(rs.getString(11)) ? "" : " " + rs.getString(11)) + //zipcode
                                 (TextUtil.isEmptyOrSpaces(rs.getString(12)) ? "" : " " + rs.getString(12)); //zip+4                                 

                writer.write(fullName + "," + //first name + middle name + last name
                             rs.getString(5) + "," + //local number
                             rs.getString(6) + "," + //council number
                             address + "," ) ;//addr1 + addr2 + city, + state + zipcode + zipplus
                             
                writer.newLine();
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
            writer.flush();
            writer.close();
        }
        return ;
        
 
    }
 
    
    /**
     * Performs the run for a given run type.  This method does not return until the run
     *  is complete.  This method is called by the CardsMessageBean.
     * @param runType common code primary key.  The type of run to perform
     */

     
    public void performRunSync    (Integer runType) {

        Connection con = null;
        PreparedStatement ps = null;
        logger.debug(SQL_SELECT_PERFORM_RUN_SYNC);
       
        
        try {
            
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERFORM_RUN_SYNC);
            int rt = ps.executeUpdate();
   
           
        }
            
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in Perform Run Sync method" + e);
            }
           
        finally {
            DBUtil.cleanup(con, ps, null);
        } 
 
    
     }
    
    /**
     * Clears the completed list.
     */
    public void initializeNewRun    () {
        
     
        Connection con = null;
        PreparedStatement ps = null;
        logger.debug(SQL_SELECT_INITIALIZE_NEW_RUN);
        
        
        try {
            
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_INITIALIZE_NEW_RUN);
            int rt = ps.executeUpdate();
   
        }
            
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred Trying to Initialize New Run" + e);
            }
        
        finally {
            DBUtil.cleanup(con, ps, null);

         }
 
    }
}