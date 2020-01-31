INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpkiebpm_config', 'PAM service configuration',
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
            <password>jvK4iusIxd7iqfDVu/CuvC5Q6gfR1cTS9wexnoVwk+aGKsLS6asGWA==</password>
            <hostname>localhost</hostname>
            <schema>http</schema>
            <port>8080</port>
            <timeout>1000</timeout>
            <webapp>kie-server</webapp>
         </value>
      </entry>
   </kieBpmConfigeMap>
</kiaBpmConfigFactory>');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-datatype-form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Form by DataType</property>
<property key="it">PAM-Form by DataType</property>
</properties>', '<config>
	<parameter name="dataTypeCode">Data Type Code</parameter>
	<parameter name="dataUxId">Data Ux ID</parameter>
	<parameter name="widgetInfoId">Widget Info ID</parameter>
	<action name="jpkiebpmBpmFormWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-datatype-task-form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Task Form by DataType</property>
<property key="it">PAM-Task Form by DataType</property>
</properties>', '<config>
	<parameter name="dataTypeCode">Data Type Code</parameter>
	<parameter name="dataUxId">Data Ux ID</parameter>
	<parameter name="widgetInfoId">Widget Info ID</parameter>
	<action name="jpkiebpmBpmTaskFormWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-datatable-task-list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Datatable Task List</property>
<property key="it">PAM-Datatable Task List</property>
</properties>', '<config>
	<parameter name="widgetInfoId">WidgetInfoID</parameter>
	<action name="jpkiebpmBpmTaskListDatatableWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-datatable-process-list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Process list</property>
<property key="it">PAM-Lista processi</property>
</properties>', '<config>
	<parameter name="widgetInfoId">WidgetInfoID</parameter>
	<action name="jpkiebpmBpmProcessDatatableWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-case-progress-status', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Case progress status</property>
<property key="it">PAM-Case stato del processo</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="showMilestones">showMilestones</parameter>
	<parameter name="showNumberOfTasks">showNumberOfTasks</parameter>
	<parameter name="progressBarType">progressBarType</parameter>
<action name="jpkiebpmBpmCaseProgressWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-case-instance-selector', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Case instance selector</property>
<property key="it">Selettore di istanza Case PAM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceSelectorWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-case-comments', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Case comments</property>
<property key="it">Commenti del caso PAM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceCommentsWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-case-details', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Case details</property>
<property key="it">Dettagli caso PAM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceDetailsWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-case-actions', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Case actions</property>
<property key="it">Azioni caso PAM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceActionsWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-case-roles', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Case roles</property>
<property key="it">Ruoli caso PAM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceRolesWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-case-chart', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Case chart</property>
<property key="it">PAM-Case chart</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceChartWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-process-diagram', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Process diagram</property>
<property key="it">Diagramma di processo PAM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmProcessDiagramWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-case-file', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Case file</property>
<property key="it">File caso PAM</property>
</properties>', '<config>
        <parameter name="channel">channel</parameter>
	<parameter name="frontEndCaseData">frontEndCaseData</parameter>
	<action name="jpkiebpmBpmCaseInstanceFileWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('bpm-task-details','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">PAM-Task details</property>
<property key="it">Dettagli task PAM</property>
</properties>',NULL,'jpkiebpm', NULL, NULL, 1, NULL);
