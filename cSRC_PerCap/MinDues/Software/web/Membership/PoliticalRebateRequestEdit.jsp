<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<bean:define id="form" name="politicalRebateRequestForm" type="org.afscme.enterprise.rebate.web.PoliticalRebateRequestForm"/>
<%!String title, help;%>
<%title = "Political Rebate " + (form.isEdit() ? "Request Edit" : "Request Add");%>
<%help = "PoliticalRebateRequest" + (form.isEdit() ? "Edit" : "Add") + ".html";%>
<%@ include file="../include/header.inc" %>
<body onLoad="initRebateDenial(politicalRebateRequestForm);">

<html:form action="savePoliticalRebateRequest.action" focus="requestDate">
<html:hidden name="form" property="back"/>
<html:hidden name="form" property="rqstPk"/>
<html:hidden name="form" property="appPk"/>
<html:hidden name="form" property="keyedDate"/>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable">
    <TR>
        <TD align="center"> 
            <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR">
			<afscme:currentPersonName displayAsHeader="true" />
			<BR><BR> 
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR> 
					<TD width="16%" class="ContentHeaderTD"> 
						<LABEL for="label_RebateYear">* Rebate Year</LABEL> 
					</TD>
					<TD width="10%" class="ContentTD"> 
					<% if (form.isPrbYearEditable()) { %>
						<html:text name="form" property="prbYear" size="4" maxlength="4"/> 
					<% } else { %>
						<bean:write name="form" property="prbYear"/> 
						<html:hidden name="form" property="prbYear"/>
					<% } %>
					</TD>
					<TD width="18%" class="ContentHeaderTD">
						<LABEL for="label_KeyedDate">* Request Date</LABEL>
					</TD>
					<TD width="20%" class="ContentTD"> 
                            			<html:text name="form" property="requestDate" size="10" maxlength="10"/>
						<A href="javascript:show_calendar('politicalRebateRequestForm.requestDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
					</TD>
					<TD width="18%" class="ContentHeaderTD"> 
						<LABEL for="label_RebateYear">Certified Mail Number</LABEL>
					</TD>
					<TD width="18%" class="ContentTD"> 
						<html:text name="form" property="certifiedMailNumber" size="20" maxlength="20"/> 
					</TD>
				</TR>
				<TR> 
					<TD class="ContentHeaderTD"> 
						<LABEL for="label_RequestDenied">Request Denied</LABEL> 
					</TD>
					<TD class="smallFont"> 
						<html:checkbox name="form" property="denied" onclick="validateRebateDenial(politicalRebateRequestForm);"/>
					</TD>
					<TD class="ContentHeaderTD"> 
						<LABEL for="label_ReasonDenied">Reason Denied</LABEL> 
					</TD>
					<TD class="ContentTD"> 
						<html:select name="form" property="deniedReason">
							<afscme:codeOptions useCode="false" codeType="RebateReasonDenied" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentHeaderTD"> 
						<LABEL for="label_DateDenied">Date Denied</LABEL> 
					</TD>
					<TD class="smallFont"> 
                            			<html:text name="form" property="deniedDate" size="10" maxlength="10"/>
						<A name="deniedCalendar" href="javascript:show_calendar('politicalRebateRequestForm.deniedDate');" onclick="if (disabled) return false;" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
					</TD>
				</TR>
<% if (form.isEdit()) { %>
				<TR>
					<TD class="ContentHeaderTD">
						Request Re-submitted
					</TD>
					<TD class="smallFont">
						<html:checkbox name="form" property="resubmitted" onclick="validateRebateResubmitDenial(politicalRebateRequestForm);"/>
					</TD>
					<TD class="ContentHeaderTD">
						Re-submission Denied
					</TD>
					<TD class="ContentTD">
						<html:select name="form" property="resubmittedDeniedReason" onchange="validateRebateResubmitDenial(politicalRebateRequestForm);">
							<afscme:codeOptions useCode="false" codeType="RebateReasonDenied" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentHeaderTD">
						Date Denied
					</TD>
					<TD class="ContentTD">
                            			<html:text name="form" property="resubmittedDeniedDate" size="10" maxlength="10"/>
						<A name="resubmittedDeniedCalendar" href="javascript:show_calendar('politicalRebateRequestForm.resubmittedDeniedDate');" onclick="if (disabled) return false;" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
					</TD>
				</TR>
<% } %>
				<TR> 
					<TD class="ContentHeaderTD"> 
						<LABEL for="label_KeyedDate">Keyed Date</LABEL> 
					</TD>
					<TD class="ContentTD" colspan="5">
						<bean:write name="form" property="keyedDate"/> 
					</TD>
				</TR>
				<TR>
					<TD colspan="6" align="center">		
						<html:errors property="prbYear"/>
						<html:errors property="requestDate"/>
						<html:errors property="deniedDate"/>
						<html:errors property="deniedReason"/>
						<html:errors property="resubmittedDeniedDate"/>
						<html:errors property="resubmittedDeniedReason"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH width="50%" colspan="5">Dues Paid To</TH>
					<TH width="15%">Duration</TH>
					<TH width="15%">Filed With</TH>
				</TR>
				<TR>
					<TH class="small">Finder</TH>
					<TH class="small">* Type</TH>
					<TH class="small">* Loc/Sub Chap</TH>
					<TH class="small">* State/Nat'l</TH>
					<TH class="small">* CN/Ret Chap</TH>
					<TD colspan="2">&nbsp;</TD>
				</TR>
			<% for (int i=1; i<=form.getAffCount(); i++) { %>
				<html:hidden name="form" property='<%="affPk_"+i%>'/>
				<html:hidden name="form" property='<%="code_"+i%>'/>
				<html:hidden name="form" property='<%="subUnit_"+i%>'/>
				<TR>
					<TD align="center"><afscme:affiliateFinder formName="politicalRebateRequestForm" affPkParam='<%="affPk_"+i%>' affIdTypeParam='<%="type_"+i%>' affIdCouncilParam='<%="council_"+i%>' affIdLocalParam='<%="local_"+i%>' affIdStateParam='<%="state_"+i%>' affIdSubUnitParam='<%="subUnit_"+i%>' affIdCodeParam='<%="code_"+i%>' styleClass="action"/>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="form" property='<%="type_"+i%>'>
	                                		<afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}" excludeCodes="C, U"/>
						</html:select> 
					</TD>
					<TD class="ContentTD" align="center">
						<html:text name="form" property='<%="local_"+i%>' size="4" maxlength="4"/>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="form" property='<%="state_"+i%>'>
							<afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:text name="form" property='<%="council_"+i%>' size="4" maxlength="4"/>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="form" property='<%="duration_"+i%>'>
							<afscme:codeOptions useCode="false" codeType="RebateDuration" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
					<TD class="ContentTD" align="center">
						<html:select name="form" property='<%="filedWith_"+i%>'>
							<afscme:codeOptions useCode="false" codeType="RebateFiledWith" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
				</TR>
			<% } %>
				<TR>
					<TD colspan="7" align="center">		
						<html:errors property="affPk_1"/>
						<html:errors property="affPk_2"/>
						<html:errors property="affPk_3"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD class="ContentTD">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
<% if (form.isEdit()) { %>
					<TD><LABEL for="label_ApplicationComments">Edit Comment</LABEL></TD>
<% } else { %>
					<TD><LABEL for="label_ApplicationComments">Enter New Comments</LABEL></TD>
<% } %>
				</TR>
				<TR>
					<TD class="ContentTD">
						<html:textarea name="form" property="comment" cols="115" rows="3" onkeyup="validateComments(this);"/>
					</TD>
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
			<BR><BR> 
		</TD>
		<TD align="right">
			<BR>
			<html:reset styleClass="button" onclick="resetRebateDenial(politicalRebateRequestForm);" />
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
</TABLE>
</html:form>
<%@ include file="../include/footer.inc" %>
