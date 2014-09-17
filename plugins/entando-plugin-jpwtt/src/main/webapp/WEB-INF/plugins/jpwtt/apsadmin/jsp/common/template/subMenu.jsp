<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:ifauthorized permission="jpwttAdmin" var="isAdmin"/>
<wp:ifauthorized permission="jpwttOperator" var="isOperator"/>
<c:if test="${isAdmin || isOperator}">
<li>
	<a href="<s:url action="list" namespace="/do/jpwtt/Ticket" />"  ><s:text name="jpwtt.admin.menu"/></a>
</li>
</c:if>