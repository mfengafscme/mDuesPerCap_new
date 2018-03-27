<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Verify Person", help = "VerifyMember.html";%>
<%@ include file="../include/header.inc" %>

<!--This page allows criteria to be added to search for duplicate members. Very similar to VerifyPerson
except that affiate identifier and the affiliate finder functionality is included for saving an affiliate identifier 
for later   -->


<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>

<html:form action="viewVerifyMember" focus="firstNm">
    <html:hidden property="ssn"/>
    <html:hidden  property="affPk"/>

<!-- Display global errors -->		
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD align='center' 
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
                <BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <!-- can't figure out why there are two start table tags here, can only find one end table tag -->
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
                                <A href="javascript:show_calendar('verifyMemberForm.dob');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
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
        <TR align="center">
            <TD colspan="2" class="ContentHeaderTR">
		<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
		  <tr>
          	<th colspan="6">Fields below are not used as Search Criteria</th>
          </tr>
          <TR>
			<TH colspan="6" align="left">Affiliate Identifier - <afscme:affiliateFinder formName="verifyMemberForm" affIdTypeParam="affType" affIdCouncilParam="affCouncilRetireeChap" affIdLocalParam="affLocalSubChapter" affIdStateParam="affStateNatType" affIdSubUnitParam="affSubUnit" affIdCodeParam="affCode"/></TH>
                  </TR>
		  <TR>
			<TH width="14%">Type</TH>
			<TH width="14%">Local/Sub Chapter</TH>
			<TH width="20%">State/National Type</TH>
			<TH width="14%">Sub Unit</TH>
			<TH width="29%">Council/Retiree Chapter</TH>
                  </TR>
		  <TR>
                        <TD align="center" class="ContentTD">
						
                            <html:select property="affType" onchange="clearHiddenFields(this.form);">

                                <afscme:codeOptions useCode="true" codeType="AffiliateType" format="{0}" allowNull="true" nullDisplay="" excludeCodes="C"/>							
                            </html:select>
                        </TD>

						
                        <TD align="center" class="ContentTD" >
                            <html:text property="affLocalSubChapter" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="affStateNatType" onchange="clearHiddenFields(this.form);">
                               <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select></TD>
                        <TD align="center" class="ContentTD"> 
                             <html:text property="affSubUnit" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                             <html:text property="affCouncilRetireeChap" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/>
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
