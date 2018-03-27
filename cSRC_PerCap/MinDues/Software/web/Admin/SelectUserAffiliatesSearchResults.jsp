<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Select Affiliate Privileges", help = "SelectUserAffiliates.html";%>
<%@ include file="../include/header.inc" %>

    <bean:define id="form" name="selectUserAffiliatesSearchForm" type="org.afscme.enterprise.users.web.SelectUserAffiliatesSearchForm"/>

    <html:form action="saveUserAffiliatesSelection">
    <table width="80%" align="center">
    <tr><td align="left">
                <html:submit styleClass="button"/>   
			<TD align="right">
                <html:submit property="selectAllResults" value='<%="Select all " + form.getTotal() + " results"%>' styleClass="button"/>
                <html:submit property="deSelectAllResults" value='<%="Deselect all " + form.getTotal() + " results"%>' styleClass="button"/>   
                <afscme:button page="/selectUserAffiliatesSearch.action?new">New Search</afscme:button>
                <html:cancel styleClass="button"/>   
    <tr><td colspan="2">
    <afscme:searchNav formName="form" action="/selectUserAffiliatesSearch.action"/>
    <tr><td colspan="2">
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="BodyContentNoWidth">
					<TR>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectUserAffiliatesSearch.action" formName="form" field="name">Name</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectUserAffiliatesSearch.action" formName="form" field="type">Type</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectUserAffiliatesSearch.action" formName="form" field="local">Local / Sub Chapter</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectUserAffiliatesSearch.action" formName="form" field="state">State/National Type</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectUserAffiliatesSearch.action" formName="form" field="subUnit">Sub Unit</afscme:sortLink>
						</TH>
						<TH nowrap>
                            <afscme:sortLink styleClass="TH" action="/selectUserAffiliatesSearch.action" formName="form" field="council">Council/Retiree Chapter</afscme:sortLink>
						</TH>
						<TH nowrap>
                            				Selected
						</TH>
					</TR>
                    <logic:iterate name="form" property="results" type="org.afscme.enterprise.users.AffiliateData" id="affiliate">
					<TR>
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
						<TD align="center" class="ContentTD">
                            <bean:define id="id" name="affiliate" property="pk" type="Integer"/>
                            <html:multibox property="selection" value='<%=id.toString()%>'/>
						</TD>
					</TR>
                    </logic:iterate>
				</TABLE>
    <tr><td colspan="2">
    <afscme:searchNav formName="form" action="/selectUserAffiliatesSearch.action"/>
    <tr><td colspan="2"><br>
    <tr><td align="left">
                <html:submit styleClass="button"/>   
			<TD align="right">
                <html:submit property="selectAllResults" value='<%="Select all " + form.getTotal() + " results"%>' styleClass="button"/>
                <html:submit property="deSelectAllResults" value='<%="Deselect all " + form.getTotal() + " results"%>' styleClass="button"/>   
                <afscme:button page="/selectUserAffiliatesSearch.action?new">New Search</afscme:button>
                <html:cancel styleClass="button"/>   
	</table>
    </html:form>

<%@ include file="../include/footer.inc" %>

