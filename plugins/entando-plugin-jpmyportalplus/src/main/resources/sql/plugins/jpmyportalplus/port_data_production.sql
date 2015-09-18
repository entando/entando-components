INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_void',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Void</property>
<property key="it">My Portal - Vuoto</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_sample_widget',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Sample Widget</property>
<property key="it">My Portal - Widget di Esempio</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO sysconfig (version, item, descr, config) VALUES ( 'production', 'jpmyportalplus_config', 'Definizione degli oggetti configurabili di My Portal', '<?xml version="1.0" encoding="UTF-8"?>
<myportalConfig>
	<widgets>
		<widget code="jpmyportalplus_sample_widget" />
	</widgets>
</myportalConfig>' );




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpmyportalplus_widgetdecoration_header', NULL, 'jpmyportalplus', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign mppl=JspTaglibs["/jpmyportalplus-core"]>
<#assign wp=JspTaglibs["/aps-core"]>

<#assign currentFrameVar><@mppl.requestContextParam param="currentFrame" /></#assign>

<#assign showletId>showlet_<@wp.currentWidget param="code" />__s__${currentFrameVar}</#assign>

<#assign isEditFormOpen=false >
<#assign editFrameVar>${RequestParameters.editFrame?default("")}</#assign>
<#if (currentFrameVar == editFrameVar) ><#assign isEditFormOpen=true ></#if>

<#assign isClosed=false >
<@mppl.isCurrentFrameClosed><#assign isClosed=true ></@mppl.isCurrentFrameClosed>

<#assign removeActionUrl><@wp.info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/removeFrame.action?frameToEmpty=${currentFrameVar}&currentPageCode=<@wp.currentPage param="code" /></#assign>

<#if isClosed >
<#assign opencloseActionUrl><@wp.info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/openFrame.action?frameToResize=${currentFrameVar}&currentPageCode=<@wp.currentPage param="code" /></#assign>
<#else>
<#assign opencloseActionUrl><@wp.info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/closeFrame.action?frameToResize=${currentFrameVar}&currentPageCode=<@wp.currentPage param="code" /></#assign>
</#if>

<#if isEditFormOpen >
<@c.set var="editFrameValue" value=null />
<#else>
<@c.set var="editFrameValue">${currentFrameVar}</@c.set>
</#if>

<#assign moveActionUrl><@wp.url paramRepeat=false />?editFrame=<@c.out value="${editFrameValue}" escapeXml=false />&currentPageCode=<@wp.currentPage param="code" escapeXml=false /></#assign>

<div class="widget margin-medium-bottom" id="${showletId}">
<div class="widget-toolbar btn-group pull-left">
<a href="${removeActionUrl}" class="btn btn-mini btn-danger button-toggler-remove" title="remove"><i class="icon-remove"></i><span class="sr-only">x</span></a>&#32;
<a href="${moveActionUrl}" class="btn btn-mini btn-info button-toggler-edit" title="configure/options"><i class="icon-cog"></i><span class="sr-only">configure/options</span></a>
<a href="${opencloseActionUrl}" class="btn btn-mini btn-info button-toggler-open-close" title="toggle open close"><i class="icon-chevron-<#if isClosed >down<#else>up</#if>"></i><span class="sr-only">open/close</span></a>&#32;
</div>
<div class="widget-draggable">
<h1 class="pull-right">
  <#if RequestParameters.search?? ><#assign showletTitle ><@c.out value="${showletTitle}" escapeXml=false /></#assign></#if>
<#if showletTitle?? >
${showletTitle}
<#else>
<@wp.currentWidget param="title" />
</#if>
</h1>
</div>

<div class="clearfix"></div>
<div class="editContentBox <#if !isEditFormOpen >hide</#if>"></div>
<div class="clearfix widget-border-bottom"></div>
<div class="widget-body margin-medium-all padding-medium-bottom <#if isClosed >hide</#if>">', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpmyportalplus_widgetdecoration_footer', NULL, 'jpmyportalplus', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
</div>
</div>
<@c.set var="showletTitle" scope="request" value=null />', 1);




INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_APPLY','en','Apply');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_APPLY','it','Applica');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIG_INTRO','en','Choose which content you want to add in this page');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIG_INTRO','it','Scegli quali contenuti mostrare nella pagina');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIGMYHOME','en','Show Settings');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_CONFIGMYHOME','it','Configura la Pagina');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_INSERTINTOCOLUMN','en','Inserting it into column');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_INSERTINTOCOLUMN','it','Inserendolo nella colonna');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVE','en','Move');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVE','it','Sposta');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET','en','Reset the Page');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET','it','Reimposta la pagina');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVETHISWIDGET','en','Move this box');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_MOVETHISWIDGET','it','Sposta questo box');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET_INTRO','en','If you want to discard the current configuration you can reset the page.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_RESET_INTRO','it','Se desideri riportare la pagina alla configurazione predefinita, puoi resettare le impostazioni.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_SWAPITWITH','en','Swap it with');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_SWAPITWITH','it','Scambiandolo con');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_LOADING_INFO', 'it', 'Caricamento informazioni in corso...');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_LOADING_INFO', 'en', 'Loading...');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_ERROR_INFO', 'it', 'Si è verificato un errore, riprovare.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('JPMYPORTALPLUS_ERROR_INFO', 'en', 'An error has occurred, retry.');

