<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "12 Month Rebate Amount", help = "TwelveMonthRebateAmount.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="prbForm" name="twelveMonthRebateAmountForm" type="org.afscme.enterprise.rebate.web.TwelveMonthRebateAmountForm"/>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TH width="10%">
			Select
		</TH>
		<TH width="15%">
			Rebate Year
		</TH>
		<TH width="15%">
			Rebate Percentage
		</TH>
		<TH colspan="4">
			12-Month Rebate Amount
		</TH>
	</TR>
	<TR>
		<TD colspan="3">&nbsp;
		</TD>
		<TH width="15%">
			Full-Time
		</TH>
		<TH width="15%">
			Part-Time
		</TH>
		<TH width="15%">
			Lower Part-Time
		</TH>
		<TH width="15%">
			Retiree
		</TH>
	</TR>
	
	<logic:present name="prbForm" property="prb12MonthRebateAmount">
	<logic:iterate id="prb" name="prbForm" property="prb12MonthRebateAmount">
	<bean:define id="prbRebateAmount" name="prb" type="org.afscme.enterprise.rebate.PRB12MonthRebateAmount"/>
	<TR>
		<TD align="center">
		<bean:define id="year" name="prbRebateAmount" property="prbYear" type="Integer"/>
		<% if (prbForm.isPreviousRebateYear(year)) { %>
			<afscme:link page="/edit12MonthRebateAmount.action" paramName="prbRebateAmount" paramProperty="prbYear" paramId="prbYear" title="Edit 12 Month Rebate Amount" styleClass="action">Edit</afscme:link>
		<% } else { %>
			&nbsp;
		<% } %>
		</TD>
		<TD align="center">
			<bean:write name="prbRebateAmount" property="prbYear"/>
		</TD>
		<TD align="center">
			<bean:write name="prbRebateAmount" property="prbPercentage" format="0.00"/>%
		</TD>
		<TD align="center">
			<bean:write name="prbRebateAmount" property="prbFullTime" format="0.00"/>
		</TD>
		<TD align="center">
			<bean:write name="prbRebateAmount" property="prbPartTime" format="0.00"/>
		</TD>
		<TD align="center">
			<bean:write name="prbRebateAmount" property="prbLowerPartTime" format="0.00"/>
		</TD>
		<TD align="center">
			<bean:write name="prbRebateAmount" property="prbRetiree" format="0.00"/>
		</TD>
	</TR>
	</logic:iterate>
	</logic:present>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
	                <BR><afscme:button page="/showMain.action">Return</afscme:button>
		</TD>
		<TD align="right">
			<BR><afscme:button page="/edit12MonthRebateAmount.action">Add Year</afscme:button>
			<BR><BR>
		</TD>
	</TR>
</TABLE>
<%@ include file="../include/footer.inc" %>
