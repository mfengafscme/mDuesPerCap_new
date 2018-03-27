<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "General Demographic Information Add Child", help = "GeneralDemographicInformationAddChild.html";%>
<%@ include file="../include/header.inc" %>
<html:form action="saveChild" focus="relativeFirstNm">
	<bean:define id="origin" name="origin" type="java.lang.String"/>
	<html:hidden property="origin" value="<%= origin %>"/>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
					<TR valign="top">
						<TD class="ContentHeaderTR">
							<afscme:currentPersonName displayAsHeader="true"/><BR> <BR> 
						</TD>
					</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR align="center">
			<TD>
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TH>
							* First Name 
						</TH>
						<TH>
							Middle Name 
						</TH>
						<TH>
							* Last Name 
						</TH>
						<TH>
							Suffix 
						</TH>
						<TH>
							Birth Date 
						</TH>
					</TR>
					<TR>
						<TD align="center">
							<html:text property="relativeFirstNm"  size="25" maxlength="25"/>
						</TD>
						<TD align="center">
							<html:text property="relativeMiddleNm"  size="25" maxlength="25"/> 
						</TD>
						<TD align="center">
							 <html:text property="relativeLastNm"  size="25" maxlength="25"/> 
						</TD>
						<TD align="center">
							<html:select property="relativeSuffixNm">
								<afscme:codeOptions codeType="Suffix" allowNull="true" nullDisplay="[Select]" format="{1}"/>
							</html:select> 
						</TD>
						<TD align="center">
							<html:text property="relativeBirthDt" size="10" maxlength="10"/> 
							<A href="javascript:show_calendar('relationDataForm.relativeBirthDt');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
						</TD>
					</TR>
					<!-- Display validation errors -->		
					<TR>
						<TD align="center"><html:errors property="firstName"/></TD>
						<TD></TD>
						<TD align="center"><html:errors property="lastName"/></TD>
					        <TD></TD>
					        <TD align="center"><html:errors property="birthDate"/><html:errors property="year"/></TD>
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
				<BR> <BR>
			</TD>
			<TD align="right">
				<% String action = "viewDemographicData.action?origin=" + origin; %>
				<BR> <INPUT name="reset" type="reset" class="BUTTON" value="Reset"> 
				&nbsp;
				<afscme:button action="<%= action %>">Cancel</afscme:button> 
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
