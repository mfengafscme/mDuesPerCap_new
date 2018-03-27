<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Preliminary Roster, Rebate Update File Affiliates Selection", help = "PreliminaryRosterAffiliateSelection.html";%>
<%@ include file="../include/header.inc" %>
<SCRIPT LANGUAGE="JavaScript">
var checkflag = "false";
function check() {
  var rowCount = checkTable.rows.length;
  if (checkflag == "false") {
	checkflag = "true";
	for (var i=0; i < rowCount; i++) {
		checkTable.rows(i).cells(0).childNodes(0).checked = true; 
	} 
  } 
  else {
	checkflag = "false";
	for (var i=0; i < rowCount; i++) {
		checkTable.rows(i).cells(0).childNodes(0).checked = false; 
	} 
  }
}
</SCRIPT>

<bean:define id="form" name="preliminaryRosterAffiliateForm" type="org.afscme.enterprise.reporting.specialized.web.PreliminaryRosterAffiliateForm"/>
<html:form action="preliminaryRosterReport.action">
<html:hidden name="form" property="size"/>
<center><html:errors/></center>

<% if (form.getSize() > 20) { %>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<% if (form.getSize() > 0) { %>
				<html:submit styleClass="button"/>
			<% } else { %>
				<html:submit styleClass="button" disabled="true"/>
			<% } %>
			<BR>
			<BR>
		</TD>
		<TD align="right">
			<BR>
			<html:reset styleClass="button"/>
			<html:cancel styleClass="button"/>
		</TD>
	</TR>
</TABLE>
<% } %>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD colspan="6" class="ContentHeaderTR">
			Affiliate Identifier
		</TD>
	</TR>
	<TR>
		<TH width="14%">
			<html:checkbox name="form" property="selectAll" onclick="check()"/>
			&nbsp;Select
		</TH>
		<TH width="14%">
			<afscme:sortLink styleClass="TH" action="/viewPreliminaryRosterAffiliate.action" formName="form" field="aff_type" title="Sort By Type">Type</afscme:sortLink>
		</TH>
		<TH width="14%">
			<afscme:sortLink styleClass="TH" action="/viewPreliminaryRosterAffiliate.action" formName="form" field="aff_localSubChapter" title="Sort By Local/Sub Chapter">Local/Sub Chapter</afscme:sortLink>			
		</TH>
		<TH width="20%">
			<afscme:sortLink styleClass="TH" action="/viewPreliminaryRosterAffiliate.action" formName="form" field="aff_stateNat_type" title="Sort By State/National Type">State/National Type</afscme:sortLink>
		</TH>
		<TH width="14%">
			<afscme:sortLink styleClass="TH" action="/viewPreliminaryRosterAffiliate.action" formName="form" field="aff_subUnit" title="Sort By Sub Unit">Sub Unit</afscme:sortLink>
		</TH>
		<TH width="29%">
			<afscme:sortLink styleClass="TH" action="/viewPreliminaryRosterAffiliate.action" formName="form" field="aff_councilRetiree_chap" title="Sort By Council/Retiree Chapter">Council/Retiree Chapter</afscme:sortLink>
		</TH>
	</TR>
</TABLE>

<TABLE id="checkTable" cellpadding="0" cellspacing="0" border="0" class="BodyContent" align="center">
<nested:iterate name="form" property="affiliateList">
	<nested:hidden property="affPk" />
	<nested:hidden property="code" />
	<TR>
		<TD align="center" width="14%">
			<nested:checkbox property="selected" />
		</TD>
		<TD align="center" width="14%">
			<nested:write property="type" />
		</TD>
		<TD align="center" width="14%">
			<nested:write property="local" />
		</TD>
		<TD align="center" width="20%">
			<nested:write property="state" />
		</TD>
		<TD align="center" width="14%">
			<nested:write property="subUnit" />
		</TD>
		<TD align="center" width="29%">
			<nested:write property="council" />
		</TD>
	</TR>
</nested:iterate>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<% if (form.getSize() > 0) { %>
				<html:submit styleClass="button"/>
			<% } else { %>
				<html:submit styleClass="button" disabled="true"/>
			<% } %>
			<BR>
			<BR>
		</TD>
		<TD align="right">
			<BR>
			<html:reset styleClass="button"/>
			<html:cancel styleClass="button"/>
		</TD>
	</TR>
</TABLE>
</html:form>

<%@ include file="../include/footer.inc" %>
