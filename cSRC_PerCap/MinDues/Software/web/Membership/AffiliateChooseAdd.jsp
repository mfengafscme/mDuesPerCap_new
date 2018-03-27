<%! String title = "Employer Data ", help = "AffiliateDetail.html";%>
<%@ include file="../include/minimumdues_simple_header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%
String showSearchResult = (String) request.getAttribute("showSearchResult"); 
%> 

<script type="text/javascript" language="JavaScript" src="js/affiliateChooseAdd.js"></script>

<table width="95%" align="center">
<tr>
	<td width="90%" align="right">
		<logic:notPresent parameter="showSearchResult">
		    <afscme:link page="/searchAffiliate.action" styleClass="action">Search Result</afscme:link> 
		</logic:notPresent>
	</td>
	<td align="right">
		<afscme:link forward="MainMenu" title="Return to AFSCME Main Menu" styleClass="action" postfix="&nbsp;&nbsp;">Main Menu</afscme:link> 
	</td>
</tr>
</table>
<br>
<bean:define id="affiliateChooseAdd" name="affiliateChooseAdd" type="org.afscme.enterprise.minimumdues.web.AffiliateChooseAddForm"/>

<TABLE width="50%" cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD colspan="2" class="ContentHeaderTR">
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    
			<TH width="17%" height="41"> 
					Type 
			</TH>
			<TH width="17%">
				Local
			    </TH>
			    <TH width="24%">
				State
			    </TH>
			    <TH width="17%">
				Sub Unit 
			    </TH>
			<TH width="25%"> 
				Council
			</TH>
			<TH width="25%"> 
				&nbsp;Active&nbsp;
			</TH>
                </TR>
                <TR>
		  <td align="center"><bean:write name="affiliateChooseAdd" scope="request" property="type"/></td>
		  <td align="center"><bean:write name="affiliateChooseAdd" scope="request" property="local"/></td>
		  <td align="center"><bean:write name="affiliateChooseAdd" scope="request" property="state"/></td>
		  <td align="center"><bean:write name="affiliateChooseAdd" scope="request" property="chapter"/></td>
		  <td align="center"><bean:write name="affiliateChooseAdd" scope="request" property="council"/></td>
 		  <td align="center"><bean:write name="affiliateChooseAdd" scope="request" property="status"/></td>
                </TR>
            </TABLE>
        </TD>
    </TR>
    <TR valign="top">
        <TD width="65%">
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
          			<TD class="ContentHeaderTD" width="18%">
						Employer Name: 
					</TD>
                    <TD class="ContentTD" align="left">
                        <b><bean:write name="affiliateChooseAdd" scope="request" property="employer"/></b>
                    </TD>
              	</TR>
            </TABLE>
        </TD>
    </TR>
	<tr>
    <td colspan="2">&nbsp;</td>
  </tr>
	<tr>
	<html:form action="/EditDelEmployer.do">
	<html:hidden property="empAffPk" value='<%=(String)request.getParameter("empAffPk")%>'/>
    <td align="center">
	<html:submit property="editEmpButton" styleClass="button" >&nbsp;&nbsp;Edit&nbsp;&nbsp;</html:submit>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <html:submit property="deleteEmpButton" styleClass="button" onclick="return deleteEmpWarning();">Delete</html:submit>
	</td>
	</html:form>
	</tr>
</TABLE>
<br><br>
<p>
<H4 align="center" ><font color="#008000"><u>Wage Increase Data</u></font></H4>
</p>

<html:form action="/ViewDataEntry.do">
  <html:hidden property="empAffPk" value='<%=(String)request.getParameter("empAffPk")%>'/>
  <html:hidden property="empEditable" value='<%= (String) request.getAttribute("empEditable")%>' />
  <html:hidden property="empActive" value='<%= (String) request.getAttribute("empActive")%>' />
  
  <table width="30%" border="1" align="center" cellpadding="0" cellspacing="0" class="BodyContent">
    <tr>
    <th align="center"><b>Choose a Year to View:</b></th>
  </tr>
  <tr>
    <TD align="center">
           <html:select property="viewYear">
           <%  java.util.ArrayList existingYearList = (java.util.ArrayList) request.getAttribute("existingYear");
                int i = 0;
                
                for (i = 0; i < existingYearList.size(); i++) { %>
                  <html:option value="<%= (String) existingYearList.get(i) %>"></html:option>
                <% } %>   
           </html:select>
    </TD>
  </tr>
  <tr>
    <td align="center"><html:submit styleClass="button" onclick="return checkExistingYear();" >Submit</html:submit></td>
  </tr>
</table>
</html:form>
<br><br><br>

<html:form action="/AddDataEntry.do">
<html:hidden property="empAffPk" value='<%=(String)request.getParameter("empAffPk")%>'/>
<html:hidden property="empActive" value='<%= (String) request.getAttribute("empActive")%>' />
<table width="30%" cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
  <tr>
    <th align="center"><b>Add a New Data Entry Form for the Year:</b></th>
  </tr>
  <tr>
    <TD align="center">
        <html:select property="addYear">
           <%   
                java.util.ArrayList existingYearList = (java.util.ArrayList) request.getAttribute("existingYear");
                
                //java.util.Calendar cal = new java.util.GregorianCalendar();
                //int currentDuesYear = cal.get(java.util.Calendar.YEAR) + 1;
                int currentDuesYear = Integer.parseInt((String) request.getSession().getAttribute("currentDuesYear")); 
                int fiveYearAhead = currentDuesYear + 4;
                
                java.util.ArrayList otherYearList = new java.util.ArrayList();
                for (int i = currentDuesYear; i <= fiveYearAhead; i++) { 
                if ( !(existingYearList.contains(""+i)) )  {
                %>
                  <html:option value="<%= ""+i %>"></html:option>
                <% } } %>    
	</html:select>
    </TD>
    <%
       request.removeAttribute("existingYear");
    %>
  </tr>
  <tr>
    <td align="center"><html:submit property="addNewYearButton" styleClass="button" onclick="return checkAddYear();" >Submit</html:submit></td>
  </tr>
</table>
</html:form>

<br><br>
<%@ include file="../include/footer.inc" %>
