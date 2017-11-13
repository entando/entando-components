INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-banner_avarage_light', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">entando-widget-banner_avarage_light</property>
<property key="it">entando-widget-banner_avarage_light</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked)
VALUES ('entando-widget-banner_avarage_light', 'entando-widget-banner_avarage_light', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<div class="avarage" >
</div>', 1);
