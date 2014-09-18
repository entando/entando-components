INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-search_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Search Form</property>
<property key="it">Search Form</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-search_form', 'entando-widget-search_form', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<@wp.pageWithWidget var="searchResultPageVar" widgetTypeCode="search_result" listResult=false />
<form class="navbar-search pull-left" action="<#if (searchResultPageVar??) ><@wp.url page="${searchResultPageVar.code}" /></#if>" method="get">
<input type="text" name="search" class="search-query span2" placeholder="<@wp.i18n key="ESSF_SEARCH" />" x-webkit-speech="x-webkit-speech" />
</form>', 1);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESSF_SEARCH', 'en', 'Search');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESSF_SEARCH', 'it', 'Cerca');