<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
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

        <jsp:include page="inc/content_inline_editing.jsp" />
        <jsp:include page="inc/header-inclusions.jsp" />
    </head>
	<body class="pace-done">
	    <div class="pace  pace-inactive">
		<div class="pace-progress" data-progress-text="100%" data-progress="99" style="transform: translate3d(100%, 0px, 0px);">
	            <div class="pace-progress-inner"></div>
	        </div>
		<div class="pace-activity"></div>
	    </div>
	    <div id="wrapper">
	        <nav class="navbar-default navbar-static-side" role="navigation">
	            <div class="sidebar-collapse">
	                <ul class="nav metismenu" id="side-menu">
	                    <li class="nav-header">
	                        <div class="dropdown profile-element"> 
	                            <c:choose>
                                    <c:when test="${sessionScope.currentUser != 'guest'}">
                                        <span>
                                            <img alt="image" class="" src="<wp:imgURL />entando-logo.png">
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>
                                            <img alt="image" class="" src="<wp:imgURL />entando-logo-1.png">
                                        </span>
                                    </c:otherwise>
                                </c:choose>
	                            <br>
	                            <wp:show frame="0" />
	                        </div>
	                        <div class="logo-element">
	                            E
	                        </div>
	                    </li>
	
	                    <wp:show frame="6" />
	                    <wp:show frame="10" />
	                    <wp:show frame="14" />
	                    <wp:show frame="16" />
	                    <wp:show frame="18" />
	                </ul>
	            </div>
	        </nav>
	        <div id="page-wrapper" class="gray-bg dashbard-1">
	            <div class="row border-bottom">
	                <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
	                    <div class="navbar-header">
	                        <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#">
	                            <i class="fa fa-bars"></i>
	                        </a>
	                    </div>
	                    <ul class="nav navbar-top-links navbar-right">
	                        <li class="dropdown">
	                            <wp:show frame="1" />
	                            <wp:show frame="2" />
	                            <wp:show frame="3" />
	                            <wp:show frame="4" />
	                    </ul>
	                </nav>
	            </div>
	            <div class="row white-bg" style="padding-bottom:10px; border-bottom:2px solid #e7eaec;">
	                <wp:show frame="5" />
	            </div>
	            <div style="padding-top:20px;">
	                <div class="col-md-4">
	                    <div class="white-bg">
	                        <wp:show frame="7" />
	                    </div>
	                </div>
	                <div class="col-md-4">
	                    <div class="white-bg">
	                        <wp:show frame="8" />
	                    </div>
	                </div>
	                <div class="col-md-4">
	                    <div class="white-bg">
	                        <wp:show frame="9" />
	                    </div>
	                </div>
	            </div>
	            <div style="padding-top:20px;">
	                <div class="col-md-4">
	                    <div class="white-bg">
	                        <wp:show frame="11" />
	                    </div>
	                </div>
	                <div class="col-md-4">
	                    <div class="white-bg">
	                        <wp:show  frame="12" />
	                    </div>
	                </div>
	                <div class="col-md-4">
	                    <div class="white-bg">
	                        <wp:show  frame="13" />
	                    </div>
	                </div>
	            </div>
	            <div class="row">
	                <div class="col-lg-12">
	                    <div class="wrapper wrapper-content">
	                        <div class="row">
	                            <div class="col-lg-12">
	                                <wp:show frame="15" />
	                            </div>
	                        </div>
	                        <div class="row">
	                            <div class="col-lg-12">
	                                <wp:show  frame="17"/>
	                            </div>
	                        </div>
	                        <div class="row">
	                            <div class="col-lg-4">
	                                <div class="white-bg">
	                                    <wp:show  frame="19" />
	                                </div>
	                                <div class="white-bg">
	                                    <wp:show frame="22"/>
	                                </div>
	                            </div>
	                            <div class="col-lg-4">
	                                <div class="white-bg">
	                                    <wp:show frame="20" />
	                                </div>
	                                <div class="white-bg">
	                                    <wp:show  frame="23" />
	                                </div>
	                            </div>
	                            <div class="col-lg-4">
	                                <div class="white-bg">
	                                    <wp:show  frame="21" />
	                                </div>
	                                <div class="white-bg">
	                                    <wp:show frame="24" />
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="footer">
	                        <wp:show frame="25" />
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div> 
	</body>
</html>
