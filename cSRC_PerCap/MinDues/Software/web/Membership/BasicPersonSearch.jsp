<%! String title = "Basic Person Search", help = "BasicPersonSearch.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD 
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>
<html:form action="searchPerson.action" focus="firstNm">
    <input type="hidden" name="reset">
    <html:hidden name="searchPersonForm" property="personPk"/>
    
   <!-- Search Criteria Tables -->
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="100%" align="center">
        <TR>
            <TD class="ContentHeaderTR">
                Fields to Search: <BR>
                &nbsp; 
            </TD>
        </TR>
       <TR valign="top">
        <!-- Name Inner Table -->
            <TD colspan="2" class="ContentHeaderTR">
               <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH width="28%">First Name</TH>
                        <TH width="28%">Middle Name</TH>
                        <TH width="28%">Last Name</TH>
                        <TH width="16%">Suffix</TH>
                    </TR>
                    <TR>
                        <TD nowrap align="center"><html:text property="firstNm" size="25" maxlength="25"/><html:errors property="firstNm"/></TD>
			<TD nowrap align="center"><html:text property="middleNm" size="25" maxlength="25"/></TD>
                	<TD nowrap align="center"><html:text property="lastNm" size="25" maxlength="25"/><html:errors property="lastNm"/></TD>
			<TD align="center"><html:select property="suffixNm">
                            <afscme:codeOptions codeType="Suffix" format="{1}" allowNull="true" nullDisplay=""/>
			 </html:select></TD>
                    </TR>
	 <!-- Display validation errors -->   
                    <TR>
                        <TD><html:errors property="firstNm"/></TD>
			<TD></TD>
                	<TD><html:errors property="lastNm"/></TD>
			<TD></TD>
                    </TR>
               </TABLE>
            </TD
         </TR>   
	<TR valign="top">
        <!-- General Member Information -->    
            <TD>
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
			<TD width="34%" class="ContentHeaderTD"><LABEL for="state"> State (System Mailing Address)</LABEL></TD>
			<TD width="66%" align="left">
                            <html:select property="state"><afscme:codeOptions useCode="true" codeType="State" format="{0}" allowNull="true" nullDisplay=""/>
                            </html:select>
			</TD>
                    </TR>
		</TABLE>
            </TD>
        </TR>
    </TABLE> <!-- end of criteria table -->

  <!-- Bottom Buttons -->
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="100%" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR> 
                <afscme:button action="/viewPowerPersonCriteria.action">Power Search</afscme:button>
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</html:form>

<!-- Footer include -->
<%@ include file="../include/footer.inc" %> 


