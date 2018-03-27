<%@ page import="org.afscme.enterprise.roles.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Select Field Privileges", help = "SelectFieldPrivileges.html";%>
<%@ include file="../include/header.inc" %>

<script type="text/javascript" language="JavaScript" src="../js/checkbox.js">
</script>
<html:form action="/selectFieldPrivileges.action">

    <bean:define id="form" name="fieldPrivilegesForm" type="org.afscme.enterprise.roles.web.FieldPrivilegesForm"/>

    <html:hidden property="pk"/>
    <html:hidden property="update" value="true"/>

    <table align="center" width="95%"><tr>
        <td align="left"><br><html:submit styleClass="BUTTON"/></td>
        <td align="right"><br><html:cancel styleClass="BUTTON"/></td>
    </tr></table>

<%String fieldsProperty="selected";%>  
<%String formName="fieldPrivilegesForm";%>  
<%@ include file="../include/field_selection.inc" %>

    <table align="center" width="95%"><tr>
        <td align="left"><html:submit styleClass="BUTTON"/></td>
        <td align="right"><html:cancel styleClass="BUTTON"/></td>
    </tr></table>

</html:form>

<%@ include file="../include/footer.inc" %>

