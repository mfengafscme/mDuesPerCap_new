<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@ page import="java.util.*" %>
<%@ page import="org.afscme.enterprise.reporting.base.access.*" %>

<%! String title = "Mailing List Selection Criteria", help = "MailingRuntimeCriteria.html";%>
<%@ include file="../include/header.inc" %> 

<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>

<html:form action="/mailingGenerate">

  <bean:define id="form" name="mailingReportForm" scope="session" type="org.afscme.enterprise.reporting.base.web.MailingReportForm"/>
  <html:hidden property="pageName" value="Runtime Criteria"/>       
  <html:hidden property="filterDuplicateAddresses"/>
  
  <bean:define id="queryFields" name="mailingReportForm" property="queryFields" type="java.util.Map"/>
  <bean:define id="children" name="mailingReportForm" property="children" type="java.util.Map"/>

  <!-- construct a map of selection names -->
  <jsp:useBean id="selectionName" scope="page" class="java.util.HashMap"/> 
  <% 
     selectionName.put(new java.lang.String("I_EQ"), "equal to");
     selectionName.put(new java.lang.String("I_LT"), "less than");
     selectionName.put(new java.lang.String("I_GT"), "greater than");
     selectionName.put(new java.lang.String("I_LE"), "less than OR equal to");
     selectionName.put(new java.lang.String("I_GE"), "greater than OR equal to");
     selectionName.put(new java.lang.String("I_BT"), "between");
     selectionName.put(new java.lang.String("D_EQ"), "on");
     selectionName.put(new java.lang.String("D_LT"), "before");
     selectionName.put(new java.lang.String("D_GT"), "after");
     selectionName.put(new java.lang.String("D_LE"), "on OR before");
     selectionName.put(new java.lang.String("D_GE"), "on OR after");
     selectionName.put(new java.lang.String("D_BT"), "between");                    
  %>
       
  <table class="BodyContentNoWidth" border="1" cellspacing="0" width="95%" align="center">
    <tr>
      <th nowrap>Field</th>
      <th nowrap>Criteria</th>
    </tr>
    <%-- criteria has been defined --%>
    <logic:present name="form" property="runtimeCriteria">
      <logic:iterate id="element" name="form" property="runtimeCriteria" type="java.util.Map.Entry">
      <bean:define id="fieldPkStr" name="element" property="key" type="java.lang.String"/>
      <% ReportField reportField;
         Integer fieldPk = new Integer(fieldPkStr);
        if (queryFields.containsKey(fieldPk))
            reportField = (ReportField)queryFields.get(fieldPk);
        else
            reportField = (ReportField)children.get(fieldPk);
      %>               
      <tr>
        <td>
          <label for='<%=fieldPkStr%>'><%=reportField.getDisplayName()%></label>
        </td>
        <td>
          <bean:define id="criterionMap" name="element" property="value" type="org.afscme.enterprise.reporting.base.access.CriterionMap"/> 
                 
          <%-- String field, only one value is allowed --%>
          <% if (reportField.getDisplayType() == 'S') { 
               String p_s = "criterionMap(" + fieldPkStr + ").criterion(c0).value1"; %>
              <html:text property='<%=p_s%>' size="30"/>
          <%-- Integer or Date field, can have multiple criterion --%>
          <% } else if ((reportField.getDisplayType() == 'I') || (reportField.getDisplayType() == 'D')) { %>
            <table>
              <%-- Traverse all criterion for this integer field --%>
              <% boolean firstLine_i = true; 
                 String cKey;
                 String pStr;
                 java.util.Set keys = criterionMap.getKeys();
                 java.util.Iterator it = keys.iterator();
                 while (it.hasNext()) {
                    cKey = (String)it.next(); 
                    pStr = "criterion(" + cKey + ")";
              %>
              <bean:define id="criterion_i" name="criterionMap" property='<%=pStr%>' type="org.afscme.enterprise.reporting.base.access.ReportCriterionData"/>
             
              <%  String op = criterion_i.getOperator();
                  
                  String prefix;
                  if (reportField.getDisplayType() == 'I')
                    prefix = "I_";
                  else
                    prefix = "D_";
              %>
              <tr>
                <td>
                  <% String pOp = "criterionMap(" + fieldPkStr + ").criterion(" + cKey + ").operatorString"; %>
                  <html:select name="form" property='<%=pOp%>' value="<%=op%>">
                    <html:option value="EQ"><%=(java.lang.String)selectionName.get(prefix+"EQ")%></html:option>
                    <html:option value="LT"><%=(java.lang.String)selectionName.get(prefix+"LT")%></html:option>
                    <html:option value="GT"><%=(java.lang.String)selectionName.get(prefix+"GT")%></html:option>
                    <html:option value="LE"><%=(java.lang.String)selectionName.get(prefix+"LE")%></html:option>
                    <html:option value="GE"><%=(java.lang.String)selectionName.get(prefix+"GE")%></html:option>
                    <html:option value="BT"><%=(java.lang.String)selectionName.get(prefix+"BT")%></html:option>
                  </html:select>
                </td>
                <td>
                  <% String p_v1 = "criterionMap(" + fieldPkStr + ").criterion(" + cKey + ").value1"; %>
                  <html:text property='<%=p_v1%>'/>
                  <% if (reportField.getDisplayType() == 'D') { %>
                    <A href="javascript:show_calendar('');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A>                  
                  <% } %>
                  AND
                  <% String p_v2 = "criterionMap(" + fieldPkStr + ").criterion(" + cKey + ").value2"; %>
                  <html:text property='<%=p_v2%>'/>
                  <% if (reportField.getDisplayType() == 'D') { %>
                    <A href="javascript:show_calendar('');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A>
                  <% } %>
                </td>
                <td> 
                  <% if (firstLine_i) { %>
                  <html:link page="/addCriterion.action" paramId="pk" paramName="fieldPkStr" styleClass="action">OR...</html:link>
                  <% } else { %>
                    <!-- construct a map of properties -->
                    <jsp:useBean id="propMap" scope="page" class="java.util.HashMap"/>    
                    <% 
                       propMap.put("pk", fieldPkStr); 
                       propMap.put("cKey", cKey);
                    %>
                  <html:link page="/removeCriterion.action" name="propMap" styleClass="action">Remove</html:link>
                  <% } 
                    firstLine_i = false;
                  %> 
                </td>
              </tr>
              <% }  // end of "while" loop %>
            </table>
          <% } else if (reportField.getDisplayType() == 'C') { %>
          <%-- TBD --%>
          <% } %>
        </td>
      </tr>  
      </logic:iterate>            
    </logic:present>
  </table>
  
  <table width="95%" align="center" valign=TOP cellpadding="0" cellspacing="3" border="0" class="ContentTable">  
    <tr> 
      <td align="right"><BR>
        <html:submit property="button" value="Generate Report" styleClass="button"/>&nbsp;&nbsp;
        <html:submit property="button" value="Cancel" styleClass="button"/>
      </td>
    </tr> 
    <tr>
      <td>
        <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
      </td>
    </tr>       
  </table>  
  
</html:form>    
    


<%@ include file="../include/footer.inc" %> 