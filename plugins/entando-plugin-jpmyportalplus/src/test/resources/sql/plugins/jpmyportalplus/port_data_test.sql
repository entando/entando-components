INSERT INTO pagemodels(code, descr, frames, plugincode) VALUES ('jpmyportalplus_pagemodel', 'My Portal', '<frames>
<frame pos="0" ><descr>Header</descr></frame>
<frame pos="1"><descr>Left Column I</descr><defaultWidget code="jpmyportalplus_void" /></frame>
<frame pos="2"><descr>Left Column II</descr><defaultWidget code="jpmyportalplus_void" /></frame>
<frame pos="3"><descr>Middle Column I</descr><defaultWidget code="jpmyportalplus_void" /></frame>
<frame pos="4"><descr>Middle Column II</descr><defaultWidget code="jpmyportalplus_void" /></frame>
<frame pos="5"><descr>Right Column I</descr><defaultWidget code="jpmyportalplus_void" /></frame>
<frame pos="6"><descr>Right Column II</descr><defaultWidget code="jpmyportalplus_void" /></frame>
<frame pos="7"><descr>Footer</descr></frame>
</frames>', 'jpmyportalplus');

INSERT INTO jpmyportalplus_modelconfig(code, config) VALUES ('jpmyportalplus_pagemodel', '<frames>
<frame pos="0" locked="true"></frame>
<frame pos="1" column="1" locked="false"></frame>
<frame pos="2" column="1" locked="false"></frame>
<frame pos="3" column="2" locked="false"></frame>
<frame pos="4" column="2" locked="false"></frame>
<frame pos="5" column="3" locked="false"></frame>
<frame pos="6" column="3" locked="false"></frame>
<frame pos="7" locked="true"></frame>
</frames>');

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

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_test_widget_1',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Test Widget 1</property>
<property key="it">My Portal - Widget di Test 1</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_test_widget_2',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Test Widget 2</property>
<property key="it">My Portal - Widget di Test 2</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig,locked) VALUES (
'jpmyportalplus_test_widget_3',
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">My Portal - Test Widget 3</property>
<property key="it">My Portal - Widget di Test 3</property>
</properties>',null,'jpmyportalplus',null,null,1);

INSERT INTO sysconfig (version, item, descr, config) VALUES ( 'test', 'jpmyportalplus_config', 'Definizione degli oggetti configurabili di My Portal', '<?xml version="1.0" encoding="UTF-8"?>
<myportalConfig>
	<widgets>
		<widget code="jpmyportalplus_sample_widget" />
		<widget code="jpmyportalplus_test_widget_1" />
		<widget code="jpmyportalplus_test_widget_3" />
	</widgets>
</myportalConfig>' );

INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('jpmyportalplus_testpage', 'homepage', 5, 'free');

INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES (
'jpmyportalplus_testpage', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Test Page</property>
<property key="it">Test Page</property>
</properties>', 'jpmyportalplus_pagemodel', 0, NULL, '2017-02-17 13:06:24');

INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES (
'jpmyportalplus_testpage', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Test Page</property>
<property key="it">Test Page</property>
</properties>', 'jpmyportalplus_pagemodel', 0, NULL, '2017-02-17 13:06:24');

INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('jpmyportalplus_testpage', 0, 'login_form', NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('jpmyportalplus_testpage', 1, 'jpmyportalplus_sample_widget', NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('jpmyportalplus_testpage', 2, 'jpmyportalplus_void', NULL);
INSERT INTO widgetconfig (pagecode, framepos, widgetcode, config) VALUES ('jpmyportalplus_testpage', 4, 'jpmyportalplus_test_widget_3', NULL);

INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpmyportalplus_testpage', 0, 'login_form', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpmyportalplus_testpage', 1, 'jpmyportalplus_sample_widget', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpmyportalplus_testpage', 2, 'jpmyportalplus_void', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpmyportalplus_testpage', 4, 'jpmyportalplus_test_widget_3', NULL);

INSERT INTO jpmyportalplus_userpageconfig(username, pagecode, framepos, widgetcode, config, closed)
    VALUES ('editorCustomers', 'jpmyportalplus_testpage',
    1, 'jpmyportalplus_void', null, 0);

INSERT INTO jpmyportalplus_userpageconfig(username, pagecode, framepos, widgetcode, config, closed)
    VALUES ('editorCustomers', 'jpmyportalplus_testpage',
    2, 'jpmyportalplus_test_widget_1', null, 1);

INSERT INTO jpmyportalplus_userpageconfig(username, pagecode, framepos, widgetcode, config, closed)
    VALUES ('editorCustomers', 'jpmyportalplus_testpage',
    3, 'jpmyportalplus_sample_widget', null, 0);

INSERT INTO jpmyportalplus_userpageconfig(username, pagecode, framepos, widgetcode, config, closed)
    VALUES ('editorCustomers', 'jpmyportalplus_testpage',
    6, 'jpmyportalplus_test_widget_3', null, 1);
