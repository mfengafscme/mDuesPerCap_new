<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%! String title = "Upload Affiliate Officer Data", help = "UploadAffliateOfficerData.html";%>
<%@ include file="../include/header.inc" %> 


<SCRIPT language="JavaScript" src="../js/membership.js"></SCRIPT>
<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>

<html:form action="/uploadAffiliateOfficer" enctype="multipart/form-data" method="post" styleId="form">
	<table cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="60%" align="center">
		<tr>
			<td colspan="2">
				<table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<tr>
						<th colspan="5" align="left">
							* Affiliate Identifier - <!-- <A href="javascript:showAffiliateIDResults(form.affiliateType, form.affiliateLocal, form.affiliateState, form.affiliateSubunit, form.affiliateCouncil);" class="tr" title="Retrieve your Affilitate Identifier">Finder</A> --> Finder
						</th>
					</tr>
					<tr>
						<th width="14%" class="small">
							Type 
						</th>
						<th width="16%" class="small">
							Local/Sub Chapter 
						</th>
						<th width="22%" class="small">
							State/National Type 
						</th>
						<th width="16%" class="small">
							Sub Unit 
						</th>
						<th width="32%" class="small">
							Council/Retiree Chapter 
						</th>
					</tr>
					<tr>
						<td align="center" class="ContentTD">
              <html:select property="affType" styleId="affiliateType">
                <afscme:codeOptions codeType="AffiliateType" useCode="true" allowNull="true"/>
            </html:select> 
						</td>
						<td align="center" class="ContentTD"> 
              <html:text property="affLocal" value="" size="4" maxlength="4" styleId="affiliateLocal"/>
						</td>
						<td align="center" class="ContentTD">
              <html:select property="affState" styleId="affiliateState">
                <afscme:codeOptions codeType="AffiliateState" useCode="true" allowNull="true"/>
                </html:select> 
						</td>
						<td align="center" class="ContentTD">
              <html:text property="affSubunit" value="" size="4" maxlength="4" styleId="affiliateSubunit"/> 
						</td>
						<td align="center" class="ContentTD">
              <html:text property="affCouncil" value="" size="4" maxlength="4" styleId="affiliateCouncil"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="40%" height="48" class="ContentHeaderTD">
				<label for="fileName">* Enter file name or browse to find it</label> 
			</td>
			<td width="60%"> 
        <html:file property="file" styleId="fileName" size="30"/>
			</td>
		</tr>
		<tr>
			<td class="ContentHeaderTD">
				<label for="validDate">* Data valid as of</label> 
			</td>
			<td class="ContentTD">
        <html:text property="validDateStr" value="" styleId="validDate" size="10"/><a href="javascript:show_calendar('form.validDate');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width="24" height="22" border="0" alt="Calendar"/></a>
			</td>
		</tr>
		<tr>
			<td class="ContentHeaderTD">
				<label for="updateType">* Update Type</label> 
			</td>
			<td class="ContentTD">
        <html:select property="updateTypeCode" styleId="updateType">
          <html:option value="F">Full</html:option>
          <html:option value="P">Partical</html:option>
				</html:select> 
			</td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="60%" align="center">
		<tr valign="top">
			<td>
				<BR>
        <html:submit styleClass="button" value="Perform Upload"/>
				<BR><BR> 
			</td>
			<td align="right">
				<BR>
        <html:cancel styleClass="button">Cancel</html:cancel>
			</td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr>
      <td align="center">
        <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
      </td>
    </tr>
		<tr>
			<td align="center">
				<BR><B><I>* Indicates Required Fields</I></B>
				<BR>
			</td>
		</tr>
	</table>

</html:form>

<%@ include file="../include/footer.inc" %>


