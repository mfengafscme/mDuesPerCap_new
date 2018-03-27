<%@ taglib uri	="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "General Demographic Information", help = "GeneralDemographicInformation.html";%>
<%@ include file="../include/header.inc" %>

<!-- Tabs. -->
<bean:define id="screen" value="GeneralDemographicInformation"/>
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
			<TD width="45%">
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD width="30%" class="ContentHeaderTD">
							<LABEL for="label_BirthDate"> Birth Date</LABEL> 
						</TD>
						<TD>
							<afscme:dateWrite name="demographicData" property="dob"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_deceased"> Deceased </LABEL> 
						</TD>
						<TD>
							<html:checkbox name="demographicData" property="deceasedFg" disabled="true"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_deceasedDate"> Deceased Date </LABEL> 
						</TD>
						<TD>
							<afscme:dateWrite name="demographicData" property="deceasedDt"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_gender"> Gender </LABEL> 
						</TD>
						<TD>
							<afscme:codeWrite name="demographicData" property="genderCodePK" codeType="Gender" format="{1}"/> 
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_ethnicOrigin"> Ethnic Origin </LABEL> 
						</TD>
						<TD>
							<afscme:codeWrite name="demographicData" property="ethnicOriginCodePK" codeType="EthnicOrigin" format="{1}"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_citizenship"> Citizenship</LABEL> 
						</TD>
						<TD>
							<afscme:codeWrite name="demographicData" property="citizenshipCodePK" codeType="CountryCitizenship" format="{1}"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_maritalStatus"> Marital Status </LABEL> 
						</TD>
						<TD>
							<afscme:codeWrite name="demographicData" property="maritalStatusCodePK" codeType="MaritalStatus" format="{1}"/>
						</TD>
					</TR>
				</TABLE>
			</TD>
			<TD>
				<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD WIDTH="35%" class="ContentHeaderTD">
							<LABEL for="label_DisabilityCode">Disabilities</LABEL> 
						</TD>
						<TD>
							<% int count = 0; %>
							<logic:iterate id="disabilityCodePK" name="demographicData" property="disabilityCodePKs">
								
								<% int disabilityCodePKSize = ((org.afscme.enterprise.person.DemographicData)request.getAttribute("demographicData")).getDisabilityCodePKs().size(); %>
								<afscme:codeWrite name="disabilityCodePK" codeType="Disabilities" format="{1}"/>
								<% if(disabilityCodePKSize != (++count)) { %>
								,
								<%}%>
							</logic:iterate>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_Accommodation">Accommodation</LABEL> 
						</TD>
						<TD>
							<% count = 0; %>
							<logic:iterate id="disabilityAccommodationCodePK" name="demographicData" property="disabilityAccommodationCodePKs">
								<% int disabilityAccommodationCodePKSize  = ((org.afscme.enterprise.person.DemographicData)request.getAttribute("demographicData")).getDisabilityAccommodationCodePKs().size();%>
								<afscme:codeWrite name="disabilityAccommodationCodePK" codeType="Accomodations" format="{1}"/>
								<% if(disabilityAccommodationCodePKSize != (++count)) { %>
								,
								<%}%>
							</logic:iterate>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_PrimaryLanguage"> Primary Language </LABEL> 
						</TD>
						<TD>
							<afscme:codeWrite name="demographicData" property="primaryLanguageCodePK" codeType="Languages" format="{1}"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_OtherLanguages"> Other Languages<BR>
							(multiple select) </LABEL> 
						</TD>
						<TD>
							<% count = 0; %>
							<% int otherLanguageCodePKSize = ((org.afscme.enterprise.person.DemographicData)request.getAttribute("demographicData")).getOtherLanguageCodePKs().size(); %>
							<logic:iterate id="otherLanguageCodePK" name="demographicData" property="otherLanguageCodePKs">
								<afscme:codeWrite name="otherLanguageCodePK" codeType="Languages" format="{1}"/>
								<% if(otherLanguageCodePKSize != (++count)) { %>
								,
								<%}%>
							</logic:iterate>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_religion"> Religion </LABEL> 
						</TD>
						<TD>
							<afscme:codeWrite name="demographicData" property="religionCodePK" codeType="Religion" format="{1}"/>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TH align="left" colspan="2">
				Spouse/Domestic Partner Name 
			</TH>
		</TR>
		<TR>
			<TD colspan="2">
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TH width="28%">
							First Name 
						</TH>
						<TH width="28%">
							Middle Name 
						</TH>
						<TH width="28%">
							Last Name 
						</TH>
						<TH width="16%">
							Suffix 
						</TH>
					</TR>
					<TR>
						<TD align="center" class="ContentTD">
							<logic:notEmpty name="demographicData" property="thePartnerRelationData">
								<bean:write name="demographicData" property="thePartnerRelationData.relativeFirstNm"/> 
							</logic:notEmpty>
						</TD>
						<TD align="center" class="ContentTD">
							<logic:notEmpty name="demographicData" property="thePartnerRelationData">
								<bean:write name="demographicData" property="thePartnerRelationData.relativeMiddleNm"/> 
							</logic:notEmpty>
						</TD>
						<TD align="center" class="ContentTD">
							<logic:notEmpty name="demographicData" property="thePartnerRelationData">
								<bean:write name="demographicData" property="thePartnerRelationData.relativeLastNm"/>  
							</logic:notEmpty>
						</TD>
						<TD align="center" class="ContentTD">
							<logic:notEmpty name="demographicData" property="thePartnerRelationData"> 
								<afscme:codeWrite name="demographicData" property="thePartnerRelationData.relativeSuffixNm" codeType="Suffix" format="{1}"/>
							</logic:notEmpty>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TH align="left" colspan="2">
				Children Information 
			</TH>
		</TR>
		<TR align="center">
			<TD colspan="2">
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TH width="6%">
							Select 
						</TH>
						<TH width="24%">
							First Name 
						</TH>
						<TH width="24%">
							Middle Name 
						</TH>
						<TH width="24%">
							Last Name 
						</TH>
						<TH width="6%">
							Suffix 
						</TH>
						<TH width="10%">
							Birth Date 
						</TH>
					</TR>
					<logic:iterate id="childrenRelationData" name="demographicData" property="theChildrenRelationData">
						<TR>
							<bean:define id="relativePk" name="childrenRelationData" property="relativePk"/>
							<% String action = "/deleteChild.action?origin=" + origin + "&relativePk=" + relativePk; %>
							<TD class="ContentHeaderTD" align="center">
								<afscme:link page="<%= action %>" styleClass="action" confirm="Are you sure you want to delete this Child?">
									Delete
								</afscme:link>
							</TD>
							<html:hidden name="childrenRelationData" property="relativePk"/>
							<TD align="center">
								<bean:write name="childrenRelationData" property="relativeFirstNm"/> 
							</TD>
							<TD align="center">&nbsp;
								<bean:write name="childrenRelationData" property="relativeMiddleNm"/>
							</TD>
							<TD align="center">
								<bean:write name="childrenRelationData" property="relativeLastNm"/>
							</TD>
							<TD align="center">
								<afscme:codeWrite name="childrenRelationData" property="relativeSuffixNm" codeType="Suffix" format="{1}"/>
							</TD>
							<TD align="center">
								<afscme:dateWrite name="childrenRelationData" property="relativeBirthDt"/> 
							</TD>
						</TR>
					</logic:iterate>
					<TR>
						<TD colspan="6" align="center">&nbsp;
						</TD>
					</TR>
					<TR>
						<% String action = "/addChild.action?origin=" + origin; %>
						<TD class="ContentHeaderTD" width="6%" align="center">
							<afscme:link page="<%= action %>" styleClass="action">
								Add
							</afscme:link> 
						</TD>
						<TD class="ContentTD" colspan=5>
							&nbsp;&nbsp;&nbsp;New Child 
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR align="center">
			<TD>
				<TABLE width="100%" cellpadding="2" cellspacing="0" border="0" class="InnerContentTable">
					<TR>
						<TD class="ContentHeaderTR">
							<BR>
							<afscme:currentPersonName displayAsHeader="true"/> 
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
<% if (origin.equalsIgnoreCase("person")) { %>
    <%@ include file="../include/person_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("member")) { %>
    <%@ include file="../include/member_tab.inc" %>
<% } %>
<%@ include file="../include/footer.inc" %>
