<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Member Search Results", help = "MemberSearchResults.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="searchMembersForm" type="org.afscme.enterprise.member.web.SearchMembersForm"/>

<!-- if no results returned go to the MemberDetailAdd page -->


<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr>
        <td align="right">
            <BR>
            <afscme:button action="/verifyMember.action">Add New Member</afscme:button>&nbsp;
            <afscme:button action="/viewBasicMemberCriteria.action">New Search</afscme:button>
            <BR><BR>
        </td> 
    </tr>
</table>
<logic:present name="form" property="results"> 
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <afscme:searchNav formName="form" action="/searchMembers.action"/>
                <BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="1" cellspacing="0" border="1" class="BodyContent" align="center">
        <tr align="center">
            <th width="10%" align="center">Select</th>
            <!-- List each column in order desired, wrapped by logic which tests for the presence of the column in the collection -->
            <!-- Title Row 1 -->
             <%if (form.getSelectHash().contains("person_nm")) {%>
                    <th align="center"><afscme:sortLink styleClass="TH" action="/searchMembers.action" formName="form" field="person_nm" title="Sort By Name">Name</afscme:sortLink></th> 
            <% } %>
            <%if (form.getSelectHash().contains("p.prefix_nm")) {%>
               <th align="center"><afscme:sortLink styleClass="TH" action="/searchMembers.action" formName="form" field="p.prefix_nm" title="Sort By Prefix">Prefix</afscme:sortLink></th>
            <% } %>
            <%if (form.getSelectHash().contains("p.nick_nm")) {%>
               <th align="center"><afscme:sortLink styleClass="TH" action="/searchMembers.action" formName="form" field="p.nick_nm" title="Sort By Nick Name">Nick Name</afscme:sortLink></th>
            <% } %>
            <%if (form.getSelectHash().contains("p.ssn")) {%>
               <th align="center">SSN</th>
            <% } %>
            <%if (form.getSelectHash().contains("p.valid_ssn_fg")) {%>
                <th align="center">Valid SSN</th>
            <% } %>
            <%if (form.getSelectHash().contains("affId")) {%>
               <th align="center" colspan="5">Affiliate Identifier</th>
            <% } %>
             <%if (form.getSelectHash().contains("sma")) {%>
               <th align="center" colspan="4">System Mailing Address</th>
            <% } %>

            <%int countCol = 0; %>
            <%if (form.getSelectHash().contains("am.mbr_type")) countCol++; %>
            <%if (form.getSelectHash().contains("am.mbr_status")) countCol++; %>
            <%if (form.getSelectHash().contains("am.person_pk")) countCol++; %>
            <%if (form.getSelectHash().contains("am.mbr_type") || form.getSelectHash().contains("am.mbr_status") || form.getSelectHash().contains("am.person_pk")) {%>
               <th align="center" colspan="<%=countCol%>">Member</th>
            <% } %> 

            <%if (form.getSelectHash().contains("pa.lst_mod_dt")) {%>
                <th align="center" >Address Last Updated</th>
            <% } %>
            <%if (form.getSelectHash().contains("pa.lst_mod_user_pk")) {%>
                <th align="center" >Address Updated By</th>
            <% } %>
            <%if (form.getSelectHash().contains("am.lst_mod_dt")) {%>
                <th align="center" >Member Last Updated</th>
            <% } %>
            <%if (form.getSelectHash().contains("am.lst_mod_user_pk")) {%>
                <th align="center" >Member Updated By</th>
            <% } %>
            <%if (form.getSelectHash().contains("p.alternate_mailing_nm")) {%>
                <th align="center" >Alt Mailing Name</th>
            <% } %>
            <%if (form.getSelectHash().contains("am.primary_information_source")) {%>
                <th align="center" >Primary Information Source</th>
            <% } %>
            <%if (form.getSelectHash().contains("phone")) {%>
                <th align="center" colspan="3">Phone</th>
            <% } %>
            <%if (form.getSelectHash().contains("email")) {%>
                <th align="center" >Email</th>
            <% } %>
            <%if (form.getSelectHash().contains("am.mbr_card_sent_dt")) {%>
                <th align="center" >Date Membership Card Sent</th>
            <% } %>
            <%if (form.getSelectHash().contains("mail")) {%>
                <th align="center" colspan="4">Mail</th>
            <% } %>

        </TR>
        <!-- Title Row 2 (Composite details)-->
        <TR>
            <TD>&nbsp;</TD>
            <%if (form.getSelectHash().contains("person_nm")) {%>
                <TD>&nbsp;</TD>
            <% } %>
             <%if (form.getSelectHash().contains("p.prefix_nm")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("p.nick_nm")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("p.ssn")) {%>
               <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("p.valid_ssn_fg")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("affId")) {%>
               <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="a.aff_type" title="Sort By Type">Type</afscme:sortLink></th>
               <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="int_local" title="Sort By Local">Loc/Sub Chap</afscme:sortLink></th>
               <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="a.aff_stateNat_type" title="Sort By State">State/Natl</afscme:sortLink></th>
               <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="a.aff_SubUnit" title="Sort By SubUnit">Sub Unit</afscme:sortLink></th>
               <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="int_council" title="Sort By Council">Cn/Ret Chap</afscme:sortLink></th>
            <% } %>
            <%if (form.getSelectHash().contains("sma")) {%>
                <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="address" title="Sort By Address">Address</afscme:sortLink></th>
                <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="pa.city" title="Sort By City">City</afscme:sortLink></th>
                <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="pa.state" title="Sort By State">State</afscme:sortLink></th>
                <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="pa.zipcode" title="Sort By Zip">Zip/Postal Code</afscme:sortLink></th>
            <% } %>
            <%if (form.getSelectHash().contains("am.mbr_type")) {%>
                <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="am.mbr_type" title="Sort By Type">Type</afscme:sortLink></th>
            <% } %>
            <%if (form.getSelectHash().contains("am.mbr_status")) {%>
                <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="am.mbr_status" title="Sort By Status">Status</afscme:sortLink></th>
            <% } %>
            <%if (form.getSelectHash().contains("am.person_pk")) {%>
                <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchMembers.action" formName="form" field="am.person_pk" title="Sort By Number">#</afscme:sortLink></th>
            <% } %>
            <%if (form.getSelectHash().contains("pa.lst_mod_dt")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("pa.lst_mod_user_pk")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("am.lst_mod_dt")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("am.lst_mod_user_pk")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("p.alternate_mailing_nm")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("am.primary_information_source")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("phone")) {%>
                <th align="center" styleClass="smallTH">Country Code</th>
                <th align="center" styleClass="smallTH">Area Code</th>
                <th align="center" styleClass="smallTH">Number</th>
            <% } %>
            <%if (form.getSelectHash().contains("email")) {%>
                <TD>&nbsp;</TD>
            <% } %>
             <%if (form.getSelectHash().contains("am.mbr_card_sent_dt")) {%>
                <TD>&nbsp;</TD>
            <% } %>
            <%if (form.getSelectHash().contains("mail")) {%>
                <th align="center" styleClass="smallTH">No Mail</th>
                <th align="center" styleClass="smallTH">No Cards</th>
                <th align="center" styleClass="smallTH">No Public Emp</th>
                <th align="center" styleClass="smallTH">No Legislative</th>
            <% } %>
        </TR>
      <!--    iterate over results,  if the column exists display, if not, don't -->  
      <logic:iterate id="element" name="form" property="results" type="org.afscme.enterprise.member.MemberResult">

        <TR>
            <td align="center"><afscme:link page="/viewMemberDetail.action" paramId="personPk" paramName="element" paramProperty="personPk" styleClass="action" title="View Member Detail">View</afscme:link></td>

            <%if (form.getSelectHash().contains("person_nm")) {%>
                <td nowrap align="center"><bean:write name="element" property="personNm"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("p.prefix_nm")) {%>
                <td align="center"><bean:write name="element" property="prefixNm"/></td>
            <% } %>
           <%if (form.getSelectHash().contains("p.nick_nm")) {%>
                <td nowrap align="center"><bean:write name="element" property="nickNm"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("p.ssn")) {%>
                <td align="center"><bean:write name="element" property="ssn"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("p.valid_ssn_fg")) {%>
                <td align="center"><html:checkbox name="element" property="validSsn" disabled="true"/></td>
            <% } %>  
            <%if (form.getSelectHash().contains("affId")) {%>
                <afscme:affiliateIdWrite 
                            name="element" 
                            property="theAffiliateIdentifier"
                />
            <% } %>
            <%if (form.getSelectHash().contains("sma")) {%>
                <td nowrap align="center"><bean:write name="element" property="address"/></td>
                <td nowrap align="center"><bean:write name="element" property="city"/></td>
                <td align="center"><bean:write name="element" property="state"/></td>
                <td align="center"><bean:write name="element" property="zipCode"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("am.mbr_type")) {%>
                <td nowrap align="center"><bean:write name="element" property="mbrType"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("am.mbr_status")) {%>
                <td align="center"><bean:write name="element" property="mbrStatus"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("am.person_pk")) {%>
                <td align="center"><bean:write name="element" property="personPk"/></td>
            <% } %>

            <%if (form.getSelectHash().contains("pa.lst_mod_dt")) {%>
                <td align="center"><afscme:dateWrite name="element" property="addrUpdatedDt"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("pa.lst_mod_user_pk")) {%>
                <td align="center"><afscme:userWrite name="element" property="addrUpdatedByInt"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("am.lst_mod_dt")) {%>
                <td align="center"><afscme:dateWrite name="element" property="lstModDt"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("am.lst_mod_user_pk")) {%>
                <td align="center"><bean:write name="element" property="lstModUserPk"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("p.alternate_mailing_nm")) {%>
                <td nowrap align="center"><bean:write name="element" property="altMailingNm"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("am.primary_information_source")) {%>
                <td align="center"><bean:write name="element" property="primaryInformationSource"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("phone")) {%>
                <td align="center"><bean:write name="element" property="countryCode"/></td>
                <td align="center"><bean:write name="element" property="areaCode"/></td>
                <td align="center"><bean:write name="element" property="phoneNumber"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("email")) {%>
                <td align="center"><bean:write name="element" property="personEmailAddr"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("am.mbr_card_sent_dt")) {%>
                <td align="center"><bean:write name="element" property="mbrCardSentDt"/></td>
            <% } %>
            <%if (form.getSelectHash().contains("mail")) {%>
                <td align="center"><html:checkbox name="element" property="noMailFg" disabled="true"/></td>
                <td align="center"><html:checkbox name="element" property="noCardsFg" disabled="true"/></td>
                <td align="center"><html:checkbox name="element" property="noPublicEmpFg" disabled="true"/></td>
                <td align="center"><html:checkbox name="element" property="noLegislativeMailFg" disabled="true"/></td>
            <% } %>

        </tr>
        </logic:iterate> 
    </table>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <afscme:searchNav formName="form" action="/searchMembers.action"/>
                <BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr>
            <td align="right">
                <BR>
                <afscme:button action="/addMemberDetail.action">Add New Member</afscme:button>&nbsp;
                <afscme:button action="/viewBasicMemberCriteria.action">New Search</afscme:button>
                <BR><BR>
            </TD>
        </tr>
        <tr>
            <td align="left" class="ContentHeaderTD">
                <BR>Total Number of Matches:&nbsp;<bean:write name="form" property="total"/>
            </td>
        </tr>
    </table> 
</logic:present>

<%@ include file="../include/footer.inc" %>
