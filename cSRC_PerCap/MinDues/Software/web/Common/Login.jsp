<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%!String title = "Login", help = "Login.html";%>
<%@ include file="../include/minimumduesheader.inc" %>

<!--AFSCME Release Version = R1_Final_Delivery_1.0 -->
<!--AFSCME Release Date = 2003-12-17 -->
<!--AFSCME Release Time = 12:00 PM -->

<html:form action="login" focus="userId">

    <table align="center" width="40%"><tr><td>

        <table class="BodyContentNoWidth">
          <tr>
            <td align=middle colSpan="3">Please enter your user ID and password below, 
              then click the "Login" button to access the AFSCME Enterprise Application. 
            </td>
          </tr>
          <tr>
            <td nowrap align="left"><label for="userId">Enter user ID</label></td>
            <td align="left"><html:text property="userId" size="15"/></td>
            <td nowrap width="100%"><html:errors property="userId"/></td>
          </tr>
          <tr>
            <td nowrap align="left"><label for="password">Enter password</label></td>
            <td align="left"><html:password property="password" size="15"/></td>
            <td nowrap width="100%"><html:errors property="password"/></td>
          </tr>
        </table>

        </td></tr>
        <tr><td colspan="2">
        <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
        </td></tr>
        <tr><td>
	<br>

        <table width="100%">
          <tr>
            <td align="left">
                <html:submit styleClass="button" value="Login"/>
            </td>
            <td align="right">
                <html:reset styleClass="button"/>
            </td>
          </tr>
        </table>
        
    <td><tr></table>

</html:form>



