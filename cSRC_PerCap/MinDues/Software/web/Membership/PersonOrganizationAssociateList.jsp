<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Person/Organization Associate List", help = "PersonOrganizationAssociateList.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="personOrgAssociateListForm" type="org.afscme.enterprise.organization.web.PersonOrgAssociateListForm"/>

<table width="100%" align="center">
    <tr>
        <td valign="top" align="left">
            <afscme:button action="/viewPersonDetail.action">Return</afscme:button><BR><BR>
	</td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <afscme:currentPersonName /><BR><BR> 
        </td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr>
        <th width="7%">Select</th>
        <th><afscme:sortLink styleClass="TH" action="/viewPersonOrgAssociateList.action" formName="form" field="name" title="Sort By Organization Name">Organization Name</afscme:sortLink></th>
        <th><afscme:sortLink styleClass="TH" action="/viewPersonOrgAssociateList.action" formName="form" field="title" title="Sort By Organization Title">Organization Title</afscme:sortLink></th>
        <th><afscme:sortLink styleClass="TH" action="/viewPersonOrgAssociateList.action" formName="form" field="location" title="Sort By Location">Location</afscme:sortLink></th>
    </tr>
<logic:iterate id="associate" name="form" property="results" type="org.afscme.enterprise.organization.OrgAssociateResult">
    <tr>
        <td align="center">
            <afscme:link page="/viewOrganizationAssociateDetail.action" paramName="associate" paramProperty="orgPk" paramId="orgPK" styleClass="action" title="View Organization Person Detail">View</afscme:link> 
        </td>
        <td align="center"><bean:write name="associate" property="name"/></td>
        <td align="center">
            <bean:write name="associate" property="orgTitle"/>
            <logic:empty name="associate" property="orgTitle">&nbsp;</logic:empty>
        </td>
        <td align="center">
            <bean:write name="associate" property="locationName"/>
            <logic:empty name="associate" property="locationName">&nbsp;</logic:empty>
        </td>
    </tr>
</logic:iterate>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <BR><afscme:currentPersonName />
        </td>
    </tr>
</table>

<table width="100%" align="center">
    <tr valign="top">
        <td>
            <BR><afscme:button action="/viewPersonDetail.action">Return</afscme:button>
        </td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>
