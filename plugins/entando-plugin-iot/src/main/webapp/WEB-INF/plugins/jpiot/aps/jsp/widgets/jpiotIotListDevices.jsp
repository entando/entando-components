<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="jpiot" uri="/jpiot-core"%>

<jpiot:iotListDevices var="iotListDevices" />
<article>
<c:choose>
	<c:when test="${not empty iotListDevices}">
	<h1><wp:i18n key="jpiot_IOTLISTDEVICES_ID" />: <c:out value="${iotListDevices.id}" /></h1>
	<ul>
		<li>
			<wp:i18n key="jpiot_IOTLISTDEVICES_WIDGETTITLE" />: <c:out value="${iotListDevices.widgetTitle}" /><br />
			<wp:i18n key="jpiot_IOTLISTDEVICES_DATASOURCE" />: <c:out value="${iotListDevices.datasource}" /><br />
			<wp:i18n key="jpiot_IOTLISTDEVICES_CONTEXT" />: <c:out value="${iotListDevices.context}" /><br />
			<wp:i18n key="jpiot_IOTLISTDEVICES_DOWNLOAD" />: <c:out value="${iotListDevices.download}" /><br />
			<wp:i18n key="jpiot_IOTLISTDEVICES_FILTER" />: <c:out value="${iotListDevices.filter}" /><br />
			<wp:i18n key="jpiot_IOTLISTDEVICES_ALLCOLUMNS" />: <c:out value="${iotListDevices.allColumns}" /><br />
			<wp:i18n key="jpiot_IOTLISTDEVICES_COLUMNS" />: <c:out value="${iotListDevices.columns}" /><br />
		</li>
	</ul>
	</c:when>
	<c:otherwise>
	<div class="alert alert-error">
		<p><wp:i18n key="jpiot_IOTLISTDEVICES_NOT_FOUND" /></p>
	</div>
	</c:otherwise>
</c:choose>
</article>