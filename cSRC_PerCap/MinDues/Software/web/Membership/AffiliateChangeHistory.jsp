<%@page import="org.afscme.enterprise.affiliate.AffiliateChangeCriteria" %>

<%! String title = "Affiliate Change History", help = "AffiliateChangeHistory.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define id="screen" value="ChangeHistory"/>
<%@ include file="../include/affiliate_tab.inc" %>

<html:form action="searchAffiliateChangeHistory.action?new" focus="section">

<html:hidden name="changeHistorySearchForm" property="affPk"/>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<!-- begin center table -->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD colspan="2" class="ContentHeaderTR">
            Fields to Search:<BR> 
        </TD>
    </TR>
    <TR valign="top">
        <TD colspan="2" class="ContentHeaderTR">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TH width="45%">* Section</TH>
                    <TH colspan="2">Changed</TH>
                </TR>
                <TR>
                    <TD>&nbsp;</TD>
                    <TH class="small">* From Date</TH>
                    <TH class="small">* To Date</TH>
                </TR>
                <TR>
                    <TD align="center">
                        <html:select name="changeHistorySearchForm" property="section">
                            <afscme:codeOptions useCode="false" codeType="AffiliateSections" allowNull="true" nullDisplay="ALL" format="{1}"/>
                        </html:select>
                    </TD>
                    <TD align="center">
                        <html:text name="changeHistorySearchForm" property="changedFrom" size="10" maxlength="10"/>
                        <A href="javascript:show_calendar('changeHistorySearchForm.changedFrom');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"> 
                        <IMG src="../images/calendar.gif" width="24" height="22" border="0" alt="Calendar"></A> 
                    </TD>
                    <TD align="center">
                        <html:text name="changeHistorySearchForm" property="changedTo" size="10" maxlength="10"/>
                        <A href="javascript:show_calendar('changeHistorySearchForm.changedTo');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"> 
                        <IMG src="../images/calendar.gif" width="24" height="22" border="0" alt="Calendar"></A> 
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD align="left">
            <BR>
            <html:submit styleClass="button" />
        </TD>
        <TD align="center" class="DataFontI">
            <BR>
            * You must enter at least one field to search. 
        </TD>
        <TD align="right">
            <BR>
            <html:reset styleClass="button"/>
            <BR><BR> 
        </TD>
    </TR>
</TABLE>
<logic:equal name="changeHistorySearchForm" property="hasCriteria" value="true">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD>
                	<BR> <HR> <BR> 
            </TD>
        </TR>
    </TABLE>
    <TABLE width="100%" align="center">
        <TR>
            <TD align="left">
                  <afscme:searchNav formName="changeHistorySearchForm" action="/searchAffiliateChangeHistory.action"/>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align="center">
                <html:errors/>
            </TD>
        </TR>
    </TABLE>
    <logic:present name="changeHistorySearchForm" property="results">
        <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
            <TR>
                <TH width="10%">Select</TH>
                <TH>
                    <afscme:sortLink styleClass="TH" action="/searchAffiliateChangeHistory.action" formName="changeHistorySearchForm" field="<%=AffiliateChangeCriteria.SORT_BY_SECTION%>">Section</afscme:sortLink>
                </TH>
                <TH WIDTH="25%">
                    <afscme:sortLink styleClass="TH" action="/searchAffiliateChangeHistory.action" formName="changeHistorySearchForm" field="<%=AffiliateChangeCriteria.SORT_BY_CHANGED_DATE%>">Changed Date</afscme:sortLink>
                </TH>
            </TR>
            <logic:iterate name="changeHistorySearchForm" property="results" id="result">
                <TR>
                    <TD align="center">
                        <afscme:link page="/viewAffiliateChangeHistoryDetail.action" name="result" property="attributes" styleClass="action" title="See history of changes for this Section on this Change Date.">View</afscme:link>
                    </TD>
                    <TD align="center">
                        <afscme:codeWrite name="result" property="sectionCodePk" codeType="AffiliateSections" format="{1}"/>
                    </TD>
                    <TD align="center">
                        <afscme:dateWrite name="result" property="changedDate"/>
                    </TD>
                </TR>
            </logic:iterate>
        </TABLE>
        <TABLE cellpadding="0" cellspacing="0" border="0" class="BodyContent" align="center">
            <TR>
                <TD align="left">
                     <afscme:searchNav formName="changeHistorySearchForm" action="/searchAffiliateChangeHistory.action"/>
                </TD>
            </TR>
            <TR>
                <TD class="ContentHeaderTR">
                    <BR>
                    <afscme:currentAffiliate />
                </TD>
            </TR>
        </TABLE>
        <%@ include file="/include/affiliate_tab.inc" %>
    </logic:present>
</logic:equal>
</html:form>
<!-- end center table -->

<%@ include file="../include/footer.inc" %> 