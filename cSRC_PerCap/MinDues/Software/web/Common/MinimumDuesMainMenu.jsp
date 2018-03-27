
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "", help = "MainMenu.html";%>
<%@ include file="../include/minimumduesheader.inc" %>

<div align="center"><font color="#808000" size="4"><strong>Minimum Dues Main Menu</strong></font><br>
</div><br><br>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="75%" align="center">
    <tr valign="top">  
    <th width="25%">Employer</th>
    <th width="26%">Wage Increase</th>
	<th width="29%">Agreement</th>
	<th width="20%">Import / Reports</th>
    </tr>
    <tr valign="top">
    <td> 
	    <ul>
                <afscme:link page="/viewBasicAffiliateCriteria.action?new" title="Search for Employer" prefix="<li>" postfix="</li>">Search Employer</afscme:link>
                <afscme:link page="/AddEmployer.action" title="Add Employer" prefix="<li>" postfix="</li>">Add Employer</afscme:link>
            </ul>
    </td>
    <td>
	    <ul>
	    	<afscme:link page="/DataEntry.action" title="Wage Increase Form" prefix="<li>" postfix="</li>">Wage Increase Form</afscme:link>
		<afscme:link page="/viewPreAffiliateCriteria.action?new" title="Process Imported Data" prefix="<li>" postfix="</li>">Process Imported Data</afscme:link>
                <%-- <li><html:link href="/mdu/Minimumdues/DataEntry.jsp">Wage Increase Form</html:link></li> --%>
            </ul>
    </td>
	<td>           
	    <ul>
                <afscme:link page="/AddAgreement.action" title="Add New Agreement" prefix="<li>" postfix="</li>">Add New Agreement</afscme:link>
                <afscme:link page="/EditAgreement.action" title="Manage Agreement" prefix="<li>" postfix="</li>">Edit Agreement</afscme:link>
            </ul>

	</td>
	<td>           
	    <ul>
        	<li><a href="http://191.1.0.105:8282/crystal/MDU/" Target="_blank">Report Server </a> </li>
        </ul>			    
	</td> 
    </tr>
	
</table>
<br><br><br><br><br><br><br><br>
<%@ include file="../include/footer.inc" %>
