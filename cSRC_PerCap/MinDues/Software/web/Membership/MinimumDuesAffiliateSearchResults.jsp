<%! String title = "Employer Search Results", help = "AffiliateSearchResults.html";%>
<%@ include file="../include/minimumduesheader.inc" %>

<bean:define id="action" name="affiliateSearchForm" property="searchAction" type="java.lang.String"/> 

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
     	<TD align="left">
		<BR>
		<afscme:button action="/AddEmployer.action">Add New Employer</afscme:button>
		<BR> 
    	</TD>
    	<TD align="right">
		<BR>
		<afscme:button action="/viewBasicAffiliateCriteria.action">New Search</afscme:button>
		<BR> 
    	</TD>
    </TR>
</TABLE>
<br>
<logic:notPresent name="affiliateSearchForm" property="results">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD class="ContentHeaderTD" align="center">
                <bean:message key="error.noResultsFound"/>
            </TD>
        </TR>
    </TABLE>
</logic:notPresent>
<logic:present name="affiliateSearchForm" property="results">
  <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top"> 
      <TD> <afscme:searchNav formName="affiliateSearchForm" action="<%=action%>"/> 
        <BR> </TD>
    </TR>
  </TABLE>
  <table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr> 
      <th width="7%"> Select </th>
      <th width="35%"> 
        <afscme:sortLink styleClass="TH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_ABBR_NAME%>">Employer Name</afscme:sortLink>
      </th>
      <th colspan="5"> Employer Identifier </th>
      <th> Active </th>
    </tr>
    <tr> 
      <td colspan="2">&nbsp; </td>
      <th width="6%">  <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_TYPE%>">Type</afscme:sortLink>
      </th>
      <th width="11%"> <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_LOCAL%>">Local</afscme:sortLink> 
      </th>
      <th width="12%"> <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_STATE%>">State</afscme:sortLink> 
      </th>
      <th width="7%"> <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_SUB_UNIT%>">Subunit</afscme:sortLink> 
      </th>
      <th width="17%"> <afscme:sortLink styleClass="smallTH" action="<%=action%>" formName="affiliateSearchForm" field="<%=org.afscme.enterprise.affiliate.AffiliateCriteria.SORT_BY_AFF_ID_COUNCIL%>">Council</afscme:sortLink> 
      </th>
      <td>&nbsp; </td>
    </tr>
    <logic:iterate name="affiliateSearchForm" property="results" id="result" type="org.afscme.enterprise.affiliate.AffiliateResult">
      <!-- <bean:define id="affId" name="result" property="affiliateId" type="org.afscme.enterprise.affiliate.AffiliateIdentifier"/> -->
      <bean:define id="empAffPk" name="result" property="empAffPk" />
      <tr> 
        <td align="center"> 
			<html:link page="/AffiliateChooseAdd.action" paramId="empAffPk" paramName="empAffPk" styleClass="action">View</html:link> 
        </td>
        <td align="center"> 
			<bean:write name="result" property="affAbreviatedNm"/> 
        </td>
        <afscme:affiliateIdWrite  name="result" property="affiliateId" /> 
        <td align="center"> <bean:write name="result" property="active"/> 
		</td>
      </tr>
    </logic:iterate>
  </table>
  <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top"> 
      <TD> <afscme:searchNav formName="affiliateSearchForm" action="<%=action%>"/> 
        <BR> </TD>
    </TR>
  </TABLE>
  <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top"> 
      <TD align="left"> <BR> <afscme:button action="/AddEmployer.action">Add New 
        Employer</afscme:button> <BR> </TD>
      <TD align="right"> <BR> <afscme:button action="/viewBasicAffiliateCriteria.action">New 
        Search</afscme:button> <BR> </TD>
    </TR>
  </TABLE>
</logic:present>
<%@ include file="../include/footer.inc" %>
