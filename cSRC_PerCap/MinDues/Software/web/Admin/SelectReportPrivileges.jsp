<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Select Report Privileges", help = "SelectReportPrivileges.html";%>
<%@ include file="../include/header.inc" %>

<script type="text/javascript" language="JavaScript" src="../js/checkbox.js">
</script>
<html:form action="/selectReportPrivileges.action">

    <bean:define id="form" name="reportPrivilegesForm" type="org.afscme.enterprise.roles.web.ReportPrivilegesForm"/>

    <html:hidden property="pk"/>
    <html:hidden property="update" value="true"/>

    <table width="95%"><tr><td colspan="2">
    <table class="BodyContentNoWidth" border="1" width="100%" cellspacing="0">
        <tr>
            <logic:iterate id="catName" name="form" property="categories" type="String">
                <th align="left"><a class="TH" href="javascript:toggleCheckBoxes('reportPrivilegesForm', '<%=catName%>')"><%=catName%></a></th>
            </logic:iterate>
        </tr>
        <tr>

            <logic:iterate id="catName" name="form" property="categories" type="String">
            <td valign="top" align="left"><table>

                    <logic:iterate id="element" collection='<%=form.getReports(catName)%>'>
                    <bean:define id="id" name="element" property="pk" type="Integer"/>
                        <tr><td>
                            <html:multibox styleId='<%=catName+id%>' name="form" property="selected"><%=id%></html:multibox>
                            <label for='<%=catName+id%>'><bean:write name="element" property="name"/></label><br>
                            <bean:write name="element" property="description"/>
                        </td></tr>
                    </logic:iterate>
            </table></td>
            </logic:iterate>

        </tr>
    </table>
    </td></tr>
        <tr>
            <td align="left"><html:submit styleClass="button"/></td>
            <td align="right"><html:reset styleClass="button"/> <html:cancel styleClass="button"/></td>
        </tr>
    </table>

</html:form>

<%@ include file="../include/footer.inc" %>

