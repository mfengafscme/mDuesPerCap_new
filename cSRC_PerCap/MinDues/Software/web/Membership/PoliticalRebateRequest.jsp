<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title="Political Rebate Request", help="PoliticalRebateRequest.html";%>
<%@ include file="../include/header.inc" %>
<bean:define id="form" name="politicalRebateRequestForm" type="org.afscme.enterprise.rebate.web.PoliticalRebateRequestForm"/>
<html:hidden name="form" property="rqstPk"/>
<html:hidden name="form" property="appPk"/>

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
						<LABEL for="label_RebateYear">Rebate Year</LABEL> 
					</TD>
					<TD width="10%" class="ContentTD"> 
						<bean:write name="form" property="prbYear"/> 
					</TD>
					<TD width="18%" class="ContentHeaderTD">
						<LABEL for="label_KeyedDate">Request Date</LABEL>
					</TD>
					<TD width="20%" class="ContentTD"> 
						<bean:write name="form" property="requestDate"/> 
					</TD>
					<TD width="18%" class="ContentHeaderTD"> 
						<LABEL for="label_RebateYear">Certified Mail Number</LABEL>
					</TD>
					<TD width="18%" class="ContentTD"> 
						<bean:write name="form" property="certifiedMailNumber"/> 
					</TD>
				</TR>
				<TR> 
					<TD class="ContentHeaderTD"> 
						<LABEL for="label_RequestDenied">Request Denied</LABEL> 
					</TD>
					<TD class="smallFont"> 
						<html:checkbox name="form" property="denied" disabled="true"/>
					</TD>
					<TD class="ContentHeaderTD"> 
						<LABEL for="label_ReasonDenied">Reason Denied</LABEL> 
					</TD>
					<TD class="ContentTD"> 
                                		<afscme:codeWrite name="form" codeType="RebateReasonDenied" property="deniedReason" format="{1}"/>
					</TD>
					<TD class="ContentHeaderTD"> 
						<LABEL for="label_DateDenied">Date Denied</LABEL> 
					</TD>
					<TD class="ContentTD"> 
						<bean:write name="form" property="deniedDate"/> 
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">
						Request Re-submitted
					</TD>
					<TD class="smallFont">
						<html:checkbox name="form" property="resubmitted" disabled="true"/>
					</TD>
					<TD class="ContentHeaderTD">
						Re-submission Denied
					</TD>
					<TD class="ContentTD">
                                		<afscme:codeWrite name="form" codeType="RebateReasonDenied" property="resubmittedDeniedReason" format="{1}"/>
					</TD>
					<TD class="ContentHeaderTD">
						Date Denied
					</TD>
					<TD class="ContentTD">
						<bean:write name="form" property="resubmittedDeniedDate"/> 
					</TD>
				</TR>
				<TR> 
					<TD class="ContentHeaderTD"> 
						<LABEL for="label_KeyedDate">Keyed Date</LABEL> 
					</TD>
					<TD class="ContentTD" colspan="5">
						<bean:write name="form" property="keyedDate"/> 
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
					<TH class="small">Select</TH>
					<TH class="small">Type</TH>
					<TH class="small">Loc/Sub Chap</TH>
					<TH class="small">State/Nat'l</TH>
					<TH class="small">CN/Ret Chap</TH>
					<TD colspan="2">&nbsp;</TD>
				</TR>
			<% for (int i=1; i<=form.getAffCount(); i++) { %>
				<html:hidden name="form" property='<%="affPk_"+i%>'/>
				<html:hidden name="form" property='<%="code_"+i%>'/>
				<html:hidden name="form" property='<%="subUnit_"+i%>'/>

				<logic:notEmpty name="form" property='<%="affPk_"+i%>'>
				<bean:define id="affPk" name="form" property='<%="affPk_"+i%>'/>
				<TR>
					<TD align="center">
					<% if (form.getPrbRequestEditable()) { %>
						<afscme:link page='<%="/viewPoliticalRebateRequest.action?pk="+form.getRqstPk()+"&affPk="+affPk+"&clear"%>' title="Remove this Affiliate from the Political Rebate Request" styleClass="action" confirm="Are you sure you want to clear this Affiliate from Rebate consideration?">Clear</afscme:link>
					<% } else { %>
						&nbsp;
					<% } %>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="form" property='<%="type_"+i%>'/>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="form" property='<%="local_"+i%>'/>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="form" property='<%="state_"+i%>'/>
					</TD>
					<TD class="ContentTD" align="center">
						<bean:write name="form" property='<%="council_"+i%>'/>
					</TD>
					<TD class="ContentTD" align="center">
                                		<afscme:codeWrite name="form" codeType="RebateDuration" property='<%="duration_"+i%>' format="{1}"/>
					</TD>
					<TD class="ContentTD" align="center">
                                		<afscme:codeWrite name="form" codeType="RebateFiledWith" property='<%="filedWith_"+i%>' format="{1}"/>
					</TD>
				</TR>
				</logic:notEmpty>
			<% } %>
				<TR>
					<TD colspan="7" align="center">
        				    <html:errors property="affPk_1"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD class="ContentTD">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD><LABEL for="label_ApplicationComments">Comment</LABEL></TD>
				</TR>
				<TR>
					<TD class="ContentTD">
						<bean:write name="form" property="comment"/>
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
			<afscme:button page="/viewPoliticalRebateSummary.action" paramName="form" paramProperty="prbYear" paramId="prbYear">Return</afscme:button>
			<BR><BR>
		</TD>
		<logic:equal name="form" property="prbRequestEditable" value="true">
		<TD align="right">
			<BR>
			<afscme:button page='<%="/viewPoliticalRebateRequest.action?pk="+form.getRqstPk()+"&edit"%>'>Edit</afscme:button>
			<BR><BR>
		</TD>
		</logic:equal>
	</TR>
</TABLE>
<%@ include file="../include/footer.inc" %>
