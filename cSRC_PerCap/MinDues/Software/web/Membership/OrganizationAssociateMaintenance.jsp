<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Organization Associate Maintenance", help = "OrganizationAssociateMaintenance.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="organizationAssociateListForm" type="org.afscme.enterprise.organization.web.OrganizationAssociateListForm"/>

<table width="100%" align="center">
    <tr>
        <td valign="top" align="left">
            <afscme:button forward="ViewOrganizationDetail">Return</afscme:button>
	</td>
        <td align="right">
            <afscme:button page="/verifyPerson.action?back=AssociateAdd">Add New</afscme:button>
	</td>
    </tr>
    <tr>
        <td align="left" colspan="2"><BR>
           <afscme:searchNav formName="form" action="/viewOrganizationAssociateList.action"/>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <afscme:currentOrganizationName /><BR><BR> 
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr>
        <th width="11%">Select</th>
        <th><afscme:sortLink styleClass="TH" action="/viewOrganizationAssociateList.action" formName="form" field="name" title="Sort By Name">Name</afscme:sortLink></th>
        <th><afscme:sortLink styleClass="TH" action="/viewOrganizationAssociateList.action" formName="form" field="title" title="Sort By Organization Title">Organization Title</afscme:sortLink></th>
        <th><afscme:sortLink styleClass="TH" action="/viewOrganizationAssociateList.action" formName="form" field="location" title="Sort By Location">Location</afscme:sortLink></th>
        <th width="11%"><afscme:sortLink styleClass="TH" action="/viewOrganizationAssociateList.action" formName="form" field="phone" title="Sort By Phone Number">Phone Number</afscme:sortLink></th>
        <th><afscme:sortLink styleClass="TH" action="/viewOrganizationAssociateList.action" formName="form" field="email" title="Sort By Email Address">Email Address</afscme:sortLink></th>
    </tr>
<logic:iterate id="associate" name="form" property="results" type="org.afscme.enterprise.organization.OrgAssociateResult">
    <tr>
        <td align="center">
            <afscme:link page="/viewOrganizationAssociateDetail.action" paramName="associate" paramProperty="personPk" paramId="personPk" styleClass="action" title="View Organization Person Detail">View</afscme:link> 
            <afscme:link page="/removeOrganizationAssociate.action" paramName="associate" paramProperty="personPk" paramId="personPk" styleClass="action" title="Remove  from Organization" confirm="Are you sure you want to remove this person from the Organization?">Remove</afscme:link> 
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
        <td align="center">
            <bean:write name="associate" property="phoneNumber"/>
            <logic:empty name="associate" property="phoneNumber">&nbsp;</logic:empty>
        </td>
        <td align="center">
            <bean:write name="associate" property="emailAddress"/>
            <logic:empty name="associate" property="emailAddress">&nbsp;</logic:empty>
        </td>
    </tr>
</logic:iterate>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <BR><afscme:currentOrganizationName />
        </td>
    </tr>
</table>

<table width="100%" align="center">
    <tr>
        <td align="left" colspan="2">
           <afscme:searchNav formName="form" action="/viewOrganizationAssociateList.action"/>
        </td>
    </tr>
    <tr valign="top">
        <td>
            <BR><afscme:button forward="ViewOrganizationDetail">Return</afscme:button>
        </td>
        <td align="right">
            <BR><afscme:button page="/verifyPerson.action?back=AssociateAdd">Add New</afscme:button>
        </td>
    </tr>
    <tr>
        <td align="left" colspan="10" class="ContentHeaderTD">
            <BR>Total Number of Matches:&nbsp;<bean:write name="form" property="total"/>
        </td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>
