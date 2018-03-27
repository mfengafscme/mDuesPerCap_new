<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Organization Detail", help = "OrganizationDetail.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="organizationDetail" name="organizationDetailForm" type="org.afscme.enterprise.organization.web.OrganizationDetailForm"/>

<!-- Something for tabs. -->
<bean:define id="screen" value="OrganizationDetail"/>
<%@ include file="../include/organization_tab.inc" %>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <afscme:currentOrganizationName /><BR><BR> 
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td COLSPAN="2">
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th width="50%">Organization Name</th>
                    <th>Organization Type</th>
                </tr>
                <tr>
                    <td align="center"><bean:write name="organizationDetail" property="orgName"/></td>
                    <td align="center"><afscme:codeWrite name="organizationDetail" codeType="organizationType" property="orgType" format="{1}"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr VALIGN="top">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <td width="20%" CLASS="ContentHeaderTD">Organization Website</td>
                    <td class="ContentTD"><bean:write name="organizationDetail" property="orgWebSite"/></td>
                </tr>
                <tr>
                    <td CLASS="ContentHeaderTD">Marked for Deletion</td>
                    <td CLASS="ContentTD"><html:checkbox property="markedForDeletion" name="organizationDetail" disabled="true"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <th align="left" COLSPAN="2">
            <afscme:link page="/viewLocationInformation.action" paramId="orgPK" paramName="organizationDetail" paramProperty="orgPK" styleClass="largeTH" title="Maintain Locations">Maintain Location Information</afscme:link>
        </th>
    </tr>

<!-- Organization Primary Location -->
<logic:notEmpty name="organizationDetail" property="orgPrimaryLocation">
    <bean:define id="location" name="organizationDetail" property="orgPrimaryLocation" type="org.afscme.enterprise.organization.LocationData"/>

    <%@ include file="../include/location_primary_content.inc" %>

</logic:notEmpty>

<logic:empty name="organizationDetail" property="orgPrimaryLocation">

    <%@ include file="../include/location_noprimary_content.inc" %>

</logic:empty>

</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <BR><afscme:currentOrganizationName />
        </td>
    </tr>
</table>

<!-- Something for tabs. -->
<%@ include file="/include/organization_tab.inc" %>

<%@ include file="../include/footer.inc" %>
