package org.afscme.enterprise.participationgroups.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJBException;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.participationgroups.ParticipationDetailData;
import org.afscme.enterprise.participationgroups.ParticipationGroupData;
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;
import org.afscme.enterprise.participationgroups.ParticipationTypeData;
import org.afscme.enterprise.util.DBUtil;
import org.apache.log4j.Logger;


/**
 * Encapsulates operations against participation group data
 *
 * @ejb:bean name="MaintainParticipationGroups" display-name="MaintainParticipationGroups"
 *              jndi-name="MaintainParticipationGroups"
 *              type="Stateless" view-type="local"
 */
public class MaintainParticipationGroupsBean extends SessionBase {
    
    static Logger logger = Logger.getLogger(MaintainParticipationGroupsBean.class);
    
    
    /** INSERTS a new Participation Group */
    private static final String SQL_INSERT_PARTICIPATION_CD_GROUP =
        "INSERT INTO Participation_Cd_Group " +
        "   (particip_group_nm, particip_group_desc) " +
        "VALUES (?, ?)";
    
    /** INSERTS a new Participation Type for a Group */
    private static final String SQL_INSERT_PARTICIPATION_CD_TYPE =
        "INSERT INTO Participation_Cd_Type " +
        "   (particip_type_nm, particip_type_desc, particip_group_pk) " +
        "VALUES (?, ?, ?)";

    /** SELECTS MAX value plus one for unique Participation Detail Shortcut */
    private static final String SQL_NEXT_SHORTCUT =
        "SELECT MAX(particip_detail_shortcut) + 1  as nextShortCut " +
        "FROM   Participation_Cd_Dtl ";

    /** INSERTS a new Participation Detail */
    private static final String SQL_INSERT_PARTICIPATION_CD_DETAIL =
        "INSERT INTO Participation_Cd_Dtl " +
        "   (particip_detail_nm, particip_detail_desc, particip_detail_status, " +
        "    particip_detail_shortcut, particip_type_pk, " +
        "   created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        "VALUES (?, ?, ?, ?, ?, ?, GETDATE(), ?, GETDATE() )";

    /** INSERTS a new Participation Outcome */
    private static final String SQL_INSERT_PARTICIPATION_CD_OUTCOMES =
        "INSERT INTO Participation_Cd_Outcomes " +
        "   (particip_outcome_nm, particip_outcome_desc, " +
        "   created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        "VALUES (?, ?, ?, GETDATE(), ?, GETDATE() )";

    /** INSERTS a new Participation Outcome Detail record */
    private static final String SQL_INSERT_PARTICIPATION_DTL_OUTCOMES =
        "INSERT INTO Participation_Dtl_Outcomes " +
        "   (particip_detail_pk, particip_outcome_pk) " +
        "VALUES (?, ?)";

    /** UPDATES the Participation Detail Status (for inactivations) */
    private static final String SQL_UPDATE_PARTICIPATION_CD_DETAIL_INACTIVATE =
        "UPDATE Participation_Cd_Dtl " +
        "SET    particip_detail_status = 0, " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
        "WHERE  particip_detail_pk = ? ";

    /** SELECTS the count of Groups with the same name */
    private static final String SQL_SELECT_DUPLICATE_GROUP_COUNT =
        "SELECT COUNT(*) " +
        "FROM   Participation_Cd_Group " +
        "WHERE  particip_group_nm = ? ";

   /** SELECTS the count of Groups with the same name */
    private static final String SQL_SELECT_DISTINCT_GROUPS =
        "SELECT particip_group_pk, particip_group_nm " +
        "FROM   Participation_Cd_Group ";
    
    /** SELECTS a Participation Group by its groupPk */
    private static final String SQL_SELECT_PARTICIPATION_CD_GROUP_BY_GROUP =
        "SELECT particip_group_nm, particip_group_desc " +
        "FROM   Participation_Cd_Group " +
        "WHERE  particip_group_pk = ? ";

    /** SELECTS all of the Participation Groups */
    private static final String SQL_SELECT_PARTICIPATION_GROUPS =
        "SELECT particip_group_pk, particip_group_nm, particip_group_desc " +
        "FROM   Participation_Cd_Group ";

    /** SELECTS a Participation Type by its groupPk and typePk */
    private static final String SQL_SELECT_PARTICIPATION_CD_TYPE_BY_TYPE =
        "SELECT particip_type_nm, particip_type_desc " +
        "FROM   Participation_Cd_Type " +
        "WHERE  particip_group_pk = ? " +
        "AND    particip_type_pk = ? ";

    /** SELECTS all of the Participation Types for a Group */
    private static final String SQL_SELECT_PARTICIPATION_TYPES_BY_GROUP =
        "SELECT particip_type_pk, particip_type_nm, particip_type_desc " +
        "FROM   Participation_Cd_Type " +
        "WHERE  particip_group_pk = ? ";

    /** SELECTS a of the Participation Types for a Group */
    private static final String SQL_SELECT_PARTICIPATION_TYPE_BY_GROUP =
        "SELECT particip_type_pk, particip_type_nm, particip_type_desc " +
        "FROM   Participation_Cd_Type " +
        "WHERE  particip_group_pk = ? and particip_type_pk = ? ";
    
    /** SELECTS a Participation Detail by its Group, Type and Detail Pks */
    private static final String SQL_SELECT_PARTICIPATION_DETAIL_BY_DETAIL =
        "SELECT particip_detail_pk, particip_detail_nm, particip_detail_desc, " +
        "       particip_detail_status, particip_detail_shortcut, " +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt " +        
        "FROM   Participation_Cd_Dtl pcd " +
        "JOIN   Participation_Cd_Type pct   ON pct.particip_type_pk = pcd.particip_type_pk " +
        "JOIN   Participation_Cd_Group pcg  ON pcg.particip_group_pk = pct.particip_group_pk " +
        "WHERE  pcg.particip_group_pk = ? " +
        "AND    pct.particip_type_pk = ? " +
        "AND    particip_detail_pk = ? ";

    /** SELECTS a Participation Detail by its Shortcut */
    private static final String SQL_SELECT_PARTICIPATION_DETAIL_BY_SHORTCUT =
        "SELECT particip_detail_pk, particip_detail_nm, particip_detail_desc, " +
        "       particip_detail_status, particip_detail_shortcut, " +
        "       pct.particip_group_pk, pct.particip_type_pk, " +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt " +
        "FROM   Participation_Cd_Dtl pcd " +
        "JOIN   Participation_Cd_Type pct   ON pct.particip_type_pk = pcd.particip_type_pk " +
        "WHERE  particip_detail_shortcut = ? ";
    
    /** SELECTS all of the Participation Details for a Type */
    private static final String SQL_SELECT_PARTICIPATION_DETAILS_BY_TYPE =
        "SELECT particip_detail_pk, particip_detail_nm, particip_detail_desc, " +
        "       particip_detail_status, particip_detail_shortcut, " +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt " +        
        "FROM   Participation_Cd_Dtl " +
        "WHERE  particip_type_pk = ? ";    

    /** SELECTS the Participation Detail for a type with a given detail */
    private static final String SQL_SELECT_PARTICIPATION_DETAILS_BY_DETAIL =
        "SELECT particip_detail_pk, particip_detail_nm, particip_detail_desc, " +
        "       particip_detail_status, particip_detail_shortcut, " +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt " +        
        "FROM   Participation_Cd_Dtl " +
        "WHERE  particip_type_pk = ? and particip_detail_pk = ? ";    
    
    /** SELECTS all of the Participation Details for a Group (regardless of Type) */
    private static final String SQL_SELECT_PARTICIPATION_DETAILS_BY_GROUP =
        "SELECT     particip_detail_pk, particip_detail_nm, particip_detail_desc, " +
        "           particip_detail_status, particip_detail_shortcut, pcd.particip_type_pk, " +
        "           created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt " +
        "FROM       Participation_Cd_Dtl pcd " +
        "JOIN       Participation_Cd_Type pct   ON pct.particip_type_pk = pcd.particip_type_pk " +
        "WHERE      particip_group_pk = ? " +
        "ORDER BY   pcd.particip_type_pk, particip_detail_pk ";

    /** SELECTS a Participation Outcome Detail by its groupPk, typePk, detailPk and outcomePk */
    private static final String SQL_SELECT_PARTICIPATION_DETAIL_OUTCOME_BY_OUTCOME =
        "SELECT pco.particip_outcome_pk, particip_outcome_nm, particip_outcome_desc, " +
        "       pco.created_user_pk, pco.created_dt, pco.lst_mod_user_pk, pco.lst_mod_dt, " +
        "       pcg.particip_group_pk, pcg.particip_group_nm, " +
        "       pct.particip_type_pk, pct.particip_type_nm, " +
        "       pcd.particip_detail_pk, pcd.particip_detail_nm, pcd.particip_detail_shortcut " +
        "FROM   Participation_Cd_Outcomes   pco " +
        "JOIN   Participation_Dtl_Outcomes  pdo  ON pdo.particip_outcome_pk = pco.particip_outcome_pk " +
        "JOIN   Participation_Cd_Dtl        pcd  ON pcd.particip_detail_pk = pdo.particip_detail_pk " +
        "JOIN   Participation_Cd_Type       pct  ON pct.particip_type_pk = pcd.particip_type_pk " +
        "JOIN   Participation_Cd_Group      pcg  ON pcg.particip_group_pk = pct.particip_group_pk " +
        "WHERE  pcg.particip_group_pk = ? " +
        "AND    pct.particip_type_pk = ? " +
        "AND    pdo.particip_detail_pk = ? " +
        "AND    pdo.particip_outcome_pk = ? ";

    /** SELECTS all of the Participation Outcomes for a Detail */
    private static final String SQL_SELECT_PARTICIPATION_OUTCOMES_BY_DETAIL =
        "SELECT pco.particip_outcome_pk, particip_outcome_nm, particip_outcome_desc, " +
        "       pco.created_user_pk, pco.created_dt, pco.lst_mod_user_pk, pco.lst_mod_dt, " +
        "       pcg.particip_group_pk, pcg.particip_group_nm, " +
        "       pct.particip_type_pk, pct.particip_type_nm, " +
        "       pcd.particip_detail_pk, pcd.particip_detail_nm, pcd.particip_detail_shortcut " +
        "FROM   Participation_Cd_Outcomes   pco " +
        "JOIN   Participation_Dtl_Outcomes  pdo  ON pdo.particip_outcome_pk = pco.particip_outcome_pk " +
        "JOIN   Participation_Cd_Dtl        pcd  ON pcd.particip_detail_pk = pdo.particip_detail_pk " +
        "JOIN   Participation_Cd_Type       pct  ON pct.particip_type_pk = pcd.particip_type_pk " +
        "JOIN   Participation_Cd_Group      pcg  ON pcg.particip_group_pk = pct.particip_group_pk " +
        "WHERE  pdo.particip_detail_pk = ? ";

    /** SELECTS all of the Participation Outcomes for a Group (regardless of Type or Detail) */
    private static final String SQL_SELECT_PARTICIPATION_OUTCOMES =
        "SELECT     pco.particip_outcome_pk, particip_outcome_nm, particip_outcome_desc, " +
        "           pco.created_user_pk, pco.created_dt, pco.lst_mod_user_pk, pco.lst_mod_dt, " +
        "           pcg.particip_group_pk, pcg.particip_group_nm, " +
        "           pct.particip_type_pk, pct.particip_type_nm, " +
        "           pcd.particip_detail_pk, pcd.particip_detail_nm, pcd.particip_detail_shortcut " +
        "FROM       Participation_Cd_Outcomes   pco " +
        "JOIN       Participation_Dtl_Outcomes  pdo  ON pdo.particip_outcome_pk = pco.particip_outcome_pk " +
        "JOIN       Participation_Cd_Dtl        pcd  ON pcd.particip_detail_pk = pdo.particip_detail_pk " +
        "JOIN       Participation_Cd_Type       pct  ON pct.particip_type_pk = pcd.particip_type_pk " +
        "JOIN       Participation_Cd_Group      pcg  ON pcg.particip_group_pk = pct.particip_group_pk " +
        "ORDER BY   pcg.particip_group_pk, pcd.particip_type_pk, pcd.particip_detail_pk, pdo.particip_outcome_pk ";    
    
    /** SELECTS all of the Participation Outcomes for a Group (regardless of Type or Detail) */
    private static final String SQL_SELECT_PARTICIPATION_OUTCOMES_BY_GROUP =
        "SELECT     pco.particip_outcome_pk, particip_outcome_nm, particip_outcome_desc, " +
        "           pco.created_user_pk, pco.created_dt, pco.lst_mod_user_pk, pco.lst_mod_dt, " +
        "           pcg.particip_group_pk, pcg.particip_group_nm, " +
        "           pct.particip_type_pk, pct.particip_type_nm, " +
        "           pcd.particip_detail_pk, pcd.particip_detail_nm, pcd.particip_detail_shortcut " +
        "FROM       Participation_Cd_Outcomes   pco " +
        "JOIN       Participation_Dtl_Outcomes  pdo  ON pdo.particip_outcome_pk = pco.particip_outcome_pk " +
        "JOIN       Participation_Cd_Dtl        pcd  ON pcd.particip_detail_pk = pdo.particip_detail_pk " +
        "JOIN       Participation_Cd_Type       pct  ON pct.particip_type_pk = pcd.particip_type_pk " +
        "JOIN       Participation_Cd_Group      pcg  ON pcg.particip_group_pk = pct.particip_group_pk " +
        "WHERE      pcg.particip_group_pk = ? " +
        "ORDER BY   pcd.particip_type_pk, pcd.particip_detail_pk, pdo.particip_outcome_pk ";

    /** SELECTS all of the Participation Outcomes for a Group (with a given type) */
    private static final String SQL_SELECT_PARTICIPATION_OUTCOMES_BY_GROUP_AND_TYPE =
        "SELECT     pco.particip_outcome_pk, particip_outcome_nm, particip_outcome_desc, " +
        "           pco.created_user_pk, pco.created_dt, pco.lst_mod_user_pk, pco.lst_mod_dt, " +
        "           pcg.particip_group_pk, pcg.particip_group_nm, " +
        "           pct.particip_type_pk, pct.particip_type_nm, " +
        "           pcd.particip_detail_pk, pcd.particip_detail_nm, pcd.particip_detail_shortcut " +
        "FROM       Participation_Cd_Outcomes   pco " +
        "JOIN       Participation_Dtl_Outcomes  pdo  ON pdo.particip_outcome_pk = pco.particip_outcome_pk " +
        "JOIN       Participation_Cd_Dtl        pcd  ON pcd.particip_detail_pk = pdo.particip_detail_pk " +
        "JOIN       Participation_Cd_Type       pct  ON pct.particip_type_pk = pcd.particip_type_pk " +
        "JOIN       Participation_Cd_Group      pcg  ON pcg.particip_group_pk = pct.particip_group_pk " +
        "WHERE      pcg.particip_group_pk = ? and pct.particip_type_pk = ? " +
        "ORDER BY   pcd.particip_type_pk, pcd.particip_detail_pk, pdo.particip_outcome_pk ";
    
    /** SELECTS the GroupPk for a Participation Group Type */
    private static final String SQL_SELECT_GROUP_PK_BY_TYPE =
        "SELECT particip_group_pk " +
        "FROM   Participation_Cd_Type " + 
        "WHERE  particip_type_pk = ? ";
    
    /** SELECTS the GroupPk for a Participation Group Detail */
    private static final String SQL_SELECT_GROUP_PK_BY_DETAIL =
        "SELECT particip_group_pk " +
        "FROM   Participation_Cd_Type pct " +
        "JOIN   Participation_Cd_Dtl pcd    ON pcd.particip_type_pk = pct.particip_type_pk " +
        "WHERE  particip_detail_pk = ? ";

    /** SELECTS the GroupPk for a Participation Group Outcome Detail */
    private static final String SQL_SELECT_GROUP_PK_BY_OUTCOME =
        "SELECT particip_group_pk " +
        "FROM   Participation_Cd_Type pct " +
        "JOIN   Participation_Cd_Dtl pcd        ON pcd.particip_type_pk = pct.particip_type_pk " +
        "JOIN   Participation_Dtl_Outcomes pdo  ON pdo.particip_detail_pk = pcd.particip_detail_pk " +
        "WHERE  pdo.particip_detail_pk = ? " +
        "AND    pdo.particip_outcome_pk = ? ";

     /** SELECTS the name for a Participation Group Name */
    private static final String SQL_SELECT_GROUP_NAME =
        "SELECT particip_group_nm " +
        "FROM   Participation_Cd_Group " + 
        "WHERE  particip_group_pk = ? ";
    
     /** SELECTS the name for a Participation Type Name */
    private static final String SQL_SELECT_TYPE_NAME =
        "SELECT particip_type_nm " +
        "FROM   Participation_Cd_Type " + 
        "WHERE  particip_type_pk = ? ";

     /** SELECTS the name for a Participation Detail Name */
    private static final String SQL_SELECT_DETAIL_NAME =
        "SELECT particip_detail_nm " +
        "FROM   Participation_Cd_Dtl " + 
        "WHERE  particip_detail_pk = ? ";

    /**
     * Adds a new Participation Detail to a Participation Group and Type.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param detail the Participation Detail Data
     * @param createdByUserPk - User Pk of the user adding the data
     * @return the ParticipationDetailData object with the Primary Key and Shortcut populated.
     */
    public ParticipationDetailData addParticipationDetailData(ParticipationDetailData detail, Integer createdByUserPk)  
    { 
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;        
        Integer detailPk = null;
        int nextShortCut = 1;
        
        try {
            
            con = DBUtil.getConnection();
            
            ps1 = con.prepareStatement(SQL_NEXT_SHORTCUT);
            rs = ps1.executeQuery();

            //get the next shortcut value for insert
            if (rs.next())
                nextShortCut = rs.getInt("nextShortCut");
            
            if (nextShortCut == 0)
                nextShortCut++;
            
            logger.debug("Shortcut insert = " + nextShortCut);
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Insert statement: [" + SQL_INSERT_PARTICIPATION_CD_DETAIL + "] ");
            
            // insert into the Participation_Cd_Dtl 
            ps2 = con.prepareStatement(SQL_INSERT_PARTICIPATION_CD_DETAIL);
        
            ps2.setString(1, detail.getName());
            DBUtil.setNullableVarchar(ps2, 2, detail.getDescription());
            
            //default status to active
            ps2.setInt(3, 1);
            //set the next max value as the shortcut value
            ps2.setInt(4, nextShortCut);
            
            ps2.setInt(5, detail.getTypePk().intValue());
            ps2.setInt(6, createdByUserPk.intValue());
            ps2.setInt(7, createdByUserPk.intValue());
            
            detailPk = DBUtil.insertAndGetIdentity(con, ps2);
            
            //if successful, set the pk
            detail.setDetailPk(detailPk);
            
        } catch (SQLException e) {
            throw new EJBException("Error inserting participation type in MaintainParticipationGroupsBean.addParticipationDetailData()", e);
        } finally {
            DBUtil.cleanup(null, ps1, null);
            DBUtil.cleanup(con, ps2, null);
        }

        //return the detail data with new PK
        return detail;
    }
    
    /**
     * Adds a new Participation Group.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param group the Participation Group Data
     * @return the ParticipationGroupData object with the Primary Key populated.
     */
    public ParticipationGroupData addParticipationGroupData(ParticipationGroupData group)  
    { 
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        Integer groupPk = null;
        
        try {
            
            con = DBUtil.getConnection();
            
            // insert into Participation_Cd_Group
            ps1 = con.prepareStatement(SQL_INSERT_PARTICIPATION_CD_GROUP);

            //set the values to insert
            ps1.setString(1, group.getName());
            DBUtil.setNullableVarchar(ps1, 2, group.getDescription());
            groupPk = DBUtil.insertAndGetIdentity(con, ps1);
            
            //if successful, set the pk
            group.setGroupPk(groupPk);
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Insert statement: [" + SQL_INSERT_PARTICIPATION_CD_TYPE + "] ");
            
            // insert the default Participation_Cd_Type for a new Group...
            ps2 = con.prepareStatement(SQL_INSERT_PARTICIPATION_CD_TYPE);

            ps2.setString(1, ParticipationTypeData.TYPE_GENERAL);
            ps2.setNull(2, Types.VARCHAR);
            ps2.setInt(3, groupPk.intValue());
            
            // insert into Participation_Cd_Type
            ps2.executeUpdate();
            
        } catch (SQLException e) {
            throw new EJBException("Error inserting participation group in MaintainParticipationGroupsBean.addParticipationGroupData()", e);
        } finally {
            DBUtil.cleanup(null, ps1, null);
            DBUtil.cleanup(con, ps2, null);
        }
        
        //return the group data with new PK
        return group;
    }
    
    /**
     * Adds a new Participation Outcome to a Participation Group, Type, and Detail.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param outcome the Participation Outcome Data
     * @param createdByUserPk - User Pk of the user adding the data
     * @return the ParticipationOutcomeData object with the Primary Key populated.
     */
    public ParticipationOutcomeData addParticipationOutcomeData(ParticipationOutcomeData outcome, Integer createdByUserPk)  
    { 
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        Integer outcomePk = null;
        
        try {

            con = DBUtil.getConnection();
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Insert statement: [" + SQL_INSERT_PARTICIPATION_CD_OUTCOMES + "] ");

            // insert into Participation_Cd_Outcomes
            ps1 = con.prepareStatement(SQL_INSERT_PARTICIPATION_CD_OUTCOMES);

            //set the values to insert
            ps1.setString(1, outcome.getOutcomeNm());
            DBUtil.setNullableVarchar(ps1, 2, outcome.getDescription());
            ps1.setInt(3, createdByUserPk.intValue());
            ps1.setInt(4, createdByUserPk.intValue());            
            outcomePk = DBUtil.insertAndGetIdentity(con, ps1);
            
            //if successful, set the pk
            outcome.setOutcomePk(outcomePk);
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Insert statement: [" + SQL_INSERT_PARTICIPATION_DTL_OUTCOMES + "] ");
            
            // insert the association of detail to outcome in the Participation_Dtl_Outcomes...
            ps2 = con.prepareStatement(SQL_INSERT_PARTICIPATION_DTL_OUTCOMES);
            ps2.setInt(1, outcome.getDetailPk().intValue());
            ps2.setInt(2, outcomePk.intValue());
            
            // insert into Participation_Dtl_Outcomes
            ps2.executeUpdate();
            
        } catch (SQLException e) {
            throw new EJBException("Error inserting participation outcomes in MaintainParticipationGroupsBean.addParticipationOutcomeData()", e);
        } finally {
            DBUtil.cleanup(null, ps1, null);
            DBUtil.cleanup(con, ps2, null);
        }
        
        //return the outcome data with new PK
        return outcome;
    }
    
    /**
     * Adds a new Participation Type to a Participation Group.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param type the Participation Type Data
     * @return the ParticipationTypeData object with the Primary Key populated.
     */
    public ParticipationTypeData addParticipationTypeData(ParticipationTypeData type)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        Integer typePk = null;
        
        try {
            
            con = DBUtil.getConnection();
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Insert statement: [" + SQL_INSERT_PARTICIPATION_CD_TYPE + "] ");
            
            // insert into Participation_Cd_Type
            ps = con.prepareStatement(SQL_INSERT_PARTICIPATION_CD_TYPE);

            //set the values to insert
            ps.setString(1, type.getName());
            DBUtil.setNullableVarchar(ps, 2, type.getDescription());
            ps.setInt(3, type.getGroupPk().intValue());
            typePk = DBUtil.insertAndGetIdentity(con, ps);
            
            //if successful, set the pk
            type.setTypePk(typePk);

        } catch (SQLException e) {
            throw new EJBException("Error inserting participation type in MaintainParticipationGroupsBean.addParticipationTypeData()", e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        //return the type data with new PK
        return type;
    }

    /**
     * Determines whether there are any duplicate groups based on an exact match by name.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupName - a string that contains the group name to match on
     * @return boolean TRUE means there is one or more groups that match by name
     */
    public boolean isDuplicateGroup(String groupName) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        
        //retrieve all Groups that match the  name
        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_DUPLICATE_GROUP_COUNT);
            ps.setString(1, groupName);
            
            //get the count
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            if (count != 0)
                return true;
            
        } catch (SQLException e) {
            throw new EJBException("Error checking for duplicate groups in MaintainParticipationGroupsBean.isDuplicateGroup()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return false;
    }
    
    /**
     * Inactivates a Participation Detail.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param detailPk the Participation Detail Primary Key
     * @param updatedByUserPk - User Pk of the user changing the data
     * @return boolean returns TRUE if operation completes successfully, FALSE if the operation
     *  does not complete successfully.
     */
    public boolean inactivateParticipationDetail(Integer detailPk, Integer updatedByUserPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        boolean result;
        
        //inactivate the participation detail data
        try {
            
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PARTICIPATION_CD_DETAIL_INACTIVATE);
            ps.setInt(1, updatedByUserPk.intValue());
            ps.setInt(2, detailPk.intValue());
            result = (ps.executeUpdate() != 0);

        } catch (SQLException e) {
            throw new EJBException("Error inactivating participation detail in MaintainParticipationGroupsBean.inactivateParticipationDetail()", e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        return result;
    }

    /**
     * Retrieves the data for a single Participation Detail.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @param typePk the Participation Type Primary Key
     * @param detailPk the Participation Detail Primary Key
     * @return the ParticipationDetailData object representing Participation Detail.
     */
    public ParticipationDetailData getParticipationDetailData(Integer groupPk, Integer typePk, Integer detailPk)
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationDetailData detail = null;
        
        try {
            
            //retrieve a Participation Detail by its groupPk, typePk and detailPk
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_DETAIL_BY_DETAIL);
            ps.setInt(1, groupPk.intValue());
            ps.setInt(2, typePk.intValue());
            ps.setInt(3, detailPk.intValue());
            rs = ps.executeQuery();

            //put the result into a ParticipationDetailData object
            if (rs.next()) {
                detail = new ParticipationDetailData();
                detail.setGroupPk(groupPk);
                detail.setTypePk(typePk);
                detail.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));
                detail.setName(rs.getString("particip_detail_nm"));
                detail.setDescription(rs.getString("particip_detail_desc"));
                detail.setStatus(rs.getInt("particip_detail_status") == 1);
                detail.setShortcut(rs.getInt("particip_detail_shortcut"));
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving a Participation Detail in MaintainParticipationGroupsBean.getParticipationDetailData()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the detail data object
        return detail;
    }
    
    /**
     * Retrieves the data for a single Participation Detail using the Shortcut value.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param shortcut the Participation Detail's shortcut value
     * @return the ParticipationDetailData object representing Participation Detail.
     */
    public ParticipationDetailData getParticipationDetailData(int shortcut)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationDetailData detail = null;
        
        try {
            
            //retrieve a Participation Detail by its shortcut
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_DETAIL_BY_SHORTCUT);
            ps.setInt(1, shortcut);
            rs = ps.executeQuery();

            //put the result into a ParticipationDetailData object
            if (rs.next()) {
                detail = new ParticipationDetailData();
                detail.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
                detail.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                detail.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));
                detail.setName(rs.getString("particip_detail_nm"));
                detail.setDescription(rs.getString("particip_detail_desc"));
                detail.setStatus(rs.getInt("particip_detail_status") == 1);
                detail.setShortcut(rs.getInt("particip_detail_shortcut"));
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving a Participation Detail by Shortcut in MaintainParticipationGroupsBean.getParticipationDetailData()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the detail data object
        return detail;
    }
    
    /**
     * Retrieves all of the Participation Details for a given Participation Group and Type.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @param typePk the Participation Type Primary Key
     * @return a Collection of ParticipationDetailData objects.
     */
    public List getParticipationDetails(Integer groupPk, Integer typePk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {
            
            //retrieve all the Participation Details for a Type
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_DETAILS_BY_TYPE);
            ps.setInt(1, typePk.intValue());            
            rs = ps.executeQuery();

            //put the results into a list of ParticipationDetailData objects
            while (rs.next()) {
                ParticipationDetailData detail = new ParticipationDetailData();
                detail.setGroupPk(groupPk);
                detail.setTypePk(typePk);
                detail.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));                
                detail.setName(rs.getString("particip_detail_nm"));
                detail.setDescription(rs.getString("particip_detail_desc"));
                detail.setStatus(rs.getInt("particip_detail_status") == 1);
                detail.setShortcut(rs.getInt("particip_detail_shortcut"));
                result.add(detail);
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Details in MaintainParticipationGroupsBean.getParticipationDetails()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }

    /**
     * Retrieves the Participation Details for a given Participation Group and Type and Detail.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @param typePk the Participation Type Primary Key
     * @param detailPk the Participation Detail Primary Key
     * @return a Collection of ParticipationDetailData objects.
     */
    public List getParticipationDetails(Integer groupPk, Integer typePk, Integer detailPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {
            
            //retrieve the Participation Details for a Type with a given detail
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_DETAILS_BY_DETAIL);
            ps.setInt(1, typePk.intValue());            
            ps.setInt(2, detailPk.intValue());            
            rs = ps.executeQuery();

            //put the results into a list of ParticipationDetailData objects
            while (rs.next()) {
                ParticipationDetailData detail = new ParticipationDetailData();
                detail.setGroupPk(groupPk);
                detail.setTypePk(typePk);
                detail.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));                
                detail.setName(rs.getString("particip_detail_nm"));
                detail.setDescription(rs.getString("particip_detail_desc"));
                detail.setStatus(rs.getInt("particip_detail_status") == 1);
                detail.setShortcut(rs.getInt("particip_detail_shortcut"));
                result.add(detail);
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Details in MaintainParticipationGroupsBean.getParticipationDetails()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }
    
    /**
     * Retrieves all of the Participation Details for a given Participation Group.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @return a Collection of ParticipationDetailData objects.
     */
    public List getParticipationDetails(Integer groupPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {
            
            //retrieve all the Participation Details for a Group
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_DETAILS_BY_GROUP);
            ps.setInt(1, groupPk.intValue());            
            rs = ps.executeQuery();

            //put the results into a list of ParticipationDetailData objects
            while (rs.next()) {
                ParticipationDetailData detail = new ParticipationDetailData();
                detail.setGroupPk(groupPk);
                detail.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                detail.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));                
                detail.setName(rs.getString("particip_detail_nm"));
                detail.setDescription(rs.getString("particip_detail_desc"));
                detail.setStatus(rs.getInt("particip_detail_status") == 1);
                detail.setShortcut(rs.getInt("particip_detail_shortcut"));
                result.add(detail);
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Details in a Group in MaintainParticipationGroupsBean.getParticipationDetails()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }
    
    /**
     * Retrieves the data for a single Participation Group.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @return the ParticipationGroupData object representing Participation Group.
     */
    public ParticipationGroupData getParticipationGroupData(Integer groupPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationGroupData group = null;
        
        try {
            
            //retrieve a Participation Group by its groupPk
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_CD_GROUP_BY_GROUP);
            ps.setInt(1, groupPk.intValue());
            rs = ps.executeQuery();

            //put the result into a ParticipationGroupData object
            if (rs.next()) {
                group = new ParticipationGroupData();
                group.setGroupPk(groupPk);
                group.setName(rs.getString("particip_group_nm"));
                group.setDescription(rs.getString("particip_group_desc"));
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving a Participation Group in MaintainParticipationGroupsBean.getParticipationGroupData()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the group data object
        return group;
    }
    
    /**
     * Retrieves all of the Participation groups in the system.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return a Collection of ParticipationGroupData objects.
     */
    public List getParticipationGroups()  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {
            
            //retrieve all the Participation Groups
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_GROUPS);
            rs = ps.executeQuery();
            
            //put the results into a list of ParticipationGroupData objects
            while (rs.next()) {
                ParticipationGroupData group = new ParticipationGroupData();
                group.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
                group.setName(rs.getString("particip_group_nm"));
                group.setDescription(rs.getString("particip_group_desc"));
                result.add(group);
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Groups in MaintainParticipationGroupsBean.getParticipationGroups()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }
    
        /**
     * Retrieves all of the Participation groups in the system.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return a Collection of distinct ParticipationGroupData objects.
     */
    public List getDistinctParticipationGroups()  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {
            
            //retrieve all the Participation Groups
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_DISTINCT_GROUPS);
            rs = ps.executeQuery();
            
            //put the results into a list of ParticipationGroupData objects
            while (rs.next()) {
                ParticipationGroupData group = new ParticipationGroupData();
                group.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
                group.setName(rs.getString("particip_group_nm"));
                result.add(group);
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Groups in MaintainParticipationGroupsBean.getParticipationGroups()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }
    
    /**
     * Retrieves the data for a single Participation Outcome.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @param typePk the Participation Type Primary Key
     * @param detailPk the Participation Detail Primary Key
     * @param outcomePk the Participation Outcome Primary Key
     * @return the ParticipationOutcomeData object representing Participation Outcome.
     */
    public ParticipationOutcomeData getParticipationOutcomeData(Integer groupPk, Integer typePk, Integer detailPk, Integer outcomePk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationOutcomeData outcome = null;
        
        try {

            //retrieve a Participation Outcome Detail 
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_DETAIL_OUTCOME_BY_OUTCOME);
            ps.setInt(1, groupPk.intValue());
            ps.setInt(2, typePk.intValue());
            ps.setInt(3, detailPk.intValue());
            ps.setInt(4, outcomePk.intValue());
            rs = ps.executeQuery();

            //put the result into a ParticipationOutcomeData object
            if (rs.next()) {
                outcome = new ParticipationOutcomeData();
                outcome.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
                outcome.setGroupNm(rs.getString("particip_group_nm"));
                outcome.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                outcome.setTypeNm(rs.getString("particip_type_nm"));
                outcome.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));
                outcome.setDetailNm(rs.getString("particip_detail_nm"));
                outcome.setDetailShortcut(rs.getInt("particip_detail_shortcut"));
                outcome.setOutcomePk(new Integer(rs.getInt("particip_outcome_pk")));
                outcome.setOutcomeNm(rs.getString("particip_outcome_nm"));
                outcome.setDescription(rs.getString("particip_outcome_desc"));
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving a Participation Outcome Detail in MaintainParticipationGroupsBean.getParticipationOutcomeData()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the outcome data object
        return outcome;
    }
    
    /**
     * Retrieves all of the Participation Outcomes for a given Participation Group, Type
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @param typePk the Participation Type Primary Key
     * @return a Collection of ParticipationOutcomeData objects.
     */
    public List getParticipationOutcomes(Integer groupPk, Integer typePk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {

            //retrieve all the Participation Outcome Details for a Group
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_OUTCOMES_BY_GROUP_AND_TYPE);
            ps.setInt(1, groupPk.intValue());            
            ps.setInt(2, typePk.intValue());            
            rs = ps.executeQuery();
            
            //put the results into a list of ParticipationOutcomeData objects
            while (rs.next()) {
                ParticipationOutcomeData outcome = new ParticipationOutcomeData();
                outcome.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
                outcome.setGroupNm(rs.getString("particip_group_nm"));
                outcome.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                outcome.setTypeNm(rs.getString("particip_type_nm"));
                outcome.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));
                outcome.setDetailNm(rs.getString("particip_detail_nm"));
                outcome.setDetailShortcut(rs.getInt("particip_detail_shortcut"));
                outcome.setOutcomePk(new Integer(rs.getInt("particip_outcome_pk")));
                outcome.setOutcomeNm(rs.getString("particip_outcome_nm"));
                outcome.setDescription(rs.getString("particip_outcome_desc"));
                result.add(outcome);
            }
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Outcomes for a Group and Type in MaintainParticipationGroupsBean.getParticipationOutcomes()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }
 
    /**
     * Retrieves all of the Participation Outcomes for a given Participation Group, Type
     *  and Detail.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @param typePk the Participation Type Primary Key
     * @param detailPk the Participation Detail Primary Key
     * @return a Collection of ParticipationOutcomeData objects.
     */
    public List getParticipationOutcomes(Integer groupPk, Integer typePk, Integer detailPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {
            
            //retrieve all the Participation Outcomes for a Detail
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_OUTCOMES_BY_DETAIL);
            ps.setInt(1, detailPk.intValue());            
            rs = ps.executeQuery();

            //put the results into a list of ParticipationOutcomeData objects
            while (rs.next()) {
                ParticipationOutcomeData outcome = new ParticipationOutcomeData();
                outcome.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
                outcome.setGroupNm(rs.getString("particip_group_nm"));
                outcome.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                outcome.setTypeNm(rs.getString("particip_type_nm"));
                outcome.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));
                outcome.setDetailNm(rs.getString("particip_detail_nm"));
                outcome.setDetailShortcut(rs.getInt("particip_detail_shortcut"));
                outcome.setOutcomePk(new Integer(rs.getInt("particip_outcome_pk")));
                outcome.setOutcomeNm(rs.getString("particip_outcome_nm"));
                outcome.setDescription(rs.getString("particip_outcome_desc"));
                result.add(outcome);
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Outcomes in MaintainParticipationGroupsBean.getParticipationOutcomes()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }

    /**
     * Retrieves all of the Participation Outcomes Details for a given Participation Group.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @return a Collection of ParticipationOutcomeData objects.
     */
    public List getParticipationOutcomes(Integer groupPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {

            //retrieve all the Participation Outcome Details for a Group
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_OUTCOMES_BY_GROUP);
            ps.setInt(1, groupPk.intValue());            
            rs = ps.executeQuery();
            
            //put the results into a list of ParticipationOutcomeData objects
            while (rs.next()) {
                ParticipationOutcomeData outcome = new ParticipationOutcomeData();
                outcome.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
                outcome.setGroupNm(rs.getString("particip_group_nm"));
                outcome.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                outcome.setTypeNm(rs.getString("particip_type_nm"));
                outcome.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));
                outcome.setDetailNm(rs.getString("particip_detail_nm"));
                outcome.setDetailShortcut(rs.getInt("particip_detail_shortcut"));
                outcome.setOutcomePk(new Integer(rs.getInt("particip_outcome_pk")));
                outcome.setOutcomeNm(rs.getString("particip_outcome_nm"));
                outcome.setDescription(rs.getString("particip_outcome_desc"));
                result.add(outcome);
            }
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Outcomes for a Group in MaintainParticipationGroupsBean.getParticipationOutcomes()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }
    
    /**
     * Retrieves all of the Participation Outcomes Details for a given Participation Group.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return a Collection of all ParticipationOutcomeData objects.
     */
    public List getParticipationOutcomes()  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {

            //retrieve all the Participation Outcome Details for a Group
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_OUTCOMES);          
            rs = ps.executeQuery();
            
            //put the results into a list of ParticipationOutcomeData objects
            while (rs.next()) {
                ParticipationOutcomeData outcome = new ParticipationOutcomeData();
                outcome.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
                outcome.setGroupNm(rs.getString("particip_group_nm"));
                outcome.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                outcome.setTypeNm(rs.getString("particip_type_nm"));
                outcome.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));
                outcome.setDetailNm(rs.getString("particip_detail_nm"));
                outcome.setDetailShortcut(rs.getInt("particip_detail_shortcut"));
                outcome.setOutcomePk(new Integer(rs.getInt("particip_outcome_pk")));
                outcome.setOutcomeNm(rs.getString("particip_outcome_nm"));
                outcome.setDescription(rs.getString("particip_outcome_desc"));
                result.add(outcome);
            }
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Outcomes in MaintainParticipationGroupsBean.getParticipationOutcomes()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }    
    
    
    /**
     * Retrieves the data for a single Participation Type.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @param typePk the Participation Type Primary Key
     * @return the ParticipationTypeData object representing Participation Type.
     */
    public ParticipationTypeData getParticipationTypeData(Integer groupPk, Integer typePk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationTypeData type = null;
        
        try {

            //retrieve a Participation Type 
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_CD_TYPE_BY_TYPE);
            ps.setInt(1, groupPk.intValue());
            ps.setInt(2, typePk.intValue());
            rs = ps.executeQuery();
            
            //put the result into a ParticipationTypeData object
            if (rs.next()) {
                type = new ParticipationTypeData();
                type.setGroupPk(groupPk);
                type.setTypePk(typePk);
                type.setName(rs.getString("particip_type_nm"));
                type.setDescription(rs.getString("particip_type_desc"));
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving a Participation Type in MaintainParticipationGroupsBean.getParticipationTypeData()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the type data object
        return type;
    }
    
    /**
     * Retrieves all of the Participation Types for a given group.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @return a Collection of ParticipationTypeData objects.
     */
    public List getParticipationTypes(Integer groupPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {
            
            //retrieve all the Participation Types for a Group
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_TYPES_BY_GROUP);
            ps.setInt(1, groupPk.intValue());            
            rs = ps.executeQuery();

            //put the results into a list of ParticipationTypeData objects
            while (rs.next()) {
                ParticipationTypeData type = new ParticipationTypeData();
                type.setGroupPk(groupPk);
                type.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                type.setName(rs.getString("particip_type_nm"));
                type.setDescription(rs.getString("particip_type_desc"));
                result.add(type);
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Types in MaintainParticipationGroupsBean.getParticipationTypes()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of objects
        return result;
    }

    /**
     * Retrieves a Participation Type for a given group.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupPk the Participation Group Primary Key
     * @param typePk the Participation Type Primary Key
     * @return a Collection of ParticipationTypeData objects.
     */
    public List getParticipationTypes(Integer groupPk, Integer typePk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {
            
            //retrieve a Participation Type for a Group
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_TYPE_BY_GROUP);
            ps.setInt(1, groupPk.intValue());            
            ps.setInt(2, typePk.intValue());            
            rs = ps.executeQuery();

            //put the result into a list of ParticipationTypeData object
            if (rs.next()) {
                ParticipationTypeData type = new ParticipationTypeData();
                type.setGroupPk(groupPk);
                type.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                type.setName(rs.getString("particip_type_nm"));
                type.setDescription(rs.getString("particip_type_desc"));
                result.add(type);
            }    
        } catch (SQLException e) {
            throw new EJBException("Error retrieving a Participation Type in MaintainParticipationGroupsBean.getParticipationTypes()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the list of object
        return result;
    }

    /**
     * Gets the GroupPk for any pk below the hierarchy.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param pk the Participation Primary Key
     * @param pkType the type of Primary Key (either Group, Type, Detail, Outcome)
     * @param detailPk the Participation Detail Primary Key (needed if Outcome)
     * @return the Participation Group Primary associated with pk.
     */
    public Integer getGroupPk(Integer pk, int pkType, Integer detailPk)
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer groupPk = null;
        
        try {
            
            con = DBUtil.getConnection();

            //determine which query to execute            
            switch (pkType) {
                case ParticipationGroupData.PK_GROUP:
                    return pk;
                case ParticipationGroupData.PK_TYPE:
                    ps = con.prepareStatement(SQL_SELECT_GROUP_PK_BY_TYPE);
                    ps.setInt(1, pk.intValue());
                    break;
                case ParticipationGroupData.PK_DETAIL:
                    ps = con.prepareStatement(SQL_SELECT_GROUP_PK_BY_DETAIL);
                    ps.setInt(1, pk.intValue());
                    break;
                case ParticipationGroupData.PK_OUTCOME:
                    ps = con.prepareStatement(SQL_SELECT_GROUP_PK_BY_OUTCOME);
                    ps.setInt(1, detailPk.intValue());
                    ps.setInt(2, pk.intValue());
                    break;
                default:
                    // should never happen...
                    break;
            }                    

            //retrieve the groupPk
            rs = ps.executeQuery();
            if (rs.next()) 
                groupPk = new Integer(rs.getInt("particip_group_pk"));

        } catch (SQLException e) {
            throw new EJBException("Error determining the groupPk in MaintainParticipationGroupsBean.getGroupPk()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the groupPk
        return groupPk;
    }
    
    /**
     * Gets the Name for any pk below the hierarchy.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param pk the Participation Primary Key
     * @param pkType the type of Primary Key (either Group, Type, Detail, Outcome)
     * @return the Name with pk.
     */
    public String getParticipationName(Integer pk, int pkType)
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String name = null;
        
        try {
            
            con = DBUtil.getConnection();

            //determine which query to execute            
            switch (pkType) {
                case ParticipationGroupData.PK_GROUP:
                    ps = con.prepareStatement(SQL_SELECT_GROUP_NAME);
                    ps.setInt(1, pk.intValue());
                    break;
                case ParticipationGroupData.PK_TYPE:
                    ps = con.prepareStatement(SQL_SELECT_TYPE_NAME);
                    ps.setInt(1, pk.intValue());
                    break;
                case ParticipationGroupData.PK_DETAIL:
                    ps = con.prepareStatement(SQL_SELECT_DETAIL_NAME);
                    ps.setInt(1, pk.intValue());
                    break;
                default:
                    // should never happen...
                    break;
            }                    

            //retrieve the participation name
            rs = ps.executeQuery();
            if (rs.next()) 
                name = rs.getString(1);

        } catch (SQLException e) {
            throw new EJBException("Error determining the name in MaintainParticipationGroupsBean.getParticipationName()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the name
        return name;
    }   
}