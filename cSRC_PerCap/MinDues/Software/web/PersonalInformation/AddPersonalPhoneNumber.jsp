<%--
This page is not implemented yet, it will be implemented as part of Membership.  It was
placed here to demostrade the privileges for this screen.
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Add Personal Phone Number", help = "AddPersonalPhoneNumber.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="/savePersonalPhoneNumber.action">
    <html:hidden property="personPk"/>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TD class="ContentTD" colspan="3">
				<LABEL for="label_MbrNumber">Member Number</LABEL> &nbsp;&nbsp;&nbsp;10000004 <BR> <BR> 
			</TD>
		</TR>
		<TR>
			<TD width="50%">
				<TABLE cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH class="small">
							Primary 
						</TH>
						<TH width="18%" class="small">
							Type 
						</TH>
						<TH width="19%" class="small">
							Intl Code 
						</TH>
						<TH width="19%" class="small">
							Area Code 
						</TH>
						<TH width="24%" class="small">
							Number 
						</TH>
					</TR>
					<TR align="center">
						<TD class="ContentTD" align="center">
							<INPUT type="checkbox" name="HomePhoneBad"> 
						</TD>
						<TD class="ContentTD">
							<html:select property="phoneType"> 
                                                        <afscme:codeOptions codeType="PhoneType"/>
                                                        </html:select>
						</TD>
						<TD class="ContentTD">
                                                    <html:text property="countryCode" name="personalPhoneNumberForm"/>
						</TD>
						<TD class="ContentTD">
                                                    <html:text property="areaCode" name="personalPhoneNumberForm"/> 
						</TD>
						<TD class="ContentTD">
                                                    <html:text property="phoneNumber" name="personalPhoneNumberForm"/> 
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR>
			<TD>
				<BR> <html:submit styleClass="BUTTON"/> 
			</TD>
			<TD align="right">
				<BR> <html:reset styleClass="BUTTON"/> 
                                <html:cancel styleClass="BUTTON"/> 
			</TD>
		</TR>
	</TABLE>
	<!-- End Action Bar and footer -->
</html:form>

<%@ include file="../include/footer.inc" %>

