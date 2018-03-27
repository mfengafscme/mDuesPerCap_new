<%! String title = "Constitution Information Edit", help = "ConstitutionInformationEdit.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<!-- Start Main Content -->
<html:form action="saveConstitutionInformation.action" enctype="multipart/form-data" method="post" focus="mostCurrentApprovalDate">
    <html:hidden name="constitutionForm" property="affPk"/>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD width="45%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="50%" class="ContentHeaderTD">Approved Constitution</TD>
                        <TD width="50%" class="ContentTD">
                            <!-- 
                                This is calculated. If the Most Current Approval 
                                Date has data when this screen loads, then the flag 
                                is checked. If the user then submits the form, 
                                with the Most Current Approval Date empty, and the 
                                box checked, then an error message is generated, 
                                since once a constitution is approved, it's always 
                                approved.
                            -->
                            <html:checkbox name="constitutionForm" property="approvedConstitution" disabled="true"/>
                            <html:hidden name="constitutionForm" property="approvedConstitution"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="approvedConstitution"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Automatic Delegate Provision</TD>
                        <TD class="ContentTD">
                            <html:checkbox name="constitutionForm" property="automaticDelegateProvision" disabled="true"/>
                            <html:hidden name="constitutionForm" property="automaticDelegateProvision"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="automaticDelegateProvision"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Most Current Approval Date</TD>
                        <TD class="ContentTD">
                            <html:text name="constitutionForm" property="mostCurrentApprovalDate" size="10" maxlength="10"/>
                            <A href="javascript:show_calendar('constitutionForm.mostCurrentApprovalDate');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"> 
                            <IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="mostCurrentApprovalDate"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Affiliation Agreement Date</TD>
                        <TD class="ContentTD">
                            <html:text name="constitutionForm" property="affiliationAgreementDate" size="10" maxlength="10"/>
                            <A href="javascript:show_calendar('constitutionForm.affiliationAgreementDate');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"> 
                            <IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="affiliationAgreementDate"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
            <TD width="55%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="60%" class="ContentHeaderTD">Method of Officer Election</TD>
                        <TD width="40%" class="ContentTD">
                            <html:select name="constitutionForm" property="methodOfOfficerElection">
                                <afscme:codeOptions useCode="false" codeType="MethodOffcrElection" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="methodOfOfficerElection"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Constitutional Regions</TD>
                        <TD class="smallFont">
                            <html:checkbox name="constitutionForm" property="constitutionalRegions"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="constitutionalRegions"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Meeting Frequency for Affiliate Meetings</TD>
                        <TD class="ContentTD">
                            <html:select name="constitutionForm" property="meetingFrequency">
                                <afscme:codeOptions useCode="false" codeType="MeetingFrequency" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="meetingFrequency"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR valign="top">
            <TD colspan="2">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD class="ContentHeaderTD">Constitution Document</TD>
                        <TD class="ContentTD">
                            <!--html:text name="constitutionForm" property="constitutionDocumentName" size="30"/-->
                            <!--INPUT type="button" class="BUTTON" value="Browse..." onclick="constitutionForm.constitutionDocument.click();"--> 
                            <!--html:file name="constitutionForm" property="constitutionDocument" style="VISIBILITY: hidden" size="30" onchange="constitutionForm.constitutionDocumentName.value=constitutionForm.constitutionDocument.value"/-->
                            <html:file name="constitutionForm" property="constitutionDocument" size="30"/>
                        </TD>                             
                    </TR>
                    <tr>
                        <TD colspan='2'>
		             <html:errors property="constitutionDocument"/>
                        </TD> 
                    </tr>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <BR>
                <html:submit styleClass="button"/>
                <BR> <BR> 
            </TD>
            <TD align="right">
                <BR>
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewConstitutionInformation.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
</html:form>
<!-- End Main Content -->
<%@ include file="../include/footer.inc" %> 
