<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%-- Header --%>
<%!String title = "User Edit", help = "EditUser.html";%>
<%@ include file="../include/header.inc" %>

<%-- Edit User form --%>
<html:form action="saveUser" focus="userId">

    <table align="center" width="95%">
        <tr>
            <td colspan="2" align="center"><html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
        <tr>
            <td colspan="2">

                <%-- Displayed User Properties --%>

                <table class="BodyContentNoWidth" width="100%">
                    <tr>
                        <td nowrap><label for="userId">User ID</label></td>
                        <td width="50%"><html:text property="userId" size="30" maxlength="10" tabindex="1"/><html:errors property="userId"/></td>
                        <td nowrap><label for="challengeQuestion">Challenge Question</label></td>
                        <td width="50%">
                            <html:select property="challengeQuestion" tabindex="4">
                                <afscme:codeOptions codeType="ChallengeQuestion" format="{1}"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td nowrap><label for="password">Password</label></td>
                        <td nowrap width="50%"><html:password property="password" size="30" maxlength="30" tabindex="2"/><html:errors property="password"/></td>
                        <td nowrap><label for="challengeResponse">Challenge Response</label></td>
                        <td nowrap width="50%"><html:text property="challengeResponse" size="30" maxlength="30" tabindex="5"/><html:errors property="challengeResponse"/></td>
                    </tr>
                    <tr>
                        <td nowrap><label for="password2">Repeat Password</label></td>
                        <td nowrap width="50%"><html:password property="password2" size="30" maxlength="30" tabindex="3"/><html:errors property="password2"/></td>
                        <td nowrap><label for="remarks">Remarks</label></td>
                        <td nowrap width="50%"><html:text property="remarks" size="50" maxlength="50" tabindex="6"/><html:errors property="remarks"/></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Password Expires</label></td>
                        <td nowrap width="50%"><html:hidden property="passwordExpirationDate"/><bean:write name="userForm" property="passwordExpirationDate"/></td>
                        <td nowrap><label for="department">Department</label></td>
                        <td nowrap width="50%">
                            <html:select property="department" tabindex="7">
                                <afscme:codeOptions codeType="Department" allowNull="true" nullDisplay="None"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td nowrap><label>Last Login</label>
                        <td nowrap width="50%"><html:hidden property="lastLoginDate"/><bean:write name="userForm" property="lastLoginDate"/>
                        <td nowrap><label for="startPage">Start Page</label>
                        <td width="50%">
                            <bean:define id="startPage" name="userForm" property="startPage"/>
                            <html:select property="startPage" tabindex="8">
                                <afscme:codeOptions codeType="StartPage" format="{1}" useCode="true"/>
                            </html:select>
                    </tr>
                    <tr>
                        <td nowrap><label>Roles</label>
                        <td width="100%" colspan="3">
                        <html:hidden property="roles"/>
                        <bean:write name="userForm" property="roles"/>
                    </tr>
                    <logic:equal name="userForm" property="lockedOut" value="true">
                    <tr>
                        <td nowrap><label>Locked Out Since</label>
                        <td width="50%"><html:hidden property="lockoutDate"/><bean:write name="userForm" property="lockoutDate"/>
                        <td colspan="2">&nbsp;
                    </tr>
                    </logic:equal>
                </table>
            </td>
        </tr>
        <tr>

            <%-- Form Buttons --%>

            <td align="left">
                <html:submit styleClass="button"/>
            </td>
            <td align="right">
                <logic:equal name="userForm" property="lockedOut" value="true">
                    <html:submit property="unlock" value="Unlock" styleClass="button"/>
                </logic:equal>
                <html:submit property="selectRoles" value="Select Roles" styleClass="button" tabindex="9"/>
                <html:submit property="selectAffiliates" value="Select Affiliates" styleClass="button" tabindex="10"/>
                <html:reset styleClass="button" tabindex="11"/>
                <html:cancel styleClass="button" tabindex="12"/>
            </td>
        </tr>
    </table>

</html:form>

<%-- Footer --%>
<%@ include file="../include/footer.inc" %>

