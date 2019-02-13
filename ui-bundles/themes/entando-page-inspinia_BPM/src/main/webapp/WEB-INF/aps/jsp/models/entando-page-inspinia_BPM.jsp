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
        <link 
            rel="icon"
            href="<wp:info key="systemParam" paramName="applicationBaseURL" />
            favicon.png" 
            type="image/png"
            />
    <c:import url="/WEB-INF/aps/jsp/models/inc/content_inline_editing.jsp" />
    <c:import url="/WEB-INF/aps/jsp/models/inc/header-inclusions.jsp" />
</head>
<body class="bpm-inspinia">
    <div class="pace pace-inactive">
        <div class="pace-progress" data-progress-text="100%" data-progress="99" style="transform: translate3d(100%, 0px, 0px);">
            <div class="pace-progress-inner"></div>
        </div>
        <div class="pace-activity"></div>
    </div>
    <header class="header-fixed">
        <div class="header-limiter">
            <h1>
                <a href="#">Entando Case Management Widgets<span></span></a>
            </h1>
            <span class=" text-right user-logged">Entando Admin</span>
        </div>
    </header>

    <div  class="container-bpm" style="margin:10px;">

        <div class="row ">
            <div class="col-md-12">
                <wp:show frame="0" />
            </div>
        </div>

        <div class="row ">
            <div class="col-md-12">
                <wp:show frame="1"/>
            </div>
        </div>

        <div class="row ">
            <div class="col-md-12">
                <wp:show frame="2" />
            </div>
        </div>

        <div class="row ">
            <div class="col-md-6">
                <wp:show frame="3" />
            </div>
            <div class="col-md-6">
                <wp:show frame="4" />
            </div>
        </div>


        <div class="row ">
            <div class="col-md-12">

                <wp:show frame="5" />
            </div>
        </div>

        <div class="row ">
            <div class="col-md-6">
                <wp:show frame="6" />
            </div>

            <div class="col-md-6">
                <wp:show frame="7" />
            </div>
        </div>

    </div>
</body>
</html>
