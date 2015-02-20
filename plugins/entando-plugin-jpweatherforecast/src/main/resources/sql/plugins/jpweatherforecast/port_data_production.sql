INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpweatherforecast', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Weather farecast</property>
<property key="it">Previsioni meteo</property>
</properties>', '<config>
	<parameter name="city">City</parameter>
	<parameter name="country">Country Code</parameter>
	<parameter name="latitude">Decimal Latitude</parameter>
	<parameter name="longitude">Decimal Longitude</parameter>
	<action name="forecastLocationConfig"/>
</config>', 'jpweatherforecast', NULL, NULL, 1, NULL);