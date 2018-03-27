<%@page import="org.afscme.enterprise.codes.Codes.AffiliateSections" %>

<%! String title = "Affiliate Change History Detail", help = "AffiliateChangeHistoryDetail.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD>
            <BR>
            <afscme:button action="/searchAffiliateChangeHistory.action">Return</afscme:button>
        </TD>
        <TD align="right">
            <BR>
            <INPUT type="button" class="BUTTON" name="NewSearchButton" value="New Search" onClick="window.location='/viewAffiliateChangeHistoryCriteria.action'"/>
            <BR><BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate /><BR>
            Changed Date : <bean:write name="changeHistoryDetailForm" property="changedDate"/> - 
            Section : <afscme:codeWrite name="changeHistoryDetailForm" property="section" codeType="AffiliateSections" format="{1}"/>
            <BR><BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <logic:present name="changeHistoryDetailForm" property="changes">
        <TR valign="top">
            <TH WIDTH="23%">Field</TH>
            <TH WIDTH="35%">Old Value</TH>
            <TH WIDTH="35%">New Value</TH>
            <TH WIDTH="7%">User ID</TH> 
        </TR>
        <logic:iterate name="changeHistoryDetailForm" property="changes" id="changeData">
            <TR>
                <TD align="center">
                    <afscme:codeWrite name="changeData" property="fieldChangedCodePk" codeType="ChangeHistoryFields" format="{1}"/>
                </TD>
                <TD align="center">
                    <logic:equal name="changeData" property="oldValue" value=" ">
                        &nbsp;
                    </logic:equal>
                    <logic:notEqual name="changeData" property="oldValue" value=" ">
                        <bean:write name="changeData" property="oldValue"/>
                    </logic:notEqual>
                </TD>
                <TD align="center">
                    <logic:equal name="changeData" property="newValue" value=" ">
                        &nbsp;
                    </logic:equal>
                    <logic:notEqual name="changeData" property="newValue" value=" ">
                        <bean:write name="changeData" property="newValue"/>
                    </logic:notEqual>
                </TD>
                <TD ALIGN="center">
                    <afscme:userWrite name="changeData" property="changedByUserPk"/>
                </TD>
            </TR>
        </logic:iterate>
    </logic:present>
    <logic:notPresent name="changeHistoryDetailForm" property="changes">
        <TR valign="top">
            <TD align='center'>
                <bean:message key="error.noResultsFound"/>
            </TD>
        </TR>
    </logic:notPresent>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR">
            <BR>
            <afscme:currentAffiliate /><BR>
            Changed Date : <bean:write name="changeHistoryDetailForm" property="changedDate"/> - 
            Section : <afscme:codeWrite name="changeHistoryDetailForm" property="section" codeType="AffiliateSections" format="{1}"/>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD>
            <BR>
            <afscme:button action="/searchAffiliateChangeHistory.action">Return</afscme:button>
        </TD>
        <TD align="right">
            <BR>	
            <INPUT type="button" class="BUTTON" name="NewSearchButton" value="New Search" onClick="window.location='/viewAffiliateChangeHistoryCriteria.action'"/>
            <BR><BR> 
        </TD>
    </TR>
</TABLE>

<%@ include file="../include/footer.inc" %> 