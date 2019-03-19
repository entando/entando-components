
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
VALUES ('dashboard-table',
  '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Dashboard Table</property>
<property key="it">Pubblica Dashboard Table</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="dashboardDashboardTableConfig"/>
</config>','dashboard', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
VALUES ('dashboard-line-chart',
  '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Dashboard Line Chart</property>
<property key="it">Pubblica Dashboard Line Chart</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="dashboardDashboardLineChartConfig"/>
</config>','dashboard', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
VALUES ('dashboard-bar-chart',
  '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Dashboard Bar Chart</property>
<property key="it">Pubblica Dashboard Bar Chart</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="dashboardDashboardBarChartConfig"/>
</config>','dashboard', NULL, NULL, 1, 'free');


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
VALUES ('dashboard-donut-chart',
  '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Dashboard Donut Chart</property>
<property key="it">Pubblica Dashboard Donut Chart</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="dashboardDashboardDonutChartConfig"/>
</config>','dashboard', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
VALUES ('dashboard-gauge-chart',
  '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Dashboard Gauge Chart</property>
<property key="it">Pubblica Dashboard Gauge Chart</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="dashboardDashboardGaugeChartConfig"/>
</config>','dashboard', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
VALUES ('dashboard-pie-chart',
  '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Dashboard Pie Chart</property>
<property key="it">Pubblica Dashboard Pie Chart</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="dashboardDashboardPieChartConfig"/>
</config>','dashboard', NULL, NULL, 1, 'free');


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
VALUES ('dashboard-map', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Dashboard Map</property>
<property key="it">Pubblica Dashboard Map</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="dashboardDashboardMapConfig"/>
</config>','dashboard', NULL, NULL, 1, 'free');
