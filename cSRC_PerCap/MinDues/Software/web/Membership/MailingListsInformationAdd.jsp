<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Mailing Lists Information - Add", help = "MailingListsInformationAdd.html";%>
<%@ include file="../include/header.inc" %>

<bean:define name="mailingListsInformationForm" id="mailingListsInformation" type="org.afscme.enterprise.mailinglists.web.MailingListsInformationForm"/>
<% String mailingListType = "person"; %>

<html:form action="/addMailingListsInformation" focus="mailingListPk">
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR" valign="top">
			<%=mailingListsInformation.getHeader()%>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="2" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TH>
			* Mailing List Codes 
		</TH>
	<logic:equal name="mailingListsInformation" property="MLBP" value="false">
	<% mailingListType = "organization"; %>
		<TH>
			Bulk Count
		</TH>
	</logic:equal>
	</TR>
	<TR>
		<TD class="ContentHeaderTD" width="80%" align="center">
			<html:select property="mailingListPk">
			<% if (mailingListsInformation.isMLBP()) { %>		
				<afscme:mailingListOptions type="<%=mailingListType%>" allowNull="true" nullDisplay="[Select a Mailing List]"/>
			<% } else { %>
				<afscme:mailingListOptions type="<%=mailingListType%>" allowNull="true" nullDisplay="[Select a Mailing List]" format="{1}"/>
			<% } %>
			</html:select>
		</TD>
	<logic:equal name="mailingListsInformation" property="MLBP" value="false">
		<TD class="ContentHeaderTD" ALIGN="center">
			<html:text property="mailingListBulkCount" size="2" maxlength="4"/>
		</TD>
	</logic:equal>
	</TR>
	<TR>
		<TD colspan="2" align="center">
			<html:errors property="mailingListPk"/>
			<html:errors property="mailingListBulkCount"/>
			<html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<html:submit styleClass="button"/>
			<BR>
		</TD>
		<TD align="right">
			<BR>
			<html:reset styleClass="button"/>
			<html:cancel styleClass="button"/>
			<BR>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR>
		<TD align="center">
			<BR><B><I>* Indicates Required Fields</I></B>
			<BR>
		</TD>
	</TR>
</TABLE>
</html:form>
<%@ include file="../include/footer.inc" %>
