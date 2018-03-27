<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title, help;%>
<logic:present parameter="add">
	<%title = "Add Role"; help = "AddRole.html";%>
</logic:present>
<logic:notPresent parameter="add">
	<%title = "Role Detail"; help = "RoleDetail.html";%>
</logic:notPresent>
<%@ include file="../include/header.inc" %>

<html:form action="saveRole" focus="name">

    <html:hidden property="pk"/>

    <table align="center" width="95%">
        <tr>
            <td colspan="2">
                <table class="BodyContentNoWidth" width="100%">
                    <tr>
                        <td><label for="name">Name</label></td>
                        <td width="100%"><html:text property="name" size="30" maxlength="30"/><html:errors property="name"/></td>
                    </tr>
                    <tr>
                        <td><label for="description">Description</label></td>
                        <td width="100%"><html:text property="description" size="50" maxlength="50"/><html:errors property="description"/></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align="left">
                <html:submit styleClass="button"/>
            </td>
            <td align="right">
                <html:submit property="selectPrivileges" value="Select Privileges" styleClass="button"/>
                <html:submit property="selectReportPrivileges" value="Select Report Privileges" styleClass="button"/>
                <html:submit property="selectFieldPrivileges" value="Select Field Privileges" styleClass="button"/>
                <html:reset styleClass="button"/>
                <html:cancel styleClass="button"/>
            </td>
        </tr>
    </table>

</html:form>

<%@ include file="../include/footer.inc" %>

