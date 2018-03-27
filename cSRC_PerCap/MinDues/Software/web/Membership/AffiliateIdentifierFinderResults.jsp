<%@page import="org.afscme.enterprise.affiliate.*"%>

<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>

<%! String title = "Employer Identifier Finder Results", help = "AffiliateIdentifierFinderResults.html";%>

<!-- start header -->
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/afscme.css">
    <title><%=title%></title>
    <SCRIPT language="JavaScript" src="js/date.js"></SCRIPT>
    <SCRIPT language="JavaScript" src="js/membership.js"></SCRIPT>
</head>

<body>

<table class="pageHeader">
    <tr>
        <td rowspan="2"><img src="images/logoSmall.jpg"></td>
        <td width="100%" align="left">
            <span class="appTitle">AFSCME Minimum Dues Application</span><br>
            <span class="appSubTitle">AMERICAN FEDERATION OF STATE, COUNTY AND MUNICIPAL EMPLOYEES, AFL-CIO</span>
	</td>
    </tr>
</table>

<h1><%=title%></h1>

<table width="95%" align="center">
<tr>

</tr>
<tr>
    <td>
<!-- end header -->



<html:form action="searchAffiliateFinder">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <logic:notPresent name="affiliateFinderForm" property="results">
                <TD align="center" width='10%'>
                    <A href="javascript:window.close();" class='action'>Close</A>
                </TD>
            </logic:notPresent>
            <TD class="ContentHeaderTD" align="center">
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
    </TABLE>
    <logic:present name="affiliateFinderForm" property="results">
        <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
            <TR valign="top">
                <TD>
                    <afscme:searchNav formName="affiliateFinderForm" action="/searchAffiliateFinder.action"/>
                    <BR>
                </TD>
            </TR>
        </TABLE>
        <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
            <TR>
                <TH width="6%">Select</TH>
                <TH width="44%">
                    <!-- <afscme:sortLink styleClass="TH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_ABBR_NAME%>" title="Sort By Abbreviated Affiliate Name">Abbreviated Affiliate Name</afscme:sortLink> -->
                    Employer Name
                </TH>
                <TH width="50%" colspan="5">Affiliate Identifier</TH>
            </TR>
            <TR>
                <TD colspan="2">&nbsp;</TD>
                <TH>
                    <!-- <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_TYPE%>" title="Sort By Affiliate Identifier Type">Type</afscme:sortLink> --> 
		    Type
                </TH>
                <TH>
                    <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_LOCAL%>" title="Sort By Affiliate Identifier Local/Sub Chapter">Local</afscme:sortLink>
                </TH>
                <TH>
                    <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_STATE%>" title="Sort By Affiliate Identifier State/National Type">State</afscme:sortLink>
                </TH>
                <TH>
                    <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_SUB_UNIT%>" title="Sort By Affiliate Identifier Sub Unit">Sub Unit</afscme:sortLink>
                </TH>
                <TH>
                    <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_COUNCIL%>" title="Sort By Affiliate Identifier Council/Retiree Chapter">Council</afscme:sortLink> 
                </TH>
            </TR>
            <logic:iterate id="result" name="affiliateFinderForm" property="results" type="org.afscme.enterprise.affiliate.AffiliateResult">
                <bean:define id="resultAffId" name="result" property="affiliateId" type="org.afscme.enterprise.affiliate.AffiliateIdentifier"/>
                
                <TR>
                    <TD align="center">
                        <A href="javascript:window.opener.resultsReturn('<%=result.getAffAbreviatedNm()%>', '<%=resultAffId.getType()%>', '<%=resultAffId.getLocal()%>', '<%=resultAffId.getState()%>', '<%=resultAffId.getSubUnit()%>', '<%=resultAffId.getCouncil()%>', '<%=resultAffId.getCode()%>', '<%=result.getAffPk()%>');" class="action" title="Return This Identifier">Select</A>
                    </TD>
                    <TD align="center">
                        <bean:write name="result" property="affAbreviatedNm"/>
                    </TD>
                    <afscme:affiliateIdWrite 
                            name="result" 
                            property="affiliateId" 
                    />
                </TR>
            </logic:iterate>
        </TABLE>
        <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
            <TR valign="top">
                <TD>
                    <afscme:searchNav formName="affiliateFinderForm" action="/searchAffiliateFinder.action"/>
                    <BR>
                </TD>
            </TR>
        </TABLE>
    </logic:present>
</html:form>

<!-- start footer -->
        </td>
    </tr>
</table>
<br>
</body>
</html>
<!-- end footer -->
