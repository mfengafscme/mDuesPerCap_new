<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Political Rebate Summary", help = "PoliticalRebateSummary.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="prbForm" name="politicalRebateSummaryForm" type="org.afscme.enterprise.rebate.web.PoliticalRebateSummaryForm"/>
<html:hidden name="prbForm" property="prbYear"/>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<afscme:button page="/viewPoliticalRebateSummaryByYear.action">Return</afscme:button>
			<BR><BR>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD class="ContentHeaderTR">
			<afscme:currentPersonName displayAsHeader="true" />
			<BR>Rebate Year - <%= prbForm.getPrbYear() %><BR><BR>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD class="ContentHeaderTR">
			Requests
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH width="10%">Select</TH>
					<TH width="15%">Date of Request</TH>
					<TH width="20%">Application Mailed Date</TH>
					<TH colspan="4">Dues Paid To</TH>
					<TH width="20%">Status</TH>
				</TR>
				<TR>
					<TD COLSPAN="3">&nbsp;</TD>
					<TH class="small">Type</TH>
					<TH class="small">Loc/Sub Chap</TH>
					<TH class="small">State/Nat'l</TH>
					<TH class="small">CN/Ret Chap</TH>
					<TD>&nbsp;</TD>
				</TR>

				<logic:present name="prbForm" property="prbRequestList">
				<logic:iterate id="prbRequestList" name="prbForm" property="prbRequestList">
				<bean:define id="prbRequestRec" name="prbRequestList" type="org.afscme.enterprise.rebate.PRBSummary"/>
				<TR align="center">
					<TD>
						<afscme:link page="/viewPoliticalRebateRequest.action" paramName="prbRequestList" paramProperty="pk" paramId="pk" title="View Political Rebate Request" styleClass="action">View</afscme:link>
					</TD>
					<TD>
						<bean:write name="prbRequestRec" property="rbtRequestDate"/>
					</TD>
					<TD>
						<bean:write name="prbRequestRec" property="rbtMailedDate"/>
					</TD>

					<logic:notEmpty name="prbRequestList" property="affiliateIdentifier">
					<bean:define id="prbRequestAffiliateID" name="prbRequestList" property="affiliateIdentifier"/>
					<TD align="center">
						<bean:write name="prbRequestAffiliateID" property="type"/>
					</TD>
					<TD align="center">
						<bean:write name="prbRequestAffiliateID" property="local"/>
					</TD>
					<TD align="center">
						<bean:write name="prbRequestAffiliateID" property="state"/>
					</TD>
					<TD align="center">
						<bean:write name="prbRequestAffiliateID" property="council"/>
					</TD>
					</logic:notEmpty> 

					<logic:empty name="prbRequestList" property="affiliateIdentifier">
					<TD colspan="4">&nbsp;</TD>
					</logic:empty>

					<TD valign="top">
						<bean:write name="prbRequestRec" property="rbtStatus"/>
					</TD>
				</TR>

				<logic:present name="prbRequestList" property="prbAffiliateData">
				<logic:iterate id="prbRequestAffiliateList" name="prbRequestList" property="prbAffiliateData">
				<bean:define id="prbRequestAffilateRec" name="prbRequestAffiliateList" type="org.afscme.enterprise.person.PRBAffiliateData"/>
				<TR>
					<TD COLSPAN="3">&nbsp;</TD>
					<TD align="center">
						<bean:write name="prbRequestAffilateRec" property="theAffiliateIdentifier.type"/>
					</TD>
					<TD align="center">
						<bean:write name="prbRequestAffilateRec" property="theAffiliateIdentifier.local"/>
					</TD>
					<TD align="center">
						<bean:write name="prbRequestAffilateRec" property="theAffiliateIdentifier.state"/>
					</TD>
					<TD align="center">
						<bean:write name="prbRequestAffilateRec" property="theAffiliateIdentifier.council"/>
					</TD>
					<TD>&nbsp;</TD>
				</TR>
				</logic:iterate>
				</logic:present>

				</logic:iterate>
				</logic:present>
				<TR>
					<TD colspan="8">&nbsp;</TD>
				</TR>
				<TR> 
					<TD align="center">
						<afscme:link page="/editPoliticalRebateRequest.action?back=Summary" paramName="prbForm" paramProperty="prbYear" paramId="prbYear" title="Add Political Rebate Request" styleClass="action">Add</afscme:link>
					</TD>
					<TD COLSPAN="7">New Rebate Request</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD class="ContentHeaderTR">
			Applications
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH width="10%">Select</TH>
					<TH width="20%">Application Mailed Date</TH>
					<TH colspan="4">Dues Paid To</TH>
					<TH width="20%">Status</TH>
				</TR>
				<TR>
					<TD COLSPAN="2">&nbsp;</TD>
					<TH class="small">Type</TH>
					<TH class="small">Loc/Sub Chap</TH>
					<TH class="small">State/Nat'l</TH>
					<TH class="small">CN/Ret Chap</TH>
					<TD>&nbsp;</TD>
				</TR>

				<logic:present name="prbForm" property="prbApplicationList">
				<logic:iterate id="prbAppList" name="prbForm" property="prbApplicationList">
				<bean:define id="prbAppRec" name="prbAppList" type="org.afscme.enterprise.rebate.PRBSummary"/>
				<TR align="center">
					<TD>
						<afscme:link page="/viewPoliticalRebateApplication.action" paramName="prbAppList" paramProperty="pk" paramId="pk" title="View Political Rebate Application" styleClass="action">View</afscme:link>
					</TD>
					<TD>
						<bean:write name="prbAppRec" property="rbtMailedDate"/>
					</TD>

					<logic:notEmpty name="prbAppList" property="affiliateIdentifier">
					<bean:define id="prbAppAffiliateID" name="prbAppList" property="affiliateIdentifier"/>
					<TD align="center">
						<bean:write name="prbAppAffiliateID" property="type"/>
					</TD>
					<TD align="center">
						<bean:write name="prbAppAffiliateID" property="local"/>
					</TD>
					<TD align="center">
						<bean:write name="prbAppAffiliateID" property="state"/>
					</TD>
					<TD align="center">
						<bean:write name="prbAppAffiliateID" property="council"/>
					</TD>
					</logic:notEmpty>

					<logic:empty name="prbAppList" property="affiliateIdentifier">
					<TD colspan="4">&nbsp;</TD>
					</logic:empty>

					<TD valign="top">
						<bean:write name="prbAppRec" property="rbtStatus"/>
					</TD>
				</TR>

				<logic:present name="prbAppList" property="prbAffiliateData">
				<logic:iterate id="prbAppAffiliateList" name="prbAppList" property="prbAffiliateData">
				<bean:define id="prbAppAffilateRec" name="prbAppAffiliateList" type="org.afscme.enterprise.person.PRBAffiliateData"/>
				<TR>
					<TD COLSPAN="2">&nbsp;</TD>
					<TD align="center">
						<bean:write name="prbAppAffilateRec" property="theAffiliateIdentifier.type"/>
					</TD>
					<TD align="center">
						<bean:write name="prbAppAffilateRec" property="theAffiliateIdentifier.local"/>
					</TD>
					<TD align="center">
						<bean:write name="prbAppAffilateRec" property="theAffiliateIdentifier.state"/>
					</TD>
					<TD align="center">
						<bean:write name="prbAppAffilateRec" property="theAffiliateIdentifier.council"/>
					</TD>
					<TD>&nbsp;</TD>
				</TR>
				</logic:iterate>
				</logic:present>

				</logic:iterate>
				</logic:present>
				<TR>
					<TD colspan="7">&nbsp;</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<logic:present name="prbForm" property="prbAnnualInfoStatus">
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD class="ContentHeaderTR">
			Annual Rebate Information
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH width="10%">Select</TH>
					<TH width="25%">Status</TH>
				</TR>
				<TR align="center">
					<TD>
						<afscme:link page="/viewPoliticalRebateAnnualRebateInformation.action" paramName="prbForm" paramProperty="prbYear" paramId="prbYear" title="View Political Rebate Annual Rebate Information" styleClass="action">View</afscme:link>
					<TD>
						<bean:write name="prbForm" property="prbAnnualInfoStatus"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</logic:present>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD class="ContentHeaderTR">
			<BR><afscme:currentPersonName displayAsHeader="true" />
			<BR>Rebate Year - <%= prbForm.getPrbYear() %>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<afscme:button page="/viewPoliticalRebateSummaryByYear.action">Return</afscme:button>
			<BR><BR>
		</TD>
	</TR>
</TABLE>
<%@ include file="../include/footer.inc" %>
