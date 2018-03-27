<%! String title = "Apply Update Officer Edit Detail Per Affiliate", help = "ApplyUpdateOfficerEditDetailPerAffiliate.html";%>
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
<bean:define    id="affPk"      name="affPk"/>
<bean:define    id="queuePk"    name="queuePk"   />
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD>
            <BR> <INPUT type="button" name="ReturnButton" class="BUTTON" value="Return" onClick="history.go(-1);"> 
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
            <TD class="ContentHeaderTR" valign="top">
                    <afscme:currentAffiliate/>  
                    <BR>
                    <BR>
            </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
            <TH colspan="5" align="left">
                    Summary of Officer Position Changes
            </TH>
    </TR>
    <TR>
            <TH width="30%">
                    Constitutional Title
            </TH>
            <TH width="30%">
                    Affiliate Title
            </TH>
            <TH width="13%">
                    Allowed for this Affiliate
            </TH>
            <TH width="13%">
                    Contained in the Update File
            </TH>
            <TH width="14%">
                    Term Expiration Date
            </TH>
    </TR>
    
        <bean:define id="pcResult" name="PCResults"/>
        <logic:iterate id="pcRes"  name="pcResult">
            <TR>
                <TD class="ContentTD" align="center">
                    <bean:write name="pcRes" property="value.constitutionalTitle"/>
                </TD>
                <TD class="ContentTD" align="center">
                    <bean:write name="pcRes" property="value.affiliateTitle"/>
                </TD>
                <TD class="ContentTD" align="center">
                    <bean:write name="pcRes" property="value.allowed"/>
                </TD>
                <TD class="ContentTD" align="center">
                    <bean:write name="pcRes" property="value.inFile"/>
                </TD>
                <TD class="ContentTD" align="center">
                    <bean:write name="pcRes" property="value.date"/>
                </TD>
            </TR>
        </logic:iterate>
    
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
            <TD class="ContentHeaderTR" valign="top">
                    <afscme:currentAffiliate/>  
                    <BR>
                    <BR>
            </TD>
    </TR>
</TABLE>
<!--************************************************************************//-->

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD>
            <BR> <INPUT type="button" name="ReturnButton" class="BUTTON" value="Return" onClick="history.go(-1);"> 
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>

<!--**************************************************************************//-->
</html:form>
<%@ include file="../include/footer.inc" %>     
