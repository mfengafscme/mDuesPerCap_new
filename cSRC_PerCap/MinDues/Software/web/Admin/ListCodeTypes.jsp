<%@ page import="org.afscme.enterprise.roles.*,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Code Type", help = "CodeType.html";%>
<%@ include file="../include/header.inc" %>

    <bean:define name="CategoryTypes" id="categoryTypes" type="java.util.Map"/>
    <table align="center">
    <tr><td align="left"><afscme:button page="/editCodeType.action?add=true">Add Code Type</afscme:button></td><td></td></tr>
    <tr><td colspan="2">
        <table class="BodyContentNoWidth">
            <tr>
                <%--
                Print table headers for each category
                --%>
                <logic:iterate name="Categories" id="category">
                <th><bean:write name="category" property="value.name"/>
                </logic:iterate>
            <tr>
                <%--
                FOR EACH Category
                --%>
                <logic:iterate name="Categories" id="category">
                <bean:define id="categoryPk" name="category" property="value.pk"/>
                <td valign="top"><table cellpadding="4">
                    <%--
                    FOR EACH CodeType
                    --%>
                    <logic:iterate id="codeType" collection='<%=categoryTypes.get(categoryPk)%>'>
                        <tr>
                            <td>
                                <afscme:link page="/editCodeType.action" paramName="codeType" paramId="codeTypeKey" paramProperty="key"><bean:write name="codeType" property="name"/></afscme:link><br>
                                <bean:write name="codeType" property="description"/>
                            </td>
                        </tr>
                    </logic:iterate>
                </td></table>
                </logic:iterate>
                </tr>
            </table>
        </td></tr>
    </table>


<%@ include file="../include/footer.inc" %>

