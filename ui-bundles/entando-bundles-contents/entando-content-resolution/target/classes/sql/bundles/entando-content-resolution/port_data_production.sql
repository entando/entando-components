INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('RSL_DOCUMENTS', 'en', 'Documents');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('RSL_DOCUMENTS', 'it', 'Documenti');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('RSL_Latest', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Resolutions - Latest Resolutions</property>
<property key="it">Delibere - Ultime Delibere</property>
</properties>', NULL, NULL, 'content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElements">4</property>
<property key="filters">(attributeFilter=true;order=DESC;key=Date)+(order=DESC;attributeFilter=true;key=Number)</property>
<property key="title_it">Delibere</property>
<property key="linkDescr_it">Archivio</property>
<property key="pageLink">resolutions</property>
<property key="title_en">Resolutions</property>
<property key="contentType">RSL</property>
<property key="modelId">10041</property>
<property key="linkDescr_en">Archive</property>
</properties>', 0, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('RSL_Archive', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Resolutions - Archive</property>
<property key="it">Delibere - Archivio</property>
</properties>', NULL, NULL, 'content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElemForItem">10</property>
<property key="title_it">Archivio Delibere</property>
<property key="userFilters">(attributeFilter=false;key=fulltext)+(attributeFilter=true;key=Number)+(attributeFilter=false;key=category;categoryCode=resolutions)+(attributeFilter=true;key=Date)</property>
<property key="filters">(attributeFilter=true;order=DESC;key=Number)+(order=DESC;attributeFilter=true;key=Date)</property>
<property key="title_en">Resolutions Archive</property>
<property key="contentType">RSL</property>
<property key="modelId">10041</property>
</properties>', 0, NULL);