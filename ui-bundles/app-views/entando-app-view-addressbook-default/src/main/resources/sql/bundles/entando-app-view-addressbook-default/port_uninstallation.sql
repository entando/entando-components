DELETE FROM widgetconfig WHERE widgetcode = 'jpaddressbook_search_form';
DELETE FROM widgetconfig WHERE widgetcode = 'jpaddressbook_search_result';
DELETE FROM widgetconfig WHERE widgetcode = 'jpaddressbook_search_form_advanced';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpaddressbook_search_form');
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpaddressbook_search_result');
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpaddressbook_search_form_advanced');

DELETE FROM widgetcatalog WHERE parenttypecode = 'jpaddressbook_search_form';
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpaddressbook_search_result';
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpaddressbook_search_form_advanced';
DELETE FROM widgetcatalog WHERE code = 'jpaddressbook_search_form';
DELETE FROM widgetcatalog WHERE code = 'jpaddressbook_search_result';
DELETE FROM widgetcatalog WHERE code = 'jpaddressbook_search_form_advanced';

DELETE FROM localstrings WHERE keycode LIKE 'jpaddressbook_%';
