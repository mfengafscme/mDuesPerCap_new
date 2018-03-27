<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@ page import="org.afscme.enterprise.returnedmail.*" %>
<%!String title = "Process Returned Mail Summary", help = "ProcessReturnedMailSummary.html";%>
<%@ include file="../include/header.inc" %>
<SCRIPT language="JavaScript" src="../js/membership.js"></SCRIPT>

<bean:define id="form" name="processReturnedMailForm" type="org.afscme.enterprise.returnedmail.web.ProcessReturnedMailForm"/>
	<TABLE cellpadding="0" cellspacing="0" class="ContentTable" align="center">
		<TR valign="top"> 
			<TD align="right">
				<afscme:button page="/processReturnedMailSummary.action?done">Done</afscme:button><BR><BR>
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR valign="top"> 
			<TD class="ContentHeaderTR"> Returned Mail Process Complete </TD>
		</TR>
	</TABLE>
        <bean:define name="form" property="summary" id="element" type="org.afscme.enterprise.returnedmail.ReturnedMailSummary"/>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR valign="top"> 
			<TH>Number of Addresses Attempted to be Flagged as Bad</TH>
			<TH>Number of Addresses Successfully Flagged as Bad</TH>
			<TH>Number of Exceptions</TH>
		</TR>
		<TR> 
			<TD class="ContentTD" align="center"><bean:write name="element" property="attemptedCount"/></TD>
			<TD class="ContentTD" align="center"><bean:write name="element" property="successfulCount"/></TD>
			<TD class="ContentTD" align="center"><bean:write name="element" property="exceptionCount"/></TD>
		</TR>
	</TABLE>

	<logic:equal name="form" property="exceptionFlag" value="true">
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR> 
			<TH align="left" colspan="8">Summary of Exceptions</TH>
		</TR>
		<TR> 
			<TH width="5%">Select</TH>
			<TH width="5%">Reason</TH>
			<TH align="left">Name</TH>
			<TH width="10%">Address </TH>
			<TH width="6%">System</TH>
			<TH width="10%">Address ID </TH>
			<TH width="10%">Last Updated </TH>
			<TH width="10%">Updated By </TH>
		</TR>
 
		<!-- Person Exception List -->
		<logic:iterate id="pExceptionList" name="form" property="personExceptionList">
		<bean:define id="pExceptionAddress" name="pExceptionList" type="org.afscme.enterprise.returnedmail.ReturnedPersonAddress"/> 
		<bean:define id="pExceptionRecordData" name="pExceptionAddress" property="theRecordData" type="org.afscme.enterprise.common.RecordData"/> 
		<TR> 
			<TD align="center" class="ContentTD"><afscme:link page="/editPersonAddress.action?back=ProcessReturnedMail" styleClass="action" paramName="pExceptionAddress" paramProperty="theRecordData.pk" paramId="addrPk" title="Edit this Address">Edit</afscme:link></td>
			<TD align="center" class="ContentTD"><bean:write name="pExceptionAddress" property="exceptionReason"/></TD>
			<TD align="left" class="ContentTD"><bean:write name="pExceptionAddress" property="lastNm"/> <bean:write name="pExceptionAddress" property="suffixNm"/>, <bean:write name="pExceptionAddress" property="firstNm"/> <bean:write name="pExceptionAddress" property="middleNm"/></TD>
			<TD align="center" class="ContentTD"><afscme:codeWrite name="pExceptionAddress" property="type" codeType="PersonAddressType" format="{1}"/></TD>
			<TD align="center" class="ContentTD"><html:checkbox name="pExceptionAddress" property="smaFlag" disabled="true"/></TD>
			<TD align="center" class="ContentTD"><bean:write name="pExceptionAddress" property="addressPK"/></TD>
			<TD align="center" class="ContentTD"><afscme:dateWrite name="pExceptionRecordData" property="modifiedDate"/></TD>
			<TD align="center" class="ContentTD"><afscme:userWrite name="pExceptionRecordData" property="modifiedBy"/></TD>
		</TR>		
               	</logic:iterate>

		<!-- Organization Exception List -->
		<logic:iterate id="oExceptionList" name="form" property="orgExceptionList">
		<bean:define id="oExceptionAddress" name="oExceptionList" type="org.afscme.enterprise.returnedmail.ReturnedOrganizationAddress"/> 
		<bean:define id="oExceptionRecordData" name="oExceptionList" property="theRecordData" type="org.afscme.enterprise.common.RecordData"/> 
		<TR> 
			<TD align="center" class="ContentTD"><afscme:link page="/editLocation.action?back=ProcessReturnedMail" styleClass="action" paramName="oExceptionAddress" paramProperty="theRecordData.pk" paramId="pk" title="Edit this Address">Edit</afscme:link></td>
			<TD align="center" class="ContentTD"><bean:write name="oExceptionAddress" property="exceptionReason"/></TD>
			<TD align="left" class="ContentTD"><bean:write name="oExceptionAddress" property="affAbbreviatedName"/></TD>
			<TD align="center" class="ContentTD"><bean:write name="oExceptionAddress" property="addrTypeDescr"/></TD>
			<TD align="center" class="ContentTD">&nbsp;</TD>
			<TD align="center" class="ContentTD"><bean:write name="oExceptionAddress" property="addressPK"/></TD>
			<TD align="center" class="ContentTD"><afscme:dateWrite name="oExceptionRecordData" property="modifiedDate"/></TD>
			<TD align="center" class="ContentTD"><afscme:userWrite name="oExceptionRecordData" property="modifiedBy"/></TD>
		</TR>		
               	</logic:iterate>

		<!-- Invalid Address Exception List -->
		<logic:iterate id="invalidList" name="form" property="invalidAddressList" type="java.util.Map.Entry">
		<TR> 
			<TD align="center" class="ContentTD">&nbsp;</td>
			<TD align="center" class="ContentTD">Invalid</TD>
			<TD align="left" class="ContentTD">&nbsp;</TD>
			<TD align="center" class="ContentTD"><bean:write name="invalidList" property="value"/></TD>
			<TD align="center" class="ContentTD">&nbsp;</TD>
			<TD align="center" class="ContentTD">&nbsp;</TD>
			<TD align="center" class="ContentTD">&nbsp;</TD>
			<TD align="center" class="ContentTD">&nbsp;</TD>
		</TR>		
               	</logic:iterate>
	</TABLE>
	</logic:equal>


	<logic:equal name="form" property="successfulFlag" value="true">
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<logic:present name="form" property="personSuccessfulList">
		<TR> 
			<TH align="left" colspan="8"> Summary of Addresses Flagged as Bad </TH>
		</TR>
		<TR> 
			<TH width="5%"> Select </TH>
			<TH align="left" colspan="2">Name</TH>
			<TH width="10%">Address</TH>
			<TH width="6%">System</TH>
			<TH width="10%">Address ID </TH>
			<TH width="10%">Last Updated </TH>
			<TH width="10%">Updated By </TH>
		</TR>
		</logic:present>

		<!-- Person Successful List -->
		<logic:iterate id="pSuccessfulList" name="form" property="personSuccessfulList">
		<bean:define id="pSuccessfulAddress" name="pSuccessfulList" type="org.afscme.enterprise.returnedmail.ReturnedPersonAddress"/> 
		<bean:define id="pSuccessfulRecordData" name="pSuccessfulAddress" property="theRecordData" type="org.afscme.enterprise.common.RecordData"/> 
		<TR> 
			<TD align="center" class="ContentTD"><afscme:link page="/editPersonAddress.action?back=ProcessReturnedMail" styleClass="action" paramName="pSuccessfulAddress" paramProperty="theRecordData.pk" paramId="addrPk" title="Edit this Address">Edit</afscme:link></td>
			<TD align="left" class="ContentTD" colspan="2"><bean:write name="pSuccessfulAddress" property="lastNm"/> <bean:write name="pSuccessfulAddress" property="suffixNm"/>, <bean:write name="pSuccessfulAddress" property="firstNm"/> <bean:write name="pSuccessfulAddress" property="middleNm"/></TD>
			<TD align="center" class="ContentTD"><afscme:codeWrite name="pSuccessfulAddress" property="type" codeType="PersonAddressType" format="{1}"/></TD>
			<TD align="center" class="ContentTD"><html:checkbox name="pSuccessfulAddress" property="smaFlag" disabled="true"/></TD>
			<TD align="center" class="ContentTD"><bean:write name="pSuccessfulAddress" property="addressPK"/></TD>
			<TD align="center" class="ContentTD"><afscme:dateWrite name="pSuccessfulRecordData" property="modifiedDate"/></TD>
			<TD align="center" class="ContentTD"><afscme:userWrite name="pSuccessfulRecordData" property="modifiedBy"/></TD>
		</TR>		
               	</logic:iterate>

		<!-- Organization Successful List -->
		<logic:iterate id="oSuccessfulList" name="form" property="orgSuccessfulList">
		<bean:define id="oSuccessfulAddress" name="oSuccessfulList" type="org.afscme.enterprise.returnedmail.ReturnedOrganizationAddress"/> 
		<bean:define id="oSuccessfulRecordData" name="oSuccessfulList" property="theRecordData" type="org.afscme.enterprise.common.RecordData"/> 
		<TR> 
			<TD align="center" class="ContentTD"><afscme:link page="/editLocation.action?back=ProcessReturnedMail" styleClass="action" paramName="oSuccessfulAddress" paramProperty="theRecordData.pk" paramId="pk" title="Edit this Address">Edit</afscme:link></td>
			<TD align="left" class="ContentTD" colspan="2"><bean:write name="oSuccessfulAddress" property="affAbbreviatedName"/></TD>
			<TD align="center" class="ContentTD"><bean:write name="oSuccessfulAddress" property="addrTypeDescr"/></TD>
			<TD align="center" class="ContentTD">&nbsp;</TD>
			<TD align="center" class="ContentTD"><bean:write name="oSuccessfulAddress" property="addressPK"/></TD>
			<TD align="center" class="ContentTD"><afscme:dateWrite name="oSuccessfulRecordData" property="modifiedDate"/></TD>
			<TD align="center" class="ContentTD"><afscme:userWrite name="oSuccessfulRecordData" property="modifiedBy"/></TD>
		</TR>		
               	</logic:iterate>
	</TABLE>
	</logic:equal>

	<TABLE cellpadding="0" cellspacing="0" class="ContentTable" align="center">
		<TR valign="top"> 
			<TD align="right">
				<BR><afscme:button page="/processReturnedMailSummary.action?done">Done</afscme:button><BR>
			</TD>
		</TR>
	</TABLE>

<%@ include file="../include/footer.inc" %>
