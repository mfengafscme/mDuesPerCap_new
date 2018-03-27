<%@page import="org.afscme.enterprise.affiliate.*"%>

<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Duplicate Affiliate Identifier Notifier", help = "DuplicateAffiliateIdentifierNotifier.html";%>
<%@include file="../include/header.inc" %>

<html:form action="searchAffiliateFinder">
    <bean:define id="action" name="affiliateFinderForm" property="linkAction" type="String"/>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
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
             	<logic:notEqual name="action" value="/saveAffiliateDetailAdd.action?continue">
                <TH width="6%">Select</TH>
                </logic:notEqual>
                <TH width="44%">
                    <afscme:sortLink styleClass="TH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_ABBR_NAME%>" title="Sort By Abbreviated Affiliate Name">Abbreviated Affiliate Name</afscme:sortLink>
                </TH>
                <TH width="50%" colspan="5">Affiliate Identifier</TH>
            </TR>
            <TR>
            	<logic:notEqual name="action" value="/saveAffiliateDetailAdd.action?continue">            
                <TD colspan="2">&nbsp;</TD>
                </logic:notEqual>
            	<logic:equal name="action" value="/saveAffiliateDetailAdd.action?continue">            
                <TD>&nbsp;</TD>
                </logic:equal>                
                
                <TH>
                    <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_TYPE%>" title="Sort By Affiliate Identifier Type">Type</afscme:sortLink>
                </TH>
                <TH>
                    <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_LOCAL%>" title="Sort By Affiliate Identifier Local/Sub Chapter">Loc/Sub Chap</afscme:sortLink>
                </TH>
                <TH>
                    <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_STATE%>" title="Sort By Affiliate Identifier State/National Type">State/Nat'l</afscme:sortLink>
                </TH>
                <TH>
                    <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_SUB_UNIT%>" title="Sort By Affiliate Identifier Sub Unit">Sub Unit</afscme:sortLink>
                </TH>
                <TH>
                    <afscme:sortLink styleClass="smallTH" action="/searchAffiliateFinder.action" formName="affiliateFinderForm" field="<%=AffiliateCriteria.SORT_BY_AFF_ID_COUNCIL%>" title="Sort By Affiliate Identifier Council/Retiree Chapter">CN/Ret Chap</afscme:sortLink>
                </TH>
            </TR>
            <logic:iterate id="result" name="affiliateFinderForm" property="results" type="org.afscme.enterprise.affiliate.AffiliateResult">
                <bean:define id="resultAffId" name="result" property="affiliateId" type="org.afscme.enterprise.affiliate.AffiliateIdentifier"/>
                <TR>
              	    <logic:notEqual name="action" value="/saveAffiliateDetailAdd.action?continue">
                    <TD align="center">
                        <afscme:link page="<%=action%>" paramId="affPk" paramName="result" paramProperty="affPk" styleClass="action">Select</afscme:link>
                    </TD>
                    </logic:notEqual>
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
    <br>
    <logic:present name="affiliateFinderForm" property="cancelAction">
        <bean:define id="cancel" name="affiliateFinderForm" property="cancelAction" type="String"/>
        <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
            <TR>
              	<logic:equal name="action" value="/saveAffiliateDetailAdd.action?continue">            
                <TD align="left">
                    <afscme:button action="<%=action%>">Continue</afscme:button>
                </TD>
                </logic:equal>
                <TD align="right">
                    <afscme:button action="<%=cancel%>">Cancel</afscme:button>
                </TD>
            </TR>
        </TABLE>
    </logic:present>
</html:form>

<%@ include file="../include/footer.inc" %>