<%@ include file="../include/minimumduesheader.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Web Form Data Verification", help = "MainMenu.html";%>

 <body background="images/butterflies.jpg" onload="initForm();">
<script type="text/javascript" language="JavaScript" src="js/mdues_pre_affiliate_detail_edit.js"></script>

<bean:define name="preAffiliateDetailForm" id="adf" type="org.afscme.enterprise.affiliate.web.PreAffiliateDetailForm"/>
<BR>
<center><html:errors/></center>

<html:form action="/saveApproveImpRecd.do">

<html:hidden property="empAffPk"/>
<html:hidden property="affPk"/>
<html:hidden property="batch_ID"/>

<!-- Display global errors -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
</TABLE>
		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr>
            <td align="left">
            	<html:submit property="saveImpRecdBtn" styleClass="button">Save</html:submit>
            	<html:submit property="approveImpRecdBtn" styleClass="button">Approve</html:submit>
	    </td>
            <td align="right">
                <html:reset styleClass="button"/>&nbsp;
                <html:submit property="back" styleClass="button">Back</html:submit>
		<!-- <INPUT type="button" class="BUTTON" value="Cancel" onClick="history.go(-1);"> -->
           </td>
        </tr>      
        <tr valign="top">
            <td colspan="3"><BR></td>
        </tr>   
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
    	<TD class="ContentHeaderTR">
            <strong><bean:write name="preAffiliateDetailForm" scope="request" property="employerName"/></strong> <BR> 
            		State:	<bean:write name="preAffiliateDetailForm" scope="request" property="affIdState"/> &nbsp - &nbsp; &nbsp;
			Council: <bean:write name="preAffiliateDetailForm" scope="request" property="affIdCouncil"/> &nbsp - &nbsp; &nbsp;
			Local: <bean:write name="preAffiliateDetailForm" scope="request" property="affIdLocal"/> &nbsp - &nbsp; &nbsp;
			SubUnit: <bean:write name="preAffiliateDetailForm" scope="request" property="affIdSubUnit"/> &nbsp; &nbsp;
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
  <tr valign="top"> 
    <td colspan="2"> <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
        <tr> 
          <th align="left" colspan="4"> Detailed Information</th>
        </tr>
        <tr> 
          <td width="35%" class="ContentHeaderTD">Stat Member Count: </td>
          <td class="ContentTD"> 
			<bean:write name="preAffiliateDetailForm" scope="request" property="statMbrCount"/>
            </td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD">Increase Type: </td><td class="ContentTD"><html:text property="increase_type" styleId="increase_type" onchange="changeTypeSection(this.form)" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
          <td class="ContentHeaderTD">Number of Members and Fee Payers:</td><td class="ContentTD"><html:text property="noMemFeePayer" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
          </tr>
        <tr> 
          <td class="ContentHeaderTD">Agreement Effective Date:</td><td class="ContentTD"><html:text property="agmtEffDate" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
          <td class="ContentHeaderTD">Agreement Expiration Date:</td><td class="ContentTD"><html:text property="agmtExpDate" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
          </tr>
        <tr> 
          <td class="ContentHeaderTD">Members Afps Affected:</td><td class="ContentTD"><html:text property="mbrsAfps_Affected" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
          <td class="ContentHeaderTD"> Adjusted Members Afps Affected:</td><td class="ContentTD"><html:text property="adj_MbrsAfps_Affected" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
           </tr>
        <tr> 
          <td class="ContentHeaderTD">Did this unit receive a wage increase between 
            8/1/2016 and 7/31/2017?:</td><td class="ContentTD"><html:text property="ifRecInc" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
          <td class="ContentHeaderTD"> If No, is it because the unit is still in negotiations for that 
            period?:</td><td class="ContentTD"><html:text property="ifInNego" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD"> Contact Name:</td>
          <td class="ContentTD"><html:text property="contactName" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
          <td class="ContentHeaderTD"> Contact Phone or Email:
          </td><td class="ContentTD"><html:text property="contactPhoneEmail" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
          
        </tr>
		<tr><td class="ContentHeaderTD"> Comment - Web Form:</td><td colspan="3" class="ContentTD"><html:textarea property="comment" name="preAffiliateDetailForm" cols="125" rows="2"  disabled="true"/></td></tr>
		<tr><td class="ContentHeaderTD"> Notes:</td><td colspan="3" class="ContentTD"><html:textarea property="notes" name="preAffiliateDetailForm" cols="125" rows="2"/></td></tr>		
      </table></td>
  </tr>
  <TR><td colspan="3">&nbsp; &nbsp; <BR></td></TR>
  <tr valign="top"> 
    <td width="50%"> <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
        <tr> 
          <td class="ContentHeaderTD" width="50%">Percent Wage Increase:</td>
          <td class="ContentTD"><html:text property="percentWageInc" styleId="percentWageInc" name="preAffiliateDetailForm" size="35" maxlength="55"/> </td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD">Effective Date of Increase:</td>
          <td class="ContentTD"> <html:text property="wageIncEffDate" styleId="wageIncEffDate" name="preAffiliateDetailForm" size="35" maxlength="55"/> </td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD">Number of Members and Fee Payers Affected:</td>
          <td class="ContentTD"> <html:text property="noMemFeePayerAff1" styleId="noMemFeePayerAff1" name="preAffiliateDetailForm" size="35" maxlength="55"/> </td>
        </tr>
      </table></td>
    <td width="50%"> <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
        <tr> 
          <td width="55%" class="ContentHeaderTD"> Cents Per Hour or Dollar Lump 
            Sum Bonus:</td>
          <td class="ContentTD"><html:text property="centPerHrDoLumpSumBonus" styleId="centPerHrDoLumpSumBonus" name="preAffiliateDetailForm" size="35" maxlength="55"/>
          </td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD">Average Wage Per Hour or Year:</td>
          <td class="ContentTD"> <html:text property="avgWagePerHrYr" styleId="avgWagePerHrYr" name="preAffiliateDetailForm" size="35" maxlength="55"/> 
          </td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD">Effective Date of Increase:</td>
          <td class="ContentTD"> <html:text property="effDateInc" styleId="effDateInc" name="preAffiliateDetailForm" size="35" maxlength="55"/>
          </td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD">Number of Members and Fee Payers Affected:</td>
          <td class="ContentTD"> <html:text property="noMemFeePayerAff2" styleId="noMemFeePayerAff2" name="preAffiliateDetailForm" size="35" maxlength="55"/>
          </td>
        </tr>
      </table></td>
  </tr>
  <tr valign="top"> 
    <td width="50%"> <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
        <tr> 
          <td width="50%" class="ContentHeaderTD">Special Wage Adjustments: Type of Payment:</td>
          <td class="ContentTD"> <html:text property="speWageAgj" name="preAffiliateDetailForm" size="35" maxlength="55" disabled="true"/>
          </td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD">Percent Increase:</td>
          <td class="ContentTD"><html:text property="percentInc" name="preAffiliateDetailForm" size="35" maxlength="55" disabled="true"/></td>
        </tr>
      </table></td>
    <td width="50%"> <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
        <tr> 
          <td width="55%" class="ContentHeaderTD">Dollars / Cents: 
          <td class="ContentTD"> <html:text property="dollarCent" name="preAffiliateDetailForm" size="35" maxlength="55"/></td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD">Average Pay:</td>
          <td class="ContentTD"><html:text property="avgPay" name="preAffiliateDetailForm" size="35" maxlength="55"/> </td>
        </tr>
        <tr> 
          <td class="ContentHeaderTD">Number of Members and Fee Payers Affected:</td>
          <td class="ContentTD"> <html:text property="noMemFeePayerAff3" name="preAffiliateDetailForm" size="35" maxlength="55"/> </td>
        </tr>
      </table></td>
  </tr>
 
  </tr>

</table>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TD class="smallFont">
				<BR>
				Load ID: <bean:write name="preAffiliateDetailForm" scope="request" property="load_ID"/> &nbsp; &nbsp;&nbsp; &nbsp;
				Batch ID: <bean:write name="preAffiliateDetailForm" scope="request" property="batch_ID"/> &nbsp; &nbsp;&nbsp; &nbsp;
				Processed: <bean:write name="preAffiliateDetailForm" scope="request" property="processed"/> &nbsp; &nbsp;&nbsp; &nbsp;
				UserPosting: <bean:write name="preAffiliateDetailForm" scope="request" property="userPosting"/>  &nbsp; &nbsp;&nbsp; &nbsp;
				Do Not Process: <bean:write name="preAffiliateDetailForm" scope="request" property="doNotProcess"/> &nbsp; &nbsp;&nbsp; &nbsp;
				WifPk: <bean:write name="preAffiliateDetailForm" scope="request" property="wifPk"/> &nbsp; &nbsp;&nbsp; &nbsp;
				WidPk: <bean:write name="preAffiliateDetailForm" scope="request" property="widPk"/>
			</TD>
		</TR>
	</TABLE>
		
        <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
            <tr valign="top">
                <td colspan="3"><BR></td>
            </tr>   
			<tr>
				<td align="left">
					<html:submit styleClass="button"/>
					<afscme:button action="/viewMemberAffiliateInformation.action">Approve</afscme:button>
				</td>
				<td align="right">
					<html:reset styleClass="button"/>&nbsp;
					<INPUT type="button" class="BUTTON" value="Cancel" onClick="history.go(-1);">
			   </td>
			</tr>         
            <tr>
                <!-- Required Content -->
                
    <td colspan="3" align="center"><BR></td>
            </tr>
    </TABLE>
</html:form>	
</body>
<br>
<br>
<%@ include file="../include/footer.inc" %> 


