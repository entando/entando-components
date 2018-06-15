INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, locked ) VALUES ( 'jpseo_content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Contents - Publish a Content with extra meta-description</property>
<property key="it">Contenuti - Pubblica un Contenuto con extra meta-description</property>
</properties>', '<config>
	<parameter name="contentId">Content ID</parameter>
	<parameter name="modelId">Content Model ID</parameter>
	<action name="viewerConfig"/>
</config>', 'jpseo', 1 );




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpseo_content_viewer', 'jpseo_content_viewer', 'jpseo', NULL, '<#assign jacms=JspTaglibs["/jacms-aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign jpseo=JspTaglibs["/jpseo-aps-core"]>
<@jacms.contentInfo param="authToEdit" var="canEditThis" />
<@jacms.contentInfo param="contentId" var="myContentId" />
<#if (canEditThis?? && canEditThis)>
	<div class="bar-content-edit">
		<a href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/edit.action?contentId=<@jacms.contentInfo param="contentId" />" class="btn btn-info">
		<@wp.i18n key="EDIT_THIS_CONTENT" /> <i class="icon-edit icon-white"></i></a>
	</div>
</#if>
<@jpseo.content publishExtraTitle=true publishExtraDescription=true />', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpseo_model_meta_info', NULL, 'jpseo', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign jpseo=JspTaglibs["/jpseo-aps-core"]>

<@jpseo.currentPage param="description" var="metaDescrVar" />
<#if (metaDescrVar??)>
<meta name="description" content="<@c.out value="${metaDescrVar}" />" />
</#if>

<#-- EXAMPLE OF meta infos on page -->
<#--
<@jpseo.seoMetaTag key="author" var="metaAuthorVar" />
<#if (metaAuthorVar??)>
<meta name="author" content="<@c.out value="${metaAuthorVar}" />" />
</#if>

<@jpseo.seoMetaTag key="keywords" var="metaKeywords" />
<#if (metaKeywords??)>
<meta name="keywords" content="<@c.out value="${metaKeywords}" />" />
</#if>
-->
', 1);