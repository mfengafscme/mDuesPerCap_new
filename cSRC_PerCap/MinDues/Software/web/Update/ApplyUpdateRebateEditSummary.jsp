<%! String title = "Apply Update Rebate Edit Summary", help = "ApplyUpdateRebateEditSummary.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@page import="java.util.*"%>
<%@page import="org.afscme.enterprise.update.Codes.UpdateFieldError"%>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD align="center">
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>
<bean:define    id="updateType"name="updateType"/>
<bean:define    id="queuePk"   name="queuePk"   />
<!--Check for rejected functionality //-->
<bean:define    id="rejected"  name="rejected"  />
<bean:define    id="affPk"     name="affPk"     />
<!--**************************************************************//-->
<html:form action="performRebateUpdate.action" method="post">

<!--**************************************************************************//-->
<logic:equal name="rejected" value="false">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" >
        <TR valign="top">
            <TD align="LEFT">
                <BR>
                <html:submit styleClass="button" value="Perform Update"/>                
                <BR> <BR> 
            </TD>
            <TD align="RIGHT">
                <BR>
               <afscme:button action="/viewApplyUpdateQueue.action" >Cancel</afscme:button>
                <% String url = "/viewApplyUpdateRebateRejectSummary.action?queuePk=" + queuePk + "&affPk=" + affPk ; %>
                            
                <afscme:button action="<%=url%>"  >Reject</afscme:button>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</logic:equal>
<!--************************************************************************//-->
<logic:equal name="rejected" value="true">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <BR> <afscme:button page="/viewApplyUpdateQueue.action">Return</afscme:button>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</logic:equal>
<!--**************************************************************************//-->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR" valign="top">

            <afscme:currentAffiliate/><br>  

            Update Type - <afscme:codeWrite name="updateType" codeType="UpdateType"    format="{1}"/>
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TH colspan="14" align="left">
				Summary of Rebate Requests per Affiliate 
			</TH>
		</TR>
		<TR>
			<TH>
				Select For 
			</TH>
			<TH colspan="5">
				Affiliates in File 
			</TH>
			<TH colspan="3">
				Members 
			</TH>
			<TH colspan="3">
				Acceptance Code Change 
			</TH>
		</TR>
		<TR>
			<TH class="small">
				Update 
			</TH>
			<TH class="small">
				Type 
			</TH>
			<TH class="small">
				Local/Sub Chapter  
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
				Sent 
			</TH>
			<TH class="small">
				Rec'd 
			</TH>
			<TH class="small" width="8%">
				Unknown 
			</TH>
			<TH class="small">
				Council 
			</TH>
			<TH class="small">
				Local 
			</TH>
			<TH class="small">
				No Chg 
			</TH>
		</TR>
                    
                            <!--******************************************************************************************************--//>
           <bean:define id="memChanges" name="memberCount" />


           <!--********************************************************************************************************--//>
           <logic:iterate id="me" name="memChanges"> 
           
           <!--loop through the membercount map to display the values //-->
           <!--**************************************************************//-->
           <!--Indicate on the screen an error (red) or a warning (yellow) //--> 
               <TR>
                    <% boolean error = false, warning = false; %>

                    <logic:equal name="me" property="value.hasError" value="true">
                         <% error = true;%>
                    </logic:equal>

                    <logic:equal name="me" property="value.hasWarning" value="true">
                         <% warning = true;%>
                    </logic:equal>
                    
                    <bean:define id="affid" name="me" property="value.affPk" type="java.lang.Object"/>

                    <%if(error){%>

                        <TD align="center" bgcolor="red">
                            &nbsp
                            <%error=false;
                            %>
                         </TD>

                    <%} else if(warning){ %>
                        <logic:equal name="rejected" value="true">
                            <TD align="center" bgcolor="yellow">
                                <html:multibox property="affPks"                        value="<%=(Integer.toString(((Integer)affid).intValue()))%>" disabled="true"/>
                            </TD>
                        </logic:equal>
                        <logic:equal name="rejected" value="false">
                            <TD align="center" bgcolor="yellow">
                                <html:multibox                  property="affPks"       value="<%=(Integer.toString(((Integer)affid).intValue()))     %>" />
                                <html:hidden name="queuePk"     property="queuePk"      value="<%=(queuePk).toString()   %>" />
                                <html:hidden name="affPk"       property="affPk"        value="<%=(Integer.toString(((Integer)affid).intValue()))     %>" />
                                <html:hidden name="updateType"  property="updateType"   value="<%=(updateType).toString()%>" />

                            </TD>
                            <%warning=false;%>
                        </logic:equal>
                    <%} else{ %>
                        <logic:equal name="rejected" value="true">
                            <TD align="center" >
                                <html:multibox property="affPks"                        value="<%=(Integer.toString(((Integer)affid).intValue()))     %>" disabled="true"/>


                            </TD>
                        </logic:equal>
                        <logic:equal name="rejected" value="false">
                            <TD align="center" >
                                <html:multibox                  property="affPks"       value="<%=(Integer.toString(((Integer)affid).intValue()))     %>" disabled="false"/>
                                <html:hidden name="queuePk"     property="queuePk"      value="<%=(queuePk).toString()   %>"                 />
                                <html:hidden name="affPk"       property="affPk"        value="<%=(Integer.toString(((Integer)affid).intValue()))     %>"                 />
                                <html:hidden name="updateType"  property="updateType"   value="<%=(updateType).toString()%>"                 />

                            </TD>
                        </logic:equal>
                    <%}%>

               <!--***********************************************************************//-->
                    <TD align="center">

                            <bean:write name="me" property="key.type"/>
                            &nbsp;
                    </TD>
                    <TD align="center">

                            <bean:write name="me" property="key.local"/>
                            &nbsp;
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="key.state"/>
                            &nbsp;
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="key.subUnit"/>
                            &nbsp;
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="key.council"/>
                            &nbsp;
                    </TD>

                    <TD align="center">
                            <bean:write name="me" property="value.sent" ignore="true"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.received" ignore="true"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.unknown" ignore="true"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.council" ignore="true"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.local" ignore="true"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.unchanged" ignore="true"/>
                    </TD>
                    
              </TR>
        </logic:iterate>
                
                 <!--*************************************************************-->
                <bean:define id="tCounts" name="tCount"/>
                <!--********************************************************************************************************//-->
               <TR>

                    <TD colspan="6" class="ContentHeaderTD" align="right">
                            Totals: 
                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="sent" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="received" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="unknown" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="council" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="local" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="unchanged" ignore="true"/>

                    </TD>
                    



            </TR>



</TABLE>

<!--**********************************************************************************************//-->
<!--Exception display starts from here                                                            //-->
<!--**********************************************************************************************//-->
<bean:define id="exception" name="exceptions"/>
<logic:present name="exception">
    <bean:define id="es"     name="exception" type="java.util.HashMap"/>
    
    <% if(!es.isEmpty()){ %>
    

        <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
            <TR>
                <TH colspan="11" align="left">
                        Summary of Exceptions 
                </TH>
            </TR>
            <TR>
                    <TH>
                            Select
                    </TH>
                    <TH>
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
                <TD width="5%">&nbsp;
                </TD>
                <TD width="5%">&nbsp;
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
                <TD>&nbsp;
                </TD>
            </TR>
            <!--********************************************************************************************************--//>

            <logic:iterate id="eSmry"   name="exceptions"   >
              <!--loop through the membercount map to display the values //-->
               <!--**************************************************************//-->
                <!--*************************************************************************//-->
                <!--Iterate through the map to display the exceptions to the user           //-->
                <!--************************************************************************//-->
                <TR valign="top">

                    <logic:iterate id="exceptionData" name="eSmry" property="value" type="org.afscme.enterprise.update.ExceptionData">
                        <bean:define id="recId" name="exceptionData" property="recordId"/>
                        <bean:define id="affPk2" name="exceptionData" property="affPk"/>
                        <bean:define id="eComp" name="exceptionData" property="fieldChangeDetails" type="java.util.Map"/>
                        <%  if(eComp != null) {
                                session.setAttribute(new String("e"), exception);
                            }            
                         %>
                        <TD align="center" class="ContentTD">
                            <bean:define id="errorCode" name="exceptionData" property="updateErrorCodePk" type="java.lang.Integer"/>

                            
                            <% int code =  UpdateFieldError.DUPLICATE.intValue();%>
                            <% if((errorCode != null) && (errorCode.intValue() != code)){%>
                            
                                <A href="/viewApplyUpdateRebateExceptionDetail.action?recId=<%=recId%>&affPk=<%=affPk2%>" class="action" title="View exception comparison">View</A> 
                            <%}%>
                        </TD>
                        <TD align="middle">
                            <afscme:codeWrite name="exceptionData" codeType="UpdateFieldError" property="updateErrorCodePk"   format="{1}"/>

                        </TD>

                        <logic:present name="eSmry"  >
                            <afscme:affiliateIdWrite name="exceptionData" property="affId" />

                        </logic:present>

                        <TD align="middle">
                            <% if(exceptionData.getLastName()  !=   null){ %> 
                                <bean:write name="exceptionData" property="lastName" />
                                
                            <% } %>  
                            <% if(exceptionData.getSuffix()    !=   null){ %>
                                <bean:write name="exceptionData" property="suffix" /> 
                            <% } %>
                            <% out.write(","); %>
                            <% if(exceptionData.getFirstName() !=   null){ %> 
                                <bean:write name="exceptionData" property="firstName" />
                            <% } %>
                            <% if(exceptionData.getMiddleName() !=  null){ %> 
                                <bean:write name="exceptionData" property="middleName" />
                            <% } %>
                            
                        </TD>

                     </logic:iterate>
                </logic:iterate> 
                </TR>
        </TABLE>

    <%}%>

</logic:present>
<!--**********************************************************************************************//-->
<!--Exception display ends from here and footer display starts from here below                   //-->
<!--**********************************************************************************************//-->
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

<!--**************************************************************************//-->
<logic:equal name="rejected" value="false">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" >
        <TR valign="top">
            <TD align="LEFT">
                <BR>
                <html:submit styleClass="button" value="Perform Update"/>                
                <BR> <BR> 
            </TD>
            <TD align="RIGHT">
                <BR>
               <afscme:button action="/viewApplyUpdateQueue.action" >Cancel</afscme:button>
                <afscme:button action="/viewApplyUpdateRebateRejectSummary" paramId="queuePk" paramName="queuePk" >Reject</afscme:button>                
                <BR> <BR>  
            </TD>
        </TR>
    </TABLE>
</logic:equal>
<!--************************************************************************//-->
<logic:equal name="rejected" value="true">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <BR> <afscme:button page="/viewApplyUpdateQueue.action">Return</afscme:button>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</logic:equal>
<!--**************************************************************************//-->
</html:form>
<%@ include file="../include/footer.inc" %>     
