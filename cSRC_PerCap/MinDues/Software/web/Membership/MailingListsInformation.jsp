<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Mailing Lists Information", help = "MailingListsInformation.html";%>
<%@ include file="../include/header.inc" %>


<bean:define name="mailingListsInformationForm" id="mailingListsInformation" type="org.afscme.enterprise.mailinglists.web.MailingListsInformationForm"/>

<!-- Tabs -->
<bean:define id="screen" value="MailingListsInformation"/>
<bean:define id="recordType" value="entity"/>

<% String origin = mailingListsInformation.getOriginate(); %>
<% if (origin.equalsIgnoreCase("organization")) { %>
    <%@ include file="../include/organization_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("affiliate")) { %>
    <%@ include file="../include/affiliate_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("person")) { %>
    <%@ include file="../include/person_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("member")) { %>
    <%@ include file="../include/member_tab.inc" %>
<% } %>


<!-- Header -->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR" valign="top">
			<%=mailingListsInformation.getHeader()%>
			<logic:equal name="mailingListsInformation" property="MLBP" value="true">
				<% recordType = "member"; %>
			</logic:equal>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
				<logic:equal name="mailingListsInformation" property="actingAsAffiliate" value="false">
					<TH align="center" width="10%">
						Select 
					</TH>
				</logic:equal>
					<TH align="left" width="60%">
						Mail Code 
					</TH>
				<logic:equal name="mailingListsInformation" property="MLBP" value="false">
					<TH align="left" width="15%">
						Bulk Count
					</TH>
				</logic:equal>
					<TH align="center" width="15%">
						Send Mail To 
					</TH>
				</TR>

			<logic:present name="mailingListsInformation" property="mailingLists">
			<logic:iterate id="mailingLists" name="mailingListsInformation" property="mailingLists">
			<bean:define id="data" name="mailingLists" type="org.afscme.enterprise.mailinglists.MailingListData"/> 
				<TR>
				<logic:equal name="mailingListsInformation" property="actingAsAffiliate" value="false">
					<TD align="center" class="ContentHeaderTD">
						<afscme:link page='<%="/deleteMailingListsInformation.action?mailingListPk="+data.getMailingListPk()%>' title="Delete from Mailing List" confirm='<%="Do you want to remove this "+recordType+" from this Mailing List?"%>' styleClass="action">Delete</afscme:link>
					</TD>
				</logic:equal>

					<TD class="ContentTD">
						<bean:write name="data" property="mailingListPk"/> - <bean:write name="data" property="mailingListNm"/>
					</TD>

				<% if (!mailingListsInformation.isMLBP())  { %>
					<TD class="ContentTD">
						<bean:write name="data" property="mailingListBulkCount"/>
					</TD>

					<TD class="ContentTD" align="center">
						<afscme:link page='<%="/viewMailingListsAddressAssociation.action?pk="+data.getAddressPk()+"&mailingListPk="+data.getMailingListPk()%>' title="Change Mailing Address Association" styleClass="action">							
							<bean:write name="data" property="locationNm"/>
						</afscme:link> 
					</TD>
				<% } else { %>
				<logic:equal name="mailingListsInformation" property="actingAsAffiliate" value="false">
					<TD class="ContentTD" align="center">
						<afscme:link page='<%="/viewMailingListsAddressAssociation.action?pk="+data.getAddressPk()+"&mailingListPk="+data.getMailingListPk()%>' title="Change Mailing Address Association" styleClass="action">							
							<afscme:codeWrite name="data" property="addressType" codeType="PersonAddressType" format="{1}"/>
						</afscme:link> 
					</TD>
				</logic:equal>

				<logic:equal name="mailingListsInformation" property="actingAsAffiliate" value="true">
					<TD class="ContentTD" align="center">
						<afscme:codeWrite name="data" property="addressType" codeType="PersonAddressType" format="{1}"/>
					</TD>
				</logic:equal>
				<% } %>
				</TR>
			</logic:iterate>
			</logic:present>

				<TR>
					<TD colspan="4">&nbsp;
					</TD>
				</TR>

			<logic:equal name="mailingListsInformation" property="actingAsAffiliate" value="false">
				<TR>
					<TD align="center" class="ContentHeaderTD">
						<afscme:link page="/viewMailingListsInformation.action?add" title='<%="Add "+recordType+" to another Mailing List"%>' styleClass="action">Add</afscme:link> 
					</TD>
					<TD class="ContentTD" colspan="3">
						New Mail Code 
					</TD>
				</TR>
			</logic:equal>

				<TR>
					<TD colspan="4" align="center">
						<html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!-- Footer -->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD class="ContentHeaderTR" valign="bottom">
			<%=mailingListsInformation.getHeader()%>
		</TD>
	</TR>
</TABLE>

<!-- Tabs -->
<% if (origin.equalsIgnoreCase("organization")) { %>
    <%@ include file="../include/organization_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("affiliate")) { %>
    <%@ include file="../include/affiliate_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("person")) { %>
    <%@ include file="../include/person_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("member")) { %>
    <%@ include file="../include/member_tab.inc" %>
<% } %>
<%@ include file="../include/footer.inc" %>
