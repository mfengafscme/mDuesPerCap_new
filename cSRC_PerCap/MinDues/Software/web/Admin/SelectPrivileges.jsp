<%@ page import="org.afscme.enterprise.roles.*,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Select Privileges", help = "SelectPrivileges.html";%>
<%@ include file="../include/header.inc" %>

<script type="text/javascript" language="JavaScript" src="../js/checkbox.js">
</script>
<html:form action="/selectPrivileges.action">

    <bean:define id="form" name="privilegesForm" type="org.afscme.enterprise.roles.web.PrivilegesForm"/>

    <html:hidden property="pk"/>
    <html:hidden property="update" value="true"/>
<%!String[] catNames = new String[] {"Member", "Officer", "Affiliate", "Organization", "Person", "System"}; %>
<%!char[] catKeys = new char[] {'M', 'O', 'A', 'R', 'P', 'S'}; %>
<%!List privileges;%>
<%boolean isDataUtility = false;%>

    <table width="95%">
    <tr>
        <td align="left">
		<html:submit styleClass="button"/>
	  </td>
        <td align="right">
		<html:button property="resetButton" value="Reset" styleClass="button" onclick="document.forms['privilegesForm'].reset(); syncCheckBoxes('privilegesForm', '', '_edit', '_view')"/> 
		<html:cancel styleClass="button"/>
	  </td>
    </tr>
    <tr><td colspan="2">
    <% do { %>

    <tr><td colspan="2">
    <b><%if (isDataUtility) { %>Data Utility Privileges<% } else { %>Main Privileges<%}%></b>
    <table class="BodyContentNoWidth" border="1" width="100%" cellspacing="0">
        <% final int COLS = 3; %>
        <% for (int catStart = 0; catStart < catNames.length; catStart += COLS) { %>
        <tr>
            <%-- Display the column headers (Member, Officer, ...) --%>
            <%for (int i = catStart; i < catNames.length && i - catStart < COLS; i++) {%>
            <%String javaScriptId = form.getJavaScriptId(isDataUtility, catKeys[i], null, null);%>
            <th align="left"><a class="TH" href="javascript:toggleCheckBoxes('privilegesForm', '<%=javaScriptId%>');syncCheckBoxes('privilegesForm', '<%=javaScriptId%>', '_edit', '_view')"><%=catNames[i]%></th>
            <%}%>
        </tr>
        <tr>

            <%-- For each category...(Member, Officer, ...) --%>
            <%for (int i = catStart; i < catKeys.length && i - catStart < COLS; i++) {%>
            <td valign="top" align="left">
                    <table>
                    <%-- The General Privileges for the category --%>
                    <%privileges = form.getPrivileges(catKeys[i], 'G', isDataUtility);%>
                    <%if (privileges.size() != 0) {%>
                        <logic:iterate id="element" collection="<%=privileges%>">
                            <bean:define id="id" name="element" property="key" type="String"/>
                            <%String javaScriptId = form.getJavaScriptId(isDataUtility, catKeys[i], "general", id);%>
                                <tr>
                                    <td><html:multibox styleId='<%=javaScriptId%>' name="form" property="selected" value='<%=id%>'/></td>
                                    <td>&nbsp;</td>
                                    <td nowrap><label for='<%=javaScriptId%>'><bean:write name="element" property="name"/></label></td>
                                </tr>
                        </logic:iterate>
                    <%}%>

                    <%-- The Search Privileges for the category --%>
                    <%privileges = form.getPrivileges(catKeys[i], 'S', isDataUtility);%>
                    <%if (privileges.size() != 0) {%>
                        <tr><td colspan="2"><label><a href="javascript:toggleCheckBoxes('privilegesForm', '<%=form.getJavaScriptId(isDataUtility, catKeys[i], "search", null)%>')">Search</a></label></td></tr>
                        <logic:iterate id="element" collection="<%=privileges%>">
                            <bean:define id="id" name="element" property="key" type="String"/>
                            <%String javaScriptId = form.getJavaScriptId(isDataUtility, catKeys[i], "search", id);%>
                            <tr>
                                <td><html:multibox styleId='<%=javaScriptId%>' name="form" property="selected" value='<%=id%>'/></td>
                                <td>&nbsp;</td>
                                    <td nowrap><label for='<%=javaScriptId%>'><bean:write name="element" property="name"/></label></td>
                            </tr>
                        </logic:iterate>
                    <%}%>

                    <%-- The Edit and View Privileges for the category --%>
                    <%privileges = form.getEditViewPrivileges(catKeys[i], isDataUtility);%>
                    <%String javaScriptId=form.getJavaScriptId(isDataUtility, catKeys[i], null, null);%>
                    <%if (privileges.size() != 0) {%>
                        <tr>
                            <td><label><a href="javascript:toggleCheckBoxes('privilegesForm', '<%=javaScriptId+"_edit"%>');syncCheckBoxes('privilegesForm', '<%=javaScriptId%>', '_edit', '_view')">Maintain</a></label></td>
                            <td><label><a href="javascript:toggleCheckBoxes('privilegesForm', '<%=javaScriptId+"_view"%>')">View</a></label></td>
                        </tr>

                        <logic:iterate id="element" collection="<%=privileges%>" type="java.lang.String">
                            <%PrivilegeData editPrivilege = form.getPrivilege(catKeys[i], 'E', isDataUtility, element); %>
                            <%PrivilegeData viewPrivilege = form.getPrivilege(catKeys[i], 'V', isDataUtility, element); %>
                            <tr>
                                <% if (editPrivilege != null) { %>
                                    <% String javaScriptIdEdit=form.getJavaScriptId(isDataUtility, catKeys[i], "edit", editPrivilege.getKey());%>
                                    <% if (viewPrivilege == null) { %>
                                    	<td><html:multibox styleId='<%=javaScriptIdEdit%>' name="form" property="selected" value='<%=editPrivilege.getKey()%>'/></td>
                                    <% } else { %>
                                   	<td><html:multibox styleId='<%=javaScriptIdEdit%>' name="form" property="selected" value='<%=editPrivilege.getKey()%>' onclick="changeNext(this)"/></td>                                    
                                    <% } %>
                                <% } else { %>
                                    <td>&nbsp;</td>
                                <% } %>
                                <% if (viewPrivilege != null) { %>
                                    <% String javaScriptIdView=form.getJavaScriptId(isDataUtility, catKeys[i], "view", viewPrivilege.getKey());%>
                                    <td><html:multibox styleId='<%=javaScriptIdView%>' name="form" property="selected" value='<%=viewPrivilege.getKey()%>'/></td>
                                <% } else { %>
                                    <td>&nbsp;</td>
                                <% } %>
                                <td nowrap><label><bean:write name="element"/></label></td>
                            </tr>
                        </logic:iterate>
                    <%}%>
            </table>
                <%}%>
        <%}%>
    </table>

    <% isDataUtility = !isDataUtility; %>
    <%} while (isDataUtility);%>
    </td></tr>
        <tr>
            <td align="left"><html:submit styleClass="button"/></td>
            <td align="right"><html:button property="resetButton" value="Reset" styleClass="button" onclick="document.forms['privilegesForm'].reset(); syncCheckBoxes('privilegesForm', '', '_edit', '_view')"/> <html:cancel styleClass="button"/></td>
        </tr>
    </table>

</html:form>

<script language="JavaScript">
<!--
syncCheckBoxes('privilegesForm', '', '_edit', '_view')
-->
</script>

<%@ include file="../include/footer.inc" %>

