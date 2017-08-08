INSERT INTO pagemodels (code, descr, frames, plugincode, templategui) VALUES ('entando-page-inspinia', 'Inspinia - BPM layout', '<frames>
	<frame pos="0">
		<descr>Sidebar 1</descr>
		<sketch x1="0" x2="1" y1="0" y2="0" />
	</frame>
	<frame pos="1">
		<descr>Header</descr>
		<sketch x1="2" x2="11" y1="0" y2="0" />
	</frame>
	<frame pos="2">
		<descr>Sidebar 2</descr>
		<sketch x1="0" x2="1" y1="1" y2="1" />
	</frame>
	<frame pos="3">
		<descr>Content Left 1</descr>
		<sketch x1="2" x2="4" y1="1" y2="1" />
	</frame>
	<frame pos="4">
		<descr>Content Central 1</descr>
		<sketch x1="5" x2="8" y1="1" y2="1" />
	</frame>
	<frame pos="5">
		<descr>Content Right 1</descr>
		<sketch x1="9" x2="11" y1="1" y2="1" />
	</frame>
	<frame pos="6">
		<descr>Sidebar 3</descr>
		<sketch x1="0" x2="1" y1="2" y2="2" />
	</frame>
	<frame pos="7">
		<descr>Content Left 2</descr>
		<sketch x1="2" x2="4" y1="2" y2="2" />
	</frame>
	<frame pos="8">
		<descr>Content Central 2</descr>
		<sketch x1="5" x2="8" y1="2" y2="2" />
	</frame>
	<frame pos="9">
		<descr>Content Right 2</descr>
		<sketch x1="9" x2="11" y1="2" y2="2" />
	</frame>
	<frame pos="10">
		<descr>Sidebar 4</descr>
		<sketch x1="0" x2="1" y1="3" y2="3" />
	</frame>
	<frame pos="11">
		<descr>Content Full 1</descr>
		<sketch x1="2" x2="11" y1="3" y2="3" />
	</frame>
	<frame pos="12">
		<descr>Sidebar 5</descr>
		<sketch x1="0" x2="1" y1="4" y2="4" />
	</frame>
	<frame pos="13">
		<descr>Content Full 2</descr>
		<sketch x1="2" x2="11" y1="4" y2="4" />
	</frame>	
	<frame pos="13">
		<descr>Sidebar 6</descr>
		<sketch x1="0" x2="1" y1="5" y2="5" />
	</frame>
	<frame pos="14">
		<descr>Content Left 3</descr>
		<sketch x1="2" x2="4" y1="5" y2="5" />
	</frame>
	<frame pos="15">
		<descr>Content Central 3</descr>
		<sketch x1="5" x2="8" y1="5" y2="5" />
	</frame>
	<frame pos="16">
		<descr>Content Right 3</descr>
		<sketch x1="9" x2="11" y1="5" y2="5" />
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
