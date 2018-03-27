<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<bean:define name="personAddressForm" id="form" type="org.afscme.enterprise.address.web.PersonAddressForm"/>
<%!String title, help;%>
<%title = "Address Maintenance " + (form.isAdd() ? "Add" : "Edit");%>
<%help = "AddressMaintenance" + (form.isAdd() ? "Add" : "Edit") + ".html";%>
<%@ include file="../include/header.inc" %>

<center><html:errors/></center>
<html:form action="savePersonAddress">

    <html:hidden property="addrPk"/>
    <html:hidden property="back"/>

	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR valign="top">
			<TD class="ContentHeaderTR">
				<afscme:currentPersonName/> <BR> <BR> 
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR valign="top">
			<TD>
				<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TH width="20%">
							Primary
						</TH>
						<TH width="20%">
							* Code
						</TH>
						<TH width="20%">
							Private
						</TH>
						<TH width="20%">
							Bad
						</TH>
						<TH width="20%">
							Date Marked Bad
						</TH>
					</TR>
					<TR>
						<TD class="ContentTD" align="center">
                            <html:checkbox property="primary" disabled="<%=(!form.isAdd() && form.isPrimary() && form.getType().equals(org.afscme.enterprise.codes.Codes.PersonAddressType.HOME) )%>"/>
                        <% 
                            if (!form.isAdd() && form.isPrimary() && form.getType().equals(org.afscme.enterprise.codes.Codes.PersonAddressType.HOME) ) { %>
                            <html:hidden property="primary" />
                            <%}%>
						</TD>
						<TD class="ContentTD" align="center">
							<html:select property="type">
                                <afscme:codeOptions codeType="PersonAddressType" />
							</html:select>
							<% if (!form.isAdd()) { %>
							<html:hidden property="oldType" value="<%=form.getType().toString()%>" />
							<%}%>
						</TD>
						<TD class="ContentTD" align="center">
                            <html:checkbox property="private"/>
						</TD>
						<TD class="ContentTD" align="center">
                            <html:checkbox property="bad" disabled="<%=form.isAdd()%>"/>
						</TD>
						<TD class="ContentTD" align="center">
                            <afscme:dateWrite property="badDate" writeHidden="true"/>
						</TD>
					</TR>
					<TR>
						<TD colspan="5">
							<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
								<TR>
									<TD class="ContentHeaderTD">
										<LABEL for="addr1">Address 1</LABEL> 
									</TD>
									<TD class="ContentTD">
                                        <html:text property="addr1" size="50" maxlength="50" />
									</TD>
									<TD class="ContentHeaderTD">
										<LABEL for="addr2">Address 2</LABEL> 
									</TD>
									<TD class="ContentTD" colspan="3">
                                        <html:text property="addr2" size="50" maxlength="50" />
									</TD>
								</TR>
								<TR>
									<TD width="10%" class="ContentHeaderTD">
										<LABEL for="city">* City</LABEL>
									</TD>
									<TD width="35%">
                                        <html:text property="city" size="25" maxlength="25"/>
									</TD>
									<TD width="10%" class="ContentHeaderTD">
										<LABEL for="state">State</LABEL>
									</TD>
									<TD width="20%">
										<html:select property="state">
                                            <afscme:codeOptions useCode="true" codeType="State" allowNull="true" format="{0}"/>
										</html:select>
									</TD>
									<TD width="10%" class="ContentHeaderTD">
										<LABEL for="zipCode">Zip/Postal Code</LABEL>
									</TD>
									<TD width="15%">
										<html:text property="zipCode" size="5" maxlength="12" onkeyup="return autoTab(this, 12, event);"/>
										- 
										<html:text property="zipPlus" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>
									</TD>
								</TR>
								<TR>
									<TD class="ContentHeaderTD">
										<LABEL for="county">County</LABEL> 
									</TD>
									<TD class="ContentTD">
										<html:text property="county" size="25" />
									</TD>
									<TD class="ContentHeaderTD">
										<LABEL for="province">Province</LABEL> 
									</TD>
									<TD class="ContentTD">
										<html:text property="province" size="25" />
									</TD>
									<TD class="ContentHeaderTD">
										<LABEL for="countryPk">* Country</LABEL> 
									</TD>
									<TD class="ContentTD">
										<html:select property="countryPk">
                                            <afscme:codeOptions codeType="Country"/>
										</html:select> 
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD colspan="5">
							<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
								<TR>
									<TD colspan="8">
										<HR>
									</TD>
								</TR>
								<TR>
									<TD class="ContentHeaderTD">
										Date Created
									</TD>
									<TD class="ContentTD">
                                        <afscme:dateWrite property="recordData.createdDate" writeHidden="true"/>
									</TD>
									<TD class="ContentHeaderTD">
										<LABEL for="recordData.createdBy">Created By</LABEL>
									</TD>
									<TD class="ContentTD">
                                        <afscme:userWrite property="recordData.createdBy" writeHidden="true"/>
									</TD>
									<TD class="ContentHeaderTD">
										<LABEL for="recordDate.modifiedDate">Last Updated</LABEL>
									</TD>
									<TD class="ContentTD">
                                        <afscme:dateWrite property="recordData.modifiedDate" writeHidden="true"/> 
									</TD>
									<TD class="ContentHeaderTD">
										<LABEL for="recordData.createdBy">Updated By</LABEL>
									</TD>
									<TD class="ContentTD">
                                        <afscme:userWrite property="recordData.modifiedBy" writeHidden="true"/>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
            <td align="left">
                <br><html:submit styleClass="button"/>
            </td>
            <td align="right">
                <br><html:reset styleClass="button"/>
                <html:cancel styleClass="button"/>
            </td>
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
