<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@ page import="java.util.*" %>
<%@ page import="org.afscme.enterprise.reporting.base.access.*" %>
<%@ page import="org.afscme.enterprise.reporting.base.*" %>

<%! String title = "Generate Report Selection Criteria", help = "RegularRuntimeCriteria.html";%>
<%@ include file="../include/header.inc" %> 

<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>
<script language=javascript>
  function showValuesForOp(theValue, span1, span2) {
    if (theValue == 'NL')
        span1.style.visibility='hidden';
    else if (theValue == 'NN')
        span1.style.visibility='hidden';
    else
        span1.style.visibility='visible';

    if (theValue == 'BT')
        span2.style.visibility='visible';
    else
        span2.style.visibility='hidden';
}
</script>

<html:form action="/regularGenerate">

  <bean:define id="form" name="regularReportForm" scope="session" type="org.afscme.enterprise.reporting.base.web.RegularReportForm"/>
  <html:hidden property="pageName" value="Runtime Criteria"/>
  
  <bean:define id="queryFields" name="regularReportForm" property="queryFields" type="java.util.Map"/>
  <bean:define id="children" name="regularReportForm" property="children" type="java.util.Map"/>

  <table class="BodyContentNoWidth" border="1" cellspacing="0" width="95%" align="center">
    <tr>
      <th nowrap align="left">Field</th>
      <th nowrap align="left">Criteria</th>
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
          <label for='<%=fieldPkStr%>'><%=reportField.getFullName()%></label>
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
              <% boolean firstLine = true; 
                 String cKey;
                 String pStr;
                 java.util.Set keys = criterionMap.getKeys();
                 java.util.Iterator it = keys.iterator();
                 while (it.hasNext()) {
                    cKey = (String)it.next(); 
                    pStr = "criterion(" + cKey + ")";
              %>
              <bean:define id="criterion_i" name="criterionMap" property='<%=pStr%>' type="org.afscme.enterprise.reporting.base.access.ReportCriterionData"/>
             
              <%  String op = criterion_i.getOperator(); %>
              <tr>
                <td>
                  <% String opProperty = "criterionMap(" + fieldPk + ").criterion(" + cKey + ").operator"; %>
                  <% String value1Property = "criterionMap(" + fieldPk + ").criterion(" + cKey + ").value1"; %>
                  <% String value1Id = cKey+"_"+fieldPk+"_1"; %>
                  <% String value2Property = "criterionMap(" + fieldPk + ").criterion(" + cKey + ").value2"; %>
                  <% String value2Id = cKey+"_"+fieldPk+"_2"; %>
                  <% String setVisibility = "showValuesForOp(this.value, span_"+value1Id+", span_"+value2Id+");"; %>
                  <bean:define id="theOp" name="form" property="<%=opProperty%>"/>
                  <html:select name="form" property='<%=opProperty%>' value="<%=op%>" onchange="<%=setVisibility%>">
                     <% String[] opNames = reportField.getDisplayType() == 'I' ? BRUtil.INT_OP_NAMES : BRUtil.DATE_OP_NAMES; %>
                      <% for (int i = 0; i < BRUtil.OP_CODES.length; i++) {
                            if (opNames[i] != null) {
                                %><html:option value="<%=BRUtil.OP_CODES[i]%>"><%=opNames[i]%></html:option><%
                            }
                        } %>
                  </html:select>
                </td>
                <td>
                  <% String visibility1 = (theOp.equals("NL") || theOp.equals("NN")) ? "hidden" : "visible";%>
                  <span id="span_<%=value1Id%>" style="visibility='<%=visibility1%>'">
                  <html:text property="<%=value1Property%>" styleId="<%=value1Id%>"/>
                  <% if (reportField.getDisplayType() == 'D') { %>
                  <% String showCalendar ="javascript:show_calendar('queryForm."+value1Id+"');"; %>
                    <A href="<%=showCalendar%>" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A>                  
                  <% } %>
                  <% String visibility2 = theOp.equals("BT") ? "visible" : "hidden";%>
                  </span>
                  <span id="span_<%=value2Id%>" style="visibility='<%=visibility2%>'">
                  AND
                  <html:text property="<%=value2Property%>" styleId="<%=value2Id%>"/>
                  <% if (reportField.getDisplayType() == 'D') { %>
                  <% String showCalendar ="javascript:show_calendar('queryForm."+value2Id+"');"; %>
                    <A href="<%=showCalendar%>" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A>
                  <% } %>
                  </span>
                </td>
                <td> 
                  <% if (firstLine) { %>
                  <a href="javascript:document.forms[0].action='/addRegularRuntimeCriterion.action?pk=<%=fieldPk%>';document.forms[0].submit()" class="action">OR...</a>
                  <% } else { %>
                  <a href="javascript:document.forms[0].action='/removeRegularRuntimeCriterion.action?pk=<%=fieldPk%>&cKey=<%=cKey%>';document.forms[0].submit()" class="action">Remove</a>
                  <% } 
                    firstLine = false;
                  %> 
                </td>
              </tr>
              <% }  // end of "while" loop %>
            </table>
          <% } else if (reportField.getDisplayType() == 'C') { %>
            <table>
            <% String p_c = "criterionMap(" + fieldPk + ").selectedCodes"; %>
            <% int codeCount = 0; %>
              <tr>
                <logic:iterate id="allCodes" name="criterionMap" property="allCodes" type="java.util.Map.Entry">
                    <bean:define id="codeData" name="allCodes" property="value" type="org.afscme.enterprise.codes.CodeData"/>
                      <td nowrap>
                            <html:multibox styleId='<%=codeData.getPk().toString()%>' property="<%=p_c%>" value='<%=codeData.getPk().toString()%>'/>
                            <label for='<%=codeData.getPk().toString()%>'><small><%=codeData.getDescription()%></small></label>
                      </td>
                       <% if ((++codeCount % 5) == 0) { %></tr><tr><%}%>
                </logic:iterate>
              </td>
              </tr>
            </table>
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
