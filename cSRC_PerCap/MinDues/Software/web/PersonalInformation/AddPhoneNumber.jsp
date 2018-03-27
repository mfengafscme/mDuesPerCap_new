<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "My Personal Information Add Phone Number", help = "MyPersonalInformationAddPhoneNumber.html";%>
<%@ include file="../include/header.inc" %>
<html:form action="savePhoneNumberMyInfo">
	<TABLE cellpadding="0" cellspacing="0" border="0" class="BodyContent" align="center">
		<TR valign="top">
			<TD class="ContentHeaderTR">
				<afscme:currentPersonName displayAsHeader="true" showPk="true"/> <BR> <BR> 
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="BodyContent" align="center">
		<TR valign="top">
			<TH width="20%">
				Primary 
			</TH>
			<TH width="20%">
				* Type 
			</TH>
			<TH width="20%">
				Country Code 
			</TH>
			<TH width="20%">
				Area Code 
			</TH>
			<TH width="25%">
				* Number 
			</TH>
		</TR>
		<TR>
			<TD align="middle" class="ContentTD">
				<html:checkbox property="phonePrmryFg"/> 
			</TD>
			<TD align="middle" class="ContentTD">
				<html:select property="phoneType">
					<afscme:codeOptions codeType="PhoneType" format="{1}" allowNull="false"/>
				</html:select>
			</TD>
			<TD align="middle" class="ContentTD">
				<html:text property="countryCode" size ="5" maxlength="5"/>
			</TD>
			<TD align="middle" class="ContentTD">
				<html:text property="areaCode" size ="3" maxlength="3"/> 
			</TD>
			<TD align="middle" class="ContentTD">
				<html:text property="phoneNumber" size ="15" maxlength="15"/> 
			</TD>
		</TR>
		<!-- Display validation errors -->		
		<TR>
			<TD align="middle"><html:errors property="phonePrmryFg"/></TD>
			<TD align="middle"><html:errors property="phoneType"/></TD>
			<TD></TD>
			<TD align="middle"><html:errors property="areaCode"/></TD>
		        <TD align="middle"><html:errors property="phoneNumber"/></TD>
                </TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
			<TD>
				<BR>
				<html:submit styleClass="button"/>  
				<BR><BR> 
			</TD>
			<TD align="right">
				<BR>
				<INPUT name="ResetButton" type="reset" class="BUTTON" value="Reset" tabindex="8"> 
				<afscme:button action="/viewMyInfo.action">Cancel</afscme:button> 
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
