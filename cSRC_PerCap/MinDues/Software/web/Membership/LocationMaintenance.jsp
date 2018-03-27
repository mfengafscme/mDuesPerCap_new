<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Location Maintenance", help = "LocationMaintenance.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="locationListForm" name="locationListForm" type="org.afscme.enterprise.organization.web.LocationListForm"/>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" align="center">
    <tr valign="top">
        <td align="left">
<%if (locationListForm.isAffiliatePk()) { %>
            <BR><afscme:button forward="ViewAffiliateDetail">Return</afscme:button>
<% } else { %>
            <BR><afscme:button forward="ViewOrganizationDetail">Return</afscme:button>
<% } %>
        </td>
        <td align="right">
            <BR><afscme:button page="/editLocation.action?back=LocationMaintenance" paramName="locationListForm" paramProperty="orgPK" paramId="orgPK">Add</afscme:button><BR><BR> 
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
<%if (locationListForm.isAffiliatePk()) { %>
            <afscme:currentAffiliate /><BR><BR>
<% } else { %>
            <afscme:currentOrganizationName /><BR><BR>
<% } %>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">

<logic:iterate id="locationList" name="locationListForm" property="locations">
    <bean:define id="location" name="locationList" type="org.afscme.enterprise.organization.LocationData"/>

    <tr valign="top">
        <td width="8%">
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <td class="ContentHeaderTD">
                        <afscme:link page="/editLocation.action?back=LocationMaintenance" paramName="location" paramProperty="orgLocationPK" paramId="pk" styleClass="action" title="Edit this Location">Edit</afscme:link> 
                    </td>
                    <td class="ContentHeaderTD">
                        <afscme:link page="/deleteLocation.action" paramName="location" paramProperty="orgLocationPK" paramId="pk" styleClass="action" title="Delete this Location" confirm="Are you sure you want to delete this Location?">Delete</afscme:link> 
                    </td>
                </tr>
            </table>
        </td>
        <td>
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th>Primary</th>
                    <th colspan="5">Title of Location</th>
                </tr>
                <tr>
                    <td class="ContentTD" align="center">
        		<html:radio name="location" property="primaryLocationBoolean" value="true" disabled="true"/>
                    </td>
                    <td class="ContentTD" align="center" colspan="5">
                        <bean:write name="location" property="locationNm"/>
                    </td>
                </tr>

        <!-- check for any Organization Location Addresses -->            
        <logic:notEmpty name="location" property="orgAddressData">

            <!-- Organization Location Addresses -->
            <logic:iterate id="addressList" name="location" property="orgAddressData">
            <bean:define id="address" name="addressList" type="org.afscme.enterprise.organization.OrgAddressRecord"/> 

              <!-- Organization Location Regular Addresses -->
              <logic:equal name="address" property="type" value="<%= org.afscme.enterprise.codes.Codes.OrgAddressType.REGULAR.toString() %>">
                <tr>
                    <th align="left" colspan="6">Address</th>
                </tr>
                <%@ include file="../include/location_address_content.inc" %>
                
                <!-- ONLY REGULAR Addresses -->
                <logic:equal name="location" property="hasOnlyRegularAddress" value="true">
                    <tr>
                        <th align="left" colspan="6">Ship To Address</th>
                    </tr>
                    <%@ include file="../include/location_noaddress_content.inc" %>
                </logic:equal>
                <!-- Finished ONLY REGULAR Addresses -->

              </logic:equal>

              <!-- Organization Location Shipping Addresses -->
              <logic:equal name="address" property="type" value="<%= org.afscme.enterprise.codes.Codes.OrgAddressType.SHIPPING.toString() %>">
                
                <!-- ONLY SHIPPING Addresses -->
                <logic:equal name="location" property="hasOnlyShippingAddress" value="true">
                    <tr>
                        <th align="left" colspan="6">Address</th>
                    </tr>
                    <%@ include file="../include/location_noaddress_content.inc" %>
                </logic:equal>
                <!-- Finished ONLY SHIPPING Addresses -->

                <tr>
                    <th align="left" colspan="6">Ship To Address</th>
                </tr>
                <%@ include file="../include/location_address_content.inc" %>
              </logic:equal>

            </logic:iterate>
            <!--  Organization Location Addresses -->

        </logic:notEmpty>

        <!-- if no Organization Location Addresses then show empty ones -->
        <logic:empty name="location" property="orgAddressData">
                <tr>
                    <th align="left" colspan="6">Address</th>
                </tr>
                <%@ include file="../include/location_noaddress_content.inc" %>
                <tr>
                    <th align="left" colspan="6">Ship To Address</th>
                </tr>
                <%@ include file="../include/location_noaddress_content.inc" %>
        </logic:empty>

                <!-- Organization Location Phones -->
                <tr valign="top">
                    <td COLSPAN="6">
                        <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                            <tr>
                                <th align="left" colspan="6">Phone Numbers</th>
                            </tr>
                            <tr>
                                <th width="15%" class="small">Type</th>
                                <th width="15%" class="small">Country Code</th>
                                <th width="15%" class="small">Area Code</th>
                                <th width="25%" class="small">Number</th>
                                <th width="10%" class="small">Bad</th>
                                <th width="20%" class="small">Date Marked Bad</th>
                            </tr>

        <!-- check for any Organization Location Phones -->            
        <logic:notEmpty name="location" property="orgPhoneData">

            <logic:iterate id="phoneList" name="location" property="orgPhoneData">
            <bean:define id="phone" name="phoneList" type="org.afscme.enterprise.organization.OrgPhoneData"/> 

                <!-- Organization Location Office Phone Number -->
                <logic:equal name="phone" property="phoneType" value="<%= org.afscme.enterprise.codes.Codes.OrgPhoneType.LOC_PHONE_OFFICE.toString() %>">

                    <%@ include file="../include/location_phone_content.inc" %>
                
                    <logic:equal name="location" property="hasOnlyOfficePhone" value="true">
                            <tr>
                                <td align="center">Fax</td>                    
                                <td align="center">&nbsp;</td>
                                <td align="center">&nbsp;</td>
                                <td align="center">&nbsp;</td>
                                <td align="center" class="ContentTD"><input name="phoneBadFlag" type="checkbox" disabled></td>
                                <td align="center" class="ContentTD">&nbsp;</td>
                            </tr>
                    </logic:equal>
                </logic:equal>

                <!-- Organization Location Fax Phone Number -->
                <logic:equal name="phone" property="phoneType" value="<%= org.afscme.enterprise.codes.Codes.OrgPhoneType.LOC_PHONE_FAX.toString() %>">

                    <logic:equal name="location" property="hasOnlyFaxPhone" value="true">
                            <tr>
                                <td align="center">Office</td>                    
                                <td align="center">&nbsp;</td>
                                <td align="center">&nbsp;</td>
                                <td align="center">&nbsp;</td>
                                <td align="center" class="ContentTD"><input name="phoneBadFlag" type="checkbox" disabled></td>
                                <td align="center" class="ContentTD">&nbsp;</td>
                            </tr>
                    </logic:equal>
                    <%@ include file="../include/location_phone_content.inc" %>

                </logic:equal>

            </logic:iterate>
            <!-- Organization Location Phones -->

        </logic:notEmpty>

        <!-- if no Organization Location Phones then show empty ones -->
        <logic:empty name="location" property="orgPhoneData">        
                <tr>
                    <td align="center">Office</td>                    
                    <td align="center">&nbsp;</td>
                    <td align="center">&nbsp;</td>
                    <td align="center">&nbsp;</td>
                    <td align="center" class="ContentTD"><input name="phoneBadFlag" type="checkbox" disabled></td>
                    <td align="center" class="ContentTD">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center">Fax</td>                    
                    <td align="center">&nbsp;</td>
                    <td align="center">&nbsp;</td>
                    <td align="center">&nbsp;</td>
                    <td align="center" class="ContentTD"><input name="phoneBadFlag" type="checkbox" disabled></td>
                    <td align="center" class="ContentTD">&nbsp;</td>
                </tr>
        </logic:empty>

                        </table>
                    </td>
                </tr>
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
            </table>
        </td>
    </tr>
</logic:iterate>

<logic:empty name="locationListForm" property="locations">
    <%@ include file="../include/location_noprimary_content.inc" %>
</logic:empty>

</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr>
        <td class="ContentHeaderTR">
<%if (locationListForm.isAffiliatePk()) { %>
            <BR><afscme:currentAffiliate />
<% } else { %>
            <BR><afscme:currentOrganizationName />
<% } %>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="" align="center">
    <tr valign="top">
        <td align="left">
<%if (locationListForm.isAffiliatePk()) { %>
            <BR><afscme:button forward="ViewAffiliateDetail">Return</afscme:button>
<% } else { %>
            <BR><afscme:button forward="ViewOrganizationDetail">Return</afscme:button>
<% } %>
        </td>
        <td align="right">
            <BR><afscme:button page="/editLocation.action?back=LocationMaintenance" paramName="locationListForm" paramProperty="orgPK" paramId="orgPK">Add</afscme:button><BR><BR> 
        </td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>
