INSERT INTO pagemodels(code, descr, frames, plugincode) VALUES ('jpgeoref_home',
'Home Page for test georef Content', '<frames>
	<frame pos="0">
		<descr>Test frame</descr>
	</frame>
</frames>', 'jpgeoref');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpgeoref_GoogleListViewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish contents on Map</property>
<property key="it">Pubblicazione contenuti su Mappa</property>
</properties>', '<config>
	<parameter name="contentType">Content Type (mandatory)</parameter>
	<parameter name="modelId">Content Model for Cloud</parameter>
	<parameter name="userFilters">Front-End user filter options</parameter>
	<parameter name="categories">Content Category codes (comma separeted)</parameter>
    <parameter name="orClauseCategoryFilter" />
	<parameter name="filters" />
	<parameter name="title_{lang}">Widget Title in lang {lang}</parameter>
	<parameter name="pageLink">The code of the Page to link</parameter>
	<parameter name="linkDescr_{lang}">Link description in lang {lang}</parameter>
	<parameter name="maxElements">Max number of contents</parameter>
	<action name="jpgeorefListViewerConfig"/>
</config>', 'jpgeoref', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpgeoref_GoogleRoute', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Route</property>
<property key="it">Tragitto su Mappa</property>
</properties>', '<config>
	<parameter name="contentsId">Contents id (comma separated)</parameter>
	<parameter name="listModelId">List Model id</parameter>
	<parameter name="markerModelId">Model id for marker</parameter>
	<action name="configSimpleParameter"/>
</config>', 'jpgeoref', NULL, NULL, 1);