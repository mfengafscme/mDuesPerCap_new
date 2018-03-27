<%@ page import="org.afscme.enterprise.roles.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Select Roles", help = "SelectRoles.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="/selectRoles.action">

    <bean:define id="form" name="rolesForm" type="org.afscme.enterprise.users.web.RolesForm"/>

    <html:hidden property="update" value="true"/>

    <table width="40%" align="center">
    <tr>
        <td colspan="2">
            <table class="BodyContentNoWidth" width="100%">
                    <logic:iterate id="element" name="form" property="allRoles">
                        <bean:define id="id" name="element" property="pk" type="Integer"/>
                            <tr>
                                <td><html:multibox styleId='<%=id.toString()%>' name="form" property="selected" value='<%=id.toString()%>'/>
                                <td width="100%"><label for='<%=id.toString()%>'><bean:write name="element" property="name"/></label>
                            <tr>
                                <td colspan="2"><bean:write name="element" property="description"/>
                    </logic:iterate>
            </table>
    </tr>
    <tr>
        <td align="left"><html:submit styleClass="button"/>
        <td align="right"><html:reset styleClass="button"/> <html:cancel styleClass="button"/>
    </tr>
    </table>

</html:form>

<%@ include file="../include/footer.inc" %>

