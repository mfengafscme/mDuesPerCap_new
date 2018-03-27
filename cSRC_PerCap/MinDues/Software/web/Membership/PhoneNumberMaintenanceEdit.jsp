<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<bean:define name="phoneNumberForm" id="form" type="org.afscme.enterprise.person.web.PhoneNumberForm"/>
<%!String title, help;%>
<%title = "Phone Number Maintenance - " + (form.isAdd() ? "Add" : "Edit");%>
<%help = "PhoneNumberMaintenance" + (form.isAdd() ? "Add" : "Edit") + ".html";%>
<!-- Header -->
<%@ include file="../include/header.inc" %>
<!-- Form -->
<%String rtnSave = "savePhoneNumber.action?back="+request.getAttribute("back");%>
<%String rtnCancel = "/viewPhoneNumberInformation.action?back="+request.getAttribute("back");%>
<html:form action="<%=rtnSave%>" focus="phonePrmryFg">
    <html:hidden property="personPk"/>
    <html:hidden property="phonePk"/>
    <html:hidden property="dept"/> 
    <html:hidden property="phoneExtension"/>

<!-- Display global errors -->		
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
    </TABLE>
<!-- Set the person header -->		
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD class="ContentHeaderTR">
                <afscme:currentPersonName />
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
<!-- Display phone numbers -->	
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH width="15%">Primary</TH>
                        <TH width="25%">* Type</TH>
                        <TH width="15%">Private</TH>
                        <TH width="15%">DO NOT CALL</TH>
                        <TH width="15%">Bad</TH>
                        <TH width="15%">Date Marked Bad</TH>
                    </TR>
                    <TR>
                        <TD class="ContentTD" align="center">
                            <html:checkbox name="phoneNumberForm" property="phonePrmryFg"/>
                        </TD>
                        <TD class="ContentTD" align="center">
                            <html:select property="phoneType">
                                <afscme:codeOptions codeType="PhoneType" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                            </html:select>
                        </TD>
                        <TD class="ContentTD" align="center">
                            <html:checkbox name="phoneNumberForm" property="phonePrivateFg"/>
                        </TD>
                        <TD class="ContentTD" align="center">
                            <html:checkbox name="phoneNumberForm" property="phoneDoNotCallFg" disabled="<%=form.isLocked()%>"/>
                        </TD>
                        <TD class="ContentTD" align="center">
                            <html:checkbox name="phoneNumberForm" property="phoneBadFlag" disabled="<%=form.isAdd()%>"/>
                        </TD>
                        <TD class="ContentTD" align="center">
                            <afscme:dateWrite name="phoneNumberForm" property="phoneBadDate" writeHidden="true"/>
                        </TD>
                    </TR>
<!-- Display validation errors -->		
                    <TR>
                        <TD align="center"><html:errors property="phonePrmryFg"/></TD>
                        <TD align="center"><html:errors property="phoneType"/></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                    </TR>
                    <TR>
                        <TH COLSPAN="2">Country Code</TH>
                        <TH COLSPAN="2">Area Code</TH>
                        <TH COLSPAN="2">* Number</TH>
                    </TR>
                    <TR>
                        <TD class="ContentTD" align="center" colspan="2">
                            <html:text name="phoneNumberForm" property="countryCode" size="5" maxlength="5"/>
                        </TD>
                        <TD class="ContentTD" align="center" colspan="2">
                            <html:text name="phoneNumberForm" property="areaCode" size="3" maxlength="3"/>
                        </TD>
                        <TD class="ContentTD" align="center" colspan="2">
                            <html:text name="phoneNumberForm" property="phoneNumber" size="15" maxlength="15"/>
                        </TD>
                    </TR>
<!-- Display validation errors -->		
                    <TR>
                        <TD align="center" colspan="2"></TD>
                        <TD align="center" colspan="2"><html:errors property="areaCode"/></TD>
                        <TD align="center" colspan="2"><html:errors property="phoneNumber"/></TD>
                    </TR>
                    <TR>
                        <TD colspan="6">
                            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TD colspan="8">
                                        <HR>
                                    </TD>
                                </TR>
                                <TR>
                                    <TD width="15%" class="ContentHeaderTD">
                                        Date Created
                                    </TD>
                                    <TD width="11%" class="ContentTD">
                                        <afscme:dateWrite name="phoneNumberForm" property="theRecordData.createdDate" writeHidden="true"/>
                                    </TD>
                                    <TD width="12%" class="ContentHeaderTD">
                                        <LABEL for="createdBy">Created By</LABEL>
                                    </TD>
                                    <TD width="9%" class="ContentTD">
                                        <afscme:userWrite name="phoneNumberForm" property="theRecordData.createdBy" writeHidden="true"/>
                                    </TD>
                                    <TD width="14%" class="ContentHeaderTD">
                                        <LABEL for="theRecordData.modifiedDate">Last Updated</LABEL>
                                    </TD>
                                    <TD width="11%" class="ContentTD">
                                        <afscme:dateWrite name="phoneNumberForm" property="theRecordData.modifiedDate" writeHidden="true"/>
                                    </TD>
                                    <TD width="13%" class="ContentHeaderTD">
                                        <LABEL for="theRecordData.modifiedBy">Updated By</LABEL>
                                    </TD>
                                    <TD width="15%" class="ContentTD">
                                        <afscme:userWrite name="phoneNumberForm" property="theRecordData.modifiedBy" writeHidden="true"/>
                                    </TD>
                                </TR>
                            </TABLE>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
<!-- Display buttons -->		
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


<%@ include file="../include/footer.inc" %>
