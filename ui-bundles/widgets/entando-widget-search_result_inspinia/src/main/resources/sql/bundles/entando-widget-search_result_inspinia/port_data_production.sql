INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-search_result_inspinia', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Search Result</property>
<property key="it">Search Result</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-search_result_inspinia', 'entando-widget-search_result_inspinia', NULL, NULL, '<#assign jacms=JspTaglibs["/jacms-aps-core"]>
<#assign wp=JspTaglibs["/aps-core"]>
<div class="ibox-content">
<#if (RequestParameters.search?? && RequestParameters.search!='''')>
<@jacms.searcher listName="contentListResult" />
</#if>
<#if (contentListResult??) && (contentListResult?has_content) && (contentListResult?size > 0)>
<@wp.pager listName="contentListResult" objectName="groupContent" max=10 pagerIdFromFrame=true advanced=true offset=5>
	<@wp.freemarkerTemplateParameter var="group" valueName="groupContent" removeOnEndTag=true >
	<h2> <@wp.i18n key="SEARCHED_FOR" />: <span class="text-navy">&#34;<#if (RequestParameters.search??)>${RequestParameters.search}</#if>&#34;</span> </h2>
	<small>${groupContent.size} <@wp.i18n key="SEARCH_RESULTS_OUTRO" /> [${groupContent.begin + 1} &ndash; ${groupContent.end + 1}]:</small>
	<@wp.fragment code="default_pagerBlock" escapeXml=false />
        <div class="hr-line-dashed"></div>
	<#list contentListResult as contentId>
	<#if (contentId_index >= groupContent.begin) && (contentId_index <= groupContent.end)>
		<@jacms.content contentId="${contentId}" modelId="list" />
	</#if>
	</#list>
	<@wp.fragment code="default_pagerBlock" escapeXml=false />
	</@wp.freemarkerTemplateParameter>
</@wp.pager>
<#else>
<h2><@wp.i18n key="SEARCH_RESULTS" /></h2>
<p class="text-danger"><@wp.i18n key="SEARCH_NOTHING_FOUND" /></p>
</#if>
</div>', 1);


