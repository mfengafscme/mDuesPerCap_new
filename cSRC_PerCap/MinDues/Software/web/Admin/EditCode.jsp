<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%--
If an 'add' paramter is passed to this JSP, then this is the 'Add Code' screen,
otherwise it is the 'Edit Code' screen
--%>
<%!String title, help;%>
<logic:present parameter="add">
	<%title = "Add Code"; help = "AddCode.html";%>
</logic:present>
<logic:notPresent parameter="add">
	<%title = "Code Edit"; help = "CodeEdit.html";%>
</logic:notPresent>
<%@ include file="../include/header.inc" %>

<html:form action="saveCode" focus="code">

    <html:hidden property="codeTypeKey"/>
    <html:hidden property="pk"/>

    <table align="center">
        <tr>
            <td colspan="2">
                <table class="BodyContentNoWidth" width="100%">
                    <tr>
                        <td><label for="name">Code</label>
                        <td width="100%"><html:text property="code" size="8" maxlength="8"/><html:errors property="code"/>
                    </tr>
                    <tr>
                        <td><label for="description">Description</label>
                        <td width="100%"><html:text property="codeDescription" size="50" maxlength="50"/><html:errors property="codeDescription"/>
                    </tr>
                    <tr>
                        <td><label for="description">Sort Key</label>
                        <td width="100%"><html:text property="sortKey" size="30" maxlength="30"/><html:errors property="sortKey"/>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align="left">
                <html:submit styleClass="button"/>
            </td>
            <td align="right">
                <html:reset styleClass="button"/>
                <html:cancel styleClass="button"/>
            </td>
        </tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %>

