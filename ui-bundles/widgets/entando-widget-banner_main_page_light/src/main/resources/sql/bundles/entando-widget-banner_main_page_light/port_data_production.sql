INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-banner_main_page_light', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
    <property key="en">entando-widget-banner_main_page_light</property>
    <property key="it">entando-widget-banner_main_page_light </property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-banner_main_page_light', 'entando-widget-banner_main_page_light', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<div class="home-fullscreen" id="">
    <div class="full-screen">
        <div class="home-wrapper home-wrapper-alt">
            <h1 class="font-light text-white">Personal Mortgage from 9000 to 999.999$</h1>
            <h4 class="text-light">Get feedback within 24 hours - No obligation</h4>
            <a href="#mortgage">
                <img alt="logo-arrow" class="logo-img" src="<@wp.imgURL />Fill_1.png">
            </a>
        </div>
    </div>
</div>', 1);
