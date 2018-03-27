package org.afscme.enterprise.util;

import org.afscme.enterprise.codes.ejb.MaintainCodesHome;
import java.util.StringTokenizer;
import org.afscme.enterprise.reporting.base.PDFConfigurationData;
import org.afscme.enterprise.reporting.base.LabelConfigurationData;
import javax.rmi.PortableRemoteObject;
import org.afscme.enterprise.reporting.base.ejb.ReportAccessHome;
import org.afscme.enterprise.reporting.base.ejb.BaseReportHome;
import org.afscme.enterprise.roles.ejb.MaintainPrivilegesHome;
import org.afscme.enterprise.controller.ActionPrivileges;
import org.afscme.enterprise.address.ejb.*;
import java.io.PrintStream;
import java.util.Properties;
import org.afscme.enterprise.controller.ejb.AccessControlHome;
import javax.naming.NamingEnumeration;
import org.afscme.enterprise.common.ConfigurationData;
import org.afscme.enterprise.users.ejb.MaintainUsersHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import javax.naming.Binding;
import javax.naming.NotContextException;
import javax.naming.Reference;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesHome;
import org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficersHome;
import org.afscme.enterprise.affiliate.staff.ejb.MaintainAffiliateStaffHome;
import org.afscme.enterprise.masschange.ejb.MassChangeHome;
import org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailHome;
import org.afscme.enterprise.organization.ejb.MaintainOrganizationsHome;
import org.afscme.enterprise.organization.ejb.MaintainOrgLocationsHome;
import org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsHome;
import org.afscme.enterprise.update.ejb.FileQueueHome;
import org.afscme.enterprise.update.ejb.UpdateHome;
import org.afscme.enterprise.member.ejb.MaintainMembersHome;
import org.afscme.enterprise.person.ejb.MaintainPersonsHome;
import org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsHome;
import org.afscme.enterprise.person.ejb.MaintainPersonMailingListsHome;
import org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateHome;
import org.afscme.enterprise.myinfo.ejb.MaintainMyInfoHome;
import org.afscme.enterprise.cards.ejb.CardsHome;
import org.apache.log4j.Logger;


/**
 * Provides static methods for common JNDI functions, including lookups for Home interfaces
 */
public class JNDIUtil {
    
    /** Log4J Logger for this class */
    private static Logger log = Logger.getLogger(JNDIUtil.class);

    /** JNDI Base name for objects placed in JNDI with setObject and getObject */
    private static final String STORE_BASE = "store";

    /**
     * Gets the default initial context for the application.
     * @return InitialContext
     */
    public static InitialContext getInitialContext() throws NamingException {
        return new InitialContext();
    }

    /**
     * Gets the cluster wide initial context for the application.
     * @return InitialContext
     */
    public static InitialContext getClusterWideInitialContext() throws NamingException {
        /*
         * The eap.nocluster property is set to true only if running in the development
         * configuration (i.e. runnin from ant).  In this case, there is no clustered JNDI, so
         * we just use the local JNDI
         */
        if (Boolean.getBoolean("eap.nocluster")) {
            return new InitialContext();
        } else {
            Properties p = new Properties();
            p.put(Context.PROVIDER_URL, ConfigUtil.getConfigurationData().getJNDIClusterURL());
            return new InitialContext(p);
        }
    }
    
    /**
     * Gets a home interface for the MaintainCodes bean.
     * @return MaintainCodesHome
     */
    public static MaintainCodesHome getMaintainCodesHome() throws NamingException {
        Object homeObject = lookupHome(MaintainCodesHome.JNDI_NAME);
        return (MaintainCodesHome)PortableRemoteObject.narrow(homeObject, MaintainCodesHome.class);
    }

    /**
     * Gets a home interface for the MaintainPrivileges bean.
     * @return MaintainPrivilegesHome
     */
    public static MaintainPrivilegesHome getMaintainPrivilegesHome() throws NamingException {
        Object homeObject = lookupHome(MaintainPrivilegesHome.JNDI_NAME);
        return (MaintainPrivilegesHome)PortableRemoteObject.narrow(homeObject, MaintainPrivilegesHome.class);
    }

    /**
     * Gets a home interface for the MaintainUsers bean.
     * @return MaintainUsersHome
     */
    public static MaintainUsersHome getMaintainUsersHome() throws NamingException {
        Object homeObject = lookupHome(MaintainUsersHome.JNDI_NAME);
        return (MaintainUsersHome)PortableRemoteObject.narrow(homeObject, MaintainUsersHome.class);
    }

    /**
     * Gets a home interface for the MaintainAffiliates bean.
     * @return MaintainAffiliatesHome
     */
    public static MaintainAffiliatesHome getMaintainAffiliatesHome() throws NamingException {
        Object homeObject = lookupHome(MaintainAffiliatesHome.JNDI_NAME);
        return (MaintainAffiliatesHome)PortableRemoteObject.narrow(homeObject, MaintainAffiliatesHome.class);
    }

    /**
     * Gets a home interface for the MaintainAffiliateOfficers bean.
     * @return MaintainAffiliateOfficersHome
     */
    public static MaintainAffiliateOfficersHome getMaintainAffiliateOfficersHome() throws NamingException {
        Object homeObject = lookupHome(MaintainAffiliateOfficersHome.JNDI_NAME);
        return (MaintainAffiliateOfficersHome)PortableRemoteObject.narrow(homeObject, MaintainAffiliateOfficersHome.class);
    }

    /**
     * Gets a home interface for the MaintainOrganizations bean.
     * @return MaintainOrganizationsHome
     */
    public static MaintainOrganizationsHome getMaintainOrganizationsHome() throws NamingException {
        Object homeObject = lookupHome(MaintainOrganizationsHome.JNDI_NAME);
        return (MaintainOrganizationsHome)PortableRemoteObject.narrow(homeObject, MaintainOrganizationsHome.class);
    }

    /**
     * Gets a home interface for the MaintainOrgLocations bean.
     * @return MaintainOrgLocationsHome
     */
    public static MaintainOrgLocationsHome getMaintainOrgLocationsHome() throws NamingException {
        Object homeObject = lookupHome(MaintainOrgLocationsHome.JNDI_NAME);
        return (MaintainOrgLocationsHome)PortableRemoteObject.narrow(homeObject, MaintainOrgLocationsHome.class);
    }

    /**
     * Gets a home interface for the MaintainParticipationGroups bean.
     * @return MaintainParticipationGroupsHome
     */
    public static MaintainParticipationGroupsHome getMaintainParticipationGroupsHome() throws NamingException {
        Object homeObject = lookupHome(MaintainParticipationGroupsHome.JNDI_NAME);
        return (MaintainParticipationGroupsHome)PortableRemoteObject.narrow(homeObject, MaintainParticipationGroupsHome.class);
    }

    /**
     * Gets a home interface for the AccessControl bean.
     * @return MaintainUsersHome
     */
    public static AccessControlHome getAccessControlHome() throws NamingException {
        Object homeObject = lookupHome(AccessControlHome.JNDI_NAME);
        return (AccessControlHome)PortableRemoteObject.narrow(homeObject, AccessControlHome.class);
    }

    /**
     * Gets a home interface for the ReportAccess bean.
     * @return ReportAccessHome
     */
    public static ReportAccessHome getReportAccessHome() throws NamingException {
        Object homeObject = lookupHome(ReportAccessHome.JNDI_NAME);
        return (ReportAccessHome)PortableRemoteObject.narrow(homeObject, ReportAccessHome.class);
    }

    /**
     * Gets a home interface for the BaseReport bean.
     * @return BaseReportHome
     */
    public static BaseReportHome getBaseReportHome() throws NamingException {
        Object homeObject = lookupHome(BaseReportHome.JNDI_NAME);
        return (BaseReportHome)PortableRemoteObject.narrow(homeObject, BaseReportHome.class);
    }

    /**
     * Gets a home interface for the SystemAddress bean.
     * @return SystemAddressHome
     */
    public static SystemAddressHome getSystemAddressHome() throws NamingException {
        Object homeObject = lookupHome(SystemAddressHome.JNDI_NAME);
        return (SystemAddressHome)PortableRemoteObject.narrow(homeObject, SystemAddressHome.class);
    }

    /**
     * Gets a home interface for the ProcessReturnedMail bean.
     * @return ProcessReturnedMailHome
     */
    public static ProcessReturnedMailHome getProcessReturnedMailHome() throws NamingException {
        Object homeObject = lookupHome(ProcessReturnedMailHome.JNDI_NAME);
        return (ProcessReturnedMailHome)PortableRemoteObject.narrow(homeObject, ProcessReturnedMailHome.class);
    }

    /** Gets a home interface for the FileQueue bean.
     * @return FileQueueHome
     */
    public static FileQueueHome getFileQueueHome() throws NamingException {
        Object homeObject = lookupHome(FileQueueHome.JNDI_NAME);
        return (FileQueueHome)PortableRemoteObject.narrow(homeObject, FileQueueHome.class);
    }

    /** Gets a home interface for the Update bean.
     * @return UpdateHome
     */
    public static UpdateHome getUpdateHome() throws NamingException {
        Object homeObject = lookupHome(UpdateHome.JNDI_NAME);
        return (UpdateHome)PortableRemoteObject.narrow(homeObject, UpdateHome.class);
    }

    /** Gets a home interface for the MaintainMembers bean.
     * @return MaintainMembersHome
     */
    public static MaintainMembersHome getMaintainMembersHome() throws NamingException {
        Object homeObject = lookupHome(MaintainMembersHome.JNDI_NAME);
        return (MaintainMembersHome)PortableRemoteObject.narrow(homeObject, MaintainMembersHome.class);
    }

    /** Gets a home interface for the MaintainPersons bean.
     * @return MaintainPersonsHome
     */
    public static MaintainPersonsHome getMaintainPersonsHome() throws NamingException {
        Object homeObject = lookupHome(MaintainPersonsHome.JNDI_NAME);
        return (MaintainPersonsHome)PortableRemoteObject.narrow(homeObject, MaintainPersonsHome.class);
    }

    /** Gets a home interface for the MaintainAffiliateStaff bean.
     * @return MaintainAffiliateStaffHome
     */
    public static MaintainAffiliateStaffHome getMaintainAffiliateStaffHome() throws NamingException {
        Object homeObject = lookupHome(MaintainAffiliateStaffHome.JNDI_NAME);
        return (MaintainAffiliateStaffHome)PortableRemoteObject.narrow(homeObject, MaintainAffiliateStaffHome.class);
    }

    /**
     * Gets a home interface for the MaintainOrgMailingLists bean.
     * @return MaintainOrgMailingListsHome
     */
    public static MaintainOrgMailingListsHome getMaintainOrgMailingListsHome() throws NamingException {
        Object homeObject = lookupHome(MaintainOrgMailingListsHome.JNDI_NAME);
        return (MaintainOrgMailingListsHome)PortableRemoteObject.narrow(homeObject, MaintainOrgMailingListsHome.class);
    }

    /**
     * Gets a home interface for the MaintainPersonMailingLists bean.
     * @return MaintainPersonMailingListsHome
     */
    public static MaintainPersonMailingListsHome getMaintainPersonMailingListsHome() throws NamingException {
        Object homeObject = lookupHome(MaintainPersonMailingListsHome.JNDI_NAME);
        return (MaintainPersonMailingListsHome)PortableRemoteObject.narrow(homeObject, MaintainPersonMailingListsHome.class);
    }

    /**
	 * Gets a home interface for the MaintainMyInfo bean.
	 * @return MaintainMyInfoHome
	 */
	public static MaintainMyInfoHome getMaintainMyInfoHome() throws NamingException {
	  	Object homeObject = lookupHome(MaintainMyInfoHome.JNDI_NAME);
	    return (MaintainMyInfoHome)PortableRemoteObject.narrow(homeObject, MaintainMyInfoHome.class);
    }

    /**
     * Gets a home interface for the MaintainPoliticalRebate bean.
     * @return MaintainPoliticalRebateHome
     */
    public static MaintainPoliticalRebateHome getMaintainPoliticalRebateHome() throws NamingException {
        Object homeObject = lookupHome(MaintainPoliticalRebateHome.JNDI_NAME);
        return (MaintainPoliticalRebateHome)PortableRemoteObject.narrow(homeObject, MaintainPoliticalRebateHome.class);
    }

    /**
     * Gets a home interface for the Cards bean.
     * @return CardsHome
     */
    public static CardsHome getCardsHome() throws NamingException {
        Object homeObject = lookupHome(CardsHome.JNDI_NAME);
        return (CardsHome)PortableRemoteObject.narrow(homeObject, CardsHome.class);
    }

    /**
     * Gets a home interface for the MaintainAffiliates bean.
     * @return MassChangeHome
     */
    public static MassChangeHome getMassChangeHome() throws NamingException {
        Object homeObject = lookupHome(MassChangeHome.JNDI_NAME);
        return (MassChangeHome)PortableRemoteObject.narrow(homeObject, MassChangeHome.class);
    }

    /**
     * Sets an value in the JNDI store.
     * The name may be compound, i.e, of the form a/b/c.  But, the sub names (in this case a and a/b) must either
     * be unbound, or bound to Contexts.
     */
    public static void setObject(String name, Object object) throws NamingException {
        StringTokenizer tok = new StringTokenizer(STORE_BASE + "/" + name, "/");
        Context context = getClusterWideInitialContext();
        while (tok.hasMoreTokens()) {
            String nameElement = tok.nextToken();
            if (tok.hasMoreTokens()) {
                try {
                    context = (Context)context.lookup(nameElement);
                } catch (NameNotFoundException e) {
                    context = context.createSubcontext(nameElement);
                }
            }
            else
                context.rebind(nameElement, object);
        }
    }

    /**
     * Gets a value from the JNDI store
     *
     * @return the bound object, or null if it did not exist.
     */
    public static Object getObject(String name) throws NamingException {

        Object value;

        try {
            value = lookupClusterWide(STORE_BASE + "/" + name);
        } catch (NameNotFoundException e) {
            return null;
        } catch (NotContextException e) {
            return null;
        }

        return value;
    }

    /**
     * Removes an object from the cluster wide JNDI store
     *
     */
    public static void unset(String name) throws NamingException {

        try {
            getClusterWideInitialContext().unbind(STORE_BASE + "/" + name);
        } catch (NameNotFoundException e) {
            //this is ok.
        }
    }



    /**
     * Gets a String value from the JNDI store
     * The type of the object must be java.lang.String or
     * a ClassCastException will be thrown.
     *
     * If the String is not bound, null is returned.
     */
    public static String getString(String name) throws NamingException {
        return (String)getObject(name);
    }

    /**
     * Gets an int value from the JNDI store
     * The type of the object must be java.lang.Integer or
     * a ClassCastException will be thrown.
     *
     * If the value is not bound, 0 is returned.
     */
    public static int getInt(String name) throws NamingException {
        Integer value = (Integer)getObject(name);
        if (value == null)
            return 0;
        else
            return value.intValue();
    }

    /**
     * Prints all of JNDI to the given PrintStream.
     */
    public static void printAll(PrintStream ps) {
        try {
            printAll(getInitialContext(), "", ps);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints all of JNDI from the given context to the given PrintStream.
     */
    public static void printAll(String context, String indent, PrintStream ps) {
        try {
            printAll((Context)lookup(context), indent, ps);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints all of JNDI from the given context to the given print stream.
     */
    private static void printAll(Context context, String indent, PrintStream ps) throws NamingException {
        NamingEnumeration enum = context.listBindings("");
        while (enum.hasMore()) {
            Binding b = (Binding)enum.next();
            ps.println(indent + b.getName());
            if (b.getObject() instanceof Context)
                printAll((Context)b.getObject(), indent + "    ", ps);
            else if (b.getObject() instanceof Reference)
                ps.println("   Reference to " + ((Reference)b.getObject()).getClassName());
            else
                ps.println("   " + indent + b.getObject().getClass().getName());
        }
        enum.close();
    }

    private static Object lookupClusterWide(String name) throws NamingException {
        return getClusterWideInitialContext().lookup(name);
    }
    
    private static Object lookup(String name) throws NamingException {
        return getInitialContext().lookup(name);
    }

    private static Object lookupHome(String name) throws NamingException {
        return lookup("local/" + name);
    }
}
