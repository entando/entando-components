INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpfrontshortcut_content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Front Shortcut Contents - Publish Content</property>
<property key="it">Front Shortcut Contenuti - Pubblica un Contenuto</property>
</properties>', '<config>
	<parameter name="contentId">Content ID</parameter>
	<parameter name="modelId">Content Model ID</parameter>
	<action name="viewerConfig"/>
</config>', 'jpfrontshortcut', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpfrontshortcut_navigation_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Front Shortcut Navigation - Menu</property>
<property key="it">Front Shortcut Navigazione - Menu</property>
</properties>', '<config>
<parameter name="navSpec">Rules for the Page List auto-generation</parameter>
<action name="navigatorConfig" />
</config>', 'jpfrontshortcut', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpfrontshortcut_info', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Front Shortcut Info</property>
<property key="it">Front Shortcut Info</property>
</properties>', NULL, 'jpfrontshortcut', NULL, NULL, 1, NULL);


INSERT INTO pagemodels (code, descr, frames, plugincode) VALUES ('jpfrontshortcut_test', 'jpfrontshortcut plugin - Test', '<frames>
	<frame pos="0">
		<descr>Top</descr>
	</frame>
	<frame pos="1">
		<descr>Left I</descr>
	</frame>
	<frame pos="2">
		<descr>Left II</descr>
	</frame>
	<frame pos="3">
		<descr>Left III</descr>
	</frame>
	<frame pos="4">
		<descr>Left IV</descr>
	</frame>
	<frame pos="5">
		<descr>Right I</descr>
	</frame>
	<frame pos="6">
		<descr>Right II</descr>
	</frame>
	<frame pos="7">
		<descr>Right III</descr>
	</frame>
	<frame pos="8">
		<descr>Right IV</descr>
	</frame>
	<frame pos="9">
		<descr>Footer</descr>
	</frame>
</frames>', 'jpfrontshortcut');


INSERT INTO pages (code, parentcode, pos, modelcode, titles, groupcode, showinmenu, extraconfig) VALUES ('jpfrontshortcut_test', 'homepage', 7, 'jpfrontshortcut_test', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Front Shortcut - Test Page</property>
<property key="it">Front Shortcut - Pagina di Prova</property>
</properties>', 'free', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <mimeType>text/html</mimeType>
</config>');

INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('jpfrontshortcut_test', 1, 'jpfrontshortcut_navigation_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">code(homepage).subtree(2)</property>
</properties>', NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config, publishedcontent) VALUES ('jpfrontshortcut_test', 5, 'jpfrontshortcut_info', NULL, NULL);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_POPUP_TITLE','en','Edit');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_POPUP_TITLE','it','Modifica');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_EDIT_FRAME','en','Edit frame:');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_EDIT_FRAME','it','Modifica Posizione:');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_EMPTY_FRAME','en','Empty frame:');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_EMPTY_FRAME','it','Svuota la posizione:');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_NEWPAGE','en','Create child page of:');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_NEWPAGE','it','Crea pagina figlia di:');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_EDITPAGE','en','Edit page:');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_EDITPAGE','it','Modifica pagina:');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_DELETEPAGE','en','Delete page:');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_DELETEPAGE','it','Elimina pagina:');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_EDIT_THIS_CONTENT','en','Edit this content (Front Shortcut)');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpfrontshortcut_EDIT_THIS_CONTENT','it','Modifica questo contenuto (Front Shortcut)');
