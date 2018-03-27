<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Organization Verify Results", help = "OrganizationVerifyResults.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="verifyOrganizationForm" type="org.afscme.enterprise.organization.web.VerifyOrganizationForm"/>

<table width="100%" align="center">
    <tr>
        <td align="left">
            <afscme:searchNav formName="form" action="/verifyOrganization.action"/></td>
        <td align="right">
            <afscme:button page="/addOrganizationDetail.action" postfix="&nbsp;">Add New</afscme:button>
            <afscme:button forward="ViewVerifyOrganization">New Search</afscme:button></td> 
    </tr>
</table>

<BR>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="BodyContentNoWidth" align="center">
    <tr align="center">
        <th width="10%" align="center">Select</th>
        <th align="center"><afscme:sortLink styleClass="TH" action="/verifyOrganization.action" formName="form" field="orgName" title="Sort By Organization Name">Organization Name</afscme:sortLink></th>
        <th width="25%" align="center"><afscme:sortLink styleClass="TH" action="/verifyOrganization.action" formName="form" field="orgType" title="Sort By Organization Type">Organization Type</afscme:sortLink></th>
    </tr>
    <logic:iterate id="element" name="form" property="results" type="org.afscme.enterprise.organization.OrganizationResult">
    <tr>
        <td align="center"><afscme:link page="/viewOrganizationDetail.action" paramId="orgPK" paramName="element" paramProperty="orgPK" title="View Organization Details" styleClass="action">View</afscme:link></td>
        <td align="center"><bean:write name="element" property="orgNm"/></td>
        <td align="center"><afscme:codeWrite name="element" codeType="organizationType" property="orgType" format="{1}"/></td>
    </tr>
    </logic:iterate>
</table>

<BR>
<table width="100%" align="center">
    <tr>
        <td align="left">
           <afscme:searchNav formName="form" action="/verifyOrganization.action"/></td>
        <td align="right">
            <afscme:button page="/addOrganizationDetail.action" postfix="&nbsp;">Add New</afscme:button>
            <afscme:button forward="ViewVerifyOrganization">New Search</afscme:button></td>
    </tr>
    <tr>
        <td align="left" class="ContentHeaderTD">
            <BR>Total Number of Matches:&nbsp;<bean:write name="form" property="total"/>
        </td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>