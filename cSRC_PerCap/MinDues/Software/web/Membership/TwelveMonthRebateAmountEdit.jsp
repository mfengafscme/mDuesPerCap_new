<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<bean:define id="prbEditForm" name="twelveMonthRebateAmountEditForm" type="org.afscme.enterprise.rebate.web.TwelveMonthRebateAmountEditForm"/>

<%!String title, help;%>
<%title = "12 Month Rebate Amount - " + (prbEditForm.isEdit() ? "Edit" : "Add Year");%>
<%help = (prbEditForm.isEdit()) ? "TwelveMonthRebateAmountEdit.html" : "TwelveMonthRebateAmountAddYear.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="save12MonthRebateAmount.action" focus="prbPercentage">

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TH width="20%">Rebate Year</TH>
		<TH width="20%">* Rebate Percentage</TH>
		<TH colspan="4">12-Month Rebate Amount</TH>
	</TR>
	<TR>
		<TD colspan="2">&nbsp;</TD>
		<TH width="15%">* Full-Time</TH>
		<TH width="15%">* Part-Time</TH>
		<TH width="15%">* Lower Part-Time</TH>
		<TH width="15%">* Retiree</TH>
	</TR>
	<TR>
		<TD align="center">
			<html:hidden name="prbEditForm" property="edit"/>
			<html:hidden name="prbEditForm" property="prbYear"/>
			<bean:write name="prbEditForm" property="prbYear"/>
		</TD>
		<TD align="center">
			<html:text name="prbEditForm" property="prbPercentage" size="8" maxlength="8"/>% 
		</TD>
		<TD align="center">
			<html:text name="prbEditForm" property="prbFullTime" size="8" maxlength="8"/>
		</TD>
		<TD align="center">
			<html:text name="prbEditForm" property="prbPartTime" size="8" maxlength="8"/>
		</TD>
		<TD align="center">
			<html:text name="prbEditForm" property="prbLowerPartTime" size="8" maxlength="8"/>
		</TD>
		<TD align="center">
			<html:text name="prbEditForm" property="prbRetiree" size="8" maxlength="8"/>
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<html:submit property="submit" styleClass="button" value="Submit"/>
			<BR>
			<BR>
		</TD>
		<TD align="right">
			<BR>
			<html:reset styleClass="button"/>
			<html:cancel styleClass="button"/>
		</TD>
	</TR>
	<TR>
		<TD align="center" colspan="2">
                        <html:errors property="prbYear"/>
                        <html:errors property="prbPercentage"/>
                        <html:errors property="prbFullTime"/>
                        <html:errors property="prbPartTime"/>
                        <html:errors property="prbLowerPartTime"/>
                        <html:errors property="prbRetiree"/>
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
