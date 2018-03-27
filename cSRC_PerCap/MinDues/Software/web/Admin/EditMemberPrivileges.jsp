<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Member Privileges", help = "MemberPrivileges.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="saveMemberPrivileges" onreset="memberPrivilegesForm.allowView.disabled=viewStartedDisabled">

<table align="center"><tr><td colspan="2">
    <table class="BodyContentNoWidth">
        <tr> 
            <td><label for="allowView">Members may View their own data</label></td>
            <td><html:checkbox styleId="allowView   " property="allowView"/></td>
        </tr>
        <tr> 
            <td><label for="allowEdit">Members may Edit their own data</label></td>
            <td><html:checkbox styleId="allowEdit" property="allowEdit" onclick="updateCheckboxes()"/></td>
        </tr>
    </table>
    </tr></td>
        <tr>
            <td align="left"><html:submit styleClass="button"/></td>
            <td align="right"><html:button property="none" value="Reset" onclick="memberPrivilegesForm.reset(); updateCheckboxes()" styleClass="button"/><html:cancel styleClass="button"/></td>
        </tr>
</table>
</html:form>

<%--
This javascript ensures if users can 'edit', then they can 'view'
--%>
<script language="JavaScript">
<!--
function updateCheckboxes() {
    if (memberPrivilegesForm.allowEdit.checked) {
        memberPrivilegesForm.allowView.checked = true;
    };
    memberPrivilegesForm.allowView.disabled = memberPrivilegesForm.allowEdit.checked;

}   
updateCheckboxes();
-->
</script>

<%@ include file="../include/footer.inc" %>

