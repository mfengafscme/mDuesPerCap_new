<%@ taglib uri	="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Political/Legislative Information Edit", help = "PoliticalLegislativeInformationEdit.html";%>
<%@ include file="../include/header.inc" %>
<html:form action="/savePoliticalData.action" focus="politicalObjectorFg">
	<bean:define id="originValue" name="origin" type="java.lang.String"/> 
	<html:hidden property="origin" value="<%= originValue %>"/>
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
							<logic:equal name="politicalDataForm" property="politicalObjectorEnabled" value="true">	
								<html:checkbox name="politicalDataForm" property="politicalObjectorFg" value="true"/>
							</logic:equal>
							<logic:notEqual name="politicalDataForm" property="politicalObjectorEnabled" value="true">	
								<html:checkbox name="politicalDataForm" property="politicalObjectorFg" value="true" disabled="true"/>
							</logic:notEqual>
						</TD>
						<TD width="15%" class="ContentHeaderTD">
							<LABEL for="label_DONOTCALL">DO NOT CALL</LABEL> 
						</TD>
						<TD width="15%">
							<logic:equal name="politicalDataForm" property="politicalDoNotCallEnabled" value="true">	
								<html:checkbox name="politicalDataForm" property="politicalDoNotCallFg" value="true"/>
							</logic:equal>
							<logic:notEqual name="politicalDataForm" property="politicalDoNotCallEnabled" value="true">	
								<html:checkbox name="politicalDataForm" property="politicalDoNotCallFg" value="true" disabled="true"/>
							</logic:notEqual>
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
	<bean:define id="url" value="/viewPoliticalData.action?origin="/>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
			<TD align="left">
				<BR> 
				<html:submit styleClass="button"/>
				<BR> <BR> 
			</TD>
			<TD align="right">
				<BR> 
				<INPUT type="reset" class="BUTTON" value="Reset" name="ResetButton"> 
				&nbsp;
				<afscme:button action="<%=  url + originValue %>">Cancel</afscme:button> 
			</TD>
		</TR>
	</TABLE>
</html:form>
<%@ include file="../include/footer.inc" %>
