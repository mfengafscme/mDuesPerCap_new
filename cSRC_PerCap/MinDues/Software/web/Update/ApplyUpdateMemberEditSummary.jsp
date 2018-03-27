<%! String title = "Apply Update Member Edit Summary", help = "ApplyUpdateMemberEditSummary.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@page import="java.util.*"%>
<%@page import="org.afscme.enterprise.update.Codes.UpdateFieldError"%>
<%@page import="org.afscme.enterprise.update.FieldChange"%>

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
<html:form action="performMemberUpdate.action" method="post">

<!--**************************************************************************//-->
<logic:equal name="rejected" value="false">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" >
        <TR valign="top">
            
            <TD align="left">
                <BR>
                    
                    <html:submit styleClass="button" value="Perform Update"/>
                    
                <BR> <BR> 
            </TD>
            <TD align="RIGHT">
                <BR>
                <afscme:button action="/viewApplyUpdateQueue.action" >Cancel</afscme:button>&nbsp;

                <% String url = "/viewApplyUpdateMemberRejectSummary.action?queuePk=" + queuePk + "&affPk=" + affPk ; %>
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
                            Summary of Member Changes per Active Affiliate 
                    </TH>
            </TR>
            <TR>
                    <TH>
                            Select For 
                    </TH>
                    <TH colspan="5">
                            Affiliates in File 
                    </TH>
                    <TH colspan="6">
                            Members 
                    </TH>
                    <TH colspan="2">
                            Data Match 
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
                            Loc/Sub Chap 
                    </TH>
                    <TH class="small">
                            State/Nat'l 
                    </TH>
                    <TH class="small">
                            Sub Unit 
                    </TH>
                    <TH class="small">
                            CN/Ret Chap 
                    </TH>
                    <TH class="small">
                            In System 
                    </TH>
                    <TH class="small">
                            In File 
                    </TH>
                    <TH class="small">
                            Added 
                    </TH>
                    <TH class="small">
                            Deactivated 
                    </TH>
                    <TH class="small">
                            Changed 
                    </TH>
                    <TH class="small">
                            'T' Records 
                    </TH>
                    <TH class="small">
                            Match 
                    </TH>
                    <TH class="small">
                            Non-Match 
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
                            <html:hidden name="queuePk" property="queuePk" value="<%=(queuePk.toString())  %>" />
                            <html:hidden name="affPk" property="affPk" value="<%=(affid.toString()) %>" />
                            <html:hidden name="updateType" property="updateType" value="<%=(updateType.toString())%>" />
                            <%error=false;
                            %>
                         </TD>

                    <%} else if(warning){ %>
                        <logic:equal name="rejected" value="true">
                            <TD align="center" bgcolor="yellow">
                                <html:multibox property="affPks" value="<%=(affid.toString())%>" disabled="true"/>
                                <html:hidden name="queuePk" property="queuePk" value="<%=(queuePk.toString())  %>" />
                                <html:hidden name="affPk" property="affPk" value="<%=(affid.toString()) %>" />
                                <html:hidden name="updateType" property="updateType" value="<%=(updateType.toString())%>" />
                            </TD>
                        </logic:equal>
                        <logic:equal name="rejected" value="false">
                            <TD align="center" bgcolor="yellow">
                                <html:multibox property="affPks" value="<%=(affid.toString())%>" />
                                <html:hidden name="queuePk" property="queuePk" value="<%=(queuePk.toString())  %>" />
                                <html:hidden name="affPk" property="affPk" value="<%=(affid.toString()) %>" />
                                <html:hidden name="updateType" property="updateType" value="<%=(updateType.toString())%>" />

                            </TD>
                            <%warning=false;%>
                        </logic:equal>
                    <%} else{ %>
                        <logic:equal name="rejected" value="true">
                            <TD align="center" >
                                <html:multibox property="affPks" value="<%=(affid.toString())%>" disabled="true"/>
                                <html:hidden name="queuePk" property="queuePk" value="<%=(queuePk.toString())  %>" />
                                <html:hidden name="affPk" property="affPk" value="<%=(affid.toString()) %>" />
                                <html:hidden name="updateType" property="updateType" value="<%=(updateType.toString())%>" />

                            </TD>
                        </logic:equal>
                        <logic:equal name="rejected" value="false">
                            <TD align="center" >
                                <html:multibox property="affPks" value="<%=(affid.toString())%>" disabled="false"/>
                                <html:hidden name="queuePk" property="queuePk" value="<%=(queuePk.toString())  %>" />
                                <html:hidden name="affPk" property="affPk" value="<%=(affid.toString()) %>" />
                                <html:hidden name="updateType" property="updateType" value="<%=(updateType.toString())%>" />

                            </TD>
                        </logic:equal>
                    <%}%>

               <!--***********************************************************************//-->
                    <TD align="center">

                            <bean:write name="me" property="key.type"/>
                    </TD>
                    <TD align="center">

                            <bean:write name="me" property="key.local"/>

                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="key.state"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="key.subUnit"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="key.council"/>
                    </TD>

                    <TD align="center">
                            <bean:write name="me" property="value.inSystem" ignore="true"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.inFile" ignore="true"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.added" ignore="true"/>                            
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.inactivated" ignore="true"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.changed" ignore="true"/>
                    </TD>
                    <TD align="center">
                            <bean:write name="me" property="value.newTRecords" ignore="true"/>
                    </TD>
                    <TD align="center">
                             <bean:write name="me" property="value.matchedAffMemberNumCount" ignore="true"/>
                    </TD>
                    <TD align="center">

                            <bean:write name="me" property="value.nonMatch" ignore="true"/>
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
                             <bean:write name="tCount" property="inSystem" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="inFile" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="added" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="inactivated" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="changed" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="newTRecords" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                             <bean:write name="tCount" property="matchedAffMemberNumCount" ignore="true"/>

                    </TD>
                    <TD class="ContentHeaderTD" align="center">
                              <bean:write name="tCount" property="nonMatch" ignore="true"/>

                    </TD>




            </TR>



</TABLE>
<bean:define id="fieldChange" name="fieldChange" type="java.util.HashMap"/> 
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR>
            <TH colspan="2" align="left">
                    Summary of Field Changes 
            </TH>
        </TR>
        <TR valign="top">
            <TD width="50%">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <% for (int i=1; i<=fieldChange.size(); i++) { %>
                        <TR>
                            <% FieldChange mapentry = (FieldChange) fieldChange.get(new Integer(i)); %>
                            <TD width="70%" class="ContentHeaderTD">
                                <%= mapentry.getName() %>
                            </TD>
                            <TD>
                                <%= mapentry.getCount() %>
                            </TD>
                         </tr>
                    <% } %>
                </TABLE>
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
                        Loc/Sub Chap
                </TH>
                <TH width="5%" class="small">
                        State/Nat'l
                </TH>
                <TH width="5%" class="small">
                        Sub Unit
                </TH>
                <TH width="7%" class="small">
                        CN/Ret Chap
                </TH>
                <TD>&nbsp;
                </TD>
            </TR>
            <!--********************************************************************************************************--//>

            <logic:iterate id="eSmry"   name="exception"   >
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
                            
                                <A href="/viewApplyUpdateMemberExceptionDetail.action?recId=<%=recId%>&affPk=<%=affPk2%>" class="action" title="View exception comparison">View</A> 
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
            <TD align="left">
                <BR>
                    <html:submit styleClass="button" value="Perform Update"/>
                <BR> <BR>  
            </TD>
            <TD align="right">
                <BR>
                <afscme:button action="/viewApplyUpdateQueue.action" >Cancel</afscme:button>&nbsp;

                <% String url = "/viewApplyUpdateMemberRejectSummary.action?queuePk=" + queuePk + "&affPk=" + affPk ; %>
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
</html:form>
<%@ include file="../include/footer.inc" %>     
