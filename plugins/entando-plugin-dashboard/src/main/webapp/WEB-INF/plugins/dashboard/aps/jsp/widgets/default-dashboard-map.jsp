<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="iot" uri="/dashboard-core" %>

<link rel="stylesheet" href="https://unpkg.com/leaflet@1.4.0/dist/leaflet.css"
  integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
  crossorigin=""/>
  <!-- Make sure you put this AFTER Leaflet's CSS -->
 <script src="https://unpkg.com/leaflet@1.4.0/dist/leaflet.js"
   integrity="sha512-QVftwZFqvtRNi0ZyCtsznlKSWOStnDORoefr1enyq5mVL4tmKB3S/EnC3rRJcxCPavG10IcrVGSmPh6Qw5lwrg=="
   crossorigin=""></script>
   <script src="https://cdn.jsdelivr.net/npm/lodash@4.17.4/lodash.min.js"
     crossorigin=""></script>
<iot:defaultServerConfiguration var="configDefaultDashboard"/>

<c:out value="${configDefaultDashboard.dashboardId}"/>
<c:out value="${configDefaultDashboard.datasourceCode}"/>

<link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/css/widgets/map/map.css">
<script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/map/data/traffic.js"></script>
<script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/map/map.js"></script>
<h1>MAP</h1>
<div id="map"></div>
