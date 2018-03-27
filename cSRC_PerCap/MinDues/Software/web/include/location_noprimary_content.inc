
<%if (!(title.startsWith("Organization Associate Detail"))) { %>
<!-- Additions for Org and Affiliate and Affiliate Staff Detail -->

    <tr valign="top">
        <td COLSPAN="2">
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
            
<% } %>
                <tr>
                    <th colspan="6" align="left">Primary Address</th>
                </tr>
                <tr>
                    <td class="ContentHeaderTD" colspan="2">Title of Location</td>
                    <td class="ContentTD" align="left" colspan="4">No Primary Address</td>
                </tr>
                <tr>
                    <td colspan="6"><HR></td>
                </tr>

                    <!-- Organization Primary Location Regular Address -->
                    <%@ include file="../include/location_noaddress_content.inc" %>

            </table>
            
<%if (!(title.startsWith("Organization Associate Detail"))) { %>
<!-- Additions for Org and Affiliate and Affiliate Staff Detail -->

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

<%if (!(title.startsWith("Organization Associate Detail"))) { %>
<!-- Additions for Org and Affiliate and Affiliate Staff Detail -->

            </table>
        </td>
    </tr>
<% } %>
