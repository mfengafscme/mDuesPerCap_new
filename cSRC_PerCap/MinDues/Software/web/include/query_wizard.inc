
<html:hidden property="pageName" value="<%=pageName%>"/>
<html:hidden property="linkClicked" value=""/>
  
<table width="95%" align="center" cellpadding="0" cellspacing="3" border="0">
    <tr>
      <td align=center>
        <h4>

<%
String [] pages = org.afscme.enterprise.reporting.base.web.QueryForm.WIZARD_PAGE_NAMES;
for (int i = 0; i < pages.length; i++) {
    if (pages[i].equals(pageName)) {
          %><span class="CurrentWizardPage"><%=pages[i]%></span> <%
    } else if(form.isCountQuery() && (pages[i].equals(org.afscme.enterprise.reporting.base.web.QueryForm.PAGE_OUTPUT_FIELDS_ORDER)
    	      || pages[i].equals(org.afscme.enterprise.reporting.base.web.QueryForm.PAGE_SORT_CRITERIA_FIELDS)
    	      || pages[i].equals(org.afscme.enterprise.reporting.base.web.QueryForm.PAGE_SORT_CRITERIA))) {           
    	  %><a href="javascript:restrict()" name="<%=pages[i]%>"><%=pages[i]%></a> <%
    } else {
          %><a href="javascript:submitForm('<%=pages[i]%>')" name="<%=pages[i]%>"><%=pages[i]%></a> <%
    }
    if (i < (pages.length-1)) {
        %> >> <%
    }
}
%>
        </h4>
      </td>
    </tr>
    <tr> 
        <td align="right"><BR>
        <% if (pageName.equals("Preview Query")) { %>
            <html:submit property="saveButton" value="Save" styleClass="button"/>
            <% if (form.getPk() != null) { %> 
                <html:submit property="saveAsButton" value="Save As" styleClass="button"/>
            <% } else { %>
                <html:submit disabled="true" property="saveAsButton" value="Save As" styleClass="button"/>
            <% } %>        
        <% } %>        
        <html:cancel styleClass="button">Cancel</html:cancel>
      </td>
    </tr> 
    <tr>
      <td>
        <html:errors/>
      </td>
    </tr>       
  </table>
