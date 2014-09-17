<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="gwp" uri="/jpgeoref-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="random"><%= java.lang.Math.round(java.lang.Math.random()*10000) %></c:set>
<wp:headInfo type="CSS" info="widgets/jacms/content_viewer_list.css" />
<wp:info key="currentLang" var="currentLang" />
<wp:headInfo type="JS_EXT" info="//maps.googleapis.com/maps/api/js?v=3&amp;sensor=false&language=${currentLang}" />
<wp:currentWidget param="config" configParam="listModelId" var="listModelId" />
<gwp:geoRoute listName="contentList" />
<gwp:geoRenderList
	centerCoordsParamName="center"
	southWestCoordsParamName="southWest"
	northEastCoordsParamName="northEast"
	master="contentList"
	markerParamName="markers"/>

<div class="row-fluid">
	<c:set var="javascript_map">
		jQuery(function(){
			google.maps.event.addDomListener(window, 'load', function(){
				var directionsDisplay = new google.maps.DirectionsRenderer();
				var directionsService = new google.maps.DirectionsService();
				var bounds = new google.maps.LatLngBounds();
				var map = new google.maps.Map(document.getElementById('jpgeoref-contentlist-map<c:out value="${random}" />'),
				{
					zoom: 8,
					center: new google.maps.LatLng(<c:out value="${center[0]}" />, <c:out value="${center[1]}" />),
					mapTypeId: google.maps.MapTypeId.ROADMAP
				});
				map.fitBounds(bounds);
				directionsDisplay.setMap(map);
				<c:set var="markersLength" value="${fn:length(markers)}" />
				directionsService.route({
					origin: new google.maps.LatLng(<c:out value="${markers[0].x},${markers[0].y}" />),
					destination: new google.maps.LatLng(<c:out value="${markers[markersLength-1].x},${markers[markersLength-1].y}" />),
					waypoints: [
						<c:if test="${markersLength>2}">
							<c:forEach var="currentMarker" items="${markers}" begin="${1}" end="${markersLength-2}" varStatus="status" >
								{
									"location": new google.maps.LatLng(<c:out value="${currentMarker.x},${currentMarker.y}" />)
								}<c:if test="${!status.last}">,</c:if>
							</c:forEach>
						</c:if>
					],
					optimizeWaypoints: true,
					travelMode: google.maps.TravelMode.DRIVING
				}, function(response, status) {
					if (status == google.maps.DirectionsStatus.OK) {
						directionsDisplay.setDirections(response);
						var route = response.routes[0];
						var summaryPanel = document.getElementById('directions_panel<c:out value="${random}" />');
						summaryPanel.innerHTML = '';
						// For each route, display summary information.
						for (var i = 0; i < route.legs.length; i++) {
							var routeSegment = i + 1;
							summaryPanel.innerHTML += '<p><span class="badge">Route Segment: ' + routeSegment + '</span><br />';
							summaryPanel.innerHTML += route.legs[i].start_address + ' to ';
							summaryPanel.innerHTML += route.legs[i].end_address + '<br/>';
							summaryPanel.innerHTML += route.legs[i].distance.text + '</p>';
						}
					}
				});
			});
		});
	</c:set>
	<wp:headInfo type="JS_RAW" info="${javascript_map}" />
	<wp:headInfo type="CSS" info="../../plugins/jpgeoref/static/css/jpgeoref.css" />
	<div class="row-fluid">
		<div class="span6 jpgeoref-contentlist-map" id="jpgeoref-contentlist-map<c:out value="${random}" />"></div>
		<div class="span6" id="directions_panel<c:out value="${random}" />"></div>
	</div>
	<c:if test="${!(empty listModelId)}">
		<div class="row-fluid">
			<div class="span12">
				<ol>
					<c:forEach items="${contentList}" var="contentId">
						<li><jacms:content contentId="${contentId}" modelId="${listModelId}" /></li>
					</c:forEach>
				</ol>
			</div>
		</div>
	</c:if>
</div>