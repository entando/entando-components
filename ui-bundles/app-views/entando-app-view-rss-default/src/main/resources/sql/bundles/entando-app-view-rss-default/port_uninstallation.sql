DELETE FROM widgetconfig WHERE widgetcode = 'jprss_rssChannels';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jprss_rssChannels');

DELETE FROM widgetcatalog WHERE parenttypecode = 'jprss_rssChannels';
DELETE FROM widgetcatalog WHERE code = 'jprss_rssChannels';

DELETE FROM localstrings WHERE keycode = 'jprss_FEED_LIST';