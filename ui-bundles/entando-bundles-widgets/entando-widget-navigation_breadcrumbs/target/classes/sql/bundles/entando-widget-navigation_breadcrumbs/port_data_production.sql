INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-navigation_breadcrumbs', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Navigation - Breadcrumbs</property>
<property key="it">Navigazione - Briciole di Pane</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-navigation_breadcrumbs', 'entando-widget-navigation_breadcrumbs', NULL, NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>

<@wp.currentPage param="code" var="currentViewCode" />
<p class="breadcrumb"><span class="noscreen"><@wp.i18n key="ESNB_YOU_ARE_HERE" />:</span>
<#assign first=true>
<@wp.nav spec="current.path" var="currentTarget">
      <#assign currentCode=currentTarget.code>
      <#if first>
             <span class="divider">/</span>
      </#if>
      <#if !currentTarget.voidPage>
              <#if (currentCode == currentViewCode)>
                     <span class="active">${currentTarget.title}</span>
              <#else>
                     <a href="${currentTarget.url}">${currentTarget.title}</a>
              </#if>
      <#else>
              ${currentTarget.title}
      </#if>
      <#assign first=false>
</@wp.nav>
</p>', 1);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESNB_YOU_ARE_HERE', 'en', 'You are here');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESNB_YOU_ARE_HERE', 'it', 'Sei qui');
