<%@ taglib prefix="wp" uri="/aps-core" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<title>
		<wp:currentPage param="title" /> - My Portal
	</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="description" content="" />
	<meta name="author" content="" />

		<link rel="icon" href="<wp:info key="systemParam" paramName="applicationBaseURL" />favicon.png" type="image/png" />

		<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
		<!--[if lt IE 9]>
			<script src="<wp:resourceURL />static/js/entando-misc-html5-essentials/html5shiv.js"></script>
		<![endif]-->

		<jsp:include page="inc/lesscss-active/lesscss.jsp" />
		<jsp:include page="inc/models-common-utils.jsp" />

	</head>
<body>
<header>
	<h1>Your Title</h1>
</header>
<section>
	<wp:show frame="0" />
</section>
<footer>
	<p>Your footer</p>
</footer>
</body>
</html>

