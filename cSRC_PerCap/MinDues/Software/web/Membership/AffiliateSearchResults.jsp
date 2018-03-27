<%! String title = "Affiliate Search Results", help = "AffiliateSearchResults.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="action" name="affiliateSearchForm" property="searchAction" type="java.lang.String"/>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD align="right">
            <BR>
            <afscme:button action="/viewBasicAffiliateCriteria.action">New Search</afscme:button>
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<logic:notPresent name="affiliateSearchForm" property="results">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD class="ContentHeaderTD" align="center">
                <bean:message key="error.noResultsFound"/>
            </TD>
        </TR>
    </TABLE>
</logic:notPresent>
<logic:present name="affiliateSearchForm" property="results">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <afscme:searchNav formName="affiliateSearchForm" action="<%=action%>"/>
                <BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR>
            <TH width="7%">
                Select 
            </TH>
            <TH width="35%">
                <afscme:sortLink styleClass="TH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_ABBR_NAME%>">Abbreviated Affiliate Name</afscme:sortLink>
            </TH>
            <TH colspan="5">
                Affiliate Identifier 
            </TH>
        </TR>
        <TR>
            <TD colspan="2">&nbsp;
            </TD>
            <TH width="6%">
                <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_TYPE%>">Type</afscme:sortLink>
            </TH>
            <TH width="11%">
                <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_LOCAL%>">Local/Sub Chapter</afscme:sortLink>
            </TH>
            <TH width="12%">
                <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_STATE%>">State/National Type</afscme:sortLink>
            </TH>
            <TH width="7%">
                <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_SUB_UNIT%>">Sub Unit</afscme:sortLink>
            </TH>
            <TH width="17%">
                <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_COUNCIL%>">Council/Retiree Chapter</afscme:sortLink>
            </TH>
        </TR>
        <logic:iterate name="affiliateSearchForm" property="results" id="result" type="org.afscme.enterprise.affiliate.AffiliateResult">
            <bean:define id="affId" name="result" property="affiliateId" type="org.afscme.enterprise.affiliate.AffiliateIdentifier"/>
            <TR>
                <TD align="center">
                    <afscme:link page="/viewAffiliateDetail.action" paramId="affPk" paramName="result" paramProperty="affPk" styleClass="action">View</afscme:link>
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
                <afscme:searchNav formName="affiliateSearchForm" action="<%=action%>"/>
                <BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
             <TD align="left">
                <BR>
                <afscme:button action="/addAffiliateDetail.action">Add New Affiliate</afscme:button>
                <BR> 
            </TD>
            <TD align="right">
                <BR>
                <afscme:button action="/viewBasicAffiliateCriteria.action">New Search</afscme:button>
                <BR> 
            </TD>
        </TR>
    </TABLE>
</logic:present>

<%@ include file="../include/footer.inc" %> 
