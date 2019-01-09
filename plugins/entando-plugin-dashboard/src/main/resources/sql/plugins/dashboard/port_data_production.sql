
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
VALUES ('dashboard-table', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Dashboard Table</property>
<property key="it">Pubblica Dashboard Table</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="dashboardDashboardTableConfig"/>
</config>','dashboard', NULL, NULL, 1, 'free');
