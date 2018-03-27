<%! String title = "Add Employer", help = "AffiliateDetailAdd.html";%>
<% request.setAttribute("onload", "initForm();"); %> 
<%@ include file="../include/minimumduesheader.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<script type="text/javascript" language="JavaScript" src="js/add_employer_form.js"></script>

<br>
<img src="images/yellow.jpg" width="7" height="11" border="1"> &nbsp;&nbsp;Required Field
<br><br>
<html:form action="/AddEmployer.do">

    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <BR>
				 <html:submit property="addEmployerButton" styleClass="button" onclick="return checkAllFieldsBeforeSubmit()">Add</html:submit>
                <BR> <BR> 
            </TD>
            <TD align="right">
                <BR> 
                <html:reset styleClass="button"/>
                <afscme:button action="/showMain.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
 
 
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
       <TR valign="top">  
       <TD colspan="2" class="ContentHeaderTR"> 
       <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
           <tr> 
             <th colspan="6" align="left">Employer Identifier</th>
           </tr>
           <tr> 
             <th width="17%">Type</th>
             <th width="17%">Local</th>
             <th width="24%">State</th>
             <th width="17%">Subunit</th>
             <th width="25%">Council</th>
           </tr>
           
           <tr> 
		<td align="center" class="ContentTD"> 
		  <html:select property="affIdType" onblur="empTypeChanged();" onchange="empTypeChanged();">
			<html:option value="L">L</html:option>
			<html:option value="U">U</html:option>
		  </html:select>
		</td>
		<td align="center" class="ContentTD"> 
		  <html:text property="affIdLocal" onblur="checkNumeric(this,-999999999,999999999,',','.','');" size="4" maxlength="4"/> 
		</td>

		<td align="center" class="ContentTD"> 
		  <html:select property="affIdState">
		    <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/> 
		  </html:select> 
		</td>
		<td align="center" class="ContentTD"> <html:text property="affIdSubUnit" size="10" maxlength="10" onblur="changeAffIdType();"/> 
		</td>
		<td align="center" class="ContentTD"> <html:text property="affIdCouncil" onblur="checkNumeric(this,-999999999,999999999,',','.','');" size="4" maxlength="4"/> 
		</td>


           </tr>
           
           
           
           <tr> 
             <td> <html:errors property="affIdType"/> </td>
             <td align="center"> <html:errors property="affIdLocal"/> </td>
             <td align="center"> <html:errors property="affIdState"/> </td>
             <td align="center"> <html:errors property="affIdSubUnit"/> </td>
             <td align="center"> <html:errors property="affIdCouncil"/> </td>
           </tr>
         </table>
         </TD>
         </TR>
    </TABLE>
 
 
 
 
 
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD width="70%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="20%" class="ContentHeaderTD">
                                Employer Name 
                        </TD>
                        <TD width="80%" class="ContentTD">
                            <html:text property="affilName" size="60" maxlength="60"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="affilName"/>
                        </TD>
                    </TR>
            </TABLE>
            </TD>
        </TR>
    </TABLE>
       
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <BR>
                <html:submit property="addEmployerButton" styleClass="button" onclick="return checkAllFieldsBeforeSubmit()">Add</html:submit>
                <BR> <BR> 
            </TD>
            <TD align="right">
                <BR> 
                <html:reset styleClass="button"/>
                <afscme:button action="/showMain.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align="center">
                <!-- <BR><B><I>* Indicates Required Fields</I></B> -->
                <BR>
            </TD>
        </TR>
    </TABLE>
</html:form>

<html:errors />
<br><br><br>
<%@ include file="../include/footer.inc" %> 
