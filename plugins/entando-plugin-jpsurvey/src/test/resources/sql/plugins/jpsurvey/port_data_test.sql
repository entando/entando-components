INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_pollArchive', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Surveys - Archive</property>
<property key="it">Sondaggi - Archivio</property>
</properties>', NULL, 'jpsurvey', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_pollList', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Polls - Active List</property>
<property key="it">Sondaggi - Lista Attivi</property>
</properties>', NULL, 'jpsurvey', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_questionnaireArchive', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Questionnaires - Archive</property>
<property key="it">Questionari - Archivio</property>
</properties>', NULL, 'jpsurvey', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_questionnaireList', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Questionnaires - Active List</property>
<property key="it">Questionari - Lista Attivi</property>
</properties>', NULL, 'jpsurvey', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_detailsSurvey', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Detailed questionnaire-survey</property>
<property key="it">Dettaglio questionario-sondaggio</property>
</properties>', NULL, 'jpsurvey', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpsurvey/Front/Survey/entryPoint.action</property>
</properties>', 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpsurvey_resultsSurvey', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Results questionnaire-survey</property>
<property key="it">Risultati questionari-sondaggi</property>
</properties>', NULL, 'jpsurvey', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpsurvey/Front/SurveyDetail/entryPoint.action</property>
</properties>', 1);
