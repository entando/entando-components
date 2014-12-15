DELETE FROM widgetconfig WHERE widgetcode = 'jpsurvey_pollArchive';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_pollArchive');
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_pollArchive';
DELETE FROM widgetcatalog WHERE code = 'jpsurvey_pollArchive';

DELETE FROM widgetconfig WHERE widgetcode = 'jpsurvey_pollList';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_pollList');
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_pollList';
DELETE FROM widgetcatalog WHERE code = 'jpsurvey_pollList';

DELETE FROM widgetconfig WHERE widgetcode = 'jpsurvey_questionnaireArchive';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_questionnaireArchive');
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_questionnaireArchive';
DELETE FROM widgetcatalog WHERE code = 'jpsurvey_questionnaireArchive';

DELETE FROM widgetconfig WHERE widgetcode = 'jpsurvey_questionnaireList';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_questionnaireList');
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_questionnaireList';
DELETE FROM widgetcatalog WHERE code = 'jpsurvey_questionnaireList';

DELETE FROM widgetconfig WHERE widgetcode = 'jpsurvey_detailsSurvey';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_detailsSurvey');
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_detailsSurvey';
DELETE FROM widgetcatalog WHERE code = 'jpsurvey_detailsSurvey';

DELETE FROM widgetconfig WHERE widgetcode = 'jpsurvey_resultsSurvey';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_resultsSurvey');
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpsurvey_resultsSurvey';
DELETE FROM widgetcatalog WHERE code = 'jpsurvey_resultsSurvey';

DELETE FROM localstrings WHERE keycode LIKE 'JPSURVEY_%';
DELETE FROM localstrings WHERE keycode LIKE 'jpsurvey_%';