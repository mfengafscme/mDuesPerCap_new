<%! String title = "Duplicate SSN Notifier", help = "DuplicateSSNNotifier.html";%>
<%@ include file="../include/header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<bean:define id="form" name="verifyPersonForm" type="org.afscme.enterprise.person.web.VerifyPersonForm"/>

<%String rtnCancel = "/cancelSavePerson.action?back="+request.getSession().getAttribute("back");%>
<%String rtnSave = "/continueSavePerson.action?back="+request.getSession().getAttribute("back");%>
<!-- Display instructions -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR" colspan="11" align="center">
            <BR>The following records have a matching Social Security Number to the one just entered. 
            <BR>Press the 'Cancel' button, to stop the Add/Edit, or press the 'Continue' button to proceed. 
            <BR><BR> 
        </TD>
    </TR>
</TABLE>
<!-- Display buttons -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr>
<!-- NEEDS TO BE DYNAMIC ON SCREEN RETURN/NAVIGATION (back and last screen) -->
        <td align="left">
            <afscme:button action="<%=rtnSave%>" tabindex="0">Continue</afscme:button>
       </td>
        <td align="right">
            <afscme:button action="<%=rtnCancel%>">Cancel</afscme:button>
        </td>       
    </tr>      
    <tr valign="top">
        <td colspan="3"><BR></td>
    </tr>   
</TABLE>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="BodyContentNoWidth" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR" colspan="11">
            Social Security Number: 
            <bean:write name="form" property="ssn1"/>-<bean:write name="form" property="ssn2"/>-<bean:write name="form" property="ssn3"/> 
            <BR><BR> 
        </TD>
    </TR>
    <tr align="center">
        <th align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyPerson.action" formName="form" field="personNm" title="Sort By Person Name">Name</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyPerson.action" formName="form" field="personAddr" title="Sort By Address">Address</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyPerson.action" formName="form" field="personAddrCity" title="Sort By City">City</afscme:sortLink></th>
        <th width="3%" align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyPerson.action" formName="form" field="personAddrState" title="Sort By State">State</afscme:sortLink></th>
        <th width="8%" align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyPerson.action" formName="form" field="personAddrPostalCode" title="Sort By Zip/Postal Code">Zip/Postal Code</afscme:sortLink></th>
        <th width="8%" align="center"><afscme:sortLink styleClass="TH" action="/viewVerifyPerson.action" formName="form" field="ssn" title="Sort By SSN">SSN</afscme:sortLink></th>
    </tr>
    <logic:iterate id="element" name="form" property="personResult" type="org.afscme.enterprise.person.PersonResult">
    <tr>
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

<!-- Display buttons -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr>
<!-- NEEDS TO BE DYNAMIC ON SCREEN RETURN/NAVIGATION (back and last screen) -->
        <td align="left">
            <afscme:button action="<%=rtnSave%>">Continue</afscme:button>
       </td>
        <td align="right">
            <afscme:button action="<%=rtnCancel%>">Cancel</afscme:button>
        </td>
    </tr>      
    <tr valign="top">
        <td colspan="3"><BR></td>
    </tr>   
</TABLE>

<%@ include file="../include/footer.inc" %> 
