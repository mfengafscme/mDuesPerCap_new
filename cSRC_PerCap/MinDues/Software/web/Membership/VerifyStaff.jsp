<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Verify Person", help = "VerifyStaff.html";%>
<%@ include file="../include/header.inc" %>

<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>

<html:form action="verifyStaff" focus="firstNm">

<!-- Display global errors -->		
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD align='center' 
                <html:errors/><BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    
    <table cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="60%" align="center">

        <tr valign="top">
            <td colspan="2" class="ContentHeaderTR">

                <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                            <TH width="35%">First Name</TH>
                            <TH width="35%">Last Name</TH>
                            <TH width="30%">Suffix</TH>
                    </TR>
                    <TR>
                        <TD align="center"><html:text property="firstNm" size="25" maxlength="25"/></TD>
                        <TD align="center"><html:text property="lastNm" size="25" maxlength="25"/></TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="suffixNm">
                                <afscme:codeOptions codeType="Suffix" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                            </html:select>
                        </TD>
                    </TR>
                </table>
            </td>
        </tr>
        <TR valign="top">
            <TD colspan="2" class="ContentHeaderTR">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="35%" class="ContentHeaderTD">Birth Date</TD>
                        <TD width="65%"><html:text property="dob" size="10" maxlength="10"/>
                                <A href="javascript:show_calendar('verifyStaffForm.dob');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Social Security Number</TD>
                        <TD>
                            <html:text property="ssn1" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/> - <html:text property="ssn2" size="2" maxlength="2" onkeyup="return autoTab(this, 2, event);"/> - <html:text property="ssn3" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </table>

<table cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="60%" align="center">
    <tr valign="top">
        <td colspan="2"><BR></td>
    </tr>     
    <tr>
        <td align="left"><html:submit styleClass="button"/></td>
        <td align="right"><html:reset styleClass="button"/></td>
    </tr>      
</table>

<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr>
        <td align="center">
            <BR><B><I>Note: Either 'First Name' and 'Last Name' or 'Social Security Number' is required</I></B><BR>
	</td>
    </tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %> 
