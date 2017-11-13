INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-banner-main-page', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
    <property key="en">entando-widget-banner-main-page</property>
    <property key="it">entando-widget-banner-main-page </property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-banner-main-page', 'entando-widget-banner-main-page', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<@wp.info key="langs" var="langsVar" />
<@wp.info key="currentLang" var="currentLangVar" />
 <div class="bg-custom-purple text-center">
 <div class="home-wrapper">
    <h1 class="font-light text-white">Personal Mortgage from 9000 to 999.999$</h1>
        <h4 class="text-light">Get feedback within 24 hours - No obligation</h4>
        <a href="#mortgage">
            <img alt="logo-arrow" class="logo-img" src="<@wp.imgURL />Fill_1.png">
        </a>
   </div>
</div>', 1);
