<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@ page import="java.util.*" %>
<%@ page import="org.afscme.enterprise.reporting.base.access.*" %>
<%@ page import="org.afscme.enterprise.reporting.base.*" %>

<%! String title = "Selection Criteria", help = "SelectionCriteria.html";%>
<%@ include file="../include/header.inc" %> 

<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>

<script language=javascript>
  function submitForm(linkName) {
    if (atLeastOneChecked()) {
      document.getElementById("linkClicked").setAttribute("value", linkName);
      document.forms[0].submit();    
    }
  }
  
  function atLeastOneChecked() {
        var pass = true;
        var flag1 = false;
        var ml = document.queryForm;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++) 
        {
            var e = ml.elements[i];         
   	    if (e.type != "hidden" && e.type != "submit" && e.type != "select-one" &&  e.name != "editableFields" && e.name != "toggleAll") {
   	    //  alert ('Inside if ' + e.value + " || " + e.type + " || " + e.name);   	  
   	        if (!flag1) {
   	            pass = false;
   	            flag1 = true;
   	        }        	     		
   	        if (e.type == "checkbox") {   	        
	  	    if (e.checked == true) {
		        pass = true;
		        break;
		    }      		  
   	        } else {
   	      	    if (e.value != "") {
   	      	        pass=true;
   	                break;
   	            }
   	        }
   	    }
        }        
        if (pass == false) {
            alert('You must make at least one selection below to proceed.');
        }
         
        return (pass);
  }
  
  
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

  function restrict() {
  	alert("This section does not apply to Count Queries.");
  	return;
  }

    function ToggleAll(e)
    {
		if (e.checked) {
		    CheckAll();
		}
		else {
		    ClearAll();
		}
    }

    function CheckAll()
    {
        var ml = document.queryForm;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++) 
        {
            var e = ml.elements[i];
            if (e.name == "editableFields") 
            {
                e.checked = true;
            }
        }
    }
    function ClearAll()
    {
	var ml = document.queryForm;
	var len = ml.elements.length;
	for (var i = 0; i < len; i++) {
	    var e = ml.elements[i];
	    if (e.name == "editableFields") {
		e.checked = false;		
	    }
	}
	
    }

    function ClearToggleAll(ele)
    {
                    document.queryForm.toggleAll.checked = false;
    }
</script>

<html:form action="/selectionCriteria">
  <bean:define id="form" name="queryForm" scope="session" type="org.afscme.enterprise.reporting.base.web.QueryForm"/>
  
  <bean:define id="queryFields" name="queryForm" property="queryFields" type="java.util.Map"/>
  <bean:define id="children" name="queryForm" property="children" type="java.util.Map"/>

    <% String pageName = "Selection Criteria";%>
    <%@ include file="../include/query_wizard.inc" %>
  
  <!-- construct a map of selection names -->
  <table class="BodyContentNoWidth" border="1" cellspacing="0" width="95%" align="center">
    <tr>
      <th nowrap><input type=checkbox name=toggleAll title="Select or deselect all" onclick="ToggleAll(this);">Editable</th>
      <th nowrap>Field</th>
      <th nowrap>Criteria</th>
    </tr>
    <%-- criteria has been defined --%>
    <logic:present name="form" property="criteria">
      <logic:iterate id="element" name="form" property="criteria" type="java.util.Map.Entry">
      <bean:define id="fieldPk" name="element" property="key" type="java.lang.Integer"/>
      <% ReportField reportField;
        if (queryFields.containsKey(fieldPk))
            reportField = (ReportField)queryFields.get(fieldPk);
        else
            reportField = (ReportField)children.get(fieldPk);
      %>               
      <tr>
        <td>
          <html:multibox styleId="<%=fieldPk.toString()%>" name="form" property="editableFields" value='<%=fieldPk.toString()%>' onclick="ClearToggleAll(this)"/>
        </td>
        <td nowrap>
          <label for='<%=fieldPk%>'><%=reportField.getFullName()%></label>
        </td>
        <td>
          <bean:define id="criterionMap" name="element" property="value" type="org.afscme.enterprise.reporting.base.access.CriterionMap"/> 
                 
          <% if (reportField.getDisplayType() == 'S') { 
               String p_s = "criterionMap(" + fieldPk + ").criterion(c0).value1"; %>
              <html:text property='<%=p_s%>' size="30"/>
          <% } else if (reportField.getDisplayType() == 'B') { 
              String p_b = "criterionMap(" + fieldPk + ").criterion(c0).value1"; %>
              <html:select property='<%=p_b%>'>
                <html:option value=""></html:option>
                <html:option value="True">True</html:option>
                <html:option value="False">False</html:option>
                </html:select>
          <% } else if ((reportField.getDisplayType() == 'I') || (reportField.getDisplayType() == 'D')) { %>
            <table>
              <%-- Traverse all criterion for this field --%>
              <% boolean firstLine = true; 
                 Set keys = criterionMap.getKeys();
                 Iterator it = keys.iterator();
                 while (it.hasNext()) {
                    String cKey = (String)it.next(); 
                    String pStr = "criterion(" + cKey + ")";
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
                  <a href="javascript:document.forms[0].action='/addCriterion.action?pk=<%=fieldPk%>';document.forms[0].submit()" class="action">OR...</a>
                  <% } else { %>
                  <a href="javascript:document.forms[0].action='/removeCriterion.action?pk=<%=fieldPk%>&cKey=<%=cKey%>';document.forms[0].submit()" class="action">Remove</a>
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
  
</html:form>

<%@ include file="../include/footer.inc" %> 
