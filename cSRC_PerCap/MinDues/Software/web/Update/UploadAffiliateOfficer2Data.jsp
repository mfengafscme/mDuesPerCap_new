<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%! String title = "Upload Affiliate Officer Data", help = "UploadAffiliateOfficerData.html";%>
<%@ include file="../include/header.inc" %> 


<html:form action="/uploadAffiliateOfficer" enctype="multipart/form-data" method="post" styleId="form" focus="affType">
    <html:hidden property="affPk"/> 
    <html:hidden property="affCode"/> 
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="65%" align="center">
        <TR>
            <TD colspan="2">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH colspan="5" align="left">Affiliate Identifier - <afscme:affiliateFinder formName="affiliateFileUploadForm" affIdTypeParam="affType" affIdCouncilParam="affCouncil" affIdLocalParam="affLocal" affIdStateParam="affState" affIdSubUnitParam="affSubunit" affIdCodeParam="affCode"/></TH>
                    </TR>
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
                            <html:select property="affType" onchange="clearHiddenFields(this.form);">
                               <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD" >
                            <html:text property="affLocal" size="4" maxlength="4" onchange="clearHiddenFields(this.form);"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="affState" onchange="clearHiddenFields(this.form);">
                               <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD"> 
                            <html:text property="affSubunit" size="4" maxlength="4" onchange="clearHiddenFields(this.form);"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affCouncil" size="4" maxlength="4" onchange="clearHiddenFields(this.form);"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD align="center" class="ContentTD">
                            <html:errors property="affType"/>
                        </TD>
                        <TD align="center" class="ContentTD" >
                            <html:errors property="affLocal"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:errors property="affState"/>
                        </TD>
                        <TD align="center" class="ContentTD"> 
                            <html:errors property="affSubunit"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:errors property="affCouncil"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR>
            <TD width="40%" height="48" class="ContentHeaderTD" rowspan='2'>
                <label for="fileName">* Enter file name or browse to find it</label> 
            </TD>
            <TD width="60%"> 
                <html:file property="file" styleId="fileName" size="30"/>
            </TD>
        </TR>
        <TR>
            <TD>
                <html:errors property="file"/>
            </TD>
        </TR>
        <TR>
            <TD class="ContentHeaderTD" rowspan='2'>
                <label for="validDate">* Data valid as of</label> 
            </TD>
            <TD class="ContentTD">
                <html:text property="validDateStr" styleId="validDate" size="10"/>
                <a href="javascript:show_calendar('form.validDate');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width="24" height="22" border="0" alt="Calendar"/></a>
            </TD>
        </TR>
        <TR>
            <TD colspan='1'>
                <html:errors property="validDateStr"/>
            </TD>
        </TR>
        <TR>
            <TD class="ContentHeaderTD">
                <label for="updateType">* Update Type</label> 
            </TD>
            <TD class="ContentTD">
                <html:select property="updateType" styleId="updateType">
                    <afscme:codeOptions codeType="UpdateType" format="{1}"/>
                </html:select> 
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="65%" align="center">
        <TR valign="top">
            <TD>
                <BR>
                <html:submit styleClass="button" value="Perform Upload"/>
                <BR><BR> 
            </TD>
            <TD align="right">
                <BR>
                <afscme:button action="/showMain.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align="center">
                <BR><B><I>* Indicates Required Fields</I></B>
                <BR>
            </TD>
        </TR>
    </TABLE>
</html:form>

<%@ include file="../include/footer.inc" %>


<%@page contentType="text/html"%>
<html>
<head><title>JSP Page</title></head>
<body>

<%-- <jsp:useBean id="beanInstanceName" scope="session" class="package.class" /> --%>
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>

</body>
</html>
