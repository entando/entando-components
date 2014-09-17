INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpcasclient_config', 'Configurazione del servizio di integrazione con il CAS server', 
'<?xml version="1.0" encoding="UTF-8"?>
<casclientConfig>
	<active>false</active>
	<casLoginURL>http://japs.intranet:8080/cas/login</casLoginURL>
	<casLogoutURL>http://japs.intranet:8080/cas/logout</casLogoutURL>
	<casValidateURL>http://japs.intranet:8080/cas/validate</casValidateURL>
	<serverBaseURL>http://japs.intranet:8080</serverBaseURL>
	<notAuthPage>notauth</notAuthPage>
	<realm>demo.entando.com</realm>
</casclientConfig>');