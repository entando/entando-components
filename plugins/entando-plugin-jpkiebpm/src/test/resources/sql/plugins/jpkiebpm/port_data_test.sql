INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpkiebpm_config', 'KIE-BPM service configuration',
'<?xml version="1.0" encoding="UTF-8"?>
<kiaBpmConfigFactory>
   <kieBpmConfigeMap>
      <entry>
         <key>1</key>
         <value>
            <active>true</active>
            <id>1</id>
            <name>default</name>
            <username>krisv</username>
            <password>EFMBY42jI+kPeV8xX0UqkZH6CZq8nwcdR2tJpyxSUuSYjzBn0IltoQ==</password>
            <hostname>localhost</hostname>
            <schema>http</schema>
            <port>8080</port>
            <timeout>1000</timeout>
            <webapp>kie-server</webapp>
         </value>
      </entry>
      <entry>
         <key>2</key>
         <value>
            <active>true</active>
            <id>2</id>
            <name>test2</name>
            <username>krisv</username>
            <password>EFMBY42jI+kPeV8xX0UqkZH6CZq8nwcdR2tJpyxSUuSYjzBn0IltoQ==</password>
            <hostname>localhost</hostname>
            <schema>http</schema>
            <port>8082</port>
            <timeout>1000</timeout>
            <webapp>kie-server</webapp>
         </value>
      </entry>
   </kieBpmConfigeMap>
</kiaBpmConfigFactory>');

INSERT INTO widgetcatalog(
            code, titles, parameters, plugincode, parenttypecode, defaultconfig,
            locked, maingroup)
    VALUES ('bpm-start-new-process-form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Start new process Form</property>
<property key="it">PAM-Form di avvio di un nuovo processo</property>
</properties>', '<config>
	<parameter name="dataTypeCode">Data Type Code</parameter>
	<parameter name="dataUxId">Data Ux ID</parameter>
	<parameter name="widgetInfoId">Widget Info ID</parameter>
	<action name="jpkiebpmBpmFormWidgetViewerConfig"/>
</config>', 'jpkiebpm', '', '',
            1, '');

INSERT INTO widgetcatalog(
            code, titles, parameters, plugincode, parenttypecode, defaultconfig,
            locked, maingroup) VALUES ('bpm-case-instance-selector', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Case instance selector</property>
<property key="it">Selettore di istanza Case PAM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceSelectorWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);