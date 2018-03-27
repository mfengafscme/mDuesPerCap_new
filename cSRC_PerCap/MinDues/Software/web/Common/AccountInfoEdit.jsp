<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Account Info", help = "AccountInfo.html";%>
<%@ include file="../include/header.inc" %>

    <html:form action="editAccountInfo.action">

    <html:hidden property="forceChangePassword"/>

    <table align="center"><tr><td>

        <table class="BodyContentNoWidth">
          <tr>
            <logic:equal name="accountInfoForm" property="forceChangePassword" value="true">
                <td colspan="3">You are required to choose a new password before you can continue to access the application.</td>
            </logic:equal>
            <logic:notEqual name="accountInfoForm" property="forceChangePassword" value="true">
                <td nowrap align="left"><label for="password">Current Password</label></td>
                <td align="left"><html:password property="password" size="15"/></td>
                <td nowrap width="100%"><html:errors property="password"/></td>
            </logic:notEqual>
          </tr>
          <tr>
            <td nowrap align="left"><label for="newPassword">Choose a new password</label></td>
            <td nowrap align="left"><html:password property="newPassword" size="15" maxlength="30"/><html:errors property="newPassword"/></td>
          </tr>
          <tr>
            <td nowrap align="left"><label for="newPassword2">Re-Enter new password</label></td>
            <td nowrap align="left"><html:password property="newPassword2" size="15" maxlength="30"/><html:errors property="newPassword2"/></td>
          </tr>
          <tr>
            <td nowrap align="left"><label for="challengeQuestion">Challenge Question</label></td>
            <td nowrap align="left">
                <html:select property="challengeQuestion">
                    <afscme:codeOptions codeType="ChallengeQuestion" format="{1}"/>
                </html:select>
                <html:errors property="challengeQuestion"/></td>
          </tr>
          <tr>
            <td nowrap align="left"><label for="challengeResponse">Challenge Response</label></td>
            <td nowrap align="left"><html:text property="challengeResponse" size="50" maxlength="50"/><html:errors property="challengeResponse"/></td>
          </tr>
        </table>

        </td></tr>
        <tr><td colspan="3">
        <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
        </td></tr>
        <tr><td>

        <table width="100%">
          <tr>
            <td align="left">
                <html:submit styleClass="button"/>
            </td>
            <td align="right">
                <html:reset styleClass="button"/>
                <html:cancel styleClass="button"/>
            </td>
          </tr>
        </table>

    <td><tr></table>

</html:form>

<%@ include file="../include/footer.inc" %>

<%--
This javascript puts intial focus password text box if it is present,
otherwise it puts the focus on the newPassword text box.  This is necessary,
because the password box is not shown when the user arrives at this page
due to an expired password.
--%>
<script language="JavaScript" type="text/javascript">
  <!--
    if (document.forms["accountInfoForm"].elements["password"] != null) {
        document.forms["accountInfoForm"].elements["password"].focus()
    } else {
        document.forms["accountInfoForm"].elements["newPassword"].focus();
    }
  // -->
</script>

