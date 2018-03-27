<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Person Verify Results", help = "MemberVerifyResults.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="verifyMemberForm" type="org.afscme.enterprise.member.web.VerifyMemberForm"/>

<table width="100%" align="center">
    <tr>
        <td align="left">
            <afscme:searchNav formName="form" action="/viewVerifyMember.action"/></td>
        <td align="right">
            <afscme:button page="/addMemberDetail.action?back=viewVerifyMember" postfix="&nbsp;">Add New</afscme:button>
            <afscme:button page="/viewVerifyMember.action?new" >New Search</afscme:button></td> 
    </tr>
</table>

<BR>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="BodyContentNoWidth" align="center">
    <tr align="center">
        <th width="5%" align="center">Select</th>
        <th width="5%" align="center" colspan="5">Affiliate Identifier</th>
        <th align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyMember.action" formName="form" field="person_nm" title="Sort By Person Name">Name</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyMember.action" formName="form" field="personAddr" title="Sort By Address">Address</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyMember.action" formName="form" field="personAddrCity" title="Sort By City">City</afscme:sortLink></th>
        <th width="3%" align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyMember.action" formName="form" field="personAddrState" title="Sort By State">State</afscme:sortLink></th>
        <th width="8%" align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyMember.action" formName="form" field="personAddrPostalCode" title="Sort By Zip/Postal Code">Zip/Postal Code</afscme:sortLink></th>
        <th width="8%" align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyMember.action" formName="form" field="ssn" title="Sort By SSN">SSN</afscme:sortLink></th>
    </tr>
    <tr align="center">
        <td>&nbsp;</td>
        <th align="center"><afscme:sortLink styleClass="smallTH" action="/viewVerifyMember.action" formName="form" field="aff_type" title="Sort By Type">Type</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="smallTH" action="/viewVerifyMember.action" formName="form" field="int_local" title="Sort By Local">Loc/Sub chap</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="smallTH" action="/viewVerifyMember.action" formName="form" field="aff_stateNat_type" title="Sort By State">State/Natl</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="smallTH" action="/viewVerifyMember.action" formName="form" field="aff_SubUnit" title="Sort By SubUnit">Sub Unit</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="smallTH" action="/viewVerifyMember.action" formName="form" field="int_council" title="Sort By Council">Cn/Ret Chap</afscme:sortLink></th>
    </TR>
    <logic:iterate id="element" name="form" property="results" type="org.afscme.enterprise.member.MemberResult">
    <tr>
        <td align="center"> <afscme:link page="/viewMemberDetailAddAffiliation.action?from=VerifyResults" paramId="personPk" paramName="element" paramProperty="personPk" title="Create Member" styleClass="action">Add</afscme:link> 
        <logic:notPresent name="form" property="vduAffiliates"> 
       <%--     <%if (form.getVduAffiliates() == null) { %>  --%>
            <afscme:link page="/viewMemberDetail.action" paramId="personPk" paramName="element" paramProperty="personPk" title="View Member Details" styleClass="action">View</afscme:link>
       <%--     <% } else { %> --%>
       <!--     &nbsp; -->
        <%--    <% } %> --%>
         </logic:notPresent> 
         <logic:present name="form" property="vduAffiliates">&nbsp;</logic:present> 
        </td>
        <td align="center">
            <logic:present name="element" property="theAffiliateIdentifier.type">
                <bean:write name="element" property="theAffiliateIdentifier.type"/>
            </logic:present>
            <logic:notPresent name="element" property="theAffiliateIdentifier.type">&nbsp;</logic:notPresent>
        </td>
        <td align="center">
            <logic:present name="element" property="theAffiliateIdentifier.local">
                <bean:write name="element" property="theAffiliateIdentifier.local"/>
            </logic:present>
            <logic:notPresent name="element" property="theAffiliateIdentifier.local">&nbsp;</logic:notPresent>
        </td>
        <td align="center">
            <logic:present name="element" property="theAffiliateIdentifier.state">
                <bean:write name="element" property="theAffiliateIdentifier.state"/>
            </logic:present>
            <logic:notPresent name="element" property="theAffiliateIdentifier.state"/>&nbsp;</logic:notPresent>
        </td>
        <td align="center">
            <logic:present name="element" property="theAffiliateIdentifier.subUnit">
                <bean:write name="element" property="theAffiliateIdentifier.subUnit"/>
            </logic:present>
            <logic:notPresent name="element" property="theAffiliateIdentifier.subUnit">&nbsp;</logic:notPresent>
        </td>
        <td align="center">
            <logic:present name="element" property="theAffiliateIdentifier.council">
                <bean:write name="element" property="theAffiliateIdentifier.council"/>
            </logic:present>
            <logic:notPresent name="element" property="theAffiliateIdentifier.council">&nbsp;</logic:notPresent>
        </td>

        <td align="center"><bean:write name="element" property="personNm"/></td>
        
        <td align="center">
            <logic:present name="element" property="address">
                <bean:write name="element" property="address"/>
            </logic:present>
            <logic:notPresent name="element" property="address">&nbsp;</logic:notPresent>
        </td>
        <td align="center">
            <logic:present name="element" property="city">
                <bean:write name="element" property="city"/>
            </logic:present>
            <logic:notPresent name="element" property="city">&nbsp;</logic:notPresent>
        </td>
        <td align="center">
            <logic:present name="element" property="state">
                <afscme:codeWrite name="element" property="state" codeType="State" useCode="true" format="{0}"/>
            </logic:present>
            <logic:notPresent name="element" property="state">&nbsp;</logic:notPresent>
        </td>
        <td align="center">
            <logic:present name="element" property="zipCode">
                <bean:write name="element" property="zipCode"/>
            </logic:present>
            <logic:notPresent name="element" property="zipCode">&nbsp;</logic:notPresent>
        </td>
        <td align="center"><bean:write name="element" property="ssn"/></td>
    </tr>
    </logic:iterate>
</table>
<BR>
<table width="100%" align="center">
    <tr>
        <td align="left">
           <afscme:searchNav formName="form" action="/viewVerifyMember.action"/></td>
        <td align="right">
            <afscme:button page="/addMemberDetail.action?back=viewVerifyMember" postfix="&nbsp;">Add New</afscme:button>
            <afscme:button page="/viewVerifyMember.action?new" >New Search</afscme:button></td>
    </tr>
    <tr>
        <td align="left" class="ContentHeaderTD">
            <BR>Total Number of Matches:&nbsp;<bean:write name="form" property="total"/>
        </td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>