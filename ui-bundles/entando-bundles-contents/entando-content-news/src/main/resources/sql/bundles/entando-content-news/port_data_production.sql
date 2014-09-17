INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_ATTACHMENTS', 'en', 'Attachments');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_ATTACHMENTS', 'it', 'Allegati');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_LINKS', 'en', 'Links');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_LINKS', 'it', 'Link');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_READ_MORE', 'en', 'View details');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_READ_MORE', 'it', 'Continua');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('NWS_Latest', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">News - Latest News</property>
<property key="it">Notizie - Ultime Notizie</property>
</properties>', NULL, NULL, 'content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElements">4</property>
<property key="filters">(order=DESC;attributeFilter=true;key=Date)</property>
<property key="title_it">Notizie</property>
<property key="linkDescr_it">Archivio</property>
<property key="pageLink">news</property>
<property key="title_en">News</property>
<property key="contentType">NWS</property>
<property key="modelId">10021</property>
<property key="linkDescr_en">Archive</property>
</properties>', 0, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('NWS_Archive', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">News - Archive</property>
<property key="it">Notizie - Archivio</property>
</properties>', NULL, NULL, 'content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElemForItem">10</property>
<property key="title_it">Archivio Notizie</property>
<property key="userFilters">(attributeFilter=false;key=fulltext)+(attributeFilter=true;key=Date)</property>
<property key="filters">(order=DESC;attributeFilter=true;key=Date)</property>
<property key="title_en">News Archive</property>
<property key="contentType">NWS</property>
<property key="modelId">10021</property>
</properties>', 0, NULL);


