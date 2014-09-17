<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>

<h2>Attribute List</h2>
<!-- "javax.servlet.jsp.jspException" for getting an Exception -->
<div>
<table border="1" width="100%">
<tr>
 <th>Name</th>
 <th>Value</th>
</tr>
<%
String name  = "";
String value = "";
java.util.Enumeration attributes = request.getAttributeNames();
while(attributes.hasMoreElements())
{
 name  = (String) attributes.nextElement();

 if (request.getAttribute(name) == null)
 {
  value = "null";
 }
 else
 {
  value = request.getAttribute(name).toString();
 }
%>
<tr>
 <td><%=name%></td>
 <td><%=value%></td>
</tr>
<%
}
%>
</table>
</div>