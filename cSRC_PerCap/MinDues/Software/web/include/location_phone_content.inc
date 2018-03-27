
                <tr>
                    <td align="center"><afscme:codeWrite name="phone" property="phoneType" codeType="OrgPhoneType" format="{1}"/></td>                    
                    <td align="center"><bean:write name="phone" property="countryCode"/></td>
                    <td align="center"><bean:write name="phone" property="areaCode"/></td>
                    <td align="center"><bean:write name="phone" property="phoneNumber"/></td>
                    <td align="center" class="ContentTD"><html:checkbox property="phoneBadFlag" name="phone" disabled="true"/></td>
                    <td align="center" class="ContentTD"><afscme:dateWrite name="phone" property="phoneBadDate"/></td>
                </tr>
