INSERT INTO pagemodels (code, descr, frames, plugincode, templategui) VALUES ('entando-page-bootstrap-hero', 'Bootstrap - Hero Unit', '<frames>
	<frame pos="0">
		<descr>Navbar 1</descr>
		<defaultWidget code="entando-widget-language_choose" />
	</frame>
	<frame pos="1">
		<descr>Navbar 2</descr>
		<defaultWidget code="entando-widget-navigation_bar">
			<properties>
				<property key="navSpec">code(homepage)</property>
			</properties>
		</defaultWidget>
	</frame>
	<frame pos="2">
		<descr>Navbar 3</descr>
		<defaultWidget code="entando-widget-search_form" />
	</frame>
	<frame pos="3">
		<descr>Navbar 4</descr>
		<defaultWidget code="entando-widget-login_form" />
	</frame>
	<frame pos="4">
		<descr>Toolbar 1</descr>
	</frame>
	<frame pos="5">
		<descr>Hero Unit</descr>
	</frame>
	<frame pos="6">
		<descr>Toolbar 2</descr>
	</frame>
	<frame pos="7" main="true">
		<descr>Top Story</descr>
	</frame>
	<frame pos="8">
		<descr>Box 1</descr>
	</frame>
	<frame pos="9">
		<descr>Box 2</descr>
	</frame>
	<frame pos="10">
		<descr>Box 3</descr>
	</frame>
	<frame pos="11">
		<descr>Side 1</descr>
	</frame>
	<frame pos="12">
		<descr>Side 2</descr>
	</frame>
	<frame pos="13">
		<descr>Side 3</descr>
	</frame>
	<frame pos="14">
		<descr>Side 4</descr>
	</frame>
	<frame pos="15">
		<descr>Content 1</descr>
	</frame>
	<frame pos="16">
		<descr>Content 2</descr>
	</frame>
	<frame pos="17">
		<descr>Content 3</descr>
	</frame>
	<frame pos="18">
		<descr>Content 4</descr>
	</frame>
	<frame pos="19">
		<descr>Content 5</descr>
	</frame>
	<frame pos="20">
		<descr>Side 5</descr>
	</frame>
	<frame pos="21">
		<descr>Side 6</descr>
	</frame>
	<frame pos="22">
		<descr>Side 7</descr>
	</frame>
	<frame pos="23">
		<descr>Side 8</descr>
	</frame>
	<frame pos="24">
		<descr>Footer 1</descr>
	</frame>
	<frame pos="25">
		<descr>Footer 2</descr>
	</frame>
	<frame pos="26">
		<descr>Footer 3</descr>
	</frame>
	<frame pos="27">
		<descr>Footer 4</descr>
	</frame>
	<frame pos="28">
		<descr>Footer 5</descr>
	</frame>
</frames>', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>
            <@wp.currentPage param="title" /> - <@wp.i18n key="PORTAL_TITLE" />
        </title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <link rel="icon" href="<@wp.info key="systemParam" paramName="applicationBaseURL" />favicon.png" type="image/png" />
              <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
              <!--[if lt IE 9]>
                      <script src="<@wp.resourceURL />static/js/entando-misc-html5-essentials/html5shiv.js"></script>
              <![endif]-->
              <@c.import url="/WEB-INF/aps/jsp/models/inc/lesscss-active/lesscss.jsp" />
              <@c.import url="/WEB-INF/aps/jsp/models/inc/models-common-utils.jsp" />
	      <@c.import url="/WEB-INF/aps/jsp/models/inc/content_inline_editing.jsp" />
	      <@c.import url="/WEB-INF/aps/jsp/models/inc/header-inclusions.jsp" />
    </head>
    <body>
        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a class="brand" href="#"><img src="<@wp.imgURL />entando-logo.png" alt="Entando - Access. Build. Connect." /></a>
                    <div class="nav-collapse collapse">
                        <@wp.show frame=0 />
                        <@wp.show frame=1 />
                        <@wp.show frame=2 />
                        <@wp.show frame=3 />
                    </div><!-- /.nav-collapse -->
                </div>
            </div><!-- /navbar-inner -->
        </div>
        <div class="container">
            <div class="row">
                <div class="span12">
                    <@wp.show frame=4 />
                </div>
            </div>
            <div class="row">
                <@wp.show frame=5 />
            </div>
            <div class="row">
                <div class="span12">
                    <@wp.show frame=6 />
                </div>
            </div>
            <div class="row">
                <div class="span12">
                    <@wp.show frame=7 />
                </div>
            </div>
            <div class="row">
                <div class="span4">
                    <@wp.show frame=8 />
                </div>
                <div class="span4">
                    <@wp.show frame=9 />
                </div>
                <div class="span4">
                    <@wp.show frame=10 />
                </div>
            </div>
            <div class="row">
                <div class="span6">
                    <@wp.show frame=11 />
                </div>
                <div class="span6">
                    <@wp.show frame=12 />
                </div>
            </div>
            <div class="row">
                <div class="span6">
                    <@wp.show frame=13 />
                </div>
                <div class="span6">
                    <@wp.show frame=14 />
                </div>
            </div>
            <div class="row">
                <div class="span12">
                    <@wp.show frame=15 />
                </div>
            </div>
            <div class="row">
                <div class="span12">
                    <@wp.show frame=16 />
                </div>
            </div>
            <div class="row">
                <div class="span12">
                    <@wp.show frame=17 />
                </div>
            </div>
            <div class="row">
                <div class="span12">
                    <@wp.show frame=18 />
                </div>
            </div>
            <div class="row">
                <div class="span12">
                    <@wp.show frame=19 />
                </div>
            </div>
            <div class="row">
                <div class="span6">
                    <@wp.show frame=20 />
                </div>
                <div class="span6">
                    <@wp.show frame=21 />
                </div>
            </div>
            <div class="row">
                <div class="span6">
                    <@wp.show frame=22 />
                </div>
                <div class="span6">
                    <@wp.show frame=23 />
                </div>
            </div>
        </div>
        <footer class="padding-medium-top">
            <div class="container">
                <div class="row margin-medium-bottom">
                    <div class="span12">
                        <@wp.show frame=24 />
                        <@wp.show frame=25 />
                    </div>
                </div>
                <div class="row margin-medium-bottom">
                    <div class="span4">
                        <@wp.show frame=26 />
                    </div>
                    <div class="span4">
                        <@wp.show frame=27 />
                    </div>
                    <div class="span4">
                        <@wp.show frame=28 />
                    </div>
                </div>
                <div class="row">
                    <p class="span12 text-center margin-medium-top"><@wp.i18n key="COPYRIGHT" escapeXml=false /> - Powered by <a href="http://www.entando.com/">Entando - Access. Build. Connect.</a></p>
                </div>
            </div>
        </footer>
    </body>
</html>');
