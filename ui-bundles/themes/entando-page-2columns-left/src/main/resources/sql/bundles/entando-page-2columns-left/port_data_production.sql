INSERT INTO pagemodels (code, descr, frames, plugincode, templategui) VALUES ('entando-page-2columns-left', '2 Columns - Left', '<?xml version="1.0" encoding="UTF-8"?>
<frames>
    <frame pos="0">
        <descr>Navbar 1</descr>
        <sketch x1="0" y1="0" x2="2" y2="0" />
        <defaultWidget code="entando-widget-language_choose" />
    </frame>
    <frame pos="1">
        <descr>Navbar 2</descr>
        <sketch x1="3" y1="0" x2="5" y2="0" />
        <defaultWidget code="entando-widget-navigation_bar">
            <properties>
                <property key="navSpec">code(homepage)</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="2">
        <descr>Navbar 3</descr>
        <sketch x1="6" y1="0" x2="8" y2="0" />
        <defaultWidget code="entando-widget-search_form" />
    </frame>
    <frame pos="3">
        <descr>Navbar 4</descr>
        <sketch x1="9" y1="0" x2="11" y2="0" />
        <defaultWidget code="entando-widget-login_form" />
    </frame>
    <frame pos="4">
        <descr>Toolbar 1</descr>
        <sketch x1="0" y1="1" x2="11" y2="1" />
        <defaultWidget code="entando-widget-navigation_breadcrumbs" />
    </frame>
    <frame pos="5">
        <descr>Left 1</descr>
        <sketch x1="0" y1="2" x2="2" y2="2" />
        <defaultWidget code="entando-widget-navigation_menu">
            <properties>
                <property key="navSpec">code(homepage).subtree(1)</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="6">
        <descr>Left 2</descr>
        <sketch x1="0" y1="3" x2="2" y2="3" />
    </frame>
    <frame pos="7">
        <descr>Left 3</descr>
        <sketch x1="0" y1="4" x2="2" y2="4" />
    </frame>
    <frame pos="8">
        <descr>Left 4</descr>
        <sketch x1="0" y1="5" x2="2" y2="5" />
    </frame>
    <frame pos="9">
        <descr>Left 5</descr>
        <sketch x1="0" y1="6" x2="2" y2="6" />
    </frame>
    <frame pos="10">
        <descr>Toolbar 2</descr>
        <sketch x1="3" y1="2" x2="11" y2="2" />
    </frame>
    <frame pos="11" main="true">
        <descr>Top Story</descr>
        <sketch x1="3" y1="3" x2="11" y2="3" />
    </frame>
    <frame pos="12">
        <descr>Box 1</descr>
        <sketch x1="3" y1="4" x2="5" y2="4" />
    </frame>
    <frame pos="13">
        <descr>Box 2</descr>
        <sketch x1="6" y1="4" x2="8" y2="4" />
    </frame>
    <frame pos="14">
        <descr>Box 3</descr>
        <sketch x1="9" y1="4" x2="11" y2="4" />
    </frame>
    <frame pos="15">
        <descr>Side 1</descr>
        <sketch x1="3" y1="5" x2="6" y2="5" />
    </frame>
    <frame pos="16">
        <descr>Side 2</descr>
        <sketch x1="8" y1="5" x2="11" y2="5" />
    </frame>
    <frame pos="17">
        <descr>Side 3</descr>
        <sketch x1="3" y1="6" x2="6" y2="6" />
    </frame>
    <frame pos="18">
        <descr>Side 4</descr>
        <sketch x1="8" y1="6" x2="11" y2="6" />
    </frame>
    <frame pos="19">
        <descr>Content 1</descr>
        <sketch x1="3" y1="7" x2="11" y2="7" />
    </frame>
    <frame pos="20">
        <descr>Content 2</descr>
        <sketch x1="3" y1="8" x2="11" y2="8" />
    </frame>
    <frame pos="21">
        <descr>Content 3</descr>
        <sketch x1="3" y1="9" x2="11" y2="9" />
    </frame>
    <frame pos="22">
        <descr>Content 4</descr>
        <sketch x1="3" y1="10" x2="11" y2="10" />
    </frame>
    <frame pos="23">
        <descr>Content 5</descr>
        <sketch x1="3" y1="11" x2="11" y2="11" />
    </frame>
    <frame pos="24">
        <descr>Side 5</descr>
        <sketch x1="3" y1="12" x2="6" y2="12" />
    </frame>
    <frame pos="25">
        <descr>Side 6</descr>
        <sketch x1="8" y1="12" x2="11" y2="12" />
    </frame>
    <frame pos="26">
        <descr>Side 7</descr>
        <sketch x1="3" y1="13" x2="6" y2="13" />
    </frame>
    <frame pos="27">
        <descr>Side 8</descr>
        <sketch x1="8" y1="13" x2="11" y2="13" />
    </frame>
    <frame pos="28">
        <descr>Footer 1</descr>
        <sketch x1="0" y1="14" x2="11" y2="14" />
    </frame>
    <frame pos="29">
        <descr>Footer 2</descr>
        <sketch x1="0" y1="15" x2="11" y2="15" />
    </frame>
    <frame pos="30">
        <descr>Footer 3</descr>
        <sketch x1="0" y1="16" x2="3" y2="16" />
    </frame>
    <frame pos="31">
        <descr>Footer 4</descr>
        <sketch x1="4" y1="16" x2="7" y2="16" />
    </frame>
    <frame pos="32">
        <descr>Footer 5</descr>
        <sketch x1="8" y1="16" x2="11" y2="16" />
    </frame>
</frames>', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
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
        
        <@wp.fragment code="models-lesscss-active" escapeXml=false />
        <@wp.fragment code="models-common-utils" escapeXml=false />
        <@c.import url="/WEB-INF/aps/jsp/models/inc/content_inline_editing.jsp" />
                                                                              
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
            <div class="span3">
                <@wp.show frame=5 />
                <@wp.show frame=6 />
                <@wp.show frame=7 />
                <@wp.show frame=8 />
                <@wp.show frame=9 />
            </div>
            <div class="span9">

                <div class="row">
                    <div class="span9">
                        <@wp.show frame=10 />
                    </div>
                </div>

                <div class="row">
                    <div class="span9">
                        <@wp.show frame=11 />
                    </div>
                </div>
                <div class="row">
                    <div class="span3">
                        <@wp.show frame=12 />
                    </div>
                    <div class="span3">
                        <@wp.show frame=13 />
                    </div>
                    <div class="span3">
                        <@wp.show frame=14 />
                    </div>
                </div>
                <div class="row">
                    <div class="span4">
                        <@wp.show frame=15 />
                    </div>
                    <div class="span4 offset1">
                        <@wp.show frame=16 />
                    </div>
                </div>
                <div class="row">
                    <div class="span4">
                        <@wp.show frame=17 />
                    </div>
                    <div class="span4 offset1">
                        <@wp.show frame=18 />
                    </div>
                </div>
                <div class="row">
                    <div class="span9">
                        <@wp.show frame=19 />
                    </div>
                </div>
                <div class="row">
                    <div class="span9">
                        <@wp.show frame=20 />
                    </div>
                </div>
                <div class="row">
                    <div class="span9">
                        <@wp.show frame=21 />
                    </div>
                </div>
                <div class="row">
                    <div class="span9">
                        <@wp.show frame=22 />
                    </div>
                </div>
                <div class="row">
                    <div class="span9">
                        <@wp.show frame=23 />
                    </div>
                </div>
                <div class="row">
                    <div class="span4">
                        <@wp.show frame=24 />
                    </div>
                    <div class="span4 offset1">
                        <@wp.show frame=25 />
                    </div>
                </div>
                <div class="row">
                    <div class="span4">
                        <@wp.show frame=26 />
                    </div>
                    <div class="span4 offset1">
                        <@wp.show frame=27 />
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- /container -->

    <footer class="padding-medium-top">
        <div class="container">
            <div class="row margin-medium-bottom">
                <div class="span12">
                    <@wp.show frame=28 />
                    <@wp.show frame=29 />
                </div>
            </div>
            <div class="row margin-medium-bottom">
                <div class="span4">
                    <@wp.show frame=30 />
                </div>
                <div class="span4">
                    <@wp.show frame=31 />
                </div>
                <div class="span4">
                    <@wp.show frame=32 />
                </div>
            </div>
            <div class="row">
                <p class="span12 text-center margin-medium-top"><@wp.i18n key="COPYRIGHT" escapeXml=false /> - Powered by <a href="http://www.entando.com/">Entando - Access. Build. Connect.</a></p>
            </div>
        </div>
    </footer>

</body>
</html>');

INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('models-lesscss-active',NULL,NULL,NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<link rel="stylesheet/less" href="<@wp.resourceURL />static/entando-misc-bootstrap/bootstrap/less/bootstrap.less" />
<link rel="stylesheet/less" href="<@wp.resourceURL />static/entando-misc-bootstrap/bootstrap/less/responsive.less" />
<link rel="stylesheet/less" href="<@wp.resourceURL />static/less/portalexample.less" />
<script src="<@wp.resourceURL />static/js/entando-misc-less/less-1.3.1.min.js"></script>
',1);

INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('models-common-utils',NULL,NULL,NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<#-- css -->
<@wp.outputHeadInfo type="CSS">
    <link rel="stylesheet" type="text/css" href="<@wp.cssURL /><@wp.printHeadInfo />" />
</@wp.outputHeadInfo>

<#-- css -->
<@wp.outputHeadInfo type="CSS_EXT">
    <link rel="stylesheet" type="text/css" href="<@wp.printHeadInfo />" />
</@wp.outputHeadInfo>

<#-- css for ie7 -->
<@wp.outputHeadInfo type="CSS_IE7">
    <!--[if IE 7]>
        <link rel="stylesheet" type="text/css" href="<@wp.cssURL /><@wp.printHeadInfo />" />
    <![endif]-->
</@wp.outputHeadInfo>

<#-- css for ie8 -->
<@wp.outputHeadInfo type="CSS_IE8">
    <!--[if IE 8]>
        <link rel="stylesheet" type="text/css" href="<@wp.cssURL /><@wp.printHeadInfo />" />
    <![endif]-->
</@wp.outputHeadInfo>

<#-- js global vars -->
<@wp.outputHeadInfo type="JS_VARS">
    <script>
    <!--//--><![CDATA[//><!--
        <@wp.printHeadInfo />
    //--><!]]>
    </script>
</@wp.outputHeadInfo>

<#-- js scripts (remember to include the libraries first) -->
<@wp.outputHeadInfo type="JS">
    <script src="<@wp.resourceURL />static/js/<@wp.printHeadInfo />"></script>
</@wp.outputHeadInfo>

<#-- external/CDN js scripts (remember to include the libraries first) -->
<@wp.outputHeadInfo type="JS_EXT">
    <script src="<@wp.printHeadInfo />"></script>
</@wp.outputHeadInfo>

<#-- js code -->
<@wp.outputHeadInfo type="JS_RAW">
    <script>
    <!--//--><![CDATA[//><!--
        <@wp.printHeadInfo />
    //--><!]]>
    </script>
</@wp.outputHeadInfo>',1);

