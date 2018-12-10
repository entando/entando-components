
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('iot-config', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">IoT Server Config</property>
<property key="it">IoT Configurazione del Server</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="jpiotIotConfigConfig"/>
</config>','jpiot', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('iot-table-list-devices', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Status Table IoT Devices</property>
<property key="it">Status Table IoT Devices</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="jpiotIotListDevicesConfig"/>
</config>','jpiot', NULL, NULL, 1, 'free');


INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_ID', 'en', 'id');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_ID', 'it', 'id');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_NAME', 'en', 'name');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_NAME', 'it', 'name');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_HOSTNAME', 'en', 'hostname');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_HOSTNAME', 'it', 'hostname');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_SCHEMA', 'en', 'schema');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_SCHEMA', 'it', 'schema');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_PORT', 'en', 'port');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_PORT', 'it', 'port');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_WEBAPP', 'en', 'webapp');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_WEBAPP', 'it', 'webapp');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_USERNAME', 'en', 'username');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_USERNAME', 'it', 'username');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_PASSWORD', 'en', 'password');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_PASSWORD', 'it', 'password');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_TOKEN', 'en', 'token');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_TOKEN', 'it', 'token');



INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_ID', 'en', 'id');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_ID', 'it', 'id');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_WIDGETTITLE', 'en', 'widgetTitle');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_WIDGETTITLE', 'it', 'widgetTitle');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_DATASOURCE', 'en', 'datasource');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_DATASOURCE', 'it', 'datasource');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_CONTEXT', 'en', 'context');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_CONTEXT', 'it', 'context');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_DOWNLOAD', 'en', 'download');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_DOWNLOAD', 'it', 'download');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_FILTER', 'en', 'filter');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_FILTER', 'it', 'filter');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_ALLCOLUMNS', 'en', 'allColumns');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_ALLCOLUMNS', 'it', 'allColumns');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_COLUMNS', 'en', 'columns');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_COLUMNS', 'it', 'columns');


INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_ACTIONS', 'it', 'Actions');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_ACTIONS', 'en', 'Actions');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_NEW', 'it', 'IotListDevices New');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_NEW', 'en', 'IotListDevices New');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_EDIT', 'it', 'IotListDevices Edit');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_EDIT', 'en', 'IotListDevices Edit');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_TRASH', 'it', 'IotListDevices Trash');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_TRASH', 'en', 'IotListDevices Trash');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_TRASH_CONFIRM', 'it', 'IotListDevices Trash confirm');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_TRASH_CONFIRM', 'en', 'IotListDevices Trash confirm');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_SEARCH_IOTLISTDEVICES', 'it', 'IotListDevices search');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_SEARCH_IOTLISTDEVICES', 'en', 'IotListDevices search');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_NOT_FOUND', 'it', 'IotListDevices not found');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTLISTDEVICES_NOT_FOUND', 'en', 'IotListDevices not found');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_ACTIONS', 'it', 'Actions');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_ACTIONS', 'en', 'Actions');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_NEW', 'it', 'IotConfig New');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_NEW', 'en', 'IotConfig New');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_EDIT', 'it', 'IotConfig Edit');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_EDIT', 'en', 'IotConfig Edit');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_TRASH', 'it', 'IotConfig Trash');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_TRASH', 'en', 'IotConfig Trash');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_TRASH_CONFIRM', 'it', 'IotConfig Trash confirm');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_TRASH_CONFIRM', 'en', 'IotConfig Trash confirm');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_SEARCH_IOTCONFIG', 'it', 'IotConfig search');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_SEARCH_IOTCONFIG', 'en', 'IotConfig search');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_NOT_FOUND', 'it', 'IotConfig not found');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpiot_IOTCONFIG_NOT_FOUND', 'en', 'IotConfig not found');
