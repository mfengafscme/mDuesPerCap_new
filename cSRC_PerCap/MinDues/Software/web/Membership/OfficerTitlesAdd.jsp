<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Officer Titles Add", help = "OfficerTitlesAdd.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="addOfficerTitleForm" type="org.afscme.enterprise.affiliate.officer.web.AddOfficerTitleForm"/>

<% String imageType = "top"; %>
<% String screen = "OfficerTitlesAdd"; %>
<% String left = null; %>
<% String right = null; %>
<html:form action="/addOfficerTitle" focus="afscmeTitle">
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR valign="top">
			<TD class="ContentHeaderTR">
				<afscme:currentAffiliate />
                                <BR> <BR> 
			</TD>
		</TR>
	</TABLE>
	<TABLE align="center" cellpadding="0" cellspacing="0" border="1" class="BodyContent">
		<TR>
			<TD colspan="8" class="ContentHeaderTR">
				New Officer Title 
			</TD>
		</TR>
	</TABLE>
	<TABLE align="center" cellpadding="0" cellspacing="0" border="1" class="BodyContent">
		<TR>
			<TH width="20%">* Constitutional Title</TH>
			<TH width="20%">Affiliate Title</TH>
			<TH width="6%">* # with Title</TH>
			<TH width="7%">* Month of Election</TH>
			<TH width="7%">* Length of Term</TH>
			<TH width="8%">Term End</TH>
			<TH width="7%">Delegate Priority</TH>
			<TH width="2%">RO</TH>
			<TH width="4%">E-Bd</TH>
		</TR>
		<TR>
			<TD align="center" class="ContentTD">
		            <html:select property="afscmeTitle" name="addOfficerTitleForm" onchange="validateOnePresident(addOfficerTitleForm);checkSteward(addOfficerTitleForm);">
                                <afscme:codeOptions useCode="true" codeType="AFSCMETitle" allowNull="true" nullDisplay="" format="{1}"/>
                    </html:select>
			</TD>
			<TD align="center" class="ContentTD">
			    <html:text name="form" property="affiliateTitle" size="30" maxlength="30"/>
			</TD>
			<TD align="center" class="smallFONT">
			    <html:text name="form" property="numWithTitle" size="2" maxlength="3" onchange="validateReportingOfficer(addOfficerTitleForm);"/>
			</TD>
			<TD align="center" class="ContentTD">
			    <html:select property="monthOfElection" name="form">
                     <afscme:codeOptions useCode="false" codeType="MonthOffcrElection" allowNull="true" nullDisplay="" format="{1}"/>
                </html:select>
			</TD>
			<TD align="center" class="ContentTD">
			    <html:select property="lengthOfTerm" name="form" onchange="indefiniteOrTemporaryTerm(addOfficerTitleForm);checkSteward(addOfficerTitleForm);">
                                <afscme:codeOptions useCode="false" codeType="TermLength" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
			</TD>
			<TD align="center">
			    <html:text name="form" property="termEnd" value="" size="4" maxlength="4" onblur="indefiniteOrTemporaryTerm(addOfficerTitleForm);checkSteward(addOfficerTitleForm);" onchange="indefiniteOrTemporaryTerm(addOfficerTitleForm);checkSteward(addOfficerTitleForm);"/>
			</TD>
			<TD align="center" class="ContentTD">
			    <html:text name="form" property="delegatePriority" value="" size="2" maxlength="2"/>
			</TD>
			<TD align="center">
			    <html:checkbox name="form" property="reportingOfficer" />
			</TD>
			<TD align="center">
			    <html:checkbox name="form" property="execBoard" />
			</TD>
		</TR>
                <!-- Display validation errors -->		
		<TR>
			<TD align="middle"><html:errors property="afscmeTitle"/></TD>
                        <TD></TD>
			<TD align="middle"><html:errors property="numWithTitle"/></TD>
			<TD align="middle"><html:errors property="monthOfElection"/></TD>
		        <TD align="middle"><html:errors property="lengthOfTerm"/></TD>
                        <TD align="middle"><html:errors property="termEnd"/></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                </TR>	
        </TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
			<TD align="left">
				<BR>
				<html:submit styleClass="button"/>  
				<BR><BR> 
			</TD>
			<TD valign="middle" align="center" class="DataFontI">
				Note: Setting this Officer Title as the Reporting Officer will override the 
				Officer Title previously selected. 
			</TD>
			<TD align="right">
				<BR>
				<html:reset styleClass="button"/>  
	            <html:cancel styleClass="button"/>  

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
		<TR>
			<TD valign="TOP" align="center">
				
			</TD>
		</TR>
	</TABLE>
<BR>
</html:form>
<!-- Display AFSCME Footer
<%@ include file="../include/footer.inc" %> 