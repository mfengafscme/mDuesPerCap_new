<%! String title = "Apply Update Reject File", help = "ApplyUpdateRejectFile.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@page import="java.util.*"%>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD align="center">
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>


<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR>
        <TD >
                <BR> 
        </TD>
    </TR>
    <SCRIPT language="JavaScript" src="../js/menu_help_row.js"></SCRIPT>
</TABLE>
<html:form action="viewApplyRebateRejectSummary.action" method="post">
<!--**************************************************************************//-->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR" valign="top">

            <afscme:currentAffiliate/><br>  

            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<bean:define id="queuePk"   name="queuePk" />
<bean:define id="affPk"     name="affPk" />



<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" width="93%" align="center">
    <TR>
        <TH align="left">
                * Please enter comments to indicate what was wrong with the update:<BR> 
        </TH>
    </TR>
    <TR>
        <TD align="center" class="ContentHeaderTD">
            <html:hidden    property="affPk"    value="<%=(Integer.toString(((Integer)affPk).intValue()))     %>" />
            <html:textarea  property="comments" cols="115" rows="3" ></html:textarea> 
            <html:hidden    property="queuePk"  value="<%=(queuePk).toString()   %>" />
            
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="100%" align="center">
    <TR valign="top">
        <TD align="left">
            <BR>
            <html:submit styleClass="button" value="continue"/>
        </TD>
        <TD align="right">
            <BR> 
            <afscme:button action="/viewApplyUpdateQueue.action" >Cancel</afscme:button>

            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
</html:form>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR>
        <TD align="center">
            <BR><B><I>* Indicates Required Fields</I></B>
            <BR>
        </TD>
    </TR>

</TABLE>
<%@ include file="../include/footer.inc" %> 
