<%! String title = "Membership Reporting Information", help = "MembershipReportingInformation.html";%>
<%@ include file="../include/header.inc" %>

<!-- Something for tabs. -->
<bean:define id="screen" value="MembershipReportingInformation"/>
<%@ include file="../include/affiliate_tab.inc" %>

<bean:define name="mrData" id="mrData" type="org.afscme.enterprise.affiliate.MRData"/>

	<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<tr valign="top">
			<td class="ContentHeaderTR">
				<afscme:currentAffiliate/><br> <br> 
			</td>

		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<tr>
			<td width="30%" class="ContentHeaderTD">
				Primary Information Source 
			</td>
			<td width="70%" class="ContentTD">
				<afscme:codeWrite name="mrData" property="informationSource" codeType="InformationSource" format="{1}"/>
			</td>

		</tr>
		<tr>
			<td class="ContentHeaderTD">
				Affiliate Identifier 
			</td>
			<td class="ContentTD">
				<%=mrData.getNewAffiliateId().toDisplayString()%>
			</td>
		</tr>
		<tr>

			<td class="ContentHeaderTD">
				Affiliate Status 
			</td>
			<td class="ContentTD">
				<afscme:codeWrite name="mrData" property="affStatus" codeType="AffiliateStatus" format="{1}"/>
			</td>
		</tr>
		<tr>
			<td class="ContentHeaderTD">
				Unit Wide No Member Cards 
			</td>

			<td class="ContentTD">
				<html:checkbox name="mrData" property="noCards" disabled="true"/>
			</td>
		</tr>
		<tr>
			<td class="ContentHeaderTD">
				Unit Wide No PE Mail
			</td>
			<td class="ContentTD">
				<html:checkbox name="mrData" property="noPEMail" disabled="true"/>
			</td>

		</tr>
		<tr>
			<th align="left" colspan="3">
				Comments 
			</th>
		</tr>
		<tr>
			<td class="ContentTD" colspan="2">
                <pre><bean:write name="mrData" property="comment"/></pre>
			</td>

		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<tr valign="top">
			<td align="right">
                            <br> <font size="4">*</font>
                            <afscme:button action="/viewEnableMassChange.action">Perform Mass Change</afscme:button>
                            <br> <br> 
			</td>

		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="56%" align="center">
		<tr>
			<td align="center" class="smallFontI">
				* Note: Performing a Mass Change on any of these fields, will apply that change to all 
				Affiliates, below this one, in the hierarchy. To change a field for this Affiliate only, 
				use the 'Edit' tab above. 
			</td>
		</tr>
	</table>


<%@ include file="../include/footer.inc" %> 
