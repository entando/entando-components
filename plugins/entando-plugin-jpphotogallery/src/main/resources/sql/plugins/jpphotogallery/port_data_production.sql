INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpphotogallery', '<?xml version="1.0" encoding="UTF-8"?>
	<properties>
		<property key="en">Photo Gallery</property>
		<property key="it">Galleria Fotografica</property>
	</properties>', '<config>
	<parameter name="contentType">Content Type (mandatory)</parameter>
	<parameter name="userFilters">Front-End user filter options</parameter>
    <parameter name="modelIdMaster">Main Model</parameter>
	<parameter name="modelIdPreview">Preview Model</parameter>
	<parameter name="category">Content Category **deprecated**</parameter>
	<parameter name="categories">Content Category codes (comma separeted)</parameter>
    <parameter name="orClauseCategoryFilter" />
	<parameter name="filters" />
	<parameter name="title_{lang}">Widget Title in lang {lang}</parameter>
	<parameter name="pageLink">The code of the Page to link</parameter>
	<parameter name="linkDescr_{lang}">Link description in lang {lang}</parameter>
	<parameter name="maxElements">Max Number of contents</parameter>
	<action name="jpphotogalleryConfig"/>
</config>', 'jpphotogallery', NULL, NULL, 1);