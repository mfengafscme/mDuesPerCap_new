<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Organization Search", help = "OrganizationSearch.html";%>
<%@ include file="../include/header.inc" %>

<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>

<html:form action="searchOrganizations" focus="orgName">
    
    <table width="100%" align="center">

    <tr><td colspan="3" align="center">
        <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
    </td></tr>
    <tr><td>

    <table class="BodyContentNoWidth" width="100%" cellpadding="0" cellspacing="0" border="1" align="center">
	<tr valign="top">
            <td class="ContentHeaderTR" colspan="3">Fields to Search:</td>
	</tr>
	<tr valign="top">
            <td>
                <table class="InnerContentTable" width="100%" cellpadding="1" cellspacing="1" border="0" align="center">
                    <tr>
                        <th width="50%">Organization Name</th>
                        <th>Organization Type</th>
                    </tr>
                    <tr>
                        <td nowrap align="center"><html:text property="orgName" size="29" maxlength="29"/><html:errors property="orgName"/></td>
            		<td nowrap align="center">
                            <html:select property="orgType">
                                <afscme:codeOptions codeType="OrganizationType" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                            </html:select>
                            <html:errors property="orgType"/>
                	</td>
                    </tr>
		</table>
            </td>
	</tr>
	<tr valign="top">
            <td>
                <table class="InnerContentTable" width="100%" cellpadding="1" cellspacing="1" border="0" align="center">
                    <tr>
            		<td nowrap width="20%" class="ContentHeaderTD" align="left"><label for="orgWebSite">Organization Website</label></td>
                        <td nowrap class="ContentHeader" align="left"><html:text property="orgWebSite" size="100" maxlength="100"/>
                            <html:errors property="orgWebSite"/>
                        </td>
                    </tr>
                    <tr>
			<td CLASS="ContentHeaderTD">Marked for Deletion</TD>
			<td CLASS="ContentTD" align="left">
                            <html:radio property="markedForDeletion" value="1" styleId="optionDeleteYes">
                                <label for="optionDeleteYes">Yes</label></html:radio>
                            <html:radio property="markedForDeletion" value="0" styleId="optionDeleteNo">
                                <label for="optionDeleteYes">No</label></html:radio>
                            <html:radio property="markedForDeletion" value="2" styleId="optionDeleteAll">
                                <label for="optionDeleteYes">All</label></html:radio>
			</td>
                    </tr>
		</table>
            </td>					
        </tr>
	<tr valign="top">
            <td>
                <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <tr>
                        <th colspan="6" align="left">Addresses</th>
                    </tr>
                    <tr>
            		<td nowrap class="ContentHeaderTD" align="left"><label for="attentionLine">Attention</label></td>
                        <td nowrap colspan="5" class="ContentHeader" align="left"><html:text property="attentionLine" size="75" maxlength="75"/>
                            <html:errors property="attentionLine"/>
                        </td>
                    </tr>
                    <tr>
            		<td class="ContentHeaderTD"><label for="addr1">Address 1</label></td>
                        <td class="ContentHeader"><html:text property="addr1" size="50" maxlength="50"/>
                            <html:errors property="addr1"/></td>
            		<td class="ContentHeaderTD"><label for="addr2">Address 2</label></td>
                        <td colspan="3" class="ContentHeader"><html:text property="addr2" size="50" maxlength="50"/>
                            <html:errors property="addr2"/></td>
                    </tr>
                    <tr>
            		<td width="12%" class="ContentHeaderTD"><label for="city">City</label></td>
                        <td width="35%" class="ContentHeader"><html:text property="city" size="25" maxlength="25"/>
                            <html:errors property="city"/></td>
            		<td width="18%" class="ContentHeaderTD"><label for="state">State</label></td>
                        <td width="15%" class="ContentHeader"><html:select property="state">
                                <afscme:codeOptions codeType="State" useCode="true" format="{0}" allowNull="true" nullDisplay=""/>
                            </html:select>
                            <html:errors property="state"/>
                	</td>
            		<td nowrap width="10%" class="ContentHeaderTD"><label for="zipPostal">Zip/Postal Code</label></td>
                        <td nowrap width="10%" class="ContentHeader"><html:text property="zipPostal" size="5" maxlength="12"/>
                        <html:text property="zipPlus" size="4" maxlength="4"/>						
							<html:errors property="zipPostal"/></td>
                    </tr>
                    <tr>
            		<td class="ContentHeaderTD"><label for="county">County</label></td>
                        <td class="ContentHeader"><html:text property="county" size="25" maxlength="25"/>
                            <html:errors property="county"/></td>
            		<td class="ContentHeaderTD"><label for="province">Province</label></td>
                        <td class="ContentHeader"><html:text property="province" size="25" maxlength="25"/>
                            <html:errors property="province"/></td>
            		<td class="ContentHeaderTD"><label for="country">Country</label></td>
                        <td class="ContentHeader"><html:select property="country">
                                <afscme:codeOptions codeType="Country" format="{1}" allowNull="true" nullDisplay=""/>
                            </html:select>
                            <html:errors property="country"/>
                	</td>
                    </tr>
                    <tr>
                        <td class="ContentHeaderTD"><label for="lastUpdateDate">Last Updated</label></td>
			<td class="ContentTD"><html:text property="lastUpdateDate" size="10" maxlength="10"/>
                            <html:errors property="lastUpdateDate"/>
                            <a href="javascript:show_calendar('searchOrganizationsForm.lastUpdateDate');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;">
                            <IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></a> 
			</td>
			<td class="ContentHeaderTD"><label for="label_lastUpdateUser">Updated By</label></td>
			<td class="ContentTD"><html:text property="lastUpdateUser" size="10" maxlength="10"/>
                            <html:errors property="lastUpdateUser"/></td>
                    </tr>
		</table>
            </td>					
        </tr>
	<tr valign="top">
            <td>
                <table class="InnerContentTable" width="100%" cellpadding="1" cellspacing="1" border="0" align="center">
                    <tr>
			<th align="left" colspan="4">Phone Numbers</th>
                    </tr>
                    <tr>
			<th width="15%" class="small">Code</th>
			<th width="15%" class="small">Country Code</th>
			<th width="15%" class="small">Area Code</th>
			<th width="25%" class="small">Number</th>
                    </tr>
                    <tr>
			<td align="center">Office</td>
			<td align="center"><html:text property="officeCountryCode" size="5" maxlength="5"/>
                            <html:errors property="officeCountryCode"/></td>
			<td align="center"><html:text property="officeAreaCode" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
                            <html:errors property="officeAreaCode"/></td>							
			<td align="center"><html:text property="officePhoneNo" size="15" maxlength="15"/>
                            <html:errors property="officePhoneNo"/></td>
                    </tr>
                    <tr>
			<td align="center">Fax</td>
			<td align="center"><html:text property="faxCountryCode" size="5" maxlength="5"/>
                            <html:errors property="faxCountryCode"/></td>
			<td align="center"><html:text property="faxAreaCode" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
                            <html:errors property="faxAreaCode"/></td>							
			<td align="center"><html:text property="faxPhoneNo" size="15" maxlength="15"/>
                            <html:errors property="faxPhoneNo"/></td>
                    </tr>
		</table>
            </td>
        </tr>
    </table>
    </td></tr>
    <tr><td>
        <table width="100%">
            <tr>
                <td align="left"><html:submit styleClass="button"/></td>
                <td align="right"><html:reset styleClass="button"/></td>
            </tr>
        </table>
    </td></tr>
    </table>

</html:form>

<%@ include file="../include/footer.inc" %> 
