<%! String title = "Email Address Maintenance Edit", help = "EmailAddressMaintenanceEdit.html";%>
<!-- Header -->
<% request.setAttribute("onload", "initializeFocus();"); %>
<%@ include file="../include/header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<!-- Form -->
<%String rtnSave = "saveEmailAddresses.action?back="+request.getAttribute("back");%>
<%String rtnCancel = "/viewEmailAddresses.action?back="+request.getAttribute("back");%>
<html:form action="<%=rtnSave%>">
    <html:hidden property="personPk"/>
    <html:hidden property="emailPk1"/>
    <html:hidden property="emailPk2"/>
    <html:hidden property="emailType1"/>
    <html:hidden property="emailType2"/>
    <html:hidden property="back"/>

<!-- Set the person header -->		
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD class="ContentHeaderTR">
                <afscme:currentPersonName />
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
<!-- Display the email addresses -->		
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
            <TR>
                <TH align="left" colspan="8"><afscme:codeWrite name="emailAddressForm" property="emailType1" codeType="EmailType" format="{1}"/></TH>
            </TR>
            <TR valign="top">
                <TD>
                    <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                        <TR>
                            <TH width="10%">Primary</TH>
                            <TH width="60%">Email Address</TH>
                            <TH WIDTH="15%">Bad</TH>
                            <TH WIDTH="15%">Date Marked Bad</TH>
                        </TR>
                        <TR>
                            <TD align="center"><html:radio name="emailAddressForm" property="isPrimary1" value="true"/></TD>
                            <TD align="center"><html:text name="emailAddressForm" property="personEmailAddr1" size="50" maxlength="50"/></TD>
                            <TD align="center"><html:checkbox name="emailAddressForm" property="emailBadFg1"/></TD>
                            <TD align="center"><afscme:dateWrite name="emailAddressForm" property="emailMarkedBadDt1"/></TD>
                        </TR>
                        <TR>
                            <TD colspan="4">
                                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                    <TR>
                                        <TD colspan="8"><HR></TD>
                                    </TR>
                                         <TR>
                                        <TD class="ContentHeaderTD">Date Created</TD>
                                        <TD class="ContentTD"><afscme:dateWrite name="emailAddressForm" property="theRecordData1.createdDate"/></TD>
                                        <TD class="ContentHeaderTD"><LABEL for="label_CreatedByID_1">Created By</LABEL></TD>
                                        <TD class="ContentTD"><afscme:userWrite name="emailAddressForm" property="theRecordData1.createdBy"/></TD>
                                        <TD class="ContentHeaderTD"><LABEL for="label_LastUpdate_1">Last Updated</LABEL></TD>
                                        <TD class="ContentTD"><afscme:dateWrite name="emailAddressForm" property="theRecordData1.modifiedDate"/></TD>
                                        <TD class="ContentHeaderTD"><LABEL for="label_LastUpdateUserID_1">Updated By</LABEL></TD>
                                        <TD class="ContentTD"><afscme:userWrite name="emailAddressForm" property="theRecordData1.modifiedBy"/></TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                    </TABLE>
                </TD>
            </TR>
            <TR>
                <TH align="left" colspan="8"><afscme:codeWrite name="emailAddressForm" property="emailType2" codeType="EmailType" format="{1}"/></TH>
            </TR>
            <TR valign="top">
                <TD>
                    <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                        <TR>
                            <TH width="10%">Primary</TH>
                            <TH width="60%">Email Address</TH>
                            <TH WIDTH="15%">Bad</TH>
                            <TH WIDTH="15%">Date Marked Bad</TH>
                        </TR>
                        <TR>
                            <TD align="center"><html:radio name="emailAddressForm" property="isPrimary1" value="false"/></TD>
                            <TD align="center"><html:text name="emailAddressForm" property="personEmailAddr2" size="50" maxlength="50"/></TD>
                            <TD align="center"><html:checkbox name="emailAddressForm" property="emailBadFg2"/></TD>
                            <TD align="center"><afscme:dateWrite name="emailAddressForm" property="emailMarkedBadDt2"/></TD>
                        </TR>
                        <TR>
                            <TD colspan="4">
                                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                    <TR>
                                        <TD colspan="8"><HR></TD>
                                    </TR>
                                         <TR>
                                        <TD class="ContentHeaderTD">Date Created</TD>
                                        <TD class="ContentTD"><afscme:dateWrite name="emailAddressForm" property="theRecordData2.createdDate"/></TD>
                                        <TD class="ContentHeaderTD"><LABEL for="label_CreatedByID_2">Created By</LABEL></TD>
                                        <TD class="ContentTD"><afscme:userWrite name="emailAddressForm" property="theRecordData2.createdBy"/></TD>
                                        <TD class="ContentHeaderTD"><LABEL for="label_LastUpdate_2">Last Updated</LABEL></TD>
                                        <TD class="ContentTD"><afscme:dateWrite name="emailAddressForm" property="theRecordData2.modifiedDate"/></TD>
                                        <TD class="ContentHeaderTD"><LABEL for="label_LastUpdateUserID_2">Updated By</LABEL></TD>
                                        <TD class="ContentTD"><afscme:userWrite name="emailAddressForm" property="theRecordData2.modifiedBy"/></TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                    </TABLE>
                </TD>
            </TR>

    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr valign="top">
            <td colspan="3"><BR></td>
        </tr>   
        <tr>
            <td align="left"><html:submit styleClass="button"/></td>
            <td align="right">
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="<%=rtnCancel%>">Cancel</afscme:button>
           </td>
        </tr>      
        <tr>
            <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
        </tr>
    </TABLE>
</html:form>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 