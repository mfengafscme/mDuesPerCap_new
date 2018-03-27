<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%! String title = "Upload Participation Data", help = "UploadParticipationData.html";%>
<%@ include file="../include/header.inc" %> 

<html:form action="/uploadParticipation" enctype="multipart/form-data" method="post" styleId="form" focus="affType">

    <table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr>
            <td align="center">
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </td>
        </tr>
    </table>

    <table cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="65%" align="center">
        <tr>
            <td width="40%" height="48" class="ContentHeaderTD" rowspan="2">
                <label for="fileName">* Enter file name or browse to find it</label> 
            </td>
            <td width="60%"> 
                <html:file property="file" styleId="fileName" size="30"/>
            </td>
        </tr>
        <tr>
            <td>
                <html:errors property="file"/>
            </td>
        </tr>
        <tr>
            <td class="ContentHeaderTD" rowspan="2">
                <label for="validDate">* Data valid as of</label> 
            </td>
            <td class="ContentTD">
                <html:text property="validDateStr" styleId="validDate" size="10"/>
                <a href="javascript:show_calendar('form.validDate');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width="24" height="22" border="0" alt="Calendar"/></a>
            </td>
        </tr>
        <tr>
            <td colspan="1">
                <html:errors property="validDateStr"/>
            </td>
        </tr>
    </table>

    <table cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="65%" align="center">
        <tr valign="top">
            <td>
                <BR>
                <html:submit styleClass="button" value="Perform Upload"/>
                <BR><BR> 
            </td>
            <td align="right">
                <BR>
                <afscme:button action="/showMain.action">Cancel</afscme:button>
            </td>
        </tr>
    </table>
    <table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr>
            <td align="center">
                <BR><B><I>* Indicates Required Fields</I></B><BR>
            </td>
        </tr>
    </table>

</html:form>

<%@ include file="../include/footer.inc" %>
