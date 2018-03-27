<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<bean:define id="form" name="codeTypeForm" type="org.afscme.enterprise.codes.web.CodeTypeForm"/>
<%!String title, help;%>
<logic:equal name="form" property="add" value="true">
	<%title = "Add Code Type"; help = "AddCodeType.html";%>
</logic:equal>
<logic:notEqual name="form" property="add" value="true">
	<%title = "Code Type Edit"; help = "CodeTypeEdit.html";%>
</logic:notEqual>
<%@ include file="../include/header.inc" %>


<html:form action="saveCodeType" focus="name">

    <logic:notEqual name="form" property="add" value="true">
        <html:hidden property="codeTypeKey"/>
    </logic:notEqual>
    <html:hidden property="add"/>

    <table align="center">
        <tr>
            <td align="left">
                <html:submit styleClass="button"/>
            </td>
            <td align="right">
                <logic:notEqual name="form" property="add" value="true">
                    <html:submit property="addCode" value="Add Code" styleClass="button"/>&nbsp;
                    <afscme:button page="/deleteCodeType.action" paramId="codeTypeKey" paramProperty="codeTypeKey" paramName="codeTypeForm" confirm="Are you sure you wish to delete this code type?">Delete</afscme:button>&nbsp;
                </logic:notEqual>
                <html:reset styleClass="button"/>
                <html:cancel styleClass="button"/>
            </td>
        </tr>
        <%--
        This section displays the code type data for editing
        --%>
        <tr>
            <td colspan="2">
                <table class="BodyContentNoWidth" width="100%">
                    <tr>
                        <td><label for="name">Name</label>
                        <td width="100%"><html:text property="name" size="30" maxlength="30"/><html:errors property="name"/>
                    </tr>
                    <logic:equal name="form" property="add" value="true">
                    <tr>
                        <td><label for="codeTypeKey">Key</label>
                        <td width="100%"><html:text property="codeTypeKey" size="20" maxlength="20"/><html:errors property="codeTypeKey"/>
                    </tr>
                    </logic:equal>
                    <logic:notEqual name="form" property="add" value="true">
                    <tr>
                        <td><label for="name">Key</label>
                        <td width="100%"><bean:write name="form" property="codeTypeKey"/>
                    </tr>
                    </logic:notEqual>
                    <tr>
                        <td><label for="description">Description</label>
                        <td width="100%"><html:text property="description" size="50" maxlength="50"/><html:errors property="description"/>
                    </tr>
                    <tr>
                        <td><label for="category">Category</label>
                        <td width="100%">
                            <html:select property="category">
                                <html:options name="form" labelProperty="categoryNames" property="categoryKeys"/>
                            </html:select>
                    </tr>
                </table>
            </td>
        </tr>
<%--
This section lists the codes in the code type
--%>
<tr><td colspan="2">
      <logic:notEqual name="form" property="add" value="true">
            <bean:define id="codeTypeKey" name="form" property="codeTypeKey"/>
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="BodyContentNoWidth">
                    <bean:define id="sortAction" value='<%="/editCodeType.action?codeTypeKey="+codeTypeKey%>'/>
					<TR>
						<TH nowrap>
                            Select
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action='<%=sortAction%>' formName="form" field="code">Code</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action='<%=sortAction%>' formName="form" field="description">Description</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action='<%=sortAction%>' formName="form" field="sortKey">Sort Key</afscme:sortLink>
						</TH>
					</TR>
                    <logic:iterate name="form" property="results" type="org.afscme.enterprise.codes.CodeData" id="code">
					<TR>
						<TD align="center" class="ContentTD">
                            <afscme:link page='<%="/editCode.action?codeTypeKey="+codeTypeKey%>' paramName="code" paramProperty="pk" paramId="pk" styleClass="action">Edit</afscme:link>
                            <afscme:link page='<%="/deleteCode.action?codeTypeKey="+codeTypeKey%>' paramName="code" paramProperty="pk" paramId="pk" styleClass="action" confirm="Are you sure you wish to delete this code?">Delete</afscme:link>
						</TD>
						<TD align="center" class="ContentTD">
                            <bean:write name="code" property="code"/>
						</TD>
						<TD align="center" class="ContentTD">
                            <bean:write name="code" property="description"/>
						</TD>
						<TD align="center" class="ContentTD">
                            <bean:write name="code" property="sortKey"/>
						</TD>
					</TR>
                    </logic:iterate>
				</TABLE>
      </logic:notEqual>
</td></tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %>

