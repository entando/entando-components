DELETE FROM widgetconfig WHERE widgetcode = 'jpcalendar_calendar';
DELETE FROM widgetconfig WHERE widgetcode = 'jpcalendar_dailyEvents';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpcalendar_calendar');
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpcalendar_dailyEvents');

DELETE FROM widgetcatalog WHERE parenttypecode = 'jpcalendar_calendar';
DELETE FROM widgetcatalog WHERE parenttypecode = 'jpcalendar_dailyEvents';
DELETE FROM widgetcatalog WHERE code = 'jpcalendar_calendar';
DELETE FROM widgetcatalog WHERE code = 'jpcalendar_dailyEvents';

DELETE FROM localstrings WHERE keycode LIKE 'jpcalendar_%';
