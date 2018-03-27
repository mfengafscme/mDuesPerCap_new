<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Verify Organization", help = "VerifyOrganization.html";%>
<%@ include file="../include/header.inc" %>

<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>

<html:form action="verifyOrganization" focus="orgName">
    
<table cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="60%" align="center">

    <tr valign="top">
	<td colspan="2" class="ContentHeaderTR">

        <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
            <tr>
                <th>* Organization Name</th>
            </tr>
            <tr>
                <td align="center">
                    <html:text property="orgName" size="29" maxlength="29"/>
                    <html:errors property="orgName"/></td>
            </tr>
        </table>
	</td>
    </tr>
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
            <BR><B><I>* Indicates Required Fields</I></B><BR>
	</td>
    </tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %> 
