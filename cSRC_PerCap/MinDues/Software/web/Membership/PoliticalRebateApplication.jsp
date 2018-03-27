<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title="Political Rebate Application", help="PoliticalRebateApplication.html";%>
<%@ include file="../include/header.inc" %>
<bean:define id="form" name="politicalRebateApplicationForm" type="org.afscme.enterprise.rebate.web.PoliticalRebateApplicationForm"/>
<html:hidden name="form" property="pk"/>

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
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="10%" class="ContentHeaderTD">
						Mailed Date 
					</TD>
					<TD width="15%" class="ContentTD">
						<bean:write name="form" property="appMailedDate"/> 
					</TD>
					<TD width="10%" class="ContentHeaderTD">
						Return Date 
					</TD>
					<TD width="15%" class="ContentTD">
						<bean:write name="form" property="appReturnedDate"/> 
					</TD>
					<TD width="15%" class="ContentHeaderTD">
						Evaluation Code 
					</TD>
					<TD width="35%" class="ContentTD">
                                		<afscme:codeWrite name="form" codeType="RebateAppEvalCode" property="appEvalCode" format="{1}"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="20%" class="ContentHeaderTD">
						Comment Analysis Code 
					</TD>
					<TD class="ContentTD">
                                		<afscme:codeWrite name="form" codeType="RbtCommentAnalCode" property="appCommentAnalCode" format="{1}"/>
					</TD>
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
				</TR>
				<TR>
					<TD>&nbsp;</TD>
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
					<% if (form.getPrbAppEditable()) { %>
						<afscme:link page='<%="/viewPoliticalRebateApplication.action?pk="+form.getAppPk()+"&affPk="+affPk+"&clear"%>' title="Remove this Affiliate from the Political Rebate Application" styleClass="action" confirm="Are you sure you want to clear this Affiliate from Rebate consideration?">Clear</afscme:link>
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
					<TD>
						<LABEL for="label_ApplicationComments">Comment</LABEL> 
					</TD>
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
		<logic:equal name="form" property="prbAppEditable" value="true">
		<TD align="right">
			<BR>
			<afscme:button page='<%="/viewPoliticalRebateApplication.action?pk="+form.getAppPk()+"&edit"%>'>Edit</afscme:button>
			<BR><BR>
		</TD>
		</logic:equal>
	</TR>
</TABLE>
<%@ include file="../include/footer.inc" %>
