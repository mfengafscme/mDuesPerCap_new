
<%if (!(title.startsWith("Organization Associate Detail"))) { %>
<!-- Additions for Org and Affiliate Detail -->

    <tr valign="top">
        <td COLSPAN="2">
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
            
<% } %>
                <tr>
                    <th colspan="6" align="left">Primary Address</th>
                </tr>
                <tr>
                    <td class="ContentHeaderTD" colspan="2">Title of Location</td>
                    <td class="ContentTD" align="left" colspan="4">
                        <bean:write name="location" property="locationNm"/></td>
                </tr>
                <tr>
                    <td colspan="6"><HR></td>
                </tr>

        <!-- Organization Primary Location Regular Address -->
    <logic:notEmpty name="location" property="orgAddressData">        
        <logic:iterate id="primaryAddressList" name="location" property="orgAddressData">
	<bean:define id="address" name="primaryAddressList" type="org.afscme.enterprise.organization.OrgAddressRecord"/> 

            <logic:equal name="address" property="type" value="<%= org.afscme.enterprise.codes.Codes.OrgAddressType.REGULAR.toString() %>">
                <%@ include file="../include/location_address_content.inc" %>
            </logic:equal>
            
        </logic:iterate>
            
        <logic:equal name="location" property="hasOnlyShippingAddress" value="true">
                <%@ include file="../include/location_noaddress_content.inc" %>
        </logic:equal>
        
    </logic:notEmpty>
    
    <logic:empty name="location" property="orgAddressData">        
        <%@ include file="../include/location_noaddress_content.inc" %>
    </logic:empty>
    
            </table>
            
<%if (!(title.startsWith("Organization Associate Detail"))) { %>
<!-- Additions for Org and Affiliate Detail -->

        </td>
    </tr>
    <tr valign="top">
        <td COLSPAN="2">
<% } %>
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

    <logic:notEmpty name="location" property="orgPhoneData"> 
        <logic:iterate id="primaryPhoneList" name="location" property="orgPhoneData">
	<bean:define id="phone" name="primaryPhoneList" type="org.afscme.enterprise.organization.OrgPhoneData"/> 

            <!-- Organization Primary Location Office Phone Number -->
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

            <!-- Organization Primary Location Fax Phone Number -->
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
    </logic:notEmpty>
  
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

<%if (!(title.startsWith("Organization Associate Detail"))) { %>
<!-- Additions for Org and Affiliate Detail -->

            </table>
        </td>
    </tr>
<% } %>
