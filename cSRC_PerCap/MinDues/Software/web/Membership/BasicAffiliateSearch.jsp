<%! String title = "Basic Affiliate Search", help = "BasicAffiliateSearch.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD align="center">
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>
<html:form action="searchAffiliate.action" focus="affIdType">
    <input type="hidden" name="reset">
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="80%" align="center">
        <TR>
            <TD class="ContentHeaderTR">
                Fields to Search: <BR>
                &nbsp; 
            </TD>
        </TR>
        <TR valign="top">
            <TD class="ContentHeaderTR">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH colspan="5" align="left">Affiliate Identifier</TH>
                    </TR>
                    <TR>
                        <TH width="14%">Type</TH>
                        <TH width="14%">Local/Sub Chapter</TH>
                        <TH width="20%">State/National Type</TH>
                        <TH width="14%">Sub Unit</TH>
                        <TH width="29%">Council/Retiree Chapter</TH>
                    </TR>
                    <TR>
                        <TD align="center" class="ContentTD">
                            <html:select property="affIdType" name="affiliateSearchForm" onblur="lockAffiliateIDFields(this.form);" onchange="lockAffiliateIDFields(this.form);">
                                <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdLocal" name="affiliateSearchForm" size="4" maxlength="4"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="affIdState" name="affiliateSearchForm">
                                <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdSubUnit" name="affiliateSearchForm" size="4" maxlength="4"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdCouncil" name="affiliateSearchForm" size="4" maxlength="4"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan="5">
                            <HR>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan="2" class="ContentHeaderTD">Include Sub-Units</TD>
                        <TD class="smallFont">
                            <html:radio name="affiliateSearchForm" property="includeSubUnits" value="true"/>
                            Yes 
                            <html:radio name="affiliateSearchForm" property="includeSubUnits" value="false"/>
                            No 
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR> 
                <afscme:button action="/viewPowerAffiliateCriteria.action">Power Search</afscme:button> 
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</html:form>

<%@ include file="../include/footer.inc" %> 
