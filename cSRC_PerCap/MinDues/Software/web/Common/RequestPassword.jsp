<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Request Password", help = "RequestPassword.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="requestPassword.action" focus="response">

    <html:hidden property="personPk"/>

    <table align="center" width="40%"><tr><td>

        <table class="BodyContentNoWidth">
          <tr>
            <td align="left" colSpan="3">
                <afscme:codeWrite codeType="ChallengeQuestion" property="challengeQuestion" format="{1}" writeHidden="true"/>
            </td>
          </tr>
          <tr>
            <td nowrap align="left"><label for="response">Response</label></td>
            <td align="left"><html:text property="response" size="15"/></td>
            <td nowrap width="100%"><html:errors property="response"/></td>
          </tr>
        </table>
        </td></tr>
        <tr><td>

        <table width="100%">
            <tr>
                <td>
                    <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
                </td>
            </tr>
            <tr>
                <td align="left">
                    <html:submit styleClass="button"/>
                </td>
            </tr>
        </table>

    <td><tr></table>

</html:form>

<%@ include file="../include/footer.inc" %>

