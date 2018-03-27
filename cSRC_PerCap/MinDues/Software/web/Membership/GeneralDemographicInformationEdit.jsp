<%@ taglib uri	="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "General Demographic Information Edit", help = "GeneralDemographicEdit.html";%>
<%@ include file="../include/header.inc" %>
<center><html:errors/></center>
<html:form action="/saveDemographicData.action" focus="dob">
	<bean:define id="originValue" name="origin" type="java.lang.String"/>
	<bean:define id="url" value="/viewDemographicData.action?origin="/>
	<html:hidden property="origin" value="<%= originValue %>"/>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
			<TD align="left">
				<BR> 
				<html:submit styleClass="button"/>  
				<BR> <BR>
			</TD>
			<TD align="right">
				<BR> <INPUT name="ResetButton" type="reset" class="BUTTON" value="Reset">
				&nbsp;
				<afscme:button action="<%=  url + originValue %>">Cancel</afscme:button>  
			</TD>
		</TR>
	</TABLE>
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
							<logic:notEmpty property="dob" name="demographicDataForm">
								<html:text name="demographicDataForm" property="dob" size="20" maxlength="10"/>
							</logic:notEmpty>
							
							<logic:empty property="dob" name="demographicDataForm">
								<html:text name="demographicDataForm" property="dob" value="" size="20" maxlength="10"/>
							</logic:empty>
							<A href="javascript:show_calendar('demographicDataForm.dob');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_deceased"> Deceased </LABEL> 
						</TD>
						<TD>
							<html:checkbox name="demographicDataForm" property="deceasedFg"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_deceasedDate"> Deceased Date </LABEL> 
						</TD>
						<TD>
							<logic:notEmpty property="deceasedDt" name="demographicDataForm">
								<html:text name="demographicDataForm" property="deceasedDt" size="20" maxlength="10"/> 
							</logic:notEmpty>
							<logic:empty property="deceasedDt" name="demographicDataForm">
								<html:text name="demographicDataForm" property="deceasedDt" value=""/>
							</logic:empty>
							<A href="javascript:show_calendar('demographicDataForm.deceasedDt');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"> 
							<IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_gender"> Gender </LABEL> 
						</TD>
						<TD>
							<html:radio name="demographicDataForm" property="genderCodePK" value="<%= org.afscme.enterprise.codes.Codes.Gender.MALE.toString() %>"/>
							Male 
							<html:radio name="demographicDataForm" property="genderCodePK" value="<%= org.afscme.enterprise.codes.Codes.Gender.FEMALE.toString() %>"/>
							Female 
							<html:radio name="demographicDataForm" property="genderCodePK" value="0"/>
							Unknown 
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_ethnicOrigin"> Ethnic Origin </LABEL> 
						</TD>
						<TD>
							<html:select name="demographicDataForm" property="ethnicOriginCodePK">
								<afscme:codeOptions codeType="EthnicOrigin" allowNull="true" nullDisplay="[Select]" format="{1}"/>
							</html:select>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_citizenship"> Citizenship</LABEL> 
						</TD>
						<TD>
							<html:select name="demographicDataForm" property="citizenshipCodePK">
								<afscme:codeOptions codeType="CountryCitizenship" allowNull="true" nullDisplay="[Select]"/>
							</html:select>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_maritalStatus"> Marital Status </LABEL> 
						</TD>
						<TD>
							<html:select name="demographicDataForm" property="maritalStatusCodePK">
								<afscme:codeOptions codeType="MaritalStatus" allowNull="true" nullDisplay="[Select]" format="{1}"/>
							</html:select> 
						</TD>
					</TR>
				</TABLE>
			</TD>
			<TD>
				<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD WIDTH="35%" class="ContentHeaderTD">
							<LABEL for="label_DisabilityCode">Disabilities<BR>
							(multiple select)</LABEL> 
						</TD>
						<TD>
							<html:select name="demographicDataForm" property="disabilityCodePKs" size="3" multiple="true">
								<afscme:codeOptions codeType="Disabilities" allowNull="true" nullDisplay="[Select]" format="{1}"/>
							</html:select> 
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_Accommodation">Accommodation<BR>
							(multiple select)</LABEL> 
						</TD>
						<TD>
							<html:select name="demographicDataForm" property="disabilityAccommodationCodePKs" size="3" multiple="true">
								<afscme:codeOptions codeType="Accomodations" allowNull="true" nullDisplay="[Select]" format="{1}"/>
							</html:select> 
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_PrimaryLanguage"> Primary Language </LABEL> 
						</TD>
						<TD>
							<html:select name="demographicDataForm" property="primaryLanguageCodePK">
								<afscme:codeOptions codeType="Languages" format="{1}"/>
							</html:select> 
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_OtherLanguages"> Other Languages<BR>
							(multiple select) </LABEL> 
						</TD>
						<TD>
							<html:select name="demographicDataForm" property="otherLanguageCodePKs" size="3" multiple="true">
								<afscme:codeOptions codeType="Languages" allowNull="true" nullDisplay="[Select]" format="{1}"/>
							</html:select> 
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_religion"> Religion </LABEL> 
						</TD>
						<TD>
							<html:select name="demographicDataForm" property="religionCodePK">
								<afscme:codeOptions codeType="Religion" allowNull="true" nullDisplay="[Select]" format="{1}"/>
							</html:select>  
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TH align="left" colspan="2">
				Spouse/Domestic Partner Name 
			</TH>
		</TR>
		<TR align="center">
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
						<logic:notEmpty name="demographicDataForm" property="partnerPk">
							<html:hidden name="demographicDataForm" property="partnerPk"/>
						</logic:notEmpty>
						<TD align="center" class="ContentTD">
							<logic:notEmpty name="demographicDataForm" property="partnerFirstName">
								<html:text name="demographicDataForm" property="partnerFirstName" size="25" maxlength="25"/> 
							</logic:notEmpty>
							<logic:empty name="demographicDataForm" property="partnerFirstName">
								<html:text name="demographicDataForm" property="partnerFirstName" value="" size="25" maxlength="25"/> 
							</logic:empty>
						</TD>
						<TD align="center" class="ContentTD">
							<logic:notEmpty name="demographicDataForm" property="partnerMiddleName">
								<html:text name="demographicDataForm" property="partnerMiddleName" size="20" maxlength="10"/>
							</logic:notEmpty>
							<logic:empty name="demographicDataForm" property="partnerMiddleName">
								<html:text name="demographicDataForm" property="partnerMiddleName" value="" size="20" maxlength="10"/>
							</logic:empty>
						</TD>
						<TD align="center" class="ContentTD">
							<logic:notEmpty name="demographicDataForm" property="partnerLastName">
								<html:text name="demographicDataForm" property="partnerLastName" size="20" maxlength="10"/>
							</logic:notEmpty>
							<logic:empty name="demographicDataForm" property="partnerLastName">
								<html:text name="demographicDataForm" property="partnerLastName" value="" size="20" maxlength="10"/>
							</logic:empty>
						</TD>
						<TD align="center" class="ContentTD">
							<html:select name="demographicDataForm" property="partnerSuffixName">
								<afscme:codeOptions codeType="Suffix" allowNull="true" nullDisplay="[Select]" format="{1}"/>
							</html:select> 
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TH align="left" colspan="2">
				Children Information 
			</TH>
		</TR>
		<TR align="center">
			<TD colspan="2">
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TH>
							First Name 
						</TH>
						<TH>
							Middle Name 
						</TH>
						<TH>
							Last Name 
						</TH>
						<TH>
							Suffix 
						</TH>
						<TH>
							Birth Date 
						</TH>
					</TR>
					
					<logic:notEmpty name="demographicDataForm" property="childrenFirstNames">
						<% int count = -1; %>
						<bean:define id="childrenMiddleNms" property="childrenMiddleNames" name="demographicDataForm" type="java.lang.String[]"/>
						<bean:define id="childrenLastNms" property="childrenLastNames" name="demographicDataForm" type="java.lang.String[]"/>
						<bean:define id="childrenSuffixNms" property="childrenSuffixNames" name="demographicDataForm" type="java.lang.Integer[]"/>
						<bean:define id="childrenBirthDts" property="childrenBirthDates" name="demographicDataForm" type="java.lang.String[]"/>
						<bean:define id="childPks" property="childrenPks" name="demographicDataForm" type="java.lang.Integer[]"/>

						<logic:iterate id="childrenFirstNm" name="demographicDataForm" property="childrenFirstNames">
							<% count++; %>
							<html:hidden name="demographicDataForm" property="childrenPks" value="<%= childPks[count].toString() %>"/>
							<TR>
								<TD align="center">
									<html:text property="childrenFirstNames" value="<%= childrenFirstNm.toString() %>" size="25" maxlength="25"/>
								</TD>
								<TD align="center">
									<% if(childrenMiddleNms[count] != null) { %>
										<html:text name="demographicDataForm" property="childrenMiddleNames" value="<%= childrenMiddleNms[count] %>" size="25" maxlength="25"/>
									<% } else { %>
										<html:text name="demographicDataForm" property="childrenMiddleNames" value="" size="25" maxlength="25"/>
									<% } %>
								</TD>
								<TD align="center">
									<html:text name="demographicDataForm" property="childrenLastNames" value="<%= childrenLastNms[count] %>" size="25" maxlength="25"/>
								</TD>
								<TD align="center">
									<html:select name="demographicDataForm" property="childrenSuffixNames" value="<%= childrenSuffixNms[count].toString() %>">
										<afscme:codeOptions codeType="Suffix" allowNull="true" nullDisplay="[Select]" format="{1}"/>
									</html:select> 
								</TD>
								<TD align="center">
									<% if(!org.afscme.enterprise.util.TextUtil.isEmpty(childrenBirthDts[count])) { %>
										<html:text name="demographicDataForm" property="childrenBirthDates" value="<%= childrenBirthDts[count] %>" size="10" maxlength="10"/>
									<% } else { %>
										<html:text name="demographicDataForm" property="childrenBirthDates" value="" size="10" maxlength="10"/>
									<% } %>
									<A href="javascript:show_calendar('demographicDataForm.childrenBirthDates[<%= count %>]');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
								</TD>
							</TR>
						</logic:iterate>
					</logic:notEmpty>
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
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
			<TD align="left">
				<BR> 
					<html:submit styleClass="button"/> 
				<BR>
			</TD>
			<TD align="right">
				<BR> <INPUT name="reset2" type="reset" class="BUTTON" value="Reset">
				&nbsp;
				<afscme:button action="<%=  url + originValue %>">Cancel</afscme:button>  
			</TD>
		</TR>
	</TABLE>
</html:form>
<%@ include file="../include/footer.inc" %>
