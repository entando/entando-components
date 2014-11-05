INSERT INTO widgetcatalog (code, titles, parameters, plugincode, locked) VALUES ( 'jpwidgetutils_replicator', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Widget Replicator</property>
<property key="it">Replicatore di Widget</property>
</properties>', '<config>
	<parameter name="pageCodeParam">Page</parameter>
	<parameter name="frameIdParam">Frame</parameter>
	<action name="jpwidgetutilsReplicatorConfig"/>
</config>', 'jpwidgetutils', 1 );
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, locked) VALUES ('jpwidgetutils_iframe', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">IFrame</property>
<property key="it">IFrame</property>
</properties>
', '<config>
	<parameter name="url">The URL</parameter>
	<parameter name="usernameParam">Username parameter name</parameter>
	<parameter name="username">Username</parameter>
	<parameter name="passwordParam">User Password parameter name</parameter>
	<parameter name="password">User Password</parameter>
	<parameter name="height">Pixel or percent height</parameter>
	<action name="configSimpleParameter" />
</config>', 'jpwidgetutils', 1);

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwidgetutils_ERROR_NO_IFRAME', 'en', 'Error - no frame');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwidgetutils_ERROR_NO_IFRAME', 'it', 'Errore - no frame');