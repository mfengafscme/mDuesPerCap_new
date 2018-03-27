<%@ page import="java.io.*" %>

<%

response.setContentType("text/plain");
FileInputStream log = new FileInputStream("../server/default/log/server.log");
BufferedReader br = new BufferedReader(new InputStreamReader(log));
while (true) {
	String line = br.readLine();
	if (line == null)
		break;
	response.getWriter().println(line);
}

%>

