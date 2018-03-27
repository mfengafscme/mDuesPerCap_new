<%@ taglib uri	="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Political/Legislative Information", help = "PoliticalLegislativeInformation.html";%>
<%@ include file="../include/header.inc" %>
<!-- Tabs. -->
<bean:define id="screen" value="PoliticalLegislativeInformation"/>
<bean:define id="recordType" value="entity"/>
<% String origin = (String)request.getAttribute("origin"); %>
<% if (origin.equalsIgnoreCase("person")) { %>
    <%@ include file="../include/person_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("member")) { %>
    <%@ include file="../include/member_tab.inc" %>
<% } %>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR">
			<afscme:currentPersonName displayAsHeader="true"/> <BR> <BR> 
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="15%" class="ContentHeaderTD">
						<LABEL for="label_PoliticalObjector">Political Objector</LABEL> 
					</TD>
					<TD width="15%">
						<html:checkbox name="politicalData" property="politicalObjectorFg" disabled="true"/> 
					</TD>
					<TD width="15%" class="ContentHeaderTD">
						<LABEL for="label_DONOTCALL">DO NOT CALL</LABEL> 
					</TD>
					<TD width="15%">
						<html:checkbox name="politicalData" property="politicalDoNotCallFg" disabled="true"/> 
					</TD>
					<TD width="15%" class="ContentHeaderTD">
						<LABEL for="label_PACContributor">PAC Contributor</LABEL> 
					</TD>
					<TD width="20%">
						<html:checkbox name="politicalData" property="pacContributorFg" disabled="true"/> 
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">
						<LABEL for="label_RegisteredVoter"> Registered Voter</LABEL> 
					</TD>
					<TD >
						<afscme:codeWrite name="politicalData" property="politicalRegisteredVoter" codeType="RegisteredVoter" format="{1}"/>
					</TD>
					<TD class="ContentHeaderTD">
						<LABEL for="label_PoliticalParty">Political Party</LABEL> 
					</TD>
					<TD colspan="3">
						<afscme:codeWrite name="politicalData" property="politicalParty" codeType="PoliticalParty" format="{1}"/> 
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">
						<LABEL for="label_CongressionalDistrict"> Congressional District</LABEL> 
					</TD>
					<TD>
						<bean:write name="politicalData" property="congDist"/>
					</TD>
					<TD class="ContentHeaderTD">
						<LABEL for="label_StateSenateDistrict"> State Senate District</LABEL> 
					</TD>
					<TD>
						<bean:write name="politicalData" property="upperDist"/>
					</TD>
					<TD class="ContentHeaderTD">
						<LABEL for="label_StateHouseDistrict">State House District</LABEL> 
					</TD>
					<TD>
						<bean:write name="politicalData" property="lowerDist"/>
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">
						<LABEL for="label_WardNumber">Ward Number</LABEL> 
					</TD>
					<TD>
						<bean:write name="politicalData" property="wardNumber"/>
					</TD>
					<TD class="ContentHeaderTD">
						<LABEL for="label_PrecinctNumber">Precinct Number</LABEL> 
					</TD>
					<TD>
						<bean:write name="politicalData" property="precinctNumber"/>
					</TD>
					<TD class="ContentHeaderTD">
						<LABEL for="label_PrecinctNumber">Precinct Name</LABEL> 
					</TD>
					<TD>
						<bean:write name="politicalData" property="precinctName"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
<!-- Tabs. -->
<% if (origin.equalsIgnoreCase("person")) { %>
    <%@ include file="../include/person_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("member")) { %>
    <%@ include file="../include/member_tab.inc" %>
<% } %>
<%@ include file="../include/footer.inc" %>
