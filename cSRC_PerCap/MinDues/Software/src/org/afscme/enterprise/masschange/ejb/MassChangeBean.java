package org.afscme.enterprise.masschange.ejb;

// Java Imports
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Iterator;
import javax.ejb.*;
import javax.naming.NamingException;

// AFSCME Enterprise Imports
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.masschange.MassChangeData;
import org.afscme.enterprise.masschange.MassChangeRequest;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.JNDIUtil;

// Other Imports 
import org.apache.log4j.Logger;

/**
 * Manages all Affiliate Mass Change processing.
 *
 * @ejb:bean name="MassChange" display-name="MassChange"
 * jndi-name="MassChange"
 * type="Stateless" view-type="local"
 */
public class MassChangeBean extends SessionBase {

    static Logger logger = Logger.getLogger(MassChangeBean.class);
    
    private MaintainAffiliates affsBean;
    
    /** Inserts a requested Mass Change event into the batch control table. */
    private static String SQL_INSERT_MASS_CHANGE_REQUEST = 
        "INSERT INTO Mass_Change_Batch_Control " + 
        "       (aff_pk, mass_chng_request_dt, requesting_user_pk, " + 
        "       mass_chng_priority, mass_chng_type, new_select_value, " + 
        "       new_flag_value, new_aff_status, new_aff_code, " + 
        "       new_aff_councilRetiree_chap, new_aff_subUnit, " + 
        "       new_aff_stateNat_type, new_aff_localSubChapter, " +
        "       new_aff_type, new_aff_pk) " + 
        "VALUES " + 
        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
// EJB METHODS
        
    /** Gets references to the dependent EJBs */
    public void ejbCreate() throws CreateException {
        try {
            affsBean = JNDIUtil.getMaintainAffiliatesHome().create();
        } catch (NamingException e) {
            throw new EJBException("Unable to find dependent EJBs in MaintainAffiliatesBean.ejbCreate()" + e);
        }
    }
        
// BUSINESS METHODS
    
    /**
     * Updates the nightly run table for the Mass Change Request that is being made.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param request
     */
    public void scheduleMassChange(MassChangeRequest request) { 
        logger.debug("Inside scheduleMassChange");
        if (request == null || 
            request.getAffPk() == null || 
            request.getAffPk().intValue() < 1 ||
            request.getUserPk() == null || 
            request.getUserPk().intValue() < 1 ||
            CollectionUtil.isEmpty(request.getChangePriorityList())
        ) {
            throw new EJBException("Parameter was not valid. Could not process the change request.");
        }
        Connection con = null;
        PreparedStatement ps = null;
        Timestamp now = DateUtil.getCurrentDateTimeAsTimestamp();
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_MASS_CHANGE_REQUEST);
            Iterator it = request.getChangePriorityList().iterator();
            for (int priority = 1; it.hasNext(); priority++) {
                MassChangeData data = (MassChangeData)it.next();
                if (data == null || 
                    data.getMassChangeType() == null || 
                    data.getMassChangeType().intValue() < 1
                ) {
                    throw new EJBException("One of the requested items was not valid. Could not process the change request.");
                }
                ps.setInt(1, request.getAffPk().intValue());
                ps.setTimestamp(2, now);
                ps.setInt(3, request.getUserPk().intValue());
                ps.setInt(4, priority);
                ps.setInt(5, data.getMassChangeType().intValue());
                DBUtil.setNullableInt(ps, 6, data.getNewSelect());
                DBUtil.setNullableBooleanAsShort(ps, 7, data.getNewFlag());
                DBUtil.setNullableInt(ps, 8, data.getStatusChangeType());
                if (data.getNewAffiliateID() == null) {
                    data.setNewAffiliateID(new AffiliateIdentifier(null, null, null, null, null, null, null));
                }
                DBUtil.setNullableChar(ps, 9, data.getNewAffiliateID().getCode());
                DBUtil.setNullableVarchar(ps, 10, data.getNewAffiliateID().getCouncil());
                DBUtil.setNullableVarchar(ps, 11, data.getNewAffiliateID().getSubUnit());
                DBUtil.setNullableVarchar(ps, 12, data.getNewAffiliateID().getState());
                DBUtil.setNullableVarchar(ps, 13, data.getNewAffiliateID().getLocal());
                DBUtil.setNullableChar(ps, 14, data.getNewAffiliateID().getType());
                DBUtil.setNullableInt(ps, 15, data.getNewAffPk());
                ps.addBatch();
            }
            int[] inserts = ps.executeBatch();
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
}