INSERT INTO pagemodels (code, descr, frames, plugincode,templategui) VALUES ('entando_page_home_government', 'Governament Template Homepage', '<?xml version="1.0" encoding="UTF-8"?>
<frames>
    <frame pos="0">
        <descr>Login form</descr>
        <sketch x1="0" y1="0" x2="11" y2="0" />
        <defaultWidget code="government_login_form" />
    </frame>
    <frame pos="1">
        <descr>Change language</descr>
        <sketch x1="0" y1="1" x2="3" y2="1" />
        <defaultWidget code="government_widget_language_choose" />
    </frame>
    <frame pos="2">
        <descr>Burger menu</descr>
        <sketch x1="4" y1="1" x2="7" y2="1" />
        <defaultWidget code="government_widget_burger_menu" />
    </frame>
    <frame pos="3">
        <descr>Search Form</descr>
        <sketch x1="8" y1="1" x2="11" y2="1" />
        <defaultWidget code="government_widget_search_form" />
    </frame>
    <frame pos="4">
        <descr>Horizontal menu</descr>
        <sketch x1="0" y1="2" x2="11" y2="2" />
        <defaultWidget code="content_viewer">
            <properties>
                <property key="modelId">10011</property>
                <property key="contentId">CNG10</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="5">
        <descr>Highlight Content</descr>
        <sketch x1="0" y1="3" x2="11" y2="3" />
    </frame>
    <frame pos="6">
        <descr>List of News</descr>
        <sketch x1="0" y1="4" x2="11" y2="4" />
    </frame>
    <frame pos="7">
        <descr>List of Contents</descr>
        <sketch x1="0" y1="5" x2="11" y2="5" />
    </frame>
    <frame pos="8">
        <descr>Content 2col</descr>
        <sketch x1="0" y1="6" x2="2" y2="6" />
    </frame>
    <frame pos="9">
        <descr>Content 2col</descr>
        <sketch x1="3" y1="6" x2="5" y2="6" />
    </frame>
    <frame pos="10">
        <descr>Content 2col</descr>
        <sketch x1="6" y1="6" x2="8" y2="6" />
    </frame>
    <frame pos="11">
        <descr>Content 2col</descr>
        <sketch x1="9" y1="6" x2="11" y2="6" />
    </frame>
    <frame pos="12">
        <descr>Content 3col</descr>
        <sketch x1="0" y1="7" x2="3" y2="7" />
    </frame>
    <frame pos="13">
        <descr>Content 3col</descr>
        <sketch x1="4" y1="7" x2="7" y2="7" />
    </frame>
    <frame pos="14">
        <descr>Content 3col</descr>
        <sketch x1="8" y1="7" x2="11" y2="7" />
    </frame>
    <frame pos="15">
        <descr>Content 3col</descr>
        <sketch x1="0" y1="8" x2="3" y2="8" />
    </frame>
    <frame pos="16">
        <descr>Content 3col</descr>
        <sketch x1="4" y1="8" x2="7" y2="8" />
    </frame>
    <frame pos="17">
        <descr>Content 3col</descr>
        <sketch x1="8" y1="8" x2="11" y2="8" />
    </frame>
    <frame pos="18">
        <descr>Footer</descr>
        <sketch x1="0" y1="9" x2="3" y2="9" />
        <defaultWidget code="content_viewer">
            <properties>
                <property key="modelId">10001</property>
                <property key="contentId">CNG11</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="19">
        <descr>Footer</descr>
        <sketch x1="4" y1="9" x2="7" y2="9" />
        <defaultWidget code="content_viewer">
            <properties>
                <property key="modelId">10001</property>
                <property key="contentId">CNG12</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="20">
        <descr>Footer</descr>
        <sketch x1="8" y1="9" x2="11" y2="9" />
    </frame>
    <frame pos="21">
        <descr>Footer</descr>
        <sketch x1="0" y1="10" x2="11" y2="10" />
    </frame>
</frames>', NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>

<!doctype html>
    <!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
    <!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
    <!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
    <!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>
        <base href="<@wp.info key="systemParam" paramName="applicationBaseURL" />">
        <!--[if IE]><script type="text/javascript">
            (function() {
                var baseTag = document.getElementsByTagName(''base'')[0];
                baseTag.href = baseTag.href;
            })();
        </script>
        <![endif]-->
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title><@wp.i18n key="PORTAL_TITLE" /> - <@wp.currentPage param="title" /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta id="viewport" name="viewport" content="initial-scale=1.0, width=device-width"/>
        <meta name="description" content="<@wp.i18n key="PORTAL_DESCRIPTION" />">

        <link rel="icon" type="image/png" href="<@wp.resourceURL />favicon.png" />
        <link rel="apple-touch-icon" sizes="120x120" href="<@wp.imgURL/>favicon_120.png" />

        <link href="//fonts.googleapis.com/css?family=Titillium+Web:300,300italic,600,600italic,700,700italic" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="<@wp.cssURL/>bootstrap.min.css" />
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" />
        <link href="<@wp.cssURL/>government.css" rel="stylesheet" type="text/css" />
        <link href="<@wp.cssURL/>government-print.css" rel="stylesheet" type="text/css" />
        <@wp.outputHeadInfo type="CSS_GOVERNMENT">
        <link href="<@wp.cssURL/><@wp.printHeadInfo />" rel="stylesheet" type="text/css" />
        </@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="CSS_GOVERNMENT_EXT">
        <link href="<@wp.printHeadInfo />" rel="stylesheet" type="text/css" />
        </@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="CSS_EXT">
        <link href="<@wp.printHeadInfo />" rel="stylesheet" type="text/css" />
        </@wp.outputHeadInfo>
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script>!window.jQuery && document.write(''<script src="<@wp.resourceURL />static/js/jquery-3.1.1.min.js"><\/script>'')</script>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_TOP_EXT"><script src="<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_TOP"><script src="<@wp.resourceURL />static/js/<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body class="push-body">
        <div class="body_wrapper push_container clearfix" id="page_top">
            <!--[if lt IE 8]>
            <p class="browserupgrade"><@wp.i18n key="PORTAL_BROWSERUPDATE" escapeXml=false /></p>
            <![endif]-->
            <div class="skiplink sr-only">
                <ul>
                    <li><a accesskey="2" href="<@wp.url />#main_container"><@wp.i18n key="PORTAL_SKIPLINK_GOCONTENT" /></a></li>
                    <li><a accesskey="3" href="<@wp.url />#menup"><@wp.i18n key="PORTAL_SKIPLINK_GONAVIGATION" /></a></li>
                    <li><a accesskey="4" href="<@wp.url />#footer"><@wp.i18n key="PORTAL_SKIPLINK_GOFOOTER" /></a></li>
                </ul>
            </div>
            <header id="mainheader" class="navbar-fixed-top bg-darkgrey container-fullwidth">
                <section class="preheader bg-darkblue">
                    <h3 class="sr-only"><@wp.i18n key="PORTAL_HEADER_PREHEADER_TITLE" /></h3>
                    <div class="container">
                        <div class="row clearfix">
                            <div class="col-lg-offset-10 col-lg-2 col-md-offset-10 col-md-2 col-sm-offset-9 col-sm-3 col-xs-offset-10 col-xs-2 reserved-area bg-white">
                                  <@wp.show frame=0 />
                            </div>
                        </div>
                    </div>
                </section>
                <!-- Button Menu -->
                <button class="navbar-toggle menu-btn pull-left menu-left push-body jPushMenuBtn">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar icon-bar1"></span>
                    <span class="icon-bar icon-bar2"></span>
                    <span class="icon-bar icon-bar3"></span>
                    <span class="titlemenu">Menu</span>
                </button>
                <!--End Button Menu -->

                <!-- Menu -->
                <nav class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-left" id="menup">
                    <div class="cbp-menu-wrapper clearfix">
                        <h3 class="sr-only"><@wp.i18n key="PORTAL_HEADER_MENU_TITLE" /></h3>
                        <ul class="list-inline languagemobile">
                            <@wp.show frame=1 />
                        </ul>
                        <ul class="list-inline socialmobile">
                            <li class="small"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL" /></li>
                            <li><a href="https://www.facebook.com/Entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />"><i class="fa fa-facebook" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" /></span></a></li>
                            <li><a href="https://twitter.com/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" />"><i class="fa fa-twitter" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" /></span></a></li>
                            <li><a href="https://it.linkedin.com/company/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" />"><i class="fa fa-linkedin" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" /></span></a></li>
                        </ul>
                        <@wp.show frame=2 />    
                    </div>
                </nav>
                <!-- End Menu -->
                <@wp.pageWithWidget var="searchResultPageVar" widgetTypeCode="search_result" listResult=false />
                <!-- headermenu -->
                <div class="container">
                    <div class="row clearfix">
                        <div class="col-lg-8 col-md-8 col-sm-9 col-xs-12 government">
                            <div class="logoimg">
                                <a href="<@wp.url page="homepage" />" title="<@wp.i18n key="PORTAL_DESCRIPTION" />">
                                    <img src="<@wp.imgURL/>logo-government.png" alt="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_IMGALT" />"/>
                                </a>
                            </div>
                            <!-- mobile search -->
                                <@wp.show frame=3 />
                            <!-- mobile search -->
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-3 col-xs-12 hidden-xs pull-right text-right">
                            <ul class="list-inline text-right language">
                                <@wp.show frame=1 />
                            </ul>
                            <ul class="list-inline text-right social">
                                <li class="small"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL" /></li>
                                <li><a href="https://www.facebook.com/Entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />"><i class="fa fa-facebook" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" /></span></a></li>
                                <li><a href="https://twitter.com/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" />"><i class="fa fa-twitter" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" /></span></a></li>
                                <li><a href="https://it.linkedin.com/company/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" />"><i class="fa fa-linkedin" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" /></span></a></li>
                            </ul>
                            <!-- search -->
                                <@wp.show frame=3 />
                            <!-- search -->
                        </div>
                    </div>
                </div>
                <!-- headermenu -->
                <!-- mobile search -->
                <div class="hidden-lg hidden-md collapse" id="searchMobile" aria-expanded="false" role="form">
                    <form action="<#if (searchResultPageVar??) ><@wp.url page="${searchResultPageVar.code}" /></#if>" method="post">
                        <div class="container">
                            <div class="row">
                                <label for="cm_searchmobile" class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" /></label>
                                <input type="text" class="form-control squared" placeholder="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" />" name="search" id="cm_searchmobile" />
                            </div>
                        </div>
                    </form>
                </div>      
                <!-- mobile search -->
            </header>
            <main id="main_container">
                <@wp.show frame=4 />
                <section class="content-evidence">
                    <@wp.show frame=5 />
                </section>
                <section class="list-news">
                    <@wp.show frame=6 />
                </section>
                <section class="list-contents">
                    <@wp.show frame=7 />
                </section>
                <section class="public-contents-2col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <@wp.show frame=8 />
                            </div>
                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <@wp.show frame=9 />
                            </div>
                        </div>
                    </div>
                </section>
                <section class="public-contents-2col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <@wp.show frame=10 />
                            </div>
                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <@wp.show frame=11 />
                            </div>
                        </div>
                    </div>
                </section>
                <section class="public-contents-3col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=12 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=13 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=14 />
                            </div>
                        </div>
                    </div>
                </section>
                <section class="public-contents-3col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=15 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=16 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=17 />
                            </div>
                        </div>
                    </div>
                </section>
                
            </main>
            <footer id="footer">
                <div class="container">
                    <section>
                        <div class="row clearfix">
                            <div class="col-xs-12 intestazione">
                                <div class="logoimg">
                                    <a href="<@wp.url page="homepage" />" title="<@wp.i18n key="PORTAL_DESCRIPTION" />">
                                        <img src="<@wp.imgURL/>logo-government.png" alt="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_IMGALT" />"/>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </section>
                    <div class="row">
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <@wp.show frame=18 />
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <@wp.show frame=19 />
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <div class="followus">
                            <h4><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL" /></h4>
                            <p>
                                <a href="https://www.facebook.com/Entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />"><i class="fa fa-facebook" aria-hidden="true"></i>
                                    <span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" /></a> 
                                <a href="https://twitter.com/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW"/>"><i class="fa fa-twitter" aria-hidden="true"></i>
                                    <span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" /></a>
                                <a href="https://twitter.com/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK"/>"><i class="fa fa-linkedin" aria-hidden="true"></i>
                                    <span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" /></a>
                            </p>
                        </div>
                            <@wp.show frame=20 />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <@wp.show frame=21 />
                        </div>
                    </div>
                    <section class="postFooter clearfix">
                        <h3 class="sr-only"><@wp.i18n key="PORTAL_FOOTER_LINKS_TITLE" /></h3>
                        <ul class="list-inline">
                            <li><a href="#" title="<@wp.i18n key="PORTAL_FOOTER_LINK_LEGAL_NOTICES" />"><@wp.i18n key="PORTAL_FOOTER_LINK_LEGAL_NOTICES" /></a></li>
                            <li><a href="#" title="<@wp.i18n key="PORTAL_FOOTER_LINK_PRIVACY" />"><@wp.i18n key="PORTAL_FOOTER_LINK_PRIVACY" /></a></li>
                        </ul>
                    <p class="pull-right copyright">
                    <@wp.i18n key="PORTAL_COPYRIGHT" escapeXml=false />
                    </p>    
                    </section>
                </div>
            </footer>
        </div>
        <div id="topcontrol" class="topcontrol bg-darkblu" title="<@wp.i18n key="PORTAL_BACKTOTOP" />">
            <i class="fa fa-angle-up" aria-hidden="true"></i>
        </div>
        <script src="<@wp.resourceURL />static/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="<@wp.resourceURL />static/js/government.js"></script>
        <script type="text/javascript" src="<@wp.resourceURL />static/js/jquery.cookiesdirective.js"></script>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_BTM_EXT"><script src="<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_BTM"><script src="<@wp.resourceURL />static/js/<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <script>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_BTM_INC">
            <@wp.printHeadInfo />
        </@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="JS_RAW">
            <@wp.printHeadInfo />
        </@wp.outputHeadInfo>

              (function(i,s,o,g,r,a,m){i[''GoogleAnalyticsObject'']=r;i[r]=i[r]||function(){
              (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
              m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
              })(window,document,''script'',''//www.google-analytics.com/analytics.js'',''ga'');

              ga(''create'', ''xxxxxx'', ''auto'');
              ga(''send'', ''pageview'');
        </script>
        <!-- Access reserved area -->
        <div class="modal fade" id="accessModal" tabindex="-1" role="dialog" aria-labelledby="modalaccess">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="modalaccess"><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN"/></h4>
                    </div>
                    <div class="modal-body">
                        <form action="<@wp.url/>" method="post">
                            <#if (accountExpired?? && accountExpired == true) >
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <@wp.i18n key="ESLF_USER_STATUS_EXPIRED" />
                            </div>
                            </#if>
                            <#if (wrongAccountCredential?? && wrongAccountCredential == true) >
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <@wp.i18n key="ESLF_USER_STATUS_CREDENTIALS_INVALID" />
                            </div>
                            </#if>
                            <#if (RequestParameters.returnUrl??) >
                            <input type="hidden" name="returnUrl" value="${RequestParameters.returnUrl}" />
                            </#if>
                            <div class="form-group">
                                <label for="username" class="control-label"><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_USER"/></label>
                                <input type="text" class="form-control" name="username" id="username" placeholder="<@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_USER"/>" />
                            </div>
                            <div class="form-group">
                                <label for="password_modal" class="control-label"><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_PASS"/></label>
                                <input type="password" class="form-control" name="password" id="password_modal" placeholder="<@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_PASS"/>" />
                            </div>
                            <div class="modal-footer">
                                <input type="submit" class="btn btn-primary bg-darkblue" value="<@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN" />" />
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>');

INSERT INTO pagemodels (code, descr, frames, plugincode,templategui) VALUES ('entando_page_2column_right_government',
'Governament Template - Internal Page 2 Columns',
'<?xml version="1.0" encoding="UTF-8"?>
<frames>
    <frame pos="0">
        <descr>Login form</descr>
        <sketch x1="0" y1="0" x2="11" y2="0" />
        <defaultWidget code="government_login_form" />
    </frame>
    <frame pos="1">
        <descr>Change language</descr>
        <sketch x1="0" y1="1" x2="3" y2="1" />
        <defaultWidget code="government_widget_language_choose" />
    </frame>
    <frame pos="2">
        <descr>Burger menu</descr>
        <sketch x1="4" y1="1" x2="7" y2="1" />
        <defaultWidget code="government_widget_burger_menu" />
    </frame>
    <frame pos="3">
        <descr>Search Form</descr>
        <sketch x1="8" y1="1" x2="11" y2="1" />
        <defaultWidget code="government_widget_search_form" />
    </frame>
    <frame pos="4">
        <descr>Horizontal menu</descr>
        <sketch x1="0" y1="2" x2="11" y2="2" />
        <defaultWidget code="content_viewer">
            <properties>
                <property key="contentId">CNG10</property>
                <property key="modelId">10011</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="5">
        <descr>Breadcrumbs</descr>
        <sketch x1="0" y1="3" x2="11" y2="3" />
        <defaultWidget code="government_widget_breadcrumbs" />
    </frame>
    <frame pos="6">
        <descr>Content 2col - large</descr>
        <sketch x1="0" y1="4" x2="6" y2="4" />
    </frame>
    <frame pos="7">
        <descr>Content 2col - small</descr>
        <sketch x1="7" y1="4" x2="11" y2="4" />
    </frame>
    <frame pos="8">
        <descr>Content 2col - large</descr>
        <sketch x1="0" y1="5" x2="6" y2="5" />
    </frame>
    <frame pos="9">
        <descr>Content 2col - small</descr>
        <sketch x1="7" y1="5" x2="11" y2="5" />
    </frame>
    <frame pos="10">
        <descr>Content 1col</descr>
        <sketch x1="0" y1="6" x2="11" y2="6" />
    </frame>
    <frame pos="11">
        <descr>Content 2col</descr>
        <sketch x1="0" y1="7" x2="5" y2="7" />
    </frame>
    <frame pos="12">
        <descr>Content 2col</descr>
        <sketch x1="6" y1="7" x2="11" y2="7" />
    </frame>
    <frame pos="13">
        <descr>Content 3col</descr>
        <sketch x1="0" y1="8" x2="3" y2="8" />
    </frame>
    <frame pos="14">
        <descr>Content 3col</descr>
        <sketch x1="4" y1="8" x2="7" y2="8" />
    </frame>
    <frame pos="15">
        <descr>Content 3col</descr>
        <sketch x1="8" y1="8" x2="11" y2="8" />
    </frame>
    <frame pos="16">
        <descr>Content 3col</descr>
        <sketch x1="0" y1="9" x2="3" y2="9" />
    </frame>
    <frame pos="17">
        <descr>Content 3col</descr>
        <sketch x1="4" y1="9" x2="7" y2="9" />
    </frame>
    <frame pos="18">
        <descr>Content 3col</descr>
        <sketch x1="8" y1="9" x2="11" y2="9" />
    </frame>
    <frame pos="19">
        <descr>Footer</descr>
        <sketch x1="0" y1="10" x2="3" y2="10" />
        <defaultWidget code="content_viewer">
            <properties>
                <property key="modelId">10001</property>
                <property key="contentId">CNG11</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="20">
        <descr>Footer</descr>
        <sketch x1="4" y1="10" x2="7" y2="10" />
        <defaultWidget code="content_viewer">
            <properties>
                <property key="modelId">10001</property>
                <property key="contentId">CNG12</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="21">
        <descr>Footer</descr>
        <sketch x1="8" y1="10" x2="11" y2="10" />
    </frame>
    <frame pos="22">
        <descr>Footer</descr>
        <sketch x1="0" y1="11" x2="11" y2="11" />
    </frame>
</frames>',NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>

<!doctype html>
    <!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
    <!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
    <!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
    <!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>
        <base href="<@wp.info key="systemParam" paramName="applicationBaseURL" />">
        <!--[if IE]><script type="text/javascript">
            (function() {
                var baseTag = document.getElementsByTagName(''base'')[0];
                baseTag.href = baseTag.href;
            })();
        </script>
        <![endif]-->
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title><@wp.i18n key="PORTAL_TITLE" /> - <@wp.currentPage param="title" /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta id="viewport" name="viewport" content="initial-scale=1.0, width=device-width"/>
        <meta name="description" content="<@wp.i18n key="PORTAL_DESCRIPTION" />">

        <link rel="icon" type="image/png" href="<@wp.resourceURL />favicon.png" />
        <link rel="apple-touch-icon" sizes="120x120" href="<@wp.imgURL/>favicon_120.png" />

        <link href="//fonts.googleapis.com/css?family=Titillium+Web:300,300italic,600,600italic,700,700italic" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="<@wp.cssURL/>bootstrap.min.css" />
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" />
        <link href="<@wp.cssURL/>government.css" rel="stylesheet" type="text/css" />
        <link href="<@wp.cssURL/>government-print.css" rel="stylesheet" type="text/css" />
        <@wp.outputHeadInfo type="CSS_GOVERNMENT">
        <link href="<@wp.cssURL/><@wp.printHeadInfo />" rel="stylesheet" type="text/css" />
        </@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="CSS_GOVERNMENT_EXT">
        <link href="<@wp.printHeadInfo />" rel="stylesheet" type="text/css" />
        </@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="CSS_EXT">
        <link href="<@wp.printHeadInfo />" rel="stylesheet" type="text/css" />
        </@wp.outputHeadInfo>
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script>!window.jQuery && document.write(''<script src="<@wp.resourceURL />static/js/jquery-3.1.1.min.js"><\/script>'')</script>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_TOP_EXT"><script src="<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_TOP"><script src="<@wp.resourceURL />static/js/<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body class="push-body int-pg">
        <div class="body_wrapper push_container clearfix" id="page_top">
            <!--[if lt IE 8]>
            <p class="browserupgrade"><@wp.i18n key="PORTAL_BROWSERUPDATE" escapeXml=false /></p>
            <![endif]-->
            <div class="skiplink sr-only">
                <ul>
                    <li><a accesskey="2" href="<@wp.url />#main_container"><@wp.i18n key="PORTAL_SKIPLINK_GOCONTENT" /></a></li>
                    <li><a accesskey="3" href="<@wp.url />#menup"><@wp.i18n key="PORTAL_SKIPLINK_GONAVIGATION" /></a></li>
                    <li><a accesskey="4" href="<@wp.url />#footer"><@wp.i18n key="PORTAL_SKIPLINK_GOFOOTER" /></a></li>
                </ul>
            </div>
            <header id="mainheader" class="navbar-fixed-top bg-darkgrey container-fullwidth">
                <section class="preheader bg-darkblue">
                    <h3 class="sr-only"><@wp.i18n key="PORTAL_HEADER_PREHEADER_TITLE" /></h3>
                    <div class="container">
                        <div class="row clearfix">
                            <div class="col-lg-offset-10 col-lg-2 col-md-offset-10 col-md-2 col-sm-offset-9 col-sm-3 col-xs-offset-10 col-xs-2 reserved-area bg-white">
                                  <@wp.show frame=0 />
                            </div>
                        </div>
                    </div>
                </section>
                <!-- Button Menu -->
                <button class="navbar-toggle menu-btn pull-left menu-left push-body jPushMenuBtn">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar icon-bar1"></span>
                    <span class="icon-bar icon-bar2"></span>
                    <span class="icon-bar icon-bar3"></span>
                    <span class="titlemenu">Menu</span>
                </button>
                <!--End Button Menu -->

                <!-- Menu -->
                <nav class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-left" id="menup">
                    <div class="cbp-menu-wrapper clearfix">
                        <h3 class="sr-only"><@wp.i18n key="PORTAL_HEADER_MENU_TITLE" /></h3>
                        <ul class="list-inline languagemobile">
                            <@wp.show frame=1 />
                        </ul>
                        <ul class="list-inline socialmobile">
                            <li class="small"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL" /></li>
                            <li><a href="https://www.facebook.com/Entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />"><i class="fa fa-facebook" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" /></span></a></li>
                            <li><a href="https://twitter.com/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" />"><i class="fa fa-twitter" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" /></span></a></li>
                            <li><a href="https://it.linkedin.com/company/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" />"><i class="fa fa-linkedin" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" /></span></a></li>
                        </ul>
                        <@wp.show frame=2 />
                    </div>
                </nav>
                <!-- End Menu -->
                <@wp.pageWithWidget var="searchResultPageVar" widgetTypeCode="search_result" listResult=false />
                <!-- headermenu -->
                <div class="container">
                    <div class="row clearfix">
                        <div class="col-lg-8 col-md-8 col-sm-9 col-xs-12 government">
                            <div class="logoimg">                               
                                <a href="<@wp.url page="homepage" />" title="<@wp.i18n key="PORTAL_DESCRIPTION" />">
                                    <img src="<@wp.imgURL/>logo-government.png" alt="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_IMGALT" />"/>
                                </a>
                            </div>
                            <!-- mobile search -->
                                <@wp.show frame=3 />
                            <!-- mobile search -->
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-3 col-xs-12 hidden-xs pull-right text-right">
                            <ul class="list-inline text-right language">
                                <@wp.show frame=1 />
                            </ul>
                            <ul class="list-inline text-right social">
                                <li class="small"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL" /></li>
                                <li><a href="https://www.facebook.com/Entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />"><i class="fa fa-facebook" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" /></span></a></li>
                                <li><a href="https://twitter.com/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" />"><i class="fa fa-twitter" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" /></span></a></li>
                                <li><a href="https://it.linkedin.com/company/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" />"><i class="fa fa-linkedin" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" /></span></a></li>
                            </ul>
                            <!-- search -->
                                <@wp.show frame=3 />
                            <!-- search -->
                        </div>
                    </div>
                </div>
                <!-- headermenu -->
                <!-- mobile search -->
                <div class="hidden-lg hidden-md collapse" id="searchMobile" aria-expanded="false" role="form">
                    <form action="<#if (searchResultPageVar??) ><@wp.url page="${searchResultPageVar.code}" /></#if>" method="post">
                        <div class="container">
                            <div class="row">
                                <label for="cm_searchmobile" class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" /></label>
                                <input type="text" class="form-control squared" placeholder="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" />" name="search" id="cm_searchmobile" />
                            </div>
                        </div>
                    </form>
                </div>      
                <!-- mobile search -->
                <@wp.show frame=4 />
            </header>
            <main id="main_container">
                <section class="breadcrumb bg-darkblue">
                    <@wp.show frame=5 />
                </section>
                <section class="public-contents-8-4col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-7 col-md-7 col-sm-12 col-xs-12">
                                <@wp.show frame=6 />
                                 <@wp.show frame=8 />
                            </div>
                            <div class="col-lg-offset-1 col-lg-4 col-md-offset-1 col-md-4 col-sm-12 col-xs-12 box-small">
                                <@wp.show frame=7 />
                                <@wp.show frame=9 />
                            </div>
                        </div>
                    </div>
                </section>
                <section class="public-contents-12col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <@wp.show frame=10 />
                            </div>
                        </div>
                    </div>
                </section>
                <section class="public-contents-2col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <@wp.show frame=11 />
                            </div>
                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <@wp.show frame=12 />
                            </div>
                        </div>
                    </div>
                </section>
                <section class="public-contents-3col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=13 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=14 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=15 />
                            </div>
                        </div>
                    </div>
                </section>
                <section class="public-contents-3col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=16 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=17 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <@wp.show frame=18 />
                            </div>
                        </div>
                    </div>
                </section>
            </main>
            <footer id="footer">
                <div class="container">
                    <section>
                        <div class="row clearfix">
                            <div class="col-xs-12 intestazione">
                                <div class="logoimg">
                                    <a href="<@wp.url page="homepage" />" title="<@wp.i18n key="PORTAL_DESCRIPTION" />">
                                        <img src="<@wp.imgURL/>logo-government.png" alt="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_IMGALT" />"/>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </section>
                    <div class="row">
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <@wp.show frame=19 />
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <@wp.show frame=20 />
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <div class="followus">
                                <h4><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL" /></h4>
                                <p>
                                    <a href="#" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />"><i class="fa fa-facebook" aria-hidden="true"></i>
                                        <span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />
                                    </a> 
                                    <a href="#" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW"/>"><i class="fa fa-twitter" aria-hidden="true"></i>
                                        <span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" />
                                    </a>
                                    <a href="#" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK"/>"><i class="fa fa-linkedin" aria-hidden="true"></i>
                                        <span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" />
                                    </a>
                                </p>
                            </div>
                            <@wp.show frame=21 />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <@wp.show frame=22 />
                        </div>
                    </div>
                    <section class="postFooter clearfix">
                        <h3 class="sr-only"><@wp.i18n key="PORTAL_FOOTER_LINKS_TITLE" /></h3>
                        <ul class="list-inline">
                            <li><a href="#" title="<@wp.i18n key="PORTAL_FOOTER_LINK_LEGAL_NOTICES" />"><@wp.i18n key="PORTAL_FOOTER_LINK_LEGAL_NOTICES" /></a></li>
                            <li><a href="#" title="<@wp.i18n key="PORTAL_FOOTER_LINK_PRIVACY" />"><@wp.i18n key="PORTAL_FOOTER_LINK_PRIVACY" /></a></li>
                        </ul>
                    <p class="pull-right copyright">
                    <@wp.i18n key="PORTAL_COPYRIGHT" escapeXml=false />
                    </p>    
                    </section>
                </div>
            </footer>
        </div>
        <div id="topcontrol" class="topcontrol bg-darkblu" title="<@wp.i18n key="PORTAL_BACKTOTOP" />">
            <i class="fa fa-angle-up" aria-hidden="true"></i>
        </div>
        <script src="<@wp.resourceURL />static/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="<@wp.resourceURL />static/js/government.js"></script>
        <script type="text/javascript" src="<@wp.resourceURL />static/js/jquery.cookiesdirective.js"></script>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_BTM_EXT"><script src="<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_BTM"><script src="<@wp.resourceURL />static/js/<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <script>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_BTM_INC">
            <@wp.printHeadInfo />
        </@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="JS_RAW">
            <@wp.printHeadInfo />
        </@wp.outputHeadInfo>

              (function(i,s,o,g,r,a,m){i[''GoogleAnalyticsObject'']=r;i[r]=i[r]||function(){
              (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
              m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
              })(window,document,''script'',''//www.google-analytics.com/analytics.js'',''ga'');

              ga(''create'', ''xxxxxx'', ''auto'');
              ga(''send'', ''pageview'');
        </script>
        <!-- Access reserved area -->
        <div class="modal fade" id="accessModal" tabindex="-1" role="dialog" aria-labelledby="modalaccess">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="modalaccess"><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN"/></h4>
                    </div>
                    <div class="modal-body">
                        <form action="<@wp.url/>" method="post">
                            <#if (accountExpired?? && accountExpired == true) >
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <@wp.i18n key="ESLF_USER_STATUS_EXPIRED" />
                            </div>
                            </#if>
                            <#if (wrongAccountCredential?? && wrongAccountCredential == true) >
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <@wp.i18n key="ESLF_USER_STATUS_CREDENTIALS_INVALID" />
                            </div>
                            </#if>
                            <#if (RequestParameters.returnUrl??) >
                            <input type="hidden" name="returnUrl" value="${RequestParameters.returnUrl}" />
                            </#if>
                            <div class="form-group">
                                <label for="username" class="control-label"><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_USER"/></label>
                                <input type="text" class="form-control" name="username" id="username" placeholder="<@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_USER"/>" />
                            </div>
                            <div class="form-group">
                                <label for="password_modal" class="control-label"><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_PASS"/></label>
                                <input type="password" class="form-control" name="password" id="password_modal" placeholder="<@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_PASS"/>" />
                            </div>
                            <div class="modal-footer">
                                <input type="submit" class="btn btn-primary bg-darkblue" value="<@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN" />" />
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>');

INSERT INTO pagemodels (code, descr, frames, plugincode,templategui) VALUES ('entando_page_internal_government',
'Governament Template - Internal Page','<?xml version="1.0" encoding="UTF-8"?>
<frames>
    <frame pos="0">
        <descr>Login form</descr>
        <sketch x1="0" y1="0" x2="11" y2="0" />
        <defaultWidget code="government_login_form" />
    </frame>
    <frame pos="1">
        <descr>Change language</descr>
        <sketch x1="0" y1="1" x2="3" y2="1" />
        <defaultWidget code="government_widget_language_choose" />
    </frame>
    <frame pos="2">
        <descr>Burger menu</descr>
        <sketch x1="4" y1="1" x2="7" y2="1" />
        <defaultWidget code="government_widget_burger_menu" />
    </frame>
    <frame pos="3">
        <descr>Search Form</descr>
        <sketch x1="8" y1="1" x2="11" y2="1" />
        <defaultWidget code="government_widget_search_form" />
    </frame>
    <frame pos="4">
        <descr>Horizontal menu</descr>
        <sketch x1="0" y1="2" x2="11" y2="2" />
        <defaultWidget code="content_viewer">
            <properties>
                <property key="modelId">10011</property>
                <property key="contentId">CNG10</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="5">
        <descr>Breadcrumbs</descr>
        <sketch x1="0" y1="3" x2="11" y2="3" />
        <defaultWidget code="government_widget_breadcrumbs" />
    </frame>
    <frame pos="6" main="true">
        <descr>Content evidence - fullwidth</descr>
        <sketch x1="0" y1="4" x2="11" y2="4" />
    </frame>
    <frame pos="7">
        <descr>Content 1col</descr>
        <sketch x1="0" y1="5" x2="11" y2="5" />
    </frame>
    <frame pos="8">
        <descr>Content 1col</descr>
        <sketch x1="0" y1="6" x2="11" y2="6" />
    </frame>
    <frame pos="9">
        <descr>Content list</descr>
        <sketch x1="0" y1="7" x2="11" y2="7" />
    </frame>
    <frame pos="10">
        <descr>Content list</descr>
        <sketch x1="0" y1="8" x2="11" y2="8" />
    </frame>
    <frame pos="11">
        <descr>Content list</descr>
        <sketch x1="0" y1="9" x2="11" y2="9" />
    </frame>
    <frame pos="12">
        <descr>Content 1col</descr>
        <sketch x1="0" y1="10" x2="11" y2="10" />
    </frame>
    <frame pos="13">
        <descr>Content 1col</descr>
        <sketch x1="0" y1="11" x2="11" y2="11" />
    </frame>
    <frame pos="14">
        <descr>Content list</descr>
        <sketch x1="0" y1="12" x2="11" y2="12" />
    </frame>
    <frame pos="15">
        <descr>Content list</descr>
        <sketch x1="0" y1="13" x2="11" y2="13" />
    </frame>
    <frame pos="16">
        <descr>Content 3col</descr>
        <sketch x1="0" y1="14" x2="3" y2="14" />
    </frame>
    <frame pos="17">
        <descr>Content 3col</descr>
        <sketch x1="4" y1="14" x2="7" y2="14" />
    </frame>
    <frame pos="18">
        <descr>Content 3col</descr>
        <sketch x1="8" y1="14" x2="11" y2="14" />
    </frame>
    <frame pos="19">
        <descr>Footer</descr>
        <sketch x1="0" y1="15" x2="3" y2="15" />
        <defaultWidget code="content_viewer">
            <properties>
                <property key="contentId">CNG11</property>
                <property key="modelId">10001</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="20">
        <descr>Footer</descr>
        <sketch x1="4" y1="15" x2="7" y2="15" />
        <defaultWidget code="content_viewer">
            <properties>
                <property key="contentId">CNG12</property>
                <property key="modelId">10001</property>
            </properties>
        </defaultWidget>
    </frame>
    <frame pos="21">
        <descr>Footer</descr>
        <sketch x1="8" y1="15" x2="11" y2="15" />
    </frame>
    <frame pos="22">
        <descr>Footer</descr>
        <sketch x1="0" y1="16" x2="11" y2="16" />
    </frame>
</frames>',NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>

<!doctype html>
    <!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
    <!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
    <!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
    <!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>
        <base href="<@wp.info key="systemParam" paramName="applicationBaseURL" />">
        <!--[if IE]><script type="text/javascript">
            (function() {
                var baseTag = document.getElementsByTagName(''base'')[0];
                baseTag.href = baseTag.href;
            })();
        </script>
        <![endif]-->
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title><@wp.i18n key="PORTAL_TITLE" /> - <@wp.currentPage param="title" /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta id="viewport" name="viewport" content="initial-scale=1.0, width=device-width"/>
        <meta name="description" content="<@wp.i18n key="PORTAL_DESCRIPTION" />">

        <link rel="icon" type="image/png" href="<@wp.resourceURL />favicon.png" />
        <link rel="apple-touch-icon" sizes="120x120" href="<@wp.imgURL/>favicon_120.png" />

        <link href="//fonts.googleapis.com/css?family=Titillium+Web:300,300italic,600,600italic,700,700italic" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="<@wp.cssURL/>bootstrap.min.css" />
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" />
        <link href="<@wp.cssURL/>government.css" rel="stylesheet" type="text/css" />
        <link href="<@wp.cssURL/>government-print.css" rel="stylesheet" type="text/css" />
        <@wp.outputHeadInfo type="CSS_GOVERNMENT">
        <link href="<@wp.cssURL/><@wp.printHeadInfo />" rel="stylesheet" type="text/css" />
        </@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="CSS_GOVERNMENT_EXT">
        <link href="<@wp.printHeadInfo />" rel="stylesheet" type="text/css" />
        </@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="CSS_EXT">
        <link href="<@wp.printHeadInfo />" rel="stylesheet" type="text/css" />
        </@wp.outputHeadInfo>
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script>!window.jQuery && document.write(''<script src="<@wp.resourceURL />static/js/jquery-3.1.1.min.js"><\/script>'')</script>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_TOP_EXT"><script src="<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_TOP"><script src="<@wp.resourceURL />static/js/<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body class="push-body int-pg">
        <div class="body_wrapper push_container clearfix" id="page_top">
            <!--[if lt IE 8]>
            <p class="browserupgrade"><@wp.i18n key="PORTAL_BROWSERUPDATE" escapeXml=false /></p>
            <![endif]-->
            <div class="skiplink sr-only">
                <ul>
                    <li><a accesskey="2" href="<@wp.url />#main_container"><@wp.i18n key="PORTAL_SKIPLINK_GOCONTENT" /></a></li>
                    <li><a accesskey="3" href="<@wp.url />#menup"><@wp.i18n key="PORTAL_SKIPLINK_GONAVIGATION" /></a></li>
                    <li><a accesskey="4" href="<@wp.url />#footer"><@wp.i18n key="PORTAL_SKIPLINK_GOFOOTER" /></a></li>
                </ul>
            </div>
            <header id="mainheader" class="navbar-fixed-top bg-darkgrey container-fullwidth">
                <section class="preheader bg-darkblue">
                    <h3 class="sr-only"><@wp.i18n key="PORTAL_HEADER_PREHEADER_TITLE" /></h3>
                    <div class="container">
                        <div class="row clearfix">
                            <div class="col-lg-offset-10 col-lg-2 col-md-offset-10 col-md-2 col-sm-offset-9 col-sm-3 col-xs-offset-10 col-xs-2 reserved-area bg-white">
                                  <@wp.show frame=0 />
                            </div>
                        </div>
                    </div>
                </section>
                <!-- Button Menu -->
                <button class="navbar-toggle menu-btn pull-left menu-left push-body jPushMenuBtn">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar icon-bar1"></span>
                    <span class="icon-bar icon-bar2"></span>
                    <span class="icon-bar icon-bar3"></span>
                    <span class="titlemenu">Menu</span>
                </button>
                <!--End Button Menu -->

                <!-- Menu -->
                <nav class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-left" id="menup">
                    <div class="cbp-menu-wrapper clearfix">
                        <h3 class="sr-only"><@wp.i18n key="PORTAL_HEADER_MENU_TITLE" /></h3>
                        <ul class="list-inline languagemobile">
                            <@wp.show frame=1 />
                        </ul>
                        <ul class="list-inline socialmobile">
                            <li class="small"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL" /></li>
                            <li><a href="https://www.facebook.com/Entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />"><i class="fa fa-facebook" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" /></span></a></li>
                            <li><a href="https://twitter.com/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" />"><i class="fa fa-twitter" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" /></span></a></li>
                            <li><a href="https://it.linkedin.com/company/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" />"><i class="fa fa-linkedin" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" /></span></a></li>
                        </ul>
                        <@wp.show frame=2 />
                    </div>
                </nav>
                <!-- End Menu -->
                <@wp.pageWithWidget var="searchResultPageVar" widgetTypeCode="search_result" listResult=false />
                <!-- headermenu -->
                <div class="container">
                    <div class="row clearfix">
                        <div class="col-lg-8 col-md-8 col-sm-9 col-xs-12 government">
                            <div class="logoimg">
                                <a href="<@wp.url page="homepage" />" title="<@wp.i18n key="PORTAL_DESCRIPTION" />">
                                    <img src="<@wp.imgURL/>logo-government.png" alt="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_IMGALT" />"/>
                                </a>
                            </div>
                            <!-- mobile search -->
                                <@wp.show frame=3 />
                            <!-- mobile search -->
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-3 col-xs-12 hidden-xs pull-right text-right">
                            <ul class="list-inline text-right language">
                                <@wp.show frame=1 />
                            </ul>
                            <ul class="list-inline text-right social">
                                <li class="small"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL" /></li>
                                <li><a href="https://www.facebook.com/Entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />"><i class="fa fa-facebook" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" /></span></a></li>
                                <li><a href="https://twitter.com/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" />"><i class="fa fa-twitter" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" /></span></a></li>
                                <li><a href="https://it.linkedin.com/company/entando" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" />"><i class="fa fa-linkedin" aria-hidden="true"></i><span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" /></span></a></li>
                            </ul>
                            <!-- search -->
                                <@wp.show frame=3 />
                            <!-- search -->
                        </div>
                    </div>
                </div>
                <!-- headermenu -->
                <!-- mobile search -->
                <div class="hidden-lg hidden-md collapse" id="searchMobile" aria-expanded="false" role="form">
                    <form action="<#if (searchResultPageVar??) ><@wp.url page="${searchResultPageVar.code}" /></#if>" method="post">
                        <div class="container">
                            <div class="row">
                                <label for="cm_searchmobile" class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" /></label>
                                <input type="text" class="form-control squared" placeholder="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" />" name="search" id="cm_searchmobile" />
                            </div>
                        </div>
                    </form>
                </div>      
                <!-- mobile search -->
                <@wp.show frame=4 />
            </header>
            <main id="main_container">
                <section class="breadcrumb bg-darkblue">
                    <@wp.show frame=5 />
                </section>
                <section class="public-content-in-evidence">
                    <@wp.show frame=6 />
                </section>
                <section class="public-contents-1col mt-20">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <@wp.show frame=7 />
                            </div>
                            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <@wp.show frame=8 />
                            </div>
                        </div>
                    </div>
                </section>
                <section class="public-contents-list">
                    <@wp.show frame=9 />
                    <@wp.show frame=10 />
                    <@wp.show frame=11 />
                </section>
                <section class="public-contents-1col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <@wp.show frame=12 />
                            </div>
                            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <@wp.show frame=13 />
                            </div>
                        </div>
                    </div>
                </section>
                <section class="public-contents-list">
                    <@wp.show frame=14 />
                    <@wp.show frame=15 />
                </section>
                
                <section class="public-contents-3col">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
                                <@wp.show frame=16 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
                                <@wp.show frame=17 />
                            </div>
                            <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
                                <@wp.show frame=18 />
                            </div>
                        </div>
                    </div>
                </section>
            </main>         
            <footer id="footer">
                <div class="container">
                    <section>
                        <div class="row clearfix">
                            <div class="col-xs-12 intestazione">
                                <div class="logoimg">
                                    <a href="<@wp.url page="homepage" />" title="<@wp.i18n key="PORTAL_DESCRIPTION" />">
                                        <img src="<@wp.imgURL/>logo-government.png" alt="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_IMGALT" />"/>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </section>
                    <div class="row">
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <@wp.show frame=19 />
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <@wp.show frame=20 />
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
                            <div class="followus">
                                <h4><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL" /></h4>
                                <p>
                                    <a href="#" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />"><i class="fa fa-facebook" aria-hidden="true"></i>
                                        <span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_FB" />
                                    </a> 
                                    <a href="#" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW"/>"><i class="fa fa-twitter" aria-hidden="true"></i>
                                        <span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_TW" />
                                    </a>
                                    <a href="#" title="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK"/>"><i class="fa fa-linkedin" aria-hidden="true"></i>
                                        <span class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SOCIAL_LK" />
                                    </a>
                                </p>
                            </div>
                            <@wp.show frame=21 />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <@wp.show frame=22 />
                        </div>
                    </div>
                    <section class="postFooter clearfix">
                        <h3 class="sr-only"><@wp.i18n key="PORTAL_FOOTER_LINKS_TITLE" /></h3>
                        <ul class="list-inline">
                            <li><a href="#" title="<@wp.i18n key="PORTAL_FOOTER_LINK_LEGAL_NOTICES" />"><@wp.i18n key="PORTAL_FOOTER_LINK_LEGAL_NOTICES" /></a></li>
                            <li><a href="#" title="<@wp.i18n key="PORTAL_FOOTER_LINK_PRIVACY" />"><@wp.i18n key="PORTAL_FOOTER_LINK_PRIVACY" /></a></li>
                        </ul>
                    <p class="pull-right copyright">
                    <@wp.i18n key="PORTAL_COPYRIGHT" escapeXml=false />
                    </p>    
                    </section>
                </div>
            </footer>
        </div>
        <div id="topcontrol" class="topcontrol bg-darkblu" title="<@wp.i18n key="PORTAL_BACKTOTOP" />">
            <i class="fa fa-angle-up" aria-hidden="true"></i>
        </div>
        <script src="<@wp.resourceURL />static/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="<@wp.resourceURL />static/js/government.js"></script>
        <script type="text/javascript" src="<@wp.resourceURL />static/js/jquery.cookiesdirective.js"></script>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_BTM_EXT"><script src="<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_BTM"><script src="<@wp.resourceURL />static/js/<@wp.printHeadInfo />"></script></@wp.outputHeadInfo>
        <script>
        <@wp.outputHeadInfo type="JS_GOVERNMENT_BTM_INC">
            <@wp.printHeadInfo />
        </@wp.outputHeadInfo>
        <@wp.outputHeadInfo type="JS_RAW">
            <@wp.printHeadInfo />
        </@wp.outputHeadInfo>

              (function(i,s,o,g,r,a,m){i[''GoogleAnalyticsObject'']=r;i[r]=i[r]||function(){
              (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
              m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
              })(window,document,''script'',''//www.google-analytics.com/analytics.js'',''ga'');

              ga(''create'', ''xxxxxx'', ''auto'');
              ga(''send'', ''pageview'');
        </script>
        <!-- Access reserved area -->
        <div class="modal fade" id="accessModal" tabindex="-1" role="dialog" aria-labelledby="modalaccess">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="modalaccess"><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN"/></h4>
                    </div>
                    <div class="modal-body">
                        <form action="<@wp.url/>" method="post">
                            <#if (accountExpired?? && accountExpired == true) >
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <@wp.i18n key="ESLF_USER_STATUS_EXPIRED" />
                            </div>
                            </#if>
                            <#if (wrongAccountCredential?? && wrongAccountCredential == true) >
                            <div class="alert alert-danger" role="alert">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                <@wp.i18n key="ESLF_USER_STATUS_CREDENTIALS_INVALID" />
                            </div>
                            </#if>
                            <#if (RequestParameters.returnUrl??) >
                            <input type="hidden" name="returnUrl" value="${RequestParameters.returnUrl}" />
                            </#if>
                            <div class="form-group">
                                <label for="username" class="control-label"><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_USER"/></label>
                                <input type="text" class="form-control" name="username" id="username" placeholder="<@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_USER"/>" />
                            </div>
                            <div class="form-group">
                                <label for="password_modal" class="control-label"><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_PASS"/></label>
                                <input type="password" class="form-control" name="password" id="password_modal" placeholder="<@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN_PASS"/>" />
                            </div>
                            <div class="modal-footer">
                                <input type="submit" class="btn btn-primary bg-darkblue" value="<@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN" />" />
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('government_widget_burger_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Government - Burger menu</property><property key="it">Government - Burger menu</property></properties>
', NULL, NULL, 'entando-widget-navigation_bar', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="navSpec">code(homepage).subtree(2)</property></properties>
', 0, 'free');
INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked,maingroup) VALUES ('government_widget_breadcrumbs','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Government - Breadcrumbs</property>
<property key="it">Government - Breadcrumbs</property>
</properties>','<config>
    <parameter name="navSpec">Rules for the Page List auto-generation</parameter>
    <action name="navigatorConfig" />
</config>',NULL,'entando-widget-navigation_breadcrumbs','<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="navSpec">current.path</property></properties>
',0,'free');

INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked,maingroup) VALUES ('government_widget_language_choose','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Government - Choose a Language</property>
<property key="it">Government - Seleziona una Lingua</property>
</properties>',NULL,NULL,NULL,NULL,1,'free');

INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked,maingroup) VALUES ('government_widget_search_form','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Government - Search Form</property>
<property key="it">Government - Form di ricerca</property>
</properties>',NULL,NULL,NULL,NULL,1,'free');

INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked,maingroup) VALUES ('government_login_form','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Government - Login Form</property>
<property key="it">Government - Form di Login</property>
</properties>',NULL,NULL,NULL,NULL,1,'free');


INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('government_widget_breadcrumbs','government_widget_breadcrumbs',NULL,NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<div class="container">
    <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <@wp.currentPage param="code" var="currentViewCode" />
            <@wp.freemarkerTemplateParameter var="currentViewCode" valueName="currentViewCode" />
                <#assign first=true />
                <#assign lastPageTitle="" />
                <@wp.nav spec="current.path" var="currentTarget">
                        <#assign currentCode = currentTarget.code />
                        <#if !currentTarget.voidPage>
                            <#if currentCode == currentViewCode>
                                <span>${currentTarget.title}</span>
                            <#else>
                                <a href="${currentTarget.url}" title="<@wp.i18n key="PORTAL_GOTO" />: ${currentTarget.title}">
                                    ${currentTarget.title}
                                </a>
                                <i class="fa fa-angle-right" aria-hidden="true"></i>
                            </#if>
                        <#else>
                            <span>${currentTarget.title}</span>
                            <i class="fa fa-angle-right" aria-hidden="true"></i>
                        </#if>
                    <#assign lastPageTitle = currentTarget.title />
                </@wp.nav>
        </div>
    </div>
</div>',1);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('government_template_footer_link',NULL,NULL,NULL,'<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>  

<section class="postFooter clearfix">
    <h3 class="sr-only"><@wp.i18n key="PORTAL_FOOTER_LINKS_TITLE" /></h3>
    <ul class="list-inline">
        <li><a href="#" title="<@wp.i18n key="PORTAL_FOOTER_LINK_LEGAL_NOTICES" />"><@wp.i18n key="PORTAL_FOOTER_LINK_LEGAL_NOTICES" /></a></li>
        <li><a href="#" title="<@wp.i18n key="PORTAL_FOOTER_LINK_PRIVACY" />"><@wp.i18n key="PORTAL_FOOTER_LINK_PRIVACY" /></a></li>
    </ul>
<p class="pull-right copyright">
<@wp.i18n key="PORTAL_COPYRIGHT" escapeXml=false />
</p>    
</section>',1);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('government_template_footer_header',NULL,NULL,NULL,'<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]> 
<section>
    <div class="row clearfix">
        <div class="col-xs-12 intestazione">
            <div class="logoimg">
                <a href="<@wp.url page="homepage" />" title="<@wp.i18n key="PORTAL_DESCRIPTION" />">
                    <img src="<@wp.imgURL/>logo-government.png" alt="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_IMGALT" />"/>
                </a>
            </div>
        </div>
    </div>
</section>',1);

INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('government_widget_search_form','government_widget_search_form',NULL,NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<!-- search -->
<@wp.pageWithWidget var="searchResultPageVar" widgetTypeCode="search_result" listResult=false />
<div class="search clearfix">
    <form action="<#if (searchResultPageVar??) ><@wp.url page="${searchResultPageVar.code}" /></#if>" method="post">
        <div class="input-group">
            <label for="cm_search" class="hidden"><@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" /></label>
            <input type="text" class="form-control squared" placeholder="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" />" name="search" id="cm_search" />
            <span class="input-group-btn">
                <button class="btn btn-default btn-search pull-right bg-darkblue" type="submit">
                    <img src="<@wp.imgURL/>search.svg" alt="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" />">
                </button>
            </span>
        </div>
    </form>
</div>
<!-- search -->
<!-- mobile search -->
<div class="p_searchMobile clearfix">
    <span class="input-group-btn">
        <button class="btn btn-default btn-search pull-right bg-darkblue" data-target="#searchMobile" data-toggle="collapse" type="button">
            <img src="<@wp.imgURL/>search.svg" alt="<@wp.i18n key="PORTAL_HEADER_HEADERMENU_SEARCH" />">
        </button>
    </span>
</div>
<!-- mobile search -->',1);

INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('government_widget_language_choose','government_widget_language_choose',NULL,NULL,'<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>

<@wp.info key="langs" var="langsVar" />
<@wp.info key="currentLang" var="currentLangVar" />
<@wp.freemarkerTemplateParameter var="langsListVar" valueName="langsVar" removeOnEndTag=true >
    <#list langsListVar as curLangVar>
        <li
        <#if (curLangVar.code == currentLangVar)>class="active"</#if>>
        <a href="<@wp.url lang="${curLangVar.code}" paramRepeat=true />">
            <@wp.i18n key="PORTAL_LANG_${curLangVar.code}" />
        </a>
        </li>
    </#list>
</@wp.freemarkerTemplateParameter>',1);

INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('government_login_form','government_login_form',NULL,NULL,'<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#if (Session.currentUser.username != "guest") >
    <a href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/logout.action"><@wp.i18n key="ESLF_SIGNOUT" /></a>
<#else>
<button type="button" class="btn btn-primary btn-lg bg-white" data-toggle="modal" data-target="#accessModal">
    <span><@wp.i18n key="PORTAL_HEADER_PREHEADER_LOGIN"/></span>
</button>

<!-- Modal -->
<#if (wrongAccountCredential?? && wrongAccountCredential == true) || (accountExpired?? && accountExpired == true) >
<#assign js_login="<script>$( document ).ready(function() {
    $(''#accessModal'').modal(''show'');
});</script>" >
<@wp.headInfo type="JS_GOVERNMENT_BTM_INC" info="${js_login}" />
</#if>
</#if>',1);
