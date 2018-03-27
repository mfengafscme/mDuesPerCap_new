<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Role Listing", help = "RoleListing.html";%>
<%@ include file="../include/header.inc" %>

<table width="50%" align="center"><tr><td>

    <table class="BodyContentNoWidth" width="100%" border="1" cellpadding="0" cellspacing="0">
        <tr>
            <th align="center" colspan="2">Select</th>
            <th align="center"><afscme:sortLink styleClass="TH" action="/listRoles.action" formName="listRolesForm" field="name">Name</afscme:sortLink></th>
            <th align="center"><afscme:sortLink styleClass="TH" action="/listRoles.action" formName="listRolesForm" field="description">Description</afscme:sortLink></th>
        </tr>
        <logic:iterate id="element" name="listRolesForm" property="results" type="org.afscme.enterprise.roles.RoleData">
        <tr>
            <td align="center"><afscme:link page="/editRole.action" paramId="pk" paramName="element" paramProperty="pk">Edit</afscme:link></td>
            <td align="center"><afscme:link page="/deleteRole.action" paramId="pk" paramName="element" paramProperty="pk" confirm="Are you sure you wish to delete this role?">Delete</afscme:link></td>
            <td align="center"><bean:write name="element" property="name"/></td>
            <td align="center"><bean:write name="element" property="description"/></td>
        </tr>
        </logic:iterate>
    </table>
</td></tr>
<tr><td align="left">
<afscme:button page="/editRole.action">Add Role</afscme:button>
</tr></tr>
</table>

<%@ include file="../include/footer.inc" %>

