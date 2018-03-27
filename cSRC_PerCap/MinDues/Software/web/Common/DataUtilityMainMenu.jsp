<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@ page import="org.afscme.enterprise.update.Codes.UpdateFileStatus, org.afscme.enterprise.update.Codes.UpdateFileType" %>
<%!String title = "Data Utility Main Menu", help = "DataUtilityMainMenu.html";%>
<%@ include file="../include/header.inc" %>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="65%" align="center">
    <tr valign="top">
        <th>Search</th>
        <th>Reports</th>
    </tr>
    <tr valign="top">
        <td>
            <ul>
              <afscme:link page="/viewAffiliateHierarchy.action" title="Affiliates" prefix="<li>" postfix="</li>">Affiliates</afscme:link>
              <afscme:link page="/viewBasicMemberCriteria.action?new" title="Search Members" prefix="<li>" postfix="">Members</afscme:link> 	
              <afscme:link page="/verifyMember.action?new" title="Add New Member" prefix="&nbsp;&nbsp;<B style='color:#006633'>&gt;&gt;</B>&nbsp;&nbsp" postfix="</li>">Add New Member</afscme:link>
            </ul>
        </td>
        <td>
            <ul>
              <afscme:link page="/listQueries.action" title="Maintain Queries" prefix="<li>" postfix="</li>">Maintain Queries</afscme:link>
              <afscme:link page="/listRegularReports.action" title="Generate Reports" prefix="<li>" postfix="</li>">Generate Reports</afscme:link>
              <afscme:link page="/listMailingReports.action" title="Generate Mailing Lists" prefix="<li>" postfix="</li>">Generate Mailing Lists</afscme:link>
              <afscme:link page="/viewEnrichedDataCriteria.action" title="Search Affiliates" prefix="<li>" postfix="</li>">Download Latest Update File With Enriched Data</afscme:link>
            </ul>
        </td>
    </tr>
    <tr >
        <th>Import/Export</th>
	<th>Maintenance</th>
    </tr>
    <tr valign="top">
        <td align="left">
            <ul>
                <afscme:link prefix="<li>" postfix="</li>" page="/viewUploadAffiliateMember.action" title="Upload Affiliate Member Data" >Upload Affiliate Member Data</afscme:link>
                <afscme:link prefix="<li>" postfix="</li>" page="/viewUploadAffiliateOfficer2.action" title="Upload Affiliate Officer Data" >Upload Affiliate Officer Data</afscme:link>
                <afscme:link prefix="<li>" postfix="</li>" page="/viewUploadAffiliateRebate.action" title="Upload Affiliate Rebate Data" >Upload Affiliate Rebate Data</afscme:link>
            </ul>
        </td>
        <td align="left">
            <ul>
                <%String url = "/viewApplyUpdateQueue.action?view=" + true; %>
                <afscme:link prefix="<li>" postfix="</li>" page="<%=url%>" title="View Apply Update">View Apply Update</afscme:link>
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
                <afscme:link forward="EditMemberPrivileges" prefix="<li>" postfix="</li>">Maintain Member Privileges</afscme:link>
                 <logic:notEqual parameter="noMain" value="noReturn">
                    <afscme:link forward="LeaveDataUtility" prefix="<li>" postfix="</li>">AFSCME Main Menu</afscme:link>
                </logic:notEqual>
                 <afscme:link forward="SwitchAffiliate" prefix="<li>" postfix="</li>">Switch Affiliate</afscme:link>
            </ul>
        </td>
        <td valign="Top">
            <ul>
            	<afscme:link forward="ViewPersonalInformation" prefix="<li>" postfix="</li>">View My Personal Information</afscme:link>
                <afscme:link forward="EditAccountInfo" prefix="<li>" postfix="</li>">Change My Password</afscme:link>
            </ul>
            
        </td>
    </tr>   
</table>


<%@ include file="../include/footer.inc" %>

