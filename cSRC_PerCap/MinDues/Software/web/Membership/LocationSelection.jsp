<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Location Selection", help = "LocationSelection.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="locationSelection" name="locationSelection" type="org.afscme.enterprise.affiliate.staff.web.LocationSelection"/>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" align="center">
    <tr valign="top">
        <td align="right">
            <afscme:button action='<%=(String)locationSelection.getReturnAction()%>'>Cancel</afscme:button><br><br>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <afscme:currentPersonName /><BR>
<% if (request.getParameter("back").equals("AssociateDetail")) { %>
            <afscme:currentOrganizationName /><BR><BR>
<% } else { %>
            <afscme:currentAffiliate/><BR><BR>
<% } %>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">

<logic:iterate id="location" name="locationSelection" property="locations" type="org.afscme.enterprise.organization.LocationData">

    <tr valign="top">
        <td width="8%">
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <td class="ContentHeaderTD">
                        <% if (locationSelection.getCurrentLocation() != null && location.getOrgLocationPK().equals(locationSelection.getCurrentLocation())) { %>
                        Current 
                        <% } else {%>
                            <% if (request.getParameter("back").equals("AssociateDetail")) { %>
                        <afscme:link page="/selectLocation.action?back=AssociateDetail" paramProperty="orgLocationPK" paramName="location" paramId="pk" styleClass="action">Select</afscme:link>
                            <% } else {%>
                        <afscme:link page="/selectLocation.action?back=StaffDetail" paramProperty="orgLocationPK" paramName="location" paramId="pk" styleClass="action">Select</afscme:link>
                            <% } %>
                        <% } %>
                    </td>
                </tr>
            </table>
        </td>
        <td>
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th colspan="6">Title of Location</th>
                </tr>
                <tr>
                    <td class="ContentTD" align="center" colspan="6">
                        <bean:write name="location" property="locationNm"/>
                    </td>
                </tr>

        <!-- check for any Organization Location Addresses -->            
        <logic:present name="location" property="orgAddressData">

            <!-- Organization Location Addresses -->
            <logic:iterate id="address" name="location" property="orgAddressData" type="org.afscme.enterprise.organization.OrgAddressRecord">

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

        </logic:present>

        <!-- if no Organization Location Addresses then show empty ones -->
        <logic:notPresent name="location" property="orgAddressData">
                <tr>
                    <th align="left" colspan="6">Address</th>
                </tr>
                <%@ include file="../include/location_noaddress_content.inc" %>
                <tr>
                    <th align="left" colspan="6">Ship To Address</th>
                </tr>
                <%@ include file="../include/location_noaddress_content.inc" %>
        </logic:notPresent>

                <!-- Organization Location Phones -->
                <tr valign="top">
                    <td COLSPAN="6">
                        <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                            <tr>
                                <th align="left" colspan="6">Phone Numbers</th>
                            </tr>
                            <tr>
                                <th width="15%" class="small">Code</th>
                                <th width="15%" class="small">Country Code</th>
                                <th width="15%" class="small">Area Code</th>
                                <th width="25%" class="small">Number</th>
                                <th width="10%" class="small">Bad</th>
                                <th width="20%" class="small">Date Marked Bad</th>
                            </tr>

        <logic:iterate id="phone" name="location" property="orgPhoneData" type="org.afscme.enterprise.organization.OrgPhoneData">

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

        </logic:present>

        <!-- if no Organization Location Phones then show empty ones -->
        <logic:notPresent name="location" property="orgPhoneData">        
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
        </logic:notPresent>

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

<logic:empty name="locationSelection" property="locations">
    <%@ include file="../include/location_noprimary_content.inc" %>
</logic:empty>

</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr>
        <td class="ContentHeaderTR">
            <BR><afscme:currentPersonName />
<% if (request.getParameter("back").equals("AssociateDetail")) { %>
            <BR><afscme:currentOrganizationName />
<% } else { %>
            <BR><afscme:currentAffiliate/>
<% } %>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="" align="center">
    <tr valign="top">
        <td align="right">
            <br><afscme:button action='<%=(String)locationSelection.getReturnAction()%>'>Cancel</afscme:button>
        </td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>
