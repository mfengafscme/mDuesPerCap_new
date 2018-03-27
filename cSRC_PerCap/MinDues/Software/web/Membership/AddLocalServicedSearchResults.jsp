<%! String title = "Affiliate Staff Add Local Serviced Search Results", help = "AddLocalServicedSearchResults.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="addLocalServiced">
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD align="right">
            <BR>
            <afscme:button action="/viewAddLocalServiced.action">New Search</afscme:button>
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<logic:notPresent name="addLocalServicedForm" property="results">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD class="ContentHeaderTD" align="center">
                <bean:message key="error.noResultsFound"/>
            </TD>
        </TR>
    </TABLE>
</logic:notPresent>
<logic:present name="addLocalServicedForm" property="results">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <afscme:searchNav formName="addLocalServicedForm" action="/addLocalServiced.action"/>
                <BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR>
            <TH width="7%">
                Select 
            </TH>
            <TH width="30%">
                <afscme:sortLink styleClass="TH" action="/addLocalServiced.action" formName="addLocalServicedForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_ABBR_NAME%>">Abbreviated Affiliate Name</afscme:sortLink>
            </TH>
            <TH colspan="5">
                Affiliate Identifier 
            </TH>
        </TR>
        <TR>
            <TD colspan="2">&nbsp;
            </TD>
            <TH width="9%">
                <afscme:sortLink styleClass="smallTH" action="/addLocalServiced.action" formName="addLocalServicedForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_TYPE%>">Type</afscme:sortLink>
            </TH>
            <TH width="9%">
                <afscme:sortLink styleClass="smallTH" action="/addLocalServiced.action" formName="addLocalServicedForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_LOCAL%>">Local/Sub Chapter</afscme:sortLink>
            </TH>
            <TH width="9%">
                <afscme:sortLink styleClass="smallTH" action="/addLocalServiced.action" formName="addLocalServicedForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_STATE%>">State/National Type</afscme:sortLink>
            </TH>
            <TH width="10%">
                <afscme:sortLink styleClass="smallTH" action="/addLocalServiced.action" formName="addLocalServicedForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_SUB_UNIT%>">Sub Unit</afscme:sortLink>
            </TH>
            <TH width="17%">
                <afscme:sortLink styleClass="smallTH" action="/searchAffiliate.action" formName="addLocalServicedForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_COUNCIL%>">Council/Retiree Chapter</afscme:sortLink>
            </TH>
        </TR>
        <logic:iterate name="addLocalServicedForm" property="results" id="result" type="org.afscme.enterprise.affiliate.AffiliateResult">
            <TR>
                <TD align="center">
                    <afscme:link page="/addLocalServiced.action" paramId="affPk" paramName="result" paramProperty="affPk" styleClass="action">Select</afscme:link>
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
            <TD align="right">
                <BR>
                <afscme:button action="/viewAddLocalServiced.action">New Search</afscme:button>
                <BR> 
            </TD>
        </TR>
    </TABLE>
</logic:present>
</html:form>

<%@ include file="../include/footer.inc" %> 
