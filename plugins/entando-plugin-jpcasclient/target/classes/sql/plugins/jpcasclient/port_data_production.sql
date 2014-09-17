INSERT INTO widgetcatalog(code, titles, parameters, plugincode, 
		parenttypecode, defaultconfig, locked)
    VALUES ('jpcasclient_login', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Login - with CAS</property>
<property key="it">Login - con CAS</property>
</properties>', NULL, 'jpcasclient', NULL, NULL, 1);

INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpcasclient_config', 'Configurazione del servizio di integrazione con il CAS server', 
'<?xml version="1.0" encoding="UTF-8"?>
<casclientConfig>
	<active>false</active>
	<casLoginURL>http://localhost:8080/cas/login</casLoginURL>
	<casLogoutURL>http://localhost:8080/cas/logout</casLogoutURL>
	<casValidateURL>http://localhost:8080/cas/validate</casValidateURL>
	<serverBaseURL>http://localhost:8080</serverBaseURL>
	<notAuthPage>notauth</notAuthPage>
	<realm>demo.entando.com</realm>
</casclientConfig>');