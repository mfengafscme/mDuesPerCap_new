<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Person Search Results", help = "PersonSearchResults.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="searchPersonForm" type="org.afscme.enterprise.person.web.SearchPersonForm"/>

<!-- if no results returned go to the PersonDetailAdd page -->


<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr>
        <td align="right">
            <BR>
            <afscme:button action="/verifyPerson.action?back=PersonAdd">Add New Person</afscme:button>&nbsp;
            <afscme:button action="/viewBasicPersonCriteria.action">New Search</afscme:button>
            <BR><BR>
        </td> 
    </tr>
</table>
<logic:present name="form" property="results"> 
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <afscme:searchNav formName="form" action="/searchPerson.action"/>
                <BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="1" cellspacing="0" border="1" class="BodyContent" align="center">
        <tr align="center">
            <th width="10%" align="center">Select</th>
            <!-- List each column in order desired, wrapped by logic which tests for the presence of the column in the collection -->
            <!-- Title Row 1 -->
            <th align="center"><afscme:sortLink styleClass="TH" action="/searchPerson.action" formName="form" field="person_nm" title="Sort By Name">Name</afscme:sortLink></th> 
            <th align="center" colspan="5">Affiliate Identifier</th>
            <th align="center" colspan="4">System Mailing Address</th>
            <th align="center"><afscme:sortLink styleClass="TH" action="/searchPerson.action" formName="form" field="ssn" title="Sort By SSN">SSN</afscme:sortLink></th>
            <th align="center"><afscme:sortLink styleClass="TH" action="/searchPerson.action" formName="form" field="userId" title="Sort By User Id">User ID</afscme:sortLink></th>
        </TR>
        <!-- Title Row 2 (Composite details)-->
        <TR>
            <TD>&nbsp;</TD>
            <TD>&nbsp;</TD>
            <!-- Affiliate Identifier -->
            <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchPerson.action" formName="form" field="a.aff_type" title="Sort By Type">Type</afscme:sortLink></th>
            <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchPerson.action" formName="form" field="int_local" title="Sort By Local">Loc/Sub chap</afscme:sortLink></th>
            <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchPerson.action" formName="form" field="a.aff_stateNat_type" title="Sort By State">State/Nat'l</afscme:sortLink></th>
            <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchPerson.action" formName="form" field="a.aff_SubUnit" title="Sort By SubUnit">Sub Unit</afscme:sortLink></th>
            <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchPerson.action" formName="form" field="int_council" title="Sort By Council">Cn/Ret Chap</afscme:sortLink></th>
            <!-- System Mailing Address -->
            <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchPerson.action" formName="form" field="address" title="Sort By Address">Address</afscme:sortLink></th>
            <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchPerson.action" formName="form" field="pa.city" title="Sort By City">City</afscme:sortLink></th>
            <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchPerson.action" formName="form" field="pa.state" title="Sort By State">State</afscme:sortLink></th>
            <th align="center"><afscme:sortLink styleClass="smallTH" action="/searchPerson.action" formName="form" field="pa.zipcode" title="Sort By Zip">Zip/Postal Code</afscme:sortLink></th>
            <TD>&nbsp;</TD>
            <TD>&nbsp;</TD>
        </TR>
      <!--    iterate over results,  if the column exists display, if not, don't -->  
      <logic:iterate id="element" name="form" property="results" type="org.afscme.enterprise.person.PersonResult">

        <TR>
            <td align="center"><afscme:link page="/viewPersonDetail.action" paramId="personPk" paramName="element" paramProperty="personPk" styleClass="action" title="View Person Detail">View</afscme:link></td>

            <td nowrap align="center"><bean:write name="element" property="personNm"/></td>
            <!-- Affiliate Identifier -->
            <logic:present name="element" property="theAffiliateIdentifier">
                <afscme:affiliateIdWrite name="element" property="theAffiliateIdentifier"/> 
            </logic:present>
            <logic:notPresent name="element" property="theAffiliateIdentifier"><td>&nbsp;</td></logic:notPresent>
            <logic:notPresent name="element" property="theAffiliateIdentifier"><td>&nbsp;</td></logic:notPresent>
            <logic:notPresent name="element" property="theAffiliateIdentifier"><td>&nbsp;</td></logic:notPresent>
            <logic:notPresent name="element" property="theAffiliateIdentifier"><td>&nbsp;</td></logic:notPresent>
            <logic:notPresent name="element" property="theAffiliateIdentifier"><td>&nbsp;</td></logic:notPresent>
            <!-- System Mailing Address -->
            <td nowrap align="center">
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
                    <bean:write name="element" property="state"/>
                </logic:present>
                <logic:notPresent name="element" property="state">&nbsp;</logic:notPresent>
            </td>
            <td align="center">
                <logic:present name="element" property="personAddrPostalCode">
                    <bean:write name="element" property="personAddrPostalCode"/>
                </logic:present>
                <logic:notPresent name="element" property="personAddrPostalCode">&nbsp;</logic:notPresent>
            </td>
            <td align="center">
                <logic:present name="element" property="ssn">
                    <bean:write name="element" property="ssn"/>
                </logic:present>
                <logic:notPresent name="element" property="ssn">&nbsp;</logic:notPresent>
            </td>
            <td align="center"><bean:write name="element" property="userId"/></td>
        </tr>
        </logic:iterate> 
    </table>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <afscme:searchNav formName="form" action="/searchPerson.action"/>
                <BR>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr>
            <td align="right">
                <BR>
                <afscme:button action="/verifyPerson.action?back=PersonAdd">Add New Person</afscme:button>&nbsp;
                <afscme:button action="/viewBasicPersonCriteria.action">New Search</afscme:button>
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
