<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Entando - Define Area</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta charset="utf-8" />
		<!-- entando default -->
			<link rel="stylesheet" href="<wp:resourceURL />administration/bootstrap/css/bootstrap.min.css" media="screen" />
			<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-override.css" media="screen" />
			<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-offcanvas.css" media="screen" />
			<link rel="stylesheet" href="<wp:resourceURL />administration/css/bootstrap-addendum.css" media="screen" />
			<c:catch>
				<tiles:insertAttribute name="cssSpecial" ignore="true" />
			</c:catch>

			<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
			<!--[if lt IE 9]>
				<script src="<wp:resourceURL />administration/js/html5shiv.js"></script>
				<script src="<wp:resourceURL />administration/js/respond.min.js"></script>
			<![endif]-->

<!-- jpimagemap -->
		<script type="text/javascript" src="<wp:resourceURL />plugins/jpimagemap/administration/js/mootools-core-1.4.5-full-nocompat-yc.js"></script>
		<script type="text/javascript" src="<wp:resourceURL />plugins/jpimagemap/administration/js/mootools-more-1.4.0.1.js"></script>
		<script type="text/javascript">
			var ImageMapAttribute_fieldError_linkedAreaElement_intersectedArea = "<s:property value="%{getText('ImageMapAttribute.fieldError.linkedAreaElement.intersectedArea')}" escapeJavaScript="true" />";
		</script>
		<script type="text/javascript" src="<wp:resourceURL />plugins/jpimagemap/administration/js/jpimagemap-areas.js"></script>
		<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpimagemap/administration/css/administration-jpimagemap.css" />
<!-- jpimagemap -->
	</head>

<body class="client-<s:property value="#myClient" />">
	<div class="container" id="container-main">
		<div class="navbar navbar-default navbar-static-top" id="navbar" role="banner">
			<div class="navbar-header">
				<a href="#sidebar" class="btn-offcanvas navbar-toggle pull-right visible-xs" data-toggle="offcanvas">
					<span class="sr-only"><s:text name="label.menu" /></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</a>
				<a class="navbar-brand" href="<s:url action="main" namespace="/do" />"><img src="<wp:resourceURL />administration/img/entando-logo-symbol-70x42.png" alt="Entando - Simplifying Enterprise Portals" width="70" height="42" /></a>
			</div>
		</div>
		<div id="jpimagemap_toolbarinfo">
			<s:form action="saveArea" id="mainform">
				<p>
					<input value="" size="4" name="top" type="hidden" />
					<input value="" size="4" name="left" type="hidden" />
					<input value="" size="4" name="width" type="hidden" />
					<input value="" size="4" name="height" type="hidden" />
					<input value="" size="4" name="bottom" type="hidden"/>
					<input value="" size="4" name="right" type="hidden" />

					<wpsf:hidden name="attributeName"/>
					<wpsf:hidden name="elementIndex" />
					<wpsf:hidden name="langCode"/>
					<wpsf:hidden name="contentOnSessionMarker" />

					<span class="text-info"><s:text name="jpimagemap.defineArea.note.positioning" /></span>
				</p>
				<p>
					<input class="btn btn-primary" id="pulsante" value="<s:text name="jpimagemap.label.save" />" type="submit" />
					<span id="messages"></span>
				</p>
			</s:form>
		</div>
	</div>
		<div id="main">
			<div id="areas">
				<img src="<s:property value="%{attribute.getResource().getImagePath('0')}"/>" />
				<s:if test="%{(attribute.resource != null && attribute.resource.id != 0 ) || attribute.text != ''}">
					<s:if test="%{attribute.areas != null}">
						<s:iterator value="%{attribute.areas}" id="area" status="statusElement">
							<s:if test="%{#area!=null}">
								<!-- cords: <s:property value="#area.coords=='0,0,0,0'" /> -->
								<s:if test="%{#statusElement.index == elementIndex}" >
									<s:if  test="%{#area.coords == '0,0,0,0'}" >
										<s:set var="top">0</s:set>
										<s:set var="left">0</s:set>
										<s:set var="width">120</s:set>
										<s:set var="height">120</s:set>
									</s:if>
									<s:else>
										<s:set var="left" value="%{#area.arrayCoords[0]}" />
										<s:set var="top" value="%{#area.arrayCoords[1]}" />
										<s:set var="width" value="%{#area.arrayCoords[2]-#left}" />
										<s:set var="height" value="%{#area.arrayCoords[3]-#top}" />								</s:else>
									<div class="area area<s:property value="#statusElement.index" /> area-current"
										style=" top: <s:property value="#top" />px; left: <s:property value="#left" />px; width: <s:property value="#width" />px; height: <s:property value="#height" />px; ">
											Area <s:property value="#statusElement.count" />
									</div>
								</s:if>
								<s:else>
									<s:if  test="%{#area.coords == '0,0,0,0'}" >
										<s:set var="top">0</s:set>
										<s:set var="left">0</s:set>
										<s:set var="width">120</s:set>
										<s:set var="height">120</s:set>
									</s:if>
									<s:else>
										<s:set var="left" value="%{#area.arrayCoords[0]}" />
										<s:set var="top" value="%{#area.arrayCoords[1]}" />
										<s:set var="width" value="%{#area.arrayCoords[2]-#left}" />
										<s:set var="height" value="%{#area.arrayCoords[3]-#top}" />
									</s:else>
										<div
											class="area area<s:property value="#statusElement.index" /> area-blocked"
											style=" top: <s:property value="#top" />px; left: <s:property value="#left" />px; width: <s:property value="#width" />px; height: <s:property value="#height" />px; ">Area&#32;<s:property value="#statusElement.count" /></div>
									</s:else>
							</s:if>
						</s:iterator>
					</s:if>

				</s:if>

			</div>
		</div>
		<!-- jpimagemap -->
		<p class="sr-only"><a href="#fagiano_mainContent"><s:text name="note.backToMainContent" /></a></p>
		<p class="sr-only"><a href="#fagiano_start"><s:text name="note.backToStart" /></a></p>

		<div class="col-sm-12 margin-large-top">
			<ul class="sr-only">
				<li><a href="#fagiano_mainContent"><s:text name="note.backToMainContent" /></a></li>
				<li><a href="#fagiano_start"><s:text name="note.backToStart" /></a></li>
			</ul>
			<div class="text-center" role="contentinfo">
				<jsp:include page="/WEB-INF/apsadmin/jsp/common/tiles-inserts/footer.jsp" />
			</div>
		</div>
</body>
</html>
