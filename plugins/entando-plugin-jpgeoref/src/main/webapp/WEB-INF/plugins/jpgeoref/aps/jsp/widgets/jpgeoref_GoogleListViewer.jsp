<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="gwp" uri="/jpgeoref-aps-core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- javascript escape setup --%>
<% pageContext.setAttribute("carriageReturn", "\r"); %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="singleQuotes">'</c:set>
<c:set var="singleQuotesReplace">\'</c:set>
<c:set var="doubleQuotes">"</c:set>
<c:set var="doubleQuotesReplace">\"</c:set>

<jacms:contentList listName="contentList" titleVar="titleVar"
	pageLinkVar="pageLinkVar" pageLinkDescriptionVar="pageLinkDescriptionVar" userFilterOptionsVar="userFilterOptionsVar" />

<gwp:geoRenderList
	centerCoordsParamName="center"
	southWestCoordsParamName="southWest"
	northEastCoordsParamName="northEast"
	master="contentList"
	markerParamName="markers"/>

<div class="jpgeoref-content_viewer_list">

	<c:if test="${!(empty titleVar)}">
		<h1><c:out value="${titleVar}" /></h1>
	</c:if>

	<c:set var="userFilterOptionsVar" value="${userFilterOptionsVar}" scope="request" />
	<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module.jsp" />

	<c:choose>
		<c:when test="${markers != null && !(empty markers)}">
			<wp:info key="currentLang" var="currentLang" />
			<c:set var="random"><%= java.lang.Math.round(java.lang.Math.random()*10000) %></c:set>
			<wp:headInfo type="JS_EXT" info="//maps.googleapis.com/maps/api/js?v=3&amp;sensor=false&language=${currentLang}" />
			<c:set var="javascript_map">
				jQuery(function(){
					google.maps.event.addDomListener(window, 'load', function(){
						var bounds = new google.maps.LatLngBounds();
						var map = new google.maps.Map(document.getElementById('jpgeoref-contentlist-map<c:out value="${random}" />'),
						{
							zoom: 8,
							center: new google.maps.LatLng(<c:out value="${center[0]}" />, <c:out value="${center[1]}" />),
							mapTypeId: google.maps.MapTypeId.ROADMAP
						});
						var infowindow = new google.maps.InfoWindow();
						var jsmarker;
						<c:forEach var="currentMarker" items="${markers}">
							(function(){
								<%-- your string --%>
								<c:set var="STRING_TO_ESCAPE"><jacms:content contentId="${currentMarker.contentId}" /></c:set>
								<c:set var="ESCAPED_STRING" value="${fn:replace(fn:replace(fn:replace(fn:replace(STRING_TO_ESCAPE,carriageReturn,' '),newLine,' '), singleQuotes, singleQuotesReplace),doubleQuotes,doubleQuotesReplace)}" />
								var jsmarker = new google.maps.Marker({
									position: new google.maps.LatLng(<c:out value="${currentMarker.x}" />, <c:out value="${currentMarker.y}" />),
									map: map,
									title: '<c:out value="${currentMarker.contentId}" />',
									infowindow: '<c:out value="${ESCAPED_STRING}" escapeXml="false" />'
								});
								bounds.extend(jsmarker.position);
								google.maps.event.addListener(jsmarker, 'click', function() {
									infowindow.setContent(jsmarker.infowindow);
									infowindow.open(map,jsmarker);
								});
							})();
							<c:remove var="STRING_TO_ESCAPE" />
							<c:remove var="ESCAPED_STRING" />
						</c:forEach>
						map.fitBounds(bounds);
					});
				});
			</c:set>
			<wp:headInfo type="JS_RAW" info="${javascript_map}" />
			<wp:headInfo type="CSS" info="../../plugins/jpgeoref/static/css/jpgeoref.css" />
			<div class="row-fluid">
				<div class="span12 jpgeoref-contentlist-map" id="jpgeoref-contentlist-map<c:out value="${random}" />"></div>
			</div>
		</c:when>
		<c:otherwise>
			<c:if test="${!(empty userFilterOptionsVar)}">
				<p class="alert alert-info"><wp:i18n key="LIST_VIEWER_EMPTY" /></p>
			</c:if>
		</c:otherwise>
	</c:choose>

	<c:if test="${!(empty pageLinkVar) && !(empty pageLinkDescriptionVar)}">
		<p class="text-right"><a class="btn btn-primary" href="<wp:url page="${pageLinkVar}"/>"><c:out value="${pageLinkDescriptionVar}" /></a></p>
	</c:if>
</div>

<%-- Important: reset variables --%>
<c:set var="userFilterOptionsVar" value="${null}" scope="request" />
<c:set var="contentList" value="${null}"  scope="request" />
<c:set var="group" value="${null}"  scope="request" />