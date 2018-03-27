<%@ include file="../include/minimumduesheader.inc" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%!String title = "", help = "MainMenu.html";%>

<% request.setAttribute("onload", "initForm();"); %> 

<body >
<div align="center"><font color="#808000" size="5"><strong>File Upload</strong></font><br>
</div>

<br><br>
<font size="3" >
<bean:define name="fileUploadActionForm" id="adf" type="org.afscme.enterprise.minimumdues.web.FileUploadActionForm"/>
<BR>

<center><html:errors/></center>

<html:form action="/fileUploadAction.action" method="post" enctype="multipart/form-data">
    Select File : <html:file property="file" /> <br/><br>
    <html:submit />
</html:form>

<br>
<br>
<%@ include file="../include/footer.inc" %> 

