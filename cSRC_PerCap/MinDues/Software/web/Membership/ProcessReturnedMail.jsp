<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Process Returned Mail", help = "ProcessReturnedMail.html";%>
<%@ include file="../include/header.inc" %>

<SCRIPT language="JavaScript" src="../js/membership.js"></SCRIPT>

<html:form action="processReturnedMail" focus="addressIds">
        
    <TABLE class="ContentTableNoWidth" width="45%" align="center">
        <TR>
            <TD align="center" class="ContentHeaderTD" colspan="3">
                <LABEL for="label_AddressIds">* Address IDs</LABEL>
                    <BR><html:textarea property="addressIds" cols="54" rows="18" styleId="label_AddressIds"/>
                    <BR><FONT class="SmallFont">Note: Press the Enter key after each Address ID if entered manually.</FONT> 
                    <BR><BR>
            </TD>
        </TR>
        <tr>
            <td align="center" colspan="3">
                    <html:errors property="addressIds"/>
                    <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </td>
        </tr>
        <TR>
            <TD align="left">
                <html:submit property="submit" styleClass="button" value="Submit"/>
            </TD>
            <TD align="right" colspan="2">
		<afscme:button page="/viewBasicMemberCriteria.action?new">Search</afscme:button>
                <html:cancel styleClass="button" value="Cancel"/><BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE class="ContentTable" align="center">
        <TR>
            <TD align="center">
                <BR><BR><BR><B><I>* Indicates Required Fields</I></B><BR>
            </TD>
        </TR>
    </TABLE>

</html:form>
<%@ include file="../include/footer.inc" %>

