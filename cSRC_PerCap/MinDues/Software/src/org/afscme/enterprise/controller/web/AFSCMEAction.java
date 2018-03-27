package org.afscme.enterprise.controller.web;

//java
import java.io.*;
import java.util.*;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;

//apache
import org.apache.log4j.Logger;
import org.apache.struts.action.*;

//afscme
import org.afscme.enterprise.util.*;
import org.afscme.enterprise.address.ejb.SystemAddress;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.staff.ejb.MaintainAffiliateStaff;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficers;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.controller.ActionPrivileges;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.ejb.AccessControl;
import org.afscme.enterprise.log.SystemLog;
import org.afscme.enterprise.masschange.ejb.MassChange;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.organization.ejb.MaintainOrgLocations;
import org.afscme.enterprise.organization.ejb.MaintainOrgMailingLists;
import org.afscme.enterprise.organization.ejb.MaintainOrganizations;
import org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroups;
import org.afscme.enterprise.person.ejb.MaintainPersonMailingLists;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.reporting.base.ejb.BaseReport;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.roles.ejb.MaintainPrivileges;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import org.afscme.enterprise.myinfo.ejb.MaintainMyInfo;
import org.afscme.enterprise.affiliate.AffiliateCriteria;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.web.AffiliateFinderForm;
import org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebate;
import org.afscme.enterprise.update.ejb.*;
import org.afscme.enterprise.update.web.AffiliateFileUploadForm;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;

/**
 * Base class for all actions in the system, save some of the framework actions.  Provides
 * access control and auditing.
 */
public abstract class AFSCMEAction extends Action {
    /** Session key of the UserSecurityData object */
    /*package*/ static final String SESSION_USER_SECURITY_DATA = "SESSION_USER_SECURITY_DATA";

    /** Session key of the UserSecurityData object */
    private static final String SESSION_CURRENT_PERSON_PK = "SESSION_CURRENT_PERSON_PK";

    private static final String SESSION_CURRENT_PERSON_NAME = "SESSION_CURRENT_PERSON_NAME";

    /** Session keys for Affiliate information */
    private static final String SESSION_CURRENT_AFF_PK = "SESSION_CURRENT_AFF_PK";
    private static final String SESSION_CURRENT_AFF = "SESSION_CURRENT_AFF";
    private static final String SESSION_CURRENT_AFF_ID = "SESSION_CURRENT_AFF_ID";

    /** Session keys for Organization information */
    private static final String SESSION_CURRENT_ORG_PK = "SESSION_CURRENT_ORG_PK";
    private static final String SESSION_CURRENT_ORG_NAME = "SESSION_CURRENT_ORG_NAME";

    /** Session key for flow of which the task originate from */
    protected static final String SESSION_CURRENT_FLOW  = "SESSION_CURRENT_FLOW";

    /** Suffix of all action  urls - '.action'  - Used when checking URL's for access */
    private static final String ACTION_SUFFIX = ".action"; //intentional package scope

    /** Logger for this class */
    protected static Logger log = Logger.getLogger(AFSCMEAction.class);

    /** true iff the EJB references have been loaded */
    protected static boolean s_initialized;

    // references to stateless session EJBs for use in this and derived classes
    protected static MaintainPrivileges s_maintainPrivileges;
    protected static MaintainUsers s_maintainUsers;
    protected static AccessControl s_accessControl;
    protected static MaintainCodes s_maintainCodes;
    protected static MaintainAffiliates s_maintainAffiliates;
    protected static MaintainAffiliateOfficers s_maintainAffiliateOfficers;
    protected static MaintainAffiliateStaff s_maintainAffiliateStaff;
    protected static MaintainOrganizations s_maintainOrganizations;
    protected static MaintainOrgLocations s_maintainOrgLocations;
    protected static MaintainParticipationGroups s_maintainParticipationGroups;
    protected static BaseReport s_baseReport;
    protected static ReportAccess s_reportAccess;
    protected static SystemAddress s_systemAddress;
    protected static MaintainPersons s_maintainPersons;
    protected static MaintainMembers s_maintainMembers;
    protected static MaintainOrgMailingLists s_maintainOrgMailingLists;
    protected static MaintainPersonMailingLists s_maintainPersonMailingLists;
    protected static MaintainPoliticalRebate s_maintainPoliticalRebate;
    protected static MaintainMyInfo s_maintainMyInfo;
    protected static MassChange s_massChange;
    protected static FileQueue s_fileQueue;
    protected static Update s_update;


    /**
     * Performes security checking on the action, the forwards it to the perform() method in the derived class
     */
    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        UserSecurityData usd = null;

        if (!s_initialized) {
            init();
        }

        //log the request
        if (log.isDebugEnabled()) {
            StringBuffer buf = new StringBuffer();
            buf.append("Action Invoked\n");
            buf.append("Class: " + this.getClass().getName() + "\n");
            buf.append("Input: " + mapping.getInput() + "\n");
            buf.append(toString(request));
            log.debug(buf.toString());
        }

        //derived class determines if login is required.  (It should aloways be requried, except for the login and request password actions)
        if (isLoginRequired()) {

            //Get the user's security info.
            //If they are not logged in, bounce them to the login page
            HttpSession session = request.getSession(false);
            if (session == null)
                return mapping.findForward("ShowLogin");
            usd = (UserSecurityData)session.getAttribute(SESSION_USER_SECURITY_DATA);
            if (usd == null)
                return mapping.findForward("ShowLogin");

            //Check if the user has access to the requested action
            String action = getActionPart(request.getRequestURI());
            ActionPrivileges ap = ConfigUtil.getActionPrivileges();
            if (!ap.isActionAllowed(action, usd))
                return mapping.findForward("ErrorNoAccess");

            //Check if the user is in 'force change password' mode.
            //If so, bounce them to the 'change password' page.
            if (usd.isForceChangePassword() && !action.equals("editAccountInfo"))
                return mapping.findForward("ForceChangePassword");
        }

        //Perform the requested action (implemented in the derived class)
        ActionForward forward = null;
        try {
            forward = perform(mapping, form, request, response, usd);
        } catch (Exception e) {
            SystemLog.logApplicationError(e, request.getRequestURI(), usd != null ? usd.getUserId() : null);
            throw new ServletException(e);
        }

        //log the resulting action-forward
        if (log.isDebugEnabled()) {
            String name = null;
            if (forward != null) {
                name = forward.getName();
                if (name == null) {
                    name = forward.getPath();
                }
            }
            log.debug("Action Forwarded to " + name);
        }

        return forward;
    }

    /**
     * ThisMakes an action forward that goes back to the input page, with 1 error
     * @param request the HttpRequest that was passed to perform()
     * @param mapping the ActionMapping that was passed to perform()
     * @param field the field the error is associated with
     * @param error the message id of the error
     */
    protected ActionForward makeErrorForward(HttpServletRequest request, ActionMapping mapping, String field, String error) {
        ActionErrors actionErrors = new ActionErrors();
        actionErrors.add(field, new ActionError(error));
        saveErrors(request, actionErrors);
        return new ActionForward(mapping.getInput());
    }

    /**
     * ThisMakes an action forward that goes back to the input page, with 1 error
     * @param request the HttpRequest that was passed to perform()
     * @param mapping the ActionMapping that was passed to perform()
     * @param error the message id of the error
     */
    protected ActionForward makeLoginErrorForward(HttpServletRequest request, ActionMapping mapping, String error) {
        ActionErrors actionErrors = new ActionErrors();

        actionErrors.add("useIdOrPasswdError", new ActionMessage(error));
        saveErrors(request, actionErrors);

         return new ActionForward(mapping.getInput());
    }

    /**
     * ThisMakes an action forward that goes back to the input page, with a collection of errors
     * @param request the HttpRequest that was passed to perform()
     * @param mapping the ActionMapping that was passed to perform()
     * @param field Collection of field name strings
     * @param error Collection of error key strings
     */
    protected ActionForward makeErrorForward(HttpServletRequest request, ActionMapping mapping, Collection fields, Collection errors) {
        ActionErrors actionErrors = new ActionErrors();
        Iterator fit = fields.iterator();
        Iterator eit = errors.iterator();
        while (fit.hasNext())
            actionErrors.add((String)fit.next(), new ActionError((String)eit.next()));
        saveErrors(request, actionErrors);
        return new ActionForward(mapping.getInput());
    }

    /**
     * Retrieves the action name from the given URL path component.
     * example:
     *
     * path = "/editMember.action&id=3"
     * return value  = "editMember"
     *
     * This is intentionally given package scope, so the privilege tags can use it.
     */
    /*package*/ static String getActionPart(String path) throws ServletException {

        if (!path.startsWith("/"))
            throw new ServletException("Malformed action '" + path + "'.  Must begin with /");

        int endindex;
        if (!path.endsWith(ACTION_SUFFIX)) {
            int qMarkIndex = path.indexOf("?");
            if (qMarkIndex == -1)
                throw new ServletException("Malformed action '" + path + "'");
            endindex = qMarkIndex - ACTION_SUFFIX.length();
        }
        else
            endindex = path.length() - ACTION_SUFFIX.length();

        return path.substring(1, endindex);
    }

    /** Returns true iff this action requires login.
     * The default implementation here always returns true, this can be overridden by derived classes
     */
    protected boolean isLoginRequired() {
        return true;
    }

    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public abstract ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception;

    /**
     * Retrieves static references to the stateless session EJBs, for use
     * by the derived classes
     */
    private static synchronized void init() throws ServletException {

        //recheck the initialization variable
        if (s_initialized)
            return;

        s_initialized = true;

        try {
            s_maintainUsers = JNDIUtil.getMaintainUsersHome().create();
            s_maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();
            s_accessControl = JNDIUtil.getAccessControlHome().create();
            s_maintainCodes = JNDIUtil.getMaintainCodesHome().create();
            s_maintainAffiliates = JNDIUtil.getMaintainAffiliatesHome().create();
            s_maintainAffiliateOfficers = JNDIUtil.getMaintainAffiliateOfficersHome().create();
            s_maintainAffiliateStaff = JNDIUtil.getMaintainAffiliateStaffHome().create();
            s_maintainOrganizations = JNDIUtil.getMaintainOrganizationsHome().create();
            s_maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();
            s_maintainParticipationGroups = JNDIUtil.getMaintainParticipationGroupsHome().create();
            s_baseReport = JNDIUtil.getBaseReportHome().create();
            s_reportAccess = JNDIUtil.getReportAccessHome().create();
            s_systemAddress = JNDIUtil.getSystemAddressHome().create();
            s_maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
            s_maintainMembers = JNDIUtil.getMaintainMembersHome().create();
            s_maintainPersonMailingLists = JNDIUtil.getMaintainPersonMailingListsHome().create();
            s_maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
            s_maintainPoliticalRebate = JNDIUtil.getMaintainPoliticalRebateHome().create();
            s_maintainMyInfo = JNDIUtil.getMaintainMyInfoHome().create();
            s_massChange = JNDIUtil.getMassChangeHome().create();
            s_fileQueue = JNDIUtil.getFileQueueHome().create();
            s_update = JNDIUtil.getUpdateHome().create();
        } catch (NamingException e) {
            throw new ServletException("Unable to find dependent EJBs in AFSCMEAction.init()", e);
        } catch (CreateException e) {
            throw new ServletException("Unable to find dependent EJBs in AFSCMEAction.init()", e);
        }
    }

    /** Returns a string representation of HttpServletRequest */
    public static String toString(HttpServletRequest request) {
        StringBuffer buf = new StringBuffer();
        buf.append("\nParameters:\n");
        Enumeration enum = request.getParameterNames();
        while (enum.hasMoreElements()) {
            String key = (String)enum.nextElement();
            String value = TextUtil.toString(request.getParameter(key));

            buf.append("\t" + key + "=" + value + "\n");
        }
        buf.append("Attributes:\n");
        enum = request.getAttributeNames();
        while (enum.hasMoreElements()) {
            String key = (String)enum.nextElement();
            String value = TextUtil.toString(request.getAttribute(key));

            buf.append("\t" + key + "=" + value + "\n");
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            buf.append("Session:\n");
            enum = session.getAttributeNames();
            while (enum.hasMoreElements()) {
                String key = (String)enum.nextElement();
                String value = TextUtil.toString(session.getAttribute(key));
                buf.append("\t" + key + "=" + value + "\n");
            }
        }
        return buf.toString();
    }

    public static String toString(Throwable t) {
        while (t instanceof ServletException && ((ServletException)t).getRootCause() != null)
            t = ((ServletException)t).getRootCause();
        while (t instanceof EJBException && ((EJBException)t).getCausedByException() != null)
            t = ((EJBException)t).getCausedByException();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        t.printStackTrace(pw);
        pw.close();
        return baos.toString();
    }

    /**
     * Sets the current person being edited.  This value is stored in the session.
     *
     * @param request The current request
     * @param personPk The primary key of the person being edited
     */
    protected static void setCurrentPerson(HttpServletRequest request, Integer personPk) {
        HttpSession session = request.getSession(true);
        if (personPk == null || personPk.intValue() == 0) {
            session.removeAttribute(SESSION_CURRENT_PERSON_PK);
            session.removeAttribute(SESSION_CURRENT_PERSON_NAME);
        } else {
            Integer currentPersonPk = getCurrentPersonPk(request);
            if (currentPersonPk == null || !currentPersonPk.equals(personPk)) {
                session.setAttribute(SESSION_CURRENT_PERSON_PK, personPk);
                String name = s_maintainUsers.getPersonName(personPk);
                session.setAttribute(SESSION_CURRENT_PERSON_NAME, name);
            }
        }
    }
    /**
     * Sets the current person name being edited for cases
     * where a name changes but the same person is still the current person
     *
     * @param request The current request
     * @param name The new name of the person being edited
     */
    protected static void setCurrentPersonName(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_CURRENT_PERSON_NAME, name);
    }

    /**
     * Gets the name of the current person being edited.
     *
     * @param session The session to retreive the value from
     */
    public static String getCurrentPersonName(HttpSession session) {
        return (String)session.getAttribute(SESSION_CURRENT_PERSON_NAME);
    }

    /**
     * Gets the name of the current person being edited.
     *
     * @param request The current request
     */
    protected static String getCurrentPersonName(HttpServletRequest request) {
        return getCurrentPersonName(request.getSession(true));
    }

    /**
     * Gets the pk of the current person being edited.
     *
     * @param request The current request
     */
    public static Integer getCurrentPersonPk(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return null;
        else
            return getCurrentPersonPk(session);
    }

    /**
     * Gets the pk of the current person being edited.
     *
     * @param session The current session
     */
    public static Integer getCurrentPersonPk(HttpSession session) {
        return (Integer)session.getAttribute(SESSION_CURRENT_PERSON_PK);
    }

    /**
     * Gets the pk of the current person being edited.
     * The pk is first looked for in the reqest.
     * If it's there, it's palced in the session,
     * otherwise it is retrieved from the session.
     *
     * @param request The current request
     * @param parameter The name of the parameter to look for the person pk in
     */
    protected static Integer getCurrentPersonPk(HttpServletRequest request, String parameter) {
        if (!TextUtil.isEmpty(request.getParameter(parameter)))
            setCurrentPerson(request, Integer.valueOf(request.getParameter(parameter)));
        return getCurrentPersonPk(request);
    }

    public static Integer getCurrentAffiliatePk(HttpServletRequest request) {
        return (Integer)request.getSession(true).getAttribute(SESSION_CURRENT_AFF_PK);
    }

    protected static Integer getCurrentAffiliatePk(HttpServletRequest request, String parameter) {
        if (!TextUtil.isEmpty(request.getParameter(parameter))) {
            AffiliateData data = s_maintainAffiliates.getAffiliateData(Integer.valueOf(request.getParameter(parameter)));
            setCurrentAffiliate(request, data);
        }
        return getCurrentAffiliatePk(request);
    }

    protected static void setCurrentAffiliatePk(HttpSession session, Integer pk) {
        session.setAttribute(SESSION_CURRENT_AFF_PK, pk);
        session.removeAttribute(SESSION_CURRENT_AFF);
    }

    protected static void setCurrentAffiliatePk(HttpServletRequest request, Integer pk) {
        setCurrentAffiliatePk(request.getSession(true), pk);
    }

    protected static void setCurrentAffiliatePk(HttpSession session, String pk) {
        session.setAttribute(SESSION_CURRENT_AFF_PK, pk);
        session.removeAttribute(SESSION_CURRENT_AFF);
    }

    protected static void setCurrentAffiliatePk(HttpServletRequest request, String pk) {
        setCurrentAffiliatePk(request.getSession(true), pk);
    }

    protected static void setCurrentAffiliate(HttpServletRequest request, Integer pk) {
        setCurrentAffiliate(request, s_maintainAffiliates.getAffiliateData(pk));
    }

    protected static void setCurrentAffiliate(HttpServletRequest request, AffiliateData data) {
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_CURRENT_AFF_ID , data.getAffiliateId());
        if (data == null || data.getAffPk() == null) {
            session.removeAttribute(SESSION_CURRENT_AFF_PK);
            session.removeAttribute(SESSION_CURRENT_AFF);
        } else {
            DelimitedStringBuffer sb = new DelimitedStringBuffer(" - ");
            setCurrentAffiliatePk(request, data.getAffPk());
            AffiliateData parent = null;
            sb.append(data.getAffiliateId().getState());
            switch (data.getAffiliateId().getType().charValue()) {
                case 'L':
                case 'S':
                    // append Local's Name
                    sb.append(data.getAbbreviatedName());
                    // append the Council's Name if exists
                    if (data.getParentFk() != null && data.getParentFk().intValue() > 0) {
                        parent = s_maintainAffiliates.getAffiliateData(data.getParentFk());
                        sb.append(parent.getAbbreviatedName());
                    }
                    break;
                case 'U':
                    // append Local's Name
                    if (data.getParentFk() != null && data.getParentFk().intValue() > 0) {
                        parent = s_maintainAffiliates.getAffiliateData(data.getParentFk());
                        sb.append(parent.getAbbreviatedName());
                    }
                    // append Sub Local's Name
                    sb.append(data.getAbbreviatedName());
                    // append the Council's Name if exists
                    if ((data.getParentFk() != null && data.getParentFk().intValue() > 0)  && (parent.getParentFk() != null)) {
                        parent = s_maintainAffiliates.getAffiliateData(parent.getParentFk());
                        sb.append(parent.getAbbreviatedName());
                    }
                    break;
                case 'C':
                case 'R':
                    // append the Council's Name
                    sb.append(data.getAbbreviatedName());
                    break;
                default:
                    // should never happen...
                    break;
            }
            session.setAttribute(SESSION_CURRENT_AFF, sb.toString().trim());
        }
    }

    public static String getCurrentAffiliate(HttpSession session) {
        return (String)session.getAttribute(SESSION_CURRENT_AFF);
    }


    protected static String getCurrentAffiliate(HttpServletRequest request) {
        return getCurrentAffiliate(request.getSession(true));
    }

     protected static AffiliateIdentifier getCurrentAffiliateId(HttpServletRequest request) {
        return getCurrentAffiliateId(request.getSession(true));
    }

    public static AffiliateIdentifier getCurrentAffiliateId(HttpSession session) {
        return (AffiliateIdentifier) session.getAttribute(SESSION_CURRENT_AFF_ID);
    }

    // ------------------------------------
    //  ORGANIZATION CURRENT METHODS
    // ------------------------------------

    /**
     * Sets the current organization being edited.  This value is stored in the session.
     *
     * @param request The current request
     * @param orgPk The primary key of the organization being edited
     */
    protected static void setCurrentOrganization(HttpServletRequest request, Integer orgPk) {
        HttpSession session = request.getSession(true);
        if (orgPk == null) {
            session.removeAttribute(SESSION_CURRENT_ORG_PK);
            session.removeAttribute(SESSION_CURRENT_ORG_NAME);
        } else {
            Integer currentOrgPk = getCurrentOrganizationPk(request);
            if (currentOrgPk == null || !currentOrgPk.equals(orgPk)) {
                session.setAttribute(SESSION_CURRENT_ORG_PK, orgPk);
                String name = s_maintainOrganizations.getOrganizationName(orgPk);
                session.setAttribute(SESSION_CURRENT_ORG_NAME, name);
            }
        }
    }

    /**
     * Gets the name of the current organization being edited.
     *
     * @param session The session to retreive the value from
     */
    public static String getCurrentOrganizationName(HttpSession session) {
        return (String)session.getAttribute(SESSION_CURRENT_ORG_NAME);
    }

    /**
     * Gets the name of the current organization being edited.
     *
     * @param request The current request
     */
    protected static String getCurrentOrganizationName(HttpServletRequest request) {
        return getCurrentOrganizationName(request.getSession(true));
    }

    /**
     * Gets the pk of the current organization being edited.
     *
     * @param request The current request
     */
    protected static Integer getCurrentOrganizationPk(HttpServletRequest request) {
        return (Integer)request.getSession(true).getAttribute(SESSION_CURRENT_ORG_PK);
    }

    /**
     * Gets the pk of the current organization being edited.
     * The pk is first looked for in the request.
     * If it's there, it's placed in the session,
     * otherwise it is retrieved from the session.
     *
     * @param request The current request
     * @param parameter The name of the parameter to look for the organization pk in
     */
    protected static Integer getCurrentOrganizationPk(HttpServletRequest request, String parameter) {
        if (!TextUtil.isEmpty(request.getParameter(parameter)))
            setCurrentOrganization(request, Integer.valueOf(request.getParameter(parameter)));
        return getCurrentOrganizationPk(request);
    }
    // ------------------------------------
    //  END OF ORGANIZATION CURRENT METHODS
    // ------------------------------------

    /**
     * Builds and sets an AffiliateIdentifierForm in the session which is needed
     * before forwarding to the searchAffiliateIdentifier action.
     *
     * @param request
     * @param affiliateIdentifierCode
     * @param affiliateIdentifierCouncil
     * @param affiliateIdentifierLocal
     * @param affiliateIdentifierState
     * @param affiliateIdentifierSubUnit
     * @param affiliateIdentifierType
     * @param linkAction    Needed for generating the links next to each
     *                      AffiliateResult in the form's list of results. This
     *                      is required. If value is null, nothing will be stored
     *                      in the session, and the resulting screen will not
     *                      display properly.
     * @param cancelAction  Needed for generating a cancel button. If this value
     *                      is null, no button will appear on the screen.
     */
    protected static void setCurrentAffiliateFinderForm(HttpServletRequest request,
                            Character affiliateIdentifierCode,
                            String affiliateIdentifierCouncil,
                            String affiliateIdentifierLocal,
                            String affiliateIdentifierState,
                            String affiliateIdentifierSubUnit,
                            Character affiliateIdentifierType,
                            String linkAction, String cancelAction
    ) {
        if (!TextUtil.isEmptyOrSpaces(linkAction)) {
            HttpSession session = request.getSession(true);
            AffiliateFinderForm aff = new AffiliateFinderForm();
            aff.setAffIdCode(affiliateIdentifierCode);
            aff.setAffIdCouncil(affiliateIdentifierCouncil);
            aff.setAffIdLocal(affiliateIdentifierLocal);
            aff.setAffIdState(affiliateIdentifierState);
            aff.setAffIdSubUnit(affiliateIdentifierSubUnit);
            aff.setAffIdType(affiliateIdentifierType);
            aff.setLinkAction(linkAction);
            aff.setCancelAction(cancelAction);
            session.setAttribute("affiliateFinderForm", aff);
        } else {
            log.debug("AffiliateFinderForm was not stored in the session.");
        }
    }

    /**
     * Helper method that performs a search for an Affiliate using an
     * AffiliateIdentifier as the search criteria.
     *
     * This is the same as building a AffiliateCriteria object and calling
     * searchAffiliates in the MaintainAffiliatesBean.
     *
     * @param affId     The AffiliateIdentifier to use as the search criteria.
     *
     * @return a Collection of AffiliateResult objects.
     */
    protected static Collection findAffiliatesWithID(AffiliateIdentifier affId) {
        if (affId == null) {
            return null;
        }
        return findAffiliatesWithID(affId.getCode(), affId.getCouncil(),
                                    affId.getLocal(), affId.getState(),
                                    affId.getSubUnit(), affId.getType()
        );
    }

    /**
     * Helper method that performs a search for an Affiliate using the
     * AffiliateIdentifier fields as the search criteria.
     *
     * This is the same as building a AffiliateCriteria object and calling
     * searchAffiliates in the MaintainAffiliatesBean.
     *
     * @param affIdCode     The AffiliateIdentifier's code field to search for.
     * @param affidCouncil  The AffiliateIdentifier's council field to search for.
     * @param affIdLocal    The AffiliateIdentifier's local field to search for.
     * @param affIdState    The AffiliateIdentifier's state field to search for.
     * @param affIdSubUnit  The AffiliateIdentifier's sub unit field to search for.
     * @param affIdType     The AffiliateIdentifier's type field to search for.
     *
     * @return a Collection of AffiliateResult objects.
     */
    protected static Collection findAffiliatesWithID(
                                        Character affIdCode, String affIdCouncil,
                                        String affIdLocal, String affIdState,
                                        String affIdSubUnit, Character affIdType
    ) {
        AffiliateCriteria criteria = new AffiliateCriteria();
        criteria.setAffiliateIdCode(affIdCode);
        criteria.setAffiliateIdCouncil(affIdCouncil);
        criteria.setAffiliateIdLocal(affIdLocal);
        criteria.setAffiliateIdState(affIdState);
        criteria.setAffiliateIdSubUnit(affIdSubUnit);
        criteria.setAffiliateIdType(affIdType);
        log.debug("Criteria = " + affIdCode + " **** " + affIdCouncil + " **** " + affIdLocal + " **** " + affIdState + " **** " + affIdSubUnit + " **** " +affIdType);
        return s_maintainAffiliates.searchAffiliates(criteria);

    }

   /**
     * Sets the current flow of how the user entered the application.
     * This value is stored in the session.
     *
     * @param request The current request
     * @param flow The origin where the user enters from
     */
    protected static void setCurrentFlow(HttpServletRequest request, String flow) {
        HttpSession session = request.getSession(true);
        if (flow == null) {
            session.removeAttribute(SESSION_CURRENT_FLOW);
        } else {
            String currentFlow = getCurrentFlow(request);
            if (currentFlow == null || !currentFlow.equalsIgnoreCase(flow)) {
                session.setAttribute(SESSION_CURRENT_FLOW, flow);
            }
        }
    }

    /**
     * Gets the current flow of how the user entered the application
     *
     * @param request The current request
     */
    public static String getCurrentFlow(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return null;
        else
            return getCurrentFlow(session);
    }

    /**
     * Gets the current flow of how the user entered the application
     *
     * @param session The current session
     */
    public static String getCurrentFlow(HttpSession session) {
        return (String)session.getAttribute(SESSION_CURRENT_FLOW);
    }

    public ActionForward processAffiliateFile(ActionMapping mapping,
                                     AffiliateFileUploadForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     UserSecurityData usd,
                                     String successForward, int fileType)
    throws IOException {
        log.debug("----------------------------------------------------");
        log.debug("processAffiliateFile called.");

        // get pk from form in case finder was used...
        Integer affPk = form.getAffPk();
        if (affPk == null || affPk.intValue() < 1) {
            // otherwise find it...
            affPk = s_maintainAffiliates.getAffiliatePk(form.getAffId());
        }

        if (affPk == null) {
            log.debug("affPk was null");
            return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.field.update.invalidAffiliateId");
        } else if (affPk.intValue() < 1 ) {
            log.debug("affPk was " + affPk);
            return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.codes.affiliate." + affPk.intValue());
        }
        log.debug("processing file...");
        // retrieve the file content
        byte[] content = form.getFile().getFileData();
        log.debug("storing file...");
        // call the file queue bean to start the background upload
        s_fileQueue.storeFile(content, affPk, form.getValidDate(), form.getUpdateType(), fileType, usd.getPersonPk());
        log.debug("successful...");
        return mapping.findForward(successForward);
    }
}
