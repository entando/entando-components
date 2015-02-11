<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpmpp" uri="/jpmyportalplus-core" %>
<!DOCTYPE html>
<html lang="<wp:info key="currentLang" />">
<head>
	<meta charset="utf-8" />
	<title>
		<wp:currentPage param="title" /> - <wp:i18n key="PORTAL_TITLE" />
	</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="description" content="" />
	<meta name="author" content="" />

	<link rel="icon" href="<wp:info key="systemParam" paramName="applicationBaseURL" />favicon.png" type="image/png" />

	<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
		<script src="<wp:resourceURL />static/js/entando-misc-html5-essentials/html5shiv.js"></script>
  	<![endif]-->

	<jsp:include page="/WEB-INF/aps/jsp/models/inc/lesscss-active/lesscss.jsp" />
	<jsp:include page="/WEB-INF/aps/jsp/models/inc/models-common-utils.jsp" />


	<%-- jpmyportal plus - start --%>
  	<%-- enable jquery if not already included (usually it's included with lesscss.jsp or other utilities
		<script src="<wp:resourceURL />static/js/entando-misc-jquery/jquery-1.10.0.min.js"></script>
	--%>
	<script src="<wp:resourceURL />static/js/entando-misc-jquery-ui/jquery-ui-1.10.3.min.js"></script>
	<script src="<wp:resourceURL />plugins/jpmyportalplus/static/js/lib/jquery.browser.mobile.js"></script>
	<jsp:include page="/WEB-INF/plugins/jpmyportalplus/aps/jsp/models/inc/jpmyportalplus_javascript_variables.jsp" />
	<script src="<wp:resourceURL />plugins/jpmyportalplus/static/js/jpmyportalplus.js"></script>
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpmyportalplus/static/css/jpmyportalplus.css" media="screen" />
	<%-- jpmyportal plus - end --%>

</head>
<body>

	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</a>
				<a class="brand" href="#"><img src="<wp:imgURL />entando-logo.png" alt="Entando - Access. Build. Connect." /></a>
				<div class="nav-collapse collapse">
					<wp:show frame="0" />
					<wp:show frame="1" />
					<wp:show frame="2" />
					<wp:show frame="3" />
				</div><!-- /.nav-collapse -->
			</div>
		</div><!-- /navbar-inner -->
	</div>

	<div class="container">

		<div class="row">
			<div class="span12">
				<wp:show frame="4" />
			</div>
		</div>

		<div id="widget-columns-container" class="row-fluid"><%-- //IMPORTANT: the html id "columns" is referenced in /resources/plugins/jpmyportalplus/static/js/jpmyportalplus.js --%>
			<div id="widget-col1" class="widget-column span4"><%-- IMPORTANT: the html id "colonna1" is referenced in jpmyportalplus_javascript_variables.jsp --%>
				<%-- draggable showlets for column="1" here.... --%>
				<%-- First Column I --%> <wp:show frame="5" />
				<%-- First Column II --%> <wp:show frame="6" />
				<%-- First Column III --%> <wp:show frame="7" />
			</div>
			<div id="widget-col2" class="widget-column span4"><%--IMPORTANT: the html id "colonna2" is referenced in jpmyportalplus_javascript_variables.jsp --%>
				<%-- draggable showlets for column="2" here.... --%>
				<%-- Second Column I --%><wp:show frame="8" />
				<%-- Second Column II --%><wp:show frame="9" />
				<%-- Second Column III --%><wp:show frame="10" />
			</div>
			<div id="widget-col3" class="widget-column span4"><%-- IMPORTANT: the html id "colonna3" is referenced in jpmyportalplus_javascript_variables.jsp --%>
				<%-- draggable showlets for column="3" here.... --%>
				<%-- Third Column I --%><wp:show frame="11" />
				<%-- Third Column II --%><wp:show frame="12" />
				<%-- Third Column III --%><wp:show frame="13" />
			</div>
		</div>

		<div id="configure-page" class="margin-medium-vertical">
			<a class="btn btn-info" href="#editshowletlist" id="editshowlet_title"><i class="icon-cog icon-white"></i>&#32;<wp:i18n key="JPMYPORTALPLUS_CONFIGMYHOME" /></a>
		</div>

		<%-- Remember to include the Page Configuration Block --%>
		<jsp:include page="/WEB-INF/plugins/jpmyportalplus/aps/jsp/models/inc/page_configuration.jsp" />

	</div> <!-- /container -->

	<footer class="padding-medium-top">

		<div class="container">
			<div class="row margin-medium-bottom">
				<div class="span12">
					<wp:show frame="14" />
					<wp:show frame="15" />
				</div>
			</div>
			<div class="row margin-medium-bottom">
				<div class="span4">
					<wp:show frame="16" />
				</div>
				<div class="span4">
					<wp:show frame="17" />
				</div>
				<div class="span4">
					<wp:show frame="18" />
				</div>
			</div>
			<div class="row">
				<p class="span12 text-center margin-medium-top"><wp:i18n key="COPYRIGHT" escapeXml="false" /> - Powered by <a href="http://www.entando.com/">Entando - Access. Build. Connect.</a></p>
			</div>
		</div>
	</footer>

</body>
</html>