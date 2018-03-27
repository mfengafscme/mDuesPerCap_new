<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%!String title = "Replace Officer Results", help = "ReplaceOfficerResults.html";%>
<%@ include file="../include/header.inc" %>
<bean:define id="form" name="replaceOfficerResultsForm" type="org.afscme.enterprise.affiliate.officer.web.ReplaceOfficerResultsForm"/>

<html:form action="/replaceOfficerResults.action">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <logic:notPresent name="replaceOfficerResultsForm" property="results">
                <TD align="center" width='10%'>
                    <A href="javascript:window.close();" class='action'>Close</A>
                </TD>
            </logic:notPresent>
            <TD class="ContentHeaderTD" align="center">
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
    </TABLE>
    <logic:present name="replaceOfficerResultsForm" property="results">
        <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
            <TR valign="top">
                <TD>
                    <afscme:searchNav formName="replaceOfficerResultsForm" action="/replaceOfficerResults.action" />
                    <BR>
                </TD>
            </TR>
        </TABLE>

        <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">        
	<TR>
		<TH width="4%">
			Select 
		</TH>
		<TH>
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_NAME%>" title="Sort By Name">Name</afscme:sortLink>
		</TH>
		<TH colspan="5" WIDTH="27%">
			Affiliate Identifier 
		</TH>
		<TH>
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_ADDRESS%>" title="Sort By Address">Address</afscme:sortLink>
		</TH>
		<TH>
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_CITY%>" title="Sort By City">City</afscme:sortLink>
		</TH>
		<TH width="4%">
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_STATE%>" title="Sort By State">State</afscme:sortLink>
		</TH>
		<TH width="8%">
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_ZIP%>" title="Sort By Zip/Postal Code">Zip/Postal Code</afscme:sortLink>
		</TH>
	</TR>
	<TR>
		<TD colspan="2">&nbsp;</TD>
		<TH>
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_AFF_ID_TYPE%>" title="Sort By Affiliate Type">Type</afscme:sortLink>
		</TH>
		<TH>
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_AFF_ID_LOCAL%>" title="Sort By Affiliate Loc/Sub Chap">Loc/Sub Chap</afscme:sortLink>
		</TH>
		<TH>
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_AFF_ID_STATE%>" title="Sort By Affiliate State/Nat'l">State/Nat'l</afscme:sortLink>
		</TH>
		<TH>
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_AFF_ID_SUB_UNIT%>" title="Sort By Affiliate Sub Unit">Sub Unit</afscme:sortLink>
		</TH>
		<TH>
                    <afscme:sortLink styleClass="smallTH" action="/replaceOfficerResults.action" formName="replaceOfficerResultsForm" field="<%=org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria.SORT_BY_AFF_ID_COUNCIL%>" title="Sort By Affiliate CN/Ret Chap">CN/Ret Chap</afscme:sortLink>
		</TH>
		<TD colspan="4">&nbsp;</TD>
	</TR>
     <logic:iterate id="result" name="replaceOfficerResultsForm" property="results" type="org.afscme.enterprise.affiliate.officer.ReplaceOfficerResults" >
	<TR>
		<TD align="center">
			<A href="javascript:window.opener.officerResultsReturn('<%=result.getFirstName()%>', '<%=result.getMiddleName()%>', '<%=result.getLastName()%>', '<%=result.getSuffix()%>', '<%=result.getPersonPk()%>', '<%=result.getAffPk()%>');" class="action" title="Replace with This Person">Select</A> 
		</TD>
		<TD align="center"><bean:write name="result" property="lastName" /> <afscme:codeWrite name="result" property="suffix" codeType="Suffix" format="{1}"/>, <bean:write name="result" property="firstName" /> <bean:write name="result" property="middleName" /></TD>
		<afscme:affiliateIdWrite 
		                            name="result" 
		                            property="ai" 
                    />
		<TD align="center"><bean:write name="result" property="addr1" /> <bean:write name="result" property="addr2" /></TD>		
		<TD align="center"><bean:write name="result" property="city" /></TD>
		<TD align="center"><bean:write name="result" property="state" /></TD>
		<TD align="center"><bean:write name="result" property="zip" /></TD>
	</TR>
    </logic:iterate>
    </TABLE>
        <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
            <TR valign="top">
                <TD>
                    <afscme:searchNav formName="replaceOfficerResultsForm" action="/replaceOfficerResults.action" />
                    <BR>
                </TD>
            </TR>
        </TABLE>
    </logic:present>        
</html:form>
<%@ include file="../include/footer.inc" %>
