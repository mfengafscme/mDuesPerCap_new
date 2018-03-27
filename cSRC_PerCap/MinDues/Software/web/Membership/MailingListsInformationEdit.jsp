<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Mailing Lists Information - Edit", help = "MailingListsInformationEdit.html";%>
<%@ include file="../include/header.inc" %>

<bean:define name="mailingListsInformationForm" id="mailingListsInformation" type="org.afscme.enterprise.mailinglists.web.MailingListsInformationForm"/>
<bean:define name="editMailingListsInformationForm" id="form" type="org.afscme.enterprise.mailinglists.web.EditMailingListsInformationForm"/>

<html:form action="/editMailingListsInformation?submit" focus="mailingListBulkCount">

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
			<BR><BR>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR" valign="top">
			<%=mailingListsInformation.getHeader()%>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH align="left">
						Mail Code 
					</TH>
					<TH WIDTH="10%">
						* Bulk Count
					</TH>
					<TH width="17%">
						Send Mail To 
					</TH>
				</TR>
				<logic:iterate id="mailingLists" name="mailingListsInformation" property="mailingLists">
				<bean:define id="data" name="mailingLists" type="org.afscme.enterprise.mailinglists.MailingListData"/> 
				<TR>
					<TD class="ContentTD">
						<bean:write name="data" property="mailingListNm"/>
					</TD>
					<TD class="ContentTD">
					<% if (mailingListsInformation.isBulkType(data.getMailingListNm())) { %>
						<html:hidden property="pk"/>
						<html:hidden property="mailingListPk"/>
						<html:text property="mailingListBulkCount" size="2" maxlength="3"/>
					<% } else { %>
						<bean:write name="data" property="mailingListBulkCount"/>
					<% } %>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="data" property="locationNm"/>
					</TD>
				</TR>
				</logic:iterate>
				<TR>
					<TD colspan="3" align="center">
						<html:errors property="mailingListBulkCount"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD class="ContentHeaderTR" valign="bottom">
			<%=mailingListsInformation.getHeader()%>
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
