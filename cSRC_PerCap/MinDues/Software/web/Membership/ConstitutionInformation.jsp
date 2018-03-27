<%! String title = "Constitution Information", help = "ConstitutionInformation.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="constitutionForm" id="constitutionForm" type="org.afscme.enterprise.affiliate.web.ConstitutionForm"/>

<!-- Something for tabs. -->
<bean:define id="screen" value="ConstitutionInformation"/>
<%@ include file="../include/affiliate_tab.inc" %>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>

<!-- Start Main Content -->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD width="45%">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD class="ContentHeaderTD">Approved Constitution</TD>
                    <TD class="ContentTD">
                        <html:checkbox name="constitutionForm" property="approvedConstitution" disabled="true"/>
                    </TD>
                </TR>
                <TR>
                    <TD width="50%" class="ContentHeaderTD">Automatic Delegate Provision</TD>
                    <TD width="50%" class="ContentTD">
                        <html:checkbox name="constitutionForm" property="automaticDelegateProvision" disabled="true"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Most Current Approval Date</TD>
                    <TD class="ContentTD">
                        <bean:write name="constitutionForm" property="mostCurrentApprovalDate"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Affiliation Agreement Date</TD>
                    <TD class="ContentTD">
                        <bean:write name="constitutionForm" property="affiliationAgreementDate"/>
                    </TD>
                </TR>
            </TABLE>
        </TD>
        <TD width="55%">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD width="60%" class="ContentHeaderTD">Method of Officer Election</TD>
                    <TD width="40%" class="ContentTD">
                        <afscme:codeWrite name="constitutionForm" property="methodOfOfficerElection" codeType="MethodOffcrElection" format="{1}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Constitutional Regions</TD>
                    <TD class="smallFont">
                        <html:checkbox name="constitutionForm" property="constitutionalRegions" disabled="true"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Meeting Frequency for Affiliate Meetings</TD>
                    <TD class="ContentTD">
                        <afscme:codeWrite name="constitutionForm" property="meetingFrequency" codeType="MeetingFrequency" format="{1}"/>
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
    <TR valign="top">
        <TD colspan="2">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD class="ContentTD">
                        <logic:equal name="constitutionForm" property="documentUploaded" value="true">
                            <A href='javascript:openDocument()' class='action' title='Get Constitution'>Constitution Document</A> 
                        </logic:equal>
                        <logic:equal name="constitutionForm" property="documentUploaded" value="false">
                            <bean:message key="message.constitution.noDocument"/>
                        </logic:equal>
                    </TD>
                    <TD class="ContentTD">&nbsp;</TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
</TABLE>
<!-- End Main Content -->

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR">
            <BR>
            <afscme:currentAffiliate />
        </TD>
    </TR>
</TABLE>

<!-- Something for tabs. -->
<%@ include file="/include/affiliate_tab.inc" %>

<%@ include file="../include/footer.inc" %> 