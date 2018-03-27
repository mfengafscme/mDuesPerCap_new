<%! String title = "Membership Reporting Information Edit", help = "MembershipReportingInformationEdit.html";%>
<%@ include file="../include/header.inc" %>


<bean:define name="membershipReportingInformationForm" property="mrData" id="mrData" type="org.afscme.enterprise.affiliate.MRData"/>
<html:form action="saveMembershipReportingInformation">
        <bean:define id="memRepData" name="membershipReportingInformationForm" property="mrData" type="org.afscme.enterprise.affiliate.MRData" />
        <bean:define id="newAffiliateId" name="memRepData" property="newAffiliateId" type="org.afscme.enterprise.affiliate.AffiliateIdentifier" />
	<html:hidden name="newAffiliateId" property="type" />
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
                	    <html:select property="mrData.informationSource">
    				<afscme:codeOptions allowNull="true" codeType="InformationSource" format="{1}"/>
                            </html:select>
			</td>

		</tr>
		<tr>
			<td class="ContentHeaderTD">
				Affiliate Identifier 
			</td>
			<td class="ContentTD">
                            <table>
                	        <tr>
                    		    <td>
        				<%=mrData.getNewAffiliateId().toDisplayString()%>
                                    </td>
                  	            <td width="35%" class="smallFontI">
                	                Note: Can only be updated via Enable Mass Change 
                        	    </td>
                    		</tr>
                	    </table>
			</td>
		</tr>
		<tr>
			<td class="ContentHeaderTD">
				Affiliate Status 
			</td>
			<td class="ContentTD">
				<table>
				    <tr>
					<TD width="20%" class="ContentTD">
					    <afscme:codeWrite property="mrData.affStatus" codeType="AffiliateStatus" format="{1}" writeHidden="true"/>
					</TD>
					<TD width="8%" class="smallFont">
					    Change to: 
					</TD>
					<td class="ContentTD">
					    <html:select property="newStatus">
						<afscme:codeOptions allowNull="true" codeType="AffiliateStatus" format="{1}" excludeCodes="M,D,S"/>
					    </html:select>
					    <br>
					    <html:errors property="newStatus" />
					</td>               
					<td width="35%" class="smallFontI">
					    Note: You can only change the status option to 'Merged', 'Deactivated', or 'Split' via Enable Mass Change 
					</td>
				    </tr>
				</table>
            		</td>
		</tr>
		<tr>
			<td class="ContentHeaderTD">
				Unit Wide No Member Cards 
			</td>

			<td class="ContentTD" colspan="3">
				<html:checkbox property="mrData.noCards"/>
			</td>
		</tr>
		<tr>
			<td class="ContentHeaderTD">
				Unit Wide No PE Mail
			</td>
			<td class="ContentTD" colspan="3">
				<html:checkbox property="mrData.noPEMail"/>
			</td>

		</tr>
		<tr>
			<th align="left" colspan="2">
				Edit Comment
			</th>
		</tr>
		<tr>
			<td class="ContentTD" colspan="2">
                <html:textarea cols="115" rows="3" property="mrData.comment"/>
			</td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<tr valign="top">
			<td align="left">
				<br> *<html:submit styleClass="BUTTON"/> 
				<br> <br> 
			</td>
			<td align="right">
				<br> <html:reset styleClass="BUTTON"/> 
				<html:cancel styleClass="BUTTON"/> 
			</td>
		</tr>
	</table>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="61%" align="center">
		<TR>
			<TD align="center" class="smallFontI">
				* Note: Changes made here only apply to this Affiliate and will not be applied to Affiliates 
				in the Hierarchy. 
			</TD>
		</TR>
	</TABLE>

</html:form>

<%@ include file="../include/footer.inc" %> 
