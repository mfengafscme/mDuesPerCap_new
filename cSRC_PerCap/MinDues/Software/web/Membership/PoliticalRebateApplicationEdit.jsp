<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<bean:define id="form" name="politicalRebateApplicationForm" type="org.afscme.enterprise.rebate.web.PoliticalRebateApplicationForm"/>

<%!String title = "Political Rebate Application Edit", help = "PoliticalRebateApplicationEdit.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="savePoliticalRebateApplication.action" focus="appMailedDate">
<html:hidden name="form" property="prbYear"/>
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
						<LABEL for="label_ApplicationMailedDate">* Mailed Date</LABEL> 
					</TD>
					<TD width="15%" class="ContentTD">
                            			<html:text name="form" property="appMailedDate" size="10" maxlength="10"/>
						<A href="javascript:show_calendar('politicalRebateApplicationForm.appMailedDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
					</TD>
					<TD width="10%" class="ContentHeaderTD">
						<LABEL for="label_ApplicationReturnedDate">* Return Date</LABEL> 
					</TD>
					<TD width="15%" class="ContentTD">
                            			<html:text name="form" property="appReturnedDate" size="10" maxlength="10"/>
						<A href="javascript:show_calendar('politicalRebateApplicationForm.appReturnedDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
					</TD>
					<TD width="15%" class="ContentHeaderTD">
						<LABEL for="label_ApplicationEvaluationCode">Evaluation Code</LABEL> 
					</TD>
					<TD width="35%" class="ContentTD">
						<html:select name="form" property="appEvalCode">
							<afscme:codeOptions useCode="false" codeType="RebateAppEvalCode" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
				</TR>
				<TR>
					<TD colspan="6" align="center">		
						<html:errors property="appMailedDate"/>
						<html:errors property="appReturnedDate"/>
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
						<LABEL for="label_CommentAnalysisCode">Comment Analysis Code</LABEL> 
					</TD>
					<TD class="ContentTD">
						<html:select name="form" property="appCommentAnalCode">
							<afscme:codeOptions useCode="false" codeType="RbtCommentAnalCode" allowNull="true" nullDisplay="" format="{1}"/>
						</html:select>
					</TD>
				</TR>
				<TR>
					<TD colspan="2" align="center">		
						<html:errors property="appCommentAnalCode"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH colspan="5">Dues Paid To</TH>
					<TH>Duration</TH>
					<TH>Filed With</TH>
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
					<TD align="center"><afscme:affiliateFinder formName="politicalRebateApplicationForm" affPkParam='<%="affPk_"+i%>' affIdTypeParam='<%="type_"+i%>' affIdCouncilParam='<%="council_"+i%>' affIdLocalParam='<%="local_"+i%>' affIdStateParam='<%="state_"+i%>' affIdSubUnitParam='<%="subUnit_"+i%>' affIdCodeParam='<%="code_"+i%>' styleClass="action"/>
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
					<TD>
						<LABEL for="label_ApplicationComments">Edit Comment</LABEL> 
					</TD>
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
</TABLE>
</html:form>
<%@ include file="../include/footer.inc" %>