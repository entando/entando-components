<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<c:set var="random">weather-<%= java.lang.Math.round(java.lang.Math.random() * 9999999)%></c:set>
<c:set var="defaultLat">
	<wp:currentWidget param="config" configParam="latitude"/>
</c:set>
<c:set var="defaultLong">
	<wp:currentWidget param="config" configParam="longitude"/>
</c:set>
<c:set var="defaultTitle">
	<wp:currentWidget param="config" configParam="city"/>
</c:set>
<c:set var="defaultUnits">ca</c:set>
<c:set var="defaultColor">#a9d96f</c:set>

<!-- 
	City: <wp:currentWidget param="config" configParam="city"/><br/>
	lat: <wp:currentWidget param="config" configParam="latitude"/><br/>
	long: <wp:currentWidget param="config" configParam="longitude"/><br/>
-->

    <iframe
        type="text/html"
        frameborder="0"
        height="245"
        width="100%"
        id="iframe-<c:out value="${random}" />"
    src="http://forecast.io/embed/#&lat=<c:out value="${defaultLat}" />&lon=<c:out value="${defaultLong}" />&name=<c:out value="${defaultTitle}" />&color=<c:out value="${defaultColor}" />&units=<c:out value="${defaultUnits}" />"></iframe>

<script type="text/javascript">
    ;
    (function() {
        var iframeId = "iframe-<c:out value="${random}" />"
        var done = false;
        var dogeo = function(position) {
            if (!done) {
                done = true;
    <%-- http://blog.forecast.io/forecast-embeds/ --%>
                var newIframeHtmlString = '<iframe '
                        + ' type="text/html"'
                        + ' frameborder="0"'
                        + ' height="245"'
                        + ' width="100%"'
                        + ' id="iframe-<c:out value="${random}" />"'
                        + ' src="'
                        + 'http://forecast.io/embed/#'
                        + '&lat=' + position.coords.latitude
                        + '&lon=' + position.coords.longitude
                        + '&color=' + '<c:out value="${defaultColor}" />'
                        + '&units=' + '<c:out value="${defaultUnits}" />'
                        + '&name=' + escape('your position <small title="' + position.coords.latitude + ', ' + position.coords.longitude + ' ">(' + (position.coords.latitude + '').substring(0, 4) + ',' + (position.coords.longitude + '').substring(0, 4) + ')</small>')
                        + '"></iframe>';
                //console.log(newIframeHtmlString);
                jQuery('#' + iframeId).replaceWith(newIframeHtmlString);
            }
        };
        var geo_error = function(error) {
            //console.log('some error occured acquiring your position', error);
        };
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(dogeo, geo_error, {
                enableHighAccuracy: false,
                maximumAge: 3600000,
                timeout: 3000
            });
        }
        // else {
        // 	geo_error();
        // }
    })();
</script>
