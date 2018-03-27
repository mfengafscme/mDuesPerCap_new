<%! String title = "Apply Update Confirmation", help = "ApplyUpdateConfirmation.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="90%" align="center">
    <TR align="center">
        <TD class="ContentHeaderTD">
            <BR>
            The Update has been scheduled to run for the selected Affiliates. An 
            email will be sent once the Update has been applied to the system. 
            <BR>
            <BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="90%" align="center">
    <TR valign="top">
        <TD align="right">
            <BR>
            <afscme:button action="/viewApplyUpdateQueue.action">Return to Pending Update Lists</afscme:button>
            <BR>
            <BR> 
        </TD>
    </TR>
</TABLE>


<%@ include file="../include/footer.inc" %> 
