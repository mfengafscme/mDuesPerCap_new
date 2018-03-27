<%! String title = "Email Address Maintenance", help = "EmailAddressMaintenance.html";%>
<!-- Header -->
<%@ include file="../include/header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<!-- Form -->
<bean:define name="emailAddress" id="emailAddress" scope="request" type="org.afscme.enterprise.person.web.EmailAddressForm"/>


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
            <TH align="left" colspan="8"><afscme:codeWrite name="emailAddress" property="emailType1" codeType="EmailType" format="{1}"/></TH>
        </TR>
        <%String rtnDelete = "/deleteEmailAddresses.action?back="+request.getAttribute("back");%>
        <TR valign="top">
            <TD width="5%" ALIGN="center" class="ContentHeaderTD">
                <afscme:link page="<%=rtnDelete%>" paramId="emailPk" paramName="emailAddress" paramProperty="emailPk1" title="Delete this Address" confirm="Are you sure you want to delete this Email Address?">Delete</afscme:link>
            </TD>
            <TD>
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH width="60%">Email Address</TH>
                        <TH WIDTH="20%">Bad</TH>
                        <TH WIDTH="20%">Date Marked Bad</TH>
                    </TR>
                    <TR>
                        <TD align="center"><bean:write name="emailAddress" property="personEmailAddr1"/></TD>
                        <TD align="center"><html:checkbox name="emailAddress" property="emailBadFg1" disabled="true"/></TD>
                        <TD align="center"><afscme:dateWrite name="emailAddress" property="emailMarkedBadDt1"/></TD>
                    </TR>
                    <TR>
                        <TD colspan="4">
                            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TD colspan="8"><HR></TD>
                                </TR>
                                 <TR>
                                    <TD class="ContentHeaderTD">Date Created</TD>
                                    <TD class="ContentTD"><afscme:dateWrite name="emailAddress" property="theRecordData1.createdDate"/></TD>
                                    <TD class="ContentHeaderTD"><LABEL for="label_CreatedByID_1">Created By</LABEL></TD>
                                    <TD class="ContentTD"><afscme:userWrite name="emailAddress" property="theRecordData1.createdBy"/></TD>
                                    <TD class="ContentHeaderTD"><LABEL for="label_LastUpdate_1">Last Updated</LABEL></TD>
                                    <TD class="ContentTD"><afscme:dateWrite name="emailAddress" property="theRecordData1.modifiedDate"/></TD>
                                    <TD class="ContentHeaderTD"><LABEL for="label_LastUpdateUserID_1">Updated By</LABEL></TD>
                                    <TD class="ContentTD"><afscme:userWrite name="emailAddress" property="theRecordData1.modifiedBy"/></TD>
                                </TR>
                            </TABLE>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>

        <TR>
            <TH align="left" colspan="8"><afscme:codeWrite name="emailAddress" property="emailType2" codeType="EmailType" format="{1}"/></TH>
        </TR>
        <TR valign="top">
            <TD width="5%" ALIGN="center" class="ContentHeaderTD">
                <afscme:link page="<%=rtnDelete%>" paramId="emailPk" paramName="emailAddress" paramProperty="emailPk2" title="Delete this Address" confirm="Are you sure you want to delete this Email Address?">Delete</afscme:link>
            </TD>
            <TD>
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH width="60%">Email Address</TH>
                        <TH WIDTH="20%">Bad</TH>
                        <TH WIDTH="20%">Date Marked Bad</TH>
                    </TR>
                    <TR>
                        <TD align="center"><bean:write name="emailAddress" property="personEmailAddr2"/></TD>
                        <TD align="center"><html:checkbox name="emailAddress" property="emailBadFg2" disabled="true"/></TD>
                        <TD align="center"><afscme:dateWrite name="emailAddress" property="emailMarkedBadDt2"/></TD>
                    </TR>
                    <TR>
                        <TD colspan="4">
                            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TD colspan="8"><HR></TD>
                                </TR>
                                 <TR>
                                    <TD class="ContentHeaderTD">Date Created</TD>
                                    <TD class="ContentTD"><afscme:dateWrite name="emailAddress" property="theRecordData2.createdDate"/></TD>
                                    <TD class="ContentHeaderTD"><LABEL for="label_CreatedByID_2">Created By</LABEL></TD>
                                    <TD class="ContentTD"><afscme:userWrite name="emailAddress" property="theRecordData2.createdBy"/></TD>
                                    <TD class="ContentHeaderTD"><LABEL for="label_LastUpdate_2">Last Updated</LABEL></TD>
                                    <TD class="ContentTD"><afscme:dateWrite name="emailAddress" property="theRecordData2.modifiedDate"/></TD>
                                    <TD class="ContentHeaderTD"><LABEL for="label_LastUpdateUserID_2">Updated By</LABEL></TD>
                                    <TD class="ContentTD"><afscme:userWrite name="emailAddress" property="theRecordData2.modifiedBy"/></TD>
                                </TR>
                            </TABLE>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>

    
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <bean:define id="action" name="emailAddress" property="returnAction" />
        <TR valign="top">
            <TD align="left">
                 <BR> <afscme:button action="<%=action.toString()%>">Return</afscme:button>
            </TD>
            <TD align="right">
                <%String rtnEdit = "/editEmailAddresses.action?back="+request.getAttribute("back");%>
                <BR> <afscme:button page="<%=rtnEdit%>">Edit</afscme:button>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
<!-- Footer -->
<%@ include file="../include/footer.inc" %> 