<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<bean:define id="location" name="locationForm" type="org.afscme.enterprise.organization.web.LocationForm"/>

<%!String title, help;%>
<%title = "Location Maintenance " + (location.isAdd() ? "Add" : "Edit");%>
<%help = "LocationMaintenance" + (location.isAdd() ? "Add" : "Edit") + ".html";%>
<%@ include file="../include/header.inc" %>

<html:form action="saveLocation" focus="locationPrimary">

    <html:hidden property="pk"/>
    <html:hidden property="orgPK"/>
    <html:hidden property="back"/>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" align="center">
    <tr valign="top">
        <td align="left"><html:submit styleClass="button"/></td>
        <td align="right">
            <html:reset styleClass="button"/>
            <html:cancel styleClass="button"/>
        </td>
    </tr>
    <tr>
        <td colspan="3"><BR></td>
    </tr>         
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
<%if (location.isAffiliatePk()) { %>
            <afscme:currentAffiliate /><BR><BR>
<% } else { %>
            <afscme:currentOrganizationName /><BR><BR>
<% } %>
        </td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr valign="top">
        <td colspan="2">
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th>Primary</th>
                    <th colspan="5">* Title of Location</th>
                </tr>
                <tr>
                    <td class="ContentTD" align="center">
                        <html:checkbox name="location" property="locationPrimary"/>
                    </td>
                    <td class="ContentTD" align="center" colspan="5">
                        <html:text property="locationTitle" size="50" maxlength="50"/><html:errors property="locationTitle"/>
                    </td>
                </tr>
                <tr>
                    <th align="left" colspan="6">Address</th>
                    <html:hidden property="regularLocationAddressPK"/>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="label_regularLocationAttention">Attention</label></td>
                    <td class="ContentTD" COLSPAN="5">
                        <html:text property="regularLocationAttention" size="75" maxlength="75"/><html:errors property="regularLocationAttention"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="label_regularLocationAddress1">Address 1</label></td>
                    <td class="ContentTD">
                        <html:text property="regularLocationAddress1" size="50" maxlength="50"/><html:errors property="regularLocationAddress1"/>
                    </td>
                    <td class="ContentHeaderTD"><label for="label_regularLocationAddress2">Address 2</label></td>
                    <td class="ContentTD" colspan="3">
                        <html:text property="regularLocationAddress2" size="50" maxlength="50"/><html:errors property="regularLocationAddress2"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD" width="11%"><label for="label_regularLocationCity">City</label></td>
                    <td class="ContentTD" width="34%">
                        <html:text property="regularLocationCity" size="25" maxlength="25"/><html:errors property="regularLocationCity"/>
                    </td>
                    <td class="ContentHeaderTD" width="14%"><label for="label_regularLocationState">State</label></td>
                    <td class="ContentTD" width="18%">
                        <html:select property="regularLocationState">
                            <afscme:codeOptions useCode="true" codeType="State" format="{0}" allowNull="true" nullDisplay=""/>
                        </html:select>
                    </td>
                    <td class="ContentHeaderTD" width="10%"><label for="label_regularLocationZip">Zip/Postal Code</label></td>
                    <td class="ContentTD" width="13%">
                        <html:text property="regularLocationZip" size="5" maxlength="12"/>
                    - 
                        <html:text property="regularLocationZip4" onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="label_regularLocationCounty">County</label></td>
                    <td class="ContentTD">
                        <html:text property="regularLocationCounty" size="25" maxlength="25"/><html:errors property="regularLocationCounty"/>
                    </td>
                    <td class="ContentHeaderTD"><label for="label_regularLocationProvince">Province</label></td>
                    <td class="ContentTD">
                        <html:text property="regularLocationProvince" size="25" maxlength="25"/>
                    </td>
                    <td class="ContentHeaderTD"><label for="label_regularLocationCountry">* Country</label></td>
                    <td class="ContentTD">
                        <html:select property="regularLocationCountry">
                                <afscme:codeOptions codeType="Country" format="{1}" allowNull="true" nullDisplay=""/>
                        </html:select>
                        <html:errors property="regularLocationCountry"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="label_regularLocationAddressBad">Bad Address</label></td>
                    <td class="ContentTD">
                        <html:checkbox name="location" property="regularLocationAddressBad"/>
                    </td>
                    <td class="ContentHeaderTD">Date Marked Bad</td>
                     <td class="ContentTD" colspan="3">
                        <afscme:dateWrite name="location" property="regularLocationAddressBadDate"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" align="center">
                        <html:errors property="regularLocationState"/>
                        <html:errors property="regularLocationZip"/>
                        <html:errors property="regularLocationZip4"/>
                        <html:errors property="regularLocationProvince"/>
                    </td>
                </tr>
                <tr>
                    <th align="left" colspan="6">Ship To Address</th>
                    <html:hidden property="shippingLocationAddressPK"/>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="label_shippingLocationAttention">Attention</label></td>
                    <td class="ContentTD" COLSPAN="5">
                        <html:text property="shippingLocationAttention" size="75" maxlength="75"/><html:errors property="shippingLocationAttention"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="label_shippingLocationAddress1">Address 1</label></td>
                    <td class="ContentTD">
                        <html:text property="shippingLocationAddress1" size="50" maxlength="50"/><html:errors property="shippingLocationAddress1"/>
                    </td>
                    <td class="ContentHeaderTD"><label for="label_shippingLocationAddress2">Address 2</label></td>
                    <td class="ContentTD" colspan="3">
                        <html:text property="shippingLocationAddress2" size="50" maxlength="50"/><html:errors property="shippingLocationAddress2"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD" width="11%"><label for="label_shippingLocationCity">City</label></td>
                    <td class="ContentTD" width="34%">
                        <html:text property="shippingLocationCity" size="25" maxlength="25"/><html:errors property="shippingLocationCity"/>
                    </td>
                    <td class="ContentHeaderTD" width="14%"><label for="label_shippingLocationState">State</label></td>
                    <td class="ContentTD" width="18%">
                        <html:select property="shippingLocationState">
                            <afscme:codeOptions useCode="true" codeType="State" format="{0}" allowNull="true" nullDisplay=""/>
                        </html:select>
                    </td>
                    <td class="ContentHeaderTD" width="10%"><label for="label_shippingLocationZip">Zip/Postal Code</label></td>
                    <td class="ContentTD" width="13%">
                        <html:text property="shippingLocationZip" size="5" maxlength="12"/>
                    - 
                        <html:text property="shippingLocationZip4" onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="label_shippingLocationCounty">County</label></td>
                    <td class="ContentTD">
                        <html:text property="shippingLocationCounty" size="25" maxlength="25"/><html:errors property="shippingLocationCounty"/>
                    </td>
                    <td class="ContentHeaderTD"><label for="label_shippingLocationProvince">Province</label></td>
                    <td class="ContentTD">
                        <html:text property="shippingLocationProvince" size="25" maxlength="25"/>
                    </td>
                    <td class="ContentHeaderTD"><label for="label_shippingLocationCountry">Country</label></td>
                    <td class="ContentTD">
                        <html:select property="shippingLocationCountry">
                                <afscme:codeOptions codeType="Country" format="{1}" allowNull="true" nullDisplay=""/>
                        </html:select>
                        <html:errors property="shippingLocationCountry"/> 
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="label_ShipBadAddressFlag">Bad Address</label></td>
                    <td class="ContentTD">
                        <html:checkbox name="location" property="shippingLocationAddressBad"/>
                    </td>
                    <td class="ContentHeaderTD">Date Marked Bad</td>
                    <td class="ContentTD" colspan="3">
                        <afscme:dateWrite name="location" property="shippingLocationAddressBadDate"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" align="center">
                        <html:errors property="shippingLocationState"/>
                        <html:errors property="shippingLocationZip"/>
                        <html:errors property="shippingLocationZip4"/>
                        <html:errors property="shippingLocationProvince"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">
                        <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                            <tr>
                                <th align="left" colspan="6">Phone Numbers</th>
                            </tr>
                            <tr>
                                <th width="15%" class="small">Type</th>
                                <th width="15%" class="small">Country Code</th>
                                <th width="15%" class="small">Area Code</th>
                                <th width="25%" class="small">* Number</th>
                                <th width="10%" class="small">Bad</th>
                                <th width="20%" class="small">Date Marked Bad</th>
                            </tr>
                            <tr>
                                <td align="center">Office</td>
                                <html:hidden property="officeLocationPhonePK"/>
                                <td align="center">
                                    <html:text property="officeLocationPhoneCountryCode" size="5" maxlength="5"/>
                                </td>
                                <td align="center">
                                    <html:text property="officeLocationPhoneAreaCode" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
                                </td>
                                <td align="center">
                                    <html:text property="officeLocationPhoneNumber" size="15" maxlength="15"/>
                                </td>
                                <td align="center" class="ContentTD">
                                    <html:checkbox name="location" property="officeLocationPhoneBad"/>
                                </td>
                                <td align="center" class="ContentTD">
                                    <afscme:dateWrite name="location" property="officeLocationPhoneBadDate"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="center">Fax</td>
                                <html:hidden property="faxLocationPhonePK"/>
                                <td align="center">
                                    <html:text property="faxLocationPhoneCountryCode" size="5" maxlength="5"/>
                                </td>
                                <td align="center">
                                    <html:text property="faxLocationPhoneAreaCode" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
                                </td>
                                <td align="center">
                                    <html:text property="faxLocationPhoneNumber" size="15" maxlength="15"/>
                                </td>
                                <td align="center" class="ContentTD">
                                    <html:checkbox name="location" property="faxLocationPhoneBad"/>
                                </td>
                                <td align="center" class="ContentTD">
                                    <afscme:dateWrite name="location" property="faxLocationPhoneBadDate"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6" align="center">
                                    <html:errors property="officeLocationPhoneCountryCode"/>
                                    <html:errors property="officeLocationPhoneAreaCode"/>
                                    <html:errors property="officeLocationPhoneNumber"/>
                                    <html:errors property="faxLocationPhoneCountryCode"/>
                                    <html:errors property="faxLocationPhoneAreaCode"/>
                                    <html:errors property="faxLocationPhoneNumber"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

<%if (!(location.isAdd())) { %>
                <tr>
                    <td colspan="6"><HR></td>
                </tr>
                <tr>
                    <td colspan="6">
                        <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                            <tr>
                                <td width="12%" class="ContentHeaderTD">Created Date</td>
                                <td width="13%" class="ContentTD">
                                    <afscme:dateWrite name="location" property="recordData.createdDate"/>
                                </td>
                                <td width="10%" class="ContentHeaderTD">Created By</td>
                                <td width="15%" class="ContentTD">
                                    <afscme:userWrite name="location" property="recordData.createdBy"/>
                                </td>
                                <td width="12%" class="ContentHeaderTD">Last Updated</td>
                                <td width="13%" class="ContentTD">
                                    <afscme:dateWrite name="location" property="recordData.modifiedDate"/> 
                                </td>
                                <td width="10%" class="ContentHeaderTD">Updated By</td>
                                <td width="15%" class="ContentTD">
                                    <afscme:userWrite name="location" property="recordData.modifiedBy"/> 
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
<% } %>
            </table>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
<%if (location.isAffiliatePk()) { %>
            <BR><afscme:currentAffiliate />
<% } else { %>
            <BR><afscme:currentOrganizationName />
<% } %>
        </td>
    </tr>
</table>

<table  width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" align="center">
    <tr valign="top">
        <td colspan="3"><BR></td>
    </tr>   
    <tr>
        <td align="left"><html:submit styleClass="button"/></td>
        <td align="right">
            <html:reset styleClass="button"/>
            <html:cancel styleClass="button"/>
        </td>
    </tr>      
    <tr>
        <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
    </tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %>
