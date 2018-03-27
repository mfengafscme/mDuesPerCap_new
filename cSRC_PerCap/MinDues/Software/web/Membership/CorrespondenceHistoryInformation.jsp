<%@ taglib uri	="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Correspondence History", help = "CorrespondenceHistoryInformation.html";%>
<%@ page import="org.afscme.enterprise.correspondence.web.ViewCorrespondenceHistoryAction, org.afscme.enterprise.person.CorrespondenceData, org.afscme.enterprise.util.DateUtil" %>
<%@ include file="../include/header.inc" %>

<bean:define name="correspondenceHistoryInformationForm" id="correspondenceHistoryInformation" type="org.afscme.enterprise.correspondence.web.CorrespondenceHistoryInformationForm"/>

<!-- Tabs. -->
<bean:define id="screen" value="CorrespondenceHistory"/>
<bean:define id="recordType" value="entity"/>
<% String origin = correspondenceHistoryInformation.getOrigin(); %>
<% if (origin.equalsIgnoreCase("person")) { %>
    <%@ include file="../include/person_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("member")) { %>
    <%@ include file="../include/member_tab.inc" %>
<% } %>


<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR">
			<afscme:currentPersonName displayAsHeader="true"/> <BR> <BR> 
		</TD>
	</TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD>
			<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable" align="center">
				<TR>
					<TH width="20%">
                        <afscme:sortLink styleClass="TH" formName="correspondenceHistoryInformationForm" action="/viewCorrespondenceHistory.action" field="date">Date of Correspondence</afscme:sortLink>
					</TH>
					<TH align="left" width="80%">
                        <afscme:sortLink styleClass="TH" formName="correspondenceHistoryInformationForm" action="/viewCorrespondenceHistory.action" field="name">Name of Correspondence</afscme:sortLink>
					</TH>
				</TR>
				<logic:iterate id="correspondenceList" name="correspondenceHistoryInformation" property="correspondenceHistoryList">
				<bean:define id="data" name="correspondenceList" type="org.afscme.enterprise.person.CorrespondenceData"/> 
				<TR>
					<TD align="center">
						<%= DateUtil.getSimpleDateString(data.getCorrespondenceDt()) %>
					</TD>
					<TD>
						<bean:write name="data" property="correspondenceName"/>
					</TD>
				</TR>
				</logic:iterate>
				<html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!-- Tabs. -->
<% if (origin.equalsIgnoreCase("person")) { %>
    <%@ include file="../include/person_tab.inc" %>
<% } else if (origin.equalsIgnoreCase("member")) { %>
    <%@ include file="../include/member_tab.inc" %>
<% } %>
<%@ include file="../include/footer.inc" %>
