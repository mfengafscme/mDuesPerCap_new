<%--
This page currently only contains links to Admin functions.  Membership actions
will be added as they are implemented.
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Enterprise Application Main Menu", help = "MainMenu.html";%>
<%@ include file="../include/header.inc" %>
<% 
    String proc = (String)session.getAttribute("ncoaProcess");
    if (proc != null)
    {
        if (proc.equals("Success"))
        {
            session.removeAttribute("ncoaProcess");
            out.println("<script language='JavaScript'>");
            out.println("alert('The NCOA Process has been scheduled.');");
            out.println("</script>");
        }else
        {
            out.println("<script language='JavaScript'>");
            out.println("alert('The NCOA Process has been failed.');");
            out.println("</script>");
            session.removeAttribute("ncoaProcess");
        }
    }
%>
<table cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="65%" align="center">
    <tr valign="top">
        <th>Search</th>
        <th>Reports</th>
    </tr>
    <tr valign="top">
        <td>
            <ul>
                <afscme:link page="/viewBasicPersonCriteria.action?new" title="Search Person" prefix="<li>" postfix="">Person</afscme:link>
                <afscme:link page="/verifyPerson.action?back=PersonAdd" title="Add New Person" prefix="&nbsp;&nbsp;<B style='color:#006633'>&gt;&gt;</B>&nbsp;&nbsp;" postfix="</li>">Add New Person</afscme:link>
		    <afscme:link page="/viewBasicMemberCriteria.action?new" title="Search Members" prefix="<li>" postfix="">Members</afscme:link>
                <afscme:link page="/verifyMember.action?new" title="Add New Member" prefix="&nbsp;&nbsp;<B style='color:#006633'>&gt;&gt;</B>&nbsp;&nbsp" postfix="</li>">Add New Member</afscme:link>
        	    <afscme:link forward="SearchOrganizations" title="Search for Organizations" prefix="<li>" postfix="">Organizations</afscme:link>
                <afscme:link forward="VerifyOrganization" title="Add New Organization" prefix="&nbsp;&nbsp;<B style='color:#006633'>&gt;&gt;</B>&nbsp;&nbsp;" postfix="</li>">Add New Organization</afscme:link>
                <afscme:link page="/viewBasicAffiliateCriteria.action?new" title="Search for Affiliates" prefix="<li>" postfix="">Affiliates</afscme:link>
                <afscme:link page="/addAffiliateDetail.action" title="Add New Affiliate" prefix="&nbsp;&nbsp;<B style='color:#006633'>&gt;&gt;</B>&nbsp;&nbsp;" postfix="</li>">Add New Affiliate</afscme:link>
                <afscme:link page="/viewVendorMemberCriteria.action" title="Search Vendor Member" prefix="<li>" postfix="</li>">Vendor Member Search</afscme:link>
            </ul>
        </td>
        <td>
            <ul>
                <afscme:link page="/listQueries.action" title="Maintain Queries" prefix="<li>" postfix="</li>">Maintain Queries</afscme:link>
                <afscme:link page="/listRegularReports.action" title="Generate Reports" prefix="<li>" postfix="</li>">Generate Reports</afscme:link>
                <afscme:link page="/listMailingReports.action" title="Generate Mailing Lists" prefix="<li>" postfix="</li>">Generate Mailing Lists</afscme:link>
            </ul>
        </td>
    </tr>
    <tr valign="top">
        <th>Import/Export</th>
        <th>Maintenance</th>
    </tr>
    <tr valign="top">
        <td>
           <ul>
                <afscme:link page="/processReturnedMail.action" title="Process Returned Mail" prefix="<li>" postfix="</li>">Process Returned Mail</afscme:link>
                <afscme:link page="/viewUploadAffiliateMember.action" title="Upload Affiliate Member Data" prefix="<li>" postfix="</li>">Upload Affiliate Member Data</afscme:link>
                <afscme:link page="/viewUploadAffiliateOfficer2.action" title="Upload Affiliate Officer Data" prefix="<li>" postfix="</li>">Upload Affiliate Officer Data</afscme:link>
                <afscme:link page="/viewUploadAffiliateRebate.action" title="Upload Affiliate Rebate Data" prefix="<li>" postfix="</li>">Upload Affiliate Rebate Data</afscme:link>
                <afscme:link page="/viewUploadParticipation.action" title="Upload Participation Data" prefix="<li>" postfix="</li>">Upload Participation Data</afscme:link>
           </ul>
        </td>
        <td>
           <ul>
                <afscme:link page="/viewApplyUpdateQueue.action" title="Apply Update" prefix="<li>" postfix="</li>">Apply Update</afscme:link>
                <afscme:link page="/view12MonthRebateAmount.action" title="Maintain 12-Month Rebate Amount" prefix="<li>" postfix="</li>">Maintain 12-Month Rebate Amount</afscme:link>
                <afscme:link forward="ViewParticipationGroupMaintenance" title="Maintain Participation Groups" prefix="<li>" postfix="</li>">Maintain Participation Groups</afscme:link>
                <afscme:link page="/initiateNcoaProcessing.action" title="Initiate NCOA Processing" prefix="<li>" postfix="</li>">Initiate NCOA Processing</afscme:link>
           </ul>
        </td>
    </tr>     
    <tr valign="top">
        <th>Administration</th>
        <th>My Account</th>
    </tr>
    <tr valign="top">
        <td>
            <ul>
                <afscme:link forward="ListCodeTypes" title="Maintain Codes" prefix="<li>" postfix="</li>">Maintain Codes</afscme:link>
                <afscme:link forward="ListRoles" title="Maintain Privileges" prefix="<li>" postfix="</li>">Maintain Privileges</afscme:link>
                <afscme:link page="/selectAffiliateSearch.action?new=&cancel=showMain" prefix="<li>" postfix="</li>">Affiliate Data Utility</afscme:link>
            </ul>
        </td>
        <td>
            <ul>
                <afscme:link forward="ViewPersonalInformation" prefix="<li>" postfix="</li>">View My Personal Information</afscme:link>
                <afscme:link forward="EditAccountInfo" prefix="<li>" postfix="</li>">Change My Password</afscme:link>
            </ul>
        </td>
    </tr>   
</table>

<%@ include file="../include/footer.inc" %>
