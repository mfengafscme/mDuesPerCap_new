<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Select Output Format", help = "DownloadOutputFormat.html";%>
<%@ include file="../../include/header.inc" %>

<bean:define id="form" name="specializedReportForm" type="org.afscme.enterprise.reporting.specialized.web.SpecializedReportForm"/>

<html:form action="/startSpecializedReportGeneration">
<html:hidden property="affPk"/> 
<html:hidden property="reportName"/>
<TABLE width="60%" align="center" border=3 class="BodyContentNoWidth">
	<TR>
		<TH class="large" align="left">
			Save File in one of the following formats: 
		</TH>
	</TR>
	<tr>
     		<td>
     			<TABLE border="0" class="InnerContentTable">
          			<tr>
            			<td>&nbsp;&nbsp;
           	  				<html:radio name="form" property="outputFormat" value="Tab" styleId="optionTab"/>
           				</td>
		           		<td>&nbsp;&nbsp;
      	        			<LABEL for="optionTab">Tab Delimited</LABEL>
            			</td>
          			</tr>
	      		<tr>
      	    			<td>&nbsp;&nbsp;
             				<html:radio name="form" property="outputFormat" value="Comma" styleId="optionComma"/>
	           			</td>
      	     			<td>&nbsp;&nbsp;
	              			<LABEL for="optionComma">Comma Delimited</LABEL>
      	     			</td>
          			</tr>
	      		<tr>
		           		<td>&nbsp;&nbsp;
      	        			<html:radio name="form" property="outputFormat" value="Semicolon" styleId="optionSemicolon"/>
            			</td>
            			<td>&nbsp;&nbsp;
            				<LABEL for="optionSemicolon">Semicolon Delimited</LABEL>
		           		</td>
      	    		</tr>
        		</TABLE>
	      </td>
	</tr>
</TABLE>
<TABLE class="ContentTableNoWidth" width="60%" align="center">
<TR>
	<TD colspan="2">&nbsp;</TD>
</TR>
<TR>
	<td align="left">
		<html:submit value="Generate Report" styleClass="button"/>
	</td>
	<td align="right">
		<html:cancel styleClass="BUTTON"/> 
	</td>
</TR>
</TABLE>
</html:form>
<%@ include file="../../include/footer.inc" %> 