<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/ProcessDiagram/view" />

<%--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1>Configurable widget</h1>

<c:set var="configVar">
        <wp:currentWidget param="config" configParam="configParam"/>
</c:set>

Configured parameter:&nbsp;<c:out value="${configVar}"/>
--%>