<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Select Affiliate", help = "SelectAffiliateSearchResults.html";%>
<%@ include file="../include/header.inc" %>

    <bean:define id="form" name="selectAffiliateSearchForm" type="org.afscme.enterprise.controller.web.SelectAffiliateSearchForm"/>

    <html:form action="selectAffiliateSearch">
    <table width="80%" align="center">
    <tr><td colspan="2">
    <afscme:searchNav formName="form" action="/selectAffiliateSearch.action"/>
    <tr><td colspan="2">
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="BodyContentNoWidth">
					<TR>
						<TH nowrap>
                            Select
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectAffiliateSearch.action" formName="form" field="name">Name</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectAffiliateSearch.action" formName="form" field="type">Type</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectAffiliateSearch.action" formName="form" field="local">Local / Sub Chapter</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectAffiliateSearch.action" formName="form" field="state">State</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectAffiliateSearch.action" formName="form" field="subUnit">Sub Code</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectAffiliateSearch.action" formName="form" field="council">Council/Retiree Chapter</afscme:sortLink>
						</TH>
					</TR>
                    <logic:iterate name="form" property="results" type="org.afscme.enterprise.users.AffiliateData" id="affiliate">
					<TR>
						<TD align="center" class="ContentTD">
                            <afscme:link page="/selectAffiliate.action" paramName="affiliate" paramProperty="pk" paramId="pk" styleClass="action">Select</afscme:link>
						</TD>
						<TD align="center" class="ContentTD">
                            <bean:write name="affiliate" property="name"/>
						</TD>
						<TD align="center" class="ContentTD">
                            <bean:write name="affiliate" property="type"/>
						</TD>
						<TD align="center" class="ContentTD">
                            <bean:write name="affiliate" property="local"/>
						</TD>
						<TD align="center" class="ContentTD">
                            <afscme:codeWrite name="affiliate" property="state" codeType="AffiliateState" useCode="true"/>
						</TD>
						<TD align="center" class="ContentTD">
                            <bean:write name="affiliate" property="subUnit"/>
						</TD>
						<TD align="center" class="ContentTD">
                            <bean:write name="affiliate" property="council"/>
						</TD>
					</TR>
                    </logic:iterate>
				</TABLE>
    <tr><td colspan="2">
    <afscme:searchNav formName="form" action="/selectAffiliateSearch.action"/>
    <tr><td colspan="2"><br>
    <tr><td align="left">
    	<logic:equal name="form" property="showNewSearch" value="YES">
            <afscme:button page="/selectAffiliateSearch.action?submit=true&new=">New Search</afscme:button>&nbsp;        
        </logic:equal>
	</table>
    </html:form>

<%@ include file="../include/footer.inc" %>

