<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<bean:define name="mailingListsInformationForm" id="mailingListsInformation" type="org.afscme.enterprise.mailinglists.web.MailingListsInformationForm"/>
<bean:define name="mailingListsAddressAssociationForm" id="form" type="org.afscme.enterprise.mailinglists.web.MailingListsAddressAssociationForm"/>
<%!String help = "MailingListsAddressAssociation.html", title = ""; %>
<% title = (mailingListsInformation.isMLBP()) ? "Mailing Lists Address Association" : "Mailing Lists Location Association"; %>

<%@ include file="../include/header.inc" %>


<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR" valign="top">
			<%=mailingListsInformation.getHeader()%>
			<BR>
			<%=form.getHeader()%>
			<BR><BR>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	
	<logic:iterate id="addressList" name="form" property="addressList">
	<bean:define id="address" name="addressList"/>

	<TR valign="top">
		<TD width="5%" align="middle">
			<bean:define id="recordPk" name="address" property="recordData.pk"/>
			<% 
			   if (form.getCurrentAddressPk().compareTo(recordPk) == 0) { %>
				<afscme:link page="/viewMailingListsAddressAssociation.action?done" title="Leave as Current and Return" styleClass="action">Current</afscme:link>
			<% } else { %>
				<afscme:link page='<%="/viewMailingListsAddressAssociation.action?select&mailingListPk="+form.getMailingListPk()%>' paramName="address" paramProperty="recordData.pk" paramId="pk" title="Select Address" styleClass="action">Select</afscme:link>
			<% } %>
		</TD>
		<TD valign="top">
			<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH width="10%">
						Primary
					</TH>
					<TH width="50%">
						Association
					</TH>
					<TH width="10%">
						Private
					</TH>
					<TH width="10%">
						Bad
					</TH>
					<TH>
						Date Marked Bad
					</TH>
				</TR>
				<TR>
					<TD class="ContentTD" align="center">
						<html:checkbox name="address" property="primary" disabled="true"/>
					</TD>
					<TD class="ContentTD" align="center">
				<% if (mailingListsInformation.isMLBP()) { %>
						<afscme:codeWrite name="address" property="type" codeType="PersonAddressType"/>
				<% } else { %>
						<bean:write name="address" property="locationNm"/>
				<% } %>
					</TD>
					<TD class="ContentTD" align="center">
						<html:checkbox name="address" property="private" disabled="true"/>
					</TD>
					<TD class="ContentTD" align="center">
						<html:checkbox name="address" property="bad" disabled="true"/>
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:dateWrite name="address" property="badDate"/>
					</TD>
				</TR>
				<%@ include file="../include/address_content.inc" %>
			</TABLE>
		</TD>
	</TR>
	</logic:iterate>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD class="ContentHeaderTR" valign="bottom">
			<BR>
			<%=mailingListsInformation.getHeader()%>
			<BR>
			<%=form.getHeader()%>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="right">
			<BR><afscme:button page="/viewMailingListsAddressAssociation.action?done">Cancel</afscme:button>
		</TD>
	</TR>
</TABLE>

<%@ include file="../include/footer.inc" %>
