<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Export Participation Codes", help = "ExportParticipationCodes.html";%>
<%@ include file="../include/header.inc" %>
<bean:define id="form" name="exportParticipationCodesForm" type="org.afscme.enterprise.participationgroups.web.ExportParticipationCodesForm"/>

<html:form action="saveExportParticipationCodes">
<html:hidden name="form" property="groupPk"/>
<html:hidden name="form" property="typePk"/>
<html:hidden name="form" property="detailPk"/>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TH class="large" align="left">
			Save File in one of the following formats: 
		</TH>
	</TR>
	<TR valign="top">
		<TD>
			<TABLE border="0" class="InnerContentTable">
				<TR>
					<TD>
						<html:radio property="outputFormat" value="<%=String.valueOf(form.TAB)%>" styleId="optionTab"/>
					</TD>
					<TD>
						<LABEL for="optionTab">Tab Delimited</LABEL> 
					</TD>
				</TR>
				<TR>
					<TD>
						<html:radio property="outputFormat" value="<%=String.valueOf(form.COMMA)%>" styleId="optionComma"/>
					</TD>
					<TD>
						<LABEL for="optionComma">Comma Delimited</LABEL> 
					</TD>
				</TR>
				<TR>
					<TD>
						<html:radio property="outputFormat" value="<%=String.valueOf(form.SEMICOLON)%>" styleId="optionSemiColon"/>
					</TD>
					<TD>
						<LABEL for="optionSemicolon">Semicolon Delimited</LABEL> 
					</TD>
				</TR>
				<TR>
					<TD colspan="2">
			                	<html:errors property="outputFormat"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR><html:submit styleClass="button"/>
		</TD>
		<TD align="right">
			<BR>
			<BR><html:cancel styleClass="button"/>
			<BR><BR> 
		</TD>
	</TR>
</TABLE>
</html:form>
<%@ include file="../include/footer.inc" %>
