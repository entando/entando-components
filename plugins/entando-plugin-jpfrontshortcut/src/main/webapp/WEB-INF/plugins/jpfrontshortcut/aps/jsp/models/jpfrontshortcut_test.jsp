<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<!DOCTYPE html>
<html lang="<wp:info key="currentLang" />">
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<title>
			<wp:currentPage param="title" /> - Entando
		</title>
		<link rel="icon" href="<wp:info key="systemParam" paramName="applicationBaseURL" />favicon.png" type="image/png" />

		<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
		<!--[if lt IE 9]>
			<script src="<wp:resourceURL />static/js/entando-misc-html5-essentials/html5shiv.js"></script>
		<![endif]-->

		<jsp:include page="/WEB-INF/aps/jsp/models/inc/lesscss-active/lesscss.jsp" />
		<jsp:include page="/WEB-INF/aps/jsp/models/inc/models-common-utils.jsp" />
		
		<%-- JS_JQUERY: (mandatory) it's used to load the necessary coming from the plugin. It also set the flag "outputHeadInfo_JS_JQUERY_isHere"  --%>
			<wp:outputHeadInfo type="JS_JQUERY">
				<c:set var="outputHeadInfo_JS_JQUERY_isHere" value="${true}" />
				<wp:printHeadInfo />
			</wp:outputHeadInfo>
		
	</head>
	<body>

		<div class="container">
			<div class="row">
				<div class="span12">
					<h1><wp:currentPage param="title" /></h1>
					<p>Here you can test the Front Shortcut Plugins functionalities</p>
				</div>
			</div>
			
			<div class="row">
				<div class="span12">
					<wp:show frame="0" />
				</div>
			</div>

			<div class="row">
				<div class="span6">
					<wp:show frame="1" />
					<wp:show frame="2" />
					<wp:show frame="3" />
					<wp:show frame="4" />
				</div>
				<div class="span6">
					<wp:show frame="5" />
					<wp:show frame="6" />
					<wp:show frame="7" />
					<wp:show frame="8" />
				</div>
			</div>
		</div>
		<c:if test="${outputHeadInfo_JS_JQUERY_isHere}">
			<jsp:include page="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/models/inc/widget_popup_init.jsp" />
		</c:if>

		
		<script type="text/javascript" src="<wp:resourceURL />static/entando-misc-bootstrap/bootstrap/js/bootstrap-dropdown.js"></script>
		<script type="text/javascript" src="<wp:resourceURL />static/entando-misc-bootstrap/bootstrap/js/bootstrap-button.js"></script>
		<script type="text/javascript" src="<wp:resourceURL />static/entando-misc-bootstrap/bootstrap/js/bootstrap-popover.js"></script>


  </body>
</html>