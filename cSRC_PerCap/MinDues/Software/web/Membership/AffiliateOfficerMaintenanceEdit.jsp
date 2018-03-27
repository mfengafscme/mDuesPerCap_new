<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Affiliate Officer Maintenance - Edit", help = "AffiliateOfficerMaintenanceEdit.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="affiliateOfficerMaintenanceForm" type="org.afscme.enterprise.affiliate.officer.web.AffiliateOfficerMaintenanceForm" />
<html:form action="/saveAffiliateOfficerMaintenance" >
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
    </TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>
			<html:submit styleClass="button"/>
			<BR>
			<BR>
		</TD>
		<TD align="right">
			<BR>
			<input type="button" name="blah" value="Reset" onclick="resetEntireForm(this.form.iter.value);" class="button">
			<afscme:button action="/viewAffiliateOfficerMaintenance.action">Cancel</afscme:button>
		</TD>
	</TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<TABLE id="titlesTable" cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD colspan="20" class="ContentHeaderTR">
			<html:hidden name="form" property="type" />			
			Officers		
		</TD>
	</TR>
	<TR>
		<TH colspan="4">Select</TH>
		<TH width="20%">Officer Title</TH>
		<TH width="8%">End Term</TH>
		<!--TH width="3%">Susp</TH>
		<TH width="2%">T</TH-->
		<TH colspan="4">Name</TH>
		<!--TH width="2%">RO</TH-->
		<TH width="3%">Stwd</TH>
		<!--TH width="4%">E-Bd</TH-->
	</TR>
	<TR>
		<TH CLASS="small">&nbsp;</TH>
		<TH class="small">Renew</TH>
		<TH class="small">Vacate</TH>
		<TH class="small">Replace</TH>
		<TD colspan="2">&nbsp;</TD>
		<TH class="small">First</TH>
		<TH class="small">Middle</TH>
		<TH class="small">Last</TH>
		<TH class="small">Suffix</TH>
		<TD>&nbsp;</TD>
	</TR>
    <%  int i = 2;  
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1;
        String onclick = null;
    %>
    
<logic:present name="form" property="officerList">    
    <logic:iterate id="element" name="form" property="officerList" type="java.util.Map.Entry">    
        <bean:define id="fieldPk" name="element" property="key" type="java.lang.Integer"/>
	<bean:define id="officerData" name="element" property="value" type="org.afscme.enterprise.affiliate.officer.AffiliateOfficerMaintenance"/>        
        <% 
           i++;   
           String valueObjectGetString1 = "officerData(" + fieldPk + ").officerTitle";
	   String valueObjectGetString2 = "officerData(" + fieldPk + ").monthOfElection";        
           String valueObjectGetString3 = "officerData(" + fieldPk + ").endTerm";
           String valueObjectGetString6 = "officerData(" + fieldPk + ").firstName";
           String valueObjectGetString7 = "officerData(" + fieldPk + ").middleName";
           String valueObjectGetString8 = "officerData(" + fieldPk + ").lastName";
           String valueObjectGetString9 = "officerData(" + fieldPk + ").suffix";
           String valueObjectGetString11 = "officerData(" + fieldPk + ").steward";         
           String valueObjectGetString19 = "officerData(" + fieldPk + ").lengthOfTerm";
           String valueObjectGetString20 = "officerData(" + fieldPk + ").officerPersonPk";
           String valueObjectGetString21 = "officerData(" + fieldPk + ").officerAction";    
           String valueObjectGetString22 = "officerData(" + fieldPk + ").expirationYear";     
           String valueObjectGetString23 = "officerData(" + fieldPk + ").expirationMonth";
           String valueObjectGetString25 = "officerData(" + fieldPk + ").replaceAffPk"; 
           String valueObjectGetString26 = "officerData(" + fieldPk + ").replacePersonPk";
	     String valueObjectGetString27 = "officerData(" + fieldPk + ").electedOfficerFg";           
        %>	
        
	<TR align="center">
		<TD>
		    <% 
		        int newEndTerm = 0;
		        if (
			    (
			     (officerData.getExpirationYear() != null) && 
			     (
			      (officerData.getExpirationYear().intValue() < year) || 
			      (officerData.getExpirationYear().intValue() == year && 
			       officerData.getExpirationMonth() != null && 
			       officerData.getExpirationMonth().intValue() <= month)
			     )
			    ) || 
			    (
			     (officerData.getOfficerPersonPk() == null || 
			      officerData.getOfficerPersonPk().intValue() < 1) && 
			     (
			      (officerData.getOriginalEndTerm() != null) &&
			      (
			       (officerData.getOriginalEndTerm().intValue() < year) || 
			       (officerData.getOriginalEndTerm().intValue() == year && 
			        officerData.getMonthOfElection().intValue() - 78000 <= month)
			      )
			     )
			    )
			   ) {
		    	    newEndTerm = officerData.getOriginalEndTerm().intValue() + officerData.getLengthOfTerm().intValue(); 
		    	} else {
		    	    newEndTerm = officerData.getOriginalEndTerm().intValue();
		    	}	
		    	
		    %>
		    
		    <input type="hidden" name="newEnd" value="<%=newEndTerm%>" />		    
		    
		    <% if ((officerData.getOfficerPersonPk() == null) || (officerData.getOfficerPersonPk().intValue() < 1)) {  %>
			   <input type="hidden" name="currentEnd" value="<%=officerData.getOriginalEndTerm().intValue()%>" />		       
		    <% } else { 
				if (officerData.getOriginalExpirationYear() != null) { %>
				       <input type="hidden" name="currentEnd" value="<%=officerData.getOriginalExpirationYear().intValue()%>" /> 
		    <%      } else { %>	
			             <input type="hidden" name="currentEnd" value="<%=officerData.getOriginalEndTerm().intValue()%>" />		       
		    <%      } 
                   } %>	
		    		    		   
		    <html:hidden property='<%=valueObjectGetString20%>' />
		    <A HREF="javascript:rowReset(<%=i%>, 0);" class="actionSMALL">Reset</A>
		</TD>
		<TD>
		    <% if (officerData.getLengthOfTerm().intValue() == 0) {  
		    	onclick = "rowReset(" + i + ", 1); setRowDisabled(" + i +");";
		    	%>
		        <html:radio property='<%=valueObjectGetString21%>' value="r" onclick='<%=onclick%>' />
		    <% } else  {
		    	    if ((officerData.getOfficerPersonPk() == null) || (officerData.getOfficerPersonPk().intValue() < 1)) {		               		    	       		    	           
			    %>		    
			        <html:radio property='<%=valueObjectGetString21%>' value="r" disabled="true"  />		    
			    <%		               
		            } else {
		                if (officerData.getExpirationYear() != null && officerData.getExpirationYear().intValue() <= year) {		    	        
		    	            onclick = "rowReset(" + i + ", 1); titlesTable.rows(" + i + ").cells(5).childNodes(1).value=" + newEndTerm + "; setRowDisabled(" + i + ");";
		    	        %>		    
		                    <html:radio property='<%=valueObjectGetString21%>' value="r" onclick='<%=onclick%>' />		    
		            <%  } else { %>
		                    <html:radio property='<%=valueObjectGetString21%>' value="r" disabled="true" />
		                <%    
		                }
		            }
		       } %>
		</TD>
		<TD>
		    <% if ((officerData.getOfficerPersonPk() == null) || (officerData.getOfficerPersonPk().intValue() < 1)) { %>
		    	<html:radio property='<%=valueObjectGetString21%>' value="v" disabled="true" />
		    <% } else { 
		    	onclick = "rowReset(" + i + ",1);vacate(" + i + ");setRowDisabled(" + i + ");";
		    	%>
		        <html:radio property='<%=valueObjectGetString21%>' value="v" onclick='<%=onclick%>' />
		    <% } %>
		</TD>
		<TD>
		    <% onclick = "rowReset(" + i + ",1); replace(" + i +");" + "titlesTable.rows(" + i + ").cells(5).childNodes(1).value=" + newEndTerm + ";"; %>
		    <html:radio property='<%=valueObjectGetString21%>' value="p" onclick='<%=onclick%>' />
		</TD>
		<TD>
		    <bean:write name="form" property='<%=valueObjectGetString1%>' />
		</TD>
		<TD nowrap>
		    <%  boolean disabled = true;
		        if ("p".equals(officerData.getOfficerAction()))
		            disabled = false;		    		   
		        String onchange = "checkForm(" + i + ");";
		    	if (officerData.getExpirationYear() != null) {
		    	    if (officerData.getExpirationMonth().intValue() < 10) { %>	 	    	
				0<bean:write name="form" property='<%=valueObjectGetString23%>' />/<html:text name="form" property='<%=valueObjectGetString22%>' disabled='<%=disabled%>' maxlength="4" size="6" onblur='<%=onchange%>'/>		    	    
		            <% } else { %>
				<bean:write name="form" property='<%=valueObjectGetString23%>' />/<html:text name="form" property='<%=valueObjectGetString22%>' disabled='<%=disabled%>' maxlength="4" size="6" onblur='<%=onchange%>'/>		            
		            <% }		            
	 	        } else { 
	 	           if (officerData.getMonthOfElection().intValue() < 78010) {  %>
	 	                0<afscme:codeWrite name="form" property='<%=valueObjectGetString2%>' codeType="MonthOffcrElection" format="{0}"/>/<html:text name="form" property='<%=valueObjectGetString3%>' disabled='<%=disabled%>' maxlength="4" size="6" onblur='<%=onchange%>'/>
	 	           <% } else {  %>	 	    	 	  
			    	<afscme:codeWrite name="form" property='<%=valueObjectGetString2%>' codeType="MonthOffcrElection" format="{0}"/>/<html:text name="form" property='<%=valueObjectGetString3%>' disabled='<%=disabled%>' maxlength="4" size="6" onblur='<%=onchange%>'/>
			   <% }
		        } %>
		</TD>
		<TD align="center">
		    <% onchange = "clearOfficerHiddenFields(" + i + ");"; %>
		    <html:text name="form" property='<%=valueObjectGetString6%>' size="20" maxlength="25" disabled='<%=disabled%>' onchange='<%=onchange%>'/>		
		    <html:hidden property='<%=valueObjectGetString25%>' />
		    <html:hidden property='<%=valueObjectGetString26%>' />
		    <html:hidden property='<%=valueObjectGetString27%>' />
		</TD>
		<TD align="center">
		    <html:text name="form" property='<%=valueObjectGetString7%>' size="20" maxlength="25" disabled='<%=disabled%>' onchange='<%=onchange%>'/>
		</TD>
		<TD align="center">
		    <html:text name="form" property='<%=valueObjectGetString8%>' size="20" maxlength="25" disabled='<%=disabled%>' onchange='<%=onchange%>'/>
		</TD>
		<TD align="center">
		    <html:select property='<%=valueObjectGetString9%>' disabled='<%=disabled%>' onchange='<%=onchange%>'>
			<afscme:codeOptions codeType="Suffix" format="{1}" allowNull="true" nullDisplay="[Select]"/>
		    </html:select>		        
		</TD>
		<TD>		  
		    <% if (
		           (form.getType() != null && 
		            (!form.getType().equals(new Character('L')) && 
		            !form.getType().equals(new Character('U'))
		            )
		           ) ||
		           (officerData.getOfficerPersonPk() == null) ||
		           (officerData.getOfficerPersonPk().intValue() < 1) &&
		           !"p".equals(officerData.getOfficerAction())
		          ) { 	%>	        		         
		         <html:checkbox name="form" property='<%=valueObjectGetString11%>' disabled="true" />
			 <input type="hidden" name="dis" value="y" />		    	
		    <% } else { %>		        
		        <html:checkbox name="form" property='<%=valueObjectGetString11%>' disabled="false" />
			<input type="hidden" name="dis" value="n" />		        
		   <% } %>
		</TD>
	</TR>
        <TR>
            <td colspan="4">
            </td>
            <TD colspan="7">            
                <html:errors property='<%=valueObjectGetString20%>'/> <html:errors property='<%=valueObjectGetString21%>'/> <html:errors property='<%=valueObjectGetString22%>'/> <html:errors property='<%=valueObjectGetString3%>'/> <html:errors property='<%=valueObjectGetString6%>'/> <html:errors property='<%=valueObjectGetString7%>'/> <html:errors property='<%=valueObjectGetString8%>'/>
		<% i++; %>                
            </TD>
        </TR>	
    </logic:iterate>
</logic:present>



</TABLE>
<input type="hidden" name="iter" value="<%=i%>">
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR>			
			<html:submit styleClass="button"/>
			<BR>
			<BR>
		</TD>
		<TD align="right">
			<BR>
			<input type="button" name="blah" value="Reset" onclick="resetEntireForm(this.form.iter.value);" class="button">
			<afscme:button action="/viewAffiliateOfficerMaintenance.action">Cancel</afscme:button>
		</TD>
	</TR>
</TABLE>
</html:form>
<%@ include file="../include/footer.inc" %>