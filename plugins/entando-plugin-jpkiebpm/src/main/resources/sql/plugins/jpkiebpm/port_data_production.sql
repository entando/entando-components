INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpkiebpm_config', 'KIE-BPM service configuration',
'<?xml version="1.0" encoding="UTF-8"?>
<kiaBpmConfigFactory>
   <kieBpmConfigeMap>
      <entry>
         <key>default</key>
         <value>
            <active>true</active>
            <id>1</id>
            <name>default</name>
            <username>krisv</username>
            <password>krisv</password>
            <hostname>localhost</hostname>
            <schema>http</schema>
            <port>8080</port>
            <webapp>kie-server</webapp>
         </value>
      </entry>
   </kieBpmConfigeMap>
</kiaBpmConfigFactory>');

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
INSERT INTO widgetcatalog VALUES ('bpm-datatable-task-list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Datatable Task List</property>
<property key="it">BPM-Datatable Task List</property>
</properties>', '<config>
	<parameter name="widgetInfoId">WidgetInfoID</parameter>
	<action name="jpkiebpmBpmDatatableWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-datatable-process-list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Process list</property>
<property key="it">BPM-Lista processi</property>
</properties>', '<config>
	<parameter name="widgetInfoId">WidgetInfoID</parameter>
	<action name="jpkiebpmBpmProcessDatatableWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-case-progress-status', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Case progress status</property>
<property key="it">BPM-Case stato del processo</property>
</properties>', '<config>
	<parameter name="frontEndMilestonesData">frontEndMilestonesData</parameter>
	<action name="jpkiebpmBpmCaseProgressWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
