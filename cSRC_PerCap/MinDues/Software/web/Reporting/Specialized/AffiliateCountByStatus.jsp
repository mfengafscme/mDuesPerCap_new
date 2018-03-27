<%-- Program Name: AffiliateCountByStatus.jsp
     Date Written: 20030905
     Author      : Kyung A. Callahan
     Description : This collects user input prior to generating the Affiliate
                   Counts by Affiliate type.
     Note: 
     Maintenance : Kyung 20030912 cosmetic changes
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%! String title = "Affilite Counts by Affiliate Type", help = "AffiliateCountByStatus.html";%>
<%@ include file="../../include/header.inc" %> 
<html:form action="/affiliateCounByStatusReport">
<p><i>This report will show the affiliate counts by affiliate type.</i></p>
Report Type:
<html:select property="reportType" onblur="reportType(this.form);" onchange="reportType(this.form)">
<html:option value="Select a Report Type List">Select a Report Type List</html:option>
<html:option value="Detail">Detail</html:option>
<html:option value="Summary">Summary</html:option>
</html:select>
<br><br>
<html:submit styleClass="BUTTON"/>
</html:form>
<%@ include file="../../include/footer.inc" %>