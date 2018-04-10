INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpkiebpm_config', 'KIE-BPM service configuration',
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
	<action name="jpkiebpmBpmTaskListDatatableWidgetViewerConfig"/>
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
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndMilestonesData">frontEndMilestonesData</parameter>
	<action name="jpkiebpmBpmCaseProgressWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-case-instance-selector', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Case instance selector</property>
<property key="it">Selettore di istanza Case BPM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceSelectorWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-case-comments', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Case comments</property>
<property key="it">Commenti del caso BPM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceCommentsWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-case-details', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Case details</property>
<property key="it">Dettagli caso BPM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceDetailsWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-case-actions', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Case actions</property>
<property key="it">Azioni caso BPM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceActionsWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-case-roles', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Case roles</property>
<property key="it">Ruoli caso BPM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceRolesWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-case-chart', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Case chart</property>
<property key="it">BPM-Case chart</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceChartWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-process-diagram', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Process diagram</property>
<property key="it">Diagramma di processo BPM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmProcessDiagramWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog VALUES ('bpm-case-file', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">BPM-Case file</property>
<property key="it">File caso BPM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceFileWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);