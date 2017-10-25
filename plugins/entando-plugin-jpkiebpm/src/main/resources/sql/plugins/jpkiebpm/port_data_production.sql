INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpkiebpm_config', 'KIE-BPM service configuration',
'<?xml version="1.0" encoding="UTF-8"?>
<kieBpmConfig>
   <active>false</active>
   <username>USERNAME</username>
   <password>PASSWORD</password>
   <hostname>HOSTNAME</hostname>
   <schema>http</schema>
   <port>8080</port>
   <webapp>kie</webapp>
</kieBpmConfig>');

INSERT INTO widgetcatalog VALUES ('bpm-datatype-form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Form by DataType</property>
<property key="it">BPM-Form by DataType</property>
</properties>', '<config>
	<parameter name="dataTypeCode">Data Type Code</parameter>
	<parameter name="dataUxId">Data Ux ID</parameter>
	<parameter name="widgetInfoId">Widget Info ID</parameter>
	<action name="jpkiebpmBpmFormWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-datatable', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Datatable</property>
<property key="it">BPM-Datatable</property>
</properties>', '<config>
	<parameter name="widgetInfoId">WidgetInfoID</parameter>
	<action name="jpkiebpmBpmDatatableWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
