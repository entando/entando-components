<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpgeoref/administration/css/jpgeoref-administration.css" media="screen" />
<script type="text/javascript">
	jQuery(function(){
		var loadGMaps = function() {
			var script = document.createElement('script');
			script.type = 'text/javascript';
			script.src = 'http://maps.googleapis.com/maps/api/js?sensor=false&callback='+ 'jpgeoref_init_maps' + '&language=<s:property value="locale" />' ;
			document.body.appendChild(script);
		};
		window.jpgeoref_init_maps = function() {
			<wpsa:tracerFactory var="attributeTracer" lang="%{langs[0]}" /><%-- tracer init --%>
			<s:iterator value="content.attributeList" var="attribute">
				<s:if test="#attribute.type == 'Coords'">
					(function(){
						//console.log('started... <s:property value="#attribute.name" />');
						var mapcontainerHtmlId  = "mapcontainer_<s:property value="#attribute.name" />";
						var center = [<s:property value="#attribute.x" />||39.22453601575102, <s:property value="#attribute.y" />||9.096148437500005 ];
						var ourMap = new google.maps.Map(document.getElementById(mapcontainerHtmlId), {
							zoom : 5,
							center : new google.maps.LatLng(center[0], center[1]),
							streetViewControl: false,
							mapTypeId : google.maps.MapTypeId.ROADMAP
						});
						var marker = new google.maps.Marker({
							map: ourMap,
							draggable:true,
							position: new google.maps.LatLng(center[0],center[1]),
						});
						google.maps.event.addListener(marker, "dragend", function(ev)  {
							$("#<s:property value="%{'x_'+#attribute.name}" />").val(ev.latLng.lat());
							$("#<s:property value="%{'y_'+#attribute.name}" />").val(ev.latLng.lng());
						});

						var updateMarker = function(ev) {
							var pos = new google.maps.LatLng(
									$("#<s:property value="%{'x_'+#attribute.name}" />").val(),
									$("#<s:property value="%{'y_'+#attribute.name}" />").val()
							);
							ev.data.marker.setPosition(pos);
							ev.data.map.setCenter(pos);
						};

						$("#<s:property value="%{'x_'+#attribute.name}" />").on('keyup change', {
								marker: marker,
								map: ourMap
							}, updateMarker);
						$("#<s:property value="%{'y_'+#attribute.name}" />").on('keyup change', {
								marker: marker,
								map: ourMap
							}, updateMarker);
					})();
				</s:if>
			</s:iterator>
		};
		loadGMaps();
	});
</script>
