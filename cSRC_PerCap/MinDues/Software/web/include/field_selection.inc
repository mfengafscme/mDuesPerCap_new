  <table width="95%" align="center">
    <%-- For each entity --%>
    <logic:iterate id="entities" name="form" property="userFields" type="java.util.Map.Entry">
    <bean:define id="entity" name="entities" property="key" type="java.lang.Character"/>
    <tr>
      <td colspan='2' class="entityHeader">
        <%=org.afscme.enterprise.reporting.base.BRUtil.getEntityName(entity)%>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <table class="BodyContentNoWidth" border="1" width="100%" cellspacing="0">
          <tr>
            <%-- For each category name --%>
            <logic:iterate id="categoriesNames" name="entities" property="value" type="java.util.Map.Entry">
            <bean:define id="category1" name="categoriesNames" property="key" type="java.lang.String"/>
            <th align="left"><a class="TH" href="javascript:toggleCheckBoxes('<%=formName%>', '<%=entity+category1%>')"><%=category1%></a></th>
            </logic:iterate>
          </tr>
          <tr>
            <%-- For each category --%>
            <logic:iterate id="categories" name="entities" property="value" type="java.util.Map.Entry">
            <bean:define id="category2" name="categories" property="key" type="java.lang.String"/>
            <bean:define id="fields" name="categories" property="value" type="java.util.Collection"/>
            <td valign="top" align="left">
              <table>
                <%-- For each field in a category --%>
                <logic:iterate id="field" name="fields" type="org.afscme.enterprise.reporting.base.access.ReportField">
                <bean:define id="pk" name="field" property="pk" type="java.lang.Integer"/>   
                <% if (field.isAccessible()) { %>
                <tr>
                  <td><html:multibox styleId="<%=entity+category2+pk%>" name="form" property="<%=fieldsProperty%>" value='<%=pk.toString()%>'/></td>
                  <td><label for="<%=entity+category2+pk%>"><bean:write name="field" property="displayName"/></label></td>
                </tr>
                <% } %>
                
                  <%-- For each children of this field --%>
                  <% if (field.hasChildren()) { %>
                  <logic:iterate id="child" name="field" property="children" type="org.afscme.enterprise.reporting.base.access.ReportField">
                  <bean:define id="childPk" name="child" property="pk" type="java.lang.Integer"/>
                  <% if (child.isAccessible()) { %>
                  <tr>
                    <td><html:multibox styleId='<%=entity+category2+childPk%>' name="form" property="<%=fieldsProperty%>" value='<%=childPk.toString()%>'/></td>
                    <td><label for="<%=entity+category2+childPk%>">&nbsp;&nbsp;&nbsp;<i><bean:write name="child" property="displayName"/></i></label></td>
                  </tr>
                  <% } %>
                  </logic:iterate>
                  <% } %>
                  
                </logic:iterate>
              </table>
            </td>
            </logic:iterate>
          </tr>
        </table>
        <br>
      </td>
    </tr>
    </logic:iterate>
  </table>       
