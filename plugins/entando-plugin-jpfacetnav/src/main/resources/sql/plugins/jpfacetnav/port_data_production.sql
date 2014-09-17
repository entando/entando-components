INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpfacetnav_results', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Search Result</property>
<property key="it">Risultati Ricerca</property>
</properties>', '<config>
	<parameter name="contentTypesFilter">Content Type (optional)</parameter>
	<action name="facetNavResultConfig"/>
</config>', 'jpfacetnav', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpfacetnav_tree', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Facets Tree</property>
<property key="it">Albero delle faccette</property>
</properties>', '<config>
	<parameter name="facetRootNodes">Facet Category Root</parameter>
	<parameter name="contentTypesFilter">Content Type (optional)</parameter>
	<action name="facetNavTreeConfig"/>
</config>', 'jpfacetnav', NULL, NULL, 1);


INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_REMOVE_FILTER', 'it', 'Rimuovi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_REMOVE_FILTER', 'en', 'Remove');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_TITLE_TREE', 'it', 'Albero delle Faccette');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_TITLE_TREE', 'en', 'Facet Tree');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_TITLE_FACET_RESULTS', 'it', 'Faccette');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfacetnav_TITLE_FACET_RESULTS', 'en', 'Facets');