INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-language_choose', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Choose a Language</property>
<property key="it">Choose a Language</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-language_choose', 'entando-widget-language_choose', NULL, NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>

<@wp.headInfo type="JS" info="entando-misc-jquery/jquery-1.10.0.min.js" />
<@wp.headInfo type="JS" info="entando-misc-bootstrap/bootstrap.min.js" />
<@wp.info key="langs" var="langsVar" />
<@wp.info key="currentLang" var="currentLangVar" />

<ul class="nav">
  <li class="dropdown">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown" title="<@wp.i18n key="ESLC_LANGUAGE" />"><span class="icon-flag"></span><span class="caret"></span></a>
      <ul class="dropdown-menu">
				<@wp.freemarkerTemplateParameter var="langsListVar" valueName="langsVar" removeOnEndTag=true >

				<#list langsListVar as curLangVar>
				<li
							<#if (curLangVar.code == currentLangVar)>class="active" </#if>>
							<a href=<@wp.url lang="${curLangVar.code}" paramRepeat=true />">
								<@wp.i18n key="ESLC_LANG_${curLangVar.code}" />
							</a>
				</li>
				</#list>

				</@wp.freemarkerTemplateParameter>
      </ul>
  </li>
</ul>', 1);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLC_LANGUAGE', 'en', 'Language');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLC_LANGUAGE', 'it', 'Lingua');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLC_LANG_it', 'en', 'Italiano');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLC_LANG_it', 'it', 'Italiano');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLC_LANG_en', 'en', 'English');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLC_LANG_en', 'it', 'English');
