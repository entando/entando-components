INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-search_form_inspinia', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Search Form</property>
<property key="it">Search Form</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-search_form_inspinia', 'entando-widget-search_form_inspinia', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<@wp.pageWithWidget var="searchResultPageVar" widgetTypeCode="search_result" listResult=false />
<form class="navbar-form-custom" action="<#if (searchResultPageVar??) ><@wp.url page="${searchResultPageVar.code}" /></#if>" method="get">
<div class="form-group">
<input type="text" name="search" class="form-control" placeholder="<@wp.i18n key="ESSF_SEARCH" />" />
</div>
</form>', 1);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESSF_SEARCH', 'en', 'Search for something ...');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESSF_SEARCH', 'it', 'Cerca qualcosa ...');
