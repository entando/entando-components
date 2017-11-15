INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-security_banner_light', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
    <property key="en">entando-widget-security_banner_light</property>
    <property key="it">entando-widget-security_banner_light </property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-security_banner_light', 'entando-widget-security_banner_light', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<@wp.info key="langs" var="langsVar" />
<@wp.info key="currentLang" var="currentLangVar" />
<div class="text-center" >
       <img alt=""  class="logo" src="<@wp.imgURL />Loghi_Security.png">
</div>', 1);
