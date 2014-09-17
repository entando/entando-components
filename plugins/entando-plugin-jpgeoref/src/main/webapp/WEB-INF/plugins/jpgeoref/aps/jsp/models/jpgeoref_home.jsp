<%@ page contentType="application/xhtml+xml; charset=utf-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="gwp" uri="/jpgeoref-aps-core" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head>
	<title>
		Entando 2.0| <wp:currentPage param="title" />
	</title>
	
    <wp:outputHeadInfo type="CSS"> 
        <link href="resources/css/<wp:printHeadInfo />" type="text/css" rel="stylesheet" />
    </wp:outputHeadInfo>
	
	<wp:outputHeadInfo type="JS_URL"> 
		<script type="text/javascript" src="<wp:printHeadInfo />" ></script>
	</wp:outputHeadInfo>
	
</head>
	
<body <gwp:reqCtxParamPrinter var="htmlBodyTagAttribute"/> style="font-family: Verdana; font-size: 0.9em;">

	
<h2>Front-End interface</h2>
<p>
This plugin is composed of two showlet. The first showlet is used is used to view the contents in a
map, while the second showlet is used to view the directions between two contents.
</p>
<p>
The available showlets are:
</p>
<ul>
	<li>Publish contents on Map - Interface used to view the contents in a map</li>
	<li>Route - Interface used to view the directions between two contents.</li>
</ul>
<p>
Note that the interface above show only the basic functionalities with no presentation elements: that
is because the presentation or decorative layout must be designed and developed by the front-end
programmers of the local Entando installation.
</p>
<p>
IMPORTANT NOTE 1:
In jpgeoref_GoogleListViewer.jsp and jpgeoref_GoogleRoute.jsp (that corresponds to the two
showlets) is necessary to modify the value utilized in the parameter "key" of the url that calls back the
api of google. The inserted value is unusable in exercise.
</p>
<p>
IMPORTANT NOTE 2:
In the models of page where are expected the inclusion of any google maps, it is necessary to:
</p>
<ul>
	<li>insert in the tag <body> the tag <gwp:reqCtxParamPrinter var="htmlBodyTagAttribute"/> as
indicated in the model of example /WEB-INF/plugins/jpgeoref/aps/jsp/models/home.jsp</li>
	<li>insert the block of the HeadInfo JS_URL as indicated in the model of example /WEB-INF/plugins/
jpgeoref/aps/jsp/models/home.jsp</li>
</ul>

<p>
IMPORTANT NOTE 3:
It must not be used the tag wp:contentNegotiation(both in the models of page, either in the main.jsp)
Contents and Models
For the types of content that are intended to georeferencing, it is necessary, in the type definition, the
special attribute "Coords".
</p>

<h2>Configuration of the Geo Ref plugin</h2>
<p>
For the types of content that are intended to georeference and to visualize in the showlets, that publish
contents on a map of Google, it is necessary to prevent two additional Models of Content:
</p>

<ul>
	<li>A "forMap" model that allows to create the marker and the "cloudlet" of the desired information
(with possible link and images) necessary to the "Publish contents on Map" showlet</li>
	<li>A "forMarker" model that allows the creation of the marker necessarty to the "Route" showlet.</li>
</ul>
	
<p>
The "forMap" model must hold a fixed part (necessary to allow the creation of the marker from the
api of google) and a free part necessary to the creation of the content of the associated "cloudlet". In
the next example is implied that the Attribute of Content that picks up the geographical position calls
"Coordinate". The "TEXT" label represents the customizable part and can be replaced by the text that
is intended to visualize inside the "cloudet"
</p>
<pre>
var point = new GLatLng($content.Coordinate.x,$content.Coordinate.y);
var marker = createMarker(point,'TEXT');
map.addOverlay(marker);
</pre>
<p>
The "forMarker" model has only a fixed part. In the next example is implied that the attribute of
Content that picks up the geographical position calls "Coordinate".
</p>
<pre>
new GLatLng($content.Coordinate.x,$content.Coordinate.y);
</pre>
	

<div>TRY the plugin into the unique frame into this page</div>

<div><wp:show frame="0" /></div>

</body>
</html>
