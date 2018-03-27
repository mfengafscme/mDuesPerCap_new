<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Political Rebate Summary By Year", help = "PoliticalRebateSummaryByYear.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="prbForm" name="politicalRebateSummaryByYearForm" type="org.afscme.enterprise.rebate.web.PoliticalRebateSummaryByYearForm"/>

<!-- Something for tabs. -->
<bean:define id="screen" value="PoliticalRebateList"/>
<% if (prbForm.originateFromPerson()) { %>
    <%@ include file="../include/person_tab.inc" %>
<% } else  { %>
    <%@ include file="../include/member_tab.inc" %>
<% } %>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR> 
		<TD class="ContentHeaderTR">
			<afscme:currentPersonName displayAsHeader="true" />
			<BR><BR>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR> 
		<TD> 
			<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR> 
					<TH width="10%">Select </TH>
					<TH width="10%">Rebate Year </TH>
					<TH width="25%">Status </TH>
				</TR>

				<logic:present name="prbForm" property="prbSummaryByYear">
				<logic:iterate id="prb" name="prbForm" property="prbSummaryByYear">
				<bean:define id="prbSummaryRec" name="prb" type="org.afscme.enterprise.rebate.PRBSummaryByYear"/>
				<TR align="center"> 
					<TD><afscme:link page="/viewPoliticalRebateSummary.action" paramName="prbSummaryRec" paramProperty="prbYear" paramId="prbYear" title="View Political Rebate Information" styleClass="action">View</afscme:link></TD>
					<TD><bean:write name="prbSummaryRec" property="prbYear"/></TD>
					<TD><bean:write name="prbSummaryRec" property="prbStatus"/></TD>
				</TR>
				</logic:iterate>
				</logic:present>

				<TR align="center">
					<TD colspan="3">&nbsp;</TD>
				</TR>

				<TR> 
					<TD align="center"><afscme:link page="/editPoliticalRebateRequest.action?back=SummaryByYear" title="Add Political Rebate Request" styleClass="action">Add</afscme:link></TD>
					<TD COLSPAN="2">New Rebate Year/New Rebate Request</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR> 
		<TD class="ContentHeaderTR"> 
			<BR><afscme:currentPersonName displayAsHeader="true" />
		</TD>
	</TR>
</TABLE>

<!-- Something for tabs. -->
<% if (prbForm.originateFromPerson()) { %>
    <%@ include file="../include/person_tab.inc" %>
<% } else  { %>
    <%@ include file="../include/member_tab.inc" %>
<% } %>

<%@ include file="../include/footer.inc" %>
