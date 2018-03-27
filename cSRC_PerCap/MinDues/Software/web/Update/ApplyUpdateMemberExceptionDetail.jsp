<%@page contentType="text/html"%>
<%! String title = "Apply Update Member Exception Detail", help = "ApplyUpdateMemberExceptionDetail.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@page import="java.util.*, org.afscme.enterprise.update.ExceptionComparison"%>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD align="center">
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR>
		<TD>
			<BR> 
		</TD>
	</TR>
	<SCRIPT language="JavaScript" src="../js/menu_help_row.js">
	</SCRIPT>
</TABLE>

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
                <afscme:currentAffiliate/><br>  
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">

    

    <TR>
            <TH colspan="3" align="left">
                    Exception Comparison 
            </TH>
    </TR>
    <bean:define id="fieldChanges" name="exception" />
    <bean:define id="recId" name="recId"/>
    <bean:define id="affPk" name="affPk"/>
    <TR>
            <TH width="20%">
                    Field
            </TH>
            <TH width="40%">
                    In File
            </TH>
            <TH width="40%">
                    In System
            </TH>
    </TR>
    <logic:iterate id="eComp1" name="fieldChanges"  >
        <logic:iterate id="ec" name="eComp1" property="value">
            <%if(((ExceptionComparison) ec).getRecordId() == (new Integer(recId.toString()).intValue())){%>
                <TR>
                    <!--Display in red if the error flag is true/-->
                    <logic:equal name="ec" property="error" value="true">
                        <TD align="middle" >
                            <font color="red"><b><em>
                                <bean:write name="ec" property="field" />
                                &nbsp;
                            </b></em></font>
                        </TD>
                    </logic:equal>
                    <!--Display normal if the error flag is false/-->
                    <logic:equal name="ec" property="error" value="false">
                        <TD align="middle" >
                                <bean:write name="ec" property="field" />
                                &nbsp;
                        </TD>
                    </logic:equal>

                    <TD align="middle" >
                        <bean:write name="ec" property="valueInFile" />
                        &nbsp;
                    </TD>



                    <TD align="middle">
                            <bean:write name="ec" property="valueInSystem" />
                            &nbsp;
                    </TD>
                </TR>
            <%}%>
        </logic:iterate>
   </logic:iterate>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
                <TD>
                        <BR> <INPUT type="button" name="ReturnButton" class="BUTTON" value="Return" onClick="history.go(-1);"> 
                        <BR> <BR> 
                </TD>
        </TR>
</TABLE>

<%@ include file="../include/footer.inc" %> 
