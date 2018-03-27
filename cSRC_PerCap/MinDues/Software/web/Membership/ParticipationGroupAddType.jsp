<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Participation Group Add Type", help = "ParticipationGroupAddType.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="participationCodeForm" type="org.afscme.enterprise.participationgroups.web.ParticipationCodeForm"/>

<html:form action="saveParticipationGroupType" focus="name">

    <html:hidden property="groupPk"/>
    <html:hidden property="pk"/>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            Group - <%=form.getGroupNm()%><BR>
        </td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr valign="top">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <td class="ContentHeaderTD">* Name</td>
                    <td class="ContentTD">
                        <html:text name="form" property="name" size="20" maxlength="20"/><br>
                        <html:errors property="name"/>
                    </td>
                    <td class="ContentHeaderTD">* Description</td>
                    <td>
                        <html:text name="form" property="description" size="100" maxlength="100"/><br>
                        <html:errors property="description"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<table  width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr valign="top">
        <td><BR><html:submit styleClass="button"/></td>
        <td align="right"><BR>
            <html:reset styleClass="button"/>
            <html:cancel styleClass="button"/>
        </td>
    </tr>
    <tr>
        <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
    </tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %>
