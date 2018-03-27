<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Political Rebate Annual Rebate Information", help = "PoliticalRebateAnnualRebateInformation.html";%>
<%@ include file="../include/header.inc" %>
<bean:define id="prbForm" name="politicalRebateAnnualRebateInformationForm" type="org.afscme.enterprise.rebate.web.PoliticalRebateAnnualRebateInformationForm"/>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<afscme:button page="/viewPoliticalRebateSummary.action" paramName="prbForm" paramProperty="prbYear" paramId="prbYear">Return</afscme:button>
			<BR><BR>
		</TD>
		<TD align="right">
			<afscme:button page="/editPoliticalRebateAnnualRebateInformation.action" paramName="prbForm" paramProperty="prbYear" paramId="prbYear">Edit</afscme:button>
			<BR><BR>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR">
			<afscme:currentPersonName displayAsHeader="true" />
			<BR>Rebate Year - <%= prbForm.getPrbYear() %><BR><BR>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TH align="left">
			Annual Rebate Status
		</TH>
	</TR>
	<TR valign="top">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="20%" class="ContentHeaderTD"><LABEL for="label_RosterStatus">Roster Status</LABEL></TD>
					<TD width="80%" class="ContentTD"><bean:write name="prbForm" property="prbRosterStatus"/></TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH>Select</TH>
					<TH colspan="4">Dues Paid To</TH>
					<TH>Duration</TH>
					<TH>Filed With</TH>
					<TH>Acceptance Code</TH>
					<TH>Mbr Type</TH>
					<TH>Mbr Status</TH>
					<TH>Dues Type</TH>
					<TH colspan="2">Last Updated</TH>
				</TR>
				<TR>
					<TD>&nbsp;</TD>
					<TH class="small">Type</TH>
					<TH class="small">Loc/Sub Chap</TH>
					<TH class="small">State/Nat'l</TH>
					<TH class="small">CN/Ret Chap</TH>
					<TD colspan="6">&nbsp;</TD>
					<TH class="small">Date</TH>
					<TH class="small">By</TH>
				</TR>

				<logic:present name="prbForm" property="prbDuesPaidList">
				<logic:iterate id="prbDuesPaidList" name="prbForm" property="prbDuesPaidList">
				<bean:define id="prbDuesPaidRec" name="prbDuesPaidList" type="org.afscme.enterprise.person.PRBAffiliateData"/>
				<bean:define id="recData" name="prbDuesPaidRec" property="theRecordData" type="org.afscme.enterprise.common.RecordData"/>
				<bean:define id="affId" name="prbDuesPaidRec" property="theAffiliateIdentifier" type="org.afscme.enterprise.affiliate.AffiliateIdentifier"/>
				<TR>
					<TD align="center">
					<logic:empty name="prbDuesPaidRec" property="rbtCheckNumber">
						<afscme:link page='<%="/viewPoliticalRebateAnnualRebateInformation.action?prbYear="+prbForm.getPrbYear()+"&clear"%>' paramName="prbDuesPaidRec" paramProperty="affPk" paramId="affPk" title="Remove this Affiliate from the Annual Rebate" styleClass="action" confirm="Are you sure you want to clear this Affiliate from Rebate consideration?">Clear</afscme:link>
					</logic:empty>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="affId" property="type"/>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="affId" property="local"/>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="affId" property="state"/>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="affId" property="council"/>
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:codeWrite name="prbDuesPaidRec" codeType="RebateDuration" property="durationPk" format="{1}"/>
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:codeWrite name="prbDuesPaidRec" codeType="RebateFiledWith" property="filedWithPk" format="{1}"/>
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:codeWrite name="prbDuesPaidRec" codeType="RebateAcceptanceCode" property="acceptanceCodePk" format="{1}"/>
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:codeWrite name="prbDuesPaidRec" codeType="RebateMbrType" property="rbtMbrTypePk" format="{1}"/>
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:codeWrite name="prbDuesPaidRec" codeType="RebateMbrStatus" property="rbtMbrStatusPk" format="{1}"/>
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:codeWrite name="prbDuesPaidRec" codeType="DuesType" property="duesTypePk" format="{1}"/>
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:dateWrite name="recData" property="modifiedDate"/>
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:userWrite name="recData" property="modifiedBy"/>
					</TD>
				</TR>
				</logic:iterate>
				</logic:present>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH colspan="7" align="left">Check Information</TH>
				</TR>
				<TR>
					<TH class="small" width="16%">Number</TH>
					<TH class="small" width="12%">Amount</TH>
					<TH class="small" width="16%">Date</TH>
					<TH class="small" width="12%">Returned</TH>
					<TH class="small" width="16%">Second Number</TH>
					<TH class="small" width="12%">Second Amount</TH>
					<TH class="small" width="16%">Second Date</TH>
				</TR>
			        <logic:notEmpty name="prbForm" property="prbCheckInfo">
				<bean:define id="prbCheckInfoRec" name="prbForm" property="prbCheckInfo"/>
				<TR>
					<TD class="ContentTD" align="center">
						<bean:write name="prbCheckInfoRec" property="checkNumber"/>
					</TD>
					<TD class="ContentTD" align="center">
				        <logic:notEmpty name="prbCheckInfoRec" property="amountString">
						$<bean:write name="prbCheckInfoRec" property="amountString"/>
					</logic:notEmpty>						
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:dateWrite name="prbCheckInfoRec" property="date"/>
					</TD>
					<TD class="smallFont" align="center">
						<html:checkbox name="prbCheckInfoRec" property="returnedFlag" disabled="true"/>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="prbCheckInfoRec" property="checkNumber2"/>
					</TD>
					<TD class="ContentTD" align="center">
				        <logic:notEmpty name="prbCheckInfoRec" property="amount2String">
						$<bean:write name="prbCheckInfoRec" property="amount2String"/>
					</logic:notEmpty>						
					</TD>
					<TD class="ContentTD" align="center">
						<afscme:dateWrite name="prbCheckInfoRec" property="date2"/>
					</TD>
				</TR>
				</logic:notEmpty>
				<TR><TD colspan="4">&nbsp;</TD></TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TH align="left">Rebate Challenge Information</TH>
	</TR>
	<TR valign="top">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH>Judicial Panel</TH>
					<TH colspan="3">Supplemental Check</TH>
				</TR>
				<TR>
					<TH class="small">Case Number</TH>
					<TH class="small">Number</TH>
					<TH class="small">Amount</TH>
					<TH class="small">Date</TH>
				</TR>
			        <logic:notEmpty name="prbForm" property="prbCheckInfo">
				<TR>
					<TD align="center" class="ContentTD">
						<bean:write name="prbCheckInfoRec" property="caseNumber"/>
					</TD>
					<TD align="center" class="ContentTD">
						<bean:write name="prbCheckInfoRec" property="supplCheckNumber"/>
					</TD>
					<TD align="center" class="ContentTD">
				        <logic:notEmpty name="prbCheckInfoRec" property="supplAmountString">
						$<bean:write name="prbCheckInfoRec" property="supplAmountString"/>
					</logic:notEmpty>						
					</TD>
					<TD align="center" class="ContentTD">
						<afscme:dateWrite name="prbCheckInfoRec" property="supplDate"/>
					</TD>
				</TR>	
				</logic:notEmpty>
				<TR><TD colspan="4">&nbsp;</TD></TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD colspan="2" class="ContentTD"><LABEL for="label_ChallengeComments">Comment</LABEL></TD>
				</TR>
			        <logic:notEmpty name="prbForm" property="prbCheckInfo">
				<TR>
					<TD colspan="2" class="ContentTD">
						<bean:write name="prbCheckInfoRec" property="comment"/>
					</TD>
				</TR>
				</logic:notEmpty>
			</TABLE>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<afscme:button page="/viewPoliticalRebateSummary.action" paramName="prbForm" paramProperty="prbYear" paramId="prbYear">Return</afscme:button>
		</TD>
		<TD align="right">
			<BR>
			<afscme:button page="/editPoliticalRebateAnnualRebateInformation.action" paramName="prbForm" paramProperty="prbYear" paramId="prbYear">Edit</afscme:button>
			<BR><BR>
		</TD>
	</TR>
</TABLE>
<%@ include file="../include/footer.inc" %>
