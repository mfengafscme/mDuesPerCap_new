<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Person Verify Results", help = "PersonVerifyResults.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="verifyPersonForm" type="org.afscme.enterprise.person.web.VerifyPersonForm"/>

<%String sortAction = "/viewVerifyPerson.action?back="+form.getBack();%>

<table width="100%" align="center">
    <tr>
        <td align="left">
            <afscme:searchNav formName="form" action="/viewVerifyPerson.action"/></td>
        <td align="right">
<% if (form.getBack() != null && form.getBack().equals("AssociateAdd")) { %>
            <afscme:button page="/editOrganizationAssociateDetail.action?newPerson=true" postfix="&nbsp;">Add New</afscme:button>
<% } else { %>
            <afscme:button page="/addPersonDetail.action?back=PersonAdd" postfix="&nbsp;">Add New</afscme:button>
<% } %>
            <afscme:button page="/verifyPerson.action?cancel" >New Search</afscme:button></td> 
    </tr>
</table>

<BR>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="BodyContentNoWidth" align="center">
    <tr align="center">
        <th width="5%" align="center">Select</th>
        <th align="center"><afscme:sortLink styleClass="TH" action="<%=sortAction%>" formName="form" field="personNm" title="Sort By Person Name">Name</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="TH" action="<%=sortAction%>" formName="form" field="personAddr" title="Sort By Address">Address</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="TH" action="<%=sortAction%>" formName="form" field="personAddrCity" title="Sort By City">City</afscme:sortLink></th>
        <th width="3%" align="center"><afscme:sortLink styleClass="TH" action="<%=sortAction%>" formName="form" field="personAddrState" title="Sort By State">State</afscme:sortLink></th>
        <th width="8%" align="center"><afscme:sortLink styleClass="TH" action="<%=sortAction%>" formName="form" field="personAddrPostalCode" title="Sort By Zip/Postal Code">Zip/Postal Code</afscme:sortLink></th>
        <th width="8%" align="center"><afscme:sortLink styleClass="TH" action="<%=sortAction%>" formName="form" field="ssn" title="Sort By SSN">SSN</afscme:sortLink></th>
    </tr>
    <logic:iterate id="element" name="form" property="personResult" type="org.afscme.enterprise.person.PersonResult">
    <tr>
        <td align="center" nowrap>
<% if (form.getBack() != null && form.getBack().equals("AssociateAdd")) { %>
            <afscme:link page="/editOrganizationAssociateDetail.action" paramId="personPk" paramName="element" paramProperty="personPk" title="Add From This Existing Person" styleClass="action">Add</afscme:link>
<% } %>
            <afscme:link page="/viewPersonDetail.action" paramId="personPk" paramName="element" paramProperty="personPk" title="View Person Details" styleClass="action">View</afscme:link>
        </td>
        <td align="center"><bean:write name="element" property="personNm"/></td>
        <td align="center">
            <logic:present name="element" property="personAddr">
                <bean:write name="element" property="personAddr"/>
            </logic:present>
            <logic:notPresent name="element" property="personAddr">&nbsp;</logic:notPresent>
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
            <logic:present name="element" property="personAddrPostalCode">
                <bean:write name="element" property="personAddrPostalCode"/>
            </logic:present>
            <logic:notPresent name="element" property="personAddrPostalCode">&nbsp;</logic:notPresent>
        </td>
        <td align="center"><bean:write name="element" property="ssn"/></td>
    </tr>
    </logic:iterate>
</table>
<BR>
<table width="100%" align="center">
    <tr>
        <td align="left">
           <afscme:searchNav formName="form" action="/viewVerifyPerson.action"/></td>
        <td align="right">
<% if (form.getBack() != null && form.getBack().equals("AssociateAdd")) { %>
            <afscme:button page="/editOrganizationAssociateDetail.action?newPerson=true" postfix="&nbsp;">Add New</afscme:button>
<% } else { %>
            <afscme:button page="/addPersonDetail.action?back=PersonAdd" postfix="&nbsp;">Add New</afscme:button>
<% } %>
            <afscme:button page="/verifyPerson.action?cancel" >New Search</afscme:button></td>
    </tr>
    <tr>
        <td align="left" class="ContentHeaderTD">
            <BR>Total Number of Matches:&nbsp;<bean:write name="form" property="total"/>
        </td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>
