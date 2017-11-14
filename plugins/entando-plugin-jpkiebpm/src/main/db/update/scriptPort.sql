DELETE FROM widgetcatalog WHERE code ='bpm-datatable';

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
	<action name="jpkiebpmBpmDatatableWidgetViewerConfig"/>
</config>', 'jpkiebpm', NULL, NULL, 1, NULL);
