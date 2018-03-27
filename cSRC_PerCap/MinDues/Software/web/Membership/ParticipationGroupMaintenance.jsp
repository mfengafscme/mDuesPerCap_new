<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Participation Group Maintenance", help = "ParticipationGroupMaintenance.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="participationCodeListForm" type="org.afscme.enterprise.participationgroups.web.ParticipationCodeListForm"/>

<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr valign="top">
        <td align="right">
            <BR><afscme:button page="/addParticipationGroup.action">Add Group</afscme:button><BR><BR> 
        </td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr>
        <th width="6%">Select</th>
        <th>Group Name</th>
        <th align="left">Group Description</th>
    </tr>
<logic:iterate id="group" name="form" property="results" type="org.afscme.enterprise.participationgroups.ParticipationGroupData">
    <tr>
        <td align="center">
            <afscme:link page="/viewParticipationGroup.action" paramName="group" paramProperty="groupPk" paramId="groupPk" styleClass="action" title="View the Details for this Participation Group">View</afscme:link>
        </td>
        <td align="center"><bean:write name="group" property="name"/></td>
        <td>
            <bean:write name="group" property="description"/>
            <logic:empty name="group" property="description">&nbsp;</logic:empty>
        </td>
    </tr>
</logic:iterate>
</table>

<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr valign="top">
        <td align="right">
            <BR><afscme:button page="/addParticipationGroup.action">Add Group</afscme:button><BR><BR> 
        </td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>
