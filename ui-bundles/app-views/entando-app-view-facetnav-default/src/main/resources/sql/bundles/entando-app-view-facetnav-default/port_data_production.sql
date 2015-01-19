INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpfacetnav_results', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Search Result</property>
<property key="it">Risultati Ricerca</property>
</properties>', '<config>
	<parameter name="contentTypesFilter">Content Type (optional)</parameter>
	<action name="facetNavResultConfig"/>
</config>', 'jpfacetnav', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpfacetnav_tree', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Facets Tree</property>
<property key="it">Albero delle faccette</property>
</properties>', '<config>
	<parameter name="facetRootNodes">Facet Category Root</parameter>
	<parameter name="contentTypesFilter">Content Type (optional)</parameter>
	<action name="facetNavTreeConfig"/>
</config>', 'jpfacetnav', NULL, NULL, 1);




INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_REMOVE_FILTER', 'it', 'Rimuovi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_REMOVE_FILTER', 'en', 'Remove');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_TITLE_TREE', 'it', 'Albero delle Faccette');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_TITLE_TREE', 'en', 'Facet Tree');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_TITLE_FACET_RESULTS', 'it', 'Faccette');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_TITLE_FACET_RESULTS', 'en', 'Facets');




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpfacetnav_inc_facetNavTree', NULL, 'jpfacetnav', NULL, '<#assign wpfp=JspTaglibs["/jpfacetnav-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>
<ul>
	<#list currFacetRoot.children as facetNode>
		<@wpfp.hasToViewFacetNode facetNodeCode="${facetNode.code}" requiredFacetsParamName="requiredFacets">
			<#if (occurrences[facetNode.code]??)>
				<li>
				<a href="<@wp.url><@wpfp.urlPar name="selectedNode" ><@c.out value="${facetNode.code}" /></@wpfp.urlPar>
					<#list requiredFacets as requiredFacet>
					<@wpfp.urlPar name="facetNode_${requiredFacet_index + 1}" >${requiredFacet}</@wpfp.urlPar>
					</#list>
					</@wp.url>"><@wpfp.facetNodeTitle escapeXml=true facetNodeCode="${facetNode.code}" /></a>&#32;<abbr class="jpfacetnav_tree_occurences" title="<@wp.i18n key="jpfacetnav_OCCURRENCES_FOR" />&#32;<@wpfp.facetNodeTitle escapeXml=true facetNodeCode="${facetNode.code}" />:&#32;<@c.out value="${occurrences[facetNode.code]}" />"><@c.out value="${occurrences[facetNode.code]}" /></abbr>
				<@wpfp.hasToOpenFacetNode facetNodeCode="${facetNode.code}" requiredFacetsParamName="requiredFacets" occurrencesParamName="occurrences" >
					<@wp.freemarkerTemplateParameter var="currFacetRoot" valueName="facetNode" removeOnEndTag=true >
					<@wp.fragment code="jpfacetnav_inc_facetNavTree" escapeXml=false />
					</@wp.freemarkerTemplateParameter>
				</@wpfp.hasToOpenFacetNode>
				</li>
			</#if>
		</@wpfp.hasToViewFacetNode>
	</#list>
</ul>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpfacetnav_tree', 'jpfacetnav_tree', 'jpfacetnav', NULL, '<#assign wpfp=JspTaglibs["/jpfacetnav-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>
<@wp.headInfo type="CSS" info="../../plugins/jpfacetnav/static/css/jpfacetnav.css"/>
<div class="jpfacetnav">
	<h2 class="title"><@wp.i18n key="jpfacetnav_TITLE_TREE" /></h2>
	<@wpfp.facetNavTree requiredFacetsParamName="requiredFacets" facetsTreeParamName="facetsForTree" />
	<@wp.freemarkerTemplateParameter var="occurrences" valueName="occurrences" removeOnEndTag=true >
	<#list facetsForTree as facetRoot>
		<h3><@wpfp.facetNodeTitle facetNodeCode="${facetRoot.code}" /></h3>
		<#if (occurrences[facetRoot.code]??) && (occurrences[facetRoot.code]?has_content)>
			<ul>
				<#list facetRoot.children as facetNode>
					<#if (occurrences[facetNode.code]??)>
						<li>
							<a href="<@wp.url><@wp.parameter name="selectedNode" ><@c.out value="${facetNode.code}" /></@wp.parameter>
								<#list requiredFacets as requiredFacet>
								<@wpfp.urlPar name="facetNode_${requiredFacet_index + 1}" ><@c.out value="${requiredFacet}" /></@wpfp.urlPar>
								</#list>
								</@wp.url>"><@wpfp.facetNodeTitle facetNodeCode="${facetNode.code}" /></a>&#32;<abbr class="jpfacetnav_tree_occurences" title="<@wp.i18n key="jpfacetnav_OCCURRENCES_FOR" />&#32;<@wpfp.facetNodeTitle facetNodeCode="${facetNode.code}" />:&#32;<@c.out value="${occurrences[facetNode.code]}" />"><@c.out value="${occurrences[facetNode.code]}" /></abbr>
							<@wpfp.hasToOpenFacetNode facetNodeCode="${facetNode.code}" requiredFacetsParamName="requiredFacets" occurrencesParamName="occurrences" >
								<@wp.freemarkerTemplateParameter var="currFacetRoot" valueName="facetNode" removeOnEndTag=true >
								<@wp.fragment code="jpfacetnav_inc_facetNavTree" escapeXml=false />
								</@wp.freemarkerTemplateParameter>
							</@wpfp.hasToOpenFacetNode>
						</li>
					</#if>
				</#list>
			</ul>
		<#else>
			<p><abbr title="<@wp.i18n key="jpfacetnav_EMPTY_TAG" />:&#32;<@wpfp.facetNodeTitle facetNodeCode="${facetRoot.code}" escapeXml=true />">&ndash;</abbr></p>
		</#if>
	</#list>
	</@wp.freemarkerTemplateParameter>
</div>', 1);