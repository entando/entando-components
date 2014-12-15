DELETE FROM widgetconfig WHERE widgetcode = 'jpnewsletter_registration';
DELETE FROM widgetconfig WHERE widgetcode = 'jpnewsletter_subscription';
DELETE FROM widgetconfig WHERE widgetcode = 'jpnewsletter_unsubscription';

DELETE FROM widgetcatalog WHERE code = 'jpnewsletter_registration';
DELETE FROM widgetcatalog WHERE code = 'jpnewsletter_subscription';
DELETE FROM widgetcatalog WHERE code = 'jpnewsletter_unsubscription';

DELETE FROM localstrings WHERE keycode LIKE 'jpnewsletter_%';
