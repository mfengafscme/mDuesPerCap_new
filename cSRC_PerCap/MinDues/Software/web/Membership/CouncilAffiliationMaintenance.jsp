<%! String title = "Council Affiliation Maintenance", help = "CouncilAffiliationMaintenance.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<!-- Start Main Content -->
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD align="center">
            <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
        </TD>
    </TR>
</TABLE>
<html:form action="saveCouncilAffiliation.action" focus="affIdType" >
    <html:hidden name="councilAffiliationForm" property="affPk"/>
    <html:hidden name="councilAffiliationForm" property="affIdLocal"/>
    <html:hidden name="councilAffiliationForm" property="affIdSubUnit"/>
    <html:hidden name="councilAffiliationForm" property="affIdCode"/>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD class="ContentHeaderTR">
                <afscme:currentAffiliate />
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR>
            <TH ALIGN="left">New Council</TH>
        </TR>
        <TR VALIGN="top">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH class="small" width="30%">Type</TH>
                        <TH class="small" width="30%">* State/National Type</TH>
                        <TH class="small" width="30%">* Council/Retiree Chapter</TH>
                        <TH class="small" width="10%">Select</TH>
                    </TR>
                    <TR class="ContentTD">
                        <TD align="center">
     			    <bean:write name="councilAffiliationForm" property="affIdType" />
     			    <html:hidden name="councilAffiliationForm" property="affIdType" />
                        </TD>
                        <TD align="center">
                            <html:select name="councilAffiliationForm" property="affIdState" onchange="clearHiddenFieldsAff(this.form);">
                                <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center">
                            <html:text name="councilAffiliationForm" property="affIdCouncil" size="4" maxlength="4" onchange="clearHiddenFieldsAff(this.form);"/>
                        </TD>
                        <TD align="center">
                            <afscme:affiliateFinder formName="councilAffiliationForm" styleClass="action"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD align="center">
                            <html:errors property="affIdType"/>
                        </TD>
                        <TD align="center">
                            <html:errors property="affIdState"/>
                        </TD>
                        <TD align="center">
                            <html:errors property="affIdCouncil"/>
                        </TD>
                        <TD>&nbsp;</TD>
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
                <BR>
                <BR>
            </TD>
            <TD align="right">
                <BR>
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewCharterInformation.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></TD>
        </TR>
    </TABLE>
</html:form>

<!-- End Main Content -->
<%@ include file="../include/footer.inc" %> 