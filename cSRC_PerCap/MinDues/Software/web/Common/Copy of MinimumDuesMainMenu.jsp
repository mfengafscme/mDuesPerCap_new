
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "", help = "MainMenu.html";%>
<%@ include file="../include/minimumduesheader.inc" %>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="75%" align="center">
    <tr valign="top">
        
    <th>Employer</th>
    <th>Wage Increase</th>
	<th>Reports</th>
    </tr>
    <tr valign="top">
    <td> 
	    <ul>
                <afscme:link page="/viewBasicAffiliateCriteria.action?new" title="Search for Employer" prefix="<li>" postfix="</li>">Search Employer</afscme:link>
                <li>
          	<html:link href="/mdu/Minimumdues/AddEmployer.jsp">Add Employer</html:link>
        	</li>
            </ul>
    </td>
    <td>
	 		<ul>
		        <li><html:link href="/mdu/Minimumdues/DataEntry.jsp">Increase Form</html:link></li>
		        <%-- <afscme:link page="/listRegularReports.action" title="History" prefix="<li>" postfix="</li>">History</afscme:link> --%>
            </ul>
    </td>
	<td>           
			<ul>
        <a href="http://191.1.0.144:8282/crystal/MDU/" Target="_blank">Report Server </a> 
      </ul>
	</td>
    </tr>
</table>
<br><br><br><br><br><br><br><br>
<%@ include file="../include/footer.inc" %>
