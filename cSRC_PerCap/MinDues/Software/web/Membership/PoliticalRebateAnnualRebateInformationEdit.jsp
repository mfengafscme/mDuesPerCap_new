<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Political Rebate Annual Rebate Information Edit", help = "PoliticalRebateAnnualRebateInformationEdit.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="prbEditForm" name="politicalRebateAnnualRebateInformationEditForm" type="org.afscme.enterprise.rebate.web.PoliticalRebateAnnualRebateInformationEditForm"/>
<html:form action="savePoliticalRebateAnnualRebateInformation.action" focus="local_1">
<html:hidden property="prbYear"/>
<html:hidden property="actingAsAffiliate"/>
<html:hidden property="edit"/>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<html:submit styleClass="button"/>
			<BR><BR>
		</TD>
		<TD align="right">
			<BR>
			<html:reset styleClass="button"/>
			<html:cancel styleClass="button"/>
		</TD>
	</TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR">
			<afscme:currentPersonName displayAsHeader="true" />
			<BR>Rebate Year - <%= prbEditForm.getPrbYear() %><BR><BR>
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
					<TD width="80%" class="ContentTD"><bean:write name="prbEditForm" property="prbRosterStatus"/></TD>
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
					<TH>* Duration</TH>
					<TH>* Filed With</TH>
					<TH>Acceptance Code</TH>
					<TH>Mbr Type</TH>
					<TH>Mbr Status</TH>
					<TH>Dues Type</TH>
				</TR>
				<TR>
					<TD>&nbsp;</TD>
					<TH class="small">* Type</TH>
					<TH class="small">* Loc/Sub Chap</TH>
					<TH class="small">* State/Nat'l</TH>
					<TH class="small">* CN/Ret Chap</TH>
					<TD colspan="6">&nbsp;</TD>
				</TR>
			<% for (int i=1; i<=prbEditForm.getAffCount(); i++) { %>
				<html:hidden name="prbEditForm" property='<%="affPk_"+i%>'/>
				<html:hidden name="prbEditForm" property='<%="code_"+i%>'/>
				<html:hidden name="prbEditForm" property='<%="subUnit_"+i%>'/>
				<TR>				
				<logic:equal name="prbEditForm" property="actingAsAffiliate" value="false">					
					<TD align="center"><afscme:affiliateFinder formName="politicalRebateAnnualRebateInformationEditForm" affPkParam='<%="affPk_"+i%>' affIdTypeParam='<%="type_"+i%>' affIdCouncilParam='<%="council_"+i%>' affIdLocalParam='<%="local_"+i%>' affIdStateParam='<%="state_"+i%>' affIdSubUnitParam='<%="subUnit_"+i%>' affIdCodeParam='<%="code_"+i%>' styleClass="action"/></TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="type_"+i%>'>
	                                		<afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}" excludeCodes="C, U"/>
						</html:select> 
					</TD>
					<TD class="ContentTD" align="center">
						<html:text name="prbEditForm" property='<%="local_"+i%>' size="4" maxlength="4"/>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="state_"+i%>'>
							<afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:text name="prbEditForm" property='<%="council_"+i%>' size="4" maxlength="4"/>
					</TD>
				</logic:equal>
				
				<logic:notEqual name="prbEditForm" property="actingAsAffiliate" value="false">					
					<TD align="center">&nbsp</TD>
					<TD class="ContentTD" align="center">													
						<bean:write name="prbEditForm" property='<%="type_"+i%>' />
						<html:hidden name="prbEditForm" property='<%="type_"+i%>'/>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="prbEditForm" property='<%="local_"+i%>' />
						<html:hidden name="prbEditForm" property='<%="local_"+i%>' />
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="prbEditForm" property='<%="state_"+i%>' />								
						<html:hidden name="prbEditForm" property='<%="state_"+i%>' />
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="prbEditForm" property='<%="council_"+i%>' />					
						<html:hidden name="prbEditForm" property='<%="council_"+i%>' />
					</TD>
				</logic:notEqual>

				<logic:notEqual name="prbEditForm" property='<%="affPk_"+i%>' value="" >		
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="duration_"+i%>'>
							<afscme:codeOptions useCode="false" codeType="RebateDuration" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="filedWith_"+i%>'>
							<afscme:codeOptions useCode="false" codeType="RebateFiledWith" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="acceptanceCode_"+i%>'>
							<afscme:codeOptions useCode="false" codeType="RebateAcceptanceCode" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="mbrType_"+i%>'>
							<afscme:codeOptions useCode="false" codeType="RebateMbrType" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="mbrStatus_"+i%>'>
							<afscme:codeOptions useCode="false" codeType="RebateMbrStatus" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="duesType_"+i%>'>
							<afscme:codeOptions useCode="false" codeType="DuesType" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
				</logic:notEqual>
				
			        <logic:equal name="prbEditForm" property='<%="affPk_"+i%>' value="" >		
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="duration_"+i%>' disabled="true">							
							<afscme:codeOptions useCode="false" codeType="RebateDuration" allowNull="true" nullDisplay="" format="{1}"/>						
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="filedWith_"+i%>' disabled="true">
							<afscme:codeOptions useCode="false" codeType="RebateFiledWith" allowNull="true" nullDisplay="" format="{1}"/>						
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="acceptanceCode_"+i%>' disabled="true">
							<afscme:codeOptions useCode="false" codeType="RebateAcceptanceCode" allowNull="true" nullDisplay="" format="{1}"/>						
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="mbrType_"+i%>' disabled="true">
							<afscme:codeOptions useCode="false" codeType="RebateMbrType" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="mbrStatus_"+i%>' disabled="true">
							<afscme:codeOptions useCode="false" codeType="RebateMbrStatus" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="prbEditForm" property='<%="duesType_"+i%>' disabled="true">
							<afscme:codeOptions useCode="false" codeType="DuesType" allowNull="true" nullDisplay="" format="{1}"/>						
						</html:select>
					</TD>
				</logic:equal>
				</TR>
			<% } %>
			<TR>
				<TD colspan="11" align="center">		
					<html:errors property="affPk_1"/>
					<html:errors property="code_1"/>
					<html:errors property="duration_1"/>
					<html:errors property="filedWith_1"/>
					<html:errors property="acceptanceCode_1"/>
					<html:errors property="mbrType_1"/>
					<html:errors property="mbrStatus_1"/>
					<html:errors property="duesType_1"/>
					<html:errors property="affPk_2"/>
					<html:errors property="duration_2"/>
					<html:errors property="filedWith_2"/>
					<html:errors property="acceptanceCode_2"/>
					<html:errors property="mbrType_2"/>
					<html:errors property="mbrStatus_2"/>
					<html:errors property="duesType_2"/>
					<html:errors property="affPk_3"/>
					<html:errors property="duration_3"/>
					<html:errors property="filedWith_3"/>
					<html:errors property="acceptanceCode_3"/>
					<html:errors property="mbrType_3"/>
					<html:errors property="mbrStatus_3"/>
					<html:errors property="duesType_3"/>
					<html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
				</TD>
			</TR>
			</TABLE>
		</TD>
	</TR>

<% if (prbEditForm.isActingAsAffiliate()) { %>
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
				<TR>
					<TD class="ContentTD" align="center">
						<bean:write name="prbEditForm" property="checkNumber"/> 
					</TD>
					<TD class="ContentTD" align="center">
				        <logic:notEmpty name="prbEditForm" property="amount">
						$<bean:write name="prbEditForm" property="amount"/>
					</logic:notEmpty>
					</TD>
					<TD class="ContentTD" align="center">
                            			<bean:write name="prbEditForm" property="date"/>
					</TD>
					<TD class="smallFont" align="center">
						<html:checkbox name="prbEditForm" property="returnedFlag" disabled="true"/>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="prbEditForm" property="checkNumber2"/> 
					</TD>
					<TD class="ContentTD" align="center">
				        <logic:notEmpty name="prbEditForm" property="amount2">
						$<bean:write name="prbEditForm" property="amount2"/>
					</logic:notEmpty>
					</TD>
					<TD class="ContentTD" align="center">
                            			<bean:write name="prbEditForm" property="date2"/>
					</TD>
				</TR>
				<TR><TD colspan="7">&nbsp;</TD></TR>
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
				<TR>
					<TD align="center" class="ContentTD">
						<bean:write name="prbEditForm" property="caseNumber"/> 
					</TD>
					<TD align="center" class="ContentTD">
						<bean:write name="prbEditForm" property="supplCheckNumber"/> 
					</TD>
					<TD align="center" class="ContentTD">
				        <logic:notEmpty name="prbEditForm" property="supplAmount">
						$<bean:write name="prbEditForm" property="supplAmount"/>
					</logic:notEmpty>
					</TD>
					<TD align="center" class="ContentTD">
                            			<bean:write name="prbEditForm" property="supplDate"/>
					</TD>
				</TR>	
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
				<TR>
					<TD colspan="2" class="ContentTD">
						<bean:write name="prbEditForm" property="comment"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>

<% } else { %>
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
				<TR>
					<TD class="ContentTD" align="center">
						<html:text name="prbEditForm" property="checkNumber" size="6" maxlength="6"/> 
					</TD>
					<TD class="ContentTD" align="center">
						$<html:text name="prbEditForm" property="amount" size="6" maxlength="6"/>
					</TD>
					<TD class="ContentTD" align="center">
                            			<html:text name="prbEditForm" property="date" size="10" maxlength="10"/>
						<A href="javascript:show_calendar('politicalRebateAnnualRebateInformationEditForm.date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
					</TD>
					<TD class="smallFont" align="center">
						<html:checkbox name="prbEditForm" property="returnedFlag"/>
					</TD>
					<TD class="ContentTD" align="center">
						<html:text name="prbEditForm" property="checkNumber2" size="6" maxlength="6"/> 
					</TD>
					<TD class="ContentTD" align="center">
						$<html:text name="prbEditForm" property="amount2" size="6" maxlength="6"/>
					</TD>
					<TD class="ContentTD" align="center">
                            			<html:text name="prbEditForm" property="date2" size="10" maxlength="10"/>
						<A href="javascript:show_calendar('politicalRebateAnnualRebateInformationEditForm.date2');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
					</TD>
				</TR>
				<TR><TD colspan="7">&nbsp;</TD></TR>
				<TR>
					<TD colspan="7" align="center">
		                            <html:errors property="checkNumber"/>
		                            <html:errors property="amount"/>
		                            <html:errors property="date"/>
		                            <html:errors property="checkNumber2"/>
		                            <html:errors property="amount2"/>
		                            <html:errors property="date2"/>
					</TD>
				</TR>
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
				<TR>
					<TD align="center" class="ContentTD">
						<html:text name="prbEditForm" property="caseNumber" size="6" maxlength="6"/> 
					</TD>
					<TD align="center" class="ContentTD">
						<html:text name="prbEditForm" property="supplCheckNumber" size="6" maxlength="6"/> 
					</TD>
					<TD align="center" class="ContentTD">
						$<html:text name="prbEditForm" property="supplAmount" size="6" maxlength="6"/>
					</TD>
					<TD align="center" class="ContentTD">
                            			<html:text name="prbEditForm" property="supplDate" size="10" maxlength="10"/>
						<A href="javascript:show_calendar('politicalRebateAnnualRebateInformationEditForm.supplDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
					</TD>
				</TR>	
				<TR><TD colspan="4">&nbsp;</TD></TR>
				<TR>
					<TD colspan="4" align="center">
		                            <html:errors property="caseNumber"/>
		                            <html:errors property="supplCheckNumber"/>
		                            <html:errors property="supplAmount"/>
		                            <html:errors property="supplDate"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD colspan="2" class="ContentTD"><LABEL for="label_ChallengeComments">Edit Comment</LABEL></TD>
				</TR>
				<TR>
					<TD colspan="2" class="ContentTD">
						<html:textarea name="prbEditForm" property="comment" cols="115" rows="3" onkeyup="validateComments(this);"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
<% } %>

</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<html:submit styleClass="button"/>
			<BR>
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
		<td align="center" class="smallFontI">
			* Note: Member Type, Member Status, and Dues Type fields are required if 
			<BR>the value of Acceptance Code is Local Accepted or Council Accepted. 
		</td>
	</TR>
</TABLE>
</html:form>
<%@ include file="../include/footer.inc" %>
