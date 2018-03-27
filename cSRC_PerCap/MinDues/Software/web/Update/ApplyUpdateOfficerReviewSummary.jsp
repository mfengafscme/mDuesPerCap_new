<%! String title = "Apply Update Officer Review Summary", help = "ApplyUpdateOfficerReviewSummary.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@page import="java.util.*, org.afscme.enterprise.update.ExceptionData"%>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD align="center">
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>

<SCRIPT language="JavaScript" src="../js/header.js">
	</SCRIPT>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <SCRIPT language="JavaScript" src="../js/menu_help_row.js"></SCRIPT>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD>
            <BR> <afscme:button page="/viewApplyUpdateQueue.action">Return</afscme:button>
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0.5" class="BodyContent" align="center">
<TR><TD>
<!--**************************************************************************//-->
<TABLE cellpadding="0" cellspacing="0"  class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR" valign="top">
            <afscme:currentAffiliate/><br>  

            Update Type - <afscme:codeWrite name="updateType" codeType="UpdateType"    format="{1}"/>
            <BR><BR>
        </TD>
    </TR>
</TABLE>
<!--**************************************************************//-->
</TD></TR>
<TR><TD>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TH colspan="20" align="left">
				Processing Statistics 
			</TH>
		</TR>
		<TR>
			<TH colspan="5">
				Affiliates in File 
			</TH>
			<TH colspan="3">
				Transactions 
			</TH>
			<TH colspan="2">
				Replace 
			</TH>
			<TH colspan="2">
				Change 
			</TH>
			<TH colspan="2">
				Add
			</TH>
			<TH colspan="2">
				Vacant
			</TH>
		</TR>
		<TR>
			<TH class="small">
				Type 
			</TH>
			<TH class="small">
				Local/Sub Chap 
			</TH>
			<TH class="small">
				State/National Type
			</TH>
			<TH class="small">
				Sub Unit 
			</TH>
			<TH class="small">
				Council/Retiree Chapter
			</TH>
			<TH class="small">
				Attempted 
			</TH>
			<TH class="small">
				Completed 
			</TH>
			<TH class="small">
				In Error 
			</TH>
			<TH class="small">
				Attempted 
			</TH>
			<TH class="small">
				Completed 
			</TH>
			<TH class="small">
				Attempted 
			</TH>
			<TH class="small">
				Completed 
			</TH>
			<TH class="small">
				Attempted 
			</TH>
			<TH class="small">
				Completed 
			</TH>
			<TH class="small">
				Attempted 
			</TH>
			<TH class="small">
				Completed 
			</TH>
		</TR>
                <bean:define id="uRSmry" name="updateSmry"/>
                
                <logic:iterate id="smry" name="uRSmry">
                    <logic:present name="smry"  >
                        <TR>
                            <!--Write the affiliates details and create the column      //-->
                            <afscme:affiliateIdWrite name="smry" property="affId" />
                            <!--****************************************************************//-->
                            <TD align="middle">
                                <bean:write name="smry" property="transAttempted"     ignore="true"/>
                            </TD>
                            <TD align="middle">
                                <bean:write name="smry" property="transCompleted"     ignore="true"/>
                            </TD>
                            <TD align="middle">
                                <bean:write name="smry" property="transError"         ignore="true"/>
                            </TD>
                            <TD align="middle">
                                <bean:write name="smry" property="inacAttempted"      ignore="true"/>
                            </TD>
                            <TD align="middle">
                                <bean:write name="smry" property="inacCompleted"      ignore="true"/>
                            </TD>
                            <TD align="middle">
                                <bean:write name="smry" property="changesAttempted"   ignore="true"/>
                            </TD>
                            <TD align="middle">
                                <bean:write name="smry" property="changesCompleted"   ignore="true" />
                            </TD>
                            <TD align="middle">
                                <bean:write name="smry" property="addsAttempted"      ignore="true"/>
                            </TD>
                            <TD align="middle">
                                <bean:write name="smry" property="addsCompleted"      ignore="true"/>
                            </TD>
                            <TD class="ContentHeaderTD" align="center">
                                <bean:write name="smry" property="vacantAttempted"    ignore="true"/>

                            </TD>
                            <TD class="ContentHeaderTD" align="center">
                                <bean:write name="smry" property="vacantCompleted"    ignore="true"/>

                            </TD>
                        </TR>
                     </logic:present>
            </logic:iterate>
            <!--*************************************************************-->
            <bean:define id="tCounts" name="tCount"/>
            <!--********************************************************************************************************//-->
            <TR>

                <TD colspan="5" class="ContentHeaderTD" align="right">
                    Totals: 
                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="transAttempted"    ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="transCompleted"    ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="transError"        ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="inacAttempted"     ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="inacCompleted"     ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="changesAttempted"  ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="changesCompleted"  ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="addsAttempted"     ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="addsCompleted"     ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="vacantAttempted"  ignore="true"/>

                </TD>
                <TD class="ContentHeaderTD" align="center">
                     <bean:write name="tCount" property="vacantCompleted"  ignore="true"/>

                </TD>

            </TR>    
</TABLE>
</TD></TR>
<TR><TD>
<!--**************************************************************************//-->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR" valign="top">
            <BR> 
            <afscme:currentAffiliate/><br>  

            Update Type - <afscme:codeWrite name="updateType" codeType="UpdateType"    format="{1}"/>
             
        </TD>
    </TR>
</TABLE>
<!--**************************************************************//-->
<!--**********************************************************************************************//-->
<!--Exception display starts from here                                                            //-->
<!--**********************************************************************************************//-->
<!--**********************************************************************************************//-->
<!--Exception display starts from here                                                            //-->
<!--**********************************************************************************************//-->
<bean:define id="exception" name="exceptions"/>
<logic:present name="exception">
    <bean:define id="es"     name="exception" />
    <%  ExceptionData [] e = (ExceptionData []) es;
        int length = e.length;
    %>
    <% if(length > 0){ %>    
        
        <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
            <TR>
                    <TH colspan="11" align="left">
                            Summary of Exceptions 
                    </TH>
            </TR>
            <TR>
                    <TH width="4%">
                            Select 
                    </TH>
                    <TH width="9%">
                            Error 
                    </TH>
                    <TH colspan="5">
                            Affiliate Identifier 
                    </TH>
                    <TH>
                            Name 
                    </TH>
            </TR>
            <TR>
                    <TD colspan="2">&nbsp;
                    </TD>
                    <TH width="3%" class="small">
                            Type 
                    </TH>
                    <TH width="8%" class="small">
                            Local/Sub Chapter 
                    </TH>
                    <TH width="5%" class="small">
                            State/National Type
                    </TH>
                    <TH width="5%" class="small">
                            Sub Unit 
                    </TH>
                    <TH width="7%" class="small">
                            Council/Retiree Chapter 
                    </TH>
                    <TD>&nbsp;</TD>
            </TR>
            <!--********************************************************************************************************--//>

            
            <!--loop through the membercount map to display the values //-->
            <!--**************************************************************//-->
            <!--*************************************************************************//-->
            <!--Iterate through the map to display the exceptions to the user           //-->
            <!--************************************************************************//-->
                

                <logic:iterate id="exceptionData" name="exception" >
                    <TR valign="top" cellspacing="0">
                        <bean:define id="recId"  name="exceptionData" property="recordId"/>
                        <bean:define id="affPk"  name="exceptionData" property="affPk"/>
                        <bean:define id="eComp"  name="exceptionData" property="fieldChangeDetails" type="java.util.Map"/>
                        <bean:define id="first"  name="exceptionData" property="firstName"/>
                        <bean:define id="last"   name="exceptionData" property="lastName"/>
                        <bean:define id="middle" name="exceptionData" property="middleName"/>
                        <bean:define id="suffix" name="exceptionData" property="suffix"/>
                        <%  if(eComp != null) {
                            session.setAttribute(new String("e"), exception);
                        }            
                        %>

                        <TD align="center">
                            <A href="/viewApplyUpdateOfficerReviewExceptionDetail.action?recId=<%=recId%>&affPk=<%=affPk%>" class="action" title="View exception comparison">View</A> 
                        </TD>
                        <TD align="center" cellspacing="0">
                            <afscme:codeWrite name="exceptionData" codeType="UpdateFieldError" property="updateErrorCodePk"   format="{1}"/>
                        </TD>

                        <logic:present name="smry"  >
                            <afscme:affiliateIdWrite name="exceptionData" property="affId" />

                        </logic:present>

                        <TD align="center" cellspacing="0">
                            <% if(last   !=   null) { %> 
                                <%out.write(last.toString());%>
                            <% }%> &nbsp; 
                            <% if((suffix    !=   null) || (suffix    != "")){ %>
                                <%out.write(suffix.toString());%> 
                            <% }%>
                            <% out.write(","); %>
                            <% if(first  !=   null){ %> 
                                <%out.write(first.toString());%>
                            <% } %>
                            <% if(middle !=  null){ %> 
                                <%out.write(middle.toString());%>
                            <% } %>
                        </TD>

                </TR>
            </logic:iterate> 
            

         </TABLE>
    <%}%>

</logic:present>
        <!--**********************************************************************************************//-->
        <!--Exception display ends from here and footer display starts from here below                   //-->
        <!--**********************************************************************************************//-->
</TD></TR>
<TR><TD>
<!--End of parent table//-->
</TD></TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD>
            <BR> <afscme:button page="/viewApplyUpdateQueue.action">Return</afscme:button>
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<%@ include file="../include/footer.inc" %>
