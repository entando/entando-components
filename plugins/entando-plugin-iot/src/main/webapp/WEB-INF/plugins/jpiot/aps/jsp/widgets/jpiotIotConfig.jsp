<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="jpiot" uri="/jpiot-core"%>

<jpiot:iotConfig var="iotConfig" />
<article>
<c:choose>
	<c:when test="${not empty iotConfig}">
	<h1><wp:i18n key="jpiot_IOTCONFIG_ID" />: <c:out value="${iotConfig.id}" /></h1>
	<ul>
		<li>
			<wp:i18n key="jpiot_IOTCONFIG_NAME" />: <c:out value="${iotConfig.name}" /><br />
			<wp:i18n key="jpiot_IOTCONFIG_HOSTNAME" />: <c:out value="${iotConfig.hostname}" /><br />
			<wp:i18n key="jpiot_IOTCONFIG_PORT" />: <c:out value="${iotConfig.port}" /><br />
			<wp:i18n key="jpiot_IOTCONFIG_WEBAPP" />: <c:out value="${iotConfig.webapp}" /><br />
			<wp:i18n key="jpiot_IOTCONFIG_USERNAME" />: <c:out value="${iotConfig.username}" /><br />
			<wp:i18n key="jpiot_IOTCONFIG_PASSWORD" />: <c:out value="${iotConfig.password}" /><br />
			<wp:i18n key="jpiot_IOTCONFIG_TOKEN" />: <c:out value="${iotConfig.token}" /><br />
		</li>
	</ul>
	</c:when>
	<c:otherwise>
	<div class="alert alert-error">
		<p><wp:i18n key="jpiot_IOTCONFIG_NOT_FOUND" /></p>
	</div>
	</c:otherwise>
</c:choose>
</article>