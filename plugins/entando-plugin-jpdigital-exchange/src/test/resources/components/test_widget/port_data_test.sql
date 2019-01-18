INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('test_widget', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Test Widget</property>
<property key="it">Test Widget</property>
</properties>', NULL, NULL, NULL, NULL, 0, NULL);

INSERT INTO guifragment (code, widgettypecode, locked, defaultgui) VALUES ('test_widget', 'test_widget', 1, '<h1>TEST</h1>');
