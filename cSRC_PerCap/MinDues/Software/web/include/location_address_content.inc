
                <tr>
                    <td class="ContentHeaderTD">Attention</td>
                    <td class="ContentTD" COLSPAN="5">
                        <bean:write name="address" property="attentionLine"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD">Address 1</td>
                    <td class="ContentTD">
                        <bean:write name="address" property="addr1"/>
                    </td>
                    <td class="ContentHeaderTD">Address 2</td>
                    <td class="ContentTD" colspan="3">
                        <bean:write name="address" property="addr2"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD" width="11%">City</td>
                    <td class="ContentTD" width="34%">
                        <bean:write name="address" property="city"/>
                    </td>
                    <td class="ContentHeaderTD" width="14%">State</td>
                    <td class="ContentTD" width="18%">
                        <afscme:codeWrite name="address" property="state" codeType="State" format="{0}" useCode="true"/>
                    </td>
                    <td class="ContentHeaderTD" width="10%">Zip/Postal Code</td>
                    <td class="ContentTD" width="13%">
                        <bean:write name="address" property="zipCode"/>

                <logic:notEmpty name="address" property="zipPlus">
                        - <bean:write name="address" property="zipPlus"/>
                </logic:notEmpty>
                
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD">County</td>
                    <td class="ContentTD">
                        <bean:write name="address" property="county"/>
                    </td>
                    <td class="ContentHeaderTD">Province</td>
                    <td class="ContentTD">
                        <bean:write name="address" property="province"/>
                    </td>
                    <td class="ContentHeaderTD">Country</td>
                    <td class="ContentTD">
                        <afscme:codeWrite name="address" property="countryPk" codeType="Country" format="{0}"/>
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD">Bad Address</td>
                    <td class="ContentTD">
                        <html:checkbox property="bad" name="address" disabled="true"/></td>
                    </td>
                    <td class="ContentHeaderTD">Date Marked Bad</td>
                    <td class="ContentTD" colspan="3">
                        <afscme:dateWrite name="address" property="badDate"/>
                    </td>
                </tr>

<%if (title.startsWith("Organization Associate Detail") || title.startsWith("Affiliate Staff Detail")) { %>
<!-- Additions for Organization Associate Detail -->
                <tr>
                    <td class="ContentHeaderTD">Last Updated</td>
                    <td class="ContentTD">
                        <afscme:dateWrite name="address" property="recordData.modifiedDate"/>
                    </td>
                    <td class="ContentHeaderTD">Updated By</td>
                    <td class="ContentTD" colspan="3">
                        <afscme:userWrite name="address" property="recordData.modifiedBy" />                   
                    </td>
                </tr>
<% } %>